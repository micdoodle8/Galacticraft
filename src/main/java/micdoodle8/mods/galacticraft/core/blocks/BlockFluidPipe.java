package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.tile.IColorable;
import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidPipe;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockFluidPipe extends BlockTransmitter implements ITileEntityProvider, IShiftDescription, ISortableBlock
{
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    public static boolean ignoreDrop = false;

    private EnumPipeMode mode;

    private static final float MIN = 0.35F;
    private static final float MAX = 0.65F;
    protected static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[] {

            new AxisAlignedBB(MIN, MIN, MIN, MAX, MAX, MAX),  // No connection                                  000000
            new AxisAlignedBB(MIN, MIN, MIN, MAX, MAX, 1.0D), // South                                          000001
            new AxisAlignedBB(0.0D, MIN, MIN, MAX, MAX, MAX), // West                                           000010
            new AxisAlignedBB(0.0D, MIN, MIN, MAX, MAX, 1.0D), // South West                                    000011
            new AxisAlignedBB(MIN, MIN, 0.0D, MAX, MAX, MAX), // North                                          000100
            new AxisAlignedBB(MIN, MIN, 0.0D, MAX, MAX, 1.0D), // North South                                   000101
            new AxisAlignedBB(0.0D, MIN, 0.0D, MAX, MAX, MAX), // North West                                    000110
            new AxisAlignedBB(0.0D, MIN, 0.0D, MAX, MAX, 1.0D), // North South West                             000111
            new AxisAlignedBB(MIN, MIN, MIN, 1.0D, MAX, MAX), // East                                           001000
            new AxisAlignedBB(MIN, MIN, MIN, 1.0D, MAX, 1.0D), // East South                                    001001
            new AxisAlignedBB(0.0D, MIN, MIN, 1.0D, MAX, MAX), // West East                                     001010
            new AxisAlignedBB(0.0D, MIN, MIN, 1.0D, MAX, 1.0D), // South West East                              001011
            new AxisAlignedBB(MIN, MIN, 0.0D, 1.0D, MAX, MAX), // North East                                    001100
            new AxisAlignedBB(MIN, MIN, 0.0D, 1.0D, MAX, 1.0D), // North South East                             001101
            new AxisAlignedBB(0.0D, MIN, 0.0D, 1.0D, MAX, MAX), // North East West                              001110
            new AxisAlignedBB(0.0D, MIN, 0.0D, 1.0D, MAX, 1.0D), // North South East West                       001111

            new AxisAlignedBB(MIN, 0.0D, MIN, MAX, MAX, MAX),  // Down                                          010000
            new AxisAlignedBB(MIN, 0.0D, MIN, MAX, MAX, 1.0D), // Down South                                    010001
            new AxisAlignedBB(0.0D, 0.0D, MIN, MAX, MAX, MAX), // Down West                                     010010
            new AxisAlignedBB(0.0D, 0.0D, MIN, MAX, MAX, 1.0D), // Down South West                              010011
            new AxisAlignedBB(MIN, 0.0D, 0.0D, MAX, MAX, MAX), // Down North                                    010100
            new AxisAlignedBB(MIN, 0.0D, 0.0D, MAX, MAX, 1.0D), // Down North South                             010101
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, MAX, MAX, MAX), // Down North West                              010110
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, MAX, MAX, 1.0D), // Down North South West                       010111
            new AxisAlignedBB(MIN, 0.0D, MIN, 1.0D, MAX, MAX), // Down East                                     011000
            new AxisAlignedBB(MIN, 0.0D, MIN, 1.0D, MAX, 1.0D), // Down East South                              011001
            new AxisAlignedBB(0.0D, 0.0D, MIN, 1.0D, MAX, MAX), // Down West East                               011010
            new AxisAlignedBB(0.0D, 0.0D, MIN, 1.0D, MAX, 1.0D), // Down South West East                        011011
            new AxisAlignedBB(MIN, 0.0D, 0.0D, 1.0D, MAX, MAX), // Down North East                              011100
            new AxisAlignedBB(MIN, 0.0D, 0.0D, 1.0D, MAX, 1.0D), // Down North South East                       011101
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, MAX, MAX), // Down North East West                        011110
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, MAX, 1.0D), // Down North South East West                 011111

            new AxisAlignedBB(MIN, MIN, MIN, MAX, 1.0D, MAX),  // Up                                            100000
            new AxisAlignedBB(MIN, MIN, MIN, MAX, 1.0D, 1.0D), // Up South                                      100001
            new AxisAlignedBB(0.0D, MIN, MIN, MAX, 1.0D, MAX), // Up West                                       100010
            new AxisAlignedBB(0.0D, MIN, MIN, MAX, 1.0D, 1.0D), // Up South West                                100011
            new AxisAlignedBB(MIN, MIN, 0.0D, MAX, 1.0D, MAX), // Up North                                      100100
            new AxisAlignedBB(MIN, MIN, 0.0D, MAX, 1.0D, 1.0D), // Up North South                               100101
            new AxisAlignedBB(0.0D, MIN, 0.0D, MAX, 1.0D, MAX), // Up North West                                100110
            new AxisAlignedBB(0.0D, MIN, 0.0D, MAX, 1.0D, 1.0D), // Up North South West                         100111
            new AxisAlignedBB(MIN, MIN, MIN, 1.0D, 1.0D, MAX), // Up East                                       101000
            new AxisAlignedBB(MIN, MIN, MIN, 1.0D, 1.0D, 1.0D), // Up East South                                101001
            new AxisAlignedBB(0.0D, MIN, MIN, 1.0D, 1.0D, MAX), // Up West East                                 101010
            new AxisAlignedBB(0.0D, MIN, MIN, 1.0D, 1.0D, 1.0D), // Up South West East                          101011
            new AxisAlignedBB(MIN, MIN, 0.0D, 1.0D, 1.0D, MAX), // Up North East                                101100
            new AxisAlignedBB(MIN, MIN, 0.0D, 1.0D, 1.0D, 1.0D), // Up North South East                         101101
            new AxisAlignedBB(0.0D, MIN, 0.0D, 1.0D, 1.0D, MAX), // Up North East West                          101110
            new AxisAlignedBB(0.0D, MIN, 0.0D, 1.0D, 1.0D, 1.0D), // Up North South East West                   101111

            new AxisAlignedBB(MIN, 0.0D, MIN, MAX, 1.0D, MAX),  // Up Down                                      110000
            new AxisAlignedBB(MIN, 0.0D, MIN, MAX, 1.0D, 1.0D), // Up Down South                                110001
            new AxisAlignedBB(0.0D, 0.0D, MIN, MAX, 1.0D, MAX), // Up Down West                                 110010
            new AxisAlignedBB(0.0D, 0.0D, MIN, MAX, 1.0D, 1.0D), // Up Down South West                          110011
            new AxisAlignedBB(MIN, 0.0D, 0.0D, MAX, 1.0D, MAX), // Up Down North                                110100
            new AxisAlignedBB(MIN, 0.0D, 0.0D, MAX, 1.0D, 1.0D), // Up Down North South                         110101
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, MAX, 1.0D, MAX), // Up Down North West                          110110
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, MAX, 1.0D, 1.0D), // Up Down North South West                   110111
            new AxisAlignedBB(MIN, 0.0D, MIN, 1.0D, 1.0D, MAX), // Up Down East                                 111000
            new AxisAlignedBB(MIN, 0.0D, MIN, 1.0D, 1.0D, 1.0D), // Up Down East South                          111001
            new AxisAlignedBB(0.0D, 0.0D, MIN, 1.0D, 1.0D, MAX), // Up Down West East                           111010
            new AxisAlignedBB(0.0D, 0.0D, MIN, 1.0D, 1.0D, 1.0D), // Up Down South West East                    111011
            new AxisAlignedBB(MIN, 0.0D, 0.0D, 1.0D, 1.0D, MAX), // Up Down North East                          111100
            new AxisAlignedBB(MIN, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), // Up Down North South East                   111101
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, MAX), // Up Down North East West                    111110
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)}; // Up Down North South East West            111111

    public BlockFluidPipe(String assetName, EnumPipeMode mode)
    {
        super(Material.GLASS);
        this.setHardness(0.3F);
        this.setSoundType(SoundType.GLASS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
        this.setUnlocalizedName(assetName);
        this.mode = mode;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        state = this.getActualState(state, source, pos);
        return BOUNDING_BOXES[getBoundingBoxIdx(state)];
    }

    private static int getBoundingBoxIdx(IBlockState state)
    {
        int i = 0;

        if (state.getValue(NORTH).booleanValue())
        {
            i |= 1 << EnumFacing.NORTH.getHorizontalIndex();
        }

        if (state.getValue(EAST).booleanValue())
        {
            i |= 1 << EnumFacing.EAST.getHorizontalIndex();
        }

        if (state.getValue(SOUTH).booleanValue())
        {
            i |= 1 << EnumFacing.SOUTH.getHorizontalIndex();
        }

        if (state.getValue(WEST).booleanValue())
        {
            i |= 1 << EnumFacing.WEST.getHorizontalIndex();
        }

        if (state.getValue(DOWN).booleanValue())
        {
            i |= 1 << 4;
        }

        if (state.getValue(UP).booleanValue())
        {
            i |= 1 << 5;
        }

        return i;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        final TileEntityFluidPipe tile = (TileEntityFluidPipe) worldIn.getTileEntity(pos);
        int pipeColor = state.getValue(COLOR).getDyeDamage();

        if (!ignoreDrop && tile != null && pipeColor != 15)
        {
            spawnItem(worldIn, pos, pipeColor);
        }

        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(GCBlocks.oxygenPipe);
        //Never drop the 'pull' variety of pipe
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        worldIn.notifyLightSet(pos);
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        if (this.mode == EnumPipeMode.NORMAL)
        {
            return GalacticraftCore.galacticraftBlocksTab;
        }

        return null;
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote)
        {
            TileEntityFluidPipe tile = (TileEntityFluidPipe) world.getTileEntity(pos);
            tile.switchType();
        }

        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        final TileEntityFluidPipe tileEntity = (TileEntityFluidPipe) worldIn.getTileEntity(pos);

        if (super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ))
        {
            return true;
        }

        if (!worldIn.isRemote)
        {
            final ItemStack stack = playerIn.inventory.getCurrentItem();

            if (!stack.isEmpty())
            {
                if (stack.getItem() instanceof ItemDye)
                {
                    final int dyeColor = playerIn.inventory.getCurrentItem().getItemDamage();

                    final byte colorBefore = tileEntity.getColor(state);

                    tileEntity.onColorUpdate();

                    worldIn.setBlockState(pos, state.withProperty(COLOR, EnumDyeColor.byDyeDamage(dyeColor)));

                    GalacticraftCore.packetPipeline.sendToAllAround(new PacketSimple(PacketSimple.EnumSimplePacket.C_RECOLOR_PIPE, GCCoreUtil.getDimensionID(worldIn), new Object[] { pos }), new NetworkRegistry.TargetPoint(GCCoreUtil.getDimensionID(worldIn), pos.getX(), pos.getY(), pos.getZ(), 40.0));

                    if (colorBefore != (byte) dyeColor && !playerIn.capabilities.isCreativeMode)
                    {
                        playerIn.inventory.getCurrentItem().shrink(1);
                    }

                    if (colorBefore != (byte) dyeColor && colorBefore != 15)
                    {
                        spawnItem(worldIn, pos, colorBefore);
                    }

                    //					GCCorePacketManager.sendPacketToClients(GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES, tileEntity, tileEntity.getColor(), (byte) -1)); TODO Fix pipe color

                    BlockPos tileVec = tileEntity.getPos();
                    for (final EnumFacing dir : EnumFacing.VALUES)
                    {
                        final TileEntity tileAt = worldIn.getTileEntity(tileVec.offset(dir));

                        if (tileAt != null && tileAt instanceof IColorable)
                        {
                            ((IColorable) tileAt).onAdjacentColorChanged(dir);
                        }
                    }

                    return true;
                }
            }

        }

        return false;
    }

    private void spawnItem(World worldIn, BlockPos pos, int colorBefore)
    {
        final float f = 0.7F;
        Random syncRandom = GCCoreUtil.getRandom(pos);
        final double d0 = syncRandom.nextFloat() * f + (1.0F - f) * 0.5D;
        final double d1 = syncRandom.nextFloat() * f + (1.0F - f) * 0.2D + 0.6D;
        final double d2 = syncRandom.nextFloat() * f + (1.0F - f) * 0.5D;
        final EntityItem entityitem = new EntityItem(worldIn, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, new ItemStack(Items.DYE, 1, colorBefore));
        entityitem.setDefaultPickupDelay();
        worldIn.spawnEntity(entityitem);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
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
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityFluidPipe();
    }

//    @SideOnly(Side.CLIENT)
//    @Override
//    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
//    {
//        return this.getCollisionBoundingBox(worldIn.getBlockState(pos), worldIn, pos);
//    }

    @Override
    public NetworkType getNetworkType(IBlockState state)
    {
        return NetworkType.FLUID;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return GCCoreUtil.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, COLOR, UP, DOWN, NORTH, EAST, SOUTH, WEST);
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
        return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(COLOR).getMetadata();
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.TRANSMITTER;
    }

    public EnumPipeMode getMode()
    {
        return mode;
    }

    public enum EnumPipeMode implements IStringSerializable
    {
        NORMAL(0, "normal"),
        PULL(1, "pull");

        private final int meta;
        private final String name;

        EnumPipeMode(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return meta;
        }

        public static EnumPipeMode byMetadata(int ordinal)
        {
            return EnumPipeMode.values()[ordinal];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }
}
