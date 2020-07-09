//package micdoodle8.mods.galacticraft.planets.venus.blocks;
//
//import micdoodle8.mods.galacticraft.api.vector.Vector3;
//import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
//import micdoodle8.mods.galacticraft.planets.venus.VenusModule;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.material.Material;
//import net.minecraft.entity.Entity;
//import net.minecraft.util.SoundEvents;
//import net.minecraft.util.Direction;
//import net.minecraft.util.SoundCategory;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.IBlockAccess;
//import net.minecraft.world.World;
//import net.minecraftforge.fluids.BlockFluidClassic;
//import net.minecraftforge.fml.LogicalSide;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.util.Random;
//
//import javax.annotation.Nullable;
//
//public class BlockSulphuricAcid extends BlockFluidClassic
//{
//    public BlockSulphuricAcid(Properties builder)
//    {
//        super(VenusModule.sulphuricAcid, VenusModule.acidMaterial);
//        this.setQuantaPerBlock(9);
//        this.setLightLevel(0.1F);
//        this.needsRandomTick = true;
//        this.setUnlocalizedName(assetName);
//    }
//
//    @Override
//    @Nullable
//    public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos pos, BlockState state, Entity entity, double yToTest, Material material, boolean testingHead)
//    {
//        return true;
//    }
//
//    @Override
//    public boolean canDisplace(IBlockAccess world, BlockPos pos)
//    {
//        if (world.getBlockState(pos).getMaterial().isLiquid())
//        {
//            return false;
//        }
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
//        return super.displaceIfPossible(world, pos);
//    }
//
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
//    {
//        super.animateTick(stateIn, worldIn, pos, rand);
//
//        if (rand.nextInt(1200) == 0)
//        {
//            worldIn.playSound(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, rand.nextFloat() * 0.25F + 0.75F, 0.00001F + rand.nextFloat() * 0.5F);
//        }
//        if (rand.nextInt(10) == 0)
//        {
//            BlockPos below = pos.down();
//            BlockState state = worldIn.getBlockState(below);
//            if (state.getBlock().isSideSolid(state, worldIn, below, Direction.UP) && !worldIn.getBlockState(pos.down(2)).getMaterial().blocksMovement())
//            {
//                GalacticraftPlanets.addParticle("bacterialDrip", new Vector3(pos.getX() + rand.nextFloat(), pos.getY() - 1.05D, pos.getZ() + rand.nextFloat()), new Vector3(0, 0, 0));
//            }
//        }
//    }
//}
