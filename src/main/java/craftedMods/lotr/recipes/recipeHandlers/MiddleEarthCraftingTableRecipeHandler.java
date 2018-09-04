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

import craftedMods.recipes.api.RecipeHandlerCraftingHelper;
import craftedMods.recipes.base.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;

public class MiddleEarthCraftingTableRecipeHandler extends AbstractLOTRCraftingTableRecipeHandler {

	private final MiddleEarthCraftingTableRecipeHandlerCraftingHelper craftingHelper;

	public static final String UNLOCALIZED_NAME_PREFIX = "lotr.middleEarthCrafting.";

	public MiddleEarthCraftingTableRecipeHandler(String unlocalizedName, Class<? extends GuiContainer> guiClass, Collection<IRecipe> recipes) {
		super(MiddleEarthCraftingTableRecipeHandler.UNLOCALIZED_NAME_PREFIX + unlocalizedName, recipes);
		this.craftingHelper = new MiddleEarthCraftingTableRecipeHandlerCraftingHelper(guiClass);
	}

	@Override
	public String getDisplayName() {
		return StatCollector.translateToLocal(
				"container.lotr.crafting." + this.getUnlocalizedName().replace(MiddleEarthCraftingTableRecipeHandler.UNLOCALIZED_NAME_PREFIX, ""));
	}

	@Override
	public RecipeHandlerCraftingHelper<AbstractRecipe> getCraftingHelper() {
		return this.craftingHelper;
	}

	private class MiddleEarthCraftingTableRecipeHandlerCraftingHelper extends AbstractCraftingHelper<AbstractRecipe> {

		private final Collection<Class<? extends GuiContainer>> guiClass;

		public MiddleEarthCraftingTableRecipeHandlerCraftingHelper(Class<? extends GuiContainer> guiClass) {
			this.guiClass = Arrays.asList(guiClass);
		}

		@Override
		public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses(AbstractRecipe recipe) {
			return this.guiClass;
		}

		@Override
		public int getOffsetX(Class<? extends GuiContainer> guiClass, AbstractRecipe recipe) {
			return 5;
		}

		@Override
		public int getOffsetY(Class<? extends GuiContainer> guiClass, AbstractRecipe recipe) {
			return 11;
		}

	}

}
