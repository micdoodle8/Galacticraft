package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
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

    public static final ResourceLocation GC_PLAYER_PROP = new ResourceLocation(Constants.ASSET_PREFIX, "player_stats");
    public static final ResourceLocation GC_PLAYER_CLIENT_PROP = new ResourceLocation(Constants.ASSET_PREFIX, "player_stats_client");

    public static void register()
    {
        CapabilityManager.INSTANCE.register(GCPlayerStats.class, new Capability.IStorage<GCPlayerStats>()
        {
            @Override
            public NBTBase writeNBT(Capability<GCPlayerStats> capability, GCPlayerStats instance, EnumFacing side)
            {
                NBTTagCompound nbt = new NBTTagCompound();
                instance.saveNBTData(nbt);
                return nbt;
            }

            @Override
            public void readNBT(Capability<GCPlayerStats> capability, GCPlayerStats instance, EnumFacing side, NBTBase nbt)
            {
                instance.loadNBTData((NBTTagCompound) nbt);
            }
        }, StatsCapability::new);

        CapabilityManager.INSTANCE.register(GCPlayerStatsClient.class, new Capability.IStorage<GCPlayerStatsClient>()
        {
            @Override
            public NBTBase writeNBT(Capability<GCPlayerStatsClient> capability, GCPlayerStatsClient instance, EnumFacing side)
            {
                return null;
            }

            @Override
            public void readNBT(Capability<GCPlayerStatsClient> capability, GCPlayerStatsClient instance, EnumFacing side, NBTBase nbt) { }
        }, StatsClientCapability::new);
    }
}
