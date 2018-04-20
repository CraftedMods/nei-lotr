package craftedMods.recipes.api;

public interface RecipeHandlerRenderer<T extends RecipeHandler<U>, U extends Recipe> {

	public static final String DEFAULT_GUI_TEXTURE = "textures/gui/container/crafting_table.png";

	public void renderBackground(T handler, U recipe, int cycleticks);

	public void renderForeground(T handler, U recipe, int cycleticks);

}
