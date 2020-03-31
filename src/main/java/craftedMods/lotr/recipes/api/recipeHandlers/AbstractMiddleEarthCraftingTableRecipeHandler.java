/*******************************************************************************
 * Copyright (C) 2019 CraftedMods (see https://github.com/CraftedMods)
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
package craftedMods.lotr.recipes.api.recipeHandlers;

import java.util.*;
import java.util.function.Supplier;

import craftedMods.lotr.recipes.api.utils.LOTRRecipeHandlerUtils;
import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.RecipeHandlerUtils;
import craftedMods.recipes.base.*;
import lotr.common.recipe.LOTRRecipePoisonWeapon;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.crafting.IRecipe;

public abstract class AbstractMiddleEarthCraftingTableRecipeHandler extends CraftingGridRecipeHandler
{
    
    private final AbstractMiddleEarthCraftingTableRecipeHandlerCraftingHelper craftingHelper;
    private final AbstractMiddleEarthCraftingTableRecipeHandlerRecipeViewer recipeViewer;

    public AbstractMiddleEarthCraftingTableRecipeHandler(String unlocalizedName, Class<? extends GuiContainer> guiClass, Supplier<Collection<IRecipe>> recipesGetter) {
        super(unlocalizedName, recipesGetter);
        
        this.craftingHelper = new AbstractMiddleEarthCraftingTableRecipeHandlerCraftingHelper(guiClass);
        this.recipeViewer = new AbstractMiddleEarthCraftingTableRecipeHandlerRecipeViewer(this, guiClass);
    }

    @Override
    protected void undefinedRecipeTypeFound (IRecipe recipe, Collection<AbstractRecipe> container)
    {
        if (recipe instanceof LOTRRecipePoisonWeapon)
        {
            AbstractRecipe processedRecipe = LOTRRecipeHandlerUtils
                .processPoisonWeaponRecipe ((LOTRRecipePoisonWeapon) recipe);
            if (processedRecipe != null)
            {
                container.add (processedRecipe);
                return;
            }
        }
        super.undefinedRecipeTypeFound (recipe, container);
    }
    
    @Override
    public RecipeHandlerCraftingHelper<AbstractRecipe> getCraftingHelper() {
        return this.craftingHelper;
    }

    @Override
    public RecipeHandlerRecipeViewer<AbstractRecipe> getRecipeViewer() {
        return this.recipeViewer;
    }
    
    @Override
    protected boolean isMineTweakerSupportEnabled ()
    {
        return RecipeHandlerUtils.getInstance ().hasMineTweaker () && LOTRRecipeHandlerUtils.hasMtLotr ();
    }
    
    private class AbstractMiddleEarthCraftingTableRecipeHandlerCraftingHelper extends AbstractCraftingHelper<AbstractRecipe> {

        private final Collection<Class<? extends GuiContainer>> guiClass;

        public AbstractMiddleEarthCraftingTableRecipeHandlerCraftingHelper(Class<? extends GuiContainer> guiClass) {
            this.guiClass = Arrays.asList(guiClass);
        }

        @Override
        public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses(AbstractRecipe recipe) {
            return this.guiClass;
        }

        @Override
        public int getOffsetX(Class<? extends GuiContainer> guiClass, AbstractRecipe recipe) {
            return 5;
        }

        @Override
        public int getOffsetY(Class<? extends GuiContainer> guiClass, AbstractRecipe recipe) {
            return 11;
        }

    }

    private class AbstractMiddleEarthCraftingTableRecipeHandlerRecipeViewer extends AbstractRecipeViewer<AbstractRecipe, AbstractMiddleEarthCraftingTableRecipeHandler> {

        private final Collection<Class<? extends GuiContainer>> supportedGuiClasses = new ArrayList<>();

        public AbstractMiddleEarthCraftingTableRecipeHandlerRecipeViewer(AbstractMiddleEarthCraftingTableRecipeHandler handler, Class<? extends GuiContainer> guiClass) {
            super(handler);
            this.supportedGuiClasses.addAll(AbstractRecipeViewer.RECIPE_HANDLER_GUIS);
            this.supportedGuiClasses.add(guiClass);
        }

        @Override
        public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses() {
            return this.supportedGuiClasses;
        }

        @Override
        public Collection<AbstractRecipe> getAllRecipes ()
        {
            return handler.isMineTweakerSupportEnabled () ? handler.loadRecipes () : handler.getStaticRecipes ();
        }

    }

}
