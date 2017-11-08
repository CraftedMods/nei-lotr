package craftedMods.neiLotr;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import scala.actors.threadpool.Arrays;

public class Config {
	private Configuration configFile;

	private boolean hideTechnicalBlocks = true;
	private boolean useVersionChecker = true;
	// private boolean useTraderSupport = true;
	// private boolean versionCheckerShowsLinks = true;
	private boolean disabled = false;

	public static final String[] CRAFTING_TABLE_NAMES = { "angmar", "blueDwarven", "dolAmroth", "dolGuldur",
			"dunlending", "dwarven", "elven", "gondorian", "gundabad", "halfTroll", "highElven", "moredain", "morgul",
			"nearHarad", "ranger", "rohirric", "tauredain", "uruk", "woodElven", "dale", "hobbit", "dorwinion", "rhun",
			"rivendell" };

	private String[] enabledShapedCraftingTables = CRAFTING_TABLE_NAMES;

	private String[] enabledShapelessCraftingTables = CRAFTING_TABLE_NAMES;

	public static final String CATEGORY_RECIPE_HANDLERS = "recipeHandlers";

	public Config(File configFile) {
		this.configFile = new Configuration(configFile);
		update();
	}

	public void update() {
		configFile.addCustomCategoryComment(CATEGORY_RECIPE_HANDLERS,
				"Allows you to configure each recipe handler added by the addon");

		hideTechnicalBlocks = configFile.getBoolean("Hide technical blocks", Configuration.CATEGORY_GENERAL, true,
				"Hides technical blocks of the LOTR Mod in NEI.");

		useVersionChecker = configFile.getBoolean("Use version checker", Configuration.CATEGORY_GENERAL, true,
				"Enables/disables the version checker of the plugin. If it is disabled, you won't be notified about new available versions.");

		// useTraderSupport = configFile.getBoolean("Use trader support",
		// Configuration.CATEGORY_GENERAL, true,
		// "Enables/disables the recipe handler providing trader support, which
		// shows you which trader sells, and which trader buys a selected item.
		// It works like a normal NEI recipe handler.");

		// versionCheckerShowsLinks = configFile.getBoolean("Use advanced
		// version checker", Configuration.CATEGORY_GENERAL,
		// true,
		// "If enabled, the version checker provides clickable links (if the
		// version checker is enabled) to the changelog and to the new version
		// of NEI LOTR if it had found one. ");

		disabled = configFile.getBoolean("Disable NEI integration", Configuration.CATEGORY_GENERAL, false,
				"If you want to disable NEI integration without removing the addon from your mods folder, you can do it by setting this property to true. This does not disable the version checker.");

		enabledShapedCraftingTables = configFile.getStringList("Enabled shaped recipe handlers for crafting tables",
				Config.CATEGORY_RECIPE_HANDLERS, CRAFTING_TABLE_NAMES,
				"Remove an shaped recipe handler for a middle-earth crafting table from the list to disable it",
				CRAFTING_TABLE_NAMES);
		
		enabledShapelessCraftingTables = configFile.getStringList("Enabled shapeless recipe handlers for crafting tables",
				Config.CATEGORY_RECIPE_HANDLERS, CRAFTING_TABLE_NAMES,
				"Remove an shapeless recipe handler for a middle-earth crafting table from the list to disable it",
				CRAFTING_TABLE_NAMES);

		if (configFile.hasChanged()) {
			configFile.save();
		}
	}

	public Configuration getConfigFile() {
		return configFile;
	}

	public boolean isHideTechnicalBlocks() {
		return hideTechnicalBlocks;
	}

	public boolean isUseVersionChecker() {
		return useVersionChecker;
	}
	
	public boolean isShapedMECTRecipeHandlerEnabled(String name){
		return Arrays.asList(enabledShapedCraftingTables).contains(name);
	}
	
	public boolean isShapelessMECTRecipeHandlerEnabled(String name){
		return Arrays.asList(enabledShapelessCraftingTables).contains(name);
	}

	// public boolean isUseTraderSupport() {
	// return useTraderSupport;
	// }
	//
	// public void setUseTraderSupport(boolean useTraderSupport) {
	// this.useTraderSupport = useTraderSupport;
	// this.update();
	// }

	// public boolean isVersionCheckerShowsLinks() {
	// return versionCheckerShowsLinks;
	// }
	//
	// public void setVersionCheckerShowsLinks(boolean versionCheckerShowsLinks)
	// {
	// this.versionCheckerShowsLinks = versionCheckerShowsLinks;
	// update();
	// }

	public boolean isDisabled() {
		return disabled;
	}
}
