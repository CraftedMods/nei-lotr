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

import java.lang.reflect.Method;
import java.util.*;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import craftedMods.lotr.recipes.internal.recipeHandlers.UnsmelteryRecipeHandler.UnsmelteryRecipe;
import craftedMods.recipes.api.*;
import craftedMods.recipes.api.utils.*;
import craftedMods.recipes.api.utils.RecipeHandlerRendererUtils.EnumProgressBarDirection;
import craftedMods.recipes.base.*;
import lotr.client.gui.LOTRGuiUnsmeltery;
import lotr.common.LOTRMod;
import lotr.common.tileentity.LOTRTileEntityUnsmeltery;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

@RegisteredHandler
public class UnsmelteryRecipeHandler extends AbstractRecipeHandler<UnsmelteryRecipe>
{

    private final UnsmelteryRecipeHandlerRenderer renderer = new UnsmelteryRecipeHandlerRenderer ();
    private final UnsmelteryRecipeHandlerCacheManager cacheManager = new UnsmelteryRecipeHandlerCacheManager (this);
    private final UnsmelteryRecipeHandlerRecipeViewer recipeViewer = new UnsmelteryRecipeHandlerRecipeViewer (this);

    private final LOTRTileEntityUnsmeltery unsmeltery = new LOTRTileEntityUnsmeltery ();
    private Method getLargestUnsmeltingResultMethod;

    private boolean wasCacheLoaded = false;

    private boolean areRecipesLoaded = false;

    private boolean wasPlayerLoggedIn = false;

    private final Collection<UnsmelteryRecipe> recipesLoadedOnWorldJoin = new ArrayList<> (500);

    public UnsmelteryRecipeHandler ()
    {
        super ("lotr.unsmeltery");
    }

    @Override
    public void onPreLoad (RecipeHandlerConfiguration config, Logger logger)
    {
        super.onPreLoad (config, logger);
        FMLCommonHandler.instance ().bus ().register (this);
        MinecraftForge.EVENT_BUS.register (this);
        try
        {
            getLargestUnsmeltingResultMethod = unsmeltery.getClass ()
                .getDeclaredMethod ("getLargestUnsmeltingResult", ItemStack.class);
            getLargestUnsmeltingResultMethod.setAccessible (true);
        }
        catch (Exception e)
        {
            logger.error ("Couldn't access the LOTR unsmeltery: ", e);
        }
    }

    @Override
    public String getDisplayName ()
    {
        return LOTRMod.unsmeltery.getLocalizedName ();
    }

    @Override
    public int getRecipesPerPage ()
    {
        return 2;
    }

    @Override
    public Collection<UnsmelteryRecipe> getDynamicUsageRecipes (ItemStack ingredient)
    {
        Collection<UnsmelteryRecipe> usageRecipes = new ArrayList<> ();
        UnsmelteryRecipe recipe = createUnsmelteryRecipe (ingredient);
        if (recipe != null)
        {
            usageRecipes.add (recipe);
        }
        return usageRecipes;
    }

    @SubscribeEvent
    public void onWorldLoad (WorldEvent.Load event)
    {
        wasPlayerLoggedIn = true;
    }

    @SubscribeEvent
    public void onWorldExit (WorldEvent.Unload event)
    {
        unsmeltery.setWorldObj (null);
        wasPlayerLoggedIn = false;
    }

    @SubscribeEvent
    public void onTick (TickEvent.ClientTickEvent event)
    {
        if (wasPlayerLoggedIn && Minecraft.getMinecraft ().theWorld != null
            && Minecraft.getMinecraft ().thePlayer != null)
        {
            unsmeltery.setWorldObj (Minecraft.getMinecraft ().theWorld);
            if (!areRecipesLoaded && !wasCacheLoaded)
            {
                long start = System.currentTimeMillis ();
                int recipeCount = staticRecipes.size ();
                loadUnsmelteryCraftingRecipes ();
                logger
                    .info ("Loaded additional " + (staticRecipes.size () - recipeCount) + " static recipes within "
                        + (System.currentTimeMillis () - start) + " ms");
            }
            cacheManager.invalidateCache ();
            RecipeHandlerUtils.getInstance ().forceRecipeCacheRefresh ();
            wasPlayerLoggedIn = false;
        }
    }

    protected void loadUnsmelteryCraftingRecipes ()
    {
        RecipeHandlerUtils.getInstance ().getItemList ().forEach (stack ->
        {
            UnsmelteryRecipe recipe = createUnsmelteryRecipe (stack);
            if (recipe != null)
            {
                staticRecipes.add (recipe);
                recipesLoadedOnWorldJoin.add (recipe);
            }
        });
        areRecipesLoaded = true;
    }

    protected UnsmelteryRecipe createUnsmelteryRecipe (ItemStack ingredient)
    {
        UnsmelteryRecipe ret = null;
        ItemStack result = getLargestUnsmeltingResult (ingredient);
        if (result != null)
        {
            int maxStackSize = UnsmelteryRecipeHandler.getMaxUnsmeltingResultCount (ingredient, result);
            int minStackSize = UnsmelteryRecipeHandler.getMinUnsmeltingResultCount (ingredient, result);
            result.stackSize = 1;
            ret = new UnsmelteryRecipe (ingredient.copy (), result.copy (), minStackSize, maxStackSize);
        }
        return ret;
    }

    @Override
    public List<RecipeItemSlot> getSlotsForRecipeItems (UnsmelteryRecipe recipe, EnumRecipeItemRole role)
    {
        ArrayList<RecipeItemSlot> ret = new ArrayList<> ();
        switch (role)
        {
            case INGREDIENT:
                ret.add (createRecipeItemSlot (51, 6));
                break;
            case OTHER:
                ret.add (createRecipeItemSlot (51, 42));
                break;
            case RESULT:
                ret.add (createRecipeItemSlot (111, 24));
                ret.add (createRecipeItemSlot (136, 24));
                break;
        }
        return ret;
    }

    @Override
    @SuppressWarnings("unchecked")
    public UnsmelteryRecipeHandlerRenderer getRenderer ()
    {
        return renderer;
    }

    @Override
    public RecipeHandlerCacheManager<UnsmelteryRecipe> getCacheManager ()
    {
        return cacheManager;
    }

    @Override
    public RecipeHandlerRecipeViewer<UnsmelteryRecipe> getRecipeViewer ()
    {
        return recipeViewer;
    }

    public ItemStack getLargestUnsmeltingResult (ItemStack stack)
    {
        ItemStack ret = null;
        try
        {
            ret = (ItemStack) getLargestUnsmeltingResultMethod.invoke (unsmeltery, stack);
        }
        catch (Exception e)
        {
            logger.error ("Couldn't get the largest unsmeltery result: ", e);
        }
        return ret;
    }

    @Override
    public int getDefaultOrder ()
    {
        return 7000;
    }

    public static int getMinUnsmeltingResultCount (ItemStack ingredient, ItemStack largestPossibleResult)
    {
        return UnsmelteryRecipeHandler.getUnsmeltingResultCount (ingredient, largestPossibleResult, 0.7f);
    }

    public static int getMaxUnsmeltingResultCount (ItemStack ingredient, ItemStack largestPossibleResult)
    {
        return UnsmelteryRecipeHandler.getUnsmeltingResultCount (ingredient, largestPossibleResult, 1.0f);
    }

    public static int getUnsmeltingResultCount (ItemStack ingredient, ItemStack largestPossibleResult,
        float minReduction)
    {
        float size = largestPossibleResult.stackSize * 0.8f;
        if (ingredient.isItemStackDamageable ())
        {
            size *= (ingredient.getMaxDamage () - ingredient.getItemDamage ()) / (float) ingredient.getMaxDamage ();
        }
        size *= minReduction;
        return Math.max (0, Math.round (size));
    }

    public class UnsmelteryRecipeHandlerCacheManager extends AbstractRecipeHandlerCacheManager<UnsmelteryRecipe>
    {

        public UnsmelteryRecipeHandlerCacheManager (UnsmelteryRecipeHandler handler)
        {
            super (handler);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Collection<UnsmelteryRecipe> readRecipesFromCache (NBTTagCompound cacheHeaderTag,
            NBTTagCompound cacheContentTag)
        {
            Collection<UnsmelteryRecipe> ret = new ArrayList<> (150);
            for (String key : (Set<String>) cacheContentTag.func_150296_c ())
            {
                NBTTagCompound recipeTag = cacheContentTag.getCompoundTag (key);
                UnsmelteryRecipe recipe = UnsmelteryRecipe.readRecipeFromNBT (recipeTag);
                if (recipe != null)
                {
                    ret.add (recipe);
                }
            }
            wasCacheLoaded = ret.size () > 0;
            recipesLoadedOnWorldJoin.addAll (ret);
            return ret;
        }

        @Override
        public void writeRecipesToCache (NBTTagCompound cacheHeaderTag, NBTTagCompound cacheContentTag)
        {
            super.writeRecipesToCache (cacheHeaderTag, cacheContentTag);
            int recipeIndex = 0;
            for (UnsmelteryRecipe recipe : recipesLoadedOnWorldJoin)
            {
                NBTTagCompound recipeTag = new NBTTagCompound ();
                recipe.writeRecipeToNBT (recipeTag);
                cacheContentTag.setTag (Integer.toString (recipeIndex++), recipeTag);
            }
        }

    }

    public static class UnsmelteryRecipe extends FurnaceRecipe
    {

        private final ItemStack ingredientItem;
        private final ItemStack resultItem;

        private final int minCount;
        private final int maxCount;

        public UnsmelteryRecipe (ItemStack ingredient, ItemStack result, int minCount, int maxCount)
        {
            super (ingredient, result);
            ingredientItem = ingredient.copy ();
            resultItem = result.copy ();
            this.minCount = minCount;
            this.maxCount = maxCount;
            add (result, results);
        }

        @Override
        public boolean consumes (ItemStack ingredient)
        {
            return false;
        }

        public int getMinCount ()
        {
            return minCount;
        }

        public int getMaxCount ()
        {
            return maxCount;
        }

        public static final String INGREDIENT_ITEM_KEY = "ingredientItem";
        public static final String RESULT_ITEM_KEY = "resultItem";
        public static final String MIN_COUNT_KEY = "minCount";
        public static final String MAX_COUNT_KEY = "maxCount";

        public static UnsmelteryRecipe readRecipeFromNBT (NBTTagCompound parent)
        {
            UnsmelteryRecipe ret = null;
            if (parent.hasKey (UnsmelteryRecipe.INGREDIENT_ITEM_KEY) && parent.hasKey (UnsmelteryRecipe.RESULT_ITEM_KEY)
                && parent.hasKey (UnsmelteryRecipe.MIN_COUNT_KEY) && parent.hasKey (UnsmelteryRecipe.MAX_COUNT_KEY))
            {
                ret = new UnsmelteryRecipe (
                    RecipeHandlerUtils.getInstance ()
                        .readItemStackFromNBT (parent.getCompoundTag (UnsmelteryRecipe.INGREDIENT_ITEM_KEY)),
                    RecipeHandlerUtils.getInstance ()
                        .readItemStackFromNBT (parent.getCompoundTag (UnsmelteryRecipe.RESULT_ITEM_KEY)),
                    parent.getInteger (UnsmelteryRecipe.MIN_COUNT_KEY),
                    parent.getInteger (UnsmelteryRecipe.MAX_COUNT_KEY));
            }
            return ret;
        }

        public void writeRecipeToNBT (NBTTagCompound parent)
        {
            NBTTagCompound ingredientItem = new NBTTagCompound ();
            NBTTagCompound resultItem = new NBTTagCompound ();

            RecipeHandlerUtils.getInstance ().writeItemStackToNBT (this.ingredientItem, ingredientItem);
            RecipeHandlerUtils.getInstance ().writeItemStackToNBT (this.resultItem, resultItem);

            parent.setTag (UnsmelteryRecipe.INGREDIENT_ITEM_KEY, ingredientItem);
            parent.setTag (UnsmelteryRecipe.RESULT_ITEM_KEY, resultItem);
            parent.setInteger (UnsmelteryRecipe.MIN_COUNT_KEY, minCount);
            parent.setInteger (UnsmelteryRecipe.MAX_COUNT_KEY, maxCount);
        }

    }

    public class UnsmelteryRecipeHandlerRenderer
        implements RecipeHandlerRenderer<UnsmelteryRecipeHandler, UnsmelteryRecipe>
    {

        @Override
        public void renderBackground (UnsmelteryRecipeHandler handler, UnsmelteryRecipe recipe, int cycleticks)
        {
            RecipeHandlerRendererUtils.getInstance ().bindTexture ("lotr:gui/unsmelter.png");
            RecipeHandlerRendererUtils.getInstance ().drawTexturedRectangle (0, 0, 5, 11, 166, 65);
            RecipeHandlerRendererUtils.getInstance ().drawRectangle (106, 19, 26, 26, 0xFFC6C6C6);
            RecipeHandlerRendererUtils.getInstance ().drawTexturedRectangle (106, 13, 51, 6, 22, 28);
            RecipeHandlerRendererUtils.getInstance ().drawTexturedRectangle (131, 13, 51, 6, 22, 28);
            RecipeHandlerRendererUtils.getInstance ().drawProgressBar (74, 23, 176, 14, 24, 16, cycleticks % 48 / 48.0f,
                EnumProgressBarDirection.INCREASE_RIGHT);
            RecipeHandlerRendererUtils.getInstance ().drawProgressBar (51, 25, 176, 0, 14, 14, cycleticks % 48 / 48.0f,
                EnumProgressBarDirection.DECREASE_DOWN);
        }

        @Override
        public void renderForeground (UnsmelteryRecipeHandler handler, UnsmelteryRecipe recipe, int cycleticks)
        {
            RecipeHandlerRendererUtils.getInstance ().drawText (
                StatCollector.translateToLocal ("neiLotr.handler.unsmeltery.minLabel"), 111, 14, 4210752, false);
            RecipeHandlerRendererUtils.getInstance ().drawText (
                StatCollector.translateToLocal ("neiLotr.handler.unsmeltery.maxLabel"), 135, 14, 4210752, false);
            RecipeHandlerRendererUtils.getInstance ().drawText (Integer.toString (recipe.getMinCount ()), 122, 33,
                0xFFFFFFFF, true);
            RecipeHandlerRendererUtils.getInstance ().drawText (Integer.toString (recipe.getMaxCount ()), 147, 33,
                0xFFFFFFFF, true);
        }

    }

    public class UnsmelteryRecipeHandlerRecipeViewer
        extends AbstractRecipeViewer<UnsmelteryRecipe, UnsmelteryRecipeHandler>
    {

        private final Collection<Class<? extends GuiContainer>> supportedGuiClasses = new ArrayList<> ();

        public UnsmelteryRecipeHandlerRecipeViewer (UnsmelteryRecipeHandler handler)
        {
            super (handler);
            supportedGuiClasses.addAll (AbstractRecipeViewer.RECIPE_HANDLER_GUIS);
            supportedGuiClasses.add (LOTRGuiUnsmeltery.class);
        }

        @Override
        public Collection<Class<? extends GuiContainer>> getSupportedGUIClasses ()
        {
            return supportedGuiClasses;
        }

        @Override
        public Collection<UnsmelteryRecipe> getAllRecipes ()
        {
            return handler.getStaticRecipes ();
        }

        @Override
        public int getOffsetX (Class<? extends GuiContainer> guiClass)
        {
            return guiClass == LOTRGuiUnsmeltery.class ? 18 : 8;
        }

    }

}
