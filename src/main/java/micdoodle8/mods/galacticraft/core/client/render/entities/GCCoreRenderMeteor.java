package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelMeteor;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityMeteor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreRenderMeteor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreRenderMeteor extends Render
{
	private static final ResourceLocation meteorTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/meteor.png");

	private final GCCoreModelMeteor modelMeteor;

	public GCCoreRenderMeteor()
	{
		this.shadowSize = 1F;
		this.modelMeteor = new GCCoreModelMeteor();
	}

	protected ResourceLocation func_110779_a(GCCoreEntityMeteor entity)
	{
		return GCCoreRenderMeteor.meteorTexture;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return this.func_110779_a((GCCoreEntityMeteor) par1Entity);
	}

	public void doRenderMeteor(GCCoreEntityMeteor entity, double par2, double par4, double par6, float par8, float par9)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float) par2, (float) par4, (float) par6);
		GL11.glRotatef(par8, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(par8, 1.0F, 0.0F, 0.0F);
		final float f = entity.getSize();
		GL11.glScalef(f / 2, f / 2, f / 2);
		this.bindEntityTexture(entity);
		this.modelMeteor.render(entity, 0.0F, 0.0F, -0.5F, 0.0F, 0.0F, 0.1F);
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		this.doRenderMeteor((GCCoreEntityMeteor) par1Entity, par2, par4, par6, par8, par9);
	}
}
