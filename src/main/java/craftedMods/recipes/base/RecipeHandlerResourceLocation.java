package craftedMods.recipes.base;

import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import net.minecraft.util.ResourceLocation;

public class RecipeHandlerResourceLocation extends ResourceLocation {

	public RecipeHandlerResourceLocation(String path) {
		super(RecipeHandlerUtils.getInstance().getResourceDomain(), path);
	}

}
