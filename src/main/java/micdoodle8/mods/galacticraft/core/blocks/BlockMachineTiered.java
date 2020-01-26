package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.tile.IMachineSides;
import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricFurnace;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEnergyStorageModule;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockMachineTiered extends BlockMachineBase
{
    public static final PropertyEnum<EnumTieredMachineType> TYPE = PropertyEnum.create("type", EnumTieredMachineType.class);
    public static IMachineSidesProperties MACHINESIDES_RENDERTYPE = IMachineSidesProperties.TWOFACES_HORIZ;
    public static final PropertyEnum SIDES = MACHINESIDES_RENDERTYPE.asProperty;
    public static final PropertyInteger FILL_VALUE = PropertyInteger.create("fill_value", 0, 33);

    public enum EnumTieredMachineType implements IStringSerializable
    {
        STORAGE_MODULE(0, "energy_storage", TileEntityEnergyStorageModule::new, "tile.energy_storage_module_tier1.description", "tile.machine.1"),
        ELECTRIC_FURNACE(1, "electric_furnace", TileEntityElectricFurnace::new, "tile.electric_furnace_tier1.description", "tile.machine.2"),
        STORAGE_CLUSTER(2, "cluster_storage", TileEntityEnergyStorageModule::new, "tile.energy_storage_module_tier2.description", "tile.machine.8"),
        ARC_FURNACE(3, "arc_furnace", TileEntityElectricFurnace::new, "tile.electric_furnace_tier2.description", "tile.machine.7");

        private final int meta;
        private final String name;
        private TileConstructor tile;
        private final String shiftDescriptionKey;
        private final String blockName;

        EnumTieredMachineType(int meta, String name, TileConstructor tc, String key, String blockName)
        {
            this.meta = meta;
            this.name = name;
            this.tile = tc;
            this.shiftDescriptionKey = key;
            this.blockName = blockName;
        }

        public int getMetadata()
        {
            return this.meta * 4;
        }

        private final static EnumTieredMachineType[] values = values();
        public static EnumTieredMachineType byMeta(int meta)
        {
            return values[meta % values.length];
        }
        
        public static EnumTieredMachineType getByMetadata(int metadata)
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
            int tier = this.meta / 2 + 1;
            return this.tile.create(tier);
        }
        
        @FunctionalInterface
        private static interface TileConstructor
        {
              TileEntity create(int tier);
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

    public BlockMachineTiered(String assetName)
    {
        super(assetName);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        int meta = getMetaFromState(state);
        EnumTieredMachineType type = EnumTieredMachineType.getByMetadata(meta);
        TileEntity tile = type.tileConstructor();
        tile.setWorld(world);
        return tile;
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (EnumTieredMachineType type : EnumTieredMachineType.values)
            list.add(new ItemStack(this, 1, type.getMetadata()));
    }

    @Override
    public String getShiftDescription(int meta)
    {
        EnumTieredMachineType type = EnumTieredMachineType.getByMetadata(meta);
        return type.getShiftDescription();
    }

    @Override
    public String getUnlocalizedName(int meta)
    {
        EnumTieredMachineType type = EnumTieredMachineType.getByMetadata(meta);
        return type.getUnlocalizedName();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta % 4);
        EnumTieredMachineType type = EnumTieredMachineType.getByMetadata(meta);
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex() + ((EnumTieredMachineType) state.getValue(TYPE)).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, TYPE, FILL_VALUE, SIDES);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        state = IMachineSides.addPropertyForTile(state, tile, MACHINESIDES_RENDERTYPE, SIDES);

        if (!(tile instanceof TileEntityEnergyStorageModule))
        {
            return state.withProperty(FILL_VALUE, 0);
        }
        int energyLevel = ((TileEntityEnergyStorageModule) tile).scaledEnergyLevel;
        if (state.getValue(TYPE) == EnumTieredMachineType.STORAGE_CLUSTER) energyLevel += 17;
        return state.withProperty(FILL_VALUE, energyLevel);
    }
}
