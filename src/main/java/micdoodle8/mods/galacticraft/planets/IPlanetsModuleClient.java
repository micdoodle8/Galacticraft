package micdoodle8.mods.galacticraft.planets;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.List;

public interface IPlanetsModuleClient
{
    public void preInit(FMLPreInitializationEvent event);

    public void init(FMLInitializationEvent event);

    public void postInit(FMLPostInitializationEvent event);

    public void getGuiIDs(List<Integer> idList);

    public Object getGuiElement(Side side, int ID, EntityPlayer player, World world, int x, int y, int z);

	public int getBlockRenderID(Block block);

	public void spawnParticle(String particleID, Vector3 position, Vector3 motion);
}
