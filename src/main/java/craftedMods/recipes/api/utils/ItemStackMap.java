package craftedMods.recipes.api.utils;

import java.util.Map;

import craftedMods.recipes.utils.ItemStackMapImpl;
import net.minecraft.item.ItemStack;

public interface ItemStackMap<V> extends Map<ItemStack, V> {

	public static <T> ItemStackMap<T> create() {
		return new ItemStackMapImpl<>();
	}

	public static <T> ItemStackMap<T> create(boolean isNBTSensitive) {
		return new ItemStackMapImpl<>(isNBTSensitive);
	}

}
