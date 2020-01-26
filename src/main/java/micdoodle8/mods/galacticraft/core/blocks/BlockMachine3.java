package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.tile.TileEntityPainter;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

/**
 * A block for several types of Galacticraft machine
 * with a base building purpose - e.g. Painter
 *
 */
public class BlockMachine3 extends BlockMachineBase
{
    public static final PropertyEnum<EnumMachineBuildingType> TYPE = PropertyEnum.create("type", EnumMachineBuildingType.class);

    public enum EnumMachineBuildingType implements IStringSerializable
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

        public int getMetadata()
        {
            return this.meta * 4;
        }

        private final static EnumMachineBuildingType[] values = values();
        public static EnumMachineBuildingType byMeta(int meta)
        {
            return values[meta % values.length];
        }
        
        public static EnumMachineBuildingType getByMetadata(int metadata)
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
            return this.tile.create();
        }

        @FunctionalInterface
        private static interface TileConstructor
        {
              TileEntity create();
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

    public BlockMachine3(String assetName)
    {
        super(assetName);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        int meta = getMetaFromState(state);
        EnumMachineBuildingType type = EnumMachineBuildingType.getByMetadata(meta);
        return type.tileConstructor();
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        for (EnumMachineBuildingType type : EnumMachineBuildingType.values)
            list.add(new ItemStack(this, 1, type.getMetadata()));
    }

    @Override
    public String getShiftDescription(int meta)
    {
        EnumMachineBuildingType type = EnumMachineBuildingType.getByMetadata(meta);
        return type.getShiftDescription();
    }

    @Override
    public String getUnlocalizedName(int meta)
    {
        EnumMachineBuildingType type = EnumMachineBuildingType.getByMetadata(meta);
        return type.getUnlocalizedName();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta % 4);
        EnumMachineBuildingType type = EnumMachineBuildingType.getByMetadata(meta);
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
