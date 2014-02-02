package micdoodle8.mods.galacticraft.core.network;

import java.io.DataInputStream;

import cpw.mods.fml.relauncher.Side;

/**
 * IGalacticraftAdvancedPacket.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public interface IGalacticraftAdvancedPacket
{
	public void handlePacket(DataInputStream stream, Object[] extradata, Side side);

	public byte getPacketID();
}
