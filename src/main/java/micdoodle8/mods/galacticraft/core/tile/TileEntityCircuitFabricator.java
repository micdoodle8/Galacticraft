package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.recipe.CircuitFabricatorRecipes;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.items.ItemBasic;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMars;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;

public class TileEntityCircuitFabricator extends TileBaseElectricBlockWithInventory implements ISidedInventory
{
    public static final int PROCESS_TIME_REQUIRED = 300;
    @NetworkedField(targetSide = Side.CLIENT)
    public int processTicks = 0;
    private ItemStack producingStack = ItemStack.EMPTY;
    private long ticks;

    private NonNullList<ItemStack> stacks = NonNullList.withSize(7, ItemStack.EMPTY);

    public TileEntityCircuitFabricator()
    {
        this.storage.setMaxExtract(ConfigManagerCore.hardMode ? 40 : 20);
    }

    @Override
    public void update()
    {
        super.update();

        this.updateInput();

        if (!this.world.isRemote)
        {
            boolean updateInv = false;

            if (this.hasEnoughEnergyToRun)
            {
                if (this.canCompress())
                {
                    ++this.processTicks;

                    if (this.processTicks == TileEntityCircuitFabricator.PROCESS_TIME_REQUIRED)
                    {
                        this.world.playSound(null, this.getPos(), SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 0.3F, this.world.rand.nextFloat() * 0.1F + 0.9F);
                        this.processTicks = 0;
                        this.compressItems();
                        updateInv = true;
                    }
                }
                else
                {
                    this.processTicks = 0;
                }
            }
            else
            {
                this.processTicks = 0;
            }

            if (updateInv)
            {
                this.markDirty();
            }
        }

        this.ticks++;
    }

    public void updateInput()
    {
        this.producingStack = CircuitFabricatorRecipes.getOutputForInput(this.stacks.subList(1, 6));
    }

    private boolean canCompress()
    {
        if (this.producingStack.isEmpty())
        {
            return false;
        }
        if (this.stacks.get(6).isEmpty())
        {
            return true;
        }
        if (!this.stacks.get(6).isEmpty() && !this.stacks.get(6).isItemEqual(this.producingStack))
        {
            return false;
        }
        int result = this.stacks.get(6).isEmpty() ? 0 : this.stacks.get(6).getCount() + this.producingStack.getCount();
        return result <= this.getInventoryStackLimit() && result <= this.producingStack.getMaxStackSize();
    }

    public void compressItems()
    {
        if (this.canCompress())
        {
            ItemStack resultItemStack = this.producingStack.copy();
            if (ConfigManagerCore.quickMode)
            {
                if (resultItemStack.getItem() == GCItems.basicItem)
                {
                    if (resultItemStack.getItemDamage() == ItemBasic.WAFER_BASIC)
                    {
                        resultItemStack.setCount(5);
                    }
                    else if (resultItemStack.getItemDamage() == ItemBasic.WAFER_ADVANCED)
                    {
                        resultItemStack.setCount(2);
                    }
                }
            }

            if (this.stacks.get(6).isEmpty())
            {
                this.stacks.set(6, resultItemStack);
            }
            else if (this.stacks.get(6).isItemEqual(resultItemStack))
            {
                if (this.stacks.get(6).getCount() + resultItemStack.getCount() > 64)
                {
                    for (int i = 0; i < this.stacks.get(6).getCount() + resultItemStack.getCount() - 64; i++)
                    {
                        float var = 0.7F;
                        double dx = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        double dy = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        double dz = this.world.rand.nextFloat() * var + (1.0F - var) * 0.5D;
                        EntityItem entityitem = new EntityItem(this.world, this.getPos().getX() + dx, this.getPos().getY() + dy, this.getPos().getZ() + dz, new ItemStack(resultItemStack.getItem(), 1, resultItemStack.getItemDamage()));

                        entityitem.setPickupDelay(10);

                        this.world.spawnEntity(entityitem);
                    }
                    this.stacks.get(6).setCount(64);
                }
                else
                {
                    this.stacks.get(6).grow(resultItemStack.getCount());
                }
            }
        }

        for (int i = 1; i < 6; i++)
        {
            this.decrStackSize(i, 1);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.processTicks = nbt.getInteger("smeltingTicks");
        this.stacks = this.readStandardItemsFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setInteger("smeltingTicks", this.processTicks);
        this.writeStandardItemsToNBT(nbt, this.stacks);
        return nbt;
    }

    @Override
    protected NonNullList<ItemStack> getContainingItems()
    {
        return this.stacks;
    }

    @Override
    public String getName()
    {
        return GCCoreUtil.translate("tile.machine2.5.name");
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return null;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
    {
        if (slotID == 0)
        {
            return itemStack != null && ItemElectricBase.isElectricItem(itemStack.getItem());
        }

        if (slotID > 5)
        {
            return false;
        }

        ArrayList<ItemStack> list = CircuitFabricatorRecipes.slotValidItems.get(slotID - 1);

        for (ItemStack test : list)
        {
            if (test.isItemEqual(itemStack))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        if (side == EnumFacing.UP)
        {
            return new int[] { 6 };
        }

        //Offer whichever silicon slot has less silicon
        boolean siliconFlag = !this.stacks.get(2).isEmpty() && (this.stacks.get(3).isEmpty() || this.stacks.get(3).getCount() < this.stacks.get(2).getCount());
        return siliconFlag ? new int[] { 0, 1, 3, 4, 5 } : new int[] { 0, 1, 2, 4, 5 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack par2ItemStack, EnumFacing par3)
    {
        return slotID < 6 && this.isItemValidForSlot(slotID, par2ItemStack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack par2ItemStack, EnumFacing par3)
    {
        return slotID == 6;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.processTicks > 0;
    }

    @Override
    public EnumFacing getFront()
    {
        return this.world.getBlockState(getPos()).getValue(BlockMachineMars.FACING);
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        return getFront().rotateY();
    }
}
