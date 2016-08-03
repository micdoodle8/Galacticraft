package codechicken.lib.world;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ChunkWatchEvent.UnWatch;
import net.minecraftforge.event.world.ChunkWatchEvent.Watch;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;

public class WorldExtensionManager {
    public static class WorldExtensionEventHandler {
        @SubscribeEvent
        public void onChunkDataLoad(ChunkDataEvent.Load event) {
            if (!worldMap.containsKey(event.world)) {
                WorldExtensionManager.onWorldLoad(event.world);
            }

            createChunkExtension(event.world, event.getChunk());

            for (WorldExtension extension : worldMap.get(event.world)) {
                extension.loadChunkData(event.getChunk(), event.getData());
            }
        }

        @SubscribeEvent
        public void onChunkDataSave(ChunkDataEvent.Save event) {
            for (WorldExtension extension : worldMap.get(event.world)) {
                extension.saveChunkData(event.getChunk(), event.getData());
            }

            if (!event.getChunk().isChunkLoaded) {
                removeChunk(event.world, event.getChunk());
            }
        }

        @SubscribeEvent
        public void onChunkLoad(ChunkEvent.Load event) {
            if (!worldMap.containsKey(event.world)) {
                WorldExtensionManager.onWorldLoad(event.world);
            }

            createChunkExtension(event.world, event.getChunk());

            for (WorldExtension extension : worldMap.get(event.world)) {
                extension.loadChunk(event.getChunk());
            }
        }

        @SubscribeEvent
        public void onChunkUnLoad(ChunkEvent.Unload event) {
            if (event.getChunk() instanceof EmptyChunk) {
                return;
            }

            for (WorldExtension extension : worldMap.get(event.world)) {
                extension.unloadChunk(event.getChunk());
            }

            if (event.world.isRemote) {
                removeChunk(event.world, event.getChunk());
            }
        }

        @SubscribeEvent
        public void onWorldSave(WorldEvent.Save event) {
            if (worldMap.containsKey(event.world)) {
                for (WorldExtension extension : worldMap.get(event.world)) {
                    extension.save();
                }
            }
        }

        @SubscribeEvent
        public void onWorldLoad(WorldEvent.Load event) {
            if (!worldMap.containsKey(event.world)) {
                WorldExtensionManager.onWorldLoad(event.world);
            }
        }

        @SubscribeEvent
        public void onWorldUnLoad(WorldEvent.Unload event) {
            if (worldMap.containsKey(event.world))//because force closing unloads a world twice
            {
                for (WorldExtension extension : worldMap.remove(event.world)) {
                    extension.unload();
                }
            }
        }

        @SubscribeEvent
        public void onChunkWatch(Watch event) {
            Chunk chunk = event.player.worldObj.getChunkFromChunkCoords(event.chunk.chunkXPos, event.chunk.chunkZPos);
            for (WorldExtension extension : worldMap.get(event.player.worldObj)) {
                extension.watchChunk(chunk, event.player);
            }
        }

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public void onChunkUnWatch(UnWatch event) {
            Chunk chunk = event.player.worldObj.getChunkFromChunkCoords(event.chunk.chunkXPos, event.chunk.chunkZPos);
            for (WorldExtension extension : worldMap.get(event.player.worldObj)) {
                extension.unwatchChunk(chunk, event.player);
            }
        }

        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public void clientTick(TickEvent.ClientTickEvent event) {
            World world = Minecraft.getMinecraft().theWorld;
            if (worldMap.containsKey(world)) {
                if (event.phase == TickEvent.Phase.START) {
                    preTick(world);
                } else {
                    postTick(world);
                }
            }
        }

        @SubscribeEvent
        public void serverTick(TickEvent.WorldTickEvent event) {
            if (!worldMap.containsKey(event.world)) {
                WorldExtensionManager.onWorldLoad(event.world);
            }

            if (event.phase == TickEvent.Phase.START) {
                preTick(event.world);
            } else {
                postTick(event.world);
            }
        }
    }

    private static boolean initialised;
    private static ArrayList<WorldExtensionInstantiator> extensionIntialisers = new ArrayList<WorldExtensionInstantiator>();

    public static void registerWorldExtension(WorldExtensionInstantiator init) {
        if (!initialised) {
            init();
        }

        init.instantiatorID = extensionIntialisers.size();
        extensionIntialisers.add(init);
    }

    private static void init() {
        initialised = true;
        MinecraftForge.EVENT_BUS.register(new WorldExtensionEventHandler());
        //FMLCommonHandler.instance().bus().register(new WorldExtensionEventHandler());
    }

    private static HashMap<World, WorldExtension[]> worldMap = new HashMap<World, WorldExtension[]>();

    private static void onWorldLoad(World world) {
        WorldExtension[] extensions = new WorldExtension[extensionIntialisers.size()];
        for (int i = 0; i < extensions.length; i++) {
            extensions[i] = extensionIntialisers.get(i).createWorldExtension(world);
        }

        worldMap.put(world, extensions);

        for (WorldExtension extension : extensions) {
            extension.load();
        }
    }

    private static void createChunkExtension(World world, Chunk chunk) {
        WorldExtension[] extensions = worldMap.get(world);
        for (int i = 0; i < extensionIntialisers.size(); i++) {
            if (!extensions[i].containsChunk(chunk)) {
                extensions[i].addChunk(extensionIntialisers.get(i).createChunkExtension(chunk, extensions[i]));
            }
        }
    }

    private static void removeChunk(World world, Chunk chunk) {
        for (WorldExtension extension : worldMap.get(world)) {
            extension.remChunk(chunk);
        }
    }

    private static void preTick(World world) {
        for (WorldExtension extension : worldMap.get(world)) {
            extension.preTick();
        }
    }

    private static void postTick(World world) {
        for (WorldExtension extension : worldMap.get(world)) {
            extension.postTick();
        }
    }

    public static WorldExtension getWorldExtension(World world, int instantiatorID) {
        return worldMap.get(world)[instantiatorID];
    }
}
