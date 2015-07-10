package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import micdoodle8.mods.galacticraft.core.client.objload.AdvancedModelLoader;
import micdoodle8.mods.galacticraft.core.client.objload.IModelCustom;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class TileEntityMinerBaseRenderer extends TileEntitySpecialRenderer
{
    public static final ResourceLocation telepadTexture = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/model/minerbase.png");
    public static IModelCustom telepadModel;

    public TileEntityMinerBaseRenderer()
    {
        TileEntityMinerBaseRenderer.telepadModel = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/minerbase.obj"));
    }

    public void renderModelAt(TileEntityMinerBase tileEntity, double d, double d1, double d2, float f)
    {
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    	if (!tileEntity.isMaster) return;
    	// Texture file
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityMinerBaseRenderer.telepadTexture);

        int i = tileEntity.getWorldObj().getLightBrightnessForSkyBlocks(tileEntity.xCoord, tileEntity.yCoord + 1, tileEntity.zCoord, 0);
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);

        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glTranslatef((float) d + 1F, (float) d1 + 1F, (float) d2 + 1F);
        GL11.glScalef(0.05F, 0.05F, 0.05F);

        switch (tileEntity.facing)
        {
        case 0:
            GL11.glRotatef(180F, 0, 1F, 0);
            break;
        case 1:
            break;
        case 2:
            GL11.glRotatef(270F, 0, 1F, 0);
            break;
        case 3:
            GL11.glRotatef(90F, 0, 1F, 0);
            break;
        }

        TileEntityMinerBaseRenderer.telepadModel.renderAll();

        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
    {
        this.renderModelAt((TileEntityMinerBase) tileEntity, var2, var4, var6, var8);
    }
}
