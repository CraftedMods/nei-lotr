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
