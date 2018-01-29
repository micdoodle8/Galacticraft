package codechicken.core;

import codechicken.lib.util.ReflectionManager;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.ForgeVersion.CheckResult;
import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.versioning.ComparableVersion;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ModDescriptionEnhancer {
    public static void enhanceMod(Object mod) {
        ModContainer mc = FMLCommonHandler.instance().findContainerFor(mod);
        mc.getMetadata().description = enhanceDesc(mc.getMetadata().description);
    }

    public static String enhanceDesc(String desc) {
        int supportersIdx = desc.indexOf("Supporters:");
        if (supportersIdx < 0) {
            return desc;
        }

        String supportersList = desc.substring(supportersIdx);
        supportersList = supportersList.replaceAll("\\b(\\w+)\\b", TextFormatting.AQUA + "$1");
        return desc.substring(0, supportersIdx) + supportersList;
    }

    public static void setUpdateStatus(String modId, Status status, ComparableVersion version) {
        try {
            ModContainer modContainer = FMLCommonHandler.instance().findContainerFor(modId);
            Map<ComparableVersion, String> changes = new HashMap<ComparableVersion, String>();
            CheckResult result = ReflectionManager.newInstance(CheckResult.class, status, version, changes, "");
            setUpdateStatus(modContainer, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setUpdateStatus(ModContainer container, CheckResult result) {
        try {
            Map<ModContainer, CheckResult> resultMap = ReflectionManager.getField(ForgeVersion.class, Map.class, null, "results");
            synchronized (resultMap) {
                resultMap.put(container, result);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
