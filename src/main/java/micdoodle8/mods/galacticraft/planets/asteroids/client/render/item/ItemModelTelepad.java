package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;

import javax.vecmath.Matrix4f;
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
            mul.setTranslation(new Vector3f(-0.14F, -0.36F, 0.24F));
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY((float) Math.PI / 5.0F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(1.3F);
            ret.mul(mul);
            return ret;
        }

        return null;
    }
}
