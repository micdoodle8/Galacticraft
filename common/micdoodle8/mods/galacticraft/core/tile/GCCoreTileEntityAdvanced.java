package micdoodle8.mods.galacticraft.core.tile;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.relauncher.Side;

/**
 * A TileEntity with some pre-added functionalities.
 * 
 * @author Calclavia
 * 
 */
public abstract class GCCoreTileEntityAdvanced extends TileEntity implements IPacketReceiver
{
	protected long ticks = 0;
	private LinkedHashSet<Field> fieldCacheClient;
	private LinkedHashSet<Field> fieldCacheServer;

	@Override
	public void updateEntity()
	{
		if (this.ticks == 0)
		{
			this.initiate();
		}

		if (this.ticks >= Long.MAX_VALUE)
		{
			this.ticks = 1;
		}

		this.ticks++;

		if (this.isNetworkedTile() && this.ticks % this.getPacketCooldown() == 0)
		{
			if (this.fieldCacheClient == null || this.fieldCacheServer == null)
			{
				try
				{
					this.initFieldCache();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			if (this.worldObj.isRemote && this.fieldCacheServer.size() > 0)
			{
				this.sendPackets();
			}
			else if (!this.worldObj.isRemote && this.fieldCacheClient.size() > 0)
			{
				this.sendPackets();
			}
		}
	}

	private void sendPackets()
	{
		try
		{
			Packet packet = null;
			Set<Field> fieldList = null;

			if (this.worldObj.isRemote)
			{
				fieldList = this.fieldCacheServer;
			}
			else
			{
				fieldList = this.fieldCacheClient;
			}

			List<Object> objList = new ArrayList<Object>();

			for (Field f : fieldList)
			{
				objList.add(f.get(this));
			}

			this.addExtraNetworkedData(objList);

			packet = GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, objList);
			GCCorePacketManager.sendPacketToClients(packet, this.worldObj, new Vector3(this), this.getPacketRange());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void initFieldCache() throws IllegalArgumentException, IllegalAccessException
	{
		this.fieldCacheClient = new LinkedHashSet<Field>();
		this.fieldCacheServer = new LinkedHashSet<Field>();

		for (Field field : this.getClass().getFields())
		{
			if (field.isAnnotationPresent(NetworkedField.class))
			{
				NetworkedField f = field.getAnnotation(NetworkedField.class);

				if (f.targetSide() == Side.CLIENT)
				{
					this.fieldCacheClient.add(field);
				}
				else
				{
					this.fieldCacheServer.add(field);
				}
			}
		}
	}

	public abstract double getPacketRange();

	public abstract int getPacketCooldown();

	public abstract boolean isNetworkedTile();

	public void addExtraNetworkedData(List<Object> networkedList)
	{

	}

	public void readExtraNetworkedData(ByteArrayDataInput dataStream)
	{

	}

	@Override
	public void handlePacketData(INetworkManager network, int packetType, Packet250CustomPayload packet, EntityPlayer player, ByteArrayDataInput dataStream)
	{
		if (this.fieldCacheClient == null || this.fieldCacheServer == null)
		{
			try
			{
				this.initFieldCache();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		if (this.worldObj.isRemote && this.fieldCacheClient.size() == 0)
		{
			return;
		}
		else if (!this.worldObj.isRemote && this.fieldCacheServer.size() == 0)
		{
			return;
		}

		Set<Field> fieldSet = null;

		if (this.worldObj.isRemote)
		{
			fieldSet = this.fieldCacheClient;
		}
		else
		{
			fieldSet = this.fieldCacheServer;
		}

		for (Field field : fieldSet)
		{
			try
			{
				field.set(this, GCCorePacketManager.getFieldValueFromStream(field, dataStream, this.worldObj));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		this.readExtraNetworkedData(dataStream);
	}

	/**
	 * Called on the TileEntity's first tick.
	 */
	public void initiate()
	{
	}

	@Override
	public int getBlockMetadata()
	{
		if (this.blockMetadata == -1)
		{
			this.blockMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		}

		return this.blockMetadata;
	}

	@Override
	public Block getBlockType()
	{
		if (this.blockType == null)
		{
			this.blockType = Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
		}

		return this.blockType;
	}
}
