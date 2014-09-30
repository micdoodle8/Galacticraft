package micdoodle8.mods.galacticraft.core.client.render.tile;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.proxy.ClientProxyCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityScreenRenderer extends TileEntitySpecialRenderer
{
    public static final ResourceLocation blockTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/misc/underoil.png");
    public static final IModelCustom screenModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/screenWhole.obj"));
    private TextureManager renderEngine = FMLClientHandler.instance().getClient().renderEngine;
    
    public void renderModelAt(TileEntityScreen tileEntity, double d, double d1, double d2, float f)
    {
        GL11.glPushMatrix();
        // Texture file
        this.renderEngine.bindTexture(TileEntityScreenRenderer.blockTexture);
        GL11.glTranslatef((float) d, (float) d1, (float) d2);

        int meta = tileEntity.getBlockMetadata();
        boolean screenData = (meta >= 8);
        meta &= 7;

        switch (meta)
        {
        case 0:
            GL11.glRotatef(180, 1, 0, 0);
            GL11.glTranslatef(0, -1.0F, -1.0F);
            break;
        case 1:
            break;
        case 2:
            GL11.glRotatef(90, 1.0F, 0, 0);
            GL11.glTranslatef(0, 0.0F, -1.0F);
            break;
        case 3:
            GL11.glRotatef(90, -1.0F, 0, 0);
            GL11.glTranslatef(0, -1.0F, 0);
            break;
        case 4:
            GL11.glRotatef(90, 0, 0, -1.0F);
            GL11.glTranslatef(-1.0F, 0.0F, 0);
            break;
        case 5:
            GL11.glRotatef(90, 0, 0, 1.0F);
            GL11.glTranslatef(0F, -1.0F, 0);
            break;
        default:
            break;
        }

        TileEntityScreenRenderer.screenModel.renderAll();
        
        this.drawScreen(tileEntity.imageType);

        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
    {
        this.renderModelAt((TileEntityScreen) tileEntity, var2, var4, var6, var8);
    }
    
    private void drawScreen(int type)
    {
        final Tessellator tess = Tessellator.instance;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        switch(type)
        {
        case 0:
	        if (ClientProxyCore.overworldTextureClient != null)
	        {
	            GL11.glBindTexture(GL11.GL_TEXTURE_2D, ClientProxyCore.overworldTextureClient.getGlTextureId());
	        }
	        else
	        {
	            this.renderEngine.bindTexture(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/earth.png"));
	        }
	        break;
        case 1:
        	 this.renderEngine.bindTexture(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/jupiter.png"));
        	 break;
        case 2:
       	 this.renderEngine.bindTexture(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/neptune.png"));
       	 break;
        case 3:
       	 this.renderEngine.bindTexture(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/gui/celestialbodies/saturn.png"));
       	 break;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        tess.setColorRGBA(255, 255, 255, 255);
        RenderHelper.enableStandardItemLighting();
        tess.startDrawingQuads();

        float zoomIn = 0F;
        float cornerB = 1.0F;
        float frame = 0.1F;
        tess.addVertexWithUV(frame, 0.94F, 1.0F - frame, zoomIn, cornerB);
        tess.addVertexWithUV(1.0F - frame, 0.94F, 1.0F - frame, cornerB, cornerB);
        tess.addVertexWithUV(1.0F - frame, 0.94F, frame, cornerB, zoomIn);
        tess.addVertexWithUV(frame, 0.94F, frame, zoomIn, zoomIn);
        tess.draw();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
}
