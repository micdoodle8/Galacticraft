package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedSpider;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSpider;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderEvolvedSpider extends MobRenderer<EntityEvolvedSpider, ModelEvolvedSpider>
{
    private static final ResourceLocation spiderTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/spider.png");
    private boolean texSwitch;

    public RenderEvolvedSpider(EntityRendererManager manager)
    {
        super(manager, new ModelEvolvedSpider(), 1.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityEvolvedSpider spider)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedSpider.spiderTexture;
    }

    @Override
    protected void preRenderCallback(EntityEvolvedSpider spider, MatrixStack matStack, float partialTickTime)
    {
        matStack.scale(1.2F, 1.2F, 1.2F);
        if (texSwitch)
        {
            matStack.translate(0.0, -0.03, 0.0);
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    public void render(EntityEvolvedSpider entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
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

    @Override
    protected float getDeathMaxRotation(EntityEvolvedSpider spider)
    {
        return 180.0F;
    }
}
