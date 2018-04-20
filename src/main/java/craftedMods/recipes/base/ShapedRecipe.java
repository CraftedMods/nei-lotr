package craftedMods.recipes.base;

import codechicken.core.ReflectionManager;
import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedRecipe extends AbstractRecipe {

	private final int width;
	private final int height;

	public ShapedRecipe(ShapedOreRecipe recipe) throws Exception {
		this(ReflectionManager.getField(ShapedOreRecipe.class, Integer.class, recipe, 4).intValue(),
				ReflectionManager.getField(ShapedOreRecipe.class, Integer.class, recipe, 5).intValue(), recipe.getInput(), recipe.getRecipeOutput());
	}

	public ShapedRecipe(ShapedRecipes recipe) {
		this(recipe.recipeWidth, recipe.recipeHeight, recipe.recipeItems, recipe.getRecipeOutput());
	}

	public ShapedRecipe(int width, int height, Object[] ingredients, ItemStack result) {
		this.width = width;
		this.height = height;
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				Object ingred = ingredients[y * width + x];
				this.ingredients.add(ingred != null ? this.createItemStackSet(RecipeHandlerUtils.getInstance().extractRecipeItems(ingred)) : null);
			}
		this.results.add(this.createItemStackSet(result));
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

}
