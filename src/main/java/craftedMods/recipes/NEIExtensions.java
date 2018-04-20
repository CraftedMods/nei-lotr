package craftedMods.recipes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import codechicken.nei.ItemList;
import codechicken.nei.RestartableTask;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import craftedMods.recipes.provider.NEIExtensionsConfiguration;
import craftedMods.recipes.provider.NEIIntegrationManager;
import craftedMods.recipes.utils.MCVersionChecker;
import craftedMods.recipes.utils.NEIExtensionsUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

@Mod(modid = NEIExtensions.MODID, name = NEIExtensions.MODNAME, version = NEIExtensions.VERSION, acceptableRemoteVersions = "*", guiFactory = "craftedMods.recipes.ConfigurationGuiFactory")
public class NEIExtensions {

	@Instance(NEIExtensions.MODID)
	public static NEIExtensions mod = new NEIExtensions();

	private NEIExtensionsConfiguration config;
	private MCVersionChecker versionChecker;

	public static final String MODID = "neilotr";
	public static final String MODNAME = "NEI Addon for the \"The Lord of the Rings\" Minecraft Mod";
	public static final String VERSION = "0.5.3";

	public static final String DEFAULT_VERSION_FILE_URL = "https://dl.dropboxusercontent.com/s/gyz1oq7vyz753y5/version.txt";

	public static final String ADDON_DIR_NAME = "neiLotr";
	public static final String ITEM_CACHE_FILE_NAME = "itemCache.dat";
	public static final String RECIPE_CACHE_FILE_NAME = "recipeCache.dat";

	private org.apache.logging.log4j.Logger logger;

	private NEIIntegrationManager neiConfig;

	private File addonDir;
	private File itemCache;
	private File recipeCache;

	private boolean worldLoaded = false;

	public NEIExtensionsConfiguration getConfig() {
		return this.config;
	}

	public org.apache.logging.log4j.Logger getLogger() {
		return this.logger;
	}

	public MCVersionChecker getVersionChecker() {
		return this.versionChecker;
	}

	public NEIIntegrationManager getNeiConfig() {
		return this.neiConfig;
	}

	public File getRecipeCache() {
		return this.recipeCache;
	}

	@EventHandler
	public void onPreInit(FMLPreInitializationEvent event) throws IOException {
		this.logger = event.getModLog();
		this.config = new NEIExtensionsConfiguration(event.getSuggestedConfigurationFile());

		this.logger.info("NEI Integration enabled: " + !this.config.isDisabled());

		try {
			this.addonDir = new File(
					Minecraft.getMinecraft().mcDataDir.getPath() + File.separator + NEIExtensions.ADDON_DIR_NAME);

			if (!this.addonDir.exists()) {
				this.addonDir.mkdir();
				this.logger.debug("Successfully created the data directory of the addon");
			}
		} catch (Exception e) {
			this.logger.error("Couldn't create the data directory: ");
			throw e;
		}

		try {
			this.itemCache = new File(this.addonDir.getPath() + File.separator + NEIExtensions.ITEM_CACHE_FILE_NAME);

			if (!this.itemCache.exists()) {
				this.itemCache.createNewFile();
				this.logger.debug("Successfully created the item cache file");
			}
		} catch (Exception e) {
			this.logger.error("Couldn't create the item cache file: ");
			throw e;
		}

		try {
			this.recipeCache = new File(
					this.addonDir.getPath() + File.separator + NEIExtensions.RECIPE_CACHE_FILE_NAME);

			if (!this.recipeCache.exists()) {
				this.recipeCache.createNewFile();
				this.logger.debug("Successfully created the recipe cache file");
			}
		} catch (Exception e) {
			this.logger.error("Couldn't create the recipe cache file: ");
			throw e;
		}

		try {
			this.versionChecker = new MCVersionChecker(NEIExtensions.DEFAULT_VERSION_FILE_URL);

			this.logger.debug("Version check enabled: " + this.config.isUseVersionChecker());

			if (this.config.isUseVersionChecker()) {
				this.logger.debug("Starting version check...");
				this.versionChecker.checkVersion();
				this.logger.info("Version check was successful; new version available: "
						+ this.versionChecker.isNewVersionAvaible());
			}

		} catch (Exception e) {
			this.logger.error("Version check failed: ", e);
		}

		if (!this.config.isDisabled()) {
			this.neiConfig = new NEIIntegrationManager(this.config, this.logger);
			this.neiConfig.preInit();
		}

		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	@EventHandler
	public void onInit(FMLInitializationEvent event) {
	}

	@EventHandler
	public void onPostInit(FMLPostInitializationEvent event) {
		if (!this.config.isDisabled()) {
			try {
				// Load the NEI item list and wait until it's loaded
				ItemList.loadItems.restart();
				Thread thread = ReflectionHelper.getPrivateValue(RestartableTask.class, ItemList.loadItems, "thread");
				if (thread != null) {
					thread.join();
				}
				this.neiConfig.init(this.checkItemCache());
			} catch (Exception e) {
				this.logger.fatal("Couldn't load the NEI item list - the addon cannot be started", e);
			}
		}
	}

	private boolean checkItemCache() {
		boolean canCacheBeUsed = false;
		this.logger.info("Recipe caching enabled: " + this.config.isComplicatedStaticRecipeLoadingCacheEnabled());
		if (this.config.isComplicatedStaticRecipeLoadingCacheEnabled()) {
			NBTTagCompound currentItemListTag = new NBTTagCompound();
			NEIExtensionsUtils.writeItemStackListToNBT(currentItemListTag, "items", ItemList.items);
			boolean recreateItemList = true;
			try (FileInputStream in = new FileInputStream(this.itemCache)) {
				NBTTagCompound savedItemListTag = CompressedStreamTools.readCompressed(in);
				recreateItemList = !savedItemListTag.equals(currentItemListTag);
			} catch (Exception e) {
				this.logger.warn("The item cache couldn't be loaded: ", e);
			}
			canCacheBeUsed = !recreateItemList;
			if (recreateItemList) {
				this.logger.info("The item cache has to be (re)created");
			}
			if (recreateItemList) {
				try (FileOutputStream out = new FileOutputStream(this.itemCache)) {
					CompressedStreamTools.writeCompressed(currentItemListTag, out);
					this.logger.info("Wrote the item cache to the filesystem");
				} catch (Exception e) {
					this.logger.error("Couldn't write the item cache to the filesystem: ", e);
				}
			}
		}
		return canCacheBeUsed;
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(NEIExtensions.MODID)) {
			this.config.update();
		}
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		this.worldLoaded = true;
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (this.worldLoaded && Minecraft.getMinecraft().theWorld != null
				&& Minecraft.getMinecraft().thePlayer != null) {
			if (this.config.isUseVersionChecker() && this.versionChecker.isNewVersionAvaible()) {
				if (this.versionChecker.getNewestVersion() != null) {
					Minecraft.getMinecraft().thePlayer
							.addChatComponentMessage(this.versionChecker.getNewestVersion().getFormattedChatText());
				}
			}
			this.worldLoaded = false;
		}
	}

}
