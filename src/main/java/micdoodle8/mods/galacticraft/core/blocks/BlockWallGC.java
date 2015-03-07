package micdoodle8.mods.galacticraft.core.blocks;

import java.util.List;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWallGC extends Block //Don't extends to BlockWall
{
	public static PropertyBool UP = PropertyBool.create("up");
	public static PropertyBool NORTH = PropertyBool.create("north");
	public static PropertyBool EAST = PropertyBool.create("east");
	public static PropertyBool SOUTH = PropertyBool.create("south");
	public static PropertyBool WEST = PropertyBool.create("west");
	public static PropertyEnum VARIANT = PropertyEnum.create("variant", BlockType.class);

	public BlockWallGC(String name)
	{
		super(Material.rock);
		this.setHardness(1.5F);
		this.setResistance(2.5F);
		this.setDefaultState(this.getDefaultState().withProperty(UP, Boolean.valueOf(false)).withProperty(NORTH, Boolean.valueOf(false)).withProperty(EAST, Boolean.valueOf(false)).withProperty(SOUTH, Boolean.valueOf(false)).withProperty(WEST, Boolean.valueOf(false)).withProperty(VARIANT, BlockType.tin_1_wall));
		this.setUnlocalizedName(name);
	}

	@Override
	public boolean isFullCube()
	{
		return false;
	}

	@Override
	public boolean isPassable(IBlockAccess world, BlockPos pos)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean canPlaceTorchOnTop(IBlockAccess world, BlockPos pos)
	{
		return true;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess world, BlockPos pos)
	{
		boolean flag = this.canConnectTo(world, pos.north());
		boolean flag1 = this.canConnectTo(world, pos.south());
		boolean flag2 = this.canConnectTo(world, pos.west());
		boolean flag3 = this.canConnectTo(world, pos.east());
		float f = 0.25F;
		float f1 = 0.75F;
		float f2 = 0.25F;
		float f3 = 0.75F;
		float f4 = 1.0F;

		if (flag)
		{
			f2 = 0.0F;
		}
		if (flag1)
		{
			f3 = 1.0F;
		}
		if (flag2)
		{
			f = 0.0F;
		}
		if (flag3)
		{
			f1 = 1.0F;
		}

		if (flag && flag1 && !flag2 && !flag3)
		{
			f4 = 0.8125F;
			f = 0.3125F;
			f1 = 0.6875F;
		}
		else if (!flag && !flag1 && flag2 && flag3)
		{
			f4 = 0.8125F;
			f2 = 0.3125F;
			f3 = 0.6875F;
		}
		this.setBlockBounds(f, 0.0F, f2, f1, f4, f3);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World world, BlockPos pos, IBlockState state)
	{
		this.setBlockBoundsBasedOnState(world, pos);
		this.maxY = 1.5D;
		return super.getCollisionBoundingBox(world, pos, state);
	}

	private boolean canConnectTo(IBlockAccess world, BlockPos pos)
	{
		Block block = world.getBlockState(pos).getBlock();
		return block == Blocks.barrier ? false : block != this && !(block instanceof BlockFenceGate) ? block.getMaterial().isOpaque() && block.isFullCube() ? block.getMaterial() != Material.gourd : false : true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list)
	{
		if (GalacticraftCore.isPlanetsLoaded)
		{
			for (int i = 0; i < 6; ++i)
			{
				list.add(new ItemStack(this, 1, i));
			}
		}
		else
		{
			for (int i = 0; i < 4; ++i)
			{
				list.add(new ItemStack(this, 1, i));
			}
		}
	}

	@Override
	public CreativeTabs getCreativeTabToDisplayOn()
	{
		return GalacticraftCore.galacticraftBlocksTab;
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return this.getMetaFromState(state);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return side == EnumFacing.DOWN ? super.shouldSideBeRendered(world, pos, side) : true;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(VARIANT, BlockType.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((BlockType)state.getValue(VARIANT)).ordinal();
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return state.withProperty(UP, Boolean.valueOf(this.canConnectTo(world, pos.up()))).withProperty(NORTH, Boolean.valueOf(this.canConnectTo(world, pos.north()))).withProperty(EAST, Boolean.valueOf(this.canConnectTo(world, pos.east()))).withProperty(SOUTH, Boolean.valueOf(this.canConnectTo(world, pos.south()))).withProperty(WEST, Boolean.valueOf(this.canConnectTo(world, pos.west())));
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] {UP, NORTH, EAST, WEST, SOUTH, VARIANT});
	}

	public static enum BlockType implements IStringSerializable
	{
		tin_1_wall,
		tin_2_wall,
		moon_stone_wall,
		moon_dungeon_brick_wall,
		mars_cobblestone_wall,
		mars_dungeon_brick_wall;

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
	}
}
