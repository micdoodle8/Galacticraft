package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GCCoreContainerRocketBench extends Container
{
    public GCCoreInventoryRocketBench craftMatrix = new GCCoreInventoryRocketBench(this);
    public IInventory craftResult = new InventoryCraftResult();
    private final World worldObj;

    public GCCoreContainerRocketBench(InventoryPlayer par1InventoryPlayer, int x, int y, int z)
    {
    	this.worldObj = par1InventoryPlayer.player.worldObj;
        this.addSlotToContainer(new GCCoreSlotRocketBenchResult(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 142, 69));
        int var6;
        int var7;
        
        // Cone
        this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 1, 48, -8, x, y, z, par1InventoryPlayer.player));

        // Body
        for (var6 = 0; var6 < 4; ++var6)
        {
            this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 2 + var6, 39, -6 + var6 * 18 + 16, x, y, z, par1InventoryPlayer.player));
        }
        
        // Body Right
        for (var6 = 0; var6 < 4; ++var6)
        {
            this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 6 + var6, 57, -6 + var6 * 18 + 16, x, y, z, par1InventoryPlayer.player));
        }

        // Left fins
        this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 10, 21, 64, x, y, z, par1InventoryPlayer.player));
        this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 11, 21, 82, x, y, z, par1InventoryPlayer.player));
        
        // Engine
        this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 12, 48, 82, x, y, z, par1InventoryPlayer.player));
        
        // Right fins
        this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 13, 75, 64, x, y, z, par1InventoryPlayer.player));
        this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 14, 75, 82, x, y, z, par1InventoryPlayer.player));

        // Addons
        for (int var8 = 0; var8 < 3; var8++)
        {
            this.addSlotToContainer(new GCCoreSlotRocketBench(this.craftMatrix, 15 + var8, 93 + var8 * 26, -15, x, y, z, par1InventoryPlayer.player));
        }
        
        // Player inv:

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 111 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 169));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    @Override
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
    {
        super.onCraftGuiClosed(par1EntityPlayer);

        if (!this.worldObj.isRemote)
        {
            for (int var2 = 1; var2 < 18; ++var2)
            {
                final ItemStack var3 = this.craftMatrix.getStackInSlotOnClosing(var2);

                if (var3 != null)
                {
                    par1EntityPlayer.dropPlayerItem(var3);
                }
            }
        }
    }
    
    @Override
	public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        this.craftResult.setInventorySlotContents(0, RecipeUtil.findMatchingSpaceshipRecipe(this.craftMatrix));
    }

    @Override
	public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    @Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = null;
        final Slot var3 = (Slot)this.inventorySlots.get(par1);

        if (var3 != null && var3.getHasStack())
        {
            final ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            if (par1 == 0)
            {
                if (!this.mergeItemStack(var4, 11, 46, true))
                {
                    return null;
                }

                var3.onSlotChange(var4, var2);
            }
            else if (par1 >= 10 && par1 < 37)
            {
                if (!this.mergeItemStack(var4, 37, 46, false))
                {
                    return null;
                }
            }
            else if (par1 >= 37 && par1 < 46)
            {
                if (!this.mergeItemStack(var4, 11, 37, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(var4, 11, 46, false))
            {
                return null;
            }

            if (var4.stackSize == 0)
            {
                var3.putStack((ItemStack)null);
            }
            else
            {
                var3.onSlotChanged();
            }

            if (var4.stackSize == var2.stackSize)
            {
                return null;
            }

            var3.onPickupFromSlot(par1EntityPlayer, var4);
        }

        return var2;
    }
}
