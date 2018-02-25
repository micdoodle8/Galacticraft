package micdoodle8.mods.galacticraft.core.client.render.item;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.List;

@SuppressWarnings({ "deprecation" })
public class ItemLiquidCanisterModel implements ISmartItemModel
{
    private final IBakedModel iBakedModel;

    public ItemLiquidCanisterModel(IBakedModel i_modelToWrap)
    {
        this.iBakedModel = i_modelToWrap;
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
        if (stack.getTagCompound() == null)
        {
            ItemStack copy = stack.copy();
            copy.setTagCompound(new NBTTagCompound());
            copy.getTagCompound().setBoolean("Unbreakable", true);
            return FMLClientHandler.instance().getClient().getRenderItem().getItemModelMesher().getItemModel(copy);
        }
        return iBakedModel;
    }
}
