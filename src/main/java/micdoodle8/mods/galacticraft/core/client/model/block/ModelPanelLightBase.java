package micdoodle8.mods.galacticraft.core.client.model.block;

import com.google.common.collect.ImmutableList;

import micdoodle8.mods.galacticraft.core.blocks.BlockPanelLighting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.List;

public class ModelPanelLightBase implements ISmartBlockModel
{
    private final ModelResourceLocation callingBlock;
    
    public ModelPanelLightBase (ModelResourceLocation blockLoc)
    {
        this.callingBlock = blockLoc;
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state)
    {
        if (!(state.getBlock() instanceof BlockPanelLighting))
        {
            return this;
        }

        IBlockState baseState = ((IExtendedBlockState) state).getValue(BlockPanelLighting.BASE_STATE);
        if (baseState == null)
        {
            if (MinecraftForgeClient.getRenderLayer() == EnumWorldBlockLayer.SOLID)
            {
                return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelManager().getModel(this.callingBlock);
            }
        }
        else
        {
            EnumWorldBlockLayer layer = MinecraftForgeClient.getRenderLayer();
            if (layer == null || baseState.getBlock().canRenderInLayer(layer))
            {
                return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(baseState);
            }
        }
        return this;
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing p_177551_1_)
    {
        return ImmutableList.of();
    }

    @Override
    public List<BakedQuad> getGeneralQuads()
    {
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
}
