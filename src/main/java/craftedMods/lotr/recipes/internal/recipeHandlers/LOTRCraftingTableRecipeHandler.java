/*******************************************************************************
 * Copyright (C) 2019 CraftedMods (see https://github.com/CraftedMods)
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
package craftedMods.lotr.recipes.internal.recipeHandlers;

import java.util.*;

import craftedMods.lotr.recipes.api.recipeHandlers.AbstractMiddleEarthCraftingTableRecipeHandler;
import craftedMods.recipes.api.*;
import craftedMods.recipes.base.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;

public class LOTRCraftingTableRecipeHandler extends AbstractMiddleEarthCraftingTableRecipeHandler {

	private final MiddleEarthCraftingTableRecipeHandlerCraftingHelper craftingHelper;
	private final MiddleEarthCraftingTableRecipeHandlerRecipeViewer recipeViewer;

	public static final String UNLOCALIZED_NAME_PREFIX = "lotr.middleEarthCrafting.";

	public LOTRCraftingTableRecipeHandler(String unlocalizedName, Class<? extends GuiContainer> guiClass, Collection<IRecipe> recipes) {
		super(LOTRCraftingTableRecipeHandler.UNLOCALIZED_NAME_PREFIX + unlocalizedName, recipes);
		this.craftingHelper = new MiddleEarthCraftingTableRecipeHandlerCraftingHelper(guiClass);
		this.recipeViewer = new MiddleEarthCraftingTableRecipeHandlerRecipeViewer(this, guiClass);
	}

	@Override
	public String getDisplayName() {
		return StatCollector
				.translateToLocal("container.lotr.crafting." + this.getUnlocalizedName().replace(LOTRCraftingTableRecipeHandler.UNLOCALIZED_NAME_PREFIX, ""));
	}

	@Override
	public RecipeHandlerCraftingHelper<AbstractRecipe> getCraftingHelper() {
		return this.craftingHelper;
	}

	@Override
	public RecipeHandlerRecipeViewer<AbstractRecipe> getRecipeViewer() {
		return this.recipeViewer;
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

	private class MiddleEarthCraftingTableRecipeHandlerRecipeViewer extends AbstractRecipeViewer<AbstractRecipe, LOTRCraftingTableRecipeHandler> {

		private final Collection<Class<? extends GuiContainer>> supportedGuiClasses = new ArrayList<>();

		public MiddleEarthCraftingTableRecipeHandlerRecipeViewer(LOTRCraftingTableRecipeHandler handler, Class<? extends GuiContainer> guiClass) {
			super(handler);
			this.supportedGuiClasses.addAll(AbstractRecipeViewer.RECIPE_HANDLER_GUIS);
			this.supportedGuiClasses.add(guiClass);
		}

		@Override
		public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses() {
			return this.supportedGuiClasses;
		}

		@Override
		public Collection<AbstractRecipe> getAllRecipes() {
			return this.handler.getStaticRecipes();
		}

	}

}
