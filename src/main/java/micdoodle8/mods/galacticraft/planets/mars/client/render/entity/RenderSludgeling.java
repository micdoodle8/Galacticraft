package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelSludgeling;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySludgeling;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderSludgeling extends RenderLiving<EntitySludgeling>
{
    private static final ResourceLocation sludgelingTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/model/sludgeling.png");

    public RenderSludgeling(RenderManager renderManager)
    {
        super(renderManager, new ModelSludgeling(), 0.3F);
    }

    @Override
    protected float getDeathMaxRotation(EntitySludgeling par1EntityLiving)
    {
        return 180.0F;
    }

    @Override
    public void doRender(EntitySludgeling sludgeling, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRender(sludgeling, par2, par4, par6, par8, par9);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySludgeling entity)
    {
        return sludgelingTexture;
    }
}
