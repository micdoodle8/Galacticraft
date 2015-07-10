package micdoodle8.mods.galacticraft.core.client.render.tile;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.client.model.block.ModelParaChestTile;
import micdoodle8.mods.galacticraft.core.tile.TileEntityParaChest;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class TileEntityParachestRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation parachestTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/parachest.png");

    private final ModelParaChestTile chestModel = new ModelParaChestTile();

    @Override
    public void renderTileEntityAt(TileEntity tile, double par2, double par4, double par6, float partialTickTime, int par9)
    {
        TileEntityParaChest paraChest = (TileEntityParaChest) tile;
        int var9;

        if (!paraChest.hasWorldObj())
        {
            var9 = 0;
        }
        else
        {
            final Block var10 = paraChest.getBlockType();
            var9 = paraChest.getBlockMetadata();

            if (var10 != null && var9 == 0)
            {
                var9 = paraChest.getBlockMetadata();
            }
        }

        this.bindTexture(TileEntityParachestRenderer.parachestTexture);

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
        float var12 = paraChest.prevLidAngle + (paraChest.lidAngle - paraChest.prevLidAngle) * partialTickTime;

        var12 = 1.0F - var12;
        var12 = 1.0F - var12 * var12 * var12;

        this.chestModel.chestLid.rotateAngleX = -(var12 * (float) Math.PI / 4.0F);
        this.chestModel.renderAll(var12 == 0.0F);

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
