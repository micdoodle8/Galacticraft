package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelFlag;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityFlag;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemFlag;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreRenderFlag.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreRenderFlag extends Render
{
	private static ResourceLocation[] flagTextures;

	static
	{
		GCCoreRenderFlag.flagTextures = new ResourceLocation[GCCoreItemFlag.names.length];

		for (int i = 0; i < GCCoreItemFlag.names.length; i++)
		{
			GCCoreRenderFlag.flagTextures[i] = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/flag/" + GCCoreItemFlag.names[i] + ".png");
		}
	}

	protected GCCoreModelFlag modelSpaceship;

	public GCCoreRenderFlag()
	{
		this.shadowSize = 1F;
		this.modelSpaceship = new GCCoreModelFlag();
	}

	protected ResourceLocation func_110779_a(GCCoreEntityFlag entity)
	{
		if (entity.getType() == -1)
		{
			return null;
		}

		return GCCoreRenderFlag.flagTextures[entity.getType()];
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return this.func_110779_a((GCCoreEntityFlag) entity);
	}

	public void renderSpaceship(GCCoreEntityFlag entity, double par2, double par4, double par6, float par8, float par9)
	{
		GL11.glPushMatrix();
		long var10 = entity.entityId * 493286711L;
		var10 = var10 * var10 * 4392167121L + var10 * 98761L;
		final float var12 = (((var10 >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		final float var13 = (((var10 >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		final float var14 = (((var10 >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		GL11.glTranslatef(var12, var13, var14);
		final float var24 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9;

		GL11.glTranslatef((float) par2, (float) par4, (float) par6);
		GL11.glRotatef(180.0F - par8, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-var24, 0.0F, 0.0F, 1.0F);
		this.bindEntityTexture(entity);
		GL11.glScalef(-1.0F, -1.0F, 1.0F);
		this.modelSpaceship.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		this.renderSpaceship((GCCoreEntityFlag) par1Entity, par2, par4, par6, par8, par9);
	}
}
