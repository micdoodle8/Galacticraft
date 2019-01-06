package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.ISortableBlock;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockCavernousVine extends Block implements IShearable, IShiftDescription, ISortableBlock
{
    public static final PropertyEnum<EnumVineType> VINE_TYPE = PropertyEnum.create("vinetype", EnumVineType.class);

    public enum EnumVineType implements IStringSerializable
    {
        VINE_0(0, "vine_0"),
        VINE_1(1, "vine_1"),
        VINE_2(2, "vine_2");

        private final int meta;
        private final String name;

        private EnumVineType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        private final static EnumVineType[] values = values();
        public static EnumVineType byMetadata(int meta)
        {
            return values[meta % values.length];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockCavernousVine(String assetName)
    {
        super(Material.VINE);
        this.setLightLevel(1.0F);
        this.setTickRandomly(true);
        this.setSoundType(SoundType.PLANT);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (world.setBlockToAir(pos))
        {
            int y2 = pos.getY() - 1;
            while (world.getBlockState(new BlockPos(pos.getX(), y2, pos.getZ())).getBlock() == this)
            {
                world.setBlockToAir(new BlockPos(pos.getX(), y2, pos.getZ()));
                y2--;
            }

            return true;
        }

        return false;
    }

    public boolean canBlockStay(World worldIn, BlockPos pos)
    {
        IBlockState stateAbove = worldIn.getBlockState(pos.up());
        return (stateAbove.getBlock() == this || stateAbove.getMaterial().isSolid());
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);

        if (!this.canBlockStay(worldIn, pos))
        {
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (entityIn instanceof EntityLivingBase)
        {
            if (entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).capabilities.isFlying)
            {
                return;
            }

            entityIn.motionY = 0.06F;
            entityIn.rotationYaw += 0.4F;

            ((EntityLivingBase) entityIn).addPotionEffect(new PotionEffect(MobEffects.POISON, 5, 20, false, true));
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return this.getVineLight(world, pos);
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
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return null;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing facing)
    {
        return facing == EnumFacing.DOWN && world.getBlockState(pos.up()).getBlockFaceShape(world, pos.up(), facing) == BlockFaceShape.SOLID;
    }

    public int getVineLength(IBlockAccess world, BlockPos pos)
    {
        int vineCount = 0;
        int y2 = pos.getY();

        while (world.getBlockState(new BlockPos(pos.getX(), y2, pos.getZ())).getBlock() == MarsBlocks.vine)
        {
            vineCount++;
            y2++;
        }

        return vineCount;
    }

    public int getVineLight(IBlockAccess world, BlockPos pos)
    {
        int vineCount = 0;
        int y2 = pos.getY();

        while (world.getBlockState(new BlockPos(pos.getX(), y2, pos.getZ())).getBlock() == MarsBlocks.vine)
        {
            vineCount += 4;
            y2--;
        }

        return Math.max(19 - vineCount, 0);
    }

    @Override
    public int tickRate(World par1World)
    {
        return 50;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            for (int y2 = pos.getY() - 1; y2 >= pos.getY() - 2; y2--)
            {
                BlockPos pos1 = new BlockPos(pos.getX(), y2, pos.getZ());
                Block blockID = worldIn.getBlockState(pos1).getBlock();

                if (!blockID.isAir(worldIn.getBlockState(pos1), worldIn, pos1))
                {
                    return;
                }
            }

            worldIn.setBlockState(pos.down(), this.getStateFromMeta(this.getVineLength(worldIn, pos) % 3), 2);
            worldIn.checkLight(pos);
        }

    }

//    @Override
//    public void onBlockAdded(World world, int x, int y, int z)
//    {
//        if (!world.isRemote)
//        {
//            // world.scheduleBlockUpdate(x, y, z, this,
//            // this.tickRate(world) + world.rand.nextInt(10));
//        }
//    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(Blocks.AIR);
    }

    @Override
    public int quantityDropped(Random par1Random)
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
        ret.add(new ItemStack(this, 1, 0));
        return ret;
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity)
    {
        return true;
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
        return this.getDefaultState().withProperty(VINE_TYPE, EnumVineType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumVineType) state.getValue(VINE_TYPE)).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, VINE_TYPE);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
    }
}
