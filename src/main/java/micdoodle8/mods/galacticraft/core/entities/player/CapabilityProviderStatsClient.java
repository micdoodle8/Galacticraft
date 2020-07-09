package micdoodle8.mods.galacticraft.core.entities.player;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProviderStatsClient implements ICapabilityProvider
{
    private final ClientPlayerEntity owner;
    private final LazyOptional<GCPlayerStatsClient> holder = LazyOptional.of(StatsClientCapability::new);

    public CapabilityProviderStatsClient(ClientPlayerEntity owner)
    {
        this.owner = owner;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, final @Nullable Direction side)
    {
        if (cap == GCCapabilities.GC_STATS_CLIENT_CAPABILITY)
        {
            return GCCapabilities.GC_STATS_CLIENT_CAPABILITY.orEmpty(cap, holder);
        }

        return LazyOptional.empty();
    }
}
