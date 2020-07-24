//package micdoodle8.mods.galacticraft.core.client.render.tile;
//
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.blocks.BlockPanelLighting;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityPanelLight;
//import micdoodle8.mods.galacticraft.core.util.ColorUtil;
//import net.minecraft.client.renderer.*;
//import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.fml.relauncher.SideOnly;
//import net.minecraftforge.fml.LogicalSide;
//
//import org.lwjgl.opengl.GL11;
//
//@OnlyIn(Dist.CLIENT)
//public class TileEntityPanelLightRenderer extends TileEntityRenderer<TileEntityPanelLight>
//{
//    public static final ResourceLocation lightTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/misc/light.png");
//
//    @Override
//    public void render(TileEntityPanelLight tileEntity, double d, double d1, double d2, float f, int par9, float alpha)
//    {
//        int LogicalSide = tileEntity.meta;
//        int rot = LogicalSide >> 3;
//        LogicalSide = (LogicalSide & 7) ^ 1;
//        BlockPanelLighting.PanelType type = tileEntity.getType();
//
//        RenderSystem.pushMatrix();
//        RenderSystem.translatef((float) d + 0.5F, (float) d1 + 0.5F, (float) d2 + 0.5F);
//
//        switch (LogicalSide)
//        {
//        case 0:
//            break;
//        case 1:
//            RenderSystem.rotatef(180F, 1F, 0, 0);
//            break;
//        case 2:
//            RenderSystem.rotatef(90F, 1F, 0, 0);
//            break;
//        case 3:
//            RenderSystem.rotatef(90F, -1F, 0, 0);
//            break;
//        case 4:
//            RenderSystem.rotatef(90F, 0, 0, -1F);
//            rot = (rot + 1) % 4;
//            break;
//        case 5:
//            RenderSystem.rotatef(90F, 0, 0, 1F);
//            rot = (rot + 1) % 4;
//            break;
//        }
//
//        if (rot > 0)
//        {
//            RenderSystem.rotatef(90F * rot, 0, 1F, 0F);
//        }
//
//        if (type == BlockPanelLighting.PanelType.SFDIAG)
//        {
//            RenderSystem.rotatef(45F, 0, 1F, 0F);
//        }
//
//        RenderSystem.translatef(-0.5F, -0.5F, -0.5F);
//        RenderHelper.disableStandardItemLighting();
//
//        if (tileEntity.getEnabled())
//        {
//            ColorUtil.setGLColor(tileEntity.color);
//        }
//        else
//        {
//            float greyLevel = 24F / 255F;
//            RenderSystem.color4f(greyLevel, greyLevel, greyLevel, 1.0F);
//        }
//
//        //Save the lighting state
//        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
//        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
//        RenderSystem.glMultiTexCoord2f(33985, 240.0F, 240.0F);
//        RenderSystem.disableLighting();
//
//        this.bindTexture(TileEntityArclampRenderer.lightTexture);
//        RenderSystem.blendFunc(770, 771);
//        RenderSystem.disableTexture();
//        final Tessellator tess = Tessellator.getInstance();
//        BufferBuilder worldRenderer = tess.getBuffer();
//        float frameY = 1.01F;
//        float frameA, frameB, frameC;
//        switch (type) {
//        case SQUARE:
//        default:
//            frameA = 0.15F;
//            frameB = 0.5F;
//            frameC = frameA;
//            break;
//        case SPOTS:
//            frameA = 0.21F;
//            frameB = 0.29F;
//            frameC = frameA;
//            break;
//        case LINEAR:
//            frameA = 0.08F;
//            frameB = 0.5F;
//            frameC = 0.36F;
//            break;
//        case SF:
//        case SFDIAG:
//            frameA = 0.1F;
//            frameB = 0.4F;
//            frameC = 0.35F;
//        }
//        if (type != BlockPanelLighting.PanelType.SFDIAG)
//        {
//            worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//            worldRenderer.pos(frameA, frameY, frameB).endVertex();
//            worldRenderer.pos(frameB, frameY, frameB).endVertex();
//            worldRenderer.pos(frameB, frameY, frameC).endVertex();
//            worldRenderer.pos(frameA, frameY, frameC).endVertex();
//            tess.draw();
//            worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//            worldRenderer.pos(1.0F - frameA, frameY, frameC).endVertex();
//            worldRenderer.pos(1.0F - frameB, frameY, frameC).endVertex();
//            worldRenderer.pos(1.0F - frameB, frameY, frameB).endVertex();
//            worldRenderer.pos(1.0F - frameA, frameY, frameB).endVertex();
//            tess.draw();
//            frameA = 1.0F - frameA;
//            frameB = 1.0F - frameB;
//            frameC = 1.0F - frameC;
//            worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//            worldRenderer.pos(frameA, frameY, frameB).endVertex();
//            worldRenderer.pos(frameB, frameY, frameB).endVertex();
//            worldRenderer.pos(frameB, frameY, frameC).endVertex();
//            worldRenderer.pos(frameA, frameY, frameC).endVertex();
//            tess.draw();
//            worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//            worldRenderer.pos(1.0F - frameA, frameY, frameC).endVertex();
//            worldRenderer.pos(1.0F - frameB, frameY, frameC).endVertex();
//            worldRenderer.pos(1.0F - frameB, frameY, frameB).endVertex();
//            worldRenderer.pos(1.0F - frameA, frameY, frameB).endVertex();
//            tess.draw();
//        }
//        else
//        {
//            frameA += 0.02F;
//            RenderSystem.translatef(0.239F, 0F, -0.345F);
//            worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//            worldRenderer.pos(frameA, frameY, frameB).endVertex();
//            worldRenderer.pos(frameB, frameY, frameB).endVertex();
//            worldRenderer.pos(frameB, frameY, frameC).endVertex();
//            worldRenderer.pos(frameA, frameY, frameC).endVertex();
//            tess.draw();
//            frameA += 0.02F;
//            RenderSystem.translatef(0.23F, 0F, 0.233F);
//            worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//            worldRenderer.pos(frameA, frameY, frameB).endVertex();
//            worldRenderer.pos(frameB, frameY, frameB).endVertex();
//            worldRenderer.pos(frameB, frameY, frameC).endVertex();
//            worldRenderer.pos(frameA, frameY, frameC).endVertex();
//            tess.draw();
//            RenderSystem.translatef(-0.48F, 0F, 0F);
//            worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//            worldRenderer.pos(frameA, frameY, frameB).endVertex();
//            worldRenderer.pos(frameB, frameY, frameB).endVertex();
//            worldRenderer.pos(frameB, frameY, frameC).endVertex();
//            worldRenderer.pos(frameA, frameY, frameC).endVertex();
//            tess.draw();
//        }
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.enableTexture();
//        //? need to undo RenderSystem.glBlendFunc()?
//
//        //Restore the lighting state
//        RenderSystem.enableLighting();
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
//        RenderHelper.enableStandardItemLighting();
//        RenderSystem.popMatrix();
//    }
//}
