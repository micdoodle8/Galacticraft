package codechicken.lib.render.pipeline;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.attribute.AttributeKey;
import codechicken.lib.util.ArrayUtils;

/**
 * Management class for a vertex attribute such as colour, normal etc
 * This class should handle the loading of the attribute from an array provided by IVertexSource.getAttributes or the computation of this attribute from others
 *
 * @param <T> The array type for this attribute eg. int[], Vector3[]
 */
public abstract class VertexAttribute<T> implements IVertexOperation {

    private final int operationIndex = CCRenderState.registerOperation();

    /**
     * Set to true when the attribute is part of the pipeline. Should only be managed by CCRenderState when constructing the pipeline
     */
    public boolean active = false;

    /**
     * Construct a new array for storage of vertex attributes in a model
     */
    public abstract T newArray(int length);

    public abstract String getAttribName();

    @Override
    public int operationID() {
        return operationIndex;
    }

    public static <R> R copyOf(AttributeKey<R> attr, R src, int length) {
        R dst = attr.newArray(length);
        ArrayUtils.arrayCopy(src, 0, dst, 0, ((Object[]) src).length);
        return dst;
    }
}
