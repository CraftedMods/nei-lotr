package craftedMods.recipes.provider;

import java.util.regex.Pattern;

import craftedMods.recipes.api.RecipeHandlerConfiguration;
import net.minecraftforge.common.config.Configuration;

public class RecipeHandlerConfigurationImpl implements RecipeHandlerConfiguration {

	private Configuration config;
	private final String unlocalizedHandlerName;
	private final String defaultCategory;

	public static final String RECIPEHANDLER_CATEGORY = "recipeHandlers";

	public RecipeHandlerConfigurationImpl(Configuration config, String unlocalizedHandlerName) {
		this.config = config;
		this.unlocalizedHandlerName = unlocalizedHandlerName;
		this.defaultCategory = RecipeHandlerConfigurationImpl.RECIPEHANDLER_CATEGORY + "." + this.unlocalizedHandlerName;
	}

	@Override
	public void reload() {
		this.config.load();
	}

	@Override
	public boolean isEnabled() {
		return this.getBoolean("Is Enabled", true, "If false, the recipe handler won't be loaded");
	}

	@Override
	public String getString(String name, String defaultValue, String comment, String langKey, Pattern pattern) {
		return this.config.getString(name, this.defaultCategory, defaultValue, comment, langKey, pattern);
	}

	@Override
	public String getString(String name, String defaultValue, String comment, String[] validValues, String langKey) {
		return this.config.getString(name, this.defaultCategory, defaultValue, comment, validValues, langKey);
	}

	@Override
	public String[] getStringList(String name, String[] defaultValue, String comment, String[] validValues, String langKey) {
		return this.config.getStringList(name, this.defaultCategory, defaultValue, comment, validValues, langKey);
	}

	@Override
	public boolean getBoolean(String name, boolean defaultValue, String comment, String langKey) {
		return this.config.getBoolean(name, this.defaultCategory, defaultValue, comment, langKey);
	}

	@Override
	public int getInt(String name, int defaultValue, int minValue, int maxValue, String comment, String langKey) {
		return this.config.getInt(name, this.defaultCategory, defaultValue, minValue, maxValue, comment, langKey);
	}

	@Override
	public float getFloat(String name, float defaultValue, float minValue, float maxValue, String comment, String langKey) {
		return this.config.getFloat(name, this.defaultCategory, defaultValue, minValue, maxValue, comment, langKey);
	}

}
