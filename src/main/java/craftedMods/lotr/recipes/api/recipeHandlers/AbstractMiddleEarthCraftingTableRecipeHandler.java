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
package craftedMods.lotr.recipes.api.recipeHandlers;

import java.util.Collection;

import craftedMods.lotr.recipes.api.utils.LOTRRecipeHandlerUtils;
import craftedMods.recipes.base.*;
import lotr.common.recipe.LOTRRecipePoisonWeapon;
import net.minecraft.item.crafting.IRecipe;

public abstract class AbstractMiddleEarthCraftingTableRecipeHandler extends CraftingGridRecipeHandler {

	protected AbstractMiddleEarthCraftingTableRecipeHandler(String unlocalizedName) {
		super(unlocalizedName);
	}

	protected AbstractMiddleEarthCraftingTableRecipeHandler(String unlocalizedName, Collection<IRecipe> recipes) {
		super(unlocalizedName);
		this.recipes.addAll(recipes);
	}

	@Override
	protected void undefinedRecipeTypeFound(IRecipe recipe, Collection<AbstractRecipe> container) {
		if (recipe instanceof LOTRRecipePoisonWeapon) {
			AbstractRecipe processedRecipe = LOTRRecipeHandlerUtils.processPoisonWeaponRecipe((LOTRRecipePoisonWeapon) recipe);
			if (processedRecipe != null) {
				container.add(processedRecipe);
				return;
			}
		}
		super.undefinedRecipeTypeFound(recipe, container);
	}

}
