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
package craftedMods.lotr.recipes;

import java.util.*;

import craftedMods.recipes.api.*;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

@RegisteredHandler
public class LOTRItemOverrideHandler implements ItemOverrideHandler {

	private Map<ItemStack, String> overrides = new HashMap<>();

	@Override
	public Map<ItemStack, String> getItemOverrideNames() {
		this.overrides.clear();

		// Portals
		this.addOverride(LOTRMod.elvenPortal, StatCollector.translateToLocal("neiLotr.override.elvenPortal.name"));
		this.addOverride(LOTRMod.morgulPortal, StatCollector.translateToLocal("neiLotr.override.morgulPortal.name"));
		this.addOverride(LOTRMod.utumnoPortal, StatCollector.translateToLocal("neiLotr.override.utumnoPortal.name"));
		this.addOverride(LOTRMod.utumnoReturnPortal, StatCollector.translateToLocal("neiLotr.override.utumnoReturnPortal.name"));
		this.addOverride(LOTRMod.utumnoReturnPortalBase, StatCollector.translateToLocal("neiLotr.override.utumnoReturnPortalBase.name"));

		// Others
		this.addOverride(LOTRMod.rhunFire, StatCollector.translateToLocal("neiLotr.override.rhunFire.name"));

		return this.overrides;
	}

	private void addOverride(ItemStack stack, String name) {
		this.overrides.put(stack, name);
	}

	// private void addOverride(Item item, String name) {
	// this.setOverrideName(new ItemStack(item), name);
	// }

	private void addOverride(Block block, String name) {
		this.addOverride(new ItemStack(block), name);
	}

}
