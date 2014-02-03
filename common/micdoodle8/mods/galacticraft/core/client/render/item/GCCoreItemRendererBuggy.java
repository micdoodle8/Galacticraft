package micdoodle8.mods.galacticraft.core.client.render.item;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.entities.GCCoreEntityBuggy;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

/**
 * GCCoreItemRendererBuggy.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreItemRendererBuggy implements IItemRenderer
{
	private static final ResourceLocation buggyTextureBody = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/buggyMain.png");
	private static final ResourceLocation buggyTextureWheel = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/buggyWheels.png");
	private static final ResourceLocation buggyTextureStorage = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/buggyStorage.png");

	GCCoreEntityBuggy spaceship = new GCCoreEntityBuggy(FMLClientHandler.instance().getClient().theWorld);

	private final IModelCustom modelBuggy = AdvancedModelLoader.loadModel(ClientProxyCore.MODEL_DIRECTORY + "buggy.obj");
	private final IModelCustom modelBuggyWheelRight = AdvancedModelLoader.loadModel(ClientProxyCore.MODEL_DIRECTORY + "buggyWheelRight.obj");
	private final IModelCustom modelBuggyWheelLeft = AdvancedModelLoader.loadModel(ClientProxyCore.MODEL_DIRECTORY + "buggyWheelLeft.obj");

	private void renderPipeItem(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ)
	{
		GL11.glPushMatrix();
		long var10 = this.spaceship.entityId * 493286711L;
		var10 = var10 * var10 * 4392167121L + var10 * 98761L;
		final float var12 = (((var10 >> 16 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		final float var13 = (((var10 >> 20 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;
		final float var14 = (((var10 >> 24 & 7L) + 0.5F) / 8.0F - 0.5F) * 0.004F;

		GL11.glScalef(0.75F, 0.75F, 0.75F);

		if (type == ItemRenderType.EQUIPPED)
		{
			GL11.glScalef(2.2F, 2.2F, 2.2F);
			GL11.glTranslatef(0.3F, 2.7F, 0.4F);
		}
		else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glTranslatef(0.0F, 1.0F, 0.0F);
		}

		GL11.glTranslatef(var12, var13 - 0.1F, var14);
		GL11.glScalef(-0.4F, -0.4F, 0.4F);
		if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY)
		{
			if (type == ItemRenderType.INVENTORY)
			{
				GL11.glScalef(0.5F, 0.35F, 0.5F);
			}
			else
			{
				GL11.glTranslatef(0, -0.9F, 0);
				GL11.glScalef(0.5F, 0.5F, 0.5F);
			}

			GL11.glScalef(1.5F, 1.5F, 1.5F);
			GL11.glTranslatef(0, 1.6F, 0);
			GL11.glRotatef(-45.0F, 0F, 1F, 0F);
		}

		GL11.glRotatef(180, 0, 0, 1);

		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(GCCoreItemRendererBuggy.buggyTextureWheel);

		// Front wheel covers
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 1.0F, -2.6F);
		GL11.glTranslatef(1.4F, 0.0F, 0.0F);
		this.modelBuggyWheelRight.renderPart("WheelRightCover_Cover");
		GL11.glTranslatef(-2.8F, 0.0F, 0.0F);
		this.modelBuggyWheelLeft.renderPart("WheelLeftCover_Cover");
		GL11.glPopMatrix();

		// Back wheel covers
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 1.0F, 3.7F);
		GL11.glTranslatef(2.0F, 0.0F, 0.0F);
		this.modelBuggyWheelRight.renderPart("WheelRightCover_Cover");
		GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
		this.modelBuggyWheelLeft.renderPart("WheelLeftCover_Cover");
		GL11.glPopMatrix();

		// Front wheels
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 1.0F, -2.7F);
		GL11.glTranslatef(1.4F, 0.0F, 0.0F);
		this.modelBuggyWheelRight.renderPart("WheelRight_Wheel");
		GL11.glTranslatef(-2.8F, 0.0F, 0.0F);
		this.modelBuggyWheelLeft.renderPart("WheelLeft_Wheel");
		GL11.glPopMatrix();

		// Back wheels
		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, 1.0F, 3.6F);
		GL11.glTranslatef(2.0F, 0.0F, 0.0F);
		this.modelBuggyWheelRight.renderPart("WheelRight_Wheel");
		GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
		this.modelBuggyWheelLeft.renderPart("WheelLeft_Wheel");
		GL11.glPopMatrix();

		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(GCCoreItemRendererBuggy.buggyTextureBody);
		this.modelBuggy.renderPart("MainBody");

		// Radar Dish
		GL11.glPushMatrix();
		GL11.glTranslatef(-1.178F, 4.1F, -2.397F);
		this.modelBuggy.renderPart("RadarDish_Dish");
		GL11.glPopMatrix();

		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(GCCoreItemRendererBuggy.buggyTextureStorage);

		if (item.getItemDamage() > 0)
		{
			this.modelBuggy.renderPart("CargoLeft");

			if (item.getItemDamage() > 1)
			{
				this.modelBuggy.renderPart("CargoMid");

				if (item.getItemDamage() > 2)
				{
					this.modelBuggy.renderPart("CargoRight");
				}
			}
		}

		GL11.glPopMatrix();
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
			this.renderPipeItem(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		case EQUIPPED_FIRST_PERSON:
			this.renderPipeItem(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		case INVENTORY:
			this.renderPipeItem(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		case ENTITY:
			this.renderPipeItem(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		default:
		}
	}

}
