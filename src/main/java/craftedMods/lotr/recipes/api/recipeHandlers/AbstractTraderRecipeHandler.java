/*******************************************************************************
 * Copyright (C) 2018 CraftedMods (see https://github.com/CraftedMods)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package craftedMods.lotr.recipes.api.recipeHandlers;

import java.util.*;

import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.ReflectionHelper;
import craftedMods.lotr.recipes.api.recipeHandlers.AbstractTraderRecipeHandler.TraderRecipe;
import craftedMods.lotr.recipes.api.utils.LOTRRecipeHandlerUtils;
import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.*;
import craftedMods.recipes.base.*;
import lotr.client.gui.LOTRGuiTrade;
import lotr.common.LOTRMod;
import lotr.common.enchant.LOTREnchantmentHelper;
import lotr.common.entity.npc.*;
import lotr.common.item.*;
import lotr.common.item.LOTRItemMug.Vessel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.*;
import net.minecraft.util.StatCollector;

public abstract class AbstractTraderRecipeHandler extends AbstractRecipeHandler<TraderRecipe> {

	private final String traderName;
	private final TraderRecipeHandlerRenderer renderer = new TraderRecipeHandlerRenderer();
	private final TraderRecipeHandlerRecipeViewer recipeViewer = new TraderRecipeHandlerRecipeViewer(this);
	private final LOTRTradeEntries itemsBought;
	private final LOTRTradeEntries itemsSold;

	private List<Vessel> vesselsSold;

	public static final int ALL_STRENGHTS_META = 9999;

	public AbstractTraderRecipeHandler(String prefix, String unlocalizedName, String faction, LOTRTradeEntries itemsBought, LOTRTradeEntries itemsSold) {
		super(prefix + (faction == null ? "" : faction + ".") + unlocalizedName);
		this.traderName = unlocalizedName;
		this.itemsBought = itemsBought;
		this.itemsSold = itemsSold;
	}

	@Override
	public void onPreLoad(RecipeHandlerConfiguration config, Logger logger) {
		super.onPreLoad(config, logger);
		try {
			Vessel[] vesselsArray = ReflectionHelper.getPrivateValue(LOTRTradeEntries.class, this.itemsSold, "drinkVessels");
			if (vesselsArray != null) {
				this.vesselsSold = Arrays.asList(vesselsArray);
			}
		} catch (Exception e) {
			logger.error("Could not get private field value drinkVessels from LOTRTradeEntries via reflection", e);
		}
		if (this.vesselsSold == null) {
			this.vesselsSold = new ArrayList<>();
		}
	}

	@Override
	public Collection<TraderRecipe> getDynamicCraftingRecipes(ItemStack result) {
		Collection<TraderRecipe> recipes = new ArrayList<>();
		if (result.getItem() != LOTRMod.silverCoin) {
			for (LOTRTradeEntry entry : this.itemsSold.tradeEntries) {
				boolean add = false;
				int baseCost = entry.getCost();
				ItemStack baseItem = entry.createTradeItem();
				if (result.getItem() instanceof LOTRItemMug && LOTRItemMug.isItemFullDrink(result) && baseItem.getItem() instanceof LOTRItemMug
						&& LOTRItemMug.isItemFullDrink(baseItem) && result.getItem() == baseItem.getItem()
						&& LOTRPoisonedDrinks.isDrinkPoisoned(result) == LOTRPoisonedDrinks.isDrinkPoisoned(baseItem)
						&& (baseItem.getItemDamage() == AbstractTraderRecipeHandler.ALL_STRENGHTS_META
								|| LOTRItemMug.getFoodStrength(result) == LOTRItemMug.getFoodStrength(baseItem))) {
					Vessel vessel = LOTRItemMug.getVessel(result);
					if (this.vesselsSold.contains(vessel)) {
						add = true;
					}
				} else {
					add = baseItem.getItem() instanceof LOTRItemBarrel || result.getItem() instanceof LOTRItemBarrel
							? RecipeHandlerUtils.getInstance().areStacksSameType(baseItem, result)
							: RecipeHandlerUtils.getInstance().areStacksSameTypeForCrafting(baseItem, result);
				}
				if (add) {
					ItemStack toAdd = result.copy();
					toAdd.stackSize = baseItem.stackSize;
					if (toAdd.isItemStackDamageable()) {
						toAdd.setItemDamage(0);
					}
					recipes.add(new TraderRecipe(toAdd, AbstractTraderRecipeHandler.getMinTradeCost(toAdd, baseCost, true),
							AbstractTraderRecipeHandler.getMaxTradeCost(toAdd, baseCost, true), true));
				}
			}
		} else {
			recipes.addAll(getAllCraftingRecipes());
		}
		return recipes;
	}

	protected Collection<TraderRecipe> getAllCraftingRecipes() {// TODO cache in field
		Collection<TraderRecipe> recipes = new ArrayList<>();
		for (LOTRTradeEntry entry : this.itemsBought.tradeEntries) {
			// boolean add = false;
			int baseCost = entry.getCost();
			ItemStack baseItem = entry.createTradeItem();
			recipes.add(new TraderRecipe(baseItem, AbstractTraderRecipeHandler.getMinTradeCost(baseItem, baseCost, false),
					AbstractTraderRecipeHandler.getMaxTradeCost(baseItem, baseCost, false), false));
		}
		return recipes;
	}

	@Override
	public Collection<TraderRecipe> getDynamicUsageRecipes(ItemStack ingredient) {
		Collection<TraderRecipe> recipes = new ArrayList<>();
		if (ingredient.getItem() != LOTRMod.silverCoin) {
			for (LOTRTradeEntry entry : this.itemsBought.tradeEntries) {
				// boolean add = false;
				int baseCost = entry.getCost();
				ItemStack baseItem = entry.createTradeItem();
				if (RecipeHandlerUtils.getInstance().areStacksSameTypeForCrafting(baseItem, ingredient)) {
					ItemStack toAdd = ingredient.copy();
					toAdd.stackSize = baseItem.stackSize;
					if (toAdd.isItemStackDamageable()) {
						toAdd.setItemDamage(0);// TODO: Fix durability (permutation is set in plugin recipe handler)
					}
					recipes.add(new TraderRecipe(toAdd, AbstractTraderRecipeHandler.getMinTradeCost(toAdd, baseCost, false),
							AbstractTraderRecipeHandler.getMaxTradeCost(toAdd, baseCost, false), false));
				}
			}
		} else {
			recipes.addAll(getAllUsageRecipes());
		}
		return recipes;

	}

	protected Collection<TraderRecipe> getAllUsageRecipes() {// TODO cache
		Collection<TraderRecipe> recipes = new ArrayList<>();
		for (LOTRTradeEntry entry : this.itemsSold.tradeEntries) {
			// boolean add = false;
			int baseCost = entry.getCost();
			ItemStack baseItem = entry.createTradeItem();
			if (baseItem.getItem() instanceof LOTRItemMug && LOTRItemMug.isItemFullDrink(baseItem)
					&& baseItem.getItemDamage() == AbstractTraderRecipeHandler.ALL_STRENGHTS_META) {
				for (float strength : LOTRRecipeHandlerUtils.getDrinkStrenghts()) {
					baseItem = baseItem.copy();
					LOTRItemMug.setStrengthMeta(baseItem, LOTRRecipeHandlerUtils.getDrinkStrengthIndex(strength));
					recipes.add(new TraderRecipe(baseItem, AbstractTraderRecipeHandler.getMinTradeCost(baseItem, baseCost, true),
							AbstractTraderRecipeHandler.getMaxTradeCost(baseItem, baseCost, true), true));
				}
			} else {
				recipes.add(new TraderRecipe(baseItem, AbstractTraderRecipeHandler.getMinTradeCost(baseItem, baseCost, true),
						AbstractTraderRecipeHandler.getMaxTradeCost(baseItem, baseCost, true), true));
			}
		}
		return recipes;
	}

	@Override
	public String getDisplayName() {
		return StatCollector.translateToLocal("entity.lotr." + this.traderName + ".name");
	}

	@Override
	public List<RecipeItemSlot> getSlotsForRecipeItems(TraderRecipe recipe, EnumRecipeItemRole role) {
		List<RecipeItemSlot> slots = new ArrayList<>();
		if (role == EnumRecipeItemRole.INGREDIENT) {
			if (recipe.isSold()) {
				slots.add(this.createRecipeItemSlot(30, 24));
			}
			slots.add(this.createRecipeItemSlot(43 + 15, 24));
		} else {
			slots.add(this.createRecipeItemSlot(96 + 15, 24));
			if (!recipe.isSold()) {
				slots.add(this.createRecipeItemSlot(124 + 15, 24));
			}
		}
		return slots;
	}

	@Override
	public int getRecipesPerPage() {
		return 2;
	}

	@Override
	@SuppressWarnings("unchecked")
	public TraderRecipeHandlerRenderer getRenderer() {
		return this.renderer;
	}

	@Override
	public RecipeHandlerRecipeViewer<TraderRecipe> getRecipeViewer() {
		return this.recipeViewer;
	}

	public static int getMinTradeCost(ItemStack itemStack, int baseCost, boolean sold) {
		return AbstractTraderRecipeHandler.getTradeCost(itemStack, baseCost, sold, 0.75f);
	}

	public static int getMaxTradeCost(ItemStack itemStack, int baseCost, boolean sold) {
		return AbstractTraderRecipeHandler.getTradeCost(itemStack, baseCost, sold, 1.25f);
	}

	public static int getTradeCost(ItemStack itemStack, int baseCost, boolean sold, float probabilityFactor) {
		float tradeCost = baseCost;
		Item item = itemStack.getItem();
		if (item instanceof LOTRItemMug) {
			LOTRItemMug mug = (LOTRItemMug) item;
			if (mug.isBrewable) {
				tradeCost *= LOTRItemMug.getFoodStrength(itemStack);
			}
			if (LOTRItemMug.isItemFullDrink(itemStack)) {
				Vessel vessel = LOTRItemMug.getVessel(itemStack);
				if (vessel != null) {
					tradeCost += vessel.extraPrice;
				}
			}
		}

		// Vessel[] drinkVessels = NeiLotrReflection.getDrinkVessels(entries);
		// if (drinkVessels != null && LOTRItemMug.isItemFullDrink((ItemStack) tradeItem)) {
		// LOTRItemMug.Vessel v = drinkVessels[random.nextInt(this.drinkVessels.length)];
		// LOTRItemMug.setVessel((ItemStack) tradeItem, (LOTRItemMug.Vessel) v, (boolean) true);
		// tradeCost += (float) v.extraPrice;
		// }

		// if (LOTRConfig.enchantingLOTR)
		// {
		// if (tradeType == TradeType.BUY)
		// {
		// boolean skilful = random.nextInt(3) == 0;
		// LOTREnchantmentHelper.applyRandomEnchantments(tradeItem, random, skilful, false);
		// tradeCost *= LOTREnchantmentHelper.calcTradeValueFactor(tradeItem);
		// }
		// }

		if (sold) {
			tradeCost *= LOTREnchantmentHelper.calcTradeValueFactor(itemStack);
		}

		tradeCost *= probabilityFactor;

		return Math.max(Math.round(Math.max(tradeCost, 1.0F)), 1);
	}

	public class TraderRecipe extends AbstractRecipe {

		private final int minPrice;
		private final int maxPrice;
		private final boolean sold;

		public TraderRecipe(ItemStack stack, int minPrice, int maxPrice, boolean sold) {
			if (sold) {
				this.add(new ItemStack(LOTRMod.silverCoin, minPrice), this.ingredients);
				this.add(new ItemStack(LOTRMod.silverCoin, maxPrice), this.ingredients);
				this.add(stack, this.results);
			} else {
				this.add(new ItemStack(LOTRMod.silverCoin, minPrice), this.results);
				this.add(new ItemStack(LOTRMod.silverCoin, maxPrice), this.results);
				this.add(stack, this.ingredients);
			}
			this.minPrice = minPrice;
			this.maxPrice = maxPrice;
			this.sold = sold;
			this.generatePermutations();
		}

		@Override
		public boolean consumes(ItemStack ingredient) {
			return this.sold && ingredient.getItem() instanceof LOTRItemCoin ? true : super.consumes(ingredient);
		}

		@Override
		public boolean produces(ItemStack result) {
			return !this.sold && result.getItem() instanceof LOTRItemCoin ? true : super.produces(result);
		}

		public int getMinPrice() {
			return this.minPrice;
		}

		public int getMaxPrice() {
			return this.maxPrice;
		}

		public boolean isSold() {
			return this.sold;
		}

		@Override
		public ItemStack getIngredientReplacement(ItemStack defaultReplacement) {
			ItemStack ret = defaultReplacement.copy();
			if (ret.isItemStackDamageable()) {
				ret.setItemDamage(0);
			}
			return ret;
		}

		@Override
		public ItemStack getResultReplacement(ItemStack defaultReplacement) {
			ItemStack ret = defaultReplacement.copy();
			if (ret.isItemStackDamageable()) {
				ret.setItemDamage(0);
			}
			return ret;
		}

	}

	public class TraderRecipeHandlerRenderer implements RecipeHandlerRenderer<AbstractTraderRecipeHandler, TraderRecipe> {

		@Override
		public void renderBackground(AbstractTraderRecipeHandler handler, TraderRecipe recipe, int cycleticks) {
			GL11.glPushMatrix();
			GL11.glTranslatef(15, 0, 0);

			RecipeHandlerRendererUtils.getInstance().bindTexture(RecipeHandlerRenderer.DEFAULT_GUI_TEXTURE);
			RecipeHandlerRendererUtils.getInstance().drawTexturedRectangle(42, 19, 65, 30, 80, 26);
			RecipeHandlerRendererUtils.getInstance().drawRectangle(42, 13, 18, 10, 0xFFC6C6C6);
			RecipeHandlerRendererUtils.getInstance().drawRectangle(42, 41, 18, 4, 0xFFC6C6C6);
			RecipeHandlerRendererUtils.getInstance().drawRectangle(96, 19, 26, 26, 0xFFC6C6C6);
			RecipeHandlerRendererUtils.getInstance().drawTexturedRectangle(95, 23, 65, 34, 18, 18);
			if (!recipe.isSold()) {
				RecipeHandlerRendererUtils.getInstance().drawTexturedRectangle(123, 23, 65, 34, 18, 18);
			}
			if (recipe.isSold()) {
				RecipeHandlerRendererUtils.getInstance().drawTexturedRectangle(14, 23, 65, 34, 18, 18);
			}

			GL11.glPopMatrix();
		}

		@Override
		public void renderForeground(AbstractTraderRecipeHandler handler, TraderRecipe recipe, int cycleticks) {
			GL11.glPushMatrix();
			GL11.glTranslatef(15, 0, 0);

			int offsetX = recipe.isSold() ? 0 : 81;
			RecipeHandlerRendererUtils.getInstance().drawText(StatCollector.translateToLocal("neiLotr.handler.trader.minLabel"), offsetX + 15, 14, 4210752,
					false);
			RecipeHandlerRendererUtils.getInstance().drawText(StatCollector.translateToLocal("neiLotr.handler.trader.maxLabel"), offsetX + 42, 14, 4210752,
					false);

			GL11.glPopMatrix();
		}

	}

	public class TraderRecipeHandlerRecipeViewer extends AbstractRecipeViewer<TraderRecipe, AbstractTraderRecipeHandler> {

		private Collection<Class<? extends GuiContainer>> supportedGuiClasses = new ArrayList<>();

		public TraderRecipeHandlerRecipeViewer(AbstractTraderRecipeHandler handler) {
			super(handler);
			supportedGuiClasses.addAll(RECIPE_HANDLER_GUIS);
			supportedGuiClasses.add(LOTRGuiTrade.class);
		}

		@Override
		public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses() {
			return supportedGuiClasses;
		}

		@Override
		public boolean isGuiContainerSupported(GuiContainer container) {
			if (container instanceof LOTRGuiTrade) {
				LOTRGuiTrade tradeGui = (LOTRGuiTrade) container;
				if (!handler.traderName.equals(LOTRRecipeHandlerUtils.getUnlocalizedEntityName(tradeGui.theEntity.getClass()))) return false;
			}
			return true;
		}

		@Override
		public Collection<TraderRecipe> getAllRecipes() {
			Collection<TraderRecipe> recipes = handler.getAllCraftingRecipes();
			recipes.addAll(handler.getAllUsageRecipes());
			return recipes;
		}

		@Override
		public int getOffsetX(Class<? extends GuiContainer> guiClass) {
			return guiClass == LOTRGuiTrade.class ? 144 : super.getOffsetX(guiClass);
		}

		@Override
		public int getOffsetY(Class<? extends GuiContainer> guiClass) {
			return guiClass == LOTRGuiTrade.class ? 130 : super.getOffsetY(guiClass);
		}

		@Override
		public String getButtonTooltip(Class<? extends GuiContainer> guiClass) {
			return StatCollector.translateToLocal("neiLotr.handler.trader.recipeViewer.tooltip");
		}

	}

}
