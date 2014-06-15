package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.tile.TileEntityArclamp;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;



@SideOnly(Side.CLIENT)
public class TileEntityArclampRenderer extends TileEntitySpecialRenderer
{
	public static final ResourceLocation lampTexture = new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "textures/misc/underoil.png");
	public static final ResourceLocation lightTexture = new ResourceLocation("minecraft", "textures/blocks/snow.png");
	public static final IModelCustom lampMetal = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "models/arclampMetal.obj"));
	public static final IModelCustom lampLight = AdvancedModelLoader.loadModel(new ResourceLocation(GalacticraftCore.ASSET_DOMAIN, "models/arclampLight.obj"));
	
	public void renderModelAt(TileEntityArclamp tileEntity, double d, double d1, double d2, float f)
	{
		int side = tileEntity.getBlockMetadata();
		int metaFacing = side >> 4;
		side = side & 15;
		//int facing;
		/*switch (side)
		{
		case 0:
		case 1:
			facing = metaFacing + 2;
			break;
		case 2:
			facing = metaFacing;
			if (metaFacing > 1) facing= 7 - metaFacing;
			break;
		case 3:
			facing = metaFacing;
			if (metaFacing > 1) facing+=2;
			break;
		case 4:
			facing = metaFacing;
			break;
		case 5:
			facing = metaFacing;
			if (metaFacing > 1) facing= 5 - metaFacing;
			break;
		default:
			return;						
		}*/		
		
		GL11.glPushMatrix();
		GL11.glTranslatef((float) d + 0.5F, (float) d1 + 0.275F, (float) d2 + 0.5F);
		
		switch (metaFacing)
		{
		case 0:
			GL11.glRotatef(45F, 1F, 0, 0);
			break;
		case 1:
			GL11.glRotatef(45F, 0, 0, -1F);
			break;
		case 2:
			GL11.glRotatef(45F, -1F, 0, 0);
			break;
		case 3:
			GL11.glRotatef(45F, 0, 0, 1F);
			break;
			
		}
		
		switch (side)
		{
		case 0:
			break;
		case 1:
			GL11.glRotatef(180F, 1F, 0, 0);
			break;
		case 2:
			GL11.glRotatef(90F, 1F, 0, 0);
			break;
		case 3:
			GL11.glRotatef(90F, -1F, 0, 0);
			break;
		case 4:
			GL11.glRotatef(90F, 0, 0, 1F);
			break;
		case 5:
			GL11.glRotatef(90F, 0, 0, -1F);
			break;
		}

		GL11.glScalef(0.05F, 0.05F, 0.05F);
		
		// Texture file
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityArclampRenderer.lampTexture);		
		lampMetal.renderAll();
		FMLClientHandler.instance().getClient().renderEngine.bindTexture(TileEntityArclampRenderer.lightTexture);		
		lampLight.renderAll();

		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double var2, double var4, double var6, float var8)
	{
		this.renderModelAt((TileEntityArclamp) tileEntity, var2, var4, var6, var8);
	}
}
