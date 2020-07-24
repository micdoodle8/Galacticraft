package micdoodle8.mods.galacticraft.core.client.gui.overlay;

import micdoodle8.mods.galacticraft.core.entities.EntityTier1Rocket;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;

public class Overlay
{
    /**
     * Get the player's spaceship height off ground
     *
     * @param player thePlayer
     * @return position of player's spaceship
     */
    protected static int getPlayerPositionY(PlayerEntity player)
    {
        if (player.getRidingEntity() != null && player.getRidingEntity() instanceof EntityTier1Rocket)
        {
            return (int) Math.floor((player.getRidingEntity()).getPosY());
        }

        return (int) Math.floor(player.getPosY());
    }

    /**
     * Draw a textured rectangle at the specified position
     *
     * @param par1 xpos
     * @param par2 ypos
     * @param par3 u
     * @param par4 v
     * @param par5 width
     * @param par6 height
     */
    protected static void blit(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        final float var7 = 0.00390625F;
        final float var8 = 0.00390625F;
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(par1 + 0, par2 + par6, 0.0).tex((par3 + 0) * var7, (par4 + par6) * var8).endVertex();
        worldRenderer.pos(par1 + par5, par2 + par6, 0.0).tex((par3 + par5) * var7, (par4 + par6) * var8).endVertex();
        worldRenderer.pos(par1 + par5, par2 + 0, 0.0).tex((par3 + par5) * var7, (par4 + 0) * var8).endVertex();
        worldRenderer.pos(par1 + 0, par2 + 0, 0.0).tex((par3 + 0) * var7, (par4 + 0) * var8).endVertex();
        tess.draw();
    }

    /**
     * Draws a rectangle with middle at point specified
     *
     * @param var1 x
     * @param var3 y
     * @param var5 depth
     * @param var7 width
     * @param var9 height
     */
    protected static void drawCenteringRectangle(double var1, double var3, double var5, double var7, double var9)
    {
        var7 *= 0.5D;
        var9 *= 0.5D;
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(var1 - var7, var3 + var9, var5).tex(0.0F, 1.0F).endVertex();
        worldRenderer.pos(var1 + var7, var3 + var9, var5).tex(1.0F, 1.0F).endVertex();
        worldRenderer.pos(var1 + var7, var3 - var9, var5).tex(1.0F, 0.0F).endVertex();
        worldRenderer.pos(var1 - var7, var3 - var9, var5).tex(0.0F, 0.0F).endVertex();
        tess.draw();
    }
}
