package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityBeamReflector;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;

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
		if (tileEntity.target == null)
		{
			return;
		}
		
		// Texture file
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityBeamReflectorRenderer.beamTexture);
		
		Tessellator tess = Tessellator.instance;
		
		GL11.glPushMatrix();

		TileEntity[] adjecentConnections = WorldUtil.getAdjacentPowerConnections(tileEntity);
		
		float distance = 0.15F;
//		Vector3 deviation = new Vector3(Math.sin(Math.toRadians(tileEntity.yaw)) * distance, 0, Math.cos(Math.toRadians(tileEntity.yaw)) * distance);
//		Vector3 deviationOpp = new Vector3(Math.sin(Math.toRadians(tileEntity.target.yaw + 180)) * distance, 0, Math.cos(Math.toRadians(tileEntity.target.yaw + 180)) * distance);
//
//		Vector3 headVec = new Vector3(tileEntity.xCoord + 0.5, tileEntity.yCoord + 1.13228 / 2.0F, tileEntity.zCoord + 0.5);
//		headVec.translate(deviation);
//		Vector3 targetVec = new Vector3(tileEntity.target.xCoord + 0.5, tileEntity.target.yCoord + 1.13228 / 2.0F, tileEntity.target.zCoord + 0.5);
//		targetVec.translate(deviation);
//		targetVec.translate(deviation);
//		targetVec.translate(deviationOpp.clone().invert());

		Vector3 direction = Vector3.subtract(tileEntity.target.getInputPoint(), tileEntity.getOutputPoint(true));
		float directionLength = (float) direction.getMagnitude();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
		
		GL11.glColor4f(0.0F, 0.4F, 1.0F, 1.0F);
		GL11.glTranslatef((float) d + 0.5F, (float) d1, (float) d2 + 0.5F);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 1.13228F / 2.0F, 0);
		GL11.glRotatef(tileEntity.yaw + 180, 0, 1, 0);
		GL11.glRotatef(-tileEntity.pitch, 1, 0, 0);
		GL11.glRotatef(((TileEntityBeamReflector) tileEntity).ticks * 10, 0, 0, 1);

		tess.startDrawing(GL11.GL_LINES);
		
		for (ForgeDirection dir : ForgeDirection.values())
		{
			Vector3 newDirection = direction.clone().invert();
			tess.addVertex(dir.offsetX / 40.0F, dir.offsetY / 40.0F, distance + dir.offsetZ / 40.0F);
			tess.addVertex(dir.offsetX / 40.0F, dir.offsetY / 40.0F, distance + directionLength + dir.offsetZ / 40.0F);
		}
		
		tess.draw();
		
		GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
		
		GL11.glColor4f(1, 1, 1, 1);
		
		direction.normalize();
		
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
