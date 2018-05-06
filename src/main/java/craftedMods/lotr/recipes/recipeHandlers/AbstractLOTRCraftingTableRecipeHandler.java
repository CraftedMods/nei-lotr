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

import java.lang.reflect.Field;
import java.util.*;

import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import craftedMods.recipes.base.*;
import lotr.common.recipe.LOTRRecipePoisonWeapon;
import net.minecraft.item.crafting.IRecipe;

public class AbstractLOTRCraftingTableRecipeHandler extends CraftingGridRecipeHandler {

	protected AbstractLOTRCraftingTableRecipeHandler(String unlocalizedName) {
		super(unlocalizedName);
	}

	protected AbstractLOTRCraftingTableRecipeHandler(String unlocalizedName, Collection<IRecipe> recipes) {
		super(unlocalizedName);
		this.recipes.addAll(recipes);
	}

	@Override
	protected void undefinedRecipeTypeFound(IRecipe recipe, Collection<AbstractRecipe> container) {
		if (recipe instanceof LOTRRecipePoisonWeapon) {
			LOTRRecipePoisonWeapon poisonRecipe = (LOTRRecipePoisonWeapon) recipe;
			List<Object> ingredients = new ArrayList<>();
			ingredients.add(poisonRecipe.getInputItem());
			try {
				Field catalystField = LOTRRecipePoisonWeapon.class.getDeclaredField("catalystObj");
				catalystField.setAccessible(true);
				ingredients.add(RecipeHandlerUtils.getInstance().extractRecipeItems(catalystField.get(poisonRecipe)));
				container.add(new ShapelessRecipe(ingredients, recipe.getRecipeOutput()));
			} catch (Exception e) {
				this.logger.error("Couldn't load poisoned weapon recipe: ", e);
			}
			return;
		}
		super.undefinedRecipeTypeFound(recipe, container);
	}

}
