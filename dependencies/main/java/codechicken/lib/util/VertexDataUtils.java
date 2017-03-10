package codechicken.lib.util;

import codechicken.lib.math.MathHelper;
import codechicken.lib.vec.uv.UV;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.renderer.vertex.VertexFormatElement.EnumUsage;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by covers1624 on 4/10/2016.
 * Utilities for anything to do with raw vertex data access.
 */
public class VertexDataUtils {

    /**
     * Gets the position for the element 'position' in the elements list for use in LightUtil.pack/unpack for a given format.
     *
     * @param format The format.
     * @return The element position, -1 if it does not exist.
     */
    public static int getPositionElement(VertexFormat format) {
        for (int e = 0; e < format.getElementCount(); e++) {
            if (format.getElement(e).isPositionElement()) {
                return e;
            }
        }
        return -1;
    }

    /**
     * Gets the position for the element 'normal' in the elements list for use in LightUtil.pack/unpack for a given format.
     *
     * @param format The format.
     * @return The element position, -1 if it does not exist.
     */
    public static int getNormalElement(VertexFormat format) {
        for (int e = 0; e < format.getElementCount(); e++) {
            if (format.getElement(e).getUsage() == EnumUsage.NORMAL) {
                return e;
            }
        }
        return -1;
    }

    /**
     * Gets the position for the element 'uv' in the elements list for use in LightUtil.pack/unpack for a given format.
     *
     * @param format The format.
     * @return The element position, -1 if it does not exist.
     */
    public static int getUVElement(VertexFormat format) {
        for (int e = 0; e < format.getElementCount(); e++) {
            if (format.getElement(e).getUsage() == EnumUsage.UV && format.getElement(e).getIndex() == 0) {
                return e;
            }
        }
        return -1;
    }

    /**
     * Gets the position for the element provided in the elements list for use in LightUtil.pack/unpack for a given format.
     *
     * @param format  The format.
     * @param element THe element to get.
     * @return The element position, -1 if it does not exist.
     */
    public static int getElement(VertexFormat format, VertexFormatElement element) {
        for (int e = 0; e < format.getElementCount(); e++) {
            if (format.getElement(e).equals(element)) {
                return e;
            }
        }
        return -1;
    }

    /**
     * Attempts to get the TextureAtlasSprite for a given UV mapping.
     * This is not threaded and will search EVERY sprite loaded in the texture map.
     * This is meant to be a last resort, where possible always try to avoid using this or have this be fired.
     * TODO Improve searching by caching value ranges somehow.
     *
     * @param textureMap The TextureMap to search.
     * @param uv         The UV mapping to find.
     * @return The TextureAtlasSprite found, returns missing icon if it hasn't been found.
     */
    public static TextureAtlasSprite getSpriteForUV(TextureMap textureMap, UV uv) {
        for (TextureAtlasSprite sprite : textureMap.mapUploadedSprites.values()) {
            if (MathHelper.between(sprite.getMinU(), uv.u, sprite.getMaxU()) && MathHelper.between(sprite.getMinV(), uv.v, sprite.getMaxV())) {
                return sprite;
            }
        }
        return textureMap.getMissingSprite();
    }

    /**
     * Copies the data from a UnpackedBakedQuad to a normal baked quad to save space in ram.
     *
     * @param quad The UnpackedBakedQuad to copy from.
     * @return The copied BakedQuad.
     */
    public static BakedQuad copyQuad(UnpackedBakedQuad quad) {
        return new BakedQuad(quad.getVertexData(), quad.getTintIndex(), quad.getFace(), quad.getSprite(), quad.shouldApplyDiffuseLighting(), quad.getFormat());
    }

    public static Map<EnumFacing, List<BakedQuad>> sortFaceData(List<BakedQuad> quads) {
        Map<EnumFacing, List<BakedQuad>> faceQuadMap = new HashMap<EnumFacing, List<BakedQuad>>();
        for (BakedQuad quad : quads) {
            List<BakedQuad> faceQuads = faceQuadMap.get(quad.getFace());
            if (faceQuads == null) {
                faceQuads = new ArrayList<BakedQuad>();
                faceQuadMap.put(quad.getFace(), faceQuads);
            }
            faceQuads.add(quad);
        }
        return faceQuadMap;
    }

    public static void fullyPackQuads(int[] packedData, float[][][] unpackedData, VertexFormat format) {
        for (int e = 0; e < format.getElementCount(); e++) {
            for (int v = 0; v < 4; v++) {
                LightUtil.pack(unpackedData[v][e], packedData, format, v, e);
            }
        }
    }

    public static void fullyUnPackQuads(int[] packedData, float[][][] unpackedData, VertexFormat format) {
        for (int e = 0; e < format.getElementCount(); e++) {
            for (int v = 0; v < 4; v++) {
                LightUtil.unpack(packedData, unpackedData[v][e], format, v, e);
            }
        }
    }

}
