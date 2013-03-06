package micdoodle8.mods.galacticraft.core;

import java.util.List;

import micdoodle8.mods.galacticraft.API.IPlanetSlotRenderer;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiAirCompressor;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiBuggyBench;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiGalaxyMap;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiRefinery;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiRocketBench;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiRocketRefill;
import micdoodle8.mods.galacticraft.core.client.gui.GCCoreGuiTankRefill;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpaceship;
import micdoodle8.mods.galacticraft.core.entities.GCCorePlayerBase;
import micdoodle8.mods.galacticraft.core.tile.GCCoreContainerAirCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreContainerBuggyBench;
import micdoodle8.mods.galacticraft.core.tile.GCCoreContainerRefinery;
import micdoodle8.mods.galacticraft.core.tile.GCCoreContainerRocketBench;
import micdoodle8.mods.galacticraft.core.tile.GCCoreContainerRocketRefill;
import micdoodle8.mods.galacticraft.core.tile.GCCoreContainerTankRefill;
import micdoodle8.mods.galacticraft.core.tile.GCCoreInventoryTankRefill;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCompressor;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;

/**
 * Copyright 2012-2013, micdoodle8
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

	public int getGCCraftingTableRenderID()
	{
		return -1;
	}

	public int getGCOxygenDistributorRenderID()
	{
		return -1;
	}

	public int getGCOxygenCollectorRenderID()
	{
		return -1;
	}

	public int getGCCrudeOilRenderID()
	{
		return -1;
	}

	public int getGCRefineryRenderID()
	{
		return -1;
	}

	public int getGCCompressorRenderID()
	{
		return -1;
	}
	
	public World getClientWorld()
	{
		return null;
	}
	
	public void addSlotRenderer(IPlanetSlotRenderer slotRenderer)
	{
		;
	}
	
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, boolean b) {}
	
    public void spawnParticle(String var1, double var2, double var4, double var6, double var8, double var10, double var12, double var13, double var14, double var15, boolean b) {}
    
    
    // IGUIHANDLER IMPLEMENTATION:
    
    GCCoreInventoryTankRefill inv = null;

    public static List<GCCoreInventoryTankRefill> airTanks = Lists.newArrayList();
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		GCCorePlayerBase playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player);
		
		if (ID == GCCoreConfigManager.idGuiTankRefill && playerBase != null)
		{
			return new GCCoreContainerTankRefill(player, playerBase.playerTankInventory);
		}
		else if (ID == GCCoreConfigManager.idGuiRocketCraftingBench)
		{
			return new GCCoreContainerRocketBench(player.inventory, x, y, z);
		}
		else if (ID == GCCoreConfigManager.idGuiBuggyCraftingBench)
		{
			return new GCCoreContainerBuggyBench(player.inventory, x, y, z);
		}
		else if (ID == GCCoreConfigManager.idGuiSpaceshipInventory && player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntitySpaceship)
		{
			return new GCCoreContainerRocketRefill(player.inventory, (GCCoreEntitySpaceship) player.ridingEntity, ((GCCoreEntitySpaceship) player.ridingEntity).getSpaceshipType());
		}
		else if (ID == GCCoreConfigManager.idGuiRefinery)
		{
			return new GCCoreContainerRefinery(player.inventory, (GCCoreTileEntityRefinery)world.getBlockTileEntity(x, y, z));
		}
		else if (ID == GCCoreConfigManager.idGuiAirCompressor)
		{
			return new GCCoreContainerAirCompressor(player.inventory, (GCCoreTileEntityOxygenCompressor)world.getBlockTileEntity(x, y, z));
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getBlockTileEntity(x, y, z);
		
		if (ID == GCCoreConfigManager.idGuiTankRefill)
		{
			return new GCCoreGuiTankRefill(player);
		}
		else if (ID == GCCoreConfigManager.idGuiRocketCraftingBench)
		{
			return new GCCoreGuiRocketBench(player.inventory, x, y, z);
		}
		else if (ID == GCCoreConfigManager.idGuiBuggyCraftingBench)
		{
			return new GCCoreGuiBuggyBench(player.inventory);
		}
		else if (ID == GCCoreConfigManager.idGuiGalaxyMap)
		{
			return new GCCoreGuiGalaxyMap(player);
		}
		else if (ID == GCCoreConfigManager.idGuiSpaceshipInventory && player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntitySpaceship)
		{
			return new GCCoreGuiRocketRefill(player.inventory, (GCCoreEntitySpaceship) player.ridingEntity, ((GCCoreEntitySpaceship) player.ridingEntity).getSpaceshipType());
		}
		else if (ID == GCCoreConfigManager.idGuiRefinery)
		{
			return new GCCoreGuiRefinery(player.inventory, (GCCoreTileEntityRefinery)world.getBlockTileEntity(x, y, z));
		}
		else if (ID == GCCoreConfigManager.idGuiAirCompressor)
		{
			if (tile != null && tile instanceof GCCoreTileEntityOxygenCompressor)
			{
				return new GCCoreGuiAirCompressor(player.inventory, (GCCoreTileEntityOxygenCompressor)tile);
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}
}
