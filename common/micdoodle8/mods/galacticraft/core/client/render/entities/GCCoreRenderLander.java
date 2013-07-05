package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelLander;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityLander;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.resources.ResourceLocation;
import net.minecraft.entity.Entity;
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
public class GCCoreRenderLander extends Render
{
    private static final ResourceLocation landerTexture = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/model/lander.png");

    protected GCCoreModelLander landerModel;

    public GCCoreRenderLander()
    {
        this.shadowSize = 2F;
        this.landerModel = new GCCoreModelLander();
    }

    protected ResourceLocation func_110779_a(GCCoreEntityLander par1EntityArrow)
    {
        return GCCoreRenderLander.landerTexture;
    }

    @Override
    protected ResourceLocation func_110775_a(Entity par1Entity)
    {
        return this.func_110779_a((GCCoreEntityLander) par1Entity);
    }

    public void renderLander(GCCoreEntityLander entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;
        GL11.glTranslatef((float) par2, (float) par4 - 0.93F, (float) par6);
        GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
        this.func_110777_b(entity);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        this.landerModel.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderLander((GCCoreEntityLander) par1Entity, par2, par4, par6, par8, par9);
    }
}
