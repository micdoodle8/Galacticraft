package micdoodle8.mods.galacticraft.core.client.render.tile;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityArclamp;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityArclampRenderer extends TileEntitySpecialRenderer<TileEntityArclamp>
{
    public static final ResourceLocation lampTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/misc/underoil.png");
    public static final ResourceLocation lightTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/misc/light.png");
    private static OBJModel.OBJBakedModel lampMetal;
//    private static OBJModel.OBJBakedModel lampBase;
    private TextureManager renderEngine = FMLClientHandler.instance().getClient().renderEngine;

    @Override
    public void renderTileEntityAt(TileEntityArclamp tileEntity, double d, double d1, double d2, float f, int par9)
    {
        this.updateModels();
        int side = tileEntity.getBlockMetadata();
        int metaFacing = tileEntity.facing;

        GL11.glPushMatrix();
        GL11.glTranslatef((float) d + 0.5F, (float) d1 + 0.5F, (float) d2 + 0.5F);

        switch (side)
        {
        case 0:
            break;
        case 1:
            GL11.glRotatef(180F, 1F, 0, 0);
            if (metaFacing < 2)
            {
                metaFacing ^= 1;
            }
            break;
        case 2:
            GL11.glRotatef(90F, 1F, 0, 0);
            metaFacing ^= 1;
            break;
        case 3:
            GL11.glRotatef(90F, -1F, 0, 0);
            break;
        case 4:
            GL11.glRotatef(90F, 0, 0, -1F);
            metaFacing -= 2;
            if (metaFacing < 0)
            {
                metaFacing = 1 - metaFacing;
            }
            break;
        case 5:
            GL11.glRotatef(90F, 0, 0, 1F);
            metaFacing += 2;
            if (metaFacing > 3)
            {
                metaFacing = 5 - metaFacing;
            }
            break;
        }

        GL11.glTranslatef(0, -0.175F, 0);
        switch (metaFacing)
        {
        case 0:
            break;
        case 1:
            GL11.glRotatef(180F, 0, 1F, 0);
            break;
        case 2:
            GL11.glRotatef(90F, 0, 1F, 0);
            break;
        case 3:
            GL11.glRotatef(270F, 0, 1F, 0);
            break;
        }

        this.renderEngine.bindTexture(TileEntityArclampRenderer.lampTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(45F, -1F, 0, 0);
        GL11.glScalef(0.048F, 0.048F, 0.048F);
        ClientUtil.drawBakedModel(TileEntityArclampRenderer.lampMetal);
       
        float greyLevel = tileEntity.getEnabled() ? 1.0F : 26F / 255F;
        
        //Save the lighting state
        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        GL11.glDisable(GL11.GL_LIGHTING);

        this.renderEngine.bindTexture(TileEntityArclampRenderer.lightTexture);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        final Tessellator tess = Tessellator.getInstance();
        VertexBuffer worldRenderer = tess.getBuffer();
        GL11.glColor4f(greyLevel, greyLevel, greyLevel, 1.0F);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        float frameA = -3.4331F;  //These co-ordinates came originally from arclamp_light.obj model
        float frameB = -frameA;  //These co-ordinates came originally from arclamp_light.obj model
        float frameY = 2.3703F;  //These co-ordinates came originally from arclamp_light.obj model
        worldRenderer.pos(frameA, frameY, frameB).endVertex();
        worldRenderer.pos(frameB, frameY, frameB).endVertex();
        worldRenderer.pos(frameB, frameY, frameA).endVertex();
        worldRenderer.pos(frameA, frameY, frameA).endVertex();
        tess.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        //? need to undo GL11.glBlendFunc()?

        //Restore the lighting state
        GL11.glEnable(GL11.GL_LIGHTING);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
        
        GL11.glPopMatrix();
    }


    private void updateModels()
    {
        if (lampMetal == null)
        {
            try
            {
                OBJModel model = (OBJModel) ModelLoaderRegistry.getModel(new ResourceLocation(Constants.ASSET_PREFIX, "arclamp_metal.obj"));
                model = (OBJModel) model.process(ImmutableMap.of("flip-v", "true"));

                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                lampMetal = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("main"), false), DefaultVertexFormats.ITEM, spriteFunction);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
