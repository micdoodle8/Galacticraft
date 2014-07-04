package micdoodle8.mods.galacticraft.planets.asteroids.client.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;

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
		var0.setOverrideBlockTexture(AsteroidBlocks.blockWalkway.getBlockTextureFromSide(0));
		GL11.glPushMatrix();

		GL11.glRotatef(20, 0, 1, 0);

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

		if (var1 == AsteroidBlocks.blockWalkwayOxygenPipe)
		{
			var0.setOverrideBlockTexture(GCBlocks.oxygenPipe.getBlockTextureFromSide(0));
			final float minX = 0.4F;
			final float minY = 0.4F;
			final float minZ = 0.4F;
			final float maxX = 0.6F;
			final float maxY = 0.6F;
			final float maxZ = 0.6F;
			var0.setRenderBounds(minX, minY, 0.0, maxX, maxY, 0.4F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
			var0.setRenderBounds(minX, minY, 0.6F, maxX, maxY, 1.0);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
			var0.setRenderBounds(0.0, minY, minZ, 0.4F, maxY, maxZ);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
			var0.setRenderBounds(0.6F, minY, minZ, 1.0, maxY, maxZ);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
		}
		else if (var1 == AsteroidBlocks.blockWalkwayWire)
		{
			var0.setOverrideBlockTexture(GCBlocks.aluminumWire.getBlockTextureFromSide(0));

			GL11.glPushMatrix();
			GL11.glScalef(1.4F, 2.25F, 1.4F);
			GL11.glTranslatef(0.0F, 0.0F, 0.4F);
			var0.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
			GL11.glTranslatef(0.0F, 0.0F, 0.2F);
			var0.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			GL11.glScalef(1.4F, 2.25F, 1.4F);
			GL11.glTranslatef(0.4F, 0.0F, 0.0F);
			var0.setRenderBounds(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
			GL11.glTranslatef(0.2F, 0.0F, 0.0F);
			var0.setRenderBounds(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			GL11.glScalef(1.4F, 1.4F, 2.25F);
			GL11.glTranslatef(0.0F, 0.4F, 0.0F);
			var0.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
			GL11.glTranslatef(0.0F, 0.2F, 0.0F);
			var0.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			GL11.glRotatef(90, 0, 1, 0);
			GL11.glScalef(1.4F, 1.4F, 2.25F);
			GL11.glTranslatef(0.0F, 0.4F, 0.0F);
			var0.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
			GL11.glTranslatef(0.0F, 0.2F, 0.0F);
			var0.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			GL11.glScalef(2.3F, 2.3F, 2.3F);
			GL11.glTranslatef(0.234F, 0.00F, 0.375F);
			var0.setRenderBounds(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.185F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			GL11.glScalef(2.3F, 2.3F, 2.3F);
			GL11.glTranslatef(1.0F - 0.234F, 0.00F, 0.375F);
			var0.setRenderBounds(0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.185F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			GL11.glScalef(2.3F, 2.3F, 2.3F);
			GL11.glTranslatef(0.375F, 0.00F, 0.234F);
			var0.setRenderBounds(0.0F, 0.0F, 0.0F, 0.185F, 1.0F, 0.0F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			GL11.glScalef(2.3F, 2.3F, 2.3F);
			GL11.glTranslatef(0.375F, 0.00F, 1.0F - 0.234F);
			var0.setRenderBounds(0.0F, 0.0F, 0.0F, 0.185F, 1.0F, 0.0F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
			GL11.glPopMatrix();
		}
		else
		{
			// Top Runway
			var0.setRenderBounds(0.4F, 0.9F, 0.0F, 0.6F, 1.0F, 0.4F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);

			var0.setRenderBounds(0.6F, 0.9F, 0.4F, 1.0F, 1.0F, 0.6F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);

			var0.setRenderBounds(0.0F, 0.9F, 0.4F, 0.4F, 1.0F, 0.6F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);

			var0.setRenderBounds(0.4F, 0.9F, 0.6F, 0.6F, 1.0F, 1.0F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);

			// Inner Steel Bar
			var0.setRenderBounds(0.4F, 0.4F, 0.0F, 0.6F, 0.6F, 0.4F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);

			var0.setRenderBounds(0.6F, 0.4F, 0.4F, 1.0F, 0.6F, 0.6F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);

			var0.setRenderBounds(0.0F, 0.4F, 0.4F, 0.4F, 0.6F, 0.6F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);

			var0.setRenderBounds(0.4F, 0.4F, 0.6F, 0.6F, 0.6F, 1.0F);
			BlockRendererWalkway.renderStandardBlock(var0, var1, var2);
		}

		GL11.glPopMatrix();
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
		boolean connectedNorth2 = connectedNorth;
		boolean connectedEast2 = connectedEast;
		boolean connectedSouth2 = connectedSouth;
		boolean connectedWest2 = connectedWest;

		if (block == AsteroidBlocks.blockWalkwayOxygenPipe)
		{
			renderBlocks.setOverrideBlockTexture(GCBlocks.oxygenPipe.getBlockTextureFromSide(0));

			final TileEntity tileEntity = var1.getTileEntity(x, y, z);

			final float minX = 0.4F;
			final float minY = 0.4F;
			final float minZ = 0.4F;
			final float maxX = 0.6F;
			final float maxY = 0.6F;
			final float maxZ = 0.6F;

			if (tileEntity != null)
			{
				final TileEntity[] connections = WorldUtil.getAdjacentOxygenConnections(tileEntity);

				for (TileEntity connection : connections)
				{
					if (connection != null)
					{
						final int side = Arrays.asList(connections).indexOf(connection);

						switch (side)
						{
						case 0: // DOWN
							renderBlocks.setRenderBounds(minX, 0.0F, minZ, maxX, 0.15F, maxZ);
							renderBlocks.renderStandardBlock(block, x, y, z);
							break;
						case 1: // UP
							renderBlocks.setRenderBounds(minX, 0.6F, minZ, maxX, 1.0F, maxZ);
							renderBlocks.renderStandardBlock(block, x, y, z);
							break;
						case 2: // NORTH
							connectedNorth = false;
							renderBlocks.setRenderBounds(minX, minY, 0.0, maxX, maxY, 0.4F);
							renderBlocks.renderStandardBlock(block, x, y, z);
							break;
						case 3: // SOUTH
							connectedSouth = false;
							renderBlocks.setRenderBounds(minX, minY, 0.6F, maxX, maxY, 1.0);
							renderBlocks.renderStandardBlock(block, x, y, z);
							break;
						case 4: // WEST
							connectedWest = false;
							renderBlocks.setRenderBounds(0.0, minY, minZ, 0.4F, maxY, maxZ);
							renderBlocks.renderStandardBlock(block, x, y, z);
							break;
						case 5: // EAST
							connectedEast = false;
							renderBlocks.setRenderBounds(0.6F, minY, minZ, 1.0, maxY, maxZ);
							renderBlocks.renderStandardBlock(block, x, y, z);
							break;
						}
					}
				}

				renderBlocks.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
				renderBlocks.renderStandardBlock(block, x, y, z);
			}
		}

		renderBlocks.setOverrideBlockTexture(block.getIcon(var1, x, y, z, 0));

		if (connectedNorth2)
		{
			// Top Runway
			renderBlocks.setRenderBounds(0.4F, 0.9F, 0.0F, 0.6F, 1.0F, 0.4F);
			renderBlocks.renderStandardBlock(block, x, y, z);
		}

		if (connectedEast2)
		{
			renderBlocks.setRenderBounds(0.6F, 0.9F, 0.4F, 1.0F, 1.0F, 0.6F);
			renderBlocks.renderStandardBlock(block, x, y, z);
		}

		if (connectedWest2)
		{
			renderBlocks.setRenderBounds(0.0F, 0.9F, 0.4F, 0.4F, 1.0F, 0.6F);
			renderBlocks.renderStandardBlock(block, x, y, z);
		}

		if (connectedSouth2)
		{
			renderBlocks.setRenderBounds(0.4F, 0.9F, 0.6F, 0.6F, 1.0F, 1.0F);
			renderBlocks.renderStandardBlock(block, x, y, z);
		}

		if (connectedNorth)
		{
			// Inner Steel Bar
			renderBlocks.setRenderBounds(0.4F, 0.4F, 0.0F, 0.6F, 0.6F, 0.4F);
			renderBlocks.renderStandardBlock(block, x, y, z);
		}

		if (connectedEast)
		{
			renderBlocks.setRenderBounds(0.6F, 0.4F, 0.4F, 1.0F, 0.6F, 0.6F);
			renderBlocks.renderStandardBlock(block, x, y, z);
		}

		if (connectedWest)
		{
			renderBlocks.setRenderBounds(0.0F, 0.4F, 0.4F, 0.4F, 0.6F, 0.6F);
			renderBlocks.renderStandardBlock(block, x, y, z);
		}

		if (connectedSouth)
		{
			renderBlocks.setRenderBounds(0.4F, 0.4F, 0.6F, 0.6F, 0.6F, 1.0F);
			renderBlocks.renderStandardBlock(block, x, y, z);
		}

		renderBlocks.clearOverrideBlockTexture();
		block.setBlockBoundsForItemRender();
	}
}
