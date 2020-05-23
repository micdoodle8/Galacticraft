package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_PLANETS)
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

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> evt)
    {
        IForgeRegistry<Block> r = evt.getRegistry();

        Block.Properties builder = Block.Properties.create(Material.ROCK);
        register(r, new BlockBasicMars(builder), MarsBlockNames.marsBlock);

        builder = Block.Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().lightValue(15).tickRandomly().hardnessAndResistance(0.2F).sound(SoundType.PLANT);
        register(r, new BlockCavernousVine(builder), MarsBlockNames.vine);

        builder = Block.Properties.create(Material.ROCK);
        register(r, new BlockSlimelingEgg(builder), MarsBlockNames.rock);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(100000.0F).sound(SoundType.STONE).lightValue(13);
        register(r, new BlockTier2TreasureChest(builder), MarsBlockNames.treasureChestTier2);

        builder = Block.Properties.create(GCBlocks.machine).sound(SoundType.METAL);
        register(r, new BlockMachineMars(builder), MarsBlockNames.machine);
        register(r, new BlockMachineMarsT2(builder), MarsBlockNames.machineT2);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 9.0F);
        register(r, new BlockCreeperEgg(builder), MarsBlockNames.creeperEgg);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(1000000.0F).noDrops();
        register(r, new BlockBossSpawnerMars(builder), MarsBlockNames.bossSpawner);

//        register(r, new BlockStairsGC(builder), MarsBlockNames.marsCobblestoneStairs);
//        register(r, new BlockStairsGC(builder), MarsBlockNames.marsBricksStairs); TODO

//        MarsBlocks.marsBlock = new BlockBasicMars("mars").setHardness(2.2F);
//        MarsBlocks.vine = new BlockCavernousVine("cavern_vines").setHardness(0.1F);
//        MarsBlocks.rock = new BlockSlimelingEgg("slimeling_egg").setHardness(0.75F);
//        MarsBlocks.treasureChestTier2 = new BlockTier2TreasureChest("treasure_t2");
//        MarsBlocks.machine = new BlockMachineMars("mars_machine").setHardness(1.8F);
//        MarsBlocks.machineT2 = new BlockMachineMarsT2("mars_machine_t2").setHardness(1.8F);
//        MarsBlocks.creeperEgg = new BlockCreeperEgg("creeper_egg").setHardness(-1.0F);
//        MarsBlocks.bossSpawner = new BlockBossSpawnerMars("boss_spawner_mars");
//        MarsBlocks.marsCobblestoneStairs = new BlockStairsGC("mars_stairs_cobblestone", marsBlock.getDefaultState().with(BlockBasicMars.BASIC_TYPE, BlockBasicMars.EnumBlockBasic.COBBLESTONE)).setHardness(1.5F);
//        MarsBlocks.marsBricksStairs = new BlockStairsGC("mars_stairs_brick", marsBlock.getDefaultState().with(BlockBasicMars.BASIC_TYPE, BlockBasicMars.EnumBlockBasic.DUNGEON_BRICK)).setHardness(4.0F);

        GCBlocks.hiddenBlocks.add(MarsBlocks.bossSpawner);

//        MarsBlocks.registerBlocks();
//        MarsBlocks.setHarvestLevels();
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name)
    {
        reg.register(thing.setRegistryName(name));
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name)
    {
        register(reg, thing, new ResourceLocation(Constants.MOD_ID_PLANETS, name));
    }

//    public static void setHarvestLevels()
//    {
//        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 1, 0); //Copper ore
//        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 1, 1); //Tin ore
//        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 3, 2); //Desh ore
//        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 1, 3); //Iron ore
//        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 0, 4); //Cobblestone
//        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 3, 7); //Dungeon brick
//        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 0, 8); //Decoration block
//        setHarvestLevel(MarsBlocks.marsBlock, "pickaxe", 1, 9); //Stone
//        setHarvestLevel(MarsBlocks.marsBlock, "shovel", 0, 5); //Top dirt
//        setHarvestLevel(MarsBlocks.marsBlock, "shovel", 0, 6); //Dirt
//        setHarvestLevel(MarsBlocks.rock, "pickaxe", 3);
////        setHarvestLevel(MarsBlocks.marsCobblestoneStairs, "pickaxe", 0);
////        setHarvestLevel(MarsBlocks.marsBricksStairs, "pickaxe", 3);
//    }

//    public static void registerBlocks()
//    {
//        registerBlock(MarsBlocks.treasureChestTier2, ItemBlockDesc.class);
//        registerBlock(MarsBlocks.marsBlock, ItemBlockMars.class);
//        registerBlock(MarsBlocks.vine, ItemBlockDesc.class);
//        registerBlock(MarsBlocks.rock, ItemBlockEgg.class);
//        registerBlock(MarsBlocks.creeperEgg, ItemBlockDesc.class);
//        registerBlock(MarsBlocks.machine, ItemBlockMachine.class);
//        registerBlock(MarsBlocks.machineT2, ItemBlockMachine.class);
//        registerBlock(MarsBlocks.bossSpawner, ItemBlockGC.class);
//        registerBlock(MarsBlocks.marsCobblestoneStairs, ItemBlockGC.class);
//        registerBlock(MarsBlocks.marsBricksStairs, ItemBlockGC.class);
//    }

//    public static void oreDictRegistration()
//    {
//        OreDictionary.registerOre("oreCopper", new ItemStack(MarsBlocks.marsBlock, 1, 0));
//        OreDictionary.registerOre("oreTin", new ItemStack(MarsBlocks.marsBlock, 1, 1));
//        OreDictionary.registerOre("oreIron", new ItemStack(MarsBlocks.marsBlock, 1, 3));
//        OreDictionary.registerOre("oreDesh", new ItemStack(MarsBlocks.marsBlock, 1, 2));
//        OreDictionary.registerOre("blockDesh", new ItemStack(MarsBlocks.marsBlock, 1, 8));
//    } TODO
}
