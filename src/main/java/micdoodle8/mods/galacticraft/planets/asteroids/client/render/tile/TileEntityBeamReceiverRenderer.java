package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import micdoodle8.mods.galacticraft.core.client.model.OBJLoaderGC;
import micdoodle8.mods.galacticraft.core.tile.ReceiverMode;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.ColorUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReceiver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityBeamReceiverRenderer extends TileEntitySpecialRenderer<TileEntityBeamReceiver>
{
    private static OBJModel.OBJBakedModel reflectorModelMain;
    private static OBJModel.OBJBakedModel reflectorModelReceiver;
    private static OBJModel.OBJBakedModel reflectorModelRing;

    private void updateModels()
    {
        if (reflectorModelMain == null)
        {
            try
            {
                IModel model = OBJLoaderGC.instance.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "block/receiver.obj"));
                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());

                reflectorModelMain = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Main"), false), DefaultVertexFormats.ITEM, spriteFunction);
                reflectorModelReceiver = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Receiver"), false), DefaultVertexFormats.ITEM, spriteFunction);
                reflectorModelRing = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Ring"), false), DefaultVertexFormats.ITEM, spriteFunction);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void renderTileEntityAt(TileEntityBeamReceiver tile, double x, double y, double z, float partialTicks, int destroyStage)
    {
        if (tile.facing == null)
        {
            return;
        }

        GL11.glPushMatrix();

        GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
        GL11.glScalef(0.85F, 0.85F, 0.85F);

        switch (tile.facing)
        {
        case DOWN:
            GL11.glTranslatef(0.7F, -0.15F, 0.0F);
            GL11.glRotatef(90, 0, 0, 1);
            break;
        case UP:
            GL11.glTranslatef(-0.7F, 1.3F, 0.0F);
            GL11.glRotatef(-90, 0, 0, 1);
            break;
        case EAST:
            GL11.glTranslatef(0.7F, -0.15F, 0.0F);
            GL11.glRotatef(180, 0, 1, 0);
            break;
        case SOUTH:
            GL11.glTranslatef(0.0F, -0.15F, 0.7F);
            GL11.glRotatef(90, 0, 1, 0);
            break;
        case WEST:
            GL11.glTranslatef(-0.7F, -0.15F, 0.0F);
            GL11.glRotatef(0, 0, 1, 0);
            break;
        case NORTH:
            GL11.glTranslatef(0.0F, -0.15F, -0.7F);
            GL11.glRotatef(270, 0, 1, 0);
            break;
        default:
            GL11.glPopMatrix();
            return;
        }

        updateModels();

        RenderHelper.disableStandardItemLighting();
        this.bindTexture(TextureMap.locationBlocksTexture);
        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtil.drawBakedModel(reflectorModelMain);

        int color;

        if (tile.modeReceive == ReceiverMode.RECEIVE.ordinal())
        {
            color = ColorUtil.to32BitColor(255, 0, 204, 0);
        }
        else if (tile.modeReceive == ReceiverMode.EXTRACT.ordinal())
        {
            color = ColorUtil.to32BitColor(255, 0, 0, 153);
        }
        else
        {
            color = ColorUtil.to32BitColor(255, 25, 25, 25);
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);
        ClientUtil.drawBakedModelColored(reflectorModelReceiver, color);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        float dX = 0.34772F;
        float dY = 0.75097F;
        float dZ = 0.0F;
        GL11.glTranslatef(dX, dY, dZ);
        if (tile.modeReceive != ReceiverMode.UNDEFINED.ordinal())
        {
            GL11.glRotatef(-tile.ticks * 50, 1, 0, 0);
        }
        GL11.glTranslatef(-dX, -dY, -dZ);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtil.drawBakedModel(reflectorModelRing);

        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
    }
}
