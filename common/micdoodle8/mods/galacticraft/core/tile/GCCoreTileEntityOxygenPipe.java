package micdoodle8.mods.galacticraft.core.tile;

import micdoodle8.mods.galacticraft.api.tile.IColorable;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCCoreAnnotations.NetworkedField;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.GCCorePacketManager;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreTileEntityOxygenPipe.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreTileEntityOxygenPipe extends GCCoreTileEntityOxygenTransmitter implements IColorable, IPacketReceiver
{
	@NetworkedField(targetSide = Side.CLIENT)
	public byte pipeColor = 15;
	@NetworkedField(targetSide = Side.CLIENT)
	public byte preLoadColor;
	private byte preColorCooldown;
	private boolean setColor = false;

	@Override
	public boolean canConnect(ForgeDirection direction, NetworkType type)
	{
		TileEntity adjacentTile = new Vector3(this).modifyPositionFromSide(direction).getTileEntity(this.worldObj);

		if (type == NetworkType.OXYGEN)
		{
			if (adjacentTile instanceof IColorable)
			{
				if (this.getColor() == ((IColorable) adjacentTile).getColor())
				{
					return true;
				}
				else
				{
					return false;
				}
			}

			return true;
		}

		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return TileEntity.INFINITE_EXTENT_AABB;
	}

	@Override
	public boolean canUpdate()
	{
		return !this.setColor;
	}

	@Override
	public void updateEntity()
	{
		if (this.preColorCooldown > 0)
		{
			this.preColorCooldown--;
		}

		if (this.preColorCooldown == 0 && !this.worldObj.isRemote && this.preLoadColor != -1)
		{
			GCCorePacketManager.sendPacketToClients(GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, this, this.getColor(), this.preLoadColor));
			this.preLoadColor = -1;
			this.setColor = true;
		}

		if (this.preColorCooldown == 0 && this.worldObj.isRemote && this.preLoadColor == 0)
		{
			final Vector3 thisVec = new Vector3(this);
			this.worldObj.markBlockForRenderUpdate(thisVec.intX(), thisVec.intY(), thisVec.intZ());
			this.preLoadColor = -1;
			this.setColor = true;
		}
	}

	@Override
	public double getPacketRange()
	{
		return 12.0D;
	}

	@Override
	public int getPacketCooldown()
	{
		return 1;
	}

	@Override
	public boolean isNetworkedTile()
	{
		return this.preColorCooldown == 0 && !this.worldObj.isRemote && this.preLoadColor != -1;
	}

	@Override
	public void validate()
	{
		super.validate();

		this.preColorCooldown = 40;

		if (this.worldObj != null && this.worldObj.isRemote)
		{
			final Vector3 thisVec = new Vector3(this);
			this.worldObj.markBlockForRenderUpdate(thisVec.intX(), thisVec.intY(), thisVec.intZ());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setColor(byte col)
	{
		this.pipeColor = col;

		if (this.worldObj != null)
		{
			if (this.worldObj.isRemote)
			{
				final Vector3 thisVec = new Vector3(this);
				this.worldObj.markBlockForRenderUpdate(thisVec.intX(), thisVec.intY(), thisVec.intZ());
			}
			else
			{
				this.getNetwork().split(this);
				this.resetNetwork();
			}
		}

	}

	@Override
	public byte getColor()
	{
		return this.pipeColor;
	}

	@Override
	public void onAdjacentColorChanged(ForgeDirection direction)
	{
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);

		if (!this.worldObj.isRemote)
		{
			this.refresh();
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);

		final byte by = par1NBTTagCompound.getByte("pipeColor");
		this.setColor(by);
		this.preLoadColor = by;
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);

		par1NBTTagCompound.setByte("pipeColor", this.getColor());
	}
}
