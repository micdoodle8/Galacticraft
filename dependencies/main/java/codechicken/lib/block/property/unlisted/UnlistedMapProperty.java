package codechicken.lib.block.property.unlisted;

import java.util.Map;

/**
 * Created by covers1624 on 25/10/2016.
 */
public class UnlistedMapProperty extends UnlistedPropertyBase<Map> {

    private IMapStringGenerator generator = null;

    public UnlistedMapProperty(String name) {
        super(name);
    }

    public UnlistedMapProperty setStringGenerator(IMapStringGenerator generator) {
        if (generator == null) {
            this.generator = generator;
        }
        return this;
    }

    @Override
    public Class<Map> getType() {
        return Map.class;
    }

    @Override
    public String valueToString(Map value) {
        if (value == null) {
            return "null";
        }
        return generator != null ? generator.makeString(value) : value.toString();
    }

    public interface IMapStringGenerator {

        String makeString(Map map);
    }

}
