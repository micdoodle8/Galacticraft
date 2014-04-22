package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBeamReflector;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreRenderAluminumWire.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class TileEntityBeamReflectorRenderer extends TileEntitySpecialRenderer
{
	private static final ResourceLocation beamTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/misc/underoil.png");

	public final IModelCustom model;
	public final IModelCustom model2;

	public TileEntityBeamReflectorRenderer()
	{
		this.model = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "models/redirector.obj"));
		this.model2 = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "models/redirector.obj"));
	}

	public void renderModelAt(TileEntityBeamReflector tileEntity, double d, double d1, double d2, float f)
	{
		// Texture file
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityBeamReflectorRenderer.beamTexture);
		
		Tessellator tess = Tessellator.instance;
		
		GL11.glPushMatrix();
		
		GL11.glTranslatef((float) d + 0.5F, (float) d1, (float) d2 + 0.5F);		
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		
		model.renderPart("Base");
		GL11.glRotatef(tileEntity.yaw, 0, 1, 0);
		model.renderPart("Axle");
		float dX = 0.0F;
		float dY = 1.13228F;
		float dZ = 0.0F;
		GL11.glTranslatef(dX, dY, dZ);
		GL11.glRotatef(tileEntity.pitch, 1, 0, 0);
		GL11.glTranslatef(-dX, -dY, -dZ);
		model.renderPart("EnergyBlaster");
		GL11.glTranslatef(dX, dY, dZ);
		GL11.glRotatef(((TileEntityBeamReflector) tileEntity).ticks * 500, 0, 0, 1);
		GL11.glTranslatef(-dX, -dY, -dZ);
		model.renderPart("Ring");

		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
	{
		this.renderModelAt((TileEntityBeamReflector) tileEntity, var2, var4, var6, var8);
	}
}
