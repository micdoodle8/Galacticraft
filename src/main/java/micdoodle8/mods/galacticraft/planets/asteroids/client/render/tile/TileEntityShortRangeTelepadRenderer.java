package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import micdoodle8.mods.galacticraft.core.client.model.OBJLoaderGC;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityShortRangeTelepad;
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
public class TileEntityShortRangeTelepadRenderer extends TileEntitySpecialRenderer<TileEntityShortRangeTelepad>
{
    private static OBJModel.OBJBakedModel teleporterTop;
    private static OBJModel.OBJBakedModel teleporterBottom;

    private void updateModels()
    {
        if (teleporterTop == null)
        {
            try
            {
                IModel model = OBJLoaderGC.instance.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "block/telepad_short.obj"));
                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());

                teleporterTop = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Top", "Connector"), false), DefaultVertexFormats.ITEM, spriteFunction);
                teleporterBottom = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Bottom"), false), DefaultVertexFormats.ITEM, spriteFunction);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void render(TileEntityShortRangeTelepad te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        GL11.glPushMatrix();

        RenderHelper.disableStandardItemLighting();
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        updateModels();

        GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);

        GL11.glScalef(0.745F, 1.0F, 0.745F);

        ClientUtil.drawBakedModel(teleporterBottom);
        GL11.glTranslatef(0.0F, -0.7F, 0.0F);
        ClientUtil.drawBakedModel(teleporterTop);

        GL11.glPopMatrix();
    }
}
