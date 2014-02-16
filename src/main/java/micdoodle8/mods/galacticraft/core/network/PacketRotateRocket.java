package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PacketRotateRocket implements IPacket
{
	private int entityID;
	private float entityPitch;
	private float entityYaw;
	
	public PacketRotateRocket()
	{
		
	}
	
	public PacketRotateRocket(Entity rotateableEntity)
	{
		this.entityID = rotateableEntity.getEntityId();
		this.entityPitch = rotateableEntity.rotationPitch;
		this.entityYaw = rotateableEntity.rotationYaw;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		buffer.writeInt(this.entityID);
		buffer.writeFloat(this.entityPitch);
		buffer.writeFloat(this.entityYaw);
	}

	@Override
	public void decodeInto(ChannelHandlerContext context, ByteBuf buffer)
	{
		this.entityID = buffer.readInt();
		this.entityPitch = buffer.readFloat();
		this.entityYaw = buffer.readFloat();
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		
	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		Entity entity = player.worldObj.getEntityByID(this.entityID);
		
		if (entity != null)
		{
			entity.rotationPitch = entityPitch;
			entity.rotationYaw = entityYaw;
		}
	}
}
