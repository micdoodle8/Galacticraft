package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.block.GCCoreModelParachestTile;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityParachest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCCoreTileEntityParachestRenderer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCCoreTileEntityParachestRenderer extends TileEntitySpecialRenderer
{
	private static final ResourceLocation parachestTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/model/parachest.png");

	private final GCCoreModelParachestTile chestModel = new GCCoreModelParachestTile();

	public void renderGCTileEntityTreasureChestAt(GCCoreTileEntityParachest tile, double par2, double par4, double par6, float par8)
	{
		int var9;

		if (!tile.hasWorldObj())
		{
			var9 = 0;
		}
		else
		{
			final Block var10 = tile.getBlockType();
			var9 = tile.getBlockMetadata();

			if (var10 != null && var9 == 0)
			{
				var9 = tile.getBlockMetadata();
			}
		}

		this.bindTexture(GCCoreTileEntityParachestRenderer.parachestTexture);

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glTranslatef((float) par2, (float) par4 + 1.0F, (float) par6 + 1.0F);
		GL11.glScalef(1.0F, -1.0F, -1.0F);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		short var11 = 0;

		if (var9 == 2)
		{
			var11 = 180;
		}

		if (var9 == 3)
		{
			var11 = 0;
		}

		if (var9 == 4)
		{
			var11 = 90;
		}

		if (var9 == 5)
		{
			var11 = -90;
		}

		GL11.glRotatef(var11, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		float var12 = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * par8;

		var12 = 1.0F - var12;
		var12 = 1.0F - var12 * var12 * var12;

		this.chestModel.chestLid.rotateAngleX = -(var12 * (float) Math.PI / 4.0F);
		this.chestModel.renderAll(var12 == 0.0F);

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	@Override
	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
	{
		this.renderGCTileEntityTreasureChestAt((GCCoreTileEntityParachest) par1TileEntity, par2, par4, par6, par8);
	}
}
