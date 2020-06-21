package micdoodle8.mods.galacticraft.core.client.render.tile;

import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class TileEntityFluidTankRenderer extends TileEntityRenderer<TileEntityFluidTank>
{
    @Override
    public void render(TileEntityFluidTank tileTank, double x, double y, double z, float partialTicks, int destroyStage)
    {
//        FluidTankInfo[] info = tank.getTankInfo(Direction.DOWN);

//        if (info.length != 1)
//        {
//            return;
//        }
//        FluidStack tankFluid = info[0].fluid;
        FluidStack tankFluid = tileTank.fluidTank.getFluid();
        if (tankFluid == FluidStack.EMPTY || (!tankFluid.getFluid().getAttributes().isGaseous() && tankFluid.getAmount() == 0))
        {
            return;
        }

        TileEntityFluidTank tankAbove = tileTank.getNextTank(tileTank.getPos());
        TileEntityFluidTank tankBelow = tileTank.getPreviousTank(tileTank.getPos());

        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMap().getAtlasSprite(tankFluid.getFluid().getAttributes().getStillTexture().toString());
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
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();

        float level = 1.0F;
        float levelInv = 0.0F;
        float opacity = 1.0F;

        boolean compositeGaseous = tankFluid.getFluid().getAttributes().isGaseous();

        if (compositeGaseous)
        {
            opacity = Math.min(tankFluid.getAmount() / (float) tileTank.fluidTank.getCapacity() * 0.8F + 0.2F, 1F);
        }
        else
        {
            level = tileTank.fluidTank.getFluidAmount() / 16400.0F;
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
