/*******************************************************************************
 * Copyright (C) 2018 CraftedMods (see https://github.com/CraftedMods)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package craftedMods.lotr.recipes.recipeHandlers;

import java.util.*;

import org.apache.logging.log4j.Logger;

import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import craftedMods.recipes.base.*;
import lotr.common.item.LOTRPoisonedDrinks;
import lotr.common.recipe.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.StatCollector;

@RegisteredHandler
public class VanillaCraftingTableRecipeHandler extends AbstractLOTRCraftingTableRecipeHandler {

	private final VanillaCraftingTableRecipeHandlerCraftingHelper craftingHelper = new VanillaCraftingTableRecipeHandlerCraftingHelper();

	public VanillaCraftingTableRecipeHandler() {
		super("vanillaCrafting");
	}

	@Override
	public String getDisplayName() {
		return StatCollector.translateToLocal("neiLotr.handler.vanillaCrafting.name");
	}

	@Override
	public void onPreLoad(RecipeHandlerConfiguration config, Logger logger) {
		super.onPreLoad(config, logger);
		if (LOTRRecipeHandlerUtils.hasCraftTweaker()) this.logger.info("CraftTweaker was found - dynamic recipe loading will be enabled");
		this.removeRecipeHandler("codechicken.nei.recipe.ShapedRecipeHandler");
		this.removeRecipeHandler("codechicken.nei.recipe.ShapelessRecipeHandler");
	}

	private void removeRecipeHandler(String recipeHandlerClass) {
		try {
			RecipeHandlerUtils.getInstance().removeNativeRecipeHandler(recipeHandlerClass);
		} catch (Exception e) {
			this.logger.error(String.format("Couldn't remove the native recipe handler \"%s\"", recipeHandlerClass));
		}
	}

	@Override
	public Collection<AbstractRecipe> loadSimpleStaticRecipes() {
		return LOTRRecipeHandlerUtils.hasCraftTweaker() ? null : super.loadSimpleStaticRecipes();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Collection<AbstractRecipe> loadRecipes() {
		this.recipes.clear();
		this.recipes.addAll(CraftingManager.getInstance().getRecipeList());
		Collection<AbstractRecipe> ret = super.loadRecipes();
		this.logUndefinedRecipeTypes = false;
		return ret;
	}

	@Override
	protected void undefinedRecipeTypeFound(IRecipe recipe, Collection<AbstractRecipe> container) {
		if (recipe instanceof LOTRRecipesPoisonDrinks) return;
		if (recipe instanceof LOTRRecipeHobbitPipe) return;
		super.undefinedRecipeTypeFound(recipe, container);
	}

	@Override
	public Collection<AbstractRecipe> getDynamicCraftingRecipes(ItemStack result) {
		Collection<AbstractRecipe> ret = new ArrayList<>();
		if (LOTRRecipeHandlerUtils.hasCraftTweaker()) {
			Collection<AbstractRecipe> recipes = this.loadRecipes();
			for (AbstractRecipe recipe : recipes)
				if (recipe.produces(result)) ret.add(recipe);
		}
		return ret;
	}

	@Override
	public Collection<AbstractRecipe> getDynamicUsageRecipes(ItemStack ingredient) {
		Collection<AbstractRecipe> ret = new ArrayList<>();
		if (LOTRRecipeHandlerUtils.hasCraftTweaker()) {
			Collection<AbstractRecipe> recipes = this.loadRecipes();
			for (AbstractRecipe recipe : recipes)
				if (recipe.consumes(ingredient)) ret.add(recipe);
		}
		return ret;
	}

	@Override
	public RecipeHandlerCraftingHelper<AbstractRecipe> getCraftingHelper() {
		return this.craftingHelper;
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
			return this.isRecipe2x2(recipe) ? Arrays.asList(GuiInventory.class, GuiCrafting.class) : Arrays.asList(GuiCrafting.class);
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
			return super.matches(stack1, stack2) && LOTRPoisonedDrinks.isDrinkPoisoned(stack1) == LOTRPoisonedDrinks.isDrinkPoisoned(stack2);
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
