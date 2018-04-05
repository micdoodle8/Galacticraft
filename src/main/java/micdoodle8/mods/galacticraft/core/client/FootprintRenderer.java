package micdoodle8.mods.galacticraft.core.client;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.gui.overlay.OverlaySensorGlasses;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.FMLClientHandler;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FootprintRenderer
{
    public static Map<Long, List<Footprint>> footprints = new ConcurrentHashMap<Long, List<Footprint>>();
    private static final ResourceLocation footprintTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/misc/footprint.png");

    public static void renderFootprints(EntityPlayer player, float partialTicks)
    {
        int dimActive = GCCoreUtil.getDimensionID(player.worldObj);
        List<Footprint> footprintsToDraw = new LinkedList<>();

        for (List<Footprint> footprintList : footprints.values())
        {
            for (Footprint footprint : footprintList)
            {
                if (footprint.dimension == dimActive)
                {
                    footprintsToDraw.add(footprint);
                }
            }
        }

        if (footprintsToDraw.isEmpty())
        {
            return;
        }

        GlStateManager.pushMatrix();
        double interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(FootprintRenderer.footprintTexture);

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableCull();

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        float f7 = 1.0F;
        float f6 = 0.0F;
        float f8 = 0.0F;
        float f9 = 1.0F;

        float f10 = 0.4F;
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
        boolean sensorGlasses = OverlaySensorGlasses.overrideMobTexture();
        if (sensorGlasses)
        {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        for (Footprint footprint : footprintsToDraw)
        {
            GL11.glPushMatrix();

            if (!sensorGlasses)
            {
                int j = footprint.lightmapVal % 65536;
                int k = footprint.lightmapVal / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
            }

            float ageScale = footprint.age / (float) Footprint.MAX_AGE;
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

            float f11 = (float) (footprint.position.x - interpPosX);
            float f12 = (float) (footprint.position.y - interpPosY) + 0.001F;
            float f13 = (float) (footprint.position.z - interpPosZ);

            GL11.glTranslatef(f11, f12, f13);

            int brightness = (int) (100 + ageScale * 155);
            //                    worldRenderer.putBrightness4(brightness, brightness, brightness, brightness);
            GlStateManager.color(1 - ageScale, 1 - ageScale, 1 - ageScale, 1 - ageScale);
            double footprintScale = 0.5F;
            worldRenderer.pos(Math.sin((45 - footprint.rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale, 0, Math.cos((45 - footprint.rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale).tex(f7, f9).endVertex();
            worldRenderer.pos(Math.sin((135 - footprint.rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale, 0, Math.cos((135 - footprint.rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale).tex(f7, f8).endVertex();
            worldRenderer.pos(Math.sin((225 - footprint.rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale, 0, Math.cos((225 - footprint.rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale).tex(f6, f8).endVertex();
            worldRenderer.pos(Math.sin((315 - footprint.rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale, 0, Math.cos((315 - footprint.rotation) / Constants.RADIANS_TO_DEGREES_D) * footprintScale).tex(f6, f9).endVertex();

            tessellator.draw();
            GlStateManager.popMatrix();
        }

        if (sensorGlasses)
        {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
        }

        GlStateManager.popMatrix();
    }

    public static void addFootprint(long chunkKey, Footprint footprint)
    {
        List<Footprint> footprintList = footprints.get(chunkKey);

        if (footprintList == null)
        {
            footprintList = new ArrayList<Footprint>();
        }

        footprintList.add(new Footprint(footprint.dimension, footprint.position, footprint.rotation, footprint.owner, footprint.lightmapVal));
        footprints.put(chunkKey, footprintList);
    }

    public static void addFootprint(long chunkKey, int dimension, Vector3 position, float rotation, String owner, int lightmapVal)
    {
        addFootprint(chunkKey, new Footprint(dimension, position, rotation, owner, lightmapVal));
    }

    public static void setFootprints(long chunkKey, List<Footprint> prints)
    {
        List<Footprint> footprintList = footprints.get(chunkKey);

        if (footprintList == null)
        {
            footprintList = new ArrayList<Footprint>();
        }

        Iterator<Footprint> i = footprintList.iterator();
        while (i.hasNext())
        {
            Footprint print = i.next();
            if (!print.owner.equals(FMLClientHandler.instance().getClient().thePlayer.getName()))
            {
                i.remove();
            }
        }

        footprintList.addAll(prints);
        footprints.put(chunkKey, footprintList);
    }
}
