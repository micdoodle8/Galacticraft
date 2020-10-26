package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class ItemModelGrapple extends ModelTransformWrapper
{
    public ItemModelGrapple(IBakedModel modelToWrap)
    {
        super(modelToWrap);
    }

    @Override
    protected boolean getTransformForPerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat)
    {
        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
        {
            mat.push();
            mat.rotate(new Quaternion(30.0F, 45.0F, 0.0F, true));
            mat.scale(0.7F, 0.7F, 0.7F);
            mat.translate(-0.3F, 1.15F, 0.0F);
            mat.rotate(new Quaternion(0.0F, Constants.halfPI, 0.0F, false));
//            mat.translate(-0.15F, 0.0F, -0.15F);
//            mat.rotate(new Quaternion(30.0F, 225.0F, 0.0F, true));
//            mat.scale(0.57F, 0.57F, 0.57F);
//            mat.translate(-0.5F, -0.6F, 0.0F);
//            mat.rotate(new Quaternion(0.0F, Constants.halfPI, 0.0F, false));
//            mat.rotate(new Quaternion(Constants.halfPI / 4.0F, 0.0F, 0.0F, false));
//            mat.rotate(new Quaternion(0.0F, ClientUtil.getClientTimeTotal() / 1000.0F, 0.0F, true));
//            mat.translate(0.15F, 0.0F, 0.15F);
//            mat.scale(0.3F, 0.3F, 0.3F);
            return true;
        }

        return false;
    }

    //    @Override
//    protected Matrix4f getTransformForPerspective(ItemCameraTransforms.TransformType cameraTransformType)
//    {
//        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
//        {
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(30, 45, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.7F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(-0.3F, 1.15F, 0.0F));
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY(Constants.halfPI);
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND || cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND)
//        {
//            float xTran = cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND ? 0.3F : 0.3F;
//            float yTran = cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND ? 0.2F : -0.2F;
//            float zTran = cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND ? 0.2F : 0.8F;
//            int xRot = cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND ? 180 : 0;
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(xRot, 0, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.35F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(xTran, yTran, zTran));
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY((float) (-Math.PI / 2.0F));
//            mul.rotZ((float) (-Math.PI / 2.0F));
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND)
//        {
//            float xTran = cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND ? -0.275F : 0.5F;
//            float yTran = cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND ? 0.925F : 0.05F;
//            float zTran = cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND ? 0.2F : -0.2F;
//            int xRot = cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND ? -180 : 0;
//            int yRot = cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND ? 75 : 45;
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(xRot, yRot, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(1.6F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(xTran, yTran, zTran));
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY((float) (-Math.PI / 1.2F));
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == ItemCameraTransforms.TransformType.GROUND)
//        {
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            mul.setScale(0.35F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.65F, 0.35F, 0.5F));
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == ItemCameraTransforms.TransformType.FIXED)
//        {
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            mul.setScale(0.6F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY(3.15F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.8F, 0.5F, 0.525F));
//            ret.mul(mul);
//            return ret;
//        }
//
//        return null;
//    }
}
