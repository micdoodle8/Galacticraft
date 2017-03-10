package codechicken.lib.util;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

/**
 * Created by covers1624 on 13/01/2017.
 */
public class BlockStateUtils {

    public static int hashBlockState(IBlockState state) {
        if (state instanceof IExtendedBlockState) {
            return hashBlockState((IExtendedBlockState) state);
        }
        return hashBlockState_(state);
    }

    public static int hashBlockState_(IBlockState state) {
        return Objects.hashCode(state.toString());
    }

    @SuppressWarnings ("unchecked")
    private static int hashBlockState(IExtendedBlockState state) {
        List<String> toHash = new LinkedList<String>();
        toHash.add(state.toString());
        toHash.add("meta=" + state.getBlock().getMetaFromState(state));
        for (Entry<IUnlistedProperty<?>, Optional<?>> entry : state.getUnlistedProperties().entrySet()) {
            IUnlistedProperty property = entry.getKey();
            toHash.add(property.getName() + "=" + property.valueToString(state.getValue(property)));
        }
        return Objects.hashCode(toHash.toArray());
    }

}
