package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import org.lwjgl.opengl.GL11;

public class TileEntityFluidTankRenderer extends TileEntitySpecialRenderer<TileEntityFluidTank>
{
    @Override
    public void renderTileEntityAt(TileEntityFluidTank tank, double x, double y, double z, float partialTicks, int destroyStage)
    {
        FluidTankInfo[] info = tank.getTankInfo(EnumFacing.DOWN);

        if (info.length != 1)
        {
            return;
        }
        FluidStack tankFluid = info[0].fluid; 
        if (tankFluid == null || tankFluid.getFluid() == null || (!tankFluid.getFluid().isGaseous() && tankFluid.amount == 0))
        {
            return;
        }

        TileEntityFluidTank tankAbove = tank.getNextTank(tank.getPos());
        TileEntityFluidTank tankBelow = tank.getPreviousTank(tank.getPos());

        this.bindTexture(TextureMap.locationBlocksTexture);
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(tankFluid.getFluid().getStill().toString());
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
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer worldRenderer = tess.getWorldRenderer();

        float level = 1.0F;
        float levelInv = 0.0F;
        float opacity = 1.0F;

        boolean compositeGaseous = tankFluid.getFluid().isGaseous();

        if (compositeGaseous)
        {
            opacity = Math.min(tankFluid.amount / (float) info[0].capacity * 0.8F + 0.2F, 1F);
        }
        else
        {
            level = tank.fluidTank.getFluidAmount() / 16400.0F;
            if (level <= 0.012F)
            {
                levelInv = 1.0F;  //Empty tanks render empty - see #3222
            }
            else
            {
                levelInv = 0.988F - level;  //1.2% inset from each end of the tank, to avoid z-fighting with blocks above/below
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, opacity);

        if (levelInv < 1.0F)
        {
            // North
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(-0.4, levelInv, -0.399).tex(uMin, vMin).endVertex();
            worldRenderer.pos(-0.4, 1.0, -0.399).tex(uMin, vMin + (vMax - vMin) * level).endVertex();
            worldRenderer.pos(0.4, 1.0, -0.399).tex(uMax, vMin + (vMax - vMin) * level).endVertex();
            worldRenderer.pos(0.4, levelInv, -0.399).tex(uMax, vMin).endVertex();
            tess.draw();

            // South
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(0.4, levelInv, 0.399).tex(uMax, vMin).endVertex();
            worldRenderer.pos(0.4, 1.0, 0.399).tex(uMax, vMin + (vMax - vMin) * level).endVertex();
            worldRenderer.pos(-0.4, 1.0, 0.399).tex(uMin, vMin + (vMax - vMin) * level).endVertex();
            worldRenderer.pos(-0.4, levelInv, 0.399).tex(uMin, vMin).endVertex();
            tess.draw();

            // West
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(-0.399, 1.0, -0.4).tex(uMin, vMin + (vMax - vMin) * level).endVertex();
            worldRenderer.pos(-0.399, levelInv, -0.4).tex(uMin, vMin).endVertex();
            worldRenderer.pos(-0.399, levelInv, 0.4).tex(uMax, vMin).endVertex();
            worldRenderer.pos(-0.399, 1.0, 0.4).tex(uMax, vMin + (vMax - vMin) * level).endVertex();
            tess.draw();

            // East
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(0.399, 1.0, 0.4).tex(uMax, vMin + (vMax - vMin) * level).endVertex();
            worldRenderer.pos(0.399, levelInv, 0.4).tex(uMax, vMin).endVertex();
            worldRenderer.pos(0.399, levelInv, -0.4).tex(uMin, vMin).endVertex();
            worldRenderer.pos(0.399, 1.0, -0.4).tex(uMin, vMin + (vMax - vMin) * level).endVertex();
            tess.draw();

            if (tankAbove == null || (tankAbove.fluidTank.getFluidAmount() == 0 && !compositeGaseous))
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                worldRenderer.pos(0.4, 0.01 + levelInv, 0.4).tex(uMax, vMax).endVertex();
                worldRenderer.pos(-0.4, 0.01 + levelInv, 0.4).tex(uMax, vMin).endVertex();
                worldRenderer.pos(-0.4, 0.01 + levelInv, -0.4).tex(uMin, vMin).endVertex();
                worldRenderer.pos(0.4, 0.01 + levelInv, -0.4).tex(uMin, vMax).endVertex();
                tess.draw();
            }

            if (tankBelow == null || (tankBelow.fluidTank.getFluidAmount() == 0 && !compositeGaseous))
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
