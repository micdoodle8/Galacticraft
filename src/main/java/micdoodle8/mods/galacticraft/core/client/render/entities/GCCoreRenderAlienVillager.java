package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelVillager;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityAlienVillager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreRenderAlienVillager.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreRenderAlienVillager extends RenderLiving
{
	private static final ResourceLocation villagerTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/villager.png");

	protected GCCoreModelVillager villagerModel;

	public GCCoreRenderAlienVillager()
	{
		super(new GCCoreModelVillager(0.0F), 0.5F);
		this.villagerModel = (GCCoreModelVillager) this.mainModel;
	}

	protected int shouldVillagerRenderPass(GCCoreEntityAlienVillager par1EntityVillager, int par2, float par3)
	{
		return -1;
	}

	public void renderVillager(GCCoreEntityAlienVillager par1EntityVillager, double par2, double par4, double par6, float par8, float par9)
	{
		super.doRenderLiving(par1EntityVillager, par2, par4, par6, par8, par9);
	}

	protected ResourceLocation func_110902_a(GCCoreEntityAlienVillager par1EntityVillager)
	{
		return GCCoreRenderAlienVillager.villagerTexture;
	}

	protected void renderVillagerEquipedItems(GCCoreEntityAlienVillager par1EntityVillager, float par2)
	{
		super.renderEquippedItems(par1EntityVillager, par2);
	}

	protected void preRenderVillager(GCCoreEntityAlienVillager par1EntityVillager, float par2)
	{
		float f1 = 0.9375F;

		if (par1EntityVillager.getGrowingAge() < 0)
		{
			f1 = (float) (f1 * 0.5D);
			this.shadowSize = 0.25F;
		}
		else
		{
			this.shadowSize = 0.5F;
		}

		GL11.glScalef(f1, f1, f1);
	}

	@Override
	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
	{
		this.renderVillager((GCCoreEntityAlienVillager) par1EntityLiving, par2, par4, par6, par8, par9);
	}

	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLivingBase, float par2)
	{
		this.preRenderVillager((GCCoreEntityAlienVillager) par1EntityLivingBase, par2);
	}

	@Override
	protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
	{
		return this.shouldVillagerRenderPass((GCCoreEntityAlienVillager) par1EntityLivingBase, par2, par3);
	}

	@Override
	protected void renderEquippedItems(EntityLivingBase par1EntityLivingBase, float par2)
	{
		this.renderVillagerEquipedItems((GCCoreEntityAlienVillager) par1EntityLivingBase, par2);
	}

	@Override
	public void renderPlayer(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, float par8, float par9)
	{
		this.renderVillager((GCCoreEntityAlienVillager) par1EntityLivingBase, par2, par4, par6, par8, par9);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return this.func_110902_a((GCCoreEntityAlienVillager) par1Entity);
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		this.renderVillager((GCCoreEntityAlienVillager) par1Entity, par2, par4, par6, par8, par9);
	}
}
