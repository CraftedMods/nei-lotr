package craftedMods.neiLotr.handlers.template;

import java.util.List;

import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import net.minecraft.item.ItemStack;

public interface RecipeSubhandler{
	
	public RecipeSubhandler newInstance(TemplateRecipeHandler parent);
	
	public List<CachedRecipe> loadCraftingRecipes(ItemStack result);

	public List<CachedRecipe> loadUsageRecipes(ItemStack ingredient);

}
