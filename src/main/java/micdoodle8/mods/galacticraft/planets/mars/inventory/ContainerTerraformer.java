package micdoodle8.mods.galacticraft.planets.mars.inventory;

import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.inventory.SlotSpecific;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTerraformer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ContainerTerraformer extends Container
{
    private final TileEntityTerraformer tileEntity;
    private static LinkedList<ItemStack> saplingList = null;

    public ContainerTerraformer(InventoryPlayer par1InventoryPlayer, TileEntityTerraformer tileEntity, EntityPlayer player)
    {
        this.tileEntity = tileEntity;

        this.addSlotToContainer(new SlotSpecific(tileEntity, 0, 25, 19, new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.BUCKET)));

        this.addSlotToContainer(new SlotSpecific(tileEntity, 1, 25, 39, IItemElectric.class));

        int var6;
        int var7;

        for (var6 = 0; var6 < 3; ++var6)
        {
            List<ItemStack> stacks = new ArrayList<ItemStack>();

            if (var6 == 0)
            {
                stacks.add(new ItemStack(Items.DYE, 1, 15));
            }
            else if (var6 == 1)
            {
                if (ContainerTerraformer.saplingList == null)
                {
                    initSaplingList();
                }

                stacks.addAll(ContainerTerraformer.saplingList);
            }
            else if (var6 == 2)
            {
                stacks.add(new ItemStack(Items.WHEAT_SEEDS));
            }

            for (var7 = 0; var7 < 4; ++var7)
            {
                this.addSlotToContainer(new SlotSpecific(tileEntity, var7 + var6 * 4 + 2, 25 + var7 * 18, 63 + var6 * 24, stacks.toArray(new ItemStack[stacks.size()])));
            }
        }

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 155 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 213));
        }

        tileEntity.openInventory(player);
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        this.tileEntity.closeInventory(entityplayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.tileEntity.isUsableByPlayer(par1EntityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(par1);
        final int b = this.inventorySlots.size();

        if (slot != null && slot.getHasStack())
        {
            final ItemStack var4 = slot.getStack();
            var2 = var4.copy();

            if (par1 < b - 36)
            {
                if (!this.mergeItemStack(var4, b - 36, b, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else
            {
                if (EnergyUtil.isElectricItem(var4.getItem()))
                {
                    if (!this.mergeItemStack(var4, 1, 2, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (var4.getItem() == Items.WATER_BUCKET)
                {
                    if (!this.mergeItemStack(var4, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (var4.getItem() == Items.DYE && var4.getItemDamage() == 15)
                {
                    if (!this.mergeItemStack(var4, 2, 6, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (this.getSlot(6).isItemValid(var4))
                {
                    if (!this.mergeItemStack(var4, 6, 10, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (var4.getItem() == Items.WHEAT_SEEDS)
                {
                    if (!this.mergeItemStack(var4, 10, 14, false))
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

    public static boolean isOnSaplingList(ItemStack stack)
    {
        if (saplingList == null)
        {
            initSaplingList();
        }

        for (ItemStack sapling : saplingList)
        {
            if (sapling.isItemEqual(stack))
            {
                return true;
            }
        }

        return false;
    }

    private static void initSaplingList()
    {
        ContainerTerraformer.saplingList = new LinkedList<>();

        for (Block b : Block.REGISTRY)
        {
            if (b instanceof BlockBush)
            {
                try
                {
                    Item item = Item.getItemFromBlock(b);
                    if (item != Items.AIR)
                    {
                        //item.getSubItems(item, null, subItemsList); - can't use because clientside only
                        ContainerTerraformer.saplingList.add(new ItemStack(item, 1, 0));
                        String basicName = item.getUnlocalizedName(new ItemStack(item, 1, 0));
                        for (int i = 1; i < 16; i++)
                        {
                            ItemStack testStack = new ItemStack(item, 1, i);
                            String testName = item.getUnlocalizedName(testStack);
                            if (testName.equals("") || testName.equals(basicName))
                            {
                                break;
                            }
                            ContainerTerraformer.saplingList.add(testStack);
                        }
                    }
                }
                catch (Exception e)
                {
                }
            }
        }
    }
}
