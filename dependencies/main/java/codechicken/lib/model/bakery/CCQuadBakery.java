package codechicken.lib.model.bakery;

import codechicken.lib.colour.Colour;
import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.vec.Vector3;
import codechicken.lib.vec.Vertex5;
import codechicken.lib.vec.uv.UV;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;

import java.util.LinkedList;

/**
 * Created by covers1624 on 8/2/2016.
 * Bakes Quads..
 */
@Deprecated//TODO a Tessellator style BakingVertexBuffer.
public class CCQuadBakery {

    private VertexFormat format;
    private TextureAtlasSprite sprite;
    private EnumFacing face;

    //Temp storage
    private UV uv = new UV();
    private Vector3 normal = null;
    private Colour colour = new ColourRGBA(0xFFFFFFFF);
    private int brightness = 0;

    private boolean isBakingTriModel = false;
    private boolean applyDifuseLighting = true;
    private LinkedList<BakedQuad> bakedQuads = null;

    //Active storage.
    private CCQuad quad = null;
    private int index = 0;

    public CCQuadBakery(TextureAtlasSprite sprite) {
        this(DefaultVertexFormats.BLOCK, sprite);
    }

    public CCQuadBakery(VertexFormat format, TextureAtlasSprite sprite) {
        this.format = format;
        this.sprite = sprite;
    }

    public CCQuadBakery startBakingQuads() {
        return startBaking(false);
    }

    public CCQuadBakery startBakingTriangles() {
        return startBaking(true);
    }

    //TODO Have the QuadBakery bake from any DrawMode to any DrawMode and from any VertexFormat to any VertexFormat.
    //TODO Maybe a custom BakedQuad that holds all the raw info still, might make it more possible / cleaner for a VF > VF converter.
    private CCQuadBakery startBaking(boolean isTriangles) {
        if (quad != null || bakedQuads != null) {
            throw new IllegalStateException("Quads are still baking or baking has not finished yet!");
        }
        isBakingTriModel = isTriangles;
        bakedQuads = new LinkedList<BakedQuad>();
        return this;
    }

    public ImmutableList<BakedQuad> finishBaking() {
        if (quad != null) {
            throw new IllegalStateException("Quads are still baking!");
        }
        if (bakedQuads == null) {
            throw new IllegalStateException("The bakery has no baked quads!");
        }
        ImmutableList<BakedQuad> returnQuads = ImmutableList.copyOf(bakedQuads);
        reset();
        return returnQuads;
    }

    public boolean hasIncompleteQuad() {
        return quad != null;
    }

    public void reset() {
        applyDifuseLighting = true;
        isBakingTriModel = false;
        bakedQuads = null;
        quad = null;
        index = 0;
    }

    public CCQuadBakery setSprite(TextureAtlasSprite sprite) {
        if (this.sprite == sprite) {
            return this;
        }
        if (quad != null) {
            throw new IllegalStateException("Unable to set sprite whilst quad is still baking!");
        }
        this.sprite = sprite;
        return this;
    }

    public CCQuadBakery setFace(EnumFacing face) {
        if (quad != null) {
            throw new IllegalStateException("Unable to set face whilst quad is still baking!");
        }
        this.face = face;
        return this;
    }

    public CCQuadBakery disableDifuseLighting() {
        return setDifuseLightingState(false);
    }

    public CCQuadBakery setDifuseLightingState(boolean state) {
        applyDifuseLighting = state;
        return this;
    }

    public CCQuadBakery setColour(int colour) {
        return setColour(new ColourRGBA(colour));
    }

    public CCQuadBakery setColour(Colour colour) {
        this.colour = colour.copy();
        return this;
    }

    public CCQuadBakery setLightMap(int brightness) {
        this.brightness = brightness;
        return this;
        //return setLightMap(new UV((double) ((brightness >> 4) & 15 * 32) / 65535, (double) ((brightness >> 20) & 15 * 32) / 65535));
    }

    public CCQuadBakery setNormal(Vector3 normal) {
        this.normal = normal;
        return this;
    }

    public CCQuadBakery setUV(double u, double v) {
        return setUV(new UV(u, v));
    }

    public CCQuadBakery setUV(UV uv) {
        this.uv = uv.copy();
        return this;
    }

    public CCQuadBakery addVertexWithUV(Vector3 vertex, double u, double v) {
        return addVertexWithUV(vertex, new UV(u, v));
    }

    public CCQuadBakery addVertexWithUV(double x, double y, double z, UV uv) {
        return addVertexWithUV(new Vector3(x, y, z), uv);
    }

    public CCQuadBakery addVertexWithUV(double x, double y, double z, double u, double v) {
        return addVertexWithUV(new Vector3(x, y, z), new UV(u, v));
    }

    public CCQuadBakery addVertexWithUV(Vector3 vertex, UV uv) {
        return addVertexWithUV(new Vertex5(vertex, uv));
    }

    public CCQuadBakery addVertexWithUV(Vertex5 vertex) {
        setUV(vertex.uv);
        return addVertex(vertex.vec);
    }

    public CCQuadBakery addVertex(double x, double y, double z) {
        return addVertex(new Vector3(x, y, z));
    }

    public CCQuadBakery addVertex(Vector3 vertex) {
        if (quad == null) {
            quad = new CCQuad();
            if (face != null) {
                quad.face = face;
            }
            quad.sprite = sprite;
            quad.applyDifuseLighting = applyDifuseLighting;
            index = 0;
        }
        quad.vertices[index] = new Vertex5(vertex.copy(), uv.copy());
        quad.normals[index] = normal != null ? normal.copy() : null;
        quad.colours[index] = colour.copy();
        quad.lightMaps[index] = brightness;
        index++;

        int max = isBakingTriModel ? 3 : 4;

        if (index == max) {
            index = 0;
            bakedQuads.add(quad.bake(format));
            quad = null;
        }
        return this;
    }

}
