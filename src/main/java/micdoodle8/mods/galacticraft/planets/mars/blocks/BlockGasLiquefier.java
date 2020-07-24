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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class BlockGasLiquefier extends BlockMachineBase
{
    public BlockGasLiquefier(Properties builder)
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

//    @Override
//    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IWorldReader world, BlockPos pos, PlayerEntity player)
//    {
//        int metadata = this.getMetaFromState(state) & BlockMachineBase.METADATA_MASK;
//        return new ItemStack(this, 1, metadata);
//    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
//        final TileEntityGasLiquefier tileEntity = (TileEntityGasLiquefier) worldIn.getTileEntity(pos);
//
//        if (tileEntity.processTicks > 0)
//        {
//            final float x = pos.getX() + 0.5F;
//            final float y = pos.getY() + 0.8F + 0.05F * rand.nextInt(3);
//            final float z = pos.getZ() + 0.5F;
//
//            for (float i = -0.41F + 0.16F * rand.nextFloat(); i < 0.5F; i += 0.167F)
//            {
//                if (rand.nextInt(3) == 0)
//                {
//                    worldIn.addParticle("whiteSmokeTiny", new Vector3(x + i, y, z - 0.41F), new Vector3(0.0D, -0.015D, -0.0015D), new Object[] {});
//                }
//                if (rand.nextInt(3) == 0)
//                {
//                    worldIn.addParticle("whiteSmokeTiny", new Vector3(x + i, y, z + 0.537F), new Vector3(0.0D, -0.015D, 0.0015D), new Object[] {});
//                }
//                if (rand.nextInt(3) == 0)
//                {
//                    worldIn.addParticle("whiteSmokeTiny", new Vector3(x - 0.41F, y, z + i), new Vector3(-0.0015D, -0.015D, 0.0D), new Object[] {});
//                }
//                if (rand.nextInt(3) == 0)
//                {
//                    worldIn.addParticle("whiteSmokeTiny", new Vector3(x + 0.537F, y, z + i), new Vector3(0.0015D, -0.015D, 0.0D), new Object[] {});
//                }
//            }
//        } TODO Particles
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(FACING);
    }
}
