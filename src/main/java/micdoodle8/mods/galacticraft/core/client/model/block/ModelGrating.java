package micdoodle8.mods.galacticraft.core.client.model.block;

import com.google.common.collect.ImmutableList;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.TransformerHooks;
import micdoodle8.mods.galacticraft.core.blocks.BlockGrating;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.List;

public class ModelGrating implements IBakedModel
{
    private final IBakedModel gratingMetal;

    public ModelGrating (ModelResourceLocation blockLoc, ModelManager modelManager)
    {
        this.gratingMetal = modelManager.getModel(blockLoc);
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
    {
        if (side == EnumFacing.DOWN && state.getBlock() instanceof BlockGrating)
        {
            IBlockState baseState = ((IExtendedBlockState) state).getValue(BlockGrating.BASE_STATE);
            if (baseState != null)
            {
                IBlockAccess blockAccess = BlockGrating.savedBlockAccess;
                BlockPos pos = BlockGrating.savedPos;
                BufferBuilder buffer = TransformerHooks.renderBuilder.get();
                if (buffer != null)
                {
                    if (baseState.getBlock().canRenderInLayer(baseState, MinecraftForgeClient.getRenderLayer()))
                    try {
                        Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(baseState, pos, blockAccess, buffer);
                    } catch (Exception ignore) { ignore.printStackTrace(); }
                }
            }
        }
        if (MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.CUTOUT)
        {
            return this.gratingMetal.getQuads(state, side, rand);
        }
        
        return ImmutableList.of();
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return true;
    }

    @Override
    public boolean isGui3d()
    {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(Constants.ASSET_PREFIX + ":blocks/screen_side");
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return ItemOverrideList.NONE;
    }
}
