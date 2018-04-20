package craftedMods.recipes.provider;

import java.util.*;

import org.lwjgl.opengl.GL11;

import codechicken.nei.PositionedStack;
import codechicken.nei.api.*;
import codechicken.nei.recipe.*;
import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.ItemStackSet;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class PluginRecipeHandler<T extends RecipeHandler<U>, U extends Recipe> extends TemplateRecipeHandler {

	private RecipeHandler<U> innerHandler;

	private Map<CachedRecipe, Recipe> recipes = new HashMap<>();

	public PluginRecipeHandler(RecipeHandler<U> innerHandler) {
		this.innerHandler = innerHandler;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		this.innerHandler.onUpdate(this.cycleticks);
	}

	@Override
	public String getRecipeName() {
		return this.innerHandler.getDisplayName();
	}

	@Override
	public int recipiesPerPage() {
		return this.innerHandler.getRecipesPerPage();
	}

	@Override
	public void loadTransferRects() {
		// super.loadTransferRects();//TODO better integration of transfer rects
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("item")) this.loadCraftingRecipes((ItemStack) results[0]);
	}

	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients) {
		if (inputId.equals("item")) this.loadUsageRecipes((ItemStack) ingredients[0]);
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		this.loadRecipes(result, false);
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		this.loadRecipes(ingredient, true);
	}

	private void loadRecipes(ItemStack stack, boolean isUsage) {
		this.recipes.clear();
		this.handleRecipe(stack, isUsage, false, this.innerHandler.getStaticRecipes());
		this.handleRecipe(stack, isUsage, true, isUsage ? this.innerHandler.getDynamicUsageRecipes(stack) : this.innerHandler.getDynamicCraftingRecipes(stack));
	}

	private void handleRecipe(ItemStack stack, boolean isUsage, boolean isDynamic, Collection<? extends Recipe> recipes) {
		if (recipes != null) for (Recipe recipe : recipes)
			if (isDynamic || (isUsage ? recipe.consumes(stack) : recipe.produces(stack))) {
				PluginCachedRecipe pluginRecipe = new PluginCachedRecipe(recipe);// TODO: Is dynamic is false
				if (isUsage ? pluginRecipe.contains(pluginRecipe.ingredients, stack) && recipe.getIngredientReplacement(stack) != null
						: pluginRecipe.contains(pluginRecipe.others, stack) && recipe.getResultReplacement(stack) != null)
					pluginRecipe.setIngredientPermutation(isUsage ? pluginRecipe.ingredients : pluginRecipe.others,
							isUsage ? recipe.getIngredientReplacement(stack) : recipe.getResultReplacement(stack));
				this.recipes.put(pluginRecipe, recipe);
				this.arecipes.add(pluginRecipe);
			}
	}

	@Override
	public void drawBackground(int recipeIndex) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (this.innerHandler.getRenderer() != null)
			this.innerHandler.getRenderer().renderBackground(this.innerHandler, this.getRecipe(recipeIndex), this.cycleticks);
		else super.drawBackground(recipeIndex);
	}

	@Override
	public void drawForeground(int recipeIndex) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_LIGHTING);
		if (this.innerHandler.getRenderer() != null)
			this.innerHandler.getRenderer().renderForeground(this.innerHandler, this.getRecipe(recipeIndex), this.cycleticks);
		else super.drawForeground(recipeIndex);
	}

	@Override
	public String getGuiTexture() {
		return RecipeHandlerRenderer.DEFAULT_GUI_TEXTURE;
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return new PluginRecipeHandler<>(this.innerHandler);
	}

	@Override
	public boolean hasOverlay(GuiContainer gui, Container container, int recipe) {
		return this.innerHandler.getCraftingHelper() != null
				&& this.innerHandler.getCraftingHelper().getSupportedGUIClasses(this.getRecipe(recipe)).contains(gui.getClass());
	}

	@Override
	public IOverlayHandler getOverlayHandler(GuiContainer gui, int recipeIndex) {
		U recipe = this.getRecipe(recipeIndex);
		return this.innerHandler.getCraftingHelper() != null && this.innerHandler.getCraftingHelper().getSupportedGUIClasses(recipe).contains(gui.getClass())
				? new OverlayHandler(this.innerHandler.getCraftingHelper().getOffsetX(gui.getClass(), recipe),
						this.innerHandler.getCraftingHelper().getOffsetY(gui.getClass(), recipe))
				: null;
	}

	@Override
	public IRecipeOverlayRenderer getOverlayRenderer(GuiContainer gui, int recipeIndex) {
		U recipe = this.getRecipe(recipeIndex);
		return this.innerHandler.getCraftingHelper() != null && this.innerHandler.getCraftingHelper().getSupportedGUIClasses(recipe).contains(gui.getClass())
				? new DefaultOverlayRenderer(this.getIngredientStacks(recipeIndex),
						new DefaultIStackPositioner(this.innerHandler.getCraftingHelper().getOffsetX(gui.getClass(), recipe),
								this.innerHandler.getCraftingHelper().getOffsetY(gui.getClass(), recipe)))
				: null;
	}

	@SuppressWarnings("unchecked")
	private U getRecipe(int index) {
		return (U) this.recipes.get(this.arecipes.get(index));
	}

	private class OverlayHandler extends DefaultOverlayHandler {

		public OverlayHandler(int offsetX, int offsetY) {
			super(offsetX, offsetY);
		}

		@Override
		protected boolean canStack(ItemStack stack1, ItemStack stack2) {
			return PluginRecipeHandler.this.innerHandler.getCraftingHelper().matches(stack1, stack2);
		}
	}

	private class DefaultIStackPositioner implements IStackPositioner {

		private final int offsetX;
		private final int offsetY;

		public DefaultIStackPositioner(int offsetX, int offsetY) {
			this.offsetX = offsetX;
			this.offsetY = offsetY;
		}

		@Override
		public ArrayList<PositionedStack> positionStacks(ArrayList<PositionedStack> ingredients) {
			ArrayList<PositionedStack> ret = new ArrayList<>();
			for (PositionedStack stack : ingredients)
				ret.add(new PositionedStack(stack.items, stack.relx + this.offsetX, stack.rely + this.offsetY, false));
			return ret;
		}

	}

	private class PluginCachedRecipe extends TemplateRecipeHandler.CachedRecipe {

		private final Recipe recipe;

		private final List<PositionedStack> ingredients = new ArrayList<>();
		private final List<PositionedStack> others = new ArrayList<>();

		private final boolean isDynamic;

		public PluginCachedRecipe(Recipe recipe) {
			this(recipe, false);
		}

		public PluginCachedRecipe(Recipe recipe, boolean isDynamic) {
			this.recipe = recipe;
			this.isDynamic = isDynamic;
			this.initIngreds();
			this.initOthers();
		}

		@SuppressWarnings("unchecked")
		private void initIngreds() {
			this.ingredients.clear();
			this.addRecipeItems(this.recipe.getRecipeItems(EnumRecipeItemRole.INGREDIENT), this.ingredients, this.recipe,
					((RecipeHandler<Recipe>) (RecipeHandler<?>) PluginRecipeHandler.this.innerHandler).getSlotsForRecipeItems(this.recipe,
							EnumRecipeItemRole.INGREDIENT));
		}

		@SuppressWarnings("unchecked")
		private void initOthers() {
			this.others.clear();
			this.addRecipeItems(this.recipe.getRecipeItems(EnumRecipeItemRole.RESULT), this.others, this.recipe,
					((RecipeHandler<Recipe>) (RecipeHandler<?>) PluginRecipeHandler.this.innerHandler).getSlotsForRecipeItems(this.recipe,
							EnumRecipeItemRole.RESULT));
			this.addRecipeItems(this.recipe.getRecipeItems(EnumRecipeItemRole.OTHER), this.others, this.recipe,
					((RecipeHandler<Recipe>) (RecipeHandler<?>) PluginRecipeHandler.this.innerHandler).getSlotsForRecipeItems(this.recipe,
							EnumRecipeItemRole.OTHER));
		}

		private void addRecipeItems(List<ItemStackSet> source, List<PositionedStack> target, Recipe recipe, List<RecipeItemSlot> slotList) {
			for (int i = 0; i < source.size(); i++) {
				ItemStackSet items = source.get(i);
				if (items != null) {
					RecipeItemSlot slot = i < slotList.size() ? slotList.get(i) : null;
					int x = 0;
					int y = 0;
					if (slot != null) {
						x = slot.getX();
						y = slot.getY();
					}
					target.add(new PositionedStack(new ArrayList<>(items), x, y));
				}
			}
		}

		@Override
		public PositionedStack getResult() {
			return null;
		}

		@Override
		public List<PositionedStack> getIngredients() {
			if (this.isDynamic && PluginRecipeHandler.this.cycleticks % 20 == 0) this.initIngreds();
			return this.getCycledIngredients(PluginRecipeHandler.this.cycleticks / 20, this.ingredients);
		}

		@Override
		public List<PositionedStack> getOtherStacks() {
			if (this.isDynamic && PluginRecipeHandler.this.cycleticks % 20 == 0) this.initOthers();
			return this.getCycledIngredients(PluginRecipeHandler.this.cycleticks / 20, this.others);
		}

		// public Recipe getRecipe() {
		// return this.recipe;
		// }

	}

}
