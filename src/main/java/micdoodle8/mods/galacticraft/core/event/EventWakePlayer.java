package micdoodle8.mods.galacticraft.core.event;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraftforge.event.entity.player.PlayerEvent;

@Cancelable
public class EventWakePlayer extends PlayerEvent
{
    public EnumStatus result = null;
    public final BlockPos pos;
    public final boolean flag1;
    public final boolean flag2;
    public final boolean flag3;
    public final boolean bypassed;

    public EventWakePlayer(EntityPlayer player, BlockPos pos, boolean flag1, boolean flag2, boolean flag3, boolean bypassed)
    {
        super(player);
        this.pos = pos;
        this.flag1 = flag1;
        this.flag2 = flag2;
        this.flag3 = flag3;
        this.bypassed = bypassed;
    }
}
