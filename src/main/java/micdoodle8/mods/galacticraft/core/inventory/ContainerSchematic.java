package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.recipe.ISchematicItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerSchematic extends Container
{
    public InventorySchematic craftMatrix = new InventorySchematic(this);
    public IInventory craftResult = new InventoryCraftResult();
    private final World world;

    public ContainerSchematic(InventoryPlayer par1InventoryPlayer, BlockPos pos)
    {
        this.world = par1InventoryPlayer.player.world;
        this.addSlotToContainer(new SlotSpecific(this.craftMatrix, 0, 80, 1, ISchematicItem.class));
        int var6;
        int var7;

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 111 + var6 * 18 - 59 + 16));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 169 - 59 + 16));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    @Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);

        if (!this.world.isRemote)
        {
            for (int var2 = 0; var2 < 1; ++var2)
            {
                final ItemStack var3 = this.craftMatrix.removeStackFromSlot(var2);

                if (!var3.isEmpty())
                {
                    par1EntityPlayer.entityDropItem(var3, 0.0F);
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack var3 = ItemStack.EMPTY;
        final Slot var4 = (Slot) this.inventorySlots.get(par2);

        if (var4 != null && var4.getHasStack())
        {
            final ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (par2 < 1)
            {
                if (!this.mergeItemStack(var5, 1, this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(var5, 0, 1, false))
            {
                return ItemStack.EMPTY;
            }

            if (var5.getCount() == 0)
            {
                var4.putStack(ItemStack.EMPTY);
            }
            else
            {
                var4.onSlotChanged();
            }
        }

        return var3;
    }
}
