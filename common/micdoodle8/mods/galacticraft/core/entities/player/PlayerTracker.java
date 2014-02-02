package micdoodle8.mods.galacticraft.core.entities.player;

import micdoodle8.mods.galacticraft.core.world.ChunkLoadingCallback;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.IPlayerTracker;

/**
 * PlayerTracker.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class PlayerTracker implements IPlayerTracker
{
	@Override
	public void onPlayerLogin(EntityPlayer player)
	{
		ChunkLoadingCallback.onPlayerLogin(player);
	}

	@Override
	public void onPlayerLogout(EntityPlayer player)
	{
		ChunkLoadingCallback.onPlayerLogout(player);
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player)
	{
		;
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player)
	{
		;
	}
}
