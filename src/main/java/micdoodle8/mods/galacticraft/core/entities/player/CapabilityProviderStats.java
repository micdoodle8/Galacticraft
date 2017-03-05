package micdoodle8.mods.galacticraft.core.entities.player;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityProviderStats implements ICapabilitySerializable<NBTTagCompound>
{
    private EntityPlayerMP owner;
    private IStatsCapability statsCapability;

    public CapabilityProviderStats(EntityPlayerMP owner)
    {
        this.owner = owner;
        this.statsCapability = CapabilityStatsHandler.GC_STATS_CAPABILITY.getDefaultInstance();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityStatsHandler.GC_STATS_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (CapabilityStatsHandler.GC_STATS_CAPABILITY != null && capability == CapabilityStatsHandler.GC_STATS_CAPABILITY)
        {
            return CapabilityStatsHandler.GC_STATS_CAPABILITY.cast(statsCapability);
        }

        return null;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        statsCapability.saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        statsCapability.loadNBTData(nbt);
    }
}
