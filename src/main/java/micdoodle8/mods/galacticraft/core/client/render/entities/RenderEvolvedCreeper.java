package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedCreeper;
import micdoodle8.mods.galacticraft.core.client.render.entities.layer.LayerEvolvedCreeperCharge;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedCreeper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.monster.CreeperEntity;
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
    protected void preRenderCallback(EntityEvolvedCreeper entity, MatrixStack matrixStackIn, float partialTickTime)
    {
        float f = entity.getCreeperFlashIntensity(partialTickTime);
        float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = f * f;
        f = f * f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        matrixStackIn.scale(0.2F + f2, 0.2F + f3, 0.2F + f2);

        if (this.texSwitch)
        {
            matrixStackIn.translate(0.0F, -0.03F, 0.0F);
            OverlaySensorGlasses.preRenderMobs();
        }
        matrixStackIn.translate(0.0F, 0.125F, 0.0F);
    }

    @Override
    protected float getOverlayProgress(EntityEvolvedCreeper livingEntityIn, float partialTicks) {
        float f = livingEntityIn.getCreeperFlashIntensity(partialTicks);
        return (int)(f * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
    }


//    @Override
//    protected int getColorMultiplier(EntityEvolvedCreeper entity, float lightBrightness, float partialTicks)
//    {
//        float f = entity.getCreeperFlashIntensity(partialTicks);
//
//        if ((int) (f * 10.0F) % 2 == 0)
//        {
//            return 0;
//        }
//        else
//        {
//            int i = (int) (f * 0.2F * 255.0F);
//            i = MathHelper.clamp(i, 0, 255);
//            return i << 24 | 16777215;
//        }
//    }

    @Override
    public ResourceLocation getEntityTexture(EntityEvolvedCreeper entity)
    {
        return this.texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedCreeper.creeperTexture;
    }

    @Override
    public void render(EntityEvolvedCreeper entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if (OverlaySensorGlasses.overrideMobTexture())
        {
            texSwitch = true;
            super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            texSwitch = false;
            OverlaySensorGlasses.postRenderMobs();
        }
    }
}
