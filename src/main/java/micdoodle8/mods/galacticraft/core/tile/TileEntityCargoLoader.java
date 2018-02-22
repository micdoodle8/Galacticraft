package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.entity.ICargoEntity;
import micdoodle8.mods.galacticraft.api.entity.ICargoEntity.EnumCargoLoadingState;
import micdoodle8.mods.galacticraft.api.entity.ICargoEntity.RemovalResult;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.tile.ILockable;
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
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntityCargoLoader extends TileBaseElectricBlockWithInventory implements ISidedInventory, ILandingPadAttachable, ILockable
{
    private ItemStack[] containingItems = new ItemStack[15];
    public boolean outOfItems;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean targetFull;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean targetNoInventory;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean noTarget;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean locked;

    public ICargoEntity attachedFuelable;

    public TileEntityCargoLoader()
    {
        this.storage.setMaxExtract(45);
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.worldObj.isRemote)
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

                    if (this.ticks % (this.poweredByTierGC > 1 ? 9 : 15) == 0 && state == EnumCargoLoadingState.SUCCESS && !this.disabled && this.hasEnoughEnergyToRun)
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

        BlockVec3 thisVec = new BlockVec3(this);
        for (final EnumFacing dir : EnumFacing.VALUES)
        {
            final TileEntity pad = thisVec.getTileEntityOnSide(this.worldObj, dir);

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
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.containingItems = this.readStandardItemsFromNBT(par1NBTTagCompound);
        this.locked = par1NBTTagCompound.getBoolean("locked");
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        this.writeStandardItemsToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("locked", this.locked);
    }

    @Override
    protected ItemStack[] getContainingItems()
    {
        return this.containingItems;
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

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return side != this.getElectricInputDirection() ? new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 } : new int[] { };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
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

        for (count = 1; count < this.containingItems.length; count++)
        {
            ItemStack stackAt = this.containingItems[count];

            if (stackAt != null && stackAt.getItem() == stack.getItem() && stackAt.getItemDamage() == stack.getItemDamage() && stackAt.stackSize < stackAt.getMaxStackSize())
            {
                if (stackAt.stackSize + stack.stackSize <= stackAt.getMaxStackSize())
                {
                    if (doAdd)
                    {
                        this.containingItems[count].stackSize += stack.stackSize;
                        this.markDirty();
                    }

                    return EnumCargoLoadingState.SUCCESS;
                }
                else
                {
                    //Part of the stack can fill this slot but there will be some left over
                    int origSize = stackAt.stackSize;
                    int surplus = origSize + stack.stackSize - stackAt.getMaxStackSize();

                    if (doAdd)
                    {
                        this.containingItems[count].stackSize = stackAt.getMaxStackSize();
                        this.markDirty();
                    }

                    stack.stackSize = surplus;
                    if (this.addCargo(stack, doAdd) == EnumCargoLoadingState.SUCCESS)
                    {
                        return EnumCargoLoadingState.SUCCESS;
                    }

                    this.containingItems[count].stackSize = origSize;
                    return EnumCargoLoadingState.FULL;
                }
            }
        }

        for (count = 1; count < this.containingItems.length; count++)
        {
            ItemStack stackAt = this.containingItems[count];

            if (stackAt == null)
            {
                if (doAdd)
                {
                    this.containingItems[count] = stack;
                    this.markDirty();
                }

                return EnumCargoLoadingState.SUCCESS;
            }
        }

        return EnumCargoLoadingState.FULL;
    }

    public RemovalResult removeCargo(boolean doRemove)
    {
        for (int i = 1; i < this.containingItems.length; i++)
        {
            ItemStack stackAt = this.containingItems[i];

            if (stackAt != null)
            {
                ItemStack resultStack = stackAt.copy();
                resultStack.stackSize = 1;

                if (doRemove && --stackAt.stackSize <= 0)
                {
                    this.containingItems[i] = null;
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
        IBlockState state = this.worldObj.getBlockState(getPos()); 
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

    @Override
    public void clearLockedInventory()
    {
        for (int i = 1; i < 15; i++)
        {
            this.containingItems[i] = null;
        }
    }

    @Override
    public boolean getLocked()
    {
        return this.locked;
    }
}
