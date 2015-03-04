/*
package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockSlabGC extends BlockSlab
{
	private static final String[] woodTypes = new String[] {
		"tin",
		"tin",
		"moon",
		"moonBricks",
		"mars",
		"marsBricks"
	};

	*/
/*private IIcon[] textures;
	private IIcon[] tinSideIcon;*//*

	private final boolean isDoubleSlab;

	public BlockSlabGC(String name, boolean isDouble, Material material)
	{
		super(material);
		this.isDoubleSlab = isDouble;
		this.setUnlocalizedName(name);
		this.useNeighborBrightness = true;
	}

	*/
/*@Override
	public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		this.textures = new IIcon[6];
		this.textures[0] = par1IconRegister.registerIcon("galacticraftcore:deco_aluminium_4");
		this.textures[1] = par1IconRegister.registerIcon("galacticraftcore:deco_aluminium_2");
		this.textures[2] = par1IconRegister.registerIcon("galacticraftmoon:bottom");
		this.textures[3] = par1IconRegister.registerIcon("galacticraftmoon:brick");

		if (GalacticraftCore.isPlanetsLoaded)
		{
			this.textures[4] = par1IconRegister.registerIcon("galacticraftmars:cobblestone");
			this.textures[5] = par1IconRegister.registerIcon("galacticraftmars:brick");
		}
		else
		{
			this.textures[4] = this.textures[3];
			this.textures[5] = this.textures[3];
		}

		this.tinSideIcon = new IIcon[1];
		this.tinSideIcon[0] = par1IconRegister.registerIcon("galacticraftcore:deco_aluminium_1");
	}

	@Override
	public IIcon getIcon(int side, int meta)
	{
		if (meta == 1 || meta == 9)
		{
			switch (side)
			{
			case 0:
				return this.textures[0]; //BOTTOM
			case 1:
				return this.textures[1]; //TOP
			case 2:
				return this.tinSideIcon[0]; //Z-
			case 3:
				return this.tinSideIcon[0]; //Z+
			case 4:
				return this.tinSideIcon[0]; //X-
			case 5:
				return this.tinSideIcon[0]; //X+
			}
		}
		return this.textures[getTypeFromMeta(meta)];
	}*//*


	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSubBlocks(Item block, CreativeTabs creativeTabs, List list)
	{
		int max = 0;

		if (GalacticraftCore.isPlanetsLoaded)
		{
			max = 6;//Number of slab types with Planets loaded 
		}
		else
		{
			max = 4;//Number of slab types with Planets not loaded
		}
		for (int i = 0; i < max; ++i)
		{
			list.add(new ItemStack(block, 1, i));
		}
	}

	@Override
    public String getUnlocalizedName(int meta)
	{
		return new StringBuilder().append(woodTypes[this.getWoodType(meta)]).append("Slab").toString();
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return getMetaFromState(state) & 7;
	}

	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		if (this.isDoubleSlab)
		{
			if (this == GCBlocks.slabGCDouble)
			{
				return Item.getItemFromBlock(GCBlocks.slabGCHalf);
			}
		}
		return Item.getItemFromBlock(this);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		if (!this.isDoubleSlab)
		{
			return GalacticraftCore.galacticraftBlocksTab;
		}
		return null;
	}

	@Override
	public float getBlockHardness(World worldIn, BlockPos pos)
	{
		int meta = getMetaFromState(worldIn.getBlockState(pos));
		float hardness = this.blockHardness;

		switch (getTypeFromMeta(meta))
		{
		case 2:
		case 3:
			hardness = 1.5F;
			break;
		default:
			hardness = 2.0F;
			break;
		}

		return hardness;
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return super.getBlockHardness(world, pos);
	}

	@Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos)
	{
		if (this == GCBlocks.slabGCDouble)
		{
			return new ItemStack(GCBlocks.slabGCHalf, 1, getMetaFromState(world.getBlockState(pos)));
		}
		return new ItemStack(GCBlocks.slabGCHalf, 1, getMetaFromState(world.getBlockState(pos)) & 7);
	}

	@Override
	protected ItemStack createStackedBlock(IBlockState state)
	{
		return new ItemStack(this, 2, getMetaFromState(state));
	}

	private int getWoodType(int meta)
	{
		meta = getTypeFromMeta(meta);

		if (meta < woodTypes.length)
		{
			return meta;
		}
		return 0;
	}

	private static int getTypeFromMeta(int meta)
	{
		return meta & 7;
	}
}*/
