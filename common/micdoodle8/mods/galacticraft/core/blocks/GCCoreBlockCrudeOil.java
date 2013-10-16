package micdoodle8.mods.galacticraft.core.blocks;

import java.util.Random;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GCCoreBlockCrudeOil extends BlockFluidClassic
{
    @SideOnly(Side.CLIENT)
    Icon stillIcon;
    @SideOnly(Side.CLIENT)
    Icon flowingIcon;

    public GCCoreBlockCrudeOil(int id, String assetName)
    {
        super(id, GalacticraftCore.fluidOil, GCCoreBlocks.crudeOil);
        this.setQuantaPerBlock(3);
        this.setRenderPass(1);
        this.needsRandomTick = true;
        this.setTextureName(GalacticraftCore.TEXTURE_PREFIX + assetName);
        this.setUnlocalizedName(assetName);
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
        this.stillIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "oil");
        this.flowingIcon = par1IconRegister.registerIcon(GalacticraftCore.TEXTURE_PREFIX + "oil_flow");
        GalacticraftCore.fluidOil.setStillIcon(this.stillIcon);
        GalacticraftCore.fluidOil.setFlowingIcon(this.flowingIcon);
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
