package codechicken.lib.render.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;

/**
 * Created by covers1624 on 26/10/2016.
 * Implementing this on a IBakedModel will allow you to have interaction similar to Block#getExtendedState.
 * You can return an entirely different model for this or just set some flags. What ever you want.
 * Useful for on-the-fly model baking, where you need to generate from an IExtendedBlockState.
 */
@Deprecated
public interface IExtendedModel extends IBakedModel {

    /**
     * You can return what ever model you like.
     * This is fired from BlockRendererDispatcher#renderBlock before blockModelRenderer#renderModel,
     * but after getExtendedState.
     *
     * @param prevModel The model that was going to be rendered.
     * @param state     The IBlockState returned from getExtendedState.
     * @return Another IBakedModel, can be different, same, whatever.
     */
    IBakedModel handleExtendedModel(IBakedModel prevModel, IBlockState state);

}
