package micdoodle8.mods.galacticraft.mars.client.render.entity;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.client.model.GCMarsModelCreeperBoss;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityCreeperBoss;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

/**
 * GCMarsRenderCreeperBoss.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsRenderCreeperBoss extends RenderLiving
{
	private static final ResourceLocation creeperTexture = new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/model/creeper.png");
	private static final ResourceLocation powerTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/power.png");
	private final ModelBase creeperModel = new GCMarsModelCreeperBoss(2.0F);

	public GCMarsRenderCreeperBoss()
	{
		super(new GCMarsModelCreeperBoss(), 1.0F);
	}

	protected void scaleMob(float f)
	{
		GL11.glScalef(f, f, f);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return GCMarsRenderCreeperBoss.creeperTexture;
	}

	@Override
	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
	{
		BossStatus.setBossStatus((IBossDisplayData) par1EntityLiving, false);

		super.doRenderLiving(par1EntityLiving, par2, par4, par6, par8, par9);
	}

	protected int func_27006_a(GCMarsEntityCreeperBoss par1EntityCreeper, int par2, float par3)
	{
		if (par1EntityCreeper.headsRemaining == 1)
		{
			if (par2 == 1)
			{
				final float var4 = par1EntityCreeper.ticksExisted + par3;
				this.bindTexture(GCMarsRenderCreeperBoss.powerTexture);
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glLoadIdentity();
				final float var5 = var4 * 0.01F;
				final float var6 = var4 * 0.01F;
				GL11.glTranslatef(var5, var6, 0.0F);
				this.setRenderPassModel(this.creeperModel);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glEnable(GL11.GL_BLEND);
				final float var7 = 0.5F;
				GL11.glColor4f(var7, var7, var7, 1.0F);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
				return 1;
			}

			if (par2 == 2)
			{
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glLoadIdentity();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_BLEND);
			}
		}

		return -1;
	}

	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2)
	{
		this.scaleMob(4.0F);
	}

	@Override
	protected int getColorMultiplier(EntityLivingBase par1EntityLivingBase, float par2, float par3)
	{
		return super.getColorMultiplier(par1EntityLivingBase, par2, par3);
	}

	@Override
	protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
	{
		return this.func_27006_a((GCMarsEntityCreeperBoss) par1EntityLivingBase, par2, par3);
	}

	@Override
	protected int inheritRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
	{
		return -1;
	}
}
