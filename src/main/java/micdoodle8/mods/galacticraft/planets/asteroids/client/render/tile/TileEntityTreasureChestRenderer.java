package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import micdoodle8.mods.galacticraft.core.blocks.BlockTier3TreasureChest;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelTreasureChest;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelTreasureChestLarge;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityTreasureChestAsteroids;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class TileEntityTreasureChestRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation treasureChestTexture = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/model/treasure.png");
    private static final ResourceLocation treasureLargeChestTexture = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/model/treasurelarge.png");

    private final ModelTreasureChest chestModel = new ModelTreasureChest();
    private final ModelTreasureChestLarge largeChestModel = new ModelTreasureChestLarge();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float par5, int par6)
    {
        TileEntityTreasureChestAsteroids chestAsteroids = (TileEntityTreasureChestAsteroids) tileEntity;
        if (chestAsteroids.getWorld() != null)
        	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        else
        	GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        int var9;

        if (!chestAsteroids.hasWorldObj())
        {
            var9 = 0;
        }
        else
        {
            final Block var10 = chestAsteroids.getBlockType();
            var9 = chestAsteroids.getBlockMetadata();

            if (var10 != null && var9 == 0)
            {
//                ((BlockTier3TreasureChest) var10).unifyAdjacentChests(par1GCTileEntityTreasureChest.getWorld(), par1GCTileEntityTreasureChest.getPos()); TODO
                var9 = chestAsteroids.getBlockMetadata();
            }

            chestAsteroids.checkForAdjacentChests();
        }

        if (chestAsteroids.adjacentChestZNeg == null && chestAsteroids.adjacentChestXNeg == null)
        {
            ModelTreasureChest var14 = null;
            ModelTreasureChestLarge var14b = null;

            if (chestAsteroids.adjacentChestXPos == null && chestAsteroids.adjacentChestZPos == null)
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

            if (var9 == 2 && chestAsteroids.adjacentChestXPos != null)
            {
                GL11.glTranslatef(1.0F, 0.0F, 0.0F);
            }

            if (var9 == 5 && chestAsteroids.adjacentChestZPos != null)
            {
                GL11.glTranslatef(0.0F, 0.0F, -1.0F);
            }

            GL11.glRotatef(var11, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float var12 = chestAsteroids.prevLidAngle + (chestAsteroids.lidAngle - chestAsteroids.prevLidAngle) * par6;

            float var13;

            if (chestAsteroids.adjacentChestZNeg != null)
            {
                var13 = chestAsteroids.adjacentChestZNeg.prevLidAngle + (chestAsteroids.adjacentChestZNeg.lidAngle - chestAsteroids.adjacentChestZNeg.prevLidAngle) * par6;

                if (var13 > var12)
                {
                    var12 = var13;
                }
            }

            if (chestAsteroids.adjacentChestXNeg != null)
            {
                var13 = chestAsteroids.adjacentChestXNeg.prevLidAngle + (chestAsteroids.adjacentChestXNeg.lidAngle - chestAsteroids.adjacentChestXNeg.prevLidAngle) * par6;

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
                var14.renderAll(!chestAsteroids.locked);
            }

            if (var14b != null)
            {
                var14b.chestLid.rotateAngleX = -(var12 * (float) Math.PI / 4.0F);
                var14b.renderAll(!chestAsteroids.locked);
            }

            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}