package micdoodle8.mods.galacticraft.core.tile;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.client.gui.GuiIdsCore;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityNasaWorkbench extends TileEntityMulti implements IMultiBlock
{
	@Override
	public boolean onActivated(EntityPlayer entityPlayer)
	{
		entityPlayer.openGui(GalacticraftCore.instance, GuiIdsCore.NASA_WORKBENCH_ROCKET, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		return true;
	}

	@Override
	public void onCreate(BlockVec3 placedPosition)
	{
		this.mainBlockPosition = placedPosition;

		for (int x = -1; x < 2; x++)
		{
			for (int y = 1; y < 3; y++)
			{
				for (int z = -1; z < 2; z++)
				{
					final BlockVec3 vecToAdd = new BlockVec3(placedPosition.x + x, placedPosition.y + y, placedPosition.z + z);

					if (!vecToAdd.equals(placedPosition))
					{
						if (Math.abs(x) != 1 || Math.abs(z) != 1)
						{
							((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 3);
						}
					}
				}
			}
		}

		final BlockVec3 vecToAdd = new BlockVec3(placedPosition.x, placedPosition.y + 3, placedPosition.z);
		((BlockMulti) GCBlocks.fakeBlock).makeFakeBlock(this.worldObj, vecToAdd, placedPosition, 3);
	}

	@Override
	public void onDestroy(TileEntity callingBlock)
	{
		final BlockVec3 thisBlock = new BlockVec3(this);

		for (int x = -1; x < 2; x++)
		{
			for (int y = 0; y < 4; y++)
			{
				for (int z = -1; z < 2; z++)
				{
					if (Math.abs(x) != 1 || Math.abs(z) != 1)
					{
						if ((y == 0 || y == 3) && x == 0 && z == 0)
						{
							if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.05D)
							{
								FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(thisBlock.x + x, thisBlock.y + y, thisBlock.z + z, GCBlocks.nasaWorkbench, Block.getIdFromBlock(GCBlocks.nasaWorkbench) >> 12 & 255);
							}

                            if (y == 0)
                            {
                                this.worldObj.func_147480_a(thisBlock.x, thisBlock.y, thisBlock.z, true);
                            }
                            else
                            {
                                this.worldObj.setBlockToAir(thisBlock.x + x, thisBlock.y + y, thisBlock.z + z);
                            }
						}
						else if (y != 0 && y != 3)
						{
							if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.05D)
							{
								FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(thisBlock.intX() + x, thisBlock.intY() + y, thisBlock.intZ() + z, GCBlocks.nasaWorkbench, Block.getIdFromBlock(GCBlocks.nasaWorkbench) >> 12 & 255);
							}

							this.worldObj.setBlockToAir(thisBlock.x + x, thisBlock.y + y, thisBlock.z + z);
						}
					}
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
