package craftedMods.neiLotr.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import codechicken.nei.ItemList;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler.CachedShapedRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import craftedMods.neiLotr.handlers.template.AdvancedRecipeLoading;
import lotr.common.enchant.LOTREnchantment;
import lotr.common.enchant.LOTREnchantmentHelper;
import lotr.common.item.LOTRItemDye;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;

public class NeiLotrUtil {
	public static final int RECIPIES_PER_PAGE = 2;
	public static final String GUI_TEXTURE_PATH_DEFAULT_CRAFTING = "textures/gui/container/crafting_table.png";

	private static final List<ItemStack> dyes = new ArrayList<>();

	public static List<ItemStack> getDyes() {
		if (dyes.isEmpty()) {
			NeiLotrUtil.itemStackIteration((count, stack) -> {
				if (LOTRItemDye.isItemDye(stack) != -1)
					dyes.add(stack);
			});
		}
		return Collections.unmodifiableList(dyes);
	}

	public static Collection<CachedRecipe> getCachedRecipes(AdvancedRecipeLoading loader, Collection<IRecipe> recipes) {
		List<CachedRecipe> ret = new ArrayList<>();
		recipes.forEach(recipe -> {
			CachedRecipe cachedRecipe = loader.getCachedRecipe(recipe);
			if (cachedRecipe != null) {
				ret.add(cachedRecipe);
			}
		});
		return ret;
	}

	public static void loadCraftingRecipes(ItemStack result, Set<? extends CachedRecipe> recipes,
			List<CachedRecipe> arecipes) {
		loadCraftingRecipes(result, recipes, arecipes, NEIClientUtils::areStacksSameTypeCrafting);
	}

	public static void loadCraftingRecipes(ItemStack result, Set<? extends CachedRecipe> recipes,
			List<CachedRecipe> arecipes, BiFunction<ItemStack, ItemStack, Boolean> check) {
		recipes.forEach(recipe -> {
			if (check.apply(recipe.getResult().item, result)) {
				if (recipe instanceof CachedShapedRecipe)
					((CachedShapedRecipe) recipe).computeVisuals();
				arecipes.add(recipe);
			}
		});
	}

	public static void loadCraftingRecipes(ItemStack result, Set<CachedRecipe> cachedRecipes,
			Map<Item, Set<CachedRecipe>> craftingMap) {
		loadCraftingRecipes(result, cachedRecipes, craftingMap, NEIClientUtils::areStacksSameTypeCrafting);
	}

	public static void loadCraftingRecipes(ItemStack result, Set<CachedRecipe> cachedRecipes,
			Map<Item, Set<CachedRecipe>> craftingMap, BiFunction<ItemStack, ItemStack, Boolean> check) {
		cachedRecipes.forEach(recipe -> {
			if (check.apply(recipe.getResult().item, result)) {
				if (recipe instanceof CachedShapedRecipe)
					((CachedShapedRecipe) recipe).computeVisuals();
				if (craftingMap.get(result) == null)
					craftingMap.put(result.getItem(), new HashSet<>());
				craftingMap.get(result.getItem()).add(recipe);
			}
		});
	}

	public static void loadUsageRecipes(ItemStack ingredient, Set<? extends CachedRecipe> recipes,
			List<CachedRecipe> arecipes) {
		recipes.forEach(recipe -> {
			List<PositionedStack> ingreds = recipe.getIngredients();

			if (recipe instanceof CachedShapedRecipe) {
				((CachedShapedRecipe) recipe).computeVisuals();
			}

			if (recipe.contains(ingreds, ingredient)) {
				recipe.setIngredientPermutation(ingreds, ingredient);
				arecipes.add(recipe);
			}
		});
	}

	public static void loadUsageRecipes(ItemStack ingredient, Set<CachedRecipe> cachedRecipes,
			Map<Item, Set<CachedRecipe>> usageMap) {
		cachedRecipes.forEach(recipe -> {
			List<PositionedStack> ingreds = recipe.getIngredients();

			if (recipe instanceof CachedShapedRecipe)
				((CachedShapedRecipe) recipe).computeVisuals();

			if (recipe.contains(ingreds, ingredient)) {
				recipe.setIngredientPermutation(ingreds, ingredient);
				if (usageMap.get(ingredient) == null)
					usageMap.put(ingredient.getItem(), new HashSet<>());
				usageMap.get(ingredient.getItem()).add(recipe);
			}
		});
	}

	public static List<ItemStack> fillToRecipeSize(List<ItemStack> list) {
		int c = 9 - list.size();
		while (c > 0) {
			list.add(null);
			c--;
		}
		return list;
	}

	public static ArrayList<PositionedStack> copyPositionedStackList(List<PositionedStack> collection) {
		ArrayList<PositionedStack> newList = new ArrayList<>();
		collection.forEach(stack -> newList.add(stack.copy()));
		return newList;
	}

	public static String getCTRecipeName(String unlocalizedName) {
		return StatCollector.translateToLocal("container.lotr.crafting." + unlocalizedName);
	}

	public static <T> List<List<T>> asTwoDimensionalList(T[][] array) {
		List<List<T>> list = new ArrayList<List<T>>();
		for (T[] ar : array) {
			list.add(Arrays.asList(ar));
		}
		return list;
	}

	public static void itemStackIteration(BiConsumer<Integer, ItemStack> handler) {
		ItemList.items.forEach(stack -> handler.accept(ItemList.items.size(), stack.copy()));
	}

	public static List<ItemStack> extractRecipeItems(Collection<?> collection) {
		List<ItemStack> stacks = new ArrayList<ItemStack>();

		for (Object obj : collection) {
			if (obj instanceof PositionedStack[]) {
				for (PositionedStack stack : (PositionedStack[]) obj) {
					if (stack.item != null)
						stacks.add(stack.item);
				}
			} else if (obj instanceof Collection<?>) {
				for (Object o : (Collection<?>) obj) {
					if (o instanceof PositionedStack) {
						PositionedStack tmp = (PositionedStack) o;
						if (tmp.item != null) {
							stacks.add(tmp.item);
						}
					}
				}

			} else if (obj instanceof PositionedStack) {
				PositionedStack tmp = (PositionedStack) obj;
				if (tmp.item != null) {
					stacks.add(tmp.item);
				}
			} else if (obj instanceof Item) {
				stacks.add(new ItemStack((Item) obj));
			} else if (obj instanceof Block) {
				stacks.add(new ItemStack((Block) obj));
			} else {
				stacks.addAll(Arrays.asList(NEIClientUtils.extractRecipeItems(obj)));
			}
		}

		return stacks;
	}

	public static <T> List<ItemStack> extractRecipeItems(T... array) {
		if (array.length > 0) {
			if (array[0] instanceof Collection<?>) {
				return extractRecipeItems((Collection<?>) array[0]);
			}
		}
		return extractRecipeItems(Arrays.asList(array));
	}
}
