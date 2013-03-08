package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAdvancedCraftingTable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockAdvancedCraftingTable extends BlockContainer
{
	public GCCoreBlockAdvancedCraftingTable(int par1)
	{
		super(par1, Material.wood);
		this.blockIndexInTexture = 19;
        this.setCreativeTab(GalacticraftCore.galacticraftTab);
        this.setBlockBounds(-0.3F, 0.0F, -0.3F, 1.3F, 0.5F, 1.3F);
	}

	@Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getGCCraftingTableRenderID();
    }

	@Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
	{
		return AxisAlignedBB.getBoundingBox((double) i + -0.0F, (double) j + 0.0F, (double) k + -0.0F, (double) i + 1.0F, (double) j + 1.4F, (double) k + 1.0F);
	}

	@Override
    public boolean isOpaqueCube()
    {
        return false;
    }

	@SideOnly(Side.CLIENT)
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k)
	{
		return this.getCollisionBoundingBoxFromPool(world, i, j, k);
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 vec3d, Vec3 vec3d1)
	{
		this.setBlockBounds(-0.0F, 0.0F, -0.0F, 1.0F, 1.4F, 1.0F);

		final MovingObjectPosition r = super.collisionRayTrace(world, i, j, k, vec3d, vec3d1);

		this.setBlockBounds(-0.0F, 0.0F, -0.0F, 1.0F, 1.4F, 1.0F);

		return r;
	}

	@Override
	public void addCollidingBlockToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity)
	{
		this.setBlockBounds(-0.0F, 0.0F, -0.0F, 1.0F, 1.4F, 1.0F);
		super.addCollidingBlockToList(world, i, j, k, axisalignedbb, arraylist, par7Entity);
	}

	@Override
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
		boolean canPlace = true;

		for (int i = par2 - 1; i <= par2 + 1; i++)
		{
			for (int j = par3; j <= par3 + 1; j++)
			{
				for (int k = par4 - 1; k <= par4 + 1; k++)
				{
			        final int var5 = par1World.getBlockId(i, j, k);

			        if (var5 != 0 || blocksList[var5] != null && !blocksList[var5].blockMaterial.isReplaceable())
			        {
			        	canPlace = false;
			        }
				}
			}
		}

        return canPlace;
    }

	@Override
	public int getBlockTextureFromSide(int par1)
    {
        return par1 == 1 ? this.blockIndexInTexture + 1 : par1 == 0 ? this.blockIndexInTexture - 1 : this.blockIndexInTexture;
    }

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
		par5EntityPlayer.openGui(GalacticraftCore.instance, GCCoreConfigManager.idGuiRocketCraftingBench, par1World, par2, par3, par4);
		return true;
    }

	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
    	this.setBlockBounds(-0.0F, 0.0F, -0.0F, 1.0F, 1.4F, 1.0F);
    }

	@Override
	public String getTextureFile()
	{
		return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new GCCoreTileEntityAdvancedCraftingTable();
	}
}
