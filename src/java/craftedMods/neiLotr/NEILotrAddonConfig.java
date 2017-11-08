package craftedMods.neiLotr;

import java.util.*;
import java.util.function.Consumer;

import codechicken.nei.api.*;
import codechicken.nei.recipe.*;
import craftedMods.neiLotr.handlers.*;
import craftedMods.neiLotr.handlers.craftingTable.*;
import craftedMods.neiLotr.handlers.craftingTable.antihandler.NearHaradCTShapelessAntihandler;
import craftedMods.neiLotr.handlers.craftingTable.regular.*;
import craftedMods.neiLotr.handlers.craftingTable.subhandler.HaradTurbanOrnamentRecipeHandler;
import craftedMods.neiLotr.handlers.template.StaticRecipeLoader;
import lotr.client.gui.LOTRGuiCraftingTable;
import lotr.common.LOTRMod;
import lotr.common.entity.LOTREntities;
import lotr.common.entity.npc.*;
import lotr.common.recipe.LOTRRecipes;
import lotr.common.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;

public class NEILotrAddonConfig implements IConfigureNEI {

	@Override
	public String getName() {
		return StatCollector.translateToLocal("neiLotr.integration.name");
	}

	@Override
	public String getVersion() {
		return NeiLotr.mod.getVersionChecker().getCurrentVersion().getState().toString() + " " + NeiLotr.VERSION;
	}

	@Override
	public void loadConfig() {
		NeiLotr.mod.getNeiConfig().load();
	}

}
