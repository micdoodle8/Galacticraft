package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import com.mojang.blaze3d.platform.GLX;
import micdoodle8.mods.galacticraft.core.client.model.OBJLoaderGC;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelBalloonParachute;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityEntryPodVenus;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJModel;

import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class RenderEntryPodVenus extends EntityRenderer<EntityEntryPodVenus>
{
    private static OBJModel.OBJBakedModel modelEntryPod;
    private static OBJModel.OBJBakedModel modelFlame;
    protected ModelBalloonParachute parachuteModel = new ModelBalloonParachute();

    public RenderEntryPodVenus(EntityRendererManager manager)
    {
        super(manager);
    }

    public static void updateModels(ModelLoader modelLoader)
    {
        try
        {
            modelEntryPod = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "pod_flame.obj"), ImmutableList.of("PodBody"));
            modelFlame = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "pod_flame.obj"), ImmutableList.of("Flame_Sphere"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void doRender(EntityEntryPodVenus entityEntryPod, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.disableRescaleNormal();
        GlStateManager.pushMatrix();
        final float var24 = entityEntryPod.prevRotationPitch + (entityEntryPod.rotationPitch - entityEntryPod.prevRotationPitch) * partialTicks;
        final float var25 = entityEntryPod.prevRotationYaw + (entityEntryPod.rotationYaw - entityEntryPod.prevRotationYaw) * partialTicks;

        GlStateManager.translatef((float) x, (float) y, (float) z);
        GlStateManager.rotatef(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(180.0F - var24, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotatef(-var25, 0.0F, 1.0F, 0.0F);

        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.scalef(-1.0F, -1.0F, 1.0F);
        GlStateManager.scalef(0.65F, 0.6F, 0.65F);
        ClientUtil.drawBakedModel(modelEntryPod);

        if (entityEntryPod.posY > 382.0F)
        {
            RenderHelper.disableStandardItemLighting();
            GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GlStateManager.cullFace(GlStateManager.CullFace.FRONT);

            int color = ColorUtil.to32BitColor(entityEntryPod.posY >= 790.0F ? 255 : (int) Math.max(Math.min(255, -(entityEntryPod.getMotion().y + 0.6F) * 100.0F), 0), 255, 255, 255);

            GlStateManager.pushMatrix();
            float val = (float) (Math.sin(entityEntryPod.ticksExisted) / 20.0F + 0.5F);
            GlStateManager.scalef(1.0F, 1.0F + val, 1.0F);
            GlStateManager.rotatef(entityEntryPod.ticksExisted * 20.0F, 0.0F, 1.0F, 0.0F);
            ClientUtil.drawBakedModelColored(modelFlame, color);
            GlStateManager.popMatrix();

            GlStateManager.scalef(1.0F, 1.0F + val / 6.0F, 1.0F);
            GlStateManager.rotatef(entityEntryPod.ticksExisted * 5.0F, 0.0F, 1.0F, 0.0F);
            ClientUtil.drawBakedModelColored(modelFlame, color);

            GlStateManager.cullFace(GlStateManager.CullFace.BACK);
            GlStateManager.enableCull();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelper.enableStandardItemLighting();
        }

        if (entityEntryPod.getGroundPosY() != null && entityEntryPod.posY - entityEntryPod.getGroundPosY() > 5.0F && entityEntryPod.posY <= 242.0F)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(-1.4F, 1.5F, -0.3F);
            GlStateManager.scalef(2.5F, 3.0F, 2.5F);
            this.parachuteModel.renderAll();
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEntryPodVenus entityEntryPod)
    {
        return new ResourceLocation("missing");
    }
    
    @Override
    public boolean shouldRender(EntityEntryPodVenus lander, ICamera camera, double camX, double camY, double camZ)
    {
        AxisAlignedBB axisalignedbb = lander.getBoundingBox().grow(1D, 2D, 1D);
        return lander.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
