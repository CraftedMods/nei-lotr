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
package craftedMods.lotr.recipes.api.recipeHandlers;

import java.util.*;

import org.apache.logging.log4j.Logger;

import craftedMods.lotr.recipes.api.recipeHandlers.AbstractAlloyForgeRecipeHandler.AlloyForgeRecipe;
import craftedMods.lotr.recipes.api.utils.LOTRRecipeHandlerUtils;
import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.*;
import craftedMods.recipes.api.utils.RecipeHandlerRendererUtils.EnumProgressBarDirection;
import craftedMods.recipes.base.*;
import lotr.client.gui.LOTRGuiAlloyForge;
import lotr.common.tileentity.LOTRTileEntityAlloyForgeBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class AbstractAlloyForgeRecipeHandler extends AbstractRecipeHandler<AlloyForgeRecipe>
{

    protected final AlloyForgeAccess alloyForgeDummy;

    protected boolean wasCacheLoaded = false;
    protected boolean useHardcodedRecipes = false;

    private final AlloyForgeRecipeHandlerRenderer renderer = new AlloyForgeRecipeHandlerRenderer ();
    private final AlloyForgeRecipeHandlerCacheManager cacheManager = new AlloyForgeRecipeHandlerCacheManager (this);
    private final AlloyForgeRecipeHandlerRecipeViewer recipeViewer = new AlloyForgeRecipeHandlerRecipeViewer (this);

    protected AbstractAlloyForgeRecipeHandler (String unlocalizedName, AlloyForgeAccess alloyForgeDummy)
    {
        super (unlocalizedName);
        this.alloyForgeDummy = alloyForgeDummy;
    }

    public void onPreLoad (RecipeHandlerConfiguration config, Logger logger)
    {
        super.onPreLoad (config, logger);

        this.useHardcodedRecipes = config.getBoolean ("Use hardcoded recipes", false,
            "Enable this when you experience performance issues caused by this mod. This fixes that issue at the cost of lost compatibility - eventually NEI doesn't show all recipes this device supports.");
    }

    public void onPostLoad (Collection<AlloyForgeRecipe> staticRecipes)
    {
        super.onPostLoad (staticRecipes);

        if (this.useHardcodedRecipes)
        {
            Collection<AlloyForgeRecipe> hardcodedRecipes = getHardcodedRecipes ();

            if (hardcodedRecipes.size () == 0)
            {
                this.logger.warn (
                    "This recipe handler was configured to load hardcoded recipes, but there weren't any - thus this handler shows no recipes.");
            }
            else
            {
                this.logger.debug ("Found " + hardcodedRecipes.size () + " hardcoded recipes that will be used");
            }
            getStaticRecipes ().addAll (getHardcodedRecipes ());
        }
    }

    protected Collection<AlloyForgeRecipe> getHardcodedRecipes ()
    {
        return Arrays.asList ();
    }

    @Override
    public String getDisplayName ()
    {
        return alloyForgeDummy.getName ();
    }

    @Override
    public int getRecipesPerPage ()
    {
        return 1;
    }

    @Override
    public List<RecipeItemSlot> getSlotsForRecipeItems (AlloyForgeRecipe recipe, EnumRecipeItemRole role)
    {
        List<RecipeItemSlot> slots = new ArrayList<> ();
        switch (role)
        {
            case RESULT:
                for (int i = 0; i < 4; i++)
                {
                    slots.add (createRecipeItemSlot (48 + i * 18, 65));
                }
                break;
            case INGREDIENT:
                int size = recipe.getRecipeItems (EnumRecipeItemRole.INGREDIENT).size ();
                for (int i = 0; i < size; i++)
                {
                    slots.add (createRecipeItemSlot (48 + (size == 4 ? i : i / 2) * 18,
                        size == 4 ? 19 : (i + 1) % 2 == 0 ? 1 : 19));
                }
                break;
            case OTHER:
                slots.add (createRecipeItemSlot (75, 109));
                break;
        }
        return slots;
    }

    @Override
    public int getComplicatedStaticRecipeDepth ()
    {
        return (this.useHardcodedRecipes || this.wasCacheLoaded) ? 0 : 2;
    }

    @Override
    public AlloyForgeRecipe loadComplicatedStaticRecipe (ItemStack... stacks)
    {
        AlloyForgeRecipe ret = null;
        if (stacks[1] == null)
        {
            ItemStack result = alloyForgeDummy.getSmeltingResult (stacks[0]);
            if (result != null)
            {
                ret = new AlloyForgeRecipe (Arrays.asList (stacks[0]), result);
            }
        }
        else
        {
            ItemStack result = alloyForgeDummy.getAlloyResult (stacks[0], stacks[1]);
            if (result != null)
            {
                ret = new AlloyForgeRecipe (Arrays.asList (stacks), result);
            }
        }
        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AlloyForgeRecipeHandlerRenderer getRenderer ()
    {
        return renderer;
    }

    @Override
    public RecipeHandlerCacheManager<AlloyForgeRecipe> getCacheManager ()
    {
        return cacheManager;
    }

    @Override
    public RecipeHandlerRecipeViewer<AlloyForgeRecipe> getRecipeViewer ()
    {
        return recipeViewer;
    }

    public class AlloyForgeRecipeHandlerCacheManager extends AbstractRecipeHandlerCacheManager<AlloyForgeRecipe>
    {

        public AlloyForgeRecipeHandlerCacheManager (AbstractAlloyForgeRecipeHandler handler)
        {
            super (handler);
        }

        public boolean isCacheEnabled ()
        {
            return ! ((AbstractAlloyForgeRecipeHandler) this.handler).useHardcodedRecipes;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Collection<AlloyForgeRecipe> readRecipesFromCache (NBTTagCompound cacheHeaderTag,
            NBTTagCompound cacheContentTag)
        {
            Collection<AlloyForgeRecipe> ret = new ArrayList<> (150);
            for (String key : (Set<String>) cacheContentTag.func_150296_c ())
            {
                NBTTagCompound recipeTag = cacheContentTag.getCompoundTag (key);
                AlloyForgeRecipe recipe = AlloyForgeRecipe.readRecipeFromNBT (recipeTag);
                if (recipe != null)
                {
                    ret.add (recipe);
                }
            }
            wasCacheLoaded = ret.size () > 0;
            return ret;
        }

        @Override
        public void writeRecipesToCache (NBTTagCompound cacheHeaderTag, NBTTagCompound cacheContentTag)
        {
            super.writeRecipesToCache (cacheHeaderTag, cacheContentTag);
            int recipeIndex = 0;
            for (AlloyForgeRecipe recipe : AbstractAlloyForgeRecipeHandler.this.staticRecipes)
            {
                NBTTagCompound recipeTag = new NBTTagCompound ();
                recipe.writeRecipeToNBT (recipeTag);
                cacheContentTag.setTag (Integer.toString (recipeIndex++), recipeTag);
            }
        }

    }

    public class AlloyForgeRecipeHandlerRenderer
        implements RecipeHandlerRenderer<AbstractAlloyForgeRecipeHandler, AlloyForgeRecipe>
    {

        @Override
        public void renderBackground (AbstractAlloyForgeRecipeHandler handler, AlloyForgeRecipe recipe, int cycleticks)
        {
            RecipeHandlerRendererUtils.getInstance ().bindTexture ("lotr:gui/forge.png");
            RecipeHandlerRendererUtils.getInstance ().drawTexturedRectangle (15, 0, 20, 20, 110, 130);
            RecipeHandlerRendererUtils.getInstance ().drawProgressBar (75, 37, 176, 14, 16, 30, cycleticks % 48 / 48.0f,
                EnumProgressBarDirection.INCREASE_DOWN);
            RecipeHandlerRendererUtils.getInstance ().drawProgressBar (76, 93, 176, 0, 14, 13, cycleticks % 48 / 48.0f,
                EnumProgressBarDirection.DECREASE_DOWN);
        }

        @Override
        public void renderForeground (AbstractAlloyForgeRecipeHandler handler, AlloyForgeRecipe recipe, int cycleticks)
        {
        }

    }

    public static class AlloyForgeRecipe extends FurnaceRecipe
    {

        private final ItemStack ingredientItem;
        private final ItemStack alloyItem;
        private final ItemStack resultItem;

        public AlloyForgeRecipe (Collection<ItemStack> ingredients, ItemStack result)
        {
            super (ingredients, result);
            for (int i = 0; i < 3; i++)
            {
                this.addAll (ingredients, this.ingredients);
            }
            for (int i = 0; i < 3; i++)
            {
                add (result, results);
            }
            ingredientItem = this.ingredients.get (0).iterator ().next ();
            if (ingredients.size () == 2)
            {
                alloyItem = this.ingredients.get (1).iterator ().next ();
            }
            else
            {
                alloyItem = null;
            }
            resultItem = result;
        }

        public static AlloyForgeRecipe readRecipeFromNBT (NBTTagCompound parent)
        {
            AlloyForgeRecipe ret = null;
            if (parent.hasKey ("ingredientItem") && parent.hasKey ("alloyItem") && parent.hasKey ("resultItem"))
            {
                NBTTagCompound ingredientItem = parent.getCompoundTag ("ingredientItem");
                NBTTagCompound alloyItem = parent.getCompoundTag ("alloyItem");
                NBTTagCompound resultItem = parent.getCompoundTag ("resultItem");

                ItemStack alloyStack = alloyItem.hasKey ("Identifier")
                    ? RecipeHandlerUtils.getInstance ().readItemStackFromNBT (alloyItem)
                    : null;

                Collection<ItemStack> ingreds = new ArrayList<> ();
                ingreds.add (RecipeHandlerUtils.getInstance ().readItemStackFromNBT (ingredientItem));
                if (alloyStack != null)
                {
                    ingreds.add (alloyStack);
                }
                ret = new AlloyForgeRecipe (ingreds,
                    RecipeHandlerUtils.getInstance ().readItemStackFromNBT (resultItem));
            }
            return ret;
        }

        public void writeRecipeToNBT (NBTTagCompound parent)
        {
            NBTTagCompound ingredientItem = new NBTTagCompound ();
            NBTTagCompound alloyItem = new NBTTagCompound ();
            NBTTagCompound resultItem = new NBTTagCompound ();

            RecipeHandlerUtils.getInstance ().writeItemStackToNBT (this.ingredientItem, ingredientItem);
            if (this.alloyItem != null)
            {
                RecipeHandlerUtils.getInstance ().writeItemStackToNBT (this.alloyItem, alloyItem);
            }
            RecipeHandlerUtils.getInstance ().writeItemStackToNBT (this.resultItem, resultItem);

            parent.setTag ("ingredientItem", ingredientItem);
            parent.setTag ("alloyItem", alloyItem);
            parent.setTag ("resultItem", resultItem);
        }

    }

    public static interface AlloyForgeAccess
    {

        public String getName ();

        public ItemStack getSmeltingResult (ItemStack ingredient);

        public ItemStack getAlloyResult (ItemStack ingredient, ItemStack alloy);

    }

    public class AlloyForgeRecipeHandlerRecipeViewer
        extends AbstractRecipeViewer<AlloyForgeRecipe, AbstractAlloyForgeRecipeHandler>
    {

        private final Collection<Class<? extends GuiContainer>> supportedGuiClasses = new ArrayList<> ();

        public AlloyForgeRecipeHandlerRecipeViewer (AbstractAlloyForgeRecipeHandler handler)
        {
            super (handler);
            supportedGuiClasses.addAll (AbstractRecipeViewer.RECIPE_HANDLER_GUIS);
            supportedGuiClasses.add (LOTRGuiAlloyForge.class);
        }

        @Override
        public Collection<AlloyForgeRecipe> getAllRecipes ()
        {
            return handler.getStaticRecipes ();
        }

        @Override
        public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses ()
        {
            return supportedGuiClasses;
        }

        @Override
        public boolean isGuiContainerSupported (GuiContainer container)
        {
            if (container instanceof LOTRGuiAlloyForge)
            {
                LOTRTileEntityAlloyForgeBase forge = LOTRRecipeHandlerUtils
                    .getAlloyForge ((LOTRGuiAlloyForge) container);
                if (!forge.getForgeName ().equals (handler.alloyForgeDummy.getName ()))
                    return false;
            }
            return true;
        }

        @Override
        public int getOffsetX (Class<? extends GuiContainer> guiClass)
        {
            return 9;
        }

        @Override
        public int getOffsetY (Class<? extends GuiContainer> guiClass)
        {
            return guiClass == LOTRGuiAlloyForge.class ? 27 : 18;
        }

    }

}
