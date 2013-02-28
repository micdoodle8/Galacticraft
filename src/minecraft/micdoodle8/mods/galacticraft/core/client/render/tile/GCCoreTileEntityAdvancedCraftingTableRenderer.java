package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityAdvancedCraftingTable;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.FMLLog;

public class GCCoreTileEntityAdvancedCraftingTableRenderer extends TileEntitySpecialRenderer
{
	@Override
	public void renderTileEntityAt(TileEntity var1, double par2, double par4, double par6, float var8) 
	{
		GCCoreTileEntityAdvancedCraftingTable table = (GCCoreTileEntityAdvancedCraftingTable) var1;

      	GL11.glPushMatrix();
      	GL11.glEnable(GL12.GL_RESCALE_NORMAL);
      	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    GL11.glTranslatef((float)par2, (float)par4, (float)par6);
      	GL11.glTranslatef(0.5F, 3.0F, 0.5F);
	    GL11.glScalef(1.3F, -1.3F, -1.3F);
	    
		this.bindTextureByName("/micdoodle8/mods/galacticraft/core/client/entities/assembly.png");

	    table.model.renderAll();
	    
      	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
      	GL11.glPopMatrix();
      	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
