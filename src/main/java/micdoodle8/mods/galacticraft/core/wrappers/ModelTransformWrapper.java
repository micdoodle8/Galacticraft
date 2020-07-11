package micdoodle8.mods.galacticraft.core.wrappers;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;
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
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat)
    {
        if (!getTransformForPerspective(cameraTransformType, mat))
        {
            return net.minecraftforge.client.ForgeHooksClient.handlePerspective(getBakedModel(), cameraTransformType, mat);
        }

        return this;
    }

    @Override
    public boolean func_230044_c_()
    {
        return true;
    }

    abstract protected boolean getTransformForPerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat);
}
