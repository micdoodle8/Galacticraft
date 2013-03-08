package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.BlockStationary;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquid;

public class GCCoreBlockCrudeOilStationary extends BlockStationary implements ILiquid
{
	public GCCoreBlockCrudeOilStationary(int par1, Material par2Material) 
	{
		super(par1, par2Material);
		this.setHardness(100F);
		this.setLightOpacity(3);
		this.blockIndexInTexture = 237;
	}

	@Override
	public int stillLiquidId() 
	{
		return GCCoreBlocks.crudeOilStill.blockID;
	}

	@Override
	public boolean isMetaSensitive() 
	{
		return false;
	}

	@Override
	public int stillLiquidMeta() 
	{
		return 0;
	}

	@Override
    public int getRenderType()
    {
        return GalacticraftCore.proxy.getGCCrudeOilRenderID();
    }

    public static double getFlowDirection(IBlockAccess par0IBlockAccess, int par1, int par2, int par3, Material par4Material)
    {
    	return 0.0D;
    }

	@Override
    public String getTextureFile()
    {
    	return "/micdoodle8/mods/galacticraft/core/client/blocks/core.png";
    }

	@Override
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        int var6;

        if (this.blockMaterial == Material.water)
        {
            if (par5Random.nextInt(10) == 0)
            {
                var6 = par1World.getBlockMetadata(par2, par3, par4);

                if (var6 <= 0 || var6 >= 8)
                {
                    par1World.spawnParticle("suspended", (double)((float)par2 + par5Random.nextFloat()), (double)((float)par3 + par5Random.nextFloat()), (double)((float)par4 + par5Random.nextFloat()), 0.0D, 0.0D, 0.0D);
                }
            }

            for (var6 = 0; var6 < 0; ++var6)
            {
                int var7 = par5Random.nextInt(4);
                int var8 = par2;
                int var9 = par4;

                if (var7 == 0)
                {
                    var8 = par2 - 1;
                }

                if (var7 == 1)
                {
                    ++var8;
                }

                if (var7 == 2)
                {
                    var9 = par4 - 1;
                }

                if (var7 == 3)
                {
                    ++var9;
                }

                if (par1World.getBlockMaterial(var8, par3, var9) == Material.air && (par1World.getBlockMaterial(var8, par3 - 1, var9).blocksMovement() || par1World.getBlockMaterial(var8, par3 - 1, var9).isLiquid()))
                {
                    float var10 = 0.0625F;
                    double var11 = (double)((float)par2 + par5Random.nextFloat());
                    double var13 = (double)((float)par3 + par5Random.nextFloat());
                    double var15 = (double)((float)par4 + par5Random.nextFloat());

                    if (var7 == 0)
                    {
                        var11 = (double)((float)par2 - var10);
                    }

                    if (var7 == 1)
                    {
                        var11 = (double)((float)(par2 + 1) + var10);
                    }

                    if (var7 == 2)
                    {
                        var15 = (double)((float)par4 - var10);
                    }

                    if (var7 == 3)
                    {
                        var15 = (double)((float)(par4 + 1) + var10);
                    }

                    double var17 = 0.0D;
                    double var19 = 0.0D;

                    if (var7 == 0)
                    {
                        var17 = (double)(-var10);
                    }

                    if (var7 == 1)
                    {
                        var17 = (double)var10;
                    }

                    if (var7 == 2)
                    {
                        var19 = (double)(-var10);
                    }

                    if (var7 == 3)
                    {
                        var19 = (double)var10;
                    }

                    par1World.spawnParticle("splash", var11, var13, var15, var17, 0.0D, var19);
                }
            }
        }

        if (this.blockMaterial == Material.water && par5Random.nextInt(564) == 0)
        {
            var6 = par1World.getBlockMetadata(par2, par3, par4);

            if (var6 > 0 && var6 < 8)
            {
                par1World.playSound((double)((float)par2 + 0.5F), (double)((float)par3 + 0.5F), (double)((float)par4 + 0.5F), "liquid.lava", par5Random.nextFloat() * 0.25F + 0.75F, 0.00001F + par5Random.nextFloat() * 0.5F, false);
            }
        }

        double var21;
        double var23;
        double var22;

        if (this.blockMaterial == Material.lava && par1World.getBlockMaterial(par2, par3 + 1, par4) == Material.air && !par1World.isBlockOpaqueCube(par2, par3 + 1, par4))
        {
            if (par5Random.nextInt(100) == 0)
            {
                var21 = (double)((float)par2 + par5Random.nextFloat());
                var22 = (double)par3 + this.maxY;
                var23 = (double)((float)par4 + par5Random.nextFloat());
                par1World.spawnParticle("lava", var21, var22, var23, 0.0D, 0.0D, 0.0D);
                par1World.playSound(var21, var22, var23, "liquid.lavapop", 0.2F + par5Random.nextFloat() * 0.2F, 0.9F + par5Random.nextFloat() * 0.15F, false);
            }

            if (par5Random.nextInt(200) == 0)
            {
                par1World.playSound((double)par2, (double)par3, (double)par4, "liquid.lava", 0.2F + par5Random.nextFloat() * 0.2F, 0.9F + par5Random.nextFloat() * 0.15F, false);
            }
        }

        if (par5Random.nextInt(10) == 0 && par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !par1World.getBlockMaterial(par2, par3 - 2, par4).blocksMovement())
        {
            var21 = (double)((float)par2 + par5Random.nextFloat());
            var22 = (double)par3 - 1.05D;
            var23 = (double)((float)par4 + par5Random.nextFloat());

            if (this.blockMaterial == Material.water)
            {
                par1World.spawnParticle("dripWater", var21, var22, var23, 0.0D, 0.0D, 0.0D);
            }
            else
            {
                par1World.spawnParticle("dripLava", var21, var22, var23, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
