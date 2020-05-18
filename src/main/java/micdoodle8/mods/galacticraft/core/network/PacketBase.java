package micdoodle8.mods.galacticraft.core.network;

import io.netty.buffer.ByteBuf;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public abstract class PacketBase implements IPacket
{
    public static final int INVALID_DIMENSION_ID = Integer.MIN_VALUE + 12;
    private int dimensionID;

    public PacketBase()
    {
        this.dimensionID = INVALID_DIMENSION_ID;
    }

    public PacketBase(int dimensionID)
    {
        this.dimensionID = dimensionID;
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        if (dimensionID == INVALID_DIMENSION_ID)
        {
            throw new IllegalStateException("Invalid Dimension ID! [GC]");
        }
        buffer.writeInt(this.dimensionID);
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        this.dimensionID = buffer.readInt();
    }

    @Override
    public int getDimensionID()
    {
        return dimensionID;
    }

    private static String getProtocolVersion() {
        return GalacticraftCore.instance == null ? "999.999.999" : GalacticraftCore.instance.versionNumber.toString();
    }

    public static SimpleChannel createChannel(ResourceLocation name) {
        return NetworkRegistry.ChannelBuilder.named(name)
                .clientAcceptedVersions(getProtocolVersion()::equals)
                .serverAcceptedVersions(getProtocolVersion()::equals)
                .networkProtocolVersion(PacketBase::getProtocolVersion)
                .simpleChannel();
    }
}
