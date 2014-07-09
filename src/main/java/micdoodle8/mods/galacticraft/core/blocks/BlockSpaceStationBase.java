package micdoodle8.mods.galacticraft.core.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.BlockVec3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySpaceStationBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

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
		this.blockIcon = this.spaceStationIcons[0];
	}

	@Override
	public IIcon getIcon(int par1, int par2)
	{
		switch (par1)
		{
		case 1:
			return this.spaceStationIcons[0];
		default:
			return this.spaceStationIcons[1];
		}
	}

	@Override
	public void breakBlock(World var1, int var2, int var3, int var4, Block var5, int var6)
	{
		final TileEntity tileAt = var1.getTileEntity(var2, var3, var4);

		if (tileAt instanceof IMultiBlock)
		{
			((IMultiBlock) tileAt).onDestroy(tileAt);
		}

		super.breakBlock(var1, var2, var3, var4, var5, var6);
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
			((IMultiBlock) tile).onCreate(new BlockVec3(x, y, z));
		}
	}
}
