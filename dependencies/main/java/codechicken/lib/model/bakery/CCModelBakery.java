package codechicken.lib.model.bakery;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.buffer.BakingVertexBuffer;
import codechicken.lib.render.pipeline.IVertexOperation;
import codechicken.lib.render.pipeline.IVertexSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

import java.util.List;

/**
 * Created by covers1624 on 8/20/2016.
 * Uses the CCL RenderPipe to bake a CCModel to an array of BakedQuads with the provided VertexFormat.
 */
@Deprecated// Use CCRenderState with a BakingVertexBuffer bound.
public class CCModelBakery {

    /**
     * Bakes a CCModel to a List of BakedQuads.
     * Assumes DefaultVertexFormats.BLOCK.
     * Assumes you want to bake the entire model.
     *
     * @param model Model to bake.
     * @param ops   Any Operations to apply.
     * @return The BakedQuads for the model.
     */
    public static List<BakedQuad> bakeModel(CCModel model, IVertexOperation... ops) {
        return bakeModel(model, 0, model.getVertices().length, ops);
    }

    /**
     * Bakes a CCModel to a List of BakedQuads.
     * Assumes DefaultVertexFormats.BLOCK.
     *
     * @param model Model to bake.
     * @param start The first vertex index to bake.
     * @param end   The Vertex index to bake until.
     * @param ops   Any Operations to apply.
     * @return The BakedQuads for the model.
     */
    public static List<BakedQuad> bakeModel(CCModel model, int start, int end, IVertexOperation... ops) {
        return bakeModel(model, DefaultVertexFormats.ITEM, start, end, ops);
    }

    /**
     * Bakes a CCModel to a List of BakedQuads.
     * Assumes you want to bake the entire model.
     *
     * @param model  Model to bake.
     * @param format VertexFormat to bake to.
     * @param ops    Any Operations to apply.
     * @return The BakedQuads for the model.
     */
    public static List<BakedQuad> bakeModel(CCModel model, VertexFormat format, IVertexOperation... ops) {
        return bakeModel(model, format, 0, model.getVertices().length, ops);
    }

    /**
     * Bakes a IVertexSource model to a List of BakedQuads.
     *
     * @param model  Vertex source to bake.
     * @param format VertexFormat to bake to.
     * @param start  The first vertex index to bake.
     * @param end    The Vertex index to bake until.
     * @param ops    Any Operations to apply.
     * @return The BakedQuads for the model.
     */
    public static List<BakedQuad> bakeModel(CCModel model, VertexFormat format, int start, int end, IVertexOperation... ops) {
        return bakeModel(model, model.vp == 3, format, start, end, ops);
    }

    /**
     * Bakes a IVertexSource model to a List of BakedQuads.
     *
     * @param model       Vertex source to bake.
     * @param isTriangles Specifies if the model's vertices are triangles.
     * @param format      VertexFormat to bake to, You almost always want this to be ITEM.
     * @param start       The first vertex index to bake.
     * @param end         The Vertex index to bake until.
     * @param ops         Any Operations to apply.
     * @return The BakedQuads for the model.
     */
    public static List<BakedQuad> bakeModel(IVertexSource model, boolean isTriangles, VertexFormat format, int start, int end, IVertexOperation... ops) {
        BakingVertexBuffer buffer = BakingVertexBuffer.create();
        buffer.begin(isTriangles ? 4 : 7, format);
        CCRenderState ccrs = CCRenderState.instance();
        ccrs.bind(buffer);
        ccrs.setPipeline(model, start, end, ops);
        ccrs.render();
        buffer.finishDrawing();
        return buffer.bake();
    }
}
