package micdoodle8.mods.galacticraft.planets.asteroids.tile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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

        for (int y = 0; y < 3; y += 2)
        {
            for (int x = -1; x <= 1; x++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    final BlockVec3 vecToAdd = new BlockVec3(placedPosition.x + x, placedPosition.y + y, placedPosition.z + z);

                    if (!vecToAdd.equals(placedPosition))
                    {
                        ((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, y == 0 ? 7 : 6);
                    }
                }
            }
        }
	}

	@Override
	public void onDestroy(TileEntity callingBlock)
	{
        for (int y = 0; y < 3; y += 2)
        {
            for (int x = -1; x <= 1; x++)
            {
                for (int z = -1; z <= 1; z++)
                {
                    this.worldObj.func_147480_a(this.xCoord + x, this.yCoord + y, this.zCoord + z, false);
                }
            }
        }
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return TileEntity.INFINITE_EXTENT_AABB;
	}
}
