package craftedMods.recipes.api;

import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.function.Supplier;

import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * The recipe handler implementation needs - to be discovered - a default constructor.
 *
 * @param The
 *            type of cached recipes the recipe handler produces/accepts
 */
public interface RecipeHandler<T extends Recipe> {

	/**
	 * The unlocalized name of the recipe handler. It must be unique, and will be used like an ID. Use dots in the name to group the recipe handlers.
	 *
	 * @return The unlocalized name
	 */
	public String getUnlocalizedName();

	/**
	 * The localized display name of the recipe handler. Usually the name of the device where the crafting etc. takes place.
	 *
	 * @return The display name
	 */
	public String getDisplayName();

	/**
	 * @return Whether all static recipes for this handler were loaded
	 */
	public boolean areStaticRecipesLoaded();

	/**
	 * Returns a set of all pre-computed ("static") recipes of this handler. The Set returned may be null
	 *
	 * @return a set of static recipes
	 */
	public Collection<T> getStaticRecipes();

	/**
	 * Called every time the player queries the crafting recipes for an item in NEI. The set returned may be null. (Simple)Static recipe loading should be preferred if possible.
	 *
	 * @param result
	 *            The result item
	 * @return The crafting recipes with the result item as result
	 */
	public Collection<T> getDynamicCraftingRecipes(ItemStack result);

	/**
	 * Called every time the player queries the usage recipes for an item in NEI. The Set returned may be null. (Simple) Static recipe loading should be preferred if possible.
	 *
	 * @param result
	 *            The ingredient item
	 * @return The crafting recipes with the ingredient item as ingredient
	 */
	public Collection<T> getDynamicUsageRecipes(ItemStack ingredient);

	/**
	 * Called if the recipe handler is loaded, but before the static recipes are loaded. All initializations should be done here, not in a constructor.
	 *
	 * @param config
	 *            An object that allows the recipe handler to access it's own configuration file section
	 * @param logger
	 *            The logger assigned to this recipe handler
	 */
	public void onPreLoad(RecipeHandlerConfiguration config, Logger logger);

	/**
	 * Called when the recipe handler is loaded; at this time all static recipes were loaded.
	 *
	 * @param staticRecipes
	 *            The static recipes that were loaded for this handler. They've to be returned by getStaticRecipes()
	 */
	public void onPostLoad(Collection<T> staticRecipes);

	/**
	 * Loads all static recipes that don't need any external dependencies, to example a list of all items. The result may be null.
	 *
	 * @return a set of simple static recipes
	 */
	public Collection<T> loadSimpleStaticRecipes();

	/**
	 * The count of item stacks in a combination for loadComplicatedStaticRecipe(). If less equal zero, loadComplicatedStaticRecipe() won't be called.
	 *
	 * @return the depth of complicated static recipes
	 */
	public int getComplicatedStaticRecipeDepth();

	/**
	 * Loads a static recipe that needs a combination of item stacks, to example an alloy forge recipe. Something like this is necessary if a mod doesn't provide an IRecipe instance but a function like canSmelt(ItemStack ingredient, ItemStack
	 * alloyItem) that determines the recipe dynamically. A collection of all possible item combinations has to be created which will be sent to this function, which returns a cached recipe if the input matches with a "recipe". The count of item
	 * stacks in a combination (to example two for the method above) is specified in getComplicatedStaticRecipeDepth(). Please note that itemcount^staticRecipeDepth iterations through all items are necessary to test all possible combinations, so this
	 * can be very expensive. If possible, avoid this method of loading static recipes. This method can be called concurrently from multiple threads.
	 *
	 * @param stacks
	 *            An array containing a combination of getComplicatedStaticRecipeDepth() item stacks
	 * @return a set of one complicated static recipe or null if no recipe matches
	 */
	public T loadComplicatedStaticRecipe(ItemStack... stacks);

	/**
	 * Returns the URL of a file containing the current released version of the recipe handler. This will be used for an internal version checker, which notifies the user about new versions of the recipe handler. An internet connection is needed for
	 * this. If the URL is null, no version checks will be made.
	 *
	 * @return An URL to a file containing informations about the current version of the recipe handler or null.
	 */
	public URL getVersionCheckerURL();

	/**
	 * Slots may be null //TODO: Doc!
	 *
	 * @param recipe
	 * @param role
	 * @return
	 */
	public List<RecipeItemSlot> getSlotsForRecipeItems(T recipe, EnumRecipeItemRole role);

	public Map<ResourceLocation, Supplier<InputStream>> getResources();

	public <V extends RecipeHandlerRenderer<W, T>, W extends RecipeHandler<T>> V getRenderer();

	/**
	 * Returns the count of recipes displayed in the GUI per page.
	 *
	 * @return The count of recipes per page
	 */
	public int getRecipesPerPage();

	public void onUpdate(int cycleticks);

	// Can be null
	public RecipeHandlerCacheManager<T> getRecipeHandlerCacheManager();

	public String getVersion();

	public RecipeHandlerCraftingHelper<T> getCraftingHelper();

}
