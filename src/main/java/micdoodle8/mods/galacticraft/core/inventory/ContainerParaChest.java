package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityParaChest;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerParaChest extends Container
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.PARACHEST)
    public static ContainerType<ContainerParaChest> TYPE;

    private final TileEntityParaChest paraChest;
    public int numRows;

    public ContainerParaChest(int containerId, PlayerInventory playerInv, TileEntityParaChest paraChest)
    {
        super(TYPE, containerId);
        this.paraChest = paraChest;
        this.numRows = (paraChest.getSizeInventory() - 3) / 9;
        paraChest.openInventory(playerInv.player);
        int i = (this.numRows - 4) * 18 + 19;
        int j;
        int k;

        for (j = 0; j < this.numRows; ++j)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlot(new Slot(paraChest, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        this.addSlot(new Slot(paraChest, paraChest.getSizeInventory() - 3, 125 + 0 * 18, (this.numRows == 0 ? 24 : 26) + this.numRows * 18));
        this.addSlot(new Slot(paraChest, paraChest.getSizeInventory() - 2, 125 + 1 * 18, (this.numRows == 0 ? 24 : 26) + this.numRows * 18));
        this.addSlot(new Slot(paraChest, paraChest.getSizeInventory() - 1, 75, (this.numRows == 0 ? 24 : 26) + this.numRows * 18));

        for (j = 0; j < 3; ++j)
        {
            for (k = 0; k < 9; ++k)
            {
                this.addSlot(new Slot(playerInv, k + j * 9 + 9, 8 + k * 18, (this.numRows == 0 ? 116 : 118) + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j)
        {
            this.addSlot(new Slot(playerInv, j, 8 + j * 18, (this.numRows == 0 ? 174 : 176) + i));
        }
    }

    public TileEntityParaChest getParaChest()
    {
        return paraChest;
    }

    @Override
    public boolean canInteractWith(PlayerEntity par1EntityPlayer)
    {
        return this.paraChest.isUsableByPlayer(par1EntityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity par1EntityPlayer, int par2)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(par2);
        final int b = this.inventorySlots.size();

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 < this.paraChest.getSizeInventory())
            {
                if (!this.mergeItemStack(itemstack1, b - 36, b, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.paraChest.getSizeInventory(), false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0)
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    @Override
    public void onContainerClosed(PlayerEntity par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);
        this.paraChest.closeInventory(par1EntityPlayer);
    }

    /**
     * Return this chest container's lower chest inventory.
     */
    public IInventory getparachestInventory()
    {
        return this.paraChest;
    }
}
