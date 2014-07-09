package micdoodle8.mods.galacticraft.core.event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

public class EventLandingPadRemoval extends Event
{
	public boolean allow = true;
	public final int x;
	public final int y;
	public final int z;
	public final World world;

	public EventLandingPadRemoval(World world, int x, int y, int z)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
