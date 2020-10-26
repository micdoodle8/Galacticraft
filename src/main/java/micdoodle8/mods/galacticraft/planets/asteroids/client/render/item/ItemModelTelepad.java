package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class ItemModelTelepad extends ModelTransformWrapper
{
    public ItemModelTelepad(IBakedModel modelToWrap)
    {
        super(modelToWrap);
    }

    @Override
    protected boolean getTransformForPerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat)
    {
        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
        {
            mat.push();
            mat.translate(0.12F, -0.2F, 0.24F);
            mat.rotate(new Quaternion((float)Math.PI / 12.0F, 0.0F, 0.0F, false));
            mat.rotate(new Quaternion(0.0F, (float)Math.PI / 5.0F, 0.0F, false));
            mat.scale(0.1625F, 0.1625F, 0.1625F);
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
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(8, 0, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.12F, -0.2F, 0.24F));
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotX((float) Math.PI / 12.0F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY((float) Math.PI / 5.0F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.1625F);
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND || cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND)
//        {
//            Vector3f trans = new Vector3f(-0.6F, 0.1F, 1.6F);
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(80, 225, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.1F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(trans);
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND || cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND)
//        {
//            Vector3f trans = new Vector3f(0.5F, 1.0F, 0.7F);
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(-12, 20, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.1F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(trans);
//            ret.mul(mul);
//            return ret;
//        }
//        if (cameraTransformType == ItemCameraTransforms.TransformType.GROUND)
//        {
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            mul.setScale(0.1F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.5F, 0.0F, 0.5F));
//            ret.mul(mul);
//            return ret;
//        }
//        if (cameraTransformType == ItemCameraTransforms.TransformType.FIXED)
//        {
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            mul.setScale(0.15F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY(1.575F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.5F, -1.0F, 0.5F));
//            ret.mul(mul);
//            return ret;
//        }
//        return null;
//    }
}
