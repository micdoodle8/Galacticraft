package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Arrays;

import mekanism.api.ITubeConnection;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.oxygen.OxygenNetwork;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenPipe;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockOxygenPipe extends BlockContainer
{
	private final float oxygenPipeMin = 0.4F;
	private final float oxygenPipeMax = 0.6F;

	public GCCoreBlockOxygenPipe(int i)
	{
		super(i, Material.glass);
	}

	@Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftTab;
    }

	@Override
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getGCOxygenPipeRenderID();
	}

    @Override
	@SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister par1IconRegister)
    {
    	this.field_94336_cN = par1IconRegister.func_94245_a("galacticraftcore:pipe_oxygen");
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
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		float minX = 0.4F;
		float minY = 0.4F;
		float minZ = 0.4F;
		float maxX = 0.6F;
		float maxY = 0.6F;
		float maxZ = 0.6F;
		
		if(tileEntity != null)
		{
			boolean[] connectable = new boolean[] {false, false, false, false, false, false};
			ITubeConnection[] connections = OxygenNetwork.getConnections(tileEntity);
			
			for(ITubeConnection connection : connections)
			{
				if(connection !=  null)
				{
					int side = Arrays.asList(connections).indexOf(connection);
					
					if(connection.canTubeConnect(ForgeDirection.getOrientation(side).getOpposite()))
					{
						connectable[side] = true;
					}
				}
			}
			
			if(connectable[0])
			{
				minY = 0.0F;
			}
			if(connectable[1])
			{
				maxY = 1.0F;
			}
			if(connectable[2])
			{
				minZ = 0.0F;
			}
			if(connectable[3])
			{
				maxZ = 1.0F;
			}
			if(connectable[4])
			{
				minX = 0.0F;
			}
			if(connectable[5])
			{
				maxX = 1.0F;
			}
			
			this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
	{
		return this.getCollisionBoundingBoxFromPool(world, i, j, k);
	}
	
	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y, int z, Vec3 vec3d, Vec3 vec3d1)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		float minX = 0.4F;
		float minY = 0.4F;
		float minZ = 0.4F;
		float maxX = 0.6F;
		float maxY = 0.6F;
		float maxZ = 0.6F;
		
		if(tileEntity != null)
		{
			boolean[] connectable = new boolean[] {false, false, false, false, false, false};
			ITubeConnection[] connections = OxygenNetwork.getConnections(tileEntity);
			
			for(ITubeConnection connection : connections)
			{
				if(connection !=  null)
				{
					int side = Arrays.asList(connections).indexOf(connection);
					
					if(connection.canTubeConnect(ForgeDirection.getOrientation(side).getOpposite()))
					{
						connectable[side] = true;
					}
				}
			}
			
			if(connectable[0])
			{
				minY = 0.0F;
			}
			if(connectable[1])
			{
				maxY = 1.0F;
			}
			if(connectable[2])
			{
				minZ = 0.0F;
			}
			if(connectable[3])
			{
				maxZ = 1.0F;
			}
			if(connectable[4])
			{
				minX = 0.0F;
			}
			if(connectable[5])
			{
				maxX = 1.0F;
			}
			
			this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
		}

		final MovingObjectPosition r = super.collisionRayTrace(world, x, y, z, vec3d, vec3d1);

		return super.collisionRayTrace(world, x, y, z, vec3d, vec3d1);
	}
}
