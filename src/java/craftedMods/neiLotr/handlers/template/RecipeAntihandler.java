package craftedMods.neiLotr.handlers.template;

import net.minecraft.item.ItemStack;

public interface RecipeAntihandler {
	
	public boolean stopLoadCraftingRecipes(ItemStack result);
	
	public boolean stopLoadUsageRecipes(ItemStack ingredient);

}
