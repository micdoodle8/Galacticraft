//package micdoodle8.mods.galacticraft.core.client.render.tile;
//
//import com.google.common.collect.ImmutableList;
//
//import com.mojang.blaze3d.platform.GLX;
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.entities.IBubbleProvider;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityArclamp;
//import micdoodle8.mods.galacticraft.core.util.ClientUtil;
//import micdoodle8.mods.galacticraft.core.util.ColorUtil;
//import com.mojang.blaze3d.systems.RenderSystem;
//import net.minecraft.client.renderer.model.IBakedModel;
//import net.minecraft.client.renderer.texture.AtlasTexture;
//import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ResourceLocation;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//
//@Deprecated
//public class TileEntityBubbleProviderRenderer<E extends TileEntity & IBubbleProvider> extends TileEntityRenderer<E>
//{
//    public static IBakedModel sphere;
//
//    private final float colorRed;
//    private final float colorGreen;
//    private final float colorBlue;
//
//    public TileEntityBubbleProviderRenderer(float colorRed, float colorGreen, float colorBlue)
//    {
//        this.colorRed = colorRed;
//        this.colorGreen = colorGreen;
//        this.colorBlue = colorBlue;
//    }
//
//    private static void updateModels()
//    {
//        if (sphere == null)
//        {
//            try
//            {
//            }
//            catch (Exception e)
//            {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//
//    @Override
//    public void render(E tile, double x, double y, double z, float partialTicks, int destroyStage)
//    {
//        if (!tile.getBubbleVisible())
//        {
//            return;
//        }
//
//        updateModels();
//
//        RenderSystem.pushMatrix();
//        RenderSystem.enableRescaleNormal();
//        RenderSystem.translatef((float) x + 0.5F, (float) y + 1.0F, (float) z + 0.5F);
//
//        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
//
//        RenderSystem.enableBlend();
//        RenderSystem.disableLighting();
//        RenderSystem.disable(GL11.GL_CULL_FACE);
//
//        RenderSystem.AlphaFunc(GL11.GL_GREATER, 0.1F);
//        RenderSystem.blendFunc(770, 771);
//        RenderSystem.color4f(this.colorRed / 2.0F, this.colorGreen / 2.0F, this.colorBlue / 2.0F, 1.0F);
//        RenderSystem.matrixMode(GL11.GL_TEXTURE);
//        RenderSystem.loadIdentity();
//        RenderSystem.matrixMode(5888);
//        RenderSystem.depthMask(false);
////        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
////        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
////        RenderSystem.glMultiTexCoord2f(33985, 240.0F, 240.0F);
//        RenderSystem.glMultiTexCoord2f(33985, 240.0F, 240.0F);
//        RenderSystem.scalef(tile.getBubbleSize(), tile.getBubbleSize(), tile.getBubbleSize());
//
//        int color = ColorUtil.to32BitColor(30, (int) (this.colorBlue / 2.0F * 255), (int) (this.colorGreen / 2.0F * 255), (int) (this.colorRed / 2.0F * 255));
//        ClientUtil.drawBakedModelColored(sphere, color);
//
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.matrixMode(GL11.GL_TEXTURE);
//        RenderSystem.depthMask(true);
//        RenderSystem.loadIdentity();
//        RenderSystem.matrixMode(5888);
//        RenderSystem.enableLighting();
//        RenderSystem.disableBlend();
//        RenderSystem.depthFunc(GL11.GL_LEQUAL);
//        RenderSystem.enable(GL11.GL_CULL_FACE);
//
////        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
//
//        RenderSystem.disableRescaleNormal();
//        RenderSystem.popMatrix();
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//    }
//}
