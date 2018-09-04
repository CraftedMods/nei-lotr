/*******************************************************************************
 * Copyright (C) 2018 CraftedMods (see https://github.com/CraftedMods)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package craftedMods.lotr.recipes.recipeHandlers;

import java.util.*;

import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.ItemStackSet;
import craftedMods.recipes.base.*;
import lotr.client.gui.LOTRGuiBarrel;
import lotr.common.LOTRMod;
import lotr.common.item.LOTRPoisonedDrinks;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@RegisteredHandler
public class BarrelRecipeHandler extends CraftingGridRecipeHandler {

	private final BarrelRecipeHandlerCraftingHelper craftingHelper = new BarrelRecipeHandlerCraftingHelper();

	public static final int[][] BARREL_STACKORDER = new int[][] { { 0, 0 }, { 1, 0 }, { 2, 0 }, { 0, 1 }, { 1, 1 }, { 2, 1 }, { 0, 2 }, { 1, 2 }, { 2, 2 } };

	public BarrelRecipeHandler() {
		super("lotr.barrel", LOTRRecipeHandlerUtils.getBrewingRecipes());
	}

	@Override
	public Collection<AbstractRecipe> loadSimpleStaticRecipes() {
		Collection<AbstractRecipe> ret = new ArrayList<>();
		for (IRecipe recipe : this.recipes)
			if (recipe instanceof ShapelessOreRecipe) {
				ShapelessOreRecipe shapelessOreRecipe = (ShapelessOreRecipe) recipe;
				ret.add(new BrewingRecipe(shapelessOreRecipe.getInput(), shapelessOreRecipe.getRecipeOutput()));
			} else {
				this.undefinedRecipeTypeFound(recipe, ret);
			}
		return ret;
	}

	@Override
	public String getDisplayName() {
		return LOTRMod.barrel.getLocalizedName();
	}

	@Override
	public List<RecipeItemSlot> getSlotsForRecipeItems(AbstractRecipe recipe, EnumRecipeItemRole role) {
		return this.getSlotsForRecipeItems(recipe, role, BarrelRecipeHandler.BARREL_STACKORDER);
	}

	@Override
	public BarrelRecipeHandlerCraftingHelper getCraftingHelper() {
		return this.craftingHelper;
	}

	public class BarrelRecipeHandlerCraftingHelper extends AbstractCraftingHelper<AbstractRecipe> {

		@Override
		public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses(AbstractRecipe recipe) {
			return Arrays.asList(LOTRGuiBarrel.class);
		}

		@Override
		public int getOffsetX(Class<? extends GuiContainer> guiClass, AbstractRecipe recipe) {
			return -11;
		}

		@Override
		public int getOffsetY(Class<? extends GuiContainer> guiClass, AbstractRecipe recipe) {
			return 28;
		}
	}

	public class BrewingRecipe extends ShapelessRecipe {

		public BrewingRecipe(Collection<?> ingredients, ItemStack result) {
			super(ingredients, result);
			for (int i = 0; i < 3; i++) {
				this.add(new ItemStack(Items.water_bucket), this.ingredients);
			}
			for (int i = 0; i < 4; i++) {
				this.results.get(0).add(new ItemStack(result.getItem(), result.stackSize, result.getItemDamage() + i + 1));
			}
		}

		@Override
		public boolean produces(ItemStack result) {
			if (!LOTRPoisonedDrinks.isDrinkPoisoned(result)) {
				for (ItemStackSet permutations : this.results)
					if (permutations != null) {
						for (ItemStack permutation : permutations)
							if (result.getItem() == permutation.getItem()) return true;
					}
			}
			return false;
		}

		@Override
		public ItemStack getResultReplacement(ItemStack defaultReplacement) {
			return defaultReplacement;
		}

	}

}
