package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BlockMinerBaseFull extends BlockTileGC
{
    public BlockMinerBaseFull(Properties builder)
    {
        super(builder);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return 1.0F;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
    {
        return true;
    }

    @Override
    public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean canEntitySpawn(BlockState state, IBlockReader worldIn, BlockPos pos, EntityType<?> type)
    {
        return false;
    }

//    @Override
//    public int damageDropped(BlockState state)
//    {
//        return 0;
//    }
//
//    @Override
//    public int quantityDropped(BlockState state, int fortune, Random random)
//    {
//        return 1;
//    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityMinerBase();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

//    @Override
//    public BlockState getStateForPlacement(BlockItemUseContext context)
//    {
//        return this.getStateFromMeta(0);
//        //TODO
//        //return this.getMetadataFromAngle(world, x, y, z, LogicalSide);
//    }

//    @Override
//    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, Direction side)
//    {
//        //TODO
//        /*
//    	if (this.getMetadataFromAngle(world, x, y, z, LogicalSide) != -1)
//        {
//            return true;
//        }
//    	 */
//
//        return true;
//    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileEntityMinerBase)
        {
            ((TileEntityMinerBase) tileEntity).onBlockRemoval();
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ActionResultType onMachineActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof TileEntityMinerBase)
        {
            return ((TileEntityMinerBase) tileEntity).onActivated(playerIn);
        }
        else
        {
            return ActionResultType.PASS;
        }
    }

//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        return Item.getItemFromBlock(AsteroidBlocks.blockMinerBase);
//    }
//
//    @Override
//    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, BlockState state, int fortune)
//    {
//        ArrayList<ItemStack> ret = new ArrayList<ItemStack>();
//        ret.add(new ItemStack(Item.getItemFromBlock(AsteroidBlocks.blockMinerBase), 8, 0));
//        return ret;
//    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
    {
        return new ItemStack(Item.getItemFromBlock(AsteroidBlocks.blockMinerBase), 1);
    }

    @Override
    public ActionResultType onUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityMinerBase)
        {
            ((TileEntityMinerBase) te).updateFacing();
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.CONSUME;
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public BlockRenderLayer getRenderLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }
}
