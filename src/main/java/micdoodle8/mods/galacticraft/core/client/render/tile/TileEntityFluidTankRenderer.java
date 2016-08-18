package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockFluidTank;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidTank;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.FluidRegistry;
import org.lwjgl.opengl.GL11;

public class TileEntityFluidTankRenderer extends TileEntitySpecialRenderer<TileEntityFluidTank>
{
    @Override
    public void renderTileEntityAt(TileEntityFluidTank tank, double x, double y, double z, float partialTicks, int destroyStage)
    {
        if (tank.fluidTank.getFluidAmount() == 0 || tank.fluidTank.getFluid().getFluid() == null)
        {
            return;
        }

        TileEntityFluidTank tankAbove = tank.getNextTank(tank.getPos());
        TileEntityFluidTank tankBelow = tank.getPreviousTank(tank.getPos());

        this.bindTexture(TextureMap.locationBlocksTexture);
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(tank.fluidTank.getFluid().getFluid().getStill().toString());
        final double uMin = sprite.getMinU();
        final double uMax = sprite.getMaxU();
        final double vMin = sprite.getMinV();
        final double vMax = sprite.getMaxV();
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y + 1.5F, (float) z + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer worldRenderer = tess.getWorldRenderer();

        float level0 = tank.fluidTank.getFluidAmount() / 16000.0F;
        float level = 1.0F - level0;

        if (level < 1.0F)
        {
            // North
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(-0.4, level, -0.399).tex(uMin, vMin).endVertex();
            worldRenderer.pos(-0.4, 1.0, -0.399).tex(uMin, vMin + (vMax - vMin) * level0).endVertex();
            worldRenderer.pos(0.4, 1.0, -0.399).tex(uMax, vMin + (vMax - vMin) * level0).endVertex();
            worldRenderer.pos(0.4, level, -0.399).tex(uMax, vMin).endVertex();
            tess.draw();

            // South
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(0.4, level, 0.399).tex(uMax, vMin).endVertex();
            worldRenderer.pos(0.4, 1.0, 0.399).tex(uMax, vMin + (vMax - vMin) * level0).endVertex();
            worldRenderer.pos(-0.4, 1.0, 0.399).tex(uMin, vMin + (vMax - vMin) * level0).endVertex();
            worldRenderer.pos(-0.4, level, 0.399).tex(uMin, vMin).endVertex();
            tess.draw();

            // West
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(-0.399, 1.0, -0.4).tex(uMin, vMin + (vMax - vMin) * level0).endVertex();
            worldRenderer.pos(-0.399, level, -0.4).tex(uMin, vMin).endVertex();
            worldRenderer.pos(-0.399, level, 0.4).tex(uMax, vMin).endVertex();
            worldRenderer.pos(-0.399, 1.0, 0.4).tex(uMax, vMin + (vMax - vMin) * level0).endVertex();
            tess.draw();

            // East
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(0.399, 1.0, 0.4).tex(uMax, vMin + (vMax - vMin) * level0).endVertex();
            worldRenderer.pos(0.399, level, 0.4).tex(uMax, vMin).endVertex();
            worldRenderer.pos(0.399, level, -0.4).tex(uMin, vMin).endVertex();
            worldRenderer.pos(0.399, 1.0, -0.4).tex(uMin, vMin + (vMax - vMin) * level0).endVertex();
            tess.draw();

            if (tankAbove == null || tankAbove.fluidTank.getFluidAmount() == 0)
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                worldRenderer.pos(0.4, 0.01 + level, 0.4).tex(uMax, vMax).endVertex();
                worldRenderer.pos(-0.4, 0.01 + level, 0.4).tex(uMax, vMin).endVertex();
                worldRenderer.pos(-0.4, 0.01 + level, -0.4).tex(uMin, vMin).endVertex();
                worldRenderer.pos(0.4, 0.01 + level, -0.4).tex(uMin, vMax).endVertex();
                tess.draw();
            }

            if (tankBelow == null || tankBelow.fluidTank.getFluidAmount() == 0)
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                worldRenderer.pos(0.4, 0.99, 0.4).tex(uMax, vMax).endVertex();
                worldRenderer.pos(0.4, 0.99, -0.4).tex(uMin, vMax).endVertex();
                worldRenderer.pos(-0.4, 0.99, -0.4).tex(uMin, vMin).endVertex();
                worldRenderer.pos(-0.4, 0.99, 0.4).tex(uMax, vMin).endVertex();
                tess.draw();
            }
        }

        GlStateManager.enableLighting();
        GlStateManager.disableBlend();

        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
