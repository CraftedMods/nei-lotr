package craftedMods.neiLotr;

import static lotr.common.entity.npc.LOTRTradeEntries.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import codechicken.nei.api.API;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import craftedMods.neiLotr.handlers.BarrelRecipeHandler;
import craftedMods.neiLotr.handlers.BasicForgeRecipeHandler;
import craftedMods.neiLotr.handlers.BasicTradeHandler;
import craftedMods.neiLotr.handlers.craftingTable.BasicCTShapedRecipeHandler;
import craftedMods.neiLotr.handlers.craftingTable.BasicCTShapelessRecipeHandler;
import craftedMods.neiLotr.handlers.craftingTable.antihandler.NearHaradCTShapelessAntihandler;
import craftedMods.neiLotr.handlers.craftingTable.regular.PoisonedDrinkRecipeHandler;
import craftedMods.neiLotr.handlers.craftingTable.regular.PoisonedWeaponRecipeHandler;
import craftedMods.neiLotr.handlers.craftingTable.subhandler.HaradTurbanOrnamentRecipeHandler;
import craftedMods.neiLotr.handlers.template.AdvancedRecipeLoading;
import craftedMods.neiLotr.handlers.template.RecipeAntihandler;
import craftedMods.neiLotr.handlers.template.RecipeSubhandler;
import craftedMods.neiLotr.handlers.template.StaticRecipeLoader;
import craftedMods.neiLotr.util.NeiLotrReflection;
import craftedMods.neiLotr.util.NeiLotrUtil;
import lotr.client.gui.LOTRGuiCraftingTable;
import lotr.common.LOTRMod;
import lotr.common.entity.npc.LOTREntityAngmarOrcTrader;
import lotr.common.entity.npc.LOTREntityBlueDwarfMerchant;
import lotr.common.entity.npc.LOTREntityBlueDwarfMiner;
import lotr.common.entity.npc.LOTREntityBlueMountainsSmith;
import lotr.common.entity.npc.LOTREntityDaleBaker;
import lotr.common.entity.npc.LOTREntityDaleBlacksmith;
import lotr.common.entity.npc.LOTREntityDaleMerchant;
import lotr.common.entity.npc.LOTREntityDolGuldurOrcTrader;
import lotr.common.entity.npc.LOTREntityDorwinionElfVintner;
import lotr.common.entity.npc.LOTREntityDorwinionMerchantElf;
import lotr.common.entity.npc.LOTREntityDorwinionVinekeeper;
import lotr.common.entity.npc.LOTREntityDunedainBlacksmith;
import lotr.common.entity.npc.LOTREntityDunlendingBartender;
import lotr.common.entity.npc.LOTREntityDwarfMiner;
import lotr.common.entity.npc.LOTREntityDwarfSmith;
import lotr.common.entity.npc.LOTREntityEasterlingBaker;
import lotr.common.entity.npc.LOTREntityEasterlingBartender;
import lotr.common.entity.npc.LOTREntityEasterlingBlacksmith;
import lotr.common.entity.npc.LOTREntityEasterlingBrewer;
import lotr.common.entity.npc.LOTREntityEasterlingButcher;
import lotr.common.entity.npc.LOTREntityEasterlingFarmer;
import lotr.common.entity.npc.LOTREntityEasterlingFishmonger;
import lotr.common.entity.npc.LOTREntityEasterlingGoldsmith;
import lotr.common.entity.npc.LOTREntityEasterlingHunter;
import lotr.common.entity.npc.LOTREntityEasterlingLumberman;
import lotr.common.entity.npc.LOTREntityEasterlingMason;
import lotr.common.entity.npc.LOTREntityGaladhrimSmith;
import lotr.common.entity.npc.LOTREntityGaladhrimTrader;
import lotr.common.entity.npc.LOTREntityGondorBaker;
import lotr.common.entity.npc.LOTREntityGondorBartender;
import lotr.common.entity.npc.LOTREntityGondorBlacksmith;
import lotr.common.entity.npc.LOTREntityGondorBrewer;
import lotr.common.entity.npc.LOTREntityGondorButcher;
import lotr.common.entity.npc.LOTREntityGondorFarmer;
import lotr.common.entity.npc.LOTREntityGondorFishmonger;
import lotr.common.entity.npc.LOTREntityGondorFlorist;
import lotr.common.entity.npc.LOTREntityGondorGreengrocer;
import lotr.common.entity.npc.LOTREntityGondorLumberman;
import lotr.common.entity.npc.LOTREntityGondorMason;
import lotr.common.entity.npc.LOTREntityHalfTrollScavenger;
import lotr.common.entity.npc.LOTREntityHighElfSmith;
import lotr.common.entity.npc.LOTREntityHobbitBartender;
import lotr.common.entity.npc.LOTREntityHobbitFarmer;
import lotr.common.entity.npc.LOTREntityHobbitOrcharder;
import lotr.common.entity.npc.LOTREntityIronHillsMerchant;
import lotr.common.entity.npc.LOTREntityMordorOrcTrader;
import lotr.common.entity.npc.LOTREntityMoredainHuntsman;
import lotr.common.entity.npc.LOTREntityMoredainHutmaker;
import lotr.common.entity.npc.LOTREntityNearHaradDrinksTrader;
import lotr.common.entity.npc.LOTREntityNearHaradFoodTrader;
import lotr.common.entity.npc.LOTREntityNearHaradMerchant;
import lotr.common.entity.npc.LOTREntityNearHaradMineralsTrader;
import lotr.common.entity.npc.LOTREntityNearHaradPlantsTrader;
import lotr.common.entity.npc.LOTREntityRivendellSmith;
import lotr.common.entity.npc.LOTREntityRivendellTrader;
import lotr.common.entity.npc.LOTREntityRohanBaker;
import lotr.common.entity.npc.LOTREntityRohanBlacksmith;
import lotr.common.entity.npc.LOTREntityRohanBrewer;
import lotr.common.entity.npc.LOTREntityRohanBuilder;
import lotr.common.entity.npc.LOTREntityRohanButcher;
import lotr.common.entity.npc.LOTREntityRohanFarmer;
import lotr.common.entity.npc.LOTREntityRohanFishmonger;
import lotr.common.entity.npc.LOTREntityRohanLumberman;
import lotr.common.entity.npc.LOTREntityRohanMeadhost;
import lotr.common.entity.npc.LOTREntityRohanOrcharder;
import lotr.common.entity.npc.LOTREntityRohanStablemaster;
import lotr.common.entity.npc.LOTREntityScrapTrader;
import lotr.common.entity.npc.LOTREntityTauredainFarmer;
import lotr.common.entity.npc.LOTREntityTauredainShaman;
import lotr.common.entity.npc.LOTREntityUrukHaiTrader;
import lotr.common.entity.npc.LOTREntityWoodElfSmith;
import lotr.common.entity.npc.LOTRTradeEntries;
import lotr.common.entity.npc.LOTRTradeable;
import lotr.common.recipe.LOTRRecipes;
import lotr.common.tileentity.LOTRTileEntityAlloyForge;
import lotr.common.tileentity.LOTRTileEntityDwarvenForge;
import lotr.common.tileentity.LOTRTileEntityElvenForge;
import lotr.common.tileentity.LOTRTileEntityOrcForge;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;

public class NeiLotrNEIConfig {

	private Set<TemplateRecipeHandler> registeredHandlers = new HashSet<>();

	private int idGenerator = -1;

	private PoisonedDrinkRecipeHandler poisonedDrinkHandler;

	private List<BasicForgeRecipeHandler> alloyForgeHandlers = new ArrayList<>();

	private StaticRecipeLoader recipeLoader;

	public NeiLotrNEIConfig(StaticRecipeLoader recipeLoader) {
		this.recipeLoader = recipeLoader;
	}

	private void preInit() {
		poisonedDrinkHandler = new PoisonedDrinkRecipeHandler(nextId());
		alloyForgeHandlers.add(new BasicForgeRecipeHandler<>(nextId(), new LOTRTileEntityDwarvenForge()));
		alloyForgeHandlers.add(new BasicForgeRecipeHandler<>(nextId(), new LOTRTileEntityElvenForge()));
		alloyForgeHandlers.add(new BasicForgeRecipeHandler<>(nextId(), new LOTRTileEntityOrcForge()));
		alloyForgeHandlers.add(new BasicForgeRecipeHandler<>(nextId(), new LOTRTileEntityAlloyForge()));
	}

	public void init() {
		try {
			long start = System.currentTimeMillis();

			preInit();
			registerItemIterators();

			recipeLoader.itemIteration();

			initMECTHandlers();
			initCTHandlers();
			initAlloyForgeHandlers();
			registerTraderHandlers();
			registerBarrelHandler();
			
			recipeLoader.loadStaticRecipes();

			NeiLotr.mod.getLogger()
					.info("Initialized NEI config for LOTR Mod within " + (System.currentTimeMillis() - start) + " ms");
		} catch (Exception e) {
			NeiLotr.mod.getLogger().error("Couldn't initialize NEI config for LOTR Mod: ", e);
		}
	}

	private void registerItemIterators() {
		recipeLoader.registerItemIterator(this::computeAlloyForgeRecipes);
		recipeLoader.registerItemIterator(poisonedDrinkHandler::computeRecipes);
	}

	// ME crafting tables
	private void initMECTHandlers() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, ClassNotFoundException {
		for (String ctName : Config.CRAFTING_TABLE_NAMES) {
			String camelCaseCTName = new StringBuilder(ctName)
					.replace(0, 1, Character.toString(Character.toUpperCase(ctName.charAt(0)))).toString();

			List<IRecipe> recipes = (List<IRecipe>) LOTRRecipes.class
					.getDeclaredField((ctName.equals("blueDwarven") ? "blueMountains" : ctName).concat("Recipes"))
					.get(null);

			Class<? extends GuiContainer> guiClass = (Class<? extends GuiContainer>) Class
					.forName("lotr.client.gui.LOTRGuiCraftingTable$".concat(camelCaseCTName));

			List<RecipeAntihandler> antihandlers = getAntihandlersForMECT(ctName);

			if (NeiLotr.mod.getConfig().isShapedMECTRecipeHandlerEnabled(ctName)) {
				BasicCTShapedRecipeHandler shapedHandler = new BasicCTShapedRecipeHandler(nextId(), guiClass, ctName);
				antihandlers.forEach(shapedHandler::addAntihandler);
				recipeLoader.registerStaticRecipeLoaderWithIRecipe(shapedHandler, recipes);
				registeredHandlers.add(shapedHandler);
			}

			if (NeiLotr.mod.getConfig().isShapelessMECTRecipeHandlerEnabled(ctName)) {
				BasicCTShapelessRecipeHandler shapelessHandler = new BasicCTShapelessRecipeHandler(nextId(), guiClass,
						ctName);
				getSubhandlersForMECT(ctName, shapelessHandler).forEach(shapelessHandler::addSubhandler);
				antihandlers.forEach(shapelessHandler::addAntihandler);
				recipeLoader.registerStaticRecipeLoaderWithIRecipe(shapelessHandler, recipes);
				registeredHandlers.add(shapelessHandler);
			}
		}
	}

	private List<RecipeSubhandler> getSubhandlersForMECT(String name, BasicCTShapelessRecipeHandler handler) {
		List<RecipeSubhandler> ret = new ArrayList<>();
		switch (name) {
		case "nearHarad":
			//ret.add(new HaradTurbanOrnamentRecipeHandler(handler));
			break;
		}
		return ret;

	}

	private List<RecipeAntihandler> getAntihandlersForMECT(String name) {
		List<RecipeAntihandler> ret = new ArrayList<>();
		switch (name) {
		case "nearHarad":
			//ret.add(new NearHaradCTShapelessAntihandler());
			break;
		}
		return ret;
	}

	// Regular crafting table
	private void initCTHandlers() {
		this.registerDefaultHandlerWithIRecipe(new PoisonedWeaponRecipeHandler(nextId()),
				PoisonedWeaponRecipeHandler.getPoisonedWeaponRecipes());
		this.registeredHandlers.add(poisonedDrinkHandler);
	}

	private void initAlloyForgeHandlers() {
		this.alloyForgeHandlers.forEach(handler -> {
			this.registerDefaultHandler(handler, handler.getRecipes());
		});
	}

	private void computeAlloyForgeRecipes(Integer count, ItemStack stack) {
		for (BasicForgeRecipeHandler<?> handler : alloyForgeHandlers) {
			ItemStack result = handler.getAlloyForgeDummy().getSmeltingResult(stack);
			if (result != null)
				handler.getRecipes().add(handler.new CachedForgeRecipe(null, Arrays.asList(stack), result));
			NeiLotrUtil.itemStackIteration((count2, stack2) -> {
				ItemStack result2 = NeiLotrReflection.getAlloySmeltingResult(handler.getAlloyForgeDummy(), stack,
						stack2);
				if (result2 != null) {
					handler.getRecipes()
							.add(handler.new CachedForgeRecipe(Arrays.asList(stack2), Arrays.asList(stack), result2));
				}
			});
		}
	}

	private void registerTraderHandlers() {
		registerBasicTradeHandler(LOTREntityHobbitBartender.class, HOBBIT_BARTENDER_BUY, HOBBIT_BARTENDER_SELL);
		registerBasicTradeHandler(LOTREntityMordorOrcTrader.class, MORDOR_TRADER_BUY, MORDOR_TRADER_SELL);
		registerBasicTradeHandler(LOTREntityGondorBlacksmith.class, GONDOR_BLACKSMITH_BUY, GONDOR_BLACKSMITH_SELL);
		registerBasicTradeHandler(LOTREntityGaladhrimTrader.class, GALADHRIM_TRADER_BUY, GALADHRIM_TRADER_SELL);
		registerBasicTradeHandler(LOTREntityUrukHaiTrader.class, URUK_HAI_TRADER_BUY, URUK_HAI_TRADER_SELL);
		registerBasicTradeHandler(LOTREntityDwarfMiner.class, DWARF_MINER_BUY, DWARF_MINER_SELL);
		registerBasicTradeHandler(LOTREntityRohanBlacksmith.class, ROHAN_BLACKSMITH_BUY, ROHAN_BLACKSMITH_SELL);
		registerBasicTradeHandler(LOTREntityDunlendingBartender.class, DUNLENDING_BARTENDER_BUY,
				DUNLENDING_BARTENDER_SELL);
		registerBasicTradeHandler(LOTREntityRohanMeadhost.class, ROHAN_MEADHOST_BUY, ROHAN_MEADHOST_SELL);
		registerBasicTradeHandler(LOTREntityHobbitOrcharder.class, HOBBIT_ORCHARDER_BUY, HOBBIT_ORCHARDER_SELL);
		registerBasicTradeHandler(LOTREntityHobbitFarmer.class, HOBBIT_FARMER_BUY, HOBBIT_FARMER_SELL);
		registerBasicTradeHandler(LOTREntityBlueDwarfMiner.class, BLUE_DWARF_MINER_BUY, BLUE_DWARF_MINER_SELL);
		registerBasicTradeHandler(LOTREntityNearHaradDrinksTrader.class, NEAR_HARAD_DRINKS_TRADER_BUY,
				NEAR_HARAD_DRINKS_TRADER_SELL);
		registerBasicTradeHandler(LOTREntityNearHaradMineralsTrader.class, NEAR_HARAD_MINERALS_TRADER_BUY,
				NEAR_HARAD_MINERALS_TRADER_SELL);
		registerBasicTradeHandler(LOTREntityNearHaradPlantsTrader.class, NEAR_HARAD_PLANTS_TRADER_BUY,
				NEAR_HARAD_PLANTS_TRADER_SELL);
		registerBasicTradeHandler(LOTREntityNearHaradFoodTrader.class, NEAR_HARAD_FOOD_TRADER_BUY,
				NEAR_HARAD_FOOD_TRADER_SELL);
		registerBasicTradeHandler(LOTREntityBlueDwarfMerchant.class, BLUE_DWARF_MERCHANT_BUY, BLUE_DWARF_MERCHANT_SELL);
		registerBasicTradeHandler(LOTREntityNearHaradMerchant.class, NEAR_HARAD_MERCHANT_BUY, NEAR_HARAD_MERCHANT_SELL);
		registerBasicTradeHandler(LOTREntityAngmarOrcTrader.class, ANGMAR_TRADER_BUY, ANGMAR_TRADER_SELL);
		registerBasicTradeHandler(LOTREntityDolGuldurOrcTrader.class, DOL_GULDUR_TRADER_BUY, DOL_GULDUR_TRADER_SELL);
		registerBasicTradeHandler(LOTREntityHalfTrollScavenger.class, HALF_TROLL_SCAVENGER_BUY,
				HALF_TROLL_SCAVENGER_SELL);
		registerBasicTradeHandler(LOTREntityGaladhrimSmith.class, GALADHRIM_SMITH_BUY, GALADHRIM_SMITH_SELL);
		registerBasicTradeHandler(LOTREntityHighElfSmith.class, HIGH_ELF_SMITH_BUY, HIGH_ELF_SMITH_SELL);
		registerBasicTradeHandler(LOTREntityWoodElfSmith.class, WOOD_ELF_SMITH_BUY, WOOD_ELF_SMITH_SELL);
		registerBasicTradeHandler(LOTREntityMoredainHuntsman.class, MOREDAIN_HUNTSMAN_BUY, MOREDAIN_HUNTSMAN_SELL);
		registerBasicTradeHandler(LOTREntityMoredainHutmaker.class, MOREDAIN_HUTMAKER_BUY, MOREDAIN_HUTMAKER_SELL);
		registerBasicTradeHandler(LOTREntityIronHillsMerchant.class, IRON_HILLS_MERCHANT_BUY, IRON_HILLS_MERCHANT_SELL);
		registerBasicTradeHandler(LOTREntityScrapTrader.class, SCRAP_TRADER_BUY, SCRAP_TRADER_SELL);
		registerBasicTradeHandler(LOTREntityTauredainShaman.class, TAUREDAIN_SHAMAN_BUY, TAUREDAIN_SHAMAN_SELL);
		registerBasicTradeHandler(LOTREntityTauredainFarmer.class, TAUREDAIN_FARMER_BUY, TAUREDAIN_FARMER_SELL);
		registerBasicTradeHandler(LOTREntityDwarfSmith.class, DWARF_SMITH_BUY, DWARF_SMITH_SELL);
		registerBasicTradeHandler(LOTREntityBlueMountainsSmith.class, BLUE_DWARF_SMITH_BUY, BLUE_DWARF_SMITH_SELL);
		registerBasicTradeHandler(LOTREntityDaleBlacksmith.class, DALE_BLACKSMITH_BUY, DALE_BLACKSMITH_SELL);
		registerBasicTradeHandler(LOTREntityDaleBaker.class, DALE_BAKER_BUY, DALE_BAKER_SELL);
		registerBasicTradeHandler(LOTREntityDorwinionElfVintner.class, DORWINION_VINTNER_BUY, DORWINION_VINTNER_SELL);
		registerBasicTradeHandler(LOTREntityDorwinionVinekeeper.class, DORWINION_VINEKEEPER_BUY,
				DORWINION_VINEKEEPER_SELL);
		registerBasicTradeHandler(LOTREntityDorwinionMerchantElf.class, DORWINION_MERCHANT_BUY,
				DORWINION_MERCHANT_SELL);
		registerBasicTradeHandler(LOTREntityDaleMerchant.class, DALE_MERCHANT_BUY, DALE_MERCHANT_SELL);
		registerBasicTradeHandler(LOTREntityGondorFarmer.class, GONDOR_FARMER_BUY, GONDOR_FARMER_SELL);
		registerBasicTradeHandler(LOTREntityGondorBartender.class, GONDOR_BARTENDER_BUY, GONDOR_BARTENDER_SELL);
		registerBasicTradeHandler(LOTREntityGondorGreengrocer.class, GONDOR_GREENGROCER_BUY, GONDOR_GREENGROCER_SELL);
		registerBasicTradeHandler(LOTREntityGondorLumberman.class, GONDOR_LUMBERMAN_BUY, GONDOR_LUMBERMAN_SELL);
		registerBasicTradeHandler(LOTREntityGondorMason.class, GONDOR_MASON_BUY, GONDOR_MASON_SELL);
		registerBasicTradeHandler(LOTREntityGondorBrewer.class, GONDOR_BREWER_BUY, GONDOR_BREWER_SELL);
		registerBasicTradeHandler(LOTREntityGondorFlorist.class, GONDOR_FLORIST_BUY, GONDOR_FLORIST_SELL);
		registerBasicTradeHandler(LOTREntityGondorButcher.class, GONDOR_BUTCHER_BUY, GONDOR_BUTCHER_SELL);
		registerBasicTradeHandler(LOTREntityGondorFishmonger.class, GONDOR_FISHMONGER_BUY, GONDOR_FISHMONGER_SELL);
		registerBasicTradeHandler(LOTREntityGondorBaker.class, GONDOR_BAKER_BUY, GONDOR_BAKER_SELL);
		registerBasicTradeHandler(LOTREntityRohanFarmer.class, ROHAN_FARMER_BUY, ROHAN_FARMER_SELL);
		registerBasicTradeHandler(LOTREntityRohanLumberman.class, ROHAN_LUMBERMAN_BUY, ROHAN_LUMBERMAN_SELL);
		registerBasicTradeHandler(LOTREntityRohanBuilder.class, ROHAN_BUILDER_BUY, ROHAN_BUILDER_SELL);
		registerBasicTradeHandler(LOTREntityRohanBrewer.class, ROHAN_BREWER_BUY, ROHAN_BREWER_SELL);
		registerBasicTradeHandler(LOTREntityRohanButcher.class, ROHAN_BUTCHER_BUY, ROHAN_BUTCHER_SELL);
		registerBasicTradeHandler(LOTREntityRohanFishmonger.class, ROHAN_FISHMONGER_BUY, ROHAN_FISHMONGER_SELL);
		registerBasicTradeHandler(LOTREntityRohanBaker.class, ROHAN_BAKER_BUY, ROHAN_BAKER_SELL);
		registerBasicTradeHandler(LOTREntityRohanOrcharder.class, ROHAN_ORCHARDER_BUY, ROHAN_ORCHARDER_SELL);
		registerBasicTradeHandler(LOTREntityDunedainBlacksmith.class, DUNEDAIN_BLACKSMITH_BUY,
				DUNEDAIN_BLACKSMITH_SELL);
		registerBasicTradeHandler(LOTREntityRohanStablemaster.class, ROHAN_STABLEMASTER_BUY, ROHAN_STABLEMASTER_SELL);
		registerBasicTradeHandler(LOTREntityEasterlingBlacksmith.class, RHUN_BLACKSMITH_BUY, RHUN_BLACKSMITH_SELL);
		registerBasicTradeHandler(LOTREntityEasterlingLumberman.class, RHUN_LUMBERMAN_BUY, RHUN_LUMBERMAN_SELL);
		registerBasicTradeHandler(LOTREntityEasterlingMason.class, RHUN_MASON_BUY, RHUN_MASON_SELL);
		registerBasicTradeHandler(LOTREntityEasterlingButcher.class, RHUN_BUTCHER_BUY, RHUN_BUTCHER_SELL);
		registerBasicTradeHandler(LOTREntityEasterlingBrewer.class, RHUN_BREWER_BUY, RHUN_BREWER_SELL);
		registerBasicTradeHandler(LOTREntityEasterlingFishmonger.class, RHUN_FISHMONGER_BUY, RHUN_FISHMONGER_SELL);
		registerBasicTradeHandler(LOTREntityEasterlingBaker.class, RHUN_BAKER_BUY, RHUN_BAKER_SELL);
		registerBasicTradeHandler(LOTREntityEasterlingHunter.class, RHUN_HUNTER_BUY, RHUN_HUNTER_SELL);
		registerBasicTradeHandler(LOTREntityEasterlingFarmer.class, RHUN_FARMER_BUY, RHUN_FARMER_SELL);
		registerBasicTradeHandler(LOTREntityEasterlingGoldsmith.class, RHUN_GOLDSMITH_BUY, RHUN_GOLDSMITH_SELL);
		registerBasicTradeHandler(LOTREntityEasterlingBartender.class, RHUN_BARTENDER_BUY, RHUN_BARTENDER_SELL);
		registerBasicTradeHandler(LOTREntityRivendellTrader.class, RIVENDELL_TRADER_BUY, RIVENDELL_TRADER_SELL);
		registerBasicTradeHandler(LOTREntityRivendellSmith.class, RIVENDELL_SMITH_BUY, RIVENDELL_SMITH_SELL);
	}

	private void registerBarrelHandler() {
		BarrelRecipeHandler barrelRecipeHandler = new BarrelRecipeHandler(nextId());
		this.registerDefaultHandler(barrelRecipeHandler, barrelRecipeHandler.getRecipes());
	}
	
	private void registerBasicTradeHandler(Class<? extends LOTRTradeable> entityClass, LOTRTradeEntries buy,
			LOTRTradeEntries sell) {
		BasicTradeHandler handler = new BasicTradeHandler(nextId(),
				"entity." + EntityList.classToStringMapping.get(entityClass) + ".name", buy, sell);
		this.registerDefaultHandler(handler, handler.getRecipes());
	}

	public void load() {
		if (!NeiLotr.mod.getConfig().isDisabled()) {

			long start = System.currentTimeMillis();

			// Load registered handlers
			this.registeredHandlers.forEach(this::loadHandler);

			// Item hiding
			if (NeiLotr.mod.getConfig().isHideTechnicalBlocks()) {
				hideItems();
			}

			// Override names
			addOverrideNames();

			NeiLotr.mod.getLogger()
					.info("Loaded NEI config for LOTR Mod within " + (System.currentTimeMillis() - start) + " ms");
		}
	}

	private void hideItems() {
		// Beds
		hideItemAll(LOTRMod.dwarvenBed);
		hideItemAll(LOTRMod.elvenBed);
		hideItemAll(LOTRMod.highElvenBed);
		hideItemAll(LOTRMod.lionBed);
		hideItemAll(LOTRMod.orcBed);
		hideItemAll(LOTRMod.strawBed);
		hideItemAll(LOTRMod.woodElvenBed);
		hideItemAll(LOTRMod.furBed);

		// Cakes/Pies
		hideItemAll(LOTRMod.berryPie);
		hideItemAll(LOTRMod.cherryPie);
		hideItemAll(LOTRMod.dalishPastry);
		hideItemAll(LOTRMod.appleCrumble);
		hideItemAll(LOTRMod.bananaCake);
		hideItemAll(LOTRMod.lemonCake);

		// Fruits
		hideItemAll(LOTRMod.dateBlock);
		hideItemAll(LOTRMod.bananaBlock);
		hideItemAll(LOTRMod.grapevineRed);
		hideItemAll(LOTRMod.grapevineWhite);

		// Torches
		hideItemAll(LOTRMod.orcTorch);
		hideItemAll(LOTRMod.tauredainDoubleTorch);

		// Crops
		hideItemAll(LOTRMod.lettuceCrop);
		hideItemAll(LOTRMod.pipeweedCrop);
		hideItemAll(LOTRMod.flaxCrop);
		hideItemAll(LOTRMod.leekCrop);
		hideItemAll(LOTRMod.turnipCrop);
		hideItemAll(LOTRMod.yamCrop);

		// Spawner Chests
		hideItemAll(LOTRMod.spawnerChest);
		hideItemAll(LOTRMod.spawnerChestStone);

		// Vessels
		hideItemAll(LOTRMod.mugBlock);
		hideItemAll(LOTRMod.ceramicMugBlock);
		hideItemAll(LOTRMod.gobletGoldBlock);
		hideItemAll(LOTRMod.gobletSilverBlock);
		hideItemAll(LOTRMod.gobletCopperBlock);
		hideItemAll(LOTRMod.gobletWoodBlock);
		hideItemAll(LOTRMod.skullCupBlock);
		hideItemAll(LOTRMod.wineGlassBlock);
		hideItemAll(LOTRMod.glassBottleBlock);
		hideItemAll(LOTRMod.aleHornBlock);
		hideItemAll(LOTRMod.aleHornGoldBlock);

		// Others
		hideItemAll(LOTRMod.flowerPot);
		hideItemAll(LOTRMod.plateBlock);
		hideItemAll(LOTRMod.armorStand);
		hideItemAll(LOTRMod.marshLights);
		hideItemAll(LOTRMod.utumnoReturnLight);
		hideItemAll(LOTRMod.signCarved);
		hideItemAll(LOTRMod.signCarvedIthildin);
		hideItemAll(LOTRMod.bookshelfStorage);

		// Slabs
		hideItemMeta(LOTRMod.slabSingle);
		hideItemMeta(LOTRMod.slabSingle2);
		hideItemMeta(LOTRMod.slabSingle3);
		hideItemMeta(LOTRMod.slabSingle4);
		hideItemMeta(LOTRMod.slabSingle5);
		hideItemMeta(LOTRMod.slabSingle6);
		hideItemMeta(LOTRMod.slabSingle7);
		hideItemMeta(LOTRMod.slabSingle8);
		hideItemMeta(LOTRMod.slabSingle9);
		hideItemMeta(LOTRMod.slabSingle10);
		hideItemMeta(LOTRMod.slabSingle11);
		hideItemMeta(LOTRMod.slabSingle12);
		hideItemMeta(LOTRMod.slabSingleV);
		hideItemMeta(LOTRMod.slabSingleThatch);
		hideItemMeta(LOTRMod.slabSingleDirt);
		hideItemMeta(LOTRMod.slabSingleSand);
		hideItemMeta(LOTRMod.slabSingleGravel);
		hideItemMeta(LOTRMod.rottenSlabSingle);
		hideItemMeta(LOTRMod.scorchedSlabSingle);
		hideItemMeta(LOTRMod.slabUtumnoSingle);
		hideItemMeta(LOTRMod.slabUtumnoSingle2);
		hideItemMeta(LOTRMod.slabClayTileSingle);
		hideItemMeta(LOTRMod.slabClayTileDyedSingle);
		hideItemMeta(LOTRMod.slabClayTileDyedSingle2);
		hideItemMeta(LOTRMod.woodSlabSingle);
		hideItemMeta(LOTRMod.woodSlabSingle2);
		hideItemMeta(LOTRMod.woodSlabSingle3);
		hideItemMeta(LOTRMod.woodSlabSingle4);
		hideItemMeta(LOTRMod.woodSlabSingle5);

		hideItemAll(LOTRMod.slabDouble);
		hideItemAll(LOTRMod.slabDouble2);
		hideItemAll(LOTRMod.slabDouble3);
		hideItemAll(LOTRMod.slabDouble4);
		hideItemAll(LOTRMod.slabDouble5);
		hideItemAll(LOTRMod.slabDouble6);
		hideItemAll(LOTRMod.slabDouble7);
		hideItemAll(LOTRMod.slabDouble8);
		hideItemAll(LOTRMod.slabDouble9);
		hideItemAll(LOTRMod.slabDouble10);
		hideItemAll(LOTRMod.slabDouble11);
		hideItemAll(LOTRMod.slabDouble12);
		hideItemAll(LOTRMod.slabDoubleV);
		hideItemAll(LOTRMod.slabDoubleThatch);
		hideItemAll(LOTRMod.slabDoubleDirt);
		hideItemAll(LOTRMod.slabDoubleSand);
		hideItemAll(LOTRMod.slabDoubleGravel);
		hideItemAll(LOTRMod.rottenSlabDouble);
		hideItemAll(LOTRMod.scorchedSlabDouble);
		hideItemAll(LOTRMod.slabUtumnoDouble);
		hideItemAll(LOTRMod.slabUtumnoDouble2);
		hideItemAll(LOTRMod.slabClayTileDouble);
		hideItemAll(LOTRMod.slabClayTileDyedDouble);
		hideItemAll(LOTRMod.slabClayTileDyedDouble2);
		hideItemAll(LOTRMod.woodSlabDouble);
		hideItemAll(LOTRMod.woodSlabDouble2);
		hideItemAll(LOTRMod.woodSlabDouble3);
		hideItemAll(LOTRMod.woodSlabDouble4);
		hideItemAll(LOTRMod.woodSlabDouble5);
	}

	private void addOverrideNames() {
		// Portals
		setOverrideName(LOTRMod.elvenPortal, StatCollector.translateToLocal("lotrNei.lotr.block.elvenPortal.name"));
		setOverrideName(LOTRMod.morgulPortal, StatCollector.translateToLocal("lotrNei.lotr.block.morgulPortal.name"));
		setOverrideName(LOTRMod.utumnoPortal, StatCollector.translateToLocal("lotrNei.lotr.block.utumnoPortal.name"));
		setOverrideName(LOTRMod.utumnoReturnPortal,
				StatCollector.translateToLocal("lotrNei.lotr.block.utumnoReturnPortal.name"));
		setOverrideName(LOTRMod.utumnoReturnPortalBase,
				StatCollector.translateToLocal("lotrNei.lotr.block.utumnoReturnPortalBase.name"));

		// Others
		setOverrideName(LOTRMod.goran, StatCollector.translateToLocal("lotrNei.lotr.block.goranBlock.name"));
		setOverrideName(LOTRMod.rhunFire, StatCollector.translateToLocal("lotrNei.lotr.block.rhunFire.name"));
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

	private void hideItemMeta(Item item) {
		this.hideItemMeta(new ItemStack(item));
	}

	private void hideItemAll(ItemStack stack) {
		ItemStack s = new ItemStack(stack.getItem(), 1, 32767);
		API.hideItem(s);
	}

	private void hideItemAll(Block block) {
		this.hideItemAll(new ItemStack(block));
	}

	private void hideItemAll(Item item) {
		this.hideItemAll(new ItemStack(item));
	}

	private void setOverrideName(ItemStack stack, String name) {
		API.setOverrideName(stack, name);
	}

	private void setOverrideName(Item item, String name) {
		setOverrideName(new ItemStack(item), name);
	}

	private void setOverrideName(Block block, String name) {
		setOverrideName(new ItemStack(block), name);
	}

	private void loadHandler(TemplateRecipeHandler handler) {
		GuiCraftingRecipe.craftinghandlers.add(handler);
		GuiUsageRecipe.usagehandlers.add(handler);
	}

	private <T extends TemplateRecipeHandler & AdvancedRecipeLoading> void registerDefaultHandler(T handler,
			List<CachedRecipe> recipes) {
		registeredHandlers.add(handler);
		recipeLoader.registerStaticRecipeLoader(handler, recipes);
	}

	private <T extends TemplateRecipeHandler & AdvancedRecipeLoading> void registerDefaultHandlerWithIRecipe(T handler,
			List<IRecipe> recipes) {
		registeredHandlers.add(handler);
		recipeLoader.registerStaticRecipeLoaderWithIRecipe(handler, recipes);
	}

	private int nextId() {
		return ++idGenerator;
	}

	/*
	 * // Special Handlers registerHandler(new PoisonedWeaponRecipeHandler());
	 * registerHandler(new PoisonedDrinkRecipeHandler()); registerHandler(new
	 * HobbitPipeRecipeHandler()); //registerHandler(new
	 * HaradRobeDyeRecipeHandler());
	 * 
	 * // Unsmeltery Handler registerHandler(new UnsmelteryRecipeHandler());
	 * 
	 * // Barrel recipe handler registerHandler(new BarrelRecipeHandler());
	 * 
	 * // Alloy forge handlers registerHandler(new
	 * BasicForgeRecipeHandler<LOTRTileEntityDwarvenForge>(new
	 * LOTRTileEntityDwarvenForge())); registerHandler(new
	 * BasicForgeRecipeHandler<LOTRTileEntityElvenForge>(new
	 * LOTRTileEntityElvenForge())); registerHandler(new
	 * BasicForgeRecipeHandler<LOTRTileEntityOrcForge>(new
	 * LOTRTileEntityOrcForge())); registerHandler(new
	 * BasicForgeRecipeHandler<LOTRTileEntityAlloyForge>(new
	 * LOTRTileEntityAlloyForge()));
	 * 
	 * // Trader support if (NeiLotr.mod.getConfig().isUseTraderSupport()) {
	 * registerHandler(new BasicTradeHandler("entity.lotr.HobbitBartender.name",
	 * LOTRTradeEntries.HOBBIT_BARTENDER_BUY,
	 * LOTRTradeEntries.HOBBIT_BARTENDER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityHobbitBartender.class)));
	 * registerHandler(new BasicTradeHandler("entity.lotr.MordorOrcTrader.name",
	 * LOTRTradeEntries.ORC_TRADER_BUY, LOTRTradeEntries.ORC_TRADER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityMordorOrcTrader.class)));
	 * registerHandler(new
	 * BasicTradeHandler("entity.lotr.GondorBlacksmith.name",
	 * LOTRTradeEntries.GONDOR_BLACKSMITH_BUY,
	 * LOTRTradeEntries.GONDOR_BLACKSMITH_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityGondorBlacksmith.class))) ;
	 * registerHandler(new BasicTradeHandler("entity.lotr.GaladhrimTrader.name",
	 * LOTRTradeEntries.ELVEN_TRADER_BUY, LOTRTradeEntries.ELVEN_TRADER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityElvenTrader.class)));
	 * registerHandler(new BasicTradeHandler("entity.lotr.UrukHaiTrader.name",
	 * LOTRTradeEntries.URUK_HAI_TRADER_BUY,
	 * LOTRTradeEntries.URUK_HAI_TRADER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityUrukHaiTrader.class)));
	 * registerHandler(new BasicTradeHandler("entity.lotr.DwarfMiner.name",
	 * LOTRTradeEntries.DWARF_MINER_BUY, LOTRTradeEntries.DWARF_MINER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityDwarfMiner.class)));
	 * registerHandler(new BasicTradeHandler("entity.lotr.RohanBlacksmith.name",
	 * LOTRTradeEntries.ROHAN_BLACKSMITH_BUY,
	 * LOTRTradeEntries.ROHAN_BLACKSMITH_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityRohanBlacksmith.class)));
	 * registerHandler(new
	 * BasicTradeHandler("entity.lotr.DunlendingBartender.name",
	 * LOTRTradeEntries.DUNLENDING_BARTENDER_BUY,
	 * LOTRTradeEntries.DUNLENDING_BARTENDER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityDunlendingBartender.class
	 * ))); registerHandler(new
	 * BasicTradeHandler("entity.lotr.RohanMeadhost.name",
	 * LOTRTradeEntries.ROHAN_MEADHOST_BUY,
	 * LOTRTradeEntries.ROHAN_MEADHOST_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityRohanMeadhost.class)));
	 * registerHandler(new BasicTradeHandler("entity.lotr.HobbitOrcharder.name",
	 * LOTRTradeEntries.HOBBIT_ORCHARDER_BUY,
	 * LOTRTradeEntries.HOBBIT_ORCHARDER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityHobbitOrcharder.class)));
	 * registerHandler(new BasicTradeHandler("entity.lotr.HobbitFarmer.name",
	 * LOTRTradeEntries.HOBBIT_FARMER_BUY, LOTRTradeEntries.HOBBIT_FARMER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityHobbitFarmer.class)));
	 * registerHandler(new BasicTradeHandler("entity.lotr.BlueDwarfMiner.name",
	 * LOTRTradeEntries.BLUE_DWARF_MINER_BUY,
	 * LOTRTradeEntries.BLUE_DWARF_MINER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityBlueDwarfMiner.class)));
	 * registerHandler(new
	 * BasicTradeHandler("entity.lotr.NearHaradDrinksTrader.name",
	 * LOTRTradeEntries.NEAR_HARAD_DRINKS_TRADER_BUY,
	 * LOTRTradeEntries.NEAR_HARAD_DRINKS_TRADER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityNearHaradDrinksTrader.
	 * class))); registerHandler(new
	 * BasicTradeHandler("entity.lotr.NearHaradMineralsTrader.name",
	 * LOTRTradeEntries.NEAR_HARAD_MINERALS_TRADER_BUY,
	 * LOTRTradeEntries.NEAR_HARAD_MINERALS_TRADER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityNearHaradMineralsTrader.
	 * class))); registerHandler(new
	 * BasicTradeHandler("entity.lotr.NearHaradPlantsTrader.name",
	 * LOTRTradeEntries.NEAR_HARAD_PLANTS_TRADER_BUY,
	 * LOTRTradeEntries.NEAR_HARAD_PLANTS_TRADER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityNearHaradPlantsTrader.
	 * class))); registerHandler(new
	 * BasicTradeHandler("entity.lotr.NearHaradFoodTrader.name",
	 * LOTRTradeEntries.NEAR_HARAD_FOOD_TRADER_BUY,
	 * LOTRTradeEntries.NEAR_HARAD_FOOD_TRADER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityNearHaradFoodTrader.class
	 * ))); registerHandler(new
	 * BasicTradeHandler("entity.lotr.BlueDwarfMerchant.name",
	 * LOTRTradeEntries.BLUE_DWARF_MERCHANT_BUY,
	 * LOTRTradeEntries.BLUE_DWARF_MERCHANT_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityBlueDwarfMerchant.class)) );
	 * registerHandler(new
	 * BasicTradeHandler("entity.lotr.NearHaradMerchant.name",
	 * LOTRTradeEntries.NEAR_HARAD_MERCHANT_BUY,
	 * LOTRTradeEntries.NEAR_HARAD_MERCHANT_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityNearHaradMerchant.class)) );
	 * registerHandler(new BasicTradeHandler("entity.lotr.AngmarOrcTrader.name",
	 * LOTRTradeEntries.ANGMAR_TRADER_BUY, LOTRTradeEntries.ANGMAR_TRADER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityAngmarOrcTrader.class)));
	 * registerHandler(new
	 * BasicTradeHandler("entity.lotr.DolGuldurOrcTrader.name",
	 * LOTRTradeEntries.DOL_GULDUR_TRADER_BUY,
	 * LOTRTradeEntries.DOL_GULDUR_TRADER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityDolGuldurOrcTrader.class) ));
	 * registerHandler(new
	 * BasicTradeHandler("entity.lotr.HalfTrollScavenger.name",
	 * LOTRTradeEntries.HALF_TROLL_SCAVENGER_BUY,
	 * LOTRTradeEntries.HALF_TROLL_SCAVENGER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityHalfTrollScavenger.class) ));
	 * registerHandler(new BasicTradeHandler("entity.lotr.GaladhrimSmith.name",
	 * LOTRTradeEntries.GALADHRIM_SMITH_BUY,
	 * LOTRTradeEntries.GALADHRIM_SMITH_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityGaladhrimSmith.class)));
	 * registerHandler(new BasicTradeHandler("entity.lotr.HighElfSmith.name",
	 * LOTRTradeEntries.HIGH_ELF_SMITH_BUY,
	 * LOTRTradeEntries.HIGH_ELF_SMITH_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityHighElfSmith.class)));
	 * registerHandler(new BasicTradeHandler("entity.lotr.WoodElfSmith.name",
	 * LOTRTradeEntries.WOOD_ELF_SMITH_BUY,
	 * LOTRTradeEntries.WOOD_ELF_SMITH_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityWoodElfSmith.class)));
	 * registerHandler(new
	 * BasicTradeHandler("entity.lotr.MoredainHuntsman.name",
	 * LOTRTradeEntries.MOREDAIN_HUNTSMAN_BUY,
	 * LOTRTradeEntries.MOREDAIN_HUNTSMAN_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityMoredainHuntsman.class))) ;
	 * registerHandler(new
	 * BasicTradeHandler("entity.lotr.MoredainHutmaker.name",
	 * LOTRTradeEntries.MOREDAIN_HUTMAKER_BUY,
	 * LOTRTradeEntries.MOREDAIN_HUTMAKER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityMoredainHutmaker.class))) ;
	 * registerHandler(new
	 * BasicTradeHandler("entity.lotr.IronHillsMerchant.name",
	 * LOTRTradeEntries.IRON_HILLS_MERCHANT_BUY,
	 * LOTRTradeEntries.IRON_HILLS_MERCHANT_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityIronHillsMerchant.class)) );
	 * registerHandler(new BasicTradeHandler("entity.lotr.ScrapTrader.name",
	 * LOTRTradeEntries.SCRAP_TRADER_BUY, LOTRTradeEntries.SCRAP_TRADER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityScrapTrader.class)));
	 * registerHandler(new BasicTradeHandler("entity.lotr.TauredainShaman.name",
	 * LOTRTradeEntries.TAUREDAIN_SHAMAN_BUY,
	 * LOTRTradeEntries.TAUREDAIN_SHAMAN_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityTauredainShaman.class)));
	 * registerHandler(new BasicTradeHandler("entity.lotr.TauredainFarmer.name",
	 * LOTRTradeEntries.TAUREDAIN_FARMER_BUY,
	 * LOTRTradeEntries.TAUREDAIN_FARMER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityTauredainFarmer.class)));
	 * registerHandler(new BasicTradeHandler("entity.lotr.DwarfSmith.name",
	 * LOTRTradeEntries.DWARF_SMITH_BUY, LOTRTradeEntries.DWARF_SMITH_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityDwarfSmith.class)));
	 * registerHandler(new BasicTradeHandler("entity.lotr.BlueDwarfSmith.name",
	 * LOTRTradeEntries.BLUE_DWARF_SMITH_BUY,
	 * LOTRTradeEntries.BLUE_DWARF_SMITH_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityBlueMountainsSmith.class) ));
	 * registerHandler(new BasicTradeHandler("entity.lotr.DaleBlacksmith.name",
	 * LOTRTradeEntries.DALE_BLACKSMITH_BUY,
	 * LOTRTradeEntries.DALE_BLACKSMITH_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityDaleBlacksmith.class)));
	 * registerHandler(new BasicTradeHandler("entity.lotr.DaleBaker.name",
	 * LOTRTradeEntries.DALE_BAKER_BUY, LOTRTradeEntries.DALE_BAKER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityDaleBaker.class)));
	 * registerHandler(new BasicTradeHandler("entity.lotr.DaleMerchant.name",
	 * LOTRTradeEntries.DALE_MERCHANT_BUY, LOTRTradeEntries.DALE_MERCHANT_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityDaleMerchant.class)));
	 * registerHandler(new
	 * BasicTradeHandler("entity.lotr.DorwinionElfVintner.name",
	 * LOTRTradeEntries.DORWINION_VINTNER_BUY,
	 * LOTRTradeEntries.DORWINION_VINTNER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityDorwinionElfVintner.class
	 * ))); registerHandler(new
	 * BasicTradeHandler("entity.lotr.DorwinionVinekeeper.name",
	 * LOTRTradeEntries.DORWINION_VINEKEEPER_BUY,
	 * LOTRTradeEntries.DORWINION_VINEKEEPER_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityDorwinionVinekeeper.class
	 * ))); registerHandler(new
	 * BasicTradeHandler("entity.lotr.DorwinionMerchant.name",
	 * LOTRTradeEntries.DORWINION_MERCHANT_BUY,
	 * LOTRTradeEntries.DORWINION_MERCHANT_SELL,
	 * LOTREntities.getEntityIDFromClass(LOTREntityDorwinionMerchant.class)) );
	 * 
	 * }
	 * 
	 * // Hobbit Oven registerHandler(new HobbitOvenHandler(new
	 * LOTRTileEntityHobbitOven()));
	 * 
	 * // Kebab Stand registerHandler(new KebabHandler(new
	 * LOTRTileEntityKebabStand()));
	 * 
	 * // Kebab Stand registerHandler(new EntJarRecipeHandler());
	 * 
	 */

	// Item hiding

}
