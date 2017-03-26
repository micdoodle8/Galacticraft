package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.entity.ICargoEntity;
import micdoodle8.mods.galacticraft.api.entity.ICargoEntity.EnumCargoLoadingState;
import micdoodle8.mods.galacticraft.api.entity.ICargoEntity.RemovalResult;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.blocks.BlockCargoLoader;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseElectricBlockWithInventory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntityCargoLoader extends TileBaseElectricBlockWithInventory implements ISidedInventory, ILandingPadAttachable
{
    private NonNullList<ItemStack> stacks = NonNullList.withSize(15, ItemStack.EMPTY);
    public boolean outOfItems;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean targetFull;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean targetNoInventory;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean noTarget;

    public ICargoEntity attachedFuelable;

    public TileEntityCargoLoader()
    {
        this.storage.setMaxExtract(45);
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.getWorld().isRemote)
        {
            if (this.ticks % 100 == 0)
            {
                this.checkForCargoEntity();
            }

            if (this.attachedFuelable != null)
            {
                this.noTarget = false;
                ItemStack stack = this.removeCargo(false).resultStack;

                if (stack != null)
                {
                    this.outOfItems = false;

                    EnumCargoLoadingState state = this.attachedFuelable.addCargo(stack, false);

                    this.targetFull = state == EnumCargoLoadingState.FULL;
                    this.targetNoInventory = state == EnumCargoLoadingState.NOINVENTORY;
                    this.noTarget = state == EnumCargoLoadingState.NOTARGET;

                    if (this.ticks % 15 == 0 && state == EnumCargoLoadingState.SUCCESS && !this.disabled && this.hasEnoughEnergyToRun)
                    {
                        this.attachedFuelable.addCargo(this.removeCargo(true).resultStack, true);
                    }
                }
                else
                {
                    this.outOfItems = true;
                }
            }
            else
            {
                this.noTarget = true;
            }
        }
    }

    public void checkForCargoEntity()
    {
        boolean foundFuelable = false;

        for (final EnumFacing dir : EnumFacing.VALUES)
        {
            final TileEntity pad = new BlockVec3(this).getTileEntityOnSide(this.getWorld(), dir);

            if (pad != null && pad instanceof TileEntityMulti)
            {
                final TileEntity mainTile = ((TileEntityMulti) pad).getMainBlockTile();

                if (mainTile instanceof ICargoEntity)
                {
                    this.attachedFuelable = (ICargoEntity) mainTile;
                    foundFuelable = true;
                    break;
                }
            }
            else if (pad != null && pad instanceof ICargoEntity)
            {
                this.attachedFuelable = (ICargoEntity) pad;
                foundFuelable = true;
                break;
            }
        }

        if (!foundFuelable)
        {
            this.attachedFuelable = null;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.stacks = this.readStandardItemsFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        this.writeStandardItemsToNBT(nbt, stacks);
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
        return GCCoreUtil.translate("container.cargoloader.name");
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return (this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] {};
//        return side != getBlockMetadata() + 2 ? new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 } : new int[] { };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
//        if (side != this.getBlockMetadata() + 2)
//        {
//            if (slotID == 0)
//            {
//                return ItemElectricBase.isElectricItem(itemstack.getItem());
//            }
//            else
//            {
//                return true;
//            }
//        }

        return false;
    }


    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        if (slotID == 0)
        {
            return ItemElectricBase.isElectricItem(itemstack.getItem());
        }
        else
        {
            return true;
        }
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return !this.getDisabled(0);
    }

    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        int count = 1;

        for (count = 1; count < this.stacks.size(); count++)
        {
            ItemStack stackAt = this.stacks.get(count);

            if (!stackAt.isEmpty() && stackAt.getItem() == stack.getItem() && stackAt.getItemDamage() == stack.getItemDamage() && stackAt.getCount() < stackAt.getMaxStackSize())
            {
                if (stackAt.getCount() + stack.getCount() <= stackAt.getMaxStackSize())
                {
                    if (doAdd)
                    {
                        stackAt.grow(stack.getCount());
                        this.markDirty();
                    }

                    return EnumCargoLoadingState.SUCCESS;
                }
                else
                {
                    //Part of the stack can fill this slot but there will be some left over
                    int origSize = stackAt.getCount();
                    int surplus = origSize + stack.getCount() - stackAt.getMaxStackSize();

                    if (doAdd)
                    {
                        stackAt.setCount(stackAt.getMaxStackSize());
                        this.markDirty();
                    }

                    stack.setCount(surplus);
                    if (this.addCargo(stack, doAdd) == EnumCargoLoadingState.SUCCESS)
                    {
                        return EnumCargoLoadingState.SUCCESS;
                    }

                    stackAt.setCount(origSize);
                    return EnumCargoLoadingState.FULL;
                }
            }
        }

        for (count = 1; count < this.stacks.size(); count++)
        {
            ItemStack stackAt = this.stacks.get(count);

            if (stackAt == null)
            {
                if (doAdd)
                {
                    this.stacks.set(count, stack);
                    this.markDirty();
                }

                return EnumCargoLoadingState.SUCCESS;
            }
        }

        return EnumCargoLoadingState.FULL;
    }

    public RemovalResult removeCargo(boolean doRemove)
    {
        for (int i = 1; i < this.stacks.size(); i++)
        {
            ItemStack stackAt = this.stacks.get(i);

            if (stackAt != null)
            {
                ItemStack resultStack = stackAt.copy();
                resultStack.setCount(1);

                if (doRemove)
                {
                    stackAt.shrink(1);
                    if (stackAt.isEmpty())
                    {
                        this.stacks.set(i, null);
                    }
                }

                if (doRemove)
                {
                    this.markDirty();
                }

                return new RemovalResult(EnumCargoLoadingState.SUCCESS, resultStack);
            }
        }

        return new RemovalResult(EnumCargoLoadingState.EMPTY, null);
    }

    @Override
    public boolean canAttachToLandingPad(IBlockAccess world, BlockPos pos)
    {
        return true;
    }

    @Override
    public EnumFacing getFront()
    {
        return this.getWorld().getBlockState(getPos()).getValue(BlockCargoLoader.FACING);
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        return getFront().rotateY();
    }
}
