package micdoodle8.mods.galacticraft.core;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import micdoodle8.mods.galacticraft.mars.GCMarsEntityCreeperBoss;
import micdoodle8.mods.galacticraft.mars.GCMarsEntitySludgeling;
import net.minecraft.src.World;
import net.minecraft.src.WorldProviderSurface;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.NetworkMod;
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
@Mod(name="Galacticraft Core", version="v1", useMetadata = false, modid = "GalacticraftCore")
@NetworkMod(channels = {"GalacticraftCore"}, clientSideRequired = true, serverSideRequired = false)
public class GalacticraftCore 
{
	@SidedProxy(clientSide = "micdoodle8.mods.galacticraft.core.client.ClientProxyCore", serverSide = "micdoodle8.mods.galacticraft.core.CommonProxyCore")
	public static CommonProxyCore proxy;
	
	@Instance("GalacticraftCore")
	public static GalacticraftCore instance;
	
	public static GCCoreLocalization lang;
	
	public static long tick;
	
	public static List serverPlayerBaseList = new ArrayList();
	public static List serverPlayerAPIs = new ArrayList();
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event)
	{
		new GCCoreConfigManager(new File(event.getModConfigurationDirectory(), "Galacticraft/core.conf"));
		
		lang = new GCCoreLocalization("micdoodle8/mods/galacticraft/core/client");
		
		GCCoreBlocks.initBlocks();
		GCCoreBlocks.registerBlocks();
		GCCoreBlocks.setHarvestLevels();
		GCCoreBlocks.addNames();
		
		GCCoreItems.initItems();
		GCCoreItems.addNames();
	}
	
	@Init
	public void init(FMLInitializationEvent event)
	{
//		GCCoreUtil.addSmeltingRecipes(); TODO
		this.registerTileEntities();
		this.registerCreatures();
		this.registerOtherEntities();
		proxy.init(event);
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
        TickRegistry.registerTickHandler(new CommonTickHandler(), Side.SERVER);
	}
	
	public void registerTileEntities()
	{
        GameRegistry.registerTileEntity(GCCoreTileEntityTreasureChest.class, "Treasure Chest");
        GameRegistry.registerTileEntity(GCCoreTileEntityOxygenDistributor.class, "Air Distributor");
        LanguageRegistry.instance().addStringLocalization("container.airdistributor", "en_US", "Oxygen Distributor");
	}
	
	public void registerCreatures()
	{
		registerGalacticraftCreature(GCCoreEntitySpider.class, "Evolved Spider", 3419431, 11013646);
		registerGalacticraftCreature(GCCoreEntityZombie.class, "Evolved Zombie", 44975, 7969893);
		registerGalacticraftCreature(GCCoreEntityCreeper.class, "Evolved Creeper", 894731, 0);
		registerGalacticraftCreature(GCCoreEntitySkeleton.class, "Evolved Skeleton", 12698049, 4802889);
		registerGalacticraftCreature(GCMarsEntityCreeperBoss.class, "Creeper Boss", 894731, 0);
		registerGalacticraftCreature(GCMarsEntitySludgeling.class, "Sludgeling", 25600, 0);
	}
	
	public void registerOtherEntities()
	{
		registerGalacticraftNonMobEntity(GCCoreEntitySpaceship.class, "Spaceship", 150, 5, true);
		registerGalacticraftNonMobEntity(GCCoreEntityArrow.class, "Gravity Arrow", 150, 5, true);
	}

    public void registerGalacticraftCreature(Class var0, String var1, int back, int fore)
    {
    	int id = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(var0, var1, id, back, fore);
        EntityRegistry.registerModEntity(var0, var1, id, instance, 80, 3, true);
		LanguageRegistry.instance().addStringLocalization("entity." + var1 + ".name", "en_US", var1);
    }
    
    public void registerGalacticraftNonMobEntity(Class var0, String var1, int trackingDistance, int updateFreq, boolean sendVel)
    {
    	int id = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(var0, var1, id);
        EntityRegistry.registerModEntity(var0, var1, id, this, trackingDistance, updateFreq, sendVel);
    }
	
	public class CommonTickHandler implements ITickHandler
	{
		@Override
		public void tickStart(EnumSet<TickType> type, Object... tickData) 
		{
			if (type.equals(EnumSet.of(TickType.WORLD)))
            {
				World world = (World) tickData[0];
				
				if (world.provider instanceof WorldProviderSurface)
				{
					tick++;
				}
            }
		}

		@Override
		public void tickEnd(EnumSet<TickType> type, Object... tickData) { }

		@Override
		public EnumSet<TickType> ticks() 
		{
			return EnumSet.of(TickType.WORLD);
		}

		@Override
		public String getLabel() 
		{
			return "Galacticraft Core Common";
		}
	}
}
