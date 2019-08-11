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
package craftedMods.lotr.recipes.api.utils;

import java.lang.reflect.Field;
import java.util.*;

import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import craftedMods.recipes.base.*;
import lotr.client.gui.LOTRGuiAlloyForge;
import lotr.common.item.LOTRItemMug;
import lotr.common.recipe.*;
import lotr.common.tileentity.LOTRTileEntityAlloyForgeBase;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;

public class LOTRRecipeHandlerUtils {

	private static ItemStack[] poison;
	private static List<IRecipe> brewingRecipes;
	private static float[] drinkStrenghts;

	private static Field catalystField;
	private static Field theForgeField;

	public static ItemStack[] getPoison() {
		if (LOTRRecipeHandlerUtils.poison == null) {
			List<ItemStack> poisonList = OreDictionary.getOres("poison");
			LOTRRecipeHandlerUtils.poison = poisonList.toArray(new ItemStack[poisonList.size()]);
		}
		return LOTRRecipeHandlerUtils.poison;
	}

	@SuppressWarnings("unchecked")
	public static List<IRecipe> getBrewingRecipes() {
		if (LOTRRecipeHandlerUtils.brewingRecipes == null) {
			try {
				Field brewingRecipesField = LOTRBrewingRecipes.class.getDeclaredField("recipes");
				brewingRecipesField.setAccessible(true);
				LOTRRecipeHandlerUtils.brewingRecipes = (List<IRecipe>) (List<?>) brewingRecipesField.get(null);
			} catch (Exception e) {
				System.err.print("Couldn't access LOTR brewing recipes: ");
				e.printStackTrace();
			}
		}
		return LOTRRecipeHandlerUtils.brewingRecipes;
	}

	public static float[] getDrinkStrenghts() {
		if (LOTRRecipeHandlerUtils.drinkStrenghts == null) {
			try {
				Field strenghts = LOTRItemMug.class.getDeclaredField("strengths");
				strenghts.setAccessible(true);
				LOTRRecipeHandlerUtils.drinkStrenghts = (float[]) strenghts.get(null);
			} catch (Exception e) {
				System.err.print("Couldn't access drinkStrengths: ");
				e.printStackTrace();
			}
		}
		return LOTRRecipeHandlerUtils.drinkStrenghts;
	}

	public static int getDrinkStrengthIndex(float strength) {
		int index = 0;
		float[] strengths = LOTRRecipeHandlerUtils.getDrinkStrenghts();
		for (int i = 0; i < strengths.length; i++)
			if (strengths[i] == strength) {
				index = i;
				break;
			}
		return index;
	}

	public static AbstractRecipe processPoisonWeaponRecipe(LOTRRecipePoisonWeapon poisonRecipe) {
		try {
			if (catalystField == null) {
				catalystField = LOTRRecipePoisonWeapon.class.getDeclaredField("catalystObj");
				catalystField.setAccessible(true);
			}
			List<Object> ingredients = new ArrayList<>();
			ingredients.add(poisonRecipe.getInputItem());

			ingredients.add(RecipeHandlerUtils.getInstance().extractRecipeItems(catalystField.get(poisonRecipe)));
			return new ShapelessRecipe(ingredients, poisonRecipe.getRecipeOutput());
		} catch (Exception e) {
			System.err.print("Couldn't load the poisoned weapon recipe: ");
			e.printStackTrace();
		}
		return null;
	}

	public static LOTRTileEntityAlloyForgeBase getAlloyForge(LOTRGuiAlloyForge gui) {
		try {
			if (theForgeField == null) {
				theForgeField = LOTRGuiAlloyForge.class.getDeclaredField("theForge");
				theForgeField.setAccessible(true);
			}
			return (LOTRTileEntityAlloyForgeBase) theForgeField.get(gui);
		} catch (Exception e) {
			System.err.print("Couldn't load the poisoned weapon recipe: ");
			e.printStackTrace();
		}
		return null;
	}

	public static String getUnlocalizedEntityName(Class<?> entityClass) {
		return EntityList.classToStringMapping.get(entityClass).toString().replace("lotr.", "");
	}

}
