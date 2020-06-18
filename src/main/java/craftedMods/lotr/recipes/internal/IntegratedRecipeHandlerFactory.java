/*******************************************************************************
 * Copyright (C) 2020 CraftedMods (see https://github.com/CraftedMods)
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
package craftedMods.lotr.recipes.internal;

import java.util.*;

import org.apache.logging.log4j.*;

import craftedMods.lotr.recipes.api.utils.LOTRRecipeHandlerUtils;
import craftedMods.lotr.recipes.internal.recipeHandlers.*;
import craftedMods.recipes.api.*;
import lotr.client.LOTRGuiHandler;
import lotr.client.gui.LOTRGuiCraftingTable;
import lotr.common.entity.npc.*;
import lotr.common.recipe.LOTRRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.crafting.IRecipe;

@RegisteredHandler
public class IntegratedRecipeHandlerFactory implements RecipeHandlerFactory
{

    private static Logger logger = LogManager.getLogger (IntegratedRecipeHandlerFactory.class);

    private static final Set<RecipeHandler<?>> recipeHandlers = new HashSet<> ();

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
    private static final String FACTION_NEAR_HARAD_SOUTHRON = IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD
        + ".southron";
    private static final String FACTION_NEAR_HARAD_UMBAR = IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD + ".umbar";
    private static final String FACTION_NEAR_HARAD_GULF = IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD + ".gulf";
    private static final String FACTION_NEAR_HARAD_HARNEDOR = IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD
        + ".harnedor";
    private static final String FACTION_NEAR_HARAD_NOMAD = IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD + ".nomad";
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
    private static final String FACTION_BREE = "bree";

    static
    {
        // Middle-earth tables
        IntegratedRecipeHandlerFactory.registerMECTHandler ("angmar", LOTRGuiCraftingTable.Angmar.class,
            LOTRRecipes.angmarRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("blueDwarven", LOTRGuiCraftingTable.BlueDwarven.class,
            LOTRRecipes.blueMountainsRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("dale", LOTRGuiCraftingTable.Dale.class,
            LOTRRecipes.daleRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("dolAmroth", LOTRGuiCraftingTable.DolAmroth.class,
            LOTRRecipes.dolAmrothRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("dolGuldur", LOTRGuiCraftingTable.DolGuldur.class,
            LOTRRecipes.dolGuldurRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("dorwinion", LOTRGuiCraftingTable.Dorwinion.class,
            LOTRRecipes.dorwinionRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("dunlending", LOTRGuiCraftingTable.Dunlending.class,
            LOTRRecipes.dunlendingRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("dwarven", LOTRGuiCraftingTable.Dwarven.class,
            LOTRRecipes.dwarvenRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("elven", LOTRGuiCraftingTable.Elven.class,
            LOTRRecipes.elvenRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("gondorian", LOTRGuiCraftingTable.Gondorian.class,
            LOTRRecipes.gondorianRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("gulf", LOTRGuiCraftingTable.Gulf.class,
            LOTRRecipes.gulfRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("gundabad", LOTRGuiCraftingTable.Gundabad.class,
            LOTRRecipes.gundabadRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("halfTroll", LOTRGuiCraftingTable.HalfTroll.class,
            LOTRRecipes.halfTrollRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("highElven", LOTRGuiCraftingTable.HighElven.class,
            LOTRRecipes.highElvenRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("hobbit", LOTRGuiCraftingTable.Hobbit.class,
            LOTRRecipes.hobbitRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("moredain", LOTRGuiCraftingTable.Moredain.class,
            LOTRRecipes.moredainRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("morgul", LOTRGuiCraftingTable.Morgul.class,
            LOTRRecipes.morgulRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("nearHarad", LOTRGuiCraftingTable.NearHarad.class,
            LOTRRecipes.nearHaradRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("ranger", LOTRGuiCraftingTable.Ranger.class,
            LOTRRecipes.rangerRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("rhun", LOTRGuiCraftingTable.Rhun.class,
            LOTRRecipes.rhunRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("rivendell", LOTRGuiCraftingTable.Rivendell.class,
            LOTRRecipes.rivendellRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("rohirric", LOTRGuiCraftingTable.Rohirric.class,
            LOTRRecipes.rohirricRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("tauredain", LOTRGuiCraftingTable.Tauredain.class,
            LOTRRecipes.tauredainRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("uruk", LOTRGuiCraftingTable.Uruk.class,
            LOTRRecipes.urukRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("woodElven", LOTRGuiCraftingTable.WoodElven.class,
            LOTRRecipes.woodElvenRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("umbar", LOTRGuiCraftingTable.Umbar.class,
            LOTRRecipes.umbarRecipes);
        IntegratedRecipeHandlerFactory.registerMECTHandler ("bree", LOTRGuiCraftingTable.Bree.class,
            LOTRRecipes.breeRecipes);

        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityHobbitBartender.class,
            IntegratedRecipeHandlerFactory.FACTION_HOBBIT,
            LOTRTradeEntries.HOBBIT_BARTENDER_SELL, LOTRTradeEntries.HOBBIT_BARTENDER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityMordorOrcTrader.class,
            IntegratedRecipeHandlerFactory.FACTION_MORDOR,
            LOTRTradeEntries.MORDOR_TRADER_SELL, LOTRTradeEntries.MORDOR_TRADER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGondorBlacksmith.class,
            IntegratedRecipeHandlerFactory.FACTION_GONDOR,
            LOTRTradeEntries.GONDOR_BLACKSMITH_SELL, LOTRTradeEntries.GONDOR_BLACKSMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGaladhrimTrader.class,
            IntegratedRecipeHandlerFactory.FACTION_GALADHRIM,
            LOTRTradeEntries.GALADHRIM_TRADER_SELL, LOTRTradeEntries.GALADHRIM_TRADER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityUrukHaiTrader.class,
            IntegratedRecipeHandlerFactory.FACTION_URUK_HAI,
            LOTRTradeEntries.URUK_HAI_TRADER_SELL, LOTRTradeEntries.URUK_HAI_TRADER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityDwarfMiner.class,
            IntegratedRecipeHandlerFactory.FACTION_DWARF,
            LOTRTradeEntries.DWARF_MINER_SELL, LOTRTradeEntries.DWARF_MINER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityRohanBlacksmith.class,
            IntegratedRecipeHandlerFactory.FACTION_ROHAN,
            LOTRTradeEntries.ROHAN_BLACKSMITH_SELL, LOTRTradeEntries.ROHAN_BLACKSMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityDunlendingBartender.class,
            IntegratedRecipeHandlerFactory.FACTION_DUNLENDING,
            LOTRTradeEntries.DUNLENDING_BARTENDER_SELL, LOTRTradeEntries.DUNLENDING_BARTENDER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityRohanMeadhost.class,
            IntegratedRecipeHandlerFactory.FACTION_ROHAN,
            LOTRTradeEntries.ROHAN_MEADHOST_SELL, LOTRTradeEntries.ROHAN_MEADHOST_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityHobbitOrcharder.class,
            IntegratedRecipeHandlerFactory.FACTION_HOBBIT,
            LOTRTradeEntries.HOBBIT_ORCHARDER_SELL, LOTRTradeEntries.HOBBIT_ORCHARDER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityHobbitFarmer.class,
            IntegratedRecipeHandlerFactory.FACTION_HOBBIT,
            LOTRTradeEntries.HOBBIT_FARMER_SELL, LOTRTradeEntries.HOBBIT_FARMER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBlueDwarfMiner.class,
            IntegratedRecipeHandlerFactory.FACTION_BLUE_DWARF,
            LOTRTradeEntries.BLUE_DWARF_MINER_SELL, LOTRTradeEntries.BLUE_DWARF_MINER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBlueDwarfMerchant.class,
            IntegratedRecipeHandlerFactory.FACTION_BLUE_DWARF,
            LOTRTradeEntries.BLUE_DWARF_MERCHANT_SELL, LOTRTradeEntries.BLUE_DWARF_MERCHANT_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityNearHaradMerchant.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_SOUTHRON,
            LOTRTradeEntries.NEAR_HARAD_MERCHANT_SELL, LOTRTradeEntries.NEAR_HARAD_MERCHANT_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityAngmarOrcTrader.class,
            IntegratedRecipeHandlerFactory.FACTION_ANGMAR,
            LOTRTradeEntries.ANGMAR_TRADER_SELL, LOTRTradeEntries.ANGMAR_TRADER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityDolGuldurOrcTrader.class,
            IntegratedRecipeHandlerFactory.FACTION_DOL_GULDUR,
            LOTRTradeEntries.DOL_GULDUR_TRADER_SELL, LOTRTradeEntries.DOL_GULDUR_TRADER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityHalfTrollScavenger.class,
            IntegratedRecipeHandlerFactory.FACTION_HALF_TROLL,
            LOTRTradeEntries.HALF_TROLL_SCAVENGER_SELL, LOTRTradeEntries.HALF_TROLL_SCAVENGER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGaladhrimSmith.class,
            IntegratedRecipeHandlerFactory.FACTION_GALADHRIM,
            LOTRTradeEntries.GALADHRIM_SMITH_SELL, LOTRTradeEntries.GALADHRIM_SMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityHighElfSmith.class,
            IntegratedRecipeHandlerFactory.FACTION_HIGH_ELF,
            LOTRTradeEntries.HIGH_ELF_SMITH_SELL, LOTRTradeEntries.HIGH_ELF_SMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityWoodElfSmith.class,
            IntegratedRecipeHandlerFactory.FACTION_WOOD_ELF,
            LOTRTradeEntries.WOOD_ELF_SMITH_SELL, LOTRTradeEntries.WOOD_ELF_SMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityMoredainHuntsman.class,
            IntegratedRecipeHandlerFactory.FACTION_MOREDAIN,
            LOTRTradeEntries.MOREDAIN_HUNTSMAN_SELL, LOTRTradeEntries.MOREDAIN_HUNTSMAN_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityMoredainHutmaker.class,
            IntegratedRecipeHandlerFactory.FACTION_MOREDAIN,
            LOTRTradeEntries.MOREDAIN_HUTMAKER_SELL, LOTRTradeEntries.MOREDAIN_HUTMAKER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityIronHillsMerchant.class,
            IntegratedRecipeHandlerFactory.FACTION_DWARF,
            LOTRTradeEntries.IRON_HILLS_MERCHANT_SELL, LOTRTradeEntries.IRON_HILLS_MERCHANT_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityScrapTrader.class, null,
            LOTRTradeEntries.SCRAP_TRADER_SELL,
            LOTRTradeEntries.SCRAP_TRADER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityTauredainShaman.class,
            IntegratedRecipeHandlerFactory.FACTION_TAUREDAIN,
            LOTRTradeEntries.TAUREDAIN_SHAMAN_SELL, LOTRTradeEntries.TAUREDAIN_SHAMAN_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityTauredainFarmer.class,
            IntegratedRecipeHandlerFactory.FACTION_TAUREDAIN,
            LOTRTradeEntries.TAUREDAIN_FARMER_SELL, LOTRTradeEntries.TAUREDAIN_FARMER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityDwarfSmith.class,
            IntegratedRecipeHandlerFactory.FACTION_DWARF,
            LOTRTradeEntries.DWARF_SMITH_SELL, LOTRTradeEntries.DWARF_SMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBlueMountainsSmith.class,
            IntegratedRecipeHandlerFactory.FACTION_BLUE_DWARF,
            LOTRTradeEntries.BLUE_DWARF_SMITH_SELL, LOTRTradeEntries.BLUE_DWARF_SMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityDaleBlacksmith.class,
            IntegratedRecipeHandlerFactory.FACTION_DALE,
            LOTRTradeEntries.DALE_BLACKSMITH_SELL, LOTRTradeEntries.DALE_BLACKSMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityDaleBaker.class,
            IntegratedRecipeHandlerFactory.FACTION_DALE,
            LOTRTradeEntries.DALE_BAKER_SELL, LOTRTradeEntries.DALE_BAKER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityDorwinionElfVintner.class,
            IntegratedRecipeHandlerFactory.FACTION_DORWINION,
            LOTRTradeEntries.DORWINION_VINTNER_SELL, LOTRTradeEntries.DORWINION_VINTNER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityDorwinionVinekeeper.class,
            IntegratedRecipeHandlerFactory.FACTION_DORWINION,
            LOTRTradeEntries.DORWINION_VINEKEEPER_SELL, LOTRTradeEntries.DORWINION_VINEKEEPER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityDorwinionMerchantElf.class,
            IntegratedRecipeHandlerFactory.FACTION_DORWINION,
            LOTRTradeEntries.DORWINION_MERCHANT_SELL, LOTRTradeEntries.DORWINION_MERCHANT_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityDaleMerchant.class,
            IntegratedRecipeHandlerFactory.FACTION_DALE,
            LOTRTradeEntries.DALE_MERCHANT_SELL, LOTRTradeEntries.DALE_MERCHANT_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGondorFarmer.class,
            IntegratedRecipeHandlerFactory.FACTION_GONDOR,
            LOTRTradeEntries.GONDOR_FARMER_SELL, LOTRTradeEntries.GONDOR_FARMER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGondorBartender.class,
            IntegratedRecipeHandlerFactory.FACTION_GONDOR,
            LOTRTradeEntries.GONDOR_BARTENDER_SELL, LOTRTradeEntries.GONDOR_BARTENDER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGondorGreengrocer.class,
            IntegratedRecipeHandlerFactory.FACTION_GONDOR,
            LOTRTradeEntries.GONDOR_GREENGROCER_SELL, LOTRTradeEntries.GONDOR_GREENGROCER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGondorLumberman.class,
            IntegratedRecipeHandlerFactory.FACTION_GONDOR,
            LOTRTradeEntries.GONDOR_LUMBERMAN_SELL, LOTRTradeEntries.GONDOR_LUMBERMAN_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGondorMason.class,
            IntegratedRecipeHandlerFactory.FACTION_GONDOR,
            LOTRTradeEntries.GONDOR_MASON_SELL, LOTRTradeEntries.GONDOR_MASON_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGondorBrewer.class,
            IntegratedRecipeHandlerFactory.FACTION_GONDOR,
            LOTRTradeEntries.GONDOR_BREWER_SELL, LOTRTradeEntries.GONDOR_BREWER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGondorFlorist.class,
            IntegratedRecipeHandlerFactory.FACTION_GONDOR,
            LOTRTradeEntries.GONDOR_FLORIST_SELL, LOTRTradeEntries.GONDOR_FLORIST_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGondorButcher.class,
            IntegratedRecipeHandlerFactory.FACTION_GONDOR,
            LOTRTradeEntries.GONDOR_BUTCHER_SELL, LOTRTradeEntries.GONDOR_BUTCHER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGondorFishmonger.class,
            IntegratedRecipeHandlerFactory.FACTION_GONDOR,
            LOTRTradeEntries.GONDOR_FISHMONGER_SELL, LOTRTradeEntries.GONDOR_FISHMONGER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGondorBaker.class,
            IntegratedRecipeHandlerFactory.FACTION_GONDOR,
            LOTRTradeEntries.GONDOR_BAKER_SELL, LOTRTradeEntries.GONDOR_BAKER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityRohanFarmer.class,
            IntegratedRecipeHandlerFactory.FACTION_ROHAN,
            LOTRTradeEntries.ROHAN_FARMER_SELL, LOTRTradeEntries.ROHAN_FARMER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityRohanLumberman.class,
            IntegratedRecipeHandlerFactory.FACTION_ROHAN,
            LOTRTradeEntries.ROHAN_LUMBERMAN_SELL, LOTRTradeEntries.ROHAN_LUMBERMAN_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityRohanBuilder.class,
            IntegratedRecipeHandlerFactory.FACTION_ROHAN,
            LOTRTradeEntries.ROHAN_BUILDER_SELL, LOTRTradeEntries.ROHAN_BUILDER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityRohanBrewer.class,
            IntegratedRecipeHandlerFactory.FACTION_ROHAN,
            LOTRTradeEntries.ROHAN_BREWER_SELL, LOTRTradeEntries.ROHAN_BREWER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityRohanButcher.class,
            IntegratedRecipeHandlerFactory.FACTION_ROHAN,
            LOTRTradeEntries.ROHAN_BUTCHER_SELL, LOTRTradeEntries.ROHAN_BUTCHER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityRohanFishmonger.class,
            IntegratedRecipeHandlerFactory.FACTION_ROHAN,
            LOTRTradeEntries.ROHAN_FISHMONGER_SELL, LOTRTradeEntries.ROHAN_FISHMONGER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityRohanBaker.class,
            IntegratedRecipeHandlerFactory.FACTION_ROHAN,
            LOTRTradeEntries.ROHAN_BAKER_SELL, LOTRTradeEntries.ROHAN_BAKER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityRohanOrcharder.class,
            IntegratedRecipeHandlerFactory.FACTION_ROHAN,
            LOTRTradeEntries.ROHAN_ORCHARDER_SELL, LOTRTradeEntries.ROHAN_ORCHARDER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityDunedainBlacksmith.class,
            IntegratedRecipeHandlerFactory.FACTION_DUNEDAIN,
            LOTRTradeEntries.DUNEDAIN_BLACKSMITH_SELL, LOTRTradeEntries.DUNEDAIN_BLACKSMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityRohanStablemaster.class,
            IntegratedRecipeHandlerFactory.FACTION_ROHAN,
            LOTRTradeEntries.ROHAN_STABLEMASTER_SELL, LOTRTradeEntries.ROHAN_STABLEMASTER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityEasterlingBlacksmith.class,
            IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
            LOTRTradeEntries.RHUN_BLACKSMITH_SELL, LOTRTradeEntries.RHUN_BLACKSMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityDorwinionMerchantMan.class,
            IntegratedRecipeHandlerFactory.FACTION_DORWINION,
            LOTRTradeEntries.DORWINION_MERCHANT_SELL, LOTRTradeEntries.DORWINION_MERCHANT_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityEasterlingLumberman.class,
            IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
            LOTRTradeEntries.RHUN_LUMBERMAN_SELL, LOTRTradeEntries.RHUN_LUMBERMAN_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityEasterlingMason.class,
            IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
            LOTRTradeEntries.RHUN_MASON_SELL, LOTRTradeEntries.RHUN_MASON_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityEasterlingButcher.class,
            IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
            LOTRTradeEntries.RHUN_BUTCHER_SELL, LOTRTradeEntries.RHUN_BUTCHER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityEasterlingBrewer.class,
            IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
            LOTRTradeEntries.RHUN_BREWER_SELL, LOTRTradeEntries.RHUN_BREWER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityEasterlingFishmonger.class,
            IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
            LOTRTradeEntries.RHUN_FISHMONGER_SELL, LOTRTradeEntries.RHUN_FISHMONGER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityEasterlingBaker.class,
            IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
            LOTRTradeEntries.RHUN_BAKER_SELL, LOTRTradeEntries.RHUN_BAKER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityEasterlingHunter.class,
            IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
            LOTRTradeEntries.RHUN_HUNTER_SELL, LOTRTradeEntries.RHUN_HUNTER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityEasterlingFarmer.class,
            IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
            LOTRTradeEntries.RHUN_FARMER_SELL, LOTRTradeEntries.RHUN_FARMER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityEasterlingGoldsmith.class,
            IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
            LOTRTradeEntries.RHUN_GOLDSMITH_SELL, LOTRTradeEntries.RHUN_GOLDSMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityEasterlingBartender.class,
            IntegratedRecipeHandlerFactory.FACTION_EASTERLING,
            LOTRTradeEntries.RHUN_BARTENDER_SELL, LOTRTradeEntries.RHUN_BARTENDER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityRivendellTrader.class,
            IntegratedRecipeHandlerFactory.FACTION_RIVENDELL,
            LOTRTradeEntries.RIVENDELL_TRADER_SELL, LOTRTradeEntries.RIVENDELL_TRADER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityRivendellSmith.class,
            IntegratedRecipeHandlerFactory.FACTION_RIVENDELL,
            LOTRTradeEntries.RIVENDELL_SMITH_SELL, LOTRTradeEntries.RIVENDELL_SMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGundabadOrcTrader.class,
            IntegratedRecipeHandlerFactory.FACTION_GUNDABAD,
            LOTRTradeEntries.GUNDABAD_TRADER_SELL, LOTRTradeEntries.GUNDABAD_TRADER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityNearHaradBlacksmith.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_SOUTHRON,
            LOTRTradeEntries.NEAR_HARAD_BLACKSMITH_SELL, LOTRTradeEntries.NEAR_HARAD_BLACKSMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityHarnedorBlacksmith.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_HARNEDOR,
            LOTRTradeEntries.HARNEDOR_BLACKSMITH_SELL, LOTRTradeEntries.HARNEDOR_BLACKSMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityUmbarBlacksmith.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_UMBAR,
            LOTRTradeEntries.UMBAR_BLACKSMITH_SELL, LOTRTradeEntries.UMBAR_BLACKSMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGulfBlacksmith.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_GULF,
            LOTRTradeEntries.GULF_BLACKSMITH_SELL, LOTRTradeEntries.GULF_BLACKSMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityNomadMerchant.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_NOMAD,
            LOTRTradeEntries.NOMAD_MERCHANT_SELL, LOTRTradeEntries.NOMAD_MERCHANT_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityHarnedorBartender.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_HARNEDOR,
            LOTRTradeEntries.HARNEDOR_BARTENDER_SELL, LOTRTradeEntries.HARNEDOR_BARTENDER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntitySouthronLumberman.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_SOUTHRON,
            LOTRTradeEntries.HARAD_LUMBERMAN_SELL, LOTRTradeEntries.HARAD_LUMBERMAN_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityHarnedorLumberman.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_HARNEDOR,
            LOTRTradeEntries.HARAD_LUMBERMAN_SELL, LOTRTradeEntries.HARAD_LUMBERMAN_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityUmbarLumberman.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_UMBAR,
            LOTRTradeEntries.HARAD_LUMBERMAN_SELL, LOTRTradeEntries.HARAD_LUMBERMAN_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntitySouthronMason.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_SOUTHRON,
            LOTRTradeEntries.HARAD_MASON_SELL, LOTRTradeEntries.HARAD_MASON_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityHarnedorMason.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_HARNEDOR,
            LOTRTradeEntries.HARAD_MASON_SELL, LOTRTradeEntries.HARAD_MASON_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGulfMason.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_GULF,
            LOTRTradeEntries.HARAD_MASON_SELL, LOTRTradeEntries.HARAD_MASON_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntitySouthronButcher.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_SOUTHRON,
            LOTRTradeEntries.HARAD_BUTCHER_SELL, LOTRTradeEntries.HARAD_BUTCHER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityHarnedorButcher.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_HARNEDOR,
            LOTRTradeEntries.HARAD_BUTCHER_SELL, LOTRTradeEntries.HARAD_BUTCHER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityUmbarButcher.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_UMBAR,
            LOTRTradeEntries.HARAD_BUTCHER_SELL, LOTRTradeEntries.HARAD_BUTCHER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGulfButcher.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_GULF,
            LOTRTradeEntries.HARAD_BUTCHER_SELL, LOTRTradeEntries.HARAD_BUTCHER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntitySouthronBrewer.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_SOUTHRON,
            LOTRTradeEntries.HARAD_BREWER_SELL, LOTRTradeEntries.HARAD_BREWER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityHarnedorBrewer.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_HARNEDOR,
            LOTRTradeEntries.HARAD_BREWER_SELL, LOTRTradeEntries.HARAD_BREWER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityUmbarBrewer.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_UMBAR,
            LOTRTradeEntries.HARAD_BREWER_SELL, LOTRTradeEntries.HARAD_BREWER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGulfBrewer.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_GULF,
            LOTRTradeEntries.HARAD_BREWER_SELL, LOTRTradeEntries.HARAD_BREWER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityNomadBrewer.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_NOMAD,
            LOTRTradeEntries.HARAD_BREWER_SELL, LOTRTradeEntries.HARAD_BREWER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntitySouthronFishmonger.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_SOUTHRON,
            LOTRTradeEntries.HARAD_FISHMONGER_SELL, LOTRTradeEntries.HARAD_FISHMONGER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityHarnedorFishmonger.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_HARNEDOR,
            LOTRTradeEntries.HARAD_FISHMONGER_SELL, LOTRTradeEntries.HARAD_FISHMONGER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityUmbarFishmonger.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_UMBAR,
            LOTRTradeEntries.HARAD_FISHMONGER_SELL, LOTRTradeEntries.HARAD_FISHMONGER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGulfFishmonger.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_GULF,
            LOTRTradeEntries.HARAD_FISHMONGER_SELL, LOTRTradeEntries.HARAD_FISHMONGER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntitySouthronBaker.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_SOUTHRON,
            LOTRTradeEntries.HARAD_BAKER_SELL, LOTRTradeEntries.HARAD_BAKER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityHarnedorBaker.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_HARNEDOR,
            LOTRTradeEntries.HARAD_BAKER_SELL, LOTRTradeEntries.HARAD_BAKER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityUmbarBaker.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_UMBAR,
            LOTRTradeEntries.HARAD_BAKER_SELL, LOTRTradeEntries.HARAD_BAKER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityHarnedorHunter.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_HARNEDOR,
            LOTRTradeEntries.HARAD_HUNTER_SELL, LOTRTradeEntries.HARAD_HUNTER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntitySouthronFarmer.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_SOUTHRON,
            LOTRTradeEntries.HARAD_FARMER_SELL, LOTRTradeEntries.HARAD_FARMER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityHarnedorFarmer.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_HARNEDOR,
            LOTRTradeEntries.HARAD_FARMER_SELL, LOTRTradeEntries.HARAD_FARMER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityUmbarFarmer.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_UMBAR,
            LOTRTradeEntries.HARAD_FARMER_SELL, LOTRTradeEntries.HARAD_FARMER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGulfFarmer.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_GULF,
            LOTRTradeEntries.HARAD_FARMER_SELL, LOTRTradeEntries.HARAD_FARMER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntitySouthronMiner.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_SOUTHRON,
            LOTRTradeEntries.HARAD_MINER_SELL, LOTRTradeEntries.HARAD_MINER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityHarnedorMiner.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_HARNEDOR,
            LOTRTradeEntries.HARAD_MINER_SELL, LOTRTradeEntries.HARAD_MINER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityUmbarMiner.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_UMBAR,
            LOTRTradeEntries.HARAD_MINER_SELL, LOTRTradeEntries.HARAD_MINER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGulfMiner.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_GULF,
            LOTRTradeEntries.HARAD_MINER_SELL, LOTRTradeEntries.HARAD_MINER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityNomadMiner.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_NOMAD,
            LOTRTradeEntries.HARAD_MINER_SELL, LOTRTradeEntries.HARAD_MINER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntitySouthronFlorist.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_SOUTHRON,
            LOTRTradeEntries.HARAD_FLORIST_SELL, LOTRTradeEntries.HARAD_FLORIST_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityUmbarFlorist.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_UMBAR,
            LOTRTradeEntries.HARAD_FLORIST_SELL, LOTRTradeEntries.HARAD_FLORIST_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntitySouthronGoldsmith.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_SOUTHRON,
            LOTRTradeEntries.HARAD_GOLDSMITH_SELL, LOTRTradeEntries.HARAD_GOLDSMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityUmbarGoldsmith.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_UMBAR,
            LOTRTradeEntries.HARAD_GOLDSMITH_SELL, LOTRTradeEntries.HARAD_GOLDSMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGulfGoldsmith.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_GULF,
            LOTRTradeEntries.HARAD_GOLDSMITH_SELL, LOTRTradeEntries.HARAD_GOLDSMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityUmbarMason.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_UMBAR,
            LOTRTradeEntries.UMBAR_MASON_SELL, LOTRTradeEntries.UMBAR_MASON_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityNomadArmourer.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_NOMAD,
            LOTRTradeEntries.NOMAD_ARMOURER_SELL, LOTRTradeEntries.NOMAD_ARMOURER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGulfLumberman.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_GULF,
            LOTRTradeEntries.GULF_LUMBERMAN_SELL, LOTRTradeEntries.GULF_LUMBERMAN_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGulfHunter.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_GULF,
            LOTRTradeEntries.GULF_HUNTER_SELL, LOTRTradeEntries.GULF_HUNTER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntitySouthronBartender.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_SOUTHRON,
            LOTRTradeEntries.SOUTHRON_BARTENDER_SELL, LOTRTradeEntries.SOUTHRON_BARTENDER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityUmbarBartender.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_UMBAR,
            LOTRTradeEntries.SOUTHRON_BARTENDER_SELL, LOTRTradeEntries.SOUTHRON_BARTENDER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGulfBartender.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_GULF,
            LOTRTradeEntries.GULF_BARTENDER_SELL, LOTRTradeEntries.GULF_BARTENDER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityGulfBaker.class,
            IntegratedRecipeHandlerFactory.FACTION_NEAR_HARAD_GULF,
            LOTRTradeEntries.GULF_BAKER_SELL, LOTRTradeEntries.GULF_BAKER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityTauredainSmith.class,
            IntegratedRecipeHandlerFactory.FACTION_TAUREDAIN,
            LOTRTradeEntries.TAUREDAIN_SMITH_SELL, LOTRTradeEntries.TAUREDAIN_SMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityWickedDwarf.class,
            IntegratedRecipeHandlerFactory.FACTION_MORDOR,
            LOTRTradeEntries.WICKED_DWARF_SELL, LOTRTradeEntries.WICKED_DWARF_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBreeBlacksmith.class,
            IntegratedRecipeHandlerFactory.FACTION_BREE,
            LOTRTradeEntries.BREE_BLACKSMITH_SELL, LOTRTradeEntries.BREE_BLACKSMITH_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBreeInnkeeper.class,
            IntegratedRecipeHandlerFactory.FACTION_BREE,
            LOTRTradeEntries.BREE_INNKEEPER_SELL, LOTRTradeEntries.BREE_INNKEEPER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBreeHobbitInnkeeper.class,
            IntegratedRecipeHandlerFactory.FACTION_BREE,
            LOTRTradeEntries.BREE_INNKEEPER_SELL, LOTRTradeEntries.BREE_INNKEEPER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBreeBaker.class,
            IntegratedRecipeHandlerFactory.FACTION_BREE,
            LOTRTradeEntries.BREE_BAKER_SELL, LOTRTradeEntries.BREE_BAKER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBreeHobbitBaker.class,
            IntegratedRecipeHandlerFactory.FACTION_BREE,
            LOTRTradeEntries.BREE_BAKER_SELL, LOTRTradeEntries.BREE_BAKER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBreeButcher.class,
            IntegratedRecipeHandlerFactory.FACTION_BREE,
            LOTRTradeEntries.BREE_BUTCHER_SELL, LOTRTradeEntries.BREE_BUTCHER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBreeHobbitButcher.class,
            IntegratedRecipeHandlerFactory.FACTION_BREE,
            LOTRTradeEntries.BREE_BUTCHER_SELL, LOTRTradeEntries.BREE_BUTCHER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBreeBrewer.class,
            IntegratedRecipeHandlerFactory.FACTION_BREE,
            LOTRTradeEntries.BREE_BREWER_SELL, LOTRTradeEntries.BREE_BREWER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBreeHobbitBrewer.class,
            IntegratedRecipeHandlerFactory.FACTION_BREE,
            LOTRTradeEntries.BREE_BREWER_SELL, LOTRTradeEntries.BREE_BREWER_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBreeMason.class,
            IntegratedRecipeHandlerFactory.FACTION_BREE,
            LOTRTradeEntries.BREE_MASON_SELL, LOTRTradeEntries.BREE_MASON_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBreeLumberman.class,
            IntegratedRecipeHandlerFactory.FACTION_BREE,
            LOTRTradeEntries.BREE_LUMBERMAN_SELL, LOTRTradeEntries.BREE_LUMBERMAN_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBreeFlorist.class,
            IntegratedRecipeHandlerFactory.FACTION_BREE,
            LOTRTradeEntries.BREE_FLORIST_SELL, LOTRTradeEntries.BREE_FLORIST_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBreeHobbitFlorist.class,
            IntegratedRecipeHandlerFactory.FACTION_BREE,
            LOTRTradeEntries.BREE_FLORIST_SELL, LOTRTradeEntries.BREE_FLORIST_BUY);
        IntegratedRecipeHandlerFactory.registerTraderHandler (LOTREntityBreeFarmer.class,
            IntegratedRecipeHandlerFactory.FACTION_BREE,
            LOTRTradeEntries.BREE_FARMER_SELL, LOTRTradeEntries.BREE_FARMER_BUY);

        IntegratedRecipeHandlerFactory.recipeHandlers
            .add (new LOTRAlloyForgeRecipeHandler ("orc", new LOTRAlloyForgeRecipeHandler.OrcForgeAccess ()));
        IntegratedRecipeHandlerFactory.recipeHandlers
            .add (new LOTRAlloyForgeRecipeHandler ("men", new LOTRAlloyForgeRecipeHandler.MenForgeAccess ()));
        IntegratedRecipeHandlerFactory.recipeHandlers
            .add (new LOTRAlloyForgeRecipeHandler ("elven", new LOTRAlloyForgeRecipeHandler.ElvenForgeAccess ()));
        IntegratedRecipeHandlerFactory.recipeHandlers
            .add (new LOTRAlloyForgeRecipeHandler ("dwarven", new LOTRAlloyForgeRecipeHandler.DwarvenForgeAccess ()));

        IntegratedRecipeHandlerFactory.disableCoinCountDisplayForGUIs ("codechicken.nei.recipe.GuiCraftingRecipe",
            "codechicken.nei.recipe.GuiUsageRecipe");
    }

    private static void registerMECTHandler (String unlocalizedName, Class<? extends GuiContainer> guiClass,
        Collection<IRecipe> recipes)
    {
        IntegratedRecipeHandlerFactory.recipeHandlers
            .add (new LOTRCraftingTableRecipeHandler (unlocalizedName, guiClass, () -> recipes));
    }

    private static void registerTraderHandler (Class<? extends LOTRTradeable> entityClass, String faction,
        LOTRTradeEntries itemsBought,
        LOTRTradeEntries itemsSold)
    {
        IntegratedRecipeHandlerFactory.recipeHandlers
            .add (new LOTRTraderRecipeHandler (LOTRRecipeHandlerUtils.getUnlocalizedEntityName (entityClass), faction,
                itemsBought, itemsSold));
    }

    @SuppressWarnings("unchecked")
    private static void disableCoinCountDisplayForGUIs (String... guiClasses)
    {
        for (String guiClass : guiClasses)
        {
            try
            {
                LOTRGuiHandler.coinCount_excludedGUIs.add ((Class<? extends GuiContainer>) Class.forName (guiClass));
            }
            catch (ClassNotFoundException e)
            {
                IntegratedRecipeHandlerFactory.logger.error ("Couldn't retrieve the GUI class instance", e);
            }
            catch (ClassCastException e)
            {
                IntegratedRecipeHandlerFactory.logger.error ("The supplied type doesn't extend GUIContainer", e);
            }
        }
    }

    @Override
    public Set<RecipeHandler<?>> getRecipeHandlers ()
    {
        return IntegratedRecipeHandlerFactory.recipeHandlers;
    }

}
