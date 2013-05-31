//package micdoodle8.mods.galacticraft.core.client.render.tile;
//
//import micdoodle8.mods.galacticraft.core.client.model.GCCoreModelOxygenBubble;
//import micdoodle8.mods.galacticraft.core.tile.GCCoreTileEntityOxygenDistributor;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
//import net.minecraft.tileentity.TileEntity;
//
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//
//public class GCCoreTileEntityOxygenDistributorRenderer extends TileEntitySpecialRenderer
//{
//	private GCCoreModelOxygenBubble bubble;
//
//	public GCCoreTileEntityOxygenDistributorRenderer()
//	{
//		bubble = new GCCoreModelOxygenBubble();
//	}
//
//	@Override
//	public void renderTileEntityAt(TileEntity var1, double par2, double par4, double par6, float var8)
//	{
//		final GCCoreTileEntityOxygenDistributor distributor = (GCCoreTileEntityOxygenDistributor) var1;
//
//      	GL11.glPushMatrix();
//      	GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//      	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//	    GL11.glTranslatef((float)par2, (float)par4, (float)par6);
//      	GL11.glDisable(GL11.GL_LIGHTING);
//      	RenderHelper.disableStandardItemLighting();
//
//		this.bindTextureByName("/micdoodle8/mods/galacticraft/core/client/entities/bubble.png");
//
//		bubble.render(distributor.oxygenBubble, (float)par2, (float)par4, (float)par6, 0, 0, 0.062F);
//
//      	GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//      	GL11.glPopMatrix();
//      	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//	}
// }
