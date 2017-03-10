package codechicken.lib.render;

import codechicken.lib.vec.RedundantTransformation;
import codechicken.lib.vec.Transformation;
import codechicken.lib.vec.Vector3;
import codechicken.lib.vec.uv.UV;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by covers1624 on 6/21/2016.
 * TODO Support MatLib files.
 * TODO Custom Mat file for loading LightModels, calculating normals and applying the lighting and rotations.
 */
public class CCOBJParser {

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
     *
     * @param res The resource for the obj file
     * @return A map of group names to models
     */
    public static Map<String, CCModel> parseObjModels(ResourceLocation res) {
        return parseObjModels(res, null);
    }

    /**
     * Parses vertices, texture coords, normals and polygons from a WaveFront Obj file
     *
     * @param res         The resource for the obj file
     * @param coordSystem The cooridnate system transformation to apply
     * @return A map of group names to models
     */
    public static Map<String, CCModel> parseObjModels(ResourceLocation res, Transformation coordSystem) {
        return parseObjModels(res, 4, coordSystem);
    }

    /**
     * Parses vertices, texture coords, normals and polygons from a WaveFront Obj file
     *
     * @param res         The resource for the obj file
     * @param vertexMode  The vertex mode to create the model for (GL_TRIANGLES or GL_QUADS)
     * @param coordSystem The cooridnate system transformation to apply
     * @return A map of group names to models
     */
    public static Map<String, CCModel> parseObjModels(ResourceLocation res, int vertexMode, Transformation coordSystem) {
        try {
            return parseObjModels(Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream(), vertexMode, coordSystem);
        } catch (Exception e) {
            throw new RuntimeException("failed to load model: " + res, e);
        }
    }

    /**
     * Parses vertices, texture coords, normals and polygons from a WaveFront Obj file
     *
     * @param input       An input stream to a obj file
     * @param vertexMode  The vertex mode to create the model for (GL_TRIANGLES or GL_QUADS)
     * @param coordSystem The cooridnate system transformation to apply
     * @return A map of group names to models
     * @throws IOException
     */
    public static Map<String, CCModel> parseObjModels(InputStream input, int vertexMode, Transformation coordSystem) throws IOException {
        if (coordSystem == null) {
            coordSystem = new RedundantTransformation();
        }
        int vp = vertexMode == 7 ? 4 : 3;

        HashMap<String, CCModel> modelMap = new HashMap<String, CCModel>();
        ArrayList<Vector3> verts = new ArrayList<Vector3>();
        ArrayList<Vector3> uvs = new ArrayList<Vector3>();
        ArrayList<Vector3> normals = new ArrayList<Vector3>();
        ArrayList<int[]> polys = new ArrayList<int[]>();
        String modelName = "unnamed";

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.replaceAll("\\s+", " ").trim();
            if (line.startsWith("#") || line.length() == 0) {
                continue;
            }

            if (line.startsWith("v ")) {
                assertMatch(vertMatcher, line);
                double[] values = parseDoubles(line.substring(2), " ");
                illegalAssert(values.length >= 3, "Vertices must have x, y and z components");
                Vector3 vert = new Vector3(values[0], values[1], values[2]);
                coordSystem.apply(vert);
                verts.add(vert);
                continue;
            }
            if (line.startsWith("vt ")) {
                assertMatch(uvwMatcher, line);
                double[] values = parseDoubles(line.substring(3), " ");
                illegalAssert(values.length >= 2, "Tex Coords must have u, and v components");
                uvs.add(new Vector3(values[0], 1 - values[1], 0));
                continue;
            }
            if (line.startsWith("vn ")) {
                assertMatch(normalMatcher, line);
                double[] values = parseDoubles(line.substring(3), " ");
                illegalAssert(values.length >= 3, "Normals must have x, y and z components");
                Vector3 norm = new Vector3(values[0], values[1], values[2]).normalize();
                coordSystem.applyN(norm);
                normals.add(norm);
                continue;
            }
            if (line.startsWith("f ")) {
                assertMatch(polyMatcher, line);
                String[] av = line.substring(2).split(" ");
                illegalAssert(av.length >= 3, "Polygons must have at least 3 vertices");
                int[][] polyVerts = new int[av.length][3];
                for (int i = 0; i < av.length; i++) {
                    String[] as = av[i].split("/");
                    for (int p = 0; p < as.length; p++) {
                        if (as[p].length() > 0) {
                            polyVerts[i][p] = Integer.parseInt(as[p]);
                        }
                    }
                }
                if (vp == 3) {
                    triangulate(polys, polyVerts);
                } else {
                    quadulate(polys, polyVerts);
                }
            }
            if (line.startsWith("g ")) {
                if (!polys.isEmpty()) {
                    modelMap.put(modelName, CCModel.createModel(verts, uvs, normals, vertexMode, polys));
                    polys.clear();
                }
                modelName = line.substring(2);
            }
        }

        if (!polys.isEmpty()) {
            modelMap.put(modelName, CCModel.createModel(verts, uvs, normals, vertexMode, polys));
        }

        return modelMap;
    }

    /**
     * Exports an Map of CCModels to a File.
     *
     * @param models Map of models to export.
     * @param p      PrintWriter to write the model to.
     */
    public static void exportObj(Map<String, CCModel> models, PrintWriter p) {
        List<Vector3> verts = new ArrayList<Vector3>();
        List<UV> uvs = new ArrayList<UV>();
        List<Vector3> normals = new ArrayList<Vector3>();
        List<int[]> polys = new ArrayList<int[]>();
        for (Map.Entry<String, CCModel> e : models.entrySet()) {
            p.println("g " + e.getKey());
            CCModel m = e.getValue();

            int vStart = verts.size();
            int uStart = uvs.size();
            int nStart = normals.size();
            boolean hasNormals = m.normals() != null;
            polys.clear();

            for (int i = 0; i < m.verts.length; i++) {
                int[] ia = new int[hasNormals ? 3 : 2];
                ia[0] = addIndex(verts, m.verts[i].vec);
                ia[1] = addIndex(uvs, m.verts[i].uv);
                if (hasNormals) {
                    ia[2] = addIndex(normals, m.normals()[i]);
                }
                polys.add(ia);
            }

            if (vStart < verts.size()) {
                p.println();
                for (int i = vStart; i < verts.size(); i++) {
                    Vector3 v = verts.get(i);
                    p.format("v %s %s %s\n", clean(v.x), clean(v.y), clean(v.z));
                }
            }
            if (uStart < uvs.size()) {
                p.println();
                for (int i = uStart; i < uvs.size(); i++) {
                    UV uv = uvs.get(i);
                    p.format("vt %s %s\n", clean(uv.u), clean(uv.v));
                }
            }
            if (nStart < normals.size()) {
                p.println();
                for (int i = nStart; i < normals.size(); i++) {
                    Vector3 n = normals.get(i);
                    p.format("vn %s %s %s\n", clean(n.x), clean(n.y), clean(n.z));
                }
            }

            p.println();
            for (int i = 0; i < polys.size(); i++) {
                if (i % m.vp == 0) {
                    p.format("f");
                }
                int[] ia = polys.get(i);
                if (hasNormals) {
                    p.format(" %d/%d/%d", ia[0], ia[1], ia[2]);
                } else {
                    p.format(" %d/%d", ia[0], ia[1]);
                }
                if (i % m.vp == m.vp - 1) {
                    p.println();
                }
            }
        }
    }

    private static <T> int addIndex(List<T> list, T elem) {
        int i = list.indexOf(elem) + 1;
        if (i == 0) {
            list.add(elem);
            i = list.size();
        }
        return i;
    }

    private static String clean(double d) {
        return d == (int) d ? Integer.toString((int) d) : Double.toString(d);
    }

    private static void assertMatch(Matcher m, String s) {
        m.reset(s);
        illegalAssert(m.matches(), "Malformed line: " + s);
    }

    private static void illegalAssert(boolean b, String err) {
        if (!b) {
            throw new IllegalArgumentException(err);
        }
    }

    private static double[] parseDoubles(String s, String token) {
        String[] as = s.split(token);
        double[] values = new double[as.length];
        for (int i = 0; i < as.length; i++) {
            values[i] = Double.parseDouble(as[i]);
        }
        return values;
    }

    private static void triangulate(List<int[]> polys, int[][] polyVerts) {
        for (int i = 2; i < polyVerts.length; i++) {
            polys.add(polyVerts[0]);
            polys.add(polyVerts[i]);
            polys.add(polyVerts[i - 1]);
        }
    }

    private static void quadulate(List<int[]> polys, int[][] polyVerts) {
        if (polyVerts.length == 4) {
            polys.add(polyVerts[0]);
            polys.add(polyVerts[3]);
            polys.add(polyVerts[2]);
            polys.add(polyVerts[1]);
        } else {
            for (int i = 2; i < polyVerts.length; i++) {
                polys.add(polyVerts[0]);
                polys.add(polyVerts[i]);
                polys.add(polyVerts[i - 1]);
                polys.add(polyVerts[i - 1]);
            }
        }
    }
}
