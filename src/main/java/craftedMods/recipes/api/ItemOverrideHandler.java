package craftedMods.recipes.api;

import java.util.Map;

import net.minecraft.item.ItemStack;

public interface ItemOverrideHandler {
	
	public Map<ItemStack,String> getItemOverrideNames();

}
