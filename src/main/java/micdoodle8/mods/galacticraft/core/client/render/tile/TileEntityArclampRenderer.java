package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.tile.TileEntityArclamp;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityArclampRenderer extends TileEntitySpecialRenderer<TileEntityArclamp>
{
    public static final ResourceLocation lampTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/misc/underoil.png");
    public static final ResourceLocation lightTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/misc/light.png");
    private static IBakedModel lampMetal;

    @Override
    public void render(TileEntityArclamp tileEntity, double d, double d1, double d2, float f, int par9, float alpha)
    {
        this.updateModels();
        int side = tileEntity.getBlockMetadata();
        int metaFacing = tileEntity.facing;

        GlStateManager.disableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) d + 0.5F, (float) d1 + 0.5F, (float) d2 + 0.5F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();

        switch (side)
        {
        case 0:
            break;
        case 1:
            GlStateManager.rotate(180F, 1F, 0, 0);
            if (metaFacing < 2)
            {
                metaFacing ^= 1;
            }
            break;
        case 2:
            GlStateManager.rotate(90F, 1F, 0, 0);
            metaFacing ^= 1;
            break;
        case 3:
            GlStateManager.rotate(90F, -1F, 0, 0);
            break;
        case 4:
            GlStateManager.rotate(90F, 0, 0, -1F);
            metaFacing -= 2;
            if (metaFacing < 0)
            {
                metaFacing = 1 - metaFacing;
            }
            break;
        case 5:
            GlStateManager.rotate(90F, 0, 0, 1F);
            metaFacing += 2;
            if (metaFacing > 3)
            {
                metaFacing = 5 - metaFacing;
            }
            break;
        }

        GlStateManager.translate(0, -0.175F, 0);

        switch (metaFacing)
        {
        case 0:
            break;
        case 1:
            GlStateManager.rotate(180F, 0, 1F, 0);
            break;
        case 2:
            GlStateManager.rotate(90F, 0, 1F, 0);
            break;
        case 3:
            GlStateManager.rotate(270F, 0, 1F, 0);
            break;
        }

        this.bindTexture(TileEntityArclampRenderer.lampTexture);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.rotate(45F, -1F, 0, 0);
        GlStateManager.scale(0.048F, 0.048F, 0.048F);
        ClientUtil.drawBakedModel(TileEntityArclampRenderer.lampMetal);
        RenderHelper.disableStandardItemLighting();

        float greyLevel = tileEntity.getEnabled() ? 1.0F : 26F / 255F;
        //Save the lighting state
        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        GlStateManager.disableLighting();

        this.bindTexture(TileEntityArclampRenderer.lightTexture);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();
        final Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldRenderer = tess.getBuffer();
        GlStateManager.color(greyLevel, greyLevel, greyLevel, 1.0F);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        float frameA = -3.4331F;  //These co-ordinates came originally from arclamp_light.obj model
        float frameB = -frameA;  //These co-ordinates came originally from arclamp_light.obj model
        float frameY = 2.3703F;  //These co-ordinates came originally from arclamp_light.obj model
        worldRenderer.pos(frameA, frameY, frameB).endVertex();
        worldRenderer.pos(frameB, frameY, frameB).endVertex();
        worldRenderer.pos(frameB, frameY, frameA).endVertex();
        worldRenderer.pos(frameA, frameY, frameA).endVertex();
        tess.draw();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        //? need to undo GlStateManager.glBlendFunc()?

        //Restore the lighting state
        GlStateManager.enableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
        GlStateManager.popMatrix();
    }

    private void updateModels()
    {
        if (lampMetal == null)
        {
            try
            {
                lampMetal = ClientUtil.modelFromOBJ(new ResourceLocation(Constants.ASSET_PREFIX, "arclamp_metal.obj"));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
