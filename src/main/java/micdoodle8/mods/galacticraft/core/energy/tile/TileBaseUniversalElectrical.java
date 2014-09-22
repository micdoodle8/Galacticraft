package micdoodle8.mods.galacticraft.core.energy.tile;

import buildcraft.api.mj.IBatteryObject;
import buildcraft.api.mj.MjAPI;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import cpw.mods.fml.common.eventhandler.Event;
import ic2.api.item.IElectricItem;
import ic2.api.item.ISpecialElectricItem;
import mekanism.api.energy.EnergizedItemManager;
import mekanism.api.energy.IEnergizedItem;
import micdoodle8.mods.galacticraft.api.item.ElectricItemHelper;
import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.api.transmission.tile.IElectrical;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.galacticraft.core.tile.ReceiverMode;
import micdoodle8.mods.miccore.Annotations.AltForVersion;
import micdoodle8.mods.miccore.Annotations.RuntimeInterface;
import micdoodle8.mods.miccore.Annotations.VersionSpecific;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.EnumSet;

public abstract class TileBaseUniversalElectrical extends EnergyStorageTile //implements IElectrical, IElectricalStorage
{
    protected boolean isAddedToEnergyNet;
    protected Object powerHandlerBC;

    //	@NetworkedField(targetSide = Side.CLIENT)
    //	public float energyStored = 0;
    private float IC2surplusInGJ = 0F;

    @Override
    public double getPacketRange()
    {
        return 12.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 3;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }

    public EnumSet<ForgeDirection> getElectricalInputDirections()
    {
        return EnumSet.allOf(ForgeDirection.class);
    }

    public EnumSet<ForgeDirection> getElectricalOutputDirections()
    {
        return EnumSet.noneOf(ForgeDirection.class);
    }

    @Override
    public float getRequest(ForgeDirection direction)
    {
        if (this.getElectricalInputDirections().contains(direction))
        	return super.getRequest(direction);
        
        return 0F;
    }

    @Override
    public float receiveElectricity(ForgeDirection from, float receive, int tier, boolean doReceive)
    {
        if (this.getElectricalInputDirections().contains(from))
        {
        	return super.receiveElectricity(from, receive, tier, doReceive);
        }
        
        return 0F;
    }
    
    //	@Override
    //	public float receiveElectricity(ForgeDirection from, ElectricityPack receive, boolean doReceive)
    //	{
    //		if (from == ForgeDirection.UNKNOWN || this.getElectricalInputDirections().contains(from))
    //		{
    //			if (!doReceive)
    //			{
    //				return this.getRequest(from);
    //			}
    //
    //			return this.receiveElectricity(receive, doReceive);
    //		}
    //
    //		return 0F;
    //	}

    /**
     * A non-side specific version of receiveElectricity for you to optionally
     * use it internally.
     */
    //	public float receiveElectricity(ElectricityPack receive, boolean doReceive)
    //	{
    //		if (receive != null)
    //		{
    //			float prevEnergyStored = this.getEnergyStored();
    //			float newStoredEnergy = Math.min(this.getEnergyStored() + receive.getWatts(), this.getMaxEnergyStored());
    //
    //			if (doReceive)
    //			{
    //				this.setEnergyStored(newStoredEnergy);
    //			}
    //
    //			return Math.max(newStoredEnergy - prevEnergyStored, 0);
    //		}
    //
    //		return 0;
    //	}

    //	public float receiveElectricity(float energy, boolean doReceive)
    //	{
    //		return this.receiveElectricity(ElectricityPack.getFromWatts(energy, this.getVoltage()), doReceive);
    //	}

    //	@Override
    //	public void setEnergyStored(float energy)
    //	{
    //		this.energyStored = Math.max(Math.min(energy, this.getMaxEnergyStored()), 0);
    //	}

    //	@Override
    //	public float getEnergyStored()
    //	{
    //		return this.energyStored;
    //	}

    //	public boolean canConnect(ForgeDirection direction, NetworkType type)
    //	{
    //		if (direction == null || direction.equals(ForgeDirection.UNKNOWN) || type != NetworkType.POWER)
    //		{
    //			return false;
    //		}
    //
    //		return this.getElectricalInputDirections().contains(direction) || this.getElectricalOutputDirections().contains(direction);
    //	}

    //	@Override
    //	public float getVoltage()
    //	{
    //		return 0.120F;
    //	}
    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        //		this.energyStored = nbt.getFloat("energyStored");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        //		nbt.setFloat("energyStored", this.energyStored);
    }

    /**
     * Discharges electric item.
     */
    @VersionSpecific(version = "[1.7.2]")
    public void discharge(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            Item item = itemStack.getItem();
            float energyToDischarge = this.getRequest(ForgeDirection.UNKNOWN);

            if (item instanceof IItemElectric)
            {
                this.storage.receiveEnergyGC(ElectricItemHelper.dischargeItem(itemStack, energyToDischarge));
            }
            else if (EnergyConfigHandler.isIndustrialCraft2Loaded())
            {
                if (item instanceof IElectricItem)
                {
                    IElectricItem electricItem = (IElectricItem) item;
                    if (electricItem.canProvideEnergy(itemStack))
                    {
                        double result = 0;
                        int energyDischargeIC2 = (int) (energyToDischarge * EnergyConfigHandler.TO_IC2_RATIO);
                        try
                        {
                            Class<?> clazz = Class.forName("ic2.api.item.IElectricItemManager");
                            Method dischargeMethod = clazz.getMethod("discharge", ItemStack.class, int.class, int.class, boolean.class, boolean.class);
                            result = (Double) dischargeMethod.invoke(ic2.api.item.ElectricItem.manager, itemStack, energyDischargeIC2, 4, false, false);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        float energyDischarged = (float) result * EnergyConfigHandler.IC2_RATIO;
                        this.storage.receiveEnergyGC(energyDischarged);
                    }
                }
                else if (item instanceof ISpecialElectricItem)
                {
                    ISpecialElectricItem electricItem = (ISpecialElectricItem) item;
                    if (electricItem.canProvideEnergy(itemStack))
                    {
                        double result = 0;
                        int energyDischargeIC2 = (int) (energyToDischarge * EnergyConfigHandler.TO_IC2_RATIO);
                        //Do this by reflection:
                        //result = electricItem.getManager(itemStack).discharge(itemStack, energyDischargeIC2, 4, false, false, false)
                        try
                        {
                            Class<?> clazz = Class.forName("ic2.api.item.IElectricItemManager");
                            Method dischargeMethod = clazz.getMethod("discharge", ItemStack.class, int.class, int.class, boolean.class, boolean.class);
                            result = (Double) dischargeMethod.invoke(electricItem.getManager(itemStack), itemStack, energyDischargeIC2, 4, false, false);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        float energyDischarged = (float) result * EnergyConfigHandler.IC2_RATIO;
                        this.storage.receiveEnergyGC(energyDischarged);
                    }
                }
            }
            //			else if (GCCoreCompatibilityManager.isTELoaded() && itemStack.getItem() instanceof IEnergyContainerItem)
            //			{
            //				float given = ((IEnergyContainerItem) itemStack.getItem()).extractEnergy(itemStack, (int) Math.floor(this.getRequest(ForgeDirection.UNKNOWN) * EnergyConfigHandler.TO_TE_RATIO), false);
            //				this.receiveElectricity(given * EnergyConfigHandler.TE_RATIO, true);
            //			}
        }
    }

    @AltForVersion(version = "[1.7.10]")
    public void dischargeB(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            Item item = itemStack.getItem();
            float energyToDischarge = this.getRequest(ForgeDirection.UNKNOWN);

            if (item instanceof IItemElectric)
            {
                this.storage.receiveEnergyGC(ElectricItemHelper.dischargeItem(itemStack, energyToDischarge));
            }
            else if (EnergyConfigHandler.isMekanismLoaded() && item instanceof IEnergizedItem && ((IEnergizedItem) item).canSend(itemStack))
            {
                this.storage.receiveEnergyGC((float) EnergizedItemManager.discharge(itemStack, energyToDischarge * EnergyConfigHandler.TO_MEKANISM_RATIO) * EnergyConfigHandler.MEKANISM_RATIO);
            }
            else if (EnergyConfigHandler.isIndustrialCraft2Loaded())
            {
                if (item instanceof IElectricItem)
                {
                    IElectricItem electricItem = (IElectricItem) item;
                    if (electricItem.canProvideEnergy(itemStack))
                    {
                        double result = 0;
                        double energyDischargeIC2 = energyToDischarge * EnergyConfigHandler.TO_IC2_RATIO;
                        //Do this by reflection:
                        //result = ic2.api.item.ElectricItem.manager.discharge(itemStack, energyDischargeIC2, 4, false, false, false)
                        try
                        {
                            Class<?> clazz = Class.forName("ic2.api.item.IElectricItemManager");
                            Method dischargeMethod = clazz.getMethod("discharge", ItemStack.class, double.class, int.class, boolean.class, boolean.class, boolean.class);
                            result = (Double) dischargeMethod.invoke(ic2.api.item.ElectricItem.manager, itemStack, energyDischargeIC2, 4, false, false, false);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        float energyDischarged = (float) result * EnergyConfigHandler.IC2_RATIO;
                        this.storage.receiveEnergyGC(energyDischarged);
                    }
                }
                else if (item instanceof ISpecialElectricItem)
                {
                    ISpecialElectricItem electricItem = (ISpecialElectricItem) item;
                    if (electricItem.canProvideEnergy(itemStack))
                    {
                        double result = 0;
                        double energyDischargeIC2 = energyToDischarge * EnergyConfigHandler.TO_IC2_RATIO;
                        //Do this by reflection:
                        //result = electricItem.getManager(itemStack).discharge(itemStack, energyDischargeIC2, 4, false, false, false)
                        try
                        {
                            Class<?> clazz = Class.forName("ic2.api.item.IElectricItemManager");
                            Method dischargeMethod = clazz.getMethod("discharge", ItemStack.class, double.class, int.class, boolean.class, boolean.class, boolean.class);
                            result = (Double) dischargeMethod.invoke(electricItem.getManager(itemStack), itemStack, energyDischargeIC2, 4, false, false, false);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        float energyDischarged = (float) result * EnergyConfigHandler.IC2_RATIO;
                        this.storage.receiveEnergyGC(energyDischarged);
                    }
                }
            }
            //			else if (GCCoreCompatibilityManager.isTELoaded() && itemStack.getItem() instanceof IEnergyContainerItem)
            //			{
            //				float given = ((IEnergyContainerItem) itemStack.getItem()).extractEnergy(itemStack, (int) Math.floor(this.getRequest(ForgeDirection.UNKNOWN) * EnergyConfigHandler.TO_TE_RATIO), false);
            //				this.receiveElectricity(given * EnergyConfigHandler.TE_RATIO, true);
            //			}
        }
    }

    @Override
    public void initiate()
    {
        super.initiate();
        if (EnergyConfigHandler.isBuildcraftLoaded())
        {
            this.initBuildCraft();
        }
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.worldObj.isRemote)
        {
            if (!this.isAddedToEnergyNet)
            {
                // Register to the IC2 Network
                this.initIC();
            }

            if (EnergyConfigHandler.isIndustrialCraft2Loaded() && this.IC2surplusInGJ >= 0.001F)
            {
                this.IC2surplusInGJ -= this.storage.receiveEnergyGC(this.IC2surplusInGJ);
                if (this.IC2surplusInGJ < 0.001F)
                {
                    this.IC2surplusInGJ = 0;
                }
            }

            if (EnergyConfigHandler.isBuildcraftLoaded())
            {
                if (this.powerHandlerBC == null)
                {
                    this.initBuildCraft();
                }

                PowerHandler handler = (PowerHandler) this.powerHandlerBC;

                double energyBC = handler.getEnergyStored();
                if (energyBC > 0D)
                {
                    float usedBC = this.storage.receiveEnergyGC((float) energyBC * EnergyConfigHandler.BC3_RATIO) * EnergyConfigHandler.TO_BC_RATIO;
                    energyBC -= usedBC;
                    if (energyBC < 0D)
                    {
                        energyBC = 0D;
                    }
                    handler.setEnergy(energyBC);
                }
            }
        }
    }


    /**
     * IC2 Methods
     */
    @Override
    public void invalidate()
    {
        this.unloadTileIC2();
        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        this.unloadTileIC2();
        super.onChunkUnload();
    }

    protected void initIC()
    {
        if (EnergyConfigHandler.isIndustrialCraft2Loaded())
        {
            try
            {
                Class<?> tileLoadEvent = Class.forName("ic2.api.energy.event.EnergyTileLoadEvent");
                Class<?> energyTile = Class.forName("ic2.api.energy.tile.IEnergyTile");
                Constructor<?> constr = tileLoadEvent.getConstructor(energyTile);
                Object o = constr.newInstance(this);

                if (o != null && o instanceof Event)
                {
                    MinecraftForge.EVENT_BUS.post((Event) o);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        this.isAddedToEnergyNet = true;
    }

    private void unloadTileIC2()
    {
        if (this.isAddedToEnergyNet && this.worldObj != null)
        {
            if (EnergyConfigHandler.isIndustrialCraft2Loaded() && !this.worldObj.isRemote)
            {
                try
                {
                    Class<?> tileLoadEvent = Class.forName("ic2.api.energy.event.EnergyTileUnloadEvent");
                    Class<?> energyTile = Class.forName("ic2.api.energy.tile.IEnergyTile");
                    Constructor<?> constr = tileLoadEvent.getConstructor(energyTile);
                    Object o = constr.newInstance(this);

                    if (o != null && o instanceof Event)
                    {
                        MinecraftForge.EVENT_BUS.post((Event) o);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            this.isAddedToEnergyNet = false;
        }
    }

    @VersionSpecific(version = "[1.7.10]")
    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
    public double getDemandedEnergy()
    {
        try
        {
            if (this.IC2surplusInGJ < 0.001F)
            {
                this.IC2surplusInGJ = 0F;
                return Math.ceil((this.storage.receiveEnergyGC(Integer.MAX_VALUE, true)) * EnergyConfigHandler.TO_IC2_RATIO);
            }

            float received = this.storage.receiveEnergyGC(this.IC2surplusInGJ, true);
            if (received == this.IC2surplusInGJ)
            {
                return Math.ceil((this.storage.receiveEnergyGC(Integer.MAX_VALUE, true) - this.IC2surplusInGJ) * EnergyConfigHandler.TO_IC2_RATIO);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0D;
    }

    @VersionSpecific(version = "[1.7.2]")
    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
    public double demandedEnergyUnits()
    {
        try
        {
            if (this.IC2surplusInGJ < 0.001F)
            {
                this.IC2surplusInGJ = 0F;
                return Math.ceil((this.storage.receiveEnergyGC(Integer.MAX_VALUE, true)) * EnergyConfigHandler.TO_IC2_RATIO);
            }

            float received = this.storage.receiveEnergyGC(this.IC2surplusInGJ, true);
            if (received == this.IC2surplusInGJ)
            {
                return Math.ceil((this.storage.receiveEnergyGC(Integer.MAX_VALUE, true) - this.IC2surplusInGJ) * EnergyConfigHandler.TO_IC2_RATIO);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0D;
    }

    @VersionSpecific(version = "[1.7.10]")
    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
    public double injectEnergy(ForgeDirection direction, double amount, double voltage)
    {
        if (direction == ForgeDirection.UNKNOWN || this.getElectricalInputDirections().contains(direction))
        {
            float convertedEnergy = (float) amount * EnergyConfigHandler.IC2_RATIO;
            int tierFromIC2 = ((int) voltage > 120) ? 2 : 1;
            float receive = this.receiveElectricity(direction, convertedEnergy, tierFromIC2, true);

            if (convertedEnergy > receive)
            {
                this.IC2surplusInGJ = convertedEnergy - receive;
            }
            else
            {
                this.IC2surplusInGJ = 0F;
            }

            // injectEnergy returns left over energy but all is used or goes into 'surplus'
            return 0D;
        }

        return amount;
    }

    @VersionSpecific(version = "[1.7.2]")
    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
    public double injectEnergyUnits(ForgeDirection direction, double amount)
    {
        if (direction == ForgeDirection.UNKNOWN || this.getElectricalInputDirections().contains(direction))
        {
            float convertedEnergy = (float) amount * EnergyConfigHandler.IC2_RATIO;
            int tierFromIC2 = (amount >= 128) ? 2 : 1;
            float receive = this.receiveElectricity(direction, convertedEnergy, tierFromIC2, true);

            if (convertedEnergy > receive)
            {
                this.IC2surplusInGJ = convertedEnergy - receive;
            }
            else
            {
                this.IC2surplusInGJ = 0F;
            }

            // injectEnergyUnits returns left over energy but all is used or goes into 'surplus'
            return 0D;
        }

        return amount;
    }

    @VersionSpecific(version = "[1.7.10]")
    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
    public int getSinkTier()
    {
        return 3;
    }

    @VersionSpecific(version = "[1.7.2]")
    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
    public double getMaxSafeInput()
    {
        return Integer.MAX_VALUE;
    }

    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergyAcceptor", modID = "IC2")
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
    {
        //Don't add connection to IC2 grid if it's a Galacticraft tile
        if (emitter instanceof IElectrical || emitter instanceof IConductor)
        {
            return false;
        }

        try
        {
            Class<?> energyTile = Class.forName("ic2.api.energy.tile.IEnergyTile");
            if (!energyTile.isInstance(emitter))
            {
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return this.getElectricalInputDirections().contains(direction);
    }

    /**
     * BuildCraft power support
     */
    public void initBuildCraft()
    {
        if (this.powerHandlerBC == null)
        {
            this.powerHandlerBC = new PowerHandler((IPowerReceptor) this, buildcraft.api.power.PowerHandler.Type.MACHINE);
        }

        ((PowerHandler) this.powerHandlerBC).configure(0D, this.storage.getMaxReceive() * EnergyConfigHandler.TO_BC_RATIO, 0, (int) Math.ceil(this.getMaxEnergyStoredGC() * EnergyConfigHandler.TO_BC_RATIO));
        ((PowerHandler) this.powerHandlerBC).configurePowerPerdition(1, 10);
    }

    @RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "BuildCraft|Energy")
    public PowerReceiver getPowerReceiver(ForgeDirection side)
    {
        if (this.getElectricalInputDirections().contains(side))
        {
            this.initBuildCraft();
            return ((PowerHandler) this.powerHandlerBC).getPowerReceiver();
        }

        return null;
    }

    @RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "EnderIO")
    public void doWork(PowerHandler workProvider)
    {

    }

    @RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "BuildCraft|Energy")
    public World getWorld()
    {
        return this.getWorldObj();
    }

    @RuntimeInterface(clazz = "buildcraft.api.mj.ISidedBatteryProvider", modID = "BuildCraft|Energy")
    public IBatteryObject getMjBattery(String kind, ForgeDirection direction)
    {
        if (this.getElectricalInputDirections().contains(direction))
        {
            return (IBatteryObject) this;
        }

        return null;
    }

    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "BuildCraft|Energy")
    public double getEnergyRequested()
    {
        return this.getRequest(ForgeDirection.UNKNOWN) * EnergyConfigHandler.TO_BC_RATIO;
    }

    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "BuildCraft|Energy")
    public double addEnergy(double mj)
    {
        float convertedEnergy = (float) mj * EnergyConfigHandler.BC3_RATIO;
        float used = this.receiveElectricity(ForgeDirection.UNKNOWN, convertedEnergy, 1, true);
        return used * EnergyConfigHandler.TO_BC_RATIO;
    }

    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "BuildCraft|Energy")
    public double addEnergy(double mj, boolean ignoreCycleLimit)
    {
        float convertedEnergy = (float) mj * EnergyConfigHandler.BC3_RATIO;
        float used = this.receiveElectricity(ForgeDirection.UNKNOWN, convertedEnergy, 1, true);
        return used * EnergyConfigHandler.TO_BC_RATIO;
    }

    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "BuildCraft|Energy")
    public double getEnergyStored()
    {
        return this.getEnergyStoredGC() * EnergyConfigHandler.TO_BC_RATIO;
    }

    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "BuildCraft|Energy")
    public void setEnergyStored(double mj)
    {

    }

    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "BuildCraft|Energy")
    public double maxCapacity()
    {
        return this.getMaxEnergyStoredGC() * EnergyConfigHandler.TO_BC_RATIO;
    }

    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "BuildCraft|Energy")
    public double minimumConsumption()
    {
        return this.storage.getMaxReceive() * EnergyConfigHandler.TO_BC_RATIO;
    }

    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "BuildCraft|Energy")
    public double maxReceivedPerCycle()
    {
        return (this.getMaxEnergyStoredGC() - this.getEnergyStoredGC()) * EnergyConfigHandler.TO_BC_RATIO;
    }

    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "BuildCraft|Energy")
    public IBatteryObject reconfigure(double maxCapacity, double maxReceivedPerCycle, double minimumConsumption)
    {
        return (IBatteryObject) this;
    }

    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "BuildCraft|Energy")
    public String kind()
    {
        return MjAPI.DEFAULT_POWER_FRAMEWORK;
    }

    //	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
    //	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    //	{
    //		if (!this.getElectricalInputDirections().contains(from))
    //		{
    //			return 0;
    //		}
    //
    //		return (int) Math.floor(this.receiveElectricity(maxReceive * EnergyConfigHandler.TE_RATIO, !simulate)* EnergyConfigHandler.TO_TE_RATIO);
    //	}
    //
    //	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
    //	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    //	{
    //		if (!this.getElectricalOutputDirections().contains(from))
    //		{
    //			return 0;
    //		}
    //
    //		return (int) Math.floor(this.provideElectricity(maxExtract * EnergyConfigHandler.TE_RATIO, !simulate).getWatts() * EnergyConfigHandler.TO_TE_RATIO);
    //	}
    //
    //	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
    //	public boolean canInterface(ForgeDirection from)
    //	{
    //		return this.getElectricalInputDirections().contains(from) || this.getElectricalOutputDirections().contains(from);
    //	}
    //
    //	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
    //	public int getEnergyStored(ForgeDirection from)
    //	{
    //		return (int) Math.floor(this.getEnergyStored() * EnergyConfigHandler.TO_TE_RATIO);
    //	}
    //
    //	@RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "ThermalExpansion")
    //	public int getMaxEnergyStored(ForgeDirection from)
    //	{
    //		return (int) Math.floor(this.getMaxEnergyStored() * EnergyConfigHandler.TO_TE_RATIO);
    //	}

    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
    public double transferEnergyToAcceptor(ForgeDirection from, double amount)
    {
        if (!this.getElectricalInputDirections().contains(from))
        {
            return 0;
        }

        return this.receiveElectricity(from, (float) amount * EnergyConfigHandler.MEKANISM_RATIO, 1, true) * EnergyConfigHandler.TO_MEKANISM_RATIO;
    }

    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
    public boolean canReceiveEnergy(ForgeDirection side)
    {
        return this.getElectricalInputDirections().contains(side);
    }

    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
    public double getEnergy()
    {
        return this.getEnergyStoredGC() * EnergyConfigHandler.TO_MEKANISM_RATIO;
    }

    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
    public void setEnergy(double energy)
    {
        this.storage.setEnergyStored((float) energy * EnergyConfigHandler.MEKANISM_RATIO);
    }

    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
    public double getMaxEnergy()
    {
        return this.getMaxEnergyStoredGC() * EnergyConfigHandler.TO_MEKANISM_RATIO;
    }

    @Override
    public ReceiverMode getModeFromDirection(ForgeDirection direction)
    {
        if (this.getElectricalInputDirections().contains(direction))
        {
            return ReceiverMode.RECEIVE;
        }
        else if (this.getElectricalOutputDirections().contains(direction))
        {
            return ReceiverMode.EXTRACT;
        }

        return null;
    }

    /*
     * Compatibility: call this if the facing metadata is updated
     */
    public void updateFacing()
    {
        if (EnergyConfigHandler.isIndustrialCraft2Loaded() && !this.worldObj.isRemote)
        {
            //This seems the only method to tell IC2 the connection sides have changed
            //(Maybe there is an internal refresh() method but it's not in the API)
            this.unloadTileIC2();
            //This will do an initIC2 on next tick update.
        }
    }
}
