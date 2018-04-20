package craftedMods.recipes.provider;

import java.util.List;
import java.util.function.Supplier;

import org.apache.logging.log4j.Logger;

import codechicken.nei.api.API;
import codechicken.nei.recipe.*;
import craftedMods.recipes.api.*;
import craftedMods.utils.ClassDiscoverer;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class NEIIntegrationManager {

	private final NEIExtensionsConfiguration config;
	private final Logger logger;
	private final ClassDiscoverer discoverer;

	private RecipeHandlerManager recipeHandlerManager;

	public NEIIntegrationManager(NEIExtensionsConfiguration config, Logger logger) {
		this.config = config;
		this.logger = logger;
		this.discoverer = new ClassDiscoverer(logger);
	}

	public void preInit() {
		this.discoverer.registerClassToDiscover(RegisteredRecipeHandler.class, RecipeHandler.class);
		this.discoverer.registerClassToDiscover(RecipeHandlerFactory.class, Supplier.class);
		this.discoverer.discoverClassesAsync();
	}

	public void init(boolean useCachedRecipes) {
		try {
			long start = System.currentTimeMillis();

			this.recipeHandlerManager = new RecipeHandlerManager(this.config.getConfigFile(),
					this.discoverer.getDiscoveredClasses(this.config.getClassDiscovererThreadTimeout()));

			this.recipeHandlerManager.init(useCachedRecipes);

			this.logger.info("Initialized NEI config for LOTR Mod within " + (System.currentTimeMillis() - start) + " ms");
		} catch (Exception e) {
			this.logger.error("Couldn't initialize NEI config for LOTR Mod: ", e);
		}
	}

	public void load() {
		if (!this.config.isDisabled()) {

			long start = System.currentTimeMillis();

			// Remove recipe handlers
			if (this.config.isBrewingRecipeHandlerDisabled()) this.removeCraftingAndUsageHandler(BrewingRecipeHandler.class);

			this.removeCraftingAndUsageHandler(ShapedRecipeHandler.class);
			this.removeCraftingAndUsageHandler(ShapelessRecipeHandler.class);

			// Load registered handlers
			this.recipeHandlerManager.getRecipeHandlers().forEach((unlocalizedName, handler) -> this.loadHandler(new PluginRecipeHandler<>(handler)));

			// Item hiding
			if (this.config.isHideTechnicalBlocks()) this.hideItems();

			// Override names
			this.addOverrideNames();

			this.logger.info("Loaded NEI config for LOTR Mod within " + (System.currentTimeMillis() - start) + " ms");
		}
	}

	public void refreshCache() {
		this.recipeHandlerManager.refreshCache();
	}

	private void loadHandler(TemplateRecipeHandler handler) {
		GuiCraftingRecipe.craftinghandlers.add(handler);
		GuiUsageRecipe.usagehandlers.add(handler);
	}

	private <T extends ICraftingHandler & IUsageHandler> void removeCraftingAndUsageHandler(Class<T> handlerClass) {
		this.removeCraftingHandler(handlerClass);
		this.removeUsageHandler(handlerClass);
	}

	private void removeCraftingHandler(Class<? extends ICraftingHandler> handlerClass) {
		this.removeHandler(handlerClass, GuiCraftingRecipe.craftinghandlers);
	}

	private void removeUsageHandler(Class<? extends IUsageHandler> handlerClass) {
		this.removeHandler(handlerClass, GuiUsageRecipe.usagehandlers);
	}

	private <T> void removeHandler(Class<? extends T> handlerClass, List<T> handlerList) {
		for (int i = 0; i < handlerList.size(); i++) {
			T handler = handlerList.get(i);
			if (handler.getClass() == handlerClass) handlerList.remove(i);
		}
	}

	private void hideItems() {
		// Beds
		this.hideItemAll(LOTRMod.dwarvenBed);
		this.hideItemAll(LOTRMod.elvenBed);
		this.hideItemAll(LOTRMod.highElvenBed);
		this.hideItemAll(LOTRMod.lionBed);
		this.hideItemAll(LOTRMod.orcBed);
		this.hideItemAll(LOTRMod.strawBed);
		this.hideItemAll(LOTRMod.woodElvenBed);
		this.hideItemAll(LOTRMod.furBed);

		// Cakes/Pies
		this.hideItemAll(LOTRMod.berryPie);
		this.hideItemAll(LOTRMod.cherryPie);
		this.hideItemAll(LOTRMod.dalishPastry);
		this.hideItemAll(LOTRMod.appleCrumble);
		this.hideItemAll(LOTRMod.bananaCake);
		this.hideItemAll(LOTRMod.lemonCake);

		// Fruits
		this.hideItemAll(LOTRMod.dateBlock);
		this.hideItemAll(LOTRMod.bananaBlock);
		this.hideItemAll(LOTRMod.grapevineRed);
		this.hideItemAll(LOTRMod.grapevineWhite);

		// Torches
		this.hideItemAll(LOTRMod.orcTorch);
		this.hideItemAll(LOTRMod.tauredainDoubleTorch);

		// Crops
		this.hideItemAll(LOTRMod.lettuceCrop);
		this.hideItemAll(LOTRMod.pipeweedCrop);
		this.hideItemAll(LOTRMod.flaxCrop);
		this.hideItemAll(LOTRMod.leekCrop);
		this.hideItemAll(LOTRMod.turnipCrop);
		this.hideItemAll(LOTRMod.yamCrop);

		// Spawner Chests
		this.hideItemAll(LOTRMod.spawnerChest);
		this.hideItemAll(LOTRMod.spawnerChestStone);

		// Vessels
		this.hideItemAll(LOTRMod.mugBlock);
		this.hideItemAll(LOTRMod.ceramicMugBlock);
		this.hideItemAll(LOTRMod.gobletGoldBlock);
		this.hideItemAll(LOTRMod.gobletSilverBlock);
		this.hideItemAll(LOTRMod.gobletCopperBlock);
		this.hideItemAll(LOTRMod.gobletWoodBlock);
		this.hideItemAll(LOTRMod.skullCupBlock);
		this.hideItemAll(LOTRMod.wineGlassBlock);
		this.hideItemAll(LOTRMod.glassBottleBlock);
		this.hideItemAll(LOTRMod.aleHornBlock);
		this.hideItemAll(LOTRMod.aleHornGoldBlock);

		// Others
		this.hideItemAll(LOTRMod.flowerPot);
		this.hideItemAll(LOTRMod.plateBlock);
		this.hideItemAll(LOTRMod.armorStand);
		this.hideItemAll(LOTRMod.marshLights);
		this.hideItemAll(LOTRMod.utumnoReturnLight);
		this.hideItemAll(LOTRMod.signCarved);
		this.hideItemAll(LOTRMod.signCarvedIthildin);
		this.hideItemAll(LOTRMod.bookshelfStorage);

		// Slabs
		this.hideItemMeta(LOTRMod.slabSingle);
		this.hideItemMeta(LOTRMod.slabSingle2);
		this.hideItemMeta(LOTRMod.slabSingle3);
		this.hideItemMeta(LOTRMod.slabSingle4);
		this.hideItemMeta(LOTRMod.slabSingle5);
		this.hideItemMeta(LOTRMod.slabSingle6);
		this.hideItemMeta(LOTRMod.slabSingle7);
		this.hideItemMeta(LOTRMod.slabSingle8);
		this.hideItemMeta(LOTRMod.slabSingle9);
		this.hideItemMeta(LOTRMod.slabSingle10);
		this.hideItemMeta(LOTRMod.slabSingle11);
		this.hideItemMeta(LOTRMod.slabSingle12);
		this.hideItemMeta(LOTRMod.slabSingleV);
		this.hideItemMeta(LOTRMod.slabSingleThatch);
		this.hideItemMeta(LOTRMod.slabSingleDirt);
		this.hideItemMeta(LOTRMod.slabSingleSand);
		this.hideItemMeta(LOTRMod.slabSingleGravel);
		this.hideItemMeta(LOTRMod.rottenSlabSingle);
		this.hideItemMeta(LOTRMod.scorchedSlabSingle);
		this.hideItemMeta(LOTRMod.slabUtumnoSingle);
		this.hideItemMeta(LOTRMod.slabUtumnoSingle2);
		this.hideItemMeta(LOTRMod.slabClayTileSingle);
		this.hideItemMeta(LOTRMod.slabClayTileDyedSingle);
		this.hideItemMeta(LOTRMod.slabClayTileDyedSingle2);
		this.hideItemMeta(LOTRMod.woodSlabSingle);
		this.hideItemMeta(LOTRMod.woodSlabSingle2);
		this.hideItemMeta(LOTRMod.woodSlabSingle3);
		this.hideItemMeta(LOTRMod.woodSlabSingle4);
		this.hideItemMeta(LOTRMod.woodSlabSingle5);

		this.hideItemAll(LOTRMod.slabDouble);
		this.hideItemAll(LOTRMod.slabDouble2);
		this.hideItemAll(LOTRMod.slabDouble3);
		this.hideItemAll(LOTRMod.slabDouble4);
		this.hideItemAll(LOTRMod.slabDouble5);
		this.hideItemAll(LOTRMod.slabDouble6);
		this.hideItemAll(LOTRMod.slabDouble7);
		this.hideItemAll(LOTRMod.slabDouble8);
		this.hideItemAll(LOTRMod.slabDouble9);
		this.hideItemAll(LOTRMod.slabDouble10);
		this.hideItemAll(LOTRMod.slabDouble11);
		this.hideItemAll(LOTRMod.slabDouble12);
		this.hideItemAll(LOTRMod.slabDoubleV);
		this.hideItemAll(LOTRMod.slabDoubleThatch);
		this.hideItemAll(LOTRMod.slabDoubleDirt);
		this.hideItemAll(LOTRMod.slabDoubleSand);
		this.hideItemAll(LOTRMod.slabDoubleGravel);
		this.hideItemAll(LOTRMod.rottenSlabDouble);
		this.hideItemAll(LOTRMod.scorchedSlabDouble);
		this.hideItemAll(LOTRMod.slabUtumnoDouble);
		this.hideItemAll(LOTRMod.slabUtumnoDouble2);
		this.hideItemAll(LOTRMod.slabClayTileDouble);
		this.hideItemAll(LOTRMod.slabClayTileDyedDouble);
		this.hideItemAll(LOTRMod.slabClayTileDyedDouble2);
		this.hideItemAll(LOTRMod.woodSlabDouble);
		this.hideItemAll(LOTRMod.woodSlabDouble2);
		this.hideItemAll(LOTRMod.woodSlabDouble3);
		this.hideItemAll(LOTRMod.woodSlabDouble4);
		this.hideItemAll(LOTRMod.woodSlabDouble5);
	}

	private void addOverrideNames() {
		// Portals
		this.setOverrideName(LOTRMod.elvenPortal, StatCollector.translateToLocal("neiLotr.lotr.block.elvenPortal.name"));
		this.setOverrideName(LOTRMod.morgulPortal, StatCollector.translateToLocal("neiLotr.lotr.block.morgulPortal.name"));
		this.setOverrideName(LOTRMod.utumnoPortal, StatCollector.translateToLocal("neiLotr.lotr.block.utumnoPortal.name"));
		this.setOverrideName(LOTRMod.utumnoReturnPortal, StatCollector.translateToLocal("neiLotr.lotr.block.utumnoReturnPortal.name"));
		this.setOverrideName(LOTRMod.utumnoReturnPortalBase, StatCollector.translateToLocal("neiLotr.lotr.block.utumnoReturnPortalBase.name"));

		// Others
		this.setOverrideName(LOTRMod.rhunFire, StatCollector.translateToLocal("neiLotr.lotr.block.rhunFire.name"));
	}

	private void hideItemMeta(ItemStack stack) {
		for (int i = 8; i < 16; i++) {
			ItemStack s = new ItemStack(stack.getItem(), 1, i);
			API.hideItem(s);
		}
	}

	private void hideItemMeta(Block block) {
		this.hideItemMeta(new ItemStack(block));
	}

	// private void hideItemMeta(Item item) {
	// this.hideItemMeta(new ItemStack(item));
	// }

	private void hideItemAll(ItemStack stack) {
		ItemStack s = new ItemStack(stack.getItem(), 1, 32767);
		API.hideItem(s);
	}

	private void hideItemAll(Block block) {
		this.hideItemAll(new ItemStack(block));
	}

	// private void hideItemAll(Item item) {
	// this.hideItemAll(new ItemStack(item));
	// }

	private void setOverrideName(ItemStack stack, String name) {
		API.setOverrideName(stack, name);
	}

	// private void setOverrideName(Item item, String name) {
	// this.setOverrideName(new ItemStack(item), name);
	// }

	private void setOverrideName(Block block, String name) {
		this.setOverrideName(new ItemStack(block), name);
	}

}
