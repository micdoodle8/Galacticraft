package micdoodle8.mods.galacticraft.planets.mars.util;

import java.util.HashMap;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.ContainerParaChest;
import micdoodle8.mods.galacticraft.core.inventory.ContainerRocketInventory;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.core.recipe.NasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityLandingBalloons;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySlimeling;
import micdoodle8.mods.galacticraft.planets.mars.inventory.ContainerSlimeling;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars;
import micdoodle8.mods.galacticraft.planets.mars.network.PacketSimpleMars.EnumSimplePacketMars;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

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
        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_PARACHEST_GUI, new Object[] { windowId, 1, landerInv.getEntityId() }), player);
        player.openContainer = new ContainerParaChest(player.inventory, landerInv);
        player.openContainer.windowId = windowId;
        player.openContainer.addCraftingToCrafters(player);
    }

    public static void openSlimelingInventory(EntityPlayerMP player, EntitySlimeling slimeling)
    {
        player.getNextWindowId();
        player.closeContainer();
        int windowId = player.currentWindowId;
        GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMars(EnumSimplePacketMars.C_OPEN_CUSTOM_GUI, new Object[] { windowId, 0, slimeling.getEntityId() }), player);
        player.openContainer = new ContainerSlimeling(player.inventory, slimeling);
        player.openContainer.windowId = windowId;
        player.openContainer.addCraftingToCrafters(player);
    }

    public static void openCargoRocketInventory(EntityPlayerMP player, EntityCargoRocket rocket)
    {
        player.getNextWindowId();
        player.closeContainer();
        int windowId = player.currentWindowId;
        GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMars(EnumSimplePacketMars.C_OPEN_CUSTOM_GUI, new Object[] { windowId, 1, rocket.getEntityId() }), player);
        player.openContainer = new ContainerRocketInventory(player.inventory, rocket, rocket.rocketType);
        player.openContainer.windowId = windowId;
        player.openContainer.addCraftingToCrafters(player);
    }
}
