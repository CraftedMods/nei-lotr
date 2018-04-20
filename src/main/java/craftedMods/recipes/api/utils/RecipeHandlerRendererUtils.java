package craftedMods.recipes.api.utils;

import craftedMods.recipes.utils.RecipeHandlerRendererUtilsImpl;

public interface RecipeHandlerRendererUtils {

	public static final RecipeHandlerRendererUtils instance = new RecipeHandlerRendererUtilsImpl();

	public static RecipeHandlerRendererUtils getInstance() {
		return RecipeHandlerRendererUtils.instance;
	}

	public void bindTexture(String texture);

	public void drawRectangle(int x, int y, int width, int height, int color);

	public void drawGradientRectangle(int x, int y, int width, int height, int color1, int color2);

	public void drawTexturedRectangle(int x, int y, int textureX, int textureY, int width, int height);

	public void drawText(String text, int x, int y, int color, boolean shadow);

	public void drawText(String text, int x, int y, int color);

	public void drawTextCentered(String text, int x, int y, int color, boolean shadow);

	public void drawTextCentered(String text, int x, int y, int color);

	public int getStringWidth(String text);

	public void drawProgressBar(int x, int y, int textureX, int textureY, int width, int height, float completion, EnumProgressBarDirection direction);

	public enum EnumProgressBarDirection {
		INCREASE_RIGHT, INCREASE_DOWN, INCREASE_LEFT, INCREASE_UP, DECREASE_LEFT, DECREASE_UP, DECREASE_RIGHT, DECREASE_DOWN;
	}

}
