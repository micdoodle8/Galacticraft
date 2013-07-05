package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAdvancedCraftingTable;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.ResourceLocation;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GCCoreTileEntityAdvancedCraftingTableRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation assemblyTableTexture = new ResourceLocation(GalacticraftCore.TEXTURE_DOMAIN, "textures/model/assembly.png");

    @Override
    public void renderTileEntityAt(TileEntity var1, double par2, double par4, double par6, float var8)
    {
        final GCCoreTileEntityAdvancedCraftingTable table = (GCCoreTileEntityAdvancedCraftingTable) var1;

        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4, (float) par6);
        GL11.glTranslatef(0.5F, 3.0F, 0.5F);
        GL11.glScalef(1.3F, -1.3F, -1.3F);

        this.func_110628_a(GCCoreTileEntityAdvancedCraftingTableRenderer.assemblyTableTexture);

        table.model.renderAll();

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
