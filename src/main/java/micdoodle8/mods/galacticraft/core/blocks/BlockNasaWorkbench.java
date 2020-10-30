package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.recipe.SchematicRegistry;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.inventory.ContainerFuelLoader;
import micdoodle8.mods.galacticraft.core.inventory.ContainerSchematic;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFuelLoader;
import micdoodle8.mods.galacticraft.core.tile.TileEntityNasaWorkbench;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BlockNasaWorkbench extends Block implements IShiftDescription, IPartialSealableBlock, ISortable
{
    public BlockNasaWorkbench(Properties builder)
    {
        super(builder);
    }

//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(BlockState blockState, IBlockReader worldIn, BlockPos pos)
//    {
//        return VoxelShapes.create((double) pos.getX() + -0.0F, (double) pos.getY() + 0.0F, (double) pos.getZ() + -0.0F, (double) pos.getX() + 1.0F, (double) pos.getY() + 1.4F, (double) pos.getZ() + 1.0F);
//    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
//    {
//        return this.getCollisionBoundingBox(worldIn, pos, worldIn.getBlockState(pos));
//    }
//
//    @Override
//    public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end)
//    {
//        this.setBlockBounds(-0.0F, 0.0F, -0.0F, 1.0F, 1.4F, 1.0F);
//
//        final RayTraceResult r = super.collisionRayTrace(worldIn, pos, start, end);
//
//        this.setBlockBounds(-0.0F, 0.0F, -0.0F, 1.0F, 1.4F, 1.0F);
//
//        return r;
//    }
//
//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
//    {
//        this.setBlockBounds(-0.0F, 0.0F, -0.0F, 1.0F, 1.4F, 1.0F);
//        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//    }

//    @Override
//    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
//    {
//        return true;
//    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        BlockMulti.onPlacement(worldIn, pos, placer, this);
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        final TileEntity var9 = worldIn.getTileEntity(pos);

        int fakeBlockCount = 0;

        for (int x = -1; x < 2; x++)
        {
            for (int y = 0; y < 4; y++)
            {
                for (int z = -1; z < 2; z++)
                {
                    if (!(x == 0 && y == 0 && z == 0))
                    {
                        if (Math.abs(x) != 1 || Math.abs(z) != 1)
                        {
                            Block block = worldIn.getBlockState(pos.add(x, y, z)).getBlock();

                            if ((y == 0 || y == 3) && x == 0 && z == 0)
                            {
                                if (block == GCBlocks.fakeBlock)
                                {
                                    fakeBlockCount++;
                                }
                            }
                            else if (y != 0 && y != 3)
                            {
                                if (block == GCBlocks.fakeBlock)
                                {
                                    fakeBlockCount++;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (fakeBlockCount >= 11 && var9 instanceof IMultiBlock)
        {
            ((IMultiBlock) var9).onDestroy(var9);
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
    {
        if (!worldIn.isRemote)
        {
            SimpleNamedContainerProvider container = SchematicRegistry.getMatchingRecipeForID(0).getContainerProvider(playerIn);
            NetworkHooks.openGui((ServerPlayerEntity) playerIn, container, buf -> buf.writeBlockPos(pos));
        }
        return ActionResultType.SUCCESS;
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockReader worldIn, BlockPos pos)
//    {
//        this.setBlockBounds(-0.0F, 0.0F, -0.0F, 1.0F, 1.4F, 1.0F);
//    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityNasaWorkbench();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public String getShiftDescription(ItemStack stack)
    {
        return GCCoreUtil.translate(this.getTranslationKey() + ".description");
    }

    @Override
    public boolean showDescription(ItemStack stack)
    {
        return true;
    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, Direction direction)
    {
        return true;
    }

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }

    @Override
    public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        super.eventReceived(state, worldIn, pos, id, param);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(id, param);
    }
}
