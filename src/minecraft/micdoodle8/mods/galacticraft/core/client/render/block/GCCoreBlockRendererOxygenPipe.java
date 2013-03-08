package micdoodle8.mods.galacticraft.core.client.render.block;

import micdoodle8.mods.galacticraft.API.IConnectableToPipe;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

import org.lwjgl.opengl.GL11;

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
		final float minSize = 0.4F;
		final float maxSize = 0.6F;

		renderblocks.setRenderBounds(minSize, minSize, minSize, maxSize, maxSize, maxSize);
		renderblocks.renderStandardBlock(block, x, y, z);

		if (Block.blocksList[iblockaccess.getBlockId(x - 1, y, z)] instanceof IConnectableToPipe)
		{
			final IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[iblockaccess.getBlockId(x - 1, y, z)];

			if (pipe.isConnectableOnSide(iblockaccess, x, y, z, ForgeDirection.WEST))
			{
				renderblocks.setRenderBounds(0.0F, minSize, minSize, minSize, maxSize, maxSize);
				renderblocks.renderStandardBlock(block, x, y, z);
			}
		}

		if (Block.blocksList[iblockaccess.getBlockId(x + 1, y, z)] instanceof IConnectableToPipe)
		{
			final IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[iblockaccess.getBlockId(x + 1, y, z)];

			if (pipe.isConnectableOnSide(iblockaccess, x, y, z, ForgeDirection.EAST))
			{
				renderblocks.setRenderBounds(maxSize, minSize, minSize, 1.0F, maxSize, maxSize);
				renderblocks.renderStandardBlock(block, x, y, z);
			}
		}

		if (Block.blocksList[iblockaccess.getBlockId(x, y - 1, z)] instanceof IConnectableToPipe)
		{
			final IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[iblockaccess.getBlockId(x, y - 1, z)];

			if (pipe.isConnectableOnSide(iblockaccess, x, y, z, ForgeDirection.DOWN))
			{
				renderblocks.setRenderBounds(minSize, 0.0F, minSize, maxSize, minSize, maxSize);
				renderblocks.renderStandardBlock(block, x, y, z);
			}
		}

		if (Block.blocksList[iblockaccess.getBlockId(x, y + 1, z)] instanceof IConnectableToPipe)
		{
			final IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[iblockaccess.getBlockId(x, y + 1, z)];

			if (pipe.isConnectableOnSide(iblockaccess, x, y, z, ForgeDirection.UP))
			{
				renderblocks.setRenderBounds(minSize, maxSize, minSize, maxSize, 1.0F, maxSize);
				renderblocks.renderStandardBlock(block, x, y, z);
			}
		}

		if (Block.blocksList[iblockaccess.getBlockId(x, y, z - 1)] instanceof IConnectableToPipe)
		{
			final IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[iblockaccess.getBlockId(x, y, z - 1)];

			if (pipe.isConnectableOnSide(iblockaccess, x, y, z, ForgeDirection.NORTH))
			{
				renderblocks.setRenderBounds(minSize, minSize, 0.0F, maxSize, maxSize, minSize);
				renderblocks.renderStandardBlock(block, x, y, z);
			}
		}

		if (Block.blocksList[iblockaccess.getBlockId(x, y, z + 1)] instanceof IConnectableToPipe)
		{
			final IConnectableToPipe pipe = (IConnectableToPipe) Block.blocksList[iblockaccess.getBlockId(x, y, z + 1)];

			if (pipe.isConnectableOnSide(iblockaccess, x, y, z, ForgeDirection.SOUTH))
			{
				renderblocks.setRenderBounds(minSize, minSize, maxSize, maxSize, maxSize, 1.0F);
				renderblocks.renderStandardBlock(block, x, y, z);
			}
		}
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		final float minSize = 0.4F;
		final float maxSize = 0.6F;

        final Tessellator var3 = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        renderer.setRenderBounds(minSize, minSize, 0.0F, maxSize, maxSize, 1.0F);
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
		this.renderPipe(renderer, world, block, x, y, z);
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
