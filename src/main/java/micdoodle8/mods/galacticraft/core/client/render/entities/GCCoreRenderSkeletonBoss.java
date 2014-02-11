package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelSkeletonBoss;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntitySkeletonBoss;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemSensorGlasses;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreRenderSkeletonBoss.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreRenderSkeletonBoss extends RenderLiving
{
	private static final ResourceLocation skeletonBossTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/skeletonboss.png");
	private static final ResourceLocation powerTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/power.png");

	private final GCCoreModelSkeletonBoss model = new GCCoreModelSkeletonBoss();

	public GCCoreRenderSkeletonBoss()
	{
		super(new GCCoreModelSkeletonBoss(), 1.0F);
	}

	protected ResourceLocation func_110779_a(GCCoreEntitySkeletonBoss par1EntityArrow)
	{
		return GCCoreRenderSkeletonBoss.skeletonBossTexture;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return this.func_110779_a((GCCoreEntitySkeletonBoss) par1Entity);
	}

	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2)
	{
		GL11.glScalef(1.2F, 1.2F, 1.2F);
	}

	@Override
	public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
	{
		BossStatus.setBossStatus((IBossDisplayData) par1EntityLiving, false);

		super.doRenderLiving(par1EntityLiving, par2, par4, par6, par8, par9);
	}

	@Override
	protected void renderEquippedItems(EntityLivingBase par1EntityLiving, float par2)
	{
		if (((GCCoreEntitySkeletonBoss) par1EntityLiving).throwTimer + ((GCCoreEntitySkeletonBoss) par1EntityLiving).postThrowDelay == 0)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(-0.3F, -1.6F, -1.2F);
			GL11.glTranslatef(0.1F, 0.0F, 0.0F);
			GL11.glRotatef(41, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-20, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-20, 0.0F, 0.0F, 1.0F);
			GL11.glScalef(0.7F, 0.7F, 0.7F);
			this.renderManager.itemRenderer.renderItem(par1EntityLiving, new ItemStack(Item.bow), 0);
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			GL11.glTranslatef(0.11F, -1.6F, -1.2F);
			GL11.glTranslatef(0.1F, 0.0F, 0.0F);
			GL11.glRotatef(46, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-20, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-20, 0.0F, 0.0F, 1.0F);
			GL11.glScalef(0.7F, 0.7F, 0.7F);
			this.renderManager.itemRenderer.renderItem(par1EntityLiving, new ItemStack(Item.bow), 0);
			GL11.glPopMatrix();
		}
	}

	@Override
	protected int shouldRenderPass(EntityLivingBase par1EntityLiving, int par2, float par3)
	{
		final Minecraft minecraft = FMLClientHandler.instance().getClient();

		final EntityPlayerSP player = minecraft.thePlayer;

		ItemStack helmetSlot = null;

		if (player != null && player.inventory.armorItemInSlot(3) != null)
		{
			helmetSlot = player.inventory.armorItemInSlot(3);
		}

		if (helmetSlot != null && helmetSlot.getItem() instanceof GCCoreItemSensorGlasses && minecraft.currentScreen == null)
		{
			if (par2 == 1)
			{
				final float var4 = par1EntityLiving.ticksExisted * 2 + par3;
				this.bindTexture(GCCoreRenderSkeletonBoss.powerTexture);
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glLoadIdentity();
				final float var5 = var4 * 0.01F;
				final float var6 = var4 * 0.01F;
				GL11.glTranslatef(var5, var6, 0.0F);
				// this.model.aimedBow = true;
				this.setRenderPassModel(this.model);
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
}
