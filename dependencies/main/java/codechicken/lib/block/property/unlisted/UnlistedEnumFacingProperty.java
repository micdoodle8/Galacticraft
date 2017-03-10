package codechicken.lib.block.property.unlisted;

import net.minecraft.util.EnumFacing;

/**
 * Created by covers1624 on 23/11/2016.
 */
public class UnlistedEnumFacingProperty extends UnlistedPropertyBase<EnumFacing> {

    public UnlistedEnumFacingProperty(String name) {
        super(name);
    }

    @Override
    public Class<EnumFacing> getType() {
        return EnumFacing.class;
    }

    @Override
    public String valueToString(EnumFacing value) {
        return value.toString();
    }
}
