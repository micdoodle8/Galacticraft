package micdoodle8.mods.galacticraft.core.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockPlatform;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPlatform;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class TileEntityPlatformRenderer extends TileEntityRenderer<TileEntityPlatform>
{
    public static class ModelPlatform extends Model
    {
        ModelRenderer panelMain;

        public ModelPlatform()
        {
            super(RenderType::getEntitySolid);
            this.textureWidth = 128;
            this.textureHeight = 128;
            this.panelMain = new ModelRenderer(this, 0, 0);
            this.panelMain.addBox(-22F, -3.5F, -22F, 44, 7, 44);
            this.panelMain.setRotationPoint(0F, 0F, 0F);
            this.panelMain.setTextureSize(128, 128);
            this.panelMain.mirror = true;
            this.setRotation(this.panelMain, 0F, 0F, 0F);
        }

        private void setRotation(ModelRenderer model, float x, float y, float z)
        {
            model.rotateAngleX = x;
            model.rotateAngleY = y;
            model.rotateAngleZ = z;
        }

        @Override
        public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
        {
            matrixStackIn.push();
            matrixStackIn.scale(0.409090909091F, 0.409090909091F, 0.409090909091F);
            this.panelMain.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
            matrixStackIn.pop();
        }
    }

    public static final ResourceLocation platformTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/model/platform_moving.png");
    public static final ResourceLocation lightTexture = new ResourceLocation(Constants.MOD_ID_CORE, "textures/misc/light.png");
    private final ModelPlatform platform = new ModelPlatform();
    private static final Map<Integer, Float> lastYMap = new HashMap<>();
    private static float lastPartialTicks = -1F;
    private static Field yPosField = null;

    public TileEntityPlatformRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
        try
        {
            yPosField = Matrix4f.class.getDeclaredField("m13");
            yPosField.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void render(TileEntityPlatform platform, float partialTicks, MatrixStack matStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
        float currentyPos = 0.0F;
        try
        {
            currentyPos = (float) yPosField.get(matStack.getLast().getMatrix());
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        if (partialTicks != lastPartialTicks)
        {
            lastPartialTicks = partialTicks;
            lastYMap.clear();
        }
        BlockState state = platform.getWorld().getBlockState(platform.getPos());
        float yOffset = platform.getYOffset(partialTicks);
        if (state.getBlock() == GCBlocks.platform && state.get(BlockPlatform.CORNER) == BlockPlatform.EnumCorner.NW)
        {
//            GlStateManager.pushMatrix();
            matStack.push();
//            GlStateManager.translatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);
            matStack.translate(0.5F, 0.5F, 0.5F);
//            GlStateManager.rotatef(90F, 0, 1F, 0F);
            matStack.rotate(new Quaternion(Vector3f.YP, 90.0F, true));
//            GlStateManager.translatef(-0.5F, -0.5F, 0.5F);
            matStack.translate(-0.5F, -0.5F, 0.5F);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();

            // Render a moving platform
            boolean renderPlatformForThisTE = false;
            float newY = currentyPos + yOffset;
            int xz = tenLSB(platform.getPos().getX()) << 10;
            xz += tenLSB(platform.getPos().getZ());
            Float lastYF = lastYMap.get(xz);
            float lastY = lastYF == null ? -1 : lastYF;
            if (!platform.isMoving() || Math.abs(newY - lastY) > 0.001F || Math.abs(partialTicks - lastPartialTicks) > 0.001F)
            {
                renderPlatformForThisTE = true;
                lastYMap.put(xz, newY);
//                GlStateManager.pushMatrix();
                matStack.push();
//                if (platform.isMoving())
//                {
//                    GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, platform.getMeanLightX(yOffset), platform.getMeanLightZ(yOffset));
//                }
//                else
//                {
//                    int light = platform.getBlendedLight();
//                    GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, (float) (light % 65536), (float) (light / 65536));
//                }
//                GlStateManager.translatef(0F, 0.79F + yOffset, 0F);
                matStack.translate(0.0F, 0.79F + yOffset, 0.0F);
//                this.bindTexture(TileEntityPlatformRenderer.platformTexture);
                RenderType renderType = RenderType.getEntitySolid(platformTexture);
                IVertexBuilder builder = bufferIn.getBuffer(renderType);
                this.platform.render(matStack, builder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
//                GlStateManager.popMatrix();
                matStack.pop();
            }

            if (platform.lightEnabled() || platform.isMoving())
            {
//                GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager.disableTexture();

                RenderType renderType = RenderType.getEntitySolid(lightTexture);
                IVertexBuilder builder = bufferIn.getBuffer(renderType);
//                this.bindTexture(TileEntityPlatformRenderer.lightTexture);
//                final Tessellator tess = Tessellator.getInstance();
//                BufferBuilder worldRenderer = tess.getBuffer();
                float frameA, frameB, frameC, frameD;

                // Draw the moving platform LogicalSide-lights
                if (platform.isMoving() && renderPlatformForThisTE)
                {
//                    GlStateManager.pushMatrix();
                    matStack.push();
//                    GlStateManager.translatef(0F, 0.26F + yOffset, 0F);
                    matStack.translate(0.0F, 0.26F + yOffset, 0.0F);
//                    GlStateManager.color4f(1.0F, 0.84F, 65F / 255F, 1.0F);
                    float r = 1.0F;
                    float g = 0.84F;
                    float b = 65F / 255F;

                    float frameRadius = 1.126F / 2F;
                    frameB = frameRadius - 0.04F;
                    frameC = -frameB;
                    frameA = 0.552F;
                    frameD = 0.578F;
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameA, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameD, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameD, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameA, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    frameA = 0.482F;
                    frameD = 0.504666F;
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameA, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameD, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameD, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameA, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
//                    GlStateManager.rotatef(90F, 0, 1F, 0F);
                    matStack.rotate(new Quaternion(Vector3f.YP, 90.0F, true));
                    frameA = 0.552F;
                    frameD = 0.578F;
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameA, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameD, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameD, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameA, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    frameA = 0.482F;
                    frameD = 0.504666F;
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameA, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameD, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameD, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameA, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
//                    GlStateManager.rotatef(90F, 0, 1F, 0F);
                    matStack.rotate(new Quaternion(Vector3f.YP, 90.0F, true));
                    frameA = 0.552F;
                    frameD = 0.578F;
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameA, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameD, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameD, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameA, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    frameA = 0.482F;
                    frameD = 0.504666F;
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameA, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameD, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameD, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameA, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
//                    GlStateManager.rotatef(90F, 0, 1F, 0F);
                    matStack.rotate(new Quaternion(Vector3f.YP, 90.0F, true));
                    frameA = 0.552F;
                    frameD = 0.578F;
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameA, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameD, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameD, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameA, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    frameA = 0.482F;
                    frameD = 0.504666F;
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameA, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameD, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameD, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, -frameRadius, frameA, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);

//                    GlStateManager.popMatrix();
                    matStack.pop();
                }

                // Draw the activation lights
                if (platform.lightEnabled())
                {
                    float greyLevel = 125F / 255F;
                    float r = 0.0F;
                    float g = 0.0F;
                    float b = 0.0F;
                    switch (platform.lightColor())
                    {
                    case 1:
                        r = 1.0F;
                        g = 115F / 255F;
                        b = 115F / 255F;
                        break;
                    default:
                        r = greyLevel;
                        g = 1.0F;
                        b = greyLevel;
                    }

                    float frameY = 0.9376F;
                    frameC = 0.7F;
                    frameD = 0.58F;
                    frameB = frameC + 0.02F;
                    frameA = -frameD;
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, frameA, frameY, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, frameD, frameY, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, frameD, frameY, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, frameA, frameY, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
//                    GlStateManager.rotatef(90F, 0, 1F, 0F);
                    matStack.rotate(new Quaternion(Vector3f.YP, 90.0F, true));
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, frameA, frameY, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, frameD, frameY, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, frameD, frameY, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, frameA, frameY, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
//                    GlStateManager.rotatef(90F, 0, 1F, 0F);
                    matStack.rotate(new Quaternion(Vector3f.YP, 90.0F, true));
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, frameA, frameY, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, frameD, frameY, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, frameD, frameY, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, frameA, frameY, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
//                    GlStateManager.rotatef(90F, 0, 1F, 0F);
                    matStack.rotate(new Quaternion(Vector3f.YP, 90.0F, true));
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, frameA, frameY, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, frameD, frameY, frameB, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, frameD, frameY, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                    addVertex(matStack.getLast().getMatrix(), matStack.getLast().getNormal(), builder, frameA, frameY, frameC, 0.0F, 0.0F, 0, 1, 0, Constants.PACKED_LIGHT_FULL_BRIGHT, r, g, b);
                }

                // Restore the lighting state
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                //? need to undo GlStateManager.glBlendFunc()?
                GlStateManager.enableLighting();
                GlStateManager.enableTexture();
//                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
            }

            GlStateManager.enableRescaleNormal();
            RenderHelper.enableStandardItemLighting();
//            GlStateManager.popMatrix();
            matStack.pop();
        }
    }

    private void addVertex(Matrix4f mat, Matrix3f matNorm, IVertexBuilder builder, float pX, float pY, float pZ, float tX, float tY, int nX, int nY, int nZ, int light) {
        addVertex(mat, matNorm, builder, pX, pY, pZ, tX, tY, nX, nY, nZ, light, 1.0F, 1.0F, 1.0F);
    }

    private void addVertex(Matrix4f mat, Matrix3f matNorm, IVertexBuilder builder, float pX, float pY, float pZ, float tX, float tY, int nX, int nY, int nZ, int light, float r, float g, float b) {
        builder.pos(mat, pX, pY, pZ).color(r, g, b, 1.0F).tex(tX, tY).overlay(OverlayTexture.NO_OVERLAY).lightmap(light).normal(matNorm, (float)nX, (float)nY, (float)nZ).endVertex();
    }

    private int tenLSB(int x)
    {
        if (x < 0)
        {
            return x % 1024 + 1024;
        }

        return x % 1024;
    }
}
