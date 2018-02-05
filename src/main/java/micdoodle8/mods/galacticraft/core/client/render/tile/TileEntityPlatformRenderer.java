package micdoodle8.mods.galacticraft.core.client.render.tile;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockPlatform;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPlatform;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityPlatformRenderer extends TileEntitySpecialRenderer<TileEntityPlatform>
{
    public static final ResourceLocation lightTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/misc/light.png");

    @Override
    public void renderTileEntityAt(TileEntityPlatform tileEntity, double d, double d1, double d2, float f, int par9)
    {
        IBlockState bs = tileEntity.getWorld().getBlockState(tileEntity.getPos()); 
        int rot = ((BlockPlatform.EnumCorner)bs.getValue(BlockPlatform.CORNER)).getMeta();
        if (rot == 0 || !tileEntity.lightEnabled()) return;
        
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) d + 0.5F, (float) d1 + 0.5F, (float) d2 + 0.5F);

        if (rot > 0)
        {
            GlStateManager.rotate(90F * rot, 0, 1F, 0F);
        }

        GlStateManager.translate(-0.5F, -0.5F, 0.5F);
        RenderHelper.disableStandardItemLighting();

        float greyLevel = 125F / 255F;
        switch (tileEntity.lightColor())
        {
        case 1: GlStateManager.color(1.0F, 115F/255F, 115F/255F, 1.0F);
            break;
        default: GlStateManager.color(greyLevel, 1.0F, greyLevel, 1.0F);
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
        WorldRenderer worldRenderer = tess.getWorldRenderer();
        float frameY = 1.001F;
        float frameA, frameB, frameC, frameD;
        frameC = 0.7F;
        frameD = 0.58F;
        frameB = frameC + 0.02F;
        frameA = -frameD;
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(frameA, frameY, frameB).endVertex();
        worldRenderer.pos(frameD, frameY, frameB).endVertex();
        worldRenderer.pos(frameD, frameY, frameC).endVertex();
        worldRenderer.pos(frameA, frameY, frameC).endVertex();
        tess.draw();
        GlStateManager.rotate(90F, 0, 1F, 0F);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(frameA, frameY, frameB).endVertex();
        worldRenderer.pos(frameD, frameY, frameB).endVertex();
        worldRenderer.pos(frameD, frameY, frameC).endVertex();
        worldRenderer.pos(frameA, frameY, frameC).endVertex();
        tess.draw();
        GlStateManager.rotate(90F, 0, 1F, 0F);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(frameA, frameY, frameB).endVertex();
        worldRenderer.pos(frameD, frameY, frameB).endVertex();
        worldRenderer.pos(frameD, frameY, frameC).endVertex();
        worldRenderer.pos(frameA, frameY, frameC).endVertex();
        tess.draw();
        GlStateManager.rotate(90F, 0, 1F, 0F);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(frameA, frameY, frameB).endVertex();
        worldRenderer.pos(frameD, frameY, frameB).endVertex();
        worldRenderer.pos(frameD, frameY, frameC).endVertex();
        worldRenderer.pos(frameA, frameY, frameC).endVertex();
        tess.draw();
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
