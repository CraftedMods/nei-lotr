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
package craftedMods.lotr.recipes.internal.recipeHandlers;

import java.awt.Color;
import java.util.*;
import java.util.function.*;

import craftedMods.lotr.recipes.api.utils.TriFunction;
import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.*;
import craftedMods.recipes.base.*;
import lotr.common.item.*;
import lotr.common.recipe.LOTRRecipePoisonWeapon;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.item.ItemArmor.ArmorMaterial;

@RegisteredHandler
public class CauldronRecipeHandler extends AbstractRecipeHandler<ShapelessRecipe> {

	private final CauldronRecipeHandlerRenderer renderer = new CauldronRecipeHandlerRenderer();

	public CauldronRecipeHandler() {
		super("lotr.cauldron");
	}

	@Override
	public String getDisplayName() {
		return Blocks.cauldron.getLocalizedName();
	}

	@Override
	public Collection<ShapelessRecipe> getDynamicCraftingRecipes(ItemStack result) {
		Collection<ShapelessRecipe> recipes = new ArrayList<>();

		this.handleUndyedResultItem(LOTRItemPouch.class, result, LOTRItemPouch::isPouchDyed,
				this.getDefaultColorPermutations(result, LOTRItemPouch::setPouchColor), recipes);
		this.handleUndyedResultItem(LOTRItemHobbitPipe.class, result, LOTRItemHobbitPipe::isPipeDyed, this.getHobbitPipeDyePermutations(result), recipes);
		this.handleUndyedResultItem(LOTRItemLeatherHat.class, result,
				((Predicate<ItemStack>) LOTRItemLeatherHat::isHatDyed).or(LOTRItemLeatherHat::isFeatherDyed), this.getLeatherHatDyePermutations(result),
				recipes);
		this.handleUndyedResultItem(LOTRItemFeatherDyed.class, result, LOTRItemFeatherDyed::isFeatherDyed,
				this.getDefaultColorPermutations(result, LOTRItemFeatherDyed::setFeatherColor), recipes);
		this.handleUndyedResultItem(LOTRItemHaradRobes.class, result, LOTRItemHaradRobes::areRobesDyed,
				this.getDefaultColorPermutations(result, LOTRItemHaradRobes::setRobesColor), recipes);
		this.handleUndyedResultItem(LOTRItemPartyHat.class, result, LOTRItemPartyHat::isHatDyed,
				this.getDefaultColorPermutations(result, LOTRItemPartyHat::setHatColor), recipes);
		if (result.getItem() instanceof ItemArmor && recipes.isEmpty()) {
			ItemArmor armorItem = (ItemArmor) result.getItem();
			if (armorItem.getArmorMaterial() == ArmorMaterial.CLOTH) {
				this.handleUndyedResultItem(ItemArmor.class, result, armorItem::hasColor, this.getDefaultColorPermutations(result, armorItem::func_82813_b),
						recipes);
			}
		}
		if (LOTRRecipePoisonWeapon.inputToPoisoned.containsKey(result.getItem())) {
			ItemStack input = new ItemStack(LOTRRecipePoisonWeapon.inputToPoisoned.get(result.getItem()));
			if (result.isItemStackDamageable()) {
				input.setItemDamage(result.getItemDamage());
			}
			recipes.add(new ShapelessRecipe(input, result));
		}
		return recipes;
	}

	private <T extends Item> void handleUndyedResultItem(Class<T> itemType, ItemStack result, Predicate<ItemStack> isDyedFunction,
			Function<ItemStack, Collection<ItemStack>> permutationsFunction, Collection<ShapelessRecipe> recipes) {
		if (itemType.isAssignableFrom(result.getItem().getClass()) && !isDyedFunction.test(result)) {
			recipes.add(new UndyeningRecipe(permutationsFunction.apply(result.copy()), result));
		}
	}

	private Function<ItemStack, Collection<ItemStack>> getHobbitPipeDyePermutations(ItemStack result) {
		Collection<ItemStack> data = new ArrayList<>();
		for (int i = 0; i < 15; i++) {
			ItemStack dyedStack = result.copy();
			LOTRItemHobbitPipe.setSmokeColor(dyedStack, i + 1);
			data.add(dyedStack);
		}
		return (stack) -> data;
	}

	private Function<ItemStack, Collection<ItemStack>> getDefaultColorPermutations(ItemStack result,
			TriFunction<Integer, Integer, Integer, ItemStack> function) {
		Collection<ItemStack> data = new ArrayList<>();
		for (int r = 0; r < 255; r += 25) {
			for (int g = 0; g < 255; g += 25) {
				for (int b = 0; b < 255; b += 25)
					if (r < 255 && g < 255 && b < 255) {
						data.add(function.accept(r, g, b));
					}
			}
		}
		return (stack) -> data;
	}

	private Function<ItemStack, Collection<ItemStack>> getDefaultColorPermutations(ItemStack result, BiConsumer<ItemStack, Integer> dyeFunction) {
		return this.getDefaultColorPermutations(result, (r, g, b) -> {
			ItemStack dyedStack = result.copy();
			dyeFunction.accept(dyedStack, new Color(r, g, b, 0).getRGB());// ItemArmor doesn't work if alpha > 127
			return dyedStack;
		});
	}

	private Function<ItemStack, Collection<ItemStack>> getLeatherHatDyePermutations(ItemStack result) {
		return this.getDefaultColorPermutations(result, (stack, colorInt) -> {
			Color color = new Color(colorInt);
			LOTRItemLeatherHat.setFeatherColor(stack, colorInt);
			LOTRItemLeatherHat.setHatColor(stack, new Color(color.getGreen(), color.getRed(), color.getBlue()).getRGB());
		});
	}

	@Override
	public Collection<ShapelessRecipe> getDynamicUsageRecipes(ItemStack ingredient) {
		Collection<ShapelessRecipe> recipes = new ArrayList<>();

		this.handleDyeableIngredientItem(LOTRItemPouch.class, ingredient, LOTRItemPouch::isPouchDyed, LOTRItemPouch::removePouchDye, recipes);
		this.handleDyeableIngredientItem(LOTRItemHobbitPipe.class, ingredient, LOTRItemHobbitPipe::isPipeDyed, LOTRItemHobbitPipe::removePipeDye, recipes);
		this.handleDyeableIngredientItem(LOTRItemLeatherHat.class, ingredient,
				((Predicate<ItemStack>) LOTRItemLeatherHat::isHatDyed).or(LOTRItemLeatherHat::isFeatherDyed), LOTRItemLeatherHat::removeHatAndFeatherDye,
				recipes);
		this.handleDyeableIngredientItem(LOTRItemFeatherDyed.class, ingredient, LOTRItemFeatherDyed::isFeatherDyed, LOTRItemFeatherDyed::removeFeatherDye,
				recipes);
		this.handleDyeableIngredientItem(LOTRItemHaradRobes.class, ingredient, LOTRItemHaradRobes::areRobesDyed, LOTRItemHaradRobes::removeRobeDye, recipes);
		this.handleDyeableIngredientItem(LOTRItemPartyHat.class, ingredient, LOTRItemPartyHat::isHatDyed, LOTRItemPartyHat::removeHatDye, recipes);
		if (ingredient.getItem() instanceof ItemArmor) {
			ItemArmor armorItem = (ItemArmor) ingredient.getItem();
			this.handleDyeableIngredientItem(ItemArmor.class, ingredient, armorItem::hasColor, armorItem::removeColor, recipes);

		}
		if (LOTRRecipePoisonWeapon.poisonedToInput.containsKey(ingredient.getItem())) {
			ItemStack result = new ItemStack(LOTRRecipePoisonWeapon.poisonedToInput.get(ingredient.getItem()));
			if (ingredient.isItemStackDamageable()) {
				result.setItemDamage(ingredient.getItemDamage());
			}
			recipes.add(new ShapelessRecipe(ingredient, result));
		}
		return recipes;
	}

	private <T extends Item> void handleDyeableIngredientItem(Class<T> itemType, ItemStack ingredient, Predicate<ItemStack> isDyedFunction,
			Consumer<ItemStack> removeDyeFunction, Collection<ShapelessRecipe> recipes) {
		if (itemType.isAssignableFrom(ingredient.getItem().getClass()) && isDyedFunction.test(ingredient)) {
			ItemStack resultStack = ingredient.copy();
			removeDyeFunction.accept(resultStack);
			recipes.add(new ShapelessRecipe(ingredient, resultStack));
		}
	}

	@Override
	public List<RecipeItemSlot> getSlotsForRecipeItems(ShapelessRecipe recipe, EnumRecipeItemRole role) {
		return Arrays.asList(role == EnumRecipeItemRole.INGREDIENT ? this.createRecipeItemSlot(43, 24) : this.createRecipeItemSlot(101, 24));
	}

	@Override
	@SuppressWarnings("unchecked")
	public CauldronRecipeHandlerRenderer getRenderer() {
		return this.renderer;
	}

	public class UndyeningRecipe extends ShapelessRecipe {

		public UndyeningRecipe(Collection<ItemStack> ingredients, ItemStack result) {
			super(Arrays.asList(), result);
			this.ingredients.add(this.createItemStackSet(ingredients));
		}

		@Override
		protected ItemStackSet createItemStackSet(ItemStack... stacks) {
			return ItemStackSet.create(true, stacks);
		}

		protected ItemStackSet createItemStackSet(Collection<ItemStack> stacks) {
			return ItemStackSet.create(true, stacks);
		}

		@Override
		public ItemStack getResultReplacement(ItemStack defaultReplacement) {
			return defaultReplacement;
		}

	}

	public class CauldronRecipeHandlerRenderer implements RecipeHandlerRenderer<CauldronRecipeHandler, ShapelessRecipe> {

		@Override
		public void renderBackground(CauldronRecipeHandler handler, ShapelessRecipe recipe, int cycleticks) {
			RecipeHandlerRendererUtils.getInstance().bindTexture(RecipeHandlerRenderer.DEFAULT_GUI_TEXTURE);
			RecipeHandlerRendererUtils.getInstance().drawTexturedRectangle(42, 19, 65, 30, 80, 26);
			RecipeHandlerRendererUtils.getInstance().drawRectangle(42, 13, 18, 10, 0xFFC6C6C6);
			RecipeHandlerRendererUtils.getInstance().drawRectangle(42, 41, 18, 4, 0xFFC6C6C6);
		}

		@Override
		public void renderForeground(CauldronRecipeHandler handler, ShapelessRecipe recipe, int cycleticks) {}

	}

}
