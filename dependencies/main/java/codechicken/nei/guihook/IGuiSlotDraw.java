package codechicken.nei.guihook;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public interface IGuiSlotDraw {
    void drawSlotItem(Slot slot, ItemStack stack, int x, int y, String quantity);
}
