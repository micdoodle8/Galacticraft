package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelSludgeling;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntitySludgeling;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderSludgeling extends RenderLiving<EntitySludgeling>
{
    private static final ResourceLocation sludgelingTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/model/sludgeling.png");
    private boolean texSwitch;

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
    public void doRender(EntitySludgeling entity, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRender(entity, par2, par4, par6, par8, par9);
        if (OverlaySensorGlasses.overrideMobTexture())
        {
            texSwitch = true;
            super.doRender(entity, par2, par4, par6, par8, par9);
            texSwitch = false;
            OverlaySensorGlasses.postRenderMobs();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntitySludgeling entity)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : sludgelingTexture;
    }
    
    @Override
    protected void preRenderCallback(EntitySludgeling entity, float par2)
    {
        super.preRenderCallback(entity, par2);
        if (texSwitch)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }
}
