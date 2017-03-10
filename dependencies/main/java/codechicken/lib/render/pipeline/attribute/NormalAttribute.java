package codechicken.lib.render.pipeline.attribute;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.VertexAttribute;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;

/**
 * Apples normals to the render operation. If the model is a planar model it uses known normals.
 */
public class NormalAttribute extends VertexAttribute<Vector3[]> {

    public static final AttributeKey<Vector3[]> attributeKey = new AttributeKey<Vector3[]>() {
        @Override
        public Vector3[] newArray(int length) {
            return new Vector3[length];
        }
    };

    private Vector3[] normalRef;

    @Override
    public Vector3[] newArray(int length) {
        return new Vector3[length];
    }

    @Override
    public String getAttribName() {
        return "normalAttrib";
    }

    @Override
    public boolean load(CCRenderState state) {
        normalRef = state.model.getAttributes(NormalAttribute.attributeKey);
        if (state.model.hasAttribute(NormalAttribute.attributeKey)) {
            return normalRef != null;
        }

        if (state.model.hasAttribute(SideAttribute.attributeKey)) {
            state.pipeline.addDependency(state.sideAttrib);
            return true;
        }
        throw new IllegalStateException("Normals requested but neither normal or side attrutes are provided by the model");
    }

    @Override
    public void operate(CCRenderState state) {
        if (normalRef != null) {
            state.normal.set(normalRef[state.vertexIndex]);
        } else {
            state.normal.set(Rotation.axes[state.side]);
        }
    }
}
