package craftedMods.neiLotr.handlers.craftingTable.regular;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import codechicken.nei.ItemStackMap;
import codechicken.nei.NEIClientUtils;
import craftedMods.neiLotr.handlers.template.ExtendedShapelessRecipeHandler;
import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemMug;
import lotr.common.item.LOTRPoisonedDrinks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class PoisonedDrinkRecipeHandler extends ExtendedShapelessRecipeHandler {

	private final ItemStackMap<Set<ExtendedCachedShapelessRecipe>> craftingRecipes = new ItemStackMap<>();
	private final ItemStackMap<Set<ExtendedCachedShapelessRecipe>> usageRecipes = new ItemStackMap<>();

	private final ItemStack POISON_STACK = new ItemStack(LOTRMod.bottlePoison);

	public PoisonedDrinkRecipeHandler() {
		super();
	}

	public PoisonedDrinkRecipeHandler(long id) {
		super(id);
	}

	public void computeRecipes(Integer count, ItemStack stack) {
		if (stack.getItemDamage() == 0 && stack.getItem() instanceof LOTRItemMug) {
			Item item = stack.getItem();
			List<ItemStack> list = new ArrayList<>();
			item.getSubItems(item, null, list);
			list.forEach(stack2 -> {
				if (LOTRPoisonedDrinks.canPoison(stack2)) {
					ItemStack result = stack2.copy();
					LOTRPoisonedDrinks.setDrinkPoisoned(result, true);
					ItemStack ingred = stack2.copy();
					ExtendedCachedShapelessRecipe recipe = new ExtendedCachedShapelessRecipe(
							Arrays.asList(ingred, POISON_STACK), Arrays.asList(result));
					if (craftingRecipes.get(result) == null)
						craftingRecipes.put(result, new HashSet<>());
					craftingRecipes.get(result).add(recipe);
					if (usageRecipes.get(ingred) == null)
						usageRecipes.put(ingred, new HashSet<>());
					usageRecipes.get(ingred).add(recipe);
					if (usageRecipes.get(POISON_STACK) == null)
						usageRecipes.put(POISON_STACK, new HashSet<>());
					usageRecipes.get(POISON_STACK).add(recipe);
				}
			});
		}
	}

	@Override
	public void loadDynamicCraftingRecipes(ItemStack result) {
		if (craftingRecipes.get(result) != null) {
			for (ExtendedCachedShapelessRecipe instance : craftingRecipes.get(result)) {
				arecipes.add(this.newRecipeInstance(instance));
			}
		}
	}

	@Override
	public void loadDynamicUsageRecipes(ItemStack ingred) {
		if (usageRecipes.get(ingred) != null) {
			for (ExtendedCachedShapelessRecipe instance : usageRecipes.get(ingred)) {
				arecipes.add(this.newRecipeInstance(instance));
			}
		}
	}

}