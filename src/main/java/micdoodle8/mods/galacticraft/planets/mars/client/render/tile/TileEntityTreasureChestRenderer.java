package micdoodle8.mods.galacticraft.planets.mars.client.render.tile;

import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelTreasureChest;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelTreasureChestLarge;
import micdoodle8.mods.galacticraft.planets.mars.MarsModule;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockTier2TreasureChest;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTreasureChestMars;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class TileEntityTreasureChestRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation treasureChestTexture = new ResourceLocation(MarsModule.ASSET_PREFIX, "textures/model/treasure.png");
    private static final ResourceLocation treasureLargeChestTexture = new ResourceLocation(MarsModule.ASSET_PREFIX, "textures/model/treasurelarge.png");

    private final ModelTreasureChest chestModel = new ModelTreasureChest();
    private final ModelTreasureChestLarge largeChestModel = new ModelTreasureChestLarge();

    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float par7, int par8)
    {
            TileEntityTreasureChestMars treasureChestMars = (TileEntityTreasureChestMars)tileEntity;
        int var9;

        if (!treasureChestMars.hasWorldObj())
        {
            var9 = 0;
        }
        else
        {
            final Block var10 = treasureChestMars.getBlockType();
            var9 = treasureChestMars.getBlockMetadata();

            if (var10 != null && var9 == 0)
            {
//                ((BlockTier2TreasureChest) var10).unifyAdjacentChests(par1GCTileEntityTreasureChest.getWorldObj(), par1GCTileEntityTreasureChest.xCoord, par1GCTileEntityTreasureChest.yCoord, par1GCTileEntityTreasureChest.zCoord);
                var9 = treasureChestMars.getBlockMetadata();
            }

            treasureChestMars.checkForAdjacentChests();
        }

        if (treasureChestMars.adjacentChestZNeg == null && treasureChestMars.adjacentChestXNeg == null)
        {
            ModelTreasureChest var14 = null;
            ModelTreasureChestLarge var14b = null;

            if (treasureChestMars.adjacentChestXPos == null && treasureChestMars.adjacentChestZPos == null)
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
            GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
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

            if (var9 == 2 && treasureChestMars.adjacentChestXPos != null)
            {
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            }

            if (var9 == 5 && treasureChestMars.adjacentChestZPos != null)
            {
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            }

            GL11.glRotatef(var11, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float var12 = treasureChestMars.prevLidAngle + (treasureChestMars.lidAngle - treasureChestMars.prevLidAngle) * par8;

            float var13;

            if (treasureChestMars.adjacentChestZNeg != null)
            {
                var13 = treasureChestMars.adjacentChestZNeg.prevLidAngle + (treasureChestMars.adjacentChestZNeg.lidAngle - treasureChestMars.adjacentChestZNeg.prevLidAngle) * par8;

                if (var13 > var12)
                {
                    var12 = var13;
                }
            }

            if (treasureChestMars.adjacentChestXNeg != null)
            {
                var13 = treasureChestMars.adjacentChestXNeg.prevLidAngle + (treasureChestMars.adjacentChestXNeg.lidAngle - treasureChestMars.adjacentChestXNeg.prevLidAngle) * par8;

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
                var14.renderAll(!treasureChestMars.locked);
            }

            if (var14b != null)
            {
                var14b.chestLid.rotateAngleX = -(var12 * (float) Math.PI / 4.0F);
                var14b.renderAll(!treasureChestMars.locked);
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}