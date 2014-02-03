package micdoodle8.mods.galacticraft.core.client.render.item;

import micdoodle8.mods.galacticraft.api.entity.IRocketType.EnumRocketType;
import micdoodle8.mods.galacticraft.api.prefab.entity.EntitySpaceshipBase;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityRocketT1;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

/**
 * GCCoreItemRendererSpaceship.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreItemRendererSpaceship implements IItemRenderer
{
	protected static final ResourceLocation chestTexture = new ResourceLocation("textures/entity/chest/normal.png");

	protected EntitySpaceshipBase spaceship;
	protected ModelBase modelSpaceship;
	protected final ModelChest chestModel = new ModelChest();

	protected static RenderItem drawItems = new RenderItem();

	protected ResourceLocation texture;

	public GCCoreItemRendererSpaceship(EntitySpaceshipBase spaceship, ModelBase model, ResourceLocation texture)
	{
		this.spaceship = spaceship;
		this.modelSpaceship = model;
		this.texture = texture;
	}

	protected void renderSpaceship(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ)
	{
		GL11.glPushMatrix();

		this.transform(item, type);

		FMLClientHandler.instance().getClient().renderEngine.bindTexture(this.texture);
		this.modelSpaceship.render(this.spaceship, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		GL11.glPopMatrix();

		if (type == ItemRenderType.INVENTORY)
		{
			int index = Math.min(Math.max(item.getItemDamage(), 0), EnumRocketType.values().length - 1);
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
			GL11.glRotatef(70, 1.0F, 0, 0);
			GL11.glRotatef(-10, 0.0F, 1, 0);
			GL11.glRotatef(50, 0.0F, 1, 1);
			GL11.glTranslatef(0F, 2.0F, 0F);
			GL11.glScalef(5.2F, 5.2F, 5.2F);

			if (player != null && player.ridingEntity != null && player.ridingEntity instanceof GCCoreEntityRocketT1)
			{
				GL11.glScalef(0.0F, 0.0F, 0.0F);
			}
		}

		if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glTranslatef(-0.5F, 4.2F, 0F);
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

			GL11.glScalef(1.3F, 1.3F, 1.3F);
			GL11.glTranslatef(0, -0.6F, 0);
			GL11.glRotatef(Sys.getTime() / 90F % 360F, 0F, 1F, 0F);
		}
	}

	/** IItemRenderer implementation **/

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		switch (type)
		{
		case ENTITY:
			return true;
		case EQUIPPED:
			return true;
		case EQUIPPED_FIRST_PERSON:
			return true;
		case INVENTORY:
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		switch (type)
		{
		case EQUIPPED:
			this.renderSpaceship(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		case EQUIPPED_FIRST_PERSON:
			this.renderSpaceship(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		case INVENTORY:
			this.renderSpaceship(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		case ENTITY:
			this.renderSpaceship(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		default:
		}
	}

}
