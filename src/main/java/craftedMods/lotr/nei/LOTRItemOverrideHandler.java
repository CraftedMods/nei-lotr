package craftedMods.lotr.nei;

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
