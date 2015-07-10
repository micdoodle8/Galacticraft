package micdoodle8.mods.galacticraft.core.event;

import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraft.world.World;

public class EventLandingPadRemoval extends Event
{
    public boolean allow = true;
    public final BlockPos pos;
    public final World world;

    public EventLandingPadRemoval(World world, BlockPos pos)
    {
        this.world = world;
        this.pos = pos;
    }
}
