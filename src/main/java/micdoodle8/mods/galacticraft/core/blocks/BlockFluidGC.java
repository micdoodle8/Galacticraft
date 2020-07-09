//package micdoodle8.mods.galacticraft.core.blocks;
//
//import micdoodle8.mods.galacticraft.api.vector.Vector3;
//import micdoodle8.mods.galacticraft.core.fluid.GCFluids;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
//import net.minecraft.block.BlockLiquid;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.material.Material;
//import net.minecraft.client.entity.player.ClientPlayerEntity;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.util.Direction;
//import net.minecraft.util.SoundEvents;
//import net.minecraft.util.Hand;
//import net.minecraft.util.SoundCategory;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.IBlockReader;
//import net.minecraft.world.World;
//import net.minecraftforge.common.property.IExtendedBlockState;
//import net.minecraftforge.fluids.BlockFluidBase;
//import net.minecraftforge.fluids.BlockFluidClassic;
//import net.minecraftforge.fluids.Fluid;
//import net.minecraftforge.fluids.IFluidBlock;
//
//import java.util.Random;
//
//import javax.annotation.Nullable;
//
//public class BlockFluidGC extends BlockFluidClassic
//{
//    private final String fluidName;
//    private final Fluid fluid;
//
//    public BlockFluidGC(Fluid fluid, String assetName)
//    {
//        super(fluid, (assetName.startsWith("oil") || assetName.startsWith("fuel")) ? GCFluids.materialOil : Material.WATER);
//        this.fluidName = assetName;
//        this.fluid = fluid;
//
//        if (assetName.startsWith("oil"))
//        {
//            this.needsRandomTick = true;
//        }
//
//        this.setUnlocalizedName(assetName);
//    }
//
//    @Override
//    @Nullable
//    public Boolean isEntityInsideMaterial(IBlockReader world, BlockPos pos, BlockState state, Entity entity, double yToTest, Material material, boolean testingHead)
//    {
//        return true;
//    }
//
//    @Override
//    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult hit)
//    {
//        if (worldIn.isRemote && this.fluidName.startsWith("oil") && playerIn instanceof ClientPlayerEntity)
//        {
//            ClientProxyCore.playerClientHandler.onBuild(7, (ClientPlayerEntity) playerIn);
//        }
//
//        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, LogicalSide, hitX, hitY, hitZ);
//    }
//
//    @Override
//    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
//    {
//        super.animateTick(stateIn, worldIn, pos, rand);
//
//        if (this.fluidName.startsWith("oil") && rand.nextInt(1200) == 0)
//        {
//            worldIn.playSound(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, rand.nextFloat() * 0.25F + 0.75F, 0.00001F + rand.nextFloat() * 0.5F);
//        }
//        if (this.fluidName.equals("oil") && rand.nextInt(10) == 0)
//        {
//            BlockPos below = pos.down();
//            BlockState state = worldIn.getBlockState(below);
//            if (state.getBlock().isSideSolid(state, worldIn, below, Direction.UP) && !worldIn.getBlockState(pos.down(2)).getMaterial().blocksMovement())
//            {
//                this.world.addParticle("oilDrip", new Vector3(pos.getX() + rand.nextFloat(), pos.getY() - 1.05D, pos.getZ() + rand.nextFloat()), new Vector3(0, 0, 0), new Object[] {});
//            }
//        }
//    }
//
//    @Override
//    public boolean canDisplace(IBlockReader world, BlockPos pos)
//    {
//        if (world.getBlockState(pos).getMaterial().isLiquid())
//        {
//            return false;
//        }
//
//        return super.canDisplace(world, pos);
//    }
//
//    @Override
//    public boolean displaceIfPossible(World world, BlockPos pos)
//    {
//        if (world.getBlockState(pos).getMaterial().isLiquid())
//        {
//            return false;
//        }
//
//        return super.displaceIfPossible(world, pos);
//    }
//
//    @Override
//    public boolean isFlammable(IBlockReader world, BlockPos pos, Direction face)
//    {
//        if (this.fluidName.startsWith("fuel"))
//        {
//            ((World) world).createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 6.0F, true);
//            return true;
//        }
//        return (this.fluidName.startsWith("oil"));
//    }
//
//    @Override
//    public BlockState getExtendedState(BlockState oldState, IBlockReader world, BlockPos pos)
//    {
//        IExtendedBlockState state = (IExtendedBlockState)oldState;
//        state = state.with(FLOW_DIRECTION, (float)getFlowDirection(state, world, pos));
//        BlockState[][] upBlockState = new BlockState[3][3];
//        float[][] height = new float[3][3];
//        float[][] corner = new float[2][2];
//        upBlockState[1][1] = world.getBlockState(pos.down(this.densityDir));
//        height[1][1] = this.getFluidHeightForRender(world, pos, upBlockState[1][1]);
//
//        if (height[1][1] == 1)
//        {
//            for (int i = 0; i < 2; i++)
//            {
//                for (int j = 0; j < 2; j++)
//                {
//                    corner[i][j] = 1;
//                }
//            }
//        }
//        else
//        {
//            for (int i = 0; i < 3; i++)
//            {
//                for (int j = 0; j < 3; j++)
//                {
//                    if (i != 1 || j != 1)
//                    {
//                        upBlockState[i][j] = world.getBlockState(pos.add(i - 1, 0, j - 1).down(this.densityDir));
//                        height[i][j] = this.getFluidHeightForRender(world, pos.add(i - 1, 0, j - 1), upBlockState[i][j]);
//                    }
//                }
//            }
//            for (int i = 0; i < 2; i++)
//            {
//                for (int j = 0; j < 2; j++)
//                {
//                    corner[i][j] = this.getFluidHeightAverage(height[i][j], height[i][j + 1], height[i + 1][j], height[i + 1][j + 1]);
//                }
//            }
//            //check for downflow above corners
//            boolean n =  this.isFluid(upBlockState[0][1]);
//            boolean s =  this.isFluid(upBlockState[2][1]);
//            boolean w =  this.isFluid(upBlockState[1][0]);
//            boolean e =  this.isFluid(upBlockState[1][2]);
//            boolean nw = this.isFluid(upBlockState[0][0]);
//            boolean ne = this.isFluid(upBlockState[0][2]);
//            boolean sw = this.isFluid(upBlockState[2][0]);
//            boolean se = this.isFluid(upBlockState[2][2]);
//
//            if (nw || n || w)
//            {
//                corner[0][0] = 0.999F;
//            }
//            if (ne || n || e)
//            {
//                corner[0][1] = 0.999F;
//            }
//            if (sw || s || w)
//            {
//                corner[1][0] = 0.999F;
//            }
//            if (se || s || e)
//            {
//                corner[1][1] = 0.999F;
//            }
//        }
//        state = state.with(LEVEL_CORNERS[0], corner[0][0]);
//        state = state.with(LEVEL_CORNERS[1], corner[0][1]);
//        state = state.with(LEVEL_CORNERS[2], corner[1][1]);
//        state = state.with(LEVEL_CORNERS[3], corner[1][0]);
//        return state;
//    }
//
//    public static double getFlowDirection(BlockState state, IBlockReader world, BlockPos pos)
//    {
//        if (!state.getMaterial().isLiquid())
//        {
//            return -1000.0;
//        }
//        Vec3d vec = ((BlockFluidBase)state.getBlock()).getFlowVector(world, pos);
//        return vec.x == 0.0D && vec.z == 0.0D ? -1000.0D : Math.atan2(vec.z, vec.x) - Math.PI / 2D;
//    }
//
//    private boolean isFluid(BlockState state)
//    {
//        return state.getMaterial().isLiquid() || state.getBlock() instanceof IFluidBlock;
//    }
//
//    @Override
//    public float getFluidHeightForRender(IBlockReader world, BlockPos pos, BlockState up)
//    {
//        BlockState here = world.getBlockState(pos);
//
//        if (here.getBlock() == this)
//        {
//            if (up.getMaterial().isLiquid() || up.getBlock() instanceof IFluidBlock)
//            {
//                return 1;
//            }
//            if (this.getMetaFromState(here) == this.getMaxRenderHeightMeta())
//            {
//                return 0.875F;
//            }
//        }
//        if (here.getBlock() instanceof FlowingFluidBlock)
//        {
//            return Math.min(1 - BlockLiquid.getLiquidHeightPercent(here.getValue(BlockLiquid.LEVEL)), 14f / 16);
//        }
//        return !here.getMaterial().isSolid() && up.getBlock() == this ? 1 : this.getQuantaPercentage(world, pos) * 0.875F;
//    }
//}
