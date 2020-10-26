//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
//import micdoodle8.mods.galacticraft.api.item.IPaintable;
//import micdoodle8.mods.galacticraft.core.GCBlocks;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategory;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import micdoodle8.mods.galacticraft.core.util.JavaUtil;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.Blocks;
//import net.minecraft.block.material.Material;
//import net.minecraft.block.properties.IProperty;
//import net.minecraft.block.properties.PropertyEnum;
//import net.minecraft.block.state.BlockFaceShape;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.client.Minecraft;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemGroup;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.BlockRenderLayer;
//import net.minecraft.util.Direction;
//import net.minecraft.util.IStringSerializable;
//import net.minecraft.util.NonNullList;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import javax.annotation.Nullable;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//public class BlockSpaceGlass extends Block implements IPartialSealableBlock, IShiftDescription, ISortable, IPaintable
//{
//    public static final PropertyEnum MODEL = EnumProperty.create("modeltype", GlassModel.class);
//    public static final EnumProperty<GlassRotation> ROTATION  = EnumProperty.create("rot", GlassRotation.class);
//    //public static final IntegerProperty PLACING  = IntegerProperty.create("placing", 0, 2);
//    //This will define whether originally placed by the player facing NS - EW - or UD
//
//    public final GlassType type;
//    private final GlassFrame frame; //frameValue corresponds to the damage of the placing item
//    public int color = 0xFFFFFF;
//    private final Block baseBlock;
//    private boolean isClient; 
//    private static Class clazz = Blocks.WATER.getClass().getSuperclass();
//    
//    public BlockSpaceGlass(Properties builder, GlassType newType, GlassFrame newFrame, Block base)
//    {
//        super(builder);
//        this.isClient = GalacticraftCore.proxy.getClass().getName().equals("micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore");
//        this.type = newType;
//        this.frame = newFrame;
//        this.baseBlock = base == null ? this : base;
//        this.color = frame.getDefaultColor();
//        this.setDefaultState(stateContainer.getBaseState().with(MODEL, GlassModel.STANDARD_PANE).with(ROTATION, GlassRotation.N));
//    }
//
//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(new IProperty[] {MODEL, ROTATION});
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    @Override
//    public void getSubBlocks(ItemGroup tab, NonNullList<ItemStack> list)
//    {
//        //The plain block variety produces items carrying all variants as damage values
//        //Other block varieties have no corresponding ItemBlock (see registration in GCBlocks)
//        if (this.frame == GlassFrame.PLAIN)
//        {
//            for (int i = 0; i < GlassFrame.values().length; i++)
//                list.add(new ItemStack(this, 1, i));
//        }
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
//    public BlockState getStateForPlacement(World worldIn, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer)
//    {
//        //This allows the different item damage values to place different instances of the block - the instances of the block are constructed in GCBlocks
//        if (meta >= GlassFrame.values().length) meta = 0;
//        switch (GlassFrame.values()[meta])
//        {
//        case TIN_DECO:
//        {
//            switch (this.type)
//            {
//            case STRONG:
//                return GCBlocks.spaceGlassTinStrong.getDefaultState();
//            case VANILLA:
//                return GCBlocks.spaceGlassTinVanilla.getDefaultState();
//            default:
//                return GCBlocks.spaceGlassTinClear.getDefaultState();
//            }
//        }
//        case PLAIN:
//        default:
//            return this.getDefaultState();
//        }
//        //TODO: Add PLACING direction
//    }
//
//    @Override
//    public int damageDropped(BlockState state)
//    {
//        return this.frame.ordinal();
//    }
//
//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        return Item.getItemFromBlock(this.baseBlock);
//        //TODO: override getItemDropped() to return null if we make a broken Space Glass variant...
//        // (if doing that, also update EntityAstroMiner.getPickBlock()
//    }
//
//    @Override
//    public List<ItemStack> getDrops(IBlockReader world, BlockPos pos, BlockState state, int fortune)
//    {
//        ArrayList<ItemStack> ret = new ArrayList<>(1);
//        ret.add(new ItemStack(this.getItemDropped(state, null, fortune), 1, this.frame.ordinal()));
//        return ret;
//    }
//    
//    @Override
//    public boolean canSilkHarvest(World world, BlockPos pos, BlockState state, PlayerEntity player)
//    {
//        return true;
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
//    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, BlockState state, BlockPos pos, Direction face)
//    {
//        return BlockFaceShape.UNDEFINED;
//    }
//
//    @Override
//    public boolean isSealed(World world, BlockPos pos, Direction direction)
//    {
//        return direction.ordinal() > 1;
//    }
//
//    @Override
//    public Material getMaterial(BlockState state)
//    {
//        if (this.isClient && JavaUtil.instance.isCalledBySpecific(clazz))
//        {
//            return Material.WATER;
//        }
//        return this.blockMaterial;
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
//         return BlockRenderLayer.TRANSLUCENT;
//    }
//
//    @Override
//    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer)
//    {
//        return layer == BlockRenderLayer.TRANSLUCENT || layer == BlockRenderLayer.SOLID;
//    }
//    
//    @Override
//    public void addCollisionBoxToList(BlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, @Nullable Entity entityIn, boolean p_185477_7_)
//    {
//        BlockState above = worldIn.getBlockState(pos.up());
//        BlockState below = worldIn.getBlockState(pos.down());
//        BlockState north = worldIn.getBlockState(pos.north());
//        BlockState south = worldIn.getBlockState(pos.south());
//        BlockState west = worldIn.getBlockState(pos.west());
//        BlockState east = worldIn.getBlockState(pos.east());
//
//        boolean connectN = this.canPaneConnectToBlock(north, state);
//        boolean connectS = this.canPaneConnectToBlock(south, state);
//        boolean connectW = this.canPaneConnectToBlock(west, state);
//        boolean connectE = this.canPaneConnectToBlock(east, state);
//
//        boolean plateD = this.buildSolidSide(below, state);
//        boolean plateU = this.buildSolidSide(above, state);
//        boolean plateN = this.buildSolidSide(north, state);
//        boolean plateS = this.buildSolidSide(south, state);
//        boolean plateW = this.buildSolidSide(west, state);
//        boolean plateE = this.buildSolidSide(east, state);
//
//        //No plate on the sides which glass is facing
//        if (connectN || connectS)
//        {
//            plateW = plateE = false;
//        }
//        if (connectW || connectE)
//        {
//            plateN = plateS = false;
//        }
//
//        //Singleton
//        if (!connectE && !connectW && !connectN && !connectS)
//        {
//            boolean prefEW = false;
//            if (below.getBlock() == this)
//            {
//                prefEW = (isConnectedEW(below, worldIn, pos.down()) || isPreferenceEW(below, worldIn, pos.down()));
//            }
//            else if (above.getBlock() == this)
//            {
//                prefEW = (isConnectedEW(above, worldIn, pos.up()) || isPreferenceEW(above, worldIn, pos.up()));
//            }
//
//            if (this.isPreferenceEW(state, worldIn, pos))
//                prefEW = true;
//
//            if (prefEW)
//            {
//                plateN = plateS = false;
//            }
//            else
//            {
//                plateW = plateE = false;
//            }
//        }
//
//        final double posGlass = 0.375D;
//        final double posBase = 0.225D;
//        double xMin = plateU || plateD || plateN || plateS ? posBase : posGlass;
//        double xMax = plateU || plateD || plateN || plateS ? 1D - posBase : 1D - posGlass;
//        double yMin = 0D; //plateE || plateW || plateN || plateS ? posBase : 0.375D;
//        double yMax = 1D; //plateE || plateW || plateN || plateS ? 0.775D : 0.625D;
//        double zMin = plateU || plateD || plateE || plateW ? posBase : posGlass;
//        double zMax = plateU || plateD || plateE || plateW ? 1D - posBase : 1D - posGlass;
//
//        if (plateW || connectW) xMin = 0D;
//        if (plateE || connectE) xMax = 1D;
//        if (plateN || connectN) zMin = 0D;
//        if (plateS || connectS) zMax = 1D;
//
//        //Special for corner diagonals
//        if ((connectW ^ connectE) && (connectN ^ connectS) && !(plateU && plateD))
//        {
//            double diag = 0.25D;
//            if (connectW)
//            {
//                xMin = diag - 0.01D;
//                xMax = diag;
//            }
//            else
//            {
//                xMin = 1D - diag;
//                xMax = 1.01D - diag;
//            }
//            if (connectN)
//            {
//                zMin = diag - 0.01D;
//                zMax = diag;
//            }
//            else
//            {
//                zMin = 1D - diag;
//                zMax = 1.01D - diag;
//            }
//        }
//
//        xMin += pos.getX();
//        xMax += pos.getX();
//        yMin += pos.getY();
//        yMax += pos.getY();
//        zMin += pos.getZ();
//        zMax += pos.getZ();
//        VoxelShape axisalignedbb = VoxelShapes.create(xMin, yMin, zMin, xMax, yMax, zMax);
//        if (axisalignedbb != null && mask.intersects(axisalignedbb))
//        {
//            list.add(axisalignedbb);
//        }
//    }
//    
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public AxisAlignedBB getSelectedBoundingBox(BlockState bs, World worldIn, BlockPos pos)
//    {
//        return this.getBoundingBox(bs, worldIn, pos).offset(pos);
//    }
//
//    @Override
//    public AxisAlignedBB getBoundingBox(BlockState bs, IBlockReader worldIn, BlockPos pos)
//    {
//        BlockState state = worldIn.getBlockState(pos);
//        BlockState above = worldIn.getBlockState(pos.up());
//        BlockState below = worldIn.getBlockState(pos.down());
//        BlockState north = worldIn.getBlockState(pos.north());
//        BlockState south = worldIn.getBlockState(pos.south());
//        BlockState west = worldIn.getBlockState(pos.west());
//        BlockState east = worldIn.getBlockState(pos.east());
//
//        boolean connectedN = this.canPaneConnectToBlock(north, state);
//        boolean connectedS = this.canPaneConnectToBlock(south, state);
//        boolean connectedW = this.canPaneConnectToBlock(west, state);
//        boolean connectedE = this.canPaneConnectToBlock(east, state);
//
//        boolean plateD = this.buildSolidSide(below, state);
//        boolean plateU = this.buildSolidSide(above, state);
//        boolean plateN = this.buildSolidSide(north, state);
//        boolean plateS = this.buildSolidSide(south, state);
//        boolean plateW = this.buildSolidSide(west, state);
//        boolean plateE = this.buildSolidSide(east, state);
//
//        //No plate on the sides which glass is facing
//        if (connectedN || connectedS)
//        {
//            plateW = plateE = false;
//        }
//        if (connectedW || connectedE)
//        {
//            plateN = plateS = false;
//        }
//
//        //Singleton
//        if (!connectedE && !connectedW && !connectedN && !connectedS)
//        {
//            boolean prefEW = false;
//            if (below.getBlock() == this)
//            {
//                prefEW = (isConnectedEW(below, worldIn, pos.down()) || isPreferenceEW(below, worldIn, pos.down()));
//            }
//            else if (above.getBlock() == this)
//            {
//                prefEW = (isConnectedEW(above, worldIn, pos.up()) || isPreferenceEW(above, worldIn, pos.up()));
//            }
//
//            if (this.isPreferenceEW(state, worldIn, pos))
//                prefEW = true;
//
//            if (prefEW)
//            {
//                plateN = plateS = false;
//            }
//            else
//            {
//                plateW = plateE = false;
//            }
//        }
//
//        int offset = 0;
//        int base = 1;
//        int solids = (plateD ? 1 : 0) + (plateU ? 1 : 0) + (plateN ? 1 : 0) + (plateS ? 1 : 0) + (plateW ? 1 : 0) + (plateE ? 1 : 0);
//        if (solids > 2 || (plateU && plateD) || (plateW && plateE) || (plateN && plateS))
//        {
//            base = 0;
//        }
//
//        if (connectedW || plateW)
//        {
//            offset = 2;
//        }
//        if (connectedN || plateN)
//        {
//            offset += 4;
//        }
//        if (connectedE || plateE)
//        {
//            offset += 8;
//        }
//        if (connectedS || plateS)
//        {
//            offset += 16;
//        }
//        
//        // See 1.10 version for the logic
//        return BOUNDING_BOXES[offset + base];
//    }
//
//    protected static final AxisAlignedBB[] BOUNDING_BOXES = {
//        VoxelShapes.create(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D), //Widen to the frame width, if frames on opposite sides
//        VoxelShapes.create(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D), //The glass width
//        VoxelShapes.create(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D),     //0001
//        VoxelShapes.create(0.0D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D),   //0001
//        VoxelShapes.create(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D),     //0010
//        VoxelShapes.create(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 0.625D),   //0010
//        VoxelShapes.create(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 0.75D),     //0011
//        VoxelShapes.create(0.0D, 0.0D, 0.0D, 0.625D, 1.0D, 0.625D),   //0011
//        VoxelShapes.create(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D),   //0100
//        VoxelShapes.create(0.375D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D), //0100
//        VoxelShapes.create(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D),     //0101
//        VoxelShapes.create(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D),   //0101
//        VoxelShapes.create(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D),     //0110
//        VoxelShapes.create(0.375D, 0.0D, 0.0D, 1.0D, 1.0D, 0.625D),   //0110
//        VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D),     //0111
//        VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.625D),   //0111
//        VoxelShapes.create(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D),     //1000
//        VoxelShapes.create(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D), //1000
//        VoxelShapes.create(0.0D, 0.0D, 0.25D, 0.75D, 1.0D, 1.0D),     //1001
//        VoxelShapes.create(0.0D, 0.0D, 0.375D, 0.625D, 1.0D, 1.0D),   //1001
//        VoxelShapes.create(0.25D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D),     //1010
//        VoxelShapes.create(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D),   //1010
//        VoxelShapes.create(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D),     //1011
//        VoxelShapes.create(0.0D, 0.0D, 0.0D, 0.625D, 1.0D, 1.0D),   //1011
//        VoxelShapes.create(0.25D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D),   //1100
//        VoxelShapes.create(0.375D, 0.0D, 0.375D, 1.0D, 1.0D, 1.0D), //1100
//        VoxelShapes.create(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D),     //1101
//        VoxelShapes.create(0.0D, 0.0D, 0.375D, 1.0D, 1.0D, 1.0D),   //1101
//        VoxelShapes.create(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),     //1110
//        VoxelShapes.create(0.375D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),   //1110
//        VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D),     //1111
//        VoxelShapes.create(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)   //1111
//    };
//
//    @Override
//    public boolean isSideSolid(BlockState base_state, IBlockReader world, BlockPos pos, Direction dir)
//    {
//        BlockState thisBlock = world.getBlockState(pos);
//        if ((dir == Direction.NORTH || dir == Direction.SOUTH) && this.isConnectedEW(thisBlock, world, pos)) return false;
//        if ((dir == Direction.WEST || dir == Direction.EAST) && this.isConnectedNS(thisBlock, world, pos)) return false;
//        BlockState otherBlock = world.getBlockState(pos.offset(dir.getOpposite()));
//        return this.buildSolidSide(otherBlock, thisBlock);
//    }
//
//    protected boolean isConnectedEW(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        BlockState west = worldIn.getBlockState(pos.west());
//        BlockState east = worldIn.getBlockState(pos.east());
//        return this.canPaneConnectToBlock(west, state) || this.canPaneConnectToBlock(east, state);
//    }
//
//    protected boolean isConnectedEWRecursive(BlockState state, IBlockReader worldIn, BlockPos pos, Direction dir)
//    {
//        boolean connectN = this.canPaneConnectToBlock(worldIn.getBlockState(pos.north()), state);
//        boolean connectS = this.canPaneConnectToBlock(worldIn.getBlockState(pos.south()), state);
//        boolean connectW = this.canPaneConnectToBlock(worldIn.getBlockState(pos.west()), state);
//        boolean connectE = this.canPaneConnectToBlock(worldIn.getBlockState(pos.east()), state);
//        if (connectN || connectS || connectW || connectE)
//        {
//            return (connectW || connectE) && !connectN && !connectS;
//        }
//        BlockPos next = pos.offset(dir, 1);
//        BlockState nextState = worldIn.getBlockState(next);
//        if (nextState.getBlock() == this)
//        {
//            return this.isConnectedEWRecursive(nextState, worldIn, next, dir);
//        }
//        return dir == Direction.DOWN && this.isPreferenceEW(state, worldIn, pos);
//    }
//    
//    protected boolean isConnectedNS(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        BlockState north = worldIn.getBlockState(pos.north());
//        BlockState south = worldIn.getBlockState(pos.south());
//        return this.canPaneConnectToBlock(north, state) || this.canPaneConnectToBlock(south, state);
//    }
//
//    protected boolean isConnectedNSRecursive(BlockState state, IBlockReader worldIn, BlockPos pos, Direction dir)
//    {
//        boolean connectN = this.canPaneConnectToBlock(worldIn.getBlockState(pos.north()), state);
//        boolean connectS = this.canPaneConnectToBlock(worldIn.getBlockState(pos.south()), state);
//        boolean connectW = this.canPaneConnectToBlock(worldIn.getBlockState(pos.west()), state);
//        boolean connectE = this.canPaneConnectToBlock(worldIn.getBlockState(pos.east()), state);
//        if (connectN || connectS || connectW || connectE)
//        {
//            return (connectN || connectS) && !connectW && !connectE;
//        }
//        BlockPos next = pos.offset(dir, 1);
//        BlockState nextState = worldIn.getBlockState(next);
//        if (nextState.getBlock() == this)
//        {
//            return this.isConnectedNSRecursive(nextState, worldIn, next, dir);
//        }
//        return dir == Direction.DOWN && !this.isPreferenceEW(state, worldIn, pos);
//    }
//    
//    protected boolean isPreferenceEW(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        BlockState north = worldIn.getBlockState(pos.north());
//        BlockState south = worldIn.getBlockState(pos.south());
//        BlockState west = worldIn.getBlockState(pos.west());
//        BlockState east = worldIn.getBlockState(pos.east());
//        int solidsEW = (west.getBlock().isSideSolid(worldIn.getBlockState(pos.west()), worldIn, pos.west(), Direction.EAST) ? 1 : 0) + (east.getBlock().isSideSolid(worldIn.getBlockState(pos.east()), worldIn, pos.east(), Direction.WEST) ? 1 : 0);
//        int solidsNS = (north.getBlock().isSideSolid(worldIn.getBlockState(pos.north()), worldIn, pos.north(), Direction.SOUTH) ? 1 : 0) + (south.getBlock().isSideSolid(worldIn.getBlockState(pos.south()), worldIn, pos.south(), Direction.NORTH) ? 1 : 0);
//        return solidsEW > solidsNS;
//    }
//    
//    protected boolean canPaneConnectToBlock(BlockState off, BlockState blockState)
//    {
//        return off.getBlock() == this;
//    }
//
//    protected boolean buildSolidSide(BlockState off, BlockState blockState)
//    {
//        return !(off.getBlock() == this);
//    }
//
//    protected boolean buildSolidSideUD(BlockPos pos, Direction testUD, IBlockReader worldIn, BlockState blockState)
//    {
//        BlockPos offPos = pos.offset(testUD, 1);
//        BlockState off = worldIn.getBlockState(offPos);
//        if (off.getBlock() != this) return true;
//        int connThis = (isConnectedEW(blockState, worldIn, pos) ? 2 : 0) + (isConnectedNS(blockState, worldIn, pos) ? 1 : 0);
//        int connOther = (isConnectedEW(off, worldIn, offPos) ? 2 : 0) + (isConnectedNS(off, worldIn, offPos) ? 1 : 0);
//        if (connThis == 0 && connOther == 0)
//            return false;
//        int trueThis = identifyHorizConnections(worldIn, pos, blockState);
//        int trueOther = identifyHorizConnections(worldIn, offPos, off);
//
//        //Singles (no horizontal connections) -> does it match the plane of the one above or below?
//        if (connThis == 0 && (((trueOther & 3) == 0) && trueThis != 3 || ((trueOther & 12) == 0) && trueThis == 3))
//        {
//            return false;
//        }
//        if (connOther == 0 && (((trueThis & 3) == 0) && trueOther != 3 || ((trueThis & 12) == 0)  && trueOther == 3))
//        {
//            return false;
//        }
//
//        //Non-matching planes of connection, including all singles not already dealt with -> solid LogicalSide
//        if (connThis != connOther)
//            return true;
//
//        //One LogicalSide of connection only - and matches plane of the one above/below -> no solid LogicalSide
//        if (trueThis < 3 || trueThis == 4 || trueThis == 8)
//        {
//            return false;
//        }
//        if (trueOther < 3 || trueOther == 4 || trueOther == 8)
//        {
//            return false;
//        }
//        
//        //Special cases: T junctions above/below 2-way corners, or similar -> solid LogicalSide because the glass can't connect properly
//        if ((trueThis & 7) != (trueOther & 7))
//        {
//            return true;
//        }
//        if ((trueThis & 11) != (trueOther & 11))
//        {
//            return true;
//        }
//        if ((trueThis & 13) != (trueOther & 13))
//        {
//            return true;
//        }
//        if ((trueThis & 14) != (trueOther & 14))
//        {
//            return true;
//        }
//
//        //Anything still left, it's matching planes of connection and not a special case -> no solid LogicalSide
//        return false;
//    }
//    
//    private int identifyHorizConnections(IBlockReader worldIn, BlockPos pos, BlockState state)
//    {
//        BlockState north = worldIn.getBlockState(pos.north());
//        BlockState south = worldIn.getBlockState(pos.south());
//        BlockState west = worldIn.getBlockState(pos.west());
//        BlockState east = worldIn.getBlockState(pos.east());
//        
//        boolean connectN = this.canPaneConnectToBlock(north, state);
//        boolean connectS = this.canPaneConnectToBlock(south, state);
//        boolean connectW = this.canPaneConnectToBlock(west, state);
//        boolean connectE = this.canPaneConnectToBlock(east, state);
//
//        int connections = (connectN ? 1 : 0) + (connectS ? 1 : 0) + (connectW ? 1 : 0) + (connectE ? 1 : 0);
//
//        if (!connectN && !connectS && !connectW && !connectE)
//        {
//            BlockState above = worldIn.getBlockState(pos.up());
//            BlockState below = worldIn.getBlockState(pos.down());
//
//            if (below.getBlock() == this && this.isConnectedEWRecursive(below, worldIn, pos.down(), Direction.DOWN))
//            {
//                return 3;
//            }
//            else if (above.getBlock() == this && this.isConnectedEWRecursive(above, worldIn, pos.up(), Direction.UP))
//            {
//                return 3;
//            }
//            if (below.getBlock() == this && this.isConnectedNSRecursive(below, worldIn, pos.down(), Direction.DOWN))
//            {
//                return 12;
//            }
//            else if (above.getBlock() == this && this.isConnectedNSRecursive(above, worldIn, pos.up(), Direction.UP))
//            {
//                return 12;
//            }
//            if (this.isPreferenceEW(state, worldIn, pos))
//            {
//                return 3;
//            }
//        }
//        
//        return (connectN ? 8 : 0) + (connectS ? 4 : 0) + (connectW ? 2 : 0) + (connectE ? 1 : 0);
//    }
//
//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        BlockState above = worldIn.getBlockState(pos.up());
//        BlockState below = worldIn.getBlockState(pos.down());
//        BlockState north = worldIn.getBlockState(pos.north());
//        BlockState south = worldIn.getBlockState(pos.south());
//        BlockState west = worldIn.getBlockState(pos.west());
//        BlockState east = worldIn.getBlockState(pos.east());
//        
//        boolean connectN = this.canPaneConnectToBlock(north, state);
//        boolean connectS = this.canPaneConnectToBlock(south, state);
//        boolean connectW = this.canPaneConnectToBlock(west, state);
//        boolean connectE = this.canPaneConnectToBlock(east, state);
//        
//        boolean plateD = buildSolidSideUD(pos, Direction.DOWN, worldIn, state);
//        boolean plateU = buildSolidSideUD(pos, Direction.UP, worldIn, state);
//        boolean plateN = this.buildSolidSide(north, state);
//        boolean plateS = this.buildSolidSide(south, state);
//        boolean plateW = this.buildSolidSide(west, state);
//        boolean plateE = this.buildSolidSide(east, state);
//
//        int connections = (connectN ? 1 : 0) + (connectS ? 1 : 0) + (connectW ? 1 : 0) + (connectE ? 1 : 0);
//        GlassRotation rot = GlassRotation.N;
//        int cornerPiece = 0;
//        
//        if (connections == 4)
//            return getModel(state, 4, rot, plateD, false, false, plateU, 0);
//
//        if (connections == 3)
//        {
//            if (!connectE) rot = GlassRotation.S;
//            else if (!connectN) rot = GlassRotation.E;
//            else if (!connectS) rot = GlassRotation.W;
//
//            return getModel(state, 3, rot, plateD, false, false, plateU, 0);
//        }
//
//        if (connections == 0)
//        {
//            if (below.getBlock() == this && this.isConnectedEWRecursive(below, worldIn, pos.down(), Direction.DOWN))
//            {
//                rot = GlassRotation.E;
//            }
//            else if (above.getBlock() == this && this.isConnectedEWRecursive(above, worldIn, pos.up(), Direction.UP))
//            {
//                rot = GlassRotation.E;
//            }
//            if (below.getBlock() == this && this.isConnectedNSRecursive(below, worldIn, pos.down(), Direction.DOWN))
//            {
//            }
//            else if (above.getBlock() == this && this.isConnectedNSRecursive(above, worldIn, pos.up(), Direction.UP))
//            {
//            }
//            else if (this.isPreferenceEW(state, worldIn, pos))
//            {
//                rot = GlassRotation.E;
//            }
//
//            return getModel(state, 1, rot, plateD, plateS, plateN, plateU, 0);
//        }
//        
////        EnumFacing testUD = EnumFacing.UP;
////        if (plateU && !plateD)           
////        {
////            testUD = EnumFacing.DOWN;
////        }
////        if (plateD && !plateU)           
////        {
////            testUD = EnumFacing.UP;
////        }
//
//        // Two glass connections in a flat plane - this also covers connected flat on one LogicalSide only
//        if (connectW && connectE || connections == 1 && (connectW || connectE))
//        {
//            rot = GlassRotation.E;
//            if (connectW && !plateU && buildSolidSideUD(pos.west(), Direction.UP, worldIn, west))
//            {
//                cornerPiece++;
//            }
//            if (connectW && !plateD && buildSolidSideUD(pos.west(), Direction.DOWN, worldIn, west))
//            {
//                cornerPiece += 5;
//            }
//            if (connectE && !plateU && buildSolidSideUD(pos.east(), Direction.UP, worldIn, east))
//            {
//                cornerPiece++;
//                if (connectW) rot = GlassRotation.W;
//            }
//            if (connectE && !plateD && buildSolidSideUD(pos.east(), Direction.DOWN, worldIn, east))
//            {
//                cornerPiece += 5;
//                if (connectW) rot = GlassRotation.W;
//            }
//            return getModel(state, 1, rot, plateD, plateW, plateE, plateU, cornerPiece);
//        }
//
//        if (connectN && connectS || connections == 1)
//        {
//            if (connectN && !plateU && buildSolidSideUD(pos.north(), Direction.UP, worldIn, north))
//            {
//                if (!connectS)
//                {
//                    cornerPiece |= 5;
//                    if (!plateD) rot = GlassRotation.S;
//                }
//                else
//                {
//                    cornerPiece |= 1;
//                    rot = GlassRotation.S;
//                }
//            }
//            if (connectN && !plateD && buildSolidSideUD(pos.north(), Direction.DOWN, worldIn, north))
//            {
//                cornerPiece ++;
//                if (connectS) rot = GlassRotation.S;
//            }
//            if (connectS && !plateU && buildSolidSideUD(pos.south(), Direction.UP, worldIn, south))
//            {
//                if (cornerPiece == 0 && !plateD)
//                {
//                    cornerPiece = 5;
//                    rot = GlassRotation.S;
//                }
//                else if ((cornerPiece & 3) < 2)
//                {
//                    cornerPiece ++;
//                }
//            }
//            if (connectS && !plateD && buildSolidSideUD(pos.south(), Direction.DOWN, worldIn, south))
//            {
//                if ((cornerPiece & 3) < 2)
//                {
//                    cornerPiece ++;
//                }
//                else
//                {
//                    cornerPiece |= 5;
//                    //rot = GlassRotation.S;
//                }
//            }
//            return getModel(state, 1, rot, plateD, plateS, plateN, plateU, cornerPiece);
//        }
//
//        //It must be two glass connections on a corner
//
//        if (connectW && connectS) rot = GlassRotation.S;
//        else if (connectS && connectE) rot = GlassRotation.E;
//        else if (connectN && connectW) rot = GlassRotation.W;
//        
//        return getModel(state, 2, rot, plateD, false, false, plateU, 0);
//    }
//
//    private BlockState getModel(BlockState state, int model, GlassRotation rot, boolean plateD, boolean plateL, boolean plateR, boolean plateU, int cornerPiece)
//    {
//        int x = 0;
//        int y = rot.ordinal();
//        if (y % 2 == 1) y = y ^ 2;
//        int solids = (plateD ? 1 : 0) + (plateU ? 1 : 0) + (plateR ? 1 : 0) + (plateL ? 1 : 0);
//        switch (model)
//        {
//        case 1:
//            if (solids == 4)
//                return state.with(MODEL, GlassModel.STANDARD_S4).with(ROTATION, rot.get(y, 0));
//
//            if (solids == 3)
//            {
//                if (!plateD) x = ( y % 2 == 1) ? 3 : 1; 
//                else if (!plateR) { x = 2; y = y ^ 2; }
//                else if (!plateU) x = ( y % 2 == 1) ? 1 :3;
//                return state.with(MODEL, GlassModel.STANDARD_S3).with(ROTATION, rot.get(y, x));
//            }
//
//            if (plateD && plateU)
//                return state.with(MODEL, GlassModel.STANDARD_S2A).with(ROTATION, rot.get(y, 0));
//
//            if (plateL && plateR)
//                return state.with(MODEL, GlassModel.STANDARD_S2A).with(ROTATION, rot.get(y, 1));
//
//            if (plateD && (plateR || plateL))
//            {
//                return state.with(MODEL, cornerPiece > 0 ? GlassModel.STANDARD_S2B : GlassModel.STANDARD_S2).with(ROTATION, rot.get(plateL ? (y ^ 2) : y, 0));
//            }
//
//            if (plateU && (plateR || plateL))
//            {
//                return state.with(MODEL, cornerPiece > 0 ? GlassModel.STANDARD_S2B : GlassModel.STANDARD_S2).with(ROTATION, rot.get(plateL ? (y ^ 2) : y, 2));
//            }
//
//            if (plateU || plateD)
//                return state.with(MODEL, getCornerModel(cornerPiece)).with(ROTATION, rot.get(y, (plateU || cornerPiece > 4) ? 2 : 0));
//
//            if (plateR || plateL)
//                return state.with(MODEL, getCornerModel(cornerPiece)).with(ROTATION, rot.get(plateL ? (y ^ 2) : y, 1 + ( cornerPiece > 4 ? 2 : 0)));
//            
//            return state.with(MODEL, GlassModel.STANDARD_PANE).with(ROTATION, rot.get(y, 0));
//        case 2:
//            if (plateU && plateD)
//                return state.with(MODEL, GlassModel.CORNER_S2).with(ROTATION, rot.get(y, 0));
//            if (plateD)
//                return state.with(MODEL, GlassModel.CORNER_S).with(ROTATION, rot.get(y, 0));
//            if (plateU)
//                return state.with(MODEL, GlassModel.CORNER_S).with(ROTATION, rot.get((y + 3) % 4, 2));
//            
//            return state.with(MODEL, GlassModel.CORNER).with(ROTATION, rot.get(y, 0));
//        case 3:
//            if (plateD && plateU)
//                return state.with(MODEL, GlassModel.T_JUNCTION_S2).with(ROTATION, rot.get(y, 0));
//            if (plateD)
//                return state.with(MODEL, GlassModel.T_JUNCTION_S).with(ROTATION, rot.get(y, 0));
//            if (plateU)
//                return state.with(MODEL, GlassModel.T_JUNCTION_S).with(ROTATION, rot.get(y ^ 2, 2));
//                
//            return state.with(MODEL, GlassModel.T_JUNCTION).with(ROTATION, rot.get(y, x));
//        case 4:
//            if (plateD && plateU)
//                return state.with(MODEL, GlassModel.CROSSROADS_S2).with(ROTATION, rot.get(y, 0));
//            if (plateD)
//                return state.with(MODEL, GlassModel.CROSSROADS_S).with(ROTATION, rot.get(y, 0));
//            if (plateU)
//                return state.with(MODEL, GlassModel.CROSSROADS_S).with(ROTATION, rot.get(y ^ 2, 2));
//        default:
//            return state.with(MODEL, GlassModel.CROSSROADS).with(ROTATION, rot.get(y, x));
//        }
//    }
//    
//    private Comparable getCornerModel(int cornerPiece)
//    {
//        if ((cornerPiece & 2) == 2)
//            return GlassModel.STANDARD_S1BB;
//        return cornerPiece > 0 ? GlassModel.STANDARD_S1B : GlassModel.STANDARD_S1;
//    }
//
//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState();
//    }
//
//    @Override
//    public int setColor(int newColor, PlayerEntity p, LogicalSide LogicalSide)
//    {
//        if (newColor >= 0 && this.color != newColor)
//        {
//            this.color = newColor;
//            return 1;
//        }
//        return 0;
//    }
//    
//    public void resetColor()
//    {
//        this.color = this.frame.defaultColor;
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public static void updateGlassColors(int color1, int color2, int color3)
//    {
//        int changes = 0;
//        changes += GCBlocks.spaceGlassVanilla.setColor(color1, null, LogicalSide.CLIENT);
//        changes += GCBlocks.spaceGlassClear.setColor(color2, null, LogicalSide.CLIENT);
//        changes += GCBlocks.spaceGlassStrong.setColor(color3, null, LogicalSide.CLIENT);
//        
//        if (changes > 0)
//            BlockSpaceGlass.updateClientRender();
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    public static void updateClientRender()
//    {
//        Minecraft.getInstance().renderGlobal.loadRenderers();
//        //TODO: improve performance, this is re-rendering ALL chunks on client
//        //Can we somehow limit this to chunks containing BlockSpaceGlass?
//        //or else: don't do all the chunk redrawing at once, queue them?
//    }
//
//    @Override
//    public String getShiftDescription(ItemStack stack)
//    {
//        switch (this.type)
//        {
//        case CLEAR:
//            return GCCoreUtil.translate("tile.space_glass.clear.description");
//        case VANILLA:
//            return GCCoreUtil.translate("tile.space_glass.description");
//        case STRONG:
//        default:
//            return GCCoreUtil.translate("tile.space_glass.strong.description");
//        }
//        
//    }
//
//    @Override
//    public boolean showDescription(ItemStack stack)
//    {
//        return true;
//    }
//
//    public enum GlassModel implements IStringSerializable
//    {
//        STANDARD_PANE("standard"),
//        CORNER("corner"),
//        T_JUNCTION("joint"),
//        CROSSROADS("joinx"),
//        STANDARD_S1("standards1"),
//        STANDARD_S1B("standards1b"),
//        STANDARD_S1BB("standards1bb"),
//        STANDARD_S2("standards2"),
//        STANDARD_S2A("standards2a"),
//        STANDARD_S2B("standards2b"),
//        STANDARD_S3("standards3"),
//        STANDARD_S4("standards4"),
//        CORNER_S("corner_s"),
//        CORNER_S2("corner_s2"),
//        T_JUNCTION_S("joint_s"),
//        T_JUNCTION_S2("joint_s2"),
//        CROSSROADS_S("joinx_s"),
//        CROSSROADS_S2("joinx_s2");
//
//        private final String name;
//
//        private GlassModel(String name)
//        {
//            this.name = name;
//        }
//
//        @Override
//        public String getName()
//        {
//            return this.name;
//        }
//    }
//
//    public enum GlassRotation implements IStringSerializable
//    {
//        N("0"),
//        E("y"),
//        S("yy"),
//        W("yyy"),
//        UN("u"),
//        UE("uyyy"),
//        US("uyy"),
//        UW("uy"),
//        UUN("uuyy"),
//        UUE("uuyyy"),
//        UUS("uu"),
//        UUW("uuy"),
//        DN("d"),
//        DE("dy"),
//        DS("dyy"),
//        DW("dyyy");
//
//        private final String name;
//
//        private GlassRotation(String name)
//        {
//            this.name = name;
//        }
//
//        @Override
//        public String getName()
//        {
//            return this.name;
//        }
//        
//        private static GlassRotation get(int i)
//        {
//            return GlassRotation.values()[i];
//        }
//
//        private static GlassRotation get(int y, int x)
//        {
//            int i = y + x *4;
//            return GlassRotation.values()[i];
//        }
//}
//
//    public enum GlassFrame implements IStringSerializable
//    {
//        PLAIN("plain", 0xfafaf7),
//        TIN_DECO("tin_deco", 0xFFFFFF);
//        //TODO - more frame textures can be added in future - maybe only on Asteroids?
//
//        private final String name;
//        private final int defaultColor;
//        
//        private GlassFrame (String name, int color)
//        {
//            this.name = name;
//            this.defaultColor = color;
//        }
//
//        @Override
//        public String getName()
//        {
//            return this.name;
//        }
//
//        public int getDefaultColor()
//        {
//            return this.defaultColor;
//        }
//    }
//
//    public enum GlassType implements IStringSerializable
//    {
//        VANILLA("vanilla"),
//        CLEAR("clear"),
//        STRONG("strong");
//
//        private final String name;
//
//        private GlassType (String name)
//        {
//            this.name = name;
//        }
//
//        @Override
//        public String getName()
//        {
//            return this.name;
//        }
//    }
//}
//
