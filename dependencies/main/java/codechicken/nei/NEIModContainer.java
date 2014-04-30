package codechicken.nei;

import codechicken.core.CommonUtils;
import codechicken.core.launch.CodeChickenCorePlugin;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.asm.NEICorePlugin;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.client.FMLFileResourcePack;
import cpw.mods.fml.client.FMLFolderResourcePack;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;
import net.minecraft.util.EnumChatFormatting;

import java.io.File;
import java.util.*;

public class NEIModContainer extends DummyModContainer
{
    public static LinkedList<IConfigureNEI> plugins = new LinkedList<IConfigureNEI>();

    public NEIModContainer() {
        super(MetadataCollection.from(MetadataCollection.class.getResourceAsStream("/neimod.info"), "NotEnoughItems").getMetadataForId("NotEnoughItems", null));
        getMetadata();
    }

    @Override
    public Set<ArtifactVersion> getRequirements() {
        Set<ArtifactVersion> deps = new HashSet<ArtifactVersion>();
        deps.add(VersionParser.parseVersionReference("CodeChickenCore@[0,)"));
        return deps;
    }

    @Override
    public List<ArtifactVersion> getDependencies() {
        return new LinkedList<ArtifactVersion>(getRequirements());
    }

    @Override
    public ModMetadata getMetadata() {
        ModMetadata meta = super.getMetadata();
        meta.description = "Recipe Viewer, Inventory Manager, Item Spawner, Cheats and more.\n"+ EnumChatFormatting.WHITE+"\n";

        if (plugins.size() == 0) {
            meta.description += EnumChatFormatting.RED+"No installed plugins.";
        } else {
            meta.description += EnumChatFormatting.GREEN+"Installed plugins: ";
            for (int i = 0; i < plugins.size(); i++) {
                if (i > 0)
                    meta.description += ", ";
                IConfigureNEI plugin = plugins.get(i);
                meta.description += plugin.getName() + " " + plugin.getVersion();
            }
            meta.description += ".";
        }

        return meta;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void init(FMLInitializationEvent event) {
        if (CommonUtils.isClient())
            ClientHandler.load();

        ServerHandler.load();
    }

    @Override
    public VersionRange acceptableMinecraftVersionRange() {
        return VersionParser.parseRange(CodeChickenCorePlugin.mcVersion);
    }

    @Override
    public File getSource() {
        return NEICorePlugin.location;
    }

    @Override
    public Class<?> getCustomResourcePackClass() {
        return getSource().isDirectory() ? FMLFolderResourcePack.class : FMLFileResourcePack.class;
    }
}
