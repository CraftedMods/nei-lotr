package craftedMods.recipes.base;

import craftedMods.recipes.api.*;
import net.minecraft.nbt.NBTTagCompound;

public abstract class AbstractRecipeHandlerCacheManager<T extends Recipe> implements RecipeHandlerCacheManager<T> {

	public static final String RECIPE_HANDLER_VERSION_KEY = "handlerVersion";

	protected final RecipeHandler<T> handler;

	private boolean isCacheValid = true;

	protected AbstractRecipeHandlerCacheManager(RecipeHandler<T> handler) {
		this.handler = handler;
	}

	@Override
	public boolean isCacheEnabled() {
		return true;
	}

	@Override
	public void invalidateCache() {
		isCacheValid = false;
	}

	@Override
	public void validateCache() {
		isCacheValid = true;
	}

	@Override
	public boolean isCacheValid(NBTTagCompound cacheHeaderTag) {
		return isCacheValid && cacheHeaderTag.getString(RECIPE_HANDLER_VERSION_KEY).equals(handler.getVersion());
	}

	@Override
	public void writeRecipesToCache(NBTTagCompound cacheHeaderTag, NBTTagCompound cacheContentTag) {
		cacheHeaderTag.setString(RECIPE_HANDLER_VERSION_KEY, handler.getVersion());
	}

}
