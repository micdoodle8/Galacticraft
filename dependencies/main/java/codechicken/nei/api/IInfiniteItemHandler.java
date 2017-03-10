package codechicken.nei.api;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public interface IInfiniteItemHandler {
    void onPickup(ItemStack heldItem);

    void onPlaceInfinite(ItemStack heldItem);

    boolean canHandleItem(ItemStack stack);

    boolean isItemInfinite(ItemStack stack);

    void replenishInfiniteStack(InventoryPlayer inventory, int slotNo);

    ItemStack getInfiniteItem(ItemStack typeStack);
}
