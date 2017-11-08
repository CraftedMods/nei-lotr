package craftedMods.neiLotr.handlers.craftingTable.regular;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import craftedMods.neiLotr.handlers.template.ExtendedShapelessRecipeHandler;
import lotr.common.LOTRMod;
import lotr.common.recipe.LOTRRecipePoisonWeapon;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

public class PoisonedWeaponRecipeHandler extends ExtendedShapelessRecipeHandler {

	public PoisonedWeaponRecipeHandler() {
		super();
	}

	public PoisonedWeaponRecipeHandler(long id) {
		super(id);
	}

	public static List<IRecipe> getPoisonedWeaponRecipes() {
		List<IRecipe> ret = new ArrayList<>();
		CraftingManager.getInstance().getRecipeList().forEach(recipe -> {
			if (recipe instanceof LOTRRecipePoisonWeapon) {
				ret.add((IRecipe) recipe);
			}
		});
		return ret;
	}

	@Override
	public CachedRecipe getCachedRecipe(IRecipe recipe) {
		CachedRecipe ret = null;
		if (recipe instanceof LOTRRecipePoisonWeapon) {
			LOTRRecipePoisonWeapon poisonRecipe = (LOTRRecipePoisonWeapon) recipe;
			ret = new ExtendedCachedShapelessRecipe(Arrays.asList(poisonRecipe.getInputItem(), LOTRMod.bottlePoison),
					Arrays.asList(poisonRecipe.func_77571_b()));
		}
		return ret;
	}
}
