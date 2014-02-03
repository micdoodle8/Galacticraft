package micdoodle8.mods.galacticraft.core.entities;

import java.util.ArrayList;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * NetworkedEntity.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public abstract class NetworkedEntity extends Entity implements IPacketReceiver
{
	public NetworkedEntity(World par1World)
	{
		super(par1World);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (!this.worldObj.isRemote)
		{
			PacketDispatcher.sendPacketToAllAround(this.posX, this.posY, this.posZ, this.getPacketRange(), this.worldObj.provider.dimensionId, GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.getNetworkedData(new ArrayList<Object>())));
		}
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			this.readNetworkedData(dataStream);
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
	}

	public abstract void readNetworkedData(ByteArrayDataInput dataStream);

	public abstract ArrayList<Object> getNetworkedData(ArrayList<Object> list);

	public abstract double getPacketRange();
}
