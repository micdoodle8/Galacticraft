package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityMeteorChunk;
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
public class GCCoreRenderMeteorChunk extends Render
{
    private static final ResourceLocation meteorChunkTexture = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/model/meteorChunk.png");

    protected IModelCustom meteorChunkModel;

    public GCCoreRenderMeteorChunk(IModelCustom model)
    {
        this.shadowSize = 0.1F;
        this.meteorChunkModel = model;
    }

    protected ResourceLocation func_110779_a(GCCoreEntityMeteorChunk par1EntityArrow)
    {
        return GCCoreRenderMeteorChunk.meteorChunkTexture;
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return this.func_110779_a((GCCoreEntityMeteorChunk) par1Entity);
    }

    public void renderMeteorChunk(GCCoreEntityMeteorChunk entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        final float var24 = entity.rotationPitch;
        final float var24b = entity.rotationYaw;
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glScalef(0.3F, 0.3F, 0.3F);
        GL11.glRotatef(var24b, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(var24, 0.0F, 0.0F, 1.0F);
        
        this.bindTexture(meteorChunkTexture);
        this.meteorChunkModel.renderAll();
        
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.renderMeteorChunk((GCCoreEntityMeteorChunk) par1Entity, par2, par4, par6, par8, par9);
    }
}
