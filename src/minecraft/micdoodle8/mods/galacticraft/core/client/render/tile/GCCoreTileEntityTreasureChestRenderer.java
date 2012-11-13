package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.blocks.GCCoreBlockTreasureChest;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityTreasureChest;
import net.minecraft.src.Block;
import net.minecraft.src.ModelChest;
import net.minecraft.src.ModelLargeChest;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 * Copyright 2012, micdoodle8
 * 
 *  All rights reserved.
 *
 */
@SideOnly(Side.CLIENT)
public class GCCoreTileEntityTreasureChestRenderer extends TileEntitySpecialRenderer
{
    /** The normal small chest model. */
    private ModelChest chestModel = new ModelChest();

    /** The large double chest model. */
    private ModelChest largeChestModel = new ModelLargeChest();

    /**
     * Renders the TileEntity for the chest at a position.
     */
    public void renderGCTileEntityTreasureChestAt(GCCoreTileEntityTreasureChest par1GCTileEntityTreasureChest, double par2, double par4, double par6, float par8)
    {
        int var9;

        if (!par1GCTileEntityTreasureChest.func_70309_m())
        {
            var9 = 0;
        }
        else
        {
            Block var10 = par1GCTileEntityTreasureChest.getBlockType();
            var9 = par1GCTileEntityTreasureChest.getBlockMetadata();

            if (var10 != null && var9 == 0)
            {
                ((GCCoreBlockTreasureChest)var10).unifyAdjacentChests(par1GCTileEntityTreasureChest.getWorldObj(), par1GCTileEntityTreasureChest.xCoord, par1GCTileEntityTreasureChest.yCoord, par1GCTileEntityTreasureChest.zCoord);
                var9 = par1GCTileEntityTreasureChest.getBlockMetadata();
            }

            par1GCTileEntityTreasureChest.checkForAdjacentChests();
        }

        if (par1GCTileEntityTreasureChest.adjacentChestZNeg == null && par1GCTileEntityTreasureChest.adjacentChestXNeg == null)
        {
            ModelChest var14;

            if (par1GCTileEntityTreasureChest.adjacentChestXPos == null && par1GCTileEntityTreasureChest.adjacentChestZPosition == null)
            {
                var14 = this.chestModel;
                this.bindTextureByName("/micdoodle8/mods/galacticraft/core/client/entities/chest.png");
            }
            else
            {
                var14 = this.largeChestModel;
                this.bindTextureByName("/micdoodle8/mods/galacticraft/core/client/entities/largechest.png");
            }

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float)par2, (float)par4 + 1.0F, (float)par6 + 1.0F);
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

            if (var9 == 5 && par1GCTileEntityTreasureChest.adjacentChestZPosition != null)
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
            var14.chestLid.rotateAngleX = -(var12 * (float)Math.PI / 4.0F);
            var14.renderAll();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
    {
        this.renderGCTileEntityTreasureChestAt((GCCoreTileEntityTreasureChest)par1TileEntity, par2, par4, par6, par8);
    }
}
