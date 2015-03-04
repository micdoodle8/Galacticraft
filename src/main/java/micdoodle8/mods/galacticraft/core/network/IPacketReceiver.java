package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public interface IPacketReceiver
{
    public void getNetworkedData(ArrayList<Object> sendData);

    public void decodePacketdata(ByteBuf buffer);

    public void handlePacketData(Side side, EntityPlayer player);
}
