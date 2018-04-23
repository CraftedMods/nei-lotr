package craftedMods.recipes.api.utils;

import java.util.*;

import craftedMods.recipes.api.RecipeItemSlot;
import craftedMods.recipes.utils.RecipeHandlerUtilsImpl;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface RecipeHandlerUtils {

	public static final RecipeHandlerUtils instance = new RecipeHandlerUtilsImpl();

	public static RecipeHandlerUtils getInstance() {
		return RecipeHandlerUtils.instance;
	}

	public ItemStackSet generatePermutations(ItemStack... stacks);

	public ItemStackSet generatePermutations(Collection<ItemStack> stacks);

	public boolean areStacksSameType(ItemStack stack1, ItemStack stack2);

	public boolean areStacksSameTypeForCrafting(ItemStack stack1, ItemStack stack2);

	public ItemStack[] extractRecipeItems(Object container);

	// Only call this after the item list was initialized
	public List<ItemStack> getItemList();

	public Collection<ItemStack> readItemStackListFromNBT(NBTTagCompound compound, String tagName);

	public void writeItemStackListToNBT(NBTTagCompound compound, String tagName, Collection<? extends ItemStack> stacks);

	public List<RecipeItemSlot> offset(List<RecipeItemSlot> slotsList, int xOffset, int yOffset);

	public void forceRecipeCacheRefresh();
	
	public String getResourceDomain();

}
