package codechicken.lib.vec.uv;

import codechicken.lib.math.MathHelper;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.util.TripleABC;
import codechicken.lib.vec.IrreversibleTransformationException;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by covers1624 on 8/29/2016.
 * This UVTransform is used to assign "reserved" vertex indexes for an icon transform.
 */
public class IconVertexRangeUVTransform extends UVTransformation {

    private final ImmutableList<TripleABC<Integer, Integer, TextureAtlasSprite>> transformMap;

    private IconVertexRangeUVTransform(List<TripleABC<Integer, Integer, TextureAtlasSprite>> transformMap) {
        this.transformMap = ImmutableList.copyOf(transformMap);
    }

    /**
     * Creates a new Builder for the IconVertexRangeUVTransform.
     *
     * @return The Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void operate(CCRenderState state) {
        UV uv = state.vert.uv;
        int index = state.vertexIndex;
        TextureAtlasSprite sprite = getSpriteForVertexIndex(index);
        if (sprite == null) {
            return;
        }
        uv.u = sprite.getInterpolatedU(uv.u * 16);
        uv.v = sprite.getInterpolatedV(uv.v * 16);
        state.sprite = getSpriteForVertexIndex(state.vertexIndex);
    }

    @Override
    public void apply(UV uv) {
        //We override operate as we need CCRS access.
    }

    @Override
    public UVTransformation inverse() {
        throw new IrreversibleTransformationException(this);
    }

    /**
     * Retrieves a TextureAtlasSprite from the internal map based on a vertex index.
     *
     * @param index The vertex index.
     * @return Returns the Icon, Null if no transform for the vertex index.
     */
    public TextureAtlasSprite getSpriteForVertexIndex(int index) {
        for (TripleABC<Integer, Integer, TextureAtlasSprite> entry : transformMap) {
            if (MathHelper.between(entry.getA(), index, entry.getB())) {
                return entry.getC();
            }
        }
        return null;
    }

    /**
     * Builds a IconVertexRangeUVTransform.
     */
    public static class Builder {

        private LinkedList<TripleABC<Integer, Integer, TextureAtlasSprite>> transformMap;

        private Builder() {
            transformMap = new LinkedList<TripleABC<Integer, Integer, TextureAtlasSprite>>();
        }

        /**
         * Builds the IconVertexRangeUVTransform, Should be safe to call this more than once.
         *
         * @return The IconVertexRangeUVTransform.
         */
        public IconVertexRangeUVTransform build() {
            return new IconVertexRangeUVTransform(transformMap);
        }

        /**
         * Reserves a range for a specific transform.
         *
         * @param start  The start index to reserve.
         * @param end    The end vertex to reserve.
         * @param sprite The sprite to transform to in the range.
         * @return The same instance of the builder.
         * @throws IllegalArgumentException Thrown if the specified vertex range is already reserved.
         */
        public Builder addTransform(int start, int end, TextureAtlasSprite sprite) {
            for (TripleABC<Integer, Integer, TextureAtlasSprite> entry : transformMap) {
                if (MathHelper.between(entry.getA(), start, entry.getB()) || MathHelper.between(entry.getA(), end, entry.getB())) {
                    throw new IllegalArgumentException("Unable to have overlapping sprite transforms!");
                }
            }
            transformMap.add(new TripleABC<Integer, Integer, TextureAtlasSprite>(start, end, sprite));
            return this;
        }

    }
}
