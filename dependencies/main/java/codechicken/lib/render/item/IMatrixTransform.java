package codechicken.lib.render.item;

import codechicken.lib.vec.Matrix4;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;

/**
 * Created by covers1624 on 18/10/2016.
 * Used to define IITemRenderer to apply transforms using a Matrix4
 */
public interface IMatrixTransform extends IBakedModel {

    Matrix4 getTransform(TransformType transformType, boolean isLeftHand);
}
