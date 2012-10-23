package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.core.GCCoreBlockUnlitTorch;
import net.minecraft.src.Block;
import net.minecraft.src.BlockEndPortalFrame;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
public class GCCoreBlockRendererMeteor implements ISimpleBlockRenderingHandler
{
    final int renderID;
    private GCCoreModelMeteor modelMeteor;

    public GCCoreBlockRendererMeteor(int var1)
    {
        this.renderID = var1;
    	modelMeteor = new GCCoreModelMeteor();
    }

	@Override
    public boolean renderWorldBlock(IBlockAccess var1, int var2, int var3, int var4, Block var5, int var6, RenderBlocks var7)
    {
    	this.renderBlockMeteor(var7, var5, var1, var2, var3, var4);
        return true;
    }

    public boolean shouldRender3DInInventory()
    {
        return false;
    }

    public int getRenderId()
    {
        return this.renderID;
    }

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) 
	{
	}
	
    public void renderBlockMeteor(RenderBlocks renderBlocks, Block par1Block, IBlockAccess var1, int par2, int par3, int par4)
    {
        int var5 = var1.getBlockMetadata(par2, par3, par4);
        int var6 = var5 & 3;

        if (var6 == 0)
        {
        	renderBlocks.uvRotateTop = 3;
        }
        else if (var6 == 3)
        {
        	renderBlocks.uvRotateTop = 1;
        }
        else if (var6 == 1)
        {
        	renderBlocks.uvRotateTop = 2;
        }
        
        par1Block.setBlockBounds(0.2F, 0.2F, 0.2F, 0.8F, 0.8F, 0.8F);
        renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);

        par1Block.setBlockBounds(0.52F, 0.85F, 0.4F, 0.68F, 0.88F, 0.6F);
        renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
        
        par1Block.setBlockBounds(0.25F, 0.8F, 0.25F, 0.75F, 0.85F, 0.75F);
        renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
        
        par1Block.setBlockBounds(0.25F, 0.15F, 0.25F, 0.75F, 0.2F, 0.75F);
        renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
        
        par1Block.setBlockBounds(0.15F, 0.3F, 0.25F, 0.2F, 0.7F, 0.75F);
        renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
        
        par1Block.setBlockBounds(0.8F, 0.3F, 0.25F, 0.85F, 0.7F, 0.75F);
        renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
        
        par1Block.setBlockBounds(0.25F, 0.3F, 0.15F, 0.75F, 0.7F, 0.2F);
        renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
        
        par1Block.setBlockBounds(0.25F, 0.3F, 0.8F, 0.75F, 0.7F, 0.85F);
        renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);

        par1Block.setBlockBounds(0.12F, 0.12F, 0.12F, 0.88F, 0.88F, 0.88F);
        
        renderBlocks.clearOverrideBlockTexture();
        par1Block.setBlockBoundsForItemRender();
        renderBlocks.uvRotateTop = 0;
    }
}
