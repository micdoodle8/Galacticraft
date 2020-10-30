package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReflector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class TileEntityBeamReflectorRenderer extends TileEntityRenderer<TileEntityBeamReflector>
{
    private IBakedModel reflectorModelBase;
    private IBakedModel reflectorModelAxle;
    private IBakedModel reflectorModelEnergyBlaster;
    private IBakedModel reflectorModelRing;

    public TileEntityBeamReflectorRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
        GCModelCache.INSTANCE.reloadCallback(this::updateModels);
    }

    private void updateModels()
    {
        reflectorModelBase = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/block/reflector.obj"), ImmutableList.of("Base"));
        reflectorModelAxle = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/block/reflector.obj"), ImmutableList.of("Axle"));
        reflectorModelEnergyBlaster = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/block/reflector.obj"), ImmutableList.of("EnergyBlaster"));
        reflectorModelRing = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/block/reflector.obj"), ImmutableList.of("Ring"));
    }

    @Override
    public void render(TileEntityBeamReflector tile, float partialTicks, MatrixStack matStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        GlStateManager.disableRescaleNormal();
//        GlStateManager.pushMatrix();
        matStack.push();
//        GlStateManager.translatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
        matStack.translate(0.5F, 0.0F, 0.5F);
//        GlStateManager.scalef(0.5F, 0.5F, 0.5F);
        matStack.scale(0.5F, 0.5F, 0.5F);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        ClientUtil.drawBakedModel(reflectorModelBase, bufferIn, matStack, combinedLightIn);
//        GlStateManager.rotatef(tile.yaw, 0, 1, 0);
        matStack.rotate(new Quaternion(new Vector3f(0, 1, 0), tile.yaw, true));
        ClientUtil.drawBakedModel(reflectorModelAxle, bufferIn, matStack, combinedLightIn);
        float dX = 0.0F;
        float dY = 1.13228F;
        float dZ = 0.0F;
//        GlStateManager.translatef(dX, dY, dZ);
        matStack.translate(dX, dY, dZ);
//        GlStateManager.rotatef(tile.pitch, 1, 0, 0);
        matStack.rotate(new Quaternion(new Vector3f(1, 0, 0), tile.pitch, true));
//        GlStateManager.translatef(-dX, -dY, -dZ);
        matStack.translate(-dX, -dY, -dZ);
        ClientUtil.drawBakedModel(reflectorModelEnergyBlaster, bufferIn, matStack, combinedLightIn);
//        GlStateManager.translatef(dX, dY, dZ);
        matStack.translate(dX, dY, dZ);
//        GlStateManager.rotatef(tile.ticks * 5, 0, 0, 1);
        matStack.rotate(new Quaternion(new Vector3f(0, 0, 1), tile.ticks * 5, true));
//        GlStateManager.translatef(-dX, -dY, -dZ);
        matStack.translate(-dX, -dY, -dZ);
        ClientUtil.drawBakedModel(reflectorModelRing, bufferIn, matStack, combinedLightIn);
//        GlStateManager.popMatrix();
        matStack.pop();
        RenderHelper.enableStandardItemLighting();
    }
}
