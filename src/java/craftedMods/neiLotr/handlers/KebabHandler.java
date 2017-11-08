package craftedMods.neiLotr.handlers;

import java.util.Iterator;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import craftedMods.neiLotr.util.NeiLotrUtil;
import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityKebabStand;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class KebabHandler extends TemplateRecipeHandler {

	private LOTRTileEntityKebabStand kebabStand;

	public KebabHandler(LOTRTileEntityKebabStand kebabStand) {
		this.kebabStand = kebabStand;
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("tile.lotr:kebabStand.name");
	}

	@Override
	public String getGuiTexture() {
		return NeiLotrUtil.GUI_TEXTURE_PATH_DEFAULT_CRAFTING;
	}
	
	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return GuiCrafting.class;
	}

	@Override
	public int recipiesPerPage() {
		return 2;
	}
	
	@Override
	public KebabHandler newInstance() {
		return new KebabHandler(kebabStand);
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("item")) {
			loadCraftingRecipes((ItemStack) results[0]);
		}
	}
	
	@Override
	public void drawForeground(int recipe) {
		super.drawForeground(recipe);
		GuiDraw.drawRect(24,5, 54, 18, 0xFFC6C6C6);
		GuiDraw.drawRect(24,41, 54, 18, 0xFFC6C6C6);
		GuiDraw.drawRect(24,23, 18, 18, 0xFFC6C6C6);
		GuiDraw.drawRect(60,23, 18, 18, 0xFFC6C6C6);
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		result.stackSize = 1;
		if (NEIClientUtils.areStacksSameTypeCrafting(result, new ItemStack(LOTRMod.kebab, 1))) {
			FMLControlledNamespacedRegistry<Item> items = GameData.getItemRegistry();
			Iterator<Item> it = items.iterator();
			ItemStack stack;
			while (it.hasNext()) {
				stack = new ItemStack(it.next(), 1);
				if (kebabStand.isMeat(stack)) {
					arecipes.add(new CachedKebabRecipe(stack));
				}
			}
		}
	}

	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients) {
		if (inputId.equals("item")) {
			loadUsageRecipes((ItemStack) ingredients[0]);
		}
	}

	public void loadUsageRecipes(ItemStack ingredient) {
		if (kebabStand.isMeat(ingredient)) {
			ingredient.stackSize = 1;
			arecipes.add(new CachedKebabRecipe(ingredient));
		}
	}

	private class CachedKebabRecipe extends CachedRecipe {

		private PositionedStack result;
		private PositionedStack ingredient;

		public CachedKebabRecipe(ItemStack ingredient) {
			this.result = new PositionedStack(new ItemStack(LOTRMod.kebab, 1), 119, 24);
			this.ingredient = new PositionedStack(ingredient, 43, 24);
		}

		@Override
		public PositionedStack getResult() {
			return result;
		}

		@Override
		public PositionedStack getIngredient() {
			return ingredient;
		}

	}

}
