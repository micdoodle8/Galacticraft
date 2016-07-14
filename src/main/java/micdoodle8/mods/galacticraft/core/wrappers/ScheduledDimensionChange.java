package micdoodle8.mods.galacticraft.core.wrappers;

import net.minecraft.entity.player.EntityPlayerMP;

public class ScheduledDimensionChange
{
    private EntityPlayerMP player;
    private String dimensionName;

    public ScheduledDimensionChange(EntityPlayerMP player, String dimensionName)
    {
        this.player = player;
        this.dimensionName = dimensionName;
    }

    public EntityPlayerMP getPlayer()
    {
        return player;
    }

    public String getDimensionName()
    {
        return dimensionName;
    }

    public void setPlayer(EntityPlayerMP player)
    {
        this.player = player;
    }

    public void setDimensionName(String dimensionName)
    {
        this.dimensionName = dimensionName;
    }
}
