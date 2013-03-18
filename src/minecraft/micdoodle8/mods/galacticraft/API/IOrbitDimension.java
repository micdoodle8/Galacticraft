package micdoodle8.mods.galacticraft.API;

import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldProvider;

public interface IOrbitDimension extends IGalacticraftWorldProvider
{
	/**
	 * @return the name of the world that this dimension is orbiting. For the overworld it returns "Overworld"
	 */
	public String getPlanetToOrbit();
	
	/**
	 * @return the itemstack required to create a spacestation here
	 */
	public ItemStack getRequiredItemStack();
	
	/**
	 * @return the y-coordinate that entities will fall back into the world we are orbiting
	 */
	public int getYCoordToTeleport();
}
