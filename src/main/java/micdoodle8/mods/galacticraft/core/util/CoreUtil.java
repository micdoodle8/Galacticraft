package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.inventory.ContainerBuggy;
import micdoodle8.mods.galacticraft.core.inventory.ContainerParachest;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * GCCoreUtil.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class CoreUtil
{
	public static int to32BitColor(int a, int r, int g, int b)
	{
		a = a << 24;
		r = r << 16;
		g = g << 8;

		return a | r | g | b;
	}

	public static void openBuggyInv(EntityPlayerMP player, IInventory buggyInv, int type)
	{
		player.getNextWindowId();
		player.closeContainer();
		int id = player.currentWindowId;
		GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_PARACHEST_GUI, new Object[] { id, 0, 0 }), player);
		player.openContainer = new ContainerBuggy(player.inventory, buggyInv, type);
		player.openContainer.windowId = id;
		player.openContainer.addCraftingToCrafters(player);
	}

	public static void openParachestInv(EntityPlayerMP player, EntityLander landerInv)
	{
		player.getNextWindowId();
		player.closeContainer();
		int windowId = player.currentWindowId;
		GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_PARACHEST_GUI, new Object[] { windowId, 1, landerInv.getEntityId() }), player);
		player.openContainer = new ContainerParachest(player.inventory, landerInv);
		player.openContainer.windowId = windowId;
		player.openContainer.addCraftingToCrafters(player);
	}

    public static void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String id)
    {
    	registerTileEntity(GalacticraftCore.MOD_ID, tileEntityClass, id);
    }

    public static void registerTileEntity(String modID, Class<? extends TileEntity> tileEntityClass, String id)
    {
    	GameRegistry.registerTileEntity(tileEntityClass, modID + ":" + id);
    }

	public static void registerCreature(Class<? extends Entity> entityClass, String entityName, int entityID, int eggColorBack, int eggColorFront)
	{
		registerCreature(GalacticraftCore.instance, entityClass, entityName, entityID, eggColorBack, eggColorFront);
	}

	public static void registerCreature(Object modInstance, Class<? extends Entity> entityClass, String entityName, int entityID, int eggColorBack, int eggColorFront)
	{
		EntityRegistry.registerGlobalEntityID(entityClass, entityName, entityID, eggColorBack, eggColorFront);
		EntityRegistry.registerModEntity(entityClass, entityName, entityID, modInstance, 80, 3, true);
	}

	public static void registerNonMobEntity(Class<? extends Entity> entityClass, String entityName, int entityID, int trackingDistance, int updateFreq, boolean sendVel)
	{
		registerNonMobEntity(GalacticraftCore.instance, entityClass, entityName, entityID, trackingDistance, updateFreq, sendVel);
	}

	public static void registerNonMobEntity(Object modInstance, Class<? extends Entity> entityClass, String entityName, int entityID, int trackingDistance, int updateFreq, boolean sendVel)
	{
		EntityRegistry.registerGlobalEntityID(entityClass, entityName, entityID);
		EntityRegistry.registerModEntity(entityClass, entityName, entityID, GalacticraftCore.instance, trackingDistance, updateFreq, sendVel);
	}

	public static void registerGalacticraftItem(String key, Item item)
	{
		GalacticraftCore.itemList.put(key, new ItemStack(item));
	}

	public static void registerGalacticraftItem(String key, Item item, int metadata)
	{
		GalacticraftCore.itemList.put(key, new ItemStack(item, 1, metadata));
	}

	public static void registerGalacticraftItem(String key, ItemStack stack)
	{
		GalacticraftCore.itemList.put(key, stack);
	}

	public static void registerGalacticraftBlock(String key, Block block)
	{
		GalacticraftCore.blocksList.put(key, new ItemStack(block));
	}

	public static void registerGalacticraftBlock(String key, Block block, int metadata)
	{
		GalacticraftCore.blocksList.put(key, new ItemStack(block, 1, metadata));
	}

	public static void registerGalacticraftBlock(String key, ItemStack stack)
	{
		GalacticraftCore.blocksList.put(key, stack);
	}
}
