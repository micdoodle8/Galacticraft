//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.core.tile.TileEntityPainter;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.Direction;
//import net.minecraft.util.IStringSerializable;
//
///**
// * A block for several types of UNPOWERED Galacticraft machine
// * with a base building purpose - e.g. Painter
// *
// * Note: does not implement IMachineSides
// *
// */
//public class BlockMachine3 extends BlockMachineBase
//{
//    public static final EnumProperty<EnumMachineBuildingType> TYPE = EnumProperty.create("type", EnumMachineBuildingType.class);
//
//    public enum EnumMachineBuildingType implements EnumMachineBase, IStringSerializable
//    {
//        PAINTER(0, "painter", TileEntityPainter::new, "tile.painter.description", "tile.machine3.9");
//
//        private final int meta;
//        private final String name;
//        private final TileConstructor tile;
//        private final String shiftDescriptionKey;
//        private final String blockName;
//
//        EnumMachineBuildingType(int meta, String name, TileConstructor tile, String key, String blockName)
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
//        private final static EnumMachineBuildingType[] values = values();
//        @Override
//        public EnumMachineBuildingType fromMetadata(int meta)
//        {
//            return values[(meta / 4) % values.length];
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
//    public BlockMachine3(Properties builder)
//    {
//        super(builder);
//    }
//
//    @Override
//    protected void initialiseTypes()
//    {
//        this.types = EnumMachineBuildingType.values;
//        this.typeBase = EnumMachineBuildingType.values[0];
//    }
//
//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.byHorizontalIndex(meta % 4);
//        EnumMachineBuildingType type = (EnumMachineBuildingType) typeBase.fromMetadata(meta);
//        return this.getDefaultState().with(FACING, enumfacing).with(TYPE, type);
//    }
//
//    @Override
//    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
//    {
//        builder.add(FACING, TYPE);
//    }
//}
