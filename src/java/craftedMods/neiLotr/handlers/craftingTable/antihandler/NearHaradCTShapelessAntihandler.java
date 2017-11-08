package craftedMods.neiLotr.handlers.craftingTable.antihandler;

import javax.swing.plaf.basic.BasicComboBoxUI.ItemHandler;

import craftedMods.neiLotr.handlers.template.RecipeAntihandler;
import lotr.common.item.LOTRItemHaradTurban;
import net.minecraft.item.ItemStack;

public class NearHaradCTShapelessAntihandler implements RecipeAntihandler {

	@Override
	public boolean stopLoadCraftingRecipes(ItemStack result) {
		return LOTRItemHaradTurban.hasOrnament(result) ? true : false;
	}

	@Override
	public boolean stopLoadUsageRecipes(ItemStack ingredient) {
		return false;
	}

}
