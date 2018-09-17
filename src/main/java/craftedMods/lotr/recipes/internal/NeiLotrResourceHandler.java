/*******************************************************************************
 * Copyright (C) 2018 CraftedMods (see https://github.com/CraftedMods)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package craftedMods.lotr.recipes.internal;

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
