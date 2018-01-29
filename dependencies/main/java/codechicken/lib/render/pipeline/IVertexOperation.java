package codechicken.lib.render.pipeline;

import codechicken.lib.render.CCRenderState;

/**
 * Represents an operation to be run for each vertex that operates on and modifies the current state
 */
public interface IVertexOperation {

    /**
     * Load any required references and add dependencies to the pipeline based on the current model (may be null)
     * Return false if this operation is redundant in the pipeline with the given model
     */
    boolean load(CCRenderState state);

    /**
     * Perform the operation on the current render state
     */
    void operate(CCRenderState state);

    /**
     * Get the unique id representing this type of operation. Duplicate operation IDs within the pipeline may have unexpected results.
     * ID shoulld be obtained from CCRenderState.registerOperation() and stored in a static variable
     */
    int operationID();
}
