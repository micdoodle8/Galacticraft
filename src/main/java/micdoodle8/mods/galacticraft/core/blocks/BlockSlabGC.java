package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSlabGC extends BlockSlab
{
	public static PropertyEnum VARIANT = PropertyEnum.create("variant", BlockType.class);

	public BlockSlabGC(String name, Material material)
	{
		super(material);
		this.setUnlocalizedName(name);
		this.useNeighborBrightness = true;
	}

	public BlockSlabGC(Material material)
	{
		super(material);
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
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
			list.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return this.getMetaFromState(state) & 7;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(this);
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftBlocksTab;
	}

	@Override
	public float getBlockHardness(World world, BlockPos pos)
	{
		Block block = world.getBlockState(pos).getBlock();

		if (!(block instanceof BlockSlabGC)) //This will prevent the crash when harvest block
		{
			return 0;
		}

		switch (this.getMetaFromState(world.getBlockState(pos)))
		{
		case 2:
		case 3:
			return 1.5F;
		default:
			return 2.0F;
		}
	}

	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
	{
		return super.getBlockHardness(world, pos);
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos)
	{
		return new ItemStack(this, 1, this.getMetaFromState(world.getBlockState(pos)) & 7);
	}

	@Override
	public String getUnlocalizedName(int meta)
	{
		return super.getUnlocalizedName();
	}

	@Override
	public boolean isDouble()
	{
		return false;
	}

	@Override
	public IProperty getVariantProperty()
	{
		return VARIANT;
	}

	@Override
	public Object getVariant(ItemStack itemStack)
	{
		return BlockType.byMetadata(itemStack.getMetadata() & 7);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState state = this.getDefaultState().withProperty(VARIANT, BlockType.byMetadata(meta & 7));

		if (!this.isDouble())
		{
			state = state.withProperty(HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
		}
		return state;
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		byte b0 = 0;
		int i = b0 | ((BlockType)state.getValue(VARIANT)).getMetadata();

		if (!this.isDouble() && state.getValue(HALF) == EnumBlockHalf.TOP)
		{
			i |= 8;
		}
		return i;
	}

	@Override
	protected BlockState createBlockState()
	{
		return this.isDouble() ? new BlockState(this, new IProperty[] {VARIANT}): new BlockState(this, new IProperty[] {HALF, VARIANT});
	}

	public static enum BlockType implements IStringSerializable
	{
		tin_slab_1(0),
		tin_slab_2(1),
		moon_stone_slab(2),
		moon_dungeon_brick_slab(3),
		mars_cobblestone_slab(4),
		mars_dungeon_slab(5);

		private int meta;
		private static BlockType[] META_LOOKUP = new BlockType[values().length];

		private BlockType(int meta)
		{
			this.meta = meta;
		}

		@Override
		public String toString()
		{
			return this.getName();
		}

		@Override
		public String getName()
		{
			return this.name();
		}

		public int getMetadata()
		{
			return this.meta;
		}

		public static BlockType byMetadata(int meta)
		{
			if (meta < 0 || meta >= META_LOOKUP.length)
			{
				meta = 0;
			}
			return META_LOOKUP[meta];
		}

		static
		{
			BlockType[] var0 = values();
			int var1 = var0.length;

			for (int var2 = 0; var2 < var1; ++var2)
			{
				BlockType var3 = var0[var2];
				META_LOOKUP[var3.getMetadata()] = var3;
			}
		}
	}
}
