package micdoodle8.mods.galacticraft.core.entities.player;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CapabilityProviderStatsClient implements ICapabilityProvider
{
    private ClientPlayerEntity owner;
    private GCPlayerStatsClient statsCapability;

    public CapabilityProviderStatsClient(ClientPlayerEntity owner)
    {
        this.owner = owner;
        this.statsCapability = GCCapabilities.GC_STATS_CLIENT_CAPABILITY.getDefaultInstance();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, Direction facing)
    {
        return capability == GCCapabilities.GC_STATS_CLIENT_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, Direction facing)
    {
        if (GCCapabilities.GC_STATS_CLIENT_CAPABILITY != null && capability == GCCapabilities.GC_STATS_CLIENT_CAPABILITY)
        {
            return GCCapabilities.GC_STATS_CLIENT_CAPABILITY.cast(statsCapability);
        }

        return null;
    }
}
