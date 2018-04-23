package craftedMods.recipes.provider;

import java.lang.annotation.Annotation;
import java.util.*;

import org.apache.logging.log4j.Logger;

import codechicken.nei.api.API;
import codechicken.nei.recipe.*;
import craftedMods.recipes.NEIExtensions;
import craftedMods.recipes.api.*;
import craftedMods.utils.ClassDiscoverer;
import lotr.common.LOTRMod;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class NEIIntegrationManager {

	private final NEIExtensionsConfiguration config;
	private final Logger logger;
	private final ClassDiscoverer discoverer;

	private RecipeHandlerManager recipeHandlerManager;

	private Collection<ItemHidingHandler> itemHidingHandlers = new ArrayList<>();

	public NEIIntegrationManager(NEIExtensionsConfiguration config, Logger logger) {
		this.config = config;
		this.logger = logger;
		this.discoverer = new ClassDiscoverer(logger);
	}

	public void preInit() {
		this.discoverer.registerClassToDiscover(RegisteredHandler.class, RecipeHandler.class);
		this.discoverer.registerClassToDiscover(RegisteredHandler.class, RecipeHandlerFactory.class);
		this.discoverer.registerClassToDiscover(RegisteredHandler.class, ItemHidingHandler.class);
		this.discoverer.discoverClassesAsync();
	}

	public void init(boolean useCachedRecipes) {
		try {
			long start = System.currentTimeMillis();

			Map<Class<? extends Annotation>, Map<Class<?>, Set<Class<?>>>> discoveredClasses = this.discoverer
					.getDiscoveredClasses(this.config.getClassDiscovererThreadTimeout());

			this.recipeHandlerManager = new RecipeHandlerManager(this.config.getConfigFile(), discoveredClasses);

			this.recipeHandlerManager.init(useCachedRecipes);

			NEIExtensions.mod.getLogger().info("Enable item hiding handlers: " + this.config.isHideTechnicalBlocks());
			if (this.config.isHideTechnicalBlocks()) this.discoverItemHidingHandlers(discoveredClasses);

			this.logger.info("Initialized NEI config for LOTR Mod within " + (System.currentTimeMillis() - start) + " ms");
		} catch (Exception e) {
			this.logger.error("Couldn't initialize NEI config for LOTR Mod: ", e);
		}
	}

	@SuppressWarnings("unchecked")
	private void discoverItemHidingHandlers(Map<Class<? extends Annotation>, Map<Class<?>, Set<Class<?>>>> discoveredClasses) {
		Set<Class<?>> itemHidingHandlers = discoveredClasses.get(RegisteredHandler.class).get(ItemHidingHandler.class);
		NEIExtensions.mod.getLogger().info("Found " + itemHidingHandlers.size() + " item hiding handlers in classpath");
		itemHidingHandlers.forEach(clazz -> {
			try {
				Class<? extends ItemHidingHandler> handler = (Class<? extends ItemHidingHandler>) clazz;
				if (handler.getAnnotation(RegisteredHandler.class).isEnabled()) {
					this.itemHidingHandlers.add(handler.newInstance());
					NEIExtensions.mod.getLogger().debug("Successfully registered item hiding handler \"" + handler.getName() + "\"");
				} else NEIExtensions.mod.getLogger().info("The item hiding handler \"" + handler.getName() + "\" was disabled by the author.");
			} catch (Exception e) {
				NEIExtensions.mod.getLogger().error("Couldn't create an instance of class \"" + clazz.getName() + "\"", e);
			}
		});
	}

	public void load() {
		if (!this.config.isDisabled()) {

			long start = System.currentTimeMillis();

			// Remove recipe handlers
			if (this.config.isBrewingRecipeHandlerDisabled()) this.removeCraftingAndUsageHandler(BrewingRecipeHandler.class);

			this.removeCraftingAndUsageHandler(ShapedRecipeHandler.class);
			this.removeCraftingAndUsageHandler(ShapelessRecipeHandler.class);

			// Load registered handlers
			this.recipeHandlerManager.getRecipeHandlers().forEach((unlocalizedName, handler) -> this.loadHandler(new PluginRecipeHandler<>(handler)));

			// Item hiding
			if (this.config.isHideTechnicalBlocks()) this.registerHiddenItems();

			// Override names
			this.addOverrideNames();

			this.logger.info("Loaded NEI config for LOTR Mod within " + (System.currentTimeMillis() - start) + " ms");
		}
	}

	public void refreshCache() {
		this.recipeHandlerManager.refreshCache();
	}

	private void loadHandler(TemplateRecipeHandler handler) {
		GuiCraftingRecipe.craftinghandlers.add(handler);
		GuiUsageRecipe.usagehandlers.add(handler);
	}

	private <T extends ICraftingHandler & IUsageHandler> void removeCraftingAndUsageHandler(Class<T> handlerClass) {
		this.removeCraftingHandler(handlerClass);
		this.removeUsageHandler(handlerClass);
	}

	private void removeCraftingHandler(Class<? extends ICraftingHandler> handlerClass) {
		this.removeHandler(handlerClass, GuiCraftingRecipe.craftinghandlers);
	}

	private void removeUsageHandler(Class<? extends IUsageHandler> handlerClass) {
		this.removeHandler(handlerClass, GuiUsageRecipe.usagehandlers);
	}

	private <T> void removeHandler(Class<? extends T> handlerClass, List<T> handlerList) {
		for (int i = 0; i < handlerList.size(); i++) {
			T handler = handlerList.get(i);
			if (handler.getClass() == handlerClass) handlerList.remove(i);
		}
	}

	private void registerHiddenItems() {
		for (ItemHidingHandler handler : this.itemHidingHandlers) {
			Collection<ItemStack> hiddenStacks = handler.getHiddenStacks();
			if (hiddenStacks != null) {
				hiddenStacks.forEach(API::hideItem);
				NEIExtensions.mod.getLogger().debug(
						"The item hiding handler \"" + handler.getClass() + "\" has hidden " + (hiddenStacks == null ? 0 : hiddenStacks.size()) + " items");
			}
		}
	}

	private void addOverrideNames() {
		// Portals
		this.setOverrideName(LOTRMod.elvenPortal, StatCollector.translateToLocal("neiLotr.lotr.block.elvenPortal.name"));
		this.setOverrideName(LOTRMod.morgulPortal, StatCollector.translateToLocal("neiLotr.lotr.block.morgulPortal.name"));
		this.setOverrideName(LOTRMod.utumnoPortal, StatCollector.translateToLocal("neiLotr.lotr.block.utumnoPortal.name"));
		this.setOverrideName(LOTRMod.utumnoReturnPortal, StatCollector.translateToLocal("neiLotr.lotr.block.utumnoReturnPortal.name"));
		this.setOverrideName(LOTRMod.utumnoReturnPortalBase, StatCollector.translateToLocal("neiLotr.lotr.block.utumnoReturnPortalBase.name"));

		// Others
		this.setOverrideName(LOTRMod.rhunFire, StatCollector.translateToLocal("neiLotr.lotr.block.rhunFire.name"));
	}

	private void setOverrideName(ItemStack stack, String name) {
		API.setOverrideName(stack, name);
	}

	// private void setOverrideName(Item item, String name) {
	// this.setOverrideName(new ItemStack(item), name);
	// }

	private void setOverrideName(Block block, String name) {
		this.setOverrideName(new ItemStack(block), name);
	}

}
