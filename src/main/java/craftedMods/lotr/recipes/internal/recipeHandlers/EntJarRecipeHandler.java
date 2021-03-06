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

import java.lang.reflect.Field;
import java.util.*;

import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.RecipeHandlerRendererUtils;
import craftedMods.recipes.base.*;
import lotr.common.LOTRMod;
import lotr.common.recipe.LOTREntJarRecipes;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

@RegisteredHandler
public class EntJarRecipeHandler extends AbstractRecipeHandler<ShapelessRecipe>
{

    private final EntJarRecipeHandlerRenderer renderer = new EntJarRecipeHandlerRenderer ();
    private final EntJarRecipeHandlerRecipeViewer recipeViewer = new EntJarRecipeHandlerRecipeViewer (this);

    public EntJarRecipeHandler ()
    {
        super ("lotr.entJar");
    }

    @Override
    public String getDisplayName ()
    {
        return LOTRMod.entJar.getLocalizedName ();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<ShapelessRecipe> loadSimpleStaticRecipes ()
    {
        Collection<ShapelessRecipe> ret = new ArrayList<> ();
        try
        {
            Field entJarRecipesField = LOTREntJarRecipes.class.getDeclaredField ("recipes");
            entJarRecipesField.setAccessible (true);
            Map<ItemStack, ItemStack> recipes = (Map<ItemStack, ItemStack>) entJarRecipesField.get (null);
            recipes.forEach ( (ingredient, result) ->
            {
                ret.add (new ShapelessRecipe (ingredient.copy (), result.copy ()));
            });
        }
        catch (Exception e)
        {
            logger.error ("Couldn't access the field \"recipes\" in LOTREntJarRecipes.class", e);
        }

        return ret;
    }

    @Override
    public List<RecipeItemSlot> getSlotsForRecipeItems (ShapelessRecipe recipe, EnumRecipeItemRole role)
    {
        return Arrays.asList (role == EnumRecipeItemRole.INGREDIENT ? createRecipeItemSlot (43, 24)
            : createRecipeItemSlot (101, 24));
    }

    @Override
    @SuppressWarnings("unchecked")
    public EntJarRecipeHandlerRenderer getRenderer ()
    {
        return renderer;
    }

    @Override
    public RecipeHandlerRecipeViewer<ShapelessRecipe> getRecipeViewer ()
    {
        return recipeViewer;
    }

    @Override
    public int getDefaultOrder ()
    {
        return 6000;
    }

    public class EntJarRecipeHandlerRenderer implements RecipeHandlerRenderer<EntJarRecipeHandler, ShapelessRecipe>
    {

        @Override
        public void renderBackground (EntJarRecipeHandler handler, ShapelessRecipe recipe, int cycleticks)
        {
            RecipeHandlerRendererUtils.getInstance ().bindTexture (RecipeHandlerRenderer.DEFAULT_GUI_TEXTURE);
            RecipeHandlerRendererUtils.getInstance ().drawTexturedRectangle (42, 19, 65, 30, 80, 26);
            RecipeHandlerRendererUtils.getInstance ().drawRectangle (42, 13, 18, 10, 0xFFC6C6C6);
            RecipeHandlerRendererUtils.getInstance ().drawRectangle (42, 41, 18, 4, 0xFFC6C6C6);
        }

        @Override
        public void renderForeground (EntJarRecipeHandler handler, ShapelessRecipe recipe, int cycleticks)
        {
        }

    }

    public class EntJarRecipeHandlerRecipeViewer extends AbstractRecipeViewer<ShapelessRecipe, EntJarRecipeHandler>
    {

        public EntJarRecipeHandlerRecipeViewer (EntJarRecipeHandler handler)
        {
            super (handler);
        }

        @Override
        public Collection<ShapelessRecipe> getAllRecipes ()
        {
            return handler.getStaticRecipes ();
        }

        @Override
        public int getOffsetX (Class<? extends GuiContainer> guiClass)
        {
            return 8;
        }

    }

}
