package micdoodle8.mods.galacticraft.core.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockFluidGC extends BlockFluidClassic
{
    /*private IIcon stillIcon;
    private IIcon flowingIcon;*/
    private final String fluidName;
    private final Fluid fluid;

    public BlockFluidGC(Fluid fluid, String assetName)
    {
        super(fluid, Material.water);
        //this.setRenderPass(1);
        this.fluidName = assetName;
        this.fluid = fluid;

        if (assetName.equals("oil"))
        {
            this.needsRandomTick = true;
        }
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftCore.galacticraftBlocksTab;
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2)
    {
        return par1 != 0 && par1 != 1 ? this.flowingIcon : this.stillIcon;
    }*/

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.stillIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + this.fluidName + "_still");
        this.flowingIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + this.fluidName + "_flow");
        this.fluid.setStillIcon(this.stillIcon);
        this.fluid.setFlowingIcon(this.flowingIcon);
    }*/

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	if (worldIn.isRemote && this.fluidName.equals("oil") && playerIn instanceof EntityPlayerSP)
        	ClientProxyCore.playerClientHandler.onBuild(7, (EntityPlayerSP) playerIn);

    	return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.randomDisplayTick(worldIn, pos, state, rand);

        if (this.fluidName.equals("oil") && rand.nextInt(1200) == 0)
        {
            worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, "liquid.lava", rand.nextFloat() * 0.25F + 0.75F, 0.00001F + rand.nextFloat() * 0.5F, false);
        }
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

    /*public IIcon getStillIcon()
    {
        return this.stillIcon;
    }

    public IIcon getFlowingIcon()
    {
        return this.flowingIcon;
    }*/
    
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
