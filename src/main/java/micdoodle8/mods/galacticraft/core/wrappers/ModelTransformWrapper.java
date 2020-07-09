package micdoodle8.mods.galacticraft.core.wrappers;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;
import java.util.Random;

abstract public class ModelTransformWrapper implements IBakedModel
{
    private final IBakedModel parent;

    public ModelTransformWrapper(IBakedModel parent)
    {
        this.parent = parent;
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return parent.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d()
    {
        return parent.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return parent.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return parent.getParticleTexture();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return parent.getItemCameraTransforms();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand)
    {
        return parent.getQuads(state, side, rand);
    }

    @Override
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
            return net.minecraftforge.client.ForgeHooksClient.handlePerspective(getBakedModel(), cameraTransformType);
        }

        return Pair.of(this, matrix4f);
    }

    abstract protected Matrix4f getTransformForPerspective(ItemCameraTransforms.TransformType cameraTransformType);
}
