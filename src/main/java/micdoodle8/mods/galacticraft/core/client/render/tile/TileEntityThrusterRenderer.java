package micdoodle8.mods.galacticraft.core.client.render.tile;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityThruster;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityThrusterRenderer extends TileEntitySpecialRenderer
{
	public static final ResourceLocation thrusterTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/model/thruster.png");
	public static final IModelCustom thrusterModel = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "models/thruster.obj"));

	public void renderModelAt(TileEntityThruster tileEntity, double d, double d1, double d2, float f)
	{
		// Texture file
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityThrusterRenderer.thrusterTexture);

		GL11.glPushMatrix();
		GL11.glTranslatef((float) d + 0.5F, (float) d1 + 0.5F, (float) d2 + 0.5F);
		
		int meta = tileEntity.getBlockMetadata();
		boolean reverseThruster = (meta >= 8);
		meta &= 7;

		if (meta >= 1)
		{
			switch (meta)
			{
			case 1:
				GL11.glTranslatef(-0.475F, 0.0F, 0.0F);
				GL11.glRotatef(0, 0, 1, 0);
				GL11.glScalef(0.55F, 0.55F, 0.55F);
				break;
			case 2:
				GL11.glTranslatef(0.475F, 0.0F, 0.0F);
				GL11.glRotatef(180, 0, 1, 0);
				GL11.glScalef(0.55F, 0.55F, 0.55F);
				break;
			case 3:
				GL11.glTranslatef(0.0F, 0.0F, -0.475F);
				GL11.glRotatef(270, 0, 1, 0);
				GL11.glScalef(0.55F, 0.55F, 0.55F);
				break;
			case 4:
				GL11.glTranslatef(0.0F, 0.0F, 0.475F);
				GL11.glRotatef(90, 0, 1, 0);
				GL11.glScalef(0.55F, 0.55F, 0.55F);
				break;
			default:
				break;
			}

			if (!reverseThruster)
				GL11.glRotatef(180, 1, 0, 0);
			TileEntityThrusterRenderer.thrusterModel.renderAll();
		}

		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
	{
		this.renderModelAt((TileEntityThruster) tileEntity, var2, var4, var6, var8);
	}
}
