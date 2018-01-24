package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.recipe.CircuitFabricatorRecipes;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCircuitFabricator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class ContainerCircuitFabricator extends Container
{
    private TileEntityCircuitFabricator tileEntity;

    public ContainerCircuitFabricator(InventoryPlayer playerInv, TileEntityCircuitFabricator tileEntity)
    {
        this.tileEntity = tileEntity;

        // Energy slot
        this.addSlotToContainer(new SlotSpecific(tileEntity, 0, 6, 69, IItemElectric.class));

        // Diamond
        ArrayList<ItemStack> slotContentsList = CircuitFabricatorRecipes.slotValidItems.get(0);
        this.addSlotToContainer(new SlotSpecific(tileEntity, 1, 15, 17, slotContentsList.toArray(new ItemStack[slotContentsList.size()])));

        // Silicon
        slotContentsList = CircuitFabricatorRecipes.slotValidItems.get(1);
        this.addSlotToContainer(new SlotSpecific(tileEntity, 2, 74, 46, slotContentsList.toArray(new ItemStack[slotContentsList.size()])));
        slotContentsList = CircuitFabricatorRecipes.slotValidItems.get(2);
        this.addSlotToContainer(new SlotSpecific(tileEntity, 3, 74, 64, slotContentsList.toArray(new ItemStack[slotContentsList.size()])));

        // Redstone
        slotContentsList = CircuitFabricatorRecipes.slotValidItems.get(3);
        this.addSlotToContainer(new SlotSpecific(tileEntity, 4, 122, 46, slotContentsList.toArray(new ItemStack[slotContentsList.size()])));

        // Optional
        slotContentsList = CircuitFabricatorRecipes.slotValidItems.get(4);
        this.addSlotToContainer(new SlotSpecific(tileEntity, 5, 145, 20, slotContentsList.toArray(new ItemStack[slotContentsList.size()])));

        // Smelting result
        this.addSlotToContainer(new SlotFurnaceOutput(playerInv.player, tileEntity, 6, 152, 86));

        int slot;

        for (slot = 0; slot < 3; ++slot)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                this.addSlotToContainer(new Slot(playerInv, var4 + slot * 9 + 9, 8 + var4 * 18, 110 + slot * 18));
            }
        }

        for (slot = 0; slot < 9; ++slot)
        {
            this.addSlotToContainer(new Slot(playerInv, slot, 8 + slot * 18, 168));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.tileEntity.isUsableByPlayer(par1EntityPlayer);
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        super.onCraftMatrixChanged(par1IInventory);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift
     * clicking.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(par1);
        final int b = this.inventorySlots.size();

        if (slot != null && slot.getHasStack())
        {
            ItemStack var4 = slot.getStack();
            var2 = var4.copy();

            if (par1 < b - 36)
            {
                if (!this.mergeItemStack(var4, b - 36, b, true))
                {
                    return ItemStack.EMPTY;
                }

                if (par1 == 6)
                {
                    slot.onSlotChange(var4, var2);
                }
            }
            else
            {
                Item i = var4.getItem();
                if (EnergyUtil.isElectricItem(i))
                {
                    if (!this.mergeItemStack(var4, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (i == Items.DIAMOND)
                {
                    if (!this.mergeItemStack(var4, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (i == GCItems.basicItem && i.getDamage(var4) == 2)
                {
                    if (!this.mergeItemStack(var4, 2, 4, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (i == Items.REDSTONE)
                {
                    if (!this.mergeItemStack(var4, 4, 5, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (i == Items.REPEATER || i == new ItemStack(Blocks.REDSTONE_TORCH).getItem() || i == Items.DYE && i.getDamage(var4) == 4)
                {
                    if (!this.mergeItemStack(var4, 5, 6, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par1 < b - 9)
                {
                    if (!this.mergeItemStack(var4, b - 9, b, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.mergeItemStack(var4, b - 36, b - 9, false))
                {
                    return ItemStack.EMPTY;
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
