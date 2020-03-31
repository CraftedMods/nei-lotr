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

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import craftedMods.lotr.recipes.api.utils.LOTRRecipeHandlerUtils;
import craftedMods.recipes.api.*;
import craftedMods.recipes.base.*;
import lotr.common.item.LOTRPoisonedDrinks;
import lotr.common.recipe.*;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;

@RegisteredHandler
public class LOTRVanillaCraftingTableSupportHandler implements VanillaCraftingTableRecipeHandlerSupport
{

    @Override
    public Pair<Collection<AbstractRecipe>, Boolean> undefinedRecipeTypeFound (IRecipe recipe)
    {
        if (recipe instanceof LOTRRecipesPoisonDrinks)
            return Pair.of (null, true);
        if (recipe instanceof LOTRRecipePoisonWeapon)
            return Pair.of (
                Arrays.asList (LOTRRecipeHandlerUtils.processPoisonWeaponRecipe ((LOTRRecipePoisonWeapon) recipe)),
                false);
        if (recipe instanceof LOTRRecipesTreasurePile)
            return processTreasurePileRecipe ((LOTRRecipesTreasurePile) recipe);
        return null;
    }

    private Pair<Collection<AbstractRecipe>, Boolean> processTreasurePileRecipe (LOTRRecipesTreasurePile recipe)
    {
        Pair<Block, Item> recipeItems = LOTRRecipeHandlerUtils.getTreasurePileRecipeItems (recipe);
        // TODO Only a subset of the treasure pile recipes is currently supported (pile
        // to ingot)
        return Pair.of (
            Arrays.asList (new ShapedRecipe (1, 1, new Object[]
            {new ItemStack (recipeItems.getLeft (), 1, 7)}, new ItemStack (recipeItems.getRight (), 4))),
            false);
    }

    @Override
    public int getComplicatedStaticRecipeDepth ()
    {
        return 1;
    }

    @Override
    public AbstractRecipe loadComplicatedStaticRecipe (ItemStack... stacks)
    {
        ItemStack stack = stacks[0];
        PoisonedDrinkRecipe recipe = null;
        if (LOTRPoisonedDrinks.canPoison (stack))
        {
            List<Object> ingredients = new ArrayList<> ();
            ingredients.add (stack.copy ());
            ingredients.add (LOTRRecipeHandlerUtils.getPoison ());
            ItemStack result = stack.copy ();
            LOTRPoisonedDrinks.setDrinkPoisoned (result, true);
            recipe = new PoisonedDrinkRecipe (ingredients, result);
        }
        return recipe;
    }

    @Override
    public Collection<AbstractRecipe> getDynamicCraftingRecipes (ItemStack result)
    {
        return Arrays.asList ();
    }

    @Override
    public Collection<AbstractRecipe> getDynamicUsageRecipes (ItemStack ingredient)
    {
        return Arrays.asList ();
    }

    @Override
    public boolean matches (ItemStack stack1, ItemStack stack2)
    {
        return LOTRPoisonedDrinks.isDrinkPoisoned (stack1) == LOTRPoisonedDrinks.isDrinkPoisoned (stack2);
    }

    public class PoisonedDrinkRecipe extends ShapelessRecipe
    {

        public PoisonedDrinkRecipe (List<?> ingredients, ItemStack result)
        {
            super (ingredients, result);
        }

        @Override
        public boolean produces (ItemStack result)
        {
            return LOTRPoisonedDrinks.isDrinkPoisoned (result) ? super.produces (result) : false;
        }

        @Override
        public boolean consumes (ItemStack ingredient)
        {
            return !LOTRPoisonedDrinks.isDrinkPoisoned (ingredient) ? super.consumes (ingredient) : false;
        }

    }

}
