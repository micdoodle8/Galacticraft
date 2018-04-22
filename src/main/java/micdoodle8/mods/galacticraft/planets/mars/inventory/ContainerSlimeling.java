package micdoodle8.mods.galacticraft.planets.mars.inventory;

import micdoodle8.mods.galacticraft.core.inventory.SlotSpecific;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.Collections;

public class ContainerSlimeling extends Container
{
    private final InventorySlimeling slimelingInventory;

    public ContainerSlimeling(InventoryPlayer playerInventory, EntitySlimeling slimeling, EntityPlayer player)
    {
        this.slimelingInventory = slimeling.slimelingInventory;
        this.slimelingInventory.currentContainer = this;

        ContainerSlimeling.addSlots(this, playerInventory, slimeling);
        ContainerSlimeling.addAdditionalSlots(this, slimeling, this.slimelingInventory.getStackInSlot(1));

        this.slimelingInventory.openInventory(player);
    }

    public static void addSlots(ContainerSlimeling container, InventoryPlayer playerInventory, EntitySlimeling slimeling)
    {
        Slot slot = new SlotSpecific(slimeling.slimelingInventory, 1, 9, 30, new ItemStack(MarsItems.marsItemBasic, 1, 4));
        container.addSlotToContainer(slot);

        int var3;

        for (var3 = 0; var3 < 3; ++var3)
        {
            for (int var4 = 0; var4 < 9; ++var4)
            {
                slot = new Slot(playerInventory, var4 + var3 * 9 + 9, 8 + var4 * 18, 129 + var3 * 18);
                container.addSlotToContainer(slot);
            }
        }

        for (var3 = 0; var3 < 9; ++var3)
        {
            slot = new Slot(playerInventory, var3, 8 + var3 * 18, 187);
            container.addSlotToContainer(slot);
        }
    }
    
    public static void removeSlots(ContainerSlimeling container)
    {
        Collections.copy(container.inventoryItemStacks, container.inventoryItemStacks.subList(0, 37));
        container.inventorySlots = container.inventorySlots.subList(0, 37);
    }

    public static void addAdditionalSlots(ContainerSlimeling container, EntitySlimeling slimeling, ItemStack stack)
    {
        if (!stack.isEmpty() && stack.getItem() == MarsItems.marsItemBasic && stack.getItemDamage() == 4)
        {
        	//Note that if NEI is installed, this can be called by InventorySlimeling.setInventorySlotContents even if the container already has the slots
        	if (container.inventorySlots.size() < 63)
        	{
        		for (int var3 = 0; var3 < 3; ++var3)
        		{
        			for (int var4 = 0; var4 < 9; ++var4)
        			{
        				Slot slot = new Slot(slimeling.slimelingInventory, var4 + var3 * 9 + 2, 8 + var4 * 18, 54 + var3 * 18);
        				slot.slotNumber = container.inventorySlots.size();
        				container.inventorySlots.add(slot);
        				container.inventoryItemStacks.add(ItemStack.EMPTY);
        			}
        		}
        	}
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        this.slimelingInventory.closeInventory(entityplayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.slimelingInventory.isUsableByPlayer(par1EntityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot slot = (Slot) this.inventorySlots.get(par1);
        final int b = this.inventorySlots.size();

        if (slot != null && slot.getHasStack())
        {
            final ItemStack var4 = slot.getStack();
            var2 = var4.copy();

            if (b < 39)
            {
                if (par1 < b - 36)
                {
                    if (!this.mergeItemStack(var4, b - 36, b, true))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    if (var4.getItem() == MarsItems.marsItemBasic && var4.getItemDamage() == 4)
                    {
                        if (!this.mergeItemStack(var4, 0, 1, false))
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
            }
            else
            {
                //With inventory bag, slot 0 is a bag slot
                //Slots 1-36 are regular inventory (27 inventory, 9 hotbar)
                //Slots 37-63 are the inventory bag slots
                if (par1 == 0)
                {
                    return ItemStack.EMPTY;
                }

                if (par1 > 36)
                {
                    if (!this.mergeItemStack(var4, 1, 37, true))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    if (par1 < 28)
                    {
                        if (!this.mergeItemStack(var4, 37, 64, false))
                        {
                            if (!this.mergeItemStack(var4, 28, 37, false))
                            {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                    else if (!this.mergeItemStack(var4, 37, 64, false))
                    {
                        if (!this.mergeItemStack(var4, 1, 28, false))
                        {
                            return ItemStack.EMPTY;
                        }
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
