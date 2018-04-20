package craftedMods.recipes.utils;

import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;

public class ItemStackWrapper {

	private final boolean isNBTSensitive;
	private final ItemStack stack;
	private final Item item;
	private final int damage;
	private final NBTTagCompound compound;

	public ItemStackWrapper(ItemStack stack) {
		this(stack, false);
	}

	public ItemStackWrapper(ItemStack stack, boolean isNBTSensitive) {
		this.isNBTSensitive = isNBTSensitive;
		this.item = stack.getItem();
		this.damage = stack.getItemDamage();
		this.compound = stack.getTagCompound();
		this.stack = stack;
	}

	public Item getItem() {
		return this.item;
	}

	public int getDamage() {
		return this.damage;
	}

	public NBTTagCompound getTagCompound() {
		return this.compound;
	}

	public ItemStack toItemStack() {
		return this.stack;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.item == null ? 0 : this.item.hashCode());
		result = prime * result + this.damage;
		result = prime * result + (this.isNBTSensitive ? 1231 : 1237);
		if (this.isNBTSensitive) result = prime * result + (this.compound == null ? 0 : this.compound.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (this.getClass() != obj.getClass()) return false;
		ItemStackWrapper other = (ItemStackWrapper) obj;
		if (this.item == null) {
			if (other.item != null) return false;
		} else if (!this.item.equals(other.item)) return false;
		if (this.damage != other.damage) return false;
		if (this.isNBTSensitive != other.isNBTSensitive) return false;
		if (this.isNBTSensitive) if (this.compound == null) {
			if (other.compound != null) return false;
		} else if (!this.compound.equals(other.compound)) return false;
		return true;
	}

}
