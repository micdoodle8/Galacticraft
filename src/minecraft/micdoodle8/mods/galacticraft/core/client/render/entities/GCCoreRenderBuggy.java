package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelBuggy;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.relauncher.Side;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
@SideOnly(Side.CLIENT)
public class GCCoreRenderBuggy extends Render
{
    /** instance of ModelMinecart for rendering */
    protected GCCoreModelBuggy modelSpaceship;
    
	float turn = 0;

    public GCCoreRenderBuggy()
    {
        this.shadowSize = 2F;
        this.modelSpaceship = new GCCoreModelBuggy();
    }

    public void renderSpaceship(GCCoreEntityBuggy entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        final double var15 = entity.prevPosX + (entity.posX - entity.prevPosX) * par9;
        final double var17 = entity.prevPosY + (entity.posY - entity.prevPosY) * par9;
        final double var19 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * par9;
        final double var21 = 0.30000001192092896D;
        final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;

        GL11.glTranslatef((float)par2, (float)par4 - 1, (float)par6);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
        
        this.modelSpaceship.radarCenter.rotateAngleY += 0.01F;
        
        if (entity instanceof GCCoreEntityBuggy && entity.riddenByEntity != null)
        {
        	GCCoreEntityBuggy buggy = (GCCoreEntityBuggy)entity;
        	
        	GL11.glPushMatrix();
        	this.modelSpaceship.wheel1a.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel2a.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel3a.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel4a.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel1b.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel2b.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel3b.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel4b.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel1c.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel2c.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel3c.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel4c.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel1d.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel2d.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel3d.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel4d.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel1e.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel2e.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel3e.rotateAngleY = buggy.turnProgress;
        	this.modelSpaceship.wheel4e.rotateAngleY = buggy.turnProgress;
            GL11.glPopMatrix();
        }

        this.loadTexture("/micdoodle8/mods/galacticraft/core/client/entities/buggy.png");
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.modelSpaceship.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderSpaceship((GCCoreEntityBuggy)par1Entity, par2, par4, par6, par8, par9);
    }
}
