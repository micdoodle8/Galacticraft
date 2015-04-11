package micdoodle8.mods.galacticraft.core.energy;

import buildcraft.api.mj.MjAPI;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import ic2.api.energy.tile.*;
import mekanism.api.energy.ICableOutputter;
import mekanism.api.energy.IStrictEnergyAcceptor;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;

import java.lang.reflect.Method;

public class EnergyUtil
{
    private static boolean isMekLoaded = EnergyConfigHandler.isMekanismLoaded();
    private static boolean isRFLoaded = EnergyConfigHandler.isRFAPILoaded();
    private static boolean isRF1Loaded = EnergyConfigHandler.isRFAPIv1Loaded();
    private static boolean isRF2Loaded = EnergyConfigHandler.isRFAPIv2Loaded();
    private static boolean isIC2Loaded = EnergyConfigHandler.isIndustrialCraft2Loaded();
    private static boolean isBCLoaded = EnergyConfigHandler.isBuildcraftLoaded();
    private static boolean isBC6Loaded = isBCLoaded && EnergyConfigHandler.getBuildcraftVersion() == 6;
    private static boolean isBCReallyLoaded = EnergyConfigHandler.isBuildcraftReallyLoaded();
  
    public static boolean voltageParameterIC2 = false;
    public static Method demandedEnergyIC2 = null;
    public static Method injectEnergyIC2 = null;
    private static Class<?> clazzMekCable = null;
    public static Class<?> clazzEnderIOCable = null;
    private static Class<?> clazzPipeTile = null;
    private static Class<?> clazzPipeWood = null;  
    public static boolean initialisedIC2Methods = EnergyUtil.initialiseIC2Methods();
    
    public static TileEntity[] getAdjacentPowerConnections(TileEntity tile)
    {
        TileEntity[] adjacentConnections = new TileEntity[6];
        
        BlockVec3 thisVec = new BlockVec3(tile);
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS)
        {
            TileEntity tileEntity = thisVec.getTileEntityOnSide(tile.getWorldObj(), direction);

            if (tileEntity instanceof IConnector)
            {
                if (((IConnector) tileEntity).canConnect(direction.getOpposite(), NetworkType.POWER))
                {
                    adjacentConnections[direction.ordinal()] = tileEntity;
                }
                continue;
            }
            else if (isMekLoaded && (tileEntity instanceof IStrictEnergyAcceptor || tileEntity instanceof ICableOutputter))
            {
                //Do not connect GC wires directly to Mek Universal Cables
                try {
                    if (clazzMekCable != null && clazzMekCable.isInstance(tileEntity))
                    {
                        continue;
                    }
                } catch (Exception e) { e.printStackTrace(); }

                if (tileEntity instanceof IStrictEnergyAcceptor && ((IStrictEnergyAcceptor) tileEntity).canReceiveEnergy(direction.getOpposite()))
                {
                    adjacentConnections[direction.ordinal()] = tileEntity;
                }
                else if (tileEntity instanceof ICableOutputter && ((ICableOutputter) tileEntity).canOutputTo(direction.getOpposite()))
                {
                    adjacentConnections[direction.ordinal()] = tileEntity;
                }
            }
            else if (isRFLoaded && tileEntity instanceof IEnergyConnection)
            {
                if (isRF1Loaded && tileEntity instanceof IEnergyHandler || isRF2Loaded && (tileEntity instanceof IEnergyProvider || tileEntity instanceof IEnergyReceiver))
                {
                    //Do not connect GC wires directly to power conduits
                    try {
                        if (clazzEnderIOCable != null && clazzEnderIOCable.isInstance(tileEntity))
                        {
                            continue;
                        }
                    } catch (Exception e) { }
	                if (((IEnergyConnection)tileEntity).canConnectEnergy(direction.getOpposite()))
	                	adjacentConnections[direction.ordinal()] = tileEntity;
                }
                continue;
            }
            else if (isIC2Loaded && tileEntity instanceof IEnergyTile)
            {
                if (tileEntity instanceof IEnergyConductor)
                {
                    continue;
                }

                if (tileEntity instanceof IEnergyAcceptor)
                {
                    if (((IEnergyAcceptor) tileEntity).acceptsEnergyFrom(tile, direction.getOpposite()))
                    {
                        adjacentConnections[direction.ordinal()] = tileEntity;
                        continue;
                    }
                }
                if (tileEntity instanceof IEnergyEmitter)
                {
                    if (((IEnergyEmitter) tileEntity).emitsEnergyTo(tile, direction.getOpposite()))
                    {
                        adjacentConnections[direction.ordinal()] = tileEntity;
                        continue;
                    }
                }
            }
            else if (isBCLoaded)
            {
                if (isBCReallyLoaded)
                {
                    //Do not connect GC wires to BC wooden power pipes
                    try
                    {
                        if (clazzPipeTile.isInstance(tileEntity))
                        {
                            Object pipe = clazzPipeTile.getField("pipe").get(tileEntity);
                            if (clazzPipeWood.isInstance(pipe))
                            {
                                continue;
                            }
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                }

                //New BC API
                if (EnergyConfigHandler.getBuildcraftVersion() == 6 && MjAPI.getMjBattery(tileEntity, MjAPI.DEFAULT_POWER_FRAMEWORK, direction.getOpposite()) != null)
                {
                    adjacentConnections[direction.ordinal()] = tileEntity;
                }

                //Legacy BC API
                if (tileEntity instanceof IPowerReceptor && ((IPowerReceptor) tileEntity).getPowerReceiver(direction.getOpposite()) != null)
                {
                    adjacentConnections[direction.ordinal()] = tileEntity;
                }
            }
        }

        return adjacentConnections;
    }
    
    public static float otherModsEnergyTransfer(TileEntity tileAdj, ForgeDirection inputAdj, float toSend, boolean simulate)
    {
        if (isMekLoaded && tileAdj instanceof IStrictEnergyAcceptor)
        {
            IStrictEnergyAcceptor tileMek = (IStrictEnergyAcceptor) tileAdj;
            if (tileMek.canReceiveEnergy(inputAdj))
            {
            	float transferredMek;
            	if (simulate)
            		transferredMek = tileMek.canReceiveEnergy(inputAdj) ? (float) (tileMek.getMaxEnergy() - tileMek.getEnergy()) : 0F;
            	else
            		transferredMek = (float) tileMek.transferEnergyToAcceptor(inputAdj, toSend * EnergyConfigHandler.TO_MEKANISM_RATIO);
                return transferredMek / EnergyConfigHandler.TO_MEKANISM_RATIO;
            }
        }
        else if (isIC2Loaded && tileAdj instanceof IEnergySink)
        {
            double demanded = 0;
        	try
            {
                demanded = (Double) EnergyUtil.demandedEnergyIC2.invoke(tileAdj);
            }
            catch (Exception ex)
            {
                if (ConfigManagerCore.enableDebug)
                {
                    ex.printStackTrace();
                }
            }
            double energySendingIC2 = Math.min(toSend * EnergyConfigHandler.TO_IC2_RATIO, demanded);
            if (energySendingIC2 >= 1D)
            {
            	double result = 0;
                try
                {
                    if (EnergyUtil.voltageParameterIC2)
                    {
                        result = energySendingIC2 - (Double) EnergyUtil.injectEnergyIC2.invoke(tileAdj, inputAdj, energySendingIC2, 120D);
                    }
                    else
                    {
                        result = energySendingIC2 - (Double) EnergyUtil.injectEnergyIC2.invoke(tileAdj, inputAdj, energySendingIC2);
                    }
                }
                catch (Exception ex)
                {
                    if (ConfigManagerCore.enableDebug)
                    {
                        ex.printStackTrace();
                    }
                }
                if (result < 0D)
                {
                    result = 0D;
                }
                return (float) result / EnergyConfigHandler.TO_IC2_RATIO;
            }
        }
        else if (isRF1Loaded && tileAdj instanceof IEnergyHandler)
        {
        	return ((IEnergyHandler)tileAdj).receiveEnergy(inputAdj, MathHelper.floor_float(toSend * EnergyConfigHandler.TO_RF_RATIO), simulate) / EnergyConfigHandler.TO_RF_RATIO;
        }
        else if (isRF2Loaded && tileAdj instanceof IEnergyReceiver)
        {
        	return ((IEnergyReceiver)tileAdj).receiveEnergy(inputAdj, MathHelper.floor_float(toSend * EnergyConfigHandler.TO_RF_RATIO), simulate) / EnergyConfigHandler.TO_RF_RATIO;
        }
        else if (isBC6Loaded && MjAPI.getMjBattery(tileAdj, MjAPI.DEFAULT_POWER_FRAMEWORK, inputAdj) != null)
        //New BC API
        {
            double toSendBC = Math.min(toSend * EnergyConfigHandler.TO_BC_RATIO, MjAPI.getMjBattery(tileAdj, MjAPI.DEFAULT_POWER_FRAMEWORK, inputAdj).getEnergyRequested());
            if (simulate)
            {
                return (float) toSendBC / EnergyConfigHandler.TO_BC_RATIO;
            }
            return (float) MjAPI.getMjBattery(tileAdj, MjAPI.DEFAULT_POWER_FRAMEWORK, inputAdj).addEnergy(toSendBC) / EnergyConfigHandler.TO_BC_RATIO;
        }
        else if (isBCLoaded && tileAdj instanceof IPowerReceptor)
        //Legacy BC API
        {
            PowerReceiver receiver = ((IPowerReceptor) tileAdj).getPowerReceiver(inputAdj);
            if (receiver != null)
            {
                double toSendBC = Math.min(toSend * EnergyConfigHandler.TO_BC_RATIO, Math.min(receiver.powerRequest(), receiver.getMaxEnergyReceived()));
                if (simulate)
                {
                    return (float) toSendBC / EnergyConfigHandler.TO_BC_RATIO;
                }
                float rec = (float) receiver.receiveEnergy(buildcraft.api.power.PowerHandler.Type.PIPE, toSendBC, inputAdj); 
                return rec / EnergyConfigHandler.TO_BC_RATIO;
            }
        }
        
        return 0F;
    }

    /**
     * Test whether an energy connection can be made to a tile using other mods' energy methods.
     * 
     * Parameters:
     * @param tileAdj - the tile under test, it might be an energy tile from another mod
     * @param inputAdj - the energy input side for that tile which is under test
     */
	public static boolean otherModCanReceive(TileEntity tileAdj, ForgeDirection inputAdj)
	{
        if (isMekLoaded && tileAdj instanceof IStrictEnergyAcceptor)
        {
            return ((IStrictEnergyAcceptor) tileAdj).canReceiveEnergy(inputAdj);
        }
        else if (isIC2Loaded && tileAdj instanceof IEnergyAcceptor)
        {
            return ((IEnergyAcceptor) tileAdj).acceptsEnergyFrom(null, inputAdj);
        }
        else if (isRF1Loaded && tileAdj instanceof IEnergyHandler || isRF2Loaded && tileAdj instanceof IEnergyReceiver)
        {
        	return ((IEnergyConnection)tileAdj).canConnectEnergy(inputAdj);
        }
        else if (isBC6Loaded && MjAPI.getMjBattery(tileAdj, MjAPI.DEFAULT_POWER_FRAMEWORK, inputAdj) != null)
        //New BC API
        {
            return true;
        }
        else if (isBCLoaded && tileAdj instanceof IPowerReceptor)
        //Legacy BC API
        {
            return ((IPowerReceptor) tileAdj).getPowerReceiver(inputAdj) != null;
        }
		
		return false;
	}

    public static boolean initialiseIC2Methods()
    {
    	//Initialise a couple of non-IC2 classes
    	try {
        	clazzMekCable = Class.forName("codechicken.multipart.TileMultipart");
        } catch (Exception e) { }
        try {
        	clazzEnderIOCable = Class.forName("crazypants.enderio.conduit.TileConduitBundle");
        } catch (Exception e) { }
        try {
        	clazzPipeTile = Class.forName("buildcraft.transport.TileGenericPipe");
        } catch (Exception e) { }
        try {
        	clazzPipeWood = Class.forName("buildcraft.transport.pipes.PipePowerWood");
        } catch (Exception e) { }

        if (isIC2Loaded)
        {
            if (ConfigManagerCore.enableDebug)
            {
                GCLog.info("Debug: Initialising IC2 methods OK");
            }
            try
            {
                Class<?> clazz = Class.forName("ic2.api.energy.tile.IEnergySink");

                if (ConfigManagerCore.enableDebug)
                {
                    GCLog.info("Debug: Found IC2 IEnergySink class OK");
                }

                try
                {
                    //1.7.2 version
                    EnergyUtil.demandedEnergyIC2 = clazz.getMethod("demandedEnergyUnits");
                }
                catch (Exception e)
                {
                	//if that fails, try 1.7.10 version
                    try
                    {
                        EnergyUtil.demandedEnergyIC2 = clazz.getMethod("getDemandedEnergy");
                    }
                    catch (Exception ee)
                    {
                        ee.printStackTrace();
                    }
                }

                if (ConfigManagerCore.enableDebug)
                {
                    GCLog.info("Debug: Set IC2 demandedEnergy method OK");
                }

                try
                {
                    //1.7.2 version
                    EnergyUtil.injectEnergyIC2 = clazz.getMethod("injectEnergyUnits", ForgeDirection.class, double.class);
                    if (ConfigManagerCore.enableDebug)
                    {
                        GCLog.info("Debug: IC2 inject 1.7.2 succeeded");
                    }
                }
                catch (Exception e)
                {
                    //if that fails, try 1.7.10 version
                    try
                    {
                        EnergyUtil.injectEnergyIC2 = clazz.getMethod("injectEnergy", ForgeDirection.class, double.class, double.class);
                        EnergyUtil.voltageParameterIC2 = true;
                        if (ConfigManagerCore.enableDebug)
                        {
                            GCLog.info("Debug: IC2 inject 1.7.10 succeeded");
                        }
                    }
                    catch (Exception ee)
                    {
                        ee.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }
}
