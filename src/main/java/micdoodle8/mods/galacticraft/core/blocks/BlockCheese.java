package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockCheese extends Block implements IShiftDescription, ISortableBlock
{
    public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 6);
    protected static final AxisAlignedBB[] CHEESE_AABB = new AxisAlignedBB[] {
            new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            new AxisAlignedBB(0.1875, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            new AxisAlignedBB(0.3125, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            new AxisAlignedBB(0.4375, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            new AxisAlignedBB(0.5625, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            new AxisAlignedBB(0.6875, 0.0, 0.0625, 0.9375, 0.5, 0.9375),
            new AxisAlignedBB(0.8125, 0.0, 0.0625, 0.9375, 0.5, 0.9375)
    };

    public BlockCheese(String assetName)
    {
        super(Material.CAKE);
        this.setTickRandomly(true);
        this.disableStats();
        this.setHardness(0.5F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BITES, Integer.valueOf(0)));
        this.setSoundType(SoundType.CLOTH);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return CHEESE_AABB[((Integer)state.getValue(BITES)).intValue()];
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
//    {
//        float f = 0.0625F;
//        float f1 = (float) (1 + ((Integer) worldIn.getBlockState(pos).getValue(BITES)).intValue() * 2) / 16.0F;
//        float f2 = 0.5F;
//        this.setBlockBounds(f1, 0.0F, f, 1.0F - f, f2, 1.0F - f);
//    }
//
//    @Override
//    public void setBlockBoundsForItemRender()
//    {
//        float f = 0.0625F;
//        float f1 = 0.5F;
//        this.setBlockBounds(f, 0.0F, f, 1.0F - f, f1, 1.0F - f);
//    }
//
//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
//    {
//        float f = 0.0625F;
//        float f1 = (float) (1 + ((Integer) state.getValue(BITES)).intValue() * 2) / 16.0F;
//        float f2 = 0.5F;
//        return new AxisAlignedBB((double) ((float) pos.getX() + f1), (double) pos.getY(), (double) ((float) pos.getZ() + f), (double) ((float) (pos.getX() + 1) - f), (double) ((float) pos.getY() + f2), (double) ((float) (pos.getZ() + 1) - f));
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
//    {
//        return this.getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
//    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        this.eatCheeseSlice(worldIn, pos, worldIn.getBlockState(pos), playerIn);
        return true;
    }

    @Override
    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {
        this.eatCheeseSlice(worldIn, pos, worldIn.getBlockState(pos), playerIn);
    }

    private void eatCheeseSlice(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn)
    {
        if (playerIn.canEat(false))
        {
            playerIn.getFoodStats().addStats(2, 0.1F);
            int i = ((Integer) state.getValue(BITES)).intValue();

            if (i < 6)
            {
                worldIn.setBlockState(pos, state.withProperty(BITES, Integer.valueOf(i + 1)), 3);
            }
            else
            {
                worldIn.setBlockToAir(pos);
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
    {
        if (!this.canBlockStay(worldIn, pos))
        {
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean canBlockStay(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).getBlock().getMaterial(worldIn.getBlockState(pos)).isSolid();
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.AIR);
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BITES, Integer.valueOf(meta));
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(Items.CAKE, 1, 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer) state.getValue(BITES)).intValue();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] { BITES });
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return (7 - ((Integer) worldIn.getBlockState(pos).getValue(BITES)).intValue()) * 2;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
    }
}
