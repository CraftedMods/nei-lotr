package craftedMods.lotr.recipes;

import java.util.*;

import craftedMods.recipes.api.*;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

@RegisteredHandler
public class LOTRItemHidingHandler implements ItemHidingHandler {

	private static final Collection<ItemStack> hiddenStacks = new ArrayList<>();

	@Override
	public Collection<ItemStack> getHiddenStacks() {
		LOTRItemHidingHandler.hiddenStacks.clear();

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
		return LOTRItemHidingHandler.hiddenStacks;
	}

	private void hideItemMeta(ItemStack stack) {
		for (int i = 8; i < 16; i++) {
			hiddenStacks.add(new ItemStack(stack.getItem(), 1, i));
		}
	}

	private void hideItemMeta(Block block) {
		this.hideItemMeta(new ItemStack(block));
	}

	// private void hideItemMeta(Item item) {
	// this.hideItemMeta(new ItemStack(item));
	// }

	private void hideItemAll(ItemStack stack) {
		hiddenStacks.add(new ItemStack(stack.getItem(), 1, 32767));
	}

	private void hideItemAll(Block block) {
		this.hideItemAll(new ItemStack(block));
	}

	// private void hideItemAll(Item item) {
	// this.hideItemAll(new ItemStack(item));
	// }

}
