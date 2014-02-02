package micdoodle8.mods.galacticraft.core.event;

import net.minecraft.world.World;
import net.minecraftforge.event.Event;

/**
 * GCCoreLandingPadRemovalEvent.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreLandingPadRemovalEvent extends Event
{
	public boolean allow = true;
	public final int x;
	public final int y;
	public final int z;
	public final World world;

	public GCCoreLandingPadRemovalEvent(World world, int x, int y, int z)
	{
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
