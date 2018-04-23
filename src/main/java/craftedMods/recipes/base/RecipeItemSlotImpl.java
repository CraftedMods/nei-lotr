package craftedMods.recipes.base;

import craftedMods.recipes.api.RecipeItemSlot;

public class RecipeItemSlotImpl implements RecipeItemSlot {

	private final int x;
	private final int y;

	public RecipeItemSlotImpl(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.y;
	}

}
