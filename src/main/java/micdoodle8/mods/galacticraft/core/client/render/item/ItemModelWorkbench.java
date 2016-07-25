package micdoodle8.mods.galacticraft.core.client.render.item;

import micdoodle8.mods.galacticraft.core.wrappers.ModelTransformWrapper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.resources.model.IBakedModel;
import org.lwjgl.Sys;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

public class ItemModelWorkbench extends ModelTransformWrapper
{
    public ItemModelWorkbench(IBakedModel modelToWrap)
    {
        super(modelToWrap);
    }

    @Override
    protected Matrix4f getTransformForPerspective(ItemCameraTransforms.TransformType cameraTransformType)
    {
        return null;
    }
}
