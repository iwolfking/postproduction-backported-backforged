package moe.paring.createlogisticsbackport.foundation.util;

import net.minecraft.world.item.ItemStack;

public class ItemStackHelper {
    public static ItemStack copyWithCount(ItemStack stack, int count) {
        ItemStack copy = stack.copy();
        copy.setCount(count);
        return copy;
    }
}
