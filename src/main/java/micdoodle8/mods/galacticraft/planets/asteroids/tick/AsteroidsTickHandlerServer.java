package micdoodle8.mods.galacticraft.planets.asteroids.tick;

import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.ShortRangeTelepadHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AsteroidsTickHandlerServer
{
    public static ShortRangeTelepadHandler spaceRaceData = null;
//    public static Map<Integer, List<BlockPos>> requestedMinerBaseFacingUpdates = new ConcurrentHashMap<Integer, List<BlockPos>>();

    public static void restart()
    {
        spaceRaceData = null;
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event)
    {
    	MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
    	//Prevent issues when clients switch to LAN servers
    	if (server == null) return;

    	if (event.phase == TickEvent.Phase.START)
        {
            if (AsteroidsTickHandlerServer.spaceRaceData == null)
            {
                World world = server.worldServerForDimension(0);
                AsteroidsTickHandlerServer.spaceRaceData = (ShortRangeTelepadHandler) world.getMapStorage().loadData(ShortRangeTelepadHandler.class, ShortRangeTelepadHandler.saveDataID);

                if (AsteroidsTickHandlerServer.spaceRaceData == null)
                {
                    AsteroidsTickHandlerServer.spaceRaceData = new ShortRangeTelepadHandler(ShortRangeTelepadHandler.saveDataID);
                    world.getMapStorage().setData(ShortRangeTelepadHandler.saveDataID, AsteroidsTickHandlerServer.spaceRaceData);
                }
            }

//            for (Integer dimension : requestedMinerBaseFacingUpdates.keySet())
//            {
//                World world = server.worldServerForDimension(dimension);
//
//                if (world == null)
//                {
//                    continue;
//                }
//
////                Iterator<BlockPos> minerBaseLocations = requestedMinerBaseFacingUpdates.get(dimension).iterator();
////
////                while (minerBaseLocations.hasNext())
////                {
////                    minerBaseLocations.next();
//////                    TileEntity tile = world.getTileEntity();
//////                    if (tile instanceof TileEntityMinerBase)
//////                    {
//////                        ((TileEntityMinerBase)tile).updateClientFlag = true;
//////                    }
////                    minerBaseLocations.remove();
////                }
//            }
        }
    }

//    public static void requestMinerBaseFacingUpdate(int dimension, BlockPos blockPos)
//    {
//        if (requestedMinerBaseFacingUpdates.containsKey(dimension))
//        {
//            requestedMinerBaseFacingUpdates.get(dimension).add(blockPos);
//        }
//        else
//        {
//            List<BlockPos> blockPositions = Lists.newArrayList();
//            blockPositions.add(blockPos);
//            requestedMinerBaseFacingUpdates.put(dimension, blockPositions);
//        }
//    }
}
