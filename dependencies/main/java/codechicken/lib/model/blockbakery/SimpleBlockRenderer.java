package codechicken.lib.model.blockbakery;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.buffer.BakingVertexBuffer;
import codechicken.lib.texture.TextureUtils.IIconRegister;
import codechicken.lib.util.TripleABC;
import codechicken.lib.util.VertexDataUtils;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Vector3;
import codechicken.lib.vec.uv.UVTransformation;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.List;

/**
 * Created by covers1624 on 26/12/2016.
 * TODO Document.
 */
public abstract class SimpleBlockRenderer implements ISimpleBlockBakery, IIconRegister {

    //TODO Face based models.
    private static final CCModel[][] models = new CCModel[6][4];

    static {
        CCModel model = CCModel.quadModel(24).generateBlock(0, Cuboid6.full);
        for (int s = 0; s < 6; s++) {
            for (int r = 0; r < 4; r++) {
                CCModel m = model.copy().apply(Rotation.sideOrientation(s, r).at(Vector3.center));
                m.computeNormals();
                models[s][r] = m;
            }
        }
    }

    public abstract TripleABC<Integer, Integer, UVTransformation> getWorldTransforms(IExtendedBlockState state);

    public abstract TripleABC<Integer, Integer, UVTransformation> getItemTransforms(ItemStack stack);

    public abstract boolean shouldCull();

    @Override
    public IExtendedBlockState handleState(IExtendedBlockState state, TileEntity tileEntity) {
        return state;
    }

    @Override
    public List<BakedQuad> bakeQuads(EnumFacing face, IExtendedBlockState state) {
        BakingVertexBuffer buffer = BakingVertexBuffer.create();
        TripleABC<Integer, Integer, UVTransformation> worldData = getWorldTransforms(state);
        CCRenderState ccrs = CCRenderState.instance();
        ccrs.reset();
        ccrs.startDrawing(0x7, DefaultVertexFormats.ITEM, buffer);
        models[worldData.getA()][worldData.getB()].render(ccrs, worldData.getC());
        buffer.finishDrawing();
        List<BakedQuad> quads = buffer.bake();
        if (face == null && !shouldCull()) {
            return quads;
        } else if (face != null) {
            return VertexDataUtils.sortFaceData(quads).get(face);
        }
        return ImmutableList.of();
    }

    @Override
    public List<BakedQuad> bakeItemQuads(EnumFacing face, ItemStack stack) {
        BakingVertexBuffer buffer = BakingVertexBuffer.create();
        TripleABC<Integer, Integer, UVTransformation> worldData = getItemTransforms(stack);
        CCRenderState ccrs = CCRenderState.instance();
        ccrs.reset();
        ccrs.startDrawing(0x7, DefaultVertexFormats.ITEM, buffer);
        models[worldData.getA()][worldData.getB()].render(ccrs, worldData.getC());
        buffer.finishDrawing();
        List<BakedQuad> quads = buffer.bake();

        if (face == null && !shouldCull()) {
            return quads;
        } else if (face != null) {
            return VertexDataUtils.sortFaceData(quads).get(face);
        }
        return ImmutableList.of();
    }
}
