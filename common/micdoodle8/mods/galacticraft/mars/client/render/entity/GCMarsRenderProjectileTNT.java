package micdoodle8.mods.galacticraft.mars.client.render.entity;

import micdoodle8.mods.galacticraft.mars.entities.GCMarsEntityProjectileTNT;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsRenderProjectileTNT.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCMarsRenderProjectileTNT extends Render
{
	private final RenderBlocks renderBlocks = new RenderBlocks();

	public GCMarsRenderProjectileTNT()
	{
		this.shadowSize = 0.5F;
	}

	public void renderProjectileTNT(GCMarsEntityProjectileTNT tnt, double par2, double par4, double par6, float par8, float par9)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float) par2, (float) par4, (float) par6);
		this.bindTexture(TextureMap.locationBlocksTexture);
		final Block var10 = Block.tnt;
		GL11.glDisable(GL11.GL_LIGHTING);
		if (var10 != null)
		{
			this.renderBlocks.setRenderBoundsFromBlock(var10);
			this.renderBlocks.renderBlockSandFalling(var10, tnt.worldObj, MathHelper.floor_double(tnt.posX), MathHelper.floor_double(tnt.posY), MathHelper.floor_double(tnt.posZ), 0);
		}

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	@Override
	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		this.renderProjectileTNT((GCMarsEntityProjectileTNT) par1Entity, par2, par4, par6, par8, par9);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return null;
	}
}
