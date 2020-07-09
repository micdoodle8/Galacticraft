package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerEvolvedCreeperCharge;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderEvolvedCreeper extends MobRenderer<EntityEvolvedCreeper, ModelEvolvedCreeper>
{
    private static final ResourceLocation creeperTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/creeper.png");
    private boolean texSwitch;

    public RenderEvolvedCreeper(EntityRendererManager manager)
    {
        super(manager, new ModelEvolvedCreeper(), 0.5F);
        this.addLayer(new LayerEvolvedCreeperCharge(this));
    }

    @Override
    protected void preRenderCallback(EntityEvolvedCreeper entity, float partialTicks)
    {
        float f = entity.getCreeperFlashIntensity(partialTicks);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        GlStateManager.scalef(0.2F + f2, 0.2F + f3, 0.2F + f2);

        if (this.texSwitch)
        {
            GlStateManager.translatef(0.0F, -0.03F, 0.0F);
            OverlaySensorGlasses.preRenderMobs();
        }
        GlStateManager.translatef(0.0F, 0.125F, 0.0F);
    }

    @Override
    protected int getColorMultiplier(EntityEvolvedCreeper entity, float lightBrightness, float partialTicks)
    {
        float f = entity.getCreeperFlashIntensity(partialTicks);

        if ((int) (f * 10.0F) % 2 == 0)
        {
            return 0;
        }
        else
        {
            int i = (int) (f * 0.2F * 255.0F);
            i = MathHelper.clamp(i, 0, 255);
            return i << 24 | 16777215;
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEvolvedCreeper entity)
    {
        return this.texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedCreeper.creeperTexture;
    }

    @Override
    public void doRender(EntityEvolvedCreeper entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (OverlaySensorGlasses.overrideMobTexture())
        {
            texSwitch = true;
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
            texSwitch = false;
            OverlaySensorGlasses.postRenderMobs();
        }
    }
}
