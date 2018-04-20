package craftedMods.recipes.api;

import java.util.List;

import craftedMods.recipes.api.utils.ItemStackSet;
import net.minecraft.item.ItemStack;

public interface Recipe {

	public List<ItemStackSet> getRecipeItems(EnumRecipeItemRole role);

	public boolean produces(ItemStack result);

	public boolean consumes(ItemStack ingredient);

	public ItemStack getIngredientReplacement(ItemStack defaultReplacement);// Can return null

	public ItemStack getResultReplacement(ItemStack defaultReplacement);// Can return null

}
