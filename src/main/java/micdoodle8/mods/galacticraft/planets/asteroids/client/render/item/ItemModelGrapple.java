package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class ItemModelGrapple extends ModelTransformWrapper
{
    public ItemModelGrapple(IBakedModel modelToWrap)
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
            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(30, 45, 0));
            mul.setRotation(rot);
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(0.7F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(-0.3F, 1.15F, 0.0F));
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY(Constants.halfPI);
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == TransformType.THIRD_PERSON_RIGHT_HAND || cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND)
        {
            float xTran = cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND ? 0.3F : 0.3F;
            float yTran = cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND ? 0.2F : -0.2F;
            float zTran = cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND ? 0.2F : 0.8F;
            int xRot = cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND ? 180 : 0;
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(xRot, 0, 0));
            mul.setRotation(rot);
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(0.35F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(xTran, yTran, zTran));
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY((float) (-Math.PI / 2.0F));
            mul.rotZ((float) (-Math.PI / 2.0F));
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND || cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND)
        {
            float xTran = cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND ? -0.275F : 0.5F;
            float yTran = cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND ? 0.925F : 0.05F;
            float zTran = cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND ? 0.2F : -0.2F;
            int xRot = cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND ? -180 : 0;
            int yRot = cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND ? 75 : 45;
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(xRot, yRot, 0));
            mul.setRotation(rot);
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(1.6F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(xTran, yTran, zTran));
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY((float) (-Math.PI / 1.2F));
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == TransformType.GROUND)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.35F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.65F, 0.35F, 0.5F));
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == TransformType.FIXED)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.6F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY(3.15F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.8F, 0.5F, 0.525F));
            ret.mul(mul);
            return ret;
        }

        return null;
    }
}
