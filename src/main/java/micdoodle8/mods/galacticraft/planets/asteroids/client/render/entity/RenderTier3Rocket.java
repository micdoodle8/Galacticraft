//package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;
//
//import com.google.common.base.Function;
//import com.google.common.collect.ImmutableList;
//
//import micdoodle8.mods.galacticraft.api.vector.Vector3;
//import micdoodle8.mods.galacticraft.core.client.model.OBJLoaderGC;
//import micdoodle8.mods.galacticraft.core.util.ClientUtil;
//import micdoodle8.mods.galacticraft.core.util.ColorUtil;
//import micdoodle8.mods.galacticraft.core.util.PlayerUtil;
//import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
//import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityTier3Rocket;
//import net.minecraft.client.Minecraft;
//import com.mojang.blaze3d.platform.GlStateManager;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.culling.ICamera;
//import net.minecraft.client.renderer.entity.EntityRenderer;
//import net.minecraft.client.renderer.entity.EntityRendererManager;
//import net.minecraft.client.renderer.texture.AtlasTexture;
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.client.model.IModel;
//import net.minecraftforge.client.model.ModelLoader;
//import net.minecraftforge.client.model.obj.OBJModel;
//
//import org.lwjgl.opengl.GL11;
//
//import java.io.IOException;
//
//@OnlyIn(Dist.CLIENT)
//public class RenderTier3Rocket extends EntityRenderer<EntityTier3Rocket>
//{
//    private static OBJModel.OBJBakedModel rocketModel;
//    private static OBJModel.OBJBakedModel coneModel;
//    private static OBJModel.OBJBakedModel cubeModel;
//
//    public RenderTier3Rocket(EntityRendererManager manager)
//    {
//        super(manager);
//        this.shadowSize = 2F;
//    }
//
//    public static void updateModels(ModelLoader modelLoader)
//    {
//        try
//        {
//            rocketModel = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "tier3rocket.obj"), ImmutableList.of("Boosters", "Rocket"));
//            coneModel = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "tier3rocket.obj"), ImmutableList.of("NoseCone"));
//            cubeModel = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "tier3rocket.obj"), ImmutableList.of("Cube"));
//        }
//        catch (IOException e)
//        {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    protected ResourceLocation getEntityTexture(EntityTier3Rocket entity)
//    {
//        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
//    }
//
//    @Override
//    public void doRender(EntityTier3Rocket entity, double x, double y, double z, float entityYaw, float partialTicks)
//    {
//        float pitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks + 180;
//        GlStateManager.disableRescaleNormal();
//        GlStateManager.pushMatrix();
//        GlStateManager.translatef((float) x, (float) y, (float) z);
//        GlStateManager.rotatef(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
//        GlStateManager.rotatef(-pitch, 0.0F, 0.0F, 1.0F);
//        GlStateManager.translatef(0.0F, entity.getRenderOffsetY(), 0.0F);
//        float rollAmplitude = entity.rollAmplitude / 3 - partialTicks;
//
//        if (rollAmplitude > 0.0F)
//        {
//            final float i = entity.getLaunched() ? (5 - MathHelper.floor(entity.timeUntilLaunch / 85)) / 10F : 0.3F;
//            GlStateManager.rotatef(MathHelper.sin(rollAmplitude) * rollAmplitude * i * partialTicks, 1.0F, 0.0F, 0.0F);
//            GlStateManager.rotatef(MathHelper.sin(rollAmplitude) * rollAmplitude * i * partialTicks, 1.0F, 0.0F, 1.0F);
//        }
//
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
//        GlStateManager.scalef(-1.0F, -1.0F, 1.0F);
//        GlStateManager.scalef(0.8F, 0.8F, 0.8F);
//        ClientUtil.drawBakedModel(rocketModel);
//
//        Vector3 teamColor = ClientUtil.updateTeamColor(PlayerUtil.getName(Minecraft.getInstance().player), true);
//
//        if (teamColor != null)
//        {
//            int color = ColorUtil.to32BitColor(255, (int) (teamColor.floatZ() * 255), (int) (teamColor.floatY() * 255), (int) (teamColor.floatX() * 255));
//            GlStateManager.disableTexture();
//            ClientUtil.drawBakedModelColored(coneModel, color);
//        }
//        else
//        {
//            ClientUtil.drawBakedModel(coneModel);
//            GlStateManager.disableTexture();
//        }
//
//        GlStateManager.disableLighting();
//
//        boolean red = Minecraft.getInstance().player.ticksExisted / 10 % 2 < 1;
//        int color = ColorUtil.to32BitColor(255, 0, red ? 0 : 255, red ? 255 : 0);
//        ClientUtil.drawBakedModelColored(cubeModel, color);
//
//        GlStateManager.enableTexture();
//        GlStateManager.enableLighting();
//
//        GlStateManager.color3f(1F, 1F, 1F);
//        GlStateManager.popMatrix();
//        RenderHelper.enableStandardItemLighting();
//    }
//
//    @Override
//    public boolean shouldRender(EntityTier3Rocket rocket, ICamera camera, double camX, double camY, double camZ)
//    {
//        AxisAlignedBB axisalignedbb = rocket.getBoundingBox().grow(0.5D, 0, 0.5D);
//        return rocket.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
//    }
//}
