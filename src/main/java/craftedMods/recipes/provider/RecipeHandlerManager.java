package craftedMods.recipes.provider;

import java.io.*;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;

import codechicken.nei.ItemList;
import craftedMods.recipes.NEIExtensions;
import craftedMods.recipes.api.*;
import craftedMods.recipes.utils.NEIExtensionsUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraftforge.common.config.Configuration;

public class RecipeHandlerManager {

	private Map<String, RecipeHandler<?>> recipeHandlers = new HashMap<>();

	private final Configuration config;
	private final Map<Class<? extends Annotation>, Map<Class<?>, Set<Class<?>>>> discoveredClasses;

	private final ExecutorService complicatedStaticRecipeLoadingThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	private long complicatedStaticRecipeIterations = 0;
	private final AtomicLong complicatedStaticRecipeIterationsCounter = new AtomicLong(1);

	private static final int COMPLICATED_STATIC_RECIPE_BATCH_SIZE = 300;

	public static final String NEI_LOTR_VERSION_TAG_KEY = "neiLotrVersion";
	public static final String RECIPE_HANDLER_HEADER_TAG_KEY = "header";
	public static final String RECIPE_HANDLER_CONTENT_TAG_KEY = "content";

	private RecipeHandlerResourcePack recipeHandlerResources;
	private Collection<RecipeHandlerFactory> recipeHandlerFactories = new ArrayList<>();

	public RecipeHandlerManager(Configuration config, Map<Class<? extends Annotation>, Map<Class<?>, Set<Class<?>>>> discoveredClasses) {
		this.config = config;
		this.discoveredClasses = discoveredClasses;
	}

	public void init(boolean useCache) {
		this.discoverRecipeHandlersInClasspath();
		this.loadRecipeHandlers();
		Map<RecipeHandler<?>, Collection<Recipe>> recipes = new HashMap<>(this.recipeHandlers.size());
		if (useCache) this.loadRecipesFromCache(recipes);
		this.loadStaticRecipes(recipes);
		this.writeRecipesToCache();
	}

	@SuppressWarnings("unchecked")
	private void discoverRecipeHandlersInClasspath() {
		Set<Class<?>> recipeHandlers = this.discoveredClasses.get(RegisteredHandler.class).get(RecipeHandler.class);
		NEIExtensions.mod.getLogger().info("Found " + recipeHandlers.size() + " recipe handlers in classpath");
		recipeHandlers.forEach(clazz -> {
			try {
				Class<? extends RecipeHandler<?>> handler = (Class<? extends RecipeHandler<?>>) clazz;
				if (handler.getAnnotation(RegisteredHandler.class).isEnabled()) this.registerRecipeHandler(handler.newInstance());
				else NEIExtensions.mod.getLogger().info("The recipe handler \"" + handler.getName() + "\" was disabled by the author.");
			} catch (Exception e) {
				NEIExtensions.mod.getLogger().error("Couldn't create an instance of class \"" + clazz.getName() + "\"", e);
			}
		});

		Set<Class<?>> recipeHandlerFactories = this.discoveredClasses.get(RegisteredHandler.class).get(RecipeHandlerFactory.class);
		NEIExtensions.mod.getLogger().info("Found " + recipeHandlerFactories.size() + " recipe handler factories in classpath");
		recipeHandlerFactories.forEach(clazz -> {
			try {
				Class<? extends RecipeHandlerFactory> factory = (Class<? extends RecipeHandlerFactory>) clazz;
				try {
					if (factory.getAnnotation(RegisteredHandler.class).isEnabled()) {
						RecipeHandlerFactory factoryInstance = factory.newInstance();
						for (RecipeHandler<?> handler : factoryInstance.getRecipeHandlers())
							this.registerRecipeHandler(handler);
						this.recipeHandlerFactories.add(factoryInstance);
					} else NEIExtensions.mod.getLogger().info("The recipe handler factory\"" + factory.getName() + "\" was disabled by the author.");
				} catch (Exception e) {
					NEIExtensions.mod.getLogger().error("Couldn't create the recipe handlers supplied by the factory \"" + factory.getName() + "\"", e);
				}
			} catch (Exception e) {
				NEIExtensions.mod.getLogger().error("Couldn't create an instance of class \"" + clazz.getName() + "\"", e);
			}
		});
	}

	private void registerRecipeHandler(RecipeHandler<?> instance) {
		if (this.recipeHandlers.putIfAbsent(instance.getUnlocalizedName(), instance) == null) NEIExtensions.mod.getLogger()
				.debug("Successfully registered recipe handler \"" + instance.getUnlocalizedName() + "\" of class \"" + instance.getClass().getName() + "\"");
		else NEIExtensions.mod.getLogger().warn("Couldn't register recipe handler \"" + instance.getClass().getName()
				+ "\". A recipe handler with the unlocalized name \"" + instance.getUnlocalizedName() + "\" is already registered!");
	}

	private void loadRecipeHandlers() {
		Iterator<RecipeHandler<?>> handlers = this.recipeHandlers.values().iterator();
		while (handlers.hasNext()) {
			RecipeHandler<?> handler = handlers.next();
			try {
				RecipeHandlerConfiguration config = new RecipeHandlerConfigurationImpl(this.config, handler.getUnlocalizedName());
				boolean categoryDisabled = false;
				StringBuilder parentCategory = new StringBuilder();
				for (String category : NEIExtensionsUtils.getRecipeHandlerCategories(handler.getUnlocalizedName())) {
					parentCategory.append(category);
					categoryDisabled = this.config.getBoolean("Disable all recipe handlers in this category",
							RecipeHandlerConfigurationImpl.RECIPEHANDLER_CATEGORY + "." + parentCategory.toString(), false,
							"If set to true, all recipe handlers in this category and all subcategories will be disabled");
					parentCategory.append(".");
				}
				if (config.isEnabled() && !categoryDisabled) handler.onPreLoad(config, LogManager.getLogger(handler.getUnlocalizedName()));
				else {
					handlers.remove();
					NEIExtensions.mod.getLogger()
							.info("The recipe handler \"" + handler.getDisplayName() + "\" was disabled by the user (via the config file).");
				}
			} catch (Exception e) {
				handlers.remove();
				NEIExtensions.mod.getLogger().error("Couldn't load recipe handler \"" + handler.getUnlocalizedName() + "\"", e);
			}
		}
		this.registerResources();
		this.config.save();
	}

	private void registerResources() {
		Collection<RecipeHandler<?>> handlersToRegister = new ArrayList<>();
		this.recipeHandlers.values().forEach(handler -> {
			if (handler.getResources() != null && !handler.getResources().isEmpty()) handlersToRegister.add(handler);
			NEIExtensions.mod.getLogger().debug(String.format("The recipe handler \"%s\" registered %d resources", handler.getUnlocalizedName(),
					handler.getResources() == null ? 0 : handler.getResources().size()));
		});
		this.recipeHandlerResources = new RecipeHandlerResourcePack(handlersToRegister);
		for (RecipeHandlerFactory factory : this.recipeHandlerFactories) {
			if (factory.getResources() != null && !factory.getResources().isEmpty()) this.recipeHandlerResources.addResources(factory.getResources());
			NEIExtensions.mod.getLogger().debug(String.format("The recipe handler \"%s\" registered %d resources", factory.getClass().getName(),
					factory.getResources() == null ? 0 : factory.getResources().size()));
		}
		NEIExtensionsUtils.registerDefaultResourcePack(this.recipeHandlerResources);
	}

	@SuppressWarnings("unchecked")
	private void loadRecipesFromCache(Map<RecipeHandler<?>, Collection<Recipe>> cachedRecipes) {
		if (NEIExtensions.mod.getConfig().isComplicatedStaticRecipeLoadingCacheEnabled())
			try (FileInputStream in = new FileInputStream(NEIExtensions.mod.getRecipeCache())) {
			NBTTagCompound cacheRootTag = CompressedStreamTools.readCompressed(in);
			if (cacheRootTag.getString(RecipeHandlerManager.NEI_LOTR_VERSION_TAG_KEY).equals(NEIExtensions.VERSION)) {
			for (String key : (Set<String>) cacheRootTag.func_150296_c())
			if (!key.equals(RecipeHandlerManager.NEI_LOTR_VERSION_TAG_KEY)) {
			NBTTagCompound handlerTag = cacheRootTag.getCompoundTag(key);
			boolean wasHandlerFound = false;
			for (RecipeHandler<?> handler : this.recipeHandlers.values())
			if (handler.getUnlocalizedName().equals(key)) {
			wasHandlerFound = true;
			RecipeHandlerCacheManager<?> cache = handler.getRecipeHandlerCacheManager();
			if (cache != null && cache.isCacheEnabled()) {
			NBTTagCompound headerTag = handlerTag.getCompoundTag(RecipeHandlerManager.RECIPE_HANDLER_HEADER_TAG_KEY);
			if (cache.isCacheValid(headerTag)) {
			Collection<? extends Recipe> readRecipes = cache.readRecipesFromCache(headerTag,
					handlerTag.getCompoundTag(RecipeHandlerManager.RECIPE_HANDLER_CONTENT_TAG_KEY));
			if (readRecipes != null && !readRecipes.isEmpty()) {
			if (!cachedRecipes.containsKey(handler)) cachedRecipes.put(handler, new ArrayList<>());
			cachedRecipes.get(handler).addAll(readRecipes);
			NEIExtensions.mod.getLogger()
					.info("The recipe handler \"" + handler.getUnlocalizedName() + "\" loaded " + readRecipes.size() + " recipes from the cache");
			} else NEIExtensions.mod.getLogger().info("The recipe handler \"" + handler.getUnlocalizedName() + "\" loaded no recipes from the cache");
			} else NEIExtensions.mod.getLogger().info("The cache of the recipe handler \"" + key + "\" is not valid, the discovered data won't be used");
			} else NEIExtensions.mod.getLogger().debug("The recipe handler \"" + key + "\" doesn't support caching (the discovered cache data won't be used)");
			}
			if (!wasHandlerFound)
				NEIExtensions.mod.getLogger().debug("A cache entry \"" + key + "\" was found with no corresponding recipe handler, it'll be ignored");
			}
			} else NEIExtensions.mod.getLogger()
					.info("The cache was created with another version of the addon than the current installed one - it will be recreated");
			} catch (Exception e) {
			NEIExtensions.mod.getLogger().error("Couldn't load the static recipes from the cache: ", e);
			}
	}

	@SuppressWarnings("unchecked")
	private void loadStaticRecipes(Map<RecipeHandler<?>, Collection<Recipe>> staticRecipes) {
		this.loadSimpleStaticRecipes(staticRecipes);
		int maxItemIterationDepth = 0;
		for (RecipeHandler<?> handler : this.recipeHandlers.values())
			maxItemIterationDepth = Math.max(maxItemIterationDepth, handler.getComplicatedStaticRecipeDepth());
		if (maxItemIterationDepth > 2) NEIExtensions.mod.getLogger().warn("The static recipe iteration depth (" + maxItemIterationDepth
				+ ") is very high (yes, three is high because itemCount^iterationDepth iterations are required) which can delay the startup of MC.");
		Map<RecipeHandler<?>, Collection<Recipe>> complicatedStaticRecipes = new HashMap<>();
		for (RecipeHandler<?> handler : this.recipeHandlers.values())
			if (handler.getComplicatedStaticRecipeDepth() > 0) complicatedStaticRecipes.put(handler, new ArrayList<>(150));
		this.loadComplicatedStaticRecipes(maxItemIterationDepth, complicatedStaticRecipes);
		try {
			this.complicatedStaticRecipeLoadingThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		complicatedStaticRecipes.forEach((handler, recipes) -> {
			if (!staticRecipes.containsKey(handler)) staticRecipes.put(handler, new ArrayList<>());
			if (recipes != null && !recipes.isEmpty()) staticRecipes.get(handler).addAll(recipes);
		});
		staticRecipes.forEach((handler, recipes) -> {
			this.postLoadHandler((RecipeHandler<Recipe>) handler, recipes);
		});

	}

	@SuppressWarnings("unchecked")
	private void loadSimpleStaticRecipes(Map<RecipeHandler<?>, Collection<Recipe>> staticRecipes) {
		for (RecipeHandler<?> handler : this.recipeHandlers.values())
			try {
				Collection<Recipe> recipes = (Collection<Recipe>) handler.loadSimpleStaticRecipes();
				NEIExtensions.mod.getLogger().debug("The recipe handler \"" + handler.getUnlocalizedName() + "\" loaded "
						+ (recipes != null ? recipes.size() : 0) + " simple static recipes");
				if (!staticRecipes.containsKey(handler)) staticRecipes.put(handler, new ArrayList<>(50));
				if (recipes != null && !recipes.isEmpty()) staticRecipes.get(handler).addAll(recipes);
			} catch (Exception e) {
				NEIExtensions.mod.getLogger().error("Couldn't load simple static recipes of recipe handler \"" + handler.getUnlocalizedName() + "\"", e);
			}
	}

	private void loadComplicatedStaticRecipes(int maxDepth, Map<RecipeHandler<? extends Recipe>, Collection<Recipe>> staticRecipes) {
		this.complicatedStaticRecipeIterations = (long) Math.pow(ItemList.items.size(), maxDepth);
		this.itemStackIteration(1, maxDepth, new ItemStack[maxDepth], staticRecipes);
	}

	private void itemStackIteration(int start, int end, ItemStack[] stackArray, Map<RecipeHandler<? extends Recipe>, Collection<Recipe>> staticRecipes) {
		List<Runnable> complicatedStaticRecipeLoadingTaskList = new ArrayList<>(RecipeHandlerManager.COMPLICATED_STATIC_RECIPE_BATCH_SIZE);
		for (ItemStack stack : ItemList.items) {
			stackArray[start - 1] = stack;
			staticRecipes.forEach((handler, recipes) -> {
				if (handler.getComplicatedStaticRecipeDepth() >= start) {
					Recipe recipe = handler.loadComplicatedStaticRecipe(stackArray);
					if (recipe != null) recipes.add(recipe);
				}
			});
			if (start != end) {
				ItemStack[] clone = Arrays.copyOf(stackArray, end);
				complicatedStaticRecipeLoadingTaskList.add(() -> {
					this.itemStackIteration(start + 1, end, clone, staticRecipes);
				});
			}
			if (complicatedStaticRecipeLoadingTaskList.size() > RecipeHandlerManager.COMPLICATED_STATIC_RECIPE_BATCH_SIZE)
				this.executeTasks(complicatedStaticRecipeLoadingTaskList);
			this.complicatedStaticRecipeIterationsCounter.incrementAndGet();
		}
		if (complicatedStaticRecipeLoadingTaskList.size() > 0) this.executeTasks(complicatedStaticRecipeLoadingTaskList);
		if (this.complicatedStaticRecipeIterationsCounter.get() >= this.complicatedStaticRecipeIterations)
			this.complicatedStaticRecipeLoadingThreadPool.shutdown();
	}

	private void executeTasks(Collection<Runnable> tasks) {
		List<Runnable> copy = new ArrayList<>(tasks);
		tasks.clear();
		this.complicatedStaticRecipeLoadingThreadPool.execute(() -> {
			try {
				for (Runnable runnable : copy)
					runnable.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void writeRecipesToCache() {
		if (NEIExtensions.mod.getConfig().isComplicatedStaticRecipeLoadingCacheEnabled())
			try (FileOutputStream out = new FileOutputStream(NEIExtensions.mod.getRecipeCache())) {
			NBTTagCompound cacheRootTag = new NBTTagCompound();
			cacheRootTag.setString(RecipeHandlerManager.NEI_LOTR_VERSION_TAG_KEY, NEIExtensions.VERSION);
			for (RecipeHandler<?> handler : this.recipeHandlers.values())
			try {
			if (handler.getRecipeHandlerCacheManager() != null) {
			RecipeHandlerCacheManager<?> cache = handler.getRecipeHandlerCacheManager();
			if (cache.isCacheEnabled()) {
			NBTTagCompound handlerTag = new NBTTagCompound();
			NBTTagCompound headerTag = new NBTTagCompound();
			NBTTagCompound contentTag = new NBTTagCompound();
			cache.writeRecipesToCache(headerTag, contentTag);
			cache.validateCache();
			handlerTag.setTag(RecipeHandlerManager.RECIPE_HANDLER_HEADER_TAG_KEY, headerTag);
			handlerTag.setTag(RecipeHandlerManager.RECIPE_HANDLER_CONTENT_TAG_KEY, contentTag);
			cacheRootTag.setTag(handler.getUnlocalizedName(), handlerTag);
			NEIExtensions.mod.getLogger().debug("The recipe handler \"" + handler.getUnlocalizedName() + "\" wrote data to the cache");
			}
			}
			} catch (Exception e) {
			NEIExtensions.mod.getLogger().error("The recipe handler \"" + handler.getUnlocalizedName() + "\" couldn't write data to the cache: ", e);
			}
			CompressedStreamTools.writeCompressed(cacheRootTag, out);
			} catch (Exception e) {
			NEIExtensions.mod.getLogger().error("Couldn't write data to the cache: ", e);
			}
	}

	private <T extends Recipe> void postLoadHandler(RecipeHandler<T> handler, Collection<T> recipes) {
		handler.onPostLoad(recipes);
	}

	public void refreshCache() {
		this.writeRecipesToCache();
	}

	public Map<String, RecipeHandler<?>> getRecipeHandlers() {
		return this.recipeHandlers;
	}

}
