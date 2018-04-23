package craftedMods.recipes.api;

import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;

public interface RecipeHandlerFactory {

	public Set<RecipeHandler<?>> getRecipeHandlers();

	public Map<ResourceLocation, Supplier<InputStream>> getResources();

}
