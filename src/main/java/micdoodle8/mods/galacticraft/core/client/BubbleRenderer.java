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
//import com.mojang.blaze3d.systems.RenderSystem;
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
//        RenderSystem.pushMatrix();
//
//        RenderSystem.enableRescaleNormal();
//        RenderSystem.enableBlend();
//        RenderSystem.disableLighting();
//        RenderSystem.disableCull();
//
//        RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1F);
//        RenderSystem.blendFunc(770, 771);
//        RenderSystem.matrixMode(GL11.GL_TEXTURE);
//        RenderSystem.loadIdentity();
//        RenderSystem.matrixMode(5888);
//        RenderSystem.depthMask(false);
//        float lightMapSaveX = GLX.lastBrightnessX;
//        float lightMapSaveY = GLX.lastBrightnessY;
//        RenderSystem.glMultiTexCoord2f(33985, 240.0F, 240.0F);
//
//        for (IBubbleProviderColored dimension : bubbleProviders)
//        {
//            TileEntity tile = (TileEntity) dimension;
//            if (!dimension.getBubbleVisible())
//            {
//                continue;
//            }
//
//            RenderSystem.pushMatrix();
//
//            float x = (float) (tile.getPos().getX() - interpPosX);
//            float y = (float) (tile.getPos().getY() - interpPosY);
//            float z = (float) (tile.getPos().getZ() - interpPosZ);
//
//            RenderSystem.translatef(x + 0.5F, y + 1.0F, z + 0.5F);
//            RenderSystem.scalef(dimension.getBubbleSize(), dimension.getBubbleSize(), dimension.getBubbleSize());
//
//            Vector3 colorVec = dimension.getColor();
//            int color = ColorUtil.to32BitColor(30, (int) (colorVec.z * 255), (int) (colorVec.y * 255), (int) (colorVec.x * 255));
//            ClientUtil.drawBakedModelColored(sphere, color);
//
//            RenderSystem.popMatrix();
//        }
//
//        RenderSystem.glMultiTexCoord2f(33985, lightMapSaveX, lightMapSaveY);
//        RenderSystem.popMatrix();
//    }
//}
