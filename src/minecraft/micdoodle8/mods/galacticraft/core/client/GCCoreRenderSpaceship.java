package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.core.GCCoreEntitySpaceship;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBase;
import net.minecraft.src.Render;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.FMLLog;
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
        long var10 = (long)par1GCEntitySpaceship.entityId * 493286711L;
        var10 = var10 * var10 * 4392167121L + var10 * 98761L;
        float var12 = (((float)(var10 >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float var13 = (((float)(var10 >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        float var14 = (((float)(var10 >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
        GL11.glTranslatef(var12, var13, var14);
        double var15 = par1GCEntitySpaceship.prevPosX + (par1GCEntitySpaceship.posX - par1GCEntitySpaceship.prevPosX) * (double)par9;
        double var17 = par1GCEntitySpaceship.prevPosY + (par1GCEntitySpaceship.posY - par1GCEntitySpaceship.prevPosY) * (double)par9;
        double var19 = par1GCEntitySpaceship.prevPosZ + (par1GCEntitySpaceship.posZ - par1GCEntitySpaceship.prevPosZ) * (double)par9;
        double var21 = 0.30000001192092896D;
        float var24 = par1GCEntitySpaceship.prevRotationPitch + (par1GCEntitySpaceship.rotationPitch - par1GCEntitySpaceship.prevRotationPitch) * par9;

        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
        float var28 = (float)par1GCEntitySpaceship.func_70496_j() - par9;
        float var30 = (float)par1GCEntitySpaceship.getDamage() - par9;

        if (var30 < 0.0F)
        {
            var30 = 0.0F;
        }

        if (var28 > 0.0F)
        {
            GL11.glRotatef(MathHelper.sin(var28) * var28 * 3 / 10.0F * (float)par1GCEntitySpaceship.func_70493_k(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(MathHelper.sin(var28) * var28 * 3 / 10.0F * (float)par1GCEntitySpaceship.func_70493_k(), 1.0F, 0.0F, 1.0F);
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
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderSpaceship((GCCoreEntitySpaceship)par1Entity, par2, par4, par6, par8, par9);
    }
}
