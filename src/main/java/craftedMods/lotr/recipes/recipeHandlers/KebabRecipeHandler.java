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
import craftedMods.recipes.api.utils.RecipeHandlerRendererUtils;
import craftedMods.recipes.base.*;
import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityKebabStand;
import net.minecraft.item.ItemStack;

@RegisteredHandler
public class KebabRecipeHandler extends AbstractRecipeHandler<ShapelessRecipe> {

	private final KebabRecipeHandlerRenderer renderer = new KebabRecipeHandlerRenderer();
	private final LOTRTileEntityKebabStand kebabStandDummy = new LOTRTileEntityKebabStand();

	public KebabRecipeHandler() {
		super("lotr.kebab");
	}

	@Override
	public String getDisplayName() {
		return LOTRMod.kebabStand.getLocalizedName();
	}

	@Override
	public int getComplicatedStaticRecipeDepth() {
		return 1;
	}

	@Override
	public ShapelessRecipe loadComplicatedStaticRecipe(ItemStack... stacks) {
		ShapelessRecipe recipe = null;
		if (this.kebabStandDummy.isMeat(stacks[0])) {
			recipe = new ShapelessRecipe(Arrays.asList(stacks[0]), new ItemStack(LOTRMod.kebab));
		}
		return recipe;
	}

	@Override
	public List<RecipeItemSlot> getSlotsForRecipeItems(ShapelessRecipe recipe, EnumRecipeItemRole role) {
		return Arrays.asList(role == EnumRecipeItemRole.INGREDIENT ? this.createRecipeItemSlot(43, 24) : this.createRecipeItemSlot(101, 24));
	}

	@Override
	@SuppressWarnings("unchecked")
	public KebabRecipeHandlerRenderer getRenderer() {
		return this.renderer;
	}

	public class KebabRecipeHandlerRenderer implements RecipeHandlerRenderer<KebabRecipeHandler, ShapelessRecipe> {

		@Override
		public void renderBackground(KebabRecipeHandler handler, ShapelessRecipe recipe, int cycleticks) {
			RecipeHandlerRendererUtils.getInstance().bindTexture(RecipeHandlerRenderer.DEFAULT_GUI_TEXTURE);
			RecipeHandlerRendererUtils.getInstance().drawTexturedRectangle(42, 19, 65, 30, 80, 26);
			RecipeHandlerRendererUtils.getInstance().drawRectangle(42, 13, 18, 10, 0xFFC6C6C6);
			RecipeHandlerRendererUtils.getInstance().drawRectangle(42, 41, 18, 4, 0xFFC6C6C6);
		}

		@Override
		public void renderForeground(KebabRecipeHandler handler, ShapelessRecipe recipe, int cycleticks) {}

	}
}
