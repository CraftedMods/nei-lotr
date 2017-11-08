package craftedMods.neiLotr.handlers.craftingTable.subhandler;

import java.util.*;
import java.util.function.BiConsumer;

import codechicken.nei.recipe.ShapelessRecipeHandler.CachedShapelessRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import craftedMods.neiLotr.handlers.craftingTable.BasicCTShapelessRecipeHandler;
import craftedMods.neiLotr.handlers.template.*;
import craftedMods.neiLotr.handlers.template.ExtendedShapelessRecipeHandler.ExtendedCachedShapelessRecipe;
import craftedMods.neiLotr.util.NeiLotrUtil;
import lotr.common.item.LOTRItemHaradTurban;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class HaradTurbanOrnamentRecipeHandler extends AbstractRecipeSubhandler {

	public HaradTurbanOrnamentRecipeHandler(BasicCTShapelessRecipeHandler handler) {
		super(handler);
	}

	private void handleRecipe(ItemStack stack1, boolean usage) {

		if ((!(stack1.getItem() instanceof LOTRItemHaradTurban)) || (!usage && !LOTRItemHaradTurban.hasOrnament(stack1))
				|| (usage && (LOTRItemHaradTurban.hasOrnament(stack1))))
			return;

		ExtendedCachedShapelessRecipe recipe = null;

		ItemStack ingred = stack1.copy();
		ItemStack result = stack1.copy();

		if (usage) {
			LOTRItemHaradTurban.setHasOrnament(result, true);
		}
		LOTRItemHaradTurban.setHasOrnament(ingred, false);

		recipe = ((BasicCTShapelessRecipeHandler) parent).new ExtendedCachedShapelessRecipe(
				Arrays.asList(new Object[] { ingred, new ItemStack(Items.gold_nugget) }), Arrays.asList(result));

		if (recipe != null) {
			arecipes.add(recipe);
		}
	}

	@Override
	protected void loadCraftingRecipeHandler(ItemStack result) {
		this.handleRecipe(result, false);
	}

	@Override
	protected void loadUsageRecipeHandler(ItemStack ingredient) {
		if (ingredient.getItem() == Items.gold_nugget) {
			NeiLotrUtil.itemStackIteration((count,itemStack) -> {
				handleRecipe(itemStack, true);
			});
		} else {
			handleRecipe(ingredient, true);
		}
	}

	@Override
	public HaradTurbanOrnamentRecipeHandler newInstance(TemplateRecipeHandler handler) {
		if (handler instanceof BasicCTShapelessRecipeHandler) {
			return new HaradTurbanOrnamentRecipeHandler((BasicCTShapelessRecipeHandler) handler);
		}
		return null;
	}
}
