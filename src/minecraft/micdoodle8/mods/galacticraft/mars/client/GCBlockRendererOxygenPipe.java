package micdoodle8.mods.galacticraft.mars.client;

import micdoodle8.mods.galacticraft.mars.GCMarsBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraftforge.client.ForgeHooksClient;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class GCBlockRendererOxygenPipe implements ISimpleBlockRenderingHandler 
{
    final int renderID;

    public GCBlockRendererOxygenPipe(int var1)
    {
        this.renderID = var1;
    }
    
	public void renderPipe(RenderBlocks renderblocks, IBlockAccess iblockaccess, Block block, int x, int y, int z) 
	{
		float minSize = 0.4F;
		float maxSize = 0.6F;

		block.setBlockBounds(minSize, minSize, minSize, maxSize, maxSize, maxSize);
		renderblocks.renderStandardBlock(block, x, y, z);
		
		if (iblockaccess.getBlockId(x - 1, y, z) == GCMarsBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x - 1, y, z) == GCMarsBlocks.airDistributor.blockID || iblockaccess.getBlockId(x - 1, y, z) == GCMarsBlocks.airDistributorActive.blockID) 
		{
			block.setBlockBounds(0.0F, minSize, minSize, minSize, maxSize, maxSize);
			renderblocks.renderStandardBlock(block, x, y, z);
		}

		if (iblockaccess.getBlockId(x + 1, y, z) == GCMarsBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x + 1, y, z) == GCMarsBlocks.airDistributor.blockID || iblockaccess.getBlockId(x + 1, y, z) == GCMarsBlocks.airDistributorActive.blockID) 
		{
			block.setBlockBounds(maxSize, minSize, minSize, 1.0F, maxSize, maxSize);
			renderblocks.renderStandardBlock(block, x, y, z);
		}

		if (iblockaccess.getBlockId(x, y - 1, z) == GCMarsBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x, y - 1, z) == GCMarsBlocks.airDistributor.blockID || iblockaccess.getBlockId(x, y - 1, z) == GCMarsBlocks.airDistributorActive.blockID) 
		{
			block.setBlockBounds(minSize, 0.0F, minSize, maxSize, minSize, maxSize);
			renderblocks.renderStandardBlock(block, x, y, z);
		}

		if (iblockaccess.getBlockId(x, y + 1, z) == GCMarsBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x, y + 1, z) == GCMarsBlocks.airDistributor.blockID || iblockaccess.getBlockId(x, y + 1, z) == GCMarsBlocks.airDistributorActive.blockID) 
		{
			block.setBlockBounds(minSize, maxSize, minSize, maxSize, 1.0F, maxSize);
			renderblocks.renderStandardBlock(block, x, y, z);
		}

		if (iblockaccess.getBlockId(x, y, z - 1) == GCMarsBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x, y, z - 1) == GCMarsBlocks.airDistributor.blockID || iblockaccess.getBlockId(x, y, z - 1) == GCMarsBlocks.airDistributorActive.blockID) 
		{
			block.setBlockBounds(minSize, minSize, 0.0F, maxSize, maxSize, minSize);
			renderblocks.renderStandardBlock(block, x, y, z);
		}

		if (iblockaccess.getBlockId(x, y, z + 1) == GCMarsBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x, y, z + 1) == GCMarsBlocks.airDistributor.blockID || iblockaccess.getBlockId(x, y, z + 1) == GCMarsBlocks.airDistributorActive.blockID) 
		{
			block.setBlockBounds(minSize, minSize, maxSize, maxSize, maxSize, 1.0F);
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
