package craftedMods.recipes.base;

import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import net.minecraft.item.ItemStack;

public abstract class AbstractCraftingHelper<T extends Recipe> implements RecipeHandlerCraftingHelper<T> {

	@Override
	public boolean matches(ItemStack stack1, ItemStack stack2) {
		return RecipeHandlerUtils.getInstance().areStacksSameTypeForCrafting(stack1, stack2);
	}

}
