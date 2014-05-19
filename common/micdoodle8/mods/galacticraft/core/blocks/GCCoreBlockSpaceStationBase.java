package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntitySpaceStationBase;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
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
public class GCCoreBlockSpaceStationBase extends BlockContainer implements ITileEntityProvider
{
	private Icon[] spaceStationIcons;

	public GCCoreBlockSpaceStationBase(int id, String assetName)
	{
		super(id, Material.rock);
		this.setHardness(10000.0F);
		this.setTextureName(GalacticraftCore.ASSET_PREFIX + assetName);
		this.setUnlocalizedName(assetName);
	}

	@Override
	public float getBlockHardness(World par1World, int par2, int par3, int par4)
	{
		return -1.0F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.spaceStationIcons = new Icon[2];
		this.spaceStationIcons[0] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "space_station_top");
		this.spaceStationIcons[1] = par1IconRegister.registerIcon(GalacticraftCore.ASSET_PREFIX + "space_station_side");
		this.blockIcon = this.spaceStationIcons[0];
	}
	
    public Icon getIcon(int par1, int par2)
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
	public void breakBlock(World var1, int var2, int var3, int var4, int var5, int var6)
	{
		final TileEntity tileAt = var1.getBlockTileEntity(var2, var3, var4);

		if (tileAt instanceof IMultiBlock)
		{
			((IMultiBlock) tileAt).onDestroy(tileAt);
		}

		super.breakBlock(var1, var2, var3, var4, var5, var6);
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new GCCoreTileEntitySpaceStationBase();
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
	{
		super.onBlockPlacedBy(world, x, y, z, entityLiving, itemStack);

		TileEntity tile = world.getBlockTileEntity(x, y, z);

		if (tile instanceof IMultiBlock)
		{
			((IMultiBlock) tile).onCreate(new Vector3(x, y, z));
		}
	}
}
