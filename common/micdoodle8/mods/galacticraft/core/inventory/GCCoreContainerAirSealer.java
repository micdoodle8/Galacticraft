package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenSealer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import universalelectricity.core.item.IItemElectric;
import universalelectricity.prefab.SlotSpecific;

public class GCCoreContainerAirSealer extends Container
{
	private final GCCoreTileEntityOxygenSealer sealer;

    public GCCoreContainerAirSealer(InventoryPlayer par1InventoryPlayer, GCCoreTileEntityOxygenSealer distributor)
    {
    	this.sealer = distributor;
        this.addSlotToContainer(new SlotSpecific(distributor, 0, 32, 27, IItemElectric.class));

        int var6;
        int var7;

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 46 + 78 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 46 + 136));
        }
    }

	@Override
	public boolean canInteractWith(EntityPlayer var1)
	{
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack var3 = null;
        final Slot var4 = (Slot)this.inventorySlots.get(par2);

        if (var4 != null && var4.getHasStack())
        {
            final ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (par2 < 27)
            {
                if (!this.mergeItemStack(var5, 27, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var5, 0, 27, false))
            {
                return null;
            }

            if (var5.stackSize == 0)
            {
                var4.putStack((ItemStack)null);
            }
            else
            {
                var4.onSlotChanged();
            }
        }

        return var3;
    }
}
