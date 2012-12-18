package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelSpaceship;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySpaceship;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
@SideOnly(Side.CLIENT)
public class GCCoreRenderSpaceship extends Render
{
    /** instance of ModelMinecart for rendering */
    protected ModelBase modelSpaceship;

    public GCCoreRenderSpaceship()
    {
        this.shadowSize = 2F;
        this.modelSpaceship = new GCCoreModelSpaceship();
    }

    public void renderSpaceship(GCCoreEntitySpaceship par1GCEntitySpaceship, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        final float var24 = par1GCEntitySpaceship.prevRotationPitch + (par1GCEntitySpaceship.rotationPitch - par1GCEntitySpaceship.prevRotationPitch) * par9;

        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
        final float var28 = par1GCEntitySpaceship.func_70496_j() - par9;
        float var30 = par1GCEntitySpaceship.getDamage() - par9;

        if (var30 < 0.0F)
        {
            var30 = 0.0F;
        }

        if (var28 > 0.0F)
        {
        	final float i = par1GCEntitySpaceship.getLaunched() == 0 ? (5 - MathHelper.floor_double(par1GCEntitySpaceship.getTimeUntilLaunch() / 85)) / 10F : 0.3F;
//            GL11.glRotatef(MathHelper.sin(var28) * var28 * i * par1GCEntitySpaceship.func_70493_k() * par9, 1.0F, 0.0F, 0.0F);
//            GL11.glRotatef(MathHelper.sin(var28) * var28 * i * par1GCEntitySpaceship.func_70493_k() * par9, 1.0F, 0.0F, 1.0F);
        }

        this.loadTexture("/micdoodle8/mods/galacticraft/core/client/entities/spaceship1.png");
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.modelSpaceship.render(par1GCEntitySpaceship, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
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
        this.renderSpaceship((GCCoreEntitySpaceship)par1Entity, par2, par4, par6, par8, par9);
    }
}
