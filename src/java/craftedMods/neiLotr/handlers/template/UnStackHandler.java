package craftedMods.neiLotr.handlers.template;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface UnStackHandler {
	
	public boolean handleStack(ItemStack stack);

}
