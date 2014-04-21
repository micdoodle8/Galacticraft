package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBeamReflector;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockBeamReflector extends BlockTileGC
{
	public BlockBeamReflector(String assetName)
	{
		super(Material.iron);
		this.setBlockName(assetName);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftTab;
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
	public int getRenderType()
	{
		return -1;
	}

	@Override
	public int damageDropped(int metadata)
	{
		return metadata;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata)
	{
//		switch (metadata)
//		{
//		case 0:
//			return new TileEntityBeamReflector();
//		case 1:
//			return new TileEntityBeamReflector();
//		default:
//			return null;
//		}

		return new TileEntityBeamReflector();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(par1, 1, 0));
	}
}
