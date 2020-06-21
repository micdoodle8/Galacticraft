package micdoodle8.mods.galacticraft.core.wrappers;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;

public class ScheduledDimensionChange
{
    private ServerPlayerEntity player;
    private DimensionType dimensionName;

    public ScheduledDimensionChange(ServerPlayerEntity player, DimensionType dimensionName)
    {
        this.player = player;
        this.dimensionName = dimensionName;
    }

    public ServerPlayerEntity getPlayer()
    {
        return player;
    }

    public DimensionType getDimensionId()
    {
        return dimensionName;
    }

    public void setPlayer(ServerPlayerEntity player)
    {
        this.player = player;
    }

    public void setDimensionName(DimensionType dimensionName)
    {
        this.dimensionName = dimensionName;
    }
}
