package codechicken.lib.model.modelbase;

import codechicken.lib.colour.Colour;
import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.model.bakery.CCQuad;
import codechicken.lib.vec.Vertex5;

/**
 * Created by covers1624 on 8/24/2016.
 * TODO CCModel.
 */
public class CCModelBox {

    private final CCQuad[] quads;
    public final float posX1;
    public final float posY1;
    public final float posZ1;
    public final float posX2;
    public final float posY2;
    public final float posZ2;
    public String boxName;

    public CCModelBox(CCModelRenderer renderer, int textureX, int textureY, float xOffset, float yOffset, float zOffset, int width, int height, int depth, float scaleFactor) {
        this(renderer, textureX, textureY, xOffset, yOffset, zOffset, width, height, depth, scaleFactor, renderer.mirror);
    }

    public CCModelBox(CCModelRenderer renderer, int textureX, int textureY, float xOffset, float yOffset, float zOffset, int width, int height, int depth, float scaleFactor, boolean mirrored) {

        this.posX1 = xOffset;
        this.posY1 = yOffset;
        this.posZ1 = zOffset;
        this.posX2 = xOffset + (float) width;
        this.posY2 = yOffset + (float) height;
        this.posZ2 = zOffset + (float) depth;
        this.quads = new CCQuad[6];
        float f = xOffset + (float) width;
        float f1 = yOffset + (float) height;
        float f2 = zOffset + (float) depth;
        xOffset = xOffset - scaleFactor;
        yOffset = yOffset - scaleFactor;
        zOffset = zOffset - scaleFactor;
        f = f + scaleFactor;
        f1 = f1 + scaleFactor;
        f2 = f2 + scaleFactor;

        if (mirrored) {
            float f3 = f;
            f = xOffset;
            xOffset = f3;
        }
        Vertex5[] verts = new Vertex5[8];
        verts[0] = new Vertex5(xOffset, yOffset, zOffset, 0.0F, 0.0F);
        verts[1] = new Vertex5(f, yOffset, zOffset, 0.0F, 8.0F);
        verts[2] = new Vertex5(f, f1, zOffset, 8.0F, 8.0F);
        verts[3] = new Vertex5(xOffset, f1, zOffset, 8.0F, 0.0F);
        verts[4] = new Vertex5(xOffset, yOffset, f2, 0.0F, 0.0F);
        verts[5] = new Vertex5(f, yOffset, f2, 0.0F, 8.0F);
        verts[6] = new Vertex5(f, f1, f2, 8.0F, 8.0F);
        verts[7] = new Vertex5(xOffset, f1, f2, 8.0F, 0.0F);
        quads[0] = setTextureCoords(new CCQuad(verts[5], verts[1], verts[2], verts[6]), textureX + depth + width, textureY + depth, textureX + depth + width + depth, textureY + depth + height, renderer.textureWidth, renderer.textureHeight);
        quads[1] = setTextureCoords(new CCQuad(verts[0], verts[4], verts[7], verts[3]), textureX, textureY + depth, textureX + depth, textureY + depth + height, renderer.textureWidth, renderer.textureHeight);
        quads[2] = setTextureCoords(new CCQuad(verts[5], verts[4], verts[0], verts[1]), textureX + depth, textureY, textureX + depth + width, textureY + depth, renderer.textureWidth, renderer.textureHeight);
        quads[3] = setTextureCoords(new CCQuad(verts[2], verts[3], verts[7], verts[6]), textureX + depth + width, textureY + depth, textureX + depth + width + width, textureY, renderer.textureWidth, renderer.textureHeight);
        quads[4] = setTextureCoords(new CCQuad(verts[1], verts[0], verts[3], verts[2]), textureX + depth, textureY + depth, textureX + depth + width, textureY + depth + height, renderer.textureWidth, renderer.textureHeight);
        quads[5] = setTextureCoords(new CCQuad(verts[4], verts[5], verts[6], verts[7]), textureX + depth + width + depth, textureY + depth, textureX + depth + width + depth + width, textureY + depth + height, renderer.textureWidth, renderer.textureHeight);
        for (CCQuad quad : quads) {
            quad.computeNormals();
            fillQuadData(quad);
            if (mirrored) {
                flipQuad(quad);
            }
        }
    }

    private CCQuad setTextureCoords(CCQuad quad, int texCoordU1, int texCoordV1, int texCoordU2, int texCoordV2, double textureWidth, double textureHeight) {
        double f = 0.0D / textureWidth;
        double f1 = 0.0D / textureHeight;
        quad.vertices[0].uv.set(texCoordU2 / textureWidth - f, texCoordV1 / textureHeight + f1);
        quad.vertices[1].uv.set(texCoordU1 / textureWidth + f, texCoordV1 / textureHeight + f1);
        quad.vertices[2].uv.set(texCoordU1 / textureWidth + f, texCoordV2 / textureHeight - f1);
        quad.vertices[2].uv.set(texCoordU2 / textureWidth - f, texCoordV2 / textureHeight - f1);
        return quad;
    }

    private CCQuad fillQuadData(CCQuad quad) {
        Colour[] colours = new Colour[quad.colours.length];
        for (int i = 0; i < quad.colours.length; i++) {
            Colour colour = quad.colours[i];
            if (colour == null) {
                colour = new ColourRGBA(0xFFFFFFFF);
            }
            colours[i] = colour;
        }
        quad.colours = colours;

        Integer[] lightMaps = new Integer[quad.lightMaps.length];
        for (int i = 0; i < quad.lightMaps.length; i++) {
            Integer lightMap = quad.lightMaps[i];
            if (lightMap == null) {
                lightMap = 0;
            }
            lightMaps[i] = lightMap;
        }
        quad.lightMaps = lightMaps;
        return quad;
    }

    public CCQuad flipQuad(CCQuad quad) {
        Vertex5[] verts = new Vertex5[4];
        verts[0] = quad.vertices[3];
        verts[1] = quad.vertices[2];
        verts[2] = quad.vertices[1];
        verts[3] = quad.vertices[0];
        quad.vertices = verts;
        return quad;
    }

    public CCQuad[] getQuads() {
        CCQuad[] quads = new CCQuad[this.quads.length];
        for (int i = 0; i < quads.length; i++) {
            quads[i] = this.quads[i].copy();
        }
        return quads;
    }

    public CCModelBox setBoxName(String name) {
        this.boxName = name;
        return this;
    }
}
