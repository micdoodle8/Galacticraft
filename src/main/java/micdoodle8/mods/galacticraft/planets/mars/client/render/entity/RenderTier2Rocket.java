//package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//import com.mojang.blaze3d.platform.GlStateManager;
//import micdoodle8.mods.galacticraft.core.util.ClientUtil;
//import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
//import micdoodle8.mods.galacticraft.planets.mars.client.render.item.ItemModelRocketT2;
//import micdoodle8.mods.galacticraft.planets.mars.entities.EntityTier2Rocket;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.IRenderTypeBuffer;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.Vector3f;
//import net.minecraft.client.renderer.culling.ClippingHelperImpl;
//import net.minecraft.client.renderer.entity.EntityRenderer;
//import net.minecraft.client.renderer.entity.EntityRendererManager;
//import net.minecraft.client.renderer.model.ModelResourceLocation;
//import net.minecraft.client.renderer.texture.AtlasTexture;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.MathHelper;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import org.lwjgl.opengl.GL11;
//
//@OnlyIn(Dist.CLIENT)
//public class RenderTier2Rocket extends EntityRenderer<EntityTier2Rocket>
//{
//    private ItemModelRocketT2 rocketModel;
//
//    public RenderTier2Rocket(EntityRendererManager manager)
//    {
//        super(manager);
//        this.shadowSize = 2F;
//    }
//
//    private void updateModel()
//    {
//        if (this.rocketModel == null)
//        {
//            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "rocket_t2", "inventory");
//            this.rocketModel = (ItemModelRocketT2) Minecraft.getInstance().getItemRenderer().getItemModelMesher().getModelManager().getModel(modelResourceLocation);
//        }
//    }
//
////    @Override
////    protected ResourceLocation getEntityTexture(EntityTier2Rocket entity)
////    {
////        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
////    }
//
//
//    @Override
//    public ResourceLocation getEntityTexture(EntityTier2Rocket entity)
//    {
//        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
//    }
//
//    @Override
//    public void render(EntityTier2Rocket entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
//    {
//        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks + 180;
//        float rollAmplitude = entity.rollAmplitude / 3 - partialTicks;
//        GlStateManager.disableRescaleNormal();
//        matrixStackIn.push();
//        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - entityYaw));
//        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-pitch));
//        matrixStackIn.translate(0.0F, entity.getRenderOffsetY(), 0.0F);
//
//        if (rollAmplitude > 0.0F)
//        {
//            final float i = entity.getLaunched() ? (5 - MathHelper.floor(entity.timeUntilLaunch / 85)) / 10F : 0.3F;
//            GlStateManager.rotatef(MathHelper.sin(rollAmplitude) * rollAmplitude * i * partialTicks, 1.0F, 0.0F, 0.0F);
//            GlStateManager.rotatef(MathHelper.sin(rollAmplitude) * rollAmplitude * i * partialTicks, 1.0F, 0.0F, 1.0F);
//        }
//
//        this.updateModel();
////        this.bindEntityTexture(entity);
//
//        if (Minecraft.isAmbientOcclusionEnabled())
//        {
//            GlStateManager.shadeModel(GL11.GL_SMOOTH);
//        }
//        else
//        {
//            GlStateManager.shadeModel(GL11.GL_FLAT);
//        }
//
//        GlStateManager.scalef(-1.0F, -1.0F, 1.0F);
//        GlStateManager.scalef(0.8F, 0.8F, 0.8F);
//        ClientUtil.drawBakedModel(this.rocketModel);
//        GlStateManager.popMatrix();
//        RenderHelper.enableStandardItemLighting();
//    }
//
////    @Override
////    public boolean shouldRender(EntityTier2Rocket rocket, ICamera camera, double camX, double camY, double camZ)
////    {
////        AxisAlignedBB axisalignedbb = rocket.getBoundingBox().grow(0.6D, 1D, 0.6D);
////        return rocket.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
////    }
//
//
//    @Override
//    public boolean shouldRender(EntityTier2Rocket rocket, ClippingHelperImpl camera, double camX, double camY, double camZ)
//    {
//        AxisAlignedBB axisalignedbb = rocket.getBoundingBox().grow(0.6D, 1D, 0.6D);
//        return rocket.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
//    }
//}
