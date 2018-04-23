package craftedMods.lotr.nei.handlers;

import java.util.*;

import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.*;
import craftedMods.recipes.base.*;
import lotr.common.LOTRMod;
import lotr.common.item.LOTRItemHobbitPipe;
import lotr.common.recipe.LOTRRecipePoisonWeapon;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

@RegisteredHandler(isEnabled = false)
public class CauldronRecipeHandler extends AbstractRecipeHandler<ShapelessRecipe> {

	private final CauldronRecipeHandlerRenderer renderer = new CauldronRecipeHandlerRenderer();
	private final List<ItemStack> dyedHobbitPipes = new ArrayList<>();

	public CauldronRecipeHandler() {
		super("cauldron");
	}

	@Override
	public String getDisplayName() {
		return Blocks.cauldron.getLocalizedName();
	}

	@Override
	public Collection<ShapelessRecipe> loadSimpleStaticRecipes() {
		for (int i = 1; i < 17; i++) {
			ItemStack pipe = new ItemStack(LOTRMod.hobbitPipe);
			LOTRItemHobbitPipe.setSmokeColor(pipe, i);
			this.dyedHobbitPipes.add(pipe);
		}
		return null;
	}

	@Override
	public Collection<ShapelessRecipe> getDynamicCraftingRecipes(ItemStack result) {
		Collection<ShapelessRecipe> recipes = new ArrayList<>();
		if (result.getItem() == LOTRMod.hobbitPipe && LOTRItemHobbitPipe.getSmokeColor(result) == 0)
			recipes.add(new HobbitPipeUndyeningRecipe(this.dyedHobbitPipes, new ItemStack(LOTRMod.hobbitPipe)));
		else if (LOTRRecipePoisonWeapon.inputToPoisoned.containsKey(result.getItem())) {
			ItemStack input = new ItemStack(LOTRRecipePoisonWeapon.inputToPoisoned.get(result.getItem()));
			if (result.isItemStackDamageable()) input.setItemDamage(result.getItemDamage());
			recipes.add(new ShapelessRecipe(input, result));
		}
		return recipes;
	}

	@Override
	public Collection<ShapelessRecipe> getDynamicUsageRecipes(ItemStack ingredient) {
		Collection<ShapelessRecipe> recipes = new ArrayList<>();
		if (ingredient.getItem() == LOTRMod.hobbitPipe && LOTRItemHobbitPipe.getSmokeColor(ingredient) != 0)
			recipes.add(new HobbitPipeUndyeningRecipe(ingredient, new ItemStack(LOTRMod.hobbitPipe)));
		else if (LOTRRecipePoisonWeapon.poisonedToInput.containsKey(ingredient.getItem())) {
			ItemStack result = new ItemStack(LOTRRecipePoisonWeapon.poisonedToInput.get(ingredient.getItem()));
			if (ingredient.isItemStackDamageable()) result.setItemDamage(ingredient.getItemDamage());
			recipes.add(new ShapelessRecipe(ingredient, result));
		}
		return recipes;
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

	public class HobbitPipeUndyeningRecipe extends ShapelessRecipe {

		public HobbitPipeUndyeningRecipe(Collection<ItemStack> ingredients, ItemStack result) {
			super(Arrays.asList(), result);
			this.ingredients.add(this.createItemStackSet(ingredients));
		}

		public HobbitPipeUndyeningRecipe(ItemStack ingredient, ItemStack result) {
			super(ingredient, result);
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
