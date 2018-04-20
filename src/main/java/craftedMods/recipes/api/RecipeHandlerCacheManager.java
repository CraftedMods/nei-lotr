package craftedMods.recipes.api;

import java.util.Collection;

import net.minecraft.nbt.NBTTagCompound;

public interface RecipeHandlerCacheManager<T extends Recipe> {

	public boolean isCacheEnabled();

	/**
	 * Returns whether the cache is valid. Must return false if invalidateCache was called and true if validateCache was called.
	 * 
	 * @param cacheHeaderTag
	 * @return
	 */
	public boolean isCacheValid(NBTTagCompound cacheHeaderTag);

	public void invalidateCache();

	public void validateCache();

	public Collection<T> readRecipesFromCache(NBTTagCompound cacheHeaderTag, NBTTagCompound cacheContentTag);

	// Cache is validated after writing
	public void writeRecipesToCache(NBTTagCompound cacheHeaderTag, NBTTagCompound cacheContentTag);

}
