package codechicken.lib.block.property.unlisted;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

/**
 * Created by covers1624 on 21/02/2017.
 */
public class UnlistedByteArrayProperty extends UnlistedPropertyBase<byte[]> {

    public UnlistedByteArrayProperty(String name) {
        super(name);
    }

    @Override
    public Class<byte[]> getType() {
        return byte[].class;
    }

    @Override
    public String valueToString(byte[] value) {
        ToStringHelper helper = Objects.toStringHelper("ByteArray");
        if (value != null) {
            for (int i1 = 0; i1 < value.length; i1++) {
                byte i = value[i1];
                helper.add("Index:" + i1, i);
            }
        }
        return helper.toString();
    }
}
