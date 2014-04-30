package codechicken.lib.render.uv;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.vec.ITransformation;

/**
 * Abstract supertype for any UV transformation
 */
public abstract class UVTransformation extends ITransformation<UV, UVTransformation> implements CCRenderState.IVertexOperation
{
    public static final int operationIndex = CCRenderState.registerOperation();

    public UVTransformation at(UV point) {
        return new UVTransformationList(new UVTranslation(-point.u, -point.v), this, new UVTranslation(point.u, point.v));
    }

    public UVTransformationList with(UVTransformation t) {
        return new UVTransformationList(this, t);
    }

    @Override
    public boolean load() {
        return !isRedundant();
    }

    @Override
    public void operate() {
        apply(CCRenderState.vert.uv);
    }

    @Override
    public int operationID() {
        return operationIndex;
    }
}


