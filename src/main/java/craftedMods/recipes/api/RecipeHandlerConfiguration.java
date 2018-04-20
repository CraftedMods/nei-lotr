package craftedMods.recipes.api;

import java.util.regex.Pattern;

public interface RecipeHandlerConfiguration {

	public void reload();

	public boolean isEnabled();

	public default String getString(String name, String defaultValue, String comment) {
		return this.getString(name, defaultValue, comment, name, null);
	}

	public default String getString(String name, String defaultValue, String comment, String langKey) {
		return this.getString(name, defaultValue, comment, langKey, null);
	}

	public default String getString(String name, String defaultValue, String comment, Pattern pattern) {
		return this.getString(name, defaultValue, comment, name, pattern);
	}

	public String getString(String name, String defaultValue, String comment, String langKey, Pattern pattern);

	public default String getString(String name, String defaultValue, String comment, String[] validValues) {
		return this.getString(name, defaultValue, comment, validValues, name);
	}

	public String getString(String name, String defaultValue, String comment, String[] validValues, String langKey);

	public default String[] getStringList(String name, String[] defaultValues, String comment) {
		return this.getStringList(name, defaultValues, comment, (String[]) null, name);
	}

	public default String[] getStringList(String name, String[] defaultValue, String comment, String[] validValues) {
		return this.getStringList(name, defaultValue, comment, validValues, name);
	}

	public String[] getStringList(String name, String[] defaultValue, String comment, String[] validValues, String langKey);

	public default boolean getBoolean(String name, boolean defaultValue, String comment) {
		return this.getBoolean(name, defaultValue, comment, name);
	}

	public boolean getBoolean(String name, boolean defaultValue, String comment, String langKey);

	public default int getInt(String name, int defaultValue, int minValue, int maxValue, String comment) {
		return this.getInt(name, defaultValue, minValue, maxValue, comment, name);
	}

	public int getInt(String name, int defaultValue, int minValue, int maxValue, String comment, String langKey);

	public default float getFloat(String name, float defaultValue, float minValue, float maxValue, String comment) {
		return this.getFloat(name, defaultValue, minValue, maxValue, comment, name);
	}

	public float getFloat(String name, float defaultValue, float minValue, float maxValue, String comment, String langKey);

}
