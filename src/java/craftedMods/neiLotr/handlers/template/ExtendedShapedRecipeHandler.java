package craftedMods.neiLotr.handlers.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.ShapedRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import craftedMods.neiLotr.NeiLotr;
import craftedMods.neiLotr.util.NeiLotrUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;

public abstract class ExtendedShapedRecipeHandler extends ShapedRecipeHandler implements AdvancedRecipeLoading {

	private long id = -1;

	protected Set<RecipeSubhandler> subhandlers = new HashSet<>();
	protected Set<RecipeAntihandler> antihandlers = new HashSet<>();
	
	protected final StaticRecipeLoader recipeLoader = NeiLotr.mod.getStaticRecipeLoader();

	public ExtendedShapedRecipeHandler() {
		super();
	}

	public ExtendedShapedRecipeHandler(long id) {
		this();
		this.id = id;
	}

	@Override
	public long getHandlerId() {
		return id;
	}

	public CachedRecipe getCachedRecipe(IRecipe recipe) {
		CachedRecipe ret = null;
		if (recipe instanceof ShapedRecipes) {
			ret = new ExtendedCachedShapedRecipe((ShapedRecipes) recipe);
		} else if (recipe instanceof ShapedOreRecipe) {
			ret = new ExtendedCachedShapedRecipe((ShapedOreRecipe) recipe);
		}
		return ret;
	}
	
	@Override
	public void loadTransferRects() {
		//super.loadTransferRects();//TODO better integration of transfer rects
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("item")) {
			this.loadCraftingRecipes((ItemStack) results[0]);
		}
	}

	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients) {
		if (inputId.equals("item")) {
			this.loadUsageRecipes((ItemStack) ingredients[0]);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		for (RecipeAntihandler antihandler : antihandlers) {
			if (antihandler.stopLoadCraftingRecipes(result))
				return;
		}
		Set<CachedRecipe> recipes = recipeLoader.getCraftingRecipes(id, this, result);
		if (recipes != null)
			arecipes.addAll(recipes);
		loadDynamicCraftingRecipes(result);
		subhandlers.forEach(subhandler -> arecipes.addAll(subhandler.loadCraftingRecipes(result)));
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		for (RecipeAntihandler antihandler : antihandlers) {
			if (antihandler.stopLoadUsageRecipes(ingredient))
				return;
		}
		Set<CachedRecipe> recipes = recipeLoader.getUsageRecipes(id, this, ingredient);
		if (recipes != null)
			arecipes.addAll(recipes);
		loadDynamicUsageRecipes(ingredient);
		subhandlers.forEach(subhandler -> arecipes.addAll(subhandler.loadUsageRecipes(ingredient)));
	}

	public boolean addSubhandler(RecipeSubhandler subhandler) {
		return this.subhandlers.add(subhandler);
	}

	public boolean removeSubhandler(RecipeSubhandler subhandler) {
		return this.subhandlers.remove(subhandler);
	}

	public Set<RecipeSubhandler> getSubhandlers() {
		return Collections.unmodifiableSet(subhandlers);
	}

	public boolean addAntihandler(RecipeAntihandler antihandler) {
		return this.antihandlers.add(antihandler);
	}

	public boolean removeAntihandler(RecipeAntihandler antihandler) {
		return this.antihandlers.remove(antihandler);
	}

	public Set<RecipeAntihandler> getAntihandlers() {
		return Collections.unmodifiableSet(antihandlers);
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		TemplateRecipeHandler instance = null;
		try {
			instance = super.newInstance();
		} catch (RuntimeException e) {
			NeiLotr.mod.getLogger().error("Could not create new instance of " + this.getClass().getSimpleName() + ": ",
					e);
		}
		this.transferImportantData((ExtendedShapedRecipeHandler) instance);
		return instance;

	}

	protected void transferImportantData(ExtendedShapedRecipeHandler newInstance) {
		newInstance.id = this.id;
		transferOtherHandlers(newInstance);
	}

	protected void transferOtherHandlers(ExtendedShapedRecipeHandler newInstance) {
		newInstance.antihandlers = this.antihandlers;
		subhandlers.forEach(subhandler -> {
			newInstance.subhandlers.add(subhandler.newInstance(newInstance));
		});
	}

	@Override
	public CachedRecipe newRecipeInstance(CachedRecipe oldInstance) {
		CachedRecipe ret = null;
		if (oldInstance instanceof ExtendedCachedShapedRecipe) {
			ExtendedCachedShapedRecipe recipe = (ExtendedCachedShapedRecipe) oldInstance;
			if (recipe.fromShapedRecipes()) {
				ret = new ExtendedCachedShapedRecipe(recipe.recipe);
			} else {
				List<ItemStack> items = NeiLotrUtil
						.fillToRecipeSize(NeiLotrUtil.extractRecipeItems(recipe.ingredients));
				ExtendedCachedShapedRecipe rec = new ExtendedCachedShapedRecipe(recipe.width, recipe.height,
						items.toArray(new Object[recipe.ingredients.size()]), recipe.result.item);
				rec.ingredients = NeiLotrUtil.copyPositionedStackList(recipe.ingredients);
				recipe.others = NeiLotrUtil.copyPositionedStackList(recipe.others);
				rec.result = recipe.result.copy();
				ret = rec;
			}
		}
		return ret;
	}

	public class ExtendedCachedShapedRecipe extends ShapedRecipeHandler.CachedShapedRecipe {

		public List<PositionedStack> others = new ArrayList<PositionedStack>();

		private int width = -1;
		private int height = -1;

		private ShapedRecipes recipe;

		public ExtendedCachedShapedRecipe(int width, int height, Object[] items, ItemStack out) {
			super(width, height, items, out);
			this.width = width;
			this.height = height;
		}

		public ExtendedCachedShapedRecipe(ShapedRecipes recs) {
			super(recs);
			this.recipe = recs;
		}

		public ExtendedCachedShapedRecipe(ShapedOreRecipe rec) {
			this((Integer) ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, rec, 4),
					(Integer) ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, rec, 5), rec.getInput(),
					rec.getRecipeOutput());
		}

		public boolean fromShapedRecipes() {
			return recipe != null;
		}
	}

}
