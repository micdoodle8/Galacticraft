package micdoodle8.mods.galacticraft.core.util;

import micdoodle8.mods.galacticraft.core.GCCoreThreadVersionCheck;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityLander;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerBuggy;
import micdoodle8.mods.galacticraft.core.inventory.GCCoreContainerParachest;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;

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
        player.closeInventory();
        int id = player.currentWindowId;
        player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 28, new Object[] { id, 0, 0 }));
        player.openContainer = new GCCoreContainerBuggy(player.inventory, buggyInv, type);
        player.openContainer.windowId = id;
        player.openContainer.addCraftingToCrafters(player);
    }

    public static void openParachestInv(EntityPlayerMP player, GCCoreEntityLander landerInv)
    {
        player.incrementWindowID();
        player.closeInventory();
        int windowId = player.currentWindowId;
        player.playerNetServerHandler.sendPacketToPlayer(PacketUtil.createPacket(GalacticraftCore.CHANNEL, 28, new Object[] { windowId, 1, landerInv.entityId }));
        player.openContainer = new GCCoreContainerParachest(player.inventory, landerInv);
        player.openContainer.windowId = windowId;
        player.openContainer.addCraftingToCrafters(player);
    }
}
