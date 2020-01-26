package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.tile.IMachineSides;
import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCircuitFabricator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDeconstructor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenStorageModule;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockMachine2 extends BlockMachineBase
{
    public static final PropertyEnum<EnumMachineExtendedType> TYPE = PropertyEnum.create("type", EnumMachineExtendedType.class);
    public static IMachineSidesProperties MACHINESIDES_RENDERTYPE = IMachineSidesProperties.TWOFACES_HORIZ;
    public static final PropertyEnum SIDES = MACHINESIDES_RENDERTYPE.asProperty;

    public enum EnumMachineExtendedType implements EnumMachineBase, IStringSerializable
    {
        ELECTRIC_COMPRESSOR(0, "electric_compressor", TileEntityElectricIngotCompressor::new, "tile.compressor_electric.description", "tile.machine2.4"),
        CIRCUIT_FABRICATOR(4, "circuit_fabricator", TileEntityCircuitFabricator::new, "tile.circuit_fabricator.description", "tile.machine2.5"),
        OXYGEN_STORAGE(8, "oxygen_storage", TileEntityOxygenStorageModule::new, "tile.oxygen_storage_module.description", "tile.machine2.6"),
        DECONSTRUCTOR(12, "deconstructor", TileEntityDeconstructor::new, "tile.deconstructor.description", "tile.machine2.10");
        
        private final int meta;
        private final String name;
        private final TileConstructor tile;
        private final String shiftDescriptionKey;
        private final String blockName;

        EnumMachineExtendedType(int meta, String name, TileConstructor tile, String key, String blockName)
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

        private final static EnumMachineExtendedType[] values = values();
        @Override
        public EnumMachineExtendedType fromMetadata(int meta)
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

    public BlockMachine2(String assetName)
    {
        super(assetName);
    }

    @Override
    protected void initialiseTypes()
    {
        this.types = EnumMachineExtendedType.values;
        this.typeBase = EnumMachineExtendedType.values[0];
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta % 4);
        EnumMachineExtendedType type = (EnumMachineExtendedType) typeBase.fromMetadata(meta);
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(FACING)).getHorizontalIndex() + ((EnumMachineExtendedType) state.getValue(TYPE)).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, TYPE, SIDES);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        return IMachineSides.addPropertyForTile(state, tile, MACHINESIDES_RENDERTYPE, SIDES);
    }
}
