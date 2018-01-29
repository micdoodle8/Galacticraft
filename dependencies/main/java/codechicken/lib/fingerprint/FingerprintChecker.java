package codechicken.lib.fingerprint;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.fml.common.*;
import org.apache.logging.log4j.Level;

import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by covers1624 on 13/12/2016.
 * To be packaged in each mod runtime, used to cross verify jar fingerprints.
 * Idea behind this:
 * A malicious user would have to override the fingerprint data in all mod jars this is packaged with in order to avoid detection.
 * This is not to be used as DRM. It is used simply to provide cross verification of jars.
 */
public class FingerprintChecker {

    private static final Map<String, String> modCertMap = new HashMap<String, String>();
    private static final List<String> invalidMods = new ArrayList<String>();

    static {
        //@formatter:off
        modCertMap.put("CodeChickenLib"    , "f1850c39b2516232a2108a7bd84d1cb5df93b261");
        modCertMap.put("CodeChickenCore"   , "f1850c39b2516232a2108a7bd84d1cb5df93b261");
        modCertMap.put("ChickenChunks"     , "f1850c39b2516232a2108a7bd84d1cb5df93b261");
        modCertMap.put("EnderStorage"      , "f1850c39b2516232a2108a7bd84d1cb5df93b261");
        modCertMap.put("Translocator"      , "f1850c39b2516232a2108a7bd84d1cb5df93b261");
        modCertMap.put("NotEnoughItems"    , "f1850c39b2516232a2108a7bd84d1cb5df93b261");
        modCertMap.put("forgemultipartcbe" , "f1850c39b2516232a2108a7bd84d1cb5df93b261");
        //@formatter:on
    }

    public static void runFingerprintChecks() {
        try {
            ModContainer activeContainer = Loader.instance().activeModContainer();
            for (Entry<String, ModContainer> modEntry : Loader.instance().getIndexedModList().entrySet()) {
                for (Entry<String, String> certEntry : modCertMap.entrySet()) {
                    if (modEntry.getKey().equals(certEntry.getKey())) {
                        Object modInstance = modEntry.getValue().getMod();
                        if (modInstance == null) {
                            FMLLog.log(activeContainer.getModId() + " Fingerprint Verification", Level.FATAL, "Unable to do Fingerprint Verification for mod %s! ModContainer returned a null mod instance!", modEntry.getKey());
                            break;
                        }
                        if (modInstance.getClass().getName().contains("net.minecraftforge.")) {
                            FMLLog.log(activeContainer.getModId() + " Fingerprint Verification", Level.FATAL, "Unable to do Fingerprint Verification for mod %s! ModContainer returned is a suspected FML class! [%s]", modEntry.getKey(), modInstance.getClass().getName());
                        }

                        Certificate[] certificates = modInstance.getClass().getProtectionDomain().getCodeSource().getCertificates();
                        ImmutableList.Builder<String> certBuilder = ImmutableList.builder();
                        if (certificates != null) {
                            for (Certificate cert : certificates) {
                                certBuilder.add(CertificateHelper.getFingerprint(cert));
                            }
                        }

                        ImmutableList<String> certList = certBuilder.build();

                        String expectedFingerprint = certEntry.getValue();

                        if (expectedFingerprint != null && !expectedFingerprint.isEmpty()) {
                            if (!certList.contains(expectedFingerprint)) {
                                FMLLog.log(activeContainer.getModId() + " Fingerprint Verification", Level.FATAL, "The fingerprint for mod %s is invalid! Expected: %s", modEntry.getKey(), expectedFingerprint);
                                invalidMods.add(modEntry.getKey());
                                break;
                            } else {
                                FMLLog.log(activeContainer.getModId() + " Fingerprint Verification", Level.DEBUG, "Valid fingerprint found for mod %s.", modEntry.getKey());
                            }
                        }

                        break;
                    }
                }
            }
            FMLCommonHandler.instance().registerCrashCallable(new FingerprintViolatedCrashCallable(activeContainer.getModId(), invalidMods));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
