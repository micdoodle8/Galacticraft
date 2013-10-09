package codechicken.core.asm;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import codechicken.core.CCUpdateChecker;
import codechicken.core.featurehack.LiquidTextures;
import codechicken.core.internal.ClientTickHandler;
import codechicken.core.launch.CodeChickenCorePlugin;
import codechicken.lib.config.ConfigFile;
import codechicken.packager.Packager;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;
import cpw.mods.fml.relauncher.Side;

@Packager(getBaseDirectories = {"CodeChickenCore"}, getName = "CodeChickenCore", getVersion = "0.9.0.6")
public class CodeChickenCoreModContainer extends DummyModContainer
{
    public static ConfigFile config;
    
    public static void loadConfig()
    {
        File cfgDir = new File(CodeChickenCorePlugin.minecraftDir+"/config");
        if(!cfgDir.exists())
            cfgDir.mkdirs();
        config = new ConfigFile(new File(cfgDir, "CodeChickenCore.cfg")).setComment("CodeChickenCore configuration file.");
    }
    
    public CodeChickenCoreModContainer()
    {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId       = "CodeChickenCore";
        meta.name        = "CodeChicken Core";
        meta.version     = getClass().getAnnotation(Packager.class).getVersion();
        meta.authorList  = Arrays.asList("ChickenBones");
        meta.description = "Base common code for all chickenbones mods.";
        meta.url         = "http://www.minecraftforum.net/topic/909223-";
    }
    
    @Override
    public List<ArtifactVersion> getDependants()
    {
        LinkedList<ArtifactVersion> deps = new LinkedList<ArtifactVersion>();
        deps.add(VersionParser.parseVersionReference("NotEnoughItems@[1.6.1.5,)"));
        deps.add(VersionParser.parseVersionReference("EnderStorage@[1.4.3.4,)"));
        deps.add(VersionParser.parseVersionReference("ChickenChunks@[1.3.3.3,)"));
        deps.add(VersionParser.parseVersionReference("Translocator@[1.1.0.13,)"));
        deps.add(VersionParser.parseVersionReference("WR-CBE|Core@[1.4.0.6,)"));
        return deps;
    }
    
    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }
    
    @Subscribe
    public void preInit(FMLPreInitializationEvent event)
    {
        if(event.getSide().isClient())
            LiquidTextures.init();
    }
    
    @Subscribe
    public void init(FMLInitializationEvent event)
    {
        if(event.getSide().isClient())
        {
            if(config.getTag("checkUpdates").getBooleanValue(true))
                CCUpdateChecker.updateCheck(getModId());
            TickRegistry.registerTickHandler(new ClientTickHandler(), Side.CLIENT);
        }
    }
    
    @Override
    public VersionRange acceptableMinecraftVersionRange()
    {
        return VersionParser.parseRange(CodeChickenCorePlugin.mcVersion);
    }
}
