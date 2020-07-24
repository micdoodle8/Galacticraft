package micdoodle8.mods.galacticraft.core.client.screen;

import micdoodle8.mods.galacticraft.api.client.IGameScreen;
import micdoodle8.mods.galacticraft.api.client.IScreenManager;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;

public class GameScreenBasic implements IGameScreen
{
    private TextureManager textureManager;

    private float frameA;
    private float frameBx;
    private float frameBy;
    private double textureAx = 0D;
    private double textureAy = 0D;
    private double textureBx = 1.0D;
    private double textureBy = 1.0D;

    public GameScreenBasic()
    {
        //This can be called from either server or client, so don't include
        //client-LogicalSide only code on the server.
        if (GCCoreUtil.getEffectiveSide().isClient())
        {
            textureManager = Minecraft.getInstance().textureManager;
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
            textureAx = 0D;
            textureAy = 0D;
            textureBx = 1.0D;
            textureBy = 1.0D;
        }
        else if (scaleX < scaleY)
        {
            textureAx = (1.0D - (scaleX / scaleY)) / 2D;
            textureAy = 0D;
            textureBx = 1.0D - textureAx;
            textureBy = 1.0D;
        }
        else if (scaleY < scaleX)
        {
            textureAx = 0D;
            textureAy = (1.0D - (scaleY / scaleX)) / 2D;
            textureBx = 1.0D;
            textureBy = 1.0D - textureAy;
        }

//        switch (type)
//        {
//        case 0:
//            drawBlackBackground(0.09F);
//            break;
//        case 1:
//            if (scr instanceof DrawGameScreen && ((DrawGameScreen) scr).mapDone)
//            {
//                RenderSystem.bindTexture(DrawGameScreen.reusableMap.getGlTextureId());
//                draw2DTexture();
//            }
//            else if (ClientProxyCore.overworldTexturesValid)
//            {
//                RenderSystem.pushMatrix();
//                float centreX = scaleX / 2;
//                float centreY = scaleY / 2;
//                RenderSystem.translatef(centreX, centreY, 0F);
//                RenderPlanet.renderPlanet(ClientProxyCore.overworldTextureWide.getGlTextureId(), Math.min(scaleX, scaleY) - 0.2F, ticks, 45F);
//                RenderSystem.popMatrix();
//            }
//            else
//            {
//                this.textureManager.bindTexture(new ResourceLocation(Constants.MOD_ID_CORE, "textures/gui/celestialbodies/earth.png"));
//                if (!ClientProxyCore.overworldTextureRequestSent)
//                {
//                    GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_OVERWORLD_IMAGE, GCCoreUtil.getDimensionType(Minecraft.getInstance().world), new Object[]{}));
//                    ClientProxyCore.overworldTextureRequestSent = true;
//                }
//////                 Overworld texture is 48x48 in a 64x64 .png file
////                this.textureBx -= 0.25D;
////                this.textureBy -= 0.25D;
//                draw2DTexture();
////                this.textureBx += 0.25D;
////                this.textureBy += 0.25D;
//            }
//            break;
//        } TODO Game screen
    }

//    private void draw2DTexture()
//    {
//        final Tessellator tess = Tessellator.getInstance();
//        BufferBuilder worldRenderer = tess.getBuffer();
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
//
//        worldRenderer.pos(frameA, frameBy, 0F).tex(textureAx, textureBy).endVertex();
//        worldRenderer.pos(frameBx, frameBy, 0F).tex(textureBx, textureBy).endVertex();
//        worldRenderer.pos(frameBx, frameA, 0F).tex(textureBx, textureAy).endVertex();
//        worldRenderer.pos(frameA, frameA, 0F).tex(textureAx, textureAy).endVertex();
//        tess.draw();
//    }
//
//    private void drawBlackBackground(float greyLevel)
//    {
//        RenderSystem.disableLighting();
//        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.param, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.param, GlStateManager.SourceFactor.ONE.param, GlStateManager.DestFactor.ZERO.param);
//        RenderSystem.disableTexture();
//        final Tessellator tess = Tessellator.getInstance();
//        BufferBuilder worldRenderer = tess.getBuffer();
//        RenderSystem.color4f(greyLevel, greyLevel, greyLevel, 1.0F);
//        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//
//        worldRenderer.pos(frameA, frameBy, 0.005F).endVertex();
//        worldRenderer.pos(frameBx, frameBy, 0.005F).endVertex();
//        worldRenderer.pos(frameBx, frameA, 0.005F).endVertex();
//        worldRenderer.pos(frameA, frameA, 0.005F).endVertex();
//        tess.draw();
//
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.enableTexture();
//        RenderSystem.enableLighting();
//    }
}
