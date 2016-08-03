//package micdoodle8.mods.galacticraft.core.client.render.tile;
//
//import micdoodle8.mods.galacticraft.core.client.objload.AdvancedModelLoader;
//import micdoodle8.mods.galacticraft.core.client.objload.IModelCustom;
//import net.minecraftforge.fml.client.FMLClientHandler;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityThruster;
//import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ResourceLocation;
//import org.lwjgl.opengl.GL11;
//
//@SideOnly(Side.CLIENT)
//public class TileEntityThrusterRenderer extends TileEntitySpecialRenderer
//{
//    public static final ResourceLocation thrusterTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/thruster.png");
//    public static final IModelCustom thrusterModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/thruster.obj"));
//
//    @Override
//    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float partialTickTime, int par9)
//    {
//        GL11.glPushMatrix();
//
//        // Texture file
//        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityThrusterRenderer.thrusterTexture);
//
//        GL11.glTranslatef((float) par2 + 0.5F, (float) par4 + 0.5F, (float) par6 + 0.5F);
//
//        int meta = tile.getBlockMetadata();
//        boolean reverseThruster = (meta >= 8);
//        meta &= 7;
//
//        if (meta >= 1)
//        {
//            switch (meta)
//            {
//            case 1:
//                GL11.glTranslatef(-0.475F, 0.0F, 0.0F);
//                GL11.glRotatef(0, 0, 1, 0);
//                GL11.glScalef(0.55F, 0.55F, 0.55F);
//                break;
//            case 2:
//                GL11.glTranslatef(0.475F, 0.0F, 0.0F);
//                GL11.glRotatef(180, 0, 1, 0);
//                GL11.glScalef(0.55F, 0.55F, 0.55F);
//                break;
//            case 3:
//                GL11.glTranslatef(0.0F, 0.0F, -0.475F);
//                GL11.glRotatef(270, 0, 1, 0);
//                GL11.glScalef(0.55F, 0.55F, 0.55F);
//                break;
//            case 4:
//                GL11.glTranslatef(0.0F, 0.0F, 0.475F);
//                GL11.glRotatef(90, 0, 1, 0);
//                GL11.glScalef(0.55F, 0.55F, 0.55F);
//                break;
//            default:
//                break;
//            }
//
//            if (!reverseThruster)
//            {
//                GL11.glRotatef(180, 1, 0, 0);
//            }
//
//            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//            TileEntityThrusterRenderer.thrusterModel.renderAll();
//        }
//
//        GL11.glPopMatrix();
//    }
//}
