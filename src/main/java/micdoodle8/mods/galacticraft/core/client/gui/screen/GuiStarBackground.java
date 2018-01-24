package micdoodle8.mods.galacticraft.core.client.gui.screen;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public abstract class GuiStarBackground extends GuiScreen
{
    private static final ResourceLocation backgroundTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/stars.png");
    private static final ResourceLocation blackTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/gui/black.png");

    public void drawBlackBackground()
    {
        final ScaledResolution var5 = ClientUtil.getScaledRes(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        final int var6 = var5.getScaledWidth();
        final int var7 = var5.getScaledHeight();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        this.mc.getTextureManager().bindTexture(GuiStarBackground.blackTexture);
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(0.0D, var7, -90.0D).tex(0.0D, 1.0D).endVertex();
        worldRenderer.pos(var6, var7, -90.0D).tex(1.0D, 1.0D).endVertex();
        worldRenderer.pos(var6, 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
        worldRenderer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
        tess.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void drawPanorama2(float par1)
    {
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GLU.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        final byte var5 = 1;

        for (int var6 = 0; var6 < var5 * var5; ++var6)
        {
            GL11.glPushMatrix();
            final float var7 = ((float) (var6 % var5) / (float) var5 - 0.5F) / 128.0F;
            final float var8 = ((float) (var6 / var5) / (float) var5 - 0.5F) / 128.0F;
            final float var9 = 0.0F;

            float mY;
            float mX;

            if (Mouse.getY() < this.height)
            {
                mY = (-this.height + Mouse.getY()) / 100F;
            }
            else
            {
                mY = (-this.height + Mouse.getY()) / 100F;
            }

            mX = (this.width - Mouse.getX()) / 100F;

            this.doCustomTranslation(0, var7, var8, var9, mX, mY);

            for (int var10 = 0; var10 < 9; ++var10)
            {
                GL11.glPushMatrix();

                if (var10 == 1)
                {
                    GL11.glTranslatef(1.96F, 0.0F, 0.0F);
                }

                if (var10 == 2)
                {
                    GL11.glTranslatef(-1.96F, 0.0F, 0.0F);
                }

                if (var10 == 3)
                {
                    GL11.glTranslatef(0.0F, 1.96F, 0.0F);
                }

                if (var10 == 4)
                {
                    GL11.glTranslatef(0.0F, -1.96F, 0.0F);
                }

                if (var10 == 5)
                {
                    GL11.glTranslatef(-1.96F, -1.96F, 0.0F);
                }

                if (var10 == 6)
                {
                    GL11.glTranslatef(-1.96F, 1.96F, 0.0F);
                }

                if (var10 == 7)
                {
                    GL11.glTranslatef(1.96F, -1.96F, 0.0F);
                }

                if (var10 == 8)
                {
                    GL11.glTranslatef(1.96F, 1.96F, 0.0F);
                }

                this.mc.getTextureManager().bindTexture(GuiStarBackground.backgroundTexture);
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldRenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0F + 1, 0.0F + 1).color(1.0F, 1.0F, 1.0F, 1.0F / (var6 + 1)).endVertex();
                worldRenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0F - 1, 0.0F + 1).color(1.0F, 1.0F, 1.0F, 1.0F / (var6 + 1)).endVertex();
                worldRenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0F - 1, 1.0F - 1).color(1.0F, 1.0F, 1.0F, 1.0F / (var6 + 1)).endVertex();
                worldRenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0F + 1, 1.0F - 1).color(1.0F, 1.0F, 1.0F, 1.0F / (var6 + 1)).endVertex();
                tess.draw();
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
        }

        worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void drawPanorama(float par1)
    {
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GLU.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        final byte var5 = 1;

        for (int var6 = 0; var6 < var5 * var5; ++var6)
        {
            GL11.glPushMatrix();
            final float var7 = ((float) (var6 % var5) / (float) var5 - 0.5F) / 64.0F;
            final float var8 = ((float) (var6 / var5) / (float) var5 - 0.5F) / 64.0F;
            final float var9 = 0.0F;

            float mY;
            float mX;

            if (Mouse.getY() < this.height)
            {
                mY = (-this.height + Mouse.getY()) / 100F;
            }
            else
            {
                mY = (-this.height + Mouse.getY()) / 100F;
            }

            mX = (this.width - Mouse.getX()) / 100F;

            this.doCustomTranslation(1, var7, var8, var9, mX, mY);

            GL11.glRotatef(MathHelper.sin(par1 / 1000.0F) * 25.0F + 20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-par1 * 0.005F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(41, 0, 0, 1);

            for (int var10 = 0; var10 < 6; ++var10)
            {
                GL11.glPushMatrix();

                if (var10 == 1)
                {
                    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var10 == 2)
                {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var10 == 3)
                {
                    GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                }

                if (var10 == 4)
                {
                    GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
                }

                if (var10 == 5)
                {
                    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
                }

                this.mc.getTextureManager().bindTexture(GuiStarBackground.backgroundTexture);
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
                worldRenderer.pos(-1.0D, -1.0D, 1.0D).tex(0.0F + 1, 0.0F + 1).color(1.0F, 1.0F, 1.0F, 1.0F / (var6 + 1)).endVertex();
                worldRenderer.pos(1.0D, -1.0D, 1.0D).tex(1.0F - 1, 0.0F + 1).color(1.0F, 1.0F, 1.0F, 1.0F / (var6 + 1)).endVertex();
                worldRenderer.pos(1.0D, 1.0D, 1.0D).tex(1.0F - 1, 1.0F - 1).color(1.0F, 1.0F, 1.0F, 1.0F / (var6 + 1)).endVertex();
                worldRenderer.pos(-1.0D, 1.0D, 1.0D).tex(0.0F + 1, 1.0F - 1).color(1.0F, 1.0F, 1.0F, 1.0F / (var6 + 1)).endVertex();
                tess.draw();
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
        }

        worldRenderer.setTranslation(0.0D, 0.0D, 0.0D);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void rotateAndBlurSkybox()
    {
        this.mc.getTextureManager().bindTexture(GuiStarBackground.backgroundTexture);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColorMask(true, true, true, false);
        GL11.glPushMatrix();
        GL11.glPopMatrix();
        GL11.glColorMask(true, true, true, true);
    }

    public void renderSkybox()
    {
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GL11.glPushMatrix();
        GL11.glScalef(1.0F, 0.0F, 1.0F);
        this.drawPanorama(1);
        this.drawPanorama2(1);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        this.rotateAndBlurSkybox();
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        final float var5 = this.width > this.height ? 120.0F / this.width : 120.0F / this.height;
        final float var6 = this.height * var5 / 256.0F;
        final float var7 = this.width * var5 / 256.0F;
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        final int var8 = this.width;
        final int var9 = this.height;
        worldRenderer.pos(0.0D, var9, this.zLevel).tex(0.5F - var6, 0.5F + var7).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldRenderer.pos(var8, var9, this.zLevel).tex(0.5F - var6, 0.5F - var7).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldRenderer.pos(var8, 0.0D, this.zLevel).tex(0.5F + var6, 0.5F - var7).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        worldRenderer.pos(0.0D, 0.0D, this.zLevel).tex(0.5F + var6, 0.5F + var7).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tess.draw();
        GL11.glPopMatrix();
    }

    public abstract void doCustomTranslation(int type, float coord1, float coord2, float coord3, float mX, float mY);
}
