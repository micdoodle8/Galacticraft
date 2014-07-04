package micdoodle8.mods.galacticraft.core.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.EntityLander;
import micdoodle8.mods.galacticraft.core.inventory.ContainerBuggy;
import micdoodle8.mods.galacticraft.core.inventory.ContainerParaChest;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class GCCoreUtil
{
	public static int to32BitColor(int a, int r, int g, int b)
	{
		a = a << 24;
		r = r << 16;
		g = g << 8;

		return a | r | g | b;
	}

	public static void checkVersion(Side side)
	{
		ThreadVersionCheck.startCheck(side);
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
		player.openContainer = new ContainerParaChest(player.inventory, landerInv);
		player.openContainer.windowId = windowId;
		player.openContainer.addCraftingToCrafters(player);
	}

	public static void registerGalacticraftCreature(Class<? extends Entity> var0, String var1, int id, int back, int fore)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			LanguageRegistry.instance().addStringLocalization("entity." + var1 + ".name", GCCoreUtil.translate("entity.GalacticraftCore." + var1 + ".name"));
		}

		EntityRegistry.registerGlobalEntityID(var0, var1, id, back, fore);
		EntityRegistry.registerModEntity(var0, var1, id, GalacticraftCore.instance, 80, 3, true);
	}

	public static void registerGalacticraftNonMobEntity(Class<? extends Entity> var0, String var1, int id, int trackingDistance, int updateFreq, boolean sendVel)
	{
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
		{
			LanguageRegistry.instance().addStringLocalization("entity." + var1 + ".name", GCCoreUtil.translate("entity.GalacticraftCore." + var1 + ".name"));
		}

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

	public static String translate(String key)
	{
		return StatCollector.translateToLocal(key);
	}

	public static String translateWithFormat(String key, Object... values)
	{
		return StatCollector.translateToLocalFormatted(key, values);
	}
}
