package craftedMods.recipes.base;

import java.util.*;

import craftedMods.recipes.api.*;
import net.minecraft.item.crafting.*;
import net.minecraftforge.oredict.*;

public abstract class CraftingGridRecipeHandler extends AbstractRecipeHandler<AbstractRecipe> {

	protected final Collection<IRecipe> recipes = new ArrayList<>();

	public static final int[][] DEFAULT_SHAPELESS_STACKORDER = new int[][] { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 }, { 0, 2 }, { 1, 2 }, { 2, 0 }, { 2, 1 }, { 2, 2 } };

	protected boolean logUndefinedRecipeTypes = true;

	protected CraftingGridRecipeHandler(String unlocalizedName) {
		super(unlocalizedName);
	}

	protected CraftingGridRecipeHandler(String unlocalizedName, Collection<IRecipe> recipes) {
		super(unlocalizedName);
		this.recipes.addAll(recipes);
	}

	@Override
	public Collection<AbstractRecipe> loadSimpleStaticRecipes() {
		return this.loadRecipes();
	}

	protected Collection<AbstractRecipe> loadRecipes() {
		List<AbstractRecipe> ret = new ArrayList<>();
		for (IRecipe recipe : this.recipes)
			if (recipe instanceof ShapedOreRecipe) {
				ShapedOreRecipe shapedOreRecipe = (ShapedOreRecipe) recipe;
				for (Object ingred : shapedOreRecipe.getInput())
					if (ingred instanceof List<?> && ((List<?>) ingred).isEmpty()) continue;
				try {
					ret.add(new ShapedRecipe(shapedOreRecipe));
				} catch (Exception e) {
					this.logger.error("Couldn't load shaped ore recipe: ", e);
				}
			} else if (recipe instanceof ShapedRecipes) ret.add(new ShapedRecipe((ShapedRecipes) recipe));
			else if (recipe instanceof ShapelessOreRecipe) {
				ShapelessOreRecipe shapelessOreRecipe = (ShapelessOreRecipe) recipe;
				for (Object ingred : shapelessOreRecipe.getInput())
					if (ingred instanceof List<?> && ((List<?>) ingred).isEmpty()) continue;
				ret.add(new ShapelessRecipe(shapelessOreRecipe));
			} else if (recipe instanceof ShapelessRecipes) {
				ShapelessRecipes shapelessRecipe = (ShapelessRecipes) recipe;
				if (shapelessRecipe.recipeItems != null) ret.add(new ShapelessRecipe(shapelessRecipe));
			} else this.undefinedRecipeTypeFound(recipe, ret);
		return ret;
	}

	protected void undefinedRecipeTypeFound(IRecipe recipe, Collection<AbstractRecipe> container) {
		if (this.logUndefinedRecipeTypes) this.logger
				.warn("The recipe handler \"" + this.getUnlocalizedName() + "\" got a recipe (\"" + recipe.getClass() + "\") which couldn't be processed");
	}

	@Override
	public List<RecipeItemSlot> getSlotsForRecipeItems(AbstractRecipe recipe, EnumRecipeItemRole role) {
		return this.getSlotsForRecipeItems(recipe, role, CraftingGridRecipeHandler.DEFAULT_SHAPELESS_STACKORDER);
	}

	@SuppressWarnings("incomplete-switch")
	protected List<RecipeItemSlot> getSlotsForRecipeItems(AbstractRecipe recipe, EnumRecipeItemRole role, int[][] shapelessStackorder) {
		List<RecipeItemSlot> ret = new ArrayList<>();
		switch (role) {
			case INGREDIENT:
				if (recipe instanceof ShapedRecipe) {
					ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
					for (int x = 0; x < shapedRecipe.getWidth(); x++)
						for (int y = 0; y < shapedRecipe.getHeight(); y++)
							ret.add(this.createRecipeItemSlot(25 + x * 18, 6 + y * 18));
				} else if (recipe instanceof ShapelessRecipe) {
					ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;
					for (int i = 0; i < shapelessRecipe.getRecipeItems(EnumRecipeItemRole.INGREDIENT).size() && i < 9; i++)
						ret.add(this.createRecipeItemSlot(25 + shapelessStackorder[i][0] * 18, 6 + shapelessStackorder[i][1] * 18));
				}
				break;
			case RESULT:
				ret.add(this.createRecipeItemSlot(119, 24));
				break;
		}
		return ret;
	}

}
