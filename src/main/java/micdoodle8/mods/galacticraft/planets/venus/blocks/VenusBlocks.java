package micdoodle8.mods.galacticraft.planets.venus.blocks;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.GCItems;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.items.ItemDesc;
import micdoodle8.mods.galacticraft.core.tile.TileEntityDungeonSpawner;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlockNames;
import micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks;
import micdoodle8.mods.galacticraft.planets.asteroids.tile.*;
import micdoodle8.mods.galacticraft.planets.venus.tile.*;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

import static micdoodle8.mods.galacticraft.planets.asteroids.blocks.AsteroidBlocks.register;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_PLANETS, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_PLANETS)
public class VenusBlocks
{
    public static final Block rockSoft = new BlockVenusRock(Block.Properties.create(Material.ROCK).hardnessAndResistance(2.2F));
    public static final Block rockMagma = new BlockVenusRock(Block.Properties.from(rockSoft));
    public static final Block rockVolcanicDeposit = new BlockVenusRock(Block.Properties.from(rockSoft));
    public static final Block leadBlock = new BlockVenusRock(Block.Properties.from(rockSoft));

    public static final Block rockHard = new BlockVenusRock(Block.Properties.from(rockSoft).hardnessAndResistance(3.0F));

    public static final Block oreAluminum = new BlockOreVenus(Block.Properties.from(rockSoft).hardnessAndResistance(5.0F));

    public static final Block oreCopper = new BlockOreVenus(Block.Properties.from(rockSoft));
    public static final Block oreGalena = new BlockOreVenus(Block.Properties.from(rockSoft));
    public static final Block oreQuartz = new BlockOreVenus(Block.Properties.from(rockSoft));
    public static final Block oreSilicon = new BlockOreVenus(Block.Properties.from(rockSoft));
    public static final Block oreTin = new BlockOreVenus(Block.Properties.from(rockSoft));
    public static final Block oreSolarDust = new BlockOreVenus(Block.Properties.from(rockSoft));

    public static final Block dungeonBrick1 = new BlockDungeonBrick(Block.Properties.from(rockSoft).hardnessAndResistance(40.0F));
    public static final Block dungeonBrick2 = new BlockDungeonBrick(Block.Properties.from(dungeonBrick1));

    public static final Block spout = new BlockSpout(Block.Properties.create(Material.ROCK).hardnessAndResistance(4.5F));

    public static final Block bossSpawner = new BlockBossSpawnerVenus(Block.Properties.create(Material.ROCK).hardnessAndResistance(1000000.0F).noDrops());

    public static final Block treasureChestTier3 = new BlockTier3TreasureChest(Block.Properties.create(Material.ROCK).hardnessAndResistance(100000.0F).sound(SoundType.STONE).lightValue(13));

    public static final Block torchWebSupport = new BlockTorchWeb(Block.Properties.create(Material.WEB).doesNotBlockMovement());
    public static final Block torchWebLight = new BlockTorchWeb(Block.Properties.from(torchWebSupport).lightValue(15));

    public static final Block geothermalGenerator = new BlockGeothermalGenerator(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.0F).sound(SoundType.METAL));
    public static final Block solarArrayModule = new BlockSolarArrayModule(Block.Properties.from(geothermalGenerator));
    public static final Block solarArrayController = new BlockSolarArrayController(Block.Properties.from(geothermalGenerator));
    public static final Block laserTurret = new BlockLaserTurret(Block.Properties.from(geothermalGenerator));

    public static final Block crashedProbe = new BlockCrashedProbe(Block.Properties.create(Material.IRON).tickRandomly().hardnessAndResistance(4.5F).sound(SoundType.METAL));

    public static final Block scorchedRock = new BlockScorchedRock(Block.Properties.create(Material.ROCK).tickRandomly().hardnessAndResistance(0.9F, 2.5F));

    //    @ObjectHolder(VenusBlockNames.venusBlock) public static Block venusBlock;
//    @ObjectHolder(VenusBlockNames.spout)
//    public static Block spout;
//    @ObjectHolder(VenusBlockNames.bossSpawner)
//    public static Block bossSpawner;
//    @ObjectHolder(VenusBlockNames.treasureChestTier3)
//    public static Block treasureChestTier3;
//    @ObjectHolder(VenusBlockNames.torchWeb)
//    public static Block torchWeb;
//    //    public static Block sulphuricAcid; TODO
//    @ObjectHolder(VenusBlockNames.geothermalGenerator)
//    public static Block geothermalGenerator;
//    @ObjectHolder(VenusBlockNames.crashedProbe)
//    public static Block crashedProbe;
//    @ObjectHolder(VenusBlockNames.scorchedRock)
//    public static Block scorchedRock;
//    @ObjectHolder(VenusBlockNames.solarArrayModule)
//    public static Block solarArrayModule;
//    @ObjectHolder(VenusBlockNames.solarArrayController)
//    public static Block solarArrayController;
//    @ObjectHolder(VenusBlockNames.laserTurret)
//    public static Block laserTurret;
//    @ObjectHolder(VenusBlockNames.rockSoft)
//    public static Block rockSoft;
//    @ObjectHolder(VenusBlockNames.rockHard)
//    public static Block rockHard;
//    @ObjectHolder(VenusBlockNames.rockMagma)
//    public static Block rockMagma;
//    @ObjectHolder(VenusBlockNames.rockVolcanicDeposit)
//    public static Block rockVolcanicDeposit;
//    @ObjectHolder(VenusBlockNames.dungeonBrick1)
//    public static Block dungeonBrick1;
//    @ObjectHolder(VenusBlockNames.dungeonBrick2)
//    public static Block dungeonBrick2;
//    @ObjectHolder(VenusBlockNames.oreAluminum)
//    public static Block oreAluminum;
//    @ObjectHolder(VenusBlockNames.oreCopper)
//    public static Block oreCopper;
//    @ObjectHolder(VenusBlockNames.oreGalena)
//    public static Block oreGalena;
//    @ObjectHolder(VenusBlockNames.oreQuartz)
//    public static Block oreQuartz;
//    @ObjectHolder(VenusBlockNames.oreSilicon)
//    public static Block oreSilicon;
//    @ObjectHolder(VenusBlockNames.oreTin)
//    public static Block oreTin;
//    @ObjectHolder(VenusBlockNames.leadBlock)
//    public static Block leadBlock;
//    @ObjectHolder(VenusBlockNames.oreSolarDust)
//    public static Block oreSolarDust;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> evt)
    {
        IForgeRegistry<Block> r = evt.getRegistry();

        register(r, rockSoft, VenusBlockNames.rockSoft);
        register(r, rockMagma, VenusBlockNames.rockMagma);
        register(r, rockVolcanicDeposit, VenusBlockNames.rockVolcanicDeposit);
        register(r, leadBlock, VenusBlockNames.leadBlock);
        register(r, rockHard, VenusBlockNames.rockHard);
        register(r, oreAluminum, VenusBlockNames.oreAluminum);
        register(r, oreCopper, VenusBlockNames.oreCopper);
        register(r, oreGalena, VenusBlockNames.oreGalena);
        register(r, oreQuartz, VenusBlockNames.oreQuartz);
        register(r, oreSilicon, VenusBlockNames.oreSilicon);
        register(r, oreTin, VenusBlockNames.oreTin);
        register(r, oreSolarDust, VenusBlockNames.oreSolarDust);
        register(r, dungeonBrick1, VenusBlockNames.dungeonBrick1);
        register(r, dungeonBrick2, VenusBlockNames.dungeonBrick2);
        register(r, spout, VenusBlockNames.spout);
        register(r, bossSpawner, VenusBlockNames.bossSpawner);
        register(r, treasureChestTier3, VenusBlockNames.treasureChestTier3);
        register(r, torchWebSupport, VenusBlockNames.torchWebSupport);
        register(r, torchWebLight, VenusBlockNames.torchWebLight);
        register(r, geothermalGenerator, VenusBlockNames.geothermalGenerator);
        register(r, solarArrayModule, VenusBlockNames.solarArrayModule);
        register(r, solarArrayController, VenusBlockNames.solarArrayController);
        register(r, laserTurret, VenusBlockNames.laserTurret);
        register(r, crashedProbe, VenusBlockNames.crashedProbe);
        register(r, scorchedRock, VenusBlockNames.scorchedRock);

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

//    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name)
//    {
//        reg.register(thing.setRegistryName(name));
//    }
//
//    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name)
//    {
//        register(reg, thing, new ResourceLocation(Constants.MOD_ID_PLANETS, name));
//    }
//
//    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, ResourceLocation name, IForgeRegistryEntry<V> thing) {
//        reg.register(thing.setRegistryName(name));
//    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> evt)
    {
        IForgeRegistry<Item> r = evt.getRegistry();
        Item.Properties props = GCItems.defaultBuilder().group(GalacticraftCore.galacticraftBlocksTab);
        register(r, Registry.BLOCK.getKey(rockSoft), new BlockItem(rockSoft, props));
        register(r, Registry.BLOCK.getKey(rockMagma), new BlockItem(rockMagma, props));
        register(r, Registry.BLOCK.getKey(rockVolcanicDeposit), new BlockItem(rockVolcanicDeposit, props));
        register(r, Registry.BLOCK.getKey(leadBlock), new BlockItem(leadBlock, props));
        register(r, Registry.BLOCK.getKey(rockHard), new BlockItem(rockHard, props));
        register(r, Registry.BLOCK.getKey(oreAluminum), new BlockItem(oreAluminum, props));
        register(r, Registry.BLOCK.getKey(oreCopper), new BlockItem(oreCopper, props));
        register(r, Registry.BLOCK.getKey(oreGalena), new BlockItem(oreGalena, props));
        register(r, Registry.BLOCK.getKey(oreQuartz), new BlockItem(oreQuartz, props));
        register(r, Registry.BLOCK.getKey(oreSilicon), new BlockItem(oreSilicon, props));
        register(r, Registry.BLOCK.getKey(oreTin), new BlockItem(oreTin, props));
        register(r, Registry.BLOCK.getKey(oreSolarDust), new BlockItem(oreSolarDust, props));
        register(r, Registry.BLOCK.getKey(dungeonBrick1), new BlockItem(dungeonBrick1, props));
        register(r, Registry.BLOCK.getKey(dungeonBrick2), new BlockItem(dungeonBrick2, props));
        register(r, Registry.BLOCK.getKey(spout), new BlockItem(spout, props));
        register(r, Registry.BLOCK.getKey(treasureChestTier3), new ItemBlockDesc(treasureChestTier3, props));
        register(r, Registry.BLOCK.getKey(torchWebSupport), new ItemBlockDesc(torchWebSupport, props));
        register(r, Registry.BLOCK.getKey(torchWebLight), new ItemBlockDesc(torchWebLight, props));
        register(r, Registry.BLOCK.getKey(geothermalGenerator), new ItemBlockDesc(geothermalGenerator, props));
        register(r, Registry.BLOCK.getKey(solarArrayModule), new ItemBlockDesc(solarArrayModule, props));
        register(r, Registry.BLOCK.getKey(solarArrayController), new ItemBlockDesc(solarArrayController, props));
        register(r, Registry.BLOCK.getKey(laserTurret), new ItemBlockDesc(laserTurret, props));
        register(r, Registry.BLOCK.getKey(crashedProbe), new BlockItem(crashedProbe, props));
        register(r, Registry.BLOCK.getKey(scorchedRock), new BlockItem(scorchedRock, props));
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

    @SubscribeEvent
    public static void initTileEntities(RegistryEvent.Register<TileEntityType<?>> evt)
    {
        IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();

        register(r, TileEntityType.Builder.create(TileEntityCrashedProbe::new, crashedProbe).build(null), VenusBlockNames.crashedProbe);
        register(r, TileEntityType.Builder.create(TileEntityDungeonSpawner::new, bossSpawner).build(null), VenusBlockNames.bossSpawner);
        register(r, TileEntityType.Builder.create(TileEntityGeothermalGenerator::new, geothermalGenerator).build(null), VenusBlockNames.geothermalGenerator);
        register(r, TileEntityType.Builder.create(TileEntityLaserTurret::new, laserTurret).build(null), VenusBlockNames.laserTurret);
        register(r, TileEntityType.Builder.create(TileEntitySolarArrayController::new, solarArrayController).build(null), VenusBlockNames.solarArrayController);
        register(r, TileEntityType.Builder.create(TileEntitySolarArrayModule::new, solarArrayModule).build(null), VenusBlockNames.solarArrayModule);
        register(r, TileEntityType.Builder.create(TileEntitySpout::new, spout).build(null), VenusBlockNames.spout);
        register(r, TileEntityType.Builder.create(TileEntityTreasureChestVenus::new, treasureChestTier3).build(null), VenusBlockNames.treasureChestTier3);
    }
}
