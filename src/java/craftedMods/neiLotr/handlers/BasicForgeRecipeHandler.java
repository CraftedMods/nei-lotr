package craftedMods.neiLotr.handlers;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.ItemList;
import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.FurnaceRecipeHandler;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import craftedMods.neiLotr.handlers.template.ExtendedShapedRecipeHandler;
import craftedMods.neiLotr.handlers.template.ExtendedShapelessRecipeHandler;
import craftedMods.neiLotr.util.NeiLotrReflection;
import craftedMods.neiLotr.util.NeiLotrUtil;
import lotr.client.gui.LOTRGuiAlloyForge;
import lotr.common.LOTRMod;
import lotr.common.block.LOTRBlockOre;
import lotr.common.tileentity.LOTRTileEntityAlloyForgeBase;
import lotr.common.tileentity.LOTRTileEntityDwarvenForge;
import lotr.common.tileentity.LOTRTileEntityElvenForge;
import lotr.common.tileentity.LOTRTileEntityOrcForge;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class BasicForgeRecipeHandler<T extends LOTRTileEntityAlloyForgeBase> extends ExtendedShapelessRecipeHandler {

	private T alloyForgeDummy;
	private List<CachedRecipe> recipes = new ArrayList<>();

	public BasicForgeRecipeHandler() {
		super();
	}

	public BasicForgeRecipeHandler(long id, T dummy) {
		super(id);
		this.alloyForgeDummy = dummy;
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	@Override
	public String getRecipeName() {
		return alloyForgeDummy.getForgeName();
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return LOTRGuiAlloyForge.class;
	}

	@Override
	public String getGuiTexture() {
		return "lotr:gui/forge.png";
	}

	public List<CachedRecipe> getRecipes() {
		return recipes;
	}

	public T getAlloyForgeDummy() {
		return alloyForgeDummy;
	}

	@Override
	public void drawBackground(int recipe) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect(0, 0, 5, 12, 166, 138);
	}

	@Override
	public void drawExtras(int recipe) {
		drawProgressBar(75, 100, 176, 0, 14, 14, 48, 7);
		drawProgressBar(75, 45, 176, 14, 24, 25, 48, 1);
	}

	@Override
	protected void transferImportantData(ExtendedShapedRecipeHandler newInstance) {
		super.transferImportantData(newInstance);
		BasicForgeRecipeHandler<T> handler = (BasicForgeRecipeHandler<T>) newInstance;
		handler.alloyForgeDummy = this.alloyForgeDummy;
		handler.recipes = recipes;
	}

	@Override
	public CachedRecipe newRecipeInstance(CachedRecipe oldInstance) {
		CachedRecipe ret = null;
		if (oldInstance instanceof BasicForgeRecipeHandler.CachedForgeRecipe) {
			CachedForgeRecipe recipe = (BasicForgeRecipeHandler<T>.CachedForgeRecipe) oldInstance;
			ret = new CachedForgeRecipe(recipe);
		}
		return ret;
	}

	public class CachedForgeRecipe extends ExtendedCachedShapelessRecipe {

		private int fuelX = 75;
		private int fuelY = 117;

		public CachedForgeRecipe(CachedForgeRecipe old) {
			super(old);
		}

		public CachedForgeRecipe(List<ItemStack> alloyItems, List<ItemStack> forgeItems, ItemStack resultItem) {
			if (alloyItems != null) {
				for (ItemStack tmpStackA : alloyItems) {
					tmpStackA.stackSize = 1;
				}
			}
			
			for (ItemStack tmpStackF : forgeItems) {
				tmpStackF.stackSize = 1;
			}

			if (alloyItems != null) {
				ingredients.add(new PositionedStack(alloyItems, 48, 9, true));
				ingredients.add(new PositionedStack(alloyItems, 66, 9, true));
				ingredients.add(new PositionedStack(alloyItems, 84, 9, true));
				ingredients.add(new PositionedStack(alloyItems, 102, 9, true));
			}

			ingredients.add(new PositionedStack(forgeItems, 48, 27, true));
			ingredients.add(new PositionedStack(forgeItems, 66, 27, true));
			ingredients.add(new PositionedStack(forgeItems, 84, 27, true));
			ingredients.add(new PositionedStack(forgeItems, 102, 27, true));

			this.results.add(new PositionedStack(resultItem, 48, 73));
			this.results.add(new PositionedStack(resultItem, 66, 73));
			this.results.add(new PositionedStack(resultItem, 84, 73));
			this.results.add(new PositionedStack(resultItem, 102, 73));
		}

		@Override
		public List<PositionedStack> getIngredients() {
			// System.out.println("ms");
			super.getIngredients().forEach(ingred -> {
			});
			return super.getIngredients();
		}

		@Override
		public List<PositionedStack> getOtherStacks() {
			ArrayList<PositionedStack> tmp = new ArrayList<PositionedStack>();
			PositionedStack tmpStack = FurnaceRecipeHandler.afuels
					.get((cycleticks / 48) % FurnaceRecipeHandler.afuels.size()).stack;
			tmpStack.relx = fuelX;
			tmpStack.rely = fuelY;

			tmp.add(tmpStack);

			tmp.add(results.get(1));
			tmp.add(results.get(2));
			tmp.add(results.get(3));

			return tmp;
		}
	}
}
