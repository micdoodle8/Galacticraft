package codechicken.lib.world;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

import net.minecraft.client.Minecraft;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.ChunkWatchEvent.UnWatch;
import net.minecraftforge.event.world.ChunkWatchEvent.Watch;

public class WorldExtensionManager
{    
    public static class WorldExtensionEventHandler
    {
        @ForgeSubscribe
        public void onChunkDataLoad(ChunkDataEvent.Load event)
        {
            if(!worldMap.containsKey(event.world))
                WorldExtensionManager.onWorldLoad(event.world);
            
            createChunkExtension(event.world, event.getChunk());

            for(WorldExtension extension : worldMap.get(event.world))
                extension.loadChunkData(event.getChunk(), event.getData());
        }

        @ForgeSubscribe
        public void onChunkDataSave(ChunkDataEvent.Save event)
        {
            for(WorldExtension extension : worldMap.get(event.world))
                extension.saveChunkData(event.getChunk(), event.getData());
            
            if(!event.getChunk().isChunkLoaded)
                removeChunk(event.world, event.getChunk());
        }
        
        @ForgeSubscribe
        public void onChunkLoad(ChunkEvent.Load event)
        {
            if(!worldMap.containsKey(event.world))
                WorldExtensionManager.onWorldLoad(event.world);
            
            createChunkExtension(event.world, event.getChunk());
            
            for(WorldExtension extension : worldMap.get(event.world))
                extension.loadChunk(event.getChunk());
        }

        @ForgeSubscribe
        public void onChunkUnLoad(ChunkEvent.Unload event)
        {
            if(event.getChunk() instanceof EmptyChunk)
                return;
            
            for(WorldExtension extension : worldMap.get(event.world))
                extension.unloadChunk(event.getChunk());
            
            if(event.world.isRemote)
                removeChunk(event.world, event.getChunk());
        }

        @ForgeSubscribe
        public void onWorldSave(WorldEvent.Save event)
        {
            if(worldMap.containsKey(event.world))
                for(WorldExtension extension : worldMap.get(event.world))
                    extension.save();
        }

        @ForgeSubscribe
        public void onWorldLoad(WorldEvent.Load event)
        {
            if(!worldMap.containsKey(event.world))
                WorldExtensionManager.onWorldLoad(event.world);
        }

        @ForgeSubscribe
        public void onWorldUnLoad(WorldEvent.Unload event)
        {
            if(worldMap.containsKey(event.world))//because force closing unloads a world twice
                for(WorldExtension extension : worldMap.remove(event.world))
                    extension.unload();
        }
        
        @ForgeSubscribe
        public void onChunkWatch(Watch event)
        {            
            Chunk chunk = event.player.worldObj.getChunkFromChunkCoords(event.chunk.chunkXPos, event.chunk.chunkZPos);
            for(WorldExtension extension : worldMap.get(event.player.worldObj))
                extension.watchChunk(chunk, event.player);
        }

        @ForgeSubscribe
        public void onChunkUnWatch(UnWatch event)
        {
            Chunk chunk = event.player.worldObj.getChunkFromChunkCoords(event.chunk.chunkXPos, event.chunk.chunkZPos);
            for(WorldExtension extension : worldMap.get(event.player.worldObj))
                extension.unwatchChunk(chunk, event.player);
        }
    }
    
    public static class WorldExtensionClientTickHandler implements ITickHandler
    {
        @Override
        public void tickStart(EnumSet<TickType> type, Object... tickData)
        {
            if(type.contains(TickType.CLIENT))
            {
                World world = Minecraft.getMinecraft().theWorld;
                if(worldMap.containsKey(world))
                    preTick(world);
            }
        }

        @Override
        public void tickEnd(EnumSet<TickType> type, Object... tickData)
        {
            if(type.contains(TickType.CLIENT))
            {
                World world = Minecraft.getMinecraft().theWorld;
                if(worldMap.containsKey(world))
                    postTick(world);
            }
        }

        @Override
        public EnumSet<TickType> ticks()
        {
            return EnumSet.of(TickType.CLIENT);
        }

        @Override
        public String getLabel()
        {
            return "WorldExtenstions";
        }
    }
    
    public static class WorldExtensionServerTickHandler implements ITickHandler
    {
        @Override
        public void tickStart(EnumSet<TickType> type, Object... tickData)
        {
            if(type.contains(TickType.WORLD))
            {
                preTick((World)tickData[0]);
            }
        }

        @Override
        public void tickEnd(EnumSet<TickType> type, Object... tickData)
        {
            if(type.contains(TickType.WORLD))
            {
                postTick((World)tickData[0]);
            }
        }

        @Override
        public EnumSet<TickType> ticks()
        {
            return EnumSet.of(TickType.WORLD, TickType.CLIENT);
        }

        @Override
        public String getLabel()
        {
            return "WorldExtenstions";
        }
    }
    
    private static boolean initialised;
    private static ArrayList<WorldExtensionInstantiator> extensionIntialisers = new ArrayList<WorldExtensionInstantiator>();
    
    public static void registerWorldExtension(WorldExtensionInstantiator init)
    {
        if(!initialised)
            init();
        
        init.instantiatorID = extensionIntialisers.size();
        extensionIntialisers.add(init);
    }

    private static void init()
    {
        initialised = true;
        MinecraftForge.EVENT_BUS.register(new WorldExtensionEventHandler());
        TickRegistry.registerTickHandler(new WorldExtensionServerTickHandler(), Side.SERVER);
        if(FMLCommonHandler.instance().getSide().isClient())
        {
            TickRegistry.registerTickHandler(new WorldExtensionClientTickHandler(), Side.CLIENT);
        }
    }

    private static HashMap<World, WorldExtension[]> worldMap = new HashMap<World, WorldExtension[]>();
    
    private static void onWorldLoad(World world)
    {
        WorldExtension[] extensions = new WorldExtension[extensionIntialisers.size()];
        for(int i = 0; i < extensions.length; i++)
            extensions[i] = extensionIntialisers.get(i).createWorldExtension(world);
        
        worldMap.put(world, extensions);
        
        for(WorldExtension extension : extensions)
            extension.load();
    }

    private static void createChunkExtension(World world, Chunk chunk)
    {
        WorldExtension[] extensions = worldMap.get(world);
        for(int i = 0; i < extensionIntialisers.size(); i++)
            if(!extensions[i].containsChunk(chunk))
                extensions[i].addChunk(extensionIntialisers.get(i).createChunkExtension(chunk, extensions[i]));
    }
    
    private static void removeChunk(World world, Chunk chunk)
    {
        for(WorldExtension extension : worldMap.get(world))
            extension.remChunk(chunk);
    }
    
    private static void preTick(World world)
    {
        for(WorldExtension extension : worldMap.get(world))
            extension.preTick();
    }
    
    private static void postTick(World world)
    {
        for(WorldExtension extension : worldMap.get(world))
            extension.postTick();
    }

    public static WorldExtension getWorldExtension(World world, int instantiatorID)
    {
        return worldMap.get(world)[instantiatorID];
    }
}
