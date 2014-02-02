package micdoodle8.mods.galacticraft.mars.client.render.entity;

import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityCargoRocket;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsRenderCargoRocket.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCMarsRenderCargoRocket extends Render
{
	private static final ResourceLocation cargoRocketTexture = new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/model/cargoRocket.png");

	protected IModelCustom rocketModel;

	public GCMarsRenderCargoRocket(IModelCustom model)
	{
		this.shadowSize = 0.5F;
		this.rocketModel = model;
	}

	protected ResourceLocation func_110779_a(GCMarsEntityCargoRocket par1EntityArrow)
	{
		return GCMarsRenderCargoRocket.cargoRocketTexture;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return this.func_110779_a((GCMarsEntityCargoRocket) par1Entity);
	}

	public void renderBuggy(GCMarsEntityCargoRocket entity, double par2, double par4, double par6, float par8, float par9)
	{
		GL11.glPushMatrix();
		final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;
		GL11.glTranslatef((float) par2, (float) par4, (float) par6);
		GL11.glScalef(0.4F, 0.4F, 0.4F);
		GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);

		this.bindTexture(GCMarsRenderCargoRocket.cargoRocketTexture);
		this.rocketModel.renderAll();

		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		this.renderBuggy((GCMarsEntityCargoRocket) par1Entity, par2, par4, par6, par8, par9);
	}
}
