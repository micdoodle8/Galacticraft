package micdoodle8.mods.galacticraft.io.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.EnumSet;

import micdoodle8.mods.galacticraft.API.IGalacticraftSubModClient;
import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.GCCoreLocalization;
import micdoodle8.mods.galacticraft.core.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.io.blocks.GCIoBlocks;
import micdoodle8.mods.galacticraft.io.dimension.GCIoWorldProvider;
import micdoodle8.mods.galacticraft.titan.client.ClientProxyTitan;
import micdoodle8.mods.galacticraft.titan.client.ClientProxyTitan.ClientPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.INetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.WorldClient;
import net.minecraftforge.client.MinecraftForgeClient;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.TickRegistry;

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
		return this.lang;
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
            DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
            int packetType = GCCoreUtil.readPacketID(data);
            EntityPlayer player = (EntityPlayer)p;
            
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
    		
    		Minecraft minecraft = FMLClientHandler.instance().getClient();
    		
            WorldClient world = minecraft.theWorld;
            
            EntityClientPlayerMP player = minecraft.thePlayer;
    		
    		if (type.equals(EnumSet.of(TickType.CLIENT)))
            {
    			if (world != null && world.provider instanceof GCIoWorldProvider)
    			{
    				if (world.provider.getSkyProvider() == null)
                    {
    					world.provider.setSkyProvider(new GCIoSkyProvider());
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
}
