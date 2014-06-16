package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockSolar;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelSolarPanel;
import micdoodle8.mods.galacticraft.core.tile.TileEntitySolar;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;



public class TileEntitySolarPanelRenderer extends TileEntitySpecialRenderer
{
	private static final ResourceLocation solarPanelTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/solarPanelBasic.png");
	private static final ResourceLocation solarPanelAdvTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/solarPanelAdvanced.png");
	public ModelSolarPanel model = new ModelSolarPanel();

	@Override
	public void renderTileEntityAt(TileEntity var1, double par2, double par4, double par6, float var8)
	{
		TileEntitySolar panel = (TileEntitySolar) var1;

		if (var1.getBlockMetadata() >= BlockSolar.ADVANCED_METADATA)
		{
			this.bindTexture(TileEntitySolarPanelRenderer.solarPanelAdvTexture);
		}
		else
		{
			this.bindTexture(TileEntitySolarPanelRenderer.solarPanelTexture);
		}

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef((float) par2, (float) par4, (float) par6);

		GL11.glTranslatef(0.5F, 1.0F, 0.5F);
		this.model.renderPole();
		GL11.glTranslatef(0.0F, 1.5F, 0.0F);

		GL11.glRotatef(180.0F, 0, 0, 1);
		GL11.glRotatef(-90.0F, 0, 1, 0);

		float celestialAngle = (panel.getWorldObj().getCelestialAngle(1.0F) - 0.784690560F) * 360.0F;
		float celestialAngle2 = panel.getWorldObj().getCelestialAngle(1.0F) * 360.0F;

		GL11.glRotatef(panel.currentAngle - (celestialAngle - celestialAngle2), 1.0F, 0.0F, 0.0F);

		this.model.renderPanel();

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
