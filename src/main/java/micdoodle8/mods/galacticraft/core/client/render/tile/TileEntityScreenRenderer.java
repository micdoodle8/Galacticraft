package micdoodle8.mods.galacticraft.core.client.render.tile;

import java.nio.FloatBuffer;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityScreen;
import net.minecraft.client.renderer.GLAllocation;
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
    public static final ResourceLocation blockTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/blocks/screenSide.png");
    public static final IModelCustom screenModel0 = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/screenWhole.obj"));
    public static final IModelCustom screenModel1 = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/screen3Quarters.obj"));
    public static final IModelCustom screenModel2 = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/screen2Quarters.obj"));
    public static final IModelCustom screenModel3 = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/screen1Quarters.obj"));
    public static final IModelCustom screenModel4 = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/screen0Quarters.obj"));
    private TextureManager renderEngine = FMLClientHandler.instance().getClient().renderEngine;
    private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);

    private float yPlane = 0.91F;
    float frame = 0.098F;
    
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
            GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            break;
        case 3:
            GL11.glRotatef(90, -1.0F, 0, 0);
            GL11.glTranslatef(1.0F, -1.0F, 1.0F);
            GL11.glRotatef(180, 0, -1.0F, 0);
            break;
        case 4:
            GL11.glRotatef(90, 0, 0, -1.0F);
            GL11.glTranslatef(-1.0F, 0.0F, 1.0F);
            GL11.glRotatef(90, 0, 1.0F, 0);
            break;
        case 5:
            GL11.glRotatef(90, 0, 0, 1.0F);
            GL11.glTranslatef(1.0F, -1.0F, 0.0F);
            GL11.glRotatef(90, 0, -1.0F, 0);
            break;
        default:
            break;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();

        int count = 0;
        if (tileEntity.connectedDown) count++;
        if (tileEntity.connectedUp) count++;
        if (tileEntity.connectedLeft) count++;
        if (tileEntity.connectedRight) count++;

        switch (count)
        {
        case 0:
        	GL11.glTranslatef(-0.001F, -0.001F, -0.001F);
        	GL11.glScalef(1.002F, 1.002F, 1.002F);
            TileEntityScreenRenderer.screenModel0.renderAll();
            break;
        case 1:
        	if (tileEntity.connectedUp)
        	{
                GL11.glRotatef(90, 0, -1.0F, 0);
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
        	}
        	else if (tileEntity.connectedRight)
        	{
                GL11.glRotatef(180, 0, -1.0F, 0);
                GL11.glTranslatef(-1.0F, 0.0F, -1.0F);
        	}
        	else if (tileEntity.connectedDown)
        	{
                GL11.glRotatef(270, 0, -1.0F, 0);
                GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
        	}
        	GL11.glTranslatef(-0.001F, -0.001F, -0.001F);
        	GL11.glScalef(1.002F, 1.002F, 1.002F);
            TileEntityScreenRenderer.screenModel1.renderAll();
            break;
        case 2:
        	if (!tileEntity.connectedRight && !tileEntity.connectedDown)
        	{
                GL11.glRotatef(90, 0, -1.0F, 0);
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
        	}
        	else if (!tileEntity.connectedDown && !tileEntity.connectedLeft)
        	{
                GL11.glRotatef(180, 0, -1.0F, 0);
                GL11.glTranslatef(-1.0F, 0.0F, -1.0F);
        	}
        	else if (!tileEntity.connectedUp && !tileEntity.connectedLeft)
        	{
                GL11.glRotatef(270, 0, -1.0F, 0);
                GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
        	}
        	GL11.glTranslatef(-0.001F, -0.001F, -0.001F);
        	GL11.glScalef(1.002F, 1.002F, 1.002F);
            TileEntityScreenRenderer.screenModel2.renderAll();
            break;
        case 3:
        	if (!tileEntity.connectedRight)
        	{
                GL11.glRotatef(90, 0, -1.0F, 0);
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
        	}
        	else if (!tileEntity.connectedDown)
        	{
                GL11.glRotatef(180, 0, -1.0F, 0);
                GL11.glTranslatef(-1.0F, 0.0F, -1.0F);
        	}
        	else if (!tileEntity.connectedLeft)
        	{
                GL11.glRotatef(270, 0, -1.0F, 0);
                GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
        	}
        	GL11.glTranslatef(-0.001F, -0.001F, -0.001F);
        	GL11.glScalef(1.002F, 1.002F, 1.002F);
            TileEntityScreenRenderer.screenModel3.renderAll();
            break;
        case 4:
        	GL11.glTranslatef(-0.001F, -0.001F, -0.001F);
        	GL11.glScalef(1.002F, 1.002F, 1.002F);
            TileEntityScreenRenderer.screenModel4.renderAll();
            break;
        }
        GL11.glPopMatrix();
        
        GL11.glTranslatef(-tileEntity.screenOffsetx, this.yPlane, -tileEntity.screenOffsetz);
        GL11.glRotatef(90, 1F, 0F, 0F);
        boolean cornerblock = false;
        if (tileEntity.connectionsLeft == 0 || tileEntity.connectionsRight == 0)
        	cornerblock = (tileEntity.connectionsUp == 0 || tileEntity.connectionsDown == 0);
        int totalLR = tileEntity.connectionsLeft + tileEntity.connectionsRight; 
        int totalUD = tileEntity.connectionsUp+ tileEntity.connectionsDown; 
        if (totalLR > 1 && totalUD > 1 && !cornerblock)
        {
    		//centre block
        	if (tileEntity.connectionsLeft == tileEntity.connectionsRight - (totalLR | 1))
        	{
    			if (tileEntity.connectionsUp == tileEntity.connectionsDown - (totalUD | 1))
        			cornerblock = true;
        	}
        }	
        tileEntity.screen.drawScreen(tileEntity.imageType, f + tileEntity.getWorldObj().getWorldTime(), cornerblock);

        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
    {
        this.renderModelAt((TileEntityScreen) tileEntity, var2, var4, var6, var8);
    }
   
}
