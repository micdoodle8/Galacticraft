package micdoodle8.mods.galacticraft.core.client;

import org.lwjgl.opengl.GL11;

import micdoodle8.mods.galacticraft.core.GCCoreBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
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

		renderblocks.setRenderMinMax(minSize, minSize, minSize, maxSize, maxSize, maxSize);
		renderblocks.renderStandardBlock(block, x, y, z);
		
		if (iblockaccess.getBlockId(x - 1, y, z) == GCCoreBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x - 1, y, z) == GCCoreBlocks.airDistributor.blockID || iblockaccess.getBlockId(x - 1, y, z) == GCCoreBlocks.airDistributorActive.blockID || iblockaccess.getBlockId(x - 1, y, z) == GCCoreBlocks.blockAirCollector.blockID) 
		{
			renderblocks.setRenderMinMax(0.0F, minSize, minSize, minSize, maxSize, maxSize);
			renderblocks.renderStandardBlock(block, x, y, z);
		}

		if (iblockaccess.getBlockId(x + 1, y, z) == GCCoreBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x + 1, y, z) == GCCoreBlocks.airDistributor.blockID || iblockaccess.getBlockId(x + 1, y, z) == GCCoreBlocks.airDistributorActive.blockID || iblockaccess.getBlockId(x + 1, y, z) == GCCoreBlocks.blockAirCollector.blockID) 
		{
			renderblocks.setRenderMinMax(maxSize, minSize, minSize, 1.0F, maxSize, maxSize);
			renderblocks.renderStandardBlock(block, x, y, z);
		}

		if (iblockaccess.getBlockId(x, y - 1, z) == GCCoreBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x, y - 1, z) == GCCoreBlocks.airDistributor.blockID || iblockaccess.getBlockId(x, y - 1, z) == GCCoreBlocks.airDistributorActive.blockID || iblockaccess.getBlockId(x, y - 1, z) == GCCoreBlocks.blockAirCollector.blockID) 
		{
			renderblocks.setRenderMinMax(minSize, 0.0F, minSize, maxSize, minSize, maxSize);
			renderblocks.renderStandardBlock(block, x, y, z);
		}

		if (iblockaccess.getBlockId(x, y + 1, z) == GCCoreBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x, y + 1, z) == GCCoreBlocks.airDistributor.blockID || iblockaccess.getBlockId(x, y + 1, z) == GCCoreBlocks.airDistributorActive.blockID || iblockaccess.getBlockId(x, y + 1, z) == GCCoreBlocks.blockAirCollector.blockID) 
		{
			renderblocks.setRenderMinMax(minSize, maxSize, minSize, maxSize, 1.0F, maxSize);
			renderblocks.renderStandardBlock(block, x, y, z);
		}

		if (iblockaccess.getBlockId(x, y, z - 1) == GCCoreBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x, y, z - 1) == GCCoreBlocks.airDistributor.blockID || iblockaccess.getBlockId(x, y, z - 1) == GCCoreBlocks.airDistributorActive.blockID || iblockaccess.getBlockId(x, y, z - 1) == GCCoreBlocks.blockAirCollector.blockID) 
		{
			renderblocks.setRenderMinMax(minSize, minSize, 0.0F, maxSize, maxSize, minSize);
			renderblocks.renderStandardBlock(block, x, y, z);
		}

		if (iblockaccess.getBlockId(x, y, z + 1) == GCCoreBlocks.oxygenPipe.blockID || iblockaccess.getBlockId(x, y, z + 1) == GCCoreBlocks.airDistributor.blockID || iblockaccess.getBlockId(x, y, z + 1) == GCCoreBlocks.airDistributorActive.blockID || iblockaccess.getBlockId(x, y, z + 1) == GCCoreBlocks.blockAirCollector.blockID) 
		{
			renderblocks.setRenderMinMax(minSize, minSize, maxSize, maxSize, maxSize, 1.0F);
			renderblocks.renderStandardBlock(block, x, y, z);
		}
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) 
	{ 
		float minSize = 0.4F;
		float maxSize = 0.6F;
		
        Tessellator var3 = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        renderer.setRenderMinMax(minSize, minSize, 0.0F, maxSize, maxSize, 1.0F);
        var3.startDrawingQuads();
        var3.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(0, metadata));
        var3.draw();
        var3.startDrawingQuads();
        var3.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(1, metadata));
        var3.draw();
        var3.startDrawingQuads();
        var3.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(2, metadata));
        var3.draw();
        var3.startDrawingQuads();
        var3.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(3, metadata));
        var3.draw();
        var3.startDrawingQuads();
        var3.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(4, metadata));
        var3.draw();
        var3.startDrawingQuads();
        var3.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSideAndMetadata(5, metadata));
        var3.draw();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) 
	{
		renderPipe(renderer, world, block, x, y, z);
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
}
