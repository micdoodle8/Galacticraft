package codechicken.lib.render.pipeline.attribute;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.pipeline.VertexAttribute;

/**
 * Sets colour in CCRS to the specified colour in the model.
 */
public class ColourAttribute extends VertexAttribute<int[]> {

    public static final AttributeKey<int[]> attributeKey = new AttributeKey<int[]>() {
        @Override
        public int[] newArray(int length) {
            return new int[length];
        }
    };

    private int[] colourRef;

    @Override
    public int[] newArray(int length) {
        return new int[length];
    }

    @Override
    public String getAttribName() {
        return "colourAttrib";
    }

    @Override
    public boolean load(CCRenderState state) {
        colourRef = state.model.getAttributes(ColourAttribute.attributeKey);
        return colourRef != null || !state.model.hasAttribute(ColourAttribute.attributeKey);
    }

    @Override
    public void operate(CCRenderState state) {
        if (colourRef != null) {
            state.colour = ColourRGBA.multiply(state.baseColour, colourRef[state.vertexIndex]);
        } else {
            state.colour = state.baseColour;
        }
    }
}
