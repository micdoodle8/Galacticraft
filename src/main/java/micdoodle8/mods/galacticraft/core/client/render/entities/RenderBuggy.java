package micdoodle8.mods.galacticraft.core.client.render.entities;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderBuggy extends Render<EntityBuggy>
{
    private OBJModel.OBJBakedModel mainModel;
    private OBJModel.OBJBakedModel radarDish;
    private OBJModel.OBJBakedModel wheelBackLeft;
    private OBJModel.OBJBakedModel wheelBackRight;
    private OBJModel.OBJBakedModel wheelFrontLeft;
    private OBJModel.OBJBakedModel wheelFrontRight;
    private OBJModel.OBJBakedModel cargoLeft;
    private OBJModel.OBJBakedModel cargoMid;
    private OBJModel.OBJBakedModel cargoRight;

    private void updateModels()
    {
        if (mainModel == null)
        {
            try
            {
                OBJModel model = (OBJModel) ModelLoaderRegistry.getModel(new ResourceLocation(Constants.ASSET_PREFIX, "buggy.obj"));
                model = (OBJModel) model.process(ImmutableMap.of("flip-v", "true"));

                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                mainModel = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("MainBody"), false), DefaultVertexFormats.ITEM, spriteFunction);
                radarDish = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("RadarDish_Dish"), false), DefaultVertexFormats.ITEM, spriteFunction);
                wheelBackLeft = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Wheel_Back_Left"), false), DefaultVertexFormats.ITEM, spriteFunction);
                wheelBackRight = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Wheel_Back_Right"), false), DefaultVertexFormats.ITEM, spriteFunction);
                wheelFrontLeft = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Wheel_Front_Left"), false), DefaultVertexFormats.ITEM, spriteFunction);
                wheelFrontRight = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Wheel_Front_Right"), false), DefaultVertexFormats.ITEM, spriteFunction);
                cargoLeft = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("CargoLeft"), false), DefaultVertexFormats.ITEM, spriteFunction);
                cargoMid = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("CargoMid"), false), DefaultVertexFormats.ITEM, spriteFunction);
                cargoRight = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("CargoRight"), false), DefaultVertexFormats.ITEM, spriteFunction);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public RenderBuggy(RenderManager manager)
    {
        super(manager);
        this.shadowSize = 1.0F;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityBuggy par1Entity)
    {
        return new ResourceLocation("missing");
    }

    @Override
    public void doRender(EntityBuggy entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(0.41F, 0.41F, 0.41F);

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

        float rotation = entity.wheelRotationX;

        // Front wheels
        GL11.glPushMatrix();
        GL11.glRotatef(entity.wheelRotationZ, 0, 1, 0);
        GL11.glRotatef(rotation, 1, 0, 0);
        this.drawBakedModel(wheelFrontRight);
        this.drawBakedModel(wheelFrontLeft);
        GL11.glPopMatrix();

        // Back wheels
        GL11.glPushMatrix();
        GL11.glRotatef(-entity.wheelRotationZ, 0, 1, 0);
        GL11.glRotatef(rotation, 1, 0, 0);
        this.drawBakedModel(wheelBackRight);
        this.drawBakedModel(wheelBackLeft);
        GL11.glPopMatrix();

        this.drawBakedModel(mainModel);

        // Radar Dish
        GL11.glPushMatrix();
        GL11.glTranslatef(-1.178F, 4.1F, -2.397F);
        int ticks = entity.ticksExisted + entity.getEntityId() * 10000;
        GL11.glRotatef((float) Math.sin(ticks * 0.05) * 50.0F, 1, 0, 0);
        GL11.glRotatef((float) Math.cos(ticks * 0.1) * 50.0F, 0, 0, 1);
        this.drawBakedModel(radarDish);
        GL11.glPopMatrix();

        if (entity.buggyType > 0)
        {
            this.drawBakedModel(cargoLeft);

            if (entity.buggyType > 1)
            {
                this.drawBakedModel(cargoMid);

                if (entity.buggyType > 2)
                {
                    this.drawBakedModel(cargoRight);
                }
            }
        }

        GL11.glPopMatrix();
        RenderHelper.enableStandardItemLighting();
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
