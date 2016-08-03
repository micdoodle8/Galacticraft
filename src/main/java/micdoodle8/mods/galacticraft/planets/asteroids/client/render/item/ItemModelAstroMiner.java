package micdoodle8.mods.galacticraft.planets.asteroids.client.render.item;

import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.resources.model.IBakedModel;
import org.lwjgl.Sys;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class ItemModelAstroMiner extends ModelTransformWrapper
{
    public ItemModelAstroMiner(IBakedModel modelToWrap)
    {
        super(modelToWrap);
    }

    @Override
    protected Matrix4f getTransformForPerspective(ItemCameraTransforms.TransformType cameraTransformType)
    {
        if (cameraTransformType == ItemCameraTransforms.TransformType.GUI)
        {
            Vector3f trans = new Vector3f(-0.08F, 0.0F, -0.08F);
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(1.35F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(new Vector3f(0.16F, 0.1F, 0.0F));
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

        if (cameraTransformType == ItemCameraTransforms.TransformType.FIRST_PERSON)
        {
            Vector3f trans = new Vector3f(0.0F, -0.4F, -0.3F);
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(1.0F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotX((float) (Math.PI / 2.0F));
            ret.mul(mul);
            mul.setIdentity();
            mul.rotZ(-0.65F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(trans);
            ret.mul(mul);
            return ret;
        }

        if (cameraTransformType == ItemCameraTransforms.TransformType.THIRD_PERSON)
        {
            Vector3f trans = new Vector3f(0.0F, -0.4F, 0.6F);
            Matrix4f ret = new Matrix4f();
            ret.setIdentity();
            Matrix4f mul = new Matrix4f();
            mul.setIdentity();
            mul.setScale(0.8F);
            ret.mul(mul);
            mul.setIdentity();
            mul.rotX((float) (Math.PI / 2.0F));
            ret.mul(mul);
            mul.setIdentity();
            mul.rotZ(-0.65F);
            ret.mul(mul);
            mul.setIdentity();
            mul.setTranslation(trans);
            ret.mul(mul);
            return ret;
        }

        return null;
    }
}
