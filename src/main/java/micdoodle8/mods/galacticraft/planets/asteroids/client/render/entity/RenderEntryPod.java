package micdoodle8.mods.galacticraft.planets.asteroids.client.render.entity;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.entities.EntityEntryPod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderEntryPod extends Render<EntityEntryPod>
{
    private OBJModel.OBJBakedModel modelEntryPod;

    public RenderEntryPod(RenderManager manager)
    {
        super(manager);
    }

    private void updateModels()
    {
        if (modelEntryPod == null)
        {
            try
            {
                OBJModel model = (OBJModel) ModelLoaderRegistry.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "pod.obj"));
                model = (OBJModel) model.process(ImmutableMap.of("flip-v", "true"));

                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = new Function<ResourceLocation, TextureAtlasSprite>()
                {
                    @Override
                    public TextureAtlasSprite apply(ResourceLocation location)
                    {
                        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                    }
                };
                modelEntryPod = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("PodBody"), false), DefaultVertexFormats.ITEM, spriteFunction);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void doRender(EntityEntryPod entityEntryPod, double par2, double par4, double par6, float par8, float par9)
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

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

//        this.bindEntityTexture(entityEntryPod);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glScalef(0.65F, 0.6F, 0.65F);
        this.drawBakedModel(modelEntryPod);

        GL11.glPopMatrix();
        RenderHelper.enableStandardItemLighting();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityEntryPod entityEntryPod)
    {
        return new ResourceLocation("missing");
    }

    private void drawBakedModel(IFlexibleBakedModel model)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL11.GL_QUADS, model.getFormat());

        for (BakedQuad bakedquad : model.getGeneralQuads())
        {
            LightUtil.renderQuadColor(worldrenderer, bakedquad, -1);
        }

        tessellator.draw();
    }
}
