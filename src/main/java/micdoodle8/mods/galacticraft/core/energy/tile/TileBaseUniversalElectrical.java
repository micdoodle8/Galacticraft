package micdoodle8.mods.galacticraft.core.energy.tile;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.eventhandler.Event;
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
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

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

    public EnumSet<EnumFacing> getElectricalInputDirections()
    {
        return EnumSet.allOf(EnumFacing.class);
    }

    public EnumSet<EnumFacing> getElectricalOutputDirections()
    {
        return EnumSet.noneOf(EnumFacing.class);
    }

    @Override
    public float getRequest(EnumFacing direction)
    {
        if (this.getElectricalInputDirections().contains(direction) || direction == null)
        	return super.getRequest(direction);
        
        return 0F;
    }

    @Override
    public float receiveElectricity(EnumFacing from, float receive, int tier, boolean doReceive)
    {
        if (this.getElectricalInputDirections().contains(from) || from == null)
        {
        	return super.receiveElectricity(from, receive, tier, doReceive);
        }
        
        return 0F;
    }
    
    //	@Override
    //	public float receiveElectricity(EnumFacing from, ElectricityPack receive, boolean doReceive)
    //	{
    //		if (from == EnumFacing.UNKNOWN || this.getElectricalInputDirections().contains(from))
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

    //	public boolean canConnect(EnumFacing direction, NetworkType type)
    //	{
    //		if (direction == null || direction.equals(EnumFacing.UNKNOWN) || type != NetworkType.POWER)
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

//    /**
//     * Discharges electric item.
//     */
//    @VersionSpecific(version = "[1.7.2]")
//    public void discharge(ItemStack itemStack)
//    {
//        if (itemStack != null)
//        {
//            Item item = itemStack.getItem();
//            float energyToDischarge = this.getRequest(null);
//
//            if (item instanceof IItemElectric)
//            {
//                this.storage.receiveEnergyGC(ElectricItemHelper.dischargeItem(itemStack, energyToDischarge));
//            }
//            else if (EnergyConfigHandler.isIndustrialCraft2Loaded())
//            {
////                if (item instanceof IElectricItem)
////                {
////                    IElectricItem electricItem = (IElectricItem) item;
////                    if (electricItem.canProvideEnergy(itemStack))
////                    {
////                        double result = 0;
////                        int energyDischargeIC2 = (int) (energyToDischarge / EnergyConfigHandler.IC2_RATIO);
////                        try
////                        {
////                            Class<?> clazz = Class.forName("ic2.api.item.IElectricItemManager");
////                            Method dischargeMethod = clazz.getMethod("discharge", ItemStack.class, int.class, int.class, boolean.class, boolean.class);
////                            result = (Integer) dischargeMethod.invoke(ic2.api.item.ElectricItem.manager, itemStack, energyDischargeIC2, 4, false, false);
////                        }
////                        catch (Exception e)
////                        {
////                            e.printStackTrace();
////                        }
////                        float energyDischarged = (float) result * EnergyConfigHandler.IC2_RATIO;
////                        this.storage.receiveEnergyGC(energyDischarged);
////                    }
////                }
////                else if (item instanceof ISpecialElectricItem)
////                {
////                    ISpecialElectricItem electricItem = (ISpecialElectricItem) item;
////                    if (electricItem.canProvideEnergy(itemStack))
////                    {
////                        double result = 0;
////                        int energyDischargeIC2 = (int) (energyToDischarge / EnergyConfigHandler.IC2_RATIO);
////                        //Do this by reflection:
////                        //result = electricItem.getManager(itemStack).discharge(itemStack, energyDischargeIC2, 4, false, false, false)
////                        try
////                        {
////                            Class<?> clazz = Class.forName("ic2.api.item.IElectricItemManager");
////                            Method dischargeMethod = clazz.getMethod("discharge", ItemStack.class, int.class, int.class, boolean.class, boolean.class);
////                            result = (Integer) dischargeMethod.invoke(electricItem.getManager(itemStack), itemStack, energyDischargeIC2, 4, false, false);
////                        }
////                        catch (Exception e)
////                        {
////                            e.printStackTrace();
////                        }
////                        float energyDischarged = (float) result * EnergyConfigHandler.IC2_RATIO;
////                        this.storage.receiveEnergyGC(energyDischarged);
////                    }
////                } // TODO
//            }
//            //			else if (GCCoreCompatibilityManager.isTELoaded() && itemStack.getItem() instanceof IEnergyContainerItem)
//            //			{
//            //				float given = ((IEnergyContainerItem) itemStack.getItem()).extractEnergy(itemStack, (int) Math.floor(this.getRequest(EnumFacing.UNKNOWN) * EnergyConfigHandler.TO_TE_RATIO), false);
//            //				this.receiveElectricity(given * EnergyConfigHandler.TE_RATIO, true);
//            //			}
//        }
//    }

    public void discharge(ItemStack itemStack)
    {
        if (itemStack != null)
        {
            Item item = itemStack.getItem();
            float energyToDischarge = this.getRequest(null);

            if (item instanceof IItemElectric)
            {
                this.storage.receiveEnergyGC(ElectricItemHelper.dischargeItem(itemStack, energyToDischarge));
            }
//            else if (EnergyConfigHandler.isMekanismLoaded() && item instanceof IEnergizedItem && ((IEnergizedItem) item).canSend(itemStack))
//            {
//                this.storage.receiveEnergyGC((float) EnergizedItemManager.discharge(itemStack, energyToDischarge / EnergyConfigHandler.MEKANISM_RATIO) * EnergyConfigHandler.MEKANISM_RATIO);
//            }
//            else if (EnergyConfigHandler.isIndustrialCraft2Loaded())
//            {
//                if (item instanceof IElectricItem)
//                {
//                    IElectricItem electricItem = (IElectricItem) item;
//                    if (electricItem.canProvideEnergy(itemStack))
//                    {
//                        double result = 0;
//                        double energyDischargeIC2 = energyToDischarge / EnergyConfigHandler.IC2_RATIO;
//                        //Do this by reflection:
//                        //result = ic2.api.item.ElectricItem.manager.discharge(itemStack, energyDischargeIC2, 4, false, false, false)
//                        try
//                        {
//                            Class<?> clazz = Class.forName("ic2.api.item.IElectricItemManager");
//                            Method dischargeMethod = clazz.getMethod("discharge", ItemStack.class, double.class, int.class, boolean.class, boolean.class, boolean.class);
//                            result = (Double) dischargeMethod.invoke(ic2.api.item.ElectricItem.manager, itemStack, energyDischargeIC2, 4, false, false, false);
//                        }
//                        catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                        float energyDischarged = (float) result * EnergyConfigHandler.IC2_RATIO;
//                        this.storage.receiveEnergyGC(energyDischarged);
//                    }
//                }
//                else if (item instanceof ISpecialElectricItem)
//                {
//                    ISpecialElectricItem electricItem = (ISpecialElectricItem) item;
//                    if (electricItem.canProvideEnergy(itemStack))
//                    {
//                        double result = 0;
//                        double energyDischargeIC2 = energyToDischarge / EnergyConfigHandler.IC2_RATIO;
//                        //Do this by reflection:
//                        //result = electricItem.getManager(itemStack).discharge(itemStack, energyDischargeIC2, 4, false, false, false)
//                        try
//                        {
//                            Class<?> clazz = Class.forName("ic2.api.item.IElectricItemManager");
//                            Method dischargeMethod = clazz.getMethod("discharge", ItemStack.class, double.class, int.class, boolean.class, boolean.class, boolean.class);
//                            result = (Double) dischargeMethod.invoke(electricItem.getManager(itemStack), itemStack, energyDischargeIC2, 4, false, false, false);
//                        }
//                        catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                        float energyDischarged = (float) result * EnergyConfigHandler.IC2_RATIO;
//                        this.storage.receiveEnergyGC(energyDischarged);
//                    }
//                }
//            } TODO
            //			else if (GCCoreCompatibilityManager.isTELoaded() && itemStack.getItem() instanceof IEnergyContainerItem)
            //			{
            //				float given = ((IEnergyContainerItem) itemStack.getItem()).extractEnergy(itemStack, (int) Math.floor(this.getRequest(EnumFacing.UNKNOWN) * EnergyConfigHandler.TO_TE_RATIO), false);
            //				this.receiveElectricity(given * EnergyConfigHandler.TE_RATIO, true);
            //			}
        }
    }

    @Override
    public void initiate()
    {
        super.initiate();
//        if (EnergyConfigHandler.isBuildcraftLoaded())
//        {
//            this.initBuildCraft();
//        } TODO
    }

    @Override
    public void update()
    {
        super.update();

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

//            if (EnergyConfigHandler.isBuildcraftLoaded())
//            {
//                if (this.powerHandlerBC == null)
//                {
//                    this.initBuildCraft();
//                }
//
//                PowerHandler handler = (PowerHandler) this.powerHandlerBC;
//
//                double energyBC = handler.getEnergyStored();
//                if (energyBC > 0D)
//                {
//                    float usedBC = this.storage.receiveEnergyGC((float) energyBC * EnergyConfigHandler.BC3_RATIO) / EnergyConfigHandler.BC3_RATIO;
//                    energyBC -= usedBC;
//                    if (energyBC < 0D)
//                    {
//                        energyBC = 0D;
//                    }
//                    handler.setEnergy(energyBC);
//                }
//            } TODO
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

//    @VersionSpecific(version = "[1.7.10]")
//    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
//    public double getDemandedEnergy()
//    {
//        try
//        {
//            if (this.IC2surplusInGJ < 0.001F)
//            {
//                this.IC2surplusInGJ = 0F;
//                return Math.ceil((this.storage.receiveEnergyGC(Integer.MAX_VALUE, true)) / EnergyConfigHandler.IC2_RATIO);
//            }
//
//            float received = this.storage.receiveEnergyGC(this.IC2surplusInGJ, true);
//            if (received == this.IC2surplusInGJ)
//            {
//                return Math.ceil((this.storage.receiveEnergyGC(Integer.MAX_VALUE, true) - this.IC2surplusInGJ) / EnergyConfigHandler.IC2_RATIO);
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return 0D;
//    }
//
//    @VersionSpecific(version = "[1.7.2]")
//    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
//    public double demandedEnergyUnits()
//    {
//        try
//        {
//            if (this.IC2surplusInGJ < 0.001F)
//            {
//                this.IC2surplusInGJ = 0F;
//                return Math.ceil((this.storage.receiveEnergyGC(Integer.MAX_VALUE, true)) / EnergyConfigHandler.IC2_RATIO);
//            }
//
//            float received = this.storage.receiveEnergyGC(this.IC2surplusInGJ, true);
//            if (received == this.IC2surplusInGJ)
//            {
//                return Math.ceil((this.storage.receiveEnergyGC(Integer.MAX_VALUE, true) - this.IC2surplusInGJ) / EnergyConfigHandler.IC2_RATIO);
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return 0D;
//    }
//
//    @VersionSpecific(version = "[1.7.10]")
//    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
//    public double injectEnergy(EnumFacing direction, double amount, double voltage)
//    {
//        if (direction == null || this.getElectricalInputDirections().contains(direction))
//        {
//            float convertedEnergy = (float) amount * EnergyConfigHandler.IC2_RATIO;
//            int tierFromIC2 = ((int) voltage > 120) ? 2 : 1;
//            float receive = this.receiveElectricity(direction, convertedEnergy, tierFromIC2, true);
//
//            if (convertedEnergy > receive)
//            {
//                this.IC2surplusInGJ = convertedEnergy - receive;
//            }
//            else
//            {
//                this.IC2surplusInGJ = 0F;
//            }
//
//            // injectEnergy returns left over energy but all is used or goes into 'surplus'
//            return 0D;
//        }
//
//        return amount;
//    }
//
//    @VersionSpecific(version = "[1.7.2]")
//    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
//    public double injectEnergyUnits(EnumFacing direction, double amount)
//    {
//        if (direction == null || this.getElectricalInputDirections().contains(direction))
//        {
//            float convertedEnergy = (float) amount * EnergyConfigHandler.IC2_RATIO;
//            int tierFromIC2 = (amount >= 128) ? 2 : 1;
//            float receive = this.receiveElectricity(direction, convertedEnergy, tierFromIC2, true);
//
//            if (convertedEnergy > receive)
//            {
//                this.IC2surplusInGJ = convertedEnergy - receive;
//            }
//            else
//            {
//                this.IC2surplusInGJ = 0F;
//            }
//
//            // injectEnergyUnits returns left over energy but all is used or goes into 'surplus'
//            return 0D;
//        }
//
//        return amount;
//    }
//
//    @VersionSpecific(version = "[1.7.10]")
//    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
//    public int getSinkTier()
//    {
//        return 3;
//    }
//
//    @VersionSpecific(version = "[1.7.2]")
//    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
//    public double getMaxSafeInput()
//    {
//        return Integer.MAX_VALUE;
//    }
//
//    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergyAcceptor", modID = "IC2")
//    public boolean acceptsEnergyFrom(TileEntity emitter, EnumFacing direction)
//    {
//        //Don't add connection to IC2 grid if it's a Galacticraft tile
//        if (emitter instanceof IElectrical || emitter instanceof IConductor)
//        {
//            return false;
//        }
//
//        try
//        {
//            Class<?> energyTile = Class.forName("ic2.api.energy.tile.IEnergyTile");
//            if (!energyTile.isInstance(emitter))
//            {
//                return false;
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        return this.getElectricalInputDirections().contains(direction);
//    }
//
//    /**
//     * BuildCraft power support
//     */
//    public void initBuildCraft()
//    {
//        if (this.powerHandlerBC == null)
//        {
//            this.powerHandlerBC = new PowerHandler((IPowerReceptor) this, buildcraft.api.power.PowerHandler.Type.MACHINE);
//        }
//        float receive = this.storage.receiveEnergyGC(this.storage.getMaxReceive(), true) / EnergyConfigHandler.BC3_RATIO;
//        if (receive < 0.1F) receive = 0F;
//        ((PowerHandler) this.powerHandlerBC).configure(0D, receive, 0, (int) (this.getMaxEnergyStoredGC() / EnergyConfigHandler.BC3_RATIO));
//        ((PowerHandler) this.powerHandlerBC).configurePowerPerdition(1, 10);
//    }
//
//    @RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "")
//    public PowerReceiver getPowerReceiver(EnumFacing side)
//    {
//        if (this.getElectricalInputDirections().contains(side))
//        {
//            this.initBuildCraft();
//            return ((PowerHandler) this.powerHandlerBC).getPowerReceiver();
//        }
//
//        return null;
//    }
//
//    @RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "")
//    public void doWork(PowerHandler workProvider)
//    {
//    	this.initBuildCraft();
//    }
//
//    @RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "")
//    public World getWorld()
//    {
//        return this.getWorldObj();
//    }
//
//    @RuntimeInterface(clazz = "buildcraft.api.mj.ISidedBatteryProvider", modID = "")
//    public IBatteryObject getMjBattery(String kind, EnumFacing direction)
//    {
//        if (this.getElectricalInputDirections().contains(direction))
//        {
//            return (IBatteryObject) this;
//        }
//
//        return null;
//    }
//
//    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "")
//    public double getEnergyRequested()
//    {
//        float requested = this.getRequest(EnumFacing.UNKNOWN) / EnergyConfigHandler.BC3_RATIO;
//        if (requested < 0.1F) requested = 0F;
//        return requested;
//    }
//
//    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "")
//    public double addEnergy(double mj)
//    {
//        float convertedEnergy = (float) mj * EnergyConfigHandler.BC3_RATIO;
//        float used = this.receiveElectricity(EnumFacing.UNKNOWN, convertedEnergy, 1, true);
//        return used / EnergyConfigHandler.BC3_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "")
//    public double addEnergy(double mj, boolean ignoreCycleLimit)
//    {
//        float convertedEnergy = (float) mj * EnergyConfigHandler.BC3_RATIO;
//        float used = this.receiveElectricity(EnumFacing.UNKNOWN, convertedEnergy, 1, true);
//        return used / EnergyConfigHandler.BC3_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "")
//    public double getEnergyStored()
//    {
//        return this.getEnergyStoredGC() / EnergyConfigHandler.BC3_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "")
//    public void setEnergyStored(double mj)
//    {
//
//    }
//
//    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "")
//    public double maxCapacity()
//    {
//        return this.getMaxEnergyStoredGC() / EnergyConfigHandler.BC3_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "")
//    public double minimumConsumption()
//    {
//        return this.storage.getMaxReceive() / EnergyConfigHandler.BC3_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "")
//    public double maxReceivedPerCycle()
//    {
//        return (this.getMaxEnergyStoredGC() - this.getEnergyStoredGC()) / EnergyConfigHandler.BC3_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "")
//    public IBatteryObject reconfigure(double maxCapacity, double maxReceivedPerCycle, double minimumConsumption)
//    {
//        return (IBatteryObject) this;
//    }
//
//    @RuntimeInterface(clazz = "buildcraft.api.mj.IBatteryObject", modID = "")
//    public String kind()
//    {
//        return MjAPI.DEFAULT_POWER_FRAMEWORK;
//    }
//
//    @RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "")
//    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate)
//    {
//    	if (!this.getElectricalInputDirections().contains(from))
//    	{
//    		return 0;
//    	}
//
//    	return MathHelper.floor_float(super.receiveElectricity(from, maxReceive * EnergyConfigHandler.RF_RATIO, 1, !simulate) / EnergyConfigHandler.RF_RATIO);
//    }
//
//    @RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "")
//    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate)
//    {
//    	return 0;
//    }
//
//    @RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "")
//    public boolean canConnectEnergy(EnumFacing from)
//    {
//    	return this.getElectricalInputDirections().contains(from) || this.getElectricalOutputDirections().contains(from);
//    }
//
//    @RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "")
//    public int getEnergyStored(EnumFacing from)
//    {
//    	return MathHelper.floor_float(this.getEnergyStoredGC() / EnergyConfigHandler.RF_RATIO);
//    }
//
//    @RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "")
//    public int getMaxEnergyStored(EnumFacing from)
//    {
//    	return MathHelper.floor_float(this.getMaxEnergyStoredGC() / EnergyConfigHandler.RF_RATIO);
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
//    public double transferEnergyToAcceptor(EnumFacing from, double amount)
//    {
//        if (!this.getElectricalInputDirections().contains(from))
//        {
//            return 0;
//        }
//
//        return this.receiveElectricity(from, (float) amount * EnergyConfigHandler.MEKANISM_RATIO, 1, true) / EnergyConfigHandler.MEKANISM_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
//    public boolean canReceiveEnergy(EnumFacing side)
//    {
//        return this.getElectricalInputDirections().contains(side);
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
//    public double getEnergy()
//    {
//        return this.getEnergyStoredGC() / EnergyConfigHandler.MEKANISM_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
//    public void setEnergy(double energy)
//    {
//        this.storage.setEnergyStored((float) energy * EnergyConfigHandler.MEKANISM_RATIO);
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
//    public double getMaxEnergy()
//    {
//        return this.getMaxEnergyStoredGC() / EnergyConfigHandler.MEKANISM_RATIO;
//    }
//
//    @RuntimeInterface(clazz = "mekanism.api.energy.ICableOutputter", modID = "Mekanism")
//    public boolean canOutputTo(EnumFacing side)
//    {
//        return false;
//    } TODO

    @Override
    public ReceiverMode getModeFromDirection(EnumFacing direction)
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
