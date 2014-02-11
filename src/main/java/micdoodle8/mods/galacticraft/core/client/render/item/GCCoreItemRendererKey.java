package micdoodle8.mods.galacticraft.core.client.render.item;

import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelKey;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

/**
 * GCCoreItemRendererKey.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreItemRendererKey implements IItemRenderer
{
	private final ResourceLocation treasureChestTexture;

	GCCoreModelKey keyModel = new GCCoreModelKey();

	public GCCoreItemRendererKey(ResourceLocation resourceLocation)
	{
		this.treasureChestTexture = resourceLocation;
	}

	private void renderKey(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ, Object... data)
	{
		GL11.glPushMatrix();

		EntityItem entityItem = null;

		if (data.length == 2 && data[1] instanceof EntityItem)
		{
			entityItem = (EntityItem) data[1];
		}

		if (type == ItemRenderType.INVENTORY)
		{
			GL11.glTranslatef(8.0F, 8.0F, 0F);
			GL11.glRotatef((MathHelper.sin(Sys.getTime() / 90F / 20.0F) - 55.0F) * 50.0F, 0, 0, 1);
			GL11.glScalef(5.0F, 5.0F, 5.0F);
			GL11.glScalef(1.5F, 1.5F, 1.5F);
		}
		else if (type == ItemRenderType.ENTITY)
		{
			GL11.glTranslatef(0.0F, 2.0F, 0F);
			GL11.glScalef(3.0F, 3.0F, 3.0F);
		}
		else if (type == ItemRenderType.EQUIPPED)
		{
			GL11.glRotatef(100.0F, 0, 1, 0);
			GL11.glRotatef(60.0F, 0, 0, 1);
			GL11.glRotatef(-10.0F, 1, 0, 0);
			GL11.glTranslatef(0.4F, 0.1F, 0.5F);
		}
		else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glRotatef(-4.0F, 0, 1, 0);
			GL11.glRotatef(2.0F, 1, 0, 0);
			GL11.glTranslatef(3.0F, 2.0F, -0.6F);
			GL11.glScalef(3.0F, 3.0F, 3.0F);
		}

		GL11.glRotatef(45, 0, 0, 1);

		if (entityItem != null)
		{
			final float f2 = MathHelper.sin(((float) entityItem.age + 1) / 10.0F + entityItem.hoverStart) * 0.1F + 0.1F;
			GL11.glRotatef(f2 * 90F - 45F, 0, 0, 1);
			GL11.glRotatef((float) (Math.sin((entityItem.age + 1) / 100.0F) * 180.0F), 0, 1, 0);
		}

		FMLClientHandler.instance().getClient().renderEngine.bindTexture(this.treasureChestTexture);

		this.keyModel.renderAll();
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
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		switch (type)
		{
		case EQUIPPED:
			this.renderKey(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f, data);
			break;
		case EQUIPPED_FIRST_PERSON:
			this.renderKey(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f, data);
			break;
		case INVENTORY:
			this.renderKey(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f, data);
			break;
		case ENTITY:
			this.renderKey(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f, data);
			break;
		default:
		}
	}

}
