package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemBlockEgg;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemBlockMachine;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemBlockMars;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class MarsBlocks
{
    public static Block marsBlock;
    public static Block blockSludge;
    public static Block vine;
    public static Block rock;
    public static Block tier2TreasureChest;
    public static Block machine;
    public static Block machineT2;
    public static Block creeperEgg;
    public static Block marsCobblestoneStairs;
    public static Block marsBricksStairs;
    public static Block hydrogenPipe;

    public static void initBlocks()
    {
    	MarsBlocks.marsBlock = new BlockBasicMars("mars").setHardness(2.2F);
        MarsBlocks.blockSludge = new BlockSludge("sludge");
        MarsBlocks.vine = new BlockCavernousVine("cavernVines").setHardness(0.1F);
        MarsBlocks.rock = new BlockSlimelingEgg("slimelingEgg").setHardness(0.75F);
        MarsBlocks.tier2TreasureChest = AsteroidBlocks.treasureChestTier2;
        MarsBlocks.machine = new BlockMachineMars("marsMachine").setHardness(1.8F);
        MarsBlocks.machineT2 = new BlockMachineMarsT2("marsMachineT2").setHardness(1.8F);
        MarsBlocks.creeperEgg = new BlockCreeperEgg("creeperEgg").setHardness(-1.0F);
//        MarsBlocks.marsCobblestoneStairs = new BlockStairsGC("marsCobblestoneStairs", marsBlock, BlockStairsGC.StairsCategoryGC.MARS_COBBLESTONE).setHardness(1.5F);
//        MarsBlocks.marsBricksStairs = new BlockStairsGC("marsDungeonBricksStairs", marsBlock, BlockStairsGC.StairsCategoryGC.MARS_BRICKS).setHardness(4.0F);
        MarsBlocks.hydrogenPipe = new BlockHydrogenPipe("hydrogenPipe");
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
        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 2, 0); //Copper ore
        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 2, 1); //Tin ore
        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 3, 2); //Desh ore
        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 1, 3); //Iron ore
        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 0, 4); //Cobblestone
        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 3, 7); //Dungeon brick
        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 0, 8); //Decoration block
        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 1, 9); //Stone
        setHarvestLevel(MarsBlocks.marsBlock, "shovel", 0, 5); //Top dirt
        setHarvestLevel(MarsBlocks.marsBlock, "shovel", 0, 6); //Dirt
        setHarvestLevel(MarsBlocks.rock, "pickaxe", 3);
        setHarvestLevel(MarsBlocks.marsCobblestoneStairs, "pickaxe", 0);
        setHarvestLevel(MarsBlocks.marsBricksStairs, "pickaxe", 3);
    }

    public static void registerBlocks()
    {
        GameRegistry.registerBlock(MarsBlocks.marsBlock, ItemBlockMars.class, MarsBlocks.marsBlock.getUnlocalizedName());
        GameRegistry.registerBlock(MarsBlocks.blockSludge, ItemBlockDesc.class, MarsBlocks.blockSludge.getUnlocalizedName());
        GameRegistry.registerBlock(MarsBlocks.vine, ItemBlockDesc.class, MarsBlocks.vine.getUnlocalizedName());
        GameRegistry.registerBlock(MarsBlocks.rock, ItemBlockEgg.class, MarsBlocks.rock.getUnlocalizedName());
        GameRegistry.registerBlock(MarsBlocks.creeperEgg, ItemBlockDesc.class, MarsBlocks.creeperEgg.getUnlocalizedName());
        GameRegistry.registerBlock(MarsBlocks.machine, ItemBlockMachine.class, MarsBlocks.machine.getUnlocalizedName());
        GameRegistry.registerBlock(MarsBlocks.machineT2, ItemBlockMachine.class, MarsBlocks.machineT2.getUnlocalizedName());
        GameRegistry.registerBlock(MarsBlocks.marsCobblestoneStairs, ItemBlockGC.class, MarsBlocks.marsCobblestoneStairs.getUnlocalizedName());
        GameRegistry.registerBlock(MarsBlocks.marsBricksStairs, ItemBlockGC.class, MarsBlocks.marsBricksStairs.getUnlocalizedName());
        GameRegistry.registerBlock(MarsBlocks.hydrogenPipe, ItemBlockDesc.class, MarsBlocks.hydrogenPipe.getUnlocalizedName());
    }
    
    public static void oreDictRegistration()
    {
        OreDictionary.registerOre("oreCopper", new ItemStack(MarsBlocks.marsBlock, 1, 0));
        OreDictionary.registerOre("oreTin", new ItemStack(MarsBlocks.marsBlock, 1, 1));
        OreDictionary.registerOre("oreIron", new ItemStack(MarsBlocks.marsBlock, 1, 3));
    }
}
