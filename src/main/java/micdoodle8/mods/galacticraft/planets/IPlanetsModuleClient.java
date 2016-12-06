package micdoodle8.mods.galacticraft.planets;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

public interface IPlanetsModuleClient
{
    public void preInit(FMLPreInitializationEvent event);

    public void registerVariants();

    public void init(FMLInitializationEvent event);

    public void postInit(FMLPostInitializationEvent event);

    public void getGuiIDs(List<Integer> idList);

    public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z);

    public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData);
}
