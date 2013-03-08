package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpaceship;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.client.FMLClientHandler;

public class GCCoreOverlay
{
	/**
	 * Get the player's spaceship height off ground
	 *
	 * @param player thePlayer
	 * @return position of player's spaceship
	 */
	protected static int getPlayerPositionY(EntityPlayer player)
	{
		if (player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntitySpaceship)
		{
			return (int) Math.floor(((GCCoreEntitySpaceship) player.ridingEntity).posY);
		}

		return (int) Math.floor(player.posY);
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
	protected static void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        final float var7 = 0.00390625F;
        final float var8 = 0.00390625F;
        final Tessellator var9 = Tessellator.instance;
        var9.startDrawingQuads();
        var9.addVertexWithUV((par1 + 0), (par2 + par6), 0.0, ((par3 + 0) * var7), ((par4 + par6) * var8));
        var9.addVertexWithUV((par1 + par5), (par2 + par6), 0.0, ((par3 + par5) * var7), ((par4 + par6) * var8));
        var9.addVertexWithUV((par1 + par5), (par2 + 0), 0.0, ((par3 + par5) * var7), ((par4 + 0) * var8));
        var9.addVertexWithUV((par1 + 0), (par2 + 0), 0.0, ((par3 + 0) * var7), ((par4 + 0) * var8));
        var9.draw();
    }

    /**
     * Downloads image and binds it
     *
     * @param par1Str texture to download and bind
     * @param par2Str if fails, use this one
     */
    protected static boolean loadDownloadableImageTexture(String par1Str, String par2Str)
    {
        final RenderEngine var3 = FMLClientHandler.instance().getClient().renderEngine;
        final int var4 = var3.getTextureForDownloadableImage(par1Str, par2Str);

        if (var4 >= 0)
        {
            var3.bindTexture(var4);
            return true;
        }
        else
        {
            return false;
        }
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
        final Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV(var1 - var7, var3 + var9, var5, 0.0D, 1.0D);
        t.addVertexWithUV(var1 + var7, var3 + var9, var5, 1.0D, 1.0D);
        t.addVertexWithUV(var1 + var7, var3 - var9, var5, 1.0D, 0.0D);
        t.addVertexWithUV(var1 - var7, var3 - var9, var5, 0.0D, 0.0D);
        t.draw();
    }
}
