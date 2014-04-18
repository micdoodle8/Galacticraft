package micdoodle8.mods.galacticraft.mars.util;

import java.util.HashMap;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerParachest;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerRocketRefill;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.recipe.GCCoreNasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.mars.entities.EntityLandingBalloons;
import micdoodle8.mods.galacticraft.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.mars.inventory.GCMarsContainerSlimeling;
import micdoodle8.mods.galacticraft.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

/**
 * GCMarsUtil.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsUtil
{
	public static void addRocketBenchT2Recipe(ItemStack result, HashMap<Integer, ItemStack> input)
	{
		GalacticraftRegistry.addT2RocketRecipe(new GCCoreNasaWorkbenchRecipe(result, input));
	}

	public static void adCargoRocketRecipe(ItemStack result, HashMap<Integer, ItemStack> input)
	{
		GalacticraftRegistry.addCargoRocketRecipe(new GCCoreNasaWorkbenchRecipe(result, input));
	}

	public static void openParachestInventory(EntityPlayerMP player, EntityLandingBalloons landerInv)
	{
		player.getNextWindowId();
		player.closeContainer();
		int windowId = player.currentWindowId;
		GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_PARACHEST_GUI, new Object[] { windowId, 1, landerInv.getEntityId() }), player);
		player.openContainer = new GCCoreContainerParachest(player.inventory, landerInv);
		player.openContainer.windowId = windowId;
		player.openContainer.addCraftingToCrafters(player);
	}

	public static void openSlimelingInventory(EntityPlayerMP player, EntitySlimeling slimeling)
	{
		player.getNextWindowId();
		player.closeContainer();
		int windowId = player.currentWindowId;
		GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMars(EnumSimplePacketMars.C_OPEN_CUSTOM_GUI, new Object[] { windowId, 0, slimeling.getEntityId() }), player);
		player.openContainer = new GCMarsContainerSlimeling(player.inventory, slimeling);
		player.openContainer.windowId = windowId;
		player.openContainer.addCraftingToCrafters(player);
	}

	public static void openCargoRocketInventory(EntityPlayerMP player, EntityCargoRocket rocket)
	{
		player.getNextWindowId();
		player.closeContainer();
		int windowId = player.currentWindowId;
		GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMars(EnumSimplePacketMars.C_OPEN_CUSTOM_GUI, new Object[] { windowId, 1, rocket.getEntityId() }), player);
		player.openContainer = new GCCoreContainerRocketRefill(player.inventory, rocket, rocket.rocketType);
		player.openContainer.windowId = windowId;
		player.openContainer.addCraftingToCrafters(player);
	}
}
