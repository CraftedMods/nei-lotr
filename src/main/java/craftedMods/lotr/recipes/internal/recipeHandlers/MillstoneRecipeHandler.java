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

import craftedMods.lotr.recipes.api.utils.LOTRRecipeHandlerUtils;
import craftedMods.lotr.recipes.internal.recipeHandlers.MillstoneRecipeHandler.MillstoneRecipe;
import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.*;
import craftedMods.recipes.api.utils.RecipeHandlerRendererUtils.EnumProgressBarDirection;
import craftedMods.recipes.base.*;
import lotr.client.gui.LOTRGuiMillstone;
import lotr.common.LOTRMod;
import lotr.common.recipe.LOTRMillstoneRecipes;
import lotr.common.recipe.LOTRMillstoneRecipes.MillstoneResult;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

@RegisteredHandler
public class MillstoneRecipeHandler extends AbstractMTRecipeHandler<MillstoneRecipe>
{

    private final MillstoneRecipeHandlerRenderer renderer = new MillstoneRecipeHandlerRenderer ();
    private final MillstoneRecipeHandlerRecipeViewer recipeViewer = new MillstoneRecipeHandlerRecipeViewer (this);

    public MillstoneRecipeHandler ()
    {
        super ("lotr.millstone");
    }

    @Override
    public String getDisplayName ()
    {
        return LOTRMod.millstone.getLocalizedName ();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<MillstoneRecipe> loadRecipes ()
    {
        Collection<MillstoneRecipe> ret = new ArrayList<> ();
        try
        {
            Field millstoneRecipesField = LOTRMillstoneRecipes.class.getDeclaredField ("recipeList");
            millstoneRecipesField.setAccessible (true);
            Map<ItemStack, MillstoneResult> recipes = (Map<ItemStack, MillstoneResult>) millstoneRecipesField
                .get (null);
            recipes.forEach ( (ingredient, result) ->
            {
                ret.add (new MillstoneRecipe (ingredient, result.resultItem, result.chance));
            });

        }
        catch (Exception e)
        {
            logger.error ("Couldn't access the field \"recipes\" in LOTRMillstoneRecipes", e);
        }
        return ret;
    }

    @Override
    public List<RecipeItemSlot> getSlotsForRecipeItems (MillstoneRecipe recipe, EnumRecipeItemRole role)
    {
        return Arrays.asList (role == EnumRecipeItemRole.INGREDIENT ? createRecipeItemSlot (75, 16)
            : createRecipeItemSlot (75, 62));
    }

    @Override
    @SuppressWarnings("unchecked")
    public MillstoneRecipeHandlerRenderer getRenderer ()
    {
        return renderer;
    }

    @Override
    public int getRecipesPerPage ()
    {
        return 1;
    }

    @Override
    public RecipeHandlerRecipeViewer<MillstoneRecipe> getRecipeViewer ()
    {
        return recipeViewer;
    }

    @Override
    protected boolean isMineTweakerSupportEnabled ()
    {
        return RecipeHandlerUtils.getInstance ().hasMineTweaker () && LOTRRecipeHandlerUtils.hasMtLotr ();
    }

    @Override
    public int getDefaultOrder ()
    {
        return 5000;
    }

    public class MillstoneRecipe extends ShapelessRecipe
    {

        private final float chance;

        public MillstoneRecipe (ItemStack ingredient, ItemStack result, float chance)
        {
            super (ingredient, result);
            this.chance = chance;
        }

        public float getChance ()
        {
            return chance;
        }

    }

    public class MillstoneRecipeHandlerRenderer
        implements RecipeHandlerRenderer<MillstoneRecipeHandler, MillstoneRecipe>
    {

        @Override
        public void renderBackground (MillstoneRecipeHandler handler, MillstoneRecipe recipe, int cycleticks)
        {
            RecipeHandlerRendererUtils.getInstance ().bindTexture ("lotr:gui/millstone.png");
            RecipeHandlerRendererUtils.getInstance ().drawTexturedRectangle (70, 15, 79, 24, 26, 68);
            RecipeHandlerRendererUtils.getInstance ().drawProgressBar (76, 38, 176, 0, 14, 32, cycleticks % 48 / 48.0f,
                EnumProgressBarDirection.INCREASE_DOWN);
        }

        @Override
        public void renderForeground (MillstoneRecipeHandler handler, MillstoneRecipe recipe, int cycleticks)
        {
            String text = StatCollector.translateToLocalFormatted ("neiLotr.handler.millstone.chanceLabel",
                recipe.getChance () * 100);
            RecipeHandlerRendererUtils.getInstance ().drawTextCentered (text,
                48 + RecipeHandlerRendererUtils.getInstance ().getStringWidth (text) / 2, 95, 4210752,
                false);
        }

    }

    public class MillstoneRecipeHandlerRecipeViewer
        extends AbstractRecipeViewer<MillstoneRecipe, MillstoneRecipeHandler>
    {

        private final Collection<Class<? extends GuiContainer>> supportedGuiClasses = new ArrayList<> ();

        public MillstoneRecipeHandlerRecipeViewer (MillstoneRecipeHandler handler)
        {
            super (handler);
            supportedGuiClasses.addAll (AbstractRecipeViewer.RECIPE_HANDLER_GUIS);
            supportedGuiClasses.add (LOTRGuiMillstone.class);
        }

        @Override
        public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses ()
        {
            return supportedGuiClasses;
        }

        @Override
        public Collection<MillstoneRecipe> getAllRecipes ()
        {
            return handler.isMineTweakerSupportEnabled () ? handler.loadRecipes () : handler.getStaticRecipes ();
        }

        @Override
        public int getOffsetX (Class<? extends GuiContainer> guiClass)
        {
            return guiClass == LOTRGuiMillstone.class ? 36 : 9;
        }

        @Override
        public int getOffsetY (Class<? extends GuiContainer> guiClass)
        {
            return guiClass == LOTRGuiMillstone.class ? 11 : 13;
        }
    }

}
