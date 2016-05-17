//package micdoodle8.mods.galacticraft.core.client.render.tile;
//
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.client.objload.AdvancedModelLoader;
//import micdoodle8.mods.galacticraft.core.client.objload.IModelCustom;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityArclamp;
//import net.minecraft.client.renderer.OpenGlHelper;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.client.renderer.texture.TextureManager;
//import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.fml.client.FMLClientHandler;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//import org.lwjgl.opengl.GL11;
//
//@SideOnly(Side.CLIENT)
//public class TileEntityArclampRenderer extends TileEntitySpecialRenderer
//{
//    public static final ResourceLocation lampTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/misc/underoil.png");
//    public static final ResourceLocation lightTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/misc/light.png");
//    public static final IModelCustom lampMetal = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/arclampMetal.obj"));
//    public static final IModelCustom lampLight = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/arclampLight.obj"));
//    public static final IModelCustom lampBase = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/arclampBase.obj"));
//    private TextureManager renderEngine = FMLClientHandler.instance().getClient().renderEngine;
//
//    @Override
//    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float par8, int par9)
//    {
//        TileEntityArclamp arclamp = (TileEntityArclamp) tile;
//        int side = arclamp.getBlockMetadata();
//        int metaFacing = arclamp.facing;
//
//        //int facing;
//        /*switch (side)
//		{
//		case 0:
//		case 1:
//			facing = metaFacing + 2;
//			break;
//		case 2:
//			facing = metaFacing;
//			if (metaFacing > 1) facing= 7 - metaFacing;
//			break;
//		case 3:
//			facing = metaFacing;
//			if (metaFacing > 1) facing+=2;
//			break;
//		case 4:
//			facing = metaFacing;
//			break;
//		case 5:
//			facing = metaFacing;
//			if (metaFacing > 1) facing= 5 - metaFacing;
//			break;
//		default:
//			return;
//		}*/
//
//        GL11.glPushMatrix();
//        GL11.glTranslatef((float) par2 + 0.5F, (float) par4 + 0.5F, (float) par6 + 0.5F);
//
//        switch (side)
//        {
//        case 0:
//            break;
//        case 1:
//            GL11.glRotatef(180F, 1F, 0, 0);
//            if (metaFacing < 2)
//            {
//                metaFacing ^= 1;
//            }
//            break;
//        case 2:
//            GL11.glRotatef(90F, 1F, 0, 0);
//            metaFacing ^= 1;
//            break;
//        case 3:
//            GL11.glRotatef(90F, -1F, 0, 0);
//            break;
//        case 4:
//            GL11.glRotatef(90F, 0, 0, -1F);
//            metaFacing -= 2;
//            if (metaFacing < 0)
//            {
//                metaFacing = 1 - metaFacing;
//            }
//            break;
//        case 5:
//            GL11.glRotatef(90F, 0, 0, 1F);
//            metaFacing += 2;
//            if (metaFacing > 3)
//            {
//                metaFacing = 5 - metaFacing;
//            }
//            break;
//        }
//
//        GL11.glTranslatef(0, -0.175F, 0);
//        switch (metaFacing)
//        {
//        case 0:
//            break;
//        case 1:
//            GL11.glRotatef(180F, 0, 1F, 0);
//            break;
//        case 2:
//            GL11.glRotatef(90F, 0, 1F, 0);
//            break;
//        case 3:
//            GL11.glRotatef(270F, 0, 1F, 0);
//            break;
//        }
//
//        this.renderEngine.bindTexture(TileEntityArclampRenderer.lampTexture);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        TileEntityArclampRenderer.lampBase.renderAll();
//        GL11.glRotatef(45F, -1F, 0, 0);
//        GL11.glScalef(0.048F, 0.048F, 0.048F);
//        TileEntityArclampRenderer.lampMetal.renderAll();
//
//    	//Save the lighting state
//        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
//        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
//        GL11.glDisable(GL11.GL_LIGHTING);
//
//        this.renderEngine.bindTexture(TileEntityArclampRenderer.lightTexture);
//        Tessellator tessellator = Tessellator.getInstance();
//        //tessellator.getWorldRenderer().startDrawing(GL11.GL_QUADS);
//        //tessellator.getWorldRenderer().setColorRGBA(255, 255, 255, 255);
//        //((WavefrontObject) TileEntityArclampRenderer.lampLight).tessellateAll(tessellator.getWorldRenderer());
//        tessellator.draw();
//
//        //Restore the lighting state
//        GL11.glEnable(GL11.GL_LIGHTING);
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
//
//        GL11.glPopMatrix();
//    }
//}
