package micdoodle8.mods.galacticraft.core.client.render;

import micdoodle8.mods.galacticraft.core.Constants;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

public class RenderPlanet
{
    private static TextureManager renderEngine = FMLClientHandler.instance().getClient().renderEngine;

    private static ResourceLocation textureEuropa = new ResourceLocation(Constants.ASSET_PREFIX, "textures/misc/planets/europa.png");
    private static ResourceLocation textureGanymede = new ResourceLocation(Constants.ASSET_PREFIX, "textures/misc/planets/ganymede.png");
    private static ResourceLocation textureIo = new ResourceLocation(Constants.ASSET_PREFIX, "textures/misc/planets/io.png");
    private static ResourceLocation textureSaturn = new ResourceLocation(Constants.ASSET_PREFIX, "textures/misc/planets/saturn.png");
    private static ResourceLocation textureJupiterInner = new ResourceLocation(Constants.ASSET_PREFIX, "textures/misc/planets/jupiter_inner.png");
    private static ResourceLocation textureJupiterUpper = new ResourceLocation(Constants.ASSET_PREFIX, "textures/misc/planets/jupiter_upper.png");

    public static void renderPlanet(int textureId, float scale, float ticks, float relSize)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        float size = relSize / 70 * scale;
        ticks = ((float) System.nanoTime()) / 50000000F;
        RenderPlanet.drawTexturedRectUV(-size / 2, -size / 2, size, size, ticks);
    }

    public static void renderPlanet(ResourceLocation texture, float scale, float ticks, float relSize)
    {
        RenderPlanet.renderEngine.bindTexture(texture);
        float size = relSize / 70 * scale;
        ticks = ((float) System.nanoTime()) / 50000000F;
        RenderPlanet.drawTexturedRectUV(-size / 2, -size / 2, size, size, ticks);
    }

    public static void renderID(int id, float scale, float ticks)
    {
        ResourceLocation texture;
        switch (id)
        {
        case 0:
            texture = textureEuropa;
            break;
        case 1:
            texture = textureGanymede;
            break;
        case 2:
            texture = textureIo;
            break;
        case 3:
            texture = textureJupiterInner;
            break;
        case 4:
            texture = textureSaturn;
            break;
        default:
            texture = textureGanymede;
        }
        if (id == 3)  //Jupiter
        {
            float relSize = 48F;
            float size = relSize / 70 * scale;
            RenderPlanet.renderEngine.bindTexture(texture);
            RenderPlanet.drawTexturedRectUV(-size / 2, -size / 2, size, size, ticks);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef(0, 0, -0.001F);
            RenderPlanet.renderEngine.bindTexture(textureJupiterUpper);
            size *= 1.001F;
            RenderPlanet.drawTexturedRectUV(-size / 2, -size / 2, size, size, ticks * 0.85F);
        }
        else
        {
            RenderPlanet.renderPlanet(texture, scale, ticks, 8F);
        }
    }

    public static void drawTexturedRectUV(float x, float y, float width, float height, float ticks)
    {
        for (int ysect = 0; ysect < 6; ysect++)
        {
//    		drawTexturedRectUVSixth(x, y, width, height, (ticks / 600F) % 1F, ysect / 6F);
            // - 80F * MathHelper.sin(angle)
            float factor = 1F + MathHelper.cos((7.5F + 10F * ysect) / 62F);
            drawTexturedRectUVSixth(x, y, width, height, (ticks / 1100F) % 1F - (1F - factor) * 0.15F, ysect / 6F, 0.16F * factor);
        }
    }

    public static void drawTexturedRectUVSixth(float x, float y, float width, float height, float prog, float y0, float span)
    {
        y0 /= 2;
        if (prog < 0F)
        {
            prog += 1.0F;
        }
        prog = 1.0F - prog;
        float y1 = y0 + 1 / 12F;
        float y2 = 1F - y1;
        float y3 = 1F - y0;
        float yaa = y + height * y0;
        float yab = y + height * y1;
        float yba = y + height * y2;
        float ybb = y + height * y3;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        if (prog <= 1F - span)
        {
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(x, yab, 0F).tex(prog, y1).endVertex();
            worldRenderer.pos(x + width, yab, 0F).tex(prog + span, y1).endVertex();
            worldRenderer.pos(x + width, yaa, 0F).tex(prog + span, y0).endVertex();
            worldRenderer.pos(x, yaa, 0F).tex(prog, y0).endVertex();
            tessellator.draw();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(x, ybb, 0F).tex(prog, y3).endVertex();
            worldRenderer.pos(x + width, ybb, 0F).tex(prog + span, y3).endVertex();
            worldRenderer.pos(x + width, yba, 0F).tex(prog + span, y2).endVertex();
            worldRenderer.pos(x, yba, 0F).tex(prog, y2).endVertex();
            tessellator.draw();
        }
        else
        {
            double xp = x + width * (1F - prog) / span;
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(x, yab, 0F).tex(prog, y1).endVertex();
            worldRenderer.pos(xp, yab, 0F).tex(1.0F, y1).endVertex();
            worldRenderer.pos(xp, yaa, 0F).tex(1.0F, y0).endVertex();
            worldRenderer.pos(x, yaa, 0F).tex(prog, y0).endVertex();
            tessellator.draw();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(x, ybb, 0F).tex(prog, y3).endVertex();
            worldRenderer.pos(xp, ybb, 0F).tex(1.0F, y3).endVertex();
            worldRenderer.pos(xp, yba, 0F).tex(1.0F, y2).endVertex();
            worldRenderer.pos(x, yba, 0F).tex(prog, y2).endVertex();
            tessellator.draw();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(xp, yab, 0F).tex(0F, y1).endVertex();
            worldRenderer.pos(x + width, yab, 0F).tex(prog - 1F + span, y1).endVertex();
            worldRenderer.pos(x + width, yaa, 0F).tex(prog - 1F + span, y0).endVertex();
            worldRenderer.pos(xp, yaa, 0F).tex(0F, y0).endVertex();
            tessellator.draw();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(xp, ybb, 0F).tex(0F, y3).endVertex();
            worldRenderer.pos(x + width, ybb, 0F).tex(prog - 1F + span, y3).endVertex();
            worldRenderer.pos(x + width, yba, 0F).tex(prog - 1F + span, y2).endVertex();
            worldRenderer.pos(xp, yba, 0F).tex(0F, y2).endVertex();
            tessellator.draw();
        }
    }
}
