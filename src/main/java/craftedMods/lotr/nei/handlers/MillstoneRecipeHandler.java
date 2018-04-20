package craftedMods.lotr.nei.handlers;

import java.lang.reflect.Field;
import java.util.*;

import craftedMods.lotr.nei.handlers.MillstoneRecipeHandler.MillstoneRecipe;
import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.RecipeHandlerRendererUtils;
import craftedMods.recipes.api.utils.RecipeHandlerRendererUtils.EnumProgressBarDirection;
import craftedMods.recipes.base.*;
import lotr.common.recipe.LOTRMillstoneRecipes;
import lotr.common.recipe.LOTRMillstoneRecipes.MillstoneResult;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

@RegisteredRecipeHandler
public class MillstoneRecipeHandler extends AbstractRecipeHandler<MillstoneRecipe> {

	private final MillstoneRecipeHandlerRenderer renderer = new MillstoneRecipeHandlerRenderer();

	public MillstoneRecipeHandler() {
		super("millstone");
	}

	@Override
	public String getDisplayName() {
		return StatCollector.translateToLocal("container.lotr.millstone");
	}

	@Override
	public Collection<MillstoneRecipe> loadSimpleStaticRecipes() {
		Collection<MillstoneRecipe> ret = new ArrayList<>();
		try {
			Field millstoneRecipesField = LOTRMillstoneRecipes.class.getDeclaredField("recipeList");
			millstoneRecipesField.setAccessible(true);
			Map<ItemStack, MillstoneResult> recipes = (Map<ItemStack, MillstoneResult>) millstoneRecipesField.get(null);
			recipes.forEach((ingredient, result) -> {
				ret.add(new MillstoneRecipe(ingredient, result.resultItem, result.chance));
			});

		} catch (Exception e) {
			this.logger.error("Couldn't access field \"recipes\" in LOTRMillstoneRecipes", e);
		}
		return ret;
	}

	@Override
	public List<RecipeItemSlot> getSlotsForRecipeItems(MillstoneRecipe recipe, EnumRecipeItemRole role) {
		return Arrays.asList(role == EnumRecipeItemRole.INGREDIENT ? createRecipeItemSlot(75, 16) : createRecipeItemSlot(75, 62));
	}

	@Override
	public MillstoneRecipeHandlerRenderer getRenderer() {
		return renderer;
	}
	
	@Override
	public int getRecipesPerPage() {
		return 1;
	}
	
	public class MillstoneRecipe extends ShapelessRecipe {

		private final float chance;

		public MillstoneRecipe(ItemStack ingredient, ItemStack result, float chance) {
			super(ingredient, result);
			this.chance = chance;
		}

		public float getChance() {
			return this.chance;
		}

	}
	
	public class MillstoneRecipeHandlerRenderer implements RecipeHandlerRenderer<MillstoneRecipeHandler, MillstoneRecipe> {

		@Override
		public void renderBackground(MillstoneRecipeHandler handler, MillstoneRecipe recipe, int cycleticks) {
			RecipeHandlerRendererUtils.getInstance().bindTexture("lotr:gui/millstone.png");
			RecipeHandlerRendererUtils.getInstance().drawTexturedRectangle(70, 15, 79, 24, 26, 68);
			RecipeHandlerRendererUtils.getInstance().drawProgressBar(76, 38, 176, 0, 14, 32, cycleticks % 48 / 48.0f, EnumProgressBarDirection.INCREASE_DOWN);
		}

		@Override
		public void renderForeground(MillstoneRecipeHandler handler, MillstoneRecipe recipe, int cycleticks) {
			String text = "Chance: " + recipe.getChance() * 100 + "%";
			RecipeHandlerRendererUtils.getInstance().drawTextCentered(text, 48 + RecipeHandlerRendererUtils.getInstance().getStringWidth(text)/2, 95, 4210752, false);
		}

	}

}
