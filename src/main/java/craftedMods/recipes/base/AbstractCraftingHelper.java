package craftedMods.recipes.base;

import java.util.Collection;

import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

public abstract class AbstractCraftingHelper<T extends Recipe> implements RecipeHandlerCraftingHelper<T> {

	@Override
	public boolean matches(ItemStack stack1, ItemStack stack2) {
		return RecipeHandlerUtils.getInstance().areStacksSameTypeForCrafting(stack1, stack2);
	}

}
