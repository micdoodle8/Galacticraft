package buildcraft.api;

import java.util.Locale;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;

public enum BCModules implements IBuildCraftMod {
    LIB,
    // Base module for all BC.
    CORE,
    // Potentially optional modules for adding more BC functionality
    BUILDERS,
    ENERGY,
    FACTORY,
    ROBOTICS,
    SILICON,
    TRANSPORT,
    // Optional module for compatibility with other mods
    COMPAT;

    public static final BCModules[] VALUES = values();
    private static boolean hasChecked = false;

    public final String lowerCaseName = name().toLowerCase(Locale.ROOT);
    private final String modId = "buildcraft" + lowerCaseName;
    private boolean loaded;

    private static void checkLoadStatus() {
        if (hasChecked) {
            return;
        }
        hasChecked = true;
        if (!Loader.instance().hasReachedState(LoaderState.PREINITIALIZATION)) {
            throw new RuntimeException("You can only use EnumBuidCraftModule.isLoaded from pre-init onwards!");
        }
        for (BCModules module : VALUES) {
            module.loaded = Loader.isModLoaded(module.modId);
        }
    }

    public static boolean isBcMod(String testModId) {
        for (BCModules mod : VALUES) {
            if (mod.modId.equals(testModId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getModId() {
        return modId;
    }

    public boolean isLoaded() {
        checkLoadStatus();
        return loaded;
    }
}
