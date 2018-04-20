package craftedMods.lotr.nei.handlers;

import java.lang.reflect.Field;
import java.util.*;

import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.RecipeHandlerRendererUtils;
import craftedMods.recipes.base.*;
import lotr.common.recipe.LOTREntJarRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

@RegisteredRecipeHandler
public class EntJarRecipeHandler extends AbstractRecipeHandler<ShapelessRecipe> {

	private final EntJarRecipeHandlerRenderer renderer = new EntJarRecipeHandlerRenderer();

	public EntJarRecipeHandler() {
		super("entJar");
	}

	@Override
	public String getDisplayName() {
		return StatCollector.translateToLocal("tile.lotr:entJar.name");
	}

	@Override
	public Collection<ShapelessRecipe> loadSimpleStaticRecipes() {
		Collection<ShapelessRecipe> ret = new ArrayList<>();
		try {
			Field entJarRecipesField = LOTREntJarRecipes.class.getDeclaredField("recipes");
			entJarRecipesField.setAccessible(true);
			Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) entJarRecipesField.get(null);
			recipes.forEach((ingredient, result) -> {
				ret.add(new ShapelessRecipe(ingredient.copy(), result.copy()));
			});
		} catch (Exception e) {
			this.logger.error("Couldn't access field \"recipes\" in LOTREntJarRecipes.class", e);
		}

		return ret;
	}

	@Override
	public List<RecipeItemSlot> getSlotsForRecipeItems(ShapelessRecipe recipe, EnumRecipeItemRole role) {
		return Arrays.asList(role == EnumRecipeItemRole.INGREDIENT ? this.createRecipeItemSlot(43, 24) : this.createRecipeItemSlot(101, 24));
	}

	@Override
	public EntJarRecipeHandlerRenderer getRenderer() {
		return renderer;
	}
	
	public class EntJarRecipeHandlerRenderer implements RecipeHandlerRenderer<EntJarRecipeHandler, ShapelessRecipe> {

		@Override
		public void renderBackground(EntJarRecipeHandler handler, ShapelessRecipe recipe, int cycleticks) {
			RecipeHandlerRendererUtils.getInstance().bindTexture(DEFAULT_GUI_TEXTURE);
			RecipeHandlerRendererUtils.getInstance().drawTexturedRectangle(42, 19, 65, 30, 80, 26);
			RecipeHandlerRendererUtils.getInstance().drawRectangle(42, 13, 18, 10, 0xFFC6C6C6);
			RecipeHandlerRendererUtils.getInstance().drawRectangle(42, 41, 18, 4, 0xFFC6C6C6);
		}

		@Override
		public void renderForeground(EntJarRecipeHandler handler, ShapelessRecipe recipe, int cycleticks) {}

	}

}
