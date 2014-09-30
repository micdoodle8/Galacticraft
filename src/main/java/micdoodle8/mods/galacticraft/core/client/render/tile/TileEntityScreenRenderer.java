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
    public static final ResourceLocation blockTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/misc/underoil.png");
    public static final IModelCustom screenModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/screenWhole.obj"));
    private TextureManager renderEngine = FMLClientHandler.instance().getClient().renderEngine;
    private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);

    private float yPlane = 0.94F;
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

        TileEntityScreenRenderer.screenModel.renderAll();
        
        tileEntity.screen.drawScreen(tileEntity.imageType, f + tileEntity.getWorldObj().getWorldTime());

        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
    {
        this.renderModelAt((TileEntityScreen) tileEntity, var2, var4, var6, var8);
    }
   
}
