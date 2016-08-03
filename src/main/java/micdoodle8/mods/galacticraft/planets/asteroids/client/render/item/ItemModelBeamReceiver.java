package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.resources.model.IBakedModel;
import org.lwjgl.Sys;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class ItemModelBeamReceiver extends ModelTransformWrapper
{
    public ItemModelBeamReceiver(IBakedModel modelToWrap)
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
            mul.setTranslation(new Vector3f(0.24F, -0.05F, 0.24F));
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(1.4F);
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.rotY((float) (Math.PI));
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.0F, 0.1F, 0.2F));
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.5F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY((float) (Math.PI / 2.0F));
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.4F, 0.0F, 0.2F));
            ret.mul(mul);
            return ret;
        }

        return null;
    }
}
