package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.item.IItemOxygenSupply;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockMachine2;
import micdoodle8.mods.galacticraft.core.inventory.IInventoryDefaults;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class TileEntityOxygenStorageModule extends TileEntityOxygen implements IInventoryDefaults, ISidedInventory, IFluidHandler, IMachineSides
{
    public final Set<EntityPlayer> playersUsing = new HashSet<EntityPlayer>();
    public int scaledOxygenLevel;
    private int lastScaledOxygenLevel;

    public static final int OUTPUT_PER_TICK = 500;
    public static final int OXYGEN_CAPACITY = 60000;
    private ItemStack[] containingItems = new ItemStack[1];

    public TileEntityOxygenStorageModule()
    {
        super(OXYGEN_CAPACITY, 40);
        this.storage.setCapacity(0);
        this.storage.setMaxExtract(0);
    }

    @Override
    public void update()
    {
        if (!this.worldObj.isRemote)
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
            this.worldObj.notifyLightSet(this.getPos());
        }

        this.lastScaledOxygenLevel = this.scaledOxygenLevel;

        this.produceOxygen(getFront().rotateY().getOpposite());

        // if (!this.worldObj.isRemote)
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
        // thisVec.modifyPositionFromSide(this.getOxygenOutputDirection()).getTileEntity(this.worldObj);
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
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);

        final NBTTagList var2 = par1NBTTagCompound.getTagList("Items", 10);
        this.containingItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            final int var5 = var4.getByte("Slot") & 255;

            if (var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
        
        this.readMachineSidesFromNBT(par1NBTTagCompound);  //Needed by IMachineSides
    }

    @Override
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);

        final NBTTagList list = new NBTTagList();

        for (int var3 = 0; var3 < this.containingItems.length; ++var3)
        {
            if (this.containingItems[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.containingItems[var3].writeToNBT(var4);
                list.appendTag(var4);
            }
        }
        par1NBTTagCompound.setTag("Items", list);

        this.addMachineSidesToNBT(par1NBTTagCompound);  //Needed by IMachineSides
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
        return null;
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
        IBlockState state = this.worldObj.getBlockState(this.getPos());
        if (state.getBlock() != GCBlocks.machineBase2)
        {
            return EnumFacing.NORTH;
        }
        return state.getValue(BlockMachine2.FACING);
    }

    @Override
    public int getSizeInventory()
    {
        return this.containingItems.length;
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.containingItems[par1];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.containingItems[par1] != null)
        {
            ItemStack var3;

            if (this.containingItems[par1].stackSize <= par2)
            {
                var3 = this.containingItems[par1];
                this.containingItems[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.containingItems[par1].splitStack(par2);

                if (this.containingItems[par1].stackSize == 0)
                {
                    this.containingItems[par1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int par1)
    {
        if (this.containingItems[par1] != null)
        {
            final ItemStack var2 = this.containingItems[par1];
            this.containingItems[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.containingItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getName()
    {
        return GCCoreUtil.translate("tile.machine2.6.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.getPos()) == this && par1EntityPlayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
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
        if (slotID == 0 && itemstack != null)
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
    public MachineSidePack[] getAllMachineSides()
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
