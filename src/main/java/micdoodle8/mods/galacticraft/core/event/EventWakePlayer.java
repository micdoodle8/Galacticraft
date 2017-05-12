package micdoodle8.mods.galacticraft.core.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

@Cancelable
public class EventWakePlayer extends PlayerEvent
{
    public EnumStatus result = null;
    public final BlockPos pos;
    public final boolean immediately;
    public final boolean updateWorld;
    public final boolean setSpawn;
    public final boolean bypassed;

    public EventWakePlayer(EntityPlayer player, BlockPos pos, boolean immediately, boolean updateWorld, boolean setSpawn, boolean bypassed)
    {
        super(player);
        this.pos = pos;
        this.immediately = immediately;
        this.updateWorld = updateWorld;
        this.setSpawn = setSpawn;
        this.bypassed = bypassed;
    }
}
