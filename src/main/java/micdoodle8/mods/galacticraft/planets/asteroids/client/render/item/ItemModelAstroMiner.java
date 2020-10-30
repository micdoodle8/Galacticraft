package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

public class ItemModelAstroMiner extends ModelTransformWrapper
{
    public ItemModelAstroMiner(IBakedModel modelToWrap)
    {
        super(modelToWrap);
    }

    @Override
    protected boolean getTransformForPerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat)
    {
        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
        {
            float scale = 0.25F;
            mat.push();
            mat.translate(0.0F, -0.05F, 0.0F);
            mat.rotate(new Quaternion(25.0F, 0.0F, 0.0F, true));
            mat.rotate(new Quaternion(0.0F, ClientUtil.getClientTimeTotal() / 1000.0F, 0.0F, false));
            mat.scale(scale, scale, scale);
            mat.translate(0.5D, 0.5D, 0.5D);
            return true;
        }

        return false;
    }

    //    @Override
//    protected Matrix4f getTransformForPerspective(ItemCameraTransforms.TransformType cameraTransformType)
//    {
//        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
//        {
//            Vector3f trans = new Vector3f(-0.17F, 0.0F, -0.17F);
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(30, 225, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.85F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.36F, 0.3F, 0.0F));
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(trans);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY(ClientUtil.getClientTimeTotal() / 1000.0F);
//            ret.mul(mul);
//            mul.setIdentity();
//            trans.scale(-1.0F);
//            mul.setTranslation(trans);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.3F);
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND ||
//                cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND)
//        {
//            Vector3f trans = new Vector3f(0.5F, 0.0F, -1.7F);
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, 45, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(4.0F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotX(Constants.halfPI);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotZ(-0.65F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(trans);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotX((float) -(Math.PI / 2.0F));
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND ||
//                cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND)
//        {
//            Vector3f trans = new Vector3f(-0.2F, -0.0F, 1.1F);
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(75, 15, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.8F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotZ(-Constants.halfPI);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY(Constants.halfPI);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotX(0.2F);
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
//            mul.setIdentity();
//            mul.rotX((float) -(Math.PI / 2.0F));
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotZ((float) (Math.PI));
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
//            mul.setScale(0.2F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.5F, 1.0F, 0.5F));
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
//            mul.setScale(0.3F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY(1.5F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.5F, 0.5F, 0.6F));
//            ret.mul(mul);
//            return ret;
//        }
//
//        return null;
//    }
}
