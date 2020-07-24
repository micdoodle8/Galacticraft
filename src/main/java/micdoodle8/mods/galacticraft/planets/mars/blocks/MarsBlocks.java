package micdoodle8.mods.galacticraft.planets.mars.blocks;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.planets.mars.tile.*;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityType;
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
    //    @ObjectHolder(MarsBlockNames.blockSludge) public static Block blockSludge; TODO liquids
    @ObjectHolder(MarsBlockNames.vine)
    public static Block vine;
    @ObjectHolder(MarsBlockNames.slimelingEgg)
    public static Block slimelingEgg;
    @ObjectHolder(MarsBlockNames.treasureChestTier2)
    public static Block treasureChestTier2;
    @ObjectHolder(MarsBlockNames.cryoChamber)
    public static Block cryoChamber;
    @ObjectHolder(MarsBlockNames.launchController)
    public static Block launchController;
    @ObjectHolder(MarsBlockNames.terraformer)
    public static Block terraformer;
    @ObjectHolder(MarsBlockNames.methaneSynthesizer)
    public static Block methaneSynthesizer;
    @ObjectHolder(MarsBlockNames.gasLiquefier)
    public static Block gasLiquefier;
    @ObjectHolder(MarsBlockNames.electrolyzer)
    public static Block electrolyzer;
    @ObjectHolder(MarsBlockNames.creeperEgg)
    public static Block creeperEgg;
    @ObjectHolder(MarsBlockNames.marsCobblestoneStairs)
    public static Block marsCobblestoneStairs;
    @ObjectHolder(MarsBlockNames.marsBricksStairs)
    public static Block marsBricksStairs;
    @ObjectHolder(MarsBlockNames.bossSpawner)
    public static Block bossSpawner;
    @ObjectHolder(MarsBlockNames.oreCopper)
    public static Block oreCopper;
    @ObjectHolder(MarsBlockNames.oreTin)
    public static Block oreTin;
    @ObjectHolder(MarsBlockNames.oreDesh)
    public static Block oreDesh;
    @ObjectHolder(MarsBlockNames.oreIron)
    public static Block oreIron;
    @ObjectHolder(MarsBlockNames.cobblestone)
    public static Block cobblestone;
    @ObjectHolder(MarsBlockNames.rockSurface)
    public static Block rockSurface;
    @ObjectHolder(MarsBlockNames.rockMiddle)
    public static Block rockMiddle;
    @ObjectHolder(MarsBlockNames.dungeonBrick)
    public static Block dungeonBrick;
    @ObjectHolder(MarsBlockNames.deshBlock)
    public static Block deshBlock;
    @ObjectHolder(MarsBlockNames.stone)
    public static Block stone;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> evt)
    {
        IForgeRegistry<Block> r = evt.getRegistry();

        Block.Properties builder = Block.Properties.create(Material.ROCK);
        register(r, new BlockBasicMars(builder), MarsBlockNames.oreCopper);
        register(r, new BlockBasicMars(builder), MarsBlockNames.oreTin);
        register(r, new BlockBasicMars(builder), MarsBlockNames.oreDesh);
        register(r, new BlockBasicMars(builder), MarsBlockNames.oreIron);
        register(r, new BlockBasicMars(builder), MarsBlockNames.cobblestone);
        register(r, new BlockBasicMars(builder), MarsBlockNames.rockSurface);
        register(r, new BlockBasicMars(builder), MarsBlockNames.rockMiddle);
        register(r, new BlockBasicMars(builder), MarsBlockNames.stone);

        builder = builder.hardnessAndResistance(5.0F, 20.0F);
        register(r, new BlockBasicMars(builder), MarsBlockNames.deshBlock);

        builder = builder.hardnessAndResistance(5.0F, 60.0F).tickRandomly();
        register(r, new BlockBasicMars(builder), MarsBlockNames.dungeonBrick);

        builder = Block.Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().lightValue(15).tickRandomly().hardnessAndResistance(0.2F).sound(SoundType.PLANT).noDrops();
        register(r, new BlockCavernousVine(builder), MarsBlockNames.vine);

        builder = Block.Properties.create(Material.ROCK);
        register(r, new BlockSlimelingEgg(builder), MarsBlockNames.slimelingEgg);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(100000.0F).sound(SoundType.STONE).lightValue(13);
        register(r, new BlockTier2TreasureChest(builder), MarsBlockNames.treasureChestTier2);

        builder = Block.Properties.create(GCBlocks.machine).sound(SoundType.METAL);
        register(r, new BlockCryoChamber(builder), MarsBlockNames.cryoChamber);
        register(r, new BlockLaunchController(builder), MarsBlockNames.launchController);
        register(r, new BlockTerraformer(builder), MarsBlockNames.terraformer);
        register(r, new BlockMethaneSynthesizer(builder), MarsBlockNames.methaneSynthesizer);
        register(r, new BlockGasLiquefier(builder), MarsBlockNames.gasLiquefier);
        register(r, new BlockElectrolyzer(builder.tickRandomly()), MarsBlockNames.electrolyzer);

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

    @SubscribeEvent
    public static void initTileEntities(RegistryEvent.Register<TileEntityType<?>> evt)
    {
        IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();

        register(r, TileEntityType.Builder.create(TileEntityCryogenicChamber::new, cryoChamber).build(null), MarsBlockNames.cryoChamber);
        register(r, TileEntityType.Builder.create(TileEntityDungeonSpawnerMars::new, bossSpawner).build(null), MarsBlockNames.bossSpawner);
        register(r, TileEntityType.Builder.create(TileEntityElectrolyzer::new, electrolyzer).build(null), MarsBlockNames.electrolyzer);
        register(r, TileEntityType.Builder.create(TileEntityGasLiquefier::new, gasLiquefier).build(null), MarsBlockNames.gasLiquefier);
        register(r, TileEntityType.Builder.create(TileEntityLaunchController::new, launchController).build(null), MarsBlockNames.launchController);
        register(r, TileEntityType.Builder.create(TileEntityMethaneSynthesizer::new, methaneSynthesizer).build(null), MarsBlockNames.methaneSynthesizer);
        register(r, TileEntityType.Builder.create(TileEntitySlimelingEgg::new, slimelingEgg).build(null), MarsBlockNames.slimelingEgg);
        register(r, TileEntityType.Builder.create(TileEntityTerraformer::new, terraformer).build(null), MarsBlockNames.terraformer);
        register(r, TileEntityType.Builder.create(TileEntityTreasureChestMars::new, treasureChestTier2).build(null), MarsBlockNames.treasureChestTier2);
    }
}
