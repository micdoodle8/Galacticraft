//package micdoodle8.mods.galacticraft.core.client.render.tile;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.client.objload.AdvancedModelLoader;
//import micdoodle8.mods.galacticraft.core.client.objload.IModelCustom;
//import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWire;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidPipe;
//import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.client.FMLClientHandler;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//import org.lwjgl.opengl.GL11;
//
//@SideOnly(Side.CLIENT)
//public class TileEntityOxygenPipeRenderer extends TileEntitySpecialRenderer
//{
//    private static final ResourceLocation pipeTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/blocks/pipe_oxygen_white.png");
//
//    public final IModelCustom model;
//
//    public TileEntityOxygenPipeRenderer()
//    {
//        this.model = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/oxygenPipe.obj"));
//    }
//
//    @Override
//    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8, int par9)
//    {
//        TileEntityFluidPipe oxygenPipe = (TileEntityFluidPipe) tile;
//        // Texture file
//        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityOxygenPipeRenderer.pipeTexture);
//        GL11.glPushMatrix();
//        GL11.glTranslatef((float) par2 + 0.5F, (float) par4 + 0.5F, (float) par6 + 0.5F);
//        GL11.glScalef(1.0F, -1F, -1F);
//
//        TileEntity[] adjecentConnections = OxygenUtil.getAdjacentOxygenConnections(oxygenPipe);
//
////        IBlockState blockState = aluminumWire.getWorld().getBlockState(aluminumWire.getPos());
////        int metadata = blockState.getBlock().getMetaFromState(blockState);
//
//        if (adjecentConnections[0] != null)
//        {
//            model.renderPart("Top");
//        }
//
//        if (adjecentConnections[1] != null)
//        {
//            model.renderPart("Bottom");
//        }
//
//        if (adjecentConnections[2] != null)
//        {
//            model.renderPart("Front");
//        }
//
//        if (adjecentConnections[3] != null)
//        {
//            model.renderPart("Back");
//        }
//
//        if (adjecentConnections[4] != null)
//        {
//            model.renderPart("Right");
//        }
//
//        if (adjecentConnections[5] != null)
//        {
//            model.renderPart("Left");
//        }
//
//        model.renderPart("Middle");
//        GL11.glPopMatrix();
//    }
//}
