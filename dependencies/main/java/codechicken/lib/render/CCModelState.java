package codechicken.lib.render;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraftforge.client.model.IPerspectiveAwareModel.MapWrapper;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

/**
 * Created by covers1624 on 5/16/2016.
 * Same as a SimpleModelState except has a getter for the Transform map.
 * TODO Fix toVanillaTransform.
 */
public class CCModelState implements IModelState {

    private final ImmutableMap<TransformType, TRSRTransformation> map;
    private final Optional<TRSRTransformation> defaultTransform;

    public CCModelState(ImmutableMap<TransformType, TRSRTransformation> map) {
        this(map, Optional.<TRSRTransformation>absent());
    }

    public CCModelState(ImmutableMap<TransformType, TRSRTransformation> map, Optional<TRSRTransformation> defaultTransform) {
        this.map = map;
        this.defaultTransform = defaultTransform;
    }

    public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part) {
        if (!part.isPresent() || !(part.get() instanceof TransformType) || !map.containsKey(part.get())) {
            return defaultTransform;
        }
        return Optional.fromNullable(map.get(part.get()));
    }

    public ImmutableMap<TransformType, TRSRTransformation> getTransforms() {
        return MapWrapper.getTransforms(this);
    }

    @SuppressWarnings ("deprecation")//TODO, This seems to create invalid transforms.
    public ItemCameraTransforms toVanillaTransform() {
        ItemTransformVec3f thirdPLeft = ItemTransformVec3f.DEFAULT;
        ItemTransformVec3f thirdPRight = ItemTransformVec3f.DEFAULT;
        ItemTransformVec3f firstPLeft = ItemTransformVec3f.DEFAULT;
        ItemTransformVec3f firstPRight = ItemTransformVec3f.DEFAULT;
        ItemTransformVec3f head = ItemTransformVec3f.DEFAULT;
        ItemTransformVec3f gui = ItemTransformVec3f.DEFAULT;
        ItemTransformVec3f ground = ItemTransformVec3f.DEFAULT;
        ItemTransformVec3f fixed = ItemTransformVec3f.DEFAULT;
        if (map.containsKey(TransformType.THIRD_PERSON_LEFT_HAND)) {
            thirdPLeft = map.get(TransformType.THIRD_PERSON_LEFT_HAND).toItemTransform();
        }
        if (map.containsKey(TransformType.THIRD_PERSON_RIGHT_HAND)) {
            thirdPRight = map.get(TransformType.THIRD_PERSON_RIGHT_HAND).toItemTransform();
        }
        if (map.containsKey(TransformType.FIRST_PERSON_LEFT_HAND)) {
            firstPLeft = map.get(TransformType.FIRST_PERSON_LEFT_HAND).toItemTransform();
        }
        if (map.containsKey(TransformType.FIRST_PERSON_RIGHT_HAND)) {
            firstPRight = map.get(TransformType.FIRST_PERSON_RIGHT_HAND).toItemTransform();
        }
        if (map.containsKey(TransformType.HEAD)) {
            head = map.get(TransformType.HEAD).toItemTransform();
        }
        if (map.containsKey(TransformType.GUI)) {
            gui = map.get(TransformType.GUI).toItemTransform();
        }
        if (map.containsKey(TransformType.GROUND)) {
            ground = map.get(TransformType.GROUND).toItemTransform();
        }
        if (map.containsKey(TransformType.FIXED)) {
            fixed = map.get(TransformType.FIXED).toItemTransform();
        }
        return new ItemCameraTransforms(thirdPLeft, thirdPRight, firstPLeft, firstPRight, head, gui, ground, fixed);
    }
}
