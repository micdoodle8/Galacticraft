package codechicken.lib.model.bakedmodels;

import codechicken.lib.model.BakedModelProperties;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.IModelState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

/**
 * Created by covers1624 on 1/02/2017.
 */
public class PerspectiveAwareOverrideModel extends AbstractBakedPropertiesModel implements IPerspectiveAwareModel {

    private final ItemOverrideList overrideList;
    private final IModelState state;
    private final List<BakedQuad> quads;

    public PerspectiveAwareOverrideModel(ItemOverrideList overrideList, IModelState state, BakedModelProperties properties) {
        super(properties);
        this.state = state;
        this.overrideList = overrideList;
        this.quads = ImmutableList.of();
    }

    public PerspectiveAwareOverrideModel(ItemOverrideList overrideList, IModelState state, BakedModelProperties properties, List<BakedQuad> quads) {
        super(properties);
        this.state = state;
        this.overrideList = overrideList;
        this.quads = quads;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        if (state == null && side == null) {
            return quads;
        }
        return ImmutableList.of();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return overrideList;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
        return MapWrapper.handlePerspective(this, state, cameraTransformType);
    }
}
