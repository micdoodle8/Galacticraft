package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedSpider;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderEvolvedSpider extends RenderLiving<EntityEvolvedSpider>
{
    private static final ResourceLocation spiderTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/spider.png");
    private boolean texSwitch;

    public RenderEvolvedSpider(RenderManager manager)
    {
        super(manager, new ModelEvolvedSpider(), 1.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEvolvedSpider par1Entity)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedSpider.spiderTexture;
    }

    @Override
    protected void preRenderCallback(EntityEvolvedSpider par1EntityLiving, float par2)
    {
        GL11.glScalef(1.2F, 1.2F, 1.2F);
        if (texSwitch)
        {
            GL11.glTranslatef(0.0F, -0.03F, 0.0F);
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    public void doRender(EntityEvolvedSpider entity, double par2, double par4, double par6, float par8, float par9)
    {
        texSwitch = false;
        super.doRender(entity, par2, par4, par6, par8, par9);
        if (OverlaySensorGlasses.overrideMobTexture())
        {
            texSwitch = true;
            super.doRender(entity, par2, par4, par6, par8, par9);
        }
    }

    @Override
    protected float getDeathMaxRotation(EntityEvolvedSpider par1EntityLiving)
    {
        return 180.0F;
    }
}
