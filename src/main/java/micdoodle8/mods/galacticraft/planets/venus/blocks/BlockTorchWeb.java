package micdoodle8.mods.galacticraft.planets.venus.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTorchWeb extends Block implements IShearable, IShiftDescription, ISortableBlock
{
    public static final PropertyEnum WEB_TYPE = PropertyEnum.create("webtype", EnumWebType.class);
    protected static final AxisAlignedBB AABB_WEB = new AxisAlignedBB(0.35, 0.0, 0.35, 0.65, 1.0, 0.65);
    protected static final AxisAlignedBB AABB_WEB_TORCH = new AxisAlignedBB(0.35, 0.25, 0.35, 0.65, 1.0, 0.65);

    public enum EnumWebType implements IStringSerializable
    {
        WEB_0(0, "web_torch_0"),
        WEB_1(1, "web_torch_1");

        private final int meta;
        private final String name;

        EnumWebType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumWebType byMetadata(int meta)
        {
            return values()[meta];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockTorchWeb(String assetName)
    {
        super(Material.CIRCUITS);
        this.setLightLevel(1.0F);
        this.setUnlocalizedName(assetName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        if (state.getValue(WEB_TYPE) == EnumWebType.WEB_1)
        {
            return AABB_WEB_TORCH;
        }

        return AABB_WEB;
    }

    //    @Override
//    public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end)
//    {
//        EnumWebType type = (EnumWebType)worldIn.getBlockState(pos).getValue(WEB_TYPE);
//        float f = 0.15F;
//
//        if (type == EnumWebType.WEB_0)
//        {
//            this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
//        }
//        else if (type == EnumWebType.WEB_1)
//        {
//            this.setBlockBounds(0.5F - f, 0.25F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
//        }
//
//        return super.collisionRayTrace(worldIn, pos, start, end);
//    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (state.getValue(WEB_TYPE) == EnumWebType.WEB_1)
        {
            return 15;
        }

        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return null;
    }

    public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
    {
        IBlockState blockUp = world.getBlockState(pos.up());
        
        int meta = this.getMetaFromState(state);

        if (meta == 0)
        {
            return blockUp.getMaterial().isSolid() || blockUp.getBlock() == this && blockUp.getValue(WEB_TYPE) == EnumWebType.WEB_0;
        }
        else
        {
            return blockUp.getBlock() == this && blockUp.getValue(WEB_TYPE) == EnumWebType.WEB_0;
        }
    }

    @Override
    public boolean canReplace(World world, BlockPos pos, EnumFacing side, ItemStack itemStack)
    {
        if (world == null || pos == null || side == null || itemStack == null)
        {
            return false;
        }
        return this.canBlockStay(world, pos, this.getStateFromMeta(itemStack.getMetadata()));
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn)
    {
        this.checkAndDropBlock(world, pos, state);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        this.checkAndDropBlock(world, pos, state);
    }

    protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state)
    {
        if (!this.canBlockStay(world, pos, state))
        {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.AIR);
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }

    @Override
    public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos)
    {
        return true;
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
    {
        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
        ret.add(new ItemStack(this, 1, this.getMetaFromState(world.getBlockState(pos))));
        return ret;
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
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(WEB_TYPE, EnumWebType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumWebType) state.getValue(WEB_TYPE)).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, WEB_TYPE);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
    }
}
