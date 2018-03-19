package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import micdoodle8.mods.galacticraft.core.client.model.OBJLoaderGC;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelBalloonParachute;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityEntryPodVenus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;

import org.lwjgl.opengl.GL11;

public class RenderEntryPodVenus extends Render<EntityEntryPodVenus>
{
    private OBJModel.OBJBakedModel modelEntryPod;
    private OBJModel.OBJBakedModel modelFlame;
    protected ModelBalloonParachute parachuteModel = new ModelBalloonParachute();

    public RenderEntryPodVenus(RenderManager manager)
    {
        super(manager);
    }

    private void updateModels()
    {
        if (modelEntryPod == null)
        {
            try
            {
                IModel model = OBJLoaderGC.instance.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "pod_flame.obj"));
                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());

                modelEntryPod = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("PodBody"), false), DefaultVertexFormats.ITEM, spriteFunction);
                modelFlame = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Flame_Sphere"), false), DefaultVertexFormats.ITEM, spriteFunction);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void doRender(EntityEntryPodVenus entityEntryPod, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.disableRescaleNormal();
        GlStateManager.pushMatrix();
        final float var24 = entityEntryPod.prevRotationPitch + (entityEntryPod.rotationPitch - entityEntryPod.prevRotationPitch) * partialTicks;
        final float var25 = entityEntryPod.prevRotationYaw + (entityEntryPod.rotationYaw - entityEntryPod.prevRotationYaw) * partialTicks;

        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(180.0F - entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F - var24, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(-var25, 0.0F, 1.0F, 0.0F);

        this.updateModels();
        this.bindTexture(TextureMap.locationBlocksTexture);

        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.scale(0.65F, 0.6F, 0.65F);
        ClientUtil.drawBakedModel(modelEntryPod);

        if (entityEntryPod.posY > 382.0F)
        {
            RenderHelper.disableStandardItemLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GlStateManager.cullFace(GL11.GL_FRONT);

            int color = ColorUtil.to32BitColor(entityEntryPod.posY >= 790.0F ? 255 : (int) Math.max(Math.min(255, -(entityEntryPod.motionY + 0.6F) * 100.0F), 0), 255, 255, 255);

            GlStateManager.pushMatrix();
            float val = (float) (Math.sin(entityEntryPod.ticksExisted) / 20.0F + 0.5F);
            GlStateManager.scale(1.0F, 1.0F + val, 1.0F);
            GlStateManager.rotate(entityEntryPod.ticksExisted * 20.0F, 0.0F, 1.0F, 0.0F);
            ClientUtil.drawBakedModelColored(modelFlame, color);
            GlStateManager.popMatrix();

            GlStateManager.scale(1.0F, 1.0F + val / 6.0F, 1.0F);
            GlStateManager.rotate(entityEntryPod.ticksExisted * 5.0F, 0.0F, 1.0F, 0.0F);
            ClientUtil.drawBakedModelColored(modelFlame, color);

            GlStateManager.cullFace(GL11.GL_BACK);
            GlStateManager.enableCull();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelper.enableStandardItemLighting();
        }

        if (entityEntryPod.getGroundPosY() != null && entityEntryPod.posY - entityEntryPod.getGroundPosY() > 5.0F && entityEntryPod.posY <= 242.0F)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-1.4F, 1.5F, -0.3F);
            GlStateManager.scale(2.5F, 3.0F, 2.5F);
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
        AxisAlignedBB axisalignedbb = lander.getEntityBoundingBox().expand(1D, 2D, 1D);
        return lander.isInRangeToRender3d(camX, camY, camZ) && camera.isBoundingBoxInFrustum(axisalignedbb);
    }
}
