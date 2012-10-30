package micdoodle8.mods.galacticraft.mars.client;

import micdoodle8.mods.galacticraft.mars.GCMarsBlockFluid;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCMarsBlockRendererBacterialSludge implements ISimpleBlockRenderingHandler
{
    final int renderID;

    public GCMarsBlockRendererBacterialSludge(int var1)
    {
        this.renderID = var1;
    }

    public boolean renderWorldBlock(IBlockAccess var1, int var2, int var3, int var4, Block var5, int var6, RenderBlocks var7)
    {
    	this.renderGCFluid(var7, var5, var1, var2, var3, var4);
        return true;
    }

    public boolean shouldRender3DInInventory()
    {
        return true;
    }

    public int getRenderId()
    {
        return this.renderID;
    }

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) 
	{
        renderInvNormalBlock(renderer, block, metadata);
	}

    public static void renderInvNormalBlock(RenderBlocks var0, Block var1, int var2)
    {
        Tessellator var3 = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        var0.func_83020_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
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
    
    public static void renderGCFluid(RenderBlocks renderBlocks, Block par1Block, IBlockAccess var1, int par2, int par3, int par4)
    {
    	Tessellator var5 = Tessellator.instance;
        int var6 = par1Block.colorMultiplier(var1, par2, par3, par4);
        float var7 = (var6 >> 16 & 255) / 255.0F;
        float var8 = (var6 >> 8 & 255) / 255.0F;
        float var9 = (var6 & 255) / 255.0F;
        boolean var10 = par1Block.shouldSideBeRendered(var1, par2, par3 + 1, par4, 1);
        boolean var11 = par1Block.shouldSideBeRendered(var1, par2, par3 - 1, par4, 0);
        boolean[] var12 = new boolean[] {par1Block.shouldSideBeRendered(var1, par2, par3, par4 - 1, 2), par1Block.shouldSideBeRendered(var1, par2, par3, par4 + 1, 3), par1Block.shouldSideBeRendered(var1, par2 - 1, par3, par4, 4), par1Block.shouldSideBeRendered(var1, par2 + 1, par3, par4, 5)};

        if (!var10 && !var11 && !var12[0] && !var12[1] && !var12[2] && !var12[3])
        {
            return;
        }
        else
        {
            boolean var13 = false;
            float var14 = 0.5F;
            float var15 = 1.0F;
            float var16 = 0.8F;
            float var17 = 0.6F;
            double var18 = 0.0D;
            double var20 = 1.0D;
            Material var22 = par1Block.blockMaterial;
            int var23 = var1.getBlockMetadata(par2, par3, par4);
            double var24 = getFluidHeight(var1, par2, par3, par4, var22);
            double var26 = getFluidHeight(var1, par2, par3, par4 + 1, var22);
            double var28 = getFluidHeight(var1, par2 + 1, par3, par4 + 1, var22);
            double var30 = getFluidHeight(var1, par2 + 1, par3, par4, var22);
            double var32 = 0.0010000000474974513D;
            int var34;
            int var37;

//            if (this.renderAllFaces || var10)
            {
                var13 = true;
                var34 = par1Block.getBlockTextureFromSideAndMetadata(1, var23);
                float var35 = (float)GCMarsBlockFluid.getFlowDirection(var1, par2, par3, par4, var22);

                if (var35 > -999.0F)
                {
                    var34 = par1Block.getBlockTextureFromSideAndMetadata(2, var23);
                }

                var24 -= var32;
                var26 -= var32;
                var28 -= var32;
                var30 -= var32;
                int var36 = (var34 & 15) << 4;
                var37 = var34 & 240;
                double var38 = (var36 + 8.0D) / 256.0D;
                double var40 = (var37 + 8.0D) / 256.0D;

                if (var35 < -999.0F)
                {
                    var35 = 0.0F;
                }
                else
                {
                    var38 = (var36 + 16) / 256.0F;
                    var40 = (var37 + 16) / 256.0F;
                }

                double var42 = MathHelper.sin(var35) * 8.0F / 256.0D;
                double var44 = MathHelper.cos(var35) * 8.0F / 256.0D;
                var5.setBrightness(par1Block.getMixedBrightnessForBlock(var1, par2, par3, par4));
                float var46 = 1.0F;
                var5.setColorOpaque_F(var15 * var46 * var7, var15 * var46 * var8, var15 * var46 * var9);
                var5.addVertexWithUV(par2 + 0, par3 + var24, par4 + 0, var38 - var44 - var42, var40 - var44 + var42);
                var5.addVertexWithUV(par2 + 0, par3 + var26, par4 + 1, var38 - var44 + var42, var40 + var44 + var42);
                var5.addVertexWithUV(par2 + 1, par3 + var28, par4 + 1, var38 + var44 + var42, var40 + var44 - var42);
                var5.addVertexWithUV(par2 + 1, par3 + var30, par4 + 0, var38 + var44 - var42, var40 - var44 - var42);
            }

//            if (this.renderAllFaces || var11)
            {
                var5.setBrightness(par1Block.getMixedBrightnessForBlock(var1, par2, par3 - 1, par4));
                float var65 = 1.0F;
                var5.setColorOpaque_F(var14 * var65, var14 * var65, var14 * var65);
                renderBlocks.renderBottomFace(par1Block, par2, par3 + var32, par4, par1Block.getBlockTextureFromSide(0));
                var13 = true;
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

                int var66 = par1Block.getBlockTextureFromSideAndMetadata(var34 + 2, var23);
                int var39 = (var66 & 15) << 4;
                int var67 = var66 & 240;

//                if (this.renderAllFaces || var12[var34])
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

                    var13 = true;
                    double var53 = (var39 + 0) / 256.0F;
                    double var55 = (var39 + 16 - 0.01D) / 256.0D;
                    double var57 = (var67 + (1.0D - var41) * 16.0D) / 256.0D;
                    double var59 = (var67 + (1.0D - var43) * 16.0D) / 256.0D;
                    double var61 = (var67 + 16 - 0.01D) / 256.0D;
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

            renderBlocks.field_83027_i = var18;
            renderBlocks.field_83024_j = var20;
            return;
        }
    }
    
    public static float getFluidHeight(IBlockAccess var1, int par1, int par2, int par3, Material par4Material)
    {
        int var5 = 0;
        float var6 = 0.0F;

        for (int var7 = 0; var7 < 4; ++var7)
        {
            int var8 = par1 - (var7 & 1);
            int var10 = par3 - (var7 >> 1 & 1);

            if (var1.getBlockMaterial(var8, par2 + 1, var10) == par4Material)
            {
                return 1.0F;
            }

            Material var11 = var1.getBlockMaterial(var8, par2, var10);

            if (var11 == par4Material)
            {
                int var12 = var1.getBlockMetadata(var8, par2, var10);

                if (var12 >= 8 || var12 == 0)
                {
                    var6 += GCMarsBlockFluid.getFluidHeightPercent(var12) * 10.0F;
                    var5 += 10;
                }

                var6 += GCMarsBlockFluid.getFluidHeightPercent(var12);
                ++var5;
            }
            else if (!var11.isSolid())
            {
                ++var6;
                ++var5;
            }
        }

        return 1.0F - var6 / var5;
    }
}
