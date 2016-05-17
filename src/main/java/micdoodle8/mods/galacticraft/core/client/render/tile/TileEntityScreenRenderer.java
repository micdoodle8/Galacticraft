//package micdoodle8.mods.galacticraft.core.client.render.tile;
//
//import micdoodle8.mods.galacticraft.core.client.objload.AdvancedModelLoader;
//import micdoodle8.mods.galacticraft.core.client.objload.IModelCustom;
//import net.minecraftforge.fml.client.FMLClientHandler;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//import micdoodle8.mods.galacticraft.core.GalacticraftCore;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityScreen;
//import net.minecraft.client.renderer.GLAllocation;
//import net.minecraft.client.renderer.texture.TextureManager;
//import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ResourceLocation;
//import org.lwjgl.opengl.GL11;
//
//import java.nio.FloatBuffer;
//
//@SideOnly(Side.CLIENT)
//public class TileEntityScreenRenderer extends TileEntitySpecialRenderer
//{
//    public static final ResourceLocation blockTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/blocks/screenSide.png");
//    public static final IModelCustom screenModel0 = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/screenWhole.obj"));
//    public static final IModelCustom screenModel1 = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/screen3Quarters.obj"));
//    public static final IModelCustom screenModel2 = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/screen2Quarters.obj"));
//    public static final IModelCustom screenModel3 = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/screen1Quarters.obj"));
//    public static final IModelCustom screenModel4 = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/screen0Quarters.obj"));
//    private TextureManager renderEngine = FMLClientHandler.instance().getClient().renderEngine;
//    private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);
//
//    private float yPlane = 0.91F;
//    float frame = 0.098F;
//
//    @Override
//    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float partialTickTime, int par9)
//    {
//        TileEntityScreen screen = (TileEntityScreen) tile;
//        GL11.glPushMatrix();
//        // Texture file
//        this.renderEngine.bindTexture(TileEntityScreenRenderer.blockTexture);
//        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
//
//        int meta = screen.getBlockMetadata();
//        boolean screenData = (meta >= 8);
//        meta &= 7;
//
//        switch (meta)
//        {
//        case 0:
//            GL11.glRotatef(180, 1, 0, 0);
//            GL11.glTranslatef(0, -1.0F, -1.0F);
//            break;
//        case 1:
//            break;
//        case 2:
//            GL11.glRotatef(90, 1.0F, 0, 0);
//            GL11.glTranslatef(0.0F, 0.0F, -1.0F);
//            break;
//        case 3:
//            GL11.glRotatef(90, -1.0F, 0, 0);
//            GL11.glTranslatef(1.0F, -1.0F, 1.0F);
//            GL11.glRotatef(180, 0, -1.0F, 0);
//            break;
//        case 4:
//            GL11.glRotatef(90, 0, 0, -1.0F);
//            GL11.glTranslatef(-1.0F, 0.0F, 1.0F);
//            GL11.glRotatef(90, 0, 1.0F, 0);
//            break;
//        case 5:
//            GL11.glRotatef(90, 0, 0, 1.0F);
//            GL11.glTranslatef(1.0F, -1.0F, 0.0F);
//            GL11.glRotatef(90, 0, -1.0F, 0);
//            break;
//        default:
//            break;
//        }
//
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GL11.glPushMatrix();
//
//        int count = 0;
//        if (screen.connectedDown) count++;
//        if (screen.connectedUp) count++;
//        if (screen.connectedLeft) count++;
//        if (screen.connectedRight) count++;
//
//        switch (count)
//        {
//        case 0:
//        	GL11.glTranslatef(-0.001F, -0.001F, -0.001F);
//        	GL11.glScalef(1.002F, 1.002F, 1.002F);
//            TileEntityScreenRenderer.screenModel0.renderAll();
//            break;
//        case 1:
//        	if (screen.connectedUp)
//        	{
//                GL11.glRotatef(90, 0, -1.0F, 0);
//                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
//        	}
//        	else if (screen.connectedRight)
//        	{
//                GL11.glRotatef(180, 0, -1.0F, 0);
//                GL11.glTranslatef(-1.0F, 0.0F, -1.0F);
//        	}
//        	else if (screen.connectedDown)
//        	{
//                GL11.glRotatef(270, 0, -1.0F, 0);
//                GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
//        	}
//        	GL11.glTranslatef(-0.001F, -0.001F, -0.001F);
//        	GL11.glScalef(1.002F, 1.002F, 1.002F);
//            TileEntityScreenRenderer.screenModel1.renderAll();
//            break;
//        case 2:
//        	if (!screen.connectedRight && !screen.connectedDown)
//        	{
//                GL11.glRotatef(90, 0, -1.0F, 0);
//                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
//        	}
//        	else if (!screen.connectedDown && !screen.connectedLeft)
//        	{
//                GL11.glRotatef(180, 0, -1.0F, 0);
//                GL11.glTranslatef(-1.0F, 0.0F, -1.0F);
//        	}
//        	else if (!screen.connectedUp && !screen.connectedLeft)
//        	{
//                GL11.glRotatef(270, 0, -1.0F, 0);
//                GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
//        	}
//        	GL11.glTranslatef(-0.001F, -0.001F, -0.001F);
//        	GL11.glScalef(1.002F, 1.002F, 1.002F);
//            TileEntityScreenRenderer.screenModel2.renderAll();
//            break;
//        case 3:
//        	if (!screen.connectedRight)
//        	{
//                GL11.glRotatef(90, 0, -1.0F, 0);
//                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
//        	}
//        	else if (!screen.connectedDown)
//        	{
//                GL11.glRotatef(180, 0, -1.0F, 0);
//                GL11.glTranslatef(-1.0F, 0.0F, -1.0F);
//        	}
//        	else if (!screen.connectedLeft)
//        	{
//                GL11.glRotatef(270, 0, -1.0F, 0);
//                GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
//        	}
//        	GL11.glTranslatef(-0.001F, -0.001F, -0.001F);
//        	GL11.glScalef(1.002F, 1.002F, 1.002F);
//            TileEntityScreenRenderer.screenModel3.renderAll();
//            break;
//        case 4:
//        	GL11.glTranslatef(-0.001F, -0.001F, -0.001F);
//        	GL11.glScalef(1.002F, 1.002F, 1.002F);
//            TileEntityScreenRenderer.screenModel4.renderAll();
//            break;
//        }
//        GL11.glPopMatrix();
//
//        GL11.glTranslatef(-screen.screenOffsetx, this.yPlane, -screen.screenOffsetz);
//        GL11.glRotatef(90, 1F, 0F, 0F);
//        boolean cornerblock = false;
//        if (screen.connectionsLeft == 0 || screen.connectionsRight == 0)
//        	cornerblock = (screen.connectionsUp == 0 || screen.connectionsDown == 0);
//        int totalLR = screen.connectionsLeft + screen.connectionsRight;
//        int totalUD = screen.connectionsUp+ screen.connectionsDown;
//        if (totalLR > 1 && totalUD > 1 && !cornerblock)
//        {
//    		//centre block
//        	if (screen.connectionsLeft == screen.connectionsRight - (totalLR | 1))
//        	{
//    			if (screen.connectionsUp == screen.connectionsDown - (totalUD | 1))
//        			cornerblock = true;
//        	}
//        }
//        screen.screen.drawScreen(screen.imageType, partialTickTime + screen.getWorld().getWorldTime(), cornerblock);
//
//        GL11.glPopMatrix();
//    }
//}
