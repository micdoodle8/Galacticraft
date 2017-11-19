package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockStairsGC;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemBlockEgg;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemBlockMachine;
import micdoodle8.mods.galacticraft.planets.mars.items.ItemBlockMars;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

public class MarsBlocks
{
    public static Block marsBlock;
    public static Block blockSludge;
    public static Block vine;
    public static Block rock;
    public static Block treasureChestTier2;
    public static Block machine;
    public static Block machineT2;
    public static Block creeperEgg;
    public static Block marsCobblestoneStairs;
    public static Block marsBricksStairs;
    public static Block bossSpawner;

    public static void initBlocks()
    {
        MarsBlocks.marsBlock = new BlockBasicMars("mars").setHardness(2.2F);
        MarsBlocks.vine = new BlockCavernousVine("cavern_vines").setHardness(0.1F);
        MarsBlocks.rock = new BlockSlimelingEgg("slimeling_egg").setHardness(0.75F);
        MarsBlocks.treasureChestTier2 = new BlockTier2TreasureChest("treasure_t2");
        MarsBlocks.machine = new BlockMachineMars("mars_machine").setHardness(1.8F);
        MarsBlocks.machineT2 = new BlockMachineMarsT2("mars_machine_t2").setHardness(1.8F);
        MarsBlocks.creeperEgg = new BlockCreeperEgg("creeper_egg").setHardness(-1.0F);
        MarsBlocks.bossSpawner = new BlockBossSpawnerMars("boss_spawner_mars");
        MarsBlocks.marsCobblestoneStairs = new BlockStairsGC("mars_stairs_cobblestone", marsBlock.getDefaultState().withProperty(BlockBasicMars.BASIC_TYPE, BlockBasicMars.EnumBlockBasic.COBBLESTONE)).setHardness(1.5F);
        MarsBlocks.marsBricksStairs = new BlockStairsGC("mars_stairs_brick", marsBlock.getDefaultState().withProperty(BlockBasicMars.BASIC_TYPE, BlockBasicMars.EnumBlockBasic.DUNGEON_BRICK)).setHardness(4.0F);

        GCBlocks.hiddenBlocks.add(MarsBlocks.bossSpawner);

        MarsBlocks.registerBlocks();
        MarsBlocks.setHarvestLevels();
        MarsBlocks.oreDictRegistration();
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
        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 1, 0); //Copper ore
        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 1, 1); //Tin ore
        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 3, 2); //Desh ore
        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 1, 3); //Iron ore
        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 0, 4); //Cobblestone
        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 3, 7); //Dungeon brick
        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 0, 8); //Decoration block
        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 1, 9); //Stone
        setHarvestLevel(MarsBlocks.marsBlock, "shovel", 0, 5); //Top dirt
        setHarvestLevel(MarsBlocks.marsBlock, "shovel", 0, 6); //Dirt
        setHarvestLevel(MarsBlocks.rock, "pickaxe", 3);
//        setHarvestLevel(MarsBlocks.marsCobblestoneStairs, "pickaxe", 0);
//        setHarvestLevel(MarsBlocks.marsBricksStairs, "pickaxe", 3);
    }

    public static void registerBlock(Block block, Class<? extends ItemBlock> itemClass)
    {
        String name = block.getUnlocalizedName().substring(5);
        GCCoreUtil.registerGalacticraftBlock(name, block);
        GameRegistry.registerBlock(block, itemClass, name);
        if (GCCoreUtil.getEffectiveSide() == Side.CLIENT)
        {
            GCBlocks.registerSorted(block);
        }
    }

    public static void registerBlocks()
    {
        registerBlock(MarsBlocks.treasureChestTier2, ItemBlockDesc.class);
        registerBlock(MarsBlocks.marsBlock, ItemBlockMars.class);
        registerBlock(MarsBlocks.vine, ItemBlockDesc.class);
        registerBlock(MarsBlocks.rock, ItemBlockEgg.class);
        registerBlock(MarsBlocks.creeperEgg, ItemBlockDesc.class);
        registerBlock(MarsBlocks.machine, ItemBlockMachine.class);
        registerBlock(MarsBlocks.machineT2, ItemBlockMachine.class);
        registerBlock(MarsBlocks.bossSpawner, ItemBlockGC.class);
        registerBlock(MarsBlocks.marsCobblestoneStairs, ItemBlockGC.class);
        registerBlock(MarsBlocks.marsBricksStairs, ItemBlockGC.class);
    }

    public static void oreDictRegistration()
    {
        OreDictionary.registerOre("oreCopper", new ItemStack(MarsBlocks.marsBlock, 1, 0));
        OreDictionary.registerOre("oreTin", new ItemStack(MarsBlocks.marsBlock, 1, 1));
        OreDictionary.registerOre("oreIron", new ItemStack(MarsBlocks.marsBlock, 1, 3));
        OreDictionary.registerOre("oreDesh", new ItemStack(MarsBlocks.marsBlock, 1, 2));
        OreDictionary.registerOre("blockDesh", new ItemStack(MarsBlocks.marsBlock, 1, 8));
    }
}
