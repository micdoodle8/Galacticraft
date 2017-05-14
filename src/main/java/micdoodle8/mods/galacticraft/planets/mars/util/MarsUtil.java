package micdoodle8.mods.galacticraft.planets.mars.util;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.ContainerParaChest;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRocketInventory;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.recipe.NasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityLandingBalloons;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerLaunchControllerAdvanced;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerSlimeling;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityLaunchController;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

import java.util.HashMap;

public class MarsUtil
{
    public static void addRocketBenchT2Recipe(ItemStack result, HashMap<Integer, ItemStack> input)
    {
        GalacticraftRegistry.addT2RocketRecipe(new NasaWorkbenchRecipe(result, input));
    }

    public static void adCargoRocketRecipe(ItemStack result, HashMap<Integer, ItemStack> input)
    {
        GalacticraftRegistry.addCargoRocketRecipe(new NasaWorkbenchRecipe(result, input));
    }

    public static void openParachestInventory(EntityPlayerMP player, EntityLandingBalloons landerInv)
    {
        player.getNextWindowId();
        player.closeContainer();
        int windowId = player.currentWindowId;
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_PARACHEST_GUI, GCCoreUtil.getDimensionID(player.worldObj), new Object[] { windowId, 1, landerInv.getEntityId() }), player);
        player.openContainer = new ContainerParaChest(player.inventory, landerInv, player);
        player.openContainer.windowId = windowId;
        player.openContainer.onCraftGuiOpened(player);
    }

    public static void openSlimelingInventory(EntityPlayerMP player, EntitySlimeling slimeling)
    {
        player.getNextWindowId();
        player.closeContainer();
        int windowId = player.currentWindowId;
        GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMars(EnumSimplePacketMars.C_OPEN_CUSTOM_GUI, GCCoreUtil.getDimensionID(player.worldObj), new Object[] { windowId, 0, slimeling.getEntityId() }), player);
        player.openContainer = new ContainerSlimeling(player.inventory, slimeling, player);
        player.openContainer.windowId = windowId;
        player.openContainer.onCraftGuiOpened(player);
    }

    public static void openCargoRocketInventory(EntityPlayerMP player, EntityCargoRocket rocket)
    {
        player.getNextWindowId();
        player.closeContainer();
        int windowId = player.currentWindowId;
        GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMars(EnumSimplePacketMars.C_OPEN_CUSTOM_GUI, GCCoreUtil.getDimensionID(player.worldObj), new Object[] { windowId, 1, rocket.getEntityId() }), player);
        player.openContainer = new ContainerRocketInventory(player.inventory, rocket, rocket.rocketType, player);
        player.openContainer.windowId = windowId;
        player.openContainer.onCraftGuiOpened(player);
    }

    public static void openAdvancedLaunchController(EntityPlayerMP player, TileEntityLaunchController launchController)
    {
        player.getNextWindowId();
        player.closeContainer();
        int windowId = player.currentWindowId;
        GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMars(EnumSimplePacketMars.C_OPEN_CUSTOM_GUI_TILE, GCCoreUtil.getDimensionID(player.worldObj), new Object[] { windowId, 0, launchController.getPos() }), player);
        player.openContainer = new ContainerLaunchControllerAdvanced(player.inventory, launchController, player);
        player.openContainer.windowId = windowId;
        player.openContainer.onCraftGuiOpened(player);
    }
}
