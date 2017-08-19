package micdoodle8.mods.galacticraft.planets.mars.client.render.item;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.resources.model.IBakedModel;

import org.lwjgl.Sys;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class ItemModelRocketT2 extends ModelTransformWrapper
{
    public ItemModelRocketT2(IBakedModel modelToWrap)
    {
        super(modelToWrap);
    }

    @Override
    protected Matrix4f getTransformForPerspective(TransformType cameraTransformType)
    {
        if (cameraTransformType == TransformType.GUI)
        {
            Vector3f trans = new Vector3f(-0.08F, 0.0F, -0.08F);
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.82F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(-0.28F, -0.35F, 0.0F));
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY(Constants.halfPI);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotX((float) (Math.PI / 4.0F));
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(trans);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY(Sys.getTime() / 1000.0F);
            ret.mul(mul);
            mul.setIdentity();
            trans.scale(-1.0F);
            mul.setTranslation(trans);
            ret.mul(mul);
            mul.setIdentity();
            mul.setScale(0.3F);
            ret.mul(mul);
            return ret;
        }
        if (cameraTransformType == TransformType.FIRST_PERSON)
        {
            Vector3f trans = new Vector3f(0.5F, 1.2F, -1.2F);
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(4.0F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotX(Constants.halfPI);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotZ(-0.65F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(trans);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotX((float) (Math.PI));
            ret.mul(mul);
            return ret;
        }
        if (cameraTransformType == TransformType.THIRD_PERSON)
        {
            Vector3f trans = new Vector3f(0.0F, -0.9F, -0.1F);
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.5F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotX((float) (Math.PI / 3.0F));
            ret.mul(mul);
            mul.setIdentity();
            mul.rotZ((float) (-Math.PI / 2.0F));
            ret.mul(mul);
            mul.setIdentity();
            mul.rotX(0.3F);
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
            mul.setScale(0.5F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.25F, 0.25F, 0.25F));
            ret.mul(mul);
            return ret;
        }
        if (cameraTransformType == TransformType.FIXED)
        {
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.225F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotY(0.0125F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.25F, -1.75F, 0.25F));
            ret.mul(mul);
            return ret;
        }
        return null;
    }
}
