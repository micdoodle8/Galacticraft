package micdoodle8.mods.galacticraft.core.client.render.tile;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.client.obj.GCModelCache;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class TileEntityFluidTankRenderer extends TileEntityRenderer<TileEntityFluidTank>
{
    public TileEntityFluidTankRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
    {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileEntityFluidTank tileTank, float partialTicks, MatrixStack matStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
    {
//        FluidTankInfo[] info = tank.getTankInfo(Direction.DOWN);

//        if (info.length != 1)
//        {
//            return;
//        }
//        FluidStack tankFluid = info[0].fluid;
        FluidStack tankFluid = tileTank.fluidTank.getFluid();
        if (tankFluid == FluidStack.EMPTY || (!tankFluid.getFluid().getAttributes().isGaseous() && tankFluid.getAmount() == 0))
        {
            return;
        }

        TileEntityFluidTank tankAbove = tileTank.getNextTank(tileTank.getPos());
        TileEntityFluidTank tankBelow = tileTank.getPreviousTank(tileTank.getPos());

//        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
//        Tessellator tess = Tessellator.getInstance();
        TextureAtlasSprite fluidSprite = ModelLoader.defaultTextureGetter().apply(ForgeHooksClient.getBlockMaterial(tankFluid.getFluid().getAttributes().getStillTexture()));
        RenderType renderType = RenderType.getEntitySolid(tankFluid.getFluid().getAttributes().getStillTexture());
        IVertexBuilder builder = bufferIn.getBuffer(renderType);
        final float uMin = fluidSprite.getMinU();
        final float uMax = fluidSprite.getMaxU();
        final float vMin = fluidSprite.getMinV();
        final float vMax = fluidSprite.getMaxV();
//        GL11.glPushMatrix();
        matStack.push();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GL11.glTranslatef((float) x, (float) y + 1.5F, (float) z + 1.0F);
        matStack.translate(0.0F, 1.5F, 1.0F);
//        GL11.glScalef(1.0F, -1.0F, -1.0F);
        matStack.scale(1.0F, -1.0F, -1.0F);
//        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        matStack.translate(0.5F, 0.5F, 0.5F);

        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        Tessellator tess = Tessellator.getInstance();
//        BufferBuilder worldRenderer = tess.getBuffer();

        float level = 1.0F;
        float levelInv = 0.0F;
        float opacity = 1.0F;

        boolean compositeGaseous = tankFluid.getFluid().getAttributes().isGaseous();

        if (compositeGaseous)
        {
            opacity = Math.min(tankFluid.getAmount() / (float) tileTank.fluidTank.getCapacity() * 0.8F + 0.2F, 1F);
        }
        else
        {
            level = tileTank.fluidTank.getFluidAmount() / 16400.0F;
            if (level <= 0.012F)
            {
                levelInv = 1.0F;  //Empty tanks render empty - see #3222
            }
            else
            {
                levelInv = 0.988F - level;  //1.2% inset from each end of the tank, to avoid z-fighting with blocks above/below
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, opacity);

        if (levelInv < 1.0F)
        {
            Matrix4f matrix = matStack.getLast().getMatrix();

            Vector3f norm = Vector3f.ZN;
            // North
            builder.pos(matrix, -0.4F, levelInv, -0.399F).color(255, 255, 255, 255).tex(uMin, vMin).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
            builder.pos(matrix, -0.4F, 1.0F, -0.399F).color(255, 255, 255, 255).tex(uMin, vMin + (vMax - vMin) * level).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
            builder.pos(matrix, 0.4F, 1.0F, -0.399F).color(255, 255, 255, 255).tex(uMax, vMin + (vMax - vMin) * level).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
            builder.pos(matrix, 0.4F, levelInv, -0.399F).color(255, 255, 255, 255).tex(uMax, vMin).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();

            norm = Vector3f.ZP;
            // South
            builder.pos(matrix, 0.4F, levelInv, 0.399F).color(255, 255, 255, 255).tex(uMax, vMin).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
            builder.pos(matrix, 0.4F, 1.0F, 0.399F).color(255, 255, 255, 255).tex(uMax, vMin + (vMax - vMin) * level).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
            builder.pos(matrix, -0.4F, 1.0F, 0.399F).color(255, 255, 255, 255).tex(uMin, vMin + (vMax - vMin) * level).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
            builder.pos(matrix, -0.4F, levelInv, 0.399F).color(255, 255, 255, 255).tex(uMin, vMin).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();

            norm = Vector3f.XN;
            // West
            builder.pos(matrix, -0.399F, 1.0F, -0.4F).color(255, 255, 255, 255).tex(uMin, vMin + (vMax - vMin) * level).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
            builder.pos(matrix, -0.399F, levelInv, -0.4F).color(255, 255, 255, 255).tex(uMin, vMin).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
            builder.pos(matrix, -0.399F, levelInv, 0.4F).color(255, 255, 255, 255).tex(uMax, vMin).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
            builder.pos(matrix, -0.399F, 1.0F, 0.4F).color(255, 255, 255, 255).tex(uMax, vMin + (vMax - vMin) * level).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();

            norm = Vector3f.XP;
            // East
            builder.pos(matrix, 0.399F, 1.0F, 0.4F).color(255, 255, 255, 255).tex(uMax, vMin + (vMax - vMin) * level).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
            builder.pos(matrix, 0.399F, levelInv, 0.4F).color(255, 255, 255, 255).tex(uMax, vMin).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
            builder.pos(matrix, 0.399F, levelInv, -0.4F).color(255, 255, 255, 255).tex(uMin, vMin).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
            builder.pos(matrix, 0.399F, 1.0F, -0.4F).color(255, 255, 255, 255).tex(uMin, vMin + (vMax - vMin) * level).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();

            if (tankAbove == null || (tankAbove.fluidTank.getFluidAmount() == 0 && !compositeGaseous))
            {
                norm = Vector3f.YP;
                builder.pos(matrix, 0.4F, 0.01F + levelInv, 0.4F).color(255, 255, 255, 255).tex(uMax, vMax).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
                builder.pos(matrix, -0.4F, 0.01F + levelInv, 0.4F).color(255, 255, 255, 255).tex(uMax, vMin).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
                builder.pos(matrix, -0.4F, 0.01F + levelInv, -0.4F).color(255, 255, 255, 255).tex(uMin, vMin).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
                builder.pos(matrix, 0.4F, 0.01F + levelInv, -0.4F).color(255, 255, 255, 255).tex(uMin, vMax).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
            }

            if (tankBelow == null || (tankBelow.fluidTank.getFluidAmount() == 0 && !compositeGaseous))
            {
                norm = Vector3f.YN;
                builder.pos(matrix, 0.4F, 0.99F, 0.4F).color(255, 255, 255, 255).tex(uMax, vMax).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
                builder.pos(matrix, 0.4F, 0.99F, -0.4F).color(255, 255, 255, 255).tex(uMin, vMax).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
                builder.pos(matrix, -0.4F, 0.99F, -0.4F).color(255, 255, 255, 255).tex(uMin, vMin).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
                builder.pos(matrix, -0.4F, 0.99F, 0.4F).color(255, 255, 255, 255).tex(uMax, vMin).overlay(OverlayTexture.NO_OVERLAY).lightmap(combinedLightIn).normal(norm.getX(), norm.getY(), norm.getZ()).endVertex();
            }
        }

        GlStateManager.enableLighting();
        GlStateManager.disableBlend();

//        GL11.glPopMatrix();
        matStack.pop();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
