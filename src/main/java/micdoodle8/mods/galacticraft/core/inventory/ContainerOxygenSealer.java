package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenSealer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerOxygenSealer extends Container
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.OXYGEN_SEALER)
    public static ContainerType<ContainerOxygenSealer> TYPE;

    private TileEntityOxygenSealer sealer;

    public ContainerOxygenSealer(int containerId, PlayerInventory playerInv, TileEntityOxygenSealer sealer)
    {
        super(TYPE, containerId);
        this.sealer = sealer;
        this.addSlot(new SlotSpecific(sealer, 0, 33, 27, IItemElectric.class));
        this.addSlot(new SlotSpecific(sealer, 1, 10, 27, IItemOxygenSupply.class));
        this.addSlot(new SlotSpecific(sealer, 2, 56, 27, new ItemStack(GCItems.ambientThermalController, 1)));

        int var6;
        int var7;

        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlot(new Slot(playerInv, var7 + var6 * 9 + 9, 8 + var7 * 18, 46 + 78 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlot(new Slot(playerInv, var6, 8 + var6 * 18, 46 + 136));
        }
    }

    public TileEntityOxygenSealer getSealer()
    {
        return sealer;
    }

    @Override
    public boolean canInteractWith(PlayerEntity var1)
    {
        return this.sealer.isUsableByPlayer(var1);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(par1);
        final int b = this.inventorySlots.size();

        if (slot != null && slot.getHasStack())
        {
            final ItemStack stack = slot.getStack();
            var2 = stack.copy();

            if (par1 <= 2)
            {
                if (!this.mergeItemStack(stack, b - 36, b, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (EnergyUtil.isElectricItem(stack.getItem()))
                {
                    if (!this.mergeItemStack(stack, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (stack.getItem() instanceof IItemOxygenSupply)
                {
                    if (!this.mergeItemStack(stack, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (stack.getItem() == GCItems.ambientThermalController)
                {
                    if (!this.mergeItemStack(stack, 2, 3, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    if (par1 < b - 9)
                    {
                        if (!this.mergeItemStack(stack, b - 9, b, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.mergeItemStack(stack, b - 36, b - 9, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stack.getCount() == 0)
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (stack.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(par1EntityPlayer, stack);
        }

        return var2;
    }
}
