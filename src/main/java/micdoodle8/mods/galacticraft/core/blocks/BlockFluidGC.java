package micdoodle8.mods.galacticraft.core.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.block.BlockLiquid;
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
        super(fluid, (assetName.startsWith("oil") || assetName.startsWith("fuel")) ? GalacticraftCore.materialOil : Material.water);
        this.setRenderPass(1);
        this.fluidName = assetName;
        this.fluid = fluid;

        if (assetName.startsWith("oil"))
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
    	if (world.isRemote && this.fluidName.startsWith("oil") && entityPlayer instanceof EntityPlayerSP)
        	ClientProxyCore.playerClientHandler.onBuild(7, (EntityPlayerSP) entityPlayer);

    	return super.onBlockActivated(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);	
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        super.randomDisplayTick(world, x, y, z, rand);

        if (this.fluidName.startsWith("oil") && rand.nextInt(1200) == 0)
        {
            world.playSound(x + 0.5F, y + 0.5F, z + 0.5F, "liquid.lava", rand.nextFloat() * 0.25F + 0.75F, 0.00001F + rand.nextFloat() * 0.5F, false);
        }
		if (this.fluidName.equals("oil") && rand.nextInt(10) == 0)
		{
			if (World.doesBlockHaveSolidTopSurface(world, x, y - 1, z) && !world.getBlock(x, y - 2, z).getMaterial().blocksMovement())
			{
				GalacticraftCore.proxy.spawnParticle("oilDrip", new Vector3(x + rand.nextFloat(), y - 1.05D, z + rand.nextFloat()), new Vector3(0, 0, 0), new Object[] {});
			}
		}
    }

    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z)
    {
        if (world.getBlock(x, y, z) instanceof BlockLiquid)
        {
        	int meta = world.getBlockMetadata(x,  y,  z);
            return (meta > 1 || meta == -1);
        }

        return super.canDisplace(world, x, y, z);
    }

    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z)
    {
        if (world.getBlock(x, y, z) instanceof BlockLiquid)
        {
        	int meta = world.getBlockMetadata(x,  y,  z);
            if (meta > 1 || meta == -1) 
            	return super.displaceIfPossible(world, x, y, z);
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

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
//    	Block block = world.getBlock(x, y, z);
//    	if (block != this)
//    	{
//    		return !block.isOpaqueCube();
//    	}
//    	return side == 0 && this.minY > 0.0D ? true : (side == 1 && this.maxY < 1.0D ? true : (side == 2 && this.minZ > 0.0D ? true : (side == 3 && this.maxZ < 1.0D ? true : (side == 4 && this.minX > 0.0D ? true : (side == 5 && this.maxX < 1.0D ? true : !block.isOpaqueCube())))));    	
    	return super.shouldSideBeRendered(world, x, y, z, side);
	}
}
