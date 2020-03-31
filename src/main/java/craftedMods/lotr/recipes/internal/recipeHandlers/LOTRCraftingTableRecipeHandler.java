/*******************************************************************************
 * Copyright (C) 2020 CraftedMods (see https://github.com/CraftedMods)
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
package craftedMods.lotr.recipes.internal.recipeHandlers;

import java.util.Collection;
import java.util.function.Supplier;

import craftedMods.lotr.recipes.api.recipeHandlers.AbstractMiddleEarthCraftingTableRecipeHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;

public class LOTRCraftingTableRecipeHandler extends AbstractMiddleEarthCraftingTableRecipeHandler
{

    public static final String UNLOCALIZED_NAME_PREFIX = "lotr.middleEarthCrafting.";

    public LOTRCraftingTableRecipeHandler (String unlocalizedName, Class<? extends GuiContainer> guiClass,
        Supplier<Collection<IRecipe>> recipesGetter)
    {
        super (LOTRCraftingTableRecipeHandler.UNLOCALIZED_NAME_PREFIX + unlocalizedName, guiClass, recipesGetter);
    }

    @Override
    public String getDisplayName ()
    {
        return StatCollector
            .translateToLocal ("container.lotr.crafting."
                + getUnlocalizedName ().replace (LOTRCraftingTableRecipeHandler.UNLOCALIZED_NAME_PREFIX, ""));
    }

    @Override
    public int getDefaultOrder ()
    {
        return 2000;
    }

}
