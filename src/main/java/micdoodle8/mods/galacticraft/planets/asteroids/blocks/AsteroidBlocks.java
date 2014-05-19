package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;

public class AsteroidBlocks
{
	public static Block blockWalkway;
	public static Block blockWalkwayWire;
	public static Block blockWalkwayOxygenPipe;
	
	public static void initBlocks()
	{
		blockWalkway = new BlockWalkway("walkway");
		blockWalkwayWire = new BlockWalkway("walkwayWire");
		blockWalkwayOxygenPipe = new BlockWalkway("walkwayOxygenPipe");
	}

	public static void registerBlocks()
	{
		GameRegistry.registerBlock(AsteroidBlocks.blockWalkway, ItemBlockGC.class, AsteroidBlocks.blockWalkway.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerBlock(AsteroidBlocks.blockWalkwayWire, ItemBlockGC.class, AsteroidBlocks.blockWalkwayWire.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerBlock(AsteroidBlocks.blockWalkwayOxygenPipe, ItemBlockGC.class, AsteroidBlocks.blockWalkwayOxygenPipe.getUnlocalizedName(), GalacticraftPlanets.MODID);
	}
}
