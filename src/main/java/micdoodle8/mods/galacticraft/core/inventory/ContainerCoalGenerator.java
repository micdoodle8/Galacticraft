package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCoalGenerator;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ObjectHolder;

public class ContainerCoalGenerator extends Container
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + GCContainerNames.COAL_GENERATOR)
    public static ContainerType<ContainerCoalGenerator> TYPE;

    private final TileEntityCoalGenerator generator;

//    public ContainerCoalGenerator(int containerId, PlayerInventory playerInv)
//    {
//        this(containerId, playerInv, new Inventory(1));
//    }

    public ContainerCoalGenerator(int containerId, PlayerInventory playerInv, TileEntityCoalGenerator generator)
    {
        super(TYPE, containerId);
        this.generator = generator;
        this.addSlot(new SlotSpecific(generator, 0, 33, 34, new ItemStack(Items.COAL), new ItemStack(Item.getItemFromBlock(Blocks.COAL_BLOCK))));
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
    }

    public TileEntityCoalGenerator getGenerator()
    {
        return generator;
    }

    @Override
    public void onContainerClosed(PlayerEntity entityplayer)
    {
        super.onContainerClosed(entityplayer);
    }

    @Override
    public boolean canInteractWith(PlayerEntity par1EntityPlayer)
    {
        return this.generator.isUsableByPlayer(par1EntityPlayer);
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

            if (par1 != 0)
            {
                if (var4.getItem() == Items.COAL)
                {
                    if (!this.mergeItemStack(var4, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (par1 >= 28)
                {
                    if (!this.mergeItemStack(var4, 1, 28, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.mergeItemStack(var4, 28, 37, false))
                {
                    return ItemStack.EMPTY;
                }

            }
            else if (!this.mergeItemStack(var4, 1, 37, false))
            {
                return ItemStack.EMPTY;
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
