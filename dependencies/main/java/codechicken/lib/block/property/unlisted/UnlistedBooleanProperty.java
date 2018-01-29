package codechicken.lib.block.property.unlisted;

/**
 * Created by covers1624 on 28/10/2016.
 */
public class UnlistedBooleanProperty extends UnlistedPropertyBase<Boolean> {

    public UnlistedBooleanProperty(String name) {
        super(name);
    }

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }

    @Override
    public String valueToString(Boolean value) {
        return value.toString();
    }
}
