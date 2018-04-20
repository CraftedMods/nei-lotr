package craftedMods.recipes.api;

import java.util.Collection;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

public interface RecipeHandlerCraftingHelper<T extends Recipe> {

	public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses(T recipe);

	public int getOffsetX(Class<? extends GuiContainer> guiClass, T recipe);

	public int getOffsetY(Class<? extends GuiContainer> guiClass, T recipe);

	public boolean matches(ItemStack stack1, ItemStack stack2);

}
