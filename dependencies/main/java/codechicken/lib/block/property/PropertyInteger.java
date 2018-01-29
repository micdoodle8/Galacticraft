package codechicken.lib.block.property;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.properties.PropertyHelper;

import java.util.Collection;

/**
 * Created by covers1624 on 5/23/2016.
 */
public class PropertyInteger extends PropertyHelper<Integer> {

    private final ImmutableSet<Integer> valueSet;

    public PropertyInteger(String name, Collection<Integer> values) {
        super(name, Integer.class);
        valueSet = ImmutableSet.copyOf(values);
    }

    //EG, 16 = 0 - 15, 4 = 0 - 3
    public PropertyInteger(String name, int max) {
        super(name, Integer.class);
        ImmutableSet.Builder<Integer> builder = ImmutableSet.builder();
        for (int i = 0; i < max; i++) {
            builder.add(i);
        }
        valueSet = builder.build();
    }

    @Override
    public Collection<Integer> getAllowedValues() {
        return valueSet;
    }

    @Override
    public Optional<Integer> parseValue(String value) {
        if (valueSet.contains(Integer.valueOf(value))) {
            return Optional.of(Integer.valueOf(value));
        }
        return Optional.absent();
    }

    @Override
    public String getName(Integer value) {
        return String.valueOf(value);
    }
}
