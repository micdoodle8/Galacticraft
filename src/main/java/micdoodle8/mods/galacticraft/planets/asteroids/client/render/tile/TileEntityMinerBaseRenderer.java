package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

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
    	if (!tileEntity.isMaster) return;
    	// Texture file
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityMinerBaseRenderer.telepadTexture);

        GL11.glPushMatrix();

        GL11.glTranslatef((float) d + 1F, (float) d1 + 1F, (float) d2 + 1F);
        GL11.glScalef(0.1F, 0.1F, 0.1F);

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
        GL11.glRotatef(90, -1, 0, 0);
        GL11.glTranslatef(0F, -10F, 0F);

        TileEntityMinerBaseRenderer.telepadModel.renderAll();

        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
    {
        this.renderModelAt((TileEntityMinerBase) tileEntity, var2, var4, var6, var8);
    }
}
