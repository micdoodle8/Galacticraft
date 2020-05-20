package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityParaChest;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.*;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Random;

public class BlockParaChest extends ContainerBlock implements ITileEntityProvider, IShiftDescription, ISortableBlock
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", Direction.Plane.HORIZONTAL);
    public static final PropertyEnum<DyeColor> COLOR = PropertyEnum.create("color", DyeColor.class);
    protected static final AxisAlignedBB NOT_CONNECTED_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);

    public BlockParaChest(String assetName)
    {
        super(Material.WOOD);
        this.setHardness(3.0F);
        this.setSoundType(SoundType.WOOD);
//        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        this.setUnlocalizedName(assetName);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, Direction.NORTH));
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos)
    {
        return NOT_CONNECTED_AABB;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @Override
    public ItemGroup getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    @Override
    public boolean isOpaqueCube(BlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(BlockState state)
    {
        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, BlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, Direction side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            IInventory iinventory = this.getInventory(worldIn, pos);

            if (iinventory != null && playerIn instanceof ServerPlayerEntity)
            {
                playerIn.openGui(GalacticraftCore.instance, -1, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }

            return true;
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        TileEntityParaChest tileentitychest = (TileEntityParaChest) worldIn.getTileEntity(pos);

        if (tileentitychest != null)
        {
            tileentitychest.updateContainingBlockInfo();
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, BlockState state)
    {
        TileEntityParaChest tileentitychest = (TileEntityParaChest) worldIn.getTileEntity(pos);

        if (tileentitychest != null)
        {
            Random syncRandom = GCCoreUtil.getRandom(pos);
            for (int j1 = 0; j1 < tileentitychest.getSizeInventory(); ++j1)
            {
                ItemStack itemstack = tileentitychest.getStackInSlot(j1);

                if (itemstack != null)
                {

                    float f = syncRandom.nextFloat() * 0.8F + 0.1F;
                    float f1 = syncRandom.nextFloat() * 0.8F + 0.1F;
                    ItemEntity entityitem;

                    for (float f2 = syncRandom.nextFloat() * 0.8F + 0.1F; !itemstack.isEmpty(); worldIn.spawnEntity(entityitem))
                    {
                        entityitem = new ItemEntity(worldIn, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, itemstack.splitStack(syncRandom.nextInt(21) + 10));
                        float f3 = 0.05F;
                        entityitem.motionX = (float) syncRandom.nextGaussian() * f3;
                        entityitem.motionY = (float) syncRandom.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float) syncRandom.nextGaussian() * f3;
                    }
                }
            }

            worldIn.updateComparatorOutputLevel(pos, null);
        }

        super.breakBlock(worldIn, pos, state);
    }

    public IInventory getInventory(World par1World, BlockPos pos)
    {
        Object object = par1World.getTileEntity(pos);

        if (object == null)
        {
            return null;
        }
        else if (par1World.isSideSolid(pos.offset(Direction.UP), Direction.DOWN))
        {
            return null;
        }
        else if (BlockParaChest.isOcelotBlockingChest(par1World, pos))
        {
            return null;
        }
        else
        {
            return (IInventory) object;
        }
    }

    public static boolean isOcelotBlockingChest(World par0World, BlockPos pos)
    {
        Iterator<?> iterator = par0World.getEntitiesWithinAABB(OcelotEntity.class, new AxisAlignedBB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1)).iterator();
        OcelotEntity entityocelot;

        do
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            entityocelot = (OcelotEntity) iterator.next();
        }
        while (!entityocelot.isSitting());

        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World par1World, int meta)
    {
        return new TileEntityParaChest();
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
    public BlockState getStateFromMeta(int meta)
    {
        Direction enumfacing = Direction.getFront(meta);

        if (enumfacing.getAxis() == Direction.Axis.Y)
        {
            enumfacing = Direction.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(BlockState state)
    {
        return (state.getValue(FACING)).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, COLOR, FACING);
    }

    @Override
    public BlockState getActualState(BlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (!(tile instanceof TileEntityParaChest))
        {
            return state;
        }
        TileEntityParaChest chest = (TileEntityParaChest) tile;
        return state.withProperty(COLOR, chest.color);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
    }
}
