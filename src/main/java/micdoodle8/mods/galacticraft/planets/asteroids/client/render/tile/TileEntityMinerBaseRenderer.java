package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import micdoodle8.mods.galacticraft.core.client.model.OBJLoaderGC;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityMinerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityMinerBaseRenderer extends TileEntitySpecialRenderer<TileEntityMinerBase>
{
    public static OBJModel.OBJBakedModel minerBaseModelBaked;

    public IBakedModel getBakedModel()
    {
        if (minerBaseModelBaked == null)
        {
            OBJModel minerBaseModel;
            try
            {
                minerBaseModel = (OBJModel) OBJLoaderGC.instance.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "minerbase0.obj"));
                minerBaseModel = (OBJModel) minerBaseModel.process(ImmutableMap.of("ambient", "false"));
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
            Function<ResourceLocation, TextureAtlasSprite> spriteFunction = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
            minerBaseModelBaked = (OBJModel.OBJBakedModel) minerBaseModel.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, spriteFunction);
        }
        return minerBaseModelBaked;
    }

    @Override
    public void render(TileEntityMinerBase tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
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
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 8.0F, k / 8.0F);

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
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        GlStateManager.translate(-tile.getPos().getX(), -tile.getPos().getY(), -tile.getPos().getZ());
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(tile.getWorld(), getBakedModel(), tile.getWorld().getBlockState(tile.getPos()), tile.getPos(), tessellator.getBuffer(), false);
        tessellator.draw();

        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
    }
}
