package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityShortRangeTelepadRenderer extends TileEntitySpecialRenderer
{
    public static final ResourceLocation telepadTexture = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/model/teleporter.png");
    public static final ResourceLocation telepadTexture0 = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/model/teleporter0.png");
    public static IModelCustom telepadModel;

    public TileEntityShortRangeTelepadRenderer()
    {
        TileEntityShortRangeTelepadRenderer.telepadModel = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/teleporter.obj"));
    }

    public void renderModelAt(TileEntityShortRangeTelepad tileEntity, double d, double d1, double d2, float f)
    {
        // Texture file
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityShortRangeTelepadRenderer.telepadTexture);

        GL11.glPushMatrix();

        GL11.glTranslatef((float) d + 0.5F, (float) d1, (float) d2 + 0.5F);
        GL11.glScalef(1F, 0.65F, 1F);
        TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("Base");
        GL11.glTranslatef(0.0F, (float) Math.sin(tileEntity.ticks / 10.0F) / 15.0F - 0.25F, 0.0F);
        TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("Top");

        GL11.glPopMatrix();

        GL11.glPushMatrix();

        GL11.glTranslatef((float) d + 0.5F, (float) d1 - 0.18F, (float) d2 + 0.5F);
        GL11.glScalef(1F, 0.65F, 1F);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityShortRangeTelepadRenderer.telepadTexture0);
        TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopMidxNegz");
        TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopPosxNegz");
        TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopNegxNegz");

        TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopMidxMidz");
        TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopPosxMidz");
        TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopNegxMidz");

        TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopMidxPosz");
        TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopPosxPosz");
        TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("TopNegxPosz");

        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
    {
        this.renderModelAt((TileEntityShortRangeTelepad) tileEntity, var2, var4, var6, var8);
    }
}
