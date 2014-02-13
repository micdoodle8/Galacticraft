package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBuggyFuelerSingle;
import micdoodle8.mods.galacticraft.core.tile.TileEntityLandingPadSingle;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlockLandingPad.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockLandingPad extends GCCoreBlockAdvancedTile implements IPartialSealableBlock
{
	private IIcon[] icons = new IIcon[3];

	public GCCoreBlockLandingPad(String assetName)
	{
		super(Material.iron);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.2F, 1.0F);
		this.setHardness(1.0F);
		this.setResistance(10.0F);
		this.setStepSound(Block.soundTypeStone);
		this.setBlockTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setBlockName(assetName);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftTab;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int i = 0; i < 2; i++)
		{
			par3List.add(new ItemStack(par1, 1, i));
		}
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.icons[0] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "launch_pad");
		this.icons[1] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "buggy_fueler_blank");
		this.icons[2] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "cargo_pad");
		this.blockIcon = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "launch_pad");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2)
	{
		if (par2 < 0 || par2 > this.icons.length)
		{
			return this.blockIcon;
		}

		return this.icons[par2];
	}

	@Override
	public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5)
	{
		Block block = GCCoreBlocks.landingPad;

		if (par1World.getBlock(par2 + 1, par3, par4) == block && par1World.getBlock(par2 + 2, par3, par4) == block && par1World.getBlock(par2 + 3, par3, par4) == block)
		{
			return false;
		}

		if (par1World.getBlock(par2 - 1, par3, par4) == block && par1World.getBlock(par2 - 2, par3, par4) == block && par1World.getBlock(par2 - 3, par3, par4) == block)
		{
			return false;
		}

		if (par1World.getBlock(par2, par3, par4 + 1) == block && par1World.getBlock(par2, par3, par4 + 2) == block && par1World.getBlock(par2, par3, par4 + 3) == block)
		{
			return false;
		}

		if (par1World.getBlock(par2, par3, par4 - 1) == block && par1World.getBlock(par2, par3, par4 - 2) == block && par1World.getBlock(par2, par3, par4 - 3) == block)
		{
			return false;
		}

		if (par1World.getBlock(par2, par3 - 1, par4) == GCCoreBlocks.landingPad && par5 == 1)
		{
			return false;
		}
		else
		{
			return this.canPlaceBlockAt(par1World, par2, par3, par4);
		}
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
		switch (metadata)
		{
		case 0:
			return new TileEntityLandingPadSingle();
		case 1:
			return new TileEntityBuggyFuelerSingle();
			// case 2:
			// return new TileEntityCargoPadSingle();
		default:
			return null;
		}
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
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		return null;
	}

	@Override
	public boolean isSealed(World world, int x, int y, int z, ForgeDirection direction)
	{
		return direction == ForgeDirection.UP || direction == ForgeDirection.DOWN;
	}
}
