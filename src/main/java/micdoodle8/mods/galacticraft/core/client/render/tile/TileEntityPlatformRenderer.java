package micdoodle8.mods.galacticraft.core.client.render.tile;

import java.util.HashMap;
import java.util.Map;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockPlatform;
import micdoodle8.mods.galacticraft.core.tile.TileEntityPlatform;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.relauncher.Side;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityPlatformRenderer extends TileEntitySpecialRenderer<TileEntityPlatform>
{
    public class ModelPlatform extends ModelBase
    {
        ModelRenderer panelMain;

        public ModelPlatform()
        {
            this(0.0F);
        }

        public ModelPlatform(float var1)
        {
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

        public void render()
        {
            this.panelMain.render(1.125F/44F);
        }
    }

    public static final ResourceLocation platformTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/model/platform_moving.png");
    public static final ResourceLocation lightTexture = new ResourceLocation(Constants.ASSET_PREFIX, "textures/misc/light.png");
    private ModelPlatform platform = new ModelPlatform();
    private static Map<Integer, Float> lastYMap = new HashMap<>();
    private static float lastPartialTicks = -1F;

    @Override
    public void render(TileEntityPlatform tileEntity, double d, double d1, double d2, float f, int par9, float alpha)
    {
        if (f != lastPartialTicks)
        {
            lastPartialTicks = f;
            lastYMap.clear();
        }
        IBlockState b = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        float yOffset = tileEntity.getYOffset(f);
        if (b.getBlock() == GCBlocks.platform && b.getValue(BlockPlatform.CORNER) == BlockPlatform.EnumCorner.NW)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) d + 0.5F, (float) d1 + 0.5F, (float) d2 + 0.5F);
            GlStateManager.rotate(90F, 0, 1F, 0F);
            GlStateManager.translate(-0.5F, -0.5F, 0.5F);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();

            // Render a moving platform
            boolean renderPlatformForThisTE = false;
            float newY = (float) d1 + yOffset;
            int xz = tenLSB(tileEntity.getPos().getX()) << 10;
            xz += tenLSB(tileEntity.getPos().getZ());
            Float lastYF = lastYMap.get(xz);
            float lastY = lastYF == null ? -1 : lastYF;
            if (!tileEntity.isMoving() || Math.abs(newY - lastY) > 0.001F || Math.abs(f - lastPartialTicks) > 0.001F)
            {
                renderPlatformForThisTE = true;
                lastYMap.put(xz, newY);
                GlStateManager.pushMatrix();
                if (tileEntity.isMoving())
                {
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, tileEntity.getMeanLightX(yOffset), tileEntity.getMeanLightZ(yOffset));
                }
                else
                {
                    int light = tileEntity.getBlendedLight();
                    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)(light % 65536), (float)(light / 65536));
                }
                GlStateManager.translate(0F, 0.79F + yOffset, 0F);
                this.bindTexture(TileEntityPlatformRenderer.platformTexture);
                this.platform.render();
                GlStateManager.popMatrix();
            }

            if (tileEntity.lightEnabled() || tileEntity.isMoving())
            {
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager.disableTexture2D();

                this.bindTexture(TileEntityPlatformRenderer.lightTexture);
                final Tessellator tess = Tessellator.getInstance();
                BufferBuilder worldRenderer = tess.getBuffer();
                float frameA, frameB, frameC, frameD;

                // Draw the moving platform side-lights 
                if (tileEntity.isMoving() && renderPlatformForThisTE)
                {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(0F, 0.26F + yOffset, 0F);
                    GlStateManager.color(1.0F, 0.84F, 65F / 255F, 1.0F);

                    float frameRadius = 1.126F / 2F;
                    frameB = frameRadius - 0.04F;
                    frameC = -frameB;
                    frameA = 0.552F;
                    frameD = 0.578F;
                    worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                    worldRenderer.pos(-frameRadius, frameA, frameB).endVertex();
                    worldRenderer.pos(-frameRadius, frameD, frameB).endVertex();
                    worldRenderer.pos(-frameRadius, frameD, frameC).endVertex();
                    worldRenderer.pos(-frameRadius, frameA, frameC).endVertex();
                    tess.draw();
                    frameA = 0.482F;
                    frameD = 0.504666F;
                    worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                    worldRenderer.pos(-frameRadius, frameA, frameB).endVertex();
                    worldRenderer.pos(-frameRadius, frameD, frameB).endVertex();
                    worldRenderer.pos(-frameRadius, frameD, frameC).endVertex();
                    worldRenderer.pos(-frameRadius, frameA, frameC).endVertex();
                    tess.draw();
                    GlStateManager.rotate(90F, 0, 1F, 0F);
                    frameA = 0.552F;
                    frameD = 0.578F;
                    worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                    worldRenderer.pos(-frameRadius, frameA, frameB).endVertex();
                    worldRenderer.pos(-frameRadius, frameD, frameB).endVertex();
                    worldRenderer.pos(-frameRadius, frameD, frameC).endVertex();
                    worldRenderer.pos(-frameRadius, frameA, frameC).endVertex();
                    tess.draw();
                    frameA = 0.482F;
                    frameD = 0.504666F;
                    worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                    worldRenderer.pos(-frameRadius, frameA, frameB).endVertex();
                    worldRenderer.pos(-frameRadius, frameD, frameB).endVertex();
                    worldRenderer.pos(-frameRadius, frameD, frameC).endVertex();
                    worldRenderer.pos(-frameRadius, frameA, frameC).endVertex();
                    tess.draw();
                    GlStateManager.rotate(90F, 0, 1F, 0F);
                    frameA = 0.552F;
                    frameD = 0.578F;
                    worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                    worldRenderer.pos(-frameRadius, frameA, frameB).endVertex();
                    worldRenderer.pos(-frameRadius, frameD, frameB).endVertex();
                    worldRenderer.pos(-frameRadius, frameD, frameC).endVertex();
                    worldRenderer.pos(-frameRadius, frameA, frameC).endVertex();
                    tess.draw();
                    frameA = 0.482F;
                    frameD = 0.504666F;
                    worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                    worldRenderer.pos(-frameRadius, frameA, frameB).endVertex();
                    worldRenderer.pos(-frameRadius, frameD, frameB).endVertex();
                    worldRenderer.pos(-frameRadius, frameD, frameC).endVertex();
                    worldRenderer.pos(-frameRadius, frameA, frameC).endVertex();
                    tess.draw();
                    GlStateManager.rotate(90F, 0, 1F, 0F);
                    frameA = 0.552F;
                    frameD = 0.578F;
                    worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                    worldRenderer.pos(-frameRadius, frameA, frameB).endVertex();
                    worldRenderer.pos(-frameRadius, frameD, frameB).endVertex();
                    worldRenderer.pos(-frameRadius, frameD, frameC).endVertex();
                    worldRenderer.pos(-frameRadius, frameA, frameC).endVertex();
                    tess.draw();
                    frameA = 0.482F;
                    frameD = 0.504666F;
                    worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                    worldRenderer.pos(-frameRadius, frameA, frameB).endVertex();
                    worldRenderer.pos(-frameRadius, frameD, frameB).endVertex();
                    worldRenderer.pos(-frameRadius, frameD, frameC).endVertex();
                    worldRenderer.pos(-frameRadius, frameA, frameC).endVertex();
                    tess.draw();
                    
                    GlStateManager.popMatrix();
                }

                // Draw the activation lights 
                if (tileEntity.lightEnabled())
                {
                    float greyLevel = 125F / 255F;
                    switch (tileEntity.lightColor())
                    {
                    case 1: GlStateManager.color(1.0F, 115F/255F, 115F/255F, 1.0F);
                    break;
                    default: GlStateManager.color(greyLevel, 1.0F, greyLevel, 1.0F);
                    }

                    float frameY = 0.9376F;
                    frameC = 0.7F;
                    frameD = 0.58F;
                    frameB = frameC + 0.02F;
                    frameA = -frameD;
                    worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                    worldRenderer.pos(frameA, frameY, frameB).endVertex();
                    worldRenderer.pos(frameD, frameY, frameB).endVertex();
                    worldRenderer.pos(frameD, frameY, frameC).endVertex();
                    worldRenderer.pos(frameA, frameY, frameC).endVertex();
                    tess.draw();
                    GlStateManager.rotate(90F, 0, 1F, 0F);
                    worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                    worldRenderer.pos(frameA, frameY, frameB).endVertex();
                    worldRenderer.pos(frameD, frameY, frameB).endVertex();
                    worldRenderer.pos(frameD, frameY, frameC).endVertex();
                    worldRenderer.pos(frameA, frameY, frameC).endVertex();
                    tess.draw();
                    GlStateManager.rotate(90F, 0, 1F, 0F);
                    worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                    worldRenderer.pos(frameA, frameY, frameB).endVertex();
                    worldRenderer.pos(frameD, frameY, frameB).endVertex();
                    worldRenderer.pos(frameD, frameY, frameC).endVertex();
                    worldRenderer.pos(frameA, frameY, frameC).endVertex();
                    tess.draw();
                    GlStateManager.rotate(90F, 0, 1F, 0F);
                    worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                    worldRenderer.pos(frameA, frameY, frameB).endVertex();
                    worldRenderer.pos(frameD, frameY, frameB).endVertex();
                    worldRenderer.pos(frameD, frameY, frameC).endVertex();
                    worldRenderer.pos(frameA, frameY, frameC).endVertex();
                    tess.draw();
                }

                // Restore the lighting state
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                //? need to undo GlStateManager.glBlendFunc()?
                GlStateManager.enableLighting();
                GlStateManager.enableTexture2D();
//                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
            }

            GlStateManager.enableRescaleNormal();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }

    private int tenLSB(int x)
    {
        if (x < 0)
            return x % 1024 + 1024;
        
        return x % 1024;
    }
}
