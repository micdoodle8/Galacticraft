package codechicken.lib.render;

import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.*;
import codechicken.lib.vec.uv.IconTransformation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

//TODO Document and reorder things in here.
public class RenderUtils {

    private static Vector3[] vectors = new Vector3[8];
    private static RenderEntityItem uniformRenderItem;
    private static boolean hasInitRenderItem;

    private static ThreadLocal<IconTransformation> iconTransformCache = new ThreadLocal<IconTransformation>() {
        @Override
        protected IconTransformation initialValue() {
            return new IconTransformation(TextureUtils.getBlockTexture("stone"));
        }
    };

    private static EntityItem entityItem;

    static {
        for (int i = 0; i < vectors.length; i++) {
            vectors[i] = new Vector3();
        }

        entityItem = new EntityItem(null);
        entityItem.hoverStart = 0;
    }

    private static void loadItemRenderer() {
        if (!hasInitRenderItem) {
            Minecraft minecraft = Minecraft.getMinecraft();
            uniformRenderItem = new RenderEntityItem(minecraft.getRenderManager(), minecraft.getRenderItem());
            hasInitRenderItem = true;
        }
    }

    public static void renderFluidQuad(Vector3 point1, Vector3 point2, Vector3 point3, Vector3 point4, TextureAtlasSprite icon, double res) {
        renderFluidQuad(point2, vectors[0].set(point4).subtract(point1), vectors[1].set(point1).subtract(point2), icon, res);
    }

    /**
     * Draws a tessellated quadrilateral bottom to top, left to right
     *
     * @param base The bottom left corner of the quad
     * @param wide The bottom of the quad
     * @param high The left side of the quad
     * @param res  Units per icon
     */
    public static void renderFluidQuad(Vector3 base, Vector3 wide, Vector3 high, TextureAtlasSprite icon, double res) {
        VertexBuffer r = CCRenderState.instance().getBuffer();
        double u1 = icon.getMinU();
        double du = icon.getMaxU() - icon.getMinU();
        double v2 = icon.getMaxV();
        double dv = icon.getMaxV() - icon.getMinV();

        double wlen = wide.mag();
        double hlen = high.mag();

        double x = 0;
        while (x < wlen) {
            double rx = wlen - x;
            if (rx > res) {
                rx = res;
            }

            double y = 0;
            while (y < hlen) {
                double ry = hlen - y;
                if (ry > res) {
                    ry = res;
                }

                Vector3 dx1 = vectors[2].set(wide).multiply(x / wlen);
                Vector3 dx2 = vectors[3].set(wide).multiply((x + rx) / wlen);
                Vector3 dy1 = vectors[4].set(high).multiply(y / hlen);
                Vector3 dy2 = vectors[5].set(high).multiply((y + ry) / hlen);

                r.pos(base.x + dx1.x + dy2.x, base.y + dx1.y + dy2.y, base.z + dx1.z + dy2.z).tex(u1, v2 - ry / res * dv).endVertex();
                r.pos(base.x + dx1.x + dy1.x, base.y + dx1.y + dy1.y, base.z + dx1.z + dy1.z).tex(u1, v2).endVertex();
                r.pos(base.x + dx2.x + dy1.x, base.y + dx2.y + dy1.y, base.z + dx2.z + dy1.z).tex(u1 + rx / res * du, v2).endVertex();
                r.pos(base.x + dx2.x + dy2.x, base.y + dx2.y + dy2.y, base.z + dx2.z + dy2.z).tex(u1 + rx / res * du, v2 - ry / res * dv).endVertex();

                y += ry;
            }

            x += rx;
        }
    }

    public static void translateToWorldCoords(Entity entity, float frame) {
        double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * frame;
        double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * frame;
        double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * frame;

        GlStateManager.translate(-interpPosX, -interpPosY, -interpPosZ);
    }

    public static void drawCuboidOutline(Cuboid6 c) {
        CCRenderState state = CCRenderState.instance();
        VertexBuffer r = state.startDrawing(3, DefaultVertexFormats.POSITION);
        r.pos(c.min.x, c.min.y, c.min.z).endVertex();
        r.pos(c.max.x, c.min.y, c.min.z).endVertex();
        r.pos(c.max.x, c.min.y, c.max.z).endVertex();
        r.pos(c.min.x, c.min.y, c.max.z).endVertex();
        r.pos(c.min.x, c.min.y, c.min.z).endVertex();
        state.draw();
        state.startDrawing(3, DefaultVertexFormats.POSITION);
        r.pos(c.min.x, c.max.y, c.min.z).endVertex();
        r.pos(c.max.x, c.max.y, c.min.z).endVertex();
        r.pos(c.max.x, c.max.y, c.max.z).endVertex();
        r.pos(c.min.x, c.max.y, c.max.z).endVertex();
        r.pos(c.min.x, c.max.y, c.min.z).endVertex();
        state.draw();
        state.startDrawing(1, DefaultVertexFormats.POSITION);
        r.pos(c.min.x, c.min.y, c.min.z).endVertex();
        r.pos(c.min.x, c.max.y, c.min.z).endVertex();
        r.pos(c.max.x, c.min.y, c.min.z).endVertex();
        r.pos(c.max.x, c.max.y, c.min.z).endVertex();
        r.pos(c.max.x, c.min.y, c.max.z).endVertex();
        r.pos(c.max.x, c.max.y, c.max.z).endVertex();
        r.pos(c.min.x, c.min.y, c.max.z).endVertex();
        r.pos(c.min.x, c.max.y, c.max.z).endVertex();
        state.draw();
    }

    public static void renderFluidCuboid(Cuboid6 bound, TextureAtlasSprite tex, double res) {
        renderFluidQuad(//bottom
                new Vector3(bound.min.x, bound.min.y, bound.min.z), new Vector3(bound.max.x, bound.min.y, bound.min.z), new Vector3(bound.max.x, bound.min.y, bound.max.z), new Vector3(bound.min.x, bound.min.y, bound.max.z), tex, res);
        renderFluidQuad(//top
                new Vector3(bound.min.x, bound.max.y, bound.min.z), new Vector3(bound.min.x, bound.max.y, bound.max.z), new Vector3(bound.max.x, bound.max.y, bound.max.z), new Vector3(bound.max.x, bound.max.y, bound.min.z), tex, res);
        renderFluidQuad(//-x
                new Vector3(bound.min.x, bound.max.y, bound.min.z), new Vector3(bound.min.x, bound.min.y, bound.min.z), new Vector3(bound.min.x, bound.min.y, bound.max.z), new Vector3(bound.min.x, bound.max.y, bound.max.z), tex, res);
        renderFluidQuad(//+x
                new Vector3(bound.max.x, bound.max.y, bound.max.z), new Vector3(bound.max.x, bound.min.y, bound.max.z), new Vector3(bound.max.x, bound.min.y, bound.min.z), new Vector3(bound.max.x, bound.max.y, bound.min.z), tex, res);
        renderFluidQuad(//-z
                new Vector3(bound.max.x, bound.max.y, bound.min.z), new Vector3(bound.max.x, bound.min.y, bound.min.z), new Vector3(bound.min.x, bound.min.y, bound.min.z), new Vector3(bound.min.x, bound.max.y, bound.min.z), tex, res);
        renderFluidQuad(//+z
                new Vector3(bound.min.x, bound.max.y, bound.max.z), new Vector3(bound.min.x, bound.min.y, bound.max.z), new Vector3(bound.max.x, bound.min.y, bound.max.z), new Vector3(bound.max.x, bound.max.y, bound.max.z), tex, res);
    }

    public static void renderBlockOverlaySide(int x, int y, int z, int side, double tx1, double tx2, double ty1, double ty2) {
        double[] points = new double[] { x - 0.009, x + 1.009, y - 0.009, y + 1.009, z - 0.009, z + 1.009 };

        VertexBuffer r = Tessellator.getInstance().getBuffer();
        //TODO
        switch (side) {
            case 0:
                r.pos(points[0], points[2], points[4]).tex(tx1, ty1).endVertex();
                r.pos(points[1], points[2], points[4]).tex(tx2, ty1).endVertex();
                r.pos(points[1], points[2], points[5]).tex(tx2, ty2).endVertex();
                r.pos(points[0], points[2], points[5]).tex(tx1, ty2).endVertex();
                break;
            case 1:
                r.pos(points[1], points[3], points[4]).tex(tx2, ty1).endVertex();
                r.pos(points[0], points[3], points[4]).tex(tx1, ty1).endVertex();
                r.pos(points[0], points[3], points[5]).tex(tx1, ty2).endVertex();
                r.pos(points[1], points[3], points[5]).tex(tx2, ty2).endVertex();
                break;
            case 2:
                r.pos(points[0], points[3], points[4]).tex(tx2, ty1).endVertex();
                r.pos(points[1], points[3], points[4]).tex(tx1, ty1).endVertex();
                r.pos(points[1], points[2], points[4]).tex(tx1, ty2).endVertex();
                r.pos(points[0], points[2], points[4]).tex(tx2, ty2).endVertex();
                break;
            case 3:
                r.pos(points[1], points[3], points[5]).tex(tx2, ty1).endVertex();
                r.pos(points[0], points[3], points[5]).tex(tx1, ty1).endVertex();
                r.pos(points[0], points[2], points[5]).tex(tx1, ty2).endVertex();
                r.pos(points[1], points[2], points[5]).tex(tx2, ty2).endVertex();
                break;
            case 4:
                r.pos(points[0], points[3], points[5]).tex(tx2, ty1).endVertex();
                r.pos(points[0], points[3], points[4]).tex(tx1, ty1).endVertex();
                r.pos(points[0], points[2], points[4]).tex(tx1, ty2).endVertex();
                r.pos(points[0], points[2], points[5]).tex(tx2, ty2).endVertex();
                break;
            case 5:
                r.pos(points[1], points[3], points[4]).tex(tx2, ty1).endVertex();
                r.pos(points[1], points[3], points[5]).tex(tx1, ty1).endVertex();
                r.pos(points[1], points[2], points[5]).tex(tx1, ty2).endVertex();
                r.pos(points[1], points[2], points[4]).tex(tx2, ty2).endVertex();
                break;
        }
    }

    public static void renderHitBox(EntityPlayer player, Cuboid6 cuboid, float partialTicks) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        double xPos = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
        double yPos = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
        double zPos = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;
        RenderGlobal.drawSelectionBoundingBox(cuboid.aabb().expandXyz(0.0020000000949949026D).offset(-xPos, -yPos, -zPos), 0.0F, 0.0F, 0.0F, 0.4F);
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }

    /**
     * Checks to see if the FluidStack should be rendered.
     *
     * @param stack FluidStack to render.
     * @return Weather to render or not.
     */
    public static boolean shouldRenderFluid(FluidStack stack) {
        return stack.amount > 0 && stack.getFluid() != null;
    }

    /**
     * Sets the colour of the fluid and returns the texture
     *
     * @param stack The fluid stack to render
     * @return The icon of the fluid
     */
    public static TextureAtlasSprite prepareFluidRender(FluidStack stack, int alpha) {
        Fluid fluid = stack.getFluid();
        CCRenderState.instance().colour = fluid.getColor(stack) << 8 | alpha;
        return TextureUtils.getTexture(fluid.getStill());
    }

    /**
     * Disables lighting, enables blending and changes to the blocks texture
     */
    public static void preFluidRender() {
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        TextureUtils.bindBlockTexture();
    }

    /**
     * Re-enables lighting and disables blending.
     */
    public static void postFluidRender() {
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
    }

    public static double fluidDensityToAlpha(double density) {
        return Math.pow(density, 0.4);
    }

    /**
     * Renders a fluid within a bounding box.
     * Assumes that CCRenderstate is already drawing with appropriate gl state. For a separate draw call see renderFluidCuboidGL
     * If the fluid is a liquid it will render as a normal tank with height equal to density/bound.height.
     * If the fluid is a gas, it will render the full box with an alpha equal to density.
     * Warning, bound will be mutated if the fluid is a liquid
     *
     * @param stack   The fluid to render.
     * @param bound   The box within which the fluid is contained.
     * @param density The volume of fluid / the capacity of the tank. For gases this determines the alpha, for liquids this determines the height.
     * @param res     The resolution to render at.
     */
    public static void renderFluidCuboid(FluidStack stack, Cuboid6 bound, double density, double res) {
        if (!shouldRenderFluid(stack)) {
            return;
        }

        int alpha = 255;
        if (stack.getFluid().isGaseous()) {
            alpha = (int) (fluidDensityToAlpha(density) * 255);
        } else {
            bound.max.y = bound.min.y + (bound.max.y - bound.min.y) * density;
        }

        renderFluidCuboid(bound, prepareFluidRender(stack, alpha), res);
    }

    public static void renderFluidCuboidGL(FluidStack stack, Cuboid6 bound, double density, double res) {
        if (!shouldRenderFluid(stack)) {
            return;
        }

        preFluidRender();
        CCRenderState state = CCRenderState.instance();
        state.startDrawing(7, DefaultVertexFormats.POSITION_TEX);
        renderFluidCuboid(stack, bound, density, res);
        state.pushColour();
        state.draw();
        postFluidRender();
    }

    /**
     * Assumes that CCRenderstate is already drawing with appropriate gl state. For a separate draw call see renderFluidGaugeGL
     */
    public static void renderFluidGauge(FluidStack stack, Rectangle4i rect, double density, double res) {
        if (!shouldRenderFluid(stack)) {
            return;
        }

        int alpha = 255;
        if (stack.getFluid().isGaseous()) {
            alpha = (int) (fluidDensityToAlpha(density) * 255);
        } else {
            int height = (int) (rect.h * density);
            rect.y += rect.h - height;
            rect.h = height;
        }
        CCRenderState state = CCRenderState.instance();
        state.startDrawing(7, DefaultVertexFormats.POSITION_TEX);
        renderFluidQuad(new Vector3(rect.x, rect.y + rect.h, 0), new Vector3(rect.w, 0, 0), new Vector3(0, -rect.h, 0), prepareFluidRender(stack, alpha), res);
        state.draw();
        postFluidRender();
    }

    public static void renderFluidGaugeGL(FluidStack stack, Rectangle4i rect, double density, double res) {
        if (!shouldRenderFluid(stack)) {
            return;
        }

        preFluidRender();
        CCRenderState state = CCRenderState.instance();
        state.startDrawing(7, DefaultVertexFormats.POSITION_TEX);
        renderFluidGauge(stack, rect, density, res);
        state.pushColour();
        state.draw();
    }

    public static Matrix4 getMatrix(Vector3 position, Rotation rotation, double scale) {
        return new Matrix4().translate(position).apply(new Scale(scale)).apply(rotation);
    }

    /**
     * Renders items and blocks in the world at 0,0,0 with transformations that size them appropriately
     */
    public static void renderItemUniform(ItemStack item) {
        renderItemUniform(item, 0);
    }

    /*
     * Renders items and blocks in the world at 0,0,0 with transformations that size them appropriately
     *
     * @param spin The spin angle of the item around the y axis in degrees
     */
    public static void renderItemUniform(ItemStack item, double spin) {
        loadItemRenderer();
        //IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(item, ENTITY);
        //boolean is3D = customRenderer != null && customRenderer.shouldUseRenderHelper(ENTITY, item, BLOCK_3D);

        //boolean larger = false;
        //if (item.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item.getItem()).getRenderType())) {
        //    int renderType = Block.getBlockFromItem(item.getItem()).getRenderType();
        //    larger = !(renderType == 1 || renderType == 19 || renderType == 12 || renderType == 2);
        //} else if (is3D) {
        //    larger = true;
        //}

        //double d = 2;
        //double d1 = 1 / d;
        //if (larger) {
        //    GLStateManager.scale(d, d, d);
        //}

        GlStateManager.color(1, 1, 1, 1);

        entityItem.setEntityItemStack(item);
        uniformRenderItem.doRender(entityItem, 0, 0.06, 0, 0, (float) (spin * 9 / Math.PI));

        //if (larger) {
        //    GLStateManager.scale(d1, d1, d1);
        //}*/
    }

    /**
     * Checks if stencil buffer is supported and attempts to enable it if so.
     */
    public static boolean checkEnableStencil() {
        Framebuffer fb = Minecraft.getMinecraft().getFramebuffer();
        return fb.isStencilEnabled() || fb.enableStencil();
    }

    public static float getPearlBob(double time) {
        return (float) Math.sin(time / 25 * 3.141593) * 0.1F;
    }

    public static int getTimeOffset(BlockPos pos) {
        return getTimeOffset(pos.getX(), pos.getY(), pos.getZ());
    }

    public static int getTimeOffset(int x, int y, int z) {
        return x * 3 + y * 5 + z * 9;
    }

    public static IconTransformation getIconTransformation(TextureAtlasSprite sprite) {
        IconTransformation transformation = iconTransformCache.get();
        transformation.icon = sprite;
        return transformation;
    }

}
