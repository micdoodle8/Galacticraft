package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockPanelLighting;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPanelLight;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityPanelLightRenderer extends TileEntitySpecialRenderer<TileEntityPanelLight>
{
    public static final ResourceLocation lightTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/misc/light.png");

    @Override
    public void render(TileEntityPanelLight tileEntity, double d, double d1, double d2, float f, int par9, float alpha)
    {
        int side = tileEntity.meta;
        int rot = side >> 3;
        side = (side & 7) ^ 1;
        BlockPanelLighting.PanelType type = tileEntity.getType();

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) d + 0.5F, (float) d1 + 0.5F, (float) d2 + 0.5F);

        switch (side)
        {
        case 0:
            break;
        case 1:
            GlStateManager.rotate(180F, 1F, 0, 0);
            break;
        case 2:
            GlStateManager.rotate(90F, 1F, 0, 0);
            break;
        case 3:
            GlStateManager.rotate(90F, -1F, 0, 0);
            break;
        case 4:
            GlStateManager.rotate(90F, 0, 0, -1F);
            rot = (rot + 1) % 4;
            break;
        case 5:
            GlStateManager.rotate(90F, 0, 0, 1F);
            rot = (rot + 1) % 4;
            break;
        }
        
        if (rot > 0)
        {
            GlStateManager.rotate(90F * rot, 0, 1F, 0F);
        }

        if (type == BlockPanelLighting.PanelType.SFDIAG)
        {
            GlStateManager.rotate(45F, 0, 1F, 0F);
        }

        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        RenderHelper.disableStandardItemLighting();

        if (tileEntity.getEnabled())
        {
            ColorUtil.setGLColor(tileEntity.color);
        }
        else
        {
            float greyLevel = 24F / 255F;
            GlStateManager.color(greyLevel, greyLevel, greyLevel, 1.0F);
        }
        
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
        float frameY = 1.01F;
        float frameA, frameB, frameC;
        switch (type) {
        case SQUARE:
        default:
            frameA = 0.15F;
            frameB = 0.5F;
            frameC = frameA;
            break;
        case SPOTS:
            frameA = 0.21F;
            frameB = 0.29F;
            frameC = frameA;
            break;
        case LINEAR:
            frameA = 0.08F;
            frameB = 0.5F;
            frameC = 0.36F;
            break;
        case SF:
        case SFDIAG:
            frameA = 0.1F;
            frameB = 0.4F;
            frameC = 0.35F;
        }
        if (type != BlockPanelLighting.PanelType.SFDIAG)
        {
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            worldRenderer.pos(frameA, frameY, frameB).endVertex();
            worldRenderer.pos(frameB, frameY, frameB).endVertex();
            worldRenderer.pos(frameB, frameY, frameC).endVertex();
            worldRenderer.pos(frameA, frameY, frameC).endVertex();
            tess.draw();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            worldRenderer.pos(1.0F - frameA, frameY, frameC).endVertex();
            worldRenderer.pos(1.0F - frameB, frameY, frameC).endVertex();
            worldRenderer.pos(1.0F - frameB, frameY, frameB).endVertex();
            worldRenderer.pos(1.0F - frameA, frameY, frameB).endVertex();
            tess.draw();
            frameA = 1.0F - frameA;
            frameB = 1.0F - frameB;
            frameC = 1.0F - frameC;
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            worldRenderer.pos(frameA, frameY, frameB).endVertex();
            worldRenderer.pos(frameB, frameY, frameB).endVertex();
            worldRenderer.pos(frameB, frameY, frameC).endVertex();
            worldRenderer.pos(frameA, frameY, frameC).endVertex();
            tess.draw();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            worldRenderer.pos(1.0F - frameA, frameY, frameC).endVertex();
            worldRenderer.pos(1.0F - frameB, frameY, frameC).endVertex();
            worldRenderer.pos(1.0F - frameB, frameY, frameB).endVertex();
            worldRenderer.pos(1.0F - frameA, frameY, frameB).endVertex();
            tess.draw();
        }
        else
        {
            frameA += 0.02F;
            GlStateManager.translate(0.239F, 0F, -0.345F);
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            worldRenderer.pos(frameA, frameY, frameB).endVertex();
            worldRenderer.pos(frameB, frameY, frameB).endVertex();
            worldRenderer.pos(frameB, frameY, frameC).endVertex();
            worldRenderer.pos(frameA, frameY, frameC).endVertex();
            tess.draw();
            frameA += 0.02F;
            GlStateManager.translate(0.23F, 0F, 0.233F);
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            worldRenderer.pos(frameA, frameY, frameB).endVertex();
            worldRenderer.pos(frameB, frameY, frameB).endVertex();
            worldRenderer.pos(frameB, frameY, frameC).endVertex();
            worldRenderer.pos(frameA, frameY, frameC).endVertex();
            tess.draw();
            GlStateManager.translate(-0.48F, 0F, 0F);
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
            worldRenderer.pos(frameA, frameY, frameB).endVertex();
            worldRenderer.pos(frameB, frameY, frameB).endVertex();
            worldRenderer.pos(frameB, frameY, frameC).endVertex();
            worldRenderer.pos(frameA, frameY, frameC).endVertex();
            tess.draw();
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableTexture2D();
        //? need to undo GlStateManager.glBlendFunc()?

        //Restore the lighting state
        GlStateManager.enableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
