package craftedMods.lotr.recipes.recipeHandlers;

import java.lang.reflect.Field;
import java.util.List;

import lotr.common.item.LOTRItemMug;
import lotr.common.recipe.LOTRBrewingRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;

public class LOTRRecipeHandlerUtils {

	private static ItemStack[] poison;
	private static List<IRecipe> brewingRecipes;
	private static float[] drinkStrenghts;

	public static ItemStack[] getPoison() {
		if (LOTRRecipeHandlerUtils.poison == null) {
			List<ItemStack> poisonList = OreDictionary.getOres("poison");
			LOTRRecipeHandlerUtils.poison = poisonList.toArray(new ItemStack[poisonList.size()]);
		}
		return LOTRRecipeHandlerUtils.poison;
	}

	@SuppressWarnings("unchecked")
	public static List<IRecipe> getBrewingRecipes() {
		if (LOTRRecipeHandlerUtils.brewingRecipes == null) try {
			Field brewingRecipesField = LOTRBrewingRecipes.class.getDeclaredField("recipes");
			brewingRecipesField.setAccessible(true);
			LOTRRecipeHandlerUtils.brewingRecipes = (List<IRecipe>) (List<?>) brewingRecipesField.get(null);
		} catch (Exception e) {
			System.err.print("Couldn't access LOTR brewing recipes: ");
			e.printStackTrace();
		}
		return LOTRRecipeHandlerUtils.brewingRecipes;
	}

	public static float[] getDrinkStrenghts() {
		if (LOTRRecipeHandlerUtils.drinkStrenghts == null) try {
			Field strenghts = LOTRItemMug.class.getDeclaredField("strengths");
			strenghts.setAccessible(true);
			LOTRRecipeHandlerUtils.drinkStrenghts = (float[]) strenghts.get(null);
		} catch (Exception e) {
			System.err.print("Couldn't access drinkStrengths: ");
			e.printStackTrace();
		}
		return LOTRRecipeHandlerUtils.drinkStrenghts;
	}

	public static int getDrinkStrengthIndex(float strength) {
		int index = 0;
		float[] strengths = LOTRRecipeHandlerUtils.getDrinkStrenghts();
		for (int i = 0; i < strengths.length; i++)
			if (strengths[i] == strength) {
				index = i;
				break;
			}
		return index;
	}

	private static Boolean hasCraftTweaker = null;

	public static boolean hasCraftTweaker() {
		if (LOTRRecipeHandlerUtils.hasCraftTweaker == null) try {
			Class.forName("minetweaker.MineTweakerAPI");
			LOTRRecipeHandlerUtils.hasCraftTweaker = Boolean.TRUE;
		} catch (Exception e) {
			LOTRRecipeHandlerUtils.hasCraftTweaker = Boolean.FALSE;
		}
		return LOTRRecipeHandlerUtils.hasCraftTweaker;
	}

}
