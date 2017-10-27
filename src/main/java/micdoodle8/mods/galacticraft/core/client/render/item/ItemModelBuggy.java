package micdoodle8.mods.galacticraft.core.client.render.item;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.resources.model.IBakedModel;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class ItemModelBuggy extends ModelTransformWrapper
{
    public ItemModelBuggy(IBakedModel modelToWrap)
    {
        super(modelToWrap);
    }

    @Override
    protected Matrix4f getTransformForPerspective(TransformType cameraTransformType)
    {
        if (cameraTransformType == TransformType.GUI)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.175F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.2F, -0.3F, 0.0F));
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY(-0.3F);
            ret.mul(mul);
            return ret;
        }
        if (cameraTransformType == TransformType.FIRST_PERSON)
        {
            Vector3f trans = new Vector3f(0.0F, -0.4F, -0.3F);
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.35F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(trans);
            ret.mul(mul);
            return ret;
        }
        if (cameraTransformType == TransformType.THIRD_PERSON)
        {
            Vector3f trans = new Vector3f(0.0F, -0.2F, 0.6F);
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.35F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY(Constants.halfPI);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotX(0.2F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotZ(0.5F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotZ(-0.65F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(trans);
            ret.mul(mul);
            return ret;
        }
        if (cameraTransformType == TransformType.GROUND)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.3F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.25F, 0.25F, 0.25F));
            ret.mul(mul);
            return ret;
        }
        if (cameraTransformType == TransformType.FIXED)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.225F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY(1.6F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.3F, -0.5F, -0.15F));
            ret.mul(mul);
            return ret;
        }
        return null;
    }
}
