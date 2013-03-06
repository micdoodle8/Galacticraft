package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenCompressor;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
@SideOnly(Side.CLIENT)
public class GCCoreTileEntityOxygenCompressorRenderer extends TileEntitySpecialRenderer
{
    public void renderGCTileEntityTreasureChestAt(GCCoreTileEntityOxygenCompressor par1GCTileEntityTreasureChest, double par2, double par4, double par6, float par8)
    {
        this.drawStringAt(String.valueOf("P: " + Math.round(par1GCTileEntityTreasureChest.getPower() * 10) / 10.0D), par2 + 0.0F, par4 + 0.3F, par6 + 0.0F);
        this.drawStringAt(String.valueOf("S: " + par1GCTileEntityTreasureChest.getSourceCollectors().size()), par2 + 0.0F, par4 + 0.6F, par6 + 0.0F);
    }

    @Override
	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
    {
        this.renderGCTileEntityTreasureChestAt((GCCoreTileEntityOxygenCompressor)par1TileEntity, par2, par4, par6, par8);
    }
    
    private void drawStringAt(String name, double x, double y, double z)
    {
        FontRenderer var12 = RenderManager.instance.getFontRenderer();
        float var13 = 1.6F;
        float var14 = 0.016666668F * var13;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5F, (float)y + 1.3F, (float)z + 0.5F);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-var14, -var14, var14);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator var15 = Tessellator.instance;
        byte var16 = 0;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        var15.startDrawingQuads();
        int var17 = var12.getStringWidth(name) / 2;
        var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        var15.addVertex((double)(-var17 - 1), (double)(-1 + var16), 0.0D);
        var15.addVertex((double)(-var17 - 1), (double)(8 + var16), 0.0D);
        var15.addVertex((double)(var17 + 1), (double)(8 + var16), 0.0D);
        var15.addVertex((double)(var17 + 1), (double)(-1 + var16), 0.0D);
        var15.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        var12.drawString(name, -var12.getStringWidth(name) / 2, var16, 553648127);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        var12.drawString(name, -var12.getStringWidth(name) / 2, var16, -1);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
}
