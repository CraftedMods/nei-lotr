package craftedMods.recipes.utils;

import java.util.*;

import codechicken.nei.*;
import cpw.mods.fml.relauncher.ReflectionHelper;
import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.ItemStackSet;
import craftedMods.recipes.base.RecipeItemSlotImpl;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;

public class NEIExtensionsUtils {

	public static ItemStackSet generatePermutations(Collection<ItemStack> stacks) {
		ItemStackSet permutations = ItemStackSet.create();
		for (ItemStack stack : stacks)
			if (stack != null && stack.getItem() != null) {
				List<ItemStack> perms = ItemList.itemMap.get(stack.getItem());
				if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE && perms != null && !perms.isEmpty()) permutations.addAll(perms);
				else permutations.add(stack);
			}
		return permutations;
	}

	public static boolean areStacksSameType(ItemStack stack1, ItemStack stack2) {
		return NEIServerUtils.areStacksSameType(stack1, stack2);
	}

	public static boolean areStacksSameTypeForCrafting(ItemStack stack1, ItemStack stack2) {
		return NEIServerUtils.areStacksSameTypeCrafting(stack1, stack2);
	}

	public static EnumRecipeItemRole createRecipeItemRole(String name) {
		return EnumHelper.addEnum(EnumRecipeItemRole.class, name);
	}

	public static ItemStack[] extractRecipeItems(Object container) {
		if (container instanceof String) {
			List<ItemStack> stacks = OreDictionary.getOres((String) container);
			return stacks.toArray(new ItemStack[stacks.size()]);
		} else if (container instanceof Item) return new ItemStack[] { new ItemStack((Item) container) };
		else if (container instanceof Block) return new ItemStack[] { new ItemStack((Block) container) };
		else return NEIServerUtils.extractRecipeItems(container);
	}

	public static List<ItemStack> getItemList() {
		return ItemList.items;
	}

	public static String[] getRecipeHandlerCategories(String unlocalizedName) {
		String[] categories;
		String[] cats = unlocalizedName.split("\\.");
		if (cats.length > 1) {
			categories = new String[cats.length - 1];
			for (int i = 0; i < cats.length - 1; i++)
				categories[i] = cats[i];
		} else categories = new String[] {};
		return categories;
	}

	public static Collection<ItemStack> readItemStackListFromNBT(NBTTagCompound compound, String tagName) {
		List<ItemStack> ret = new ArrayList<>();
		if (compound.hasKey(tagName)) {
			NBTTagList list = compound.getTagList(tagName, 10);
			for (int i = 0; i < list.tagCount(); i++) {
				ItemStack stack = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i));
				if (stack != null) ret.add(stack);
			}
		}
		return ret;
	}

	public static void writeItemStackListToNBT(NBTTagCompound compound, String tagName, Collection<? extends ItemStack> stacks) {
		NBTTagList list = new NBTTagList();
		for (ItemStack stack : stacks) {
			NBTTagCompound stackTag = new NBTTagCompound();
			stack.writeToNBT(stackTag);
			list.appendTag(stackTag);
		}
		compound.setTag(tagName, list);
	}

	public static List<RecipeItemSlot> offset(List<RecipeItemSlot> slotsList, int xOffset, int yOffset) {
		List<RecipeItemSlot> ret = new ArrayList<>();
		for (RecipeItemSlot slot : slotsList)
			ret.add(slot == null ? null : new RecipeItemSlotImpl(slot.getX() + xOffset, slot.getY() + yOffset));
		return ret;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean registerDefaultResourcePack(IResourcePack pack) {
		return ((List) ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "defaultResourcePacks", "field_110449_ao")).add(pack);
	}

}
