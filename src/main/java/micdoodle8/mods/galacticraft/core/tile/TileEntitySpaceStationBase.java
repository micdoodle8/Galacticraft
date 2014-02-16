package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * TileEntitySpaceStationBase.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class TileEntitySpaceStationBase extends TileEntityMulti implements IMultiBlock
{
	public String ownerUsername = "bobby";

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		this.ownerUsername = par1NBTTagCompound.getString("ownerUsername");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		par1NBTTagCompound.setString("ownerUsername", this.ownerUsername);
	}

	public void setOwner(String username)
	{
		this.ownerUsername = username;
	}

	public String getOwner()
	{
		return this.ownerUsername;
	}

	@Override
	public boolean onActivated(EntityPlayer entityPlayer)
	{
		return false;
	}

	@Override
	public void onCreate(Vector3 placedPosition)
	{
		this.mainBlockPosition = placedPosition;

		for (int y = 1; y < 3; y++)
		{
			final Vector3 vecToAdd = new Vector3(placedPosition.x, placedPosition.y + y, placedPosition.z);

			if (!vecToAdd.equals(placedPosition))
			{
				((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 1);
			}
		}
	}

	@Override
	public void onDestroy(TileEntity callingBlock)
	{
	}
}
