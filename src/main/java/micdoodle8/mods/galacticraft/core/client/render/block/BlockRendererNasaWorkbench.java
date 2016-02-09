package micdoodle8.mods.galacticraft.core.client.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import micdoodle8.mods.galacticraft.core.tile.TileEntityNasaWorkbench;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class BlockRendererNasaWorkbench implements ISimpleBlockRenderingHandler
{
    final int renderID;

    public BlockRendererNasaWorkbench(int var1)
    {
        this.renderID = var1;
    }

    public void renderNasaBench(RenderBlocks renderBlocks, IBlockAccess iblockaccess, Block par1Block, int par2, int par3, int par4)
    {
        renderBlocks.setRenderBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.92F, 1.0F);
        renderBlocks.renderStandardBlock(par1Block, par2, par3, par4);

        renderBlocks.clearOverrideBlockTexture();
    }

    private final TileEntityNasaWorkbench table = new TileEntityNasaWorkbench();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -1.1F, -0.1F);
        GL11.glScalef(0.7F, 0.6F, 0.7F);
        TileEntityRendererDispatcher.instance.renderTileEntityAt(this.table, 0.0D, 0.0D, 0.0D, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        this.renderNasaBench(renderer, world, block, x, y, z);
        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return this.renderID;
    }
}
