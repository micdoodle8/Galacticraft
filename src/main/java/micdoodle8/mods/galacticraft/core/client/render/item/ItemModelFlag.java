package micdoodle8.mods.galacticraft.core.client.render.item;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.Timer;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import java.lang.reflect.Field;

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
            mul.setScale(0.4F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.2F, -0.8F, 0.0F));
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY(Constants.halfPI);
            ret.mul(mul);
            return ret;
        }
        if (cameraTransformType == TransformType.FIRST_PERSON)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.rotY((float) -(Math.PI / 3.0F));
            ret.mul(mul);
            mul.setIdentity();
            if (Minecraft.getMinecraft().thePlayer.getItemInUseDuration() > 0)
            {
                final int useTime = Minecraft.getMinecraft().thePlayer.getItemInUseDuration();
                float var7 = useTime / 20.0F;
                var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;

                if (var7 > 1.0F)
                {
                    var7 = 1.0F;
                }
                final int useTimeFuture = Minecraft.getMinecraft().thePlayer.getItemInUseDuration() + 1;
                float var72 = useTimeFuture / 20.0F;
                var72 = (var72 * var72 + var72 * 2.0F) / 3.0F;

                if (var72 > 1.0F)
                {
                    var72 = 1.0F;
                }

                try
                {
                    Class<Minecraft> c = Minecraft.class;
                    Field f = c.getDeclaredField("timer");
                    f.setAccessible(true);
                    Timer t = (Timer) f.get(Minecraft.getMinecraft());
                    mul.rotX(((var7 + (var72 - var7) * t.renderPartialTicks) * 75.0F) / Constants.RADIANS_TO_DEGREES);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            ret.mul(mul);
            return ret;
        }
        if (cameraTransformType == TransformType.THIRD_PERSON)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.5F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.15F, 0.45F, 0.15F));
            ret.mul(mul);
            mul.setIdentity();
            mul.rotX((float) -(Math.PI / 2.0F));
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
            mul.setScale(0.5F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.25F, 0.0F, 0.25F));
            ret.mul(mul);
            return ret;
        }
        if (cameraTransformType == TransformType.FIXED)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.35F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY(3.1F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.0F, -0.75F, 0.1F));
            ret.mul(mul);
            return ret;
        }
        return null;
    }
}
