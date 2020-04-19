package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.tile.TileEntityPainter;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;

/**
 * A block for several types of UNPOWERED Galacticraft machine
 * with a base building purpose - e.g. Painter
 * 
 * Note: does not implement IMachineSides
 *
 */
public class BlockMachine3 extends BlockMachineBase
{
    public static final PropertyEnum<EnumMachineBuildingType> TYPE = PropertyEnum.create("type", EnumMachineBuildingType.class);

    public enum EnumMachineBuildingType implements EnumMachineBase, IStringSerializable
    {
        PAINTER(0, "painter", TileEntityPainter::new, "tile.painter.description", "tile.machine3.9");

        private final int meta;
        private final String name;
        private final TileConstructor tile;
        private final String shiftDescriptionKey;
        private final String blockName;

        EnumMachineBuildingType(int meta, String name, TileConstructor tile, String key, String blockName)
        {
            this.meta = meta;
            this.name = name;
            this.tile = tile;
            this.shiftDescriptionKey = key;
            this.blockName = blockName;
        }

        @Override
        public int getMetadata()
        {
            return this.meta;
        }

        private final static EnumMachineBuildingType[] values = values();
        @Override
        public EnumMachineBuildingType fromMetadata(int meta)
        {
            return values[(meta / 4) % values.length];
        }
        
        @Override
        public String getName()
        {
            return this.name;
        }
        
        @Override
        public TileEntity tileConstructor()
        {
            return this.tile.create();
        }

        @FunctionalInterface
        private static interface TileConstructor
        {
              TileEntity create();
        }

        @Override
        public String getShiftDescriptionKey()
        {
            return this.shiftDescriptionKey;
        }

        @Override
        public String getUnlocalizedName()
        {
            return this.blockName;
        }
    }

    public BlockMachine3(String assetName)
    {
        super(assetName);
    }

    @Override
    protected void initialiseTypes()
    {
        this.types = EnumMachineBuildingType.values;
        this.typeBase = EnumMachineBuildingType.values[0];
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta % 4);
        EnumMachineBuildingType type = (EnumMachineBuildingType) typeBase.fromMetadata(meta);
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(FACING)).getHorizontalIndex() + ((EnumMachineBuildingType) state.getValue(TYPE)).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, TYPE);
    }
}
