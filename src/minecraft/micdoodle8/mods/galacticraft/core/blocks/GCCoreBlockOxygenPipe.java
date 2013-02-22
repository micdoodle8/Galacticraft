package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.API.IConnectableToPipe;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenPipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockOxygenPipe extends BlockContainer implements IConnectableToPipe
{
	private final float oxygenPipeMin = 0.4F;
	private final float oxygenPipeMax = 0.6F;
	
	public GCCoreBlockOxygenPipe(int i, int j)
	{
		super(i, j, Material.glass);
	}

	@Override
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getGCOxygenPipeRenderID();
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new GCCoreTileEntityOxygenPipe();
	}
	
	@Override
	public void addCollidingBlockToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity)
	{
		this.setBlockBounds(this.oxygenPipeMin, this.oxygenPipeMin, this.oxygenPipeMin, this.oxygenPipeMax, this.oxygenPipeMax, this.oxygenPipeMax);
		super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);

		final TileEntity tile1 = world.getBlockTileEntity(i, j, k);

		if (Block.blocksList[world.getBlockId(i - 1, j, k)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i - 1, j, k)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.WEST))
			{
				this.setBlockBounds(0.0F, this.oxygenPipeMin, this.oxygenPipeMin, this.oxygenPipeMax, this.oxygenPipeMax, this.oxygenPipeMax);
				super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
			}
		}

		if (Block.blocksList[world.getBlockId(i + 1, j, k)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i + 1, j, k)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.EAST))
			{
				this.setBlockBounds(this.oxygenPipeMin, this.oxygenPipeMin, this.oxygenPipeMin, 1.0F, this.oxygenPipeMax, this.oxygenPipeMax);
				super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
			}
		}

		if (Block.blocksList[world.getBlockId(i, j - 1, k)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i, j - 1, k)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.DOWN))
			{
				this.setBlockBounds(this.oxygenPipeMin, 0.0F, this.oxygenPipeMin, this.oxygenPipeMax, this.oxygenPipeMax, this.oxygenPipeMax);
				super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
			}
		}

		if (Block.blocksList[world.getBlockId(i, j + 1, k)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i, j + 1, k)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.UP))
			{
				this.setBlockBounds(this.oxygenPipeMin, this.oxygenPipeMin, this.oxygenPipeMin, this.oxygenPipeMax, 1.0F, this.oxygenPipeMax);
				super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
			}
		}

		if (Block.blocksList[world.getBlockId(i, j, k - 1)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i, j, k - 1)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.NORTH))
			{
				this.setBlockBounds(this.oxygenPipeMin, this.oxygenPipeMin, 0.0F, this.oxygenPipeMax, this.oxygenPipeMax, this.oxygenPipeMax);
				super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
			}
		}

		if (Block.blocksList[world.getBlockId(i, j, k + 1)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i, j, k + 1)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.SOUTH))
			{
				this.setBlockBounds(this.oxygenPipeMin, this.oxygenPipeMin, this.oxygenPipeMin, this.oxygenPipeMax, this.oxygenPipeMax, 1.0F);
				super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
			}
		}

		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
	{
		float xMin = this.oxygenPipeMin, xMax = this.oxygenPipeMax, yMin = this.oxygenPipeMin, yMax = this.oxygenPipeMax, zMin = this.oxygenPipeMin, zMax = this.oxygenPipeMax;

		if (Block.blocksList[world.getBlockId(i - 1, j, k)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i - 1, j, k)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.WEST))
			{
				xMin = 0.0F;
			}
		}

		if (Block.blocksList[world.getBlockId(i + 1, j, k)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i + 1, j, k)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.EAST))
			{
				xMax = 1.0F;
			}
		}

		if (Block.blocksList[world.getBlockId(i, j - 1, k)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i, j - 1, k)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.DOWN))
			{
				yMin = 0.0F;
			}
		}

		if (Block.blocksList[world.getBlockId(i, j + 1, k)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i, j + 1, k)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.UP))
			{
				yMax = 1.0F;
			}
		}

		if (Block.blocksList[world.getBlockId(i, j, k - 1)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i, j, k - 1)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.NORTH))
			{
				zMin = 0.0F;
			}
		}

		if (Block.blocksList[world.getBlockId(i, j, k + 1)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i, j, k + 1)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.SOUTH))
			{
				zMax = 1.0F;
			}
		}
		
		return AxisAlignedBB.getBoundingBox((double) i + xMin, (double) j + yMin, (double) k + zMin, (double) i + xMax, (double) j + yMax, (double) k + zMax);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
	{
		return this.getCollisionBoundingBoxFromPool(world, i, j, k);
	}
	
    @Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6)
    {
    	for (int i = 0; i < ForgeDirection.values().length - 1; i++)
    	{
    		final TileEntity tile = world.getBlockTileEntity(x + ForgeDirection.getOrientation(i).offsetX, y + ForgeDirection.getOrientation(i).offsetY, z + ForgeDirection.getOrientation(i).offsetZ);
    		final GCCoreTileEntityOxygenPipe thisPipe = (GCCoreTileEntityOxygenPipe)world.getBlockTileEntity(x, y, z);
    		
    		if (tile != null && thisPipe != null && tile instanceof GCCoreTileEntityOxygenPipe)
    		{
    			final GCCoreTileEntityOxygenPipe pipe = (GCCoreTileEntityOxygenPipe)tile;

				pipe.setOxygenInPipe(0D);
				pipe.setZeroOxygen();
    		}
    	}
    	
    	super.breakBlock(world, x, y, z, par5, par6);
    }
	
	@Override
	public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 vec3d, Vec3 vec3d1)
	{
		float xMin = this.oxygenPipeMin, xMax = this.oxygenPipeMax, yMin = this.oxygenPipeMin, yMax = this.oxygenPipeMax, zMin = this.oxygenPipeMin, zMax = this.oxygenPipeMax;

		if (Block.blocksList[world.getBlockId(i - 1, j, k)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i - 1, j, k)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.WEST))
			{
				xMin = 0.0F;
			}
		}

		if (Block.blocksList[world.getBlockId(i + 1, j, k)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i + 1, j, k)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.EAST))
			{
				xMax = 1.0F;
			}
		}

		if (Block.blocksList[world.getBlockId(i, j - 1, k)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i, j - 1, k)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.DOWN))
			{
				yMin = 0.0F;
			}
		}

		if (Block.blocksList[world.getBlockId(i, j + 1, k)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i, j + 1, k)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.UP))
			{
				yMax = 1.0F;
			}
		}

		if (Block.blocksList[world.getBlockId(i, j, k - 1)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i, j, k - 1)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.NORTH))
			{
				zMin = 0.0F;
			}
		}

		if (Block.blocksList[world.getBlockId(i, j, k + 1)] instanceof IConnectableToPipe)
		{
			IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[world.getBlockId(i, j, k + 1)];
			
			if (pipe.isConnectableOnSide(world, i, j, k, ForgeDirection.SOUTH))
			{
				zMax = 1.0F;
			}
		}

		this.setBlockBounds(xMin, yMin, zMin, xMax, yMax, zMax);

		final MovingObjectPosition r = super.collisionRayTrace(world, i, j, k, vec3d, vec3d1);

		this.setBlockBounds(0, 0, 0, 1, 1, 1);

		return r;
	}

	@Override
    public String getTextureFile()
    {
    	return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
    }

	@Override
	public boolean isConnectableOnSide(IBlockAccess blockAccess, int x, int y, int z, ForgeDirection side) 
	{
		return true;
	}
}
