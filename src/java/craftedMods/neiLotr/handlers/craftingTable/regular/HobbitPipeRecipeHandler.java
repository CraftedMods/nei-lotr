package craftedMods.neiLotr.handlers.craftingTable.regular;

import java.util.*;

import com.google.common.collect.*;

import codechicken.nei.recipe.ShapelessRecipeHandler;
import craftedMods.neiLotr.handlers.craftingTable.BasicCTShapelessRecipeHandler;
import craftedMods.neiLotr.handlers.template.ExtendedShapelessRecipeHandler;
import craftedMods.neiLotr.handlers.template.ExtendedShapelessRecipeHandler.ExtendedCachedShapelessRecipe;
import craftedMods.neiLotr.util.NeiLotrUtil;
import lotr.common.LOTRMod;
import lotr.common.item.*;
import net.minecraft.block.BlockColored;
import net.minecraft.item.ItemStack;

public class HobbitPipeRecipeHandler extends ExtendedShapelessRecipeHandler {

	private static List<ItemStack> pipes = new ArrayList<>();
	private static ListMultimap<Integer, ItemStack> dyes = MultimapBuilder.hashKeys().arrayListValues().build();

	public HobbitPipeRecipeHandler() {
		super();
		if (pipes.isEmpty()) {
			for (int i = 0; i < 17; i++) {
				pipes.add(new ItemStack(LOTRMod.hobbitPipe));
				LOTRItemHobbitPipe.setSmokeColor((ItemStack) pipes.get(i), i);
			}
		}
		if (dyes.isEmpty()) {
			NeiLotrUtil.getDyes().forEach(stack -> {
				dyes.put(BlockColored.func_150031_c(LOTRItemDye.isItemDye(stack)), stack);
			});
			dyes.put(16, new ItemStack(LOTRMod.mithrilNugget));
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		this.handleRecipe(result, false);
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		this.handleRecipe(ingredient, true);
	}

	private void handleRecipe(ItemStack stack, boolean usage) {
		boolean isNugget = stack.getItem() == LOTRMod.mithrilNugget;
		boolean flag = LOTRItemDye.isItemDye(stack) == -1 && !isNugget;
		if ((flag && !(stack.getItem() instanceof LOTRItemHobbitPipe)) || (!usage && !flag))
			return;

		ExtendedCachedShapelessRecipe rec = null;

		List<List<?>> ingredients = new ArrayList<>();
		List<Object> results = new ArrayList<>();

		if ((usage && !flag) || (!usage))
			ingredients.add(pipes);

		if ((usage && flag) || (!usage)) {
			ingredients.add(getDyesFromPipe(stack));
			results.add(stack.copy());
		}

		if (usage) {
			if (flag) { // Pipe usage
				// Pipe
				ingredients.add(0, Arrays.asList(stack.copy()));

				// Result
				results.add(stack.copy());
			} else {// Dye usage
				// Dye
				ingredients.add(Arrays.asList(new Object[] { stack.copy() }));

				// Result
				int color = 0;

				if (isNugget) {
					color = 16;
				} else {
					color = BlockColored.func_150031_c(LOTRItemDye.isItemDye(stack));
				}

				ItemStack dyedPipe = new ItemStack(LOTRMod.hobbitPipe);
				LOTRItemHobbitPipe.setSmokeColor(dyedPipe, color);

				results.add(dyedPipe);
			}
		}

		rec = new ExtendedCachedShapelessRecipe(ingredients, results);

		arecipes.add(rec);
	}

	private static List<ItemStack> getDyesFromPipe(ItemStack pipe) {
		int color = LOTRItemHobbitPipe.getSmokeColor(pipe);
		return dyes.get(color);
	}
}
