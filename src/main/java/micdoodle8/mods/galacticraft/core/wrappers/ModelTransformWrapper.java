package micdoodle8.mods.galacticraft.core.wrappers;

import java.lang.reflect.Field;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableMap;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.common.model.TRSRTransformation;

abstract public class ModelTransformWrapper extends PerspectiveMapWrapper
{
	public ModelTransformWrapper(IBakedModel parent)
	{
		super(parent, (ImmutableMap<TransformType, TRSRTransformation>) null);
		ImmutableMap.Builder<TransformType, TRSRTransformation> buildIt = ImmutableMap.builder();
		for (TransformType tt : TransformType.values())
		{
			Matrix4f m = this.getTransformForPerspective(tt);
			buildIt.put(tt, TRSRTransformation.blockCenterToCorner(new TRSRTransformation(m)));
		}
		try {
			Field f = this.getClass().getSuperclass().getSuperclass().getDeclaredField("transforms");
			f.setAccessible(true);
			f.set(this, buildIt.build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    abstract protected Matrix4f getTransformForPerspective(TransformType cameraTransformType);
}
