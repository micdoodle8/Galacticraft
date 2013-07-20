package micdoodle8.mods.galacticraft.mars.client.render.entity;
//package micdoodle8.mods.galacticraft.mars.client;
//
//import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityCreeperBoss;
//import net.minecraft.client.model.ModelBase;
//import net.minecraft.client.model.ModelCreeper;
//import net.minecraft.client.renderer.entity.RenderLiving;
//import net.minecraft.entity.EntityLiving;
//import org.lwjgl.opengl.GL11;
//
///**
// * Copyright 2012-2013, micdoodle8
// * 
// * All rights reserved.
// * 
// */
//public class GCMarsRenderCreeperBoss extends RenderLiving
//{
//    private final ModelBase creeperModel = new ModelCreeper(2.0F);
//
//    public GCMarsRenderCreeperBoss(GCMarsModelCreeperBoss GCModelCreeperBoss, float f)
//    {
//        super(new GCMarsModelCreeperBoss(), f);
//    }
//
//    protected void scaleMob(float f)
//    {
//        GL11.glScalef(f, f, f);
//    }
//
//    protected int updateCreeperColorMultiplier(GCMarsEntityCreeperBoss par1EntityCreeper, float par2, float par3)
//    {
//        final float var5 = par1EntityCreeper.setCreeperFlashTime(par3);
//
//        if ((int) (var5 * 10.0F) % 2 == 0)
//        {
//            return 0;
//        }
//        else
//        {
//            int var6 = (int) (var5 * 0.2F * 255.0F);
//
//            if (var6 < 0)
//            {
//                var6 = 0;
//            }
//
//            if (var6 > 255)
//            {
//                var6 = 255;
//            }
//
//            final short var7 = 255;
//            final short var8 = 255;
//            final short var9 = 255;
//            return var6 << 24 | var7 << 16 | var8 << 8 | var9;
//        }
//    }
//
//    protected int func_27006_a(GCMarsEntityCreeperBoss par1EntityCreeper, int par2, float par3)
//    {
//        if (par1EntityCreeper.headsRemaining == 1)
//        {
//            if (par2 == 1)
//            {
//                final float var4 = par1EntityCreeper.ticksExisted + par3;
//                this.loadTexture("/micdoodle8/mods/galacticraft/core/client/entities/power.png");
//                GL11.glMatrixMode(GL11.GL_TEXTURE);
//                GL11.glLoadIdentity();
//                final float var5 = var4 * 0.01F;
//                final float var6 = var4 * 0.01F;
//                GL11.glTranslatef(var5, var6, 0.0F);
//                this.setRenderPassModel(this.creeperModel);
//                GL11.glMatrixMode(GL11.GL_MODELVIEW);
//                GL11.glEnable(GL11.GL_BLEND);
//                final float var7 = 0.5F;
//                GL11.glColor4f(var7, var7, var7, 1.0F);
//                GL11.glDisable(GL11.GL_LIGHTING);
//                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
//                return 1;
//            }
//
//            if (par2 == 2)
//            {
//                GL11.glMatrixMode(GL11.GL_TEXTURE);
//                GL11.glLoadIdentity();
//                GL11.glMatrixMode(GL11.GL_MODELVIEW);
//                GL11.glEnable(GL11.GL_LIGHTING);
//                GL11.glDisable(GL11.GL_BLEND);
//            }
//        }
//
//        return -1;
//    }
//
//    protected int func_27007_b(GCMarsEntityCreeperBoss par1EntityCreeper, int par2, float par3)
//    {
//        return -1;
//    }
//
//    @Override
//    protected void preRenderCallback(EntityLiving par1EntityLiving, float par2)
//    {
//        this.scaleMob(4.0F);
//    }
//
//    @Override
//    protected int getColorMultiplier(EntityLiving par1EntityLiving, float par2, float par3)
//    {
//        return this.updateCreeperColorMultiplier((GCMarsEntityCreeperBoss) par1EntityLiving, par2, par3);
//    }
//
//    @Override
//    protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
//    {
//        return this.func_27006_a((GCMarsEntityCreeperBoss) par1EntityLiving, par2, par3);
//    }
//
//    @Override
//    protected int inheritRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
//    {
//        return this.func_27007_b((GCMarsEntityCreeperBoss) par1EntityLiving, par2, par3);
//    }
// }
