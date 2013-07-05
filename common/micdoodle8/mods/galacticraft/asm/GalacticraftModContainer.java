package micdoodle8.mods.galacticraft.asm;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;

public class GalacticraftModContainer extends DummyModContainer
{
    public GalacticraftModContainer()
    {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = "GalacticraftCoremodContainer";
        meta.name = "Galacticraft Coremod Container";
        meta.logoFile = "/micdoodle8/mods/galacticraft/logo.png";
        meta.updateUrl = "http://www.micdoodle8.com/";
        meta.credits = "fishtaco - World Gen Code. Jackson Cordes - Music. crummy194 - Models.";
        meta.description = "An advanced space travel mod for Minecraft 1.6";
        meta.version = GalacticraftCore.LOCALMAJVERSION + "." + GalacticraftCore.LOCALMINVERSION + "." + GalacticraftCore.LOCALBUILDVERSION;
        meta.authorList = Arrays.asList("micdoodle8");
        meta.url = "http://www.micdoodle8.com/";
    }
    
    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }

    @Override
    public List<ArtifactVersion> getDependencies()
    {
        LinkedList<ArtifactVersion> deps = new LinkedList<ArtifactVersion>();
        deps.add(VersionParser.parseVersionReference("required-after:Forge@[8.9.0.762,)"));
        return deps;
    }

    @Override
    public VersionRange acceptableMinecraftVersionRange()
    {
        return VersionParser.parseRange(GalacticraftPlugin.mcVersion);
    }
}
