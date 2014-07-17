package micdoodle8.mods.galacticraft.core.tile;

import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.network.IPacketReceiver;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityMulti extends TileEntityAdvanced implements IPacketReceiver
{
	// The the position of the main block
	@NetworkedField(targetSide = Side.CLIENT)
	public BlockVec3 mainBlockPosition;

	public void setMainBlock(BlockVec3 mainBlock)
	{
		this.mainBlockPosition = mainBlock;

		if (!this.worldObj.isRemote)
		{
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
	}

	public void onBlockRemoval()
	{
		if (this.mainBlockPosition != null)
		{
			TileEntity tileEntity = this.worldObj.getTileEntity(this.mainBlockPosition.intX(), this.mainBlockPosition.intY(), this.mainBlockPosition.intZ());

			if (tileEntity != null && tileEntity instanceof IMultiBlock)
			{
				IMultiBlock mainBlock = (IMultiBlock) tileEntity;
                mainBlock.onDestroy(this);
			}
		}
	}

	public boolean onBlockActivated(World par1World, int x, int y, int z, EntityPlayer par5EntityPlayer)
	{
		if (this.mainBlockPosition != null)
		{
			TileEntity tileEntity = this.worldObj.getTileEntity(this.mainBlockPosition.intX(), this.mainBlockPosition.intY(), this.mainBlockPosition.intZ());

			if (tileEntity != null)
			{
				if (tileEntity instanceof IMultiBlock)
				{
					return ((IMultiBlock) tileEntity).onActivated(par5EntityPlayer);
				}
			}
		}

		return false;
	}

	/**
	 * Reads a tile entity from NBT.
	 */
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.mainBlockPosition = new BlockVec3(nbt.getCompoundTag("mainBlockPosition"));
	}

	/**
	 * Writes a tile entity to NBT.
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		if (this.mainBlockPosition != null)
		{
			nbt.setTag("mainBlockPosition", this.mainBlockPosition.writeToNBT(new NBTTagCompound()));
		}
	}

	@Override
	public double getPacketRange()
	{
		return 30.0D;
	}

	@Override
	public int getPacketCooldown()
	{
		return 50;
	}

	@Override
	public boolean isNetworkedTile()
	{
		return true;
	}
}
