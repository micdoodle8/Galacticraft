package micdoodle8.mods.galacticraft.core;

import java.util.List;

import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiAirDistributor;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiBuggyBench;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiRocketBench;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiTankRefill;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityPlayer;
import micdoodle8.mods.galacticraft.core.tile.GCCoreContainerAirDistributor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreContainerBuggyBench;
import micdoodle8.mods.galacticraft.core.tile.GCCoreContainerRocketBench;
import micdoodle8.mods.galacticraft.core.tile.GCCoreContainerTankRefill;
import micdoodle8.mods.galacticraft.core.tile.GCCoreInventoryTankRefill;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.StatBase;
import net.minecraft.src.World;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class CommonProxyCore implements IGuiHandler
{
	public void preInit(FMLPreInitializationEvent event)
	{
		
	}
	
	public void init(FMLInitializationEvent event)
	{
		
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
		
	}
	
	public void registerRenderInformation()
	{
		
	}

	public void addStat(EntityPlayer player, StatBase stat, int i)
	{
		
	}
	
	public int getGCUnlitTorchRenderID()
	{
		return -1;
	}
	
	public int getGCBreathableAirRenderID()
	{
		return -1;
	}
	
	public int getGCOxygenPipeRenderID()
	{
		return -1;
	}

	public int getGCTreasureChestRenderID() 
	{
		return -1;
	}

	public int getGCMeteorRenderID()
	{
		return -1;
	}
	
	public World getClientWorld()
	{
		return null;
	}
	
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean b) {}
	
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, double var13, double var14, double var15, boolean b) {}
    
    
    // IGUIHANDLER IMPLEMENTATION:
    
    GCCoreInventoryTankRefill inv = null;

    public static List<GCCoreInventoryTankRefill> airTanks = Lists.newArrayList();
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
	{
		if (ID == GCCoreConfigManager.idGuiTankRefill)
		{
			for (int i = 0; i < GalacticraftCore.gcPlayers.size(); ++i)
	        {
				GCCoreEntityPlayer gcPlayer = (GCCoreEntityPlayer) GalacticraftCore.gcPlayers.get(i);
				
				if (player.username == gcPlayer.getPlayer().username)
				{
					return new GCCoreContainerTankRefill(player.inventory, gcPlayer.playerTankInventory);
				}
	        }
		}
		else if (ID == GCCoreConfigManager.idGuiAirDistributor)
		{
			GCCoreTileEntityOxygenDistributor distributor = (GCCoreTileEntityOxygenDistributor) world.getBlockTileEntity(x, y, z);

			return new GCCoreContainerAirDistributor(player.inventory, distributor);
		}
		else if (ID == GCCoreConfigManager.idGuiRocketCraftingBench)
		{
			return new GCCoreContainerRocketBench(player.inventory);
		}
		else if (ID == GCCoreConfigManager.idGuiBuggyCraftingBench)
		{
			return new GCCoreContainerBuggyBench(player.inventory);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if (ID == GCCoreConfigManager.idGuiTankRefill)
		{
			return new GCCoreGuiTankRefill(player);
		}
		else if (ID == GCCoreConfigManager.idGuiAirDistributor)
		{
			GCCoreTileEntityOxygenDistributor distributor = (GCCoreTileEntityOxygenDistributor) world.getBlockTileEntity(x, y, z);
			
			return new GCCoreGuiAirDistributor(player.inventory, distributor);
		}
		else if (ID == GCCoreConfigManager.idGuiRocketCraftingBench)
		{
			return new GCCoreGuiRocketBench(player.inventory);
		}
		else if (ID == GCCoreConfigManager.idGuiBuggyCraftingBench)
		{
			return new GCCoreGuiBuggyBench(player.inventory);
		}
		else
		{
			return null;
		}
	}
}
