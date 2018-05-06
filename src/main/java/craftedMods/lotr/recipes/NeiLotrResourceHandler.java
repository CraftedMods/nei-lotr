package craftedMods.lotr.recipes;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Supplier;

import craftedMods.recipes.api.*;
import craftedMods.recipes.base.*;
import net.minecraft.util.ResourceLocation;

@RegisteredHandler
public class NeiLotrResourceHandler implements ResourceHandler {

	@Override
	public Map<ResourceLocation, Supplier<InputStream>> getResources() {
		RecipeHandlerResourceLoader resourceLoader = new ClasspathResourceLoader();
		resourceLoader.registerResource(new RecipeHandlerResourceLocation("lang/en_US.lang"));
		resourceLoader.registerResource(new RecipeHandlerResourceLocation("lang/de_DE.lang"));
		return resourceLoader.loadResources();
	}

}
