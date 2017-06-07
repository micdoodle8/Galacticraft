package micdoodle8.mods.galacticraft.core.energy;

import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.*;
import ic2.api.item.IElectricItem;
import ic2.api.item.ISpecialElectricItem;
import mekanism.api.energy.ICableOutputter;
import mekanism.api.energy.IEnergizedItem;
import mekanism.api.energy.IStrictEnergyAcceptor;
import micdoodle8.mods.galacticraft.api.item.IItemElectric;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConnector;
import micdoodle8.mods.galacticraft.api.transmission.tile.IElectrical;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.energy.tile.EnergyStorageTile;
import micdoodle8.mods.galacticraft.core.energy.tile.TileBaseConductor;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.GCLog;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class EnergyUtil
{
    private static boolean isMekLoaded = EnergyConfigHandler.isMekanismLoaded();
    private static boolean isRFLoaded = EnergyConfigHandler.isRFAPILoaded();
    private static boolean isRF1Loaded = EnergyConfigHandler.isRFAPIv1Loaded();
    private static boolean isRF2Loaded = EnergyConfigHandler.isRFAPIv2Loaded();
    private static boolean isIC2Loaded = EnergyConfigHandler.isIndustrialCraft2Loaded();
    private static boolean isIC2TileLoaded = false;
    private static boolean isBCReallyLoaded = EnergyConfigHandler.isBuildcraftReallyLoaded();

    public static boolean voltageParameterIC2 = false;
    public static Method demandedEnergyIC2 = null;
    public static Method injectEnergyIC2 = null;
    public static Method offeredEnergyIC2 = null;
    public static Method drawEnergyIC2 = null;
    public static Class<?> clazzIC2EnergyTile = null;
    public static Class<?> clazzIC2Cable = null;
    public static Class<?> clazzMekCable = null;
    public static Class<?> clazzEnderIOCable = null;
    public static Class<?> clazzMFRRednetEnergyCable = null;
    public static Class<?> clazzRailcraftEngine = null;
    private static Class<?> clazzPipeTile = null;
    private static Class<?> clazzPipeWood = null;
    public static boolean initialisedIC2Methods = EnergyUtil.initialiseIC2Methods();
    private static Capability<IStrictEnergyAcceptor> mekCableAcceptor = null;

    public static TileEntity[] getAdjacentPowerConnections(TileEntity tile)
    {
        final TileEntity[] adjacentConnections = new TileEntity[6];

        BlockVec3 thisVec = new BlockVec3(tile);
        for (EnumFacing direction : EnumFacing.VALUES)
        {
        	if (tile instanceof IConductor && !((IConductor)tile).canConnect(direction, NetworkType.POWER))
            {
                continue;
            }
        	
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
                        continue;
                    }
                }
                catch (Exception e)
                {
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

            if (isIC2Loaded)
            {
                if (tileEntity instanceof IEnergyConductor)
                {
                    continue;
                }

                if(!tile.getWorld().isRemote)
                {
                    Object IC2tile = tileEntity;
                    BlockPos checkingIC2 = thisVec.toBlockPos().offset(direction);
                    try {
                        IC2tile = EnergyNet.instance.getSubTile(tile.getWorld(), checkingIC2);
                    } catch (Exception e) { e.printStackTrace(); }

                    if (IC2tile instanceof IEnergyAcceptor && tile instanceof IEnergyEmitter)
                    {
                        if (((IEnergyAcceptor) IC2tile).acceptsEnergyFrom((IEnergyEmitter) tile, direction.getOpposite()))
                        {
                            adjacentConnections[direction.ordinal()] = tileEntity;
                        }
                        continue;
                    }
                    if (IC2tile instanceof IEnergyEmitter && tile instanceof IEnergyAcceptor)
                    {
                        if (((IEnergyEmitter) IC2tile).emitsEnergyTo((IEnergyAcceptor) tile, direction.getOpposite()))
                        {
                            adjacentConnections[direction.ordinal()] = tileEntity;
                        }
                        continue;
                    }
                }
                else
                {
                    try {
                        Class clazz = tileEntity.getClass();
                        if (clazz.getName().startsWith("ic2"))
                        {
                            //Special case: IC2's transformers don't seem to setup their sink and source directions in Energy clientside
                            if (clazz.getName().startsWith("ic2.core.block.wiring.TileEntityTransformer"))
                            {
                                adjacentConnections[direction.ordinal()] = tileEntity;
                                continue;
                            }
                            
                            Field energyField = null;
                            fieldLoop:
                            while (energyField == null && clazz != null)
                            {
                                for (Field f : clazz.getDeclaredFields())
                                {
                                    if (f.getName().equals("energy"))
                                    {
                                        energyField = f;
                                        break fieldLoop;
                                    }
                                }
                                clazz = clazz.getSuperclass();
                            }
                            energyField.setAccessible(true);
                            Object energy = energyField.get(tileEntity);
                            Set <EnumFacing> connections;
                            if (tile instanceof IEnergyEmitter)
                            {
                                connections = (Set<EnumFacing>) energy.getClass().getMethod("getSinkDirs").invoke(energy);
                                if (connections.contains(direction.getOpposite()))
                                {
                                    adjacentConnections[direction.ordinal()] = tileEntity;
                                    continue;
                                }
                            }
                            if (tile instanceof IEnergyAcceptor)
                            {
                                connections = (Set<EnumFacing>) energy.getClass().getMethod("getSourceDirs").invoke(energy);
                                if (connections.contains(direction.getOpposite()))
                                {
                                    adjacentConnections[direction.ordinal()] = tileEntity;
                                    continue;
                                }
                            }
                        }
                    } catch (Exception e) { e.printStackTrace(); }                   
                }
            }
        }

        return adjacentConnections;
    }
    /**
     * Similar to getAdjacentPowerConnections but specific to energy receivers only
     * Adds the adjacent power connections found to the passed acceptors, directions parameter Lists
     * (Note: an acceptor can therefore sometimes be entered in the Lists more than once, with a different direction each time:
     * this would represent GC wires connected to the acceptor on more than one side.)
     * 
     * @param conductor
     * @param connectedAcceptors
     * @param directions
     * @throws Exception
     */
    public static void setAdjacentPowerConnections(TileEntity conductor, List<Object> connectedAcceptors, List <EnumFacing> directions) throws Exception
    {
        final BlockVec3 thisVec = new BlockVec3(conductor);
        final World world = conductor.getWorld(); 
        for (EnumFacing direction : EnumFacing.VALUES)
        {
            TileEntity tileEntity = thisVec.getTileEntityOnSide(world, direction);
            
            if (tileEntity == null || tileEntity instanceof IConductor)  //world.getTileEntity will not have returned an invalid tile, invalid tiles are null
            {
            	continue;
            }
            
            EnumFacing sideFrom = direction.getOpposite();

            if (tileEntity instanceof IElectrical)
            {
                if (((IElectrical) tileEntity).canConnect(sideFrom, NetworkType.POWER))
                {
                    connectedAcceptors.add(tileEntity);
                    directions.add(sideFrom);
                }
                continue;
            }
            
            if (isMekLoaded && tileEntity instanceof IStrictEnergyAcceptor)
            {
                if (clazzMekCable != null && clazzMekCable.isInstance(tileEntity))
                {
                    continue;
                }
                if (((IStrictEnergyAcceptor) tileEntity).canReceiveEnergy(sideFrom))
                {
                    connectedAcceptors.add(tileEntity);
                    directions.add(sideFrom);
                }
                continue;
            }
            
            if (isBCReallyLoaded && clazzPipeTile.isInstance(tileEntity))
            {
            	continue;
            }

            if (isIC2Loaded && !world.isRemote)
            {
                IEnergyTile IC2tile = null;
                BlockPos checkingIC2 = thisVec.toBlockPos().offset(direction);
                try {
                    IC2tile = EnergyNet.instance.getSubTile(world, checkingIC2);
                } catch (Exception e) { e.printStackTrace(); }

                if (IC2tile instanceof IEnergyConductor)
                {
                    continue;
                }
                if (IC2tile instanceof IEnergyAcceptor && ((IEnergyAcceptor) IC2tile).acceptsEnergyFrom((IEnergyEmitter) conductor, sideFrom))
                {
                    connectedAcceptors.add(IC2tile);
                    directions.add(sideFrom);
                }
                continue;
            }
            
            if ((isRF2Loaded && tileEntity instanceof IEnergyReceiver) || (isRF1Loaded && tileEntity instanceof IEnergyHandler))
            {
            	if (clazzEnderIOCable != null && clazzEnderIOCable.isInstance(tileEntity))
            	{
            		continue;
            	}
            	if (clazzMFRRednetEnergyCable != null && clazzMFRRednetEnergyCable.isInstance(tileEntity))
            	{
            		continue;
            	}

            	if (((IEnergyConnection) tileEntity).canConnectEnergy(sideFrom))
            	{
            		connectedAcceptors.add(tileEntity);
            		directions.add(sideFrom);
            	}
            	continue;
            }
        }
        return;
    }
    
    public static float otherModsEnergyTransfer(TileEntity tileAdj, EnumFacing inputAdj, float toSend, boolean simulate)
    {
        if (isMekLoaded && !EnergyConfigHandler.disableMekanismOutput)
        {
            IStrictEnergyAcceptor tileMek = null;
            if (tileAdj instanceof IStrictEnergyAcceptor)
            {
                tileMek = (IStrictEnergyAcceptor) tileAdj;
            }
            else if (mekCableAcceptor != null && hasCapability(tileAdj, mekCableAcceptor, inputAdj))
            {
                tileMek = getCapability(tileAdj, mekCableAcceptor, inputAdj);
            }

            if (tileMek != null && tileMek.canReceiveEnergy(inputAdj))
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
            //TODO: need to use new subTile system
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

        if (isMekLoaded)
        {
            try
            {
                Class mekCap = Class.forName("mekanism.common.capabilities.Capabilities");
                EnergyUtil.mekCableAcceptor = (Capability) mekCap.getField("ENERGY_ACCEPTOR_CAPABILITY").get(null);
            }
            catch (Exception e)
            {
            }
        }

        if (isIC2Loaded)
        {
            GCLog.debug("Initialising IC2 methods OK");

            try
            {
                clazzIC2EnergyTile = Class.forName("ic2.core.energy.Tile");
                if (clazzIC2EnergyTile != null) isIC2TileLoaded = true;
                
               clazzIC2Cable = Class.forName("ic2.api.energy.tile.IEnergyConductor");
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
                        GCLog.debug("Set IC2 injectEnergy method OK");
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
        if (clazzPipeTile == null)
        	isBCReallyLoaded = false;
        
        return true;
    }
    
    public static boolean isElectricItem(Item item)
    {
        if (item instanceof IItemElectric)
            return true;
        
        if (item == null)
            return false;

        if (EnergyConfigHandler.isRFAPILoaded())
        {
            if (item instanceof IEnergyContainerItem)
                return true;
        }
        if (EnergyConfigHandler.isIndustrialCraft2Loaded())
        {
            if (item instanceof IElectricItem)
                return true;
            if (item instanceof ISpecialElectricItem)
                return true;
        }
        if (EnergyConfigHandler.isMekanismLoaded())
        {
            if (item instanceof IEnergizedItem)
                return true;
        }
                    
        return false;
    }
    
    public static boolean isChargedElectricItem(ItemStack stack)
    {
        if (stack == null)
            return false;

        Item item = stack.getItem();
        if (item instanceof IItemElectric)
        {
            return ((IItemElectric) item).getElectricityStored(stack) > 0;
        }

        if (item == null)
            return false;

        if (EnergyConfigHandler.isRFAPILoaded())
        {
            if (item instanceof IEnergyContainerItem)
                return ((IEnergyContainerItem)item).getEnergyStored(stack) > 0;
        }

        if (EnergyConfigHandler.isIndustrialCraft2Loaded())
        {
            if (item instanceof ISpecialElectricItem)
            {
                ISpecialElectricItem electricItem = (ISpecialElectricItem) item;
                return electricItem.canProvideEnergy(stack);
//TODO            return (Info.itemInfo.getEnergyValue(stack) > 0.0D) || (ElectricItem.manager.discharge(stack, Double.POSITIVE_INFINITY, this.tier, true, true, true) > 0.0D);
//See also: 
            }
            else if (item instanceof IElectricItem)
            {
                IElectricItem electricItem = (IElectricItem) item;
                return electricItem.canProvideEnergy(stack);
//TODO            return (Info.itemInfo.getEnergyValue(stack) > 0.0D) || (ElectricItem.manager.discharge(stack, Double.POSITIVE_INFINITY, this.tier, true, true, true) > 0.0D);
            }
        }

        if (EnergyConfigHandler.isMekanismLoaded())
        {
            if (item instanceof IEnergizedItem)
                return ((IEnergizedItem)item).getEnergy(stack) > 0;
        }
                    
        return false;
    }

    public static boolean isFillableElectricItem(ItemStack stack)
    {
        if (stack == null)
            return false;

        Item item = stack.getItem();
        if (item instanceof IItemElectric)
        {
            return ((IItemElectric) item).getElectricityStored(stack) < ((IItemElectric) item).getMaxElectricityStored(stack);
        }

        if (item == null)
            return false;

        if (EnergyConfigHandler.isRFAPILoaded())
        {
            if (item instanceof IEnergyContainerItem)
                return ((IEnergyContainerItem)item).getEnergyStored(stack) < ((IEnergyContainerItem)item).getMaxEnergyStored(stack);
        }

        if (EnergyConfigHandler.isIndustrialCraft2Loaded())
        {
            if (item instanceof ISpecialElectricItem)
            {
                ISpecialElectricItem electricItem = (ISpecialElectricItem) item;
                return electricItem.canProvideEnergy(stack);
//TODO                return ElectricItem.manager.charge(stack, Double.POSITIVE_INFINITY, this.tier, true, true) > 0.0D;
            }
            else if (item instanceof IElectricItem)
            {
                IElectricItem electricItem = (IElectricItem) item;
                return electricItem.canProvideEnergy(stack);
//TODO                return ElectricItem.manager.charge(stack, Double.POSITIVE_INFINITY, this.tier, true, true) > 0.0D;
            }
        }

        if (EnergyConfigHandler.isMekanismLoaded())
        {
            if (item instanceof IEnergizedItem)
                return ((IEnergizedItem)item).getEnergy(stack) < ((IEnergizedItem)item).getMaxEnergy(stack);
        }
                    
        return false;
    }
    
    public static boolean hasCapability(ICapabilityProvider provider, Capability<?> capability, EnumFacing side)
    {
        return (provider == null || capability == null) ? false : provider.hasCapability(capability, side);
    }

    public static <T> T getCapability(ICapabilityProvider provider, Capability<T> capability, EnumFacing side)
    {
        return (provider == null || capability == null) ? null : provider.getCapability(capability, side);
    }
}
