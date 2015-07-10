//package micdoodle8.mods.galacticraft.planets.mars.client.render.block;
//
//import net.minecraftforge.fml.client.registry.ISimpleBlockRenderingHandler;
//import net.minecraft.block.Block;
//import net.minecraft.client.renderer.EntityRenderer;
//import net.minecraft.client.renderer.RenderBlocks;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.world.IBlockAccess;
//
//public class BlockRendererCavernousVines implements ISimpleBlockRenderingHandler
//{
//    final int renderID;
//
//    public BlockRendererCavernousVines(int var1)
//    {
//        this.renderID = var1;
//    }
//
//    @Override
//    public boolean renderWorldBlock(IBlockAccess var1, int var2, int var3, int var4, Block var5, int var6, RenderBlocks var7)
//    {
//        this.renderBlockMeteor(var7, var5, var1, var2, var3, var4);
//        return true;
//    }
//
//    @Override
//    public boolean shouldRender3DInInventory(int modelId)
//    {
//        return false;
//    }
//
//    @Override
//    public int getRenderId()
//    {
//        return this.renderID;
//    }
//
//    @Override
//    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
//    {
//    }
//
//    public void renderBlockMeteor(RenderBlocks renderBlocks, Block par1Block, IBlockAccess var1, int par2, int par3, int par4)
//    {
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(var1, par2, par3, par4));
//        float f = 1.0F;
//        int l = par1Block.colorMultiplier(var1, par2, par3, par4);
//        float f1 = (l >> 16 & 255) / 255.0F;
//        float f2 = (l >> 8 & 255) / 255.0F;
//        float f3 = (l & 255) / 255.0F;
//
//        if (EntityRenderer.anaglyphEnable)
//        {
//            float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
//            float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
//            float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
//            f1 = f4;
//            f2 = f5;
//            f3 = f6;
//        }
//
//        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
//
//        renderBlocks.drawCrossedSquares(par1Block.getIcon(0, var1.getBlockMetadata(par2, par3, par4)), par2, par3, par4, 1.0F);
//    }
//}
