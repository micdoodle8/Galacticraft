package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.relauncher.Side;

public interface IPacketReceiver
{
	public void getNetworkedData(ArrayList<Object> sendData);
	
	public void decodePacketdata(ByteBuf buffer);
	
	public void handlePacketData(Side side, EntityPlayer player);
}
