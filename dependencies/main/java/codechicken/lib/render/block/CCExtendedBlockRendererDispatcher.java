package codechicken.lib.render.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;

/**
 * Created by covers1624 on 26/10/2016.
 * TODO remove IExtendedModel.
 */
public class CCExtendedBlockRendererDispatcher extends BlockRendererDispatcher {

    private final BlockRendererDispatcher parentDispatcher;

    private static boolean initialized = false;

    public CCExtendedBlockRendererDispatcher(BlockRendererDispatcher parentDispatcher, BlockColors blockColors) {
        super(parentDispatcher.getBlockModelShapes(), blockColors);
        this.parentDispatcher = parentDispatcher;
    }

    public static void init() {
        if (!initialized) {
            Minecraft mc = Minecraft.getMinecraft();
            BlockRendererDispatcher parentDispatcher = mc.getBlockRendererDispatcher();
            CCExtendedBlockRendererDispatcher newDispatcher = new CCExtendedBlockRendererDispatcher(parentDispatcher, mc.getBlockColors());

            mc.blockRenderDispatcher = newDispatcher;
            initialized = true;
        }
    }

    @Override
    public boolean renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, VertexBuffer worldRendererIn) {
        try {
            EnumBlockRenderType enumblockrendertype = state.getRenderType();

            if (enumblockrendertype == EnumBlockRenderType.MODEL && blockAccess.getWorldType() != WorldType.DEBUG_WORLD) {
                IBakedModel model = this.getModelForState(state);
                if (!(model instanceof IExtendedModel)) {
                    return parentDispatcher.renderBlock(state, pos, blockAccess, worldRendererIn);
                }
                state = state.getBlock().getExtendedState(state, blockAccess, pos);
                model = ((IExtendedModel) model).handleExtendedModel(model, state);
                return parentDispatcher.getBlockModelRenderer().renderModel(blockAccess, model, state, pos, worldRendererIn, true);
            } else {
                return parentDispatcher.renderBlock(state, pos, blockAccess, worldRendererIn);
            }
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, pos, state.getBlock(), state.getBlock().getMetaFromState(state));
            throw new ReportedException(crashreport);
        }
    }

    @Override
    public void renderBlockDamage(IBlockState state, BlockPos pos, TextureAtlasSprite texture, IBlockAccess blockAccess) {
        parentDispatcher.renderBlockDamage(state, pos, texture, blockAccess);
    }

    @Override
    public void renderBlockBrightness(IBlockState state, float brightness) {
        parentDispatcher.renderBlockBrightness(state, brightness);
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
