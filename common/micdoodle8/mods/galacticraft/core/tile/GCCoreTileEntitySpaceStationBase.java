package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

/**
 * GCCoreTileEntitySpaceStationBase.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntitySpaceStationBase extends TileEntityMulti implements IMultiBlock
{
	public String ownerUsername = "bobby";

	public GCCoreTileEntitySpaceStationBase()
	{
		super(GalacticraftCore.CHANNELENTITIES);
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.ownerUsername = par1NBTTagCompound.getString("ownerUsername");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
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
				((GCCoreBlockMulti) GCCoreBlocks.fakeBlock).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 1);
			}
		}
	}

	@Override
	public void onDestroy(TileEntity callingBlock)
	{
	}
}
