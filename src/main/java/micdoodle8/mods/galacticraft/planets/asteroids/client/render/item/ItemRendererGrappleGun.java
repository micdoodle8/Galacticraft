package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockMachineMars;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

/**
 * GCMarsItemRendererMachine.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ItemRendererGrappleGun implements IItemRenderer
{
	private static final ResourceLocation gunTexture = new ResourceLocation(GalacticraftPlanets.ASSET_DOMAIN, "textures/model/grapplegun.png");

	private IModelCustom model;

	public ItemRendererGrappleGun(IModelCustom model)
	{
		this.model = model;
	}

	private void renderGrappleGun(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ)
	{
		GL11.glPushMatrix();

		this.transform(type);

		FMLClientHandler.instance().getClient().renderEngine.bindTexture(ItemRendererGrappleGun.gunTexture);
		this.model.renderAll();
		GL11.glPopMatrix();
	}

	public void transform(ItemRenderType type)
	{
		final EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

		if (type == ItemRenderType.EQUIPPED)
		{
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			GL11.glRotatef(185, 1, 0, 0);
			GL11.glRotatef(40, 0, 1, 0);
			GL11.glRotatef(-70, 0, 0, 1);
			GL11.glScalef(3.2F, 3.2F, 3.2F);
		}

		if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glScalef(8.2F, 8.2F, 8.2F);
			GL11.glTranslatef(0.291F, 0.1F, -0.4F);
			GL11.glRotatef(180, 1, 0, 0);
			GL11.glRotatef(136, 0, 1, 0);
			GL11.glRotatef(-5, 0, 0, 1);
		}

		GL11.glScalef(-0.4F, -0.4F, 0.4F);

		if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY)
		{
//			GL11.glTranslatef(0, -0.55F, 0);

			if (type == ItemRenderType.INVENTORY)
			{
				GL11.glScalef(1.85F, 1.85F, 1.85F);
				GL11.glTranslatef(0.0F, 0.0F, 0.33F);
				GL11.glRotatef(170, 1, 0, 0);
				GL11.glRotatef(95, 0, 1, 0);
				GL11.glRotatef(0F, 0, 0, 1);
			}
			else
			{
				GL11.glTranslatef(0, -3.9F, 0);
				GL11.glRotatef(Sys.getTime() / 90F % 360F, 0F, 1F, 0F);
			}

			GL11.glScalef(1.3F, 1.3F, 1.3F);
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
			this.renderGrappleGun(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		case EQUIPPED_FIRST_PERSON:
			this.renderGrappleGun(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		case INVENTORY:
			this.renderGrappleGun(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		case ENTITY:
			this.renderGrappleGun(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		default:
			break;
		}
	}

}
