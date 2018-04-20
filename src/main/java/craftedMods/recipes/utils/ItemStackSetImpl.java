package craftedMods.recipes.utils;

import java.util.*;

import craftedMods.recipes.api.utils.ItemStackSet;
import net.minecraft.item.ItemStack;

public class ItemStackSetImpl extends AbstractSet<ItemStack> implements ItemStackSet {

	private final boolean isNBTSensitive;
	private final ArrayList<ItemStackWrapper> innerList = new ArrayList<>();
	private final ArrayList<ItemStack> stacksList = new ArrayList<>();

	public ItemStackSetImpl(ItemStack... stacks) {
		this(false, stacks);
	}

	public ItemStackSetImpl(boolean isNBTSensitive, ItemStack... stacks) {
		this.isNBTSensitive = isNBTSensitive;
		for (ItemStack stack : stacks)
			this.add(stack);
	}

	public ItemStackSetImpl(Collection<? extends ItemStack> stacks) {
		this(false, stacks);
	}

	public ItemStackSetImpl(boolean isNBTSensitive, Collection<? extends ItemStack> stacks) {
		this.isNBTSensitive = isNBTSensitive;
		this.addAll(stacks);
	}

	@Override
	public Iterator<ItemStack> iterator() {
		return this.stacksList.iterator();
	}

	@Override
	public int size() {
		return this.innerList.size();
	}

	@Override
	public boolean isEmpty() {
		return this.innerList.isEmpty();
	}

	public boolean contains(ItemStack stack) {
		return this.innerList.contains(new ItemStackWrapper(stack, this.isNBTSensitive));
	}

	@Override
	public boolean add(ItemStack stack) {
		boolean ret = false;
		ItemStackWrapper wrapper = new ItemStackWrapper(stack, this.isNBTSensitive);
		if (!this.innerList.contains(wrapper)) {
			ret = this.innerList.add(wrapper);
			if (ret) this.stacksList.add(stack);
		}
		return ret;
	}

	public boolean remove(ItemStack stack) {
		return this.innerList.remove(new ItemStackWrapper(stack, this.isNBTSensitive)) && this.stacksList.remove(stack);
	}

	@Override
	public void clear() {
		this.innerList.clear();
		this.stacksList.clear();
	}

}
