//package micdoodle8.mods.galacticraft.core.network;
//
//import io.netty.buffer.ByteBuf;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import micdoodle8.mods.galacticraft.core.util.GCLog;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.network.PacketBuffer;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.world.dimension.DimensionType;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.network.NetworkEvent;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.function.Supplier;
//
//public class PacketRotateRocket extends PacketBase
//{
//    private int entityID;
//    private float entityPitch;
//    private float entityYaw;
//
//    public PacketRotateRocket()
//    {
//        super();
//    }
//
//    public PacketRotateRocket(Entity rotateableEntity)
//    {
//        super(GCCoreUtil.getDimensionID(rotateableEntity.world));
//        this.entityID = rotateableEntity.getEntityId();
//        this.entityPitch = rotateableEntity.rotationPitch;
//        this.entityYaw = rotateableEntity.rotationYaw;
//    }
//
//    public static void encode(final PacketRotateRocket message, final PacketBuffer buf)
//    {
//        buf.writeInt(message.type.ordinal());
//        NetworkUtil.writeUTF8String(buf, message.getDimensionID().getRegistryName().toString());
//
//        try
//        {
//            NetworkUtil.encodeData(buf, message.data);
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    public static PacketRotateRocket decode(PacketBuffer buf)
//    {
//        PacketSimple.EnumSimplePacket type = PacketSimple.EnumSimplePacket.values()[buf.readInt()];
//        ArrayList<Object> data = null;
//
//        try
//        {
//            if (type.getDecodeClasses().length > 0)
//            {
//                DimensionType dim = DimensionType.byName(new ResourceLocation(NetworkUtil.readUTF8String(buf)));
//                data = NetworkUtil.decodeData(type.getDecodeClasses(), buf);
//                return new PacketRotateRocket(type, dim, data);
//            }
//            if (buf.readableBytes() > 0 && buf.writerIndex() < 0xfff00)
//            {
//                GCLog.severe("Galacticraft packet length problem for packet type " + type.toString());
//            }
//        }
//        catch (Exception e)
//        {
//            System.err.println("[Galacticraft] Error handling simple packet type: " + type.toString() + " " + buf.toString());
//            e.printStackTrace();
//            throw e;
//        }
//        return null;
//    }
//
//    public static void handle(final PacketRotateRocket message, Supplier<NetworkEvent.Context> ctx) {
//        ctx.get().enqueueWork(() -> {
//            if (GCCoreUtil.getEffectiveSide() == LogicalSide.CLIENT) {
//                message.handleClientSide(ctx.get().getSender());
//            } else {
//                message.handleServerSide(ctx.get().getSender());
//            }
//        });
//        ctx.get().setPacketHandled(true);
//    }
//
//    @Override
//    public void encodeInto(ByteBuf buffer)
//    {
//        super.encodeInto(buffer);
//        buffer.writeInt(this.entityID);
//        buffer.writeFloat(this.entityPitch);
//        buffer.writeFloat(this.entityYaw);
//    }
//
//    @Override
//    public void decodeInto(ByteBuf buffer)
//    {
//        super.decodeInto(buffer);
//        this.entityID = buffer.readInt();
//        this.entityPitch = buffer.readFloat();
//        this.entityYaw = buffer.readFloat();
//    }
//
//    @Override
//    public void handleClientSide(PlayerEntity player)
//    {
//
//    }
//
//    @Override
//    public void handleServerSide(PlayerEntity player)
//    {
//        Entity entity = player.world.getEntityByID(this.entityID);
//
//        if (entity != null)
//        {
//            entity.rotationPitch = this.entityPitch;
//            entity.rotationYaw = this.entityYaw;
//        }
//    }
//}
