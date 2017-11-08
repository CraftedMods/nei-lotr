package craftedMods.neiLotr.handlers.craftingTable;

import java.util.*;

import codechicken.nei.recipe.TemplateRecipeHandler;
import craftedMods.neiLotr.handlers.template.*;
import craftedMods.neiLotr.util.NeiLotrUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BasicCTShapedRecipeHandler extends ExtendedShapedRecipeHandler {

	private Class<? extends GuiContainer> guiClass;
	private String unlocalizedName;

	public BasicCTShapedRecipeHandler() {
		super();
	}

	public BasicCTShapedRecipeHandler(long id, Class<? extends GuiContainer> guiClass, String unlocalizedName) {
		super(id);
		this.guiClass = guiClass;
		this.unlocalizedName = unlocalizedName;
	}

	@Override
	public String getGuiTexture() {
		return NeiLotrUtil.GUI_TEXTURE_PATH_DEFAULT_CRAFTING;
	}

	@Override
	public int recipiesPerPage() {
		return NeiLotrUtil.RECIPIES_PER_PAGE;
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return guiClass;
	}

	@Override
	public String getRecipeName() {
		return NeiLotrUtil.getCTRecipeName(unlocalizedName);
	}

	@Override
	protected void transferImportantData(ExtendedShapedRecipeHandler newInstance) {
		super.transferImportantData(newInstance);
		BasicCTShapedRecipeHandler handler = (BasicCTShapedRecipeHandler) newInstance;
		handler.guiClass = this.guiClass;
		handler.unlocalizedName = this.unlocalizedName;
	}
}