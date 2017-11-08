package craftedMods.neiLotr.handlers.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapelessRecipeHandler.CachedShapelessRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import craftedMods.neiLotr.util.NeiLotrUtil;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public abstract class ExtendedShapelessRecipeHandler extends ExtendedShapedRecipeHandler {

	public static final int[][] STACKORDER = new int[][] { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 }, { 0, 2 }, { 1, 2 },
			{ 2, 0 }, { 2, 1 }, { 2, 2 } };

	public ExtendedShapelessRecipeHandler() {
		super();
	}

	public ExtendedShapelessRecipeHandler(long id) {
		super(id);
	}

	@Override
	public String getRecipeName() {
		return NEIClientUtils.translate("recipe.shapeless");
	}

	@Override
	public CachedRecipe getCachedRecipe(IRecipe recipe) {
		ExtendedCachedShapelessRecipe ret = null;
		if (recipe instanceof ShapelessRecipes) {
			ret = new ExtendedCachedShapelessRecipe((ShapelessRecipes) recipe);
		} else if (recipe instanceof ShapelessOreRecipe) {
			ret = new ExtendedCachedShapelessRecipe((ShapelessOreRecipe) recipe);
		}
		return ret;
	}

	@Override
	public CachedRecipe newRecipeInstance(CachedRecipe oldInstance) {
		CachedRecipe ret = null;
		if (oldInstance instanceof ExtendedCachedShapelessRecipe) {
			ExtendedCachedShapelessRecipe recipe = (ExtendedCachedShapelessRecipe) oldInstance;
			ret = this.new ExtendedCachedShapelessRecipe(recipe);
		}
		return ret;
	}

	public class ExtendedCachedShapelessRecipe extends TemplateRecipeHandler.CachedRecipe {

		protected List<PositionedStack> ingredients = new ArrayList<>();
		protected List<PositionedStack> results = new ArrayList<>();
		protected List<PositionedStack> others = new ArrayList<>();

		public ExtendedCachedShapelessRecipe() {
			super();
		}

		public ExtendedCachedShapelessRecipe(List<?> output) {
			this();
			this.setResult(output);
		}

		public ExtendedCachedShapelessRecipe(List<?> input, List<?> output) {
			this(output);
			this.setIngredients(input);
		}

		public ExtendedCachedShapelessRecipe(ShapelessRecipes rec) {
			this(Arrays.asList(rec.recipeItems), Arrays.asList(rec.getRecipeOutput()));
		}

		public ExtendedCachedShapelessRecipe(ShapelessOreRecipe rec) {
			this(rec.getInput(), Arrays.asList(rec.getRecipeOutput()));
		}

		public ExtendedCachedShapelessRecipe(CachedShapelessRecipe rec) {
			this(Arrays.asList(NeiLotrUtil.extractRecipeItems(rec.ingredients)), Arrays.asList(rec.result.item));
		}

		public ExtendedCachedShapelessRecipe(ExtendedCachedShapelessRecipe rec) {
			this();
			this.ingredients.addAll(NeiLotrUtil.copyPositionedStackList(rec.ingredients));
			this.results.addAll(NeiLotrUtil.copyPositionedStackList(rec.results));
			this.others.addAll(NeiLotrUtil.copyPositionedStackList(rec.others));
		}

		@Override
		public PositionedStack getResult() {
			return getCycledResult(cycleticks / 20, results.get(0));
		}

		public PositionedStack getCycledResult(int cycle, PositionedStack result) {
			randomRenderPermutation(result, cycle);
			return result;
		}

		public void setResult(PositionedStack stack) {
			this.results.clear();
			this.results.add(stack);
		}

		public void setResult(List<?> result) {
			this.setResult(new PositionedStack(NeiLotrUtil.extractRecipeItems(result), 119, 24));
		}

		public void setResults(List<PositionedStack> results) {
			this.results.clear();
			this.results.addAll(results);
		}

		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(cycleticks / 20, ingredients);
		}

		public void setIngredients(List<?> items) {
			ingredients.clear();
			for (int i = 0; i < items.size(); i++) {
				PositionedStack stack = new PositionedStack(NeiLotrUtil.extractRecipeItems(items.get(i)),
						25 + STACKORDER[i][0] * 18, 6 + STACKORDER[i][1] * 18);
				stack.setMaxSize(1);
				ingredients.add(stack);
			}
		}

		@Override
		public List<PositionedStack> getOtherStacks() {
			if (this.getOtherStack() != null) {
				others.add(this.getOtherStack());
			}
			return getCycledOthers(cycleticks / 20, others);
		}

		public List<PositionedStack> getCycledOthers(int cycle, List<PositionedStack> others) {
			return this.getCycledIngredients(cycle, others);
		}
	}

}
