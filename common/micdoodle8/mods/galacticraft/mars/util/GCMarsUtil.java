package micdoodle8.mods.galacticraft.mars.util;

import micdoodle8.mods.galacticraft.core.util.PacketUtil;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySlimeling;
import micdoodle8.mods.galacticraft.mars.inventory.GCMarsContainerSlimeling;
import net.minecraft.entity.player.EntityPlayerMP;

public class GCMarsUtil
{
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
