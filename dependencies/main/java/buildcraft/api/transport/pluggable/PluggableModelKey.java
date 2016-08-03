package buildcraft.api.transport.pluggable;

import java.util.Arrays;
import java.util.Objects;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class PluggableModelKey<K extends PluggableModelKey<K>> {
    public final EnumWorldBlockLayer layer;
    public final IPluggableModelBaker<K> baker;
    public final EnumFacing side;
    private final int hash;

    public PluggableModelKey(EnumWorldBlockLayer layer, IPluggableModelBaker<K> baker, EnumFacing side) {
        this.layer = layer;
        if (layer != EnumWorldBlockLayer.CUTOUT && layer != EnumWorldBlockLayer.TRANSLUCENT) {
            throw new IllegalArgumentException("Can only use CUTOUT or TRANSLUCENT at the moment (was " + layer + ")");
        }
        if (baker == null) throw new NullPointerException("baker");
        this.baker = baker;
        this.side = side;
        /* Don't inclue the block layer in the hash code as there are different caches for cutout and translucent */
        this.hash = Arrays.hashCode(new int[] { System.identityHashCode(baker), Objects.hashCode(side) });
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PluggableModelKey<?> other = (PluggableModelKey<?>) obj;
        if (baker != other.baker) return false;
        if (side != other.side) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return hash;
    }
}
