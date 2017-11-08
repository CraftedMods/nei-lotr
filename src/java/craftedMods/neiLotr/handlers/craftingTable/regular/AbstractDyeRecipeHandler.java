package craftedMods.neiLotr.handlers.craftingTable.regular;

import java.math.BigDecimal;
import java.util.*;

import craftedMods.neiLotr.handlers.craftingTable.subhandler.HaradTurbanOrnamentRecipeHandler;
import craftedMods.neiLotr.handlers.template.ExtendedShapelessRecipeHandler;
import craftedMods.neiLotr.util.NeiLotrUtil;
import lotr.common.item.*;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.*;

public abstract class AbstractDyeRecipeHandler extends ExtendedShapelessRecipeHandler {

	protected final List<Item> itemsToDye = new ArrayList<>();

	protected AbstractDyeRecipeHandler(Item... itemsToDye) {
		super();
		this.itemsToDye.addAll(Arrays.asList(itemsToDye));
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		handleDyeRecipe(result, false);
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		handleDyeRecipe(ingredient, true);
	}

	private void handleDyeRecipe(ItemStack stack, boolean usage) {
		boolean isDye = LOTRItemDye.isItemDye(stack) != -1;
		if ((!usage && !isDyed(stack)) || (isDye && !usage) || (!isDye && !itemsToDye.contains(stack.getItem())))
			return;

		ExtendedCachedShapelessRecipe rec = null;

		List<List<?>> ingredients = new ArrayList<>();
		List<Object> results = new ArrayList<>();

		if (!usage && !isDye) {
			results.add(stack.copy());

			int color = getDyeColor(stack);
			int dye = -1;

			float r = (color >> 16 & 0xFF) / 255.0F;
			float g = (color >> 8 & 0xFF) / 255.0F;
			float b = (color & 0xFF) / 255.0F;
			
			for (int i = 0; i < EntitySheep.fleeceColorTable.length; i++) {
				float tmpR = EntitySheep.fleeceColorTable[i][0];
				float tmpG = EntitySheep.fleeceColorTable[i][1];
				float tmpB = EntitySheep.fleeceColorTable[i][2];
				if (r == tmpR && g == tmpG && tmpB == b) {
					dye = i;
					break;
				}
			}

			List<Object> dyes = new ArrayList<>();

			final int finalDye = dye;

			if (dye != -1) {
				NeiLotrUtil.getDyes().forEach(dyeStack -> {
					int dyeColor = BlockColored.func_150031_c(LOTRItemDye.isItemDye(dyeStack));
					if (finalDye == dyeColor)
						dyes.add(dyeStack.copy());
				});
			}
			ItemStack ingred = stack.copy();
			removeDye(ingred);
			ingredients.add(Arrays.asList(ingred));
			ingredients.add(dyes);
		}

		rec = new ExtendedCachedShapelessRecipe(ingredients, results);

		arecipes.add(rec);
	}

	protected abstract int getDyeColor(ItemStack stack);

	protected abstract boolean isDyed(ItemStack stack);

	protected abstract void setDyeColor(ItemStack stack, int color);

	protected abstract void removeDye(ItemStack stack);
}
