package micdoodle8.mods.galacticraft.core.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * GCCoreBlockRendererOxygenDistributor.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockRendererOxygenDistributor implements ISimpleBlockRenderingHandler
{
	final int renderID;

	public GCCoreBlockRendererOxygenDistributor(int var1)
	{
		this.renderID = var1;
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess var1, int var2, int var3, int var4, Block var5, int var6, RenderBlocks var7)
	{
		// Corners
		var7.setRenderBounds(0.0F, 0.1F, 0.0F, 0.1F, 0.9F, 0.1F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.setRenderBounds(0.9F, 0.1F, 0.0F, 1.0F, 0.9F, 0.1F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.setRenderBounds(0.0F, 0.1F, 0.9F, 0.1F, 0.9F, 1.0F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.setRenderBounds(0.9F, 0.1F, 0.9F, 1.0F, 0.9F, 1.0F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		// var7.overrideBlockTexture = 35;

		// Base
		var7.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		// Top
		var7.setRenderBounds(0.0F, 0.9F, 0.0F, 1.0F, 1.0F, 1.0F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		// var7.overrideBlockTexture = 33;

		// Axles
		var7.setRenderBounds(0.0F, 0.42F, 0.42F, 1.0F, 0.58F, 0.58F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.setRenderBounds(0.42F, 0.42F, 0.0F, 0.58F, 0.58F, 1.0F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		// var7.overrideBlockTexture = 36;

		// Tanks
		var7.setRenderBounds(0.35F, 0.35F, 0.35F, 0.65F, 0.85F, 0.65F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		// var7.overrideBlockTexture = 34;

		// Grate
		var7.setRenderBounds(0.3F, 0.1F, 0.002F, 0.4F, 0.9F, 0.02F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.setRenderBounds(0.6F, 0.1F, 0.002F, 0.7F, 0.9F, 0.02F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.setRenderBounds(0.1F, 0.6F, 0.001F, 0.9F, 0.7F, 0.0201F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.setRenderBounds(0.1F, 0.3F, 0.001F, 0.9F, 0.4F, 0.0201F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		//

		var7.setRenderBounds(0.3F, 0.1F, 1.0F - 0.02F, 0.4F, 0.9F, 1.0F - 0.002);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.setRenderBounds(0.6F, 0.1F, 1.0F - 0.02F, 0.7F, 0.9F, 1.0F - 0.002);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.setRenderBounds(0.1F, 0.6F, 1.0F - 0.0201F, 0.9F, 0.7F, 1.0F - 0.001);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.setRenderBounds(0.1F, 0.3F, 1.0F - 0.0201F, 0.9F, 0.4F, 1.0F - 0.001);
		var7.renderStandardBlock(var5, var2, var3, var4);

		//

		var7.setRenderBounds(0.002F, 0.1F, 0.3F, 0.02F, 0.9F, 0.4F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.setRenderBounds(0.002F, 0.1F, 0.6F, 0.02F, 0.9F, 0.7F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.setRenderBounds(0.0021F, 0.6F, 0.1F, 0.0201F, 0.7F, 0.9F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.setRenderBounds(0.0021F, 0.3F, 0.1F, 0.0201F, 0.4F, 0.9F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		//

		var7.setRenderBounds(1.0F - 0.02F, 0.1F, 0.3F, 1.0F - 0.002, 0.9F, 0.4F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.setRenderBounds(1.0F - 0.02F, 0.1F, 0.6F, 1.0F - 0.002, 0.9F, 0.7F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.setRenderBounds(1.0F - 0.0201F, 0.6F, 0.1F, 1.0F - 0.0021, 0.7F, 0.9F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.setRenderBounds(1.0F - 0.0201F, 0.3F, 0.1F, 1.0F - 0.0021, 0.4F, 0.9F);
		var7.renderStandardBlock(var5, var2, var3, var4);

		var7.clearOverrideBlockTexture();

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory()
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return this.renderID;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		GCCoreBlockRendererOxygenDistributor.renderInvNormalBlock(renderer, block, 0);
	}

	public static void renderInvNormalBlock(RenderBlocks var7, Block var5, int var2)
	{
		var7.setRenderBounds(0.0F, 0.1F, 0.0F, 0.1F, 0.9F, 0.1F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.setRenderBounds(0.9F, 0.1F, 0.0F, 1.0F, 0.9F, 0.1F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.setRenderBounds(0.0F, 0.1F, 0.9F, 0.1F, 0.9F, 1.0F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.setRenderBounds(0.9F, 0.1F, 0.9F, 1.0F, 0.9F, 1.0F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		// var7.overrideBlockTexture = 35;
		// var7.setOverrideBlockTexture(((GCCoreBlockOxygenDistributor)var5).d)

		// Base
		var7.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1F, 1.0F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		// Top
		var7.setRenderBounds(0.0F, 0.9F, 0.0F, 1.0F, 1.0F, 1.0F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		// var7.overrideBlockTexture = 33;

		// Axles
		var7.setRenderBounds(0.0F, 0.42F, 0.42F, 1.0F, 0.58F, 0.58F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.setRenderBounds(0.42F, 0.42F, 0.0F, 0.58F, 0.58F, 1.0F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		// var7.overrideBlockTexture = 36;

		// Tanks
		var7.setRenderBounds(0.35F, 0.35F, 0.35F, 0.65F, 0.85F, 0.65F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		// var7.overrideBlockTexture = 34;

		// Grate
		var7.setRenderBounds(0.3F, 0.1F, 0.002F, 0.4F, 0.9F, 0.02F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.setRenderBounds(0.6F, 0.1F, 0.002F, 0.7F, 0.9F, 0.02F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.setRenderBounds(0.1F, 0.6F, 0.001F, 0.9F, 0.7F, 0.02F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.setRenderBounds(0.1F, 0.3F, 0.001F, 0.9F, 0.4F, 0.02F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		//

		var7.setRenderBounds(0.3F, 0.1F, 1.0F - 0.02F, 0.4F, 0.9F, 1.0F - 0.002);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.setRenderBounds(0.6F, 0.1F, 1.0F - 0.02F, 0.7F, 0.9F, 1.0F - 0.002);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.setRenderBounds(0.1F, 0.6F, 1.0F - 0.02F, 0.9F, 0.7F, 1.0F - 0.001);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.setRenderBounds(0.1F, 0.3F, 1.0F - 0.02F, 0.9F, 0.4F, 1.0F - 0.001);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		//

		var7.setRenderBounds(0.002F, 0.1F, 0.3F, 0.02F, 0.9F, 0.4F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.setRenderBounds(0.002F, 0.1F, 0.6F, 0.02F, 0.9F, 0.7F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.setRenderBounds(0.002F, 0.6F, 0.1F, 0.02F, 0.7F, 0.9F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.setRenderBounds(0.002F, 0.3F, 0.1F, 0.02F, 0.4F, 0.9F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		//

		var7.setRenderBounds(1.0F - 0.02F, 0.1F, 0.3F, 1.0F - 0.002, 0.9F, 0.4F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.setRenderBounds(1.0F - 0.02F, 0.1F, 0.6F, 1.0F - 0.002, 0.9F, 0.7F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.setRenderBounds(1.0F - 0.02F, 0.6F, 0.1F, 1.0F - 0.002, 0.7F, 0.9F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.setRenderBounds(1.0F - 0.02F, 0.3F, 0.1F, 1.0F - 0.002, 0.4F, 0.9F);
		GCCoreBlockRendererOxygenDistributor.renderStandardBlock(var7, var5, var2);

		var7.clearOverrideBlockTexture();
	}

	private static void renderStandardBlock(RenderBlocks var0, Block var1, int var2)
	{
		GL11.glPushMatrix();
		final Tessellator var3 = Tessellator.instance;
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var3.startDrawingQuads();
		var3.setNormal(0.0F, -1.0F, 0.0F);
		var0.renderFaceYNeg(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(0, var2));
		var3.draw();
		var3.startDrawingQuads();
		var3.setNormal(0.0F, 1.0F, 0.0F);
		var0.renderFaceYPos(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(1, var2));
		var3.draw();
		var3.startDrawingQuads();
		var3.setNormal(0.0F, 0.0F, -1.0F);
		var0.renderFaceXPos(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(2, var2));
		var3.draw();
		var3.startDrawingQuads();
		var3.setNormal(0.0F, 0.0F, 1.0F);
		var0.renderFaceXNeg(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(3, var2));
		var3.draw();
		var3.startDrawingQuads();
		var3.setNormal(-1.0F, 0.0F, 0.0F);
		var0.renderFaceZNeg(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(4, var2));
		var3.draw();
		var3.startDrawingQuads();
		var3.setNormal(1.0F, 0.0F, 0.0F);
		var0.renderFaceZPos(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(5, var2));
		var3.draw();
		GL11.glPopMatrix();
	}
}
