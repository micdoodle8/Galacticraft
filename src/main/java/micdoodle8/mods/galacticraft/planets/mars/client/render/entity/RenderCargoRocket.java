//package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;
//
//import com.mojang.blaze3d.platform.GlStateManager;
//import micdoodle8.mods.galacticraft.core.util.ClientUtil;
//import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
//import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.ItemModelCargoRocket;
//import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.culling.ICamera;
//import net.minecraft.client.renderer.entity.EntityRenderer;
//import net.minecraft.client.renderer.entity.EntityRendererManager;
//import net.minecraft.client.renderer.model.ModelResourceLocation;
//import net.minecraft.client.renderer.texture.AtlasTexture;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import org.lwjgl.opengl.GL11;
//
//@OnlyIn(Dist.CLIENT)
//public class RenderCargoRocket extends EntityRenderer<EntityCargoRocket>
//{
//    private ItemModelCargoRocket rocketModel;
//
//    public RenderCargoRocket(EntityRendererManager renderManager)
//    {
//        super(renderManager);
//        this.shadowSize = 0.5F;
//    }
//
//    private void updateModel()
//    {
//        if (this.rocketModel == null)
//        {
//            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "rocket_cargo", "inventory");
//            this.rocketModel = (ItemModelCargoRocket) Minecraft.getInstance().getItemRenderer().getItemModelMesher().getModelManager().getModel(modelResourceLocation);
//        }
//    }
//
//    @Override
//    protected ResourceLocation getEntityTexture(EntityCargoRocket entity)
//    {
//        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
//    }
//
//    @Override
//    public void doRender(EntityCargoRocket entity, double x, double y, double z, float entityYaw, float partialTicks)
//    {
//        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
//        GlStateManager.disableRescaleNormal();
//        GlStateManager.pushMatrix();
//        GlStateManager.translatef((float) x, (float) y + entity.getRenderOffsetY(), (float) z);
//        GlStateManager.scalef(0.4F, 0.4F, 0.4F);
//        GlStateManager.rotatef(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
//        GlStateManager.rotatef(-pitch, 0.0F, 0.0F, 1.0F);
//        this.bindEntityTexture(entity);
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
//        this.updateModel();
//        ClientUtil.drawBakedModel(this.rocketModel);
//        GlStateManager.popMatrix();
//        RenderHelper.enableStandardItemLighting();
//    }
//
//    @Override
//    public boolean shouldRender(EntityCargoRocket rocket, ICamera camera, double camX, double camY, double camZ)
//    {
//        AxisAlignedBB axisalignedbb = rocket.getBoundingBox();
//        return rocket.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
//    }
//}
