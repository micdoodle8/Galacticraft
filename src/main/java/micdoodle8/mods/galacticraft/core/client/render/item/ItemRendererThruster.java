package micdoodle8.mods.galacticraft.core.client.render.item;

import micdoodle8.mods.galacticraft.core.client.render.tile.TileEntityThrusterRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

/**
 * ItemRendererThruster.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class ItemRendererThruster implements IItemRenderer
{
	private void renderMeteorChunk(ItemRenderType type, RenderBlocks render, ItemStack item, float translateX, float translateY, float translateZ)
	{
		GL11.glPushMatrix();

		FMLClientHandler.instance().getClient().getTextureManager().bindTexture(TileEntityThrusterRenderer.thrusterTexture);
		
		switch (type)
		{
		case INVENTORY:
			GL11.glTranslatef(-0.4F, -0.1F, 0.0F);
			GL11.glScalef(0.8F, 0.8F, 0.8F);
			break;
		case EQUIPPED_FIRST_PERSON:
			GL11.glTranslatef(-0.2F, 0.9F, 0.0F);
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glScalef(0.8F, 0.8F, 0.8F);
			break;
		default:
			break;
		}
		
		TileEntityThrusterRenderer.thrusterModel.renderAll();

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
			this.renderMeteorChunk(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		case EQUIPPED_FIRST_PERSON:
			this.renderMeteorChunk(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		case INVENTORY:
			this.renderMeteorChunk(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		case ENTITY:
			this.renderMeteorChunk(type, (RenderBlocks) data[0], item, -0.5f, -0.5f, -0.5f);
			break;
		default:
		}
	}

}
