package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.GCCoreThreadVersionCheck;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityLander;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerBuggy;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerParachest;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketHandlerClient.EnumPacketClient;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * GCCoreUtil.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreUtil
{
	public static int convertTo32BitColor(int a, int r, int b, int g)
	{
		a = a << 24;
		r = r << 16;
		g = g << 8;

		return a | r | g | b;
	}

	public static void checkVersion(Side side)
	{
		GCCoreThreadVersionCheck.startCheck(side);
	}

	public static void openBuggyInv(EntityPlayerMP player, IInventory buggyInv, int type)
	{
		player.incrementWindowID();
		player.closeContainer();
		int id = player.currentWindowId;
		player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.OPEN_PARACHEST_GUI, new Object[] { id, 0, 0 }));
		player.openContainer = new GCCoreContainerBuggy(player.inventory, buggyInv, type);
		player.openContainer.windowId = id;
		player.openContainer.addCraftingToCrafters(player);
	}

	public static void openParachestInv(EntityPlayerMP player, GCCoreEntityLander landerInv)
	{
		player.incrementWindowID();
		player.closeContainer();
		int windowId = player.currentWindowId;
		player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, EnumPacketClient.OPEN_PARACHEST_GUI, new Object[] { windowId, 1, landerInv.entityId }));
		player.openContainer = new GCCoreContainerParachest(player.inventory, landerInv);
		player.openContainer.windowId = windowId;
		player.openContainer.addCraftingToCrafters(player);
	}

	public static void registerGalacticraftCreature(Class<? extends Entity> var0, String var1, int id, int back, int fore)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			LanguageRegistry.instance().addStringLocalization("entity." + var1 + ".name", StatCollector.translateToLocal("entity.GalacticraftCore." + var1 + ".name"));
		}

		EntityRegistry.registerGlobalEntityID(var0, var1, id, back, fore);
		EntityRegistry.registerModEntity(var0, var1, id, GalacticraftCore.instance, 80, 3, true);
	}

	public static void registerGalacticraftNonMobEntity(Class<? extends Entity> var0, String var1, int id, int trackingDistance, int updateFreq, boolean sendVel)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			LanguageRegistry.instance().addStringLocalization("entity." + var1 + ".name", StatCollector.translateToLocal("entity.GalacticraftCore." + var1 + ".name"));
		}

		EntityList.addMapping(var0, var1, id);
		EntityRegistry.registerModEntity(var0, var1, id, GalacticraftCore.instance, trackingDistance, updateFreq, sendVel);
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
