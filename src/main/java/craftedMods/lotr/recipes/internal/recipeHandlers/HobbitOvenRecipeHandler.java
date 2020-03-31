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

import craftedMods.lotr.recipes.internal.recipeHandlers.HobbitOvenRecipeHandler.HobbitOvenRecipe;
import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.RecipeHandlerRendererUtils;
import craftedMods.recipes.api.utils.RecipeHandlerRendererUtils.EnumProgressBarDirection;
import craftedMods.recipes.base.*;
import lotr.client.gui.LOTRGuiHobbitOven;
import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityHobbitOven;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

@RegisteredHandler
public class HobbitOvenRecipeHandler extends AbstractRecipeHandler<HobbitOvenRecipe> {

	private final HobbitOvenRecipeHandlerRenderer renderer = new HobbitOvenRecipeHandlerRenderer();
	private final HobbitOvenRecipeHandlerRecipeViewer recipeViewer = new HobbitOvenRecipeHandlerRecipeViewer(this);

	public HobbitOvenRecipeHandler() {
		super("lotr.hobbitOven");
	}

	@Override
	public String getDisplayName() {
		return LOTRMod.hobbitOven.getLocalizedName();
	}

	@Override
	public int getRecipesPerPage() {
		return 1;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<HobbitOvenRecipe> loadSimpleStaticRecipes() {
		Collection<HobbitOvenRecipe> ret = new ArrayList<>();
		((Map<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList()).forEach((ingredient, result) -> {
			if (LOTRTileEntityHobbitOven.isCookResultAcceptable(result)) {
				ret.add(new HobbitOvenRecipe(ingredient, result));
			}
		});
		return ret;
	}

	@Override
	public List<RecipeItemSlot> getSlotsForRecipeItems(HobbitOvenRecipe recipe, EnumRecipeItemRole role) {
		List<RecipeItemSlot> slots = new ArrayList<>();
		switch (role) {
			case INGREDIENT:
				for (int i = 0; i < 9; i++) {
					slots.add(this.createRecipeItemSlot(3 + i * 18, 10));
				}
				break;
			case RESULT:
				for (int i = 0; i < 9; i++) {
					slots.add(this.createRecipeItemSlot(3 + i * 18, 56));
				}
				break;
			case OTHER:
				slots.add(this.createRecipeItemSlot(75, 100));
				break;
		}
		return slots;
	}

	@Override
	@SuppressWarnings("unchecked")
	public HobbitOvenRecipeHandlerRenderer getRenderer() {
		return this.renderer;
	}

	@Override
	public RecipeHandlerRecipeViewer<HobbitOvenRecipe> getRecipeViewer() {
		return this.recipeViewer;
	}
	
	@Override
	public int getDefaultOrder ()
	{
	    return 8000;
	}

	public class HobbitOvenRecipe extends FurnaceRecipe {

		public HobbitOvenRecipe(ItemStack ingredient, ItemStack result) {
			super(ingredient, result);
			this.addAll(Arrays.asList(ingredient, ingredient, ingredient, ingredient, ingredient, ingredient, ingredient, ingredient), this.ingredients);
			this.addAll(Arrays.asList(result, result, result, result, result, result, result, result), this.results);
		}

	}

	public class HobbitOvenRecipeHandlerRenderer implements RecipeHandlerRenderer<HobbitOvenRecipeHandler, HobbitOvenRecipe> {

		@Override
		public void renderBackground(HobbitOvenRecipeHandler handler, HobbitOvenRecipe recipe, int cycleticks) {
			RecipeHandlerRendererUtils.getInstance().bindTexture("lotr:gui/oven.png");
			RecipeHandlerRendererUtils.getInstance().drawTexturedRectangle(0, 0, 5, 11, 166, 117);
			RecipeHandlerRendererUtils.getInstance().drawProgressBar(75, 29, 176, 14, 24, 25, cycleticks % 48 / 48.0f, EnumProgressBarDirection.INCREASE_DOWN);
			RecipeHandlerRendererUtils.getInstance().drawProgressBar(75, 83, 176, 0, 14, 13, cycleticks % 48 / 48.0f, EnumProgressBarDirection.DECREASE_DOWN);

		}

		@Override
		public void renderForeground(HobbitOvenRecipeHandler handler, HobbitOvenRecipe recipe, int cycleticks) {}

	}

	public class HobbitOvenRecipeHandlerRecipeViewer extends AbstractRecipeViewer<HobbitOvenRecipe, HobbitOvenRecipeHandler> {

		private final Collection<Class<? extends GuiContainer>> supportedGuiClasses = new ArrayList<>();

		public HobbitOvenRecipeHandlerRecipeViewer(HobbitOvenRecipeHandler handler) {
			super(handler);
			this.supportedGuiClasses.addAll(AbstractRecipeViewer.RECIPE_HANDLER_GUIS);
			this.supportedGuiClasses.add(LOTRGuiHobbitOven.class);
		}

		@Override
		public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses() {
			return this.supportedGuiClasses;
		}

		@Override
		public Collection<HobbitOvenRecipe> getAllRecipes() {
			return this.handler.getStaticRecipes();
		}

		@Override
		public int getOffsetX(Class<? extends GuiContainer> guiClass) {
			return guiClass == LOTRGuiHobbitOven.class ? 144 : 0;
		}

		@Override
		public int getOffsetY(Class<? extends GuiContainer> guiClass) {
			return guiClass == LOTRGuiHobbitOven.class ? 76 : 9;
		}

	}

}
