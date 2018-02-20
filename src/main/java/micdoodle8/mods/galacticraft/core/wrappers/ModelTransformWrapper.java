package micdoodle8.mods.galacticraft.core.wrappers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

abstract public class ModelTransformWrapper implements IBakedModel
{
    private final IBakedModel parent;

	public ModelTransformWrapper(IBakedModel parent)
	{
	    this.parent = parent;
	}

    public boolean isAmbientOcclusion()
    {
        return parent.isAmbientOcclusion();
    }

    public boolean isGui3d()
    {
        return parent.isGui3d();
    }

    public boolean isBuiltInRenderer()
    {
        return parent.isBuiltInRenderer();
    }

    public TextureAtlasSprite getParticleTexture()
    {
        return parent.getParticleTexture();
    }

    @SuppressWarnings("deprecation")
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return parent.getItemCameraTransforms();
    }

    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        return parent.getQuads(state, side, rand);
    }

    public ItemOverrideList getOverrides()
    {
        return parent.getOverrides();
    }

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
	{
		Matrix4f matrix4f = getTransformForPerspective(cameraTransformType);

		if (matrix4f == null)
		{
			return Pair.of(this, TRSRTransformation.blockCornerToCenter(TRSRTransformation.identity()).getMatrix());
		}

		return Pair.of(this, matrix4f);
	}

    abstract protected Matrix4f getTransformForPerspective(TransformType cameraTransformType);
}
