package craftedMods.neiLotr.handlers.template;

import java.util.*;

import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import net.minecraft.item.ItemStack;

public abstract class AbstractRecipeSubhandler implements RecipeSubhandler {

	protected TemplateRecipeHandler parent;
	protected List<CachedRecipe> arecipes;

	protected AbstractRecipeSubhandler(TemplateRecipeHandler parent) {
		this.parent = parent;
		this.arecipes = new ArrayList<>();
	}

	@Override
	public List<CachedRecipe> loadCraftingRecipes(ItemStack result) {
		this.arecipes.clear();
		this.loadCraftingRecipeHandler(result);
		return arecipes;
	}

	@Override
	public List<CachedRecipe> loadUsageRecipes(ItemStack ingredient) {
		this.arecipes.clear();
		this.loadUsageRecipeHandler(ingredient);
		return arecipes;
	}
	
	protected abstract void loadCraftingRecipeHandler(ItemStack result);

	protected abstract void loadUsageRecipeHandler(ItemStack ingredient);

}
