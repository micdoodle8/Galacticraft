package micdoodle8.mods.galacticraft.core.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotRocketBenchResult extends Slot
{
    private final IInventory craftMatrix;
    private final EntityPlayer thePlayer;

    public SlotRocketBenchResult(EntityPlayer par1EntityPlayer, IInventory par2IInventory, IInventory par3IInventory, int par4, int par5, int par6)
    {
        super(par3IInventory, par4, par5, par6);
        this.thePlayer = par1EntityPlayer;
        this.craftMatrix = par2IInventory;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return false;
    }



    @Override
    public ItemStack onTake(EntityPlayer par1EntityPlayer, ItemStack stack)
    {
        for (int var2 = 0; var2 < this.craftMatrix.getSizeInventory(); ++var2)
        {
            final ItemStack var3 = this.craftMatrix.getStackInSlot(var2);

            if (!var3.isEmpty())
            {
                this.craftMatrix.decrStackSize(var2, 1);

                if (var3.getItem().hasContainerItem(var3))
                {
                    final ItemStack var4 = new ItemStack(var3.getItem().getContainerItem());

                    if (!this.thePlayer.inventory.addItemStackToInventory(var4))
                    {
                        if (this.craftMatrix.getStackInSlot(var2).isEmpty())
                        {
                            this.craftMatrix.setInventorySlotContents(var2, var4);
                        }
                        else
                        {
                            this.thePlayer.entityDropItem(var4, 0.0F);
                        }
                    }
                }
            }
        }

        return stack;
    }
}
