package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySpaceStationBase;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreBlockSpaceStationBase.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class BlockSpaceStationBase extends BlockContainer implements ITileEntityProvider
{
	private IIcon[] spaceStationIcons;

	public BlockSpaceStationBase(String assetName)
	{
		super(Material.rock);
		this.setHardness(10000.0F);
		this.setBlockTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setBlockName(assetName);
	}

	@Override
	public float getBlockHardness(World par1World, int par2, int par3, int par4)
	{
		return -1.0F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.spaceStationIcons = new IIcon[2];
		this.spaceStationIcons[0] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "space_station_top");
		this.spaceStationIcons[1] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "space_station_side");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
	{
		switch (par5)
		{
		case 1:
			return this.spaceStationIcons[0];
		default:
			return this.spaceStationIcons[1];
		}
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntitySpaceStationBase();
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);

		TileEntity tile = world.getTileEntity(x, y, z);

		if (tile instanceof IMultiBlock)
		{
			((IMultiBlock) tile).onCreate(new Vector3(x, y, z));
		}
	}
}
