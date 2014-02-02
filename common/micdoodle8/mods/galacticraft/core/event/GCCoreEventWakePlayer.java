package micdoodle8.mods.galacticraft.core.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumStatus;
import net.minecraftforge.event.Cancelable;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * GCCoreEventWakePlayer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@Cancelable
public class GCCoreEventWakePlayer extends PlayerEvent
{
	public EnumStatus result = null;
	public final int x;
	public final int y;
	public final int z;
	public final boolean flag1;
	public final boolean flag2;
	public final boolean flag3;
	public final boolean bypassed;

	public GCCoreEventWakePlayer(EntityPlayer player, int x, int y, int z, boolean flag1, boolean flag2, boolean flag3, boolean bypassed)
	{
		super(player);
		this.x = x;
		this.y = y;
		this.z = z;
		this.flag1 = flag1;
		this.flag2 = flag2;
		this.flag3 = flag3;
		this.bypassed = bypassed;
	}
}
