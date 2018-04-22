package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import micdoodle8.mods.galacticraft.core.client.model.OBJLoaderGC;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReflector;
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
public class TileEntityBeamReflectorRenderer extends TileEntitySpecialRenderer<TileEntityBeamReflector>
{
    private static OBJModel.OBJBakedModel reflectorModelBase;
    private static OBJModel.OBJBakedModel reflectorModelAxle;
    private static OBJModel.OBJBakedModel reflectorModelEnergyBlaster;
    private static OBJModel.OBJBakedModel reflectorModelRing;

    private void updateModels()
    {
        if (reflectorModelBase == null)
        {
            try
            {
                IModel model = OBJLoaderGC.instance.loadModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "block/reflector.obj"));
                Function<ResourceLocation, TextureAtlasSprite> spriteFunction = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());

                reflectorModelBase = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Base"), false), DefaultVertexFormats.ITEM, spriteFunction);
                reflectorModelAxle = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Axle"), false), DefaultVertexFormats.ITEM, spriteFunction);
                reflectorModelEnergyBlaster = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("EnergyBlaster"), false), DefaultVertexFormats.ITEM, spriteFunction);
                reflectorModelRing = (OBJModel.OBJBakedModel) model.bake(new OBJModel.OBJState(ImmutableList.of("Ring"), false), DefaultVertexFormats.ITEM, spriteFunction);
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void render(TileEntityBeamReflector tile, double d, double d1, double d2, float f, int i, float alpha)
    {
        GlStateManager.disableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) d + 0.5F, (float) d1, (float) d2 + 0.5F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        this.updateModels();
        ClientUtil.drawBakedModel(reflectorModelBase);
        GlStateManager.rotate(tile.yaw, 0, 1, 0);
        ClientUtil.drawBakedModel(reflectorModelAxle);
        float dX = 0.0F;
        float dY = 1.13228F;
        float dZ = 0.0F;
        GlStateManager.translate(dX, dY, dZ);
        GlStateManager.rotate(tile.pitch, 1, 0, 0);
        GlStateManager.translate(-dX, -dY, -dZ);
        ClientUtil.drawBakedModel(reflectorModelEnergyBlaster);
        GlStateManager.translate(dX, dY, dZ);
        GlStateManager.rotate(tile.ticks * 5, 0, 0, 1);
        GlStateManager.translate(-dX, -dY, -dZ);
        ClientUtil.drawBakedModel(reflectorModelRing);
        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();
    }
}
