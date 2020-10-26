package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.core.blocks.BlockTileGC;
import micdoodle8.mods.galacticraft.core.items.ISortable;
import micdoodle8.mods.galacticraft.core.client.fx.BlockPosParticleData;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.galacticraft.core.util.EnumColor;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.client.fx.AsteroidParticles;
import micdoodle8.mods.galacticraft.planets.asteroids.dimension.ShortRangeTelepadHandler;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class BlockShortRangeTelepad extends BlockTileGC implements IShiftDescription, ISortable
{
    protected static final VoxelShape AABB_TELEPAD = VoxelShapes.create(0.0F, 0.0F, 0.0F, 1.0F, 0.45F, 1.0F);

    protected BlockShortRangeTelepad(Properties builder)
    {
        super(builder);
    }

//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.INVISIBLE;
    }

//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }

//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityShortRangeTelepad();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return AABB_TELEPAD;
    }

//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
//    {
//        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.45F, 1.0F);
//    }

//    @Override
//    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
//    {
//        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.45F, 1.0F);
//        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
//    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        TileEntity tile = worldIn.getTileEntity(pos);

        boolean validSpot = true;

        for (int x = -1; x <= 1; x++)
        {
            for (int y = 0; y < 3; y += 2)
            {
                for (int z = -1; z <= 1; z++)
                {
                    if (!(x == 0 && y == 0 && z == 0))
                    {
                        BlockState stateAt = worldIn.getBlockState(pos.add(x, y, z));

                        if (!stateAt.getMaterial().isReplaceable())
                        {
                            validSpot = false;
                        }
                    }
                }
            }
        }

        if (!validSpot)
        {
            worldIn.removeBlock(pos, false);

            if (placer instanceof PlayerEntity)
            {
                if (!worldIn.isRemote)
                {
                    placer.sendMessage(new StringTextComponent(EnumColor.RED + GCCoreUtil.translate("gui.warning.noroom")));
                }
                ((PlayerEntity) placer).inventory.addItemStackToInventory(new ItemStack(Item.getItemFromBlock(this), 1));
            }

            return;
        }

        if (tile instanceof TileEntityShortRangeTelepad)
        {
            ((TileEntityShortRangeTelepad) tile).onCreate(worldIn, pos);
            ((TileEntityShortRangeTelepad) tile).setOwner(PlayerUtil.getName(((PlayerEntity) placer)));
        }
    }

    @Override
    public ActionResultType onMachineActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
        return ((IMultiBlock) worldIn.getTileEntity(pos)).onActivated(playerIn);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
    {
        final TileEntity tileAt = worldIn.getTileEntity(pos);

        int fakeBlockCount = 0;

        for (int x = -1; x <= 1; x++)
        {
            for (int y = 0; y < 3; y += 2)
            {
                for (int z = -1; z <= 1; z++)
                {
                    if (!(x == 0 && y == 0 && z == 0))
                    {
                        if (worldIn.getBlockState(pos.add(x, y, z)).getBlock() == AsteroidBlocks.fakeTelepad)
                        {
                            fakeBlockCount++;
                        }
                    }
                }
            }
        }

        if (tileAt instanceof TileEntityShortRangeTelepad)
        {
            if (fakeBlockCount > 0)
            {
                ((TileEntityShortRangeTelepad) tileAt).onDestroy(tileAt);
            }
            ShortRangeTelepadHandler.removeShortRangeTeleporter((TileEntityShortRangeTelepad) tileAt);
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        final TileEntity tileAt = worldIn.getTileEntity(pos);

        if (tileAt instanceof TileEntityShortRangeTelepad)
        {
            TileEntityShortRangeTelepad telepad = (TileEntityShortRangeTelepad) tileAt;

            for (int i = 0; i < 6; i++)
            {
                BlockPosParticleData data = new BlockPosParticleData(AsteroidParticles.TELEPAD_UP, pos);
                for (int j = 0; j < 4; j++)
                {
                    worldIn.addParticle(data, pos.getX() + 0.2 + rand.nextDouble() * 0.6, pos.getY() + 0.1, pos.getZ() + 0.2 + rand.nextDouble() * 0.6, 0.0, 1.4, 0.0);
//                    GalacticraftPlanets.addParticle("portalBlue", new Vector3(pos.getX() + 0.2 + rand.nextDouble() * 0.6, pos.getY() + 0.1, pos.getZ() + 0.2 + rand.nextDouble() * 0.6), new Vector3(0.0, 1.4, 0.0), telepad, false);
                }

                data = new BlockPosParticleData(AsteroidParticles.TELEPAD_DOWN, pos);
                worldIn.addParticle(data, pos.getX() + 0.0 + rand.nextDouble() * 0.2, pos.getY() + 2.9, pos.getZ() + rand.nextDouble(), 0.0, -2.95, 0.0);
                worldIn.addParticle(data, pos.getX() + 0.8 + rand.nextDouble() * 0.2, pos.getY() + 2.9, pos.getZ() + rand.nextDouble(), 0.0, -2.95, 0.0);
                worldIn.addParticle(data, pos.getX() + rand.nextDouble(), pos.getY() + 2.9, pos.getZ() + 0.2 + rand.nextDouble() * 0.2, 0.0, -2.95, 0.0);
                worldIn.addParticle(data, pos.getX() + rand.nextDouble(), pos.getY() + 2.9, pos.getZ() + 0.8 + rand.nextDouble() * 0.2, 0.0, -2.95, 0.0);
            }
        }
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

    @Override
    public EnumSortCategory getCategory()
    {
        return EnumSortCategory.MACHINE;
    }
}
