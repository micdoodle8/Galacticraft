package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class ItemModelBeamReceiver extends ModelTransformWrapper
{
    public ItemModelBeamReceiver(IBakedModel modelToWrap)
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
            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(30, -45, 0));
            mul.setRotation(rot);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.5F, 0.0F, 0.75F));
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(1.0F);
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND || cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND)
        {
            float xTran = cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND ? -0.5F : 0.5F;
            float zTran = cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND ? 0.3F : 0.7F;
            int yRot = cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND ? -90 : 90;
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, yRot, 0));
            mul.setRotation(rot);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY((float) (Math.PI));
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(0.35F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(xTran, 0.4F, zTran));
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == TransformType.THIRD_PERSON_RIGHT_HAND || cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND)
        {
            float xTran = cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND ? -0.4F : 0.4F;
            float zTran = cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND ? 0.5F : 0.5F;
            int yRot = cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND ? -45 : -225;
            Vector3f trans = new Vector3f(xTran, 0.0F, zTran);
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            Quat4f rot = TRSRTransformation.quatFromXYZDegrees(new Vector3f(70, yRot, 0));
            mul.setRotation(rot);
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(0.35F);
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
            mul.setScale(0.35F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.1F, 0.25F, 0.5F));
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
            mul.rotY(1.565F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.4F, -0.2F, 0.45F));
            ret.mul(mul);
            return ret;
        }
        return null;
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return super.getParticleTexture();
    }
}
