package craftedMods.neiLotr.handlers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import codechicken.nei.recipe.ShapelessRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler;
import craftedMods.neiLotr.util.NeiLotrUtil;
import lotr.client.gui.LOTRGuiBarrel;
import lotr.common.recipe.LOTREntJarRecipes;
import lotr.common.recipe.LOTRRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class EntJarRecipeHandler extends ShapelessRecipeHandler {

	private Map<ItemStack, ItemStack> invertedRecipes = new HashMap<ItemStack, ItemStack>();

	public EntJarRecipeHandler() {
		try {
			Field field = LOTREntJarRecipes.class.getDeclaredField("recipes");
			field.setAccessible(true);
			Map<ItemStack, ItemStack> tmp = (Map<ItemStack, ItemStack>) field.get(LOTREntJarRecipes.class);

			ArrayList<ItemStack> keys = new ArrayList<ItemStack>(tmp.keySet());
			ArrayList<ItemStack> values = new ArrayList<ItemStack>(tmp.values());

			for (int i = 0; i < keys.size(); i++) {
				invertedRecipes.put(values.get(i), keys.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getGuiTexture() {
		return NeiLotrUtil.GUI_TEXTURE_PATH_DEFAULT_CRAFTING;
	}

	@Override
	public Class<? extends GuiContainer> getGuiClass() {
		return LOTRGuiBarrel.class;
	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("lotrNei.recipe.entJar.name") + " ("
				+ StatCollector.translateToLocal("lotrNei.recipe.shapeless") + ")";
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if (outputId.equals("item")) {
			loadCraftingRecipes((ItemStack) results[0]);
		}
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		Iterator<ItemStack> it = invertedRecipes.keySet().iterator();
		while (it.hasNext()) {
			ItemStack recipeInput = (ItemStack) it.next();
			if (LOTRRecipes.checkItemEquals(recipeInput, result)) {
				arecipes.add(new CachedShapelessRecipe(new Object[] { (ItemStack) invertedRecipes.get(recipeInput) },
						result));
			}
		}
	}

	@Override
	public void loadUsageRecipes(String inputId, Object... ingredients) {
		if (inputId.equals("item")) {
			loadUsageRecipes((ItemStack) ingredients[0]);
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		ItemStack out = LOTREntJarRecipes.findMatchingRecipe(ingredient);
		if (out != null) {
			arecipes.add(new CachedShapelessRecipe(new Object[] { ingredient }, out));
		}
	}

	@Override
	public TemplateRecipeHandler newInstance() {
		return new EntJarRecipeHandler();
	}

	@Override
	public int recipiesPerPage() {
		return 2;
	}

	@Override
	public void drawBackground(int recipe) {
		super.drawBackground(recipe);
	}

	@Override
	public void drawForeground(int recipe) {
		super.drawForeground(recipe);
	}

	@Override
	public void drawExtras(int recipe) {
		super.drawExtras(recipe);
	}

}
