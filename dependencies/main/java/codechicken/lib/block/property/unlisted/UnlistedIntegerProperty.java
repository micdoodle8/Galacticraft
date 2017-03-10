package codechicken.lib.block.property.unlisted;

/**
 * Created by covers1624 on 28/10/2016.
 */
public class UnlistedIntegerProperty extends UnlistedPropertyBase<Integer> {

    public UnlistedIntegerProperty(String name) {
        super(name);
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public String valueToString(Integer value) {
        return value.toString();
    }
}
