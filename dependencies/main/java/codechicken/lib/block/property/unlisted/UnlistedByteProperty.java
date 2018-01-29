package codechicken.lib.block.property.unlisted;

/**
 * Created by covers1624 on 28/10/2016.
 */
public class UnlistedByteProperty extends UnlistedPropertyBase<Byte> {

    public UnlistedByteProperty(String name) {
        super(name);
    }

    @Override
    public Class<Byte> getType() {
        return Byte.class;
    }

    @Override
    public String valueToString(Byte value) {
        return value.toString();
    }
}
