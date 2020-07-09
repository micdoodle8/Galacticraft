package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class GCCapabilities
{
    @CapabilityInject(GCPlayerStats.class)
    public static Capability<GCPlayerStats> GC_STATS_CAPABILITY = null;

    @CapabilityInject(GCPlayerStatsClient.class)
    public static Capability<GCPlayerStatsClient> GC_STATS_CLIENT_CAPABILITY = null;

    public static final ResourceLocation GC_PLAYER_PROP = new ResourceLocation(Constants.MOD_ID_CORE, "player_stats");
    public static final ResourceLocation GC_PLAYER_CLIENT_PROP = new ResourceLocation(Constants.MOD_ID_CORE, "player_stats_client");

    public static void register()
    {
        CapabilityManager.INSTANCE.register(GCPlayerStats.class, new Capability.IStorage<GCPlayerStats>()
        {
            @Override
            public INBT writeNBT(Capability<GCPlayerStats> capability, GCPlayerStats instance, Direction side)
            {
                CompoundNBT nbt = new CompoundNBT();
                instance.saveNBTData(nbt);
                return nbt;
            }

            @Override
            public void readNBT(Capability<GCPlayerStats> capability, GCPlayerStats instance, Direction side, INBT nbt)
            {
                instance.loadNBTData((CompoundNBT) nbt);
            }
        }, StatsCapability::new);

        CapabilityManager.INSTANCE.register(GCPlayerStatsClient.class, new Capability.IStorage<GCPlayerStatsClient>()
        {
            @Override
            public INBT writeNBT(Capability<GCPlayerStatsClient> capability, GCPlayerStatsClient instance, Direction side)
            {
                return null;
            }

            @Override
            public void readNBT(Capability<GCPlayerStatsClient> capability, GCPlayerStatsClient instance, Direction side, INBT nbt)
            {
            }
        }, StatsClientCapability::new);
    }
}
