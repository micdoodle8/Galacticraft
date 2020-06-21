//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.core.tile.IMachineSides;
//import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricIngotCompressor;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.properties.PropertyEnum;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.Direction;
//import net.minecraft.util.IStringSerializable;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockReader;
//
///**
// * A block for advanced types of POWERED Galacticraft machine
// * with a base building purpose - e.g. Advanced Compressor
// *
// * Note: this DOES implement IMachineSides, in comparison with BlockMachine3 which does not
// *
// */
//public class BlockMachine4 extends BlockMachineBase
//{
//    public static final EnumProperty<EnumMachineAdvancedType> TYPE = EnumProperty.create("type", EnumMachineAdvancedType.class);
//    public static IMachineSidesProperties MACHINESIDES_RENDERTYPE = IMachineSidesProperties.TWOFACES_HORIZ;
//    public static final PropertyEnum SIDES = MACHINESIDES_RENDERTYPE.asProperty;
//
//    public enum EnumMachineAdvancedType implements EnumMachineBase, IStringSerializable
//    {
//        ADVANCED_COMPRESSOR(0, "advanced_compressor", () -> new TileEntityElectricIngotCompressor(true), "tile.compressor_advanced.description", "tile.machine4.11");
//
//        private final int meta;
//        private final String name;
//        private final TileConstructor tile;
//        private final String shiftDescriptionKey;
//        private final String blockName;
//
//        EnumMachineAdvancedType(int meta, String name, TileConstructor tile, String key, String blockName)
//        {
//            this.meta = meta;
//            this.name = name;
//            this.tile = tile;
//            this.shiftDescriptionKey = key;
//            this.blockName = blockName;
//        }
//
//        @Override
//        public int getMetadata()
//        {
//            return this.meta;
//        }
//
//        private final static EnumMachineAdvancedType[] values = values();
//        @Override
//        public EnumMachineAdvancedType fromMetadata(int meta)
//        {
//            return values[0]; //(meta / 4) % values.length];
//        }
//
//        @Override
//        public String getName()
//        {
//            return this.name;
//        }
//
//        @Override
//        public TileEntity tileConstructor()
//        {
//            return this.tile.create();
//        }
//
//        @FunctionalInterface
//        private static interface TileConstructor
//        {
//              TileEntity create();
//        }
//
//        @Override
//        public String getShiftDescriptionKey()
//        {
//            return this.shiftDescriptionKey;
//        }
//
//        @Override
//        public String getTranslationKey()
//        {
//            return this.blockName;
//        }
//    }
//
//    public BlockMachine4(Properties builder)
//    {
//        super(builder);
//    }
//
//    @Override
//    protected void initialiseTypes()
//    {
//        this.types = EnumMachineAdvancedType.values;
//        this.typeBase = EnumMachineAdvancedType.values[0];
//    }
//
//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.byHorizontalIndex(meta % 4);
//        EnumMachineAdvancedType type = (EnumMachineAdvancedType) typeBase.fromMetadata(meta);
//        return this.getDefaultState().with(FACING, enumfacing).with(TYPE, type);
//    }
//
//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(FACING, TYPE, SIDES);
//    }
//
//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        TileEntity tile = worldIn.getTileEntity(pos);
//        return IMachineSides.addPropertyForTile(state, tile, MACHINESIDES_RENDERTYPE, SIDES);
//    }
//}
