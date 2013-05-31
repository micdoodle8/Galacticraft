package micdoodle8.mods.galacticraft.core.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
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
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if (block.getRenderType() != this.renderID)
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
        return this.renderID;
    }
}
