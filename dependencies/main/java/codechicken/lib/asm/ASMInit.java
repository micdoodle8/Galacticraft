package codechicken.lib.asm;

import net.minecraft.launchwrapper.Launch;

/**
 * Initialisation class for using this package. Call this on coremod load
 */
public class ASMInit
{
    private static boolean initialised = false;
    public static void init() {
        if(!initialised) {
            Launch.classLoader.addTransformerExclusion("codechicken.lib.asm");
            Launch.classLoader.addTransformerExclusion("codechicken.lib.config");
            initialised = true;
        }
    }
}
