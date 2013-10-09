package codechicken.nei.asm;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import codechicken.core.CommonUtils;
import codechicken.core.launch.CodeChickenCorePlugin;
import codechicken.nei.ClientHandler;
import codechicken.nei.IDConflictReporter;
import codechicken.nei.ServerHandler;
import codechicken.nei.api.IConfigureNEI;
import codechicken.packager.Packager;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.client.FMLFileResourcePack;
import cpw.mods.fml.client.FMLFolderResourcePack;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;

@Packager(getName = "NotEnoughItems", getBaseDirectories = {"NotEnoughItems"}, getVersion = "1.6.1.5")
public class NEIModContainer extends DummyModContainer
{    
    public static LinkedList<IConfigureNEI> plugins = new LinkedList<IConfigureNEI>();
    private LoadController controller;
    
    public NEIModContainer()
    {
        super(new ModMetadata());
        getMetadata();
    }
    
    @Override
    public Set<ArtifactVersion> getRequirements()
    {
        Set<ArtifactVersion> deps = new HashSet<ArtifactVersion>();
        deps.add(VersionParser.parseVersionReference("CodeChickenCore@[0.9.0.6,)"));
        return deps;
    }
    
    @Override
    public List<ArtifactVersion> getDependencies()
    {
        return new LinkedList<ArtifactVersion>(getRequirements());
    }
    
    @Override
    public ModMetadata getMetadata()
    {
        ModMetadata meta = super.getMetadata();

        meta.modId       = "NotEnoughItems";
        meta.name        = "Not Enough Items";
        meta.version     = NEIModContainer.class.getAnnotation(Packager.class).getVersion();
        meta.authorList  = Arrays.asList("ChickenBones");
        meta.description = "Recipe Viewer, Inventory Manager, Item Spawner, Cheats and more.\n\247f\n";
        meta.url         = "http://www.minecraftforum.net/topic/909223-";
        meta.credits     = "Alexandria - Original Idea";
        
        if(plugins.size() == 0)
        {
            meta.description += "\247cNo installed plugins.";
        }
        else
        {
            meta.description += "\247aInstalled plugins: ";
            for(int i = 0; i < plugins.size(); i++)
            {
                if(i > 0)
                    meta.description += ", ";
                IConfigureNEI plugin = plugins.get(i);
                meta.description += plugin.getName()+" "+plugin.getVersion();
            }
            meta.description += ".";
        }
        
        return meta;
    }
    
    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        this.controller = controller;
        return true;        
    }
    
    @Subscribe
    public void init(FMLInitializationEvent event)
    {
        if(CommonUtils.isClient())
            ClientHandler.load();
        
        ServerHandler.load();
    }
    
    @Subscribe
    public void postInit(FMLPostInitializationEvent event)
    {
        try
        {
            IDConflictReporter.postInit();
        }
        catch(Throwable t)
        {
            controller.errorOccurred(this, t);
        }
    }
    
    @Override
    public VersionRange acceptableMinecraftVersionRange()
    {
        return VersionParser.parseRange(CodeChickenCorePlugin.mcVersion);
    }
    
    @Override
    public File getSource()
    {
        return NEICorePlugin.location;
    }
    
    @Override
    public Class<?> getCustomResourcePackClass()
    {
        return getSource().isDirectory() ? FMLFolderResourcePack.class : FMLFileResourcePack.class;
    }
}
