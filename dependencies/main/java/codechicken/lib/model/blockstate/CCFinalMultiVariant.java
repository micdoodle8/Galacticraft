package codechicken.lib.model.blockstate;

import codechicken.lib.model.BakedModelProperties;
import codechicken.lib.model.CCMultiModel;
import codechicken.lib.model.StateOverrideIModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by covers1624 on 16/12/2016.
 */
public class CCFinalMultiVariant extends Variant {

    private Variant baseVariant;
    private BakedModelProperties baseProperties;
    private List<Variant> finalVariants = new LinkedList<Variant>();
    private IModelState state;

    public CCFinalMultiVariant(CCVariant baseVariant, String textureDomain, Map<String, CCVariant> subModels) {
        super(baseVariant.model == null ? new ResourceLocation("builtin/missing") : baseVariant.model, baseVariant.state.get() instanceof ModelRotation ? ((ModelRotation) baseVariant.state.get()) : ModelRotation.X0_Y0, baseVariant.uvLock.or(false), baseVariant.weight.or(1));
        state = baseVariant.state.get();
        this.baseVariant = makeFinalVariant(baseVariant, textureDomain);
        this.baseProperties = new BakedModelProperties(baseVariant.smooth.or(true), baseVariant.gui3d.or(true));
        for (CCVariant subModel : subModels.values()) {
            finalVariants.add(makeFinalVariant(baseVariant.copy().with(subModel), textureDomain));
        }
    }

    @Override
    public IModel process(IModel base) {
        boolean hasBase = base != ModelLoaderRegistry.getMissingModel();
        if (hasBase) {
            base = baseVariant.process(base);
        }

        List<IModel> subModels = new LinkedList<IModel>();
        for (Variant variant : finalVariants) {
            if (!variant.getModelLocation().equals(new ResourceLocation("builtin/missing"))) {
                IModel subModel = ModelLoaderRegistry.getModelOrLogError(variant.getModelLocation(), "Unable to load subModel's Model: " + variant.getModelLocation());
                subModels.add(variant.process(new StateOverrideIModel(subModel, variant.getState())));
            }
        }

        return new CCMultiModel(hasBase ? base : null, baseProperties, subModels);
    }

    @Override
    public IModelState getState() {
        return state;
    }

    private static Variant makeFinalVariant(CCVariant variant, String textureDomain) {
        boolean uvLock = variant.uvLock.or(false);
        boolean smooth = variant.smooth.or(true);
        boolean gui3d = variant.gui3d.or(true);
        int weight = variant.weight.or(1);
        if (variant.model != null && variant.textures.size() == 0 && variant.customData.size() == 0 && variant.state.orNull() instanceof ModelRotation) {
            return new Variant(variant.model, ((ModelRotation) variant.state.get()), uvLock, weight);
        } else {
            return new CCFinalVariant(variant.model, variant.state.or(TRSRTransformation.identity()), uvLock, smooth, gui3d, weight, variant.textures, textureDomain, variant.customData);
        }
    }
}
