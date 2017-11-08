package craftedMods.neiLotr.handlers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.FurnaceRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import lotr.client.gui.LOTRGuiHobbitOven;
import lotr.common.tileentity.LOTRTileEntityHobbitOven;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.StatCollector;

public class HobbitOvenHandler extends TemplateRecipeHandler {

	private LOTRTileEntityHobbitOven dummy;

	public HobbitOvenHandler(LOTRTileEntityHobbitOven dummy) {
		this.dummy = dummy;
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("container.lotr.hobbitOven");
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return LOTRGuiHobbitOven.class;
	}

	@Override
	public String getGuiTexture() {
		return "lotr:gui/oven.png";
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("item")) {
			loadCraftingRecipes((ItemStack) results[0]);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		result.stackSize = 1;
		if (dummy.isCookResultAcceptable(result)) {
			Map map = FurnaceRecipes.smelting().getSmeltingList();
			Iterator it = map.keySet().iterator();

			while (it.hasNext()) {
				ItemStack itemStack = (ItemStack) it.next();
				if (NEIClientUtils.areStacksSameTypeCrafting(FurnaceRecipes.smelting().getSmeltingResult(itemStack),
						result)) {
					arecipes.add(new CachedOvenRecipe(itemStack, result));
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
		ingredient.stackSize = 1;
		ItemStack tmp = FurnaceRecipes.smelting().getSmeltingResult(ingredient);
		if (tmp != null) {
			if (dummy.isCookResultAcceptable(tmp)) {
				arecipes.add(new CachedOvenRecipe(ingredient, tmp));
			}
		}
	}

	@Override
	public HobbitOvenHandler newInstance() {
		return new HobbitOvenHandler(dummy);
	}

	@Override
	public void drawBackground(int recipe) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 166, 120);
	}

	@Override
	public void drawExtras(int recipe) {
		drawProgressBar(75, 83, 176, 0, 14, 14, 48, 7);
		drawProgressBar(75, 29, 176, 14, 24, 25, 48, 1);
	}

	private class CachedOvenRecipe extends CachedRecipe {

		private ArrayList<PositionedStack> ingredients = new ArrayList<PositionedStack>();
		private ArrayList<PositionedStack> results = new ArrayList<PositionedStack>();;

		private int fuelX = 75;
		private int fuelY = 100;

		public CachedOvenRecipe(ItemStack ingredient, ItemStack result) {

			for (int i = 0; i < 9; i++) {
				ingredients.add(new PositionedStack(ingredient, 18 * i + 3, 10));
				results.add(new PositionedStack(result, 18 * i + 3, 56));
			}
		}

		public PositionedStack getResult() {
			return results.get(0);
		};

		@Override
		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(cycleticks / 48, ingredients);
		}

		@Override
		public List<PositionedStack> getOtherStacks() {
			ArrayList<PositionedStack> tmp = new ArrayList<PositionedStack>();

			PositionedStack tmpStack = FurnaceRecipeHandler.afuels
					.get((cycleticks / 48) % FurnaceRecipeHandler.afuels.size()).stack;
			tmpStack.relx = fuelX;
			tmpStack.rely = fuelY;

			tmp.add(tmpStack);

			tmp.addAll(1, results);

			return tmp;
		}
	}
}
