package micdoodle8.mods.galacticraft.mars.client.render.entity;

import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.client.model.GCMarsModelSludgeling;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntitySludgeling;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsRenderSludgeling.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCMarsRenderSludgeling extends RenderLiving
{
	private static final ResourceLocation sludgelingTexture = new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/model/sludgeling.png");

	public GCMarsRenderSludgeling()
	{
		super(new GCMarsModelSludgeling(), 0.3F);
	}

	protected ResourceLocation func_110779_a(GCMarsEntitySludgeling par1EntityArrow)
	{
		return GCMarsRenderSludgeling.sludgelingTexture;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return this.func_110779_a((GCMarsEntitySludgeling) par1Entity);
	}

	public void renderSilverfish(GCMarsEntitySludgeling par1EntitySilverfish, double par2, double par4, double par6, float par8, float par9)
	{
		super.doRenderLiving(par1EntitySilverfish, par2, par4, par6, par8, par9);
	}

	@Override
	protected float getDeathMaxRotation(EntityLivingBase par1EntityLiving)
	{
		return 180.0F;
	}

	@Override
	protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3)
	{
		return -1;
	}

	@Override
	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
	{
		this.renderSilverfish((GCMarsEntitySludgeling) par1EntityLiving, par2, par4, par6, par8, par9);
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		this.renderSilverfish((GCMarsEntitySludgeling) par1Entity, par2, par4, par6, par8, par9);
	}
}
