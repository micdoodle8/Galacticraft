package codechicken.lib.model.bakery;

import codechicken.lib.colour.Colour;
import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.util.ArrayUtils;
import codechicken.lib.util.Copyable;
import codechicken.lib.util.VectorUtils;
import codechicken.lib.vec.Transformation;
import codechicken.lib.vec.Vector3;
import codechicken.lib.vec.Vertex5;
import codechicken.lib.vec.uv.UV;
import codechicken.lib.vec.uv.UVTransformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.IVertexProducer;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by covers1624 on 8/20/2016.
 * Basically just a holder for quads before baking.
 */
public class CCQuad implements Copyable<CCQuad>, IVertexProducer {

    public Vertex5[] vertices = new Vertex5[4];
    public Vector3[] normals = new Vector3[4];
    public Colour[] colours = new Colour[4];
    public Integer[] lightMaps = new Integer[4];

    public EnumFacing face = null;
    public int tintIndex = -1;
    public boolean applyDifuseLighting = true;
    public TextureAtlasSprite sprite;

    public CCQuad() {
    }

    public CCQuad(Vertex5... vertices) {
        if (vertices.length > 4) {
            throw new IllegalArgumentException("CCQuad is a... Quad.. only 3 or 4 vertices allowed!");
        }
        for (int i = 0; i < 4; i++) {
            this.vertices[i] = vertices[i].copy();
        }
    }

    public CCQuad(BakedQuad quad) {
        this();

        VertexFormat format = quad.getFormat();
        face = quad.getFace();
        tintIndex = quad.getTintIndex();
        sprite = quad.getSprite();
        ArrayUtils.fillArray(vertices, new Vertex5());
        UnpackingVertexConsumer consumer = new UnpackingVertexConsumer(quad.getFormat());
        quad.pipe(consumer);
        float[][][] unpackedData = consumer.getUnpackedData();
        for (int v = 0; v < 4; v++) {
            for (int e = 0; e < format.getElementCount(); e++) {
                float[] data = unpackedData[v][e];
                switch (format.getElement(e).getUsage()) {
                    case POSITION:
                        vertices[v].vec.set(data);
                        break;
                    case NORMAL:
                        normals[v] = new Vector3(data);
                        break;
                    case COLOR:
                        colours[v] = new ColourRGBA(data[0], data[1], data[2], data[3]);
                        break;
                    case UV:
                        if (format.getElement(e).getIndex() == 0) {
                            vertices[v].uv.set(data[0], data[1]);
                        } else {//TODO This SHOULD be fine.....
                            lightMaps[v] = (int) (data[1] * 65535 / 32) << 20 | (int) (data[0] * 65535 / 32) << 4;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        if (!format.hasColor()) {
            ArrayUtils.fillArray(colours, new ColourRGBA(0xFFFFFFFF));
        }
        if (!format.hasUvOffset(1)) {
            ArrayUtils.fillArray(lightMaps, 0);
        }
        if (!format.hasNormal()) {
            computeNormals();
        }
    }

    public CCQuad(CCQuad quad) {
        this();
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = quad.vertices[i].copy();
        }
        for (int i = 0; i < vertices.length; i++) {
            normals[i] = quad.normals[i].copy();
        }
        for (int i = 0; i < vertices.length; i++) {
            colours[i] = quad.colours[i].copy();
        }
        System.arraycopy(quad.lightMaps, 0, lightMaps, 0, vertices.length);
        face = quad.face;
        tintIndex = quad.tintIndex;
        applyDifuseLighting = quad.applyDifuseLighting;
        sprite = quad.sprite;
    }

    public void apply(Transformation... transforms) {
        for (Transformation t : transforms) {
            apply(t);
        }
    }

    public void apply(UVTransformation... transforms) {
        for (UVTransformation t : transforms) {
            apply(t);
        }
    }

    public void apply(Transformation t) {
        quadulate();
        if (ArrayUtils.countNoNull(normals) != 4) {
            computeNormals();
        }
        for (int i = 0; i < 4; i++) {
            Vertex5 vert = vertices[i];
            Vector3 normal = normals[i];
            t.apply(vert.vec);
            t.applyN(normal);
        }
    }

    public void apply(UVTransformation t) {
        quadulate();
        for (int i = 0; i < 4; i++) {
            Vertex5 vert = vertices[i];
            t.apply(vert.uv);
        }
    }

    public boolean isQuads() {
        int counter = ArrayUtils.countNoNull(vertices);
        return counter == 4;
    }

    public boolean hasTint() {
        return tintIndex != -1;
    }

    /**
     * Quadulates the quad by copying any element at index 2 to index 3 only if there are 3 of any given element.
     */
    public void quadulate() {
        int verticesCount = ArrayUtils.countNoNull(vertices);
        int normalCount = ArrayUtils.countNoNull(normals);
        int colourCount = ArrayUtils.countNoNull(colours);
        int lightMapCount = ArrayUtils.countNoNull(lightMaps);
        if (verticesCount == 3) {
            vertices[3] = vertices[2].copy();
        }
        if (normalCount == 3) {
            normals[3] = normals[2].copy();
        }
        if (colourCount == 3) {
            colours[3] = colours[2].copy();
        }
        if (lightMapCount == 3) {
            lightMaps[3] = lightMaps[2];
        }
    }

    /**
     * Creates a set of normals for the quad.
     * Will attempt to Quadulate the model first.
     */
    public void computeNormals() {
        if (ArrayUtils.countNoNull(normals) != 4) {
            quadulate();
            Vector3 normal = VectorUtils.calculateNormal(vertices[0].vec, vertices[1].vec, vertices[3].vec);

            for (int i = 0; i < 4; i++) {
                normals[i] = normal.copy();
            }
        }
    }

    public EnumFacing getQuadFace() {
        if (face == null) {
            if (ArrayUtils.countNoNull(normals) != 4) {
                computeNormals();
            }
            face = VectorUtils.calcNormalSide(normals[0]);
        }
        return face;
    }

    public BakedQuad bake() {
        return bake(DefaultVertexFormats.BLOCK);
    }

    public BakedQuad bake(VertexFormat format) {
        quadulate();
        computeNormals();
        UnpackedBakedQuad.Builder quadBuilder = new UnpackedBakedQuad.Builder(format);
        quadBuilder.setApplyDiffuseLighting(applyDifuseLighting);
        quadBuilder.setTexture(sprite);
        quadBuilder.setQuadOrientation(getQuadFace());
        quadBuilder.setQuadTint(tintIndex);
        for (int v = 0; v < 4; v++) {
            for (int e = 0; e < format.getElementCount(); e++) {
                VertexFormatElement element = format.getElement(e);
                switch (element.getUsage()) {
                    case POSITION:
                        Vector3 pos = vertices[v].vec;
                        quadBuilder.put(e, (float) pos.x, (float) pos.y, (float) pos.z, 1);
                        break;
                    case NORMAL:
                        Vector3 normal = normals[v];
                        quadBuilder.put(e, (float) normal.x, (float) normal.y, (float) normal.z, 0);
                        break;
                    case COLOR:
                        Colour colour = colours[v];
                        quadBuilder.put(e, (colour.r & 0xFF) / 255, (colour.g & 0xFF) / 255, (colour.b & 0xFF) / 255, (colour.a & 0xFF) / 255);
                        break;
                    case UV:
                        if (element.getIndex() == 0) {
                            UV uv = vertices[v].uv;
                            quadBuilder.put(e, (float) uv.u, (float) uv.v, 0, 1);
                        } else {
                            int brightness = lightMaps[v];
                            quadBuilder.put(e, (float) ((brightness >> 4) & 15 * 32) / 65535, (float) ((brightness >> 20) & 15 * 32) / 65535, 0, 1);
                        }
                        break;
                    case PADDING:
                    case GENERIC:
                    default:
                        quadBuilder.put(e);
                }
            }
        }
        return quadBuilder.build();
    }

    @Override
    public void pipe(IVertexConsumer consumer) {
        quadulate();
        computeNormals();
        consumer.setApplyDiffuseLighting(applyDifuseLighting);
        consumer.setTexture(sprite);
        consumer.setQuadOrientation(getQuadFace());
        consumer.setQuadTint(tintIndex);
        for (int v = 0; v < 4; v++) {
            for (int e = 0; e < consumer.getVertexFormat().getElementCount(); e++) {
                VertexFormatElement element = consumer.getVertexFormat().getElement(e);
                switch (element.getUsage()) {
                    case POSITION:
                        Vector3 pos = vertices[v].vec;
                        consumer.put(e, (float) pos.x, (float) pos.y, (float) pos.z, 1);
                        break;
                    case NORMAL:
                        Vector3 normal = normals[v];
                        consumer.put(e, (float) normal.x, (float) normal.y, (float) normal.z, 0);
                        break;
                    case COLOR:
                        Colour colour = colours[v];
                        consumer.put(e, (colour.r & 0xFF) / 255, (colour.g & 0xFF) / 255, (colour.b & 0xFF) / 255, (colour.a & 0xFF) / 255);
                        break;
                    case UV:
                        if (element.getIndex() == 0) {
                            UV uv = vertices[v].uv;
                            consumer.put(e, (float) uv.u, (float) uv.v, 0, 1);
                        } else {
                            int brightness = lightMaps[v];
                            consumer.put(e, (float) ((brightness >> 4) & 15 * 32) / 65535, (float) ((brightness >> 20) & 15 * 32) / 65535, 0, 1);
                        }
                        break;
                    case PADDING:
                    case GENERIC:
                    default:
                        consumer.put(e);
                }
            }
        }
    }

    public static List<CCQuad> fromArray(List<BakedQuad> bakedQuads) {
        List<CCQuad> quads = new LinkedList<CCQuad>();
        for (BakedQuad quad : bakedQuads) {
            quads.add(new CCQuad(quad));
        }
        return quads;
    }

    @Override
    public CCQuad copy() {
        return new CCQuad(this);
    }
}
