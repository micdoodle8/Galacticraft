package micdoodle8.mods.galacticraft.planets;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;

public interface IPlanetsModule
{
	public void preInit(FMLPreInitializationEvent event);

	public void init(FMLInitializationEvent event);

	public void postInit(FMLPostInitializationEvent event);

	public void serverStarting(FMLServerStartingEvent event);
	
	public void getGuiIDs(List<Integer> idList);
	
	public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z);
}
