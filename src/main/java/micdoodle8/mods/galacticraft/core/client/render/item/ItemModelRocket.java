package micdoodle8.mods.galacticraft.core.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;

import static net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;

public class ItemModelRocket extends ModelTransformWrapper
{
    public ItemModelRocket(IBakedModel modelToWrap)
    {
        super(modelToWrap);
    }

    @Override
    protected boolean getTransformForPerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat)
    {
        if (cameraTransformType == TransformType.GUI)
        {
            mat.push();
            mat.translate(-0.1F, -0.1F, 0.0F);
            mat.rotate(new Quaternion(55.0F, 225.0F, 0.0F, true));
            mat.scale(0.61F, 0.61F, 0.61F);
            mat.translate(-0.25F, -0.35F, 0.0F);
            mat.rotate(new Quaternion(0.0F, Constants.halfPI, 0.0F, false));
            mat.rotate(new Quaternion(Constants.halfPI / 4.0F, 0.0F, 0.0F, false));
            mat.translate(-0.15F, 0.0F, -0.15F);
            mat.rotate(new Quaternion(0.0F, ClientUtil.getClientTimeTotal() / 1000.0F, 0.0F, false));
            mat.translate(0.15F, 0.0F, 0.15F);
            mat.scale(0.3F, 0.3F, 0.3F);
            return false;
        }

        if (cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND || cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND)
        {
//            Vector3f trans = new Vector3f(0.5F, -3.2F, -2.6F);
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, 45, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.5F);
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
//            return ret;
        }

        if (cameraTransformType == TransformType.THIRD_PERSON_RIGHT_HAND || cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND)
        {
//            Vector3f trans = new Vector3f(0.4F, -2.8F, 1.2F);
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(75, 0, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.5F);
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
//            return ret;
        }

        if (cameraTransformType == TransformType.GROUND)
        {
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
        }
        if (cameraTransformType == TransformType.FIXED)
        {
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            mul.setScale(0.135F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY(0.0F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.5F, -2.75F, 0.5F));
//            ret.mul(mul);
//            return ret;
        }

        return false;
//        return null;
    }
}
