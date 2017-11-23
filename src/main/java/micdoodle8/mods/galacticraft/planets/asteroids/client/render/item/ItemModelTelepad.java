package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class ItemModelTelepad extends ModelTransformWrapper
{
    public ItemModelTelepad(IBakedModel modelToWrap)
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
            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(8, 0, 0));
            mul.setRotation(rot);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.12F, -0.2F, 0.24F));
            ret.mul(mul);
            mul.setIdentity();
            mul.rotX((float) Math.PI / 12.0F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY((float) Math.PI / 5.0F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(0.1625F);
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == TransformType.THIRD_PERSON_RIGHT_HAND || cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND)
        {
            Vector3f trans = new Vector3f(-0.6F, 0.1F, 1.6F);
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(80, 225, 0));
            mul.setRotation(rot);
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(0.1F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(trans);
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND || cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND)
        {
            Vector3f trans = new Vector3f(0.5F, 1.0F, 0.7F);
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(-12, 20, 0));
            mul.setRotation(rot);
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(0.1F);
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
            mul.setScale(0.1F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.5F, 0.0F, 0.5F));
            ret.mul(mul);
            return ret;
        }
        if (cameraTransformType == TransformType.FIXED)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.15F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY(1.575F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.5F, -1.0F, 0.5F));
            ret.mul(mul);
            return ret;
        }
        return null;
    }
}
