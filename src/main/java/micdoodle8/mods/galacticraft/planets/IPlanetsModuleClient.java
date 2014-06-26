package micdoodle8.mods.galacticraft.planets;

import micdoodle8.mods.galacticraft.api.vector.Vector3;
import net.minecraft.block.Block;

public interface IPlanetsModuleClient extends IPlanetsModule
{
	public int getBlockRenderID(Block block);

	public void spawnParticle(String particleID, Vector3 position, Vector3 color);
}
