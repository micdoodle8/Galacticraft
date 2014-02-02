package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.FMLLog;

/**
 * GCCoreTileEntityFallenMeteor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityFallenMeteor extends TileEntity implements IPacketReceiver
{
	public static final int MAX_HEAT_LEVEL = 5000;
	private int heatLevel = GCCoreTileEntityFallenMeteor.MAX_HEAT_LEVEL;
	private int lastHeatLevel;

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		if (!this.worldObj.isRemote && this.heatLevel > 0)
		{
			this.heatLevel--;
		}

		if (this.heatLevel % 20 == 0 && this.heatLevel != 0)
		{
			this.worldObj.markBlockForRenderUpdate(this.xCoord, this.yCoord, this.zCoord);
		}

		if (!this.worldObj.isRemote && (this.heatLevel % 5 == 0 && this.heatLevel != 0 || this.heatLevel == 0 && this.lastHeatLevel != 0))
		{
			GCCorePacketManager.sendPacketToClients(this.getDescriptionPacket(), this.worldObj, new Vector3(this), 50.0F);
		}

		this.lastHeatLevel = this.heatLevel;
	}

	@Override
	public Packet getDescriptionPacket()
	{
		return GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.heatLevel);
	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		try
		{
			if (this.worldObj.isRemote)
			{
				this.heatLevel = dataStream.readInt();
			}
		} catch (Exception e)
		{
			FMLLog.severe("Failed to read packet");
			e.printStackTrace();
		}
	}

	public int getHeatLevel()
	{
		return this.heatLevel;
	}

	public void setHeatLevel(int heatLevel)
	{
		this.heatLevel = heatLevel;
	}

	public float getScaledHeatLevel()
	{
		return (float) this.heatLevel / GCCoreTileEntityFallenMeteor.MAX_HEAT_LEVEL;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.heatLevel = nbt.getInteger("MeteorHeatLevel");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("MeteorHeatLevel", this.heatLevel);
	}
}
