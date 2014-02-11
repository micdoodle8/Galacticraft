package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelParaChest;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityParaChest;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreRenderParaChest.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreRenderParaChest extends Render
{
	private static final ResourceLocation parachestTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/parachest.png");

	private final GCCoreModelParaChest chestModel;

	public GCCoreRenderParaChest()
	{
		this.shadowSize = 1F;
		this.chestModel = new GCCoreModelParaChest();
	}

	protected ResourceLocation func_110779_a(Entity par1EntityArrow)
	{
		return GCCoreRenderParaChest.parachestTexture;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return this.func_110779_a(par1Entity);
	}

	public void doRenderParaChest(GCCoreEntityParaChest entity, double par2, double par4, double par6, float par8, float par9)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float) par2, (float) par4, (float) par6);

		this.bindEntityTexture(entity);

		if (!entity.isDead)
		{
			this.chestModel.renderAll();
		}
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		this.doRenderParaChest((GCCoreEntityParaChest) par1Entity, par2, par4, par6, par8, par9);
	}
}
