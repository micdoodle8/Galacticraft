package codechicken.lib.render.item;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;

/**
 * Created by covers1624 on 5/11/2016.
 * Basically the same as IPerspectiveAwareModel. Except you have reference to the ItemStack you are rendering for.
 */
public interface IStackPerspectiveAwareModel extends IBakedModel {

    Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemStack stack, TransformType cameraTransformType);
}
