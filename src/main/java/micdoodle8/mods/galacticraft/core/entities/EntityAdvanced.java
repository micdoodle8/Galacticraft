package micdoodle8.mods.galacticraft.core.entities;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.IPacket;
import micdoodle8.mods.galacticraft.core.network.NetworkUtil;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.relauncher.Side;

public abstract class EntityAdvanced extends Entity implements IPacket
{
	protected long ticks = 0;
	private LinkedHashSet<Field> fieldCacheClient;
	private LinkedHashSet<Field> fieldCacheServer;

	{
		if (this.isNetworkedEntity())
		{
			GalacticraftCore.packetPipeline.registerPacket(this.getClass());
		}
	}

	public EntityAdvanced(World world)
	{
		super(world);
	}

	/**
	 * Whether or not this entity should be sending packets to/from server
	 * 
	 * @return If the entity needs network capabilities
	 */
	public abstract boolean isNetworkedEntity();

	/**
	 * Get the amount of ticks between each packet send
	 * 
	 * @param side
	 *            The target side.
	 * @return The amount of ticks to wait before sending another packet to this
	 *         target
	 */
	public abstract int getPacketCooldown(Side side);

	/**
	 * Add any additional data to the stream
	 * 
	 * @param stream
	 *            The ByteBuf stream to write data into
	 */
	public void addExtraNetworkedData(ByteBuf stream)
	{

	}

	/**
	 * Read any additional data from the stream
	 * 
	 * @param stream
	 *            The ByteBuf stream to read data from
	 */
	public void readExtraNetworkedData(ByteBuf stream)
	{

	}

	/**
	 * Called after a packet is read, only on client side.
	 * 
	 * @param player
	 *            The player associated with the received packet
	 */
	public abstract void onPacketClient(EntityPlayer player);

	/**
	 * Called after a packet is read, only on server side.
	 * 
	 * @param player
	 *            The player associated with the received packet
	 */
	public abstract void onPacketServer(EntityPlayer player);

	/**
	 * Packets will be sent to all (client-side) players within this range
	 * 
	 * @return Maximum distance to send packets to client players
	 */
	public abstract double getPacketRange();

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (this.ticks >= Long.MAX_VALUE)
		{
			this.ticks = 1;
		}

		this.ticks++;

		if (this.isNetworkedEntity())
		{
			if (!this.worldObj.isRemote && this.getPacketCooldown(Side.CLIENT) % this.ticks == 0)
			{
				if (this.fieldCacheClient == null)
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

				this.sendPackets();
			}

			if (this.worldObj.isRemote && this.getPacketCooldown(Side.SERVER) % this.ticks == 0)
			{
				if (this.fieldCacheServer == null)
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

				this.sendPackets();
			}
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

	private void sendPackets()
	{
		GalacticraftCore.packetPipeline.sendToAllAround(this, new TargetPoint(this.worldObj.provider.dimensionId, this.posX, this.posY, this.posZ, this.getPacketRange()));
	}

	@Override
	public void encodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		try
		{
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

			NetworkUtil.encodeData(buffer, objList);

			this.addExtraNetworkedData(buffer);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext context, ByteBuf buffer)
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
				field.set(this, NetworkUtil.getFieldValueFromStream(field, buffer, this.worldObj));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		this.readExtraNetworkedData(buffer);
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		this.onPacketClient(player);
	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		this.onPacketServer(player);
	}
}
