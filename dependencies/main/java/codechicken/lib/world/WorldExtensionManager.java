package codechicken.lib.world;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
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
            if (!worldMap.containsKey(event.getWorld())) {
                WorldExtensionManager.onWorldLoad(event.getWorld());
            }

            createChunkExtension(event.getWorld(), event.getChunk());

            for (WorldExtension extension : worldMap.get(event.getWorld())) {
                extension.loadChunkData(event.getChunk(), event.getData());
            }
        }

        @SubscribeEvent
        public void onChunkDataSave(ChunkDataEvent.Save event) {
            for (WorldExtension extension : worldMap.get(event.getWorld())) {
                extension.saveChunkData(event.getChunk(), event.getData());
            }

            if (!event.getChunk().isLoaded()) {
                removeChunk(event.getWorld(), event.getChunk());
            }
        }

        @SubscribeEvent
        public void onChunkLoad(ChunkEvent.Load event) {
            if (!worldMap.containsKey(event.getWorld())) {
                WorldExtensionManager.onWorldLoad(event.getWorld());
            }

            createChunkExtension(event.getWorld(), event.getChunk());

            for (WorldExtension extension : worldMap.get(event.getWorld())) {
                extension.loadChunk(event.getChunk());
            }
        }

        @SubscribeEvent
        public void onChunkUnLoad(ChunkEvent.Unload event) {
            if (event.getChunk().isEmpty()) {
                return;
            }
            //TODO Maybe gate against worldMap.get returning null. Some dimension may be doing stupid things.
            for (WorldExtension extension : worldMap.get(event.getWorld())) {
                extension.unloadChunk(event.getChunk());
            }

            if (event.getWorld().isRemote) {
                removeChunk(event.getWorld(), event.getChunk());
            }
        }

        @SubscribeEvent
        public void onWorldSave(WorldEvent.Save event) {
            if (worldMap.containsKey(event.getWorld())) {
                for (WorldExtension extension : worldMap.get(event.getWorld())) {
                    extension.save();
                }
            }
        }

        @SubscribeEvent
        public void onWorldLoad(WorldEvent.Load event) {
            if (!worldMap.containsKey(event.getWorld())) {
                WorldExtensionManager.onWorldLoad(event.getWorld());
            }
        }

        @SubscribeEvent
        public void onWorldUnLoad(WorldEvent.Unload event) {
            if (worldMap.containsKey(event.getWorld()))//because force closing unloads a world twice
            {
                for (WorldExtension extension : worldMap.remove(event.getWorld())) {
                    extension.unload();
                }
            }
        }

        @SubscribeEvent
        public void onChunkWatch(Watch event) {
            Chunk chunk = event.getPlayer().worldObj.getChunkFromChunkCoords(event.getChunk().chunkXPos, event.getChunk().chunkZPos);
            for (WorldExtension extension : worldMap.get(event.getPlayer().worldObj)) {
                extension.watchChunk(chunk, event.getPlayer());
            }
        }

        @SubscribeEvent
        @SideOnly (Side.CLIENT)
        public void onChunkUnWatch(UnWatch event) {
            Chunk chunk = event.getPlayer().worldObj.getChunkFromChunkCoords(event.getChunk().chunkXPos, event.getChunk().chunkZPos);
            for (WorldExtension extension : worldMap.get(event.getPlayer().worldObj)) {
                extension.unwatchChunk(chunk, event.getPlayer());
            }
        }

        @SubscribeEvent
        @SideOnly (Side.CLIENT)
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
