package micdoodle8.mods.galacticraft.core.client.fx;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockPosParticleData implements IParticleData
{
    public static final IParticleData.IDeserializer<BlockPosParticleData> DESERIALIZER = new IParticleData.IDeserializer<BlockPosParticleData>()
    {
        @Override
        public BlockPosParticleData deserialize(ParticleType<BlockPosParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException
        {
            reader.expect(' ');
            return new BlockPosParticleData(particleTypeIn, BlockPos.fromLong(reader.readLong()));
        }

        @Override
        public BlockPosParticleData read(ParticleType<BlockPosParticleData> particleTypeIn, PacketBuffer buffer)
        {
            return new BlockPosParticleData(particleTypeIn, BlockPos.fromLong(buffer.readLong()));
        }
    };
    private final ParticleType<BlockPosParticleData> particleType;
    private final BlockPos blockPos;

    public BlockPosParticleData(ParticleType<BlockPosParticleData> particleTypeIn, BlockPos blockPos)
    {
        this.particleType = particleTypeIn;
        this.blockPos = blockPos;
    }

    @Override
    public void write(PacketBuffer buffer)
    {
        buffer.writeLong(this.blockPos.toLong());
    }

    @Override
    public String getParameters()
    {
        return Registry.PARTICLE_TYPE.getKey(this.getType()) + " " + this.blockPos.toString();
    }

    @Override
    public ParticleType<BlockPosParticleData> getType()
    {
        return this.particleType;
    }

    @OnlyIn(Dist.CLIENT)
    public BlockPos getBlockPos()
    {
        return this.blockPos;
    }
}