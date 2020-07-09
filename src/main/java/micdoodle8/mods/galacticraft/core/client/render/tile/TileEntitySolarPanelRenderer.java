package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelSolarPanel;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySolar;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class TileEntitySolarPanelRenderer extends TileEntityRenderer<TileEntitySolar>
{
    private static final ResourceLocation solarPanelTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/solar_panel_basic.png");
    private static final ResourceLocation solarPanelAdvTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/solar_panel_advanced.png");
    public ModelSolarPanel model = new ModelSolarPanel();

    @Override
    public void render(TileEntitySolar panel, double x, double y, double z, float partialTicks, int destroyStage)
    {
        boolean doSkyRotation = false;
        if (panel.tierGC == 2)
        {
            this.bindTexture(TileEntitySolarPanelRenderer.solarPanelAdvTexture);
//            doSkyRotation = panel.getWorld().dimension instanceof DimensionSpaceStation; TODO Sky rotation
        }
        else
        {
            this.bindTexture(TileEntitySolarPanelRenderer.solarPanelTexture);
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) x, (float) y, (float) z);

        GL11.glTranslatef(0.5F, 1.0F, 0.5F);
        if (doSkyRotation)
        {
            GL11.glPushMatrix();
//            GL11.glRotatef(((DimensionSpaceStation)panel.getWorld().dimension).getSkyRotation(), 0.0F, 1.0F, 0.0F); TODO Sky rotation
            this.model.renderPole();
            GL11.glPopMatrix();
        }
        else
        {
            this.model.renderPole();
        }

        GL11.glTranslatef(0.0F, 1.5F, 0.0F);

        GL11.glRotatef(180.0F, 0, 0, 1);
        GL11.glRotatef(-90.0F, 0, 1, 0);

        float celestialAngle = (panel.getWorld().getCelestialAngle(1.0F) - 0.784690560F) * 360.0F;
        float celestialAngle2 = panel.getWorld().getCelestialAngle(1.0F) * 360.0F;

        if (doSkyRotation)
        {
//            GL11.glRotatef(((DimensionSpaceStation)panel.getWorld().dimension).getSkyRotation(), 0.0F, -1.0F, 0.0F); TODO Sky rotation
        }

        GL11.glRotatef(panel.currentAngle - (celestialAngle - celestialAngle2), 1.0F, 0.0F, 0.0F);

        this.model.renderPanel();

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
