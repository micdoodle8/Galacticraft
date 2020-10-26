package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.util.ClientUtil;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class ItemModelBeamReceiver extends ModelTransformWrapper
{
    public ItemModelBeamReceiver(IBakedModel modelToWrap)
    {
        super(modelToWrap);
    }

    @Override
    protected boolean getTransformForPerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat)
    {
        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
        {
            mat.push();
            mat.rotate(new Quaternion(30.0F, -45.0F, 0.0F, true));
            mat.translate(0.5F, 0.0F, 0.75F);
//            mat.translate(-0.15F, 0.0F, -0.15F);
//            mat.rotate(new Quaternion(30.0F, 225.0F, 0.0F, true));
//            mat.scale(0.57F, 0.57F, 0.57F);
//            mat.translate(-0.5F, -0.6F, 0.0F);
//            mat.rotate(new Quaternion(0.0F, Constants.halfPI, 0.0F, false));
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
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(30, -45, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.5F, 0.0F, 0.75F));
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(1.0F);
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND ||
//                cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND)
//        {
//            float xTran = cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND ? -0.5F : 0.5F;
//            float zTran = cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND ? 0.3F : 0.7F;
//            int yRot = cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND ? -90 : 90;
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, yRot, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.rotY((float) (Math.PI));
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.35F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(xTran, 0.4F, zTran));
//            ret.mul(mul);
//            return ret;
//        }
//
//        if (cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND ||
//                cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND)
//        {
//            float xTran = cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND ? -0.4F : 0.4F;
//            float zTran = cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND ? 0.5F : 0.5F;
//            int yRot = cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND ? -45 : -225;
//            Vector3f trans = new Vector3f(xTran, 0.0F, zTran);
//            Matrix4f ret = new Matrix4f();
//            ret.setIdentity();
//            Matrix4f mul = new Matrix4f();
//            mul.setIdentity();
//            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(70, yRot, 0));
//            mul.setRotation(rot);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setScale(0.35F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(trans);
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
//            mul.setTranslation(new Vector3f(0.1F, 0.25F, 0.5F));
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
//            mul.rotY(1.565F);
//            ret.mul(mul);
//            mul.setIdentity();
//            mul.setTranslation(new Vector3f(0.4F, -0.2F, 0.45F));
//            ret.mul(mul);
//            return ret;
//        }
//        return null;
//    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return super.getParticleTexture();
    }
}
