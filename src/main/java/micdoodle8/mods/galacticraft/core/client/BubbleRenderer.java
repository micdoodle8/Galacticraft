//package micdoodle8.mods.galacticraft.core.client;
//
//import com.google.common.collect.ImmutableList;
//import com.google.common.collect.Lists;
//
//import com.mojang.blaze3d.platform.GLX;
//import micdoodle8.mods.galacticraft.api.vector.Vector3;
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.entities.IBubbleProviderColored;
//import micdoodle8.mods.galacticraft.core.util.ClientUtil;
//import micdoodle8.mods.galacticraft.core.util.ColorUtil;
//import net.minecraft.client.Minecraft;
//import com.mojang.blaze3d.platform.GlStateManager;
//import net.minecraft.client.renderer.model.IBakedModel;
//import net.minecraft.client.renderer.texture.AtlasTexture;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraft.util.ResourceLocation;
//
//import org.lwjgl.opengl.GL11;
//
//import java.util.List;
//
//public class BubbleRenderer
//{
//    public static IBakedModel sphere;
//    private static final List<IBubbleProviderColored> bubbleProviders = Lists.newArrayList();
//
//    public static void clearBubbles()
//    {
//        bubbleProviders.clear();
//    }
//
//    public static void addBubble(IBubbleProviderColored tile)
//    {
//        bubbleProviders.add(tile);
//    }
//
//    public static void renderBubbles(PlayerEntity player, float partialTicks)
//    {
//        if (bubbleProviders.isEmpty())
//        {
//            return;
//        }
//
//        double interpPosX = player.lastTickPosX + (player.getPosX() - player.lastTickPosX) * partialTicks;
//        double interpPosY = player.lastTickPosY + (player.getPosY() - player.lastTickPosY) * partialTicks;
//        double interpPosZ = player.lastTickPosZ + (player.getPosZ() - player.lastTickPosZ) * partialTicks;
//
//        Minecraft.getInstance().textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
//
//        GlStateManager.pushMatrix();
//
//        GlStateManager.enableRescaleNormal();
//        GlStateManager.enableBlend();
//        GlStateManager.disableLighting();
//        GlStateManager.disableCull();
//
//        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
//        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        GlStateManager.matrixMode(GL11.GL_TEXTURE);
//        GlStateManager.loadIdentity();
//        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
//        GlStateManager.depthMask(false);
//        float lightMapSaveX = GLX.lastBrightnessX;
//        float lightMapSaveY = GLX.lastBrightnessY;
//        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
//
//        for (IBubbleProviderColored dimension : bubbleProviders)
//        {
//            TileEntity tile = (TileEntity) dimension;
//            if (!dimension.getBubbleVisible())
//            {
//                continue;
//            }
//
//            GL11.glPushMatrix();
//
//            float x = (float) (tile.getPos().getX() - interpPosX);
//            float y = (float) (tile.getPos().getY() - interpPosY);
//            float z = (float) (tile.getPos().getZ() - interpPosZ);
//
//            GL11.glTranslatef(x + 0.5F, y + 1.0F, z + 0.5F);
//            GL11.glScalef(dimension.getBubbleSize(), dimension.getBubbleSize(), dimension.getBubbleSize());
//
//            Vector3 colorVec = dimension.getColor();
//            int color = ColorUtil.to32BitColor(30, (int) (colorVec.z * 255), (int) (colorVec.y * 255), (int) (colorVec.x * 255));
//            ClientUtil.drawBakedModelColored(sphere, color);
//
//            GL11.glPopMatrix();
//        }
//
//        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, lightMapSaveX, lightMapSaveY);
//        GlStateManager.popMatrix();
//    }
//}
