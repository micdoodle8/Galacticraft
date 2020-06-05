package micdoodle8.mods.galacticraft.core.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class EventWakePlayer extends PlayerEvent
{
    public PlayerEntity.SleepResult result = null;
    public final BlockPos pos;
    public final boolean immediately;
    public final boolean updateWorld;
    public final boolean setSpawn;
    public final boolean bypassed;

    public EventWakePlayer(PlayerEntity player, BlockPos pos, boolean immediately, boolean updateWorld, boolean setSpawn, boolean bypassed)
    {
        super(player);
        this.pos = pos;
        this.immediately = immediately;
        this.updateWorld = updateWorld;
        this.setSpawn = setSpawn;
        this.bypassed = bypassed;
    }
}
