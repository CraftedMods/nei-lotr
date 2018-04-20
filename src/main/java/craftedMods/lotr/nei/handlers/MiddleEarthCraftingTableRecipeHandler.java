package craftedMods.lotr.nei.handlers;

import java.util.*;

import craftedMods.recipes.api.RecipeHandlerCraftingHelper;
import craftedMods.recipes.base.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;

public class MiddleEarthCraftingTableRecipeHandler extends AbstractLOTRCraftingTableRecipeHandler {

	private final MiddleEarthCraftingTableRecipeHandlerCraftingHelper craftingHelper;

	public static final String UNLOCALIZED_NAME_PREFIX = "middleEarthCrafting.";

	public MiddleEarthCraftingTableRecipeHandler(String unlocalizedName, Class<? extends GuiContainer> guiClass, Collection<IRecipe> recipes) {
		super(UNLOCALIZED_NAME_PREFIX + unlocalizedName, recipes);
		this.craftingHelper = new MiddleEarthCraftingTableRecipeHandlerCraftingHelper(guiClass);
	}

	@Override
	public String getDisplayName() {
		return StatCollector.translateToLocal("container.lotr.crafting." + this.getUnlocalizedName().replace(UNLOCALIZED_NAME_PREFIX, ""));
	}

	@Override
	public RecipeHandlerCraftingHelper getCraftingHelper() {
		return craftingHelper;
	}

	private class MiddleEarthCraftingTableRecipeHandlerCraftingHelper extends AbstractCraftingHelper<AbstractRecipe> {

		private final Collection<Class<? extends GuiContainer>> guiClass;

		public MiddleEarthCraftingTableRecipeHandlerCraftingHelper(Class<? extends GuiContainer> guiClass) {
			this.guiClass = Arrays.asList(guiClass);
		}

		@Override
		public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses(AbstractRecipe recipe) {
			return guiClass;
		}

		@Override
		public int getOffsetX(Class<? extends GuiContainer> guiClass, AbstractRecipe recipe) {
			return 5;
		}

		@Override
		public int getOffsetY(Class<? extends GuiContainer> guiClass, AbstractRecipe recipe) {
			return 11;
		}

	}

}
