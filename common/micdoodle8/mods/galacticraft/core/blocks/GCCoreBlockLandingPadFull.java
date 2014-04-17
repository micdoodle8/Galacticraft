package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityBuggyFueler;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityLandingPad;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlockLandingPadFull.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockLandingPadFull extends GCCoreBlockAdvancedTile implements IPartialSealableBlock
{
	private Icon[] icons = new Icon[3];

	public GCCoreBlockLandingPadFull(int id, String assetName)
	{
		super(id, Material.rock);
		this.setHardness(1.0F);
		this.setResistance(10.0F);
		this.setStepSound(Block.soundStoneFootstep);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setUnlocalizedName(assetName);
	}

	@Override
	public int damageDropped(int meta)
	{
		return meta;
	}

	@Override
	public int quantityDropped(Random par1Random)
	{
		return 9;
	}

	@Override
	public void breakBlock(World var1, int var2, int var3, int var4, int var5, int var6)
	{
		final TileEntity var9 = var1.getBlockTileEntity(var2, var3, var4);

		if (var9 instanceof IMultiBlock)
		{
			((IMultiBlock) var9).onDestroy(var9);
		}

		super.breakBlock(var1, var2, var3, var4, var5, var6);
	}

	@Override
	public int idDropped(int par1, Random par2Random, int par3)
	{
		return GCCoreBlocks.landingPad.blockID;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z)
	{
		switch (world.getBlockMetadata(x, y, z))
		{
		case 0:
			return AxisAlignedBB.getAABBPool().getAABB(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
		case 2:
			return AxisAlignedBB.getAABBPool().getAABB(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
		default:
			return AxisAlignedBB.getAABBPool().getAABB(x + 0.0D, y + 0.0D, z + 0.0D, x + 1.0D, y + 0.2D, z + 1.0D);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
	{
		switch (world.getBlockMetadata(x, y, z))
		{
		case 0:
			return AxisAlignedBB.getAABBPool().getAABB(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
		case 2:
			return AxisAlignedBB.getAABBPool().getAABB(x + this.minX, y + this.minY, z + this.minZ, x + this.maxX, y + this.maxY, z + this.maxZ);
		default:
			return AxisAlignedBB.getAABBPool().getAABB(x + 0.0D, y + 0.0D, z + 0.0D, x + 1.0D, y + 0.2D, z + 1.0D);
		}
	}

	@Override
	public int getRenderType()
	{
		return GalacticraftCore.proxy.getBlockRenderID(this.blockID);
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.icons[0] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "launch_pad");
		this.icons[1] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "buggy_fueler");
		this.icons[2] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "buggy_fueler_blank");
		this.blockIcon = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "launch_pad");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2)
	{
		switch (par2)
		{
		case 0:
			return this.icons[0];
		case 1:
			return this.icons[1];
		case 2:
			return this.icons[2];
		}

		return this.blockIcon;
	}

	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		for (int x2 = -1; x2 < 2; ++x2)
		{
			for (int z2 = -1; z2 < 2; ++z2)
			{
				if (!super.canPlaceBlockAt(world, x + x2, y, z + z2))
				{
					return false;
				}
			}

		}

		return true;
	}

	@Override
	public boolean hasTileEntity(int metadata)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		switch (metadata)
		{
		case 0:
			return new GCCoreTileEntityLandingPad();
		case 1:
			return new GCCoreTileEntityBuggyFueler();
			// case 2:
			// return new GCCoreTileEntityCargoPad();
		default:
			return null;
		}
	}

	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
	{
		par1World.markBlockForUpdate(par2, par3, par4);
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
	public boolean isSealed(World world, int x, int y, int z, ForgeDirection direction)
	{
		return direction == ForgeDirection.UP;
	}
}
