package craftedMods.lotr.nei.handlers;

import java.util.*;

import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.RecipeHandlerRendererUtils;
import craftedMods.recipes.base.*;
import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityKebabStand;
import net.minecraft.item.ItemStack;

@RegisteredRecipeHandler
public class KebabRecipeHandler extends AbstractRecipeHandler<ShapelessRecipe> {

	private final KebabRecipeHandlerRenderer renderer = new KebabRecipeHandlerRenderer();
	private final LOTRTileEntityKebabStand kebabStandDummy = new LOTRTileEntityKebabStand();

	public KebabRecipeHandler() {
		super("kebab");
	}

	@Override
	public String getDisplayName() {
		return LOTRMod.kebabStand.getLocalizedName();
	}

	@Override
	public int getComplicatedStaticRecipeDepth() {
		return 1;
	}

	@Override
	public ShapelessRecipe loadComplicatedStaticRecipe(ItemStack... stacks) {
		ShapelessRecipe recipe = null;
		if (this.kebabStandDummy.isMeat(stacks[0])) recipe = new ShapelessRecipe(Arrays.asList(stacks[0]), new ItemStack(LOTRMod.kebab));
		return recipe;
	}

	@Override
	public List<RecipeItemSlot> getSlotsForRecipeItems(ShapelessRecipe recipe, EnumRecipeItemRole role) {
		return Arrays.asList(role == EnumRecipeItemRole.INGREDIENT ? this.createRecipeItemSlot(43, 24) : this.createRecipeItemSlot(101, 24));
	}

	@Override
	@SuppressWarnings("unchecked")
	public KebabRecipeHandlerRenderer getRenderer() {
		return this.renderer;
	}

	public class KebabRecipeHandlerRenderer implements RecipeHandlerRenderer<KebabRecipeHandler, ShapelessRecipe> {

		@Override
		public void renderBackground(KebabRecipeHandler handler, ShapelessRecipe recipe, int cycleticks) {
			RecipeHandlerRendererUtils.getInstance().bindTexture(RecipeHandlerRenderer.DEFAULT_GUI_TEXTURE);
			RecipeHandlerRendererUtils.getInstance().drawTexturedRectangle(42, 19, 65, 30, 80, 26);
			RecipeHandlerRendererUtils.getInstance().drawRectangle(42, 13, 18, 10, 0xFFC6C6C6);
			RecipeHandlerRendererUtils.getInstance().drawRectangle(42, 41, 18, 4, 0xFFC6C6C6);
		}

		@Override
		public void renderForeground(KebabRecipeHandler handler, ShapelessRecipe recipe, int cycleticks) {}

	}
}
