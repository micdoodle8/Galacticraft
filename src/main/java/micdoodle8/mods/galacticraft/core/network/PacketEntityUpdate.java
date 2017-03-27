package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.api.vector.Vector2;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class PacketEntityUpdate extends PacketBase
{
    private int entityID;
    private Vector3 position;
    private float rotationYaw;
    private float rotationPitch;
    private Vector3 motion;
    private boolean onGround;

    public PacketEntityUpdate()
    {
        super();
    }

    public PacketEntityUpdate(int entityID, Vector3 position, Vector2 rotation, Vector3 motion, boolean onGround, int dimID)
    {
        super(dimID);
        this.entityID = entityID;
        this.position = position;
        this.rotationYaw = (float) rotation.x;
        this.rotationPitch = (float) rotation.y;
        this.motion = motion;
        this.onGround = onGround;
    }

    public PacketEntityUpdate(Entity entity)
    {
        this(entity.getEntityId(), new Vector3(entity.posX, entity.posY, entity.posZ), new Vector2(entity.rotationYaw, entity.rotationPitch), new Vector3(entity.motionX, entity.motionY, entity.motionZ), entity.onGround, GCCoreUtil.getDimensionID(entity.world));
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        super.encodeInto(buffer);
        buffer.writeInt(this.entityID);
        buffer.writeDouble(this.position.x);
        buffer.writeDouble(this.position.y);
        buffer.writeDouble(this.position.z);
        buffer.writeFloat(this.rotationYaw);
        buffer.writeFloat(this.rotationPitch);
        buffer.writeDouble(this.motion.x);
        buffer.writeDouble(this.motion.y);
        buffer.writeDouble(this.motion.z);
        buffer.writeBoolean(this.onGround);
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        super.decodeInto(buffer);
        this.entityID = buffer.readInt();
        this.position = new Vector3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        this.rotationYaw = buffer.readFloat();
        this.rotationPitch = buffer.readFloat();
        this.motion = new Vector3(buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        this.onGround = buffer.readBoolean();
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        this.setEntityData(player);
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        this.setEntityData(player);
    }

    private void setEntityData(EntityPlayer player)
    {
        Entity entity = player.world.getEntityByID(this.entityID);

        if (entity instanceof IEntityFullSync)
        {
            if (player.world.isRemote || player.getUniqueID().equals(((IEntityFullSync) entity).getOwnerUUID()) || ((IEntityFullSync) entity).getOwnerUUID() == null)
            {
                IEntityFullSync controllable = (IEntityFullSync) entity;
                controllable.setPositionRotationAndMotion(this.position.x, this.position.y, this.position.z, this.rotationYaw, this.rotationPitch, this.motion.x, this.motion.y, this.motion.z, this.onGround);
            }
        }
    }

    public interface IEntityFullSync
    {
        void setPositionRotationAndMotion(double x, double y, double z, float yaw, float pitch, double motX, double motY, double motZ, boolean onGround);

        UUID getOwnerUUID();
    }
}
