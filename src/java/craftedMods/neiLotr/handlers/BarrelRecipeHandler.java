package craftedMods.neiLotr.handlers;

import java.lang.reflect.Field;
import java.util.*;

import codechicken.nei.*;
import codechicken.nei.recipe.TemplateRecipeHandler;
import craftedMods.neiLotr.NeiLotr;
import craftedMods.neiLotr.handlers.template.ExtendedShapelessRecipeHandler;
import craftedMods.neiLotr.util.NeiLotrUtil;
import lotr.client.gui.LOTRGuiBarrel;
import lotr.common.entity.LOTREntityUtils;
import lotr.common.recipe.LOTRBrewingRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.*;

public class BarrelRecipeHandler extends ExtendedShapelessRecipeHandler {

	private Set<ShapelessOreRecipe> barrelRecipesRaw = new HashSet<>();
	private Set<ExtendedCachedShapelessRecipe> barrelRecipes = new HashSet<>();

	public BarrelRecipeHandler() {
		if (barrelRecipesRaw.isEmpty()) {
			try {
				Field field = LOTRBrewingRecipes.class.getDeclaredField("recipes");
				field.setAccessible(true);
				barrelRecipesRaw.addAll((ArrayList<ShapelessOreRecipe>) field.get(LOTRBrewingRecipes.class));
			} catch (Exception e) {
				NeiLotr.mod.getLogger().error("Could not access field \"recipes\" in LOTRBrewingRecipes: ", e);
			}
		}
		if (barrelRecipes.isEmpty()) {
			barrelRecipesRaw.forEach(rec -> {
				Object[] stacks = new Object[rec.getInput().size() + 3];
				for (int i = 0; i < rec.getInput().size(); i++) {
					Object tmp = rec.getInput().get(i);
					if (tmp instanceof ItemStack) {
						stacks[i] = ((ItemStack) tmp).copy();
					} else if (tmp instanceof ArrayList<?>) {
						stacks[i] = OreDictionary.getOreName(OreDictionary.getOreID((ItemStack) ((ArrayList<?>) tmp).get(0)));
					}
				}
				stacks[rec.getInput().size()] = new ItemStack(Items.water_bucket);
				stacks[rec.getInput().size() + 1] = new ItemStack(Items.water_bucket);
				stacks[rec.getInput().size() + 2] = new ItemStack(Items.water_bucket);

				for (int i = 0; i < 5; i++) {
					ShapelessOreRecipe tmp = new ShapelessOreRecipe(rec.getRecipeOutput().copy(), stacks);
					tmp.getRecipeOutput().setItemDamage(i);
					barrelRecipes.add(new ExtendedCachedShapelessRecipe(tmp));
				}
			});
		}
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return LOTRGuiBarrel.class;
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("lotrNei.recipe.barrel.name") + " ("
				+ StatCollector.translateToLocal("lotrNei.recipe.shapeless") + ")";
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		NeiLotrUtil.loadCraftingRecipes(result, barrelRecipes, arecipes);
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		NeiLotrUtil.loadUsageRecipes(ingredient, barrelRecipes, arecipes);
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}
}
