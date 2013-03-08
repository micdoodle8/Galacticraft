package micdoodle8.mods.galacticraft.core.client.render.block;

import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityRefinery;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class GCCoreBlockRendererRefinery implements ISimpleBlockRenderingHandler
{
    final int renderID;

    public GCCoreBlockRendererRefinery(int var1)
    {
        this.renderID = var1;
    }

	public void renderNasaBench(RenderBlocks renderBlocks, IBlockAccess iblockaccess, Block par1Block, int par2, int par3, int par4)
	{
	}

    private final GCCoreTileEntityRefinery table = new GCCoreTileEntityRefinery();

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
	{
        GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.4F, -0.5F);
        GL11.glScalef(1.07F, 1.07F, 1.07F);
        TileEntityRenderer.instance.renderTileEntityAt(this.table, 0.0D, 0.0D, 0.0D, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
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
