package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityStatsClientHandler
{
    @CapabilityInject(IStatsClientCapability.class)
    public static Capability<IStatsClientCapability> GC_STATS_CLIENT_CAPABILITY = null;

    public static final ResourceLocation GC_PLAYER_CLIENT_PROP = new ResourceLocation(Constants.ASSET_PREFIX, "player_stats_client");

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IStatsClientCapability.class, new Capability.IStorage<IStatsClientCapability>()
        {
            @Override
            public NBTBase writeNBT(Capability<IStatsClientCapability> capability, IStatsClientCapability instance, EnumFacing side)
            {
                return null;
            }

            @Override
            public void readNBT(Capability<IStatsClientCapability> capability, IStatsClientCapability instance, EnumFacing side, NBTBase nbt) { }
        }, StatsClientCapability::new);
    }
}
