package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReflector;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityBeamReflectorRenderer extends TileEntitySpecialRenderer
{
	public static final ResourceLocation reflectorTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/misc/underoil.png");
	public static IModelCustom reflectorModel;

	public TileEntityBeamReflectorRenderer()
	{
		TileEntityBeamReflectorRenderer.reflectorModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "models/redirector.obj"));
	}

	public void renderModelAt(TileEntityBeamReflector tileEntity, double d, double d1, double d2, float f)
	{
		// Texture file
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityBeamReflectorRenderer.reflectorTexture);

		Tessellator tess = Tessellator.instance;

		GL11.glPushMatrix();

		GL11.glTranslatef((float) d + 0.5F, (float) d1, (float) d2 + 0.5F);
		GL11.glScalef(0.5F, 0.5F, 0.5F);

		TileEntityBeamReflectorRenderer.reflectorModel.renderPart("Base");
		GL11.glRotatef(tileEntity.yaw, 0, 1, 0);
		TileEntityBeamReflectorRenderer.reflectorModel.renderPart("Axle");
		float dX = 0.0F;
		float dY = 1.13228F;
		float dZ = 0.0F;
		GL11.glTranslatef(dX, dY, dZ);
		GL11.glRotatef(tileEntity.pitch, 1, 0, 0);
		GL11.glTranslatef(-dX, -dY, -dZ);
		TileEntityBeamReflectorRenderer.reflectorModel.renderPart("EnergyBlaster");
		GL11.glTranslatef(dX, dY, dZ);
		GL11.glRotatef(tileEntity.ticks * 500, 0, 0, 1);
		GL11.glTranslatef(-dX, -dY, -dZ);
		TileEntityBeamReflectorRenderer.reflectorModel.renderPart("Ring");

		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
	{
		this.renderModelAt((TileEntityBeamReflector) tileEntity, var2, var4, var6, var8);
	}
}
