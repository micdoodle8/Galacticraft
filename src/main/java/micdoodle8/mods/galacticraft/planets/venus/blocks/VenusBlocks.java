package micdoodle8.mods.galacticraft.planets.venus.blocks;

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
public class VenusBlocks
{
    @ObjectHolder(VenusBlockNames.venusBlock) public static Block venusBlock;
    @ObjectHolder(VenusBlockNames.spout) public static Block spout;
    @ObjectHolder(VenusBlockNames.bossSpawner) public static Block bossSpawner;
    @ObjectHolder(VenusBlockNames.treasureChestTier3) public static Block treasureChestTier3;
    @ObjectHolder(VenusBlockNames.torchWeb) public static Block torchWeb;
//    public static Block sulphuricAcid; TODO
    @ObjectHolder(VenusBlockNames.geothermalGenerator) public static Block geothermalGenerator;
    @ObjectHolder(VenusBlockNames.crashedProbe) public static Block crashedProbe;
    @ObjectHolder(VenusBlockNames.scorchedRock) public static Block scorchedRock;
    @ObjectHolder(VenusBlockNames.solarArrayModule) public static Block solarArrayModule;
    @ObjectHolder(VenusBlockNames.solarArrayController) public static Block solarArrayController;
    @ObjectHolder(VenusBlockNames.laserTurret) public static Block laserTurret;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> evt)
    {
        IForgeRegistry<Block> r = evt.getRegistry();

        Block.Properties builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(2.2F);
        register(r, new BlockBasicVenus(builder), VenusBlockNames.venusBlock);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(4.5F);
        register(r, new BlockSpout(builder), VenusBlockNames.spout);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(1000000.0F).noDrops();
        register(r, new BlockBossSpawnerVenus(builder), VenusBlockNames.bossSpawner);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(100000.0F).sound(SoundType.STONE).lightValue(13);
        register(r, new BlockTier3TreasureChest(builder), VenusBlockNames.treasureChestTier3);

        builder = Block.Properties.create(Material.WEB).doesNotBlockMovement().lightValue(15);
        register(r, new BlockTorchWeb(builder), VenusBlockNames.torchWeb);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(1.0F).sound(SoundType.METAL);
        register(r, new BlockGeothermalGenerator(builder), VenusBlockNames.geothermalGenerator);
        register(r, new BlockSolarArrayModule(builder), VenusBlockNames.solarArrayModule);
        register(r, new BlockSolarArrayController(builder), VenusBlockNames.solarArrayController);
        register(r, new BlockLaserTurret(builder), VenusBlockNames.laserTurret);

        builder = Block.Properties.create(Material.IRON).tickRandomly().hardnessAndResistance(4.5F).sound(SoundType.METAL);
        register(r, new BlockCrashedProbe(builder), VenusBlockNames.crashedProbe);

        builder = Block.Properties.create(Material.ROCK).tickRandomly().hardnessAndResistance(0.9F, 2.5F);
        register(r, new BlockScorchedRock(builder), VenusBlockNames.scorchedRock);

//        VenusBlocks.venusBlock = new BlockBasicVenus("venus");
//        VenusBlocks.spout = new BlockSpout("spout");
//        VenusBlocks.bossSpawner = new BlockBossSpawnerVenus("boss_spawner_venus");
//        VenusBlocks.treasureChestTier3 = new BlockTier3TreasureChest("treasure_t3");
//        VenusBlocks.torchWeb = new BlockTorchWeb("web_torch");
//        VenusBlocks.geothermalGenerator = new BlockGeothermalGenerator("geothermal_generator");
//        VenusBlocks.crashedProbe = new BlockCrashedProbe("crashed_probe");
//        VenusBlocks.scorchedRock = new BlockScorchedRock("venus_rock_scorched");
//        VenusBlocks.solarArrayModule = new BlockSolarArrayModule("solar_array_module");
//        VenusBlocks.solarArrayController = new BlockSolarArrayController("solar_array_controller");
//        VenusBlocks.laserTurret = new BlockLaserTurret("laser_turret");

        GCBlocks.hiddenBlocks.add(VenusBlocks.bossSpawner);

//        VenusBlocks.registerBlocks();
//        VenusBlocks.setHarvestLevels();
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
//        setHarvestLevel(VenusBlocks.venusBlock, "pickaxe", 1, 0);
//        setHarvestLevel(VenusBlocks.venusBlock, "pickaxe", 1, 1);
//        setHarvestLevel(VenusBlocks.venusBlock, "pickaxe", 1, 2);
//        setHarvestLevel(VenusBlocks.venusBlock, "pickaxe", 1, 3);
//        setHarvestLevel(VenusBlocks.spout, "pickaxe", 1);
//    }

//    public static void registerBlock(Block block, Class<? extends BlockItem> itemClass)
//    {
//        GCBlocks.registerBlock(block, itemClass);
//    }

//    public static void registerBlocks()
//    {
//        registerBlock(VenusBlocks.venusBlock, ItemBlockBasicVenus.class);
//        registerBlock(VenusBlocks.spout, ItemBlockGC.class);
//        registerBlock(VenusBlocks.bossSpawner, ItemBlockGC.class);
//        registerBlock(VenusBlocks.treasureChestTier3, ItemBlockDesc.class);
//        registerBlock(VenusBlocks.torchWeb, ItemBlockTorchWeb.class);
//        registerBlock(VenusBlocks.geothermalGenerator, ItemBlockDesc.class);
//        registerBlock(VenusBlocks.crashedProbe, ItemBlockDesc.class);
//        registerBlock(VenusBlocks.scorchedRock, ItemBlockGC.class);
//        registerBlock(VenusBlocks.solarArrayModule, ItemBlockDesc.class);
//        registerBlock(VenusBlocks.solarArrayController, ItemBlockDesc.class);
//        registerBlock(VenusBlocks.laserTurret, ItemBlockLaser.class);
//    }
//
//    public static void oreDictRegistration()
//    {
//        OreDictionary.registerOre("oreCopper", BlockBasicVenus.EnumBlockBasicVenus.ORE_COPPER.getItemStack());
//        OreDictionary.registerOre("oreTin", BlockBasicVenus.EnumBlockBasicVenus.ORE_TIN.getItemStack());
//        OreDictionary.registerOre("oreAluminum", BlockBasicVenus.EnumBlockBasicVenus.ORE_ALUMINUM.getItemStack());
//        OreDictionary.registerOre("oreAluminium", BlockBasicVenus.EnumBlockBasicVenus.ORE_ALUMINUM.getItemStack());
//        OreDictionary.registerOre("oreNaturalAluminum", BlockBasicVenus.EnumBlockBasicVenus.ORE_ALUMINUM.getItemStack());
//        OreDictionary.registerOre("oreQuartz", BlockBasicVenus.EnumBlockBasicVenus.ORE_QUARTZ.getItemStack());
//        OreDictionary.registerOre("oreLead", BlockBasicVenus.EnumBlockBasicVenus.ORE_GALENA.getItemStack());
//        OreDictionary.registerOre("oreSilicon", BlockBasicVenus.EnumBlockBasicVenus.ORE_SILICON.getItemStack());
//        OreDictionary.registerOre("oreSolar", BlockBasicVenus.EnumBlockBasicVenus.ORE_SOLAR_DUST.getItemStack());
//
//        OreDictionary.registerOre("blockLead", BlockBasicVenus.EnumBlockBasicVenus.LEAD_BLOCK.getItemStack());
//    }
}
