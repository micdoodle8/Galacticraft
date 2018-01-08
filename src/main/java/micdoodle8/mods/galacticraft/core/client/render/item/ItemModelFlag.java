package micdoodle8.mods.galacticraft.core.client.render.item;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.EntityLivingBase;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class ItemModelFlag extends ModelTransformWrapper
{
    public ItemModelFlag(IBakedModel modelToWrap)
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
            mul.setScale(0.25F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(-0.22F, -1.6F, 0.0F));
            ret.mul(mul);
            mul.setIdentity();
            mul.rotX(0.5F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY(-Constants.halfPI / 2.0F);
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND || cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND)
        {
            float xTran = cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND ? 0.65F : 1.5F;
            float yTran = cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND ? -0.08F : -0.05F;
            Vector3f trans = new Vector3f(xTran, yTran, 0.0F);
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.rotZ(0.0F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY(1.5F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY((float) -(Math.PI / 3.0F));
            ret.mul(mul);
            mul.setIdentity();
            EntityLivingBase player = Minecraft.getMinecraft().player;
            if (player != null && player.isHandActive() && !player.getActiveItemStack().isEmpty())
            {
                final int useTime = Minecraft.getMinecraft().player.getItemInUseMaxCount();
                float interpolate0 = useTime / 20.0F;
                interpolate0 = (interpolate0 * interpolate0 + interpolate0 * 2.0F) / 3.0F;

                if (interpolate0 > 1.0F)
                {
                    interpolate0 = 1.0F;
                }
                final int useTimeFuture = Minecraft.getMinecraft().player.getItemInUseMaxCount() + 1;
                float interpolate1 = useTimeFuture / 20.0F;
                interpolate1 = (interpolate1 * interpolate1 + interpolate1 * 2.0F) / 3.0F;

                if (interpolate1 > 1.0F)
                {
                    interpolate1 = 1.0F;
                }
                mul.rotX(((interpolate0 + (interpolate1 - interpolate0) * Minecraft.getMinecraft().getRenderPartialTicks()) * 75.0F) / Constants.RADIANS_TO_DEGREES);
            }
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(trans);
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == TransformType.THIRD_PERSON_RIGHT_HAND || cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.5F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.54F, -0.4F, 0.54F));
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.0F, 0.5F, 0.0F));
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == TransformType.GROUND)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.125F);
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
            mul.setScale(0.2F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY(3.15F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.5F, -1.0F, 0.6F));
            ret.mul(mul);
            return ret;
        }

        return null;
    }
}
