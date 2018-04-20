package craftedMods.lotr.nei.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.Logger;

import craftedMods.recipes.api.EnumRecipeItemRole;
import craftedMods.recipes.api.RecipeHandlerConfiguration;
import craftedMods.recipes.api.RecipeHandlerCraftingHelper;
import craftedMods.recipes.api.RegisteredRecipeHandler;
import craftedMods.recipes.base.AbstractCraftingHelper;
import craftedMods.recipes.base.AbstractRecipe;
import craftedMods.recipes.base.ShapedRecipe;
import craftedMods.recipes.base.ShapelessRecipe;
import craftedMods.recipes.utils.NEIExtensionsUtils;
import lotr.common.item.LOTRPoisonedDrinks;
import lotr.common.recipe.LOTRRecipeHobbitPipe;
import lotr.common.recipe.LOTRRecipesPoisonDrinks;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

@RegisteredRecipeHandler
public class VanillaCraftingTableRecipeHandler extends AbstractLOTRCraftingTableRecipeHandler {

	private final VanillaCraftingTableRecipeHandlerCraftingHelper craftingHelper = new VanillaCraftingTableRecipeHandlerCraftingHelper();

	public VanillaCraftingTableRecipeHandler() {
		super("vanillaCrafting");
	}

	@Override
	public String getDisplayName() {
		return "Vanilla Crafting";
	}

	@Override
	public void onPreLoad(RecipeHandlerConfiguration config, Logger logger) {
		super.onPreLoad(config, logger);
		if (LOTRRecipeHandlerUtils.hasCraftTweaker())
			this.logger.info("CraftTweaker was found - dynamic recipe loading will be enabled");
	}

	@Override
	public Collection<AbstractRecipe> loadSimpleStaticRecipes() {
		return LOTRRecipeHandlerUtils.hasCraftTweaker() ? null : super.loadSimpleStaticRecipes();
	}

	@Override
	protected Collection<AbstractRecipe> loadRecipes() {
		this.recipes.clear();
		this.recipes.addAll(CraftingManager.getInstance().getRecipeList());
		Collection<AbstractRecipe> ret = super.loadRecipes();
		this.logUndefinedRecipeTypes = false;
		return ret;
	}

	@Override
	protected void undefinedRecipeTypeFound(IRecipe recipe, Collection<AbstractRecipe> container) {
		if (recipe instanceof LOTRRecipesPoisonDrinks)
			return;
		if (recipe instanceof LOTRRecipeHobbitPipe)
			return;
		super.undefinedRecipeTypeFound(recipe, container);
	}

	@Override
	public Collection<AbstractRecipe> getDynamicCraftingRecipes(ItemStack result) {
		Collection<AbstractRecipe> ret = new ArrayList<>();
		if (LOTRRecipeHandlerUtils.hasCraftTweaker()) {
			Collection<AbstractRecipe> recipes = this.loadRecipes();
			for (AbstractRecipe recipe : recipes) {
				if (recipe.produces(result))
					ret.add(recipe);
			}
		}
		return ret;
	}

	@Override
	public Collection<AbstractRecipe> getDynamicUsageRecipes(ItemStack ingredient) {
		Collection<AbstractRecipe> ret = new ArrayList<>();
		if (LOTRRecipeHandlerUtils.hasCraftTweaker()) {
			Collection<AbstractRecipe> recipes = this.loadRecipes();
			for (AbstractRecipe recipe : recipes) {
				if (recipe.consumes(ingredient))
					ret.add(recipe);
			}
		}
		return ret;
	}

	@Override
	public RecipeHandlerCraftingHelper getCraftingHelper() {
		return craftingHelper;
	}

	@Override
	public AbstractRecipe loadComplicatedStaticRecipe(ItemStack... stacks) {
		ItemStack stack = stacks[0];
		PoisonedDrinkRecipe recipe = null;
		if (LOTRPoisonedDrinks.canPoison(stack)) {
			List<Object> ingredients = new ArrayList<>();
			ingredients.add(stack.copy());
			ingredients.add(LOTRRecipeHandlerUtils.getPoison());
			ItemStack result = stack.copy();
			LOTRPoisonedDrinks.setDrinkPoisoned(result, true);
			recipe = new PoisonedDrinkRecipe(ingredients, result);
		}
		return recipe;
	}

	@Override
	public int getComplicatedStaticRecipeDepth() {
		return 1;
	}

	public class VanillaCraftingTableRecipeHandlerCraftingHelper extends AbstractCraftingHelper<AbstractRecipe> {

		@Override
		public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses(AbstractRecipe recipe) {
			return isRecipe2x2(recipe) ? Arrays.asList(GuiInventory.class, GuiCrafting.class)
					: Arrays.asList(GuiCrafting.class);
		}

		@Override
		public int getOffsetX(Class<? extends GuiContainer> guiClass, AbstractRecipe recipe) {
			return guiClass == GuiInventory.class ? 63 : 5;
		}

		@Override
		public int getOffsetY(Class<? extends GuiContainer> guiClass, AbstractRecipe recipe) {
			return guiClass == GuiInventory.class ? 20 : 11;
		}

		@Override
		public boolean matches(ItemStack stack1, ItemStack stack2) {
			return super.matches(stack1, stack2)
					&& LOTRPoisonedDrinks.isDrinkPoisoned(stack1) == LOTRPoisonedDrinks.isDrinkPoisoned(stack2);
		}

		private boolean isRecipe2x2(AbstractRecipe recipe) {
			boolean ret = recipe.getRecipeItems(EnumRecipeItemRole.INGREDIENT).size() <= 4;
			if (recipe instanceof ShapedRecipe) {
				ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
				ret = shapedRecipe.getWidth() <= 2 && shapedRecipe.getHeight() <= 2;
			}
			return ret;
		}
	}

	public class PoisonedDrinkRecipe extends ShapelessRecipe {

		public PoisonedDrinkRecipe(List<?> ingredients, ItemStack result) {
			super(ingredients, result);
		}

		@Override
		public boolean produces(ItemStack result) {
			return LOTRPoisonedDrinks.isDrinkPoisoned(result) ? super.produces(result) : false;
		}

		@Override
		public boolean consumes(ItemStack ingredient) {
			return !LOTRPoisonedDrinks.isDrinkPoisoned(ingredient) ? super.consumes(ingredient) : false;
		}

	}

}