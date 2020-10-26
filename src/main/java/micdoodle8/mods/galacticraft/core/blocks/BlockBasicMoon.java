//package micdoodle8.mods.galacticraft.core.blocks;
//
//import com.google.common.base.Predicate;
//import micdoodle8.mods.galacticraft.api.block.IDetectableResource;
//import micdoodle8.mods.galacticraft.api.block.IPlantableBlock;
//import micdoodle8.mods.galacticraft.api.block.ITerraformableBlock;
//import micdoodle8.mods.galacticraft.api.vector.BlockVec3Dim;
//import micdoodle8.mods.galacticraft.core.GCItems;
//import micdoodle8.mods.galacticraft.core.tick.TickHandlerServer;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.FlowerBlock;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.ItemStack;
//import net.minecraft.state.EnumProperty;
//import net.minecraft.util.Direction;
//import net.minecraft.util.IStringSerializable;
//import net.minecraft.util.NonNullList;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.ChunkPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.math.RayTraceResult;
//import net.minecraft.world.Explosion;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//import net.minecraftforge.common.IPlantable;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//public class BlockBasicMoon extends Block implements IDetectableResource, IPlantableBlock, ITerraformableBlock, ISortable
//{
//    public static final EnumProperty<EnumBlockBasicMoon> BASIC_TYPE_MOON = EnumProperty.create("basictypemoon", EnumBlockBasicMoon.class);
//
//    public enum EnumBlockBasicMoon implements IStringSerializable
//    {
//        ORE_COPPER_MOON(0, "ore_copper_moon"),
//        ORE_TIN_MOON(1, "ore_tin_moon"),
//        ORE_CHEESE_MOON(2, "ore_cheese_moon"),
//        MOON_DIRT(3, "moon_dirt_moon"),
//        MOON_STONE(4, "moon_stone"),
//        MOON_TURF(5, "moon_turf"),
//        ORE_SAPPHIRE(6, "ore_sapphire_moon"),
//        MOON_DUNGEON_BRICK(14, "moon_dungeon_brick");
//
//        private final int meta;
//        private final String name;
//
//        EnumBlockBasicMoon(int meta, String name)
//        {
//            this.meta = meta;
//            this.name = name;
//        }
//
//        public int getMeta()
//        {
//            return this.meta;
//        }
//
//        private final static EnumBlockBasicMoon[] values = values();
//        public static EnumBlockBasicMoon byMetadata(int meta)
//        {
//            if (meta < 7)
//            {
//                return values[meta];
//            }
//
//            return MOON_DUNGEON_BRICK;
//        }
//
//        @Override
//        public String getName()
//        {
//            return this.name;
//        }
//    }
//
//    public BlockBasicMoon(Properties builder)
//    {
//        super(builder);
//        this.setDefaultState(stateContainer.getBaseState().with(BASIC_TYPE_MOON, EnumBlockBasicMoon.ORE_COPPER_MOON));
//    }
//
////    @OnlyIn(Dist.CLIENT)
////    @Override
////    public ItemGroup getCreativeTabToDisplayOn()
////    {
////        return GalacticraftCore.galacticraftBlocksTab;
////    }
//
//    @Override
//    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
//    {
//        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) world.getBlockState(pos).getValue(BASIC_TYPE_MOON));
//
//        if (type == EnumBlockBasicMoon.MOON_DUNGEON_BRICK)
//        {
//            return 40.0F;
//        }
//        else if (type == EnumBlockBasicMoon.MOON_STONE)
//        {
//            return 6.0F;
//        }
//        else if (type == EnumBlockBasicMoon.ORE_COPPER_MOON || type == EnumBlockBasicMoon.ORE_TIN_MOON ||
//                type == EnumBlockBasicMoon.ORE_SAPPHIRE || type == EnumBlockBasicMoon.ORE_CHEESE_MOON)
//        {
//            return 3.0F;
//        }
//
//        return this.blockResistance / 5.0F;
//    }
//
//    @Override
//    public float getBlockHardness(BlockState blockState, World worldIn, BlockPos pos)
//    {
//        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) worldIn.getBlockState(pos).getValue(BASIC_TYPE_MOON));
//
//        if (type == EnumBlockBasicMoon.MOON_DIRT || type == EnumBlockBasicMoon.MOON_TURF)
//        {
//            return 0.5F;
//        }
//
//        if (type == EnumBlockBasicMoon.MOON_DUNGEON_BRICK)
//        {
//            return 4.0F;
//        }
//
//        if (type == EnumBlockBasicMoon.ORE_COPPER_MOON || type == EnumBlockBasicMoon.ORE_TIN_MOON || type == EnumBlockBasicMoon.ORE_SAPPHIRE)
//        {
//            return 5.0F;
//        }
//
//        if (type == EnumBlockBasicMoon.ORE_CHEESE_MOON)
//        {
//            return 3.0F;
//        }
//
//        return this.blockHardness;
//    }
//
//    @Override
//    public boolean canHarvestBlock(IBlockReader world, BlockPos pos, PlayerEntity player)
//    {
//        BlockState bs = world.getBlockState(pos);
//        if (bs.getBlock() != this) return false;
//        EnumBlockBasicMoon type = (EnumBlockBasicMoon) bs.getValue(BASIC_TYPE_MOON);
//        if (type == EnumBlockBasicMoon.MOON_DIRT || type == EnumBlockBasicMoon.MOON_TURF)
//        {
//            return true;
//        }
//
//        return super.canHarvestBlock(world, pos, player);
//    }
//
//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) state.get(BASIC_TYPE_MOON));
//        switch (type)
//        {
//        case ORE_CHEESE_MOON:
//            return GCItems.cheeseCurd;
//        case ORE_SAPPHIRE:
//            return GCItems.itemBasicMoon;
//        default:
//            return Item.getItemFromBlock(this);
//        }
//    }
//
//    @Override
//    public int damageDropped(BlockState state)
//    {
//        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) state.get(BASIC_TYPE_MOON));
//        if (type == EnumBlockBasicMoon.ORE_CHEESE_MOON)
//        {
//            return 0;
//        }
//        else if (type == EnumBlockBasicMoon.ORE_SAPPHIRE)
//        {
//            return 2;
//        }
//        else
//        {
//            return getMetaFromState(state);
//        }
//    }
//
//    @Override
//    public int quantityDropped(BlockState state, int fortune, Random random)
//    {
//        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) state.get(BASIC_TYPE_MOON));
//        switch (type)
//        {
//        case ORE_CHEESE_MOON:
//            if (fortune >= 1)
//            {
//                return (random.nextFloat() < fortune * 0.29F - 0.25F) ? 2 : 1;
//            }
//            return 1;
//        default:
//            return 1;
//        }
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        for (EnumBlockBasicMoon type : EnumBlockBasicMoon.values())
//        {
//            list.add(new ItemStack(this, 1, type.getMeta()));
//        }
//    }
//
//    @Override
//    public boolean isValueable(BlockState state)
//    {
//        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) state.get(BASIC_TYPE_MOON));
//        switch (type)
//        {
//        case ORE_CHEESE_MOON:
//        case ORE_COPPER_MOON:
//        case ORE_SAPPHIRE:
//        case ORE_TIN_MOON:
//            return true;
//        default:
//            return false;
//        }
//    }
//
//    @Override
//    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction direction, IPlantable plantable)
//    {
//        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) world.getBlockState(pos).getValue(BASIC_TYPE_MOON));
//
//        if (type != EnumBlockBasicMoon.MOON_TURF)
//        {
//            return false;
//        }
//
//        plantable.getPlant(world, pos.offset(Direction.UP));
//
//        return plantable instanceof FlowerBlock;
//    }
//
//    @Override
//    public int requiredLiquidBlocksNearby()
//    {
//        return 4;
//    }
//
//    @Override
//    public boolean isPlantable(BlockState state)
//    {
//        return state.get(BASIC_TYPE_MOON) == EnumBlockBasicMoon.MOON_TURF;
//
//    }
//
//    @Override
//    public boolean isTerraformable(World world, BlockPos pos)
//    {
//        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) world.getBlockState(pos).getValue(BASIC_TYPE_MOON));
//
//        if (type == EnumBlockBasicMoon.MOON_TURF)
//        {
//            BlockPos above = pos.offset(Direction.UP);
//            BlockState stateAbove = world.getBlockState(above);
//            return stateAbove.getBlock().isAir(stateAbove, world, above);
//        }
//
//        return false;
//    }
//
//    @Override
//    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
//    {
//        return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(state));
//    }
//
//    @Override
//    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving)
//    {
//        super.onReplaced(state, worldIn, pos, newState, isMoving);
//        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) state.get(BASIC_TYPE_MOON));
//
//        if (!worldIn.isRemote && type == EnumBlockBasicMoon.MOON_TURF)
//        {
//            Map<Long, List<Footprint>> footprintChunkMap = TickHandlerServer.serverFootprintMap.get(GCCoreUtil.getDimensionID(worldIn));
//
//            if (footprintChunkMap != null)
//            {
//                long chunkKey = ChunkPos.asLong(pos.getX() >> 4, pos.getZ() >> 4);
//                List<Footprint> footprintList = footprintChunkMap.get(chunkKey);
//
//                if (footprintList != null && !footprintList.isEmpty())
//                {
//                    List<Footprint> toRemove = new ArrayList<Footprint>();
//
//                    for (Footprint footprint : footprintList)
//                    {
//                        if (footprint.position.x > pos.getX() && footprint.position.x < pos.getX() + 1 &&
//                                footprint.position.z > pos.getZ() && footprint.position.z < pos.getZ() + 1)
//                        {
//                            toRemove.add(footprint);
//                        }
//                    }
//
//                    if (!toRemove.isEmpty())
//                    {
//                        footprintList.removeAll(toRemove);
//                    }
//                }
//            }
//
//            TickHandlerServer.footprintBlockChanges.add(new BlockVec3Dim(pos, GCCoreUtil.getDimensionID(worldIn)));
//        }
//    }
//
//    @Override
//    public boolean isReplaceableOreGen(BlockState state, IBlockReader world, BlockPos pos, Predicate<BlockState> target)
//    {
//        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) world.getBlockState(pos).getValue(BASIC_TYPE_MOON));
//        return type == EnumBlockBasicMoon.MOON_STONE || type == EnumBlockBasicMoon.MOON_DIRT;
//    }
//
//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(BASIC_TYPE_MOON, EnumBlockBasicMoon.byMetadata(meta));
//    }
//
//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(BASIC_TYPE_MOON);
//    }
//
//    @Override
//    public EnumSortCategory getCategory()
//    {
//        EnumBlockBasicMoon type = ((EnumBlockBasicMoon) getStateFromMeta(meta).getValue(BASIC_TYPE_MOON));
//        switch (type)
//        {
//        case ORE_CHEESE_MOON:
//        case ORE_COPPER_MOON:
//        case ORE_SAPPHIRE:
//        case ORE_TIN_MOON:
//            return EnumSortCategory.ORE;
//        case MOON_DUNGEON_BRICK:
//            return EnumSortCategory.BRICKS;
//        }
//        return EnumSortCategory.GENERAL;
//    }
//
//    @Override
//    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch)
//    {
//        if (state.getBlock() != this) return 0;
//
//        EnumBlockBasicMoon type = state.get(BASIC_TYPE_MOON);
//        if (type == EnumBlockBasicMoon.ORE_CHEESE_MOON || type == EnumBlockBasicMoon.ORE_SAPPHIRE)
//        {
//            Random rand = world instanceof World ? ((World)world).rand : new Random();
//            return MathHelper.nextInt(rand, 2, 5) + (type == EnumBlockBasicMoon.ORE_SAPPHIRE ? 1 : 0);
//        }
//        return 0;
//    }
//}
