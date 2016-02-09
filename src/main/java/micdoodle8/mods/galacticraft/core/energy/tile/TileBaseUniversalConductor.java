package micdoodle8.mods.galacticraft.core.energy.tile;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import cpw.mods.fml.common.eventhandler.Event;
import ic2.api.energy.tile.IEnergySource;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.api.transmission.tile.IElectrical;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.energy.EnergyConfigHandler;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import micdoodle8.mods.galacticraft.core.energy.grid.EnergyNetwork;
import micdoodle8.mods.miccore.Annotations.RuntimeInterface;
import micdoodle8.mods.miccore.Annotations.VersionSpecific;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import java.lang.reflect.Constructor;

public abstract class TileBaseUniversalConductor extends TileBaseConductor
{
    protected boolean isAddedToEnergyNet;
    protected Object powerHandlerBC;

    //	public float buildcraftBuffer = EnergyConfigHandler.BC3_RATIO * 50;
    private float IC2surplusJoules = 0F;

    public TileBaseUniversalConductor()
    {
        this.initBC();
    }

    @Override
    public void onNetworkChanged()
    {
    }

    private void initBC()
    {
        if (EnergyConfigHandler.isBuildcraftLoaded())
        {
            if (this instanceof IPowerReceptor)
            {
                this.powerHandlerBC = new PowerHandler((IPowerReceptor) this, buildcraft.api.power.PowerHandler.Type.PIPE);
                ((PowerHandler) this.powerHandlerBC).configurePowerPerdition(0, 0);
                ((PowerHandler) this.powerHandlerBC).configure(0, 0, 0, 0);
            }
        }
    }

    @Override
    public TileEntity[] getAdjacentConnections()
    {
        return EnergyUtil.getAdjacentPowerConnections(this);
    }

    @Override
    public boolean canUpdate()
    {
        return true;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (!this.isAddedToEnergyNet)
        {
            if (!this.worldObj.isRemote)
            {
                if (EnergyConfigHandler.isIndustrialCraft2Loaded())
                {
                    this.initIC();
                }
            }

            this.isAddedToEnergyNet = true;
        }
    }

    @Override
    public void invalidate()
    {
        this.IC2surplusJoules = 0F;
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
        if (EnergyConfigHandler.isIndustrialCraft2Loaded() && !this.worldObj.isRemote)
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
    }

    private void unloadTileIC2()
    {
        if (this.isAddedToEnergyNet && this.worldObj != null)
        {
            if (!this.worldObj.isRemote && EnergyConfigHandler.isIndustrialCraft2Loaded())
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

    @VersionSpecific(version = "[1.7.2]")
    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
    public double demandedEnergyUnits()
    {
        if (this.getNetwork() == null)
        {
            return 0.0;
        }

        if (this.IC2surplusJoules < 0.001F)
        {
            this.IC2surplusJoules = 0F;
            float result = this.getNetwork().getRequest(this) / EnergyConfigHandler.IC2_RATIO;
            //Cap energy which IC2 can put into Alu Wire at 128 EU/t for regular, 256 EU/t for heavy
            result = Math.max(((EnergyNetwork) this.getNetwork()).networkTierGC == 2 ? 256F : 128F, result);
            return result;
        }

        this.IC2surplusJoules = this.getNetwork().produce(this.IC2surplusJoules, true, 1, this);
        if (this.IC2surplusJoules < 0.001F)
        {
            this.IC2surplusJoules = 0F;
            float result = this.getNetwork().getRequest(this) / EnergyConfigHandler.IC2_RATIO;
            //Cap energy which IC2 can put into Alu Wire at 128 EU/t for regular, 256 EU/t for heavy
            result = Math.max(((EnergyNetwork) this.getNetwork()).networkTierGC == 2 ? 256F : 128F, result);
            return result;
        }
        return 0D;
    }

    @VersionSpecific(version = "[1.7.10]")
    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
    public double getDemandedEnergy()
    {
        if (this.getNetwork() == null)
        {
            return 0.0;
        }

        if (this.IC2surplusJoules < 0.001F)
        {
            this.IC2surplusJoules = 0F;
            return this.getNetwork().getRequest(this) / EnergyConfigHandler.IC2_RATIO;
        }

        this.IC2surplusJoules = this.getNetwork().produce(this.IC2surplusJoules, true, 1, this);
        if (this.IC2surplusJoules < 0.001F)
        {
            this.IC2surplusJoules = 0F;
            return this.getNetwork().getRequest(this) / EnergyConfigHandler.IC2_RATIO;
        }
        return 0D;
    }

    @VersionSpecific(version = "[1.7.2]")
    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
    public double injectEnergyUnits(ForgeDirection directionFrom, double amount)
    {
        TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.worldObj, directionFrom);
        int tier = 1;
        if (tile instanceof IEnergySource && ((IEnergySource) tile).getOfferedEnergy() >= 128)
        {
            tier = 2;
        }
        float convertedEnergy = (float) amount * EnergyConfigHandler.IC2_RATIO;
        float surplus = this.getNetwork().produce(convertedEnergy, true, tier, this, tile);

        if (surplus >= 0.001F)
        {
            this.IC2surplusJoules = surplus;
        }
        else
        {
            this.IC2surplusJoules = 0F;
        }

        return 0D;
    }

    @VersionSpecific(version = "[1.7.10]")
    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergySink", modID = "IC2")
    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage)
    {
        TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.worldObj, directionFrom);
        int tier = ((int) voltage > 120) ? 2 : 1;
        if (tile instanceof IEnergySource && ((IEnergySource) tile).getOfferedEnergy() >= 128)
        {
            tier = 2;
        }
        float convertedEnergy = (float) amount * EnergyConfigHandler.IC2_RATIO;
        float surplus = this.getNetwork().produce(convertedEnergy, true, tier, this, tile);

        if (surplus >= 0.001F)
        {
            this.IC2surplusJoules = surplus;
        }
        else
        {
            this.IC2surplusJoules = 0F;
        }

        return 0D;
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

        //Don't make connection with IC2 wires [don't want risk of multiple connections + there is a graphical glitch in IC2]
        try
        {
            Class<?> conductorIC2 = Class.forName("ic2.api.energy.tile.IEnergyConductor");
            if (conductorIC2.isInstance(emitter))
            {
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return true;
    }

    @RuntimeInterface(clazz = "ic2.api.energy.tile.IEnergyEmitter", modID = "IC2")
    public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction)
    {
        //Don't add connection to IC2 grid if it's a Galacticraft tile
        if (receiver instanceof IElectrical || receiver instanceof IConductor)
        {
            return false;
        }

        //Don't make connection with IC2 wires [don't want risk of multiple connections + there is a graphical glitch in IC2]
        try
        {
            Class<?> conductorIC2 = Class.forName("ic2.api.energy.tile.IEnergyConductor");
            if (conductorIC2.isInstance(receiver))
            {
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * BuildCraft functions
     */
    @RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "")
    public PowerReceiver getPowerReceiver(ForgeDirection side)
    {
        if (this.getNetwork() == null)
        {
            return null;
        }

        double requiredEnergy = this.getNetwork().getRequest(this) / EnergyConfigHandler.BC3_RATIO;
        
        if (requiredEnergy <= 0.1D)
        {
        	requiredEnergy = 0;
        }
        
        ((PowerHandler) this.powerHandlerBC).configure(0, requiredEnergy, 0, requiredEnergy);
        return ((PowerHandler) this.powerHandlerBC).getPowerReceiver();
    }

    public void reconfigureBC()
    {
    	double requiredEnergy = this.getNetwork().getRequest(this) / EnergyConfigHandler.BC3_RATIO;
        if (requiredEnergy <= 0.1D)
        {
        	requiredEnergy = 0;
        }
        ((PowerHandler) this.powerHandlerBC).configure(0, requiredEnergy, 0, requiredEnergy);
    }

    @RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "")
    public void doWork(PowerHandler workProvider)
    {
        PowerHandler handler = (PowerHandler) this.powerHandlerBC;

        double energyBC = handler.getEnergyStored();
        if (energyBC > 0D)
        {
            energyBC = this.getNetwork().produce((float) energyBC * EnergyConfigHandler.BC3_RATIO, true, 1, this) / EnergyConfigHandler.BC3_RATIO;
            if (energyBC < 0D)
            {
                energyBC = 0D;
            }
            handler.setEnergy(energyBC);
        }

        this.reconfigureBC();
    }

    @RuntimeInterface(clazz = "buildcraft.api.power.IPowerReceptor", modID = "")
    public World getWorld()
    {
        return this.getWorldObj();
    }

    @RuntimeInterface(clazz = "cofh.api.energy.IEnergyReceiver", modID = "")
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
    	if (this.getNetwork() == null)
    	{
    		return 0;
    	}
        float receiveGC = maxReceive * EnergyConfigHandler.RF_RATIO;
        float sentGC = receiveGC - this.getNetwork().produce(receiveGC, !simulate, 1);
    	return MathHelper.floor_float(sentGC / EnergyConfigHandler.RF_RATIO);
    }

    @RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "")
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
    	return 0;
    }

    @RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "")
    public boolean canConnectEnergy(ForgeDirection from)
    {
    	//Do not form wire-to-wire connections with EnderIO conduits
    	TileEntity tile = new BlockVec3(this).getTileEntityOnSide(this.worldObj, from);
        try {
            if (EnergyUtil.clazzEnderIOCable != null && EnergyUtil.clazzEnderIOCable.isInstance(tile))
            {
                return false;
            }
        } catch (Exception e) { }
    	return true;
    }

    @RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "")
    public int getEnergyStored(ForgeDirection from)
    {
    	return 0;
    }

    @RuntimeInterface(clazz = "cofh.api.energy.IEnergyHandler", modID = "")
    public int getMaxEnergyStored(ForgeDirection from)
    {
    	if (this.getNetwork() == null)
    	{
    		return 0;
    	}

    	return MathHelper.floor_float(this.getNetwork().getRequest(this) / EnergyConfigHandler.RF_RATIO);
    }

    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
    public double transferEnergyToAcceptor(ForgeDirection side, double amount)
    {
        if (!this.canReceiveEnergy(side))
        {
            return 0;
        }

        return amount - this.getNetwork().produce((float) amount * EnergyConfigHandler.MEKANISM_RATIO, true, 1, this) / EnergyConfigHandler.MEKANISM_RATIO;
    }

    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
    public boolean canReceiveEnergy(ForgeDirection side)
    {
        if (this.getNetwork() == null)
        {
            return false;
        }

        TileEntity te = new BlockVec3(this).getTileEntityOnSide(this.worldObj, side);
        try
        {
            if (Class.forName("codechicken.multipart.TileMultipart").isInstance(te))
            {
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return true;
    }

    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
    public double getEnergy()
    {
        return 0;
    }

    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
    public void setEnergy(double energy)
    {
        ;
    }

    @RuntimeInterface(clazz = "mekanism.api.energy.IStrictEnergyAcceptor", modID = "Mekanism")
    public double getMaxEnergy()
    {
        if (this.getNetwork() == null)
        {
            return 0;
        }
        return this.getNetwork().getRequest(this) / EnergyConfigHandler.MEKANISM_RATIO;
    }
}
