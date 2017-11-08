package craftedMods.neiLotr.handlers.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler.CachedShapedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import craftedMods.neiLotr.NeiLotr;
import craftedMods.neiLotr.handlers.template.ExtendedShapedRecipeHandler.ExtendedCachedShapedRecipe;
import craftedMods.neiLotr.handlers.template.ExtendedShapelessRecipeHandler.ExtendedCachedShapelessRecipe;
import craftedMods.neiLotr.util.ItemStackMap;
import craftedMods.neiLotr.util.NeiLotrUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class StaticRecipeLoader {

	private Map<Long, Set<CachedRecipe>> unassignedRecipes = new HashMap<>();

	private Map<Long, ItemStackMap<Set<CachedRecipe>>> craftingRecipes = new HashMap<>();
	private Map<Long, ItemStackMap<Set<CachedRecipe>>> usageRecipes = new HashMap<>();

	private Map<Long, AdvancedRecipeLoading> recipeLoaders = new HashMap<>();
	private boolean loaded = false;

	private Set<BiConsumer<Integer, ItemStack>> registeredItemIterators = new HashSet<>();

	// Register handler and convert IRecipe to CachedRecipe
	public void registerStaticRecipeLoaderWithIRecipe(AdvancedRecipeLoading loader, Collection<IRecipe> recipes) {
		registerStaticRecipeLoader(loader, NeiLotrUtil.getCachedRecipes(loader, recipes));
	}

	// Register handler with cached recipes
	public void registerStaticRecipeLoader(AdvancedRecipeLoading loader, Collection<CachedRecipe> recipes) {
		checkState(loaded);
		long id = loader.getHandlerId();
		if (!recipeLoaders.containsKey(id) && recipes != null) {
			recipeLoaders.put(id, loader);
			unassignedRecipes.put(id, new HashSet<>());
			craftingRecipes.put(id, new ItemStackMap<Set<CachedRecipe>>());
			usageRecipes.put(id, new ItemStackMap<Set<CachedRecipe>>());
			unassignedRecipes.get(id).addAll(recipes);
		}
	}

	private ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public boolean registerItemIterator(BiConsumer<Integer, ItemStack> cons) {
		return registeredItemIterators.add(cons);
	}

	public void itemIteration() {
		NeiLotrUtil.itemStackIteration((count, stack) -> {
			registeredItemIterators.forEach(handler -> {
				threadPool.execute(() -> {
					handler.accept(count, stack.copy());
				});
			});
		});
		try {
			threadPool.shutdown();
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Assign CachedRecipe to Ingredients and Results
	public void loadStaticRecipes() {
		if (!loaded) {
			unassignedRecipes.forEach((id, recipes) -> {
				recipes.forEach(recipe -> {
					// Result items
					ItemStack resultItem = recipe.getResult().item;
					if (craftingRecipes.get(id).get(resultItem) == null)
						craftingRecipes.get(id).put(resultItem, new HashSet<>());
					craftingRecipes.get(id).get(resultItem).add(recipe);

					// Ingredient items
					recipe.getIngredients().forEach(ingredient -> {
						ingredient.generatePermutations();
						List<ItemStack> ingredientItems = new ArrayList<>();
						ingredientItems.add(ingredient.item);
						ingredientItems.addAll(Arrays.asList(ingredient.items));
						for (ItemStack ingredientItem : ingredientItems) {
							if (usageRecipes.get(id).get(ingredientItem) == null)
								usageRecipes.get(id).put(ingredientItem, new HashSet<>());
							usageRecipes.get(id).get(ingredientItem).add(recipe);
						}
					});
				});
			});
			unassignedRecipes.clear();
			loaded = true;
		}
	}

	// Returns CachedRecipes for handler and result
	public Set<CachedRecipe> getCraftingRecipes(long handler, ExtendedShapedRecipeHandler instance, ItemStack result) {
		checkState(!loaded);
		Set<CachedRecipe> ret = new HashSet<>();
		if (craftingRecipes.get(handler) != null) {
			Set<CachedRecipe> recipes = craftingRecipes.get(handler).get(result);
			if (recipes != null) {
				if (instance instanceof ExtendedShapelessRecipeHandler) {
					ExtendedShapelessRecipeHandler extendedHandler = (ExtendedShapelessRecipeHandler) instance;
					recipes.forEach(recipe -> {
						if (recipe instanceof ExtendedCachedShapelessRecipe) {
							ExtendedCachedShapelessRecipe newRecipe = (ExtendedCachedShapelessRecipe) extendedHandler
									.newRecipeInstance(recipe);
							ret.add(newRecipe);
						} else {
							NeiLotr.mod.getLogger().error("Recipe " + recipe.getClass().getSimpleName()
									+ " is not an instance of ExtendedCachedShapelessRecipe!");
						}
					});
				} else {
					recipes.forEach(recipe -> {
						if (recipe instanceof ExtendedCachedShapedRecipe) {
							CachedShapedRecipe newRecipe = (CachedShapedRecipe) instance.newRecipeInstance(recipe);
							newRecipe.computeVisuals();
							ret.add(newRecipe);
						} else {
							NeiLotr.mod.getLogger().error("Recipe " + recipe.getClass().getSimpleName()
									+ " is not an instance of ExtendedCachedShapedRecipe!");
						}
					});
				}
			}
		}
		return ret;
	}

	// Returns CachedRecipes for handler and ingredient
	public Set<CachedRecipe> getUsageRecipes(long handler, ExtendedShapedRecipeHandler instance, ItemStack ingredient) {
		checkState(!loaded);
		Set<CachedRecipe> ret = new HashSet<>();
		if (usageRecipes.get(handler) != null) {
			Set<CachedRecipe> recipes = usageRecipes.get(handler).get(ingredient);
			if (recipes != null) {
				if (instance instanceof ExtendedShapelessRecipeHandler) {
					ExtendedShapelessRecipeHandler extendedHandler = (ExtendedShapelessRecipeHandler) instance;
					recipes.forEach(recipe -> {
						if (recipe instanceof ExtendedCachedShapelessRecipe) {
							ExtendedCachedShapelessRecipe newRecipe = (ExtendedCachedShapelessRecipe) extendedHandler
									.newRecipeInstance(recipe);

							List<PositionedStack> ingreds = newRecipe.getIngredients();
							if (recipe.contains(ingreds, ingredient)) {
								recipe.setIngredientPermutation(ingreds, ingredient);
							}
							ret.add(newRecipe);
						} else {
							NeiLotr.mod.getLogger().error("Recipe " + recipe.getClass().getSimpleName()
									+ " is not an instance of ExtendedCachedShapelessRecipe!");
						}
					});
				} else {
					recipes.forEach(recipe -> {
						if (recipe instanceof ExtendedCachedShapedRecipe) {
							CachedShapedRecipe newRecipe = (CachedShapedRecipe) instance.newRecipeInstance(recipe);
							newRecipe.computeVisuals();
							List<PositionedStack> ingreds = newRecipe.getIngredients();
							if (recipe.contains(ingreds, ingredient)) {
								recipe.setIngredientPermutation(ingreds, ingredient);
							}
							ret.add(newRecipe);
						} else {
							NeiLotr.mod.getLogger().error("Recipe " + recipe.getClass().getSimpleName()
									+ " is not an instance of ExtendedCachedShapedRecipe!");
						}
					});
				}
			}
		}
		return ret;
	}

	private void checkState(boolean fail) {
		if (fail)
			throw new IllegalStateException(
					"StaticRecipeLoader is in wrong state for this operation: Loaded: " + loaded);
	}

}
