package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * IMultiBlock.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public interface IMultiBlock
{
	/**
	 * Called when activated
	 */
	public boolean onActivated(EntityPlayer entityPlayer);

	/**
	 * Called when this multiblock is created
	 * 
	 * @param placedPosition
	 *            - The position the block was placed at
	 */
	public void onCreate(Vector3 placedPosition);

	/**
	 * Called when one of the multiblocks of this block is destroyed
	 * 
	 * @param callingBlock
	 *            - The tile entity who called the onDestroy function
	 */
	public void onDestroy(TileEntity callingBlock);
}
