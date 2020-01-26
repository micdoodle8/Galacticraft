package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.tile.IMachineSides;
import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties;
import micdoodle8.mods.galacticraft.core.tile.TileEntityCircuitFabricator;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDeconstructor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricIngotCompressor;
import micdoodle8.mods.galacticraft.core.tile.TileEntityOxygenStorageModule;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockMachine2 extends BlockMachineBase
{
    public static final PropertyEnum<EnumMachineExtendedType> TYPE = PropertyEnum.create("type", EnumMachineExtendedType.class);
    public static IMachineSidesProperties MACHINESIDES_RENDERTYPE = IMachineSidesProperties.TWOFACES_HORIZ;
    public static final PropertyEnum SIDES = MACHINESIDES_RENDERTYPE.asProperty;

    public enum EnumMachineExtendedType implements IStringSerializable
    {
        ELECTRIC_COMPRESSOR(0, "electric_compressor", TileEntityElectricIngotCompressor.class, "tile.compressor_electric.description", "tile.machine2.4"),
        CIRCUIT_FABRICATOR(1, "circuit_fabricator", TileEntityCircuitFabricator.class, "tile.circuit_fabricator.description", "tile.machine2.5"),
        OXYGEN_STORAGE(2, "oxygen_storage", TileEntityOxygenStorageModule.class, "tile.oxygen_storage_module.description", "tile.machine2.6"),
        DECONSTRUCTOR(3, "deconstructor", TileEntityDeconstructor.class, "tile.deconstructor.description", "tile.machine2.10");
        
        private final int meta;
        private final String name;
        private final Class tile;
        private final String shiftDescriptionKey;
        private final String blockName;

        EnumMachineExtendedType(int meta, String name, Class tile, String key, String blockName)
        {
            this.meta = meta;
            this.name = name;
            this.tile = tile;
            this.shiftDescriptionKey = key;
            this.blockName = blockName;
        }

        public int getMetadata()
        {
            return this.meta * 4;
        }

        private final static EnumMachineExtendedType[] values = values();
        public static EnumMachineExtendedType byMeta(int meta)
        {
            return values[meta % values.length];
        }
        
        public static EnumMachineExtendedType getByMetadata(int metadata)
        {
            return byMeta(metadata / 4);
        }

        @Override
        public String getName()
        {
            return this.name;
        }
        
        public TileEntity tileConstructor()
        {
            try
            {
                return (TileEntity) this.tile.newInstance();
            } catch (InstantiationException | IllegalAccessException ex)
            {
                return null;
            }
        }

        public String getShiftDescription()
        {
            return GCCoreUtil.translate(this.shiftDescriptionKey);
        }

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
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        int meta = getMetaFromState(state);
        EnumMachineExtendedType type = EnumMachineExtendedType.getByMetadata(meta);
        return type.tileConstructor();
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (EnumMachineExtendedType type : EnumMachineExtendedType.values)
            list.add(new ItemStack(this, 1, type.getMetadata()));
    }

    @Override
    public String getShiftDescription(int meta)
    {
        EnumMachineExtendedType type = EnumMachineExtendedType.getByMetadata(meta);
        return type.getShiftDescription();
    }

    @Override
    public String getUnlocalizedName(int meta)
    {
        EnumMachineExtendedType type = EnumMachineExtendedType.getByMetadata(meta);
        return type.getUnlocalizedName();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta % 4);
        EnumMachineExtendedType type = EnumMachineExtendedType.getByMetadata(meta);
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
