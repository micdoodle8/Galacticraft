//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.api.tile.IColorable;
//import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
//import micdoodle8.mods.galacticraft.core.GCBlocks;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
//import micdoodle8.mods.galacticraft.core.network.PacketSimple;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidPipe;
//import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
//import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.ITileEntityProvider;
//import net.minecraft.block.state.BlockFaceShape;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.entity.item.ItemEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.*;
//import net.minecraft.state.EnumProperty;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.BlockRenderLayer;
//import net.minecraft.util.Direction;
//import net.minecraft.util.Hand;
//import net.minecraft.util.IStringSerializable;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.shapes.VoxelShape;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.common.network.NetworkRegistry;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.util.Random;
//
//public class BlockFluidPipe extends BlockTransmitter implements ITileEntityProvider, IShiftDescription, ISortableBlock
//{
//    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
//
//    public static boolean ignoreDrop = false;
//
//    private EnumPipeMode mode;
//
//    private static final float MIN = 0.35F;
//    private static final float MAX = 0.65F;
//    protected static final VoxelShape[] BOUNDING_BOXES = new VoxelShape[] {
//
//            Block.makeCuboidShape(MIN, MIN, MIN, MAX, MAX, MAX),  // No connection                                  000000
//            Block.makeCuboidShape(MIN, MIN, MIN, MAX, MAX, 1.0D), // South                                          000001
//            Block.makeCuboidShape(0.0D, MIN, MIN, MAX, MAX, MAX), // West                                           000010
//            Block.makeCuboidShape(0.0D, MIN, MIN, MAX, MAX, 1.0D), // South West                                    000011
//            Block.makeCuboidShape(MIN, MIN, 0.0D, MAX, MAX, MAX), // North                                          000100
//            Block.makeCuboidShape(MIN, MIN, 0.0D, MAX, MAX, 1.0D), // North South                                   000101
//            Block.makeCuboidShape(0.0D, MIN, 0.0D, MAX, MAX, MAX), // North West                                    000110
//            Block.makeCuboidShape(0.0D, MIN, 0.0D, MAX, MAX, 1.0D), // North South West                             000111
//            Block.makeCuboidShape(MIN, MIN, MIN, 1.0D, MAX, MAX), // East                                           001000
//            Block.makeCuboidShape(MIN, MIN, MIN, 1.0D, MAX, 1.0D), // East South                                    001001
//            Block.makeCuboidShape(0.0D, MIN, MIN, 1.0D, MAX, MAX), // West East                                     001010
//            Block.makeCuboidShape(0.0D, MIN, MIN, 1.0D, MAX, 1.0D), // South West East                              001011
//            Block.makeCuboidShape(MIN, MIN, 0.0D, 1.0D, MAX, MAX), // North East                                    001100
//            Block.makeCuboidShape(MIN, MIN, 0.0D, 1.0D, MAX, 1.0D), // North South East                             001101
//            Block.makeCuboidShape(0.0D, MIN, 0.0D, 1.0D, MAX, MAX), // North East West                              001110
//            Block.makeCuboidShape(0.0D, MIN, 0.0D, 1.0D, MAX, 1.0D), // North South East West                       001111
//
//            Block.makeCuboidShape(MIN, 0.0D, MIN, MAX, MAX, MAX),  // Down                                          010000
//            Block.makeCuboidShape(MIN, 0.0D, MIN, MAX, MAX, 1.0D), // Down South                                    010001
//            Block.makeCuboidShape(0.0D, 0.0D, MIN, MAX, MAX, MAX), // Down West                                     010010
//            Block.makeCuboidShape(0.0D, 0.0D, MIN, MAX, MAX, 1.0D), // Down South West                              010011
//            Block.makeCuboidShape(MIN, 0.0D, 0.0D, MAX, MAX, MAX), // Down North                                    010100
//            Block.makeCuboidShape(MIN, 0.0D, 0.0D, MAX, MAX, 1.0D), // Down North South                             010101
//            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, MAX, MAX, MAX), // Down North West                              010110
//            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, MAX, MAX, 1.0D), // Down North South West                       010111
//            Block.makeCuboidShape(MIN, 0.0D, MIN, 1.0D, MAX, MAX), // Down East                                     011000
//            Block.makeCuboidShape(MIN, 0.0D, MIN, 1.0D, MAX, 1.0D), // Down East South                              011001
//            Block.makeCuboidShape(0.0D, 0.0D, MIN, 1.0D, MAX, MAX), // Down West East                               011010
//            Block.makeCuboidShape(0.0D, 0.0D, MIN, 1.0D, MAX, 1.0D), // Down South West East                        011011
//            Block.makeCuboidShape(MIN, 0.0D, 0.0D, 1.0D, MAX, MAX), // Down North East                              011100
//            Block.makeCuboidShape(MIN, 0.0D, 0.0D, 1.0D, MAX, 1.0D), // Down North South East                       011101
//            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, MAX, MAX), // Down North East West                        011110
//            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, MAX, 1.0D), // Down North South East West                 011111
//
//            Block.makeCuboidShape(MIN, MIN, MIN, MAX, 1.0D, MAX),  // Up                                            100000
//            Block.makeCuboidShape(MIN, MIN, MIN, MAX, 1.0D, 1.0D), // Up South                                      100001
//            Block.makeCuboidShape(0.0D, MIN, MIN, MAX, 1.0D, MAX), // Up West                                       100010
//            Block.makeCuboidShape(0.0D, MIN, MIN, MAX, 1.0D, 1.0D), // Up South West                                100011
//            Block.makeCuboidShape(MIN, MIN, 0.0D, MAX, 1.0D, MAX), // Up North                                      100100
//            Block.makeCuboidShape(MIN, MIN, 0.0D, MAX, 1.0D, 1.0D), // Up North South                               100101
//            Block.makeCuboidShape(0.0D, MIN, 0.0D, MAX, 1.0D, MAX), // Up North West                                100110
//            Block.makeCuboidShape(0.0D, MIN, 0.0D, MAX, 1.0D, 1.0D), // Up North South West                         100111
//            Block.makeCuboidShape(MIN, MIN, MIN, 1.0D, 1.0D, MAX), // Up East                                       101000
//            Block.makeCuboidShape(MIN, MIN, MIN, 1.0D, 1.0D, 1.0D), // Up East South                                101001
//            Block.makeCuboidShape(0.0D, MIN, MIN, 1.0D, 1.0D, MAX), // Up West East                                 101010
//            Block.makeCuboidShape(0.0D, MIN, MIN, 1.0D, 1.0D, 1.0D), // Up South West East                          101011
//            Block.makeCuboidShape(MIN, MIN, 0.0D, 1.0D, 1.0D, MAX), // Up North East                                101100
//            Block.makeCuboidShape(MIN, MIN, 0.0D, 1.0D, 1.0D, 1.0D), // Up North South East                         101101
//            Block.makeCuboidShape(0.0D, MIN, 0.0D, 1.0D, 1.0D, MAX), // Up North East West                          101110
//            Block.makeCuboidShape(0.0D, MIN, 0.0D, 1.0D, 1.0D, 1.0D), // Up North South East West                   101111
//
//            Block.makeCuboidShape(MIN, 0.0D, MIN, MAX, 1.0D, MAX),  // Up Down                                      110000
//            Block.makeCuboidShape(MIN, 0.0D, MIN, MAX, 1.0D, 1.0D), // Up Down South                                110001
//            Block.makeCuboidShape(0.0D, 0.0D, MIN, MAX, 1.0D, MAX), // Up Down West                                 110010
//            Block.makeCuboidShape(0.0D, 0.0D, MIN, MAX, 1.0D, 1.0D), // Up Down South West                          110011
//            Block.makeCuboidShape(MIN, 0.0D, 0.0D, MAX, 1.0D, MAX), // Up Down North                                110100
//            Block.makeCuboidShape(MIN, 0.0D, 0.0D, MAX, 1.0D, 1.0D), // Up Down North South                         110101
//            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, MAX, 1.0D, MAX), // Up Down North West                          110110
//            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, MAX, 1.0D, 1.0D), // Up Down North South West                   110111
//            Block.makeCuboidShape(MIN, 0.0D, MIN, 1.0D, 1.0D, MAX), // Up Down East                                 111000
//            Block.makeCuboidShape(MIN, 0.0D, MIN, 1.0D, 1.0D, 1.0D), // Up Down East South                          111001
//            Block.makeCuboidShape(0.0D, 0.0D, MIN, 1.0D, 1.0D, MAX), // Up Down West East                           111010
//            Block.makeCuboidShape(0.0D, 0.0D, MIN, 1.0D, 1.0D, 1.0D), // Up Down South West East                    111011
//            Block.makeCuboidShape(MIN, 0.0D, 0.0D, 1.0D, 1.0D, MAX), // Up Down North East                          111100
//            Block.makeCuboidShape(MIN, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), // Up Down North South East                   111101
//            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, MAX), // Up Down North East West                    111110
//            Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)}; // Up Down North South East West            111111
//
//    public BlockFluidPipe(Properties builder, EnumPipeMode mode)
//    {
//        super(builder);
//        this.setDefaultState(stateContainer.getBaseState().with(COLOR, DyeColor.WHITE));
//        this.mode = mode;
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
//    {
//        state = this.getActualState(state, source, pos);
//        return BOUNDING_BOXES[getBoundingBoxIdx(state)];
//    }
//
//    private static int getBoundingBoxIdx(BlockState state)
//    {
//        int i = 0;
//
//        if (state.get(NORTH).booleanValue())
//        {
//            i |= 1 << Direction.NORTH.getHorizontalIndex();
//        }
//
//        if (state.get(EAST).booleanValue())
//        {
//            i |= 1 << Direction.EAST.getHorizontalIndex();
//        }
//
//        if (state.get(SOUTH).booleanValue())
//        {
//            i |= 1 << Direction.SOUTH.getHorizontalIndex();
//        }
//
//        if (state.get(WEST).booleanValue())
//        {
//            i |= 1 << Direction.WEST.getHorizontalIndex();
//        }
//
//        if (state.get(DOWN).booleanValue())
//        {
//            i |= 1 << 4;
//        }
//
//        if (state.get(UP).booleanValue())
//        {
//            i |= 1 << 5;
//        }
//
//        return i;
//    }
//
//    @Override
//    public void breakBlock(World worldIn, BlockPos pos, BlockState state)
//    {
//        final TileEntityFluidPipe tile = (TileEntityFluidPipe) worldIn.getTileEntity(pos);
//        int pipeColor = state.get(COLOR).getDyeDamage();
//
//        if (!ignoreDrop && tile != null && pipeColor != 15)
//        {
//            spawnItem(worldIn, pos, pipeColor);
//        }
//
//        super.breakBlock(worldIn, pos, state);
//    }
//
//    @Override
//    public Item getItemDropped(BlockState state, Random rand, int fortune)
//    {
//        return Item.getItemFromBlock(GCBlocks.oxygenPipe);
//        //Never drop the 'pull' variety of pipe
//    }
//
//    @Override
//    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
//    {
//        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
//        worldIn.notifyLightSet(pos);
//    }
//
//    @Override
//    public ItemGroup getCreativeTabToDisplayOn()
//    {
//        if (this.mode == EnumPipeMode.NORMAL)
//        {
//            return GalacticraftCore.galacticraftBlocksTab;
//        }
//
//        return null;
//    }
//
//    @Override
//    public boolean onUseWrench(World world, BlockPos pos, PlayerEntity entityPlayer, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
//    {
//        if (!world.isRemote)
//        {
//            TileEntityFluidPipe tile = (TileEntityFluidPipe) world.getTileEntity(pos);
//            tile.switchType();
//        }
//
//        return true;
//    }
//
//    @Override
//    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
//    {
//        final TileEntityFluidPipe tileEntity = (TileEntityFluidPipe) worldIn.getTileEntity(pos);
//
//        if (super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ))
//        {
//            return true;
//        }
//
//        if (!worldIn.isRemote)
//        {
//            final ItemStack stack = playerIn.inventory.getCurrentItem();
//
//            if (!stack.isEmpty())
//            {
//                if (stack.getItem() instanceof DyeItem)
//                {
//                    final int dyeColor = playerIn.inventory.getCurrentItem().getItemDamage();
//
//                    final byte colorBefore = tileEntity.getColor(state);
//
//                    tileEntity.onColorUpdate();
//
//                    worldIn.setBlockState(pos, state.with(COLOR, DyeColor.byDyeDamage(dyeColor)));
//
//                    GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(PacketSimple.EnumSimplePacket.C_RECOLOR_PIPE, GCCoreUtil.getDimensionID(worldIn), new Object[] { pos }), new PacketDistributor.TargetPoint(GCCoreUtil.getDimensionID(worldIn), pos.getX(), pos.getY(), pos.getZ(), 40.0));
//
//                    if (colorBefore != (byte) dyeColor && !playerIn.abilities.isCreativeMode)
//                    {
//                        playerIn.inventory.getCurrentItem().shrink(1);
//                    }
//
//                    if (colorBefore != (byte) dyeColor && colorBefore != 15)
//                    {
//                        spawnItem(worldIn, pos, colorBefore);
//                    }
//
//                    //					GCCorePacketManager.sendPacketToClients(GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, tileEntity, tileEntity.getColor(), (byte) -1)); TODO Fix pipe color
//
//                    BlockPos tileVec = tileEntity.getPos();
//                    for (final Direction dir : Direction.VALUES)
//                    {
//                        final TileEntity tileAt = worldIn.getTileEntity(tileVec.offset(dir));
//
//                        if (tileAt != null && tileAt instanceof IColorable)
//                        {
//                            ((IColorable) tileAt).onAdjacentColorChanged(dir);
//                        }
//                    }
//
//                    return true;
//                }
//            }
//
//        }
//
//        return false;
//    }
//
//    private void spawnItem(World worldIn, BlockPos pos, int colorBefore)
//    {
//        final float f = 0.7F;
//        Random syncRandom = GCCoreUtil.getRandom(pos);
//        final double d0 = syncRandom.nextFloat() * f + (1.0F - f) * 0.5D;
//        final double d1 = syncRandom.nextFloat() * f + (1.0F - f) * 0.2D + 0.6D;
//        final double d2 = syncRandom.nextFloat() * f + (1.0F - f) * 0.5D;
//        final ItemEntity entityitem = new ItemEntity(worldIn, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, new ItemStack(Items.DYE, 1, colorBefore));
//        entityitem.setDefaultPickupDelay();
//        worldIn.addEntity(entityitem);
//    }
//
//    @Override
//    public boolean shouldSideBeRendered(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
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
//    public TileEntity createTileEntity(BlockState state, IBlockReader world)
//    {
//        return new TileEntityFluidPipe();
//    }
//
////    @OnlyIn(Dist.CLIENT)
////    @Override
////    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
////    {
////        return this.getCollisionBoundingBox(worldIn.getBlockState(pos), worldIn, pos);
////    }
//
//    @Override
//    public NetworkType getNetworkType(BlockState state)
//    {
//        return NetworkType.FLUID;
//    }
//
//    @Override
//    public String getShiftDescription(ItemStack stack)
//    {
//        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
//    }
//
//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(COLOR, UP, DOWN, NORTH, EAST, SOUTH, WEST);
//    }
//
//    @Override
//    public boolean showDescription(ItemStack stack)
//    {
//        return true;
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public BlockRenderLayer getBlockLayer()
//    {
//        return BlockRenderLayer.CUTOUT;
//    }
//
//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        return this.getDefaultState().with(COLOR, DyeColor.byMetadata(meta));
//    }
//
//    @Override
//    public EnumSortCategoryBlock getCategory(int meta)
//    {
//        return EnumSortCategoryBlock.TRANSMITTER;
//    }
//
//    public EnumPipeMode getMode()
//    {
//        return mode;
//    }
//
//    public enum EnumPipeMode implements IStringSerializable
//    {
//        NORMAL(0, "normal"),
//        PULL(1, "pull");
//
//        private final int meta;
//        private final String name;
//
//        EnumPipeMode(int meta, String name)
//        {
//            this.meta = meta;
//            this.name = name;
//        }
//
//        public int getMeta()
//        {
//            return meta;
//        }
//
//        public static EnumPipeMode byMetadata(int ordinal)
//        {
//            return EnumPipeMode.values()[ordinal];
//        }
//
//        @Override
//        public String getName()
//        {
//            return this.name;
//        }
//    }
//}
