package codechicken.lib.gui;

import net.minecraft.entity.player.EntityPlayerMP;

public interface IGuiPacketSender {

    void sendPacket(EntityPlayerMP player, int windowId);
}
