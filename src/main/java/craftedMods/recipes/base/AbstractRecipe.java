package craftedMods.recipes.base;

import java.util.*;

import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.*;
import net.minecraft.item.ItemStack;

public abstract class AbstractRecipe implements Recipe {

	protected final List<ItemStackSet> ingredients = new ArrayList<>();
	protected final List<ItemStackSet> results = new ArrayList<>();
	protected final List<ItemStackSet> others = new ArrayList<>();

	private boolean werePermutationsGenerated = false;

	protected AbstractRecipe(Collection<ItemStack> ingredients, Collection<ItemStack> results, Collection<ItemStack> otherStacks) {
		this.addAll(ingredients, this.ingredients);
		this.addAll(results, this.results);
		this.addAll(otherStacks, this.others);
		this.generatePermutations();
	}

	protected AbstractRecipe() {}

	public boolean werePermutationsGenerated() {
		return this.werePermutationsGenerated;
	}

	@Override
	public List<ItemStackSet> getRecipeItems(EnumRecipeItemRole role) {
		switch (role) {
			case INGREDIENT:
				return this.ingredients;
			case OTHER:
				return this.others;
			case RESULT:
				return this.results;
			default:
				throw new IllegalArgumentException("Cannot handle the recipe item role \"" + role + "\"");
		}
	}

	protected void addAll(Collection<ItemStack> src, List<ItemStackSet> dest) {
		if (src != null) for (ItemStack stack : src)
			this.add(stack, dest);
	}

	protected void addAll(ItemStack[] src, List<ItemStackSet> dest) {
		if (src != null) for (ItemStack stack : src)
			this.add(stack, dest);
	}

	protected void add(ItemStack stack, List<ItemStackSet> dest) {
		dest.add(this.createItemStackSet(stack));
	}

	protected ItemStackSet createItemStackSet(ItemStack... stacks) {
		return ItemStackSet.create(stacks);
	}

	@Override
	public boolean produces(ItemStack result) {
		for (ItemStackSet permutations : this.results)
			if (permutations != null) for (ItemStack permutation : permutations)
				if (RecipeHandlerUtils.getInstance().areStacksSameTypeForCrafting(permutation, result)) return true;
		return false;
	}

	@Override
	public boolean consumes(ItemStack ingredient) {
		for (ItemStackSet permutations : this.ingredients)
			if (permutations != null) for (ItemStack permutation : permutations)
				if (RecipeHandlerUtils.getInstance().areStacksSameTypeForCrafting(permutation, ingredient)) return true;
		return false;
	}

	@Override
	public ItemStack getIngredientReplacement(ItemStack defaultReplacement) {
		return defaultReplacement;
	}

	@Override
	public ItemStack getResultReplacement(ItemStack defaultReplacement) {
		return null;
	}

	protected boolean generatePermutations() {
		if (!this.werePermutationsGenerated) {
			this.generatePermutationsForStacks(this.ingredients);
			this.generatePermutationsForStacks(this.results);
			this.generatePermutationsForStacks(this.others);
			return this.werePermutationsGenerated = true;
		}
		return false;
	}

	protected void generatePermutationsForStacks(List<ItemStackSet> stacks) {
		ListIterator<ItemStackSet> permutationsIterator = stacks.listIterator();
		while (permutationsIterator.hasNext()) {
			ItemStackSet permutations = permutationsIterator.next();
			if (permutations != null) permutationsIterator.set(RecipeHandlerUtils.getInstance().generatePermutations(permutations));
		}
	}

}
