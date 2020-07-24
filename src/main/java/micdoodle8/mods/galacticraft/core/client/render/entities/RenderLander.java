//package micdoodle8.mods.galacticraft.core.client.render.entities;
//
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.client.model.ModelLander;
//import micdoodle8.mods.galacticraft.core.entities.EntityLander;
//import net.minecraft.client.renderer.culling.ICamera;
//import net.minecraft.client.renderer.entity.EntityRenderer;
//import net.minecraft.client.renderer.entity.EntityRendererManager;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//import org.lwjgl.opengl.GL11;
//
//@OnlyIn(Dist.CLIENT)
//public class RenderLander extends EntityRenderer<EntityLander>
//{
//    private static final ResourceLocation landerTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/lander.png");
//
//    protected ModelLander landerModel;
//
//    public RenderLander(EntityRendererManager manager)
//    {
//        super(manager);
//        this.shadowSize = 2F;
//        this.landerModel = new ModelLander();
//    }
//
//    @Override
//    protected ResourceLocation getEntityTexture(EntityLander par1Entity)
//    {
//        return RenderLander.landerTexture;
//    }
//
//    @Override
//    public void doRender(EntityLander lander, double par2, double par4, double par6, float par8, float par9)
//    {
//        RenderSystem.pushMatrix();
//        final float var24 = lander.prevRotationPitch + (lander.rotationPitch - lander.prevRotationPitch) * par9;
//        RenderSystem.translatef((float) par2, (float) par4 + 1.55F, (float) par6);
//        RenderSystem.rotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
//        RenderSystem.rotatef(-var24, 0.0F, 0.0F, 1.0F);
//
//        float f6 = lander.timeSinceHit - par9;
//        float f7 = lander.currentDamage - par9;
//
//        if (f7 < 0.0F)
//        {
//            f7 = 0.0F;
//        }
//
//        if (f6 > 0.0F)
//        {
//            RenderSystem.rotatef((float) Math.sin(f6) * 0.2F * f6 * f7 / 25.0F, 1.0F, 0.0F, 0.0F);
//        }
//
//        this.bindEntityTexture(lander);
//        RenderSystem.scalef(-1.0F, -1.0F, 1.0F);
//        this.landerModel.render(lander, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
//        RenderSystem.popMatrix();
//    }
//
//    @Override
//    public boolean shouldRender(EntityLander lander, ICamera camera, double camX, double camY, double camZ)
//    {
//        AxisAlignedBB axisalignedbb = lander.getBoundingBox().grow(2D, 1D, 2D);
//        return lander.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
//    }
//}
