package craftedMods.lotr.recipes.internal.recipeHandlers;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import craftedMods.lotr.recipes.api.utils.LOTRRecipeHandlerUtils;
import craftedMods.recipes.api.*;
import craftedMods.recipes.base.*;
import lotr.common.item.LOTRPoisonedDrinks;
import lotr.common.recipe.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

@RegisteredHandler
public class LOTRVanillaCraftingTableSupportHandler implements VanillaCraftingTableRecipeHandlerSupport {

	@Override
	public Pair<AbstractRecipe, Boolean> undefinedRecipeTypeFound(IRecipe recipe) {
		if (recipe instanceof LOTRRecipesPoisonDrinks) return Pair.of(null, true);
		if (recipe instanceof LOTRRecipePoisonWeapon) return Pair.of(LOTRRecipeHandlerUtils.processPoisonWeaponRecipe((LOTRRecipePoisonWeapon) recipe), false);
		return null;
	}

	@Override
	public int getComplicatedStaticRecipeDepth() {
		return 1;
	}

	@Override
	public AbstractRecipe loadComplicatedStaticRecipe(ItemStack... stacks) {
		ItemStack stack = stacks[0];
		PoisonedDrinkRecipe recipe = null;
		if (LOTRPoisonedDrinks.canPoison(stack)) {
			List<Object> ingredients = new ArrayList<>();
			ingredients.add(stack.copy());
			ingredients.add(LOTRRecipeHandlerUtils.getPoison());
			ItemStack result = stack.copy();
			LOTRPoisonedDrinks.setDrinkPoisoned(result, true);
			recipe = new PoisonedDrinkRecipe(ingredients, result);
		}
		return recipe;
	}

	@Override
	public boolean matches(ItemStack stack1, ItemStack stack2) {
		return LOTRPoisonedDrinks.isDrinkPoisoned(stack1) == LOTRPoisonedDrinks.isDrinkPoisoned(stack2);
	}

	public class PoisonedDrinkRecipe extends ShapelessRecipe {

		public PoisonedDrinkRecipe(List<?> ingredients, ItemStack result) {
			super(ingredients, result);
		}

		@Override
		public boolean produces(ItemStack result) {
			return LOTRPoisonedDrinks.isDrinkPoisoned(result) ? super.produces(result) : false;
		}

		@Override
		public boolean consumes(ItemStack ingredient) {
			return !LOTRPoisonedDrinks.isDrinkPoisoned(ingredient) ? super.consumes(ingredient) : false;
		}

	}

}
