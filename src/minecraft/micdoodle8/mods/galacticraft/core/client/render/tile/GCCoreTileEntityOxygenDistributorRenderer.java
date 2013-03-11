package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
 *
 */
@SideOnly(Side.CLIENT)
public class GCCoreTileEntityOxygenDistributorRenderer extends TileEntitySpecialRenderer
{
    public void renderGCTileEntityTreasureChestAt(GCCoreTileEntityOxygenDistributor par1GCTileEntityTreasureChest, double par2, double par4, double par6, float par8)
    {
        this.drawStringAt(String.valueOf("P: " + Math.round(par1GCTileEntityTreasureChest.currentPower * 10) / 10.0D), par2 + 0.0F, par4 + 0.3F, par6 + 0.0F);
//        this.drawStringAt(String.valueOf("S: " + par1GCTileEntityTreasureChest.getSourceCollectors().size()), par2 + 0.0F, par4 + 0.6F, par6 + 0.0F);

    	MathHelper.floor_double(par2 + 0.5D);
    	MathHelper.floor_double(par4 + 0.5D);
    	MathHelper.floor_double(par6 + 0.5D);

		this.bindTextureByName("/micdoodle8/mods/galacticraft/core/client/entities/fan.png");

      	GL11.glPushMatrix();
      	GL11.glEnable(GL12.GL_RESCALE_NORMAL);
      	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    GL11.glTranslatef((float)par2, (float)par4, (float)par6);
	    GL11.glScalef(1.0F, -1.0F, -1.0F);
      	GL11.glTranslatef(0.1F, -0.5F, -0.5F);

      	GL11.glRotatef(90, 0.0F, 0.0F, 1.0F);

    	if (par1GCTileEntityTreasureChest.active)
    	{
    		for (int i = 0; i < 4; i++)
    		{
    			for (int j = 0; j < 3; j++)
    			{
    				par1GCTileEntityTreasureChest.fanModel1.fans[i][j].rotateAngleY += par1GCTileEntityTreasureChest.currentPower / 100.0F;
    			}
    		}

    		par1GCTileEntityTreasureChest.fanModel1.center.rotateAngleY += par1GCTileEntityTreasureChest.currentPower / 100.0F;
    	}
    	else
    	{
    		for (int i = 0; i < 4; i++)
    		{
    			for (int j = 0; j < 3; j++)
    			{
    				par1GCTileEntityTreasureChest.fanModel1.fans[i][j].rotateAngleY = 0;
    			}
    		}

    		par1GCTileEntityTreasureChest.fanModel1.center.rotateAngleY = 0;
    	}

    	par1GCTileEntityTreasureChest.fanModel1.renderAll();
      	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
      	GL11.glPopMatrix();
      	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

      	this.bindTextureByName("/micdoodle8/mods/galacticraft/core/client/entities/fan.png");

      	GL11.glPushMatrix();
      	GL11.glEnable(GL12.GL_RESCALE_NORMAL);
      	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    GL11.glTranslatef((float)par2, (float)par4, (float)par6);
	    GL11.glScalef(1.0F, -1.0F, -1.0F);
      	GL11.glTranslatef(0.5F, -0.5F, -0.1F);

      	GL11.glRotatef(90, 0.0F, 1.0F, 0.0F);
      	GL11.glRotatef(90, 0.0F, 0.0F, 1.0F);

    	if (par1GCTileEntityTreasureChest.active)
    	{
    		for (int i = 0; i < 4; i++)
    		{
    			for (int j = 0; j < 3; j++)
    			{
    				par1GCTileEntityTreasureChest.fanModel2.fans[i][j].rotateAngleY += par1GCTileEntityTreasureChest.currentPower / 100.0F;
    			}
    		}

    		par1GCTileEntityTreasureChest.fanModel2.center.rotateAngleY += par1GCTileEntityTreasureChest.currentPower / 100.0F;
    	}

    	par1GCTileEntityTreasureChest.fanModel2.renderAll();
      	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
      	GL11.glPopMatrix();
      	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);


      	this.bindTextureByName("/micdoodle8/mods/galacticraft/core/client/entities/fan.png");

      	GL11.glPushMatrix();
      	GL11.glEnable(GL12.GL_RESCALE_NORMAL);
      	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    GL11.glTranslatef((float)par2, (float)par4, (float)par6);
	    GL11.glScalef(1.0F, -1.0F, -1.0F);
      	GL11.glTranslatef(0.9F, -0.5F, -0.5F);

      	GL11.glRotatef(180, 0.0F, 1.0F, 0.0F);
      	GL11.glRotatef(90, 0.0F, 0.0F, 1.0F);

    	if (par1GCTileEntityTreasureChest.active)
    	{
    		for (int i = 0; i < 4; i++)
    		{
    			for (int j = 0; j < 3; j++)
    			{
    				par1GCTileEntityTreasureChest.fanModel3.fans[i][j].rotateAngleY += par1GCTileEntityTreasureChest.currentPower / 100.0F;
    			}
    		}

    		par1GCTileEntityTreasureChest.fanModel3.center.rotateAngleY += par1GCTileEntityTreasureChest.currentPower / 100.0F;
    	}

    	par1GCTileEntityTreasureChest.fanModel3.renderAll();
      	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
      	GL11.glPopMatrix();
      	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

      	this.bindTextureByName("/micdoodle8/mods/galacticraft/core/client/entities/fan.png");

      	GL11.glPushMatrix();
      	GL11.glEnable(GL12.GL_RESCALE_NORMAL);
      	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    GL11.glTranslatef((float)par2, (float)par4, (float)par6);
	    GL11.glScalef(1.0F, -1.0F, -1.0F);
      	GL11.glTranslatef(0.5F, -0.5F, -0.9F);

      	GL11.glRotatef(270, 0.0F, 1.0F, 0.0F);
      	GL11.glRotatef(90, 0.0F, 0.0F, 1.0F);

    	if (par1GCTileEntityTreasureChest.active)
    	{
			for (int i = 0; i < 4; i++)
			{
				for (int j = 0; j < 3; j++)
				{
					par1GCTileEntityTreasureChest.fanModel4.fans[i][j].rotateAngleY += par1GCTileEntityTreasureChest.currentPower / 100.0F;
				}
			}

			par1GCTileEntityTreasureChest.fanModel4.center.rotateAngleY += par1GCTileEntityTreasureChest.currentPower / 100.0F;
    	}

    	par1GCTileEntityTreasureChest.fanModel4.renderAll();
      	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
      	GL11.glPopMatrix();
      	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
    {
        this.renderGCTileEntityTreasureChestAt((GCCoreTileEntityOxygenDistributor)par1TileEntity, par2, par4, par6, par8);
    }

    private void drawStringAt(String name, double x, double y, double z)
    {
        final FontRenderer var12 = RenderManager.instance.getFontRenderer();
        final float var13 = 1.6F;
        final float var14 = 0.016666668F * var13;
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
        final Tessellator var15 = Tessellator.instance;
        final byte var16 = 0;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        var15.startDrawingQuads();
        final int var17 = var12.getStringWidth(name) / 2;
        var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        var15.addVertex((-var17 - 1), (-1 + var16), 0.0D);
        var15.addVertex((-var17 - 1), (8 + var16), 0.0D);
        var15.addVertex((var17 + 1), (8 + var16), 0.0D);
        var15.addVertex((var17 + 1), (-1 + var16), 0.0D);
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
