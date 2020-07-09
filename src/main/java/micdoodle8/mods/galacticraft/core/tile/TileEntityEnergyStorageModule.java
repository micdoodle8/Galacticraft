package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.item.IItemElectricBase;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.core.BlockNames;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachineBase;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseUniversalElectricalSource;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.ObjectHolder;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public abstract class TileEntityEnergyStorageModule extends TileBaseUniversalElectricalSource implements ISidedInventory, IInventoryDefaults, IConnector, IMachineSides
{
    public static class TileEntityEnergyStorageModuleT1 extends TileEntityEnergyStorageModule
    {
        @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.storageModule)
        public static TileEntityType<TileEntityEnergyStorageModuleT1> TYPE;

        public TileEntityEnergyStorageModuleT1()
        {
            super(TYPE);
            this.storage.setCapacity(BASE_CAPACITY);
            this.storage.setMaxExtract(300);
        }
    }

    public static class TileEntityEnergyStorageModuleT2 extends TileEntityEnergyStorageModule
    {
        @ObjectHolder(Constants.MOD_ID_CORE + ":" + BlockNames.storageModule)
        public static TileEntityType<TileEntityEnergyStorageModuleT2> TYPE;

        public TileEntityEnergyStorageModuleT2()
        {
            super(TYPE);
            setTier2();
        }
    }

    private final static float BASE_CAPACITY = 500000;
    private final static float TIER2_CAPACITY = 2500000;

    public final Set<PlayerEntity> playersUsing = new HashSet<PlayerEntity>();
    public int scaledEnergyLevel;
    public int lastScaledEnergyLevel;
    private float lastEnergy = 0;

    private boolean initialised = false;

    /*
     * @param tier: 1 = Electric Furnace  2 = Electric Arc Furnace
     */
    public TileEntityEnergyStorageModule(TileEntityType<?> type)
    {
        super(type);
        this.initialised = true;
        this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
    }

    protected void setTier2()
    {
        this.storage.setCapacity(TIER2_CAPACITY);
        this.storage.setMaxExtract(1800);
        this.setTierGC(2);
    }

    @Override
    public void tick()
    {
        if (!this.initialised)
        {
            //for version tick compatibility
//            Block b = this.world.getBlockState(this.getPos()).getBlock();
//            if (b == GCBlocks.machineBase)
//            {
//                this.world.setBlockState(this.getPos(), GCBlocks.machineTiered.getDefaultState(), 2);
//            }
//            else if (metadata >= 8)
//            {
//                this.setTier2();
//            }
            this.initialised = true;
        }

        float energy = this.storage.getEnergyStoredGC();
        if (this.getTierGC() == 1 && !this.world.isRemote)
        {
            if (this.lastEnergy - energy > this.storage.getMaxExtract() - 1)
            {
                //Deplete faster if being drained at maximum output
                this.storage.extractEnergyGC(25, false);
            }
        }
        this.lastEnergy = energy;

        super.tick();

        this.scaledEnergyLevel = (int) Math.floor((this.getEnergyStoredGC() + 49) * 16 / this.getMaxEnergyStoredGC());

        if (this.scaledEnergyLevel != this.lastScaledEnergyLevel)
        {
//            this.world.notifyLightSet(this.getPos());
            world.getChunkProvider().getLightManager().checkBlock(this.getPos());
        }

        if (!this.world.isRemote)
        {
            this.recharge(this.getInventory().get(0));
            this.discharge(this.getInventory().get(1));
        }

        if (!this.world.isRemote)
        {
            this.produce();
        }

        this.lastScaledEnergyLevel = this.scaledEnergyLevel;
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        super.read(nbt);
        if (this.storage.getEnergyStoredGC() > BASE_CAPACITY)
        {
            this.setTier2();
            this.initialised = true;
        }
        else
        {
            this.initialised = false;
        }

        this.readMachineSidesFromNBT(nbt);  //Needed by IMachineSides
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        if (this.tierGC == 1 && this.storage.getEnergyStoredGC() > BASE_CAPACITY)
        {
            this.storage.setEnergyStored(BASE_CAPACITY);
        }

        super.write(nbt);

        this.addMachineSidesToNBT(nbt);  //Needed by IMachineSides

        return nbt;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public int[] getSlotsForFace(Direction side)
    {
        return new int[0];
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return ItemElectricBase.isElectricItem(itemstack.getItem());
    }

//    @Override
//    public int[] getAccessibleSlotsFromSide(int slotID)
//    {
//        return new int[] { 0, 1 };
//    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, Direction side)
    {
        if (itemstack.getItem() instanceof IItemElectricBase)
        {
            if (slotID == 0)
            {
                return ((IItemElectricBase) itemstack.getItem()).getTransfer(itemstack) > 0;
            }
            else if (slotID == 1)
            {
                return ((IItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) > 0;
            }
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, Direction side)
    {
        if (itemstack.getItem() instanceof IItemElectricBase)
        {
            if (slotID == 0)
            {
                return ((IItemElectricBase) itemstack.getItem()).getTransfer(itemstack) <= 0;
            }
            else if (slotID == 1)
            {
                return ((IItemElectricBase) itemstack.getItem()).getElectricityStored(itemstack) <= 0 || this.getEnergyStoredGC() >= this.getMaxEnergyStoredGC();
            }
        }

        return false;

    }

    @Override
    public EnumSet<Direction> getElectricalInputDirections()
    {
        return EnumSet.of(getElectricInputDirection());
    }

    @Override
    public EnumSet<Direction> getElectricalOutputDirections()
    {
        return EnumSet.of(getElectricOutputDirection());
    }

    @Override
    public boolean canConnect(Direction direction, NetworkType type)
    {
        if (direction == null || type != NetworkType.POWER)
        {
            return false;
        }

        return getElectricalInputDirections().contains(direction) || getElectricalOutputDirections().contains(direction);
    }

    @Override
    public Direction getFront()
    {
        return BlockMachineBase.getFront(this.world.getBlockState(getPos()));
    }

    @Override
    public Direction getElectricInputDirection()
    {
        switch (this.getSide(MachineSide.ELECTRIC_IN))
        {
        case LEFT:
            return getFront().rotateY();
        case REAR:
            return getFront().getOpposite();
        case TOP:
            return Direction.UP;
        case BOTTOM:
            return Direction.DOWN;
        case RIGHT:
        default:
            return getFront().rotateYCCW();
        }
    }

    @Override
    public Direction getElectricOutputDirection()
    {
        switch (this.getSide(MachineSide.ELECTRIC_OUT))
        {
        case RIGHT:
            return getFront().rotateYCCW();
        case REAR:
            return getFront().getOpposite();
        case TOP:
            return Direction.UP;
        case BOTTOM:
            return Direction.DOWN;
        case LEFT:
        default:
            return getFront().rotateY();
        }
    }

    //------------------
    //Added these methods and field to implement IMachineSides properly 
    //------------------
    @Override
    public MachineSide[] listConfigurableSides()
    {
        return new MachineSide[]{MachineSide.ELECTRIC_IN, MachineSide.ELECTRIC_OUT};
    }

    @Override
    public Face[] listDefaultFaces()
    {
        return new Face[]{Face.RIGHT, Face.LEFT};
    }

    private MachineSidePack[] machineSides;

    @Override
    public synchronized MachineSidePack[] getAllMachineSides()
    {
        if (this.machineSides == null)
        {
            this.initialiseSides();
        }

        return this.machineSides;
    }

    @Override
    public void setupMachineSides(int length)
    {
        this.machineSides = new MachineSidePack[length];
    }

    @Override
    public void onLoad()
    {
        this.clientOnLoad();
    }

    @Override
    public IMachineSidesProperties getConfigurationType()
    {
        return IMachineSidesProperties.TWOFACES_HORIZ;
    }
    //------------------END OF IMachineSides implementation
}
