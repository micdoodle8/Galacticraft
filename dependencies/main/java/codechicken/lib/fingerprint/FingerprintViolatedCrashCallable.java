package codechicken.lib.fingerprint;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.common.ICrashCallable;

import java.util.List;

/**
 * Created by covers1624 on 12/12/2016.
 * TODO, report all good when no invalid mods are found.
 */
public class FingerprintViolatedCrashCallable implements ICrashCallable {

    private final String mod;
    private final ImmutableList<String> invalidMods;

    public FingerprintViolatedCrashCallable(String mod, List<String> invalidMods) {
        this.mod = mod;
        this.invalidMods = ImmutableList.copyOf(invalidMods);
    }

    @Override
    public String getLabel() {
        return mod + " Invalid Fingerprint Reports";
    }

    @Override
    public String call() throws Exception {
        StringBuilder builder = new StringBuilder();
        for (String invalidMod : invalidMods) {
            builder.append("\n\t\t");
            builder.append(invalidMod);
            builder.append(" has an invalid fingerprint.");
        }
        return builder.toString();
    }
}
