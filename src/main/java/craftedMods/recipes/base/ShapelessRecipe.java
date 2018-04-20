package craftedMods.recipes.base;

import java.util.Collection;

import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessRecipe extends AbstractRecipe {

	public ShapelessRecipe(ShapelessOreRecipe recipe) {
		this(recipe.getInput(), recipe.getRecipeOutput());
	}

	public ShapelessRecipe(ShapelessRecipes recipe) {
		this(recipe.recipeItems, recipe.getRecipeOutput());
	}

	public ShapelessRecipe(Collection<?> ingredients, ItemStack result) {
		for (Object ingredient : ingredients)
			if (ingredient != null) this.ingredients.add(this.createItemStackSet(RecipeHandlerUtils.getInstance().extractRecipeItems(ingredient)));
		this.add(result, this.results);
	}

	public ShapelessRecipe(Object ingredient, ItemStack result) {
		this.addAll(RecipeHandlerUtils.getInstance().extractRecipeItems(ingredient), this.ingredients);
		this.add(result, this.results);
	}

}
