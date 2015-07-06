package micdoodle8.mods.galacticraft.core.client.render.tile;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.blocks.BlockT1TreasureChest;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelTreasureChest;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelTreasureChestLarge;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class TileEntityTreasureChestRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation treasureChestTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/treasure.png");
    private static final ResourceLocation treasureLargeChestTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/treasurelarge.png");

    /**
     * The normal small chest model.
     */
    private final ModelTreasureChest chestModel = new ModelTreasureChest();

    /**
     * The large double chest model.
     */
    private final ModelTreasureChestLarge largeChestModel = new ModelTreasureChestLarge();

    /**
     * Renders the TileEntity for the chest at a position.
     */
    @Override
    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float partialTickTime, int par9)
    {
            TileEntityTreasureChest chest = (TileEntityTreasureChest) tile;
        int var9;

        if (!chest.hasWorldObj())
        {
            var9 = 0;
        }
        else
        {
            final Block var10 = chest.getBlockType();
            var9 = chest.getBlockMetadata();

            if (var10 != null && var9 == 0)
            {
//                ((BlockT1TreasureChest) var10).unifyAdjacentChests(chest.getWorldObj(), chest.xCoord, chest.yCoord, chest.zCoord); TODO
                var9 = chest.getBlockMetadata();
            }

            chest.checkForAdjacentChests();
        }

        if (chest.adjacentChestZNeg == null && chest.adjacentChestXNeg == null)
        {
            ModelTreasureChest var14 = null;
            ModelTreasureChestLarge var14b = null;

            if (chest.adjacentChestXPos == null && chest.adjacentChestZPos == null)
            {
                var14 = this.chestModel;
                this.bindTexture(TileEntityTreasureChestRenderer.treasureChestTexture);
            }
            else
            {
                var14b = this.largeChestModel;
                this.bindTexture(TileEntityTreasureChestRenderer.treasureLargeChestTexture);
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

            if (var9 == 2 && chest.adjacentChestXPos != null)
            {
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            }

            if (var9 == 5 && chest.adjacentChestZPos != null)
            {
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            }

            GL11.glRotatef(var11, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float var12 = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * partialTickTime;

            float var13;

            if (chest.adjacentChestZNeg != null)
            {
                var13 = chest.adjacentChestZNeg.prevLidAngle + (chest.adjacentChestZNeg.lidAngle - chest.adjacentChestZNeg.prevLidAngle) * partialTickTime;

                if (var13 > var12)
                {
                    var12 = var13;
                }
            }

            if (chest.adjacentChestXNeg != null)
            {
                var13 = chest.adjacentChestXNeg.prevLidAngle + (chest.adjacentChestXNeg.lidAngle - chest.adjacentChestXNeg.prevLidAngle) * partialTickTime;

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
                var14.renderAll(!chest.isLocked());
            }

            if (var14b != null)
            {
                var14b.chestLid.rotateAngleX = -(var12 * (float) Math.PI / 4.0F);
                var14b.renderAll(!chest.isLocked());
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
