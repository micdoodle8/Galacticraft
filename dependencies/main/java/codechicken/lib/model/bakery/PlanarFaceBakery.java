package codechicken.lib.model.bakery;

import codechicken.lib.colour.Colour;
import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.util.VertexDataUtils;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.lib.vec.Vertex5;
import codechicken.lib.vec.uv.IconTransformation;
import codechicken.lib.vec.uv.UV;
import codechicken.lib.vec.uv.UVTransformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by covers1624 on 26/10/2016.
 */
//TODO General cleanup.
public class PlanarFaceBakery {

    public static BakedQuad bakeFace(EnumFacing face, TextureAtlasSprite sprite) {
        return bakeFace(face, sprite, DefaultVertexFormats.ITEM);
    }

    public static BakedQuad bakeFace(EnumFacing face, TextureAtlasSprite sprite, VertexFormat format) {
        return bakeFace(face, sprite, format, 0xFFFFFFFF);
    }

    public static BakedQuad bakeFace(EnumFacing face, TextureAtlasSprite sprite, VertexFormat format, int colour) {
        return bakeFace(face, sprite, format, new ColourRGBA(colour));
    }

    public static BakedQuad bakeFace(EnumFacing face, TextureAtlasSprite sprite, VertexFormat format, Colour colour) {
        UVTransformation t = new IconTransformation(sprite);

        double x1 = Cuboid6.full.min.x;
        double x2 = Cuboid6.full.max.x;
        double y1 = Cuboid6.full.min.y;
        double y2 = Cuboid6.full.max.y;
        double z1 = Cuboid6.full.min.z;
        double z2 = Cuboid6.full.max.z;
        double u1;
        double u2;
        double v1;
        double v2;
        Vertex5 vert1;
        Vertex5 vert2;
        Vertex5 vert3;
        Vertex5 vert4;

        switch (face) {
            case DOWN:
                u1 = x1;
                v1 = z1;
                u2 = x2;
                v2 = z2;
                vert1 = new Vertex5(x1, y1, z2, u1, v2);
                vert2 = new Vertex5(x1, y1, z1, u1, v1);
                vert3 = new Vertex5(x2, y1, z1, u2, v1);
                vert4 = new Vertex5(x2, y1, z2, u2, v2);
                return buildQuad(format, sprite, face, colour, t, vert1, vert2, vert3, vert4);
            case UP:
                u1 = x1;
                v1 = z1;
                u2 = x2;
                v2 = z2;
                vert1 = new Vertex5(x2, y2, z2, u2, v2);
                vert2 = new Vertex5(x2, y2, z1, u2, v1);
                vert3 = new Vertex5(x1, y2, z1, u1, v1);
                vert4 = new Vertex5(x1, y2, z2, u1, v2);
                return buildQuad(format, sprite, face, colour, t, vert1, vert2, vert3, vert4);
            case NORTH:
                u1 = 1 - x1;
                v1 = 1 - y2;
                u2 = 1 - x2;
                v2 = 1 - y1;
                vert1 = new Vertex5(x1, y1, z1, u1, v2);
                vert2 = new Vertex5(x1, y2, z1, u1, v1);
                vert3 = new Vertex5(x2, y2, z1, u2, v1);
                vert4 = new Vertex5(x2, y1, z1, u2, v2);
                return buildQuad(format, sprite, face, colour, t, vert1, vert2, vert3, vert4);
            case SOUTH:
                u1 = x1;
                v1 = 1 - y2;
                u2 = x2;
                v2 = 1 - y1;
                vert1 = new Vertex5(x2, y1, z2, u2, v2);
                vert2 = new Vertex5(x2, y2, z2, u2, v1);
                vert3 = new Vertex5(x1, y2, z2, u1, v1);
                vert4 = new Vertex5(x1, y1, z2, u1, v2);
                return buildQuad(format, sprite, face, colour, t, vert1, vert2, vert3, vert4);
            case WEST:
                u1 = z1;
                v1 = 1 - y2;
                u2 = z2;
                v2 = 1 - y1;
                vert1 = new Vertex5(x1, y1, z2, u2, v2);
                vert2 = new Vertex5(x1, y2, z2, u2, v1);
                vert3 = new Vertex5(x1, y2, z1, u1, v1);
                vert4 = new Vertex5(x1, y1, z1, u1, v2);
                return buildQuad(format, sprite, face, colour, t, vert1, vert2, vert3, vert4);

            case EAST:
                u1 = 1 - z1;
                v1 = 1 - y2;
                u2 = 1 - z2;
                v2 = 1 - y1;
                vert1 = new Vertex5(x2, y1, z1, u1, v2);
                vert2 = new Vertex5(x2, y2, z1, u1, v1);
                vert3 = new Vertex5(x2, y2, z2, u2, v1);
                vert4 = new Vertex5(x2, y1, z2, u2, v2);
                return buildQuad(format, sprite, face, colour, t, vert1, vert2, vert3, vert4);
        }
        //This case will never happen. only here due to INTELLIJ NOT SHUTTING UP ABOUT POTENTIAL NULLPOINTERS!
        return new BakedQuad(null, 1, null, null, true, null);
    }

    private static BakedQuad buildQuad(VertexFormat format, TextureAtlasSprite sprite, EnumFacing face, Colour colour, UVTransformation t, Vertex5 v1, Vertex5 v2, Vertex5 v3, Vertex5 v4) {
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setQuadTint(-1);
        builder.setQuadOrientation(face);
        builder.setTexture(sprite);

        t.apply(v1.uv);
        t.apply(v2.uv);
        t.apply(v3.uv);
        t.apply(v4.uv);
        putVertex(builder, format, face, v1, colour);
        putVertex(builder, format, face, v2, colour);
        putVertex(builder, format, face, v3, colour);
        putVertex(builder, format, face, v4, colour);

        return VertexDataUtils.copyQuad(builder.build());
    }

    private static void putVertex(UnpackedBakedQuad.Builder builder, VertexFormat format, EnumFacing face, Vertex5 vert, Colour colour) {
        for (int e = 0; e < format.getElementCount(); e++) {
            VertexFormatElement element = format.getElement(e);
            switch (element.getUsage()) {

                case POSITION:
                    Vector3 vec = vert.vec;
                    builder.put(e, (float) vec.x, (float) vec.y, (float) vec.z, 1);
                    break;
                case NORMAL:
                    builder.put(e, face.getFrontOffsetX(), face.getFrontOffsetY(), face.getFrontOffsetZ(), 0);
                    break;
                case COLOR:
                    builder.put(e, (colour.r & 0xFF) / 255F, (colour.g & 0xFF) / 255F, (colour.b & 0xFF) / 255F, (colour.a & 0xFF) / 255F);
                    break;
                case UV:
                    UV uv = vert.uv;
                    builder.put(e, (float) uv.u, (float) uv.v, 0, 1);
                    break;
                default:
                    builder.put(e);
                    break;
            }
        }
    }

    public static List<BakedQuad> shadeQuadFaces(BakedQuad... quads) {
        return shadeQuadFaces(Arrays.asList(quads));
    }

    public static List<BakedQuad> shadeQuadFaces(List<BakedQuad> quads) {
        LinkedList<BakedQuad> shadedQuads = new LinkedList<BakedQuad>();
        for (BakedQuad quad : quads) {
            int[] rawData = quad.getVertexData();
            for (int v = 0; v < 4; v++) {
                for (int e = 0; e < quad.getFormat().getElementCount(); e++) {
                    VertexFormatElement element = quad.getFormat().getElement(e);
                    if (element.getUsage() == EnumUsage.COLOR) {
                        float[] data = new float[4];
                        LightUtil.unpack(rawData, data, quad.getFormat(), v, e);

                        data = diffuseFaceLight(quad.getFace(), data);

                        LightUtil.pack(data, rawData, quad.getFormat(), v, e);
                    }
                }
            }
            shadedQuads.add(new BakedQuad(rawData, quad.getTintIndex(), quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat()));
        }

        return shadedQuads;
    }

    private static float[] diffuseFaceLight(EnumFacing face, float[] colour) {
        double diffuse;
        switch (face) {
            case DOWN:
                diffuse = 0.5D;
                break;
            case NORTH:
            case SOUTH:
                diffuse = 0.8D;
                break;
            case WEST:
            case EAST:
                diffuse = 0.6D;
                break;
            case UP:
            default:
                diffuse = 1.0D;
                break;
        }

        colour[0] *= diffuse;
        colour[1] *= diffuse;
        colour[2] *= diffuse;

        return colour;
    }

}
