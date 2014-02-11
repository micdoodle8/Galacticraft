package micdoodle8.mods.galacticraft.core.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * GCCoreBlockRendererBreathableAir.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockRendererBreathableAir implements ISimpleBlockRenderingHandler
{
	final int renderID;

	public GCCoreBlockRendererBreathableAir(int var1)
	{
		this.renderID = var1;
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess var1, int var2, int var3, int var4, Block var5, int var6, RenderBlocks var7)
	{
		GCCoreBlockRendererBreathableAir.renderBreathableAir(var7, var5, var1, var2, var3, var4);
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
		GCCoreBlockRendererBreathableAir.renderInvNormalBlock(renderer, block, metadata);
	}

	public static void renderInvNormalBlock(RenderBlocks var0, Block var1, int var2)
	{
		final Tessellator var3 = Tessellator.instance;
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var0.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
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
	}

	public static void renderBreathableAir(RenderBlocks renderBlocks, Block par1Block, IBlockAccess var1, int par2, int par3, int par4)
	{
		final Tessellator var5 = Tessellator.instance;
		final int var6 = par1Block.colorMultiplier(var1, par2, par3, par4);
		final float var7 = (var6 >> 16 & 255) / 255.0F;
		final float var8 = (var6 >> 8 & 255) / 255.0F;
		final float var9 = (var6 & 255) / 255.0F;
		final boolean var10 = par1Block.shouldSideBeRendered(var1, par2, par3 + 1, par4, 1);
		final boolean var11 = par1Block.shouldSideBeRendered(var1, par2, par3 - 1, par4, 0);
		final boolean[] var12 = new boolean[] { par1Block.shouldSideBeRendered(var1, par2, par3, par4 - 1, 2), par1Block.shouldSideBeRendered(var1, par2, par3, par4 + 1, 3), par1Block.shouldSideBeRendered(var1, par2 - 1, par3, par4, 4), par1Block.shouldSideBeRendered(var1, par2 + 1, par3, par4, 5) };

		if (!var10 && !var11 && !var12[0] && !var12[1] && !var12[2] && !var12[3])
		{
			return;
		}
		else
		{
			final float var14 = 0.5F;
			final float var15 = 1.0F;
			final float var16 = 0.8F;
			final float var17 = 0.6F;
			var1.getBlockMetadata(par2, par3, par4);
			final double var24 = 1.0D;
			final double var26 = 1.0D;
			final double var28 = 1.0D;
			final double var30 = 1.0D;
			final double var32 = 0.0010000000474974513D;
			int var34;
			int var37;

			// if (renderBlocks.renderAllFaces || var10)
			// {
			// var13 = true;
			// var34 = par1Block.getIcon(1, var23);
			// float var35 = (float)BlockFluid.getFlowDirection(var1, par2,
			// par3, par4, var22);
			//
			// if (var35 > -999.0F)
			// {
			// var34 = par1Block.getIcon(2, var23);
			// }
			//
			// var24 -= var32;
			// var26 -= var32;
			// var28 -= var32;
			// var30 -= var32;
			// int var36 = (var34 & 15) << 4;
			// var37 = var34 & 240;
			// double var38 = ((double)var36 + 8.0D) / 256.0D;
			// double var40 = ((double)var37 + 8.0D) / 256.0D;
			//
			// if (var35 < -999.0F)
			// {
			// var35 = 0.0F;
			// }
			// else
			// {
			// var38 = (double)((float)(var36 + 16) / 256.0F);
			// var40 = (double)((float)(var37 + 16) / 256.0F);
			// }
			//
			// double var42 = (double)(MathHelper.sin(var35) * 8.0F) / 256.0D;
			// double var44 = (double)(MathHelper.cos(var35) * 8.0F) / 256.0D;
			// var5.setBrightness(par1Block.getMixedBrightnessForBlock(var1,
			// par2, par3, par4));
			// float var46 = 1.0F;
			// var5.setColorOpaque_F(var15 * var46 * var7, var15 * var46 * var8,
			// var15 * var46 * var9);
			// var5.addVertexWithUV((double)(par2 + 0), (double)par3 + var24,
			// (double)(par4 + 0), var38 - var44 - var42, var40 - var44 +
			// var42);
			// var5.addVertexWithUV((double)(par2 + 0), (double)par3 + var26,
			// (double)(par4 + 1), var38 - var44 + var42, var40 + var44 +
			// var42);
			// var5.addVertexWithUV((double)(par2 + 1), (double)par3 + var28,
			// (double)(par4 + 1), var38 + var44 + var42, var40 + var44 -
			// var42);
			// var5.addVertexWithUV((double)(par2 + 1), (double)par3 + var30,
			// (double)(par4 + 0), var38 + var44 - var42, var40 - var44 -
			// var42);
			// }

			if (renderBlocks.renderAllFaces || var11)
			{
				var5.setBrightness(par1Block.getMixedBrightnessForBlock(var1, par2, par3 - 1, par4));
				final float var65 = 1.0F;
				var5.setColorOpaque_F(var14 * var65, var14 * var65, var14 * var65);
				renderBlocks.renderFaceYNeg(par1Block, par2, par3 + var32, par4, par1Block.getBlockTextureFromSide(0));
			}

			for (var34 = 0; var34 < 4; ++var34)
			{
				int var64 = par2;
				var37 = par4;

				if (var34 == 0)
				{
					var37 = par4 - 1;
				}

				if (var34 == 1)
				{
					++var37;
				}

				if (var34 == 2)
				{
					var64 = par2 - 1;
				}

				if (var34 == 3)
				{
					++var64;
				}

				// final int var66 = par1Block.getIcon(var34 + 2, var23);
				final int var66 = 0;
				final int var39 = (var66 & 15) << 4;
				final int var67 = var66 & 240;

				if (renderBlocks.renderAllFaces || var12[var34])
				{
					double var43;
					double var41;
					double var47;
					double var45;
					double var51;
					double var49;

					if (var34 == 0)
					{
						var41 = var24;
						var43 = var30;
						var45 = par2;
						var49 = par2 + 1;
						var47 = par4 + var32;
						var51 = par4 + var32;
					}
					else if (var34 == 1)
					{
						var41 = var28;
						var43 = var26;
						var45 = par2 + 1;
						var49 = par2;
						var47 = par4 + 1 - var32;
						var51 = par4 + 1 - var32;
					}
					else if (var34 == 2)
					{
						var41 = var26;
						var43 = var24;
						var45 = par2 + var32;
						var49 = par2 + var32;
						var47 = par4 + 1;
						var51 = par4;
					}
					else
					{
						var41 = var30;
						var43 = var28;
						var45 = par2 + 1 - var32;
						var49 = par2 + 1 - var32;
						var47 = par4;
						var51 = par4 + 1;
					}

					final double var53 = (var39 + 0) / 256.0F;
					final double var55 = (var39 + 16 - 0.01D) / 256.0D;
					final double var57 = (var67 + (1.0D - var41) * 16.0D) / 256.0D;
					final double var59 = (var67 + (1.0D - var43) * 16.0D) / 256.0D;
					final double var61 = (var67 + 16 - 0.01D) / 256.0D;
					var5.setBrightness(par1Block.getMixedBrightnessForBlock(var1, var64, par3, var37));
					float var63 = 1.0F;

					if (var34 < 2)
					{
						var63 *= var16;
					}
					else
					{
						var63 *= var17;
					}

					var5.setColorOpaque_F(var15 * var63 * var7, var15 * var63 * var8, var15 * var63 * var9);
					var5.addVertexWithUV(var45, par3 + var41, var47, var53, var57);
					var5.addVertexWithUV(var49, par3 + var43, var51, var55, var59);
					var5.addVertexWithUV(var49, par3 + 0, var51, var55, var61);
					var5.addVertexWithUV(var45, par3 + 0, var47, var53, var61);
				}
			}

			// renderBlocks.customMinY = var18;
			// renderBlocks.customMaxY = var20;
		}
	}
}
