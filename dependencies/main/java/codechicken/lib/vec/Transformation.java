package codechicken.lib.vec;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.IVertexOperation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Abstract supertype for any 3D vector transformation
 */
public abstract class Transformation extends ITransformation<Vector3, Transformation> implements IVertexOperation {

    public static final int operationIndex = CCRenderState.registerOperation();

    /**
     * Applies this transformation to a normal (doesn't translate)
     *
     * @param normal The normal to transform
     */
    public abstract void applyN(Vector3 normal);

    /**
     * Applies this transformation to a matrix as a multiplication on the right hand side.
     *
     * @param mat The matrix to combine this transformation with
     */
    public abstract void apply(Matrix4 mat);

    public Transformation at(Vector3 point) {
        return new TransformationList(new Translation(-point.x, -point.y, -point.z), this, point.translation());
    }

    public TransformationList with(Transformation t) {
        return new TransformationList(this, t);
    }

    @SideOnly (Side.CLIENT)
    public abstract void glApply();

    @Override
    public boolean load(CCRenderState state) {
        state.pipeline.addRequirement(state.normalAttrib.operationID());
        return !isRedundant();
    }

    @Override
    public void operate(CCRenderState state) {
        apply(state.vert.vec);
        if (state.normalAttrib.active) {
            applyN(state.normal);
        }
    }

    @Override
    public int operationID() {
        return operationIndex;
    }
}
