package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.core.GCCoreBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class GCCoreBlockRendererOxygenPipe implements ISimpleBlockRenderingHandler 
{
    final int renderID;

    public GCCoreBlockRendererOxygenPipe(int var1)
    {
        this.renderID = var1;
    }
    
	public void renderPipe(RenderBlocks renderblocks, IBlockAccess iblockaccess, Block block, int x, int y, int z) 
	{
		float minSize = 0.4F;
		float maxSize = 0.6F;

		renderblocks.func_83020_a(minSize, minSize, minSize, maxSize, maxSize, maxSize);
		renderblocks.renderStandardBlock(block, x, y, z);
		
		if (iblockaccess.getBlockId(x - 1, y, z) == GCCoreBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x - 1, y, z) == GCCoreBlocks.airDistributor.blockID || iblockaccess.getBlockId(x - 1, y, z) == GCCoreBlocks.airDistributorActive.blockID || iblockaccess.getBlockId(x - 1, y, z) == GCCoreBlocks.blockAirCollector.blockID) 
		{
			renderblocks.func_83020_a(0.0F, minSize, minSize, minSize, maxSize, maxSize);
			renderblocks.renderStandardBlock(block, x, y, z);
		}

		if (iblockaccess.getBlockId(x + 1, y, z) == GCCoreBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x + 1, y, z) == GCCoreBlocks.airDistributor.blockID || iblockaccess.getBlockId(x + 1, y, z) == GCCoreBlocks.airDistributorActive.blockID || iblockaccess.getBlockId(x + 1, y, z) == GCCoreBlocks.blockAirCollector.blockID) 
		{
			renderblocks.func_83020_a(maxSize, minSize, minSize, 1.0F, maxSize, maxSize);
			renderblocks.renderStandardBlock(block, x, y, z);
		}

		if (iblockaccess.getBlockId(x, y - 1, z) == GCCoreBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x, y - 1, z) == GCCoreBlocks.airDistributor.blockID || iblockaccess.getBlockId(x, y - 1, z) == GCCoreBlocks.airDistributorActive.blockID || iblockaccess.getBlockId(x, y - 1, z) == GCCoreBlocks.blockAirCollector.blockID) 
		{
			renderblocks.func_83020_a(minSize, 0.0F, minSize, maxSize, minSize, maxSize);
			renderblocks.renderStandardBlock(block, x, y, z);
		}

		if (iblockaccess.getBlockId(x, y + 1, z) == GCCoreBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x, y + 1, z) == GCCoreBlocks.airDistributor.blockID || iblockaccess.getBlockId(x, y + 1, z) == GCCoreBlocks.airDistributorActive.blockID || iblockaccess.getBlockId(x, y + 1, z) == GCCoreBlocks.blockAirCollector.blockID) 
		{
			renderblocks.func_83020_a(minSize, maxSize, minSize, maxSize, 1.0F, maxSize);
			renderblocks.renderStandardBlock(block, x, y, z);
		}

		if (iblockaccess.getBlockId(x, y, z - 1) == GCCoreBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x, y, z - 1) == GCCoreBlocks.airDistributor.blockID || iblockaccess.getBlockId(x, y, z - 1) == GCCoreBlocks.airDistributorActive.blockID || iblockaccess.getBlockId(x, y, z - 1) == GCCoreBlocks.blockAirCollector.blockID) 
		{
			renderblocks.func_83020_a(minSize, minSize, 0.0F, maxSize, maxSize, minSize);
			renderblocks.renderStandardBlock(block, x, y, z);
		}

		if (iblockaccess.getBlockId(x, y, z + 1) == GCCoreBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x, y, z + 1) == GCCoreBlocks.airDistributor.blockID || iblockaccess.getBlockId(x, y, z + 1) == GCCoreBlocks.airDistributorActive.blockID || iblockaccess.getBlockId(x, y, z + 1) == GCCoreBlocks.blockAirCollector.blockID) 
		{
			renderblocks.func_83020_a(minSize, minSize, maxSize, maxSize, maxSize, 1.0F);
			renderblocks.renderStandardBlock(block, x, y, z);
		}
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) { }

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) 
	{
		renderPipe(renderer, world, block, x, y, z);
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
