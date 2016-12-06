package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

public interface IPacketReceiver
{
    void getNetworkedData(ArrayList<Object> sendData);

    void decodePacketdata(ByteBuf buffer);
}
