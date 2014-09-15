package micdoodle8.mods.galacticraft.core.energy;

import java.lang.reflect.Method;

import buildcraft.api.mj.MjAPI;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import mekanism.api.energy.ICableOutputter;
import mekanism.api.energy.IStrictEnergyAcceptor;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class EnergyUtil
{
    private static boolean voltageParameterIC2 = false;
    private static Method injectEnergyIC2 = null;
    private static boolean isMekLoaded = EnergyConfigHandler.isMekanismLoaded();
    //boolean isTELoaded = EnergyConfigHandler.isThermalExpansionLoaded();
    private static boolean isIC2Loaded = EnergyConfigHandler.isIndustrialCraft2Loaded();
    private static boolean isBCLoaded = EnergyConfigHandler.isBuildcraftLoaded();
    private static boolean isBCReallyLoaded = EnergyConfigHandler.isBuildcraftReallyLoaded();
    private static boolean initialisedIC2Methods = EnergyUtil.initialiseIC2Methods();
    
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
                try
                {
                    if (Class.forName("codechicken.multipart.TileMultipart").isInstance(tileEntity))
                    {
                        continue;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                if (tileEntity instanceof IStrictEnergyAcceptor && ((IStrictEnergyAcceptor) tileEntity).canReceiveEnergy(direction.getOpposite()))
                {
                    adjacentConnections[direction.ordinal()] = tileEntity;
                }
                else if (tileEntity instanceof ICableOutputter && ((ICableOutputter) tileEntity).canOutputTo(direction.getOpposite()))
                {
                    adjacentConnections[direction.ordinal()] = tileEntity;
                }
            }
            /*else if (isTELoaded && tileEntity instanceof IEnergyHandler)
			{
				if (((IEnergyHandler) tileEntity).canInterface(direction.getOpposite()))
				{
					adjacentConnections[direction.ordinal()] = tileEntity;
				}
			}*/
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
                        Class<?> clazzPipeTile = Class.forName("buildcraft.transport.TileGenericPipe");
                        if (clazzPipeTile.isInstance(tileEntity))
                        {
                            Class<?> clazzPipeWood = Class.forName("buildcraft.transport.pipes.PipePowerWood");
                            Object pipe = clazzPipeTile.getField("pipe").get(tileEntity);
                            if (clazzPipeWood.isInstance(pipe))
                            {
                                continue;
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
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
                return transferredMek * EnergyConfigHandler.MEKANISM_RATIO;
            }
        }
        else if (isIC2Loaded && tileAdj instanceof IEnergySink)
        {
            double energySendingIC2 = toSend * EnergyConfigHandler.TO_IC2_RATIO;
            if (energySendingIC2 >= 1D)
            {
                double result = 0;
                try
                {
                    if (EnergyUtil.voltageParameterIC2)
                    {
                        result = (Double) EnergyUtil.injectEnergyIC2.invoke(tileAdj, inputAdj, energySendingIC2, 120D);
                    }
                    else
                    {
                        result = (Double) EnergyUtil.injectEnergyIC2.invoke(tileAdj, inputAdj, energySendingIC2);
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
                return (float) result * EnergyConfigHandler.IC2_RATIO;
            }
        }
        else if (isBCLoaded && EnergyConfigHandler.getBuildcraftVersion() == 6 && MjAPI.getMjBattery(tileAdj, MjAPI.DEFAULT_POWER_FRAMEWORK, inputAdj) != null)
        //New BC API
        {
            double toSendBC = Math.min(toSend * EnergyConfigHandler.TO_BC_RATIO, MjAPI.getMjBattery(tileAdj, MjAPI.DEFAULT_POWER_FRAMEWORK, inputAdj).getEnergyRequested());
            if (simulate)
            {
                return (float) toSendBC * EnergyConfigHandler.BC3_RATIO;
            }
            return (float) MjAPI.getMjBattery(tileAdj, MjAPI.DEFAULT_POWER_FRAMEWORK, inputAdj).addEnergy(toSendBC) * EnergyConfigHandler.BC3_RATIO;
        }
        else if (isBCLoaded && tileAdj instanceof IPowerReceptor)
        //Legacy BC API
        {
            PowerReceiver receiver = ((IPowerReceptor) tileAdj).getPowerReceiver(inputAdj);
            if (receiver != null)
            {
                double toSendBC = Math.min(toSend * EnergyConfigHandler.TO_BC_RATIO, receiver.powerRequest());
                if (simulate)
                {
                    return (float) toSendBC * EnergyConfigHandler.BC3_RATIO;
                }
                return (float) receiver.receiveEnergy(buildcraft.api.power.PowerHandler.Type.PIPE, toSendBC, inputAdj) * EnergyConfigHandler.BC3_RATIO;
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
        else if (isBCLoaded && EnergyConfigHandler.getBuildcraftVersion() == 6 && MjAPI.getMjBattery(tileAdj, MjAPI.DEFAULT_POWER_FRAMEWORK, inputAdj) != null)
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

    private static boolean initialiseIC2Methods()
    {
        if (isIC2Loaded)
        {
            if (ConfigManagerCore.enableDebug)
            {
                GCLog.info("Debug UN: Initialising IC2 methods");
            }
            try
            {
                Class<?> clazz = Class.forName("ic2.api.energy.tile.IEnergySink");

                if (ConfigManagerCore.enableDebug)
                {
                    GCLog.info("Debug UN: Found IC2 IEnergySink class");
                }

                try
                {
                    //1.7.2 version
                    EnergyUtil.injectEnergyIC2 = clazz.getMethod("injectEnergyUnits", ForgeDirection.class, double.class);
                    if (ConfigManagerCore.enableDebug)
                    {
                        GCLog.info("Debug UN: IC2 inject 1.7.2 succeeded");
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
                            GCLog.info("Debug UN: IC2 inject 1.7.10 succeeded");
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
