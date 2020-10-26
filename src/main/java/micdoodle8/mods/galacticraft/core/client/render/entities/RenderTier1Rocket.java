package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.ModelRocketTier1;
import micdoodle8.mods.galacticraft.core.entities.EntityTier1Rocket;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderTier1Rocket extends EntityRenderer<EntityTier1Rocket>
{
    private final ResourceLocation spaceshipTexture;

    protected final EntityModel<EntityTier1Rocket> rocketModel = new ModelRocketTier1();

    public RenderTier1Rocket(EntityRendererManager manager)
    {
        this(manager, new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/rocket_t1.png"));
    }

    private RenderTier1Rocket(EntityRendererManager manager, ResourceLocation texture)
    {
        super(manager);
        this.spaceshipTexture = texture;
        this.shadowSize = 0.9F;
    }

    @Override
    public ResourceLocation getEntityTexture(EntityTier1Rocket entity) {
        return spaceshipTexture;
    }

//    @Override
//    public void doRender(EntityTier1Rocket entity, double par2, double par4, double par6, float par8, float par9)
//    {
//        GL11.glPushMatrix();
//        final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;
//        final float var25 = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * par9;
//
//        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
//        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
//        GL11.glRotatef(-var25, 0.0F, 1.0F, 0.0F);
//        GL11.glTranslatef(0.0F, entity.getRenderOffsetY(), 0.0F);
//        final float var28 = entity.rollAmplitude - par9;
//
//        if (var28 > 0.0F)
//        {
//            final float i = entity.getLaunched() ? (5 - MathHelper.floor(entity.timeUntilLaunch / 85)) / 10F : 0.3F;
//            GL11.glRotatef(MathHelper.sin(var28) * var28 * i * par9, 1.0F, 0.0F, 0.0F);
//            GL11.glRotatef(MathHelper.sin(var28) * var28 * i * par9, 1.0F, 0.0F, 1.0F);
//        }
//
//        this.bindEntityTexture(entity);
//        GL11.glScalef(-1.0F, -1.0F, 1.0F);
//        this.rocketModel.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
//
//        GL11.glPopMatrix();
//    }
//
//    @Override
//    public boolean shouldRender(EntityTier1Rocket rocket, ICamera camera, double camX, double camY, double camZ)
//    {
//        AxisAlignedBB axisalignedbb = rocket.getBoundingBox().grow(0.6D, 1D, 0.6D);
//
//        return rocket.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
//    }


    @Override
    public boolean shouldRender(EntityTier1Rocket livingEntityIn, ClippingHelperImpl camera, double camX, double camY, double camZ)
    {
        return true;
    }

    @Override
    public void render(EntityTier1Rocket entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        matrixStackIn.push();
        float pitch = entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks;
        float yaw = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.rocketModel.getRenderType(this.getEntityTexture(entityIn)));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-pitch));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-yaw));
        matrixStackIn.translate(0.0F, entityIn.getRenderOffsetY(), 0.0F);
        matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
        this.rocketModel.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
    }
}
