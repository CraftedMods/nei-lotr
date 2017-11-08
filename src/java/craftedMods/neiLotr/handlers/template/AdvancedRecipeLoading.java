package craftedMods.neiLotr.handlers.template;

import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public interface AdvancedRecipeLoading {
	
	public long getHandlerId();

	public default void loadDynamicCraftingRecipes(ItemStack result) {
	}

	public default void loadDynamicUsageRecipes(ItemStack result) {
	}

	public CachedRecipe getCachedRecipe(IRecipe recipe);
	
	public CachedRecipe newRecipeInstance(CachedRecipe oldInstance);
}
