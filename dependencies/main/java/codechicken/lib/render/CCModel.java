package codechicken.lib.render;

import codechicken.lib.lighting.LC;
import codechicken.lib.lighting.LightModel;
import codechicken.lib.render.uv.UV;
import codechicken.lib.render.uv.UVTransformation;
import codechicken.lib.render.uv.UVTranslation;
import codechicken.lib.util.Copyable;
import codechicken.lib.vec.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static codechicken.lib.vec.Rotation.sideRotations;

public class CCModel implements CCRenderState.IVertexSource, Copyable<CCModel>
{
    private static class PositionNormalEntry
    {
        public Vector3 pos;
        public LinkedList<Vector3> normals = new LinkedList<Vector3>();

        public PositionNormalEntry(Vector3 position)
        {
            pos = position;
        }

        public boolean positionEqual(Vector3 v)
        {
            return pos.x == v.x && pos.y == v.y && pos.z == v.z;
        }

        public PositionNormalEntry addNormal(Vector3 normal)
        {
            normals.add(normal);
            return this;
        }
    }

    public final int vertexMode;
    public final int vp;
    public Vertex5[] verts;
    public ArrayList<Object> attributes = new ArrayList<Object>();

    protected CCModel(int vertexMode)
    {
        if(vertexMode != 7 && vertexMode != 4)
            throw new IllegalArgumentException("Models must be GL_QUADS or GL_TRIANGLES");

        this.vertexMode = vertexMode;
        vp = vertexMode == 7 ? 4 : 3;
    }

    public Vector3[] normals() {
        return getAttributes(CCRenderState.normalAttrib);
    }

    @Override
    public Vertex5[] getVertices() {
        return verts;
    }

    @Override
    public <T> T getAttributes(CCRenderState.VertexAttribute<T> attr) {
        if(attr.attributeIndex < attributes.size())
            return (T) attributes.get(attr.attributeIndex);

        return null;
    }

    @Override
    public boolean hasAttribute(CCRenderState.VertexAttribute<?> attrib) {
        return attrib.attributeIndex < attributes.size() && attributes.get(attrib.attributeIndex) != null;
    }

    @Override
    public void prepareVertex() {
    }

    public <T> T getOrAllocate(CCRenderState.VertexAttribute<T> attrib) {
        T array = getAttributes(attrib);
        if(array == null) {
            while(attributes.size() <= attrib.attributeIndex)
                attributes.add(null);
            attributes.set(attrib.attributeIndex, array = attrib.newArray(verts.length));
        }
        return array;
    }

    /**
     * Each pixel corresponds to one unit of position when generating the model
     * @param i Vertex index to start generating at
     * @param x1 The minX bound of the box
     * @param y1 The minY bound of the box
     * @param z1 The minZ bound of the box
     * @param w The width of the box
     * @param h The height of the box
     * @param d The depth of the box
     * @param tx The distance of the top left corner of the texture map from the left in pixels
     * @param ty The distance of the top left corner of the texture map from the top in pixels
     * @param tw The width of the texture in pixels
     * @param th The height of the texture in pixels
     * @param f The scale of the model, pixels per block, normally 16
     * @return The generated model
     */
    public CCModel generateBox(int i, double x1, double y1, double z1, double w, double h, double d, double tx, double ty, double tw, double th, double f)
    {
        double u1, v1, u2, v2;
        double x2 = x1+w;
        double y2 = y1+h;
        double z2 = z1+d;
        x1 /= f; x2 /= f; y1 /= f; y2 /= f; z1 /= f; z2 /= f;

        //bottom face
        u1 = (tx + d + w) / tw; v1 = (ty + d) / th;
        u2 = (tx + d*2 + w) / tw; v2 = ty / th;
        verts[i++] = new Vertex5(x1, y1, z2, u1, v2);
        verts[i++] = new Vertex5(x1, y1, z1, u1, v1);
        verts[i++] = new Vertex5(x2, y1, z1, u2, v1);
        verts[i++] = new Vertex5(x2, y1, z2, u2, v2);

        //top face
        u1 = (tx + d) / tw; v1 = (ty + d) / th;
        u2 = (tx + d + w) / tw; v2 = ty / th;
        verts[i++] = new Vertex5(x2, y2, z2, u2, v2);
        verts[i++] = new Vertex5(x2, y2, z1, u2, v1);
        verts[i++] = new Vertex5(x1, y2, z1, u1, v1);
        verts[i++] = new Vertex5(x1, y2, z2, u1, v2);

        //front face
        u1 = (tx + d + w) / tw; v1 = (ty + d) / th;
        u2 = (tx + d) / tw; v2 = (ty + d + h) / th;
        verts[i++] = new Vertex5(x1, y2, z1, u2, v1);
        verts[i++] = new Vertex5(x2, y2, z1, u1, v1);
        verts[i++] = new Vertex5(x2, y1, z1, u1, v2);
        verts[i++] = new Vertex5(x1, y1, z1, u2, v2);

        //back face
        u1 = (tx + d*2 + w*2) / tw; v1 = (ty + d) / th;
        u2 = (tx + d*2 + w) / tw; v2 = (ty + d + h) / th;
        verts[i++] = new Vertex5(x1, y2, z2, u1, v1);
        verts[i++] = new Vertex5(x1, y1, z2, u1, v2);
        verts[i++] = new Vertex5(x2, y1, z2, u2, v2);
        verts[i++] = new Vertex5(x2, y2, z2, u2, v1);

        //left face
        u1 = (tx + d) / tw; v1 = (ty + d) / th;
        u2 = (tx) / tw; v2 = (ty + d + h) / th;
        verts[i++] = new Vertex5(x1, y2, z2, u2, v1);
        verts[i++] = new Vertex5(x1, y2, z1, u1, v1);
        verts[i++] = new Vertex5(x1, y1, z1, u1, v2);
        verts[i++] = new Vertex5(x1, y1, z2, u2, v2);

        //right face
        u1 = (tx + d*2 + w) / tw; v1 = (ty + d) / th;
        u2 = (tx + d + w) / tw; v2 = (ty + d + h) / th;
        verts[i++] = new Vertex5(x2, y1, z2, u1, v2);
        verts[i++] = new Vertex5(x2, y1, z1, u2, v2);
        verts[i++] = new Vertex5(x2, y2, z1, u2, v1);
        verts[i++] = new Vertex5(x2, y2, z2, u1, v1);

        return this;
    }

    /**
     * Generates a box, uv mapped to be the same as a minecraft block with the same bounds
     * @param i The vertex index to start generating at
     * @param bounds The bounds of the block, 0 to 1
     * @return The generated model. When rendering an icon will need to be supplied for the UV transformation.
     */
    public CCModel generateBlock(int i, Cuboid6 bounds)
    {
        return generateBlock(i, bounds, 0);
    }

    public CCModel generateBlock(int i, Cuboid6 bounds, int mask)
    {
        return generateBlock(i, bounds.min.x, bounds.min.y, bounds.min.z, bounds.max.x, bounds.max.y, bounds.max.z, mask);
    }

    public CCModel generateBlock(int i, double x1, double y1, double z1, double x2, double y2, double z2)
    {
        return generateBlock(i, x1, y1, z1, x2, y2, z2, 0);
    }

    /**
     * Generates a box, uv mapped to be the same as a minecraft block with the same bounds
     * @param i The vertex index to start generating at
     * @param x1 minX
     * @param y1 minY
     * @param z1 minZ
     * @param x2 maxX
     * @param y2 maxY
     * @param z2 maxZ
     * @param mask A bitmask of sides NOT to generate. I high bit at index s means side s will not be generated
     * @return The generated model. When rendering an icon will need to be supplied for the UV transformation.
     */
    public CCModel generateBlock(int i, double x1, double y1, double z1, double x2, double y2, double z2, int mask)
    {
        double u1, v1, u2, v2;

        if((mask&1) == 0) {//bottom face
            u1 = x1; v1 = z1;
            u2 = x2; v2 = z2;
            verts[i++] = new Vertex5(x1, y1, z2, u1, v2, 0);
            verts[i++] = new Vertex5(x1, y1, z1, u1, v1, 0);
            verts[i++] = new Vertex5(x2, y1, z1, u2, v1, 0);
            verts[i++] = new Vertex5(x2, y1, z2, u2, v2, 0);
        }

        if((mask&2) == 0) {//top face
            u1 = x1; v1 = z1;
            u2 = x2; v2 = z2;
            verts[i++] = new Vertex5(x2, y2, z2, u2, v2, 1);
            verts[i++] = new Vertex5(x2, y2, z1, u2, v1, 1);
            verts[i++] = new Vertex5(x1, y2, z1, u1, v1, 1);
            verts[i++] = new Vertex5(x1, y2, z2, u1, v2, 1);
        }

        if((mask&4) == 0) {//east face
            u1 = 1-x1; v1 = 1-y2;
            u2 = 1-x2; v2 = 1-y1;
            verts[i++] = new Vertex5(x1, y1, z1, u1, v2, 2);
            verts[i++] = new Vertex5(x1, y2, z1, u1, v1, 2);
            verts[i++] = new Vertex5(x2, y2, z1, u2, v1, 2);
            verts[i++] = new Vertex5(x2, y1, z1, u2, v2, 2);
        }

        if((mask&8) == 0) {//west face
            u1 = x1; v1 = 1-y2;
            u2 = x2; v2 = 1-y1;
            verts[i++] = new Vertex5(x2, y1, z2, u2, v2, 3);
            verts[i++] = new Vertex5(x2, y2, z2, u2, v1, 3);
            verts[i++] = new Vertex5(x1, y2, z2, u1, v1, 3);
            verts[i++] = new Vertex5(x1, y1, z2, u1, v2, 3);
        }

        if((mask&0x10) == 0) {//north face
            u1 = z1; v1 = 1-y2;
            u2 = z2; v2 = 1-y1;
            verts[i++] = new Vertex5(x1, y1, z2, u2, v2, 4);
            verts[i++] = new Vertex5(x1, y2, z2, u2, v1, 4);
            verts[i++] = new Vertex5(x1, y2, z1, u1, v1, 4);
            verts[i++] = new Vertex5(x1, y1, z1, u1, v2, 4);
        }

        if((mask&0x20) == 0) {//south face
            u1 = 1-z1; v1 = 1-y2;
            u2 = 1-z2; v2 = 1-y1;
            verts[i++] = new Vertex5(x2, y1, z1, u1, v2, 5);
            verts[i++] = new Vertex5(x2, y2, z1, u1, v1, 5);
            verts[i++] = new Vertex5(x2, y2, z2, u2, v1, 5);
            verts[i++] = new Vertex5(x2, y1, z2, u2, v2, 5);
        }

        return this;
    }

    public CCModel computeNormals()
    {
        return computeNormals(0, verts.length);
    }

    /**
     * Computes the normals of all faces in the model.
     * Uses the cross product of the vectors along 2 sides of the face
     * @param start The first vertex to generate normals for
     * @param length The number of vertices to generate normals for. Note this must be a multiple of 3 for triangles or 4 for quads
     * @return The model
     */
    public CCModel computeNormals(int start, int length)
    {
        if(length%vp != 0 || start%vp != 0)
            throw new IllegalArgumentException("Cannot generate normals across polygons");

        Vector3[] normals = getOrAllocate(CCRenderState.normalAttrib);
        for(int k = 0; k < length; k+=vp)
        {
            int i = k + start;
            Vector3 diff1 = verts[i+1].vec.copy().subtract(verts[i].vec);
            Vector3 diff2 = verts[i+vp-1].vec.copy().subtract(verts[i].vec);
            normals[i] = diff1.crossProduct(diff2).normalize();
            for(int d = 1; d < vp; d++)
                normals[i+d] = normals[i].copy();
        }

        return this;
    }

    /**
     * Computes lighting using the normals add a light model
     * If the model is rotated, the lighting will no longer be valid
     * @return The model
     */
    public CCModel computeLighting(LightModel light)
    {
        Vector3[] normals = normals();
        int[] colours = getAttributes(CCRenderState.lightingAttrib);
        if(colours == null) {
            colours = getOrAllocate(CCRenderState.lightingAttrib);
            Arrays.fill(colours, -1);
        }
        for(int k = 0; k < verts.length; k++)
            colours[k] = light.apply(colours[k], normals[k]);
        return this;
    }

    public CCModel setColour(int c)
    {
        int[] colours = getOrAllocate(CCRenderState.colourAttrib);
        Arrays.fill(colours, c);
        return this;
    }

    /**
     * Computes the minecraft lighting coordinates for use with a LightMatrix
     * @return The model
     */
    public CCModel computeLightCoords() {
        LC[] lcs = getOrAllocate(CCRenderState.lightCoordAttrib);
        Vector3[] normals = normals();
        for(int i = 0; i < verts.length; i++)
            lcs[i] = new LC().compute(verts[i].vec, normals[i]);
        return this;
    }

    /**
     * Averages all normals at the same position to produce a smooth lighting effect.
     * @return The model
     */
    public CCModel smoothNormals()
    {
        ArrayList<PositionNormalEntry> map = new ArrayList<PositionNormalEntry>();
        Vector3[] normals = normals();
        nextvert: for(int k = 0; k < verts.length; k++)
        {
            Vector3 vec = verts[k].vec;
            for(PositionNormalEntry e : map)
                if(e.positionEqual(vec))
                {
                    e.addNormal(normals[k]);
                    continue nextvert;
                }

            map.add(new PositionNormalEntry(vec).addNormal(normals[k]));
        }

        for(PositionNormalEntry e : map)
        {
            if(e.normals.size() <= 1)
                continue;

            Vector3 new_n = new Vector3();
            for(Vector3 n : e.normals)
                new_n.add(n);

            new_n.normalize();
            for(Vector3 n : e.normals)
                n.set(new_n);
        }

        return this;
    }

    public CCModel apply(Transformation t)
    {
        for(int k = 0; k < verts.length; k++)
            verts[k].apply(t);

        Vector3[] normals = normals();
        if(normals != null)
            for(int k = 0; k < normals.length; k++)
                t.applyN(normals[k]);

        return this;
    }

    public CCModel apply(UVTransformation uvt)
    {
        for(int k = 0; k < verts.length; k++)
            verts[k].apply(uvt);

        return this;
    }

    public CCModel expand(int extraVerts)
    {
        int newLen = verts.length+extraVerts;
        verts = Arrays.copyOf(verts, newLen);
        for(int i = 0; i < attributes.size(); i++)
            if(attributes.get(i) != null)
                attributes.set(i, CCRenderState.copyOf((CCRenderState.VertexAttribute)CCRenderState.getAttribute(i), attributes.get(i), newLen));

        return this;
    }

    public void render(double x, double y, double z, double u, double v)
    {
        render(new Vector3(x, y, z).translation(), new UVTranslation(u, v));
    }

    public void render(double x, double y, double z, UVTransformation u)
    {
        render(new Vector3(x, y, z).translation(), u);
    }

    public void render(Transformation t, double u, double v)
    {
        render(t, new UVTranslation(u, v));
    }

    public void render(CCRenderState.IVertexOperation... ops)
    {
        render(0, verts.length, ops);
    }

    /**
     * Renders vertices start through start+length-1 of the model
     * @param start The first vertex index to render
     * @param end The vertex index to render until
     * @param ops Operations to apply
     */
    public void render(int start, int end, CCRenderState.IVertexOperation... ops)
    {
        CCRenderState.setPipeline(this, start, end, ops);
        CCRenderState.render();
    }

    public static CCModel quadModel(int numVerts)
    {
        return newModel(7, numVerts);
    }

    public static CCModel triModel(int numVerts)
    {
        return newModel(4, numVerts);
    }

    public static CCModel newModel(int vertexMode, int numVerts)
    {
        CCModel model = newModel(vertexMode);
        model.verts = new Vertex5[numVerts];
        return model;
    }

    public static CCModel newModel(int vertexMode)
    {
        return new CCModel(vertexMode);
    }

    public static double[] parseDoubles(String s, String token)
    {
        String[] as = s.split(token);
        double[] values = new double[as.length];
        for(int i = 0; i < as.length; i++)
            values[i] = Double.parseDouble(as[i]);
        return values;
    }

    public static void illegalAssert(boolean b, String err)
    {
        if(!b) throw new IllegalArgumentException(err);
    }

    public static void assertMatch(Matcher m, String s)
    {
        m.reset(s);
        illegalAssert(m.matches(), "Malformed line: "+s);
    }

    private static final Pattern vertPattern = Pattern.compile("v(?: ([\\d\\.+-]+))+");
    private static final Pattern uvwPattern = Pattern.compile("vt(?: ([\\d\\.+-]+))+");
    private static final Pattern normalPattern = Pattern.compile("vn(?: ([\\d\\.+-]+))+");
    private static final Pattern polyPattern = Pattern.compile("f(?: ((?:\\d*)(?:/\\d*)?(?:/\\d*)?))+");
    public static final Matcher vertMatcher = vertPattern.matcher("");
    public static final Matcher uvwMatcher = uvwPattern.matcher("");
    public static final Matcher normalMatcher = normalPattern.matcher("");
    public static final Matcher polyMatcher = polyPattern.matcher("");

    /**
     * Parses vertices, texture coords, normals and polygons from a WaveFront Obj file
     * @param input An input stream to a obj file
     * @param vertexMode The vertex mode to create the model for (GL_TRIANGLES or GL_QUADS)
     * @param coordSystem The cooridnate system transformation to apply
     * @return A map of group names to models
     * @throws IOException
     */
    public static Map<String, CCModel> parseObjModels(InputStream input, int vertexMode, Transformation coordSystem) throws IOException
    {
        if(coordSystem == null)
            coordSystem = new RedundantTransformation();
        int vp = vertexMode == 7 ? 4 : 3;

        HashMap<String, CCModel> modelMap = new HashMap<String, CCModel>();
        ArrayList<Vector3> verts = new ArrayList<Vector3>();
        ArrayList<Vector3> uvs = new ArrayList<Vector3>();
        ArrayList<Vector3> normals = new ArrayList<Vector3>();
        ArrayList<int[]> polys = new ArrayList<int[]>();
        String modelName = "unnamed";

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        String line;
        while((line = reader.readLine()) != null)
        {
            line = line.replaceAll("\\s+", " ").trim();
            if(line.startsWith("#") || line.length() == 0)
                continue;

            if(line.startsWith("v "))
            {
                assertMatch(vertMatcher, line);
                double[] values = parseDoubles(line.substring(2), " ");
                illegalAssert(values.length >= 3, "Vertices must have x, y and z components");
                Vector3 vert = new Vector3(values[0], values[1], values[2]);
                coordSystem.apply(vert);
                verts.add(vert);
                continue;
            }
            if(line.startsWith("vt "))
            {
                assertMatch(uvwMatcher, line);
                double[] values = parseDoubles(line.substring(3), " ");
                illegalAssert(values.length >= 2, "Tex Coords must have u, and v components");
                uvs.add(new Vector3(values[0], 1-values[1], 0));
                continue;
            }
            if(line.startsWith("vn "))
            {
                assertMatch(normalMatcher, line);
                double[] values = parseDoubles(line.substring(3), " ");
                illegalAssert(values.length >= 3, "Normals must have x, y and z components");
                Vector3 norm = new Vector3(values[0], values[1], values[2]).normalize();
                coordSystem.applyN(norm);
                normals.add(norm);
                continue;
            }
            if(line.startsWith("f "))
            {
                assertMatch(polyMatcher, line);
                String[] av = line.substring(2).split(" ");
                illegalAssert(av.length >= 3, "Polygons must have at least 3 vertices");
                int[][] polyVerts = new int[av.length][3];
                for(int i = 0; i < av.length; i++)
                {
                    String[] as = av[i].split("/");
                    for(int p = 0; p < as.length; p++)
                        if(as[p].length() > 0)
                            polyVerts[i][p] = Integer.parseInt(as[p]);
                }
                if(vp == 3)
                    triangulate(polys, polyVerts);
                else
                    quadulate(polys, polyVerts);
            }
            if(line.startsWith("g "))
            {
                if(!polys.isEmpty())
                {
                    modelMap.put(modelName, createModel(verts, uvs, normals, vertexMode, polys));
                    polys.clear();
                }
                modelName = line.substring(2);
            }
        }

        if(!polys.isEmpty())
            modelMap.put(modelName, createModel(verts, uvs, normals, vertexMode, polys));

        return modelMap;
    }

    public static void triangulate(List<int[]> polys, int[][] polyVerts)
    {
        for(int i = 2; i < polyVerts.length; i++)
        {
            polys.add(polyVerts[0]);
            polys.add(polyVerts[i]);
            polys.add(polyVerts[i-1]);
        }
    }

    public static void quadulate(List<int[]> polys, int[][] polyVerts)
    {
        if(polyVerts.length == 4)
        {
            polys.add(polyVerts[0]);
            polys.add(polyVerts[3]);
            polys.add(polyVerts[2]);
            polys.add(polyVerts[1]);
        }
        else
        {
            for(int i = 2; i < polyVerts.length; i++)
            {
                polys.add(polyVerts[0]);
                polys.add(polyVerts[i]);
                polys.add(polyVerts[i-1]);
                polys.add(polyVerts[i-1]);
            }
        }
    }

    /**
     * Parses vertices, texture coords, normals and polygons from a WaveFront Obj file
     * @param res The resource for the obj file
     * @return A map of group names to models
     */
    public static Map<String, CCModel> parseObjModels(ResourceLocation res)
    {
        return parseObjModels(res, 4, null);
    }
    /**
     * Parses vertices, texture coords, normals and polygons from a WaveFront Obj file
     * @param res The resource for the obj file
     * @param coordSystem The cooridnate system transformation to apply
     * @return A map of group names to models
     */
    public static Map<String, CCModel> parseObjModels(ResourceLocation res, Transformation coordSystem)
    {
        try
        {
            return parseObjModels(
                    Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream(),
                    4, coordSystem);
        }
        catch(IOException e)
        {
            throw new RuntimeException("failed to load model: "+res, e);
        }
    }

    /**
     * Parses vertices, texture coords, normals and polygons from a WaveFront Obj file
     * @param res The resource for the obj file
     * @param vertexMode The vertex mode to create the model for (GL_TRIANGLES or GL_QUADS)
     * @param coordSystem The cooridnate system transformation to apply
     * @return A map of group names to models
     */
    public static Map<String, CCModel> parseObjModels(ResourceLocation res, int vertexMode, Transformation coordSystem)
    {
        try
        {
            return parseObjModels(
                    Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream(),
                    vertexMode, coordSystem);
        }
        catch(Exception e)
        {
            throw new RuntimeException("failed to load model: "+res, e);
        }
    }

    public static CCModel createModel(List<Vector3> verts, List<Vector3> uvs, List<Vector3> normals, int vertexMode, List<int[]> polys)
    {
        int vp = vertexMode == 7 ? 4 : 3;
        if(polys.size() < vp || polys.size()%vp != 0)
            throw new IllegalArgumentException("Invalid number of vertices for model: "+polys.size());

        boolean hasNormals = polys.get(0)[2] > 0;
        CCModel model = CCModel.newModel(vertexMode, polys.size());
        if(hasNormals)
            model.getOrAllocate(CCRenderState.normalAttrib);

        for(int i = 0; i < polys.size(); i++)
        {
            int[] ai = polys.get(i);
            Vector3 vert = verts.get(ai[0]-1).copy();
            Vector3 uv = ai[1] <= 0 ? new Vector3() : uvs.get(ai[1]-1).copy();
            if(ai[2] > 0 != hasNormals)
                throw new IllegalArgumentException("Normals are an all or nothing deal here.");

            model.verts[i] = new Vertex5(vert, uv.x, uv.y);
            if(hasNormals)
                model.normals()[i] = normals.get(ai[2]-1).copy();
        }

        return model;
    }

    private static <T> int addIndex(List<T> list, T elem)
    {
        int i = list.indexOf(elem)+1;
        if(i == 0) {
            list.add(elem);
            i = list.size();
        }
        return i;
    }

    private static String clean(double d) {
        return d == (int) d ? Integer.toString((int)d) : Double.toString(d);
    }

    public static void exportObj(Map<String, CCModel> models, PrintWriter p)
    {
        List<Vector3> verts = new ArrayList<Vector3>();
        List<UV> uvs = new ArrayList<UV>();
        List<Vector3> normals = new ArrayList<Vector3>();
        List<int[]> polys = new ArrayList<int[]>();
        for(Map.Entry<String, CCModel> e : models.entrySet()) {
            p.println("g "+e.getKey());
            CCModel m = e.getValue();

            int vStart = verts.size();
            int uStart = uvs.size();
            int nStart = normals.size();
            boolean hasNormals = m.normals() != null;
            polys.clear();

            for(int i = 0; i < m.verts.length; i++) {
                int[] ia = new int[hasNormals ? 3 : 2];
                ia[0] = addIndex(verts, m.verts[i].vec);
                ia[1] = addIndex(uvs, m.verts[i].uv);
                if(hasNormals)
                    ia[2] = addIndex(normals, m.normals()[i]);
                polys.add(ia);
            }

            if(vStart < verts.size()) {
                p.println();
                for(int i = vStart; i < verts.size(); i++) {
                    Vector3 v = verts.get(i);
                    p.format("v %s %s %s\n", clean(v.x), clean(v.y), clean(v.z));
                }
            }
            if(uStart < uvs.size()) {
                p.println();
                for(int i = uStart; i < uvs.size(); i++) {
                    UV uv = uvs.get(i);
                    p.format("vt %s %s\n", clean(uv.u), clean(uv.v));
                }
            }
            if(nStart < normals.size()) {
                p.println();
                for(int i = nStart; i < normals.size(); i++) {
                    Vector3 n = normals.get(i);
                    p.format("vn %s %s %s\n", clean(n.x), clean(n.y), clean(n.z));
                }
            }

            p.println();
            for(int i = 0; i < polys.size(); i++) {
                if(i%m.vp == 0)
                    p.format("f");
                int[] ia = polys.get(i);
                if(hasNormals)
                    p.format(" %d/%d/%d", ia[0], ia[1], ia[2]);
                else
                    p.format(" %d/%d", ia[0], ia[1]);
                if(i%m.vp == m.vp-1)
                    p.println();
            }
        }
    }

    /**
     * Brings the UV coordinates of each face closer to the center UV by d.
     * Useful for fixing texture seams
     */
    public CCModel shrinkUVs(double d)
    {
        for(int k = 0; k < verts.length; k+=vp)
        {
            UV uv = new UV();
            for(int i = 0; i < vp; i++)
            {
                uv.add(verts[k+i].uv);
            }
            uv.multiply(1D/vp);
            for(int i = 0; i < vp; i++)
            {
                Vertex5 vert = verts[k+i];
                vert.uv.u += vert.uv.u < uv.u ? d : -d;
                vert.uv.v += vert.uv.v < uv.v ? d : -d;
            }
        }
        return this;
    }

    /**
     * @param side1 The side of this model
     * @param side2 The side of the new model
     * @param point The point to rotate around
     * @return A copy of this model rotated to the appropriate side
     */
    public CCModel sidedCopy(int side1, int side2, Vector3 point)
    {
        return copy().apply(new TransformationList(sideRotations[side1].inverse(), sideRotations[side2]).at(point));
    }

    /**
     * Copies length vertices and normals
     */
    public static void copy(CCModel src, int srcpos, CCModel dst, int destpos, int length)
    {
        for(int k = 0; k < length; k++)
            dst.verts[destpos+k] = src.verts[srcpos+k].copy();

        for(int i = 0; i < src.attributes.size(); i++)
            if(src.attributes.get(i) != null)
                CCRenderState.arrayCopy(src.attributes.get(i), srcpos, dst.getOrAllocate(CCRenderState.getAttribute(i)), destpos, length);
    }

    /**
     * Generate models rotated to the other 5 sides of the block
     * @param models An array of 6 models
     * @param side The side of this model
     * @param point The rotation point
     */
    public static void generateSidedModels(CCModel[] models, int side, Vector3 point)
    {
        for(int s = 0; s < 6; s++)
        {
            if(s == side)
                continue;

            models[s] = models[side].sidedCopy(side, s, point);
        }
    }

    /**
     * Generate models rotated to the other 3 horizontal of the block
     * @param models An array of 4 models
     * @param side The side of this model
     * @param point The rotation point
     */
    public static void generateSidedModelsH(CCModel[] models, int side, Vector3 point)
    {
        for(int s = 2; s < 6; s++)
        {
            if(s == side)
                continue;

            models[s] = models[side].sidedCopy(side, s, point);
        }
    }

    public CCModel backfacedCopy()
    {
        return generateBackface(this, 0, copy(), 0, verts.length);
    }

    /**
     * Generates copies of faces with clockwise vertices
     * @return The model
     */
    public static CCModel generateBackface(CCModel src, int srcpos, CCModel dst, int destpos, int length)
    {
        int vp = src.vp;
        if(srcpos%vp != 0 || destpos%vp != 0 || length%vp != 0)
            throw new IllegalArgumentException("Vertices do not align with polygons");

        int[][] o = new int[][]{{0, 0}, {1, vp-1}, {2, vp-2}, {3, vp-3}};
        for(int i = 0; i < length; i++)
        {
            int b = (i/vp)*vp;
            int d = i%vp;
            int di = destpos+b+o[d][1];
            int si = srcpos+b+o[d][0];
            dst.verts[di] = src.verts[si].copy();
            for(int a = 0; a < src.attributes.size(); a++)
                if(src.attributes.get(a) != null)
                    CCRenderState.arrayCopy(src.attributes.get(a), si, dst.getOrAllocate(CCRenderState.getAttribute(a)), di, 1);

            if(dst.normals() != null && dst.normals()[di] != null)
                dst.normals()[di].negate();
        }
        return dst;
    }

    /**
     * Generates sided copies of vertices into this model.
     * Assumes that your model has been generated at vertex side*(numVerts/6)
     */
    public CCModel generateSidedParts(int side, Vector3 point)
    {
        if(verts.length%(6*vp) != 0)
            throw new IllegalArgumentException("Invalid number of vertices for sided part generation");
        int length = verts.length/6;

        for(int s = 0; s < 6; s++)
        {
            if(s == side)
                continue;

            generateSidedPart(side, s, point, length*side, length*s, length);
        }

        return this;
    }

    /**
     * Generates sided copies of vertices into this model.
     * Assumes that your model has been generated at vertex side*(numVerts/4)
     */
    public CCModel generateSidedPartsH(int side, Vector3 point)
    {
        if(verts.length%(4*vp) != 0)
            throw new IllegalArgumentException("Invalid number of vertices for sided part generation");
        int length = verts.length/4;

        for(int s = 2; s < 6; s++)
        {
            if(s == side)
                continue;

            generateSidedPart(side, s, point, length*(side-2), length*(s-2), length);
        }

        return this;
    }

    /**
     * Generates a sided copy of verts into this model
     */
    public CCModel generateSidedPart(int side1, int side2, Vector3 point, int srcpos, int destpos, int length)
    {
        return apply(new TransformationList(sideRotations[side1].inverse(), sideRotations[side2]).at(point), srcpos, destpos, length);
    }

    /**
     * Generates a rotated copy of verts into this model
     */
    public CCModel apply(Transformation t, int srcpos, int destpos, int length)
    {
        for(int k = 0; k < length; k++)
        {
            verts[destpos+k] = verts[srcpos+k].copy();
            verts[destpos+k].vec.apply(t);
        }

        Vector3[] normals = normals();
        if(normals != null)
            for(int k = 0; k < length; k++) {
                normals[destpos+k] = normals[srcpos+k].copy();
                t.applyN(normals[destpos+k]);
            }

        return this;
    }

    public static CCModel combine(Collection<CCModel> models)
    {
        if(models.isEmpty())
            return null;

        int numVerts = 0;
        int vertexMode = -1;
        for(CCModel model : models)
        {
            if(vertexMode == -1)
                vertexMode = model.vertexMode;
            if(vertexMode != model.vertexMode)
                throw new IllegalArgumentException("Cannot combine models with different vertex modes");

            numVerts+=model.verts.length;
        }

        CCModel c_model = newModel(vertexMode, numVerts);
        int i = 0;
        for(CCModel model : models)
        {
            copy(model, 0, c_model, i, model.verts.length);
            i+=model.verts.length;
        }

        return c_model;
    }

    public CCModel twoFacedCopy()
    {
        CCModel model = newModel(vertexMode, verts.length*2);
        copy(this, 0, model, 0, verts.length);
        return generateBackface(model, 0, model, verts.length, verts.length);
    }

    public CCModel copy()
    {
        CCModel model = newModel(vertexMode, verts.length);
        copy(this, 0, model, 0, verts.length);
        return model;
    }

    /**
     * @return The average of all vertices, for bones.
     */
    public Vector3 collapse()
    {
        Vector3 v = new Vector3();
        for(Vertex5 vert : verts)
            v.add(vert.vec);
        v.multiply(1/(double)verts.length);
        return v;
    }

    public CCModel zOffset(Cuboid6 offsets)
    {
        for(int k = 0; k < verts.length; k++)
        {
            Vertex5 vert = verts[k];
            Vector3 normal = normals()[k];
            switch(findSide(normal))
            {
                case 0:
                    vert.vec.y += offsets.min.y;
                    break;
                case 1:
                    vert.vec.y += offsets.max.y;
                    break;
                case 2:
                    vert.vec.z += offsets.min.z;
                    break;
                case 3:
                    vert.vec.z += offsets.max.z;
                    break;
                case 4:
                    vert.vec.x += offsets.min.x;
                    break;
                case 5:
                    vert.vec.x += offsets.max.x;
                    break;
            }
        }
        return this;
    }

    public static int findSide(Vector3 normal)
    {
        if(normal.y <=-0.99) return 0;
        if(normal.y >= 0.99) return 1;
        if(normal.z <=-0.99) return 2;
        if(normal.z >= 0.99) return 3;
        if(normal.x <=-0.99) return 4;
        if(normal.x >= 0.99) return 5;
        return -1;
    }

    /**
     * @return A Cuboid6 containing all the verts in this model
     */
    public Cuboid6 bounds()
    {
        Vector3 vec1 = verts[0].vec;
        Cuboid6 c = new Cuboid6(vec1.copy(), vec1.copy());
        for(int i = 1; i < verts.length; i++)
            c.enclose(verts[i].vec);
        return c;
    }
}
