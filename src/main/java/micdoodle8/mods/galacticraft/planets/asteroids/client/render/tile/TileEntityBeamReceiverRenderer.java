package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.tile.ReceiverMode;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReceiver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class TileEntityBeamReceiverRenderer extends TileEntityRenderer<TileEntityBeamReceiver>
{
    private static IBakedModel reflectorModelMain;
    private static IBakedModel reflectorModelReceiver;
    private static IBakedModel reflectorModelRing;

    public TileEntityBeamReceiverRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
        GCModelCache.INSTANCE.reloadCallback(this::updateModels);
    }

    private void updateModels()
    {
        reflectorModelMain = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/block/receiver.obj"), ImmutableList.of("Main"));
        reflectorModelReceiver = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/block/receiver.obj"), ImmutableList.of("Receiver"));
        reflectorModelRing = GCModelCache.INSTANCE.getModel(new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "models/block/receiver.obj"), ImmutableList.of("Ring"));
    }

    @Override
    public void render(TileEntityBeamReceiver tile, float partialTicks, MatrixStack matStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        if (tile.facing == null)
        {
            return;
        }

        GlStateManager.disableRescaleNormal();
        matStack.push();
        matStack.translate(0.5F, 0.0F, 0.5F);
        matStack.scale(0.85F, 0.85F, 0.85F);
//        GlStateManager.translatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
//        GlStateManager.scalef(0.85F, 0.85F, 0.85F);

        switch (tile.facing)
        {
        case DOWN:
            matStack.translate(0.7F, -0.15F, 0.0F);
            matStack.rotate(new Quaternion(new Vector3f(0, 0, 1), 90, true));
            break;
        case UP:
            matStack.translate(-0.7F, 1.3F, 0.0F);
            matStack.rotate(new Quaternion(new Vector3f(0, 0, 1), -90, true));
            break;
        case EAST:
            matStack.translate(0.7F, -0.15F, 0.0F);
            matStack.rotate(new Quaternion(new Vector3f(0, 1, 0), 180, true));
            break;
        case SOUTH:
            matStack.translate(0.0F, -0.15F, 0.7F);
            matStack.rotate(new Quaternion(new Vector3f(0, 1, 0), 90, true));
            break;
        case WEST:
            matStack.translate(-0.7F, -0.15F, 0.0F);
            break;
        case NORTH:
            matStack.translate(0.0F, -0.15F, -0.7F);
            matStack.rotate(new Quaternion(new Vector3f(0, 1, 0), 270, true));
            break;
        default:
            matStack.pop();
            return;
        }

//        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        }
        else
        {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

//        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtil.drawBakedModel(reflectorModelMain, bufferIn, matStack, combinedLightIn);

        GlStateManager.disableTexture();
        GlStateManager.disableCull();

        if (tile.modeReceive == ReceiverMode.RECEIVE.ordinal())
        {
            ClientUtil.drawBakedModelColored(reflectorModelReceiver, bufferIn, matStack, combinedLightIn, 0.0F, 0.8F, 0.0F, 1.0F);
        }
        else if (tile.modeReceive == ReceiverMode.EXTRACT.ordinal())
        {
            ClientUtil.drawBakedModelColored(reflectorModelReceiver, bufferIn, matStack, combinedLightIn, 0.0F, 0.0F, 0.6F, 1.0F);
        }
        else
        {
            ClientUtil.drawBakedModelColored(reflectorModelReceiver, bufferIn, matStack, combinedLightIn, 0.1F, 0.1F, 0.1F, 1.0F);
        }

        GlStateManager.enableTexture();
        GlStateManager.enableCull();
        float dX = 0.34772F;
        float dY = 0.75097F;
        float dZ = 0.0F;
        matStack.translate(dX, dY, dZ);
//        GlStateManager.translatef(dX, dY, dZ);

        if (tile.modeReceive != ReceiverMode.UNDEFINED.ordinal())
        {
            matStack.rotate(new Quaternion(new Vector3f(1, 0, 0), -tile.ticks * 50.0F, true));
//            GlStateManager.rotatef(-tile.ticks * 50, 1, 0, 0);
        }

        matStack.translate(-dX, -dY, -dZ);
//        GlStateManager.translatef(-dX, -dY, -dZ);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        ClientUtil.drawBakedModel(reflectorModelRing, bufferIn, matStack, combinedLightIn);
        matStack.pop();
//        GlStateManager.popMatrix();
        RenderHelper.enableStandardItemLighting();
    }
}
