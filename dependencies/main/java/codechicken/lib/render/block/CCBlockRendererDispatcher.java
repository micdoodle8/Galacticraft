package codechicken.lib.render.block;

import codechicken.lib.texture.TextureUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;

/**
 * Created by covers1624 on 8/09/2016.
 * TODO Move back end of this to CCExtendedBlockRenderDispatcher
 */
public class CCBlockRendererDispatcher extends BlockRendererDispatcher implements TextureUtils.IIconRegister {

    public final BlockRendererDispatcher parentDispatcher;

    public CCBlockRendererDispatcher(BlockRendererDispatcher dispatcher, BlockColors blockColours) {
        super(dispatcher.getBlockModelShapes(), blockColours);
        parentDispatcher = dispatcher;
    }

    @Override
    public void renderBlockDamage(IBlockState state, BlockPos pos, TextureAtlasSprite texture, IBlockAccess blockAccess) {
        if (BlockRenderingRegistry.canHandle(state.getRenderType())) {
            BlockRenderingRegistry.renderBlockDamage(blockAccess, pos, state, texture);
        } else {
            parentDispatcher.renderBlockDamage(state, pos, texture, blockAccess);
        }
    }

    @Override
    public boolean renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, VertexBuffer worldRendererIn) {
        try {
            if (BlockRenderingRegistry.canHandle(state.getRenderType())) {
                if (blockAccess.getWorldType() != WorldType.DEBUG_WORLD) {
                    try {
                        state = state.getActualState(blockAccess, pos);
                    } catch (Exception ignored) {
                        //Noise..
                    }
                }
                return BlockRenderingRegistry.renderBlock(blockAccess, pos, state, worldRendererIn);
            }
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tessellating CCL block in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being tessellated");
            CrashReportCategory.addBlockInfo(crashreportcategory, pos, state.getBlock(), state.getBlock().getMetaFromState(state));
            throw new ReportedException(crashreport);
        }
        return parentDispatcher.renderBlock(state, pos, blockAccess, worldRendererIn);
    }

    @Override
    public void renderBlockBrightness(IBlockState state, float brightness) {
        if (BlockRenderingRegistry.canHandle(state.getRenderType())) {
            BlockRenderingRegistry.renderBlockBrightness(state, brightness);
        }
        super.renderBlockBrightness(state, brightness);
    }

    @Override
    public void registerIcons(TextureMap textureMap) {
        BlockRenderingRegistry.registerTextures(textureMap);
    }

    @Override
    public BlockModelRenderer getBlockModelRenderer() {
        return parentDispatcher.getBlockModelRenderer();
    }

    @Override
    public IBakedModel getModelForState(IBlockState state) {
        return parentDispatcher.getModelForState(state);
    }

    @Override
    public BlockModelShapes getBlockModelShapes() {
        return parentDispatcher.getBlockModelShapes();
    }
}
