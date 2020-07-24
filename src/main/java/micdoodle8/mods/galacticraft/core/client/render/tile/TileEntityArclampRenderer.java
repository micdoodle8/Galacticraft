//package micdoodle8.mods.galacticraft.core.client.render.tile;
//
//import com.mojang.blaze3d.platform.GLX;
//import com.mojang.blaze3d.systems.RenderSystem;
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.blocks.BlockBrightLamp;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityArclamp;
//import micdoodle8.mods.galacticraft.core.util.ClientUtil;
//import net.minecraft.client.renderer.BufferBuilder;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.client.renderer.model.IBakedModel;
//import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import org.lwjgl.opengl.GL11;
//
//@OnlyIn(Dist.CLIENT)
//public class TileEntityArclampRenderer extends TileEntityRenderer<TileEntityArclamp>
//{
//    public static final ResourceLocation lampTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/misc/underoil.png");
//    public static final ResourceLocation lightTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/misc/light.png");
//    public static IBakedModel lampMetal;
//
//    @Override
//    public void render(TileEntityArclamp arclamp, double x, double y, double z, float partialTicks, int destroyStage)
//    {
//        int metaFacing = arclamp.facing;
//
//        RenderSystem.disableRescaleNormal();
//        RenderSystem.pushMatrix();
//        RenderSystem.translatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
//        RenderHelper.enableStandardItemLighting();
//        RenderSystem.enableRescaleNormal();
//
//        switch (arclamp.getBlockState().get(BlockBrightLamp.FACING))
//        {
//        case DOWN:
//            break;
//        case UP:
//            RenderSystem.rotatef(180F, 1F, 0, 0);
//            if (metaFacing < 2)
//            {
//                metaFacing ^= 1;
//            }
//            break;
//        case NORTH:
//            RenderSystem.rotatef(90F, 1F, 0, 0);
//            metaFacing ^= 1;
//            break;
//        case SOUTH:
//            RenderSystem.rotatef(90F, -1F, 0, 0);
//            break;
//        case WEST:
//            RenderSystem.rotatef(90F, 0, 0, -1F);
//            metaFacing -= 2;
//            if (metaFacing < 0)
//            {
//                metaFacing = 1 - metaFacing;
//            }
//            break;
//        case EAST:
//            RenderSystem.rotatef(90F, 0, 0, 1F);
//            metaFacing += 2;
//            if (metaFacing > 3)
//            {
//                metaFacing = 5 - metaFacing;
//            }
//            break;
//        }
//
//        RenderSystem.translatef(0, -0.175F, 0);
//
//        switch (metaFacing)
//        {
//        case 0:
//            break;
//        case 1:
//            RenderSystem.rotatef(180F, 0, 1F, 0);
//            break;
//        case 2:
//            RenderSystem.rotatef(90F, 0, 1F, 0);
//            break;
//        case 3:
//            RenderSystem.rotatef(270F, 0, 1F, 0);
//            break;
//        }
//
//        this.bindTexture(TileEntityArclampRenderer.lampTexture);
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.rotatef(45F, -1F, 0, 0);
//        RenderSystem.scalef(0.048F, 0.048F, 0.048F);
//        ClientUtil.drawBakedModel(TileEntityArclampRenderer.lampMetal);
//        RenderHelper.disableStandardItemLighting();
//
//        float greyLevel = arclamp.getEnabled() ? 1.0F : 26F / 255F;
//        //Save the lighting state
//        RenderSystem.glMultiTexCoord2f(33985, 240.0F, 240.0F);
//        RenderSystem.disableLighting();
//
//        this.bindTexture(TileEntityArclampRenderer.lightTexture);
//        RenderSystem.blendFunc(770, 771);
//        RenderSystem.disableTexture();
//        final Tessellator tess = Tessellator.getInstance();
//        BufferBuilder worldRenderer = tess.getBuffer();
//        RenderSystem.color4f(greyLevel, greyLevel, greyLevel, 1.0F);
//        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//        float frameA = -3.4331F;  //These co-ordinates came originally from arclamp_light.obj model
//        float frameB = -frameA;  //These co-ordinates came originally from arclamp_light.obj model
//        float frameY = 2.3703F;  //These co-ordinates came originally from arclamp_light.obj model
//        worldRenderer.pos(frameA, frameY, frameB).endVertex();
//        worldRenderer.pos(frameB, frameY, frameB).endVertex();
//        worldRenderer.pos(frameB, frameY, frameA).endVertex();
//        worldRenderer.pos(frameA, frameY, frameA).endVertex();
//        tess.draw();
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.enableTexture();
//        //? need to undo RenderSystem.glBlendFunc()?
//
//        //Restore the lighting state
//        RenderSystem.enableLighting();
////        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
//        RenderSystem.popMatrix();
//    }
//}
