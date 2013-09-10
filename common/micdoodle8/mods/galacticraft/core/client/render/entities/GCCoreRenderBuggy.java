package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;
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
public class GCCoreRenderBuggy extends Render
{
    private static final ResourceLocation buggyTextureBody = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/model/buggyMain.png");
    private static final ResourceLocation buggyTextureWheel = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/model/buggyWheels.png");
    private static final ResourceLocation buggyTextureStorage = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/model/buggyStorage.png");

    protected IModelCustom buggyModel;
    protected IModelCustom wheelModelRight;
    protected IModelCustom wheelModelLeft;

    public GCCoreRenderBuggy(IModelCustom model, IModelCustom wheelModelRight, IModelCustom wheelModelLeft)
    {
        this.shadowSize = 2F;
        this.buggyModel = model;
        this.wheelModelRight = wheelModelRight;
        this.wheelModelLeft = wheelModelLeft;
    }

    protected ResourceLocation func_110779_a(GCCoreEntityBuggy par1EntityArrow)
    {
        return GCCoreRenderBuggy.buggyTextureBody;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.func_110779_a((GCCoreEntityBuggy) par1Entity);
    }

    public void renderBuggy(GCCoreEntityBuggy entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;
        GL11.glTranslatef((float) par2, (float) par4 - 2.5F, (float) par6);
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(0.41F, 0.41F, 0.41F);
        this.bindTexture(buggyTextureWheel);
        
        float rotation = entity.wheelRotationX;
        
        // Front wheel covers
        GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 1.0F, -2.6F);
            GL11.glRotatef(entity.wheelRotationZ, 0, 1, 0);
            GL11.glTranslatef(1.4F, 0.0F, 0.0F);
            this.wheelModelRight.renderPart("WheelRightCover_Cover");
            GL11.glTranslatef(-2.8F, 0.0F, 0.0F);
            this.wheelModelLeft.renderPart("WheelLeftCover_Cover");
        GL11.glPopMatrix();
        
        // Back wheel covers
        GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 1.0F, 3.7F);
            GL11.glRotatef(-entity.wheelRotationZ, 0, 1, 0);
            GL11.glTranslatef(2.0F, 0.0F, 0.0F);
            this.wheelModelRight.renderPart("WheelRightCover_Cover");
            GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
            this.wheelModelLeft.renderPart("WheelLeftCover_Cover");
        GL11.glPopMatrix();
        
        // Front wheels
        GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 1.0F, -2.7F);
            GL11.glRotatef(entity.wheelRotationZ, 0, 1, 0);
            GL11.glRotatef(rotation, 1, 0, 0);
            GL11.glTranslatef(1.4F, 0.0F, 0.0F);
            this.wheelModelRight.renderPart("WheelRight_Wheel");
            GL11.glTranslatef(-2.8F, 0.0F, 0.0F);
            this.wheelModelLeft.renderPart("WheelLeft_Wheel");
        GL11.glPopMatrix();
        
        // Back wheels
        GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 1.0F, 3.6F);
            GL11.glRotatef(-entity.wheelRotationZ, 0, 1, 0);
            GL11.glRotatef(rotation, 1, 0, 0);
            GL11.glTranslatef(2.0F, 0.0F, 0.0F);
            this.wheelModelRight.renderPart("WheelRight_Wheel");
            GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
            this.wheelModelLeft.renderPart("WheelLeft_Wheel");
        GL11.glPopMatrix();
        
        this.bindTexture(buggyTextureBody);
        this.buggyModel.renderPart("MainBody");
        
        // Radar Dish
        GL11.glPushMatrix();
            GL11.glTranslatef(-1.178F, 4.1F, -2.397F);
            GL11.glRotatef(entity.radarDishRotation.floatX(), 1, 0, 0);
            GL11.glRotatef(entity.radarDishRotation.floatY(), 0, 1, 0);
            GL11.glRotatef(entity.radarDishRotation.floatZ(), 0, 0, 1);
            this.buggyModel.renderPart("RadarDish_Dish");
        GL11.glPopMatrix();
        
        this.bindTexture(buggyTextureStorage);

        if (entity.buggyType > 0)
        {
            this.buggyModel.renderPart("CargoLeft");
            
            if (entity.buggyType > 1)
            {
                this.buggyModel.renderPart("CargoMid");
                
                if (entity.buggyType > 2)
                {
                    this.buggyModel.renderPart("CargoRight");
                }
            }
        }
        
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderBuggy((GCCoreEntityBuggy) par1Entity, par2, par4, par6, par8, par9);
    }
}
