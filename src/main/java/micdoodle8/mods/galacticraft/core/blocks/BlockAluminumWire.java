package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.transmission.NetworkType;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.IShiftDescription;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWireSwitch;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAluminumWire extends BlockTransmitter implements ITileEntityProvider, IShiftDescription, ISortableBlock
{
    public static final PropertyEnum<EnumWireType> WIRE_TYPE = PropertyEnum.create("wiretype", EnumWireType.class);
    private static final float MIN = 0.38F;
    private static final float MINH = 0.3F;
    private static final float MAX = 0.62F;
    private static final float MAXH = 0.7F;
    protected static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[] {

            new AxisAlignedBB(MIN, MIN, MIN, MAX, MAX, MAX),  // No connection                                  0000000
            new AxisAlignedBB(MIN, MIN, MIN, MAX, MAX, 1.0D), // South                                          0000001
            new AxisAlignedBB(0.0D, MIN, MIN, MAX, MAX, MAX), // West                                           0000010
            new AxisAlignedBB(0.0D, MIN, MIN, MAX, MAX, 1.0D), // South West                                    0000011
            new AxisAlignedBB(MIN, MIN, 0.0D, MAX, MAX, MAX), // North                                          0000100
            new AxisAlignedBB(MIN, MIN, 0.0D, MAX, MAX, 1.0D), // North South                                   0000101
            new AxisAlignedBB(0.0D, MIN, 0.0D, MAX, MAX, MAX), // North West                                    0000110
            new AxisAlignedBB(0.0D, MIN, 0.0D, MAX, MAX, 1.0D), // North South West                             0000111
            new AxisAlignedBB(MIN, MIN, MIN, 1.0D, MAX, MAX), // East                                           0001000
            new AxisAlignedBB(MIN, MIN, MIN, 1.0D, MAX, 1.0D), // East South                                    0001001
            new AxisAlignedBB(0.0D, MIN, MIN, 1.0D, MAX, MAX), // West East                                     0001010
            new AxisAlignedBB(0.0D, MIN, MIN, 1.0D, MAX, 1.0D), // South West East                              0001011
            new AxisAlignedBB(MIN, MIN, 0.0D, 1.0D, MAX, MAX), // North East                                    0001100
            new AxisAlignedBB(MIN, MIN, 0.0D, 1.0D, MAX, 1.0D), // North South East                             0001101
            new AxisAlignedBB(0.0D, MIN, 0.0D, 1.0D, MAX, MAX), // North East West                              0001110
            new AxisAlignedBB(0.0D, MIN, 0.0D, 1.0D, MAX, 1.0D), // North South East West                       0001111

            new AxisAlignedBB(MIN, 0.0D, MIN, MAX, MAX, MAX),  // Down                                          0010000
            new AxisAlignedBB(MIN, 0.0D, MIN, MAX, MAX, 1.0D), // Down South                                    0010001
            new AxisAlignedBB(0.0D, 0.0D, MIN, MAX, MAX, MAX), // Down West                                     0010010
            new AxisAlignedBB(0.0D, 0.0D, MIN, MAX, MAX, 1.0D), // Down South West                              0010011
            new AxisAlignedBB(MIN, 0.0D, 0.0D, MAX, MAX, MAX), // Down North                                    0010100
            new AxisAlignedBB(MIN, 0.0D, 0.0D, MAX, MAX, 1.0D), // Down North South                             0010101
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, MAX, MAX, MAX), // Down North West                              0010110
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, MAX, MAX, 1.0D), // Down North South West                       0010111
            new AxisAlignedBB(MIN, 0.0D, MIN, 1.0D, MAX, MAX), // Down East                                     0011000
            new AxisAlignedBB(MIN, 0.0D, MIN, 1.0D, MAX, 1.0D), // Down East South                              0011001
            new AxisAlignedBB(0.0D, 0.0D, MIN, 1.0D, MAX, MAX), // Down West East                               0011010
            new AxisAlignedBB(0.0D, 0.0D, MIN, 1.0D, MAX, 1.0D), // Down South West East                        0011011
            new AxisAlignedBB(MIN, 0.0D, 0.0D, 1.0D, MAX, MAX), // Down North East                              0011100
            new AxisAlignedBB(MIN, 0.0D, 0.0D, 1.0D, MAX, 1.0D), // Down North South East                       0011101
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, MAX, MAX), // Down North East West                        0011110
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, MAX, 1.0D), // Down North South East West                 0011111
    
            new AxisAlignedBB(MIN, MIN, MIN, MAX, 1.0D, MAX),  // Up                                            0100000
            new AxisAlignedBB(MIN, MIN, MIN, MAX, 1.0D, 1.0D), // Up South                                      0100001
            new AxisAlignedBB(0.0D, MIN, MIN, MAX, 1.0D, MAX), // Up West                                       0100010
            new AxisAlignedBB(0.0D, MIN, MIN, MAX, 1.0D, 1.0D), // Up South West                                0100011
            new AxisAlignedBB(MIN, MIN, 0.0D, MAX, 1.0D, MAX), // Up North                                      0100100
            new AxisAlignedBB(MIN, MIN, 0.0D, MAX, 1.0D, 1.0D), // Up North South                               0100101
            new AxisAlignedBB(0.0D, MIN, 0.0D, MAX, 1.0D, MAX), // Up North West                                0100110
            new AxisAlignedBB(0.0D, MIN, 0.0D, MAX, 1.0D, 1.0D), // Up North South West                         0100111
            new AxisAlignedBB(MIN, MIN, MIN, 1.0D, 1.0D, MAX), // Up East                                       0101000
            new AxisAlignedBB(MIN, MIN, MIN, 1.0D, 1.0D, 1.0D), // Up East South                                0101001
            new AxisAlignedBB(0.0D, MIN, MIN, 1.0D, 1.0D, MAX), // Up West East                                 0101010
            new AxisAlignedBB(0.0D, MIN, MIN, 1.0D, 1.0D, 1.0D), // Up South West East                          0101011
            new AxisAlignedBB(MIN, MIN, 0.0D, 1.0D, 1.0D, MAX), // Up North East                                0101100
            new AxisAlignedBB(MIN, MIN, 0.0D, 1.0D, 1.0D, 1.0D), // Up North South East                         0101101
            new AxisAlignedBB(0.0D, MIN, 0.0D, 1.0D, 1.0D, MAX), // Up North East West                          0101110
            new AxisAlignedBB(0.0D, MIN, 0.0D, 1.0D, 1.0D, 1.0D), // Up North South East West                   0101111

            new AxisAlignedBB(MIN, 0.0D, MIN, MAX, 1.0D, MAX),  // Up Down                                      0110000
            new AxisAlignedBB(MIN, 0.0D, MIN, MAX, 1.0D, 1.0D), // Up Down South                                0110001
            new AxisAlignedBB(0.0D, 0.0D, MIN, MAX, 1.0D, MAX), // Up Down West                                 0110010
            new AxisAlignedBB(0.0D, 0.0D, MIN, MAX, 1.0D, 1.0D), // Up Down South West                          0110011
            new AxisAlignedBB(MIN, 0.0D, 0.0D, MAX, 1.0D, MAX), // Up Down North                                0110100
            new AxisAlignedBB(MIN, 0.0D, 0.0D, MAX, 1.0D, 1.0D), // Up Down North South                         0110101
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, MAX, 1.0D, MAX), // Up Down North West                          0110110
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, MAX, 1.0D, 1.0D), // Up Down North South West                   0110111
            new AxisAlignedBB(MIN, 0.0D, MIN, 1.0D, 1.0D, MAX), // Up Down East                                 0111000
            new AxisAlignedBB(MIN, 0.0D, MIN, 1.0D, 1.0D, 1.0D), // Up Down East South                          0111001
            new AxisAlignedBB(0.0D, 0.0D, MIN, 1.0D, 1.0D, MAX), // Up Down West East                           0111010
            new AxisAlignedBB(0.0D, 0.0D, MIN, 1.0D, 1.0D, 1.0D), // Up Down South West East                    0111011
            new AxisAlignedBB(MIN, 0.0D, 0.0D, 1.0D, 1.0D, MAX), // Up Down North East                          0111100
            new AxisAlignedBB(MIN, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), // Up Down North South East                   0111101
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, MAX), // Up Down North East West                    0111110
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), // Up Down North South East West             0111111
    
            new AxisAlignedBB(MINH, MINH, MINH, MAXH, MAXH, MAXH),  // No connection                            1000000
            new AxisAlignedBB(MINH, MINH, MINH, MAXH, MAXH, 1.0D), // South                                     1000001
            new AxisAlignedBB(0.0D, MINH, MINH, MAXH, MAXH, MAXH), // West                                      1000010
            new AxisAlignedBB(0.0D, MINH, MINH, MAXH, MAXH, 1.0D), // South West                                1000011
            new AxisAlignedBB(MINH, MINH, 0.0D, MAXH, MAXH, MAXH), // North                                     1000100
            new AxisAlignedBB(MINH, MINH, 0.0D, MAXH, MAXH, 1.0D), // North South                               1000101
            new AxisAlignedBB(0.0D, MINH, 0.0D, MAXH, MAXH, MAXH), // North West                                1000110
            new AxisAlignedBB(0.0D, MINH, 0.0D, MAXH, MAXH, 1.0D), // North South West                          1000111
            new AxisAlignedBB(MINH, MINH, MINH, 1.0D, MAXH, MAXH), // East                                      1001000
            new AxisAlignedBB(MINH, MINH, MINH, 1.0D, MAXH, 1.0D), // East South                                1001001
            new AxisAlignedBB(0.0D, MINH, MINH, 1.0D, MAXH, MAXH), // West East                                 1001010
            new AxisAlignedBB(0.0D, MINH, MINH, 1.0D, MAXH, 1.0D), // South West East                           1001011
            new AxisAlignedBB(MINH, MINH, 0.0D, 1.0D, MAXH, MAXH), // North East                                1001100
            new AxisAlignedBB(MINH, MINH, 0.0D, 1.0D, MAXH, 1.0D), // North South East                          1001101
            new AxisAlignedBB(0.0D, MINH, 0.0D, 1.0D, MAXH, MAXH), // North East West                           1001110
            new AxisAlignedBB(0.0D, MINH, 0.0D, 1.0D, MAXH, 1.0D), // North South East West                     1001111

            new AxisAlignedBB(MINH, 0.0D, MINH, MAXH, MAXH, MAXH),  // Down                                     1010000
            new AxisAlignedBB(MINH, 0.0D, MINH, MAXH, MAXH, 1.0D), // Down South                                1010001
            new AxisAlignedBB(0.0D, 0.0D, MINH, MAXH, MAXH, MAXH), // Down West                                 1010010
            new AxisAlignedBB(0.0D, 0.0D, MINH, MAXH, MAXH, 1.0D), // Down South West                           1010011
            new AxisAlignedBB(MINH, 0.0D, 0.0D, MAXH, MAXH, MAXH), // Down North                                1010100
            new AxisAlignedBB(MINH, 0.0D, 0.0D, MAXH, MAXH, 1.0D), // Down North South                          1010101
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, MAXH, MAXH, MAXH), // Down North West                           1010110
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, MAXH, MAXH, 1.0D), // Down North South West                     1010111
            new AxisAlignedBB(MINH, 0.0D, MINH, 1.0D, MAXH, MAXH), // Down East                                 1011000
            new AxisAlignedBB(MINH, 0.0D, MINH, 1.0D, MAXH, 1.0D), // Down East South                           1011001
            new AxisAlignedBB(0.0D, 0.0D, MINH, 1.0D, MAXH, MAXH), // Down West East                            1011010
            new AxisAlignedBB(0.0D, 0.0D, MINH, 1.0D, MAXH, 1.0D), // Down South West East                      1011011
            new AxisAlignedBB(MINH, 0.0D, 0.0D, 1.0D, MAXH, MAXH), // Down North East                           1011100
            new AxisAlignedBB(MINH, 0.0D, 0.0D, 1.0D, MAXH, 1.0D), // Down North South East                     1011101
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, MAXH, MAXH), // Down North East West                      1011110
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, MAXH, 1.0D), // Down North South East West                1011111
    
            new AxisAlignedBB(MINH, MINH, MINH, MAXH, 1.0D, MAXH),  // Up                                       1100000
            new AxisAlignedBB(MINH, MINH, MINH, MAXH, 1.0D, 1.0D), // Up South                                  1100001
            new AxisAlignedBB(0.0D, MINH, MINH, MAXH, 1.0D, MAXH), // Up West                                   1100010
            new AxisAlignedBB(0.0D, MINH, MINH, MAXH, 1.0D, 1.0D), // Up South West                             1100011
            new AxisAlignedBB(MINH, MINH, 0.0D, MAXH, 1.0D, MAXH), // Up North                                  1100100
            new AxisAlignedBB(MINH, MINH, 0.0D, MAXH, 1.0D, 1.0D), // Up North South                            1100101
            new AxisAlignedBB(0.0D, MINH, 0.0D, MAXH, 1.0D, MAXH), // Up North West                             1100110
            new AxisAlignedBB(0.0D, MINH, 0.0D, MAXH, 1.0D, 1.0D), // Up North South West                       1100111
            new AxisAlignedBB(MINH, MINH, MINH, 1.0D, 1.0D, MAXH), // Up East                                   1101000
            new AxisAlignedBB(MINH, MINH, MINH, 1.0D, 1.0D, 1.0D), // Up East South                             1101001
            new AxisAlignedBB(0.0D, MINH, MINH, 1.0D, 1.0D, MAXH), // Up West East                              1101010
            new AxisAlignedBB(0.0D, MINH, MINH, 1.0D, 1.0D, 1.0D), // Up South West East                        1101011
            new AxisAlignedBB(MINH, MINH, 0.0D, 1.0D, 1.0D, MAXH), // Up North East                             1101100
            new AxisAlignedBB(MINH, MINH, 0.0D, 1.0D, 1.0D, 1.0D), // Up North South East                       1101101
            new AxisAlignedBB(0.0D, MINH, 0.0D, 1.0D, 1.0D, MAXH), // Up North East West                        1101110
            new AxisAlignedBB(0.0D, MINH, 0.0D, 1.0D, 1.0D, 1.0D), // Up North South East West                  1101111

            new AxisAlignedBB(MINH, 0.0D, MINH, MAXH, 1.0D, MAXH),  // Up Down                                  1110000
            new AxisAlignedBB(MINH, 0.0D, MINH, MAXH, 1.0D, 1.0D), // Up Down South                             1110001
            new AxisAlignedBB(0.0D, 0.0D, MINH, MAXH, 1.0D, MAXH), // Up Down West                              1110010
            new AxisAlignedBB(0.0D, 0.0D, MINH, MAXH, 1.0D, 1.0D), // Up Down South West                        1110011
            new AxisAlignedBB(MINH, 0.0D, 0.0D, MAXH, 1.0D, MAXH), // Up Down North                             1110100
            new AxisAlignedBB(MINH, 0.0D, 0.0D, MAXH, 1.0D, 1.0D), // Up Down North South                       1110101
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, MAXH, 1.0D, MAXH), // Up Down North West                        1110110
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, MAXH, 1.0D, 1.0D), // Up Down North South West                  1110111
            new AxisAlignedBB(MINH, 0.0D, MINH, 1.0D, 1.0D, MAXH), // Up Down East                              1111000
            new AxisAlignedBB(MINH, 0.0D, MINH, 1.0D, 1.0D, 1.0D), // Up Down East South                        1111001
            new AxisAlignedBB(0.0D, 0.0D, MINH, 1.0D, 1.0D, MAXH), // Up Down West East                         1111010
            new AxisAlignedBB(0.0D, 0.0D, MINH, 1.0D, 1.0D, 1.0D), // Up Down South West East                   1111011
            new AxisAlignedBB(MINH, 0.0D, 0.0D, 1.0D, 1.0D, MAXH), // Up Down North East                        1111100
            new AxisAlignedBB(MINH, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D), // Up Down North South East                  1111101
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, MAXH), // Up Down North East West                   1111110
            new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D)}; // Up Down North South East West            1111111

    public enum EnumWireType implements IStringSerializable
    {
        ALUMINUM_WIRE(0, "alu_wire"),
        ALUMINUM_WIRE_HEAVY(1, "alu_wire_heavy"),
        ALUMINUM_WIRE_SWITCHED(2, "alu_wire_switch"),
        ALUMINUM_WIRE_SWITCHED_HEAVY(3, "alu_wire_switch_heavy");

        private final int meta;
        private final String name;

        EnumWireType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        private final static EnumWireType[] values = values();
        public static EnumWireType byMetadata(int meta)
        {
            return values[meta % values.length];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockAluminumWire(String assetName)
    {
        super(Material.CLOTH);
        this.setSoundType(SoundType.CLOTH);
        this.setResistance(0.2F);
        this.setHardness(0.075F);
        this.setUnlocalizedName(assetName);
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

        // Is heavy:
        if (((EnumWireType) state.getValue(WIRE_TYPE)).ordinal() % 2 == 1)
        {
            i |= 1 << 6;
        }

        return i;
    }

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
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        TileEntity tile;
        switch (metadata)
        {
        case 0:
            tile = new TileEntityAluminumWire(1);
            break;
        case 1:
            tile = new TileEntityAluminumWire(2);
            break;
        case 2:
            tile = new TileEntityAluminumWireSwitch(1);
            break;
        case 3:
            tile = new TileEntityAluminumWireSwitch(2);
            break;
        default:
            return null;
        }

        return tile;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        list.add(new ItemStack(this, 1, 0));
        list.add(new ItemStack(this, 1, 1));
        list.add(new ItemStack(this, 1, 2));
        list.add(new ItemStack(this, 1, 3));
    }

    @Override
    public NetworkType getNetworkType(IBlockState state)
    {
        return NetworkType.POWER;
    }

    @Override
    public String getShiftDescription(int itemDamage)
    {
        switch (itemDamage)
        {
        case 0:
            return GCCoreUtil.translate("tile.aluminum_wire.alu_wire.description");
        case 1:
            return GCCoreUtil.translate("tile.aluminum_wire.alu_wire_heavy.description");
        case 2:
            return GCCoreUtil.translate("tile.aluminum_wire.alu_wire_switch.description");
        case 3:
            return GCCoreUtil.translate("tile.aluminum_wire.alu_wire_switch_heavy.description");
        }
        return "";
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(WIRE_TYPE, EnumWireType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumWireType) state.getValue(WIRE_TYPE)).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, WIRE_TYPE, UP, DOWN, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public boolean showDescription(int itemDamage)
    {
        return true;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.TRANSMITTER;
    }
}
