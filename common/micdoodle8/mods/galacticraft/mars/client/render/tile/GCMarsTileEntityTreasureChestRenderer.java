package micdoodle8.mods.galacticraft.mars.client.render.tile;

import micdoodle8.mods.galacticraft.core.client.model.block.GCCoreModelTreasureChest;
import micdoodle8.mods.galacticraft.core.client.model.block.GCCoreModelTreasureChestLarge;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityTreasureChest;
import micdoodle8.mods.galacticraft.mars.GalacticraftMars;
import micdoodle8.mods.galacticraft.mars.blocks.GCMarsBlockT2TreasureChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * GCMarsTileEntityTreasureChestRenderer.java
 * 
 * This file is part of the Galacticraft project
 * 
 * @author micdoodle8
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
@SideOnly(Side.CLIENT)
public class GCMarsTileEntityTreasureChestRenderer extends TileEntitySpecialRenderer
{
	private static final ResourceLocation treasureChestTexture = new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/model/treasure.png");
	private static final ResourceLocation treasureLargeChestTexture = new ResourceLocation(GalacticraftMars.TEXTURE_DOMAIN, "textures/model/treasurelarge.png");

	private final GCCoreModelTreasureChest chestModel = new GCCoreModelTreasureChest();
	private final GCCoreModelTreasureChestLarge largeChestModel = new GCCoreModelTreasureChestLarge();

	public void renderGCTileEntityTreasureChestAt(GCCoreTileEntityTreasureChest par1GCTileEntityTreasureChest, double par2, double par4, double par6, float par8)
	{
		int var9;

		if (!par1GCTileEntityTreasureChest.hasWorldObj())
		{
			var9 = 0;
		}
		else
		{
			final Block var10 = par1GCTileEntityTreasureChest.getBlockType();
			var9 = par1GCTileEntityTreasureChest.getBlockMetadata();

			if (var10 != null && var9 == 0)
			{
				((GCMarsBlockT2TreasureChest) var10).unifyAdjacentChests(par1GCTileEntityTreasureChest.getWorldObj(), par1GCTileEntityTreasureChest.xCoord, par1GCTileEntityTreasureChest.yCoord, par1GCTileEntityTreasureChest.zCoord);
				var9 = par1GCTileEntityTreasureChest.getBlockMetadata();
			}

			par1GCTileEntityTreasureChest.checkForAdjacentChests();
		}

		if (par1GCTileEntityTreasureChest.adjacentChestZNeg == null && par1GCTileEntityTreasureChest.adjacentChestXNeg == null)
		{
			GCCoreModelTreasureChest var14 = null;
			GCCoreModelTreasureChestLarge var14b = null;

			if (par1GCTileEntityTreasureChest.adjacentChestXPos == null && par1GCTileEntityTreasureChest.adjacentChestZPos == null)
			{
				var14 = this.chestModel;
				this.bindTexture(GCMarsTileEntityTreasureChestRenderer.treasureChestTexture);
			}
			else
			{
				var14b = this.largeChestModel;
				this.bindTexture(GCMarsTileEntityTreasureChestRenderer.treasureLargeChestTexture);
			}

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

			if (var9 == 2 && par1GCTileEntityTreasureChest.adjacentChestXPos != null)
			{
				GL11.glTranslatef(1.0F, 0.0F, 0.0F);
			}

			if (var9 == 5 && par1GCTileEntityTreasureChest.adjacentChestZPos != null)
			{
				GL11.glTranslatef(0.0F, 0.0F, -1.0F);
			}

			GL11.glRotatef(var11, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			float var12 = par1GCTileEntityTreasureChest.prevLidAngle + (par1GCTileEntityTreasureChest.lidAngle - par1GCTileEntityTreasureChest.prevLidAngle) * par8;

			float var13;

			if (par1GCTileEntityTreasureChest.adjacentChestZNeg != null)
			{
				var13 = par1GCTileEntityTreasureChest.adjacentChestZNeg.prevLidAngle + (par1GCTileEntityTreasureChest.adjacentChestZNeg.lidAngle - par1GCTileEntityTreasureChest.adjacentChestZNeg.prevLidAngle) * par8;

				if (var13 > var12)
				{
					var12 = var13;
				}
			}

			if (par1GCTileEntityTreasureChest.adjacentChestXNeg != null)
			{
				var13 = par1GCTileEntityTreasureChest.adjacentChestXNeg.prevLidAngle + (par1GCTileEntityTreasureChest.adjacentChestXNeg.lidAngle - par1GCTileEntityTreasureChest.adjacentChestXNeg.prevLidAngle) * par8;

				if (var13 > var12)
				{
					var12 = var13;
				}
			}

			var12 = 1.0F - var12;
			var12 = 1.0F - var12 * var12 * var12;

			if (var14 != null)
			{
				var14.chestLid.rotateAngleX = -(var12 * (float) Math.PI / 4.0F);
				var14.renderAll(!par1GCTileEntityTreasureChest.locked);
			}

			if (var14b != null)
			{
				var14b.chestLid.rotateAngleX = -(var12 * (float) Math.PI / 4.0F);
				var14b.renderAll(!par1GCTileEntityTreasureChest.locked);
			}

			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			GL11.glPopMatrix();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	@Override
	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
	{
		this.renderGCTileEntityTreasureChestAt((GCCoreTileEntityTreasureChest) par1TileEntity, par2, par4, par6, par8);
	}
}
