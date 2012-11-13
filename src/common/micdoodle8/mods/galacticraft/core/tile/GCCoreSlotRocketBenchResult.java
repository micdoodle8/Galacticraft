package micdoodle8.mods.galacticraft.core.tile;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class GCCoreSlotRocketBenchResult extends Slot
{
    private final IInventory craftMatrix;
    private EntityPlayer thePlayer;
    private int field_48436_g;

    public GCCoreSlotRocketBenchResult(EntityPlayer par1EntityPlayer, IInventory par2IInventory, IInventory par3IInventory, int par4, int par5, int par6)
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
	public ItemStack decrStackSize(int par1)
    {
        if (this.getHasStack())
        {
            this.field_48436_g += Math.min(par1, this.getStack().stackSize);
        }

        return super.decrStackSize(par1);
    }

    protected void func_48435_a(ItemStack par1ItemStack, int par2)
    {
        this.field_48436_g += par2;
        this.func_48434_c(par1ItemStack);
    }

    protected void func_48434_c(ItemStack par1ItemStack)
    {
        par1ItemStack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_48436_g);
        this.field_48436_g = 0;
    }
    public void onPickupFromSlot(ItemStack par1ItemStack)
    {
        this.func_48434_c(par1ItemStack);

        for (int var2 = 0; var2 < this.craftMatrix.getSizeInventory(); ++var2)
        {
            ItemStack var3 = this.craftMatrix.getStackInSlot(var2);

            if (var3 != null)
            {
                this.craftMatrix.decrStackSize(var2, 1);

                if (var3.getItem().hasContainerItem())
                {
                    ItemStack var4 = new ItemStack(var3.getItem().getContainerItem());

                    if (!var3.getItem().doesContainerItemLeaveCraftingGrid(var3) || !this.thePlayer.inventory.addItemStackToInventory(var4))
                    {
                        if (this.craftMatrix.getStackInSlot(var2) == null)
                        {
                            this.craftMatrix.setInventorySlotContents(var2, var4);
                        }
                        else
                        {
                            this.thePlayer.dropPlayerItem(var4);
                        }
                    }
                }
            }
        }
    }
}
