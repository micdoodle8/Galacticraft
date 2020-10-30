//package micdoodle8.mods.galacticraft.core.blocks;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.EnumSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Random;
//import java.util.Set;
//
//import javax.annotation.Nullable;
//
//import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
//import micdoodle8.mods.galacticraft.core.GCBlocks;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
//import micdoodle8.mods.galacticraft.core.util.JavaUtil;
//import micdoodle8.mods.galacticraft.core.util.PropertyObject;
//import net.minecraft.block.*;
//import net.minecraft.block.BlockDynamicLiquid;
//import net.minecraft.block.BlockLiquid;
//import net.minecraft.block.DoorBlock;
//import net.minecraft.block.material.PushReaction;
//import net.minecraft.block.material.Material;
//import net.minecraft.block.properties.IProperty;
//import net.minecraft.block.state.BlockFaceShape;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.block.BlockState;
//import net.minecraft.client.renderer.block.model.ModelResourceLocation;
//import net.minecraft.client.renderer.block.statemap.StateMapperBase;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.block.Blocks;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.BlockRenderLayer;
//import net.minecraft.util.Direction;
//import net.minecraft.util.Hand;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.RayTraceResult;
//import net.minecraft.util.text.translation.I18n;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//import net.minecraft.world.ServerWorld;
//import net.minecraftforge.client.model.ModelLoader;
//import net.minecraftforge.common.property.IExtendedBlockState;
//import net.minecraftforge.common.property.IUnlistedProperty;
//import net.minecraftforge.fluids.BlockFluidBase;
//import net.minecraftforge.fluids.BlockFluidClassic;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//import net.minecraftforge.registries.IForgeRegistry;
//
//public class BlockGrating extends Block implements ISortable, IPartialSealableBlock
//{
//    public static final PropertyObject<BlockState> BASE_STATE = new PropertyObject<>("held_state", BlockState.class);
//    protected static final VoxelShape BOUNDING_BOX = VoxelShapes.create(0.0D, 0.875D, 0.0D, 1.0D, 1.0D, 1.0D);
//    public static IBlockReader savedBlockAccess;
//    public static BlockPos savedPos;
//
//    private int adjacentSourceBlocks;
//    private final BlockState liquidEquivalentDynamic;
//    private final BlockState liquidEquivalentStatic;
//    private boolean forgeFluid = false;
//    private BlockFluidBase forgeBlock = null;
//    public static List<BlockGrating> forgeBlocks = new ArrayList<>();
//    public static int number = 3;  //That's Plain, Water, Lava - any more will be Forge Fluids
//    private static Method gofd;
//    private static Field fieldQuantaPerBlock;
//    private ThreadLocal<Boolean> replaceWithFluidIntended = new ThreadLocal<>().withInitial(() -> false);
//
//    static
//    {
//        try {
//            gofd = BlockFluidClassic.class.getDeclaredMethod("getOptimalFlowDirections", World.class, BlockPos.class);
//            gofd.setAccessible(true);
//        } catch (Exception e) { e.printStackTrace(); }
//        try {
//            fieldQuantaPerBlock = BlockFluidBase.class.getDeclaredField("quantaPerBlock");
//            fieldQuantaPerBlock.setAccessible(true);
//        } catch (Exception e) { e.printStackTrace(); }
//    }
//    
//    public BlockGrating(String assetName, Material material)
//    {
//        super(material);
//        if (this.blockMaterial == Material.LAVA)
//        {
//            this.liquidEquivalentStatic = Blocks.LAVA.getDefaultState();
//            this.liquidEquivalentDynamic = Blocks.FLOWING_LAVA.getDefaultState();
//            this.setTickRandomly(true);
//        }
//        else if (this.blockMaterial == Material.WATER)
//        {
//            this.liquidEquivalentStatic = Blocks.WATER.getDefaultState();
//            this.liquidEquivalentDynamic = Blocks.FLOWING_WATER.getDefaultState();
//            this.setTickRandomly(true);
//        }
//        else
//        {
//            this.liquidEquivalentStatic = null;
//            this.liquidEquivalentDynamic = null;
//        }
//        this.setHardness(0.5F);
//        this.blockResistance = 15F;
//        this.setSoundType(SoundType.METAL);
//        this.setUnlocalizedName(assetName);
//    }
//
//    public BlockGrating(Block forge)
//    {
//        super(forge.getMaterial(forge.getDefaultState()));
//        this.forgeFluid = true;
//        this.forgeBlock = (BlockFluidBase) forge;
//        this.liquidEquivalentStatic = null;
//        this.liquidEquivalentDynamic = null;
//        this.setHardness(0.5F);
//        this.blockResistance = 15F;
//        this.setSoundType(SoundType.METAL);
//        this.setUnlocalizedName("grating" + number);
//        number++;
//    }
//
//    public static void createForgeFluidVersion(Block b, IForgeRegistry<Block> blockRegistry)
//    {
//        if (b instanceof BlockFluidBase)
//        {
//            BlockGrating grating = new BlockGrating(b);
//            grating.setLightLevel(b.getLightValue(b.getDefaultState()) / 15F);
//            BlockGrating.forgeBlocks.add(grating);
//            GCBlocks.registerBlock(grating, null);
//            blockRegistry.register(grating);
//        }
//    }
//
//    public static void createForgeFluidVersions(IForgeRegistry<Block> blockRegistry)
//    {
//        Iterator<Block> it = Block.REGISTRY.iterator();
//        while(it.hasNext())
//        {
//            Block test = it.next();
//            if (test instanceof BlockFluidBase)
//            {
//                createForgeFluidVersion(test, blockRegistry);
//            }
//        }
//    }
//
//    public static void remapForgeVariants()
//    {
//        for (Block b : forgeBlocks)
//        {
//            BlockGrating.remapVariant(b);
//        }
//    }
//
//    @Override
//    public String getLocalizedName()
//    {
//        return LanguageMap.getInstance().translateKey("tile.grating");
//    }
//
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        return GalacticraftCore.galacticraftBlocksTab;
//    }
//
//    @Override
//    public EnumSortCategory getCategory()
//    {
//        return EnumSortCategory.DECORATION;
//    }
//
//    @Override
//    public boolean isOpaqueCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isFullCube(BlockState state)
//    {
//        return false;
//    }
//
//    @Override
//    public boolean isSideSolid(BlockState base_state, IBlockReader world, BlockPos pos, Direction side)
//    {
//        return false;
//    }
//
//    @Override
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }
//
//    @Override
//    public boolean isSealed(World world, BlockPos pos, Direction direction)
//    {
//        return false;
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public boolean shouldSideBeRendered(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
//    {
//        return true;
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public BlockRenderLayer getRenderLayer()
//    {
//         return BlockRenderLayer.CUTOUT;
//    }
//
//    @Override
//    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer)
//    {
//        return true;
//    }
//    
//    @OnlyIn(Dist.CLIENT)
//    public static void remapVariant(Block grating)
//    {
//        ModelLoader.setCustomStateMapper(grating, new StateMapperBase()
//        {
//            @Override
//            protected ModelResourceLocation getModelResourceLocation(BlockState ignore)
//            {
//                return new ModelResourceLocation("galacticraftcore:grating", "normal");
//            }
//        });
//    }
//
//    @Override
//    public AxisAlignedBB getBoundingBox(BlockState state, IBlockReader world, BlockPos pos)
//    {
//        return BOUNDING_BOX;
//    }
//    
//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        if (JavaUtil.instance.isCalledBy(BlockDynamicLiquid.class, BlockFluidBase.class))
//        {
//            return Item.getItemFromBlock(Blocks.AIR);
//        }
//        Thread.dumpStack();
//        return Item.getItemFromBlock(GCBlocks.grating);
//    }
//
//    @Override
//    @Nullable
//    public ItemStack getItem(World worldIn, BlockPos pos, BlockState state)
//    {
//        return new ItemStack(Item.getItemFromBlock(GCBlocks.grating), 1, 0);
//    }
//    
//    @Override
//    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
//    {
//        return new ItemStack(Item.getItemFromBlock(GCBlocks.grating), 1, 0);
//    }
//
//    @Override
//    protected BlockStateContainer createBlockState()
//    {
//        return new BlockGratingExtendedState(this, new IProperty[] { BlockLiquid.LEVEL, BlockFluidBase.LEVEL }, new IUnlistedProperty[] { BASE_STATE });
//    }
//
//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(BlockLiquid.LEVEL, Integer.valueOf(meta)).with(BlockFluidBase.LEVEL, Integer.valueOf(meta));
//    }
//
//    @Override
//    public int getMetaFromState(BlockState state)
//    {
//        return ((Integer)state.get(BlockLiquid.LEVEL)).intValue();
//    }
//
//    @Override
//    public BlockState getExtendedState(BlockState state, IBlockReader world, BlockPos pos)
//    {
//        savedBlockAccess = world;
//        savedPos = pos;
//        return ((IExtendedBlockState) state).with(BASE_STATE, this.getLiquidBlock(state));
//    }
//
//    public BlockState getLiquidBlock(BlockState state)
//    {
//        if (this.forgeFluid && state.getPropertyKeys().contains(BlockFluidBase.LEVEL))
//        {
//            int level = state.get(BlockFluidBase.LEVEL).intValue();
//            return this.forgeBlock.getDefaultState().with(BlockFluidBase.LEVEL, level);
//        }
//        else if (this.blockMaterial == Material.WATER || this.blockMaterial == Material.LAVA)
//        {
//            int level = state.get(BlockLiquid.LEVEL).intValue();
//            return this.liquidEquivalentStatic.with(BlockLiquid.LEVEL, level);
//        }
//        
//        return null;
//    }
//    
//    @Override
//    public void breakBlock(World world, BlockPos pos, BlockState state)
//    {
//        BlockState newBlock = world.getBlockState(pos);
//        if (newBlock.getBlock() instanceof BlockGrating)
//        {
//            return;
//        }
//        if (Blocks.AIR == newBlock.getBlock() && this != GCBlocks.grating)
//        {
//            world.setBlockState(pos, this.getLiquidBlock(state));
//            this.dropBlockAsItem(world, pos, state, 0);
//        }
//        if (this.replaceWithFluidIntended.get())
//        {
//            // Intended replacement with fluid block, called from this.removedByPlayer()
//            this.replaceWithFluidIntended.set(false);
//            return;
//        }
//        if (newBlock.getBlock() instanceof BlockFluidBase)
//        {
//            for (BlockGrating b : forgeBlocks)
//            {
//                if (newBlock.getBlock() == b.forgeBlock)
//                {
//                    int level = newBlock.getValue(BlockFluidBase.LEVEL).intValue();
//                    BlockState bs = b.getDefaultState().with(BlockFluidBase.LEVEL, level).with(BlockLiquid.LEVEL, level);
//                    world.setBlockState(pos, bs);
//                    return;
//                }
//            }
//            //If not a known Forge fluid block, drop grating item
//            Block.spawnAsEntity(world, pos, new ItemStack(GCBlocks.grating));
//            return;
//        }
//        if (newBlock.getBlock() instanceof FlowingFluidBlock)
//        {
//            if (newBlock.getMaterial() == Material.WATER)
//            {
//                int level = newBlock.getValue(BlockLiquid.LEVEL).intValue();
//                BlockState bs = GCBlocks.gratingWater.getDefaultState().with(BlockLiquid.LEVEL, level);
//                world.setBlockState(pos, bs);
//            }
//            else if (newBlock.getMaterial() == Material.LAVA)
//            {
//                int level = newBlock.getValue(BlockLiquid.LEVEL).intValue();
//                BlockState bs = GCBlocks.gratingLava.getDefaultState().with(BlockLiquid.LEVEL, level);
//                world.setBlockState(pos, bs);
//            }
//        }
//    }
//
//    @Override
//    public boolean removedByPlayer(BlockState state, World world, BlockPos pos, PlayerEntity player, boolean willHarvest)
//    {
//        this.onBlockHarvested(world, pos, state, player);
//        if (this == GCBlocks.grating)
//        {
//            return world.setBlockState(pos, Blocks.AIR.getDefaultState(), world.isRemote ? 11 : 3);
//        }
//        this.replaceWithFluidIntended.set(true);
//        return world.setBlockState(pos, this.getLiquidBlock(state), world.isRemote ? 11 : 3);
//    }
//
//    @Override
//    public BlockState getStateForPlacement(BlockItemUseContext context)
//    {
//        BlockState oldBlock = world.getBlockState(pos);
//        if (oldBlock.getBlock() instanceof BlockGrating)
//        {
//            return this.getStateFromMeta(meta);
//        }
//        if (oldBlock.getBlock() instanceof BlockFluidBase)
//        {
//            for (BlockGrating b : forgeBlocks)
//            {
//                if (oldBlock.getBlock() == b.forgeBlock)
//                {
//                    int level = oldBlock.getValue(BlockFluidBase.LEVEL).intValue();
//                    return b.getDefaultState().with(BlockFluidBase.LEVEL, level).with(BlockLiquid.LEVEL, level);
//                }
//            }
//        }
//        else if (oldBlock.getBlock() instanceof FlowingFluidBlock)
//        {
//            if (oldBlock.getMaterial() == Material.WATER)
//            {
//                int level = oldBlock.getValue(BlockLiquid.LEVEL).intValue();
//                return GCBlocks.gratingWater.getDefaultState().with(BlockLiquid.LEVEL, level);
//            }
//            else if (oldBlock.getMaterial() == Material.LAVA)
//            {
//                int level = oldBlock.getValue(BlockLiquid.LEVEL).intValue();
//                return GCBlocks.gratingLava.getDefaultState().with(BlockLiquid.LEVEL, level);
//            }
//        }
//        return this.getStateFromMeta(meta);
//    }
//    
//    @Override
//    public Material getMaterial(BlockState state)
//    {
//        //Disable conversion to plain stone (lava above water in BlockDynamicLiquid.updateTick) - also allows water centre source block to form above this
//        //also allow endermen and others to teleport onto this (EntityLivingBase) and players to spawn on this (World)
//        int found = JavaUtil.instance.isCalledBySecond(BlockDynamicLiquid.class, LivingEntity.class, World.class);
//        if (found > 0)
//        {
//            if (found > 1 || JavaUtil.instance.isCalledByThird(ServerWorld.class))
//            {
//                return Material.IRON;
//            }
//        }
//
//        return this.blockMaterial;
//    }
//    
//    @Override
//    public boolean isReplaceable(IBlockReader worldIn, BlockPos pos)
//    {
//        return false;
//    }
//    
//    // Pistons cannot move this
//    @Override
//    public PushReaction getMobilityFlag(BlockState state)
//    {
//        return PushReaction.BLOCK;
//    }
//    
//    @Override
//    public boolean isPassable(IBlockReader worldIn, BlockPos pos)
//    {
//        return false;
//    }
//    
//    @Override
//    public boolean canCollideCheck(BlockState state, boolean hitIfLiquid)
//    {
//        return true;
//    }
//
//    //---------------From BlockDynamicLiquid
//    
//    @Override
//    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random)
//    {
//        if (!this.blockMaterial.isLiquid()) return;
//        if (this.forgeFluid)
//        {
//            if (this.forgeBlock instanceof BlockFluidClassic) 
//            {
//                this.updateTickForge(worldIn, pos, state, rand);
//            }
//            return;
//        }
//
//        int i = ((Integer)state.get(BlockLiquid.LEVEL)).intValue();
//        int j = 1;
//
//        if (this.blockMaterial == Material.LAVA && !worldIn.dimension.doesWaterVaporize())
//        {
//            j = 2;
//        }
//
//        int k = this.tickRate(worldIn);
//
//        if (i > 0)
//        {
//            int l = -100;
//            this.adjacentSourceBlocks = 0;
//
//            for (Direction enumfacing : Direction.Plane.HORIZONTAL)
//            {
//                l = this.checkAdjacentBlock(worldIn, pos.offset(enumfacing), l);
//            }
//
//            int i1 = l + j;
//
//            if (i1 >= 8 || l < 0)
//            {
//                i1 = -1;
//            }
//
//            int j1 = this.getDepth(worldIn.getBlockState(pos.up()));
//
//            if (j1 >= 0)
//            {
//                if (j1 >= 8)
//                {
//                    i1 = j1;
//                }
//                else
//                {
//                    i1 = j1 + 8;
//                }
//            }
//
//            if (this.adjacentSourceBlocks >= 2 && net.minecraftforge.event.ForgeEventFactory.canCreateFluidSource(worldIn, pos, state, this.blockMaterial == Material.WATER))
//            {
//                BlockState iblockstate = worldIn.getBlockState(pos.down());
//
//                if (iblockstate.getMaterial().isSolid())
//                {
//                    i1 = 0;
//                }
//                else if (iblockstate.getMaterial() == this.blockMaterial && ((Integer)iblockstate.get(BlockLiquid.LEVEL)).intValue() == 0)
//                {
//                    i1 = 0;
//                }
//            }
//
//            if (this.blockMaterial == Material.LAVA && i < 8 && i1 < 8 && i1 > i && rand.nextInt(4) != 0)
//            {
//                k *= 4;
//            }
//
//            if (i1 == i)
//            {
////                this.placeStaticBlock(worldIn, pos, state);
//            }
//            else
//            {
//                i = i1;
//
//                if (i1 < 0)
//                {
//                    worldIn.setBlockState(pos, GCBlocks.grating.getDefaultState(), 3);
//                }
//                else
//                {
//                    state = state.with(BlockLiquid.LEVEL, Integer.valueOf(i1));
//                    worldIn.setBlockState(pos, state, 2);
//                    worldIn.getPendingBlockTicks().scheduleTick(pos, this, k);
//                    worldIn.notifyNeighborsOfStateChange(pos, this, false);
//                }
//            }
//        }
//        else
//        {
////            this.placeStaticBlock(worldIn, pos, state);
//        }
//
//        BlockPos down = pos.down();
//        BlockState iblockstate1 = worldIn.getBlockState(down);
//
//        if (this.canFlowInto(worldIn, down, iblockstate1))
//        {
//            if (this.blockMaterial == Material.LAVA && iblockstate1.getMaterial() == Material.WATER && !(iblockstate1.getBlock() instanceof BlockGrating))
//            {
//                worldIn.setBlockState(down, Blocks.STONE.getDefaultState());
//                return;
//            }
//
//            if (i >= 8)
//            {
//                this.tryFlowInto(worldIn, down, iblockstate1, i);
//            }
//            else
//            {
//                this.tryFlowInto(worldIn, down, iblockstate1, i + 8);
//            }
//        }
//        else if (i >= 0 && (i == 0 || this.isBlocked(worldIn, down, iblockstate1)))
//        {
//            Set<Direction> set = this.getPossibleFlowDirections(worldIn, pos);
//            int k1 = i + j;
//
//            if (i >= 8)
//            {
//                k1 = 1;
//            }
//
//            if (k1 >= 8)
//            {
//                return;
//            }
//
//            for (Direction enumfacing1 : set)
//            {
//                this.tryFlowInto(worldIn, pos.offset(enumfacing1), worldIn.getBlockState(pos.offset(enumfacing1)), k1);
//            }
//        }
//    }
//
//    private void tryFlowInto(World worldIn, BlockPos pos, BlockState state, int level)
//    {
//        if (this.canFlowInto(worldIn, pos, state))
//        {
//            if (state.getMaterial() != Material.AIR && !(state.getBlock() instanceof BlockGrating))
//            {
//                if (this.blockMaterial == Material.LAVA)
//                {
//                }
//                else
//                {
//                    if (state.getBlock() != Blocks.SNOW_LAYER)
//                    state.getBlock().dropBlockAsItem(worldIn, pos, state, 0);
//                }
//            }
//
//            worldIn.setBlockState(pos, this.liquidEquivalentDynamic.with(BlockLiquid.LEVEL, Integer.valueOf(level)), 3);
//        }
//    }
//
//    private int getSlopeDistance(World worldIn, BlockPos pos, int distance, Direction calculateFlowCost)
//    {
//        int i = 1000;
//
//        for (Direction enumfacing : Direction.Plane.HORIZONTAL)
//        {
//            if (enumfacing != calculateFlowCost)
//            {
//                BlockPos blockpos = pos.offset(enumfacing);
//                BlockState iblockstate = worldIn.getBlockState(blockpos);
//
//                if (!this.isBlocked(worldIn, blockpos, iblockstate) && (iblockstate.getMaterial() != this.blockMaterial || ((Integer)iblockstate.get(BlockLiquid.LEVEL)).intValue() > 0))
//                {
//                    if (!this.isBlocked(worldIn, blockpos.down(), iblockstate))
//                    {
//                        return distance;
//                    }
//
//                    if (distance < this.getSlopeFindDistance(worldIn))
//                    {
//                        int j = this.getSlopeDistance(worldIn, blockpos, distance + 1, enumfacing.getOpposite());
//
//                        if (j < i)
//                        {
//                            i = j;
//                        }
//                    }
//                }
//            }
//        }
//
//        return i;
//    }
//
//    private int getSlopeFindDistance(World worldIn)
//    {
//        return this.blockMaterial == Material.LAVA && !worldIn.dimension.doesWaterVaporize() ? 2 : 4;
//    }
//
//    /**
//     * This method returns a Set of EnumFacing
//     */
//    private Set<Direction> getPossibleFlowDirections(World worldIn, BlockPos pos)
//    {
//        int i = 1000;
//        Set<Direction> set = EnumSet.<Direction>noneOf(Direction.class);
//
//        for (Direction enumfacing : Direction.Plane.HORIZONTAL)
//        {
//            BlockPos blockpos = pos.offset(enumfacing);
//            BlockState iblockstate = worldIn.getBlockState(blockpos);
//
//            if (!this.isBlocked(worldIn, blockpos, iblockstate) && (iblockstate.getMaterial() != this.blockMaterial || ((Integer)iblockstate.get(BlockLiquid.LEVEL)).intValue() > 0))
//            {
//                int j;
//
//                if (this.isBlocked(worldIn, blockpos.down(), worldIn.getBlockState(blockpos.down())))
//                {
//                    j = this.getSlopeDistance(worldIn, blockpos, 1, enumfacing.getOpposite());
//                }
//                else
//                {
//                    j = 0;
//                }
//
//                if (j < i)
//                {
//                    set.clear();
//                }
//
//                if (j <= i)
//                {
//                    set.add(enumfacing);
//                    i = j;
//                }
//            }
//        }
//
//        return set;
//    }
//
//    private boolean isBlocked(World worldIn, BlockPos pos, BlockState state)
//    {
//        BlockState bs = worldIn.getBlockState(pos);
//        Block block = bs.getBlock();
//        return !(block instanceof DoorBlock) && block != Blocks.STANDING_SIGN && block != Blocks.LADDER && block != Blocks.REEDS ? (block.getMaterial(bs) != Material.PORTAL && block.getMaterial(bs) != Material.STRUCTURE_VOID ? block.getMaterial(bs).blocksMovement() : true) : true;
//    }
//
//    protected int checkAdjacentBlock(World worldIn, BlockPos pos, int currentMinLevel)
//    {
//        int i = this.getDepth(worldIn.getBlockState(pos));
//
//        if (i < 0)
//        {
//            return currentMinLevel;
//        }
//        else
//        {
//            if (i == 0)
//            {
//                ++this.adjacentSourceBlocks;
//            }
//
//            if (i >= 8)
//            {
//                i = 0;
//            }
//
//            return currentMinLevel >= 0 && i >= currentMinLevel ? currentMinLevel : i;
//        }
//    }
//
//    private boolean canFlowInto(World worldIn, BlockPos pos, BlockState state)
//    {
//        Material material = state.getMaterial();
//        return material != this.blockMaterial && material != Material.LAVA && !this.isBlocked(worldIn, pos, state);
//    }
//
//    //---------------From BlockLiquid etc
//    
//    @Override
//    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
//    {
//        if (this.blockMaterial.isLiquid())
//        {
//            worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
//        }
//    }
//    
//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
//    {
//        if (this.blockMaterial.isLiquid())
//        {
//            worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
//        }
//    }
//    
//    @Override
//    public int tickRate(World worldIn)
//    {
//        if (this.forgeFluid)
//        {
//            return this.forgeBlock.tickRate(worldIn);
//        }
//        return this.blockMaterial == Material.WATER ? 5 : (this.blockMaterial == Material.LAVA ? (worldIn.dimension.isNether() ? 10 : 30) : 0);
//    }
//    
//    protected int getDepth(BlockState p_189542_1_)
//    {
//        return p_189542_1_.getMaterial() == this.blockMaterial ? ((Integer)p_189542_1_.getValue(BlockLiquid.LEVEL)).intValue() : -1;
//    }
//    
//    //------------------From BlockFluidBase
//    
//    public void updateTickForge(World world, BlockPos pos, BlockState state, Random rand)
//    {
////        if (!isSourceBlock(world, pos) && ForgeEventFactory.canCreateFluidSource(world, pos, state, false))
////        {
////            int adjacentSourceBlocks =
////                    (isSourceBlock(world, pos.north()) ? 1 : 0) +
////                    (isSourceBlock(world, pos.south()) ? 1 : 0) +
////                    (isSourceBlock(world, pos.east()) ? 1 : 0) +
////                    (isSourceBlock(world, pos.west()) ? 1 : 0);
////            if (adjacentSourceBlocks >= 2 && (world.getBlockState(pos.down(1)).getMaterial().isSolid() || isSourceBlock(world, pos.down(1))))
////                world.setBlockState(pos, state.with(BlockFluidBase.LEVEL, 0));
////        }
//
//        int quantaPerBlock = 8;
//        try {
//            quantaPerBlock = fieldQuantaPerBlock.getInt(this.forgeBlock);
//        } catch (Exception e) { }
//        int tickRate = this.forgeBlock.tickRate(world);
//        int quantaRemaining = quantaPerBlock - state.get(BlockFluidBase.LEVEL);
//        int expQuanta = -101;
//
//        // check adjacent block levels if non-source
//        if (quantaRemaining < quantaPerBlock)
//        {
//            if (testSame(world, pos.add( 0, 1,  0)) ||
//                testSame(world, pos.add(-1, 1,  0)) ||
//                testSame(world, pos.add( 1, 1,  0)) ||
//                testSame(world, pos.add( 0, 1, -1)) ||
//                testSame(world, pos.add( 0, 1,  1)))
//            {
//                expQuanta = quantaPerBlock - 1;
//            }
//            else
//            {
//                int maxQuanta = -100;
//                maxQuanta = getLargerQuanta(world, pos.add(-1, 0,  0), maxQuanta);
//                maxQuanta = getLargerQuanta(world, pos.add( 1, 0,  0), maxQuanta);
//                maxQuanta = getLargerQuanta(world, pos.add( 0, 0, -1), maxQuanta);
//                maxQuanta = getLargerQuanta(world, pos.add( 0, 0,  1), maxQuanta);
//
//                expQuanta = maxQuanta - 1;
//            }
//
//            // decay calculation
//            if (expQuanta != quantaRemaining)
//            {
//                quantaRemaining = expQuanta;
//
//                if (expQuanta <= 0)
//                {
//                    world.setBlockState(pos, GCBlocks.grating.getDefaultState(), 3);
//                }
//                else
//                {
//                    world.setBlockState(pos, state.with(BlockFluidBase.LEVEL, quantaPerBlock - expQuanta), 2);
//                    worldIn.getPendingBlockTicks().scheduleTick(pos, this, tickRate);
//                    world.notifyNeighborsOfStateChange(pos, this.forgeBlock, false);
//                }
//            }
//        }
//        // This is a "source" block, set meta to zero, and send a server only update
//        else if (quantaRemaining >= quantaPerBlock)
//        {
//            world.setBlockState(pos, this.getDefaultState(), 2);
//        }
//
//        // Flow vertically if possible
//        if (this.forgeBlock.canDisplace(world, pos.down(1)))
//        {
//            flowIntoBlock(world, pos.down(1), 1);
//            return;
//        }
//
//        // Flow outward if possible
//        int flowMeta = quantaPerBlock - quantaRemaining + 1;
//        if (flowMeta >= quantaPerBlock)
//        {
//            return;
//        }
//
//        if (isSourceBlock(world, pos) || !((BlockFluidClassic)this.forgeBlock).isFlowingVertically(world, pos))
//        {
//            Block test = world.getBlockState(pos.up(1)).getBlock(); 
//            if (test == this || test == this.forgeBlock)
//            {
//                flowMeta = 1;
//            }
//            
//            boolean flowTo[];
//            try
//            {
//                flowTo = (boolean []) gofd.invoke(this.forgeBlock, world, pos);
//
//                if (flowTo[0]) flowIntoBlock(world, pos.add(-1, 0,  0), flowMeta);
//                if (flowTo[1]) flowIntoBlock(world, pos.add( 1, 0,  0), flowMeta);
//                if (flowTo[2]) flowIntoBlock(world, pos.add( 0, 0, -1), flowMeta);
//                if (flowTo[3]) flowIntoBlock(world, pos.add( 0, 0,  1), flowMeta);
//            } catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//        }
//    }
//    
//    private boolean testSame(World world, BlockPos pos)
//    {
//        Block test = world.getBlockState(pos).getBlock();
//        return test == this || test == this.forgeBlock;
//    }
//
//    private boolean isSourceBlock(IBlockReader world, BlockPos pos)
//    {
//        BlockState state = world.getBlockState(pos);
//        return state.getBlock() instanceof BlockFluidBase && state.get(BlockFluidBase.LEVEL) == 0;
//    }
//    
//    protected int getLargerQuanta(IBlockReader world, BlockPos pos, int compare)
//    {
//        int quantaRemaining = this.forgeBlock.getQuantaValue(world, pos);
//        if (quantaRemaining <= 0)
//        {
//            return compare;
//        }
//        return quantaRemaining >= compare ? quantaRemaining : compare;
//    }
//
//    protected void flowIntoBlock(World world, BlockPos pos, int meta)
//    {
//        if (meta < 0) return;
//        if (this.forgeBlock.displaceIfPossible(world, pos))
//        {
//            world.setBlockState(pos, this.forgeBlock.getBlockState().getBaseState().with(BlockFluidBase.LEVEL, meta), 3);
//        }
//    }
//}