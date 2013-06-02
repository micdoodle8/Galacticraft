package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.block.BlockStationary;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.liquids.ILiquid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockCrudeOilStationary extends BlockStationary implements ILiquid
{
    @SideOnly(Side.CLIENT)
    public Icon[] fluidIcons;

    public GCCoreBlockCrudeOilStationary(int par1, Material par2Material)
    {
        super(par1, par2Material);
        this.setHardness(100F);
        this.setLightOpacity(3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        return par1 != 0 && par1 != 1 ? this.fluidIcons[1] : this.fluidIcons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.fluidIcons = new Icon[] { par1IconRegister.registerIcon("galacticraftcore:oil"), par1IconRegister.registerIcon("galacticraftcore:oil_flow") };
    }

    @SideOnly(Side.CLIENT)
    public static Icon func_94424_b(String par0Str)
    {
        return par0Str == "galacticraftcore:oil" ? GCCoreBlocks.crudeOilMoving.fluidIcons[0] : par0Str == "galacticraftcore:oil_flow" ? GCCoreBlocks.crudeOilMoving.fluidIcons[1] : null;
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
                    // par1World.spawnParticle("suspended", par2 +
                    // par5Random.nextFloat(), par3 + par5Random.nextFloat(),
                    // par4 + par5Random.nextFloat(), 0.0D, 0.0D, 0.0D);
                }
            }

            for (var6 = 0; var6 < 0; ++var6)
            {
                final int var7 = par5Random.nextInt(4);
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
                    if (var7 == 0)
                    {
                    }

                    if (var7 == 1)
                    {
                    }

                    if (var7 == 2)
                    {
                    }

                    if (var7 == 3)
                    {
                    }

                    if (var7 == 0)
                    {
                    }

                    if (var7 == 1)
                    {
                    }

                    if (var7 == 2)
                    {
                    }

                    if (var7 == 3)
                    {
                    }
                }
            }
        }

        if (this.blockMaterial == Material.water && par5Random.nextInt(564) == 0)
        {
            var6 = par1World.getBlockMetadata(par2, par3, par4);

            if (var6 > 0 && var6 < 8)
            {
                par1World.playSound(par2 + 0.5F, par3 + 0.5F, par4 + 0.5F, "liquid.lava", par5Random.nextFloat() * 0.25F + 0.75F, 0.00001F + par5Random.nextFloat() * 0.5F, false);
            }
        }

        double var21;
        double var23;
        double var22;

        if (this.blockMaterial == Material.lava && par1World.getBlockMaterial(par2, par3 + 1, par4) == Material.air && !par1World.isBlockOpaqueCube(par2, par3 + 1, par4))
        {
            if (par5Random.nextInt(100) == 0)
            {
                var21 = par2 + par5Random.nextFloat();
                var22 = par3 + this.maxY;
                var23 = par4 + par5Random.nextFloat();
                // par1World.spawnParticle("lava", var21, var22, var23, 0.0D,
                // 0.0D, 0.0D);
                par1World.playSound(var21, var22, var23, "liquid.lavapop", 0.2F + par5Random.nextFloat() * 0.2F, 0.9F + par5Random.nextFloat() * 0.15F, false);
            }

            if (par5Random.nextInt(200) == 0)
            {
                par1World.playSound(par2, par3, par4, "liquid.lava", 0.2F + par5Random.nextFloat() * 0.2F, 0.9F + par5Random.nextFloat() * 0.15F, false);
            }
        }

        if (par5Random.nextInt(10) == 0 && par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !par1World.getBlockMaterial(par2, par3 - 2, par4).blocksMovement())
        {
            var21 = par2 + par5Random.nextFloat();
            var22 = par3 - 1.05D;
            var23 = par4 + par5Random.nextFloat();

            if (this.blockMaterial == Material.water)
            {
                // par1World.spawnParticle("dripWater", var21, var22, var23,
                // 0.0D, 0.0D, 0.0D);
            }
            else
            {
                // par1World.spawnParticle("dripLava", var21, var22, var23,
                // 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
