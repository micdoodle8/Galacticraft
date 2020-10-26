package micdoodle8.mods.galacticraft.core.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

import static net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;

public class ItemModelBuggy extends ModelTransformWrapper
{
    public ItemModelBuggy(IBakedModel modelToWrap)
    {
        super(modelToWrap);
    }

    @Override
    protected boolean getTransformForPerspective(TransformType cameraTransformType, MatrixStack mat)
    {
        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
        {
            mat.push();
            mat.translate(-0.15F, 0.0F, -0.15F);
            mat.rotate(new Quaternion(30.0F, 225.0F, 0.0F, true));
            mat.scale(0.1085F, 0.1085F, 0.1085F);
            mat.translate(-0.5F, -0.6F, 0.0F);
            mat.rotate(new Quaternion(Constants.halfPI / 4.0F, 0.0F, 0.0F, false));
            mat.translate(0.2F, 0.0F, 0.0F);
            mat.rotate(new Quaternion(0.0F, -0.3F, 0.0F, false));
            return true;
        }

        return false;
    }

    //    @Override
//    protected Matrix4f getTransformForPerspective(TransformType cameraTransformType)
//    {
//        if (cameraTransformType == TransformType.GUI)
//        {
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(30, 225, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.1085F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.2F, -0.8F, 0.0F));
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY(-0.3F);
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND || cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND)
//        {
//            Vector3f trans = new Vector3f(0.0F, -0.4F, -0.3F);
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            mul.setScale(0.15F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(trans);
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == TransformType.THIRD_PERSON_RIGHT_HAND || cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND)
//        {
//            Vector3f trans = new Vector3f(0.0F, -0.4F, 1.2F);
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(75, 15, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.35F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY(Constants.halfPI);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotX((float) (0.2F - Math.PI));
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotZ(0.5F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotZ(-0.65F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(trans);
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == TransformType.GROUND)
//        {
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            mul.setScale(0.075F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.5F, 0.0F, 0.5F));
//            ret.mul(mul);
//            return ret;
//        }
//        if (cameraTransformType == TransformType.FIXED)
//        {
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            mul.setScale(0.1F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY(1.5F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(1.5F, -1.5F, -0.15F));
//            ret.mul(mul);
//            return ret;
//        }
//
//        return null;
//    }
}
