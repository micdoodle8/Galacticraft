package micdoodle8.mods.galacticraft.core.client.render.block;

import micdoodle8.mods.galacticraft.api.tile.IFuelDock;
import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockLandingPadFull;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * GCCoreBlockRendererLandingPad.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class GCCoreBlockRendererLandingPad implements ISimpleBlockRenderingHandler
{
	final int renderID;

	public GCCoreBlockRendererLandingPad(int var1)
	{
		this.renderID = var1;
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess var1, int var2, int var3, int var4, Block var5, int var6, RenderBlocks var7)
	{
		this.renderBlockLandingPad(var7, var5, var1, var2, var3, var4);
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

	public static void renderInvNormalBlock(RenderBlocks var0, Block var1, int var2)
	{
		final Tessellator var3 = Tessellator.instance;
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		var0.setRenderBounds(0.15F, 0.15F, 0.15F, 0.85F, 0.85F, 0.85F);
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

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		GCCoreBlockRendererLandingPad.renderInvNormalBlock(renderer, block, metadata);
	}

	public void renderBlockLandingPad(RenderBlocks renderBlocks, Block par1Block, IBlockAccess var1, int par2, int par3, int par4)
	{
		var1.getBlockMetadata(par2, par3, par4);
		if (var1.getBlockMetadata(par2, par3, par4) == 1)
		{
			renderBlocks.setOverrideBlockTexture(((GCCoreBlockLandingPadFull) par1Block).getIcon(0, 1));
		}

		renderBlocks.setRenderBounds(-1F, 0F, -1F, 2F, 0.2F, 2F);
		renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);

		if (var1.getBlockMetadata(par2, par3, par4) == 1)
		{
			renderBlocks.setOverrideBlockTexture(((GCCoreBlockLandingPadFull) par1Block).getIcon(0, 2));
		}

		if (var1.getBlockMetadata(par2, par3, par4) == 0)
		{
			renderBlocks.setRenderBounds(-0.5F, 0.2F, -0.5F, 1.5F, 0.3F, 1.5F);
			renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
			renderBlocks.setRenderBounds(0F, 0.3F, 0F, 1F, 0.4F, 1F);
			renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
		}

		IFuelDock landingPad = (IFuelDock) var1.getBlockTileEntity(par2, par3, par4);

		// +X left
		if (landingPad.isBlockAttachable(var1, par2 + 2, par3, par4 - 1))
		{
			renderBlocks.setRenderBounds(1.5F, 0.2F, -0.9F, 2F, 0.901F, -0.1F);
			renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
		}

		// +X middle
		if (landingPad.isBlockAttachable(var1, par2 + 2, par3, par4))
		{
			renderBlocks.setRenderBounds(1.5F, 0.2F, 0.1F, 2F, 0.9F, 0.9F);
			renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
		}

		// +X right
		if (landingPad.isBlockAttachable(var1, par2 + 2, par3, par4 + 1))
		{
			renderBlocks.setRenderBounds(1.5F, 0.2F, 1.1F, 2F, 0.9F, 1.9F);
			renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
		}

		// +Z left
		if (landingPad.isBlockAttachable(var1, par2 + 1, par3, par4 + 2))
		{
			renderBlocks.setRenderBounds(1.1F, 0.2F, 1.5F, 1.9F, 0.901F, 2F);
			renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
		}

		// +Z left
		if (landingPad.isBlockAttachable(var1, par2, par3, par4 + 2))
		{
			renderBlocks.setRenderBounds(0.1F, 0.2F, 1.5F, 0.9F, 0.901F, 2F);
			renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
		}

		// +Z right
		if (landingPad.isBlockAttachable(var1, par2 - 1, par3, par4 + 2))
		{
			renderBlocks.setRenderBounds(-0.9F, 0.2F, 1.5F, -0.1F, 0.9F, 2F);
			renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
		}

		// -X left
		if (landingPad.isBlockAttachable(var1, par2 - 2, par3, par4 + 1))
		{
			renderBlocks.setRenderBounds(-1.0F, 0.2F, 1.1F, -0.5F, 0.901F, 1.9F);
			renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
		}

		// -X middle
		if (landingPad.isBlockAttachable(var1, par2 - 2, par3, par4))
		{
			renderBlocks.setRenderBounds(-1.0F, 0.2F, 0.1F, -0.5F, 0.9F, 0.9F);
			renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
		}

		// -X right
		if (landingPad.isBlockAttachable(var1, par2 - 2, par3, par4 - 1))
		{
			renderBlocks.setRenderBounds(-1.0F, 0.2F, -0.9F, -0.5F, 0.9F, -0.1F);
			renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
		}

		// -Z right
		if (landingPad.isBlockAttachable(var1, par2 + 1, par3, par4 - 2))
		{
			renderBlocks.setRenderBounds(1.1F, 0.2F, -1.0F, 1.9F, 0.9F, -0.5F);
			renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
		}

		// -Z middle
		if (landingPad.isBlockAttachable(var1, par2, par3, par4 - 2))
		{
			renderBlocks.setRenderBounds(0.1F, 0.2F, -1.0F, 0.9F, 0.9F, -0.5F);
			renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
		}

		// -Z left
		if (landingPad.isBlockAttachable(var1, par2 - 1, par3, par4 - 2))
		{
			renderBlocks.setRenderBounds(-0.9F, 0.2F, -1.0F, -0.1F, 0.901F, -0.5F);
			renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
		}

		if (var1.getBlockMetadata(par2, par3, par4) == 2)
		{
			Tessellator tessellator = Tessellator.instance;
			tessellator.setBrightness(Block.hopperBlock.getMixedBrightnessForBlock(var1, par2, par3, par4));
			float f1 = 1.0F;
			int j1 = Block.hopperBlock.colorMultiplier(var1, par2, par3, par4);
			float f = (j1 >> 16 & 255) / 255.0F;
			float f2 = (j1 >> 8 & 255) / 255.0F;
			float f3 = (j1 & 255) / 255.0F;

			if (EntityRenderer.anaglyphEnable)
			{
				float f4 = (f * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
				float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
				float f6 = (f * 30.0F + f3 * 70.0F) / 100.0F;
				f = f4;
				f2 = f5;
				f3 = f6;
			}

			tessellator.setColorOpaque_F(f1 * f, f1 * f2, f1 * f3);

			renderBlocks.clearOverrideBlockTexture();

			Icon icon = BlockHopper.getHopperIcon("hopper");
			BlockHopper.getHopperIcon("hopper_inside");

			f = 0.125F;
			double d0 = 0.625D;

			renderBlocks.setOverrideBlockTexture(icon);

			renderBlocks.setRenderBounds(0.0D, d0, 0.0D, 1.0D, 0.9D, 1.0D);
			renderBlocks.renderStandardBlock(Block.hopperBlock, par2, par3, par4);

			renderBlocks.setOverrideBlockTexture(((GCCoreBlockLandingPadFull) par1Block).getIcon(0, 0));

			renderBlocks.setRenderBounds(-0.1D, 0.0D, -0.1D, 0.0D, 1.0D, 0.0D);
			renderBlocks.renderStandardBlock(Block.hopperBlock, par2, par3, par4);

			renderBlocks.setRenderBounds(-0.1D, 0.0D, 1.0D, 0.0D, 1.0D, 1.1D);
			renderBlocks.renderStandardBlock(Block.hopperBlock, par2, par3, par4);

			renderBlocks.setRenderBounds(1.0D, 0.0D, -0.1D, 1.1D, 1.0D, 0.0D);
			renderBlocks.renderStandardBlock(Block.hopperBlock, par2, par3, par4);

			renderBlocks.setRenderBounds(1.0D, 0.0D, 1.0D, 1.1D, 1.0D, 1.1D);
			renderBlocks.renderStandardBlock(Block.hopperBlock, par2, par3, par4);

			renderBlocks.setRenderBounds(0.0D, 0.9D, 0.0D, 1.0D, 1.0D, 1.0D);
			renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);

			renderBlocks.setOverrideBlockTexture(icon);

			double d1 = 0.1D;
			double d2 = 0.1D;
			renderBlocks.setRenderBounds(d1, d2, d1, 1.0D - d1, d0 - 0.002D, 1.0D - d1);
			renderBlocks.renderStandardBlock(Block.hopperBlock, par2, par3, par4);
		}

		renderBlocks.clearOverrideBlockTexture();
		par1Block.setBlockBoundsForItemRender();
		renderBlocks.uvRotateTop = 0;
	}
}
