package micdoodle8.mods.galacticraft.planets;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public interface IPlanetsModule
{
    void init(FMLCommonSetupEvent event);

    void serverInit(FMLServerStartedEvent event);

    void serverStarting(FMLServerStartingEvent event);

//    public void getGuiIDs(List<Integer> idList);

//    public Object getGuiElement(LogicalSide LogicalSide, int ID, PlayerEntity player, World world, int x, int y, int z);

//    public Configuration getConfiguration();
}
