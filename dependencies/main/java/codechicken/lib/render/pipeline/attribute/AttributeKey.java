package codechicken.lib.render.pipeline.attribute;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by covers1624 on 10/10/2016.
 * Used to lookup and create new arrays for an attribute without a hard dep on an attribute instance.
 */
public abstract class AttributeKey<T> {

    public final int attributeKeyIndex;

    public AttributeKey() {
        attributeKeyIndex = AttributeKeyRegistry.registerAttributeKey(this);
    }

    public abstract T newArray(int length);

    public static class AttributeKeyRegistry {

        private static ArrayList<AttributeKey<?>> attributeKeys = new ArrayList<AttributeKey<?>>();

        private static int registerAttributeKey(AttributeKey<?> attr) {
            attributeKeys.add(attr);
            return attributeKeys.size() - 1;
        }

        public static AttributeKey<?> getAttributeKey(int index) {
            return attributeKeys.get(index);
        }

        public static List<AttributeKey<?>> getRegisteredAttributeKeys() {
            return ImmutableList.copyOf(attributeKeys);
        }
    }

}
