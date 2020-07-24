//package micdoodle8.mods.galacticraft.core.client.render.entities;
//
//import com.mojang.blaze3d.systems.RenderSystem;
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.client.model.ModelFlag;
//import micdoodle8.mods.galacticraft.core.entities.EntityFlag;
//import net.minecraft.client.renderer.culling.ICamera;
//import net.minecraft.client.renderer.entity.EntityRenderer;
//import net.minecraft.client.renderer.entity.EntityRendererManager;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//@OnlyIn(Dist.CLIENT)
//public class RenderFlag extends EntityRenderer<EntityFlag>
//{
//    public static ResourceLocation flagTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/flag.png");
//
//    protected ModelFlag modelFlag;
//
//    public RenderFlag(EntityRendererManager manager)
//    {
//        super(manager);
//        this.shadowSize = 1F;
//        this.modelFlag = new ModelFlag();
//    }
//
//    @Override
//    protected ResourceLocation getEntityTexture(EntityFlag entity)
//    {
//        return RenderFlag.flagTexture;
//    }
//
//    @Override
//    public void doRender(EntityFlag entity, double x, double y, double z, float entityYaw, float partialTicks)
//    {
//        RenderSystem.disableRescaleNormal();
//        RenderSystem.pushMatrix();
//        long seed = entity.getEntityId() * 493286711L;
//        seed = seed * seed * 4392167121L + seed * 98761L;
//        float seedX = (((seed >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
//        float seedY = (((seed >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
//        float seedZ = (((seed >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
//        RenderSystem.translatef(seedX, seedY + 1.5F, seedZ);
//        RenderSystem.translatef((float) x, (float) y, (float) z);
//        RenderSystem.rotatef(180.0F - entity.getFacingAngle(), 0.0F, 1.0F, 0.0F);
//        this.bindEntityTexture(entity);
//        RenderSystem.scalef(-1.0F, -1.0F, 1.0F);
//        this.modelFlag.render(entity, 0.0625F);
//        RenderSystem.popMatrix();
//    }
//
//    @Override
//    public boolean shouldRender(EntityFlag lander, ICamera camera, double camX, double camY, double camZ)
//    {
//        AxisAlignedBB axisalignedbb = lander.getBoundingBox().grow(1D, 2D, 1D);
//        return lander.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
//    }
//}
