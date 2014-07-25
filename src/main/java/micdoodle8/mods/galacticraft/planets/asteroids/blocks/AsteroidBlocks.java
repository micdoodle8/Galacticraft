package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemBlockAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemBlockShortRangeTelepad;
import net.minecraft.block.Block;

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
    public static Block fakeTelepad;
    public static Block treasureChestTier3;

	public static void initBlocks()
	{
		AsteroidBlocks.blockWalkway = new BlockWalkway("walkway");
		AsteroidBlocks.blockWalkwayWire = new BlockWalkway("walkwayWire");
		AsteroidBlocks.blockWalkwayOxygenPipe = new BlockWalkway("walkwayOxygenPipe");
		AsteroidBlocks.blockBasic = new BlockBasicAsteroids("asteroidsBlock");
		AsteroidBlocks.machineFrame = new BlockMachineFrame("machineFrameOld");
		AsteroidBlocks.beamReflector = new BlockBeamReflector("beamReflector");
		AsteroidBlocks.beamReceiver = new BlockBeamReceiver("beamReceiver");
		AsteroidBlocks.shortRangeTelepad = new BlockShortRangeTelepad("telepadShort");
		AsteroidBlocks.fakeTelepad = new BlockTelepadFake("telepadFake");
		AsteroidBlocks.treasureChestTier3 = GCBlocks.treasureChestTier3;
	}

	public static void registerBlocks()
	{
		GameRegistry.registerBlock(AsteroidBlocks.blockWalkway, ItemBlockGC.class, AsteroidBlocks.blockWalkway.getUnlocalizedName());
		GameRegistry.registerBlock(AsteroidBlocks.blockWalkwayWire, ItemBlockGC.class, AsteroidBlocks.blockWalkwayWire.getUnlocalizedName());
		GameRegistry.registerBlock(AsteroidBlocks.blockWalkwayOxygenPipe, ItemBlockGC.class, AsteroidBlocks.blockWalkwayOxygenPipe.getUnlocalizedName());
		GameRegistry.registerBlock(AsteroidBlocks.blockBasic, ItemBlockAsteroids.class, AsteroidBlocks.blockBasic.getUnlocalizedName());
		GameRegistry.registerBlock(AsteroidBlocks.machineFrame, ItemBlockGC.class, AsteroidBlocks.machineFrame.getUnlocalizedName());
		GameRegistry.registerBlock(AsteroidBlocks.beamReflector, ItemBlockGC.class, AsteroidBlocks.beamReflector.getUnlocalizedName());
		GameRegistry.registerBlock(AsteroidBlocks.beamReceiver, ItemBlockGC.class, AsteroidBlocks.beamReceiver.getUnlocalizedName());
		GameRegistry.registerBlock(AsteroidBlocks.shortRangeTelepad, ItemBlockShortRangeTelepad.class, AsteroidBlocks.shortRangeTelepad.getUnlocalizedName());
		GameRegistry.registerBlock(AsteroidBlocks.fakeTelepad, ItemBlockGC.class, AsteroidBlocks.fakeTelepad.getUnlocalizedName());
		GameRegistry.registerBlock(AsteroidBlocks.treasureChestTier3, ItemBlockGC.class, AsteroidBlocks.treasureChestTier3.getUnlocalizedName());
	}
}
