package craftedMods.neiLotr.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import craftedMods.neiLotr.NeiLotr;
import craftedMods.neiLotr.handlers.BasicForgeRecipeHandler.CachedForgeRecipe;
import craftedMods.neiLotr.handlers.template.ExtendedShapedRecipeHandler;
import craftedMods.neiLotr.handlers.template.ExtendedShapelessRecipeHandler;
import craftedMods.neiLotr.util.NeiLotrReflection;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityAngmarOrcTrader;
import lotr.common.entity.npc.LOTRTradeEntries;
import lotr.common.entity.npc.LOTRTradeEntry;
import lotr.common.item.LOTRItemMug;
import lotr.common.item.LOTRItemMug.Vessel;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class BasicTradeHandler extends ExtendedShapelessRecipeHandler {

	public static float MAX_TRADE_COST_FACTOR = 1.3f;
	public static float MIN_TRADE_COST_FACTOR = 0.7f;

	private List<CachedRecipe> recipes = new ArrayList<>();

	private String traderName;
	private String traderNameRaw;
	private LOTRTradeEntries entriesBuy;
	private LOTRTradeEntries entriesSell;

	private LOTRItemMug.Vessel[] vesselsBuy = null;
	private LOTRItemMug.Vessel[] vesselsSell = null;

	private static Random rand = new Random();

	public BasicTradeHandler() {
		super();
	}

	public BasicTradeHandler(long id, String traderNameRaw, LOTRTradeEntries entriesBuy, LOTRTradeEntries entriesSell) {
		super(id);
		this.traderName = StatCollector.translateToLocal(traderNameRaw);
		this.traderNameRaw = traderNameRaw;
		this.entriesBuy = entriesBuy;
		this.entriesSell = entriesSell;
		this.vesselsBuy = NeiLotrReflection.getDrinkVessels(entriesBuy);
		this.vesselsSell = NeiLotrReflection.getDrinkVessels(entriesSell);
	}

	public List<CachedRecipe> getRecipes() {
		recipes.clear();
		addTradeEntryRecipes(entriesBuy, false);
		addTradeEntryRecipes(entriesSell, true);
		return recipes;
	}

	private void addTradeEntryRecipes(LOTRTradeEntries entries, boolean buy) {
		for (LOTRTradeEntry entry : entries.tradeEntries) {
			if (LOTRItemMug.isItemFullDrink(entry.createTradeItem())) {
				addDrinkRecipes(entry, buy);
			} else {
				recipes.add(new CachedTradeRecipe(entry, buy));
			}
		}
	}

	private void addDrinkRecipes(LOTRTradeEntry entry, boolean buy) {
		LOTRItemMug.Vessel[] vessels = buy ? vesselsBuy : vesselsSell;
		if (vessels != null) {
			for (Vessel vessel : vessels) {
				ItemStack stack = entry.createTradeItem().copy();
				LOTRItemMug.setVessel(stack, vessel, false);
				float[] strenghts = NeiLotrReflection.getStrenghts();
				if (((LOTRItemMug) stack.getItem()).isBrewable) {
					for (int str = 0; str < strenghts.length; str++) {
						ItemStack drink = stack.copy();
						LOTRItemMug.setStrengthMeta(drink, str);
						recipes.add(new CachedTradeRecipe(new LOTRTradeEntry(drink, entry.getCost()), buy));
					}
				} else {
					recipes.add(new CachedTradeRecipe(new LOTRTradeEntry(stack, entry.getCost()), buy));
				}
			}
		}
	}

	private static float getTradeCostFromTradeEntry(LOTRTradeEntry entry) {
		ItemStack tradeItem = entry.createTradeItem();
		float baseCost = entry.getCost();

		if (tradeItem.getItem() instanceof LOTRItemMug && ((LOTRItemMug) tradeItem.getItem()).isBrewable
				&& tradeItem.getItemDamage() == -1) {
			baseCost *= LOTRItemMug.getFoodStrength(tradeItem);
		}

		if (LOTRItemMug.isItemFullDrink(tradeItem)) {
			LOTRItemMug.Vessel vessel = LOTRItemMug.getVessel(tradeItem);
			if (vessel != null) {
				baseCost += (float) vessel.extraPrice;
			}
		}
		return baseCost;
	}

	private static int getIntegerCost(float floatCost) {
		int cost = Math.round(floatCost);
		cost = Math.max(cost, 1);
		return cost;
	}

	private static int getTradeCost(LOTRTradeEntry entry, float factor) {
		float tradeCost = getTradeCostFromTradeEntry(entry);
		tradeCost *= factor;
		tradeCost = Math.max(tradeCost, 1.0f);
		return getIntegerCost(tradeCost);
	}

	public static int getRandomTradeCost(LOTRTradeEntry entry) {
		return getTradeCost(entry,
				(float) MathHelper.getRandomDoubleInRange((Random) rand, MIN_TRADE_COST_FACTOR, MAX_TRADE_COST_FACTOR));
	}

	public static int getMinTradeCost(LOTRTradeEntry entry) {
		return getTradeCost(entry, MIN_TRADE_COST_FACTOR);
	}

	public static int getMaxTradeCost(LOTRTradeEntry entry) {
		return getTradeCost(entry, MAX_TRADE_COST_FACTOR);
	}

	@Override
	public String getRecipeName() {
		return traderName;
	}

	@Override
	public int recipiesPerPage() {
		return 2;
	}

	@Override
	public String getGuiTexture() {
		return new ResourceLocation(NeiLotr.MODID + ":textures/gui/trade.png").toString();
	}

	@Override
	public void drawBackground(int recipe) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager()
				.bindTexture(new ResourceLocation(NeiLotr.MODID + ":textures/gui/trade.png"));
		GuiDraw.changeTexture(getGuiTexture());
		GuiDraw.drawTexturedModalRect((int) (176 / 2.0f - 153 / 2.0f), 10, 0, 16, 153, 34);
	}

	@Override
	public void drawForeground(int recipe) {
		super.drawForeground(recipe);
		CachedRecipe tmp = arecipes.get(recipe);
		if (tmp != null && tmp instanceof BasicTradeHandler.CachedTradeRecipe) {
			CachedTradeRecipe rec = (CachedTradeRecipe) tmp;
			if (!rec.isUsage()) {
				GuiDraw.fontRenderer.drawString("Min:", (int) (176 / 2.0f - 153 / 2.0f + 10), 10, 4210752);
				GuiDraw.fontRenderer.drawString("Max:", (int) (176 / 2.0f - 153 / 2.0f + 38), 10, 4210752);
				GuiDraw.fontRenderer.drawString("Sells:", (int) (176 / 2.0f - 153 / 2.0f + 98), 10, 4210752);
				GuiDraw.drawRect((int) (176 / 2.0f - 153 / 2.0f) + 126, 20, 18, 18, 0xFFC6C6C6);
			} else {
				GuiDraw.fontRenderer.drawString("Min:", (int) (176 / 2.0f - 153 / 2.0f + 98), 10, 4210752);
				GuiDraw.fontRenderer.drawString("Max:", (int) (176 / 2.0f - 153 / 2.0f + 126), 10, 4210752);
				GuiDraw.fontRenderer.drawString("Buys:", (int) (176 / 2.0f - 153 / 2.0f + 10), 10, 4210752);
				GuiDraw.drawRect((int) (176 / 2.0f - 153 / 2.0f) + 38, 20, 18, 18, 0xFFC6C6C6);
			}
		}
	}

	@Override
	protected void transferImportantData(ExtendedShapedRecipeHandler newInstance) {
		super.transferImportantData(newInstance);
		BasicTradeHandler handler = (BasicTradeHandler) newInstance;
		handler.recipes = recipes;
		handler.traderName = traderName;
		handler.traderNameRaw = traderNameRaw;
		handler.entriesBuy = entriesBuy;
		handler.entriesSell = entriesSell;
		handler.vesselsBuy = vesselsBuy;
		handler.vesselsSell = vesselsSell;
	}

	@Override
	public CachedRecipe newRecipeInstance(CachedRecipe oldInstance) {
		CachedRecipe ret = null;
		if (oldInstance instanceof BasicTradeHandler.CachedTradeRecipe) {
			CachedTradeRecipe recipe = (BasicTradeHandler.CachedTradeRecipe) oldInstance;
			ret = new CachedTradeRecipe(recipe);
		}
		return ret;
	}

	public class CachedTradeRecipe extends ExtendedShapelessRecipeHandler.ExtendedCachedShapelessRecipe {

		private LOTRTradeEntry entry;
		private boolean usage;

		public CachedTradeRecipe(CachedTradeRecipe oldInstance) {
			this(oldInstance.entry, oldInstance.usage);
		}

		public CachedTradeRecipe(LOTRTradeEntry entry, boolean usage) {
			this.entry = entry;
			this.usage = usage;
			if (!usage) {
				this.results.add(new PositionedStack(entry.createTradeItem(), 110, 21));
				this.ingredients
						.add(new PositionedStack(new ItemStack(LOTRMod.silverCoin, getMaxTradeCost(entry)), 50, 21));
				this.ingredients
						.add(new PositionedStack(new ItemStack(LOTRMod.silverCoin, getMinTradeCost(entry)), 22, 21));
			} else {
				this.ingredients.add(new PositionedStack(entry.createTradeItem(), 22, 21, true));
				this.results
						.add(new PositionedStack(new ItemStack(LOTRMod.silverCoin, getMaxTradeCost(entry)), 138, 21));
				this.others
						.add(new PositionedStack(new ItemStack(LOTRMod.silverCoin, getMinTradeCost(entry)), 110, 21));
			}
		}

		public boolean isUsage() {
			return usage;
		}
	}

}
