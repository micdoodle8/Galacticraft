package micdoodle8.mods.galacticraft.core.network;

import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;

public interface IPacketReceiver
{
    /**
     * Note this can be called during the init constructor of the
     * entity's superclass, if this is a subclass of the IPacketReceiver
     * entity.  So make sure any fields referenced in
     * getNetworkedData() are either in the superclass, or
     * add some null checks!!
     */
    public void getNetworkedData(ArrayList<Object> sendData);

    public void decodePacketdata(ByteBuf buffer);

    public void handlePacketData(Side side, EntityPlayer player);
}
