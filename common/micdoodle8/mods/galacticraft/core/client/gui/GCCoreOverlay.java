package micdoodle8.mods.galacticraft.core.client.gui;

import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityRocketT1;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;

/**
 * GCCoreOverlay.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreOverlay
{
	/**
	 * Get the player's spaceship height off ground
	 * 
	 * @param player
	 *            thePlayer
	 * @return position of player's spaceship
	 */
	protected static int getPlayerPositionY(EntityPlayer player)
	{
		if (player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntityRocketT1)
		{
			return (int) Math.floor(((GCCoreEntityRocketT1) player.ridingEntity).posY);
		}

		return (int) Math.floor(player.posY);
	}

	/**
	 * Draw a textured rectangle at the specified position
	 * 
	 * @param par1
	 *            xpos
	 * @param par2
	 *            ypos
	 * @param par3
	 *            u
	 * @param par4
	 *            v
	 * @param par5
	 *            width
	 * @param par6
	 *            height
	 */
	protected static void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
	{
		final float var7 = 0.00390625F;
		final float var8 = 0.00390625F;
		final Tessellator var9 = Tessellator.instance;
		var9.startDrawingQuads();
		var9.addVertexWithUV(par1 + 0, par2 + par6, 0.0, (par3 + 0) * var7, (par4 + par6) * var8);
		var9.addVertexWithUV(par1 + par5, par2 + par6, 0.0, (par3 + par5) * var7, (par4 + par6) * var8);
		var9.addVertexWithUV(par1 + par5, par2 + 0, 0.0, (par3 + par5) * var7, (par4 + 0) * var8);
		var9.addVertexWithUV(par1 + 0, par2 + 0, 0.0, (par3 + 0) * var7, (par4 + 0) * var8);
		var9.draw();
	}

	/**
	 * Draws a rectangle with middle at point specified
	 * 
	 * @param var1
	 *            x
	 * @param var3
	 *            y
	 * @param var5
	 *            depth
	 * @param var7
	 *            width
	 * @param var9
	 *            height
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
