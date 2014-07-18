package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.planets.asteroids.AsteroidsModule;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityShortRangeTelepadRenderer extends TileEntitySpecialRenderer
{
	public static final ResourceLocation telepadTexture = new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "textures/model/teleporter.png");
	public static IModelCustom telepadModel;

	public TileEntityShortRangeTelepadRenderer()
	{
		TileEntityShortRangeTelepadRenderer.telepadModel = AdvancedModelLoader.loadModel(new ResourceLocation(AsteroidsModule.ASSET_PREFIX, "models/teleporter.obj"));
	}

	public void renderModelAt(TileEntityShortRangeTelepad tileEntity, double d, double d1, double d2, float f)
	{
		// Texture file
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityShortRangeTelepadRenderer.telepadTexture);

		GL11.glPushMatrix();

		GL11.glTranslatef((float) d + 0.5F, (float) d1, (float) d2 + 0.5F);
		GL11.glScalef(0.75F, 0.65F, 0.75F);
		TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("Base");
        GL11.glTranslatef(0.0F, (float)Math.sin(tileEntity.ticks / 10.0F) / 10.0F - 0.25F, 0.0F);
        TileEntityShortRangeTelepadRenderer.telepadModel.renderPart("Top");

		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
	{
		this.renderModelAt((TileEntityShortRangeTelepad) tileEntity, var2, var4, var6, var8);
	}
}
