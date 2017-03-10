package codechicken.lib.render;

import codechicken.lib.colour.Colour;
import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.lighting.LC;
import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.render.buffer.BakingVertexBuffer;
import codechicken.lib.render.pipeline.CCRenderPipeline;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.render.pipeline.IVertexSource;
import codechicken.lib.render.pipeline.VertexAttribute;
import codechicken.lib.render.pipeline.attribute.*;
import codechicken.lib.vec.Vector3;
import codechicken.lib.vec.Vertex5;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * The core of the CodeChickenLib render system.
 * Where possible assign a local var of CCRenderState to avoid millions of calls to instance();
 * Uses a ThreadLocal system to assign each thread their own CCRenderState so we can use it in Multithreaded chunk batching.
 * TODO, proper piping of BakedQuads and CCBakedQuads.
 */
public class CCRenderState {

    private static int nextOperationIndex;

    public static int registerOperation() {
        return nextOperationIndex++;
    }

    public static int operationCount() {
        return nextOperationIndex;
    }

    //Each attrib needs to be assigned in this order to have a valid operation index.
    public final VertexAttribute<Vector3[]> normalAttrib = new NormalAttribute();
    public final VertexAttribute<int[]> colourAttrib = new ColourAttribute();
    public final VertexAttribute<int[]> lightingAttrib = new LightingAttribute();
    public final VertexAttribute<int[]> sideAttrib = new SideAttribute();
    public final VertexAttribute<LC[]> lightCoordAttrib = new LightCoordAttribute();

    private static final ThreadLocal<CCRenderState> instances = new ThreadLocal<CCRenderState>() {
        @Override
        protected CCRenderState initialValue() {
            return new CCRenderState();
        }
    };
    private static final CCRenderState bakingRenderState = new CCRenderState();

    //pipeline state
    public IVertexSource model;
    public int firstVertexIndex;
    public int lastVertexIndex;
    public int vertexIndex;
    public CCRenderPipeline pipeline;
    @SideOnly (Side.CLIENT)
    public VertexBuffer r;
    @SideOnly (Side.CLIENT)
    public VertexFormat fmt;

    //context
    public int baseColour;
    public int alphaOverride;
    public boolean computeLighting;
    public LightMatrix lightMatrix = new LightMatrix();

    //vertex outputs
    public final Vertex5 vert = new Vertex5();
    public final Vector3 normal = new Vector3();
    public int colour;
    public int brightness;

    //attribute storage
    public int side;
    public LC lc = new LC();
    @SideOnly (Side.CLIENT)
    public TextureAtlasSprite sprite;

    private CCRenderState() {
        pipeline = new CCRenderPipeline(this);

    }

    public static CCRenderState instance() {
        return instances.get();
    }

    public static CCRenderState getBakingRenderState() {
        return bakingRenderState;
    }

    public void reset() {
        model = null;
        pipeline.reset();
        computeLighting = true;
        baseColour = alphaOverride = -1;
    }

    public void preRenderWorld(IBlockAccess world, BlockPos pos) {
        this.reset();
        this.colour = 0xFFFFFFFF;
        this.setBrightness(world, pos);
    }

    public void setPipeline(IVertexOperation... ops) {
        pipeline.setPipeline(ops);
    }

    public void setPipeline(IVertexSource model, int start, int end, IVertexOperation... ops) {
        pipeline.reset();
        pipeline.forceFormatAttributes = false;
        setModel(model, start, end);
        pipeline.forceFormatAttributes = true;
        pipeline.setPipeline(ops);
    }

    public void bindModel(IVertexSource model) {
        if (this.model != model) {
            this.model = model;
            pipeline.rebuild();
        }
    }

    public void setModel(IVertexSource source) {
        setModel(source, 0, source.getVertices().length);
    }

    public void setModel(IVertexSource source, int start, int end) {
        bindModel(source);
        setVertexRange(start, end);
    }

    public void setVertexRange(int start, int end) {
        firstVertexIndex = start;
        lastVertexIndex = end;
    }

    public void renderQuads(List<BakedQuad> quads) {
        VertexBuffer buffer = startDrawing(GL11.GL_QUADS, quads.get(0).getFormat());
        for (BakedQuad quad : quads) {
            buffer.addVertexData(quad.getVertexData());
        }
        draw();
    }

    public void render(IVertexOperation... ops) {
        setPipeline(ops);
        render();
    }

    public void render() {
        Vertex5[] verts = model.getVertices();
        for (vertexIndex = firstVertexIndex; vertexIndex < lastVertexIndex; vertexIndex++) {
            model.prepareVertex(this);
            vert.set(verts[vertexIndex]);
            runPipeline();
            writeVert();
        }
    }

    public void runPipeline() {
        pipeline.operate();
    }

    public void writeVert() {
        if (r instanceof BakingVertexBuffer) {
            ((BakingVertexBuffer) r).setSprite(sprite);
        }
        for (int e = 0; e < fmt.getElementCount(); e++) {
            VertexFormatElement fmte = fmt.getElement(e);
            switch (fmte.getUsage()) {
                case POSITION:
                    r.pos(vert.vec.x, vert.vec.y, vert.vec.z);
                    break;
                case UV:
                    if (fmte.getIndex() == 0) {
                        r.tex(vert.uv.u, vert.uv.v);
                    } else {
                        r.lightmap(brightness >> 16 & 65535, brightness & 65535);
                    }
                    break;
                case COLOR:
                    if (r.isColorDisabled()) {
                        //-_- Fucking mojang..
                        r.nextVertexFormatIndex();
                    } else {
                        r.color(colour >>> 24, colour >> 16 & 0xFF, colour >> 8 & 0xFF, alphaOverride >= 0 ? alphaOverride : colour & 0xFF);
                    }
                    break;
                case NORMAL:
                    r.normal((float) normal.x, (float) normal.y, (float) normal.z);
                    break;
                case PADDING:
                    break;
                default:
                    throw new UnsupportedOperationException("Generic vertex format element");
            }
        }
        r.endVertex();
    }

    public void pushColour() {
        GlStateManager.color((colour >>> 24) / 255F, (colour >> 16 & 0xFF) / 255F, (colour >> 8 & 0xFF) / 255F, (alphaOverride >= 0 ? alphaOverride : colour & 0xFF) / 255F);
    }

    public void setBrightness(IBlockAccess world, BlockPos pos) {
        brightness = world.getBlockState(pos).getBlock().getPackedLightmapCoords(world.getBlockState(pos), world, pos);
    }

    public void pullLightmap() {
        brightness = (int) OpenGlHelper.lastBrightnessY << 16 | (int) OpenGlHelper.lastBrightnessX;
    }

    public void pushLightmap() {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightness & 0xFFFF, brightness >>> 16);
    }

    public void setFluidColour(FluidStack fluidStack) {
        setFluidColour(fluidStack, 0xFF);
    }

    public void setFluidColour(FluidStack fluidStack, int alpha) {
        this.colour = fluidStack.getFluid().getColor(fluidStack) << 8 | alpha;
    }

    public void setColour(Colour colour) {
        this.colour = colour.rgba();
    }

    public ColourRGBA getColour() {
        return new ColourRGBA(colour);
    }

    @SideOnly (Side.CLIENT)
    public VertexBuffer startDrawing(int mode, VertexFormat format) {
        VertexBuffer r = Tessellator.getInstance().getBuffer();
        r.begin(mode, format);
        bind(r);
        return r;
    }

    @SideOnly (Side.CLIENT)
    public VertexBuffer startDrawing(int mode, VertexFormat format, VertexBuffer buffer) {
        buffer.begin(mode, format);
        bind(buffer);
        return buffer;
    }

    @SideOnly (Side.CLIENT)
    public void bind(VertexBuffer r) {
        this.r = r;
        fmt = r.getVertexFormat();
    }

    @SideOnly (Side.CLIENT)
    public VertexBuffer getBuffer() {
        return r;
    }

    @SideOnly (Side.CLIENT)
    public VertexFormat getVertexFormat() {
        return fmt;
    }

    public void draw() {
        Tessellator.getInstance().draw();
    }

    /**
     * Polls the currently bound VertexBuffer to see if it is drawing.
     * If no buffer is bound it checks Tessellator.getInstance.getBuffer.
     *
     * @return If the buffer is drawing.
     */
    public boolean isDrawing() {
        if (r != null) {
            return r.isDrawing;
        } else {
            return Tessellator.getInstance().getBuffer().isDrawing;
        }
    }
}
