package micdoodle8.mods.miccore;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;

public class MicdoodleModContainer extends DummyModContainer
{
    public MicdoodleModContainer()
    {
        super(new ModMetadata());
        ModMetadata meta = this.getMetadata();
        meta.modId = "Micdoodlecore";
        meta.name = "Micdoodle8 Core";
        meta.updateUrl = "http://www.micdoodle8.com/";
        meta.description = "Provides core features of Micdoodle8's mods";
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
        return VersionParser.parseRange(MicdoodlePlugin.mcVersion);
    }
}
