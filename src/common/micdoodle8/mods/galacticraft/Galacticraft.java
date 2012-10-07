package micdoodle8.mods.galacticraft;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import micdoodle8.mods.galacticraft.client.GCGuiAirDistributor;
import micdoodle8.mods.galacticraft.client.GCGuiTankRefill;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.Packet9Respawn;
import net.minecraft.src.ServerPlayerAPI;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraftforge.common.DimensionManager;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
@Mod(name="Galacticraft", version="v1", useMetadata = false, modid = "Galacticraft")
@NetworkMod(channels = {"Galacticraft"}, clientSideRequired = true, serverSideRequired = false)
public class Galacticraft 
{
	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.client.ClientProxy", serverSide = "micdoodle8.mods.galacticraft.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance("Galacticraft")
	public static Galacticraft instance;
	
	public long tick;
	
	public static List serverPlayerBaseList = new ArrayList();
	public static List serverPlayerAPIs = new ArrayList();
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		new GCConfigManager(event.getSuggestedConfigurationFile());
		
		GCBlocks.initBlocks();
		GCBlocks.registerBlocks();
		GCBlocks.setHarvestLevels();
		GCBlocks.addNames();
		
		GCItems.initItems();
		GCItems.addNames();
		
		proxy.preInit(event);
		
		try
		{
			ServerPlayerAPI.register("Galacticraft", GCPlayerBaseServer.class);
		}
		catch(Exception e)
		{
			FMLLog.severe("PLAYER API NOT INSTALLED.");
			FMLLog.severe("Galacticraft will now fail to load.");
			e.printStackTrace();
		}
	}
	
	@Init
	public void init(FMLInitializationEvent event)
	{
		DimensionManager.registerProviderType(GCConfigManager.dimensionIDMars, GCWorldProvider.class, true);
		DimensionManager.registerDimension(GCConfigManager.dimensionIDMars, GCConfigManager.dimensionIDMars);
		NetworkRegistry.instance().registerGuiHandler(this, proxy);
		this.registerTileEntities();
		this.registerCreatures();
		this.registerOtherEntities();
		proxy.init(event);
	}
	
	public void registerTileEntities()
	{
        GameRegistry.registerTileEntity(GCTileEntityTreasureChest.class, "Treasure Chest");
        GameRegistry.registerTileEntity(GCTileEntityAirDistributor.class, "Air Distributor");
        LanguageRegistry.instance().addStringLocalization("container.airdistributor", "en_US", "Oxygen Distributor");
	}
	
	public void registerCreatures()
	{
		registerGalacticraftCreature(GCEntityCreeperBoss.class, "Creeper Boss", 202, 894731, 0);
		registerGalacticraftCreature(GCEntitySpider.class, "Evolved Spider", 205, 3419431, 11013646);
		registerGalacticraftCreature(GCEntityZombie.class, "Evolved Zombie", 206, 44975, 7969893);
		registerGalacticraftCreature(GCEntityCreeper.class, "Evolved Creeper", 207, 894731, 0);
		registerGalacticraftCreature(GCEntitySkeleton.class, "Evolved Skeleton", 208, 12698049, 4802889);
	}
	
	public void registerOtherEntities()
	{
        EntityRegistry.registerModEntity(GCEntityProjectileTNT.class, "Projectile TNT", 203, this, 150, 5, true);
        EntityRegistry.registerModEntity(GCEntitySpaceship.class, "Spaceship", 204, this, 150, 5, true);
        EntityRegistry.registerModEntity(GCEntityArrow.class, "Gravity Arrow", 209, this, 150, 5, true);
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
		proxy.registerRenderInformation();
	}
	
	@ServerStarted
	public void serverInit(FMLServerStartedEvent event)
	{
        TickRegistry.registerScheduledTickHandler(new DimensionSyncServer(), Side.SERVER);
        TickRegistry.registerTickHandler(new CommonTickHandler(), Side.SERVER);
        NetworkRegistry.instance().registerChannel(new ServerPacketHandler(), "Galacticraft", Side.SERVER);
	}

    public void registerGalacticraftCreature(Class var0, String var1, int var2, int back, int fore)
    {
        EntityRegistry.registerGlobalEntityID(var0, var1, var2, back, fore);
        EntityRegistry.registerModEntity(var0, var1, var2, instance, 80, 3, true);
		LanguageRegistry.instance().addStringLocalization("entity." + var1 + ".name", "en_US", var1);
    }
	
	public class CommonTickHandler implements ITickHandler
	{
		@Override
		public void tickStart(EnumSet<TickType> type, Object... tickData) 
		{
			if (type.equals(EnumSet.of(TickType.PLAYER)))
            {
				tick++;
				
				if (!GCConfigManager.loaded)
				{
					return;
				}
				
				if (FMLCommonHandler.instance().getMinecraftServerInstance() == null || FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager() == null || FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList == null)
				{
					return;
				}
				
		        for (int i = 0; i < FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList.size(); ++i)
		        {
		            EntityPlayerMP entityplayermp = (EntityPlayerMP)  FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList.get(i);

		            if (entityplayermp == null || entityplayermp.worldObj == null)
		            {
		            	break;
		            }
		            
		            if (entityplayermp.dimension == GCConfigManager.dimensionIDMars && entityplayermp.inventory.getCurrentItem() != null && entityplayermp.inventory.getCurrentItem().getItem().shiftedIndex == Block.torchWood.blockID)
		            {
		            	int par1 = entityplayermp.inventory.getCurrentItem().stackSize;
		            	ItemStack stack = new ItemStack(GCBlocks.unlitTorch, par1, 0);
		            	
		                entityplayermp.inventory.mainInventory[entityplayermp.inventory.currentItem] = stack;
		            }
		            else if (entityplayermp.dimension != GCConfigManager.dimensionIDMars && entityplayermp.inventory.getCurrentItem() != null && entityplayermp.inventory.getCurrentItem().getItem().shiftedIndex == GCBlocks.unlitTorch.blockID)
		            {
		            	int par1 = entityplayermp.inventory.getCurrentItem().stackSize;
		            	ItemStack stack = new ItemStack(Block.torchWood, par1, 0);
		            	
		                entityplayermp.inventory.mainInventory[entityplayermp.inventory.currentItem] = stack;
		            }
		            
		            if (entityplayermp.timeInPortal > 0.8F && entityplayermp.timeUntilPortal == 0)
		            {
		            	byte var5;
		            	
		                if (entityplayermp.dimension == GCConfigManager.dimensionIDMars)
		                {
		                    var5 = 0;
		                }
		                else
		                {
		                    var5 = (byte) GCConfigManager.dimensionIDMars;
		                }

		                entityplayermp.mcServer.getConfigurationManager().transferPlayerToDimension(entityplayermp, var5, new GCTeleporter());
		                entityplayermp.timeUntilPortal = 10;
		                entityplayermp.timeInPortal = 0.0F;
		                
		                try
		                {
		                	ObfuscationReflectionHelper.setPrivateValue(EntityPlayer.class, entityplayermp, false, 31);
		                }
		                catch (Exception e)
		                {
		                	e.printStackTrace();
		                }
		            }
		        }
            }
		}

		@Override
		public void tickEnd(EnumSet<TickType> type, Object... tickData) { }

		@Override
		public EnumSet<TickType> ticks() 
		{
			return EnumSet.of(TickType.PLAYER);
		}

		@Override
		public String getLabel() 
		{
			return "Galacticraft Common";
		}
	}
	
    public class ServerPacketHandler implements IPacketHandler
    {
        @Override
        public void onPacketData(NetworkManager manager, Packet250CustomPayload packet, Player p)
        {
            DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
            int packetType = GCUtil.readPacketID(data);
            EntityPlayerMP player = (EntityPlayerMP)p;
            
            if (packetType == 0)
            {
                Class[] decodeAs = {String.class};
                Object[] packetReadout = GCUtil.readPacketData(data, decodeAs);

                player.openGui(instance, GCConfigManager.idGuiTankRefill, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
            }
            else if (packetType == 1)
            {
                Class[] decodeAs = {String.class};
                Object[] packetReadout = GCUtil.readPacketData(data, decodeAs);
                
                player.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(player.dimension, (byte)player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.worldObj.getHeight(), player.theItemInWorldManager.getGameType()));
            }
            else if (packetType == 2)
            {
                Class[] decodeAs = {Integer.class};
                Object[] packetReadout = GCUtil.readPacketData(data, decodeAs);
                
                for (int j = 0; j < Galacticraft.instance.serverPlayerAPIs.size(); ++j)
	            {
	    			GCPlayerBaseServer playerBase = (GCPlayerBaseServer) Galacticraft.instance.serverPlayerAPIs.get(j);
	    			
	    			if (player.username == playerBase.getPlayer().username)
	    			{
	            		playerBase.timeUntilPortal = 15;
	    				playerBase.setInPortal((Integer)packetReadout[0]);
	    			}
	            }
            }
            else if (packetType == 3)
            {
                if (!player.worldObj.isRemote && !player.isDead && player.ridingEntity != null && !player.ridingEntity.isDead && player.ridingEntity instanceof GCEntitySpaceship)
                {
                	GCEntitySpaceship ship = (GCEntitySpaceship) player.ridingEntity;
                	
                	ship.ignite();
                }
            }
        }
    }
    
    public class DimensionSyncServer implements IScheduledTickHandler
    {
    	@Override
    	public void tickStart(EnumSet<TickType> type, Object... tickData) 
    	{
    		if (FMLCommonHandler.instance().getMinecraftServerInstance() == null || FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager() == null || FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList == null)
    		{
    			return;
    		}
    		
            for (int var3 = 0; var3 < FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList.size(); ++var3)
            {
                EntityPlayerMP var4 = (EntityPlayerMP) FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList.get(var3);
                
                if (var4 == null)
                {
                	return;
                }

                Object[] toSend = {var4.dimension};
                FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerForUsername(var4.username).playerNetServerHandler.sendPacketToPlayer(GCUtil.createPacket("Galacticraft", 1, toSend));
            }
    	}

    	@Override
    	public void tickEnd(EnumSet<TickType> type, Object... tickData) { }

    	@Override
    	public EnumSet<TickType> ticks() 
    	{
    		return EnumSet.of(TickType.PLAYER);
    	}

    	@Override
    	public String getLabel() 
    	{
    		return null;
    	}

    	@Override
    	public int nextTickSpacing() 
    	{
    		return 20;
    	}
    }
}
