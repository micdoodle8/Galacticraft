package micdoodle8.mods.galacticraft.core.client.render.block;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockUnlitTorch;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * GCCoreBlockRendererUnlitTorch.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockRendererUnlitTorch implements ISimpleBlockRenderingHandler
{
	final int renderID;

	public GCCoreBlockRendererUnlitTorch(int var1)
	{
		this.renderID = var1;
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess var1, int var2, int var3, int var4, Block var5, int var6, RenderBlocks var7)
	{
		GCCoreBlockRendererUnlitTorch.renderGCUnlitTorch(var7, var5, var1, var2, var3, var4);
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

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		GCCoreBlockRendererUnlitTorch.renderTorchAtAngle(renderer, block, 0, 0, 0, 0.0D, 0.0D);
	}

	public static void renderInvNormalBlock(RenderBlocks var0, Block var1, int var2)
	{
		final Tessellator var3 = Tessellator.instance;
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var0.setRenderBounds(0.0F, 0.0F, 0.0F, 1F, 1F, 1F);
		var3.startDrawingQuads();
		var3.setNormal(0.0F, -0.8F, 0.0F);
		var0.renderFaceYNeg(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(0, var2));
		var3.draw();
		var3.startDrawingQuads();
		var3.setNormal(0.0F, 0.8F, 0.0F);
		var0.renderFaceYPos(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(1, var2));
		var3.draw();
		var3.startDrawingQuads();
		var3.setNormal(0.0F, 0.0F, -0.8F);
		var0.renderFaceXPos(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(2, var2));
		var3.draw();
		var3.startDrawingQuads();
		var3.setNormal(0.0F, 0.0F, 0.8F);
		var0.renderFaceXNeg(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(3, var2));
		var3.draw();
		var3.startDrawingQuads();
		var3.setNormal(-0.8F, 0.0F, 0.0F);
		var0.renderFaceZNeg(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(4, var2));
		var3.draw();
		var3.startDrawingQuads();
		var3.setNormal(0.8F, 0.0F, 0.0F);
		var0.renderFaceZPos(var1, 0.0D, 0.0D, 0.0D, var1.getIcon(5, var2));
		var3.draw();
	}

	public static void renderGCUnlitTorch(RenderBlocks renderBlocks, Block par1Block, IBlockAccess var1, int par2, int par3, int par4)
	{
		final int var5 = var1.getBlockMetadata(par2, par3, par4);
		final Tessellator var6 = Tessellator.instance;
		var6.setBrightness(par1Block.getMixedBrightnessForBlock(var1, par2, par3, par4));
		var6.setColorOpaque_F(1.0F, 1.0F, 1.0F);
		final double var7 = 0.4000000059604645D;
		final double var9 = 0.5D - var7;
		final double var11 = 0.20000000298023224D;

		if (var5 == 1)
		{
			GCCoreBlockRendererUnlitTorch.renderTorchAtAngle(renderBlocks, par1Block, par2 - var9, par3 + var11, par4, -var7, 0.0D);
		}
		else if (var5 == 2)
		{
			GCCoreBlockRendererUnlitTorch.renderTorchAtAngle(renderBlocks, par1Block, par2 + var9, par3 + var11, par4, var7, 0.0D);
		}
		else if (var5 == 3)
		{
			GCCoreBlockRendererUnlitTorch.renderTorchAtAngle(renderBlocks, par1Block, par2, par3 + var11, par4 - var9, 0.0D, -var7);
		}
		else if (var5 == 4)
		{
			GCCoreBlockRendererUnlitTorch.renderTorchAtAngle(renderBlocks, par1Block, par2, par3 + var11, par4 + var9, 0.0D, var7);
		}
		else
		{
			GCCoreBlockRendererUnlitTorch.renderTorchAtAngle(renderBlocks, par1Block, par2, par3, par4, 0.0D, 0.0D);
		}
	}

	public static void renderTorchAtAngle(RenderBlocks renderBlocks, Block par1Block, double par2, double par4, double par6, double par8, double par10)
	{
		// if (par1Block instanceof GCCoreBlockUnlitTorch)
		{
			final Tessellator tessellator = Tessellator.instance;
			Icon icon = renderBlocks.getBlockIconFromSideAndMetadata(par1Block, 0, 0);

			if (par1Block.blockID == GCCoreBlocks.unlitTorch.blockID)
			{
				icon = GCCoreBlockUnlitTorch.torchIcons[1];
			}

			if (par1Block.blockID == GCCoreBlocks.unlitTorchLit.blockID)
			{
				icon = GCCoreBlockUnlitTorch.torchIcons[0];
			}

			final double d5 = icon.getMinU();
			final double d6 = icon.getMinV();
			final double d7 = icon.getMaxU();
			final double d8 = icon.getMaxV();
			final double d9 = icon.getInterpolatedU(7.0D);
			final double d10 = icon.getInterpolatedV(6.0D);
			final double d11 = icon.getInterpolatedU(9.0D);
			final double d12 = icon.getInterpolatedV(8.0D);
			final double d13 = icon.getInterpolatedU(7.0D);
			final double d14 = icon.getInterpolatedV(13.0D);
			final double d15 = icon.getInterpolatedU(9.0D);
			final double d16 = icon.getInterpolatedV(15.0D);
			par2 += 0.5D;
			par6 += 0.5D;
			final double d17 = par2 - 0.5D;
			final double d18 = par2 + 0.5D;
			final double d19 = par6 - 0.5D;
			final double d20 = par6 + 0.5D;
			final double d21 = 0.0625D;
			final double d22 = 0.625D;
			tessellator.addVertexWithUV(par2 + par8 * (1.0D - d22) - d21, par4 + d22, par6 + par10 * (1.0D - d22) - d21, d9, d10);
			tessellator.addVertexWithUV(par2 + par8 * (1.0D - d22) - d21, par4 + d22, par6 + par10 * (1.0D - d22) + d21, d9, d12);
			tessellator.addVertexWithUV(par2 + par8 * (1.0D - d22) + d21, par4 + d22, par6 + par10 * (1.0D - d22) + d21, d11, d12);
			tessellator.addVertexWithUV(par2 + par8 * (1.0D - d22) + d21, par4 + d22, par6 + par10 * (1.0D - d22) - d21, d11, d10);
			tessellator.addVertexWithUV(par2 + d21 + par8, par4, par6 - d21 + par10, d15, d14);
			tessellator.addVertexWithUV(par2 + d21 + par8, par4, par6 + d21 + par10, d15, d16);
			tessellator.addVertexWithUV(par2 - d21 + par8, par4, par6 + d21 + par10, d13, d16);
			tessellator.addVertexWithUV(par2 - d21 + par8, par4, par6 - d21 + par10, d13, d14);
			tessellator.addVertexWithUV(par2 - d21, par4 + 1.0D, d19, d5, d6);
			tessellator.addVertexWithUV(par2 - d21 + par8, par4 + 0.0D, d19 + par10, d5, d8);
			tessellator.addVertexWithUV(par2 - d21 + par8, par4 + 0.0D, d20 + par10, d7, d8);
			tessellator.addVertexWithUV(par2 - d21, par4 + 1.0D, d20, d7, d6);
			tessellator.addVertexWithUV(par2 + d21, par4 + 1.0D, d20, d5, d6);
			tessellator.addVertexWithUV(par2 + par8 + d21, par4 + 0.0D, d20 + par10, d5, d8);
			tessellator.addVertexWithUV(par2 + par8 + d21, par4 + 0.0D, d19 + par10, d7, d8);
			tessellator.addVertexWithUV(par2 + d21, par4 + 1.0D, d19, d7, d6);
			tessellator.addVertexWithUV(d17, par4 + 1.0D, par6 + d21, d5, d6);
			tessellator.addVertexWithUV(d17 + par8, par4 + 0.0D, par6 + d21 + par10, d5, d8);
			tessellator.addVertexWithUV(d18 + par8, par4 + 0.0D, par6 + d21 + par10, d7, d8);
			tessellator.addVertexWithUV(d18, par4 + 1.0D, par6 + d21, d7, d6);
			tessellator.addVertexWithUV(d18, par4 + 1.0D, par6 - d21, d5, d6);
			tessellator.addVertexWithUV(d18 + par8, par4 + 0.0D, par6 - d21 + par10, d5, d8);
			tessellator.addVertexWithUV(d17 + par8, par4 + 0.0D, par6 - d21 + par10, d7, d8);
			tessellator.addVertexWithUV(d17, par4 + 1.0D, par6 - d21, d7, d6);
		}

	}
}
