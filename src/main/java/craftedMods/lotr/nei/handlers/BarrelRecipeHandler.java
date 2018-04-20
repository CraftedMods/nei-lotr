package craftedMods.lotr.nei.handlers;

import java.util.*;

import codechicken.nei.recipe.ShapedRecipeHandler;
import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.ItemStackSet;
import craftedMods.recipes.base.*;
import lotr.client.gui.LOTRGuiBarrel;
import lotr.common.item.LOTRPoisonedDrinks;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.*;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@RegisteredRecipeHandler
public class BarrelRecipeHandler extends CraftingGridRecipeHandler {

	private final BarrelRecipeHandlerCraftingHelper craftingHelper = new BarrelRecipeHandlerCraftingHelper();

	public static final int[][] BARREL_STACKORDER = new int[][] {
			{
			0, 0 }, {
			1, 0 }, {
			2, 0 }, {
			0, 1 }, {
			1, 1 }, {
			2, 1 }, {
			0, 2 }, {
			1, 2 }, {
			2, 2 } };

	public BarrelRecipeHandler() {
		super("barrel", LOTRRecipeHandlerUtils.getBrewingRecipes());
	}

	@Override
	public Collection<AbstractRecipe> loadSimpleStaticRecipes() {
		Collection<AbstractRecipe> ret = new ArrayList<>();
		for (IRecipe recipe : this.recipes) {
			if (recipe instanceof ShapelessOreRecipe) {
				ShapelessOreRecipe shapelessOreRecipe = (ShapelessOreRecipe) recipe;
				ret.add(new BrewingRecipe(shapelessOreRecipe.getInput(), shapelessOreRecipe.getRecipeOutput()));
			} else {
				this.undefinedRecipeTypeFound(recipe, ret);
			}
		}
		return ret;
	}

	@Override
	public String getDisplayName() {
		return StatCollector.translateToLocal("container.lotr.barrel");
	}

	@Override
	public List<RecipeItemSlot> getSlotsForRecipeItems(AbstractRecipe recipe, EnumRecipeItemRole role) {
		return this.getSlotsForRecipeItems(recipe, role, BARREL_STACKORDER);
	}

	@Override
	public BarrelRecipeHandlerCraftingHelper getCraftingHelper() {
		return craftingHelper;
	}

	public class BarrelRecipeHandlerCraftingHelper extends AbstractCraftingHelper<AbstractRecipe> {

		@Override
		public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses(AbstractRecipe recipe) {
			return Arrays.asList(LOTRGuiBarrel.class);
		}

		@Override
		public int getOffsetX(Class<? extends GuiContainer> guiClass, AbstractRecipe recipe) {
			return -11;
		}

		@Override
		public int getOffsetY(Class<? extends GuiContainer> guiClass, AbstractRecipe recipe) {
			return 28;
		}
	}

	public class BrewingRecipe extends ShapelessRecipe {

		public BrewingRecipe(Collection<?> ingredients, ItemStack result) {
			super(ingredients, result);
			for (int i = 0; i < 3; i++) {
				this.add(new ItemStack(Items.water_bucket), this.ingredients);
			}
			for (int i = 0; i < 4; i++) {
				this.results.get(0).add(new ItemStack(result.getItem(), result.stackSize, result.getItemDamage() + i + 1));
			}
		}

		@Override
		public boolean produces(ItemStack result) {
			if (!LOTRPoisonedDrinks.isDrinkPoisoned(result)) {
				for (ItemStackSet permutations : this.results) {
					if (permutations != null) {
						for (ItemStack permutation : permutations) {
							if (result.getItem() == permutation.getItem()) return true;
						}
					}
				}
			}
			return false;
		}

		@Override
		public ItemStack getResultReplacement(ItemStack defaultReplacement) {
			return defaultReplacement;
		}

	}

}
