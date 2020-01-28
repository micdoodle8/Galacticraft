package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine2;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachineBase;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class TileEntityOxygenStorageModule extends TileEntityOxygen implements IInventoryDefaults, ISidedInventory, IMachineSides
{
    public final Set<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();
    public int scaledOxygenLevel;
    private int lastScaledOxygenLevel;

    public static final int OUTPUT_PER_TICK = 500;
    public static final int OXYGEN_CAPACITY = 60000;

    public TileEntityOxygenStorageModule()
    {
        super("tile.machine2.6.name", OXYGEN_CAPACITY, 40);
        this.storage.setCapacity(0);
        this.storage.setMaxExtract(0);
        inventory = NonNullList.withSize(1, ItemStack.EMPTY);
    }

    @Override
    public void update()
    {
        if (!this.world.isRemote)
        {
            ItemStack oxygenItemStack = this.getStackInSlot(0);
            if (oxygenItemStack != null && oxygenItemStack.getItem() instanceof IItemOxygenSupply)
            {
                IItemOxygenSupply oxygenItem = (IItemOxygenSupply) oxygenItemStack.getItem();
                int oxygenDraw = (int) Math.floor(Math.min(this.oxygenPerTick * 2.5F, this.getMaxOxygenStored() - this.getOxygenStored()));
                this.setOxygenStored(getOxygenStored() + oxygenItem.discharge(oxygenItemStack, oxygenDraw));
                if (this.getOxygenStored() > this.getMaxOxygenStored())
                {
                    this.setOxygenStored(this.getOxygenStored());
                }
            }
        }

        super.update();

        this.scaledOxygenLevel = this.getScaledOxygenLevel(16);

        if (this.scaledOxygenLevel != this.lastScaledOxygenLevel)
        {
            this.world.notifyLightSet(this.getPos());
        }

        this.lastScaledOxygenLevel = this.scaledOxygenLevel;

        this.produceOxygen(getFront().rotateY().getOpposite());

        // if (!this.world.isRemote)
        // {
        // int gasToSend = Math.min(this.storedOxygen,
        // GCCoreTileEntityOxygenStorageModule.OUTPUT_PER_TICK);
        // GasStack toSend = new GasStack(GalacticraftCore.gasOxygen,
        // gasToSend);
        // this.storedOxygen -= GasTransmission.emitGasToNetwork(toSend, this,
        // this.getOxygenOutputDirection());
        //
        // Vector3 thisVec = new Vector3(this);
        // TileEntity tileEntity =
        // thisVec.modifyPositionFromSide(this.getOxygenOutputDirection()).getTileEntity(this.world);
        //
        // if (tileEntity instanceof IGasAcceptor)
        // {
        // if (((IGasAcceptor)
        // tileEntity).canReceiveGas(this.getOxygenInputDirection(),
        // GalacticraftCore.gasOxygen))
        // {
        // double sendingGas = 0;
        //
        // if (this.storedOxygen >=
        // GCCoreTileEntityOxygenStorageModule.OUTPUT_PER_TICK)
        // {
        // sendingGas = GCCoreTileEntityOxygenStorageModule.OUTPUT_PER_TICK;
        // }
        // else
        // {
        // sendingGas = this.storedOxygen;
        // }
        //
        // this.storedOxygen -= sendingGas - ((IGasAcceptor)
        // tileEntity).receiveGas(new GasStack(GalacticraftCore.gasOxygen, (int)
        // Math.floor(sendingGas)));
        // }
        // }
        // }

        this.lastScaledOxygenLevel = this.scaledOxygenLevel;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        this.readMachineSidesFromNBT(nbt);  //Needed by IMachineSides
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        this.addMachineSidesToNBT(nbt);  //Needed by IMachineSides
        return nbt;
    }

    @Override
    public EnumSet<EnumFacing> getElectricalInputDirections()
    {
        return EnumSet.noneOf(EnumFacing.class);
    }

    @Override
    public EnumSet<EnumFacing> getElectricalOutputDirections()
    {
        return EnumSet.noneOf(EnumFacing.class);
    }

    @Override
    public boolean shouldPullEnergy()
    {
        return false;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return false;
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        return null;
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean shouldUseOxygen()
    {
        return false;
    }

    @Override
    public int getOxygenProvide(EnumFacing direction)
    {
        return this.getOxygenOutputDirections().contains(direction) ? Math.min(TileEntityOxygenStorageModule.OUTPUT_PER_TICK, this.getOxygenStored()) : 0;
    }

    @Override
    public EnumFacing getFront()
    {
        return BlockMachineBase.getFront(this.world.getBlockState(getPos())); 
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        return slotID == 0 && itemstack != null && itemstack.getItem() instanceof IItemOxygenSupply;
    }

    //ISidedInventory
    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (slotID == 0 && this.isItemValidForSlot(slotID, itemstack))
        {
            return itemstack.getItemDamage() < itemstack.getItem().getMaxDamage();
        }
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (slotID == 0 && !itemstack.isEmpty())
        {
            return FluidUtil.isEmptyContainer(itemstack);
        }
        return false;
    }

    //IFluidHandler methods - to allow this to accept Liquid Oxygen
    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return super.canDrain(from, fluid);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        return super.drain(from, resource, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        return super.drain(from, maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
//        if (from.ordinal() == this.getBlockMetadata() - BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA + 2 && GalacticraftCore.isPlanetsLoaded)
//        {
//            //Can fill with LOX only
//            return fluid != null && fluid.getName().equals(AsteroidsModule.fluidLiquidOxygen.getName());
//        }

        return super.canFill(from, fluid);
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
//        int used = 0;
//
//        if (resource != null && this.canFill(from, resource.getFluid()))
//        {
//            used = (int) (this.receiveOxygen((int) Math.floor(resource.amount / Constants.LOX_GAS_RATIO), doFill) * Constants.LOX_GAS_RATIO);
//        }

        return super.fill(from, resource, doFill);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
//        FluidTankInfo[] tankInfo = new FluidTankInfo[] {};
//        int metaside = this.getBlockMetadata() - BlockMachine2.OXYGEN_STORAGE_MODULE_METADATA + 2;
//        int side = from.ordinal();
//
//        if (metaside == side && GalacticraftCore.isPlanetsLoaded)
//        {
//            tankInfo = new FluidTankInfo[] { new FluidTankInfo(new FluidStack(AsteroidsModule.fluidLiquidOxygen, (int) (this.getOxygenStored() * Constants.LOX_GAS_RATIO)), (int) (OXYGEN_CAPACITY * Constants.LOX_GAS_RATIO)) };
//        }
//        return tankInfo;
        return super.getTankInfo(from);
    }

    @Override
    public EnumSet<EnumFacing> getOxygenInputDirections()
    {
        EnumFacing dir;
        switch (this.getSide(MachineSide.ELECTRIC_IN))
        {
        case REAR:
            dir = getFront().getOpposite();
            break;
        case TOP:
            dir = EnumFacing.UP;
            break;
        case BOTTOM:
            dir = EnumFacing.DOWN;
            break;
        case RIGHT:
            dir = getFront().rotateYCCW();
            break;
        case LEFT:
        default:
            dir = getFront().rotateY();
        }
        return EnumSet.of(dir);
    }

    @Override
    public EnumSet<EnumFacing> getOxygenOutputDirections()
    {
        EnumFacing dir;
        switch (this.getSide(MachineSide.PIPE_OUT))
        {
        case REAR:
            dir = getFront().getOpposite();
            break;
        case TOP:
            dir = EnumFacing.UP;
            break;
        case BOTTOM:
            dir = EnumFacing.DOWN;
            break;
        case LEFT:
            dir = getFront().rotateY();
            break;
        case RIGHT:
        default:
            dir = getFront().rotateYCCW();
        }
        return EnumSet.of(dir);
    }

    //------------------
    //Added these methods and field to implement IMachineSides properly 
    //------------------
    @Override
    public MachineSide[] listConfigurableSides()
    {
        //Have to use Electric_In for compatibility with other BlockMachine2, as all use same blockstate
        return new MachineSide[] { MachineSide.ELECTRIC_IN, MachineSide.PIPE_OUT };
    }

    @Override
    public Face[] listDefaultFaces()
    {
        return new Face[] { Face.LEFT, Face.RIGHT };
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
        return BlockMachine2.MACHINESIDES_RENDERTYPE;
    }
    //------------------END OF IMachineSides implementation
}
