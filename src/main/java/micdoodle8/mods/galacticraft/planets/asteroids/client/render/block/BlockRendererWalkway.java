package micdoodle8.mods.galacticraft.planets.asteroids.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * BlockRendererWalkway.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class BlockRendererWalkway implements ISimpleBlockRenderingHandler
{
	final int renderID;

	public BlockRendererWalkway(int var1)
	{
		this.renderID = var1;
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess var1, int var2, int var3, int var4, Block var5, int var6, RenderBlocks var7)
	{
		this.renderWalkway(var7, var5, var1, var2, var3, var4);
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}

	@Override
	public int getRenderId()
	{
		return this.renderID;
	}

	public static void renderInvNormalBlock(RenderBlocks var0, Block var1, int var2)
	{
		// Top Plate
		var0.setRenderBounds(0.0F, 0.9F, 0.0F, 0.1F, 1.0F, 1.0F);
		BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
		var0.setRenderBounds(0.9F, 0.9F, 0.0F, 1.0F, 1.0F, 1.0F);
		BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
		var0.setRenderBounds(0.0F, 0.9F, 0.0F, 1.0F, 1.0F, 0.1F);
		BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
		var0.setRenderBounds(0.0F, 0.9F, 0.9F, 1.0F, 1.0F, 1.0F);
		BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
		
		// Middle
		var0.setRenderBounds(0.4F, 0.9F, 0.4F, 0.6F, 1.0F, 0.6F);
		BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
		var0.setRenderBounds(0.35F, 0.35F, 0.35F, 0.65F, 0.65F, 0.65F);
		BlockRendererWalkway.renderStandardBlock(var0, var1, var2);

		// Middle-Top Vertical Connector
		var0.setRenderBounds(0.45F, 0.5F, 0.45F, 0.55F, 0.9F, 0.55F);
		BlockRendererWalkway.renderStandardBlock(var0, var1, var2);

		var0.clearOverrideBlockTexture();
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

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		BlockRendererWalkway.renderInvNormalBlock(renderer, block, metadata);
	}

	public void renderWalkway(RenderBlocks renderBlocks, Block block, IBlockAccess var1, int x, int y, int z)
	{
		// Top Plate
		renderBlocks.setRenderBounds(0.0F, 0.9F, 0.0F, 0.1F, 1.0F, 1.0F);
		renderBlocks.renderStandardBlock(block, x, y, z);
		renderBlocks.setRenderBounds(0.9F, 0.9F, 0.0F, 1.0F, 1.0F, 1.0F);
		renderBlocks.renderStandardBlock(block, x, y, z);
		renderBlocks.setRenderBounds(0.0F, 0.9F, 0.0F, 1.0F, 1.0F, 0.1F);
		renderBlocks.renderStandardBlock(block, x, y, z);
		renderBlocks.setRenderBounds(0.0F, 0.9F, 0.9F, 1.0F, 1.0F, 1.0F);
		renderBlocks.renderStandardBlock(block, x, y, z);
		
		// Middle
		renderBlocks.setRenderBounds(0.4F, 0.9F, 0.4F, 0.6F, 1.0F, 0.6F);
		renderBlocks.renderStandardBlock(block, x, y, z);
		renderBlocks.setRenderBounds(0.35F, 0.35F, 0.35F, 0.65F, 0.65F, 0.65F);
		renderBlocks.renderStandardBlock(block, x, y, z);

		// Middle-Top Vertical Connector
		renderBlocks.setRenderBounds(0.45F, 0.5F, 0.45F, 0.55F, 0.9F, 0.55F);
		renderBlocks.renderStandardBlock(block, x, y, z);
		
		int meta = var1.getBlockMetadata(x, y, z);
		
		// Check meta value for connectedness
    	boolean connectedNorth = (meta & 1) != 0;
    	boolean connectedEast = (meta & 2) != 0;
    	boolean connectedSouth = (meta & 4) != 0;
    	boolean connectedWest = (meta & 8) != 0;
    	
    	if (connectedNorth)
    	{
    		// Top Runway
    		renderBlocks.setRenderBounds(0.4F, 0.9F, 0.0F, 0.6F, 1.0F, 0.4F);
    		renderBlocks.renderStandardBlock(block, x, y, z);
    		
    		// Inner Steel Bar
    		renderBlocks.setRenderBounds(0.4F, 0.4F, 0.0F, 0.6F, 0.6F, 0.4F);
    		renderBlocks.renderStandardBlock(block, x, y, z);
    	}

    	if (connectedEast)
    	{
    		renderBlocks.setRenderBounds(0.6F, 0.9F, 0.4F, 1.0F, 1.0F, 0.6F);
    		renderBlocks.renderStandardBlock(block, x, y, z);
    		
    		renderBlocks.setRenderBounds(0.6F, 0.4F, 0.4F, 1.0F, 0.6F, 0.6F);
    		renderBlocks.renderStandardBlock(block, x, y, z);
    	}

    	if (connectedWest)
    	{
    		renderBlocks.setRenderBounds(0.0F, 0.9F, 0.4F, 0.4F, 1.0F, 0.6F);
    		renderBlocks.renderStandardBlock(block, x, y, z);
    		
    		renderBlocks.setRenderBounds(0.0F, 0.4F, 0.4F, 0.4F, 0.6F, 0.6F);
    		renderBlocks.renderStandardBlock(block, x, y, z);
    	}

    	if (connectedSouth)
    	{
    		renderBlocks.setRenderBounds(0.4F, 0.9F, 0.6F, 0.6F, 1.0F, 1.0F);
    		renderBlocks.renderStandardBlock(block, x, y, z);
    		
    		renderBlocks.setRenderBounds(0.4F, 0.4F, 0.6F, 0.6F, 0.6F, 1.0F);
    		renderBlocks.renderStandardBlock(block, x, y, z);
    	}

		renderBlocks.clearOverrideBlockTexture();
		block.setBlockBoundsForItemRender();
	}
}
