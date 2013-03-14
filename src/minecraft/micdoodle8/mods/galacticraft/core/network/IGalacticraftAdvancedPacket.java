package micdoodle8.mods.galacticraft.core.network;

import java.io.DataInputStream;

import cpw.mods.fml.relauncher.Side;

public interface IGalacticraftAdvancedPacket 
{
	public void handlePacket(DataInputStream stream, Object[] extradata, Side side);

	public byte getPacketID();
}
