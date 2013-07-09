package micdoodle8.mods.galacticraft.mars.blocks;

import java.util.Random;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCMarsBlockSludge extends BlockFluidClassic
{
    @SideOnly(Side.CLIENT)
    Icon stillIcon;
    @SideOnly(Side.CLIENT)
    Icon flowingIcon;

    public GCMarsBlockSludge(int par1, Material par2Material)
    {
        super(par1, GalacticraftMars.SLUDGE, par2Material);
        this.setQuantaPerBlock(2);
        this.setRenderPass(1);
        this.setLightValue(1.0F);
        this.needsRandomTick = true;
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return GalacticraftMars.galacticraftMarsTab;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int par1, int par2)
    {
        return par1 == 0 ? this.stillIcon : this.flowingIcon;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.stillIcon = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "sludge");
        this.flowingIcon = par1IconRegister.registerIcon(GalacticraftMars.TEXTURE_PREFIX + "sludge_flow");
        GalacticraftMars.SLUDGE.setStillIcon(this.stillIcon);
        GalacticraftMars.SLUDGE.setFlowingIcon(this.flowingIcon);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand)
    {
        super.randomDisplayTick(world, x, y, z, rand);

        if (rand.nextInt(1200) == 0)
        {
            world.playSound(x + 0.5F, y + 0.5F, z + 0.5F, "liquid.lava", rand.nextFloat() * 0.25F + 0.75F, 0.00001F + rand.nextFloat() * 0.5F, false);
        }
    }
}
