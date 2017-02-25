package micdoodle8.mods.galacticraft.planets.venus.client.render.entity;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.client.model.ModelBalloonParachute;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityEntryPodVenus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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
                OBJModel model = (OBJModel) ModelLoaderRegistry.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "podFlame.obj"));
                model = (OBJModel) model.process(ImmutableMap.of("flip-v", "true"));

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
    public void doRender(EntityEntryPodVenus entityEntryPod, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPushMatrix();
        final float var24 = entityEntryPod.prevRotationPitch + (entityEntryPod.rotationPitch - entityEntryPod.prevRotationPitch) * par9;
        final float var25 = entityEntryPod.prevRotationYaw + (entityEntryPod.rotationYaw - entityEntryPod.prevRotationYaw) * par9;

        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0F - var24, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(-var25, 0.0F, 1.0F, 0.0F);

        this.updateModels();

        RenderHelper.disableStandardItemLighting();
        this.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glScalef(0.65F, 0.6F, 0.65F);

        ClientUtil.drawBakedModel(modelEntryPod);

        if (entityEntryPod.posY > 382.0F)
        {
            RenderHelper.disableStandardItemLighting();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glCullFace(GL11.GL_FRONT);

            int color = ColorUtil.to32BitColor(entityEntryPod.posY >= 790.0F ? 255 : (int) Math.max(Math.min(255, -(entityEntryPod.motionY + 0.6F) * 100.0F), 0), 255, 255, 255);

            GL11.glPushMatrix();
            float val = (float) (Math.sin(entityEntryPod.ticksExisted) / 20.0F + 0.5F);
            GL11.glScalef(1.0F, 1.0F + val, 1.0F);
            GL11.glRotatef(entityEntryPod.ticksExisted * 20.0F, 0.0F, 1.0F, 0.0F);
            ClientUtil.drawBakedModelColored(modelFlame, color);
            GL11.glPopMatrix();

            GL11.glScalef(1.0F, 1.0F + val / 6.0F, 1.0F);
            GL11.glRotatef(entityEntryPod.ticksExisted * 5.0F, 0.0F, 1.0F, 0.0F);
            ClientUtil.drawBakedModelColored(modelFlame, color);

            GL11.glCullFace(GL11.GL_BACK);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelper.enableStandardItemLighting();
        }

        if (entityEntryPod.getGroundPosY() != null && entityEntryPod.posY - entityEntryPod.getGroundPosY() > 5.0F && entityEntryPod.posY <= 382.0F)
        {
            GL11.glPushMatrix();

            GL11.glTranslatef((float) par2 - 1.4F, (float) par4 + 1.5F, (float) par6 - 0.3F);
            GL11.glScalef(2.5F, 3.0F, 2.5F);
            this.parachuteModel.renderAll();

            GL11.glPopMatrix();
        }

        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEntryPodVenus entityEntryPod)
    {
        return new ResourceLocation("missing");
    }
}
