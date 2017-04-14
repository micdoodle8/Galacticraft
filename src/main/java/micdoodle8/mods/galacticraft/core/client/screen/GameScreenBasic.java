package micdoodle8.mods.galacticraft.core.client.screen;

import micdoodle8.mods.galacticraft.api.client.IGameScreen;
import micdoodle8.mods.galacticraft.api.client.IScreenManager;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.render.RenderPlanet;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.opengl.GL11;

public class GameScreenBasic implements IGameScreen
{
    private TextureManager renderEngine;

    private float frameA;
    private float frameBx;
    private float frameBy;
    private float textureAx = 0F;
    private float textureAy = 0F;
    private float textureBx = 1.0F;
    private float textureBy = 1.0F;

    public GameScreenBasic()
    {
        //This can be called from either server or client, so don't include
        //client-side only code on the server.
        if (FMLCommonHandler.instance().getEffectiveSide().isClient())
        {
            renderEngine = FMLClientHandler.instance().getClient().renderEngine;
        }
    }

    @Override
    public void setFrameSize(float frameSize)
    {
        this.frameA = frameSize;
    }

    @Override
    public void render(int type, float ticks, float scaleX, float scaleY, IScreenManager scr)
    {
        frameBx = scaleX - frameA;
        frameBy = scaleY - frameA;

        if (scaleX == scaleY)
        {
            textureAx = 0F;
            textureAy = 0F;
            textureBx = 1.0F;
            textureBy = 1.0F;
        }
        else if (scaleX < scaleY)
        {
            textureAx = (1.0F - (scaleX / scaleY)) / 2;
            textureAy = 0F;
            textureBx = 1.0F - textureAx;
            textureBy = 1.0F;
        }
        else if (scaleY < scaleX)
        {
            textureAx = 0F;
            textureAy = (1.0F - (scaleY / scaleX)) / 2;
            textureBx = 1.0F;
            textureBy = 1.0F - textureAy;
        }

        switch (type)
        {
        case 0:
            drawBlackBackground(0.09F);
//        	ClientProxyCore.overworldTextureLocal = null;
            break;
        case 1:
            if (scr instanceof DrawGameScreen && ((DrawGameScreen) scr).mapDone)
            {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, DrawGameScreen.reusableMap.getGlTextureId());
                draw2DTexture();
            }
            else if (ClientProxyCore.overworldTexturesValid)
            {
                GL11.glPushMatrix();
                float centreX = scaleX / 2;
                float centreY = scaleY / 2;
                GL11.glTranslatef(centreX, centreY, 0F);
                RenderPlanet.renderPlanet(ClientProxyCore.overworldTextureWide.getGlTextureId(), Math.min(scaleX, scaleY) - 0.2F, ticks, 45F);
                GL11.glPopMatrix();
            }
            else
            {
                this.renderEngine.bindTexture(new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/celestialbodies/earth.png"));
                if (!ClientProxyCore.overworldTextureRequestSent)
                {
                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_OVERWORLD_IMAGE, GCCoreUtil.getDimensionID(FMLClientHandler.instance().getClient().theWorld), new Object[] {}));
                    ClientProxyCore.overworldTextureRequestSent = true;
                }
                draw2DTexture();
            }
            break;
        }
    }

    private void draw2DTexture()
    {
        final Tessellator tess = Tessellator.getInstance();
        VertexBuffer worldRenderer = tess.getBuffer();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        worldRenderer.pos(frameA, frameBy, 0F).tex(textureAx, textureBy).endVertex();
        worldRenderer.pos(frameBx, frameBy, 0F).tex(textureBx, textureBy).endVertex();
        worldRenderer.pos(frameBx, frameA, 0F).tex(textureBx, textureAy).endVertex();
        worldRenderer.pos(frameA, frameA, 0F).tex(textureAx, textureAy).endVertex();
        tess.draw();
    }

    private void drawBlackBackground(float greyLevel)
    {
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        final Tessellator tess = Tessellator.getInstance();
        VertexBuffer worldRenderer = tess.getBuffer();
        GL11.glColor4f(greyLevel, greyLevel, greyLevel, 1.0F);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        worldRenderer.pos(frameA, frameBy, 0.005F).endVertex();
        worldRenderer.pos(frameBx, frameBy, 0.005F).endVertex();
        worldRenderer.pos(frameBx, frameA, 0.005F).endVertex();
        worldRenderer.pos(frameA, frameA, 0.005F).endVertex();
        tess.draw();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
