package codechicken.lib.block.property.unlisted;

/**
 * Created by covers1624 on 28/10/2016.
 */
public class UnlistedStringProperty extends UnlistedPropertyBase<String> {

    public UnlistedStringProperty(String name) {
        super(name);
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String valueToString(String value) {
        return value;
    }
}
