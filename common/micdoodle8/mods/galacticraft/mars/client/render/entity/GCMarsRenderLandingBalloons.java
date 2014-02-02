package micdoodle8.mods.galacticraft.mars.client.render.entity;

import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.client.model.GCMarsModelBalloonParachute;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityLandingBalloons;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsRenderLandingBalloons.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCMarsRenderLandingBalloons extends Render
{
	private static final ResourceLocation landerTexture = new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/model/landingBalloon.png");

	protected IModelCustom landerModel;
	protected GCMarsModelBalloonParachute parachuteModel = new GCMarsModelBalloonParachute();

	public GCMarsRenderLandingBalloons()
	{
		this.shadowSize = 2F;
		this.landerModel = AdvancedModelLoader.loadModel("/assets/galacticraftmars/models/landingBalloon.obj");
	}

	protected ResourceLocation func_110779_a(GCMarsEntityLandingBalloons par1EntityArrow)
	{
		return GCMarsRenderLandingBalloons.landerTexture;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return this.func_110779_a((GCMarsEntityLandingBalloons) par1Entity);
	}

	public void renderLander(GCMarsEntityLandingBalloons entity, double par2, double par4, double par6, float par8, float par9)
	{
		GL11.glPushMatrix();
		final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;
		GL11.glTranslatef((float) par2, (float) par4 - 0.93F, (float) par6);
		GL11.glRotatef(par8, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(var24, 0.0F, 0.0F, 1.0F);
		this.bindEntityTexture(entity);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		this.landerModel.renderAll();
		GL11.glPopMatrix();

		if (entity.posY >= 500.0F)
		{
			GL11.glPushMatrix();

			GL11.glTranslatef((float) par2 - 1.25F, (float) par4 - 0.93F, (float) par6 - 0.3F);
			GL11.glScalef(2.5F, 3.0F, 2.5F);
			this.parachuteModel.renderAll();

			GL11.glPopMatrix();
		}
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		this.renderLander((GCMarsEntityLandingBalloons) par1Entity, par2, par4, par6, par8, par9);
	}
}
