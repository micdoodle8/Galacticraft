package micdoodle8.mods.galacticraft.planets.mars.client.render.entity;

import com.google.common.base.Function;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.client.render.item.ItemModelCargoRocket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.planets.mars.entities.EntityCargoRocket;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

@SideOnly(Side.CLIENT)
public class RenderCargoRocket extends Render<EntityCargoRocket>
{
    private static Function<ResourceLocation, TextureAtlasSprite> TEXTUREGETTER;
    private ItemModelCargoRocket rocketModel;

    public RenderCargoRocket(RenderManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 0.5F;
    }

    private void updateModel()
    {
        if (rocketModel == null)
        {
            TEXTUREGETTER = new Function<ResourceLocation, TextureAtlasSprite>() {
                @Override
                public TextureAtlasSprite apply(ResourceLocation input) {
                    return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(input.toString());
                }
            };

            ModelResourceLocation modelResourceLocation = new ModelResourceLocation(GalacticraftPlanets.TEXTURE_PREFIX + "cargoRocket", "inventory");
            rocketModel = (ItemModelCargoRocket) FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().getModelManager().getModel(modelResourceLocation);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCargoRocket par1Entity)
    {
        return new ResourceLocation("missing");
    }

    @Override
    public void doRender(EntityCargoRocket entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;
        GL11.glTranslatef((float) par2, (float) par4 + entity.getRenderOffsetY(), (float) par6);
        GL11.glScalef(0.4F, 0.4F, 0.4F);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);

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

        updateModel();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL11.GL_QUADS, rocketModel.getFormat());
        this.renderQuads(worldrenderer, rocketModel.getGeneralQuads(), -1);
        tessellator.draw();

        GL11.glPopMatrix();
        RenderHelper.enableStandardItemLighting();
    }

    private void renderQuads(WorldRenderer renderer, List<BakedQuad> quads, int color) {
        for(BakedQuad bakedquad : quads)
            LightUtil.renderQuadColor(renderer, bakedquad, color);
    }
}
