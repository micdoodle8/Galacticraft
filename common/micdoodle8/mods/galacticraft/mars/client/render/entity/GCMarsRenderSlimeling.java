package micdoodle8.mods.galacticraft.mars.client.render.entity;

import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.client.model.GCMarsModelSlimeling;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySlimeling;
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
        GL11.glTranslatef(0.0F, 1.18F, 0.0F);
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
}
