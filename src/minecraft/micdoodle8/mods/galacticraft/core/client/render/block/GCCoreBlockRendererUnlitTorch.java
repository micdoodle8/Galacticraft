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
 * Copyright 2012-2013, micdoodle8
 *
 *  All rights reserved.
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
        var0.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        var3.startDrawingQuads();
        var3.setNormal(0.0F, -1.0F, 0.0F);
        var0.renderBottomFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSideAndMetadata(0, var2));
        var3.draw();
        var3.startDrawingQuads();
        var3.setNormal(0.0F, 1.0F, 0.0F);
        var0.renderTopFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSideAndMetadata(1, var2));
        var3.draw();
        var3.startDrawingQuads();
        var3.setNormal(0.0F, 0.0F, -1.0F);
        var0.renderEastFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSideAndMetadata(2, var2));
        var3.draw();
        var3.startDrawingQuads();
        var3.setNormal(0.0F, 0.0F, 1.0F);
        var0.renderWestFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSideAndMetadata(3, var2));
        var3.draw();
        var3.startDrawingQuads();
        var3.setNormal(-1.0F, 0.0F, 0.0F);
        var0.renderNorthFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSideAndMetadata(4, var2));
        var3.draw();
        var3.startDrawingQuads();
        var3.setNormal(1.0F, 0.0F, 0.0F);
        var0.renderSouthFace(var1, 0.0D, 0.0D, 0.0D, var1.getBlockTextureFromSideAndMetadata(5, var2));
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
    	if (par1Block instanceof GCCoreBlockUnlitTorch)
    	{
            Tessellator tessellator = Tessellator.instance;
            Icon icon = renderBlocks.func_94165_a(par1Block, 0, 0);
            
            if (par1Block.blockID == GCCoreBlocks.unlitTorch.blockID)
            {
            	icon = GCCoreBlockUnlitTorch.torchIcons[1];
            }
            
            if (par1Block.blockID == GCCoreBlocks.unlitTorchLit.blockID)
            {
            	icon = GCCoreBlockUnlitTorch.torchIcons[0];
            }

            double d5 = (double)icon.func_94209_e();
            double d6 = (double)icon.func_94206_g();
            double d7 = (double)icon.func_94212_f();
            double d8 = (double)icon.func_94210_h();
            double d9 = (double)icon.func_94214_a(7.0D);
            double d10 = (double)icon.func_94207_b(6.0D);
            double d11 = (double)icon.func_94214_a(9.0D);
            double d12 = (double)icon.func_94207_b(8.0D);
            double d13 = (double)icon.func_94214_a(7.0D);
            double d14 = (double)icon.func_94207_b(13.0D);
            double d15 = (double)icon.func_94214_a(9.0D);
            double d16 = (double)icon.func_94207_b(15.0D);
            par2 += 0.5D;
            par6 += 0.5D;
            double d17 = par2 - 0.5D;
            double d18 = par2 + 0.5D;
            double d19 = par6 - 0.5D;
            double d20 = par6 + 0.5D;
            double d21 = 0.0625D;
            double d22 = 0.625D;
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
