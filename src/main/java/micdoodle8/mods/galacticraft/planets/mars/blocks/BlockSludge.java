package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySludgeling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

public class BlockSludge extends BlockFluidClassic
{
//    @SideOnly(Side.CLIENT)
//    IIcon stillIcon;
//    @SideOnly(Side.CLIENT)
//    IIcon flowingIcon;

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote)
        {
            if (entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).capabilities.isFlying || entityIn instanceof EntitySludgeling)
            {
                return;
            }

            int range = 5;
            List<?> l = worldIn.getEntitiesWithinAABB(EntitySludgeling.class, new AxisAlignedBB(pos.getX() - range, pos.getY() - range, pos.getZ() - range, pos.getX() + range, pos.getY() + range, pos.getZ() + range));

            if (l.size() < 3)
            {
                EntitySludgeling sludgeling = new EntitySludgeling(worldIn);
                sludgeling.setPosition(pos.getX() + worldIn.rand.nextInt(5) - 2, pos.getY(), pos.getZ() + worldIn.rand.nextInt(5) - 2);
                worldIn.spawnEntity(sludgeling);
            }
        }

        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
    }

    @Override
    @Nullable
    public Boolean isEntityInsideMaterial(IBlockAccess world, BlockPos pos, IBlockState state, Entity entity, double yToTest, Material material, boolean testingHead)
    {
        return true;
    }

    public BlockSludge(String assetName)
    {
        super(MarsModule.sludge, MarsModule.sludgeMaterial);
        this.setQuantaPerBlock(9);
        this.setLightLevel(1.0F);
        this.needsRandomTick = true;
        this.setUnlocalizedName(assetName);
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2)
    {
        return par1 != 0 && par1 != 1 ? this.flowingIcon : this.stillIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.stillIcon = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "sludge_still");
        this.flowingIcon = par1IconRegister.registerIcon(MarsModule.TEXTURE_PREFIX + "sludge_flow");
        MarsModule.SLUDGE.setStillIcon(this.stillIcon);
        MarsModule.SLUDGE.setFlowingIcon(this.flowingIcon);
    }*/

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos pos)
    {
        if (world.getBlockState(pos).getMaterial().isLiquid())
        {
            return false;
        }
        return super.canDisplace(world, pos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos pos)
    {
        if (world.getBlockState(pos).getMaterial().isLiquid())
        {
            return false;
        }
        return super.displaceIfPossible(world, pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        super.randomDisplayTick(stateIn, worldIn, pos, rand);

        if (rand.nextInt(1200) == 0)
        {
            worldIn.playSound(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, rand.nextFloat() * 0.25F + 0.75F, 0.00001F + rand.nextFloat() * 0.5F);
        }
        if (rand.nextInt(10) == 0)
        {
            BlockPos below = pos.down();
            IBlockState state = worldIn.getBlockState(below);
            if (state.getBlock().isSideSolid(state, worldIn, below, EnumFacing.UP) && !worldIn.getBlockState(pos.down(2)).getMaterial().blocksMovement())
            {
                GalacticraftPlanets.spawnParticle("bacterialDrip", new Vector3(pos.getX() + rand.nextFloat(), pos.getY() - 1.05D, pos.getZ() + rand.nextFloat()), new Vector3(0, 0, 0));
            }
        }
    }
}
