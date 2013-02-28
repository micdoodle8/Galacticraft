package micdoodle8.mods.galacticraft.io.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.EnumSet;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IMapPlanet;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GCCoreLocalization;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.io.blocks.GCIoBlocks;
import micdoodle8.mods.galacticraft.io.dimension.GCIoWorldProvider;
import micdoodle8.mods.galacticraft.titan.client.ClientProxyTitan;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxyIo implements IGalacticraftSubModClient
{
	public static GCCoreLocalization lang = new GCCoreLocalization("micdoodle8/mods/galacticraft/io/client");

	@Override
	public String getDimensionName()
	{
		return "Io";
	}
	
	public void preInit(FMLPreInitializationEvent event)
	{
	}

	public void init(FMLInitializationEvent event)
	{
		GalacticraftCore.registerClientSubMod(this);
		TickRegistry.registerTickHandler(new TickHandlerClient(), Side.CLIENT);
        NetworkRegistry.instance().registerChannel(new ClientPacketHandler(), "GcIo", Side.CLIENT);
		MinecraftForgeClient.preloadTexture("/micdoodle8/mods/galacticraft/io/client/blocks/io.png");
		MinecraftForgeClient.preloadTexture("/micdoodle8/mods/galacticraft/io/client/items/io.png");
	}

	public void postInit(FMLPostInitializationEvent event)
	{
		GCIoBlocks.addNames();
	}
	
	@Override
	public GCCoreLocalization getLanguageFile()
	{
		return ClientProxyIo.lang;
	}

	@Override
	public String getPlanetSpriteDirectory()
	{
		return "/micdoodle8/mods/galacticraft/io/client/planets/";
	}

	@Override
	public IPlanetSlotRenderer getSlotRenderer()
	{
		return new GCIoSlotRenderer();
	}
	
    public class ClientPacketHandler implements IPacketHandler
    {
        @Override
        public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player p)
        {
            final DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
            final int packetType = PacketUtil.readPacketID(data);
            final EntityPlayer player = (EntityPlayer)p;
            
            if (packetType == 0)
            {
            	
            }
        }
    }
	
	public static class TickHandlerClient implements ITickHandler
    {
    	@Override
    	public void tickStart(EnumSet<TickType> type, Object... tickData)
        {
    		ClientProxyTitan.getCurrentTime = System.currentTimeMillis();
    		
    		final Minecraft minecraft = FMLClientHandler.instance().getClient();
    		
            final WorldClient world = minecraft.theWorld;
            
            final EntityClientPlayerMP player = minecraft.thePlayer;
    		
    		if (type.equals(EnumSet.of(TickType.CLIENT)))
            {
    			if (world != null && world.provider instanceof GCIoWorldProvider)
    			{
    				if (world.provider.getSkyRenderer() == null)
                    {
    					world.provider.setSkyRenderer(new GCIoSkyProvider());
                    }
    			}
            }
        }

    	@Override
    	public void tickEnd(EnumSet<TickType> type, Object... tickData)
    	{
    	}
    	
        @Override
		public String getLabel()
        {
            return "Galacticraft Io Client";
        }

    	@Override
    	public EnumSet<TickType> ticks()
    	{
    		return EnumSet.of(TickType.CLIENT);
    	}
    }

	@Override
	public IMapPlanet getPlanetForMap()
	{
		return null;
	}

	@Override
	public IMapPlanet[] getChildMapPlanets()
	{
		return null;
	}

	@Override
	public String getPathToMusicFile()
	{
		return null;
	}
}
