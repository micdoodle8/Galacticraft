package micdoodle8.mods.galacticraft.planets.mars.client.render.tile;

import micdoodle8.mods.galacticraft.core.client.model.block.ModelTreasureChest;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.mars.tile.TileEntityTreasureChestMars;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class TileEntityTreasureChestRenderer extends TileEntitySpecialRenderer<TileEntityTreasureChestMars>
{
    private static final ResourceLocation treasureChestTexture = new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "textures/model/treasure.png");

    private final ModelTreasureChest chestModel = new ModelTreasureChest();

    @Override
    public void renderTileEntityAt(TileEntityTreasureChestMars chest, double x, double y, double z, float par7, int par8)
    {
        int var9;

        if (!chest.hasWorldObj())
        {
            var9 = 0;
        }
        else
        {
            var9 = chest.getBlockMetadata();
        }

        this.bindTexture(TileEntityTreasureChestRenderer.treasureChestTexture);

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

        GL11.glRotatef(var11, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        float var12 = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * par8;

        var12 = 1.0F - var12;
        var12 = 1.0F - var12 * var12 * var12;

        this.chestModel.chestLid.rotateAngleX = -(var12 * (float) Math.PI / 4.0F);
        this.chestModel.renderAll(!chest.locked);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}