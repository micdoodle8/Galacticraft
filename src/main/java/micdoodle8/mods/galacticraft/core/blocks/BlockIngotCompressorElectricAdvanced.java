package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties;
import micdoodle8.mods.galacticraft.core.tile.TileEntityElectricIngotCompressor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

/**
 * A block for advanced types of POWERED Galacticraft machine
 * with a base building purpose - e.g. Advanced Compressor
 * <p>
 * Note: this DOES implement IMachineSides, in comparison with BlockMachine3 which does not
 */
public class BlockIngotCompressorElectricAdvanced extends BlockMachineBase
{
    //    public static final EnumProperty<EnumMachineAdvancedType> TYPE = EnumProperty.create("type", EnumMachineAdvancedType.class);
    public static IMachineSidesProperties MACHINESIDES_RENDERTYPE = IMachineSidesProperties.TWOFACES_HORIZ;
    public static final EnumProperty SIDES = MACHINESIDES_RENDERTYPE.asProperty;

    public BlockIngotCompressorElectricAdvanced(Properties builder)
    {
        super(builder);
    }

//    @Override
//    protected void initialiseTypes()
//    {
//        this.types = EnumMachineAdvancedType.values;
//        this.typeBase = EnumMachineAdvancedType.values[0];
//    }

//    @Override
//    public BlockState getStateFromMeta(int meta)
//    {
//        Direction enumfacing = Direction.byHorizontalIndex(meta % 4);
//        EnumMachineAdvancedType type = (EnumMachineAdvancedType) typeBase.fromMetadata(meta);
//        return this.getDefaultState().with(FACING, enumfacing).with(TYPE, type);
//    }


    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TileEntityElectricIngotCompressor.TileEntityElectricIngotCompressorT2();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, SIDES);
    }

//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        TileEntity tile = worldIn.getTileEntity(pos);
//        return IMachineSides.addPropertyForTile(state, tile, MACHINESIDES_RENDERTYPE, SIDES);
//    }
}
