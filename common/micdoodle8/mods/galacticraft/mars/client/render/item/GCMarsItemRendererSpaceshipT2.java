package micdoodle8.mods.galacticraft.mars.client.render.item;

import micdoodle8.mods.galacticraft.api.entity.IRocketType.EnumRocketType;
import micdoodle8.mods.galacticraft.core.client.render.item.GCCoreItemRendererSpaceship;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityRocketT1;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.client.model.GCMarsModelSpaceshipTier2;
import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityRocketT2;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

/**
 * GCMarsItemRendererSpaceshipT2.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsItemRendererSpaceshipT2 extends GCCoreItemRendererSpaceship
{
	private static final ResourceLocation cargoRocketTexture = new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/model/cargoRocket.png");
	private IModelCustom cargoRocketModel;

	public GCMarsItemRendererSpaceshipT2(IModelCustom cargoRocketModel)
	{
		super(new GCMarsEntityRocketT2(FMLClientHandler.instance().getClient().theWorld), new GCMarsModelSpaceshipTier2(), new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/model/rocketT2.png"));
		this.cargoRocketModel = cargoRocketModel;
	}

	@Override
	protected void renderSpaceship(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ)
	{
		GL11.glPushMatrix();

		this.transform(item, type);

		if (item.getItemDamage() < 10)
		{
			FMLClientHandler.instance().getClient().renderEngine.bindTexture(this.texture);
			this.modelSpaceship.render(this.spaceship, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
			GL11.glPopMatrix();
		}
		else
		{
			FMLClientHandler.instance().getClient().renderEngine.bindTexture(GCMarsItemRendererSpaceshipT2.cargoRocketTexture);
			this.cargoRocketModel.renderAll();
			GL11.glPopMatrix();
		}

		if (type == ItemRenderType.INVENTORY)
		{
			int index = Math.min(Math.max(item.getItemDamage() >= 10 ? item.getItemDamage() - 10 : item.getItemDamage(), 0), EnumRocketType.values().length - 1);

			if (EnumRocketType.values()[index].getInventorySpace() > 3)
			{
				final ModelChest modelChest = this.chestModel;
				FMLClientHandler.instance().getClient().renderEngine.bindTexture(GCCoreItemRendererSpaceship.chestTexture);

				GL11.glPushMatrix();
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glScalef(0.5F, -0.5F, -0.5F);
				GL11.glTranslatef(1.5F, 1.95F, 1.7F);
				final short short1 = 0;

				GL11.glRotatef(short1, 0.0F, 1.0F, 0.0F);
				GL11.glTranslatef(-1.5F, -1.5F, -1.5F);
				float f1 = 0;
				f1 = 1.0F - f1;
				f1 = 1.0F - f1 * f1 * f1;
				modelChest.chestLid.rotateAngleX = -(f1 * (float) Math.PI / 2.0F);

				modelChest.chestBelow.render(0.0625F);
				modelChest.chestLid.render(0.0625F);
				modelChest.chestKnob.render(0.0625F);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glPopMatrix();
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	@Override
	public void transform(ItemStack itemstack, ItemRenderType type)
	{
		final EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;
		long var10 = this.spaceship.entityId * 493286711L;
		var10 = var10 * var10 * 4392167121L + var10 * 98761L;
		final float var12 = (((var10 >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		final float var13 = (((var10 >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		final float var14 = (((var10 >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;

		if (type == ItemRenderType.EQUIPPED)
		{
			if (itemstack.getItemDamage() >= 10)
			{
				GL11.glTranslatef(0.5F, 0.2F, 0F);
			}

			GL11.glRotatef(70, 1.0F, 0, 0);
			GL11.glRotatef(-10, 0.0F, 1, 0);
			GL11.glRotatef(50, 0.0F, 1, 1);
			GL11.glTranslatef(0F, 2.0F, 0F);
			GL11.glScalef(5.2F, 5.2F, 5.2F);

			if (itemstack.getItemDamage() >= 10)
			{
				GL11.glTranslatef(0F, 0.45F, 0F);
				GL11.glScalef(0.45F, 0.45F, 0.45F);
			}

			if (player != null && player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntityRocketT1)
			{
				GL11.glScalef(0.0F, 0.0F, 0.0F);
			}
		}

		if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glTranslatef(-0.5F, 4.2F, 0F);

			if (itemstack.getItemDamage() >= 10)
			{
				GL11.glTranslatef(0F, 1.5F, -6.0F);
			}

			GL11.glRotatef(28, 0.0F, 0, 1);
			GL11.glRotatef(50 + 180, 0.0F, 1, 0);
			GL11.glRotatef(73, 1.0F, 0, 0);
			GL11.glScalef(5.2F, 5.2F, 5.2F);

			if (player != null && player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntityRocketT1)
			{
				GL11.glScalef(0.0F, 0.0F, 0.0F);
			}
		}

		GL11.glTranslatef(var12, var13 - 0.1F, var14);
		GL11.glScalef(-0.4F, -0.4F, 0.4F);

		if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY)
		{
			if (type == ItemRenderType.INVENTORY)
			{
				GL11.glRotatef(85F, 1F, 0F, 1F);
				GL11.glRotatef(20F, 1F, 0F, 0F);
				GL11.glScalef(0.9F, 0.9F, 0.9F);
			}
			else
			{
				GL11.glTranslatef(0, -0.9F, 0);
				GL11.glScalef(0.5F, 0.5F, 0.5F);
			}

			if (itemstack.getItemDamage() >= 10)
			{
				GL11.glRotatef(90F, 1F, 0F, 1F);
				GL11.glScalef(0.45F, 0.45F, 0.45F);
				GL11.glTranslatef(0, -0.9F, 0);
				GL11.glTranslatef(0, -0.9F, 0);
				GL11.glTranslatef(0, -0.9F, 0);
			}

			GL11.glScalef(1.3F, 1.3F, 1.3F);
			GL11.glTranslatef(0, -0.6F, 0);
			GL11.glRotatef(Sys.getTime() / 90F % 360F * (itemstack.getItemDamage() >= 10 ? -1 : 1), 0F, 1F, 0F);
		}
	}
}
