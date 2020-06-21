package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.tile.TileEntityRefinery;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerRefinery extends Container
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.REFINERY)
    public static ContainerType<ContainerRefinery> TYPE;

    private final TileEntityRefinery refinery;

    public ContainerRefinery(int containerId, PlayerInventory playerInv, TileEntityRefinery refinery)
    {
        super(TYPE, containerId);
        this.refinery = refinery;

        // Electric Input Slot
        this.addSlot(new SlotSpecific(refinery, 0, 38, 51, IItemElectric.class));

        // To be smelted
        this.addSlot(new Slot(refinery, 1, 7, 7));

        // Smelting result
        this.addSlot(new Slot(refinery, 2, 153, 7));
        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(playerInv, var4 + var3 * 9 + 9, 8 + var4 * 18, 104 + var3 * 18 - 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlot(new Slot(playerInv, var3, 8 + var3 * 18, 144));
        }

        refinery.openInventory(playerInv.player);
    }

    public TileEntityRefinery getRefinery()
    {
        return refinery;
    }

    @Override
    public void onContainerClosed(PlayerEntity entityplayer)
    {
        super.onContainerClosed(entityplayer);
        this.refinery.closeInventory(entityplayer);
    }

    @Override
    public boolean canInteractWith(PlayerEntity par1EntityPlayer)
    {
        return this.refinery.isUsableByPlayer(par1EntityPlayer);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(par1);

        if (slot != null && slot.getHasStack())
        {
            final ItemStack var4 = slot.getStack();
            var2 = var4.copy();

            if (par1 < 3)
            {
                if (!this.mergeItemStack(var4, 3, 39, true))
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
                    if (FluidUtil.isOilContainerAny(var4))
                    {
                        if (!this.mergeItemStack(var4, 1, 2, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (FluidUtil.isPartialContainer(var4, GCItems.fuelCanister))
                    {
                        if (!this.mergeItemStack(var4, 2, 3, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (par1 < 30)
                    {
                        if (!this.mergeItemStack(var4, 30, 39, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.mergeItemStack(var4, 3, 30, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (var4.getCount() == 0)
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
