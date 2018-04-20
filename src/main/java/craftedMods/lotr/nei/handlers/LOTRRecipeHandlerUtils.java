package craftedMods.lotr.nei.handlers;

import java.lang.reflect.*;
import java.util.*;

import craftedMods.recipes.NEIExtensions;
import craftedMods.recipes.api.utils.*;
import lotr.common.item.LOTRItemMug;
import lotr.common.recipe.LOTRBrewingRecipes;
import lotr.common.tileentity.LOTRTileEntityUnsmeltery;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;

public class LOTRRecipeHandlerUtils {

	private static ItemStack[] poison;
	private static List<IRecipe> brewingRecipes;
	private static float[] drinkStrenghts;
	private static List<ItemStack> fuels;

	public static ItemStack[] getPoison() {
		if (poison == null) {
			List<ItemStack> poisonList = OreDictionary.getOres("poison");
			poison = poisonList.toArray(new ItemStack[poisonList.size()]);
		}
		return poison;
	}

	public static ItemStackSet getFuels() {
		if (fuels == null) {
			Set<Item> excludedfuels = new java.util.HashSet();
			excludedfuels.add(Item.getItemFromBlock(Blocks.brown_mushroom));
			excludedfuels.add(Item.getItemFromBlock(Blocks.red_mushroom));
			excludedfuels.add(Item.getItemFromBlock(Blocks.standing_sign));
			excludedfuels.add(Item.getItemFromBlock(Blocks.wall_sign));
			excludedfuels.add(Item.getItemFromBlock(Blocks.wooden_door));
			excludedfuels.add(Item.getItemFromBlock(Blocks.trapped_chest));
			fuels = new ArrayList();
			for (ItemStack item : RecipeHandlerUtils.getInstance().getItemList()) {
				if (!excludedfuels.contains(item.getItem())) {
					int burnTime = net.minecraft.tileentity.TileEntityFurnace.getItemBurnTime(item);
					if (burnTime > 0)
						fuels.add(item.copy());
				}
			}

		}
		return ItemStackSet.create(fuels);
	}

	public static List<IRecipe> getBrewingRecipes() {
		if (brewingRecipes == null) {
			try {
				Field brewingRecipesField = LOTRBrewingRecipes.class.getDeclaredField("recipes");
				brewingRecipesField.setAccessible(true);
				brewingRecipes = (List<IRecipe>) (List<?>) brewingRecipesField.get(null);
			} catch (Exception e) {
				NEIExtensions.mod.getLogger().error("Couldn't access LOTR brewing recipes: ", e);
			}
		}
		return brewingRecipes;
	}

	public static float[] getDrinkStrenghts() {
		if (drinkStrenghts == null) {
			try {
				Field strenghts = LOTRItemMug.class.getDeclaredField("strengths");
				strenghts.setAccessible(true);
				drinkStrenghts = (float[]) strenghts.get(null);
			} catch (Exception e) {
				NEIExtensions.mod.getLogger().error("Couldn't access drinkStrengths: ", e);
			}
		}
		return drinkStrenghts;
	}

	public static int getDrinkStrengthIndex(float strength) {
		int index = 0;
		float[] strengths = getDrinkStrenghts();
		for (int i = 0; i < strengths.length; i++) {
			if (strengths[i] == strength) {
				index = i;
				break;
			}
		}
		return index;
	}

	private static Boolean hasCraftTweaker = null;

	public static boolean hasCraftTweaker() {
		if (hasCraftTweaker == null) {
			try {
				Class.forName("minetweaker.MineTweakerAPI");
				hasCraftTweaker = Boolean.TRUE;
			} catch (Exception e) {
				hasCraftTweaker = Boolean.FALSE;
			}
		}
		return hasCraftTweaker;
	}

}
