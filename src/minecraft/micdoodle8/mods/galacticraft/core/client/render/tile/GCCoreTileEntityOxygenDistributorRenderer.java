package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
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
    	int x, y, z;
    	x = MathHelper.floor_double(par2 + 0.5D);
    	y = MathHelper.floor_double(par4 + 0.5D);
    	z = MathHelper.floor_double(par6 + 0.5D);
    	
		this.bindTextureByName("/micdoodle8/mods/galacticraft/core/client/entities/fan.png");

      	GL11.glPushMatrix();
      	GL11.glEnable(GL12.GL_RESCALE_NORMAL);
      	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    GL11.glTranslatef((float)par2, (float)par4, (float)par6);
	    GL11.glScalef(1.0F, -1.0F, -1.0F);
      	GL11.glTranslatef(0.1F, -0.5F, -0.5F);

      	GL11.glRotatef(90, 0.0F, 0.0F, 1.0F);

    	if (par1GCTileEntityTreasureChest.currentPower > 0)
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

    	if (par1GCTileEntityTreasureChest.currentPower > 0)
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

    	if (par1GCTileEntityTreasureChest.currentPower > 0)
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

    	if (par1GCTileEntityTreasureChest.currentPower > 0)
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
}
