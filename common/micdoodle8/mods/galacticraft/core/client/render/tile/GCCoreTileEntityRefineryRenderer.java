package micdoodle8.mods.galacticraft.core.client.render.tile;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GCCoreTileEntityRefineryRenderer extends TileEntitySpecialRenderer
{
    @Override
    public void renderTileEntityAt(TileEntity var1, double par2, double par4, double par6, float var8)
    {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glTranslatef(0.5F, 1.5F, 0.5F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);

        this.bindTextureByName("/micdoodle8/mods/galacticraft/core/client/entities/refinery.png");

        // table.modelRefinery.renderAll();
        //
        // if (table.getStackInSlot(0) != null)
        // {
        // table.modelRefinery.renderTank1();
        // }
        //
        // if (table.getStackInSlot(3) != null)
        // {
        // table.modelRefinery.renderTank2();
        // }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
