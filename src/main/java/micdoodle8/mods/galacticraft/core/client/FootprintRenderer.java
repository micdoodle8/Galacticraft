package micdoodle8.mods.galacticraft.core.client;

import cpw.mods.fml.client.FMLClientHandler;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.wrappers.Footprint;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FootprintRenderer
{
    public Map<Long, List<Footprint>> footprints = new HashMap<Long, List<Footprint>>();
    private static final ResourceLocation footprintTexture = new ResourceLocation(GalacticraftCore.ASSET_PREFIX, "textures/misc/footprint.png");

    public void renderFootprints(EntityPlayer player, float partialTicks)
    {
        GL11.glPushMatrix();
        double interpPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double interpPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double interpPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(FootprintRenderer.footprintTexture);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        Tessellator tessellator = Tessellator.instance;
        float f7 = 1.0F;
        float f6 = 0.0F;
        float f8 = 0.0F;
        float f9 = 1.0F;

        float f10 = 0.4F;
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);

        for (List<Footprint> footprintList : this.footprints.values())
        {
            for (Footprint footprint : footprintList)
            {
                if (footprint.dimension == player.worldObj.provider.dimensionId)
                {
                    GL11.glPushMatrix();
                    float ageScale = footprint.age / (float) Footprint.MAX_AGE;
                    tessellator.startDrawingQuads();

                    float f11 = (float) (footprint.position.x - interpPosX);
                    float f12 = (float) (footprint.position.y - interpPosY) + 0.001F;
                    float f13 = (float) (footprint.position.z - interpPosZ);

                    GL11.glTranslatef(f11, f12, f13);

                    tessellator.setBrightness((int) (100 + ageScale * 155));
                    GL11.glColor4f(1 - ageScale, 1 - ageScale, 1 - ageScale, 1 - ageScale);
                    double footprintScale = 0.5F;
                    tessellator.addVertexWithUV(0 + Math.sin((45 - footprint.rotation) * Math.PI / 180.0D) * footprintScale, 0, 0 + Math.cos((45 - footprint.rotation) * Math.PI / 180.0D) * footprintScale, f7, f9);
                    tessellator.addVertexWithUV(0 + Math.sin((135 - footprint.rotation) * Math.PI / 180.0D) * footprintScale, 0, 0 + Math.cos((135 - footprint.rotation) * Math.PI / 180.0D) * footprintScale, f7, f8);
                    tessellator.addVertexWithUV(0 + Math.sin((225 - footprint.rotation) * Math.PI / 180.0D) * footprintScale, 0, 0 + Math.cos((225 - footprint.rotation) * Math.PI / 180.0D) * footprintScale, f6, f8);
                    tessellator.addVertexWithUV(0 + Math.sin((315 - footprint.rotation) * Math.PI / 180.0D) * footprintScale, 0, 0 + Math.cos((315 - footprint.rotation) * Math.PI / 180.0D) * footprintScale, f6, f9);

                    tessellator.draw();
                    GL11.glPopMatrix();
                }
            }
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }

    public void addFootprint(long chunkKey, Footprint footprint)
    {
        List<Footprint> footprintList = this.footprints.get(chunkKey);

        if (footprintList == null)
        {
            footprintList = new ArrayList<Footprint>();
        }

        footprintList.add(new Footprint(footprint.dimension, footprint.position, footprint.rotation, footprint.owner));
        this.footprints.put(chunkKey, footprintList);
    }

    public void addFootprint(long chunkKey, int dimension, Vector3 position, float rotation, String owner)
    {
        this.addFootprint(chunkKey, new Footprint(dimension, position, rotation, owner));
    }

    public void setFootprints(long chunkKey, List<Footprint> prints)
    {
        List<Footprint> footprintList = this.footprints.get(chunkKey);

        if (footprintList == null)
        {
            footprintList = new ArrayList<Footprint>();
        }
        
        Iterator<Footprint> i = footprintList.iterator();
        while (i.hasNext())
        {
        	Footprint print = i.next();
        	if (!print.owner.equals(FMLClientHandler.instance().getClient().thePlayer.getCommandSenderName()))
        	{
        		i.remove();
        	}
        }

        footprintList.addAll(prints);
        this.footprints.put(chunkKey, footprintList);
    }
}
