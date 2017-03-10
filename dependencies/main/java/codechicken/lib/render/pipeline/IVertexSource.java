package codechicken.lib.render.pipeline;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.attribute.AttributeKey;
import codechicken.lib.vec.Vertex5;

/**
 * Created by covers1624 on 10/10/2016.
 */
public interface IVertexSource {

    Vertex5[] getVertices();

    /**
     * Gets an array of vertex attributes
     *
     * @param attr The vertex attribute to get
     * @param <T>  The attribute array type
     * @return An array, or null if not computed
     */
    <T> T getAttributes(AttributeKey<T> attr);

    /**
     * @return True if the specified attribute is provided by this model, either by returning an array from getAttributes or by setting the state in prepareVertex
     */
    boolean hasAttribute(AttributeKey<?> attr);

    /**
     * Callback to set CCRenderState for a vertex before the pipeline runs
     */
    void prepareVertex(CCRenderState state);
}
