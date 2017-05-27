package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;

import javax.vecmath.Matrix4f;
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
            mul.setTranslation(new Vector3f(0.24F, -0.05F, 0.24F));
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(1.4F);
            ret.mul(mul);
            return ret;
        }
        if (cameraTransformType == TransformType.FIRST_PERSON)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.rotY((float) (Math.PI));
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.0F, 0.1F, 0.2F));
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
            mul.rotY(Constants.halfPI);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.4F, 0.0F, 0.2F));
            ret.mul(mul);
            return ret;
        }
        if (cameraTransformType == TransformType.GROUND)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(1.0F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.1F, -0.1F, 0.25F));
            ret.mul(mul);
            return ret;
        }
        if (cameraTransformType == TransformType.FIXED)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(1.0F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY(1.6F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.1F, -0.125F, 0.25F));
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
