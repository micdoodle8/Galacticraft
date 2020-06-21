package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockOxygenCompressor;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.items.ItemOxygenTank;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ObjectHolder;

import java.util.EnumSet;

public class TileEntityOxygenDecompressor extends TileEntityOxygen implements IInventoryDefaults, ISidedInventory
{
    @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.oxygenDecompressor)
    public static TileEntityType<TileEntityOxygenDecompressor> TYPE;

    public static final int OUTPUT_PER_TICK = 100;
    private boolean usingEnergy = false;

    public TileEntityOxygenDecompressor()
    {
        super(TYPE, 1200, 0);
        inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    @Override
    public void tick()
    {
        super.tick();

        if (!this.world.isRemote)
        {
            this.usingEnergy = false;
            ItemStack tank1 = this.getInventory().get(0);

            if (!tank1.isEmpty() && this.hasEnoughEnergyToRun && this.getOxygenStored() < this.getMaxOxygenStored())
            {
                if (tank1.getItem() instanceof ItemOxygenTank && tank1.getDamage() < tank1.getMaxDamage())
                {
                    tank1.setDamage(tank1.getDamage() + 1); // TODO Test: Shouldn't damage only be done if receieve was successful?
                    this.receiveOxygen(1, IFluidHandler.FluidAction.EXECUTE);
                    this.usingEnergy = true;
                }
//                else if (tank1.getItem() instanceof ItemCanisterOxygenInfinite)
//                {
//                    this.receiveOxygen(1, IFluidHandler.FluidAction.EXECUTE);
//                    this.usingEnergy = true;
//                } TODO Oxygen canisters
            }

            this.produceOxygen();
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[] { 0, 1 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, Direction side)
    {
        if (this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return itemstack.getDamage() < itemstack.getMaxDamage();
            case 1:
                return ItemElectricBase.isElectricItemCharged(itemstack);
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, Direction side)
    {
        if (this.isItemValidForSlot(slotID, itemstack))
        {
            switch (slotID)
            {
            case 0:
                return itemstack.getDamage() == itemstack.getMaxDamage();
            case 1:
                return ItemElectricBase.isElectricItemEmpty(itemstack);
            default:
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        switch (slotID)
        {
        case 0:
            return itemstack.getItem() instanceof ItemOxygenTank;
        case 1:
            return ItemElectricBase.isElectricItem(itemstack.getItem());
        }

        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.usingEnergy;
    }

    @Override
    public Direction getFront()
    {
        BlockState state = this.world.getBlockState(getPos());
        if (state.getBlock() instanceof BlockOxygenCompressor)
        {
            return state.get(BlockOxygenCompressor.FACING).rotateY();
        }
        return Direction.NORTH;
    }

    @Override
    public Direction getElectricInputDirection()
    {
        return getFront();
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(1);
    }

    public Direction getOxygenOutputDirection()
    {
        return this.getElectricInputDirection().getOpposite();
    }

    @Override
    public EnumSet<Direction> getOxygenInputDirections()
    {
        return EnumSet.noneOf(Direction.class);
    }

    @Override
    public EnumSet<Direction> getOxygenOutputDirections()
    {
        return EnumSet.of(this.getElectricInputDirection().getOpposite());
    }

    @Override
    public boolean shouldPullOxygen()
    {
        return false;
    }

    @Override
    public boolean shouldUseOxygen()
    {
        return false;
    }

    @Override
    public int getOxygenProvide(Direction direction)
    {
        return this.getOxygenOutputDirections().contains(direction) ? Math.min(TileEntityOxygenDecompressor.OUTPUT_PER_TICK, this.getOxygenStored()) : 0;
    }
}
