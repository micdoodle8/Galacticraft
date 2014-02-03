package micdoodle8.mods.galacticraft.mars.client.render.block;

import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.WEST;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlockTintedGlassPane;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * GCMarsBlockRendererTintedGlassPane.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCMarsBlockRendererTintedGlassPane implements ISimpleBlockRenderingHandler
{
	final int renderID;

	public GCMarsBlockRendererTintedGlassPane(int var1)
	{
		this.renderID = var1;
	}

	public void renderGlassPaneInWorld(RenderBlocks renderBlocks, IBlockAccess iblockaccess, Block par1Block, int x, int y, int z)
	{
		par1Block.setBlockBoundsBasedOnState(iblockaccess, x, y, z);
		renderBlocks.setRenderBoundsFromBlock(par1Block);

		int metadata = iblockaccess.getBlockMetadata(x, y, z);

		renderBlocks.setOverrideBlockTexture(((GCMarsBlockTintedGlassPane) par1Block).getSideTextureIndex(metadata));

		this.renderBlockPane(renderBlocks, iblockaccess, (BlockPane) par1Block, x, y, z);

		renderBlocks.clearOverrideBlockTexture();
	}

	public boolean renderBlockPane(RenderBlocks renderBlocks, IBlockAccess iblockaccess, BlockPane par1BlockPane, int par2, int par3, int par4)
	{
		int l = iblockaccess.getHeight();
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(par1BlockPane.getMixedBrightnessForBlock(iblockaccess, par2, par3, par4));
		float f = 1.0F;
		int i1 = par1BlockPane.colorMultiplier(iblockaccess, par2, par3, par4);
		float f1 = (i1 >> 16 & 255) / 255.0F;
		float f2 = (i1 >> 8 & 255) / 255.0F;
		float f3 = (i1 & 255) / 255.0F;

		if (EntityRenderer.anaglyphEnable)
		{
			float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
			float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
			float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
			f1 = f4;
			f2 = f5;
			f3 = f6;
		}

		tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
		Icon icon;
		Icon icon1;

		if (renderBlocks.hasOverrideBlockTexture())
		{
			icon = renderBlocks.overrideBlockTexture;
			icon1 = renderBlocks.overrideBlockTexture;
		}
		else
		{
			int j1 = iblockaccess.getBlockMetadata(par2, par3, par4);
			icon = renderBlocks.getBlockIconFromSideAndMetadata(par1BlockPane, 0, j1);
			icon1 = par1BlockPane.getSideTextureIndex();
		}

		double d0 = icon.getMinU();
		double d1 = icon.getInterpolatedU(8.0D);
		double d2 = icon.getMaxU();
		double d3 = icon.getMinV();
		double d4 = icon.getMaxV();
		double d5 = icon1.getInterpolatedU(7.0D);
		double d6 = icon1.getInterpolatedU(9.0D);
		double d7 = icon1.getMinV();
		double d8 = icon1.getInterpolatedV(8.0D);
		double d9 = icon1.getMaxV();
		double d10 = par2;
		double d11 = par2 + 0.5D;
		double d12 = par2 + 1;
		double d13 = par4;
		double d14 = par4 + 0.5D;
		double d15 = par4 + 1;
		double d16 = par2 + 0.5D - 0.0625D;
		double d17 = par2 + 0.5D + 0.0625D;
		double d18 = par4 + 0.5D - 0.0625D;
		double d19 = par4 + 0.5D + 0.0625D;
		boolean flag = par1BlockPane.canPaneConnectTo(iblockaccess, par2, par3, par4, NORTH);
		boolean flag1 = par1BlockPane.canPaneConnectTo(iblockaccess, par2, par3, par4, SOUTH);
		boolean flag2 = par1BlockPane.canPaneConnectTo(iblockaccess, par2, par3, par4, WEST);
		boolean flag3 = par1BlockPane.canPaneConnectTo(iblockaccess, par2, par3, par4, EAST);
		boolean flag4 = par1BlockPane.shouldSideBeRendered(iblockaccess, par2, par3 + 1, par4, 1);
		boolean flag5 = par1BlockPane.shouldSideBeRendered(iblockaccess, par2, par3 - 1, par4, 0);
		if ((!flag2 || !flag3) && (flag2 || flag3 || flag || flag1))
		{
			if (flag2 && !flag3)
			{
				tessellator.addVertexWithUV(d10, par3 + 1, d14 + 0.001F, d0, d3);
				tessellator.addVertexWithUV(d10, par3 + 0, d14 + 0.001F, d0, d4);
				tessellator.addVertexWithUV(d11, par3 + 0, d14 + 0.001F, d1, d4);
				tessellator.addVertexWithUV(d11, par3 + 1, d14 + 0.001F, d1, d3);
				tessellator.addVertexWithUV(d11, par3 + 1, d14, d0, d3);
				tessellator.addVertexWithUV(d11, par3 + 0, d14, d0, d4);
				tessellator.addVertexWithUV(d10, par3 + 0, d14, d1, d4);
				tessellator.addVertexWithUV(d10, par3 + 1, d14, d1, d3);

				if (!flag1 && !flag)
				{
					tessellator.addVertexWithUV(d11 + 0.001F, par3 + 1, d19, d5, d7);
					tessellator.addVertexWithUV(d11 + 0.001F, par3 + 0, d19, d5, d9);
					tessellator.addVertexWithUV(d11 + 0.001F, par3 + 0, d18, d6, d9);
					tessellator.addVertexWithUV(d11 + 0.001F, par3 + 1, d18, d6, d7);
					tessellator.addVertexWithUV(d11, par3 + 1, d18, d5, d7);
					tessellator.addVertexWithUV(d11, par3 + 0, d18, d5, d9);
					tessellator.addVertexWithUV(d11, par3 + 0, d19, d6, d9);
					tessellator.addVertexWithUV(d11, par3 + 1, d19, d6, d7);
				}

				if (flag4 || par3 < l - 1 && iblockaccess.isAirBlock(par2 - 1, par3 + 1, par4))
				{
					tessellator.addVertexWithUV(d10 + 0.001F, par3 + 1 + 0.001D, d19, d6, d8);
					tessellator.addVertexWithUV(d11 + 0.001F, par3 + 1 + 0.001D, d19, d6, d9);
					tessellator.addVertexWithUV(d11 + 0.001F, par3 + 1 + 0.001D, d18, d5, d9);
					tessellator.addVertexWithUV(d10 + 0.001F, par3 + 1 + 0.001D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d19, d6, d9);
					tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d18, d5, d9);
					tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d18, d5, d8);
				}

				if (flag5 || par3 > 1 && iblockaccess.isAirBlock(par2 - 1, par3 - 1, par4))
				{
					tessellator.addVertexWithUV(d10 + 0.001F, par3 - 0.001D, d19, d6, d8);
					tessellator.addVertexWithUV(d11 + 0.001F, par3 - 0.001D, d19, d6, d9);
					tessellator.addVertexWithUV(d11 + 0.001F, par3 - 0.001D, d18, d5, d9);
					tessellator.addVertexWithUV(d10 + 0.001F, par3 - 0.001D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, par3 - 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d10, par3 - 0.01D, d19, d6, d9);
					tessellator.addVertexWithUV(d10, par3 - 0.01D, d18, d5, d9);
					tessellator.addVertexWithUV(d11, par3 - 0.01D, d18, d5, d8);
				}
			}
			else if (!flag2 && flag3)
			{
				tessellator.addVertexWithUV(d11, par3 + 1, d14 + 0.001F, d1, d3);
				tessellator.addVertexWithUV(d11, par3 + 0, d14 + 0.001F, d1, d4);
				tessellator.addVertexWithUV(d12, par3 + 0, d14 + 0.001F, d2, d4);
				tessellator.addVertexWithUV(d12, par3 + 1, d14 + 0.001F, d2, d3);
				tessellator.addVertexWithUV(d12, par3 + 1, d14, d1, d3);
				tessellator.addVertexWithUV(d12, par3 + 0, d14, d1, d4);
				tessellator.addVertexWithUV(d11, par3 + 0, d14, d2, d4);
				tessellator.addVertexWithUV(d11, par3 + 1, d14, d2, d3);

				if (!flag1 && !flag)
				{
					tessellator.addVertexWithUV(d11 + 0.001F, par3 + 1, d18, d5, d7);
					tessellator.addVertexWithUV(d11 + 0.001F, par3 + 0, d18, d5, d9);
					tessellator.addVertexWithUV(d11 + 0.001F, par3 + 0, d19, d6, d9);
					tessellator.addVertexWithUV(d11 + 0.001F, par3 + 1, d19, d6, d7);
					tessellator.addVertexWithUV(d11, par3 + 1, d19, d5, d7);
					tessellator.addVertexWithUV(d11, par3 + 0, d19, d5, d9);
					tessellator.addVertexWithUV(d11, par3 + 0, d18, d6, d9);
					tessellator.addVertexWithUV(d11, par3 + 1, d18, d6, d7);
				}

				if (flag4 || par3 < l - 1 && iblockaccess.isAirBlock(par2 + 1, par3 + 1, par4))
				{
					tessellator.addVertexWithUV(d11, par3 + 1 + 0.001D, d19 + 0.001F, d6, d7);
					tessellator.addVertexWithUV(d12, par3 + 1 + 0.001D, d19 + 0.001F, d6, d8);
					tessellator.addVertexWithUV(d12, par3 + 1 + 0.001D, d18 + 0.001F, d5, d8);
					tessellator.addVertexWithUV(d11, par3 + 1 + 0.001D, d18 + 0.001F, d5, d7);
					tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d19, d6, d7);
					tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d18, d5, d7);
				}

				if (flag5 || par3 > 1 && iblockaccess.isAirBlock(par2 + 1, par3 - 1, par4))
				{
					tessellator.addVertexWithUV(d11, par3 - 0.001D, d19 + 0.001F, d6, d7);
					tessellator.addVertexWithUV(d12, par3 - 0.001D, d19 + 0.001F, d6, d8);
					tessellator.addVertexWithUV(d12, par3 - 0.001D, d18 + 0.001F, d5, d8);
					tessellator.addVertexWithUV(d11, par3 - 0.001D, d18 + 0.001F, d5, d7);
					tessellator.addVertexWithUV(d12, par3 - 0.01D, d19, d6, d7);
					tessellator.addVertexWithUV(d11, par3 - 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, par3 - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d12, par3 - 0.01D, d18, d5, d7);
				}
			}
		}
		else
		{
			tessellator.addVertexWithUV(d10, par3 + 1, d14 + 0.001F, d0, d3);
			tessellator.addVertexWithUV(d10, par3 + 0, d14 + 0.001F, d0, d4);
			tessellator.addVertexWithUV(d12, par3 + 0, d14 + 0.001F, d2, d4);
			tessellator.addVertexWithUV(d12, par3 + 1, d14 + 0.001F, d2, d3);
			tessellator.addVertexWithUV(d12, par3 + 1, d14, d0, d3);
			tessellator.addVertexWithUV(d12, par3 + 0, d14, d0, d4);
			tessellator.addVertexWithUV(d10, par3 + 0, d14, d2, d4);
			tessellator.addVertexWithUV(d10, par3 + 1, d14, d2, d3);

			if (flag4)
			{
				tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d19, d6, d9);
				tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d19, d6, d7);
				tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d18, d5, d7);
				tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d18, d5, d9);
				tessellator.addVertexWithUV(d12, par3 + 1 + 0.001D, d19, d6, d9);
				tessellator.addVertexWithUV(d10, par3 + 1 + 0.001D, d19, d6, d7);
				tessellator.addVertexWithUV(d10, par3 + 1 + 0.001D, d18, d5, d7);
				tessellator.addVertexWithUV(d12, par3 + 1 + 0.001D, d18, d5, d9);
			}
			else
			{
				if (par3 < l - 1 && iblockaccess.isAirBlock(par2 - 1, par3 + 1, par4))
				{
					tessellator.addVertexWithUV(d10, par3 + 1 + 0.001D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, par3 + 1 + 0.001D, d19, d6, d9);
					tessellator.addVertexWithUV(d11, par3 + 1 + 0.001D, d18, d5, d9);
					tessellator.addVertexWithUV(d10, par3 + 1 + 0.001D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d19, d6, d9);
					tessellator.addVertexWithUV(d10, par3 + 1 + 0.01D, d18, d5, d9);
					tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d18, d5, d8);
				}

				if (par3 < l - 1 && iblockaccess.isAirBlock(par2 + 1, par3 + 1, par4))
				{
					tessellator.addVertexWithUV(d11, par3 + 1 + 0.001D, d19, d6, d7);
					tessellator.addVertexWithUV(d12, par3 + 1 + 0.001D, d19, d6, d8);
					tessellator.addVertexWithUV(d12, par3 + 1 + 0.001D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, par3 + 1 + 0.001D, d18, d5, d7);
					tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d19, d6, d7);
					tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, par3 + 1 + 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d12, par3 + 1 + 0.01D, d18, d5, d7);
				}
			}

			if (flag5)
			{
				tessellator.addVertexWithUV(d10, par3 - 0.001D, d19, d6, d9);
				tessellator.addVertexWithUV(d12, par3 - 0.001D, d19, d6, d7);
				tessellator.addVertexWithUV(d12, par3 - 0.001D, d18, d5, d7);
				tessellator.addVertexWithUV(d10, par3 - 0.001D, d18, d5, d9);
				tessellator.addVertexWithUV(d12, par3 - 0.01D, d19, d6, d9);
				tessellator.addVertexWithUV(d10, par3 - 0.01D, d19, d6, d7);
				tessellator.addVertexWithUV(d10, par3 - 0.01D, d18, d5, d7);
				tessellator.addVertexWithUV(d12, par3 - 0.01D, d18, d5, d9);
			}
			else
			{
				if (par3 > 1 && iblockaccess.isAirBlock(par2 - 1, par3 - 1, par4))
				{
					tessellator.addVertexWithUV(d10, par3 - 0.001D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, par3 - 0.001D, d19, d6, d9);
					tessellator.addVertexWithUV(d11, par3 - 0.001D, d18, d5, d9);
					tessellator.addVertexWithUV(d10, par3 - 0.001D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, par3 - 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d10, par3 - 0.01D, d19, d6, d9);
					tessellator.addVertexWithUV(d10, par3 - 0.01D, d18, d5, d9);
					tessellator.addVertexWithUV(d11, par3 - 0.01D, d18, d5, d8);
				}

				if (par3 > 1 && iblockaccess.isAirBlock(par2 + 1, par3 - 1, par4))
				{
					tessellator.addVertexWithUV(d11, par3 - 0.001D, d19, d6, d7);
					tessellator.addVertexWithUV(d12, par3 - 0.001D, d19, d6, d8);
					tessellator.addVertexWithUV(d12, par3 - 0.001D, d18, d5, d8);
					tessellator.addVertexWithUV(d11, par3 - 0.001D, d18, d5, d7);
					tessellator.addVertexWithUV(d12, par3 - 0.01D, d19, d6, d7);
					tessellator.addVertexWithUV(d11, par3 - 0.01D, d19, d6, d8);
					tessellator.addVertexWithUV(d11, par3 - 0.01D, d18, d5, d8);
					tessellator.addVertexWithUV(d12, par3 - 0.01D, d18, d5, d7);
				}
			}
		}

		if ((!flag || !flag1) && (flag2 || flag3 || flag || flag1))
		{
			if (flag && !flag1)
			{
				tessellator.addVertexWithUV(d11 + 0.001F, par3 + 1, d13, d0, d3);
				tessellator.addVertexWithUV(d11 + 0.001F, par3 + 0, d13, d0, d4);
				tessellator.addVertexWithUV(d11 + 0.001F, par3 + 0, d14, d1, d4);
				tessellator.addVertexWithUV(d11 + 0.001F, par3 + 1, d14, d1, d3);
				tessellator.addVertexWithUV(d11, par3 + 1, d14, d0, d3);
				tessellator.addVertexWithUV(d11, par3 + 0, d14, d0, d4);
				tessellator.addVertexWithUV(d11, par3 + 0, d13, d1, d4);
				tessellator.addVertexWithUV(d11, par3 + 1, d13, d1, d3);

				if (!flag3 && !flag2)
				{
					tessellator.addVertexWithUV(d16, par3 + 1, d14 + 0.001F, d5, d7);
					tessellator.addVertexWithUV(d16, par3 + 0, d14 + 0.001F, d5, d9);
					tessellator.addVertexWithUV(d17, par3 + 0, d14 + 0.001F, d6, d9);
					tessellator.addVertexWithUV(d17, par3 + 1, d14 + 0.001F, d6, d7);
					tessellator.addVertexWithUV(d17, par3 + 1, d14, d5, d7);
					tessellator.addVertexWithUV(d17, par3 + 0, d14, d5, d9);
					tessellator.addVertexWithUV(d16, par3 + 0, d14, d6, d9);
					tessellator.addVertexWithUV(d16, par3 + 1, d14, d6, d7);
				}

				if (flag4 || par3 < l - 1 && iblockaccess.isAirBlock(par2, par3 + 1, par4 - 1))
				{
					tessellator.addVertexWithUV(d16 + 0.001F, par3 + 1 + 0.0005D, d13, d6, d7);
					tessellator.addVertexWithUV(d16 + 0.001F, par3 + 1 + 0.0005D, d14, d6, d8);
					tessellator.addVertexWithUV(d17 + 0.001F, par3 + 1 + 0.0005D, d14, d5, d8);
					tessellator.addVertexWithUV(d17 + 0.001F, par3 + 1 + 0.0005D, d13, d5, d7);
					tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d14, d6, d7);
					tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d13, d6, d8);
					tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d13, d5, d8);
					tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d14, d5, d7);
				}

				if (flag5 || par3 > 1 && iblockaccess.isAirBlock(par2, par3 - 1, par4 - 1))
				{
					tessellator.addVertexWithUV(d16 + 0.001F, par3 - 0.0005D, d13, d6, d7);
					tessellator.addVertexWithUV(d16 + 0.001F, par3 - 0.0005D, d14, d6, d8);
					tessellator.addVertexWithUV(d17 + 0.001F, par3 - 0.0005D, d14, d5, d8);
					tessellator.addVertexWithUV(d17 + 0.001F, par3 - 0.0005D, d13, d5, d7);
					tessellator.addVertexWithUV(d16, par3 - 0.005D, d14, d6, d7);
					tessellator.addVertexWithUV(d16, par3 - 0.005D, d13, d6, d8);
					tessellator.addVertexWithUV(d17, par3 - 0.005D, d13, d5, d8);
					tessellator.addVertexWithUV(d17, par3 - 0.005D, d14, d5, d7);
				}
			}
			else if (!flag && flag1)
			{
				tessellator.addVertexWithUV(d11 + 0.001F, par3 + 1, d14, d1, d3);
				tessellator.addVertexWithUV(d11 + 0.001F, par3 + 0, d14, d1, d4);
				tessellator.addVertexWithUV(d11 + 0.001F, par3 + 0, d15, d2, d4);
				tessellator.addVertexWithUV(d11 + 0.001F, par3 + 1, d15, d2, d3);
				tessellator.addVertexWithUV(d11, par3 + 1, d15, d1, d3);
				tessellator.addVertexWithUV(d11, par3 + 0, d15, d1, d4);
				tessellator.addVertexWithUV(d11, par3 + 0, d14, d2, d4);
				tessellator.addVertexWithUV(d11, par3 + 1, d14, d2, d3);

				if (!flag3 && !flag2)
				{
					tessellator.addVertexWithUV(d17, par3 + 1, d14 + 0.001F, d5, d7);
					tessellator.addVertexWithUV(d17, par3 + 0, d14 + 0.001F, d5, d9);
					tessellator.addVertexWithUV(d16, par3 + 0, d14 + 0.001F, d6, d9);
					tessellator.addVertexWithUV(d16, par3 + 1, d14 + 0.001F, d6, d7);
					tessellator.addVertexWithUV(d16, par3 + 1, d14, d5, d7);
					tessellator.addVertexWithUV(d16, par3 + 0, d14, d5, d9);
					tessellator.addVertexWithUV(d17, par3 + 0, d14, d6, d9);
					tessellator.addVertexWithUV(d17, par3 + 1, d14, d6, d7);
				}

				if (flag4 || par3 < l - 1 && iblockaccess.isAirBlock(par2, par3 + 1, par4 + 1))
				{
					tessellator.addVertexWithUV(d16, par3 + 1 + 0.0005D, d14, d5, d8);
					tessellator.addVertexWithUV(d16, par3 + 1 + 0.0005D, d15, d5, d9);
					tessellator.addVertexWithUV(d17, par3 + 1 + 0.0005D, d15, d6, d9);
					tessellator.addVertexWithUV(d17, par3 + 1 + 0.0005D, d14, d6, d8);
					tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d15, d5, d8);
					tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d14, d5, d9);
					tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d14, d6, d9);
					tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d15, d6, d8);
				}

				if (flag5 || par3 > 1 && iblockaccess.isAirBlock(par2, par3 - 1, par4 + 1))
				{
					tessellator.addVertexWithUV(d16, par3 - 0.0005D, d14, d5, d8);
					tessellator.addVertexWithUV(d16, par3 - 0.0005D, d15, d5, d9);
					tessellator.addVertexWithUV(d17, par3 - 0.0005D, d15, d6, d9);
					tessellator.addVertexWithUV(d17, par3 - 0.0005D, d14, d6, d8);
					tessellator.addVertexWithUV(d16, par3 - 0.005D, d15, d5, d8);
					tessellator.addVertexWithUV(d16, par3 - 0.005D, d14, d5, d9);
					tessellator.addVertexWithUV(d17, par3 - 0.005D, d14, d6, d9);
					tessellator.addVertexWithUV(d17, par3 - 0.005D, d15, d6, d8);
				}
			}
		}
		else
		{
			tessellator.addVertexWithUV(d11 + 0.001F, par3 + 1, d15, d0, d3);
			tessellator.addVertexWithUV(d11 + 0.001F, par3 + 0, d15, d0, d4);
			tessellator.addVertexWithUV(d11 + 0.001F, par3 + 0, d13, d2, d4);
			tessellator.addVertexWithUV(d11 + 0.001F, par3 + 1, d13, d2, d3);
			tessellator.addVertexWithUV(d11, par3 + 1, d13, d0, d3);
			tessellator.addVertexWithUV(d11, par3 + 0, d13, d0, d4);
			tessellator.addVertexWithUV(d11, par3 + 0, d15, d2, d4);
			tessellator.addVertexWithUV(d11, par3 + 1, d15, d2, d3);

			if (flag4)
			{
				tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d15, d6, d9);
				tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d13, d6, d7);
				tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d13, d5, d7);
				tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d15, d5, d9);
				tessellator.addVertexWithUV(d17, par3 + 1 + 0.0005D, d13, d6, d9);
				tessellator.addVertexWithUV(d17, par3 + 1 + 0.0005D, d15, d6, d7);
				tessellator.addVertexWithUV(d16, par3 + 1 + 0.0005D, d15, d5, d7);
				tessellator.addVertexWithUV(d16, par3 + 1 + 0.0005D, d13, d5, d9);
			}
			else
			{
				if (par3 < l - 1 && iblockaccess.isAirBlock(par2, par3 + 1, par4 - 1))
				{
					tessellator.addVertexWithUV(d16, par3 + 1 + 0.0005D, d13, d6, d7);
					tessellator.addVertexWithUV(d16, par3 + 1 + 0.0005D, d14, d6, d8);
					tessellator.addVertexWithUV(d17, par3 + 1 + 0.0005D, d14, d5, d8);
					tessellator.addVertexWithUV(d17, par3 + 1 + 0.0005D, d13, d5, d7);
					tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d14, d6, d7);
					tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d13, d6, d8);
					tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d13, d5, d8);
					tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d14, d5, d7);
				}

				if (par3 < l - 1 && iblockaccess.isAirBlock(par2, par3 + 1, par4 + 1))
				{
					tessellator.addVertexWithUV(d16, par3 + 1 + 0.0005D, d14, d5, d8);
					tessellator.addVertexWithUV(d16, par3 + 1 + 0.0005D, d15, d5, d9);
					tessellator.addVertexWithUV(d17, par3 + 1 + 0.0005D, d15, d6, d9);
					tessellator.addVertexWithUV(d17, par3 + 1 + 0.0005D, d14, d6, d8);
					tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d15, d5, d8);
					tessellator.addVertexWithUV(d16, par3 + 1 + 0.005D, d14, d5, d9);
					tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d14, d6, d9);
					tessellator.addVertexWithUV(d17, par3 + 1 + 0.005D, d15, d6, d8);
				}
			}

			if (flag5)
			{
				tessellator.addVertexWithUV(d17, par3 - 0.0005D, d15, d6, d9);
				tessellator.addVertexWithUV(d17, par3 - 0.0005D, d13, d6, d7);
				tessellator.addVertexWithUV(d16, par3 - 0.0005D, d13, d5, d7);
				tessellator.addVertexWithUV(d16, par3 - 0.0005D, d15, d5, d9);
				tessellator.addVertexWithUV(d17, par3 - 0.005D, d13, d6, d9);
				tessellator.addVertexWithUV(d17, par3 - 0.005D, d15, d6, d7);
				tessellator.addVertexWithUV(d16, par3 - 0.005D, d15, d5, d7);
				tessellator.addVertexWithUV(d16, par3 - 0.005D, d13, d5, d9);
			}
			else
			{
				if (par3 > 1 && iblockaccess.isAirBlock(par2, par3 - 1, par4 - 1))
				{
					tessellator.addVertexWithUV(d16, par3 - 0.0005D, d13, d6, d7);
					tessellator.addVertexWithUV(d16, par3 - 0.0005D, d14, d6, d8);
					tessellator.addVertexWithUV(d17, par3 - 0.0005D, d14, d5, d8);
					tessellator.addVertexWithUV(d17, par3 - 0.0005D, d13, d5, d7);
					tessellator.addVertexWithUV(d16, par3 - 0.005D, d14, d6, d7);
					tessellator.addVertexWithUV(d16, par3 - 0.005D, d13, d6, d8);
					tessellator.addVertexWithUV(d17, par3 - 0.005D, d13, d5, d8);
					tessellator.addVertexWithUV(d17, par3 - 0.005D, d14, d5, d7);
				}

				if (par3 > 1 && iblockaccess.isAirBlock(par2, par3 - 1, par4 + 1))
				{
					tessellator.addVertexWithUV(d16, par3 - 0.0005D, d14, d5, d8);
					tessellator.addVertexWithUV(d16, par3 - 0.0005D, d15, d5, d9);
					tessellator.addVertexWithUV(d17, par3 - 0.0005D, d15, d6, d9);
					tessellator.addVertexWithUV(d17, par3 - 0.0005D, d14, d6, d8);
					tessellator.addVertexWithUV(d16, par3 - 0.005D, d15, d5, d8);
					tessellator.addVertexWithUV(d16, par3 - 0.005D, d14, d5, d9);
					tessellator.addVertexWithUV(d17, par3 - 0.005D, d14, d6, d9);
					tessellator.addVertexWithUV(d17, par3 - 0.005D, d15, d6, d8);
				}
			}
		}

		return true;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		// Tessellator tessellator = Tessellator.instance;
		// block.setBlockBoundsForItemRender();
		// renderer.setRenderBoundsFromBlock(block);
		// GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		// GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		// tessellator.startDrawingQuads();
		// tessellator.setNormal(0.0F, -1.0F, 0.0F);
		// renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D,
		// renderer.getBlockIconFromSideAndMetadata(block, 0, metadata));
		// tessellator.draw();
		//
		// tessellator.startDrawingQuads();
		// tessellator.setNormal(0.0F, 1.0F, 0.0F);
		// renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D,
		// renderer.getBlockIconFromSideAndMetadata(block, 1, metadata));
		// tessellator.draw();
		//
		// tessellator.startDrawingQuads();
		// tessellator.setNormal(0.0F, 0.0F, -1.0F);
		// renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D,
		// renderer.getBlockIconFromSideAndMetadata(block, 2, metadata));
		// tessellator.draw();
		// tessellator.startDrawingQuads();
		// tessellator.setNormal(0.0F, 0.0F, 1.0F);
		// renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D,
		// renderer.getBlockIconFromSideAndMetadata(block, 3, metadata));
		// tessellator.draw();
		// tessellator.startDrawingQuads();
		// tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		// renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D,
		// renderer.getBlockIconFromSideAndMetadata(block, 4, metadata));
		// tessellator.draw();
		// tessellator.startDrawingQuads();
		// tessellator.setNormal(1.0F, 0.0F, 0.0F);
		// renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D,
		// renderer.getBlockIconFromSideAndMetadata(block, 5, metadata));
		// tessellator.draw();
		// GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
		this.renderGlassPaneInWorld(renderer, world, block, x, y, z);
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
