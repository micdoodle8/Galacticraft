package micdoodle8.mods.galacticraft.core.client.render.block;

import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlockFluid;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreBlockRendererCrudeOil implements ISimpleBlockRenderingHandler
{
    final int renderID;

    public GCCoreBlockRendererCrudeOil(int var1)
    {
        this.renderID = var1;
    }

    @Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) 
    {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

		if (block.getRenderType() != renderID)
		{
			return true;
		}

		renderer.renderBlockFluids(block, x, y, z);
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory() 
	{
		return false;
	}

	@Override
	public int getRenderId() 
	{
		return renderID;
	}
}
