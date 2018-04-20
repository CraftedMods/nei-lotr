package craftedMods.recipes.utils;

import java.util.*;

import craftedMods.recipes.api.utils.ItemStackMap;
import net.minecraft.item.ItemStack;

public class ItemStackMapImpl<V> implements ItemStackMap<V> {

	private final boolean isNBTSensitive;
	private final HashMap<ItemStackWrapper, V> innerMap = new HashMap<>();

	public ItemStackMapImpl() {
		this(false);
	}

	public ItemStackMapImpl(boolean isNBTSensitive) {
		this.isNBTSensitive = isNBTSensitive;
	}

	@Override
	public V put(ItemStack key, V value) {
		if (key == null || value == null) throw new NullPointerException("Key or value is null");
		return this.innerMap.put(new ItemStackWrapper(key, this.isNBTSensitive), value);
	}

	@Override
	public void putAll(Map<? extends ItemStack, ? extends V> map) {
		map.forEach((key, value) -> {
			this.put(key, value);
		});
	}

	@Override
	public V get(Object key) {
		return key != null && key instanceof ItemStack ? this.innerMap.get(new ItemStackWrapper((ItemStack) key, this.isNBTSensitive)) : null;
	}

	@Override
	public Set<ItemStack> keySet() {
		Set<ItemStack> ret = new HashSet<>();
		for (ItemStackWrapper key : this.innerMap.keySet())
			ret.add(key.toItemStack());
		return ret;
	}

	@Override
	public Collection<V> values() {
		return new ArrayList<>(this.innerMap.values());
	}

	@Override
	public Set<java.util.Map.Entry<ItemStack, V>> entrySet() {
		Set<java.util.Map.Entry<ItemStack, V>> entrySet = new HashSet<>();
		this.innerMap.forEach((key, value) -> {
			entrySet.add(new AbstractMap.SimpleEntry<>(key.toItemStack(), value));
		});
		return entrySet;
	}

	@Override
	public boolean isEmpty() {
		return this.innerMap.isEmpty();
	}

	@Override
	public int size() {
		return this.innerMap.size();
	}

	@Override
	public boolean containsKey(Object key) {
		return key != null && key instanceof ItemStack && this.innerMap.containsKey(new ItemStackWrapper((ItemStack) key, this.isNBTSensitive));
	}

	@Override
	public boolean containsValue(Object value) {
		return value != null && this.innerMap.containsValue(value);
	}

	@Override
	public V remove(Object key) {
		if (key == null) throw new NullPointerException("Key is null");
		if (!(key instanceof ItemStack)) throw new IllegalArgumentException("Key is not an instance of item stack");
		return this.innerMap.remove(key);
	}

	@Override
	public void clear() {
		this.innerMap.clear();
	}

}
