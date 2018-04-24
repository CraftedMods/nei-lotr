package craftedMods.lotr.recipes.recipeHandlers;

import java.lang.reflect.Field;
import java.util.*;

import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import craftedMods.recipes.base.*;
import lotr.common.recipe.LOTRRecipePoisonWeapon;
import net.minecraft.item.crafting.IRecipe;

public class AbstractLOTRCraftingTableRecipeHandler extends CraftingGridRecipeHandler {

	protected AbstractLOTRCraftingTableRecipeHandler(String unlocalizedName) {
		super(unlocalizedName);
	}

	protected AbstractLOTRCraftingTableRecipeHandler(String unlocalizedName, Collection<IRecipe> recipes) {
		super(unlocalizedName);
		this.recipes.addAll(recipes);
	}

	@Override
	protected void undefinedRecipeTypeFound(IRecipe recipe, Collection<AbstractRecipe> container) {
		if (recipe instanceof LOTRRecipePoisonWeapon) {
			LOTRRecipePoisonWeapon poisonRecipe = (LOTRRecipePoisonWeapon) recipe;
			List<Object> ingredients = new ArrayList<>();
			ingredients.add(poisonRecipe.getInputItem());
			try {
				Field catalystField = LOTRRecipePoisonWeapon.class.getDeclaredField("catalystObj");
				catalystField.setAccessible(true);
				ingredients.add(RecipeHandlerUtils.getInstance().extractRecipeItems(catalystField.get(poisonRecipe)));
				container.add(new ShapelessRecipe(ingredients, recipe.getRecipeOutput()));
			} catch (Exception e) {
				this.logger.error("Couldn't load poisoned weapon recipe: ", e);
			}
			return;
		}
		super.undefinedRecipeTypeFound(recipe, container);
	}

}
