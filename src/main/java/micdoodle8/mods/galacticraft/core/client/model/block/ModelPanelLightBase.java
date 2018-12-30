package micdoodle8.mods.galacticraft.core.client.model.block;

import com.google.common.collect.ImmutableList;

import micdoodle8.mods.galacticraft.core.blocks.BlockPanelLighting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.List;

public class ModelPanelLightBase implements IBakedModel
{
    private final ModelResourceLocation callingBlock;

    public ModelPanelLightBase (ModelResourceLocation blockLoc)
    {
        this.callingBlock = blockLoc;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
    {
        if (state.getBlock() instanceof BlockPanelLighting)
        {
            IBlockState baseState = ((IExtendedBlockState) state).getValue(BlockPanelLighting.BASE_STATE);
            BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
            if (baseState == null)
            {
                if (layer == null || layer == BlockRenderLayer.SOLID)
                {
                    return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(this.callingBlock).getQuads(state, side, rand);
                }
            }
            else if (layer == null || baseState.getBlock().canRenderInLayer(baseState, layer))
            {
                return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(baseState).getQuads(baseState, side, rand);
            }
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
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/glass");
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
