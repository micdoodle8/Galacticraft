package micdoodle8.mods.galacticraft.core.entities.player;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import java.lang.ref.WeakReference;

public class CapabilityProviderStats implements ICapabilitySerializable<CompoundNBT>
{
    private ServerPlayerEntity owner;
    private GCPlayerStats statsCapability;

    public CapabilityProviderStats(ServerPlayerEntity owner)
    {
        this.owner = owner;
        this.statsCapability = GCCapabilities.GC_STATS_CAPABILITY.getDefaultInstance();
        this.statsCapability.setPlayer(new WeakReference<>(this.owner));
    }

    @Override
    public boolean hasCapability(Capability<?> capability, Direction facing)
    {
        return capability == GCCapabilities.GC_STATS_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, Direction facing)
    {
        if (GCCapabilities.GC_STATS_CAPABILITY != null && capability == GCCapabilities.GC_STATS_CAPABILITY)
        {
            return GCCapabilities.GC_STATS_CAPABILITY.cast(statsCapability);
        }

        return null;
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();
        statsCapability.saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        statsCapability.loadNBTData(nbt);
    }
}
