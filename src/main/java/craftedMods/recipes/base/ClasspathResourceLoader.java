package craftedMods.recipes.base;

import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;

import net.minecraft.util.ResourceLocation;

public class ClasspathResourceLoader implements RecipeHandlerResourceLoader {

	private Set<ResourceLocation> resourceLocations = new HashSet<>();

	@Override
	public boolean registerResource(ResourceLocation location) {
		return resourceLocations.add(location);
	}

	@Override
	public Map<ResourceLocation, Supplier<InputStream>> loadResources() {
		Map<ResourceLocation, Supplier<InputStream>> ret = new HashMap<>();
		resourceLocations.forEach(location -> {
			if (this.getClass().getResource("/" + location.getResourcePath()) != null) {
				ret.put(location, () -> {
					return this.getClass().getResourceAsStream("/" + location.getResourcePath());
				});
			}
		});
		return ret;
	}

}
