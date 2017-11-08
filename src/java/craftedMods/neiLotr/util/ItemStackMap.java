package craftedMods.neiLotr.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemStackMap<V> implements Map<ItemStack, V> {

	private HashMap<MapKey, V> innerMap = new HashMap<>();

	@Override
	public void clear() {
		innerMap.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return key != null && key instanceof ItemStack && innerMap.containsKey(new MapKey((ItemStack) key));
	}

	@Override
	public boolean containsValue(Object value) {
		return value != null && innerMap.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<ItemStack, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public V get(Object key) {
		return key != null && key instanceof ItemStack ? innerMap.get(new MapKey((ItemStack) key)) : null;
	}

	@Override
	public boolean isEmpty() {
		return innerMap.isEmpty();
	}

	@Override
	public Set<ItemStack> keySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public V put(ItemStack key, V value) {
		if (key == null || value == null)
			throw new NullPointerException("Key or value is null");
		return innerMap.put(new MapKey(key), value);
	}

	@Override
	public void putAll(Map<? extends ItemStack, ? extends V> m) {
		m.forEach((key, value) -> {
			if (key == null || value == null)
				throw new NullPointerException("One key or value is null");
			innerMap.put(new MapKey(key), value);
		});
	}

	@Override
	public V remove(Object key) {
		if (key == null)
			throw new NullPointerException("Key is null");
		if (!(key instanceof ItemStack))
			throw new IllegalArgumentException("Key is not an instance of item stack");
		return innerMap.remove(key);
	}

	@Override
	public int size() {
		return innerMap.size();
	}

	@Override
	public Collection<V> values() {
		throw new UnsupportedOperationException();
	}

	private class MapKey {

		private final Item item;
		private final int damage;

		public MapKey(ItemStack stack) {
			this.item = stack.getItem();
			this.damage = stack.getItemDamage();
		}

		public Item getItem() {
			return item;
		}

		public int getDamage() {
			return damage;
		}

		public ItemStack toItemStack() {
			return new ItemStack(item, 1, damage);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + damage;
			result = prime * result + ((item == null) ? 0 : item.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			MapKey other = (MapKey) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (damage != other.damage)
				return false;
			if (item == null) {
				if (other.item != null)
					return false;
			} else if (!item.equals(other.item))
				return false;
			return true;
		}

		private ItemStackMap getOuterType() {
			return ItemStackMap.this;
		}

	}

}
