package micdoodle8.mods.galacticraft.core.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.entities.IBubbleProviderColored;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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
        if (sphere == null)
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
    }

    public static void renderBubbles(EntityPlayer player, float partialTicks)
    {
        double interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        updateModels();

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glDepthMask(false);
        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);

        for (IBubbleProviderColored provider : bubbleProviders)
        {
            TileEntity tile = (TileEntity) provider;
            if (!provider.getBubbleVisible())
            {
                return;
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

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glDepthMask(true);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
    }
}
