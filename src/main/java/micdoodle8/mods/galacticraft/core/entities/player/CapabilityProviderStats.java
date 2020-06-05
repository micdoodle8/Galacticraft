package micdoodle8.mods.galacticraft.core.entities.player;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.ref.WeakReference;

public class CapabilityProviderStats implements ICapabilitySerializable<CompoundNBT>
{
    private ServerPlayerEntity owner;
    private final LazyOptional<GCPlayerStats> holder = LazyOptional.of(() -> new StatsCapability(new WeakReference<>(this.owner)));

    public CapabilityProviderStats(ServerPlayerEntity owner)
    {
        this.owner = owner;
    }

//    @Override
//    public boolean hasCapability(Capability<?> capability, Direction facing)
//    {
//        return capability == GCCapabilities.GC_STATS_CAPABILITY;
//    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side)
    {
        if (cap == GCCapabilities.GC_STATS_CAPABILITY)
        {
            return GCCapabilities.GC_STATS_CAPABILITY.orEmpty(cap, holder);
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();
        this.holder.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")).saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        this.holder.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")).loadNBTData(nbt);
    }
}
