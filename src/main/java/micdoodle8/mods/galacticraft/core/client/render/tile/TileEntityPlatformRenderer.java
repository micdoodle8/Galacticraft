//package micdoodle8.mods.galacticraft.core.client.render.tile;
//
//import com.mojang.blaze3d.platform.GLX;
//import com.mojang.blaze3d.systems.RenderSystem;
//import micdoodle8.mods.galacticraft.core.Constants;
//import micdoodle8.mods.galacticraft.core.GCBlocks;
//import micdoodle8.mods.galacticraft.core.blocks.BlockPlatform;
//import micdoodle8.mods.galacticraft.core.tile.TileEntityPlatform;
//import net.minecraft.block.BlockState;
//import net.minecraft.client.renderer.BufferBuilder;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.client.renderer.model.ModelRenderer;
//import net.minecraft.client.renderer.model.Model;
//import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
//import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import org.lwjgl.opengl.GL11;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@OnlyIn(Dist.CLIENT)
//public class TileEntityPlatformRenderer extends TileEntityRenderer<TileEntityPlatform>
//{
//    public class ModelPlatform extends Model
//    {
//        ModelRenderer panelMain;
//
//        public ModelPlatform()
//        {
//            this(0.0F);
//        }
//
//        public ModelPlatform(float var1)
//        {
//            this.textureWidth = 128;
//            this.textureHeight = 128;
//            this.panelMain = new ModelRenderer(this, 0, 0);
//            this.panelMain.addBox(-22F, -3.5F, -22F, 44, 7, 44);
//            this.panelMain.setRotationPoint(0F, 0F, 0F);
//            this.panelMain.setTextureSize(128, 128);
//            this.panelMain.mirror = true;
//            this.setRotation(this.panelMain, 0F, 0F, 0F);
//        }
//
//        private void setRotation(ModelRenderer model, float x, float y, float z)
//        {
//            model.rotateAngleX = x;
//            model.rotateAngleY = y;
//            model.rotateAngleZ = z;
//        }
//
//        public void render()
//        {
//            this.panelMain.render(1.125F / 44F);
//        }
//    }
//
//    public static final ResourceLocation platformTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/platform_moving.png");
//    public static final ResourceLocation lightTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/misc/light.png");
//    private final ModelPlatform platform = new ModelPlatform();
//    private static final Map<Integer, Float> lastYMap = new HashMap<>();
//    private static float lastPartialTicks = -1F;
//
//    @Override
////    public void render(TileEntityPlatform tileEntity, double d, double d1, double d2, float f, int par9, float alpha)
//    public void render(TileEntityPlatform tileEntity, double x, double y, double z, float partialTicks, int destroyStage)
//    {
//        if (partialTicks != lastPartialTicks)
//        {
//            lastPartialTicks = partialTicks;
//            lastYMap.clear();
//        }
//        BlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
//        float yOffset = tileEntity.getYOffset(partialTicks);
//        if (state.getBlock() == GCBlocks.platform && state.get(BlockPlatform.CORNER) == BlockPlatform.EnumCorner.NW)
//        {
//            RenderSystem.pushMatrix();
//            RenderSystem.translatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
//            RenderSystem.rotatef(90F, 0, 1F, 0F);
//            RenderSystem.translatef(-0.5F, -0.5F, 0.5F);
//            RenderHelper.disableStandardItemLighting();
//            RenderSystem.disableRescaleNormal();
//
//            // Render a moving platform
//            boolean renderPlatformForThisTE = false;
//            float newY = (float) y + yOffset;
//            int xz = tenLSB(tileEntity.getPos().getX()) << 10;
//            xz += tenLSB(tileEntity.getPos().getZ());
//            Float lastYF = lastYMap.get(xz);
//            float lastY = lastYF == null ? -1 : lastYF;
//            if (!tileEntity.isMoving() || Math.abs(newY - lastY) > 0.001F || Math.abs(partialTicks - lastPartialTicks) > 0.001F)
//            {
//                renderPlatformForThisTE = true;
//                lastYMap.put(xz, newY);
//                RenderSystem.pushMatrix();
//                if (tileEntity.isMoving())
//                {
//                    RenderSystem.glMultiTexCoord2f(33985, tileEntity.getMeanLightX(yOffset), tileEntity.getMeanLightZ(yOffset));
//                }
//                else
//                {
//                    int light = tileEntity.getBlendedLight();
//                    RenderSystem.glMultiTexCoord2f(33985, (float) (light % 65536), (float) (light / 65536));
//                }
//                RenderSystem.translatef(0F, 0.79F + yOffset, 0F);
//                this.bindTexture(TileEntityPlatformRenderer.platformTexture);
//                this.platform.render();
//                RenderSystem.popMatrix();
//            }
//
//            if (tileEntity.lightEnabled() || tileEntity.isMoving())
//            {
//                RenderSystem.glMultiTexCoord2f(33985, 240.0F, 240.0F);
//                RenderSystem.disableLighting();
//                RenderSystem.blendFunc(770, 771);
//                RenderSystem.disableTexture();
//
//                this.bindTexture(TileEntityPlatformRenderer.lightTexture);
//                final Tessellator tess = Tessellator.getInstance();
//                BufferBuilder worldRenderer = tess.getBuffer();
//                float frameA, frameB, frameC, frameD;
//
//                // Draw the moving platform LogicalSide-lights
//                if (tileEntity.isMoving() && renderPlatformForThisTE)
//                {
//                    RenderSystem.pushMatrix();
//                    RenderSystem.translatef(0F, 0.26F + yOffset, 0F);
//                    RenderSystem.color4f(1.0F, 0.84F, 65F / 255F, 1.0F);
//
//                    float frameRadius = 1.126F / 2F;
//                    frameB = frameRadius - 0.04F;
//                    frameC = -frameB;
//                    frameA = 0.552F;
//                    frameD = 0.578F;
//                    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//                    worldRenderer.pos(-frameRadius, frameA, frameB).endVertex();
//                    worldRenderer.pos(-frameRadius, frameD, frameB).endVertex();
//                    worldRenderer.pos(-frameRadius, frameD, frameC).endVertex();
//                    worldRenderer.pos(-frameRadius, frameA, frameC).endVertex();
//                    tess.draw();
//                    frameA = 0.482F;
//                    frameD = 0.504666F;
//                    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//                    worldRenderer.pos(-frameRadius, frameA, frameB).endVertex();
//                    worldRenderer.pos(-frameRadius, frameD, frameB).endVertex();
//                    worldRenderer.pos(-frameRadius, frameD, frameC).endVertex();
//                    worldRenderer.pos(-frameRadius, frameA, frameC).endVertex();
//                    tess.draw();
//                    RenderSystem.rotatef(90F, 0, 1F, 0F);
//                    frameA = 0.552F;
//                    frameD = 0.578F;
//                    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//                    worldRenderer.pos(-frameRadius, frameA, frameB).endVertex();
//                    worldRenderer.pos(-frameRadius, frameD, frameB).endVertex();
//                    worldRenderer.pos(-frameRadius, frameD, frameC).endVertex();
//                    worldRenderer.pos(-frameRadius, frameA, frameC).endVertex();
//                    tess.draw();
//                    frameA = 0.482F;
//                    frameD = 0.504666F;
//                    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//                    worldRenderer.pos(-frameRadius, frameA, frameB).endVertex();
//                    worldRenderer.pos(-frameRadius, frameD, frameB).endVertex();
//                    worldRenderer.pos(-frameRadius, frameD, frameC).endVertex();
//                    worldRenderer.pos(-frameRadius, frameA, frameC).endVertex();
//                    tess.draw();
//                    RenderSystem.rotatef(90F, 0, 1F, 0F);
//                    frameA = 0.552F;
//                    frameD = 0.578F;
//                    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//                    worldRenderer.pos(-frameRadius, frameA, frameB).endVertex();
//                    worldRenderer.pos(-frameRadius, frameD, frameB).endVertex();
//                    worldRenderer.pos(-frameRadius, frameD, frameC).endVertex();
//                    worldRenderer.pos(-frameRadius, frameA, frameC).endVertex();
//                    tess.draw();
//                    frameA = 0.482F;
//                    frameD = 0.504666F;
//                    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//                    worldRenderer.pos(-frameRadius, frameA, frameB).endVertex();
//                    worldRenderer.pos(-frameRadius, frameD, frameB).endVertex();
//                    worldRenderer.pos(-frameRadius, frameD, frameC).endVertex();
//                    worldRenderer.pos(-frameRadius, frameA, frameC).endVertex();
//                    tess.draw();
//                    RenderSystem.rotatef(90F, 0, 1F, 0F);
//                    frameA = 0.552F;
//                    frameD = 0.578F;
//                    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//                    worldRenderer.pos(-frameRadius, frameA, frameB).endVertex();
//                    worldRenderer.pos(-frameRadius, frameD, frameB).endVertex();
//                    worldRenderer.pos(-frameRadius, frameD, frameC).endVertex();
//                    worldRenderer.pos(-frameRadius, frameA, frameC).endVertex();
//                    tess.draw();
//                    frameA = 0.482F;
//                    frameD = 0.504666F;
//                    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//                    worldRenderer.pos(-frameRadius, frameA, frameB).endVertex();
//                    worldRenderer.pos(-frameRadius, frameD, frameB).endVertex();
//                    worldRenderer.pos(-frameRadius, frameD, frameC).endVertex();
//                    worldRenderer.pos(-frameRadius, frameA, frameC).endVertex();
//                    tess.draw();
//
//                    RenderSystem.popMatrix();
//                }
//
//                // Draw the activation lights
//                if (tileEntity.lightEnabled())
//                {
//                    float greyLevel = 125F / 255F;
//                    switch (tileEntity.lightColor())
//                    {
//                    case 1:
//                        RenderSystem.color4f(1.0F, 115F / 255F, 115F / 255F, 1.0F);
//                        break;
//                    default:
//                        RenderSystem.color4f(greyLevel, 1.0F, greyLevel, 1.0F);
//                    }
//
//                    float frameY = 0.9376F;
//                    frameC = 0.7F;
//                    frameD = 0.58F;
//                    frameB = frameC + 0.02F;
//                    frameA = -frameD;
//                    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//                    worldRenderer.pos(frameA, frameY, frameB).endVertex();
//                    worldRenderer.pos(frameD, frameY, frameB).endVertex();
//                    worldRenderer.pos(frameD, frameY, frameC).endVertex();
//                    worldRenderer.pos(frameA, frameY, frameC).endVertex();
//                    tess.draw();
//                    RenderSystem.rotatef(90F, 0, 1F, 0F);
//                    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//                    worldRenderer.pos(frameA, frameY, frameB).endVertex();
//                    worldRenderer.pos(frameD, frameY, frameB).endVertex();
//                    worldRenderer.pos(frameD, frameY, frameC).endVertex();
//                    worldRenderer.pos(frameA, frameY, frameC).endVertex();
//                    tess.draw();
//                    RenderSystem.rotatef(90F, 0, 1F, 0F);
//                    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//                    worldRenderer.pos(frameA, frameY, frameB).endVertex();
//                    worldRenderer.pos(frameD, frameY, frameB).endVertex();
//                    worldRenderer.pos(frameD, frameY, frameC).endVertex();
//                    worldRenderer.pos(frameA, frameY, frameC).endVertex();
//                    tess.draw();
//                    RenderSystem.rotatef(90F, 0, 1F, 0F);
//                    worldRenderer.begin(7, DefaultVertexFormats.POSITION);
//                    worldRenderer.pos(frameA, frameY, frameB).endVertex();
//                    worldRenderer.pos(frameD, frameY, frameB).endVertex();
//                    worldRenderer.pos(frameD, frameY, frameC).endVertex();
//                    worldRenderer.pos(frameA, frameY, frameC).endVertex();
//                    tess.draw();
//                }
//
//                // Restore the lighting state
//                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//                //? need to undo RenderSystem.glBlendFunc()?
//                RenderSystem.enableLighting();
//                RenderSystem.enableTexture();
////                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
//            }
//
//            RenderSystem.enableRescaleNormal();
//            RenderHelper.enableStandardItemLighting();
//            RenderSystem.popMatrix();
//        }
//    }
//
//    private int tenLSB(int x)
//    {
//        if (x < 0)
//        {
//            return x % 1024 + 1024;
//        }
//
//        return x % 1024;
//    }
//}
