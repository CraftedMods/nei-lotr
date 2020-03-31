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

import craftedMods.lotr.recipes.api.recipeHandlers.AbstractAlloyForgeRecipeHandler;
import lotr.common.tileentity.*;
import net.minecraft.item.ItemStack;

public class LOTRAlloyForgeRecipeHandler extends AbstractAlloyForgeRecipeHandler {

	public LOTRAlloyForgeRecipeHandler(String unlocalizedName, AlloyForgeAccess alloyForgeDummy) {
		super("lotr.alloyForge." + unlocalizedName, alloyForgeDummy);
	}
	
	@Override
	public int getDefaultOrder ()
	{
	    return 3000;
	}

	public static class DwarvenForgeAccess extends LOTRTileEntityDwarvenForge implements AlloyForgeAccess {

		@Override
		public String getName() {
			return super.getForgeName();
		}

		@Override
		public ItemStack getAlloyResult(ItemStack ingredient, ItemStack alloy) {
			return super.getAlloySmeltingResult(ingredient, alloy);
		}

		@Override
		public ItemStack getSmeltingResult(ItemStack itemstack) {
			return super.getSmeltingResult(itemstack);
		}

	}

	public static class ElvenForgeAccess extends LOTRTileEntityElvenForge implements AlloyForgeAccess {

		@Override
		public String getName() {
			return super.getForgeName();
		}

		@Override
		public ItemStack getAlloyResult(ItemStack ingredient, ItemStack alloy) {
			return super.getAlloySmeltingResult(ingredient, alloy);
		}

		@Override
		public ItemStack getSmeltingResult(ItemStack itemstack) {
			return super.getSmeltingResult(itemstack);
		}

	}

	public static class MenForgeAccess extends LOTRTileEntityAlloyForge implements AlloyForgeAccess {

		@Override
		public String getName() {
			return super.getForgeName();
		}

		@Override
		public ItemStack getAlloyResult(ItemStack ingredient, ItemStack alloy) {
			return super.getAlloySmeltingResult(ingredient, alloy);
		}

		@Override
		public ItemStack getSmeltingResult(ItemStack itemstack) {
			return super.getSmeltingResult(itemstack);
		}

	}

	public static class OrcForgeAccess extends LOTRTileEntityOrcForge implements AlloyForgeAccess {

		@Override
		public String getName() {
			return super.getForgeName();
		}

		@Override
		public ItemStack getAlloyResult(ItemStack ingredient, ItemStack alloy) {
			return super.getAlloySmeltingResult(ingredient, alloy);
		}

		@Override
		public ItemStack getSmeltingResult(ItemStack itemstack) {
			return super.getSmeltingResult(itemstack);
		}

	}

}
