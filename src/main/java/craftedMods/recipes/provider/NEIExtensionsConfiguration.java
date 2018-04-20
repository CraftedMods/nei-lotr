package craftedMods.recipes.provider;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class NEIExtensionsConfiguration {
	private Configuration configFile;

	private boolean hideTechnicalBlocks = true;
	private boolean useVersionChecker = true;
	private boolean brewingRecipeHandlerDisabled = false;
	private boolean complicatedStaticRecipeLoadingCacheEnabled = true;
	private int classDiscovererThreadTimeout = 10000;
	private boolean disabled = false;

	public NEIExtensionsConfiguration(File configFile) {
		this.configFile = new Configuration(configFile);
		this.update();
	}

	public void update() {
		this.hideTechnicalBlocks = this.configFile.getBoolean("Hide technical blocks", Configuration.CATEGORY_GENERAL, hideTechnicalBlocks,
				"Hides technical blocks of the LOTR Mod in NEI.");
		this.useVersionChecker = this.configFile.getBoolean("Use version checker", Configuration.CATEGORY_GENERAL, useVersionChecker,
				"Enables/disables the version checker of the plugin. If it is disabled, you won't be notified about new available versions.");

		this.brewingRecipeHandlerDisabled = this.configFile.getBoolean("Disable vanilla brewing recipe handler", Configuration.CATEGORY_GENERAL,
				brewingRecipeHandlerDisabled,
				"If set to true, the vanilla brewing recipe handler will be disabled. Set this to true if you don't use the vanilla brewing system.");
		this.complicatedStaticRecipeLoadingCacheEnabled = this.configFile.getBoolean("Enable recipe caching", Configuration.CATEGORY_GENERAL,
				complicatedStaticRecipeLoadingCacheEnabled,
				"If set to true, some recipes that are expensive to compute will be saved on the filesystem, so they don't have to be computed every time the addon starts. The cached recipes will only be used if the set of registered items doesn't change, so if you add/remove mods that add items, the recipes have to be computed again. If you experience crashes on startup or the recipe handlers behave weirdly, try to disable this feature.");
		this.classDiscovererThreadTimeout = this.configFile.getInt("Class discoverer thread timeout", Configuration.CATEGORY_GENERAL,
				classDiscovererThreadTimeout, 0, Integer.MAX_VALUE,
				"The maximum time the addon waits for the class discoverer thread to find the recipe handlers on the classpath");
		this.disabled = this.configFile.getBoolean("Disable NEI integration", Configuration.CATEGORY_GENERAL, disabled,
				"If you want to disable NEI integration without removing the addon from your mods folder, you can do it by setting this property to true. This does not disable the version checker.");

		if (this.configFile.hasChanged()) {
			this.configFile.save();
		}
	}

	public Configuration getConfigFile() {
		return this.configFile;
	}

	public boolean isHideTechnicalBlocks() {
		return this.hideTechnicalBlocks;
	}

	public boolean isUseVersionChecker() {
		return this.useVersionChecker;
	}

	public boolean isBrewingRecipeHandlerDisabled() {
		return brewingRecipeHandlerDisabled;
	}

	public boolean isComplicatedStaticRecipeLoadingCacheEnabled() {
		return complicatedStaticRecipeLoadingCacheEnabled;
	}

	public int getClassDiscovererThreadTimeout() {
		return classDiscovererThreadTimeout;
	}
	
	public boolean isDisabled() {
		return this.disabled;
	}
	
	
}
