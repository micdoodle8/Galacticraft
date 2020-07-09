package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedWitch;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerHeldItemEvolvedWitch;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedWitch;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderEvolvedWitch extends MobRenderer<EntityEvolvedWitch, ModelEvolvedWitch>
{
    private static final ResourceLocation witchTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/evolved_witch.png");
    private boolean texSwitch;

    public RenderEvolvedWitch(EntityRendererManager manager)
    {
        super(manager, new ModelEvolvedWitch(), 0.5F);
        this.addLayer(new LayerHeldItemEvolvedWitch(this));
    }

    @Override
    public void doRender(EntityEvolvedWitch entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.getEntityModel().func_205074_a(entity.getHeldItemMainhand().isEmpty());
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (OverlaySensorGlasses.overrideMobTexture())
        {
            texSwitch = true;
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
            texSwitch = false;
            OverlaySensorGlasses.postRenderMobs();
        }
    }

    @Override
    protected void preRenderCallback(EntityEvolvedWitch entity, float partialTickTime)
    {
        float f1 = 0.9375F;
        GlStateManager.scalef(f1, f1, f1);
        if (texSwitch)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEvolvedWitch entity)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedWitch.witchTexture;
    }
}