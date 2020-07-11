//package micdoodle8.mods.galacticraft.core.client.render.entities;
//
//import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
//import micdoodle8.mods.galacticraft.core.entities.EntityTier1Rocket;
//import net.minecraft.client.renderer.culling.ICamera;
//import net.minecraft.client.renderer.entity.EntityRenderer;
//import net.minecraft.client.renderer.entity.EntityRendererManager;
//import net.minecraft.client.renderer.entity.model.EntityModel;
//import net.minecraft.entity.Entity;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.MathHelper;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import org.lwjgl.opengl.GL11;
//
//@OnlyIn(Dist.CLIENT)
//public class RenderTier1Rocket extends EntityRenderer<EntityTier1Rocket>
//{
//    private final ResourceLocation spaceshipTexture;
//
//    protected EntityModel<EntityTier1Rocket> rocketModel;
//
//    public RenderTier1Rocket(EntityRendererManager manager, EntityModel<EntityTier1Rocket> rocketModel, String textureDomain, String texture)
//    {
//        this(manager, new ResourceLocation(textureDomain, "textures/model/" + texture + ".png"));
//        this.rocketModel = rocketModel;
//    }
//
//    private RenderTier1Rocket(EntityRendererManager manager, ResourceLocation texture)
//    {
//        super(manager);
//        this.spaceshipTexture = texture;
//        this.shadowSize = 0.9F;
//    }
//
//    protected ResourceLocation func_110779_a(Entity entity)
//    {
//        return this.spaceshipTexture;
//    }
//
//    @Override
//    protected ResourceLocation getEntityTexture(EntityTier1Rocket par1Entity)
//    {
//        return this.func_110779_a(par1Entity);
//    }
//
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
//}
