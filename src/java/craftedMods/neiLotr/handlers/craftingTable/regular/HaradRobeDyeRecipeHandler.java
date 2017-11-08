package craftedMods.neiLotr.handlers.craftingTable.regular;

import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemHaradRobes;
import net.minecraft.item.ItemStack;

public class HaradRobeDyeRecipeHandler extends AbstractDyeRecipeHandler {

	public HaradRobeDyeRecipeHandler() {
		super(LOTRMod.bodyHaradRobes, LOTRMod.legsHaradRobes, LOTRMod.bootsHaradRobes);
	}

	@Override
	protected int getDyeColor(ItemStack stack) {
		return LOTRItemHaradRobes.getRobesColor(stack);
	}

	@Override
	protected boolean isDyed(ItemStack stack) {
		return LOTRItemHaradRobes.areRobesDyed(stack);
	}

	@Override
	protected void setDyeColor(ItemStack stack, int color) {
		LOTRItemHaradRobes.setRobesColor(stack, color);
	}

	@Override
	protected void removeDye(ItemStack stack) {
		LOTRItemHaradRobes.removeRobeDye(stack);
	}

}
