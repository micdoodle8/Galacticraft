package micdoodle8.mods.galacticraft.core.wrappers;

import net.minecraft.entity.player.ServerPlayerEntity;

public class ScheduledDimensionChange
{
    private ServerPlayerEntity player;
    private int dimensionName;

    public ScheduledDimensionChange(ServerPlayerEntity player, int dimensionName)
    {
        this.player = player;
        this.dimensionName = dimensionName;
    }

    public ServerPlayerEntity getPlayer()
    {
        return player;
    }

    public int getDimensionId()
    {
        return dimensionName;
    }

    public void setPlayer(ServerPlayerEntity player)
    {
        this.player = player;
    }

    public void setDimensionName(int dimensionName)
    {
        this.dimensionName = dimensionName;
    }
}
