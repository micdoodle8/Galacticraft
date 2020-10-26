package micdoodle8.mods.galacticraft.core.client.render.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ItemLiquidCanisterModel implements IBakedModel
{
    private final IBakedModel iBakedModel;

    public ItemLiquidCanisterModel(IBakedModel i_modelToWrap)
    {
        this.iBakedModel = i_modelToWrap;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand)
    {
        return iBakedModel.getQuads(state, side, rand);
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return BakedLiquidCanisterOverrideHandler.INSTANCE;
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return iBakedModel.isAmbientOcclusion();
    }

    @Override
    public boolean isAmbientOcclusion(BlockState state)
    {
        return isAmbientOcclusion();
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
    public TextureAtlasSprite getParticleTexture(@Nonnull IModelData data)
    {
        return iBakedModel.getParticleTexture();
    }

    @Override
    public boolean func_230044_c_()
    {
        return iBakedModel.func_230044_c_();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return iBakedModel.getItemCameraTransforms();
    }

    private static final class BakedLiquidCanisterOverrideHandler extends ItemOverrideList
    {
        public static final BakedLiquidCanisterOverrideHandler INSTANCE = new BakedLiquidCanisterOverrideHandler();

        private BakedLiquidCanisterOverrideHandler()
        {
            super();
        }

        @Override
        public IBakedModel getModelWithOverrides(IBakedModel originalModel, ItemStack stack, World world, LivingEntity entity)
        {
            if (stack.getTag() == null)
            {
                ItemStack copy = stack.copy();
                copy.setTag(new CompoundNBT());
                copy.getTag().putBoolean("Unbreakable", true);
                return Minecraft.getInstance().getItemRenderer().getItemModelMesher().getItemModel(copy);
            }
            return originalModel;
        }
    }
}
