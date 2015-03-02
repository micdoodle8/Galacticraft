package micdoodle8.mods.galacticraft.core.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import java.util.Random;

public class BlockFluidGC extends BlockFluidClassic
{
    private IIcon stillIcon;
    private IIcon flowingIcon;
    private final String fluidName;
    private final Fluid fluid;

    public BlockFluidGC(Fluid fluid, String assetName)
    {
        super(fluid, Material.water);
        this.setRenderPass(1);
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

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2)
    {
        return par1 != 0 && par1 != 1 ? this.flowingIcon : this.stillIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.stillIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + this.fluidName + "_still");
        this.flowingIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + this.fluidName + "_flow");
        this.fluid.setStillIcon(this.stillIcon);
        this.fluid.setFlowingIcon(this.flowingIcon);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ)
    {
    	if (world.isRemote && this.fluidName.equals("oil") && entityPlayer instanceof EntityPlayerSP)
        	ClientProxyCore.playerClientHandler.onBuild(7, (EntityPlayerSP) entityPlayer);

    	return super.onBlockActivated(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);	
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        super.randomDisplayTick(world, x, y, z, rand);

        if (this.fluidName.equals("oil") && rand.nextInt(1200) == 0)
        {
            world.playSound(x + 0.5F, y + 0.5F, z + 0.5F, "liquid.lava", rand.nextFloat() * 0.25F + 0.75F, 0.00001F + rand.nextFloat() * 0.5F, false);
        }
    }

    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z)
    {
        if (world.getBlock(x, y, z).getMaterial().isLiquid())
        {
            return false;
        }

        return super.canDisplace(world, x, y, z);
    }

    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z)
    {
        if (world.getBlock(x, y, z).getMaterial().isLiquid())
        {
            return false;
        }

        return super.displaceIfPossible(world, x, y, z);
    }

    public IIcon getStillIcon()
    {
        return this.stillIcon;
    }

    public IIcon getFlowingIcon()
    {
        return this.flowingIcon;
    }
    
    @Override
    public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face) 
    {
    	if (!(world instanceof World)) return false;
    	if (OxygenUtil.noAtmosphericCombustion(((World) world).provider))
        {
        	if (!OxygenUtil.isAABBInBreathableAirBlock((World) world, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 2, z + 1)))
        		return false;
        }
        
    	if (this.fluidName.startsWith("fuel"))
    	{
    		((World) world).createExplosion(null, x, y, z, 6.0F, true);
    		return true;
    	}
    	return (this.fluidName.startsWith("oil"));
    }
}
