//package micdoodle8.mods.galacticraft.core.client.render.tile;
//
//import micdoodle8.mods.galacticraft.core.client.objload.AdvancedModelLoader;
//import micdoodle8.mods.galacticraft.core.client.objload.IModelCustom;
//import net.minecraft.block.state.IBlockState;
//import net.minecraftforge.fml.client.FMLClientHandler;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityAluminumWire;
//import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ResourceLocation;
//import org.lwjgl.opengl.GL11;
//
//@SideOnly(Side.CLIENT)
//public class TileEntityAluminumWireRenderer extends TileEntitySpecialRenderer
//{
//    private static final ResourceLocation aluminumWireTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/aluminumWire.png");
//
////    public final IModelCustom model;
////    public final IModelCustom model2;
//
//    public TileEntityAluminumWireRenderer()
//    {
////        this.model = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/aluminumWire.obj"));
////        this.model2 = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/aluminumWireHeavy.obj"));
//    }
//
//    @Override
//    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8, int par9)
//    {
//        TileEntityAluminumWire aluminumWire = (TileEntityAluminumWire) tile;
//        // Texture file
//        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityAluminumWireRenderer.aluminumWireTexture);
//        GL11.glPushMatrix();
//        GL11.glTranslatef((float) par2 + 0.5F, (float) par4 + 0.5F, (float) par6 + 0.5F);
//        GL11.glScalef(1.0F, -1F, -1F);
//
//        TileEntity[] adjecentConnections = EnergyUtil.getAdjacentPowerConnections(aluminumWire);
//
//        IBlockState blockState = aluminumWire.getWorld().getBlockState(aluminumWire.getPos());
//        int metadata = blockState.getBlock().getMetaFromState(blockState);
//
//        IModelCustom model;
//
//        if (metadata == 0)
//        {
//            model = this.model;
//        }
//        else
//        {
//            model = this.model2;
//        }
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
