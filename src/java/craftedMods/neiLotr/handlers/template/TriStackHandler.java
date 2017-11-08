package craftedMods.neiLotr.handlers.template;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface TriStackHandler {
	
	public boolean handleStacks(ItemStack stack1, ItemStack stack2, ItemStack stack3);

}
