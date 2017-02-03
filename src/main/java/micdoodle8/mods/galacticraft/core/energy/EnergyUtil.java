package micdoodle8.mods.galacticraft.core.energy;

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
import micdoodle8.mods.galacticraft.core.energy.tile.EnergyStorageTile;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseConductor;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

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
    public static Method offeredEnergyIC2 = null;
    public static Method drawEnergyIC2 = null;
    private static Class<?> clazzMekCable = null;
    public static Class<?> clazzEnderIOCable = null;
    public static Class<?> clazzMFRRednetEnergyCable = null;
    public static Class<?> clazzRailcraftEngine = null;
    private static Class<?> clazzPipeTile = null;
    private static Class<?> clazzPipeWood = null;
    public static boolean initialisedIC2Methods = EnergyUtil.initialiseIC2Methods();

    public static TileEntity[] getAdjacentPowerConnections(TileEntity tile)
    {
        final TileEntity[] adjacentConnections = new TileEntity[6];

        BlockVec3 thisVec = new BlockVec3(tile);
        for (EnumFacing direction : EnumFacing.values())
        {
            TileEntity tileEntity = thisVec.getTileEntityOnSide(tile.getWorld(), direction);

            if (tileEntity == null)
            {
                continue;
            }

            if (tileEntity instanceof IConnector)
            {
                if (((IConnector) tileEntity).canConnect(direction.getOpposite(), NetworkType.POWER))
                {
                    adjacentConnections[direction.ordinal()] = tileEntity;
                }
                continue;
            }

            if (isMekLoaded && (tileEntity instanceof IStrictEnergyAcceptor || tileEntity instanceof ICableOutputter))
            {
                //Do not connect GC wires directly to Mek Universal Cables
                try
                {
                    if (clazzMekCable != null && clazzMekCable.isInstance(tileEntity))
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
                continue;
            }

            if (isBCReallyLoaded)
            {
                //Do not connect GC wires directly to BC pipes of any type
                try
                {
                    if (clazzPipeTile.isInstance(tileEntity))
                    {
//                        Object pipe = clazzPipeTile.getField("pipe").get(tileEntity);
//                        if (clazzPipeWood.isInstance(pipe))
//                        {
                        continue;
//                        }
                    }
                }
                catch (Exception e)
                {
                }
            }

            if (isIC2Loaded && tileEntity instanceof IEnergyTile)
            {
                if (tileEntity instanceof IEnergyConductor)
                {
                    continue;
                }

                boolean doneIC2 = false;
                if (tileEntity instanceof IEnergyAcceptor && tile instanceof IEnergyEmitter)
                {
                    doneIC2 = true;
                    if (((IEnergyAcceptor) tileEntity).acceptsEnergyFrom((IEnergyEmitter) tile, direction.getOpposite()))
                    {
                        adjacentConnections[direction.ordinal()] = tileEntity;
                    }
                }
                if (tileEntity instanceof IEnergyEmitter && tile instanceof IEnergyAcceptor)
                {
                    doneIC2 = true;
                    if (((IEnergyEmitter) tileEntity).emitsEnergyTo((IEnergyAcceptor) tile, direction.getOpposite()))
                    {
                        adjacentConnections[direction.ordinal()] = tileEntity;
                    }
                }

                if (doneIC2)
                {
                    continue;
                }
            }

            if (isRFLoaded && tileEntity instanceof IEnergyConnection)
            {
                if (isRF2Loaded && (tileEntity instanceof IEnergyProvider || tileEntity instanceof IEnergyReceiver) || isRF1Loaded && tileEntity instanceof IEnergyHandler || clazzRailcraftEngine != null && clazzRailcraftEngine.isInstance(tileEntity))
                {
                    //Do not connect GC wires directly to power conduits
                    if (clazzEnderIOCable != null && clazzEnderIOCable.isInstance(tileEntity))
                    {
                        continue;
                    }
                    if (clazzMFRRednetEnergyCable != null && clazzMFRRednetEnergyCable.isInstance(tileEntity))
                    {
                        continue;
                    }

                    if (((IEnergyConnection) tileEntity).canConnectEnergy(direction.getOpposite()))
                    {
                        adjacentConnections[direction.ordinal()] = tileEntity;
                    }
                }
                continue;
            }
        }

        return adjacentConnections;
    }

    public static float otherModsEnergyTransfer(TileEntity tileAdj, EnumFacing inputAdj, float toSend, boolean simulate)
    {
        if (isMekLoaded && !EnergyConfigHandler.disableMekanismOutput && tileAdj instanceof IStrictEnergyAcceptor)
        {
            IStrictEnergyAcceptor tileMek = (IStrictEnergyAcceptor) tileAdj;
            if (tileMek.canReceiveEnergy(inputAdj))
            {
                float transferredMek;
                if (simulate)
                {
                    transferredMek = tileMek.canReceiveEnergy(inputAdj) ? (float) (tileMek.getMaxEnergy() - tileMek.getEnergy()) : 0F;
                }
                else
                {
                    transferredMek = (float) tileMek.transferEnergyToAcceptor(inputAdj, toSend * EnergyConfigHandler.TO_MEKANISM_RATIO);
                }
                return transferredMek / EnergyConfigHandler.TO_MEKANISM_RATIO;
            }
        }
        else if (isIC2Loaded && !EnergyConfigHandler.disableIC2Output && tileAdj instanceof IEnergySink)
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

            if (simulate)
            {
                return Math.min(toSend, (float) demanded / EnergyConfigHandler.TO_IC2_RATIO);
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
                    return 0F;
                }
                return (float) result / EnergyConfigHandler.TO_IC2_RATIO;
            }
        }
        else if (isRF2Loaded && !EnergyConfigHandler.disableRFOutput && tileAdj instanceof IEnergyReceiver)
        {
            float sent = ((IEnergyReceiver) tileAdj).receiveEnergy(inputAdj, (int) Math.floor(toSend * EnergyConfigHandler.TO_RF_RATIO), simulate) / EnergyConfigHandler.TO_RF_RATIO;
//        	GCLog.debug("Beam/storage offering RF2 up to " + toSend + " into pipe, it accepted " + sent);
            return sent;
        }

        return 0F;
    }

    public static float otherModsEnergyExtract(TileEntity tileAdj, EnumFacing inputAdj, float toPull, boolean simulate)
    {
        if (isIC2Loaded && !EnergyConfigHandler.disableIC2Input && tileAdj instanceof IEnergySource)
        {
            double offered = 0;
            try
            {
                offered = (Double) EnergyUtil.offeredEnergyIC2.invoke(tileAdj);
            }
            catch (Exception ex)
            {
                if (ConfigManagerCore.enableDebug)
                {
                    ex.printStackTrace();
                }
            }

            if (simulate)
            {
                return Math.min(toPull, (float) offered / EnergyConfigHandler.TO_IC2_RATIO);
            }

            double energySendingIC2 = Math.min(toPull * EnergyConfigHandler.TO_IC2_RATIO, offered);
            if (energySendingIC2 >= 1D)
            {
                double resultIC2 = 0;
                try
                {
                    resultIC2 = energySendingIC2 - (Double) EnergyUtil.drawEnergyIC2.invoke(tileAdj, energySendingIC2);
                }
                catch (Exception ex)
                {
                    if (ConfigManagerCore.enableDebug)
                    {
                        ex.printStackTrace();
                    }
                }
                if (resultIC2 < 0D)
                {
                    resultIC2 = 0D;
                }
                return (float) resultIC2 / EnergyConfigHandler.TO_IC2_RATIO;
            }
        }
        else if (isRF2Loaded && !EnergyConfigHandler.disableRFInput && tileAdj instanceof IEnergyProvider)
        {
            float sent = ((IEnergyProvider) tileAdj).extractEnergy(inputAdj, (int) Math.floor(toPull * EnergyConfigHandler.TO_RF_RATIO), simulate) / EnergyConfigHandler.TO_RF_RATIO;
            return sent;
        }

        return 0F;
    }


    /**
     * Test whether an energy connection can be made to a tile using other mods' energy methods.
     * <p>
     * Parameters:
     *
     * @param tileAdj  - the tile under test, it might be an energy tile from another mod
     * @param inputAdj - the energy input side for that tile which is under test
     */
    public static boolean otherModCanReceive(TileEntity tileAdj, EnumFacing inputAdj)
    {
        if (tileAdj instanceof TileBaseConductor || tileAdj instanceof EnergyStorageTile)
        {
            return false;  //Do not try using other mods' methods to connect to GC's own tiles
        }

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
            return ((IEnergyConnection) tileAdj).canConnectEnergy(inputAdj);
        }

        return false;
    }

    /**
     * Test whether a tile can output energy using other mods' energy methods.
     * Currently restricted to IC2 and RF mods - Mekanism tiles do not provide an interface to "output" energy
     * <p>
     * Parameters:
     *
     * @param tileAdj - the tile under test, it might be an energy tile from another mod
     * @param side    - the energy output side for that tile which is under test
     */
    public static boolean otherModCanProduce(TileEntity tileAdj, EnumFacing side)
    {
        if (tileAdj instanceof TileBaseConductor || tileAdj instanceof EnergyStorageTile)
        {
            return false;  //Do not try using other mods' methods to connect to GC's own tiles
        }

        if (isIC2Loaded && tileAdj instanceof IEnergyEmitter)
        {
            return ((IEnergyEmitter) tileAdj).emitsEnergyTo(null, side);
        }

        return false;
    }

    public static boolean initialiseIC2Methods()
    {
        //Initialise a couple of non-IC2 classes
        try
        {
            clazzMekCable = Class.forName("codechicken.multipart.TileMultipart");
        }
        catch (Exception e)
        {
        }
        try
        {
            clazzEnderIOCable = Class.forName("crazypants.enderio.conduit.TileConduitBundle");
        }
        catch (Exception e)
        {
        }
        try
        {
            clazzMFRRednetEnergyCable = Class.forName("powercrystals.minefactoryreloaded.tile.rednet.TileEntityRedNetEnergy");
        }
        catch (Exception e)
        {
        }
        try
        {
            clazzRailcraftEngine = Class.forName("mods.railcraft.common.blocks.machine.beta.TileEngine");
        }
        catch (Exception e)
        {
        }
        try
        {
            clazzPipeTile = Class.forName("buildcraft.transport.TileGenericPipe");
        }
        catch (Exception e)
        {
        }
        try
        {
            clazzPipeWood = Class.forName("buildcraft.transport.pipes.PipePowerWood");
        }
        catch (Exception e)
        {
        }

        if (isIC2Loaded)
        {
            GCLog.debug("Initialising IC2 methods OK");

            try
            {
                Class<?> clazz = Class.forName("ic2.api.energy.tile.IEnergySink");

                GCLog.debug("Found IC2 IEnergySink class OK");

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

                GCLog.debug("Set IC2 demandedEnergy method OK");

                try
                {
                    //1.7.2 version
                    EnergyUtil.injectEnergyIC2 = clazz.getMethod("injectEnergyUnits", EnumFacing.class, double.class);
                    GCLog.debug("IC2 inject 1.7.2 succeeded");
                }
                catch (Exception e)
                {
                    //if that fails, try 1.7.10 version
                    try
                    {
                        EnergyUtil.injectEnergyIC2 = clazz.getMethod("injectEnergy", EnumFacing.class, double.class, double.class);
                        EnergyUtil.voltageParameterIC2 = true;
                        GCLog.debug("IC2 inject 1.7.10 succeeded");
                    }
                    catch (Exception ee)
                    {
                        ee.printStackTrace();
                    }
                }

                Class<?> clazzSource = Class.forName("ic2.api.energy.tile.IEnergySource");
                EnergyUtil.offeredEnergyIC2 = clazzSource.getMethod("getOfferedEnergy");
                EnergyUtil.drawEnergyIC2 = clazzSource.getMethod("drawEnergy", double.class);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }
}
