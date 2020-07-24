//package micdoodle8.mods.galacticraft.planets.asteroids.client.render.tile;
//
//import com.google.common.collect.ImmutableList;
//import com.mojang.blaze3d.systems.RenderSystem;
//import micdoodle8.mods.galacticraft.core.tile.ReceiverMode;
//import micdoodle8.mods.galacticraft.core.util.ClientUtil;
//import micdoodle8.mods.galacticraft.core.util.ColorUtil;
//import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
//import micdoodle8.mods.galacticraft.planets.asteroids.tile.TileEntityBeamReceiver;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.texture.AtlasTexture;
//import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.client.model.ModelLoader;
//import net.minecraftforge.client.model.obj.OBJModel;
//import org.lwjgl.opengl.GL11;
//
//@OnlyIn(Dist.CLIENT)
//public class TileEntityBeamReceiverRenderer extends TileEntityRenderer<TileEntityBeamReceiver>
//{
//    private static OBJModel.OBJBakedModel reflectorModelMain;
//    private static OBJModel.OBJBakedModel reflectorModelReceiver;
//    private static OBJModel.OBJBakedModel reflectorModelRing;
//
//    public static void updateModels(ModelLoader modelLoader)
//    {
//        try
//        {
//            reflectorModelMain = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "block/receiver.obj"), ImmutableList.of("Main"));
//            reflectorModelReceiver = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "block/receiver.obj"), ImmutableList.of("Receiver"));
//            reflectorModelRing = ClientUtil.modelFromOBJ(modelLoader, new ResourceLocation(GalacticraftPlanets.ASSET_PREFIX, "block/receiver.obj"), ImmutableList.of("Ring"));
//        }
//        catch (Exception e)
//        {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public void render(TileEntityBeamReceiver tile, double x, double y, double z, float partialTicks, int destroyStage)
//    {
//        if (tile.facing == null)
//        {
//            return;
//        }
//
//        RenderSystem.disableRescaleNormal();
//        RenderSystem.pushMatrix();
//        RenderSystem.translatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
//        RenderSystem.scalef(0.85F, 0.85F, 0.85F);
//
//        switch (tile.facing)
//        {
//        case DOWN:
//            RenderSystem.translatef(0.7F, -0.15F, 0.0F);
//            RenderSystem.rotatef(90, 0, 0, 1);
//            break;
//        case UP:
//            RenderSystem.translatef(-0.7F, 1.3F, 0.0F);
//            RenderSystem.rotatef(-90, 0, 0, 1);
//            break;
//        case EAST:
//            RenderSystem.translatef(0.7F, -0.15F, 0.0F);
//            RenderSystem.rotatef(180, 0, 1, 0);
//            break;
//        case SOUTH:
//            RenderSystem.translatef(0.0F, -0.15F, 0.7F);
//            RenderSystem.rotatef(90, 0, 1, 0);
//            break;
//        case WEST:
//            RenderSystem.translatef(-0.7F, -0.15F, 0.0F);
//            RenderSystem.rotatef(0, 0, 1, 0);
//            break;
//        case NORTH:
//            RenderSystem.translatef(0.0F, -0.15F, -0.7F);
//            RenderSystem.rotatef(270, 0, 1, 0);
//            break;
//        default:
//            RenderSystem.popMatrix();
//            return;
//        }
//
//        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
//
//        if (Minecraft.isAmbientOcclusionEnabled())
//        {
//            RenderSystem.shadeModel(7425);
//        }
//        else
//        {
//            RenderSystem.shadeModel(7424);
//        }
//
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ClientUtil.drawBakedModel(reflectorModelMain);
//
//        int color;
//
//        if (tile.modeReceive == ReceiverMode.RECEIVE.ordinal())
//        {
//            color = ColorUtil.to32BitColor(255, 0, 204, 0);
//        }
//        else if (tile.modeReceive == ReceiverMode.EXTRACT.ordinal())
//        {
//            color = ColorUtil.to32BitColor(255, 0, 0, 153);
//        }
//        else
//        {
//            color = ColorUtil.to32BitColor(255, 25, 25, 25);
//        }
//
//        RenderSystem.disableTexture();
//        RenderSystem.disableCull();
//        ClientUtil.drawBakedModelColored(reflectorModelReceiver, color);
//        RenderSystem.enableTexture();
//        RenderSystem.enableCull();
//        float dX = 0.34772F;
//        float dY = 0.75097F;
//        float dZ = 0.0F;
//        RenderSystem.translatef(dX, dY, dZ);
//
//        if (tile.modeReceive != ReceiverMode.UNDEFINED.ordinal())
//        {
//            RenderSystem.rotatef(-tile.ticks * 50, 1, 0, 0);
//        }
//
//        RenderSystem.translatef(-dX, -dY, -dZ);
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        ClientUtil.drawBakedModel(reflectorModelRing);
//        RenderSystem.popMatrix();
//        RenderHelper.enableStandardItemLighting();
//    }
//}
