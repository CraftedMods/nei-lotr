package craftedMods.recipes.utils;

import java.util.*;

import craftedMods.recipes.NEIExtensions;
import craftedMods.recipes.api.RecipeItemSlot;
import craftedMods.recipes.api.utils.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RecipeHandlerUtilsImpl implements RecipeHandlerUtils {

	@Override
	public ItemStackSet generatePermutations(ItemStack... stacks) {
		return NEIExtensionsUtils.generatePermutations(Arrays.asList(stacks));
	}

	@Override
	public ItemStackSet generatePermutations(Collection<ItemStack> stacks) {
		return NEIExtensionsUtils.generatePermutations(stacks);
	}

	@Override
	public boolean areStacksSameType(ItemStack stack1, ItemStack stack2) {
		return NEIExtensionsUtils.areStacksSameType(stack1, stack2);
	}

	@Override
	public boolean areStacksSameTypeForCrafting(ItemStack stack1, ItemStack stack2) {
		return NEIExtensionsUtils.areStacksSameTypeForCrafting(stack1, stack2);
	}

	@Override
	public ItemStack[] extractRecipeItems(Object container) {
		return NEIExtensionsUtils.extractRecipeItems(container);
	}

	@Override
	public List<ItemStack> getItemList() {
		return NEIExtensionsUtils.getItemList();
	}

	@Override
	public Collection<ItemStack> readItemStackListFromNBT(NBTTagCompound compound, String tagName) {
		return NEIExtensionsUtils.readItemStackListFromNBT(compound, tagName);
	}

	@Override
	public void writeItemStackListToNBT(NBTTagCompound compound, String tagName, Collection<? extends ItemStack> stacks) {
		NEIExtensionsUtils.writeItemStackListToNBT(compound, tagName, stacks);
	}

	@Override
	public List<RecipeItemSlot> offset(List<RecipeItemSlot> slotsList, int xOffset, int yOffset) {
		return NEIExtensionsUtils.offset(slotsList, xOffset, yOffset);
	}

	@Override
	public void forceRecipeCacheRefresh() {
		NEIExtensions.mod.getNeiConfig().refreshCache();
	}

}
