package micdoodle8.mods.galacticraft.core.client.render.item;

import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class ItemModelWorkbench extends ModelTransformWrapper
{
    public ItemModelWorkbench(IBakedModel modelToWrap)
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
            mul.setTranslation(new Vector3f(0.0F, -0.0F, 0.0F));
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(1.0F);
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == TransformType.THIRD_PERSON_RIGHT_HAND || cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND)
        {
            Vector3f trans = new Vector3f(-0.025F, 0.4F, 0.3F);
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(38, 0, 0));
            mul.setRotation(rot);
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(1.0F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(trans);
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND || cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND)
        {
            Vector3f trans = new Vector3f(-0.05F, 0.3F, 0.4F);
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(-30, 0, 0));
            mul.setRotation(rot);
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(1.0F);
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
            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(-30, 0, 0));
            mul.setRotation(rot);
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(0.85F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(-0.05F, 0.25F, 0.45F));
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == TransformType.FIXED)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(30, 0, 0));
            mul.setRotation(rot);
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(0.8F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY(3.15F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.0F, 0.0F, 0.4F));
            ret.mul(mul);
            return ret;
        }

        return null;
    }
}
