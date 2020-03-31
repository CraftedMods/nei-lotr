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
package craftedMods.lotr.recipes.internal;

import craftedMods.recipes.api.*;
import lotr.common.enchant.LOTREnchantment;
import lotr.common.item.LOTRItemModifierTemplate;
import net.minecraft.item.ItemStack;

@RegisteredHandler
public class LOTRItemStackComparisonHandler implements ItemStackComparisonHandler
{

    @Override
    public Boolean areStacksOfSameTypeForCrafting (ItemStack stack1, ItemStack stack2)
    {
        if (stack1.getItem () instanceof LOTRItemModifierTemplate
            && stack2.getItem () instanceof LOTRItemModifierTemplate)
        {
            LOTREnchantment modifier = LOTRItemModifierTemplate.getModifier (stack1);
            if (modifier != null)
                return modifier == LOTRItemModifierTemplate.getModifier (stack2);
        }
        return null;
    }

}
