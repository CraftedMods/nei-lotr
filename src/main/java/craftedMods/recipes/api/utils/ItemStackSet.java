package craftedMods.recipes.api.utils;

import java.util.*;

import craftedMods.recipes.utils.ItemStackSetImpl;
import net.minecraft.item.ItemStack;

public interface ItemStackSet extends Set<ItemStack> {

	public static ItemStackSet create(ItemStack... stacks) {
		return new ItemStackSetImpl(stacks);
	}

	public static ItemStackSet create(boolean isNBTSensitive, ItemStack... stacks) {
		return new ItemStackSetImpl(isNBTSensitive, stacks);
	}

	public static ItemStackSet create(Collection<? extends ItemStack> stacks) {
		return new ItemStackSetImpl(stacks);
	}

	public static ItemStackSet create(boolean isNBTSensitive, Collection<? extends ItemStack> stacks) {
		return new ItemStackSetImpl(isNBTSensitive, stacks);
	}
}
