package codechicken.core.asm;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import codechicken.core.CCUpdateChecker;
import codechicken.core.featurehack.LiquidTextures;
import codechicken.core.internal.CCCEventHandler;
import codechicken.core.launch.CodeChickenCorePlugin;
import codechicken.lib.config.ConfigFile;

import codechicken.lib.config.ConfigTag;
import com.google.common.base.Function;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;

public class CodeChickenCoreModContainer extends DummyModContainer
{
    public static ConfigFile config;

    public static void loadConfig() {
        if(config == null)
            config = new ConfigFile(new File(CodeChickenCorePlugin.minecraftDir, "config/CodeChickenCore.cfg")).setComment("CodeChickenCore configuration file.");
    }

    public CodeChickenCoreModContainer() {
        super(MetadataCollection.from(MetadataCollection.class.getResourceAsStream("/cccmod.info"), "CodeChickenCore").getMetadataForId("CodeChickenCore", null));
    }

    @Override
    public List<ArtifactVersion> getDependants() {
        LinkedList<ArtifactVersion> deps = new LinkedList<ArtifactVersion>();
        if(!getVersion().contains("$")) {
            deps.add(VersionParser.parseVersionReference("NotEnoughItems@[1.0.2,)"));
            deps.add(VersionParser.parseVersionReference("EnderStorage@[1.4.4,)"));
        }
        return deps;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event) {
        if (event.getSide().isClient())
            LiquidTextures.init();
    }

    @Subscribe
    public void init(FMLInitializationEvent event) {
        if (event.getSide().isClient()) {
            if (config.getTag("checkUpdates").getBooleanValue(true))
                CCUpdateChecker.updateCheck(getModId());
            notificationCheck();
            FMLCommonHandler.instance().bus().register(new CCCEventHandler());
        }
    }

    private void notificationCheck() {
        final ConfigTag tag = config.getTag("checkNotifications").setComment("The most recent notification number recieved. -1 to disable");
        final int notify = tag.getIntValue(0);
        if(notify < 0)
            return;

        CCUpdateChecker.updateCheck(
                "http://www.chickenbones.net/Files/notification/general.php",
                new Function<String, Void>()
                {
                    @Override
                    public Void apply(String ret) {
                        Matcher m = Pattern.compile("Ret \\((\\d+)\\): (.+)").matcher(ret);
                        if (!m.matches()) {
                            CodeChickenCorePlugin.logger.error("Failed to check notifications: " + ret);
                            return null;
                        }
                        int index = Integer.parseInt(m.group(1));
                        if(index > notify) {
                            tag.setIntValue(index);
                            CCUpdateChecker.addUpdateMessage(m.group(2));
                        }
                        return null;
                    }
                });
    }

    @Override
    public VersionRange acceptableMinecraftVersionRange() {
        return VersionParser.parseRange(CodeChickenCorePlugin.mcVersion);
    }
}
