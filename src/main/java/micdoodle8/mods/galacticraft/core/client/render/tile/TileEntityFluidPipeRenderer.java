package micdoodle8.mods.galacticraft.core.client.render.tile;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import micdoodle8.mods.galacticraft.api.transmission.tile.IBufferTransmitter;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.fluid.FluidNetwork;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidPipe;
import micdoodle8.mods.galacticraft.core.tile.TileEntityFluidTank;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fluids.Fluid;

import org.lwjgl.opengl.GL11;

import java.util.HashMap;

public class TileEntityFluidPipeRenderer extends TileEntitySpecialRenderer<TileEntityFluidPipe>
{
    private static HashMap<Integer, HashMap<Fluid, Integer[]>> cache = new HashMap<>();
    private static IFlexibleBakedModel[] pullConnectorModel = new IFlexibleBakedModel[6];

    private final int stages = 100;

    private void updateModels()
    {
        if (pullConnectorModel[0] == null)
        {
            try
            {
                for (EnumFacing facing : EnumFacing.VALUES)
                {
                    // Get the first character of the direction name (n/e/s/w/u/d)
                    Character c = Character.toLowerCase(facing.getName().charAt(0));
                    IModel model = ModelLoaderRegistry.getModel(new ResourceLocation(Constants.ASSET_PREFIX, "block/fluid_pipe_pull_" + c));
                    Function<ResourceLocation, TextureAtlasSprite> spriteFunction = (ResourceLocation location) -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                    pullConnectorModel[facing.ordinal()] = model.bake(model.getDefaultState(), DefaultVertexFormats.ITEM, spriteFunction);
                }
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void renderTileEntityAt(TileEntityFluidPipe pipe, double x, double y, double z, float partialTicks, int destroyStage)
    {
        updateModels();

        if (pipe.getBlockType() == GCBlocks.oxygenPipePull)
        {
            GL11.glPushMatrix();

            GL11.glTranslatef((float) x, (float) y, (float) z);

            RenderHelper.disableStandardItemLighting();
            this.bindTexture(TextureMap.locationBlocksTexture);
            if (Minecraft.isAmbientOcclusionEnabled())
            {
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
            }
            else
            {
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            TileEntity[] adj = OxygenUtil.getAdjacentFluidConnections(pipe);

            for (EnumFacing facing : EnumFacing.VALUES)
            {
                TileEntity sideTile = adj[facing.ordinal()];

                if (sideTile != null && !(sideTile instanceof IBufferTransmitter))
                {
                    GL11.glPushMatrix();
                    if (sideTile instanceof TileEntityFluidTank)
                        switch (facing)
                        {
                        case SOUTH:
                            GL11.glTranslatef(0F, 0F, 1/16F);
                            break;
                        case NORTH:
                            GL11.glTranslatef(0F, 0F, -1/16F);
                            break;
                        case EAST:
                            GL11.glTranslatef(1/16F, 0F, 0F);
                            break;
                        case WEST:
                            GL11.glTranslatef(-1/16F, 0F, 0F);
                            break;
                        }
                    ClientUtil.drawBakedModel(pullConnectorModel[facing.ordinal()]);
                    GL11.glPopMatrix();
                }
            }

            GL11.glPopMatrix();
        }

        float scale;

        if (pipe.hasNetwork())
        {
            FluidNetwork network = (FluidNetwork) pipe.getNetwork();
            scale = network.fluidScale;
        }
        else
        {
            scale = pipe.buffer.getFluidAmount() / (float) pipe.buffer.getCapacity();
        }

        Fluid fluid;

        if (pipe.hasNetwork())
        {
            FluidNetwork network = (FluidNetwork) pipe.getNetwork();
            fluid = network.refFluid;
        }
        else
        {
            fluid = pipe.getBuffer() == null ? null : pipe.getBuffer().getFluid();
        }

        if (fluid == null)
        {
            return;
        }

        if (scale > 0.01)
        {
            this.bindTexture(TextureMap.locationBlocksTexture);
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glTranslatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
            GL11.glScalef(1.0F, -1.0F, -1.0F);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);

            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            float opacity = 1.0F;

            boolean gas = fluid.isGaseous();

            if (gas)
            {
                opacity = scale;
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, opacity);

            TileEntity[] connections = OxygenUtil.getAdjacentFluidConnections(pipe);

            for (EnumFacing side : EnumFacing.VALUES)
            {
                TileEntity sideTile = connections[side.ordinal()];
                if (sideTile != null)
                {
                    Integer[] displayLists = getListAndRender(side, fluid);

                    if (displayLists != null)
                    {
                        if (!gas)
                        {
                            Integer list = displayLists[Math.max(3, (int) (scale * (stages - 1)))];
                            GL11.glCallList(list);
                        }
                        else
                        {
                            Integer list = displayLists[stages - 1];
                            GL11.glCallList(list);
                        }
                    }
                }
            }

            Integer[] displayLists = getListAndRender(null, fluid);

            if (displayLists != null)
            {
                if (!gas)
                {
                    Integer list = displayLists[Math.max(3, (int) (scale * (stages - 1)))];
                    GL11.glCallList(list);
                }
                else
                {
                    Integer list = displayLists[stages - 1];
                    GL11.glCallList(list);
                }
            }

            GlStateManager.enableLighting();
            GlStateManager.disableBlend();

            GL11.glPopMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    private Integer[] getListAndRender(EnumFacing side, Fluid fluid)
    {
        if (fluid == null)
        {
            return null;
        }

        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getStill().toString());
        int sideIndex = side == null ? 6 : side.ordinal();

        if (cache.containsKey(sideIndex) && cache.get(sideIndex).containsKey(fluid))
        {
            return cache.get(sideIndex).get(fluid);
        }

        float size = 0.09F;
        float mid = 0.0F;
        float minX;
        float maxX;
        float minY;
        float maxY;
        float minZ;
        float maxZ;

        Integer[] displayLists = new Integer[this.stages];

        if (cache.containsKey(sideIndex))
        {
            cache.get(sideIndex).put(fluid, displayLists);
        }
        else
        {
            HashMap<Fluid, Integer[]> map = Maps.newHashMap();
            map.put(fluid, displayLists);
            cache.put(sideIndex, map);
        }

        for (int i = 0; i < stages; ++i)
        {
            displayLists[i] = GLAllocation.generateDisplayLists(1);
            GL11.glNewList(displayLists[i], GL11.GL_COMPILE);

            float level = (i / (float) stages);

            switch (sideIndex)
            {
            case 0:
                minX = mid - level * size;
                maxX = mid + level * size;
                minY = mid + size;
                maxY = mid + 0.5F;
                minZ = mid - level * size;
                maxZ = mid + level * size;
                break;
            case 1:
                minX = mid - level * size;
                maxX = mid + level * size;
                minY = mid - 0.5F;
                maxY = mid - size;
                minZ = mid - level * size;
                maxZ = mid + level * size;
                break;
            case 2:
                minX = mid - size;
                maxX = mid + size;
                maxY = mid + size;
                minY = mid + size - level * size * 2.0F;
                minZ = mid + size;
                maxZ = mid + 0.5F;
                break;
            case 3:
                minX = mid - size;
                maxX = mid + size;
                maxY = mid + size;
                minY = mid + size - level * size * 2.0F;
                minZ = mid - 0.5F;
                maxZ = mid - size;
                break;
            case 4:
                minX = mid - 0.5F;
                maxX = mid - size;
                maxY = mid + size;
                minY = mid + size - level * size * 2.0F;
                minZ = mid - size;
                maxZ = mid + size;
                break;
            case 5:
                minX = mid + size;
                maxX = mid + 0.5F;
                maxY = mid + size;
                minY = mid + size - level * size * 2.0F;
                minZ = mid - size;
                maxZ = mid + size;
                break;
            default:
                minX = mid - size;
                maxX = mid + size;
                maxY = mid + size;
                minY = mid + size - level * size * 2.0F;
                minZ = mid - size;
                maxZ = mid + size;
                break;
            }

            renderBox(minX, maxX, minY, maxY, minZ, maxZ, level, sprite);
            GL11.glEndList();
        }

        return displayLists;
    }

    private void renderBox(float minX, float maxX, float minY, float maxY, float minZ, float maxZ, float level, TextureAtlasSprite sprite)
    {
        final double uMin = sprite.getMinU();
        final double uMax = sprite.getMaxU();
        final double vMin = sprite.getMinV();
        final double vMax = sprite.getMaxV();

        Tessellator tess = Tessellator.getInstance();
        WorldRenderer worldRenderer = tess.getWorldRenderer();

        double uDiff = (uMax - uMin);
        double vDiff = (vMax - vMin);
        double texMinX_U = uMin + (minX + 0.5F) * uDiff;
        double texMaxX_U = uMin + (maxX + 0.5F) * uDiff;
        double texMinZ_U = uMin + (minZ + 0.5F) * uDiff;
        double texMaxZ_U = uMin + (maxZ + 0.5F) * uDiff;
        double texMinX_V = vMin + (minX + 0.5F) * vDiff;
        double texMaxX_V = vMin + (maxX + 0.5F) * vDiff;
        double texMinY_V = vMin + (minY + 0.5F) * vDiff;
        double texMaxY_V = vMin + (maxY + 0.5F) * vDiff;

        // North
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(minX, minY, minZ).tex(texMinX_U, texMinY_V).endVertex();
        worldRenderer.pos(minX, maxY, minZ).tex(texMinX_U, texMaxY_V).endVertex();
        worldRenderer.pos(maxX, maxY, minZ).tex(texMaxX_U, texMaxY_V).endVertex();
        worldRenderer.pos(maxX, minY, minZ).tex(texMaxX_U, texMinY_V).endVertex();
        tess.draw();

        // South
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(maxX, minY, maxZ).tex(texMaxX_U, texMinY_V).endVertex();
        worldRenderer.pos(maxX, maxY, maxZ).tex(texMaxX_U, texMaxY_V).endVertex();
        worldRenderer.pos(minX, maxY, maxZ).tex(texMinX_U, texMaxY_V).endVertex();
        worldRenderer.pos(minX, minY, maxZ).tex(texMinX_U, texMinY_V).endVertex();
        tess.draw();

        // West
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(minX, maxY, minZ).tex(texMinZ_U, texMaxY_V).endVertex();
        worldRenderer.pos(minX, minY, minZ).tex(texMinZ_U, texMinY_V).endVertex();
        worldRenderer.pos(minX, minY, maxZ).tex(texMaxZ_U, texMinY_V).endVertex();
        worldRenderer.pos(minX, maxY, maxZ).tex(texMaxZ_U, texMaxY_V).endVertex();
        tess.draw();

        // East
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(maxX, maxY, maxZ).tex(texMaxZ_U, texMaxY_V).endVertex();
        worldRenderer.pos(maxX, minY, maxZ).tex(texMaxZ_U, texMinY_V).endVertex();
        worldRenderer.pos(maxX, minY, minZ).tex(texMinZ_U, texMinY_V).endVertex();
        worldRenderer.pos(maxX, maxY, minZ).tex(texMinZ_U, texMaxY_V).endVertex();
        tess.draw();

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(maxX, minY, maxZ).tex(texMaxZ_U, texMaxX_V).endVertex();
        worldRenderer.pos(minX, minY, maxZ).tex(texMaxZ_U, texMinX_V).endVertex();
        worldRenderer.pos(minX, minY, minZ).tex(texMinZ_U, texMinX_V).endVertex();
        worldRenderer.pos(maxX, minY, minZ).tex(texMinZ_U, texMaxX_V).endVertex();
        tess.draw();

        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(maxX, maxY, maxZ).tex(texMaxZ_U, texMaxX_V).endVertex();
        worldRenderer.pos(maxX, maxY, minZ).tex(texMinZ_U, texMaxX_V).endVertex();
        worldRenderer.pos(minX, maxY, minZ).tex(texMinZ_U, texMinX_V).endVertex();
        worldRenderer.pos(minX, maxY, maxZ).tex(texMaxZ_U, texMinX_V).endVertex();
        tess.draw();
    }
}
