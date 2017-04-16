package micdoodle8.mods.galacticraft.planets.asteroids.inventory;

import micdoodle8.mods.galacticraft.core.inventory.SlotRocketBenchResult;
import micdoodle8.mods.galacticraft.planets.mars.util.RecipeUtilMars;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerSchematicAstroMiner extends Container
{
    public InventorySchematicAstroMiner craftMatrix = new InventorySchematicAstroMiner(this);
    public IInventory craftResult = new InventoryCraftResult();
    private final World world;

    public ContainerSchematicAstroMiner(InventoryPlayer par1InventoryPlayer, BlockPos pos)
    {
        this.world = par1InventoryPlayer.player.world;
        this.addSlotToContainer(new SlotRocketBenchResult(par1InventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 142, 72));
        int i;
        int j;
        int count = 1;

        // Miner top layer
        for (i = 0; i < 4; i++)
        {
            this.addSlotToContainer(new SlotSchematicAstroMiner(this.craftMatrix, count++, 27 + i * 18, 35, pos, par1InventoryPlayer.player));
        }

        // Miner mid layer
        for (i = 0; i < 5; i++)
        {
            this.addSlotToContainer(new SlotSchematicAstroMiner(this.craftMatrix, count++, 16 + i * 18, 53, pos, par1InventoryPlayer.player));
        }

        // Miner bottom layer
        for (i = 0; i < 3; i++)
        {
            this.addSlotToContainer(new SlotSchematicAstroMiner(this.craftMatrix, count++, 44 + i * 18, 71, pos, par1InventoryPlayer.player));
        }

        // Laser
        for (i = 0; i < 2; ++i)
        {
            this.addSlotToContainer(new SlotSchematicAstroMiner(this.craftMatrix, count++, 8 + i * 18, 77, pos, par1InventoryPlayer.player));
        }

        // Player inv:

        for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, j + i * 9 + 9, 8 + j * 18, 140 + i * 18 - 26));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, i, 8 + i * 18, 144 + 54 - 26));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    @Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);

        if (!this.world.isRemote)
        {
            for (int var2 = 1; var2 < this.craftMatrix.getSizeInventory(); ++var2)
            {
                final ItemStack var3 = this.craftMatrix.removeStackFromSlot(var2);

                if (!var3.isEmpty())
                {
                    par1EntityPlayer.entityDropItem(var3, 0.0F);
                }
            }
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        this.craftResult.setInventorySlotContents(0, RecipeUtilMars.findMatchingAstroMinerRecipe(this.craftMatrix));
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot var3 = this.inventorySlots.get(par1);

        if (var3 != null && var3.getHasStack())
        {
            final ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            boolean done = false;
            if (par1 <= 14)
            {
                if (!this.mergeItemStack(var4, 15, 51, false))
                {
                    return ItemStack.EMPTY;
                }

                var3.onSlotChange(var4, var2);
            }
            else
            {
                boolean valid = false;
                for (int i = 1; i < 15; i++)
                {
                    Slot testSlot = this.inventorySlots.get(i);
                    if (!testSlot.getHasStack() && testSlot.isItemValid(var2))
                    {
                        valid = true;
                        break;
                    }
                }
                if (valid)
                {
                    if (!this.mergeOneItemTestValid(var4, 1, 15, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else
                {
                    if (par1 >= 15 && par1 < 42)
                    {
                        if (!this.mergeItemStack(var4, 42, 51, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (par1 >= 42 && par1 < 51)
                    {
                        if (!this.mergeItemStack(var4, 15, 42, false))
                        {
                            return ItemStack.EMPTY;
                        }
                    }
                    else if (!this.mergeItemStack(var4, 15, 51, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (var4.isEmpty())
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

    protected boolean mergeOneItemTestValid(ItemStack par1ItemStack, int par2, int par3, boolean par4)
    {
        boolean flag1 = false;
        if (!par1ItemStack.isEmpty())
        {
            Slot slot;
            ItemStack slotStack;

            for (int k = par2; k < par3; k++)
            {
                slot = this.inventorySlots.get(k);
                slotStack = slot.getStack();

                if (slotStack.isEmpty() && slot.isItemValid(par1ItemStack))
                {
                    ItemStack stackOneItem = par1ItemStack.copy();
                    stackOneItem.setCount(1);
                    par1ItemStack.shrink(1);
                    slot.putStack(stackOneItem);
                    slot.onSlotChanged();
                    flag1 = true;
                    break;
                }
            }
        }

        return flag1;
    }
}
