package micdoodle8.mods.galacticraft.planets.mars.util;

import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.core.recipe.NasaWorkbenchRecipe;
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

//    public static void openParachestInventory(ServerPlayerEntity player, EntityLandingBalloons landerInv)
//    {
//        player.getNextWindowId();
//        player.closeContainer();
//        int windowId = player.currentWindowId;
//        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_PARACHEST_GUI, GCCoreUtil.getDimensionID(player.world), new Object[] { windowId, 1, landerInv.getEntityId() }), player);
//        player.openContainer = new ContainerParaChest(player.inventory, landerInv, player);
//        player.openContainer.windowId = windowId;
//        player.openContainer.addListener(player);
//    }
//
//    public static void openSlimelingInventory(ServerPlayerEntity player, EntitySlimeling slimeling)
//    {
//        player.getNextWindowId();
//        player.closeContainer();
//        int windowId = player.currentWindowId;
//        GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMars(EnumSimplePacketMars.C_OPEN_CUSTOM_GUI, GCCoreUtil.getDimensionID(player.world), new Object[] { windowId, 0, slimeling.getEntityId() }), player);
//        player.openContainer = new ContainerSlimeling(player.inventory, slimeling, player);
//        player.openContainer.windowId = windowId;
//        player.openContainer.addListener(player);
//    }

//    public static void openCargoRocketInventory(ServerPlayerEntity player, EntityCargoRocket rocket)
//    {
//        player.getNextWindowId();
//        player.closeContainer();
//        int windowId = player.currentWindowId;
//        GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMars(EnumSimplePacketMars.C_OPEN_CUSTOM_GUI, GCCoreUtil.getDimensionID(player.world), new Object[] { windowId, 1, rocket.getEntityId() }), player);
//        player.openContainer = new ContainerRocketInventory(player.inventory, rocket, rocket.rocketType, player);
//        player.openContainer.windowId = windowId;
//        player.openContainer.addListener(player);
//    }
//
//    public static void openAdvancedLaunchController(ServerPlayerEntity player, TileEntityLaunchController launchController)
//    {
//        player.getNextWindowId();
//        player.closeContainer();
//        int windowId = player.currentWindowId;
//        GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMars(EnumSimplePacketMars.C_OPEN_CUSTOM_GUI_TILE, GCCoreUtil.getDimensionID(player.world), new Object[] { windowId, 0, launchController.getPos() }), player);
//        player.openContainer = new ContainerLaunchControllerAdvanced(player.inventory, launchController, player);
//        player.openContainer.windowId = windowId;
//        player.openContainer.addListener(player);
//    }
}
