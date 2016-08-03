package micdoodle8.mods.galacticraft.core.client.render.item;

import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.resources.model.IBakedModel;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class ItemModelFlag extends ModelTransformWrapper
{
    public ItemModelFlag(IBakedModel modelToWrap)
    {
        super(modelToWrap);
    }

    @Override
    protected Matrix4f getTransformForPerspective(ItemCameraTransforms.TransformType cameraTransformType)
    {
        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.4F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.2F, -0.8F, 0.0F));
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY((float) (Math.PI / 2.0F));
            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotX((float) (Math.PI / 4.0F));
//            ret.mul(mul);
            return ret;
        }

        return null;
    }
}
