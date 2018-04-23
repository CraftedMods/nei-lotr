package craftedMods.lotr.nei.recipeHandlers;

import java.util.*;

import craftedMods.lotr.nei.recipeHandlers.HobbitOvenRecipeHandler.HobbitOvenRecipe;
import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.RecipeHandlerRendererUtils;
import craftedMods.recipes.api.utils.RecipeHandlerRendererUtils.EnumProgressBarDirection;
import craftedMods.recipes.base.*;
import lotr.common.tileentity.LOTRTileEntityHobbitOven;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.StatCollector;

@RegisteredHandler
public class HobbitOvenRecipeHandler extends AbstractRecipeHandler<HobbitOvenRecipe> {

	private final HobbitOvenRecipeHandlerRenderer renderer = new HobbitOvenRecipeHandlerRenderer();

	public HobbitOvenRecipeHandler() {
		super("hobbitOven");
	}

	@Override
	public String getDisplayName() {
		return StatCollector.translateToLocal("container.lotr.hobbitOven");
	}

	@Override
	public int getRecipesPerPage() {
		return 1;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<HobbitOvenRecipe> loadSimpleStaticRecipes() {
		Collection<HobbitOvenRecipe> ret = new ArrayList<>();
		((Map<ItemStack, ItemStack>) FurnaceRecipes.smelting().getSmeltingList()).forEach((ingredient, result) -> {
			if (LOTRTileEntityHobbitOven.isCookResultAcceptable(result)) ret.add(new HobbitOvenRecipe(ingredient, result));
		});
		return ret;
	}

	@Override
	public List<RecipeItemSlot> getSlotsForRecipeItems(HobbitOvenRecipe recipe, EnumRecipeItemRole role) {
		List<RecipeItemSlot> slots = new ArrayList<>();
		switch (role) {
			case INGREDIENT:
				for (int i = 0; i < 9; i++)
					slots.add(this.createRecipeItemSlot(3 + i * 18, 10));
				break;
			case RESULT:
				for (int i = 0; i < 9; i++)
					slots.add(this.createRecipeItemSlot(3 + i * 18, 56));
				break;
			case OTHER:
				slots.add(this.createRecipeItemSlot(75, 100));
				break;
		}
		return slots;
	}

	@Override
	@SuppressWarnings("unchecked")
	public HobbitOvenRecipeHandlerRenderer getRenderer() {
		return this.renderer;
	}

	public class HobbitOvenRecipe extends FurnanceRecipe {

		public HobbitOvenRecipe(ItemStack ingredient, ItemStack result) {
			super(ingredient, result);
			this.addAll(Arrays.asList(ingredient, ingredient, ingredient, ingredient, ingredient, ingredient, ingredient, ingredient), this.ingredients);
			this.addAll(Arrays.asList(result, result, result, result, result, result, result, result), this.results);
		}

	}

	public class HobbitOvenRecipeHandlerRenderer implements RecipeHandlerRenderer<HobbitOvenRecipeHandler, HobbitOvenRecipe> {

		@Override
		public void renderBackground(HobbitOvenRecipeHandler handler, HobbitOvenRecipe recipe, int cycleticks) {
			RecipeHandlerRendererUtils.getInstance().bindTexture("lotr:gui/oven.png");
			RecipeHandlerRendererUtils.getInstance().drawTexturedRectangle(0, 0, 5, 11, 166, 117);
			RecipeHandlerRendererUtils.getInstance().drawProgressBar(75, 29, 176, 14, 24, 25, cycleticks % 48 / 48.0f, EnumProgressBarDirection.INCREASE_DOWN);
			RecipeHandlerRendererUtils.getInstance().drawProgressBar(75, 83, 176, 0, 14, 13, cycleticks % 48 / 48.0f, EnumProgressBarDirection.DECREASE_DOWN);

		}

		@Override
		public void renderForeground(HobbitOvenRecipeHandler handler, HobbitOvenRecipe recipe, int cycleticks) {}

	}

}
