package craftedMods.neiLotr.handlers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import codechicken.nei.PositionedStack;
import craftedMods.neiLotr.NeiLotr;
import craftedMods.neiLotr.handlers.template.ExtendedShapedRecipeHandler;
import craftedMods.neiLotr.handlers.template.ExtendedShapelessRecipeHandler;
import craftedMods.neiLotr.util.NeiLotrUtil;
import lotr.client.gui.LOTRGuiBarrel;
import lotr.common.item.LOTRItemMug;
import lotr.common.item.LOTRItemMug.Vessel;
import lotr.common.recipe.LOTRBrewingRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BarrelRecipeHandler extends ExtendedShapelessRecipeHandler {

	private Set<ShapelessOreRecipe> barrelRecipesRaw = new HashSet<>();
	private Set<ExtendedCachedShapelessRecipe> barrelRecipes = new HashSet<>();

	public BarrelRecipeHandler() {
		super();
	}
	
	public BarrelRecipeHandler(long id) {
		super(id);
		// Load raw recipes
		if (barrelRecipesRaw.isEmpty()) {
			try {
				Field field = LOTRBrewingRecipes.class.getDeclaredField("recipes");
				field.setAccessible(true);
				barrelRecipesRaw.addAll((ArrayList<ShapelessOreRecipe>) field.get(LOTRBrewingRecipes.class));
			} catch (Exception e) {
				NeiLotr.mod.getLogger().error("Could not access field \"recipes\" in LOTRBrewingRecipes: ", e);
			}
		}
		// Convert raw recipes
		if (barrelRecipes.isEmpty()) {
			barrelRecipesRaw.forEach(rec -> {
				// Copy stacks/ores.
				Object[] stacks = Arrays.copyOf(rec.getInput().toArray(), rec.getInput().size() + 3);
				
				// Add water buckets. They are not contained in the raw recipe.
				stacks[rec.getInput().size()] = new ItemStack(Items.water_bucket);
				stacks[rec.getInput().size() + 1] = new ItemStack(Items.water_bucket);
				stacks[rec.getInput().size() + 2] = new ItemStack(Items.water_bucket);
				
				// Add recipe for all vessel types and drink strengths.
				ItemStack tmpStack = rec.getRecipeOutput().copy();
				for (int i = 0; i < 5; i++) {
					tmpStack.setItemDamage(i);
					for(Vessel v : LOTRItemMug.Vessel.values()) {
						ItemStack drink = tmpStack.copy();
						LOTRItemMug.setVessel(drink, v, false);
						barrelRecipes.add(new CachedBarrelRecipe(Arrays.asList(stacks), drink));
					}
				}
				//TODO: Add recipe for barrel item.
			});
		}
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return LOTRGuiBarrel.class;
	}
	
	public List<CachedRecipe> getRecipes(){
		return Lists.newArrayList(barrelRecipes);
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("lotrNei.recipe.barrel.name")
//				+ " (" + StatCollector.translateToLocal("lotrNei.recipe.shapeless") + ")"
				;
	}

//	@Override
//	public void loadCraftingRecipes(ItemStack result) {
//		NeiLotrUtil.loadCraftingRecipes(result, barrelRecipes, arecipes);
//	}
//
//	@Override
//	public void loadUsageRecipes(ItemStack ingredient) {
//		NeiLotrUtil.loadUsageRecipes(ingredient, barrelRecipes, arecipes);
//	}
	
	@Override
	protected void transferImportantData(ExtendedShapedRecipeHandler newInstance) {
		super.transferImportantData(newInstance);
		BarrelRecipeHandler handler = (BarrelRecipeHandler) newInstance;
		handler.barrelRecipes = this.barrelRecipes;
		handler.barrelRecipesRaw = this.barrelRecipesRaw;
	}

	@Override
	public CachedRecipe newRecipeInstance(CachedRecipe oldInstance) {
		CachedRecipe ret = null;
		if (oldInstance instanceof CachedBarrelRecipe) {
			CachedBarrelRecipe recipe = (CachedBarrelRecipe) oldInstance;
			ret = new CachedBarrelRecipe(recipe);
		}
		return ret;
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}
	
	// Change stack positions for accurate drawing
	
	public static final int[][] STACKORDER = new int[][] { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 0, 1 }, { 1, 1 }, { 2, 1 }, { 0, 2 }, { 1, 2 }, { 2, 2 } };

	public class CachedBarrelRecipe extends ExtendedShapelessRecipeHandler.ExtendedCachedShapelessRecipe {
		
		public CachedBarrelRecipe(List<?> input, ItemStack output) {
			super(input, Lists.newArrayList(output));
		}
		
		public CachedBarrelRecipe(CachedBarrelRecipe recipe) {
			super(recipe);
		}
		
		public void setResult(List<?> result) {
			this.setResult(new PositionedStack(result, 119, 24));
		}
		
		public void setIngredients(List<?> items) {
			ingredients.clear();
			for (int i = 0; i < items.size(); i++) {
				PositionedStack stack = new PositionedStack(NeiLotrUtil.extractRecipeItems(items.get(i)),
						25 + BarrelRecipeHandler.STACKORDER[i][0] * 18, 6 + BarrelRecipeHandler.STACKORDER[i][1] * 18);
				stack.setMaxSize(1);
				ingredients.add(stack);
			}
		}
	}
}
