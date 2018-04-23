package craftedMods.recipes.base;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;

public interface RecipeHandlerResourceLoader {
	
	public boolean registerResource(ResourceLocation location);
	
	public Map<ResourceLocation, Supplier<InputStream>> loadResources();

}
