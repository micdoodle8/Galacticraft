package micdoodle8.mods.galacticraft.planets.mars.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.inventory.SlotSpecific;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemAtmosphericValve;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityMethaneSynthesizer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMethaneSynthesizer extends Container
{
    private final TileEntityMethaneSynthesizer tileEntity;

    public ContainerMethaneSynthesizer(InventoryPlayer par1InventoryPlayer, TileEntityMethaneSynthesizer tileEntity, EntityPlayer player)
    {
        this.tileEntity = tileEntity;

        // Electric Input Slot
        this.addSlotToContainer(new SlotSpecific(tileEntity, 0, 53, 53, IItemElectric.class));

        // Input slot - hydrogen
        this.addSlotToContainer(new Slot(tileEntity, 1, 7, 7));

        // Input slot - CO2
        this.addSlotToContainer(new Slot(tileEntity, 2, 28, 7));

        // Carbon slot
        this.addSlotToContainer(new SlotSpecific(tileEntity, 3, 28, 53, new ItemStack(MarsItems.carbonFragments, 1, 0)));

        // Output slot
        this.addSlotToContainer(new Slot(tileEntity, 4, 153, 7));
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 104 + var3 * 18 - 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 144));
        }

        tileEntity.openInventory(player);
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        this.tileEntity.closeInventory(entityplayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.tileEntity.isUsableByPlayer(par1EntityPlayer);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(par1);

        if (slot != null && slot.getHasStack())
        {
            final ItemStack var4 = slot.getStack();
            var2 = var4.copy();

            if (par1 < 5)
            {
                if (!this.mergeItemStack(var4, 5, 41, true))
                {
                    return ItemStack.EMPTY;
                }

                if (par1 == 2)
                {
                    slot.onSlotChange(var4, var2);
                }
            }
            else
            {
                if (EnergyUtil.isElectricItem(var4.getItem()))
                {
                    if (!this.mergeItemStack(var4, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    if (var4.getItem() instanceof ItemAtmosphericValve)
                    {
                        if (!this.mergeItemStack(var4, 2, 3, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (var4.getItem() == MarsItems.carbonFragments)
                    {
                        if (!this.mergeItemStack(var4, 3, 4, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (FluidUtil.isPartialContainer(var4, AsteroidsItems.methaneCanister))
                    {
                        if (!this.mergeItemStack(var4, 4, 5, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (par1 < 32)
                    {
                        if (!this.mergeItemStack(var4, 32, 41, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.mergeItemStack(var4, 5, 32, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (var4.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (var4.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, var4);
        }

        return var2;
    }
}
