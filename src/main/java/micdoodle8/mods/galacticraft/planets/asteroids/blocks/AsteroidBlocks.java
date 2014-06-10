package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.planets.GalacticraftPlanets;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemBlockAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemBlockShortRangeTelepad;
import net.minecraft.block.Block;
import cpw.mods.fml.common.registry.GameRegistry;

public class AsteroidBlocks
{
	public static Block blockWalkway;
	public static Block blockWalkwayWire;
	public static Block blockWalkwayOxygenPipe;
	public static Block blockBasic;
	public static Block machineFrame;
	public static Block beamReflector;
	public static Block beamReceiver;
	public static Block shortRangeTelepad;
	
	public static void initBlocks()
	{
		blockWalkway = new BlockWalkway("walkway");
		blockWalkwayWire = new BlockWalkway("walkwayWire");
		blockWalkwayOxygenPipe = new BlockWalkway("walkwayOxygenPipe");
		blockBasic = new BlockBasicAsteroids("asteroidsBlock");
		machineFrame = new BlockMachineFrame("machineFrameOld");
		beamReflector = new BlockBeamReflector("beamReflector");
		beamReceiver = new BlockBeamReceiver("beamReceiver");
		shortRangeTelepad = new BlockShortRangeTelepad("telepadShort");
	}

	public static void registerBlocks()
	{
		GameRegistry.registerBlock(AsteroidBlocks.blockWalkway, ItemBlockGC.class, AsteroidBlocks.blockWalkway.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerBlock(AsteroidBlocks.blockWalkwayWire, ItemBlockGC.class, AsteroidBlocks.blockWalkwayWire.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerBlock(AsteroidBlocks.blockWalkwayOxygenPipe, ItemBlockGC.class, AsteroidBlocks.blockWalkwayOxygenPipe.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerBlock(AsteroidBlocks.blockBasic, ItemBlockAsteroids.class, AsteroidBlocks.blockBasic.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerBlock(AsteroidBlocks.machineFrame, ItemBlockGC.class, AsteroidBlocks.machineFrame.getUnlocalizedName(), GalacticraftPlanets.MODID);
		GameRegistry.registerBlock(AsteroidBlocks.beamReflector, ItemBlockGC.class, AsteroidBlocks.beamReflector.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(AsteroidBlocks.beamReceiver, ItemBlockGC.class, AsteroidBlocks.beamReceiver.getUnlocalizedName(), GalacticraftCore.MODID);
		GameRegistry.registerBlock(AsteroidBlocks.shortRangeTelepad, ItemBlockShortRangeTelepad.class, AsteroidBlocks.shortRangeTelepad.getUnlocalizedName(), GalacticraftCore.MODID);
	}
}
