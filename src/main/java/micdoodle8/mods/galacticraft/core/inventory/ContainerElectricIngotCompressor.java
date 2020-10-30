package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricIngotCompressor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerElectricIngotCompressor extends Container
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.ELECTRIC_INGOT_COMPRESSOR)
    public static ContainerType<ContainerElectricIngotCompressor> TYPE;

    private final TileEntityElectricIngotCompressor compressor;

    public ContainerElectricIngotCompressor(int containerId, PlayerInventory playerInv, TileEntityElectricIngotCompressor compressor)
    {
        super(TYPE, containerId);
        this.compressor = compressor;
        compressor.compressingCraftMatrix.eventHandler = this;

        for (int x = 0; x < 3; x++)
        {
            for (int y = 0; y < 3; y++)
            {
                this.addSlot(new Slot(compressor.compressingCraftMatrix, y + x * 3, 19 + y * 18, 18 + x * 18));
            }
        }

        // Battery Slot
        this.addSlot(new SlotSpecific(compressor, 0, 55, 75, IItemElectric.class));

        // Smelting result
        this.addSlot(new FurnaceResultSlot(playerInv.player, compressor, 1, 138, 30));
        this.addSlot(new FurnaceResultSlot(playerInv.player, compressor, 2, 138, 48));

        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlot(new Slot(playerInv, var4 + var3 * 9 + 9, 8 + var4 * 18, 117 + var3 * 18));
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            this.addSlot(new Slot(playerInv, var3, 8 + var3 * 18, 175));
        }
    }

    public TileEntityElectricIngotCompressor getCompressor()
    {
        return compressor;
    }

    @Override
    public void onContainerClosed(PlayerEntity entityplayer)
    {
        super.onContainerClosed(entityplayer);
    }

    @Override
    public boolean canInteractWith(PlayerEntity par1EntityPlayer)
    {
        return this.compressor.isUsableByPlayer(par1EntityPlayer);
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        super.onCraftMatrixChanged(par1IInventory);
        this.compressor.updateInput();
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        Slot var3 = this.inventorySlots.get(par1);

        if (var3 != null && var3.getHasStack())
        {
            ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            if (par1 <= 11)
            {
                if (!this.mergeItemStack(var4, 12, 48, true))
                {
                    return ItemStack.EMPTY;
                }

                if (par1 == 1 || par1 == 2)
                {
                    var3.onSlotChange(var4, var2);
                }
            }
            else
            {
                if (EnergyUtil.isElectricItem(var4.getItem()))
                {
                    if (!this.mergeItemStack(var4, 9, 10, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par1 < 39)
                {
                    if (!this.mergeItemStack(var4, 0, 9, false) && !this.mergeItemStack(var4, 39, 48, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.mergeItemStack(var4, 0, 9, false) && !this.mergeItemStack(var4, 12, 39, false))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (var4.getCount() == 0)
            {
                var3.putStack(ItemStack.EMPTY);
            }
            else
            {
                var3.onSlotChanged();
            }

            if (var4.getCount() == var2.getCount())
            {
                return ItemStack.EMPTY;
            }

            var3.onTake(par1EntityPlayer, var4);
        }

        return var2;
    }
}
