package craftedMods.neiLotr;

import codechicken.nei.ItemList;
import codechicken.nei.RestartableTask;
import codechicken.nei.api.API;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.relauncher.ReflectionHelper;
import craftedMods.neiLotr.events.FMLEventHandler;
import craftedMods.neiLotr.handlers.craftingTable.BasicCTShapedRecipeHandler;
import craftedMods.neiLotr.handlers.template.StaticRecipeLoader;
import craftedMods.neiLotr.version.VersionChecker;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.util.StatCollector;

@Mod(modid = NeiLotr.MODID, name = NeiLotr.MODNAME, version = NeiLotr.VERSION, acceptableRemoteVersions = "*", guiFactory = "craftedMods.neiLotr.gui.GuiFactory")
public class NeiLotr {

	@Instance(NeiLotr.MODID)
	public static NeiLotr mod = new NeiLotr();

	private Config config;
	private VersionChecker versionChecker;

	public static final String MODID = "neilotr";
	public static final String MODNAME = "NEI Addon for the \"The Lord of the Rings\" Minecraft Mod";
	public static final String VERSION = "0.4.6";

	private FMLEventHandler handler;

	private org.apache.logging.log4j.Logger logger;

	private StaticRecipeLoader recipeLoader = new StaticRecipeLoader();

	private NeiLotrNEIConfig neiConfig = new NeiLotrNEIConfig(recipeLoader);

	public Config getConfig() {
		return config;
	}

	public org.apache.logging.log4j.Logger getLogger() {
		return logger;
	}

	public FMLEventHandler getFMLEventHandler() {
		return handler;
	}

	public VersionChecker getVersionChecker() {
		return versionChecker;
	}

	public StaticRecipeLoader getStaticRecipeLoader() {
		return recipeLoader;
	}

	public NeiLotrNEIConfig getNeiConfig() {
		return neiConfig;
	}

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) {

		logger = event.getModLog();
		config = new Config(event.getSuggestedConfigurationFile());

		logger.info("NEI Integration enabled: " + !config.isDisabled());

		try {
			versionChecker = new VersionChecker();

			logger.debug("Version check enabled: " + config.isUseVersionChecker());

			if (config.isUseVersionChecker()) {
				logger.debug("Starting version-check...");
				versionChecker.checkVersion();
				logger.info(
						"Version-check was successful; new version avaible: " + versionChecker.isNewVersionAvaible());
			}

		} catch (Exception e) {
			logger.error("Version check failed, see stack trace:", e);
		}

		handler = new FMLEventHandler();
	}

	@EventHandler
	public void onInit(FMLInitializationEvent event) {
	}

	@EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {
		// Load NEI item list and wait until it's loaded
		ItemList.loadItems.restart();
		while (ReflectionHelper.getPrivateValue(RestartableTask.class, ItemList.loadItems, "thread") != null) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			} // Don't stress the CPU too much
		}
		neiConfig.init();
	}
}
