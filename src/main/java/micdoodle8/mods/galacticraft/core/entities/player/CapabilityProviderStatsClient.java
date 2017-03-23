package micdoodle8.mods.galacticraft.core.entities.player;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CapabilityProviderStatsClient implements ICapabilityProvider
{
    private EntityPlayerSP owner;
    private IStatsClientCapability statsCapability;

    public CapabilityProviderStatsClient(EntityPlayerSP owner)
    {
        this.owner = owner;
        this.statsCapability = CapabilityStatsClientHandler.GC_STATS_CLIENT_CAPABILITY.getDefaultInstance();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityStatsClientHandler.GC_STATS_CLIENT_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (CapabilityStatsClientHandler.GC_STATS_CLIENT_CAPABILITY != null && capability == CapabilityStatsClientHandler.GC_STATS_CLIENT_CAPABILITY)
        {
            return (T)(statsCapability);
        }

        return null;
    }
}
