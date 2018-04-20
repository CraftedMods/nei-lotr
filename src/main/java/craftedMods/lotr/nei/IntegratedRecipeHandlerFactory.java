package craftedMods.lotr.nei;

import static lotr.common.entity.npc.LOTRTradeEntries.*;

import java.util.*;
import java.util.function.Supplier;

import craftedMods.lotr.nei.handlers.*;
import craftedMods.lotr.nei.handlers.AlloyForgeRecipeHandler.*;
import craftedMods.recipes.api.*;
import lotr.client.gui.LOTRGuiCraftingTable;
import lotr.common.entity.npc.*;
import lotr.common.recipe.LOTRRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.EntityList;
import net.minecraft.item.crafting.IRecipe;

@RecipeHandlerFactory
public class IntegratedRecipeHandlerFactory implements Supplier<Set<RecipeHandler<?>>> {

	private static final Set<RecipeHandler<?>> recipeHandlers = new HashSet<>();

	private static final String FACTION_HOBBIT = "hobbit";
	private static final String FACTION_MORDOR = "mordor";
	private static final String FACTION_GONDOR = "gondor";
	private static final String FACTION_GALADHRIM = "galadhrim";
	private static final String FACTION_URUK_HAI = "urukHai";
	private static final String FACTION_DWARF = "dwarf";
	private static final String FACTION_ROHAN = "rohan";
	private static final String FACTION_DUNLENDING = "dunlending";
	private static final String FACTION_BLUE_DWARF = "blueDwarf";
	private static final String FACTION_NEAR_HARAD = "nearHarad";
	private static final String FACTION_ANGMAR = "angmar";
	private static final String FACTION_DOL_GULDUR = "dolGuldur";
	private static final String FACTION_HALF_TROLL = "halfTroll";
	private static final String FACTION_HIGH_ELF = "highElf";
	private static final String FACTION_WOOD_ELF = "woodElf";
	private static final String FACTION_MOREDAIN = "moredain";
	private static final String FACTION_TAUREDAIN = "tauredain";
	private static final String FACTION_DALE = "dale";
	private static final String FACTION_DORWINION = "dorwinion";
	private static final String FACTION_EASTERLING = "easterling";
	private static final String FACTION_DUNEDAIN = "dunedain";
	private static final String FACTION_RIVENDELL = "rivendell";
	private static final String FACTION_GUNDABAD = "gundabad";

	static {
		// Middle-earth tables
		IntegratedRecipeHandlerFactory.registerMECTHandler("angmar", LOTRGuiCraftingTable.Angmar.class, LOTRRecipes.angmarRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("blueDwarven", LOTRGuiCraftingTable.BlueDwarven.class, LOTRRecipes.blueMountainsRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("dale", LOTRGuiCraftingTable.Dale.class, LOTRRecipes.daleRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("dolAmroth", LOTRGuiCraftingTable.DolAmroth.class, LOTRRecipes.dolAmrothRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("dolGuldur", LOTRGuiCraftingTable.DolGuldur.class, LOTRRecipes.dolGuldurRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("dorwinion", LOTRGuiCraftingTable.Dorwinion.class, LOTRRecipes.dorwinionRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("dunlending", LOTRGuiCraftingTable.Dunlending.class, LOTRRecipes.dunlendingRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("dwarven", LOTRGuiCraftingTable.Dwarven.class, LOTRRecipes.dwarvenRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("elven", LOTRGuiCraftingTable.Elven.class, LOTRRecipes.elvenRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("gondorian", LOTRGuiCraftingTable.Gondorian.class, LOTRRecipes.gondorianRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("gundabad", LOTRGuiCraftingTable.Gundabad.class, LOTRRecipes.gundabadRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("halfTroll", LOTRGuiCraftingTable.HalfTroll.class, LOTRRecipes.halfTrollRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("highElven", LOTRGuiCraftingTable.HighElven.class, LOTRRecipes.highElvenRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("hobbit", LOTRGuiCraftingTable.Hobbit.class, LOTRRecipes.hobbitRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("moredain", LOTRGuiCraftingTable.Moredain.class, LOTRRecipes.moredainRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("morgul", LOTRGuiCraftingTable.Morgul.class, LOTRRecipes.morgulRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("nearHarad", LOTRGuiCraftingTable.NearHarad.class, LOTRRecipes.nearHaradRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("ranger", LOTRGuiCraftingTable.Ranger.class, LOTRRecipes.rangerRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("rhun", LOTRGuiCraftingTable.Rhun.class, LOTRRecipes.rhunRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("rivendell", LOTRGuiCraftingTable.Rivendell.class, LOTRRecipes.rivendellRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("rohirric", LOTRGuiCraftingTable.Rohirric.class, LOTRRecipes.rohirricRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("tauredain", LOTRGuiCraftingTable.Tauredain.class, LOTRRecipes.tauredainRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("uruk", LOTRGuiCraftingTable.Uruk.class, LOTRRecipes.urukRecipes);
		IntegratedRecipeHandlerFactory.registerMECTHandler("woodElven", LOTRGuiCraftingTable.WoodElven.class, LOTRRecipes.woodElvenRecipes);

		registerTraderHandler(LOTREntityHobbitBartender.class, FACTION_HOBBIT, HOBBIT_BARTENDER_SELL, HOBBIT_BARTENDER_BUY);
		registerTraderHandler(LOTREntityMordorOrcTrader.class, FACTION_MORDOR, MORDOR_TRADER_SELL, MORDOR_TRADER_BUY);
		registerTraderHandler(LOTREntityGondorBlacksmith.class, FACTION_GONDOR, GONDOR_BLACKSMITH_SELL, GONDOR_BLACKSMITH_BUY);
		registerTraderHandler(LOTREntityGaladhrimTrader.class, FACTION_GALADHRIM, GALADHRIM_TRADER_SELL, GALADHRIM_TRADER_BUY);
		registerTraderHandler(LOTREntityUrukHaiTrader.class, FACTION_URUK_HAI, URUK_HAI_TRADER_SELL, URUK_HAI_TRADER_BUY);
		registerTraderHandler(LOTREntityDwarfMiner.class, FACTION_DWARF, DWARF_MINER_SELL, DWARF_MINER_BUY);
		registerTraderHandler(LOTREntityRohanBlacksmith.class, FACTION_ROHAN, ROHAN_BLACKSMITH_SELL, ROHAN_BLACKSMITH_BUY);
		registerTraderHandler(LOTREntityDunlendingBartender.class, FACTION_DUNLENDING, DUNLENDING_BARTENDER_SELL, DUNLENDING_BARTENDER_BUY);
		registerTraderHandler(LOTREntityRohanMeadhost.class, FACTION_ROHAN, ROHAN_MEADHOST_SELL, ROHAN_MEADHOST_BUY);
		registerTraderHandler(LOTREntityHobbitOrcharder.class, FACTION_HOBBIT, HOBBIT_ORCHARDER_SELL, HOBBIT_ORCHARDER_BUY);
		registerTraderHandler(LOTREntityHobbitFarmer.class, FACTION_HOBBIT, HOBBIT_FARMER_SELL, HOBBIT_FARMER_BUY);
		registerTraderHandler(LOTREntityBlueDwarfMiner.class, FACTION_BLUE_DWARF, BLUE_DWARF_MINER_SELL, BLUE_DWARF_MINER_BUY);
		registerTraderHandler(LOTREntityNearHaradDrinksTrader.class, FACTION_NEAR_HARAD, NEAR_HARAD_DRINKS_TRADER_SELL, NEAR_HARAD_DRINKS_TRADER_BUY);
		registerTraderHandler(LOTREntityNearHaradMineralsTrader.class, FACTION_NEAR_HARAD, NEAR_HARAD_MINERALS_TRADER_SELL, NEAR_HARAD_MINERALS_TRADER_BUY);
		registerTraderHandler(LOTREntityNearHaradPlantsTrader.class, FACTION_NEAR_HARAD, LOTRTradeEntries.NEAR_HARAD_PLANTS_TRADER_SELL,
				LOTRTradeEntries.NEAR_HARAD_PLANTS_TRADER_BUY);
		registerTraderHandler(LOTREntityNearHaradFoodTrader.class, FACTION_NEAR_HARAD, NEAR_HARAD_FOOD_TRADER_SELL, NEAR_HARAD_FOOD_TRADER_BUY);
		registerTraderHandler(LOTREntityBlueDwarfMerchant.class, FACTION_BLUE_DWARF, BLUE_DWARF_MERCHANT_SELL, BLUE_DWARF_MERCHANT_BUY);
		registerTraderHandler(LOTREntityNearHaradMerchant.class, FACTION_NEAR_HARAD, NEAR_HARAD_MERCHANT_SELL, NEAR_HARAD_MERCHANT_BUY);
		registerTraderHandler(LOTREntityAngmarOrcTrader.class, FACTION_ANGMAR, ANGMAR_TRADER_SELL, ANGMAR_TRADER_BUY);
		registerTraderHandler(LOTREntityDolGuldurOrcTrader.class, FACTION_DOL_GULDUR, DOL_GULDUR_TRADER_SELL, DOL_GULDUR_TRADER_BUY);
		registerTraderHandler(LOTREntityHalfTrollScavenger.class, FACTION_HALF_TROLL, HALF_TROLL_SCAVENGER_SELL, HALF_TROLL_SCAVENGER_BUY);
		registerTraderHandler(LOTREntityGaladhrimSmith.class, FACTION_GALADHRIM, GALADHRIM_SMITH_SELL, GALADHRIM_SMITH_BUY);
		registerTraderHandler(LOTREntityHighElfSmith.class, FACTION_HIGH_ELF, HIGH_ELF_SMITH_SELL, HIGH_ELF_SMITH_BUY);
		registerTraderHandler(LOTREntityWoodElfSmith.class, FACTION_WOOD_ELF, WOOD_ELF_SMITH_SELL, WOOD_ELF_SMITH_BUY);
		registerTraderHandler(LOTREntityMoredainHuntsman.class, FACTION_MOREDAIN, MOREDAIN_HUNTSMAN_SELL, MOREDAIN_HUNTSMAN_BUY);
		registerTraderHandler(LOTREntityMoredainHutmaker.class, FACTION_MOREDAIN, MOREDAIN_HUTMAKER_SELL, MOREDAIN_HUTMAKER_BUY);
		registerTraderHandler(LOTREntityIronHillsMerchant.class, FACTION_DWARF, IRON_HILLS_MERCHANT_SELL, IRON_HILLS_MERCHANT_BUY);
		registerTraderHandler(LOTREntityScrapTrader.class, null, SCRAP_TRADER_SELL, SCRAP_TRADER_BUY);
		registerTraderHandler(LOTREntityTauredainShaman.class, FACTION_TAUREDAIN, TAUREDAIN_SHAMAN_SELL, TAUREDAIN_SHAMAN_BUY);
		registerTraderHandler(LOTREntityTauredainFarmer.class, FACTION_TAUREDAIN, TAUREDAIN_FARMER_SELL, TAUREDAIN_FARMER_BUY);
		registerTraderHandler(LOTREntityDwarfSmith.class, FACTION_DWARF, DWARF_SMITH_SELL, DWARF_SMITH_BUY);
		registerTraderHandler(LOTREntityBlueMountainsSmith.class, FACTION_BLUE_DWARF, BLUE_DWARF_SMITH_SELL, BLUE_DWARF_SMITH_BUY);
		registerTraderHandler(LOTREntityDaleBlacksmith.class, FACTION_DALE, DALE_BLACKSMITH_SELL, DALE_BLACKSMITH_BUY);
		registerTraderHandler(LOTREntityDaleBaker.class, FACTION_DALE, DALE_BAKER_SELL, DALE_BAKER_BUY);
		registerTraderHandler(LOTREntityDorwinionElfVintner.class, FACTION_DORWINION, DORWINION_VINTNER_SELL, DORWINION_VINTNER_BUY);
		registerTraderHandler(LOTREntityDorwinionVinekeeper.class, FACTION_DORWINION, DORWINION_VINEKEEPER_SELL, DORWINION_VINEKEEPER_BUY);
		registerTraderHandler(LOTREntityDorwinionMerchantElf.class, FACTION_DORWINION, DORWINION_MERCHANT_SELL, DORWINION_MERCHANT_BUY);
		registerTraderHandler(LOTREntityDaleMerchant.class, FACTION_DALE, DALE_MERCHANT_SELL, DALE_MERCHANT_BUY);
		registerTraderHandler(LOTREntityGondorFarmer.class, FACTION_GONDOR, GONDOR_FARMER_SELL, GONDOR_FARMER_BUY);
		registerTraderHandler(LOTREntityGondorBartender.class, FACTION_GONDOR, GONDOR_BARTENDER_SELL, GONDOR_BARTENDER_BUY);
		registerTraderHandler(LOTREntityGondorGreengrocer.class, FACTION_GONDOR, GONDOR_GREENGROCER_SELL, GONDOR_GREENGROCER_BUY);
		registerTraderHandler(LOTREntityGondorLumberman.class, FACTION_GONDOR, GONDOR_LUMBERMAN_SELL, GONDOR_LUMBERMAN_BUY);
		registerTraderHandler(LOTREntityGondorMason.class, FACTION_GONDOR, GONDOR_MASON_SELL, GONDOR_MASON_BUY);
		registerTraderHandler(LOTREntityGondorBrewer.class, FACTION_GONDOR, GONDOR_BREWER_SELL, GONDOR_BREWER_BUY);
		registerTraderHandler(LOTREntityGondorFlorist.class, FACTION_GONDOR, GONDOR_FLORIST_SELL, GONDOR_FLORIST_BUY);
		registerTraderHandler(LOTREntityGondorButcher.class, FACTION_GONDOR, GONDOR_BUTCHER_SELL, GONDOR_BUTCHER_BUY);
		registerTraderHandler(LOTREntityGondorFishmonger.class, FACTION_GONDOR, GONDOR_FISHMONGER_SELL, GONDOR_FISHMONGER_BUY);
		registerTraderHandler(LOTREntityGondorBaker.class, FACTION_GONDOR, GONDOR_BAKER_SELL, GONDOR_BAKER_BUY);
		registerTraderHandler(LOTREntityRohanFarmer.class, FACTION_ROHAN, ROHAN_FARMER_SELL, ROHAN_FARMER_BUY);
		registerTraderHandler(LOTREntityRohanLumberman.class, FACTION_ROHAN, ROHAN_LUMBERMAN_SELL, ROHAN_LUMBERMAN_BUY);
		registerTraderHandler(LOTREntityRohanBuilder.class, FACTION_ROHAN, ROHAN_BUILDER_SELL, ROHAN_BUILDER_BUY);
		registerTraderHandler(LOTREntityRohanBrewer.class, FACTION_ROHAN, ROHAN_BREWER_SELL, ROHAN_BREWER_BUY);
		registerTraderHandler(LOTREntityRohanButcher.class, FACTION_ROHAN, ROHAN_BUTCHER_SELL, ROHAN_BUTCHER_BUY);
		registerTraderHandler(LOTREntityRohanFishmonger.class, FACTION_ROHAN, ROHAN_FISHMONGER_SELL, ROHAN_FISHMONGER_BUY);
		registerTraderHandler(LOTREntityRohanBaker.class, FACTION_ROHAN, ROHAN_BAKER_SELL, ROHAN_BAKER_BUY);
		registerTraderHandler(LOTREntityRohanOrcharder.class, FACTION_ROHAN, ROHAN_ORCHARDER_SELL, ROHAN_ORCHARDER_BUY);
		registerTraderHandler(LOTREntityDunedainBlacksmith.class, FACTION_DUNEDAIN, DUNEDAIN_BLACKSMITH_SELL, DUNEDAIN_BLACKSMITH_BUY);
		registerTraderHandler(LOTREntityRohanStablemaster.class, FACTION_ROHAN, ROHAN_STABLEMASTER_SELL, ROHAN_STABLEMASTER_BUY);
		registerTraderHandler(LOTREntityEasterlingBlacksmith.class, FACTION_EASTERLING, RHUN_BLACKSMITH_SELL, RHUN_BLACKSMITH_BUY);
		registerTraderHandler(LOTREntityDorwinionMerchantMan.class, FACTION_DORWINION, DORWINION_MERCHANT_SELL, DORWINION_MERCHANT_BUY);
		registerTraderHandler(LOTREntityEasterlingLumberman.class, FACTION_EASTERLING, RHUN_LUMBERMAN_SELL, RHUN_LUMBERMAN_BUY);
		registerTraderHandler(LOTREntityEasterlingMason.class, FACTION_EASTERLING, RHUN_MASON_SELL, RHUN_MASON_BUY);
		registerTraderHandler(LOTREntityEasterlingButcher.class, FACTION_EASTERLING, RHUN_BUTCHER_SELL, RHUN_BUTCHER_BUY);
		registerTraderHandler(LOTREntityEasterlingBrewer.class, FACTION_EASTERLING, RHUN_BREWER_SELL, RHUN_BREWER_BUY);
		registerTraderHandler(LOTREntityEasterlingFishmonger.class, FACTION_EASTERLING, RHUN_FISHMONGER_SELL, RHUN_FISHMONGER_BUY);
		registerTraderHandler(LOTREntityEasterlingBaker.class, FACTION_EASTERLING, RHUN_BAKER_SELL, RHUN_BAKER_BUY);
		registerTraderHandler(LOTREntityEasterlingHunter.class, FACTION_EASTERLING, RHUN_HUNTER_SELL, RHUN_HUNTER_BUY);
		registerTraderHandler(LOTREntityEasterlingFarmer.class, FACTION_EASTERLING, RHUN_FARMER_SELL, RHUN_FARMER_BUY);
		registerTraderHandler(LOTREntityEasterlingGoldsmith.class, FACTION_EASTERLING, RHUN_GOLDSMITH_SELL, RHUN_GOLDSMITH_BUY);
		registerTraderHandler(LOTREntityEasterlingBartender.class, FACTION_EASTERLING, RHUN_BARTENDER_SELL, RHUN_BARTENDER_BUY);
		registerTraderHandler(LOTREntityRivendellTrader.class, FACTION_RIVENDELL, RIVENDELL_TRADER_SELL, RIVENDELL_TRADER_BUY);
		registerTraderHandler(LOTREntityRivendellSmith.class, FACTION_RIVENDELL, RIVENDELL_SMITH_SELL, RIVENDELL_SMITH_BUY);
		registerTraderHandler(LOTREntityGundabadOrcTrader.class, FACTION_GUNDABAD, GUNDABAD_TRADER_SELL, GUNDABAD_TRADER_BUY);
		registerTraderHandler(LOTREntityNearHaradBlacksmith.class, FACTION_NEAR_HARAD, NEAR_HARAD_BLACKSMITH_SELL, NEAR_HARAD_BLACKSMITH_BUY);

		recipeHandlers.add(new AlloyForgeRecipeHandler("orc", new AlloyForgeRecipeHandler.OrcForgeAccess()));
		recipeHandlers.add(new AlloyForgeRecipeHandler("men", new AlloyForgeRecipeHandler.MenForgeAccess()));
		recipeHandlers.add(new AlloyForgeRecipeHandler("elven", new AlloyForgeRecipeHandler.ElvenForgeAccess()));
		recipeHandlers.add(new AlloyForgeRecipeHandler("dwarven", new AlloyForgeRecipeHandler.DwarvenForgeAccess()));
	}

	private static void registerMECTHandler(String unlocalizedName, Class<? extends GuiContainer> guiClass, Collection<IRecipe> recipes) {
		IntegratedRecipeHandlerFactory.recipeHandlers.add(new MiddleEarthCraftingTableRecipeHandler(unlocalizedName, guiClass, recipes));
	}

	private static void registerTraderHandler(Class<? extends LOTRTradeable> entityClass, String faction, LOTRTradeEntries itemsBought,
			LOTRTradeEntries itemsSold) {
		recipeHandlers.add(
				new TraderRecipeHandler(EntityList.classToStringMapping.get(entityClass).toString().replace("lotr.", ""), faction, itemsBought, itemsSold));
	}

	@Override
	public Set<RecipeHandler<?>> get() {
		return IntegratedRecipeHandlerFactory.recipeHandlers;
	}
}
