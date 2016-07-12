package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityShortRangeTelepadRenderer extends TileEntitySpecialRenderer<TileEntityShortRangeTelepad>
{
    private static OBJModel.OBJBakedModel teleporterTop;
    private static OBJModel.OBJBakedModel teleporterBottom;

    private void updateModels()
    {
        if (teleporterTop == null)
        {
            try
            {
                OBJModel model = (OBJModel) ModelLoaderRegistry.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "telepadShort.obj"));
                model = (OBJModel) model.process(ImmutableMap.of("flip-v", "true"));

                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = new Function<ResourceLocation, TextureAtlasSprite>() {
                    @Override
                    public TextureAtlasSprite apply(ResourceLocation location) {
                        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                    }
                };
                teleporterTop = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Top", "Connector"), false), DefaultVertexFormats.ITEM, spriteFunction);
                teleporterBottom = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Bottom"), false), DefaultVertexFormats.ITEM, spriteFunction);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void renderTileEntityAt(TileEntityShortRangeTelepad te, double x, double y, double z, float partialTicks, int destroyStage)
    {
        GL11.glPushMatrix();

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

        updateModels();

        GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);

        GL11.glScalef(0.745F, 1.0F, 0.745F);

        drawBakedModel(teleporterBottom);
        GL11.glTranslatef(0.0F, -0.7F, 0.0F);
        drawBakedModel(teleporterTop);

        GL11.glPopMatrix();
    }

    private void drawBakedModel(IFlexibleBakedModel model)
    {
        drawBakedModel(model, -1);
    }

    private void drawBakedModel(IFlexibleBakedModel model, int color)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL11.GL_QUADS, model.getFormat());

        for(BakedQuad bakedquad : model.getGeneralQuads())
            LightUtil.renderQuadColor(worldrenderer, bakedquad, color);

        tessellator.draw();
    }
}
