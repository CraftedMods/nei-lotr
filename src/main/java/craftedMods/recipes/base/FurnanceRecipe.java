package craftedMods.recipes.base;

import java.util.Collection;

import craftedMods.lotr.nei.handlers.LOTRRecipeHandlerUtils;
import craftedMods.recipes.api.utils.ItemStackSet;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;

public class FurnanceRecipe extends ShapelessRecipe {

	public FurnanceRecipe(Collection<ItemStack> ingredients, ItemStack result) {
		super(ingredients, result);
		this.addFuels();
	}

	public FurnanceRecipe(ItemStack ingredient, ItemStack result) {
		super(ingredient, result);
		this.addFuels();
	}

	protected void addFuels() {
		this.others.add(LOTRRecipeHandlerUtils.getFuels());
	}

}
