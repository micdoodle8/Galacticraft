package micdoodle8.mods.galacticraft.core.wrappers;

import net.minecraft.entity.player.EntityPlayerMP;

public class ScheduledDimensionChange
{
    private EntityPlayerMP player;
    private int dimensionName;

    public ScheduledDimensionChange(EntityPlayerMP player, int dimensionName)
    {
        this.player = player;
        this.dimensionName = dimensionName;
    }

    public EntityPlayerMP getPlayer()
    {
        return player;
    }

    public int getDimensionId()
    {
        return dimensionName;
    }

    public void setPlayer(EntityPlayerMP player)
    {
        this.player = player;
    }

    public void setDimensionName(int dimensionName)
    {
        this.dimensionName = dimensionName;
    }
}
