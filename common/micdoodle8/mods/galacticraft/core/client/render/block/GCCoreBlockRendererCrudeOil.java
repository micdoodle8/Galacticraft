package micdoodle8.mods.galacticraft.core.client.render.block;

import micdoodle8.mods.galacticraft.core.blocks.BlockFluidClassic;
import micdoodle8.mods.galacticraft.core.blocks.BlockFluidRoot;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreBlockRendererCrudeOil implements ISimpleBlockRenderingHandler
{
    final int renderID;

    public GCCoreBlockRendererCrudeOil(int var1)
    {
        this.renderID = var1;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        if (!(block instanceof BlockFluidClassic))
        {
            return false;
        }

        Tessellator tessellator = Tessellator.instance;
        int l = block.colorMultiplier(world, x, y, z);
        float red = (l >> 16 & 255) / 255.0F;
        float green = (l >> 8 & 255) / 255.0F;
        float blue = (l & 255) / 255.0F;

        BlockFluidClassic theFluid = (BlockFluidClassic) block;
        int bMeta = world.getBlockMetadata(x, y, z);

        boolean renderTop = world.getBlockId(x, y - theFluid.densityDir, z) != theFluid.blockID;

        boolean renderBottom = block.shouldSideBeRendered(world, x, y + theFluid.densityDir, z, 0) && world.getBlockId(x, y + theFluid.densityDir, z) != theFluid.blockID;

        boolean[] renderSides = new boolean[] { block.shouldSideBeRendered(world, x, y, z - 1, 2), block.shouldSideBeRendered(world, x, y, z + 1, 3), block.shouldSideBeRendered(world, x - 1, y, z, 4), block.shouldSideBeRendered(world, x + 1, y, z, 5) };

        if (!renderTop && !renderBottom && !renderSides[0] && !renderSides[1] && !renderSides[2] && !renderSides[3])
        {
            return false;
        }
        else
        {
            boolean rendered = false;
            float f3 = 0.5F;
            float f4 = 1.0F;
            float f5 = 0.8F;
            float f6 = 0.6F;

            double d2, d3, d4, d5;

            float flow11 = this.getFluidHeightForRender(world, x, y, z, theFluid);

            if (flow11 != 1)
            {
                float flow00 = this.getFluidHeightForRender(world, x - 1, y, z - 1, theFluid);
                float flow01 = this.getFluidHeightForRender(world, x - 1, y, z, theFluid);
                float flow02 = this.getFluidHeightForRender(world, x - 1, y, z + 1, theFluid);
                float flow10 = this.getFluidHeightForRender(world, x, y, z - 1, theFluid);
                float flow12 = this.getFluidHeightForRender(world, x, y, z + 1, theFluid);
                float flow20 = this.getFluidHeightForRender(world, x + 1, y, z - 1, theFluid);
                float flow21 = this.getFluidHeightForRender(world, x + 1, y, z, theFluid);
                float flow22 = this.getFluidHeightForRender(world, x + 1, y, z + 1, theFluid);

                d2 = this.getFluidHeightAverage(new float[] { flow00, flow01, flow10, flow11 });
                d3 = this.getFluidHeightAverage(new float[] { flow01, flow02, flow11, flow12 });
                d4 = this.getFluidHeightAverage(new float[] { flow11, flow12, flow21, flow22 });
                d5 = this.getFluidHeightAverage(new float[] { flow10, flow11, flow20, flow21 });
            }
            else
            {
                d2 = flow11;
                d3 = flow11;
                d4 = flow11;
                d5 = flow11;
            }

            double d6 = 0.0010000000474974513D;
            float f7;
            float f8;

            boolean rises = theFluid.densityDir == 1;

            if (renderer.renderAllFaces || renderTop)
            {
                rendered = true;

                Icon icon = block.getIcon(1, bMeta);
                float flowDir = (float) BlockFluidRoot.getFlowDirection(world, x, y, z);

                if (flowDir > -999.0F)
                {
                    icon = block.getIcon(2, bMeta);
                }

                d2 -= d6;
                d3 -= d6;
                d4 -= d6;
                d5 -= d6;
                double d7;
                double d8;
                double d9;
                double d10;
                double d11;
                double d12;
                double d13;
                double d14;

                if (flowDir < -999.0F)
                {
                    d8 = icon.getInterpolatedU(0.0D);
                    d12 = icon.getInterpolatedV(0.0D);
                    d7 = d8;
                    d11 = icon.getInterpolatedV(16.0D);
                    d10 = icon.getInterpolatedU(16.0D);
                    d14 = d11;
                    d9 = d10;
                    d13 = d12;
                }
                else
                {
                    f8 = MathHelper.sin(flowDir) * 0.25F;
                    f7 = MathHelper.cos(flowDir) * 0.25F;
                    d8 = icon.getInterpolatedU(8.0F + (-f7 - f8) * 16.0F);
                    d12 = icon.getInterpolatedV(8.0F + (-f7 + f8) * 16.0F);
                    d7 = icon.getInterpolatedU(8.0F + (-f7 + f8) * 16.0F);
                    d11 = icon.getInterpolatedV(8.0F + (f7 + f8) * 16.0F);
                    d10 = icon.getInterpolatedU(8.0F + (f7 + f8) * 16.0F);
                    d14 = icon.getInterpolatedV(8.0F + (f7 - f8) * 16.0F);
                    d9 = icon.getInterpolatedU(8.0F + (f7 - f8) * 16.0F);
                    d13 = icon.getInterpolatedV(8.0F + (-f7 - f8) * 16.0F);
                }

                tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z));
                f8 = 1.0F;
                tessellator.setColorOpaque_F(f4 * f8 * red, f4 * f8 * green, f4 * f8 * blue);

                if (!rises)
                {
                    tessellator.addVertexWithUV(x + 0, y + d2, z + 0, d8, d12);
                    tessellator.addVertexWithUV(x + 0, y + d3, z + 1, d7, d11);
                    tessellator.addVertexWithUV(x + 1, y + d4, z + 1, d10, d14);
                    tessellator.addVertexWithUV(x + 1, y + d5, z + 0, d9, d13);
                }
                else
                {
                    tessellator.addVertexWithUV(x + 1, y + 1 - d5, z + 0, d9, d13);
                    tessellator.addVertexWithUV(x + 1, y + 1 - d4, z + 1, d10, d14);
                    tessellator.addVertexWithUV(x + 0, y + 1 - d3, z + 1, d7, d11);
                    tessellator.addVertexWithUV(x + 0, y + 1 - d2, z + 0, d8, d12);
                }
            }

            if (renderer.renderAllFaces || renderBottom)
            {
                rendered = true;

                tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y - 1, z));
                float f10 = 1.0F;

                if (!rises)
                {
                    tessellator.setColorOpaque_F(f3 * f10, f3 * f10, f3 * f10);
                    renderer.renderFaceYNeg(block, x, y + d6, z, block.getIcon(0, bMeta));
                }
                else
                {
                    tessellator.setColorOpaque_F(f4 * f10, f4 * f10, f4 * f10);
                    renderer.renderFaceYPos(block, x, y + d6, z, block.getIcon(1, bMeta));
                }
            }

            for (int side = 0; side < 4; ++side)
            {
                int x2 = x;
                int z2 = z;

                switch (side)
                {
                case 0:
                    --z2;
                    break;
                case 1:
                    ++z2;
                    break;
                case 2:
                    --x2;
                    break;
                case 3:
                    ++x2;
                    break;
                }

                Icon icon1 = block.getIcon(side + 2, bMeta);

                if (renderer.renderAllFaces || renderSides[side])
                {
                    rendered = true;

                    double d15;
                    double d16;
                    double d17;
                    double d18;
                    double d19;
                    double d20;

                    if (side == 0)
                    {
                        d15 = d2;
                        d17 = d5;
                        d16 = x;
                        d18 = x + 1;
                        d19 = z + d6;
                        d20 = z + d6;
                    }
                    else if (side == 1)
                    {
                        d15 = d4;
                        d17 = d3;
                        d16 = x + 1;
                        d18 = x;
                        d19 = z + 1 - d6;
                        d20 = z + 1 - d6;
                    }
                    else if (side == 2)
                    {
                        d15 = d3;
                        d17 = d2;
                        d16 = x + d6;
                        d18 = x + d6;
                        d19 = z + 1;
                        d20 = z;
                    }
                    else
                    {
                        d15 = d5;
                        d17 = d4;
                        d16 = x + 1 - d6;
                        d18 = x + 1 - d6;
                        d19 = z;
                        d20 = z + 1;
                    }

                    float f11 = icon1.getInterpolatedU(0.0D);
                    f8 = icon1.getInterpolatedU(8.0D);
                    f7 = icon1.getInterpolatedV((1.0D - d15) * 16.0D * 0.5D);
                    float f12 = icon1.getInterpolatedV((1.0D - d17) * 16.0D * 0.5D);
                    float f13 = icon1.getInterpolatedV(8.0D);
                    tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x2, y, z2));
                    float f14 = 1.0F;

                    if (side < 2)
                    {
                        f14 *= f5;
                    }
                    else
                    {
                        f14 *= f6;
                    }

                    tessellator.setColorOpaque_F(f4 * f14 * red, f4 * f14 * green, f4 * f14 * blue);

                    if (!rises)
                    {
                        tessellator.addVertexWithUV(d16, y + d15, d19, f11, f7);
                        tessellator.addVertexWithUV(d18, y + d17, d20, f8, f12);
                        tessellator.addVertexWithUV(d18, y + 0, d20, f8, f13);
                        tessellator.addVertexWithUV(d16, y + 0, d19, f11, f13);
                    }
                    else
                    {
                        tessellator.addVertexWithUV(d16, y + 1 - 0, d19, f11, f13);
                        tessellator.addVertexWithUV(d18, y + 1 - 0, d20, f8, f13);
                        tessellator.addVertexWithUV(d18, y + 1 - d17, d20, f8, f12);
                        tessellator.addVertexWithUV(d16, y + 1 - d15, d19, f11, f7);
                    }
                }
            }

            renderer.renderMinY = 0;
            renderer.renderMaxY = 1;
            return rendered;
        }
    }

    public float getFluidHeightForRender(IBlockAccess world, int x, int y, int z, BlockFluidClassic block)
    {
        if (world.getBlockId(x, y, z) == block.blockID)
        {

            if (world.getBlockId(x, y - block.densityDir, z) == block.blockID)
            {
                return 1;
            }
            if (world.getBlockMetadata(x, y, z) == block.quantaPerBlock - 1)
            {
                return 0.875F;
            }
        }

        return !world.getBlockMaterial(x, y, z).isSolid() && world.getBlockId(x, y - block.densityDir, z) == block.blockID ? 1 : block.getQuantaPercentage(world, x, y, z) * 0.875F;
    }

    public float getFluidHeightAverage(float[] flow)
    {

        float total = 0;
        int count = 0;

        for (float element : flow)
        {
            if (element >= 0.875F)
            {
                return element;
            }

            if (element >= 0)
            {
                total += element;
                count++;
            }
        }

        return total / count;
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
