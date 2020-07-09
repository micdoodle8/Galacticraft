package micdoodle8.mods.galacticraft.planets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

import java.util.List;

public interface IPlanetsModule
{
    public void init(FMLCommonSetupEvent event);

    public void serverInit(FMLServerStartedEvent event);

    public void serverStarting(FMLServerStartingEvent event);

//    public void getGuiIDs(List<Integer> idList);

//    public Object getGuiElement(LogicalSide LogicalSide, int ID, PlayerEntity player, World world, int x, int y, int z);

//    public Configuration getConfiguration();
}
