package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelDataManager;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class TileEntityMinerBaseRenderer extends TileEntityRenderer<TileEntityMinerBase>
{
    private static OBJModel.OBJBakedModel minerBaseModelBaked;

    public static void updateModels(ModelLoader modelLoader)
    {
        try
        {
            minerBaseModelBaked = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "minerbase0.obj"), ImmutableList.of("dock"), TRSRTransformation.identity(), ImmutableMap.of("ambient", "false"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void render(TileEntityMinerBase tile, double x, double y, double z, float partialTicks, int destroyStage)
    {
        if (!tile.isMaster)
        {
            return;
        }

        int j = 0, k = 0;
        int light = tile.getWorld().getCombinedLight(tile.getPos(), 0);
        j += light % 65536;
        k += light / 65536;
        light = tile.getWorld().getCombinedLight(tile.getPos().add(1, 0, 0), 0);
        j += light % 65536;
        k += light / 65536;
        light = tile.getWorld().getCombinedLight(tile.getPos().add(0, 0, 1), 0);
        j += light % 65536;
        k += light / 65536;
        light = tile.getWorld().getCombinedLight(tile.getPos().add(1, 0, 1), 0);
        j += light % 65536;
        k += light / 65536;
        light = tile.getWorld().getCombinedLight(tile.getPos().up(), 0);
        j += light % 65536;
        k += light / 65536;
        light = tile.getWorld().getCombinedLight(tile.getPos().add(1, 1, 0), 0);
        j += light % 65536;
        k += light / 65536;
        light = tile.getWorld().getCombinedLight(tile.getPos().add(0, 1, 1), 0);
        j += light % 65536;
        k += light / 65536;
        light = tile.getWorld().getCombinedLight(tile.getPos().add(1, 1, 1), 0);
        j += light % 65536;
        k += light / 65536;
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float) j / 8.0F, (float) k / 8.0F);
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 8.0F, k / 8.0F);

        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glTranslatef((float) x + 1F, (float) y + 1F, (float) z + 1F);
        GL11.glScalef(0.05F, 0.05F, 0.05F);

        switch (tile.facing)
        {
        case SOUTH:
            GL11.glRotatef(180F, 0, 1F, 0);
            break;
        case WEST:
            GL11.glRotatef(90F, 0, 1F, 0);
            break;
        case NORTH:
            break;
        case EAST:
            GL11.glRotatef(270F, 0, 1F, 0);
            break;
        }

        RenderHelper.disableStandardItemLighting();
        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        GlStateManager.translatef(-tile.getPos().getX(), -tile.getPos().getY(), -tile.getPos().getZ());
        IModelData data = minerBaseModelBaked.getModelData(tile.getWorld(), tile.getPos(), tile.getBlockState(), ModelDataManager.getModelData(tile.getWorld(), tile.getPos()));
        Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(tile.getWorld(), minerBaseModelBaked, tile.getWorld().getBlockState(tile.getPos()), tile.getPos(), tessellator.getBuffer(), false, new Random(), 42, data);
        tessellator.draw();

        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
    }
}
