package micdoodle8.mods.galacticraft.planets.mars.client.render.block;

import micdoodle8.mods.galacticraft.planets.mars.blocks.MarsBlocks;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTreasureChestMars;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRendererTier2TreasureChest implements ISimpleBlockRenderingHandler
{
    final int renderID;

    public BlockRendererTier2TreasureChest(int var1)
    {
        this.renderID = var1;
    }

    @Override
    public void renderInventoryBlock(Block var1, int var2, int var3, RenderBlocks var4)
    {
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        this.renderChest(var1, var2, var3);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess var1, int var2, int var3, int var4, Block var5, int var6, RenderBlocks var7)
    {
        return false;
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

    private final TileEntityTreasureChestMars chest = new TileEntityTreasureChestMars();

    public void renderChest(Block par1Block, int par2, float par3)
    {
        if (par1Block == MarsBlocks.tier2TreasureChest)
        {
            TileEntityRendererDispatcher.instance.renderTileEntityAt(this.chest, 0.0D, 0.0D, 0.0D, 0.0F);
        }
    }
}
