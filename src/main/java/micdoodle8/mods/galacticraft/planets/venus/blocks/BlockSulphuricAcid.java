package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.venus.VenusModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockSulphuricAcid extends BlockFluidClassic
{
    public BlockSulphuricAcid(String assetName)
    {
        super(VenusModule.sulphuricAcid, VenusModule.acidMaterial);
        this.setQuantaPerBlock(9);
        this.setLightLevel(0.1F);
        this.needsRandomTick = true;
        this.setUnlocalizedName(assetName);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos pos)
    {
        if (world.getBlockState(pos).getBlock().getMaterial().isLiquid())
        {
            return false;
        }
        return super.canDisplace(world, pos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos pos)
    {
        if (world.getBlockState(pos).getBlock().getMaterial().isLiquid())
        {
            return false;
        }
        return super.displaceIfPossible(world, pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.randomDisplayTick(worldIn, pos, state, rand);

        if (rand.nextInt(1200) == 0)
        {
            worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, "liquid.lava", rand.nextFloat() * 0.25F + 0.75F, 0.00001F + rand.nextFloat() * 0.5F, false);
        }
        if (rand.nextInt(10) == 0)
        {
            if (World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && !worldIn.getBlockState(pos.down(2)).getBlock().getMaterial().blocksMovement())
            {
                GalacticraftPlanets.spawnParticle("bacterialDrip", new Vector3(pos.getX() + rand.nextFloat(), pos.getY() - 1.05D, pos.getZ() + rand.nextFloat()), new Vector3(0, 0, 0));
            }
        }
    }
}
