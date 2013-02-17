package micdoodle8.mods.galacticraft.core.client.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class GCCoreBlockRendererCraftingTable implements ISimpleBlockRenderingHandler
{
    final int renderID;

    public GCCoreBlockRendererCraftingTable(int var1)
    {
        this.renderID = var1;
    }
    
	public void renderNasaBench(RenderBlocks renderBlocks, IBlockAccess iblockaccess, Block par1Block, int par2, int par3, int par4)
	{
    	renderBlocks.overrideBlockTexture = par1Block.blockIndexInTexture;
    	
        renderBlocks.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
        
        renderBlocks.setRenderBounds(0.1F, 1.0F, 0.0F, 0.9F, 1.1F, 0.1F);
        renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
        
        renderBlocks.setRenderBounds(0.9F, 1.0F, 0.0F, 1.0F, 1.1F, 1.0F);
        renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
        
        renderBlocks.setRenderBounds(0.0F, 1.0F, 0.0F, 0.1F, 1.1F, 1.0F);
        renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
        
        renderBlocks.setRenderBounds(0.1F, 1.0F, 0.9F, 0.9F, 1.1F, 1.0F);
        renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);

    	renderBlocks.overrideBlockTexture = 16;
    	
        renderBlocks.setRenderBounds(0.3F, 1.0F, 0.3F, 0.7F, 1.05F, 0.7F);
        renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
        
        final int var5 = renderBlocks.blockAccess.getBlockMetadata(par2, par3, par4);
        final int var6 = 0;
        final boolean var7 = (var5 & 8) > 0;
        final Tessellator var8 = Tessellator.instance;
        final boolean var9 = renderBlocks.overrideBlockTexture >= 0;

//        if (!var9)
        {
        }

//        if (!var9)
        {
        	renderBlocks.overrideBlockTexture = -1;
        }

        var8.setBrightness(par1Block.getMixedBrightnessForBlock(renderBlocks.blockAccess, par2, par3, par4));
        float var13 = 1.0F;

        if (Block.lightValue[par1Block.blockID] > 0)
        {
            var13 = 1.0F;
        }

        var8.setColorOpaque_F(var13, var13, var13);
        int var14 = par1Block.getBlockTextureFromSide(0);

        if (renderBlocks.overrideBlockTexture >= 0)
        {
            var14 = renderBlocks.overrideBlockTexture;
        }
        
        int var15;
        int var16;
        float var17;
        float var18;
        float var19;
        float var20;
        Vec3[] var21;
        float var22;
        float var23;
        float var24;

        Vec3 var30 = null;
        Vec3 var26 = null;
        Vec3 var27 = null;
        Vec3 var28 = null;
        
        // Pole arm up
        
        var15 = (var14 & 15) << 4;
        var16 = var14 & 240;
        var17 = var15 / 256.0F;
        var18 = (var15 + 15.99F) / 256.0F;
        var19 = var16 / 256.0F;
        var20 = (var16 + 15.99F) / 256.0F;
        var21 = new Vec3[8];
        var22 = 0.0225F;
        var23 = 0.0225F;
        var24 = 0.625F;
        var21[0] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-var22, 0.0D, -var23);
        var21[1] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(var22, 0.0D, -var23);
        var21[2] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(var22, 0.0D, var23);
        var21[3] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-var22, 0.0D, var23);
        var21[4] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-var22, var24, -var23);
        var21[5] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(var22, var24, -var23);
        var21[6] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(var22, var24, var23);
        var21[7] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-var22, var24, var23);

        for (int var25 = 0; var25 < 8; ++var25)
        {
            var21[var25].zCoord -= 0.0625D;

            var21[var25].rotateAroundY((float)Math.PI / 4F);
            
            var21[var25].xCoord += par2 + 0.9D;
            var21[var25].yCoord += (par3 + 0.8F);
            var21[var25].zCoord += par4 + 0.2D;
        }

        for (int var29 = 0; var29 < 6; ++var29)
        {
            if (var29 == 0)
            {
                var17 = (var15 + 7) / 256.0F;
                var18 = ((var15 + 9) - 0.01F) / 256.0F;
                var19 = (var16 + 6) / 256.0F;
                var20 = ((var16 + 8) - 0.01F) / 256.0F;
            }
            else if (var29 == 2)
            {
                var17 = (var15 + 7) / 256.0F;
                var18 = ((var15 + 9) - 0.01F) / 256.0F;
                var19 = (var16 + 6) / 256.0F;
                var20 = ((var16 + 16) - 0.01F) / 256.0F;
            }

            if (var29 == 0)
            {
                var30 = var21[0];
                var26 = var21[1];
                var27 = var21[2];
                var28 = var21[3];
            }
            else if (var29 == 1)
            {
                var30 = var21[7];
                var26 = var21[6];
                var27 = var21[5];
                var28 = var21[4];
            }
            else if (var29 == 2)
            {
                var30 = var21[1];
                var26 = var21[0];
                var27 = var21[4];
                var28 = var21[5];
            }
            else if (var29 == 3)
            {
                var30 = var21[2];
                var26 = var21[1];
                var27 = var21[5];
                var28 = var21[6];
            }
            else if (var29 == 4)
            {
                var30 = var21[3];
                var26 = var21[2];
                var27 = var21[6];
                var28 = var21[7];
            }
            else if (var29 == 5)
            {
                var30 = var21[0];
                var26 = var21[3];
                var27 = var21[7];
                var28 = var21[4];
            }

            var8.addVertexWithUV(var30.xCoord, var30.yCoord, var30.zCoord, var17, var20);
            var8.addVertexWithUV(var26.xCoord, var26.yCoord, var26.zCoord, var18, var20);
            var8.addVertexWithUV(var27.xCoord, var27.yCoord, var27.zCoord, var18, var19);
            var8.addVertexWithUV(var28.xCoord, var28.yCoord, var28.zCoord, var17, var19);
        }
        
        // Pole Arm Across

        var15 = (var14 & 15) << 4;
        var16 = var14 & 240;
        var17 = var15 / 256.0F;
        var18 = (var15 + 15.99F) / 256.0F;
        var19 = var16 / 256.0F;
        var20 = (var16 + 15.99F) / 256.0F;
        var21 = new Vec3[8];
        var22 = 0.0225F;
        var23 = 0.0225F;
        var24 = 0.625F;
        var21[0] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-var22, 0.0D, -var23);
        var21[1] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(var22, 0.0D, -var23);
        var21[2] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(var22, 0.0D, var23);
        var21[3] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-var22, 0.0D, var23);
        var21[4] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-var22, var24, -var23);
        var21[5] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(var22, var24, -var23);
        var21[6] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(var22, var24, var23);
        var21[7] = renderBlocks.blockAccess.getWorldVec3Pool().getVecFromPool(-var22, var24, var23);

        for (int var25 = 0; var25 < 8; ++var25)
        {
            var21[var25].zCoord -= 0.0625D;
            var21[var25].rotateAroundX((float)Math.PI * 5 / 4F);

            var21[var25].rotateAroundZ((float)Math.PI / 2F);

            var21[var25].rotateAroundY(MathHelper.cos(FMLClientHandler.instance().getClient().thePlayer.ticksExisted) / 10.0F);
            
            var21[var25].xCoord += par2 + 0.8D;
            var21[var25].yCoord += (par3 + 1.375F);
            var21[var25].zCoord += par4 + 0.12D;
        }

        var30 = null;
        var26 = null;
        var27 = null;
        var28 = null;

        for (int var29 = 0; var29 < 6; ++var29)
        {
            if (var29 == 0)
            {
                var17 = (var15 + 7) / 256.0F;
                var18 = ((var15 + 9) - 0.01F) / 256.0F;
                var19 = (var16 + 6) / 256.0F;
                var20 = ((var16 + 8) - 0.01F) / 256.0F;
            }
            else if (var29 == 2)
            {
                var17 = (var15 + 7) / 256.0F;
                var18 = ((var15 + 9) - 0.01F) / 256.0F;
                var19 = (var16 + 6) / 256.0F;
                var20 = ((var16 + 16) - 0.01F) / 256.0F;
            }

            if (var29 == 0)
            {
                var30 = var21[0];
                var26 = var21[1];
                var27 = var21[2];
                var28 = var21[3];
            }
            else if (var29 == 1)
            {
                var30 = var21[7];
                var26 = var21[6];
                var27 = var21[5];
                var28 = var21[4];
            }
            else if (var29 == 2)
            {
                var30 = var21[1];
                var26 = var21[0];
                var27 = var21[4];
                var28 = var21[5];
            }
            else if (var29 == 3)
            {
                var30 = var21[2];
                var26 = var21[1];
                var27 = var21[5];
                var28 = var21[6];
            }
            else if (var29 == 4)
            {
                var30 = var21[3];
                var26 = var21[2];
                var27 = var21[6];
                var28 = var21[7];
            }
            else if (var29 == 5)
            {
                var30 = var21[0];
                var26 = var21[3];
                var27 = var21[7];
                var28 = var21[4];
            }

            var8.addVertexWithUV(var30.xCoord, var30.yCoord, var30.zCoord, var17, var20);
            var8.addVertexWithUV(var26.xCoord, var26.yCoord, var26.zCoord, var18, var20);
            var8.addVertexWithUV(var27.xCoord, var27.yCoord, var27.zCoord, var18, var19);
            var8.addVertexWithUV(var28.xCoord, var28.yCoord, var28.zCoord, var17, var19);
        }
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
		final float minSize = 0.0F;
		final float maxSize = 1.0F;
		
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
		this.renderNasaBench(renderer, world, block, x, y, z);
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
