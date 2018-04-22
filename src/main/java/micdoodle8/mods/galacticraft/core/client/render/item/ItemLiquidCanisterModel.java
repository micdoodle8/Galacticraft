package micdoodle8.mods.galacticraft.core.client.render.item;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.List;

public class ItemLiquidCanisterModel implements IBakedModel
{
    private final IBakedModel iBakedModel;

    public ItemLiquidCanisterModel(IBakedModel i_modelToWrap)
    {
        this.iBakedModel = i_modelToWrap;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
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

    private static final class BakedLiquidCanisterOverrideHandler extends ItemOverrideList
    {
        public static final BakedLiquidCanisterOverrideHandler INSTANCE = new BakedLiquidCanisterOverrideHandler();

        private BakedLiquidCanisterOverrideHandler()
        {
            super(ImmutableList.of());
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity)
        {
            if (stack.getTagCompound() == null)
            {
                ItemStack copy = stack.copy();
                copy.setTagCompound(new NBTTagCompound());
                copy.getTagCompound().setBoolean("Unbreakable", true);
                return FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().getItemModel(copy);
            }
            return originalModel;
        }
    }
}
