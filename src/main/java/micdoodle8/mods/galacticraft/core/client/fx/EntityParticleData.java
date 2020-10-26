package micdoodle8.mods.galacticraft.core.client.fx;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.registry.Registry;

import java.util.Locale;
import java.util.UUID;

public class EntityParticleData implements IParticleData
{
    public static final IParticleData.IDeserializer<EntityParticleData> DESERIALIZER = new IParticleData.IDeserializer<EntityParticleData>()
    {
        @Override
        public EntityParticleData deserialize(ParticleType<EntityParticleData> particleTypeIn, StringReader reader) throws CommandSyntaxException
        {
            reader.expect(' ');
            return new EntityParticleData(particleTypeIn, UUID.fromString(reader.readString()));
        }

        @Override
        public EntityParticleData read(ParticleType<EntityParticleData> particleTypeIn, PacketBuffer buffer)
        {
            return new EntityParticleData(particleTypeIn, buffer.readUniqueId());
        }
    };

    private final ParticleType<EntityParticleData> particleType;
    private final UUID entityUUID;

    public EntityParticleData(ParticleType<EntityParticleData> particleType, UUID entityUUID)
    {
        this.particleType = particleType;
        this.entityUUID = entityUUID;
    }

    @Override
    public ParticleType<?> getType()
    {
        return particleType;
    }

    @Override
    public void write(PacketBuffer buffer)
    {
        buffer.writeBoolean(this.entityUUID != null);
        if (this.entityUUID != null)
        {
            buffer.writeUniqueId(this.entityUUID);
        }
    }

    @Override
    public String getParameters()
    {
        return String.format(Locale.ROOT, "%s %s", Registry.PARTICLE_TYPE.getKey(this.getType()), this.entityUUID.toString());
    }

    public UUID getEntityUUID()
    {
        return entityUUID;
    }
}
