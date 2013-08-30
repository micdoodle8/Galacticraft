package micdoodle8.mods.galacticraft.mars.util;

import java.util.HashMap;
import micdoodle8.mods.galacticraft.api.GalacticraftRegistry;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerParachest;
import micdoodle8.mods.galacticraft.core.recipe.GCCoreNasaWorkbenchRecipe;
import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityLandingBalloons;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySlimeling;
import micdoodle8.mods.galacticraft.mars.inventory.GCMarsContainerSlimeling;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class GCMarsUtil
{
    public static void addRocketBenchT2Recipe(ItemStack result, HashMap<Integer, ItemStack> input)
    {
        GalacticraftRegistry.addT2RocketRecipe(new GCCoreNasaWorkbenchRecipe(result, input));
    }

    public static void openParachestInv(EntityPlayerMP player, GCMarsEntityLandingBalloons landerInv)
    {
        player.incrementWindowID();
        player.closeContainer();
        int windowId = player.currentWindowId;
        player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 28, new Object[] { windowId, 1, landerInv.entityId }));
        player.openContainer = new GCCoreContainerParachest(player.inventory, landerInv);
        player.openContainer.windowId = windowId;
        player.openContainer.addCraftingToCrafters(player);
    }
    
    public static void openSlimelingInventory(EntityPlayerMP player, GCMarsEntitySlimeling slimeling)
    {
        player.incrementWindowID();
        player.closeContainer();
        int windowId = player.currentWindowId;
        player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftMars.CHANNEL, 0, new Object[] { windowId, 0, slimeling.entityId }));
        player.openContainer = new GCMarsContainerSlimeling(player.inventory, slimeling);
        player.openContainer.windowId = windowId;
        player.openContainer.addCraftingToCrafters(player);
    }
}
