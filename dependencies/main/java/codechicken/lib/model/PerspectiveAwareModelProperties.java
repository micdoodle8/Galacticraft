package codechicken.lib.model;

import codechicken.lib.util.TransformUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.common.model.IModelState;

/**
 * Created by covers1624 on 21/02/2017.
 */
public class PerspectiveAwareModelProperties {

    public static final PerspectiveAwareModelProperties DEFAULT_ITEM = new PerspectiveAwareModelProperties(TransformUtils.DEFAULT_ITEM, true, false);
    public static final PerspectiveAwareModelProperties DEFAULT_BLOCK = new PerspectiveAwareModelProperties(TransformUtils.DEFAULT_BLOCK, true, true);

    private final IModelState modelState;
    private final BakedModelProperties properties;

    public PerspectiveAwareModelProperties(IModelState state, boolean isAO, boolean isGui3D) {
        this(state, isAO, isGui3D, false, null);
    }

    public PerspectiveAwareModelProperties(IModelState state, boolean isAO, boolean isGui3D, boolean isBuiltInRenderer, TextureAtlasSprite particle) {
        this.modelState = state;
        properties = new BakedModelProperties(isAO, isGui3D, isBuiltInRenderer, particle);
    }

    public IModelState getModelState() {
        return modelState;
    }

    public BakedModelProperties getProperties() {
        return properties;
    }
}
