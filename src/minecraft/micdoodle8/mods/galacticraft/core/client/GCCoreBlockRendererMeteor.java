package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.core.GCCoreBlockUnlitTorch;
import net.minecraft.src.Block;
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
        renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);
    }
}
