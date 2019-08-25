package micdoodle8.mods.galacticraft.planets.venus;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.items.ItemBlockDesc;
import micdoodle8.mods.galacticraft.core.items.ItemBlockGC;
import micdoodle8.mods.galacticraft.planets.asteroids.items.ItemBlockLaser;
import micdoodle8.mods.galacticraft.planets.venus.blocks.*;
import micdoodle8.mods.galacticraft.planets.venus.items.ItemBlockBasicVenus;
import micdoodle8.mods.galacticraft.planets.venus.items.ItemBlockTorchWeb;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.oredict.OreDictionary;

public class VenusBlocks
{
    public static Block venusBlock;
    public static Block spout;
    public static Block bossSpawner;
    public static Block treasureChestTier3;
    public static Block torchWeb;
    public static Block sulphuricAcid;
    public static Block geothermalGenerator;
    public static Block crashedProbe;
    public static Block scorchedRock;
    public static Block solarArrayModule;
    public static Block solarArrayController;
    public static Block laserTurret;

    public static void initBlocks()
    {
        VenusBlocks.venusBlock = new BlockBasicVenus("venus");
        VenusBlocks.spout = new BlockSpout("spout");
        VenusBlocks.bossSpawner = new BlockBossSpawnerVenus("boss_spawner_venus");
        VenusBlocks.treasureChestTier3 = new BlockTier3TreasureChest("treasure_t3");
        VenusBlocks.torchWeb = new BlockTorchWeb("web_torch");
        VenusBlocks.geothermalGenerator = new BlockGeothermalGenerator("geothermal_generator");
        VenusBlocks.crashedProbe = new BlockCrashedProbe("crashed_probe");
        VenusBlocks.scorchedRock = new BlockScorchedRock("venus_rock_scorched");
        VenusBlocks.solarArrayModule = new BlockSolarArrayModule("solar_array_module");
        VenusBlocks.solarArrayController = new BlockSolarArrayController("solar_array_controller");
        VenusBlocks.laserTurret = new BlockLaserTurret("laser_turret");

        GCBlocks.hiddenBlocks.add(VenusBlocks.bossSpawner);

        VenusBlocks.registerBlocks();
        VenusBlocks.setHarvestLevels();
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
        setHarvestLevel(VenusBlocks.venusBlock, "pickaxe", 1, 0);
        setHarvestLevel(VenusBlocks.venusBlock, "pickaxe", 1, 1);
        setHarvestLevel(VenusBlocks.venusBlock, "pickaxe", 1, 2);
        setHarvestLevel(VenusBlocks.venusBlock, "pickaxe", 1, 3);
        setHarvestLevel(VenusBlocks.spout, "pickaxe", 1);
    }

    public static void registerBlock(Block block, Class<? extends ItemBlock> itemClass)
    {
        GCBlocks.registerBlock(block, itemClass);
    }

    public static void registerBlocks()
    {
        registerBlock(VenusBlocks.venusBlock, ItemBlockBasicVenus.class);
        registerBlock(VenusBlocks.spout, ItemBlockGC.class);
        registerBlock(VenusBlocks.bossSpawner, ItemBlockGC.class);
        registerBlock(VenusBlocks.treasureChestTier3, ItemBlockDesc.class);
        registerBlock(VenusBlocks.torchWeb, ItemBlockTorchWeb.class);
        registerBlock(VenusBlocks.geothermalGenerator, ItemBlockDesc.class);
        registerBlock(VenusBlocks.crashedProbe, ItemBlockDesc.class);
        registerBlock(VenusBlocks.scorchedRock, ItemBlockGC.class);
        registerBlock(VenusBlocks.solarArrayModule, ItemBlockDesc.class);
        registerBlock(VenusBlocks.solarArrayController, ItemBlockDesc.class);
        registerBlock(VenusBlocks.laserTurret, ItemBlockLaser.class);
    }

    public static void oreDictRegistration()
    {
        OreDictionary.registerOre("oreCopper", BlockBasicVenus.EnumBlockBasicVenus.ORE_COPPER.getItemStack());
        OreDictionary.registerOre("oreTin", BlockBasicVenus.EnumBlockBasicVenus.ORE_TIN.getItemStack());
        OreDictionary.registerOre("oreAluminum", BlockBasicVenus.EnumBlockBasicVenus.ORE_ALUMINUM.getItemStack());
        OreDictionary.registerOre("oreAluminium", BlockBasicVenus.EnumBlockBasicVenus.ORE_ALUMINUM.getItemStack());
        OreDictionary.registerOre("oreNaturalAluminum", BlockBasicVenus.EnumBlockBasicVenus.ORE_ALUMINUM.getItemStack());
        OreDictionary.registerOre("oreQuartz", BlockBasicVenus.EnumBlockBasicVenus.ORE_QUARTZ.getItemStack());
        OreDictionary.registerOre("oreLead", BlockBasicVenus.EnumBlockBasicVenus.ORE_GALENA.getItemStack());
        OreDictionary.registerOre("oreSilicon", BlockBasicVenus.EnumBlockBasicVenus.ORE_SILICON.getItemStack());
        OreDictionary.registerOre("oreSolar", BlockBasicVenus.EnumBlockBasicVenus.ORE_SOLAR_DUST.getItemStack());

        OreDictionary.registerOre("blockLead", BlockBasicVenus.EnumBlockBasicVenus.LEAD_BLOCK.getItemStack());
    }
}
