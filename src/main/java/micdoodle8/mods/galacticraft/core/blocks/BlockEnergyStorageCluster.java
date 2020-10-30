package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.tile.IMachineSidesProperties;
import micdoodle8.mods.galacticraft.core.tile.TileEntityEnergyStorageModule;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockEnergyStorageCluster extends BlockMachineBase
{
    //    public static final EnumProperty<EnumTieredMachineType> TYPE = EnumProperty.create("type", EnumTieredMachineType.class);
    public static IMachineSidesProperties MACHINESIDES_RENDERTYPE = IMachineSidesProperties.TWOFACES_HORIZ;
    public static final EnumProperty SIDES = MACHINESIDES_RENDERTYPE.asProperty;
    public static final IntegerProperty FILL_VALUE = IntegerProperty.create("fill_value", 0, 33);

    public BlockEnergyStorageCluster(Properties builder)
    {
        super(builder);
    }

//    @Override
//    protected void initialiseTypes()
//    {
//        this.types = EnumTieredMachineType.values;
//        this.typeBase = EnumTieredMachineType.values[0];
//    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
//        TileEntity tile = super.createTileEntity(state, world);
//        tile.setWorld(world); TODO Needed?
        return new TileEntityEnergyStorageModule.TileEntityEnergyStorageModuleT2();
    }

    @Override
    public boolean hasTileEntity(BlockState state)
    {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.getDefaultState().with(FACING, context.getPlayer().getHorizontalFacing());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, FILL_VALUE, SIDES);
    }

//    @Override
//    public BlockState getActualState(BlockState state, IBlockReader worldIn, BlockPos pos)
//    {
//        TileEntity tile = worldIn.getTileEntity(pos);
//        state = IMachineSides.addPropertyForTile(state, tile, MACHINESIDES_RENDERTYPE, SIDES);
//
//        if (!(tile instanceof TileEntityEnergyStorageModule))
//        {
//            return state.with(FILL_VALUE, 0);
//        }
//        int energyLevel = ((TileEntityEnergyStorageModule) tile).scaledEnergyLevel;
//        if (state.get(TYPE) == EnumTieredMachineType.STORAGE_CLUSTER) energyLevel += 17;
//        return state.with(FILL_VALUE, energyLevel);
//    }
}
