package micdoodle8.mods.galacticraft.core.client.render.block;

import java.util.Arrays;

import mekanism.api.GasTransmission;
import mekanism.api.ITubeConnection;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
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
		final TileEntity tileEntity = iblockaccess.getBlockTileEntity(x, y, z);

		final float minX = 0.40F;
		final float minY = 0.40F;
		final float minZ = 0.40F;
		final float maxX = 0.60F;
		final float maxY = 0.60F;
		final float maxZ = 0.60F;

		if(tileEntity != null)
		{
			final boolean[] connectable = new boolean[] {false, false, false, false, false, false};
			final ITubeConnection[] connections = GasTransmission.getConnections(tileEntity);

			for(final ITubeConnection connection : connections)
			{
				if(connection !=  null)
				{
					final int side = Arrays.asList(connections).indexOf(connection);

					if(connection.canTubeConnect(ForgeDirection.getOrientation(side).getOpposite()))
					{
						switch (side)
						{
						case 0: // DOWN
							renderblocks.setRenderBounds(minX, 0.0F, minZ, maxX, 0.4F, maxZ);
							renderblocks.renderStandardBlock(block, x, y, z);
							break;
						case 1: // UP
							renderblocks.setRenderBounds(minX, 0.6F, minZ, maxX, 1.0F, maxZ);
							renderblocks.renderStandardBlock(block, x, y, z);
							break;
						case 2: // NORTH
							renderblocks.setRenderBounds(minX, minY, 0.0, maxX, maxY, 0.4F);
							renderblocks.renderStandardBlock(block, x, y, z);
							break;
						case 3: // SOUTH
							renderblocks.setRenderBounds(minX, minY, 0.6F, maxX, maxY, 1.0);
							renderblocks.renderStandardBlock(block, x, y, z);
							break;
						case 4: // WEST
							renderblocks.setRenderBounds(0.0, minY, minZ, 0.4F, maxY, maxZ);
							renderblocks.renderStandardBlock(block, x, y, z);
							break;
						case 5: // EAST
							renderblocks.setRenderBounds(0.6F, minY, minZ, 1.0, maxY, maxZ);
							renderblocks.renderStandardBlock(block, x, y, z);
							break;
						}
					}
				}
			}

			renderblocks.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
			renderblocks.renderStandardBlock(block, x, y, z);
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
