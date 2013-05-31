package micdoodle8.mods.galacticraft.core.client.render.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class GCCoreTileEntityLandingPadRenderer extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity var1, double par2, double par4, double par6, float var8)
    {
        this.bindTextureByName("/micdoodle8/mods/galacticraft/core/client/entities/refinery.png");

        GL11.glPushMatrix();
        GL11.glTranslatef((float) par2 + 0.5F, (float) par4 + 1.5F, (float) par6 + 0.5F);
        GL11.glScalef(1.0F, -1F, -1F);
        GL11.glPopMatrix();
    }
}
