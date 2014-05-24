package micdoodle8.mods.galacticraft.core.client.render.entities;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.ModelEvolvedZombie;
import micdoodle8.mods.galacticraft.core.items.ItemSensorGlasses;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreRenderZombie.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
<<<<<<< HEAD:src/main/java/micdoodle8/mods/galacticraft/core/client/render/entities/RenderEvolvedZombie.java
public class RenderEvolvedZombie extends RenderLiving
=======
public class GCCoreRenderZombie extends RenderBiped
>>>>>>> 58f48f8b7e9a89c745a63e4440ff91be6c07e9bf:common/micdoodle8/mods/galacticraft/core/client/render/entities/GCCoreRenderZombie.java
{
	private static final ResourceLocation zombieTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/zombie.png");
	private static final ResourceLocation powerTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/power.png");

	private final ModelBase model = new ModelEvolvedZombie(0.2F);

	public RenderEvolvedZombie()
	{
<<<<<<< HEAD:src/main/java/micdoodle8/mods/galacticraft/core/client/render/entities/RenderEvolvedZombie.java
		super(new ModelEvolvedZombie(), 1.0F);
=======
		super(new GCCoreModelZombie(), 0.5F);
	}

	@Override
    protected void func_82421_b()
    {
        this.field_82423_g = new ModelZombie(1.0F, true);
        this.field_82425_h = new ModelZombie(0.5F, true);
    }

	protected ResourceLocation func_110779_a(GCCoreEntityZombie par1EntityArrow)
	{
		return GCCoreRenderZombie.zombieTexture;
>>>>>>> 58f48f8b7e9a89c745a63e4440ff91be6c07e9bf:common/micdoodle8/mods/galacticraft/core/client/render/entities/GCCoreRenderZombie.java
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return RenderEvolvedZombie.zombieTexture;
	}

	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2)
	{
		GL11.glScalef(1.2F, 1.2F, 1.2F);
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

		if (helmetSlot != null && helmetSlot.getItem() instanceof ItemSensorGlasses && minecraft.currentScreen == null)
		{
			if (par2 == 1)
			{
				final float var4 = par1EntityLiving.ticksExisted * 2 + par3;
				this.bindTexture(RenderEvolvedZombie.powerTexture);
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glLoadIdentity();
				final float var5 = var4 * 0.01F;
				final float var6 = var4 * 0.01F;
				GL11.glTranslatef(var5, var6, 0.0F);
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

		return super.shouldRenderPass(par1EntityLiving, par2, par2);
	}
}
