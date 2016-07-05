package micdoodle8.mods.galacticraft.planets.asteroids.blocks;

import micdoodle8.mods.galacticraft.core.blocks.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemBlockAsteroids;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemBlockShortRangeTelepad;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemBlockWalkway;
import micdoodle8.mods.galacticraft.planets.mars.blocks.BlockTier2TreasureChest;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class AsteroidBlocks
{
    public static Block blockWalkway;
    public static Block blockBasic;
    //	public static Block machineFrame;
    public static Block beamReflector;
    public static Block beamReceiver;
    public static Block shortRangeTelepad;
    public static Block fakeTelepad;
    public static Block treasureChestTier2;
    public static Block treasureChestTier3;
    public static Block blockDenseIce;
	public static Block blockMinerBase;
	public static Block minerBaseFull;

    public static void initBlocks()
    {
        AsteroidBlocks.treasureChestTier2 = new BlockTier2TreasureChest("treasureT2");
        AsteroidBlocks.treasureChestTier3 = new BlockTier3TreasureChest("treasureT3");
        AsteroidBlocks.blockWalkway = new BlockWalkway("walkway");
        AsteroidBlocks.blockBasic = new BlockBasicAsteroids("asteroidsBlock");
//		AsteroidBlocks.machineFrame = new BlockMachineFrame("machineFrameOld");
        AsteroidBlocks.beamReflector = new BlockBeamReflector("beamReflector");
        AsteroidBlocks.beamReceiver = new BlockBeamReceiver("beamReceiver");
        AsteroidBlocks.shortRangeTelepad = new BlockShortRangeTelepad("telepadShort");
        AsteroidBlocks.fakeTelepad = new BlockTelepadFake("telepadFake");
        AsteroidBlocks.blockDenseIce = new BlockIceAsteroids("denseIce");
        AsteroidBlocks.blockMinerBase = new BlockMinerBase("minerBase");
        AsteroidBlocks.minerBaseFull = new BlockMinerBaseFull("minerBaseFull");

        GCBlocks.hiddenBlocks.add(AsteroidBlocks.fakeTelepad);
        GCBlocks.hiddenBlocks.add(AsteroidBlocks.minerBaseFull);
    }

    private static void registerBlock(Block block, Class<? extends ItemBlock> itemClass)
    {
        GameRegistry.registerBlock(block, itemClass, block.getUnlocalizedName().substring(5));
    }

    public static void registerBlocks()
    {
        registerBlock(AsteroidBlocks.treasureChestTier2, ItemBlockDesc.class);
        registerBlock(AsteroidBlocks.treasureChestTier3, ItemBlockDesc.class);
        registerBlock(AsteroidBlocks.blockBasic, ItemBlockAsteroids.class);
        registerBlock(AsteroidBlocks.blockWalkway, ItemBlockWalkway.class);
//		registerBlock(AsteroidBlocks.machineFrame, ItemBlockGC.class);
        registerBlock(AsteroidBlocks.beamReflector, ItemBlockDesc.class);
        registerBlock(AsteroidBlocks.beamReceiver, ItemBlockDesc.class);
        registerBlock(AsteroidBlocks.shortRangeTelepad, ItemBlockShortRangeTelepad.class);
        registerBlock(AsteroidBlocks.fakeTelepad, ItemBlockGC.class);
        registerBlock(AsteroidBlocks.blockDenseIce, ItemBlockGC.class);
       	registerBlock(AsteroidBlocks.blockMinerBase, ItemBlockDesc.class);
       	registerBlock(AsteroidBlocks.minerBaseFull, ItemBlockDesc.class);
    }

    private static void setHarvestLevel(Block block, String toolClass, int level, int meta)
    {
        block.setHarvestLevel(toolClass, level, block.getStateFromMeta(meta));
    }

    private static void setHarvestLevel(Block block, String toolClass, int level)
    {
        block.setHarvestLevel(toolClass, level);
    }
    
    public static void setHarvestLevels()
    {
        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 0, 0);   //Rock
        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 0, 1);   //Rock
        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 0, 2);   //Rock
        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 2, 3);   //Aluminium
        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 3, 4);   //Ilmenite
        setHarvestLevel(AsteroidBlocks.blockBasic, "pickaxe", 2, 5);   //Iron
    }

    public static void oreDictRegistration()
    {
        OreDictionary.registerOre("oreAluminum", new ItemStack(AsteroidBlocks.blockBasic, 1, 3));
        OreDictionary.registerOre("oreAluminium", new ItemStack(AsteroidBlocks.blockBasic, 1, 3));
        OreDictionary.registerOre("oreNaturalAluminum", new ItemStack(AsteroidBlocks.blockBasic, 1, 3));
		OreDictionary.registerOre("oreIlmenite", new ItemStack(AsteroidBlocks.blockBasic, 1, 4));
        OreDictionary.registerOre("oreIron", new ItemStack(AsteroidBlocks.blockBasic, 1, 5));
    }
}
