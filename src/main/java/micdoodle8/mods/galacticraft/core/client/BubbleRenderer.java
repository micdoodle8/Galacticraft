package micdoodle8.mods.galacticraft.core.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.entities.IBubbleProviderColored;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IFlexibleBakedModel;

import org.lwjgl.opengl.GL11;
import java.util.List;

public class BubbleRenderer
{
    private static IFlexibleBakedModel sphere;
    private static List<IBubbleProviderColored> bubbleProviders = Lists.newArrayList();

    public static void clearBubbles()
    {
        bubbleProviders.clear();
    }

    public static void addBubble(IBubbleProviderColored tile)
    {
        bubbleProviders.add(tile);
    }

    private static void updateModels()
    {
        try
        {
            sphere = ClientUtil.modelFromOBJ(new ResourceLocation(Constants.ASSET_PREFIX, "sphere.obj"), ImmutableList.of("Sphere"));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void renderBubbles(EntityPlayer player, float partialTicks)
    {
        if (bubbleProviders.isEmpty())
        {
            return;
        }

        double interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        if (sphere == null)
        {
            updateModels();
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        GlStateManager.pushMatrix();
        
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();

        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.matrixMode(GL11.GL_TEXTURE);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.depthMask(false);
        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);

        for (IBubbleProviderColored provider : bubbleProviders)
        {
            TileEntity tile = (TileEntity) provider;
            if (!provider.getBubbleVisible())
            {
                continue;
            }

            GL11.glPushMatrix();

            float x = (float) (tile.getPos().getX() - interpPosX);
            float y = (float) (tile.getPos().getY() - interpPosY);
            float z = (float) (tile.getPos().getZ() - interpPosZ);

            GL11.glTranslatef(x + 0.5F, y + 1.0F, z + 0.5F);
            GL11.glScalef(provider.getBubbleSize(), provider.getBubbleSize(), provider.getBubbleSize());

            Vector3 colorVec = provider.getColor();
            int color = ColorUtil.to32BitColor(30, (int)(colorVec.z * 255), (int)(colorVec.y * 255), (int)(colorVec.x * 255));
            ClientUtil.drawBakedModelColored(sphere, color);

            GL11.glPopMatrix();
        }

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
        GlStateManager.popMatrix();
    }
}
