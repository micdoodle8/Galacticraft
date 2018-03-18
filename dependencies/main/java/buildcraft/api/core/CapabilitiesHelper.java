package buildcraft.api.core;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/** Forge has a wonderful system for capabilities, which provides a better way of managing mod compat even if the target
 * mod isn't loaded. Said system uses ASM data to inject capabilities into every mod, which makes it a matter of
 * checking a (generic) field at runtime.
 * <p>
 * Unfortunately this doesn't even slightly work when using a test framework like JUnit, as
 * {@link CapabilityManager#register(Class, IStorage, Callable)} doesn't return the registered {@link Capability}
 * instances, and we don't load classes with forge's class loaders in JUnit. This class provides two useful methods to
 * compensate: {@link #registerCapability(Class)} and {@link #registerCapability(CheckedStorage, Callable)}, both of
 * which returns the registered capability.
 * <p>
 * This is NOT designed for mods wishing to add compatibility for buildcraft capability instances: those should still go
 * via the forge-recommended {@link CapabilityInject} route, or refer to the various fields in buildcraft api classes
 * containing the capability instances. */
public class CapabilitiesHelper {

    // ################
    //
    // Public API
    //
    // ################

    /** Registers a given type with {@link #registerCapability(CheckedStorage, Callable)}, but with a
     * {@link ThrowingStorage} and a factory that throws an {@link UnsupportedOperationException} instead of creating a
     * new capability instance.
     * 
     * @param clazz The type that all instances must derive from.
     * @return The registered {@link Capability} */
    @Nonnull
    public static <T> Capability<T> registerCapability(Class<T> clazz) {
        return registerCapability(new ThrowingStorage<>(clazz), () -> {
            throw new UnsupportedOperationException("You must create your own instances!");
        });
    }

    /** Registers a given type with the {@link CapabilityManager}, but also returns the capability instance.
     * 
     * @param storage The storage for the capability. This must extend {@link CheckedStorage} in order to allow the
     *            internal mechanisms to ensure that nothing went wrong during our meddling into forge.
     * @param factory The factory for the capability.
     * @return The registered {@link Capability} */
    @Nonnull
    public static <T> Capability<T> registerCapability(CheckedStorage<T> storage, Callable<T> factory) {
        return registerCapInternal(storage, factory);
    }

    /** A type of {@link IStorage} that contains the class that it would store. Used by the internal mechanisms of
     * {@link CapabilitiesHelper} to ensure that everything registers properly. A default always-throwing implementation
     * is {@link ThrowingStorage}.
     * 
     * @param <T> The type of this storage */
    public static abstract class CheckedStorage<T> implements IStorage<T> {

        public final Class<T> clazz;

        public CheckedStorage(Class<T> clazz) {
            this.clazz = clazz;
        }
    }

    /** A type of {@link CheckedStorage} that throws an {@link UnsupportedOperationException} from both the read and
     * write methods. It is designed for {@link Capability Capability's} that must be written and read separately. */
    public static final class ThrowingStorage<T> extends CheckedStorage<T> {

        public ThrowingStorage(Class<T> clazz) {
            super(clazz);
        }

        @Override
        public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
            throw new UnsupportedOperationException("You must create your own instances!");
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
            throw new UnsupportedOperationException("You must create your own instances!");
        }
    }

    // ################
    //
    // Internals
    //
    // ################

    private static final IdentityHashMap<?, ?> __FIELD_CAP_MANAGER_PROVIDERS;

    static {
        try {
            Field fld = CapabilityManager.class.getDeclaredField("providers");
            fld.setAccessible(true);
            Object obj = fld.get(CapabilityManager.INSTANCE);
            __FIELD_CAP_MANAGER_PROVIDERS = (IdentityHashMap<?, ?>) obj;
            if (__FIELD_CAP_MANAGER_PROVIDERS == null) {
                throw new Error("Couldn't find providers!");
            }
        } catch (ReflectiveOperationException | ClassCastException e) {
            throw new Error(e);
        }
    }

    @Nonnull
    private static <T> Capability<T> registerCapInternal(CheckedStorage<T> storage, Callable<T> factory) {
        Class<T> clazz = storage.clazz;

        CapabilityManager.INSTANCE.register(clazz, storage, factory);

        String fullName = clazz.getName().intern();
        Object obj = __FIELD_CAP_MANAGER_PROVIDERS.get(fullName);
        if (obj == null) {
            throw new IllegalStateException("Apparently we didn't register the capability? How?");
        }
        if (!(obj instanceof Capability)) {
            throw new Error("We must have the wrong map! providers.get(key) returned " + obj.getClass()
                + " rather than " + Capability.class);
        }
        Capability<?> cap = (Capability<?>) obj;
        // Ensure that the given cap is actually *our* capability
        // compare the given class with the VoidStorage class, as
        // Capability<T> doesn't have a way of doing that directly.
        IStorage<?> cStorage = cap.getStorage();
        if (!(cStorage instanceof CheckedStorage)) {
            throw new IllegalStateException(
                "Returned capability storage has a different storage class than expected! " + cStorage.getClass());
        }
        CheckedStorage<?> vStorage = (CheckedStorage<?>) cStorage;
        if (vStorage.clazz != clazz) {
            throw new IllegalStateException(
                "Returned capability storage has a different class than expected! " + vStorage.clazz + " vs " + clazz);
        }
        return (Capability<T>) cap;
    }

    /** Prefer {@link #registerCapability(Class)} or other methods over this one: this will be removed at some point in
     * the future. Most likely before beta. */
    @Nonnull
    @Deprecated
    public static <T> Capability<T> ensureRegistration(Capability<T> cap, Class<T> clazz) {
        if (cap == null) {
            throw new Error("Capability registration failed for " + clazz);
        }
        return cap;
    }

    public static class VoidStorage<T> implements Capability.IStorage<T> {
        @Override
        public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
            throw new IllegalStateException("You must create your own instances!");
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
            throw new IllegalStateException("You must create your own instances!");
        }
    }
}
