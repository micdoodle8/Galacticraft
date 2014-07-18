package codechicken.lib.render;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.lighting.LC;
import codechicken.lib.lighting.LightMatrix;
import codechicken.lib.util.Copyable;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Transformation;
import codechicken.lib.vec.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;

/**
 * The core of the CodeChickenLib render system.
 * Rendering operations are written to avoid object allocations by reusing static variables.
 */
public class CCRenderState
{
    private static int nextOperationIndex;

    public static int registerOperation() {
        return nextOperationIndex++;
    }

    public static int operationCount() {
        return nextOperationIndex;
    }

    /**
     * Represents an operation to be run for each vertex that operates on and modifies the current state
     */
    public static interface IVertexOperation
    {
        /**
         * Load any required references and add dependencies to the pipeline based on the current model (may be null)
         * Return false if this operation is redundant in the pipeline with the given model
         */
        public boolean load();

        /**
         * Perform the operation on the current render state
         */
        public void operate();

        /**
         * Get the unique id representing this type of operation. Duplicate operation IDs within the pipeline may have unexpected results.
         * ID shoulld be obtained from CCRenderState.registerOperation() and stored in a static variable
         */
        public int operationID();
    }

    private static ArrayList<VertexAttribute<?>> vertexAttributes = new ArrayList<VertexAttribute<?>>();
    private static int registerVertexAttribute(VertexAttribute<?> attr) {
        vertexAttributes.add(attr);
        return vertexAttributes.size()-1;
    }

    public static VertexAttribute<?> getAttribute(int index) {
        return vertexAttributes.get(index);
    }

    /**
     * Management class for a vertex attrute such as colour, normal etc
     * This class should handle the loading of the attrute from an array provided by IVertexSource.getAttributes or the computation of this attrute from others
     * @param <T> The array type for this attrute eg. int[], Vector3[]
     */
    public static abstract class VertexAttribute<T> implements IVertexOperation
    {
        public final int attributeIndex = registerVertexAttribute(this);
        private final int operationIndex = registerOperation();
        /**
         * Set to true when the attrute is part of the pipeline. Should only be managed by CCRenderState when constructing the pipeline
         */
        public boolean active = false;

        /**
         * Construct a new array for storage of vertex attrutes in a model
         */
        public abstract T newArray(int length);

        @Override
        public int operationID() {
            return operationIndex;
        }
    }

    public static void arrayCopy(Object src, int srcPos, Object dst, int destPos, int length) {
        System.arraycopy(src, srcPos, dst, destPos, length);
        if(dst instanceof Copyable[]) {
            Object[] oa = (Object[])dst;
            Copyable<Object>[] c = (Copyable[])dst;
            for(int i = destPos; i < destPos+length; i++)
                if(c[i] != null)
                    oa[i] = c[i].copy();
        }
    }

    public static <T> T copyOf(VertexAttribute<T> attr, T src, int length) {
        T dst = attr.newArray(length);
        arrayCopy(src, 0, dst, 0, length);
        return dst;
    }

    public static interface IVertexSource
    {
        public Vertex5[] getVertices();

        /**
         * Gets an array of vertex attrutes
         * @param attr The vertex attrute to get
         * @param <T> The attrute array type
         * @return An array, or null if not computed
         */
        public <T> T getAttributes(VertexAttribute<T> attr);

        /**
         * @return True if the specified attrute is provided by this model, either by returning an array from getAttributes or by setting the state in prepareVertex
         */
        public boolean hasAttribute(VertexAttribute<?> attr);

        /**
         * Callback to set CCRenderState for a vertex before the pipeline runs
         */
        public void prepareVertex();
    }

    public static VertexAttribute<Vector3[]> normalAttrib = new VertexAttribute<Vector3[]>() {
        private Vector3[] normalRef;

        @Override
        public Vector3[] newArray(int length) {
            return new Vector3[length];
        }

        @Override
        public boolean load() {
            normalRef = model.getAttributes(this);
            if(model.hasAttribute(this))
                return normalRef != null;

            if(model.hasAttribute(sideAttrib)) {
                pipeline.addDependency(sideAttrib);
                return true;
            }
            throw new IllegalStateException("Normals requested but neither normal or side attrutes are provided by the model");
        }

        @Override
        public void operate() {
            if(normalRef != null)
                setNormal(normalRef[vertexIndex]);
            else
                setNormal(Rotation.axes[side]);
        }
    };
    public static VertexAttribute<int[]> colourAttrib = new VertexAttribute<int[]>() {
        private int[] colourRef;

        @Override
        public int[] newArray(int length) {
            return new int[length];
        }

        @Override
        public boolean load() {
            colourRef = model.getAttributes(this);
            return colourRef != null || !model.hasAttribute(this);
        }

        @Override
        public void operate() {
            if(colourRef != null)
                setColour(ColourRGBA.multiply(baseColour, colourRef[vertexIndex]));
            else
                setColour(baseColour);
        }
    };
    public static VertexAttribute<int[]> sideAttrib = new VertexAttribute<int[]>() {
        private int[] sideRef;

        @Override
        public int[] newArray(int length) {
            return new int[length];
        }

        @Override
        public boolean load() {
            sideRef = model.getAttributes(this);
            if(model.hasAttribute(this))
                return sideRef != null;

            pipeline.addDependency(normalAttrib);
            return true;
        }

        @Override
        public void operate() {
            if(sideRef != null)
                side = sideRef[vertexIndex];
            else
                side = CCModel.findSide(normal);
        }
    };
    /**
     * Uses the position of the lightmatrix to compute LC if not provided
     */
    public static VertexAttribute<LC[]> lightCoordAttrib = new VertexAttribute<LC[]>() {
        private LC[] lcRef;
        private Vector3 vec = new Vector3();//for computation
        private Vector3 pos = new Vector3();

        @Override
        public LC[] newArray(int length) {
            return new LC[length];
        }

        @Override
        public boolean load() {
            lcRef = model.getAttributes(this);
            if(model.hasAttribute(this))
                return lcRef != null;

            pos.set(lightMatrix.pos.x, lightMatrix.pos.y, lightMatrix.pos.z);
            pipeline.addDependency(sideAttrib);
            pipeline.addRequirement(Transformation.operationIndex);
            return true;
        }

        @Override
        public void operate() {
            if(lcRef != null)
                lc.set(lcRef[vertexIndex]);
            else
                lc.compute(vec.set(vert.vec).sub(pos), side);
        }
    };

    //pipeline state
    public static IVertexSource model;
    public static int firstVertexIndex;
    public static int lastVertexIndex;
    public static int vertexIndex;
    public static CCRenderPipeline pipeline = new CCRenderPipeline();

    //context
    public static int baseColour;
    public static int alphaOverride;
    public static boolean useNormals;
    public static LightMatrix lightMatrix = new LightMatrix();

    //vertex outputs
    public static Vertex5 vert = new Vertex5();
    public static boolean hasNormal;
    public static Vector3 normal = new Vector3();
    public static boolean hasColour;
    public static int colour;
    public static boolean hasBrightness;
    public static int brightness;

    //attrute storage
    public static int side;
    public static LC lc = new LC();

    public static void reset() {
        model = null;
        pipeline.reset();
        hasNormal = hasColour = hasBrightness = false;
        baseColour = alphaOverride = -1;
    }

    public static void setPipeline(IVertexOperation... ops) {
        pipeline.setPipeline(ops);
    }

    public static void setPipeline(IVertexSource model, int start, int end, IVertexOperation... ops) {
        pipeline.reset();
        setModel(model, start, end);
        pipeline.setPipeline(ops);
    }

    public static void bindModel(IVertexSource model) {
        if(CCRenderState.model != model) {
            CCRenderState.model = model;
            pipeline.rebuild();
        }
    }

    public static void setModel(IVertexSource source) {
        setModel(source, 0, source.getVertices().length);
    }

    public static void setModel(IVertexSource source, int start, int end) {
        bindModel(source);
        firstVertexIndex = start;
        lastVertexIndex = end;
    }

    public static void render(IVertexOperation... ops) {
        setPipeline(ops);
        render();
    }

    public static void render() {
        Vertex5[] verts = model.getVertices();
        for(vertexIndex = firstVertexIndex; vertexIndex < lastVertexIndex; vertexIndex++) {
            model.prepareVertex();
            vert.set(verts[vertexIndex]);
            runPipeline();
            writeVert();
        }
    }

    public static void runPipeline() {
        pipeline.operate();
    }

    public static void writeVert() {
        if(hasNormal)
            Tessellator.instance.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
        if(hasColour)
            Tessellator.instance.setColorRGBA(colour>>>24, colour>>16 & 0xFF, colour>>8 & 0xFF, alphaOverride >= 0 ? alphaOverride : colour & 0xFF);
        if(hasBrightness)
            Tessellator.instance.setBrightness(brightness);
        Tessellator.instance.addVertexWithUV(vert.vec.x, vert.vec.y, vert.vec.z, vert.uv.u, vert.uv.v);
    }

    public static void setNormal(double x, double y, double z) {
        hasNormal = true;
        normal.set(x, y, z);
    }

    public static void setNormal(Vector3 n) {
        hasNormal = true;
        normal.set(n);
    }

    public static void setColour(int c) {
        hasColour = true;
        colour = c;
    }

    public static void setBrightness(int b) {
        hasBrightness = true;
        brightness = b;
    }

    public static void setBrightness(IBlockAccess world, int x, int y, int z) {
        setBrightness(world.getBlock(x, y, z).getMixedBrightnessForBlock(world, x, y, z));
    }

    public static void pullLightmap() {
        setBrightness((int)OpenGlHelper.lastBrightnessY << 16 | (int)OpenGlHelper.lastBrightnessX);
    }

    public static void changeTexture(String texture) {
        changeTexture(new ResourceLocation(texture));
    }

    public static void changeTexture(ResourceLocation texture) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
    }

    public static void startDrawing() {
        startDrawing(7);
    }

    private static void startDrawing(int mode) {
        Tessellator.instance.startDrawing(mode);
        if(hasColour)
            Tessellator.instance.setColorRGBA(colour>>>24, colour>>16 & 0xFF, colour>>8 & 0xFF, alphaOverride >= 0 ? alphaOverride : colour & 0xFF);
        if(hasBrightness)
            Tessellator.instance.setBrightness(brightness);
    }

    public static void draw() {
        Tessellator.instance.draw();
    }
}
