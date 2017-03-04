package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GCFluids;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import java.util.Random;

public class BlockFluidGC extends BlockFluidClassic
{
    private final String fluidName;
    private final Fluid fluid;

    public BlockFluidGC(Fluid fluid, String assetName)
    {
        super(fluid, (assetName.startsWith("oil") || assetName.startsWith("fuel")) ? GCFluids.materialOil : Material.WATER);
        this.fluidName = assetName;
        this.fluid = fluid;

        if (assetName.startsWith("oil"))
        {
            this.needsRandomTick = true;
        }

        this.setUnlocalizedName(assetName);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote && this.fluidName.startsWith("oil") && playerIn instanceof EntityPlayerSP)
        {
            ClientProxyCore.playerClientHandler.onBuild(7, (EntityPlayerSP) playerIn);
        }

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        super.randomDisplayTick(stateIn, worldIn, pos, rand);

        if (this.fluidName.startsWith("oil") && rand.nextInt(1200) == 0)
        {
            worldIn.playSound(null, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.BLOCK_LAVA_AMBIENT, SoundCategory.BLOCKS, rand.nextFloat() * 0.25F + 0.75F, 0.00001F + rand.nextFloat() * 0.5F);
        }
        if (this.fluidName.equals("oil") && rand.nextInt(10) == 0)
        {
            BlockPos below = pos.down();
            IBlockState state = worldIn.getBlockState(below);
            if (state.getBlock().isSideSolid(state, worldIn, below, EnumFacing.UP) && !worldIn.getBlockState(pos.down(2)).getBlock().getMaterial(worldIn.getBlockState(pos)).blocksMovement())
            {
                GalacticraftCore.proxy.spawnParticle("oilDrip", new Vector3(pos.getX() + rand.nextFloat(), pos.getY() - 1.05D, pos.getZ() + rand.nextFloat()), new Vector3(0, 0, 0), new Object[] {});
            }
        }
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos pos)
    {
        if (world.getBlockState(pos).getBlock().getMaterial(world.getBlockState(pos)).isLiquid())
        {
            return false;
        }

        return super.canDisplace(world, pos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos pos)
    {
        if (world.getBlockState(pos).getBlock().getMaterial(world.getBlockState(pos)).isLiquid())
        {
            return false;
        }

        return super.displaceIfPossible(world, pos);
    }

    @Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        if (this.fluidName.startsWith("fuel"))
        {
            ((World) world).createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 6.0F, true);
            return true;
        }
        return (this.fluidName.startsWith("oil"));
    }
}
