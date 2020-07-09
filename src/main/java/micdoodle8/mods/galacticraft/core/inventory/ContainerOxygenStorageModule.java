package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenStorageModule;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerOxygenStorageModule extends Container
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.OXYGEN_STORAGE_MODULE)
    public static ContainerType<ContainerOxygenStorageModule> TYPE;

    private final TileEntityOxygenStorageModule storageModule;

    public ContainerOxygenStorageModule(int containerId, PlayerInventory playerInv, TileEntityOxygenStorageModule storageModule)
    {
        super(TYPE, containerId);
        this.storageModule = storageModule;
        this.addSlot(new SlotSpecific(storageModule, 0, 17, 22, IItemOxygenSupply.class));

        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(playerInv, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlot(new Slot(playerInv, var3, 8 + var3 * 18, 142));
        }

        this.storageModule.playersUsing.add(playerInv.player);
    }

    public TileEntityOxygenStorageModule getStorageModule()
    {
        return storageModule;
    }

    @Override
    public void onContainerClosed(PlayerEntity entityplayer)
    {
        super.onContainerClosed(entityplayer);
        this.storageModule.playersUsing.remove(entityplayer);
    }

    @Override
    public boolean canInteractWith(PlayerEntity par1EntityPlayer)
    {
        return true;
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
        final int b = this.inventorySlots.size();

        if (slot != null && slot.getHasStack())
        {
            final ItemStack stack = slot.getStack();
            var2 = stack.copy();

            if (par1 < 1)
            {
                if (!this.mergeItemStack(stack, b - 36, b, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (stack.getItem() instanceof IItemOxygenSupply)
                {
                    if (!this.mergeItemStack(stack, 0, 1, false))
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
