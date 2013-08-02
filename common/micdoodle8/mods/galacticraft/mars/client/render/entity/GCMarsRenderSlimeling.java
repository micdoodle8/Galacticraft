package micdoodle8.mods.galacticraft.mars.client.render.entity;

import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.client.model.GCMarsModelSlimeling;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySlimeling;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
@SideOnly(Side.CLIENT)
public class GCMarsRenderSlimeling extends RenderLiving
{
    private static final ResourceLocation landerTexture = new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/model/slimeling/green.png");

    public GCMarsRenderSlimeling()
    {
        super(new GCMarsModelSlimeling(16), 0.5F);
        
        this.renderPassModel = new GCMarsModelSlimeling(0.0F);
    }

    protected ResourceLocation func_110779_a(GCMarsEntitySlimeling par1EntityArrow)
    {
        return GCMarsRenderSlimeling.landerTexture;
    }

    @Override
    protected ResourceLocation func_110775_a(Entity par1Entity)
    {
        return this.func_110779_a((GCMarsEntitySlimeling) par1Entity);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2)
    {
        super.preRenderCallback(par1EntityLivingBase, par2);
        
        GL11.glRotatef(180.0F, 0, 1, 0);
        GCMarsEntitySlimeling slimeling = (GCMarsEntitySlimeling) par1EntityLivingBase;
        
        GL11.glColor3f(slimeling.getColorRed(), slimeling.getColorGreen(), slimeling.getColorBlue());
        GL11.glScalef(slimeling.getScale(), slimeling.getScale(), slimeling.getScale());
        GL11.glTranslatef(0.0F, 1.10F, 0.0F);
    }

    @Override
    protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
    {
        if (par2 == 0)
        {
            this.setRenderPassModel(this.renderPassModel);
            GL11.glEnable(GL11.GL_NORMALIZE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            return 1;
        }
        else
        {
            if (par2 == 1)
            {
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }

            return -1;
        }
    }

    @Override
    protected void passSpecialRender(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6)
    {
        this.renderLivingLabel(par1EntityLivingBase, ((GCMarsEntitySlimeling) par1EntityLivingBase).getName(), par2, par4 + 0.28, par6, 64);
        int health = Math.round(((GCMarsEntitySlimeling) par1EntityLivingBase).func_110143_aJ());
        int maxHealth = Math.round(((GCMarsEntitySlimeling) par1EntityLivingBase).func_110138_aP());
        int difference = maxHealth - health;
        
        if (difference < 7)
        {
            this.renderLivingLabelWithColor(par1EntityLivingBase, "" + health + " / " + maxHealth, par2, par4, par6, 64, 0, 1, 0);
        }
        else if (difference < 13)
        {
            this.renderLivingLabelWithColor(par1EntityLivingBase, "" + health + " / " + maxHealth, par2, par4, par6, 64, 1, 1, 0);
        }
        else
        {
            this.renderLivingLabelWithColor(par1EntityLivingBase, "" + health + " / " + maxHealth, par2, par4, par6, 64, 1, 0, 0);
        }
        
        super.passSpecialRender(par1EntityLivingBase, par2, par4, par6);
    }
    
    protected void renderLivingLabelWithColor(EntityLivingBase par1EntityLivingBase, String par2Str, double par3, double par5, double par7, int par9, float cR, float cG, float cB)
    {
        double d3 = par1EntityLivingBase.getDistanceSqToEntity(this.renderManager.livingPlayer);

        if (d3 <= (double)(par9 * par9))
        {
            FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            GL11.glPushMatrix();
            GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + par1EntityLivingBase.height + 0.5F, (float)par7);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-f1, -f1, f1);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Tessellator tessellator = Tessellator.instance;
            byte b0 = 0;

            if (par2Str.equals("deadmau5"))
            {
                b0 = -10;
            }

            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator.startDrawingQuads();
            int j = fontrenderer.getStringWidth(par2Str) / 2;
            tessellator.setColorRGBA_F(cR, cG, cB, 0.25F);
            tessellator.addVertex((double)(-j - 1), (double)(-1 + b0), 0.0D);
            tessellator.addVertex((double)(-j - 1), (double)(8 + b0), 0.0D);
            tessellator.addVertex((double)(j + 1), (double)(8 + b0), 0.0D);
            tessellator.addVertex((double)(j + 1), (double)(-1 + b0), 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            fontrenderer.drawString(par2Str, -fontrenderer.getStringWidth(par2Str) / 2, b0, 553648127);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            fontrenderer.drawString(par2Str, -fontrenderer.getStringWidth(par2Str) / 2, b0, -1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glPopMatrix();
        }
    }
}
