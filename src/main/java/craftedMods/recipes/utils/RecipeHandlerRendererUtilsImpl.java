package craftedMods.recipes.utils;

import codechicken.lib.gui.GuiDraw;
import craftedMods.recipes.api.utils.RecipeHandlerRendererUtils;

public class RecipeHandlerRendererUtilsImpl implements RecipeHandlerRendererUtils {

	@Override
	public void bindTexture(String texture) {
		GuiDraw.changeTexture(texture);
	}

	@Override
	public void drawRectangle(int x, int y, int width, int height, int color) {
		GuiDraw.drawRect(x, y, width, height, color);
	}

	@Override
	public void drawGradientRectangle(int x, int y, int width, int height, int color1, int color2) {
		GuiDraw.drawGradientRect(x, y, width, height, color1, color2);
	}

	@Override
	public void drawTexturedRectangle(int x, int y, int textureX, int textureY, int width, int height) {
		GuiDraw.drawTexturedModalRect(x, y, textureX, textureY, width, height);
	}

	@Override
	public void drawText(String text, int x, int y, int color, boolean shadow) {
		GuiDraw.drawString(text, x, y, color, shadow);
	}

	@Override
	public void drawText(String text, int x, int y, int color) {
		GuiDraw.drawString(text, x, y, color);
	}

	@Override
	public void drawTextCentered(String text, int x, int y, int color, boolean shadow) {
		GuiDraw.drawStringC(text, x, y, color, shadow);
	}

	@Override
	public void drawTextCentered(String text, int x, int y, int color) {
		GuiDraw.drawStringC(text, x, y, color);
	}

	@Override
	public int getStringWidth(String text) {
		return GuiDraw.getStringWidth(text);
	}

	@Override
	public void drawProgressBar(int x, int y, int textureX, int textureY, int width, int height, float completion, EnumProgressBarDirection direction) {
		this.drawProgressBar(x, y, textureX, textureY, width, height, completion, direction.ordinal());
	}

	public void drawProgressBar(int x, int y, int textureX, int textureY, int width, int height, float completion, int direction) {
		if (direction > 3) {
			completion = 1.0f - completion;
			direction %= 4;
		}
		int var = (int) (completion * (direction % 2 == 0 ? width : height));

		switch (direction) {
			case 0:
				this.drawTexturedRectangle(x, y, textureX, textureY, var, height);
				break;
			case 1:
				this.drawTexturedRectangle(x, y, textureX, textureY, width, var);
				break;
			case 2:
				this.drawTexturedRectangle(x + width - var, y, textureX + width - var, textureY, var, height);
				break;
			case 3:
				this.drawTexturedRectangle(x, y + height - var, textureX, textureY + height - var, width, var);
		}

	}

}
