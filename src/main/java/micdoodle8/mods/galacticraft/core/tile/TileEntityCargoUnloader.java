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
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntityCargoUnloader extends TileBaseElectricBlockWithInventory implements ISidedInventory, ILandingPadAttachable
{
    private NonNullList<ItemStack> stacks = NonNullList.withSize(15, ItemStack.EMPTY);
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean targetEmpty;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean targetNoInventory;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean noTarget;

    public ICargoEntity attachedFuelable;

    public TileEntityCargoUnloader()
    {
        this.storage.setMaxExtract(45);
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.world.isRemote)
        {
            if (this.ticks % 100 == 0)
            {
                this.checkForCargoEntity();
            }

            if (this.attachedFuelable != null)
            {
                this.noTarget = false;
                RemovalResult result = this.attachedFuelable.removeCargo(false);

                if (!result.resultStack.isEmpty())
                {
                    this.targetEmpty = false;

                    EnumCargoLoadingState state = this.addCargo(result.resultStack, false);

                    this.targetEmpty = state == EnumCargoLoadingState.EMPTY;

                    if (this.ticks % (this.poweredByTierGC > 1 ? 9 : 15) == 0 && state == EnumCargoLoadingState.SUCCESS && !this.disabled && this.hasEnoughEnergyToRun)
                    {
                        this.addCargo(this.attachedFuelable.removeCargo(true).resultStack, true);
                    }
                }
                else
                {
                    this.targetNoInventory = result.resultState == EnumCargoLoadingState.NOINVENTORY;
                    this.noTarget = result.resultState == EnumCargoLoadingState.NOTARGET;
                    this.targetEmpty = true;
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

        for (final EnumFacing dir : EnumFacing.values())
        {
            final TileEntity pad = new BlockVec3(this).getTileEntityOnSide(this.world, dir);

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
        this.writeStandardItemsToNBT(nbt, this.stacks);
        return nbt;
    }

    @Override
    protected NonNullList<ItemStack> getContainingItems()
    {
        return this.stacks;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }


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
    public String getName()
    {
        return GCCoreUtil.translate("container.cargounloader.name");
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return side != this.getElectricInputDirection() ? new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 } : new int[] { };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (side != this.getElectricInputDirection())
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

            if (stackAt.isEmpty())
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

    @Override
    public boolean canAttachToLandingPad(IBlockAccess world, BlockPos pos)
    {
        return true;
    }

    @Override
    public EnumFacing getFront()
    {
        IBlockState state = this.world.getBlockState(getPos()); 
        if (state.getBlock() instanceof BlockCargoLoader)
        {
            return (state.getValue(BlockCargoLoader.FACING));
        }
        return EnumFacing.NORTH;
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        return getFront().rotateY();
    }
}
