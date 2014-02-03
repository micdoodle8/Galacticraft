package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAluminumWire;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
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
public class GCCoreRenderAluminumWire extends TileEntitySpecialRenderer
{
	private static final ResourceLocation aluminumWireTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/aluminumWire.png");

	public final IModelCustom model;
	public final IModelCustom model2;

	public GCCoreRenderAluminumWire()
	{
		this.model = AdvancedModelLoader.loadModel("/assets/galacticraftcore/models/aluminumWire.obj");
		this.model2 = AdvancedModelLoader.loadModel("/assets/galacticraftcore/models/aluminumWireHeavy.obj");
	}

	public void renderModelAt(GCCoreTileEntityAluminumWire tileEntity, double d, double d1, double d2, float f)
	{
		// Texture file
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(GCCoreRenderAluminumWire.aluminumWireTexture);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d + 0.5F, (float) d1 + 0.5F, (float) d2 + 0.5F);
		GL11.glScalef(1.0F, -1F, -1F);

		TileEntity[] adjecentConnections = WorldUtil.getAdjacentPowerConnections(tileEntity);

		// for (byte i = 0; i < 6; i++)
		// {
		// ForgeDirection side = ForgeDirection.getOrientation(i);
		// Vector3 tileVec = new Vector3(tileEntity);
		// TileEntity adjacentTile =
		// tileVec.modifyPositionFromSide(side).getTileEntity(tileEntity.worldObj);
		//
		// if (adjacentTile instanceof IConnector)
		// {
		// if (((IConnector) adjacentTile).canConnect(side.getOpposite()))
		// {
		// adjecentConnections.add(adjacentTile);
		// }
		// else
		// {
		// adjecentConnections.add(null);
		// }
		// }
		// else if (PowerConfigHandler.isIndustrialCraft2Loaded() &&
		// adjacentTile instanceof IEnergyTile)
		// {
		// if (adjacentTile instanceof IEnergyAcceptor)
		// {
		// if (((IEnergyAcceptor) adjacentTile).acceptsEnergyFrom(tileEntity,
		// side.getOpposite()))
		// {
		// adjecentConnections.add(adjacentTile);
		// }
		// else
		// {
		// if (adjacentTile instanceof IEnergySource && ((IEnergyEmitter)
		// adjacentTile).emitsEnergyTo(tileEntity, side.getOpposite()))
		// {
		// adjecentConnections.add(adjacentTile);
		// }
		// else
		// {
		// adjecentConnections.add(null);
		// }
		// }
		// }
		// else
		// {
		// adjecentConnections.add(adjacentTile);
		// }
		// }
		// else if (PowerConfigHandler.isBuildcraftLoaded() && adjacentTile
		// instanceof IPowerReceptor)
		// {
		// adjecentConnections.add(adjacentTile);
		// }
		// else
		// {
		// adjecentConnections.add(null);
		// }
		// }

		int metadata = tileEntity.worldObj.getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

		IModelCustom model = null;

		if (metadata == 0)
		{
			model = this.model;
		}
		else
		{
			model = this.model2;
		}

		if (adjecentConnections[0] != null)
		{
			model.renderPart("Top");
		}

		if (adjecentConnections[1] != null)
		{
			model.renderPart("Bottom");
		}

		if (adjecentConnections[2] != null)
		{
			model.renderPart("Front");
		}

		if (adjecentConnections[3] != null)
		{
			model.renderPart("Back");
		}

		if (adjecentConnections[4] != null)
		{
			model.renderPart("Right");
		}

		if (adjecentConnections[5] != null)
		{
			model.renderPart("Left");
		}

		model.renderPart("Middle");
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
	{
		this.renderModelAt((GCCoreTileEntityAluminumWire) tileEntity, var2, var4, var6, var8);
	}
}
