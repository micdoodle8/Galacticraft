package codechicken.lib.render.item;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;

/**
 * Created by covers1624 on 18/10/2016.
 * Used to manually apply OpenGL transforms based on the TransformType.
 */
public interface IGLTransform extends IBakedModel {

    void applyTransforms(TransformType transformType, boolean isLeftHand);
}
