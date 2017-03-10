package codechicken.lib.block.property.unlisted;

import net.minecraft.util.ResourceLocation;

/**
 * Created by covers1624 on 28/10/2016.
 */
public class UnlistedResourceLocationProperty extends UnlistedPropertyBase<ResourceLocation> {

    public UnlistedResourceLocationProperty(String name) {
        super(name);
    }

    @Override
    public Class<ResourceLocation> getType() {
        return ResourceLocation.class;
    }

    @Override
    public String valueToString(ResourceLocation value) {
        return value.toString();
    }
}
