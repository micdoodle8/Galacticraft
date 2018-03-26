package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelTreasureChest;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelTreasureChestLarge;
import micdoodle8.mods.galacticraft.core.tile.TileEntityTreasureChest;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class TileEntityTreasureChestRenderer extends TileEntitySpecialRenderer<TileEntityTreasureChest>
{
    private static final ResourceLocation treasureChestTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/treasure.png");

    private final ModelTreasureChest chestModel = new ModelTreasureChest();

    /**
     * Renders the TileEntity for the chest at a position.
     */
    @Override
    public void renderTileEntityAt(TileEntityTreasureChest chest, double par2, double par4, double par6, float partialTickTime, int par9)
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

        ModelTreasureChest var14 = null;
        ModelTreasureChestLarge var14b = null;

        var14 = this.chestModel;
        this.bindTexture(TileEntityTreasureChestRenderer.treasureChestTexture);

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
        float var12 = chest.prevLidAngle + (chest.lidAngle - chest.prevLidAngle) * partialTickTime;

        float var13;

        var12 = 1.0F - var12;
        var12 = 1.0F - var12 * var12 * var12;

        if (var14 != null)
        {
            var14.chestLid.rotateAngleX = -(var12 * (float) Math.PI / 4.0F);
            var14.renderAll(!chest.locked);
        }

        if (var14b != null)
        {
            var14b.chestLid.rotateAngleX = -(var12 * (float) Math.PI / 4.0F);
            var14b.renderAll(!chest.locked);
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
