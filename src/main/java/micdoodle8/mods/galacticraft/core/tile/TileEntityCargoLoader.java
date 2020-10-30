package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.entity.ICargoEntity;
import micdoodle8.mods.galacticraft.api.entity.ICargoEntity.EnumCargoLoadingState;
import micdoodle8.mods.galacticraft.api.entity.ICargoEntity.RemovalResult;
import micdoodle8.mods.galacticraft.api.tile.ILandingPadAttachable;
import micdoodle8.mods.galacticraft.api.tile.ILockable;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.Annotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockCargoLoader;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCargoBase;
import micdoodle8.mods.galacticraft.core.inventory.ContainerCargoBase.ContainerCargoLoader;
import micdoodle8.mods.galacticraft.core.util.RecipeUtil;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.registries.ObjectHolder;

public class TileEntityCargoLoader extends TileEntityCargoBase implements ISidedInventory, ILandingPadAttachable, ILockable, INamedContainerProvider
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.cargoLoader)
    public static TileEntityType<TileEntityCargoLoader> TYPE;

    public boolean outOfItems;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean targetFull;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean targetNoInventory;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean noTarget;
    @NetworkedField(targetSide = LogicalSide.CLIENT)
    public boolean locked;

    public ICargoEntity attachedFuelable;

    public TileEntityCargoLoader()
    {
        super(TYPE);
        this.storage.setMaxExtract(45);
        this.inventory = NonNullList.withSize(15, ItemStack.EMPTY);
    }

    @Override
    public void tick()
    {
        super.tick();

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

                if (!stack.isEmpty())
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
        for (final Direction dir : Direction.values())
        {
            final TileEntity pad = thisVec.getTileEntityOnSide(this.getWorld(), dir);

            if (pad != null && pad instanceof TileEntityFake)
            {
                final TileEntity mainTile = ((TileEntityFake) pad).getMainBlockTile();

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
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        this.locked = nbt.getBoolean("locked");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        super.write(nbt);
        nbt.putBoolean("locked", this.locked);
        return nbt;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return side != this.getElectricInputDirection() ? new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14} : new int[]{};
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, Direction side)
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
    public boolean canExtractItem(int slotID, ItemStack itemstack, Direction side)
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

    public RemovalResult removeCargo(boolean doRemove)
    {
        for (int i = 1; i < this.getInventory().size(); i++)
        {
            ItemStack stackAt = this.getInventory().get(i);

            if (!stackAt.isEmpty())
            {
                ItemStack resultStack = stackAt.copy();
                resultStack.setCount(1);

                if (doRemove)
                {
                    stackAt.shrink(1);
                    if (stackAt.isEmpty())
                    {
                        this.getInventory().set(i, ItemStack.EMPTY);
                    }
                }

                if (doRemove)
                {
                    this.markDirty();
                }

                return new RemovalResult(EnumCargoLoadingState.SUCCESS, resultStack);
            }
        }

        return new RemovalResult(EnumCargoLoadingState.EMPTY, ItemStack.EMPTY);
    }

    //Used by Abandoned Base worldgen
    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        int count = 1;

        for (count = 1; count < this.getInventory().size(); count++)
        {
            ItemStack stackAt = this.getInventory().get(count);

            if (RecipeUtil.stacksMatch(stack, stackAt) && stackAt.getCount() < stackAt.getMaxStackSize())
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

        int size = this.getInventory().size();
        for (count = 1; count < size; count++)
        {
            ItemStack stackAt = this.getInventory().get(count);

            if (stackAt.isEmpty())
            {
                if (doAdd)
                {
                    this.getInventory().set(count, stack);
                    this.markDirty();
                }

                return EnumCargoLoadingState.SUCCESS;
            }
        }

        return EnumCargoLoadingState.FULL;
    }

    @Override
    public boolean canAttachToLandingPad(IWorldReader world, BlockPos pos)
    {
        return true;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.world.getBlockState(getPos());
        if (state.getBlock() instanceof BlockCargoLoader)
        {
            return (state.get(BlockCargoLoader.FACING));
        }
        return Direction.NORTH;
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return getFront().rotateY();
    }

    @Override
    public void clearLockedInventory()
    {
        for (int i = 1; i < 15; i++)
        {
            this.getInventory().set(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean getLocked()
    {
        return this.locked;
    }

    @Override
    public Container createMenu(int containerId, PlayerInventory playerInv, PlayerEntity player)
    {
        return new ContainerCargoLoader(containerId, playerInv, this);
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new TranslationTextComponent("container.cargo_loader");
    }
}
