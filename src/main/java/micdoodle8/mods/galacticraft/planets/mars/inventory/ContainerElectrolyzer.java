package micdoodle8.mods.galacticraft.planets.mars.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.inventory.SlotSpecific;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityElectrolyzer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerElectrolyzer extends Container
{
    @ObjectHolder(Constants.MOD_ID_PLANETS + ":" + MarsContainerNames.ELECTROLYZER)
    public static ContainerType<ContainerElectrolyzer> TYPE;

    private final TileEntityElectrolyzer electrolyzer;

    public ContainerElectrolyzer(int containerId, PlayerInventory playerInv, TileEntityElectrolyzer electrolyzer)
    {
        super(TYPE, containerId);
        this.electrolyzer = electrolyzer;

        // Electric Input Slot
        this.addSlot(new SlotSpecific(this.electrolyzer, 0, 34, 50, IItemElectric.class));

        // Input slot
        this.addSlot(new Slot(this.electrolyzer, 1, 7, 7));

        // 2 output slots
        this.addSlot(new Slot(this.electrolyzer, 2, 132, 7));
        this.addSlot(new Slot(this.electrolyzer, 3, 153, 7));
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

        this.electrolyzer.openInventory(playerInv.player);
    }

    public TileEntityElectrolyzer getElectrolyzer()
    {
        return electrolyzer;
    }

    @Override
    public void onContainerClosed(PlayerEntity entityplayer)
    {
        super.onContainerClosed(entityplayer);
        this.electrolyzer.closeInventory(entityplayer);
    }

    @Override
    public boolean canInteractWith(PlayerEntity par1EntityPlayer)
    {
        return this.electrolyzer.isUsableByPlayer(par1EntityPlayer);
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

            if (par1 < 4)
            {
                if (!this.mergeItemStack(var4, 4, 40, true))
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
                    if (FluidUtil.isWaterContainer(var4))
                    {
                        if (!this.mergeItemStack(var4, 1, 2, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (FluidUtil.isEmptyGasContainer(var4))
                    {
                        if (!this.mergeItemStack(var4, 2, 4, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (par1 < 31)
                    {
                        if (!this.mergeItemStack(var4, 31, 40, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.mergeItemStack(var4, 4, 31, false))
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
