package codechicken.lib.model;

import codechicken.lib.model.loader.CCBakedModelLoader;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by covers1624 on 7/25/2016.
 * <p/>
 * Default implementation for CCBakedModelLoader.
 */
@Deprecated
public class CCOverrideListHandler extends ItemOverrideList {

    public CCOverrideListHandler() {
        super(ImmutableList.<ItemOverride>of());
    }

    @Override
    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
        IBakedModel model = CCBakedModelLoader.getModel(stack);
        if (model == null) {
            return originalModel;
        }
        return model;
    }
}
