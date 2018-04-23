package craftedMods.lotr.nei;

import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;

import craftedMods.lotr.nei.handlers.*;
import craftedMods.recipes.api.*;
import craftedMods.recipes.base.*;
import lotr.client.gui.LOTRGuiCraftingTable;
import lotr.common.entity.npc.*;
import lotr.common.recipe.LOTRRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.EntityList;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

@RegisteredHandler
public class IntegratedRecipeHandlerFactory implements RecipeHandlerFactory {

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

		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityHobbitBartender.class, IntegratedRecipeHandlerFactory.FACTION_HOBBIT,
				LOTRTradeEntries.HOBBIT_BARTENDER_SELL, LOTRTradeEntries.HOBBIT_BARTENDER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityMordorOrcTrader.class, IntegratedRecipeHandlerFactory.FACTION_MORDOR,
				LOTRTradeEntries.MORDOR_TRADER_SELL, LOTRTradeEntries.MORDOR_TRADER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityGondorBlacksmith.class, IntegratedRecipeHandlerFactory.FACTION_GONDOR,
				LOTRTradeEntries.GONDOR_BLACKSMITH_SELL, LOTRTradeEntries.GONDOR_BLACKSMITH_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityGaladhrimTrader.class, IntegratedRecipeHandlerFactory.FACTION_GALADHRIM,
				LOTRTradeEntries.GALADHRIM_TRADER_SELL, LOTRTradeEntries.GALADHRIM_TRADER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityUrukHaiTrader.class, IntegratedRecipeHandlerFactory.FACTION_URUK_HAI,
				LOTRTradeEntries.URUK_HAI_TRADER_SELL, LOTRTradeEntries.URUK_HAI_TRADER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityDwarfMiner.class, IntegratedRecipeHandlerFactory.FACTION_DWARF,
				LOTRTradeEntries.DWARF_MINER_SELL, LOTRTradeEntries.DWARF_MINER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityRohanBlacksmith.class, IntegratedRecipeHandlerFactory.FACTION_ROHAN,
				LOTRTradeEntries.ROHAN_BLACKSMITH_SELL, LOTRTradeEntries.ROHAN_BLACKSMITH_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityDunlendingBartender.class, IntegratedRecipeHandlerFactory.FACTION_DUNLENDING,
				LOTRTradeEntries.DUNLENDING_BARTENDER_SELL, LOTRTradeEntries.DUNLENDING_BARTENDER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityRohanMeadhost.class, IntegratedRecipeHandlerFactory.FACTION_ROHAN,
				LOTRTradeEntries.ROHAN_MEADHOST_SELL, LOTRTradeEntries.ROHAN_MEADHOST_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityHobbitOrcharder.class, IntegratedRecipeHandlerFactory.FACTION_HOBBIT,
				LOTRTradeEntries.HOBBIT_ORCHARDER_SELL, LOTRTradeEntries.HOBBIT_ORCHARDER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityHobbitFarmer.class, IntegratedRecipeHandlerFactory.FACTION_HOBBIT,
				LOTRTradeEntries.HOBBIT_FARMER_SELL, LOTRTradeEntries.HOBBIT_FARMER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityBlueDwarfMiner.class, IntegratedRecipeHandlerFactory.FACTION_BLUE_DWARF,
				LOTRTradeEntries.BLUE_DWARF_MINER_SELL, LOTRTradeEntries.BLUE_DWARF_MINER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityNearHaradDrinksTrader.class, IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD,
				LOTRTradeEntries.NEAR_HARAD_DRINKS_TRADER_SELL, LOTRTradeEntries.NEAR_HARAD_DRINKS_TRADER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityNearHaradMineralsTrader.class, IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD,
				LOTRTradeEntries.NEAR_HARAD_MINERALS_TRADER_SELL, LOTRTradeEntries.NEAR_HARAD_MINERALS_TRADER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityNearHaradPlantsTrader.class, IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD,
				LOTRTradeEntries.NEAR_HARAD_PLANTS_TRADER_SELL, LOTRTradeEntries.NEAR_HARAD_PLANTS_TRADER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityNearHaradFoodTrader.class, IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD,
				LOTRTradeEntries.NEAR_HARAD_FOOD_TRADER_SELL, LOTRTradeEntries.NEAR_HARAD_FOOD_TRADER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityBlueDwarfMerchant.class, IntegratedRecipeHandlerFactory.FACTION_BLUE_DWARF,
				LOTRTradeEntries.BLUE_DWARF_MERCHANT_SELL, LOTRTradeEntries.BLUE_DWARF_MERCHANT_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityNearHaradMerchant.class, IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD,
				LOTRTradeEntries.NEAR_HARAD_MERCHANT_SELL, LOTRTradeEntries.NEAR_HARAD_MERCHANT_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityAngmarOrcTrader.class, IntegratedRecipeHandlerFactory.FACTION_ANGMAR,
				LOTRTradeEntries.ANGMAR_TRADER_SELL, LOTRTradeEntries.ANGMAR_TRADER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityDolGuldurOrcTrader.class, IntegratedRecipeHandlerFactory.FACTION_DOL_GULDUR,
				LOTRTradeEntries.DOL_GULDUR_TRADER_SELL, LOTRTradeEntries.DOL_GULDUR_TRADER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityHalfTrollScavenger.class, IntegratedRecipeHandlerFactory.FACTION_HALF_TROLL,
				LOTRTradeEntries.HALF_TROLL_SCAVENGER_SELL, LOTRTradeEntries.HALF_TROLL_SCAVENGER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityGaladhrimSmith.class, IntegratedRecipeHandlerFactory.FACTION_GALADHRIM,
				LOTRTradeEntries.GALADHRIM_SMITH_SELL, LOTRTradeEntries.GALADHRIM_SMITH_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityHighElfSmith.class, IntegratedRecipeHandlerFactory.FACTION_HIGH_ELF,
				LOTRTradeEntries.HIGH_ELF_SMITH_SELL, LOTRTradeEntries.HIGH_ELF_SMITH_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityWoodElfSmith.class, IntegratedRecipeHandlerFactory.FACTION_WOOD_ELF,
				LOTRTradeEntries.WOOD_ELF_SMITH_SELL, LOTRTradeEntries.WOOD_ELF_SMITH_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityMoredainHuntsman.class, IntegratedRecipeHandlerFactory.FACTION_MOREDAIN,
				LOTRTradeEntries.MOREDAIN_HUNTSMAN_SELL, LOTRTradeEntries.MOREDAIN_HUNTSMAN_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityMoredainHutmaker.class, IntegratedRecipeHandlerFactory.FACTION_MOREDAIN,
				LOTRTradeEntries.MOREDAIN_HUTMAKER_SELL, LOTRTradeEntries.MOREDAIN_HUTMAKER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityIronHillsMerchant.class, IntegratedRecipeHandlerFactory.FACTION_DWARF,
				LOTRTradeEntries.IRON_HILLS_MERCHANT_SELL, LOTRTradeEntries.IRON_HILLS_MERCHANT_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityScrapTrader.class, null, LOTRTradeEntries.SCRAP_TRADER_SELL,
				LOTRTradeEntries.SCRAP_TRADER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityTauredainShaman.class, IntegratedRecipeHandlerFactory.FACTION_TAUREDAIN,
				LOTRTradeEntries.TAUREDAIN_SHAMAN_SELL, LOTRTradeEntries.TAUREDAIN_SHAMAN_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityTauredainFarmer.class, IntegratedRecipeHandlerFactory.FACTION_TAUREDAIN,
				LOTRTradeEntries.TAUREDAIN_FARMER_SELL, LOTRTradeEntries.TAUREDAIN_FARMER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityDwarfSmith.class, IntegratedRecipeHandlerFactory.FACTION_DWARF,
				LOTRTradeEntries.DWARF_SMITH_SELL, LOTRTradeEntries.DWARF_SMITH_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityBlueMountainsSmith.class, IntegratedRecipeHandlerFactory.FACTION_BLUE_DWARF,
				LOTRTradeEntries.BLUE_DWARF_SMITH_SELL, LOTRTradeEntries.BLUE_DWARF_SMITH_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityDaleBlacksmith.class, IntegratedRecipeHandlerFactory.FACTION_DALE,
				LOTRTradeEntries.DALE_BLACKSMITH_SELL, LOTRTradeEntries.DALE_BLACKSMITH_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityDaleBaker.class, IntegratedRecipeHandlerFactory.FACTION_DALE,
				LOTRTradeEntries.DALE_BAKER_SELL, LOTRTradeEntries.DALE_BAKER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityDorwinionElfVintner.class, IntegratedRecipeHandlerFactory.FACTION_DORWINION,
				LOTRTradeEntries.DORWINION_VINTNER_SELL, LOTRTradeEntries.DORWINION_VINTNER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityDorwinionVinekeeper.class, IntegratedRecipeHandlerFactory.FACTION_DORWINION,
				LOTRTradeEntries.DORWINION_VINEKEEPER_SELL, LOTRTradeEntries.DORWINION_VINEKEEPER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityDorwinionMerchantElf.class, IntegratedRecipeHandlerFactory.FACTION_DORWINION,
				LOTRTradeEntries.DORWINION_MERCHANT_SELL, LOTRTradeEntries.DORWINION_MERCHANT_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityDaleMerchant.class, IntegratedRecipeHandlerFactory.FACTION_DALE,
				LOTRTradeEntries.DALE_MERCHANT_SELL, LOTRTradeEntries.DALE_MERCHANT_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityGondorFarmer.class, IntegratedRecipeHandlerFactory.FACTION_GONDOR,
				LOTRTradeEntries.GONDOR_FARMER_SELL, LOTRTradeEntries.GONDOR_FARMER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityGondorBartender.class, IntegratedRecipeHandlerFactory.FACTION_GONDOR,
				LOTRTradeEntries.GONDOR_BARTENDER_SELL, LOTRTradeEntries.GONDOR_BARTENDER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityGondorGreengrocer.class, IntegratedRecipeHandlerFactory.FACTION_GONDOR,
				LOTRTradeEntries.GONDOR_GREENGROCER_SELL, LOTRTradeEntries.GONDOR_GREENGROCER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityGondorLumberman.class, IntegratedRecipeHandlerFactory.FACTION_GONDOR,
				LOTRTradeEntries.GONDOR_LUMBERMAN_SELL, LOTRTradeEntries.GONDOR_LUMBERMAN_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityGondorMason.class, IntegratedRecipeHandlerFactory.FACTION_GONDOR,
				LOTRTradeEntries.GONDOR_MASON_SELL, LOTRTradeEntries.GONDOR_MASON_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityGondorBrewer.class, IntegratedRecipeHandlerFactory.FACTION_GONDOR,
				LOTRTradeEntries.GONDOR_BREWER_SELL, LOTRTradeEntries.GONDOR_BREWER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityGondorFlorist.class, IntegratedRecipeHandlerFactory.FACTION_GONDOR,
				LOTRTradeEntries.GONDOR_FLORIST_SELL, LOTRTradeEntries.GONDOR_FLORIST_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityGondorButcher.class, IntegratedRecipeHandlerFactory.FACTION_GONDOR,
				LOTRTradeEntries.GONDOR_BUTCHER_SELL, LOTRTradeEntries.GONDOR_BUTCHER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityGondorFishmonger.class, IntegratedRecipeHandlerFactory.FACTION_GONDOR,
				LOTRTradeEntries.GONDOR_FISHMONGER_SELL, LOTRTradeEntries.GONDOR_FISHMONGER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityGondorBaker.class, IntegratedRecipeHandlerFactory.FACTION_GONDOR,
				LOTRTradeEntries.GONDOR_BAKER_SELL, LOTRTradeEntries.GONDOR_BAKER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityRohanFarmer.class, IntegratedRecipeHandlerFactory.FACTION_ROHAN,
				LOTRTradeEntries.ROHAN_FARMER_SELL, LOTRTradeEntries.ROHAN_FARMER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityRohanLumberman.class, IntegratedRecipeHandlerFactory.FACTION_ROHAN,
				LOTRTradeEntries.ROHAN_LUMBERMAN_SELL, LOTRTradeEntries.ROHAN_LUMBERMAN_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityRohanBuilder.class, IntegratedRecipeHandlerFactory.FACTION_ROHAN,
				LOTRTradeEntries.ROHAN_BUILDER_SELL, LOTRTradeEntries.ROHAN_BUILDER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityRohanBrewer.class, IntegratedRecipeHandlerFactory.FACTION_ROHAN,
				LOTRTradeEntries.ROHAN_BREWER_SELL, LOTRTradeEntries.ROHAN_BREWER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityRohanButcher.class, IntegratedRecipeHandlerFactory.FACTION_ROHAN,
				LOTRTradeEntries.ROHAN_BUTCHER_SELL, LOTRTradeEntries.ROHAN_BUTCHER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityRohanFishmonger.class, IntegratedRecipeHandlerFactory.FACTION_ROHAN,
				LOTRTradeEntries.ROHAN_FISHMONGER_SELL, LOTRTradeEntries.ROHAN_FISHMONGER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityRohanBaker.class, IntegratedRecipeHandlerFactory.FACTION_ROHAN,
				LOTRTradeEntries.ROHAN_BAKER_SELL, LOTRTradeEntries.ROHAN_BAKER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityRohanOrcharder.class, IntegratedRecipeHandlerFactory.FACTION_ROHAN,
				LOTRTradeEntries.ROHAN_ORCHARDER_SELL, LOTRTradeEntries.ROHAN_ORCHARDER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityDunedainBlacksmith.class, IntegratedRecipeHandlerFactory.FACTION_DUNEDAIN,
				LOTRTradeEntries.DUNEDAIN_BLACKSMITH_SELL, LOTRTradeEntries.DUNEDAIN_BLACKSMITH_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityRohanStablemaster.class, IntegratedRecipeHandlerFactory.FACTION_ROHAN,
				LOTRTradeEntries.ROHAN_STABLEMASTER_SELL, LOTRTradeEntries.ROHAN_STABLEMASTER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityEasterlingBlacksmith.class, IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
				LOTRTradeEntries.RHUN_BLACKSMITH_SELL, LOTRTradeEntries.RHUN_BLACKSMITH_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityDorwinionMerchantMan.class, IntegratedRecipeHandlerFactory.FACTION_DORWINION,
				LOTRTradeEntries.DORWINION_MERCHANT_SELL, LOTRTradeEntries.DORWINION_MERCHANT_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityEasterlingLumberman.class, IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
				LOTRTradeEntries.RHUN_LUMBERMAN_SELL, LOTRTradeEntries.RHUN_LUMBERMAN_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityEasterlingMason.class, IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
				LOTRTradeEntries.RHUN_MASON_SELL, LOTRTradeEntries.RHUN_MASON_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityEasterlingButcher.class, IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
				LOTRTradeEntries.RHUN_BUTCHER_SELL, LOTRTradeEntries.RHUN_BUTCHER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityEasterlingBrewer.class, IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
				LOTRTradeEntries.RHUN_BREWER_SELL, LOTRTradeEntries.RHUN_BREWER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityEasterlingFishmonger.class, IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
				LOTRTradeEntries.RHUN_FISHMONGER_SELL, LOTRTradeEntries.RHUN_FISHMONGER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityEasterlingBaker.class, IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
				LOTRTradeEntries.RHUN_BAKER_SELL, LOTRTradeEntries.RHUN_BAKER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityEasterlingHunter.class, IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
				LOTRTradeEntries.RHUN_HUNTER_SELL, LOTRTradeEntries.RHUN_HUNTER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityEasterlingFarmer.class, IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
				LOTRTradeEntries.RHUN_FARMER_SELL, LOTRTradeEntries.RHUN_FARMER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityEasterlingGoldsmith.class, IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
				LOTRTradeEntries.RHUN_GOLDSMITH_SELL, LOTRTradeEntries.RHUN_GOLDSMITH_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityEasterlingBartender.class, IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
				LOTRTradeEntries.RHUN_BARTENDER_SELL, LOTRTradeEntries.RHUN_BARTENDER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityRivendellTrader.class, IntegratedRecipeHandlerFactory.FACTION_RIVENDELL,
				LOTRTradeEntries.RIVENDELL_TRADER_SELL, LOTRTradeEntries.RIVENDELL_TRADER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityRivendellSmith.class, IntegratedRecipeHandlerFactory.FACTION_RIVENDELL,
				LOTRTradeEntries.RIVENDELL_SMITH_SELL, LOTRTradeEntries.RIVENDELL_SMITH_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityGundabadOrcTrader.class, IntegratedRecipeHandlerFactory.FACTION_GUNDABAD,
				LOTRTradeEntries.GUNDABAD_TRADER_SELL, LOTRTradeEntries.GUNDABAD_TRADER_BUY);
		IntegratedRecipeHandlerFactory.registerTraderHandler(LOTREntityNearHaradBlacksmith.class, IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD,
				LOTRTradeEntries.NEAR_HARAD_BLACKSMITH_SELL, LOTRTradeEntries.NEAR_HARAD_BLACKSMITH_BUY);

		IntegratedRecipeHandlerFactory.recipeHandlers.add(new AlloyForgeRecipeHandler("orc", new AlloyForgeRecipeHandler.OrcForgeAccess()));
		IntegratedRecipeHandlerFactory.recipeHandlers.add(new AlloyForgeRecipeHandler("men", new AlloyForgeRecipeHandler.MenForgeAccess()));
		IntegratedRecipeHandlerFactory.recipeHandlers.add(new AlloyForgeRecipeHandler("elven", new AlloyForgeRecipeHandler.ElvenForgeAccess()));
		IntegratedRecipeHandlerFactory.recipeHandlers.add(new AlloyForgeRecipeHandler("dwarven", new AlloyForgeRecipeHandler.DwarvenForgeAccess()));
	}

	private static void registerMECTHandler(String unlocalizedName, Class<? extends GuiContainer> guiClass, Collection<IRecipe> recipes) {
		IntegratedRecipeHandlerFactory.recipeHandlers.add(new MiddleEarthCraftingTableRecipeHandler(unlocalizedName, guiClass, recipes));
	}

	private static void registerTraderHandler(Class<? extends LOTRTradeable> entityClass, String faction, LOTRTradeEntries itemsBought,
			LOTRTradeEntries itemsSold) {
		IntegratedRecipeHandlerFactory.recipeHandlers.add(
				new TraderRecipeHandler(EntityList.classToStringMapping.get(entityClass).toString().replace("lotr.", ""), faction, itemsBought, itemsSold));
	}

	@Override
	public Set<RecipeHandler<?>> getRecipeHandlers() {
		return IntegratedRecipeHandlerFactory.recipeHandlers;
	}

	@Override
	public Map<ResourceLocation, Supplier<InputStream>> getResources() {
		RecipeHandlerResourceLoader resourceLoader = new ClasspathResourceLoader();
		resourceLoader.registerResource(new RecipeHandlerResourceLocation("lang/en_US.lang"));
		resourceLoader.registerResource(new RecipeHandlerResourceLocation("lang/de_DE.lang"));
		return resourceLoader.loadResources();
	}
}
