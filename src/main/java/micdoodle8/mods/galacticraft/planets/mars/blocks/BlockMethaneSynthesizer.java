package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.blocks.BlockMachineBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class BlockMethaneSynthesizer extends BlockMachineBase
{
//        GAS_LIQUEFIER(0, "gas_liquefier", TileEntityGasLiquefier::new, "tile.gas_liquefier.description", "tile.mars_machine.4"),
//        METHANE_SYNTHESIZER(4, "methane_synthesizer", TileEntityMethaneSynthesizer::new, "tile.methane_synthesizer.description", "tile.mars_machine.5"),
//        ELECTROLYZER(8, "electrolyzer", TileEntityElectrolyzer::new, "tile.electrolyzer.description", "tile.mars_machine.6");

    public BlockMethaneSynthesizer(Properties builder)
    {
        super(builder);
    }

    /**
     * Called when the block is right clicked by the player
     */
    @Override
    public ActionResultType onMachineActivated(World worldIn, BlockPos pos, BlockState state, PlayerEntity playerIn, Hand hand, ItemStack heldItem, BlockRayTraceResult hit)
    {
//        playerIn.openGui(GalacticraftPlanets.instance, GuiIdsPlanets.MACHINE_MARS, worldIn, pos.getX(), pos.getY(), pos.getZ()); TODO guis
        return ActionResultType.SUCCESS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }
}
