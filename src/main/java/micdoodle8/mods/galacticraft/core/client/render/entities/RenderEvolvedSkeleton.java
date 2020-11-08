package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedSkeleton;
import micdoodle8.mods.galacticraft.core.entities.EntityEvolvedSkeleton;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.SkeletonModel;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderEvolvedSkeleton extends BipedRenderer<EntityEvolvedSkeleton, ModelEvolvedSkeleton>
{
    private static final ResourceLocation skeletonTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/skeleton.png");
    private static final ResourceLocation powerTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/power.png");

    private final ModelEvolvedSkeleton model = new ModelEvolvedSkeleton(0.2F);
    private boolean texSwitch;

    public RenderEvolvedSkeleton(EntityRendererManager manager)
    {
        super(manager, new ModelEvolvedSkeleton(), 0.6F);
        this.addLayer(new HeldItemLayer(this));
        this.addLayer(new BipedArmorLayer<>(this, new SkeletonModel<>(0.5F, true), new SkeletonModel<>(1.0F, true)));
    }

    @Override
    public ResourceLocation getEntityTexture(EntityEvolvedSkeleton par1Entity)
    {
        return texSwitch ? OverlaySensorGlasses.altTexture : RenderEvolvedSkeleton.skeletonTexture;
    }

    @Override
    protected void preRenderCallback(EntityEvolvedSkeleton entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime)
    {
        matrixStackIn.scale(1.2F, 1.2F, 1.2F);
        if (texSwitch)
        {
            OverlaySensorGlasses.preRenderMobs();
        }
    }

    @Override
    public void render(EntityEvolvedSkeleton entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
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
    protected void applyRotations(EntityEvolvedSkeleton skellie, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks)
    {
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        matrixStackIn.translate(0F, -skellie.getHeight() * 0.55F, 0F);
        matrixStackIn.rotate(new Quaternion(new Vector3f(skellie.getTumbleAxisX(), 0F, skellie.getTumbleAxisZ()), skellie.getTumbleAngle(partialTicks), true));
        matrixStackIn.translate(0F, skellie.getHeight() * 0.55F, 0F);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        super.applyRotations(skellie, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }
}
