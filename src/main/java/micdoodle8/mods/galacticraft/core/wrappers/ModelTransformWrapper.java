package micdoodle8.mods.galacticraft.core.wrappers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.*;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.List;

@SuppressWarnings({ "deprecation" })
public abstract class ModelTransformWrapper implements IFlexibleBakedModel, ISmartItemModel, ISmartBlockModel, IPerspectiveAwareModel
{
    private final IBakedModel iBakedModel;

    public ModelTransformWrapper(IBakedModel i_modelToWrap)
    {
        this.iBakedModel = i_modelToWrap;
    }

    protected abstract Matrix4f getTransformForPerspective(TransformType cameraTransformType);

    @Override
    public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
    {
        Matrix4f matrix4f = getTransformForPerspective(cameraTransformType);

        if (matrix4f == null)
        {
            return Pair.of(this, TRSRTransformation.blockCornerToCenter(new TRSRTransformation(ItemTransformVec3f.DEFAULT)).getMatrix());
        }

        return Pair.of(this, matrix4f);
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing enumFacing)
    {
        return iBakedModel.getFaceQuads(enumFacing);
    }

    @Override
    public List<BakedQuad> getGeneralQuads()
    {
        return iBakedModel.getGeneralQuads();
    }

    @Override
    public VertexFormat getFormat()
    {
        if (iBakedModel instanceof IFlexibleBakedModel)
        {
            return ((IFlexibleBakedModel) iBakedModel).getFormat();
        }
        else
        {
            return Attributes.DEFAULT_BAKED_FORMAT;
        }
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return iBakedModel.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d()
    {
        return iBakedModel.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return iBakedModel.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return iBakedModel.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack)
    {
        return this;
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state)
    {
        return this;
    }
}
