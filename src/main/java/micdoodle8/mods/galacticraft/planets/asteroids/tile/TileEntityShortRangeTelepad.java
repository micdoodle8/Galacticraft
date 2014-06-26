package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityMulti;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityShortRangeTelepad extends TileEntityMulti implements IMultiBlock
{
	@NetworkedField(targetSide = Side.CLIENT)
	public int address = -1;
	@NetworkedField(targetSide = Side.CLIENT)
	public int targetAddress = -1;

	@Override
	public void updateEntity()
	{
		super.updateEntity();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.address = nbt.getInteger("Address");
		this.targetAddress = nbt.getInteger("TargetAddress");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("TargetAddress", this.targetAddress);
		nbt.setInteger("Address", this.address);
	}

	@Override
	public double getPacketRange()
	{
		return 24.0D;
	}

	@Override
	public int getPacketCooldown()
	{
		return 3;
	}

	@Override
	public boolean isNetworkedTile()
	{
		return true;
	}

	@Override
	public boolean onActivated(EntityPlayer entityPlayer)
	{
		return false;
	}

	@Override
	public void onCreate(BlockVec3 placedPosition)
	{
		this.mainBlockPosition = placedPosition;

		for (int y = 2; y < 3; y++)
		{
			final BlockVec3 vecToAdd = new BlockVec3(placedPosition.x, placedPosition.y + y, placedPosition.z);

			if (!vecToAdd.equals(placedPosition))
			{
				((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 6);
			}
		}
	}

	@Override
	public void onDestroy(TileEntity callingBlock)
	{
		this.worldObj.func_147480_a(this.xCoord, this.yCoord, this.zCoord, false);
		this.worldObj.func_147480_a(this.xCoord, this.yCoord + 2, this.zCoord, false);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return TileEntity.INFINITE_EXTENT_AABB;
	}
}
