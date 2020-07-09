package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.tile.TileEntityCrafting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;

public class SlotCraftingMemory extends CraftingResultSlot
{
    public IInventory tileEntity;

    public SlotCraftingMemory(PlayerEntity player, CraftingInventory craftingInventory, IInventory p_i45790_3_, int slotIndex, int xPosition, int yPosition, IInventory tile)
    {
        super(player, craftingInventory, p_i45790_3_, slotIndex, xPosition, yPosition);
        this.tileEntity = tile;
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack)
    {
        if (!stack.isEmpty() && this.tileEntity instanceof TileEntityCrafting)
        {
            ((TileEntityCrafting) this.tileEntity).updateMemory(stack);
        }
        return super.onTake(thePlayer, stack);
    }
}
