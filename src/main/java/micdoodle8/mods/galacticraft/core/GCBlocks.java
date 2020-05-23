package micdoodle8.mods.galacticraft.core;

import com.google.common.collect.Maps;
import micdoodle8.mods.galacticraft.core.blocks.*;
import micdoodle8.mods.galacticraft.core.blocks.BlockSpaceGlass.GlassFrame;
import micdoodle8.mods.galacticraft.core.blocks.BlockSpaceGlass.GlassType;
import micdoodle8.mods.galacticraft.core.util.EnumSortCategoryBlock;
import micdoodle8.mods.galacticraft.core.util.StackSorted;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_CORE)
public class GCBlocks
{
    @ObjectHolder(BlockNames.breatheableAir) public static Block breatheableAir;
    @ObjectHolder(BlockNames.brightAir) public static Block brightAir;
    @ObjectHolder(BlockNames.brightBreatheableAir) public static Block brightBreatheableAir;
    @ObjectHolder(BlockNames.brightLamp) public static Block brightLamp;
    @ObjectHolder(BlockNames.treasureChestTier1) public static Block treasureChestTier1;
    @ObjectHolder(BlockNames.landingPad) public static Block landingPad;
    @ObjectHolder(BlockNames.unlitTorch) public static Block unlitTorch;
    @ObjectHolder(BlockNames.unlitTorchLit) public static Block unlitTorchLit;
    @ObjectHolder(BlockNames.oxygenDistributor) public static Block oxygenDistributor;
    @ObjectHolder(BlockNames.oxygenPipe) public static Block oxygenPipe;
    @ObjectHolder(BlockNames.oxygenPipePull) public static Block oxygenPipePull;
    @ObjectHolder(BlockNames.oxygenCollector) public static Block oxygenCollector;
    @ObjectHolder(BlockNames.oxygenCompressor) public static Block oxygenCompressor;
    @ObjectHolder(BlockNames.oxygenSealer) public static Block oxygenSealer;
    @ObjectHolder(BlockNames.oxygenDetector) public static Block oxygenDetector;
    @ObjectHolder(BlockNames.nasaWorkbench) public static Block nasaWorkbench;
    @ObjectHolder(BlockNames.fallenMeteor) public static Block fallenMeteor;
    @ObjectHolder(BlockNames.basicBlock) public static Block basicBlock;
    @ObjectHolder(BlockNames.airLockFrame) public static Block airLockFrame;
    @ObjectHolder(BlockNames.airLockSeal) public static Block airLockSeal;
    @ObjectHolder(BlockNames.spaceGlassClear) public static Block spaceGlassClear;
    @ObjectHolder(BlockNames.spaceGlassVanilla) public static Block spaceGlassVanilla;
    @ObjectHolder(BlockNames.spaceGlassStrong) public static Block spaceGlassStrong;
    @ObjectHolder(BlockNames.spaceGlassTinClear) public static Block spaceGlassTinClear;
    @ObjectHolder(BlockNames.spaceGlassTinVanilla) public static Block spaceGlassTinVanilla;
    @ObjectHolder(BlockNames.spaceGlassTinStrong) public static Block spaceGlassTinStrong;
    @ObjectHolder(BlockNames.crafting) public static Block crafting;
    @ObjectHolder(BlockNames.crudeOil) public static Block crudeOil;
    @ObjectHolder(BlockNames.fuel) public static Block fuel;
    @ObjectHolder(BlockNames.refinery) public static Block refinery;
    @ObjectHolder(BlockNames.fuelLoader) public static Block fuelLoader;
    @ObjectHolder(BlockNames.landingPadFull) public static Block landingPadFull;
    @ObjectHolder(BlockNames.spaceStationBase) public static Block spaceStationBase;
    @ObjectHolder(BlockNames.fakeBlock) public static Block fakeBlock;
    @ObjectHolder(BlockNames.sealableBlock) public static Block sealableBlock;
    @ObjectHolder(BlockNames.cargoLoader) public static Block cargoLoader;
    @ObjectHolder(BlockNames.parachest) public static Block parachest;
    @ObjectHolder(BlockNames.solarPanel) public static Block solarPanel;
    @ObjectHolder(BlockNames.radioTelescope) public static Block radioTelescope;
    @ObjectHolder(BlockNames.machineBase) public static Block machineBase;
    @ObjectHolder(BlockNames.machineBase2) public static Block machineBase2;
    @ObjectHolder(BlockNames.machineBase3) public static Block machineBase3;
    @ObjectHolder(BlockNames.machineBase4) public static Block machineBase4;
    @ObjectHolder(BlockNames.machineTiered) public static Block machineTiered;
    @ObjectHolder(BlockNames.aluminumWire) public static Block aluminumWire;
    @ObjectHolder(BlockNames.panelLighting) public static Block panelLighting;
    @ObjectHolder(BlockNames.glowstoneTorch) public static Block glowstoneTorch;
    @ObjectHolder(BlockNames.blockMoon) public static Block blockMoon;
    @ObjectHolder(BlockNames.cheeseBlock) public static Block cheeseBlock;
    @ObjectHolder(BlockNames.spinThruster) public static Block spinThruster;
    @ObjectHolder(BlockNames.screen) public static Block screen;
    @ObjectHolder(BlockNames.telemetry) public static Block telemetry;
    @ObjectHolder(BlockNames.fluidTank) public static Block fluidTank;
    @ObjectHolder(BlockNames.bossSpawner) public static Block bossSpawner;
    @ObjectHolder(BlockNames.slabGCHalf) public static Block slabGCHalf;
    @ObjectHolder(BlockNames.slabGCDouble) public static Block slabGCDouble;
    @ObjectHolder(BlockNames.tinStairs1) public static Block tinStairs1;
    @ObjectHolder(BlockNames.tinStairs2) public static Block tinStairs2;
    @ObjectHolder(BlockNames.moonStoneStairs) public static Block moonStoneStairs;
    @ObjectHolder(BlockNames.moonBricksStairs) public static Block moonBricksStairs;
    @ObjectHolder(BlockNames.wallGC) public static Block wallGC;
    @ObjectHolder(BlockNames.concealedRedstone) public static Block concealedRedstone;
    @ObjectHolder(BlockNames.concealedRepeater) public static Block concealedRepeater;
    @ObjectHolder(BlockNames.concealedDetector) public static Block concealedDetector;
    @ObjectHolder(BlockNames.platform) public static Block platform;
    @ObjectHolder(BlockNames.emergencyBox) public static Block emergencyBox;
    @ObjectHolder(BlockNames.grating) public static Block grating;
    @ObjectHolder(BlockNames.gratingWater) public static Block gratingWater;
    @ObjectHolder(BlockNames.gratingLava) public static Block gratingLava;

//    public static Block breatheableAir;
//    public static Block brightAir;
//    public static Block brightBreatheableAir;
//    public static Block brightLamp;
//    public static Block treasureChestTier1;
//    public static Block landingPad;
//    public static Block unlitTorch;
//    public static Block unlitTorchLit;
//    public static Block oxygenDistributor;
//    public static Block oxygenPipe;
//    public static Block oxygenPipePull;
//    public static Block oxygenCollector;
//    public static Block oxygenCompressor;
//    public static Block oxygenSealer;
//    public static Block oxygenDetector;
//    public static Block nasaWorkbench;
//    public static Block fallenMeteor;
//    public static Block basicBlock;
//    public static Block airLockFrame;
//    public static Block airLockSeal;
//    public static BlockSpaceGlass spaceGlassClear;
//    public static BlockSpaceGlass spaceGlassVanilla;
//    public static BlockSpaceGlass spaceGlassStrong;
//    public static BlockSpaceGlass spaceGlassTinClear;
//    public static BlockSpaceGlass spaceGlassTinVanilla;
//    public static BlockSpaceGlass spaceGlassTinStrong;
//    public static Block crafting;
//    public static Block crudeOil;
//    public static Block fuel;
//    public static Block refinery;
//    public static Block fuelLoader;
//    public static Block landingPadFull;
//    public static Block spaceStationBase;
//    public static Block fakeBlock;
//    public static Block sealableBlock;
//    public static Block cargoLoader;
//    public static Block parachest;
//    public static Block solarPanel;
//    public static Block radioTelescope;
//    public static Block machineBase;
//    public static Block machineBase2;
//    public static Block machineBase3;
//    public static Block machineBase4;
//    public static Block machineTiered;
//    public static Block aluminumWire;
//    public static Block panelLighting;
//    public static Block glowstoneTorch;
//    public static Block blockMoon;
//    public static Block cheeseBlock;
//    public static Block spinThruster;
//    public static Block screen;
//    public static Block telemetry;
//    public static Block fluidTank;
//    public static Block bossSpawner;
//    public static Block slabGCHalf;
//    public static Block slabGCDouble;
//    public static Block tinStairs1;
//    public static Block tinStairs2;
//    public static Block moonStoneStairs;
//    public static Block moonBricksStairs;
//    public static Block wallGC;
//    public static Block concealedRedstone;
//    public static Block concealedRepeater_Powered;
//    public static Block concealedRepeater_Unpowered;
//    public static Block concealedDetector;
//    public static Block platform;
//    public static Block emergencyBox;
//    public static Block grating;
//    public static Block gratingWater;
//    public static Block gratingLava;

    public static final Material machine = new Material.Builder(MaterialColor.IRON).build();

    public static ArrayList<Block> hiddenBlocks = new ArrayList<Block>();
    public static ArrayList<Block> otherModTorchesLit = new ArrayList<Block>();
    public static ArrayList<Block> otherModTorchesUnlit = new ArrayList<Block>();

    public static Map<EnumSortCategoryBlock, List<StackSorted>> sortMapBlocks = Maps.newHashMap();
    public static HashMap<Block, Block> itemChanges = new HashMap<>(4, 1.0F);

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name)
    {
        reg.register(thing.setRegistryName(name));
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name)
    {
        register(reg, thing, new ResourceLocation(Constants.MOD_ID_CORE, name));
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> evt)
    {
        IForgeRegistry<Block> r = evt.getRegistry();

        Block.Properties builder = Block.Properties.create(Material.AIR).doesNotBlockMovement().noDrops().hardnessAndResistance(0.0F, 10000.0F);
        register(r, new BlockBreathableAir(builder), BlockNames.breatheableAir);

        builder = builder.lightValue(15);
        register(r, new BlockBrightAir(builder), BlockNames.brightAir);
        register(r, new BlockBrightBreathableAir(builder), BlockNames.brightBreatheableAir);

        builder = Block.Properties.create(Material.GLASS).hardnessAndResistance(0.1F).sound(SoundType.METAL).lightValue(13);
        register(r, new BlockBrightLamp(builder), BlockNames.brightLamp);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(100000.0F).sound(SoundType.STONE).lightValue(13);
        register(r, new BlockTier1TreasureChest(builder), BlockNames.treasureChestTier1);

        builder = Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F).sound(SoundType.METAL);
        register(r, new BlockLandingPad(builder), BlockNames.landingPad);
        register(r, new BlockLandingPadFull(builder), BlockNames.landingPadFull);

        builder = Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.0F).lightValue(3).sound(SoundType.WOOD);
        register(r, new BlockUnlitTorch(false, builder), BlockNames.unlitTorch);
        register(r, new BlockUnlitTorch(true, builder.lightValue(14)), BlockNames.unlitTorchLit);
        register(r, new BlockGlowstoneTorch(builder.lightValue(12)), BlockNames.glowstoneTorch);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(1.0F).sound(SoundType.METAL);
        register(r, new BlockOxygenDistributor(builder), BlockNames.oxygenDistributor);
        register(r, new BlockOxygenCollector(builder), BlockNames.oxygenCollector);
        register(r, new BlockNasaWorkbench(builder), BlockNames.nasaWorkbench);
        register(r, new BlockAirLockFrame(builder), BlockNames.airLockFrame);
        register(r, new BlockMachine(builder), BlockNames.machineBase);
        register(r, new BlockMachine2(builder), BlockNames.machineBase2);
        register(r, new BlockMachine3(builder), BlockNames.machineBase3);
        register(r, new BlockMachine4(builder), BlockNames.machineBase4);
        register(r, new BlockCrafting(builder), BlockNames.crafting);
        register(r, new BlockRefinery(builder), BlockNames.refinery);
        register(r, new BlockFuelLoader(builder), BlockNames.fuelLoader);
        register(r, new BlockOxygenCompressor(builder), BlockNames.oxygenCompressor);
        register(r, new BlockOxygenSealer(builder), BlockNames.oxygenSealer);
        register(r, new BlockOxygenDetector(builder), BlockNames.oxygenDetector);
        register(r, new BlockCargoLoader(builder), BlockNames.cargoLoader);
        register(r, new BlockSolar(builder), BlockNames.solarPanel);
        register(r, new BlockDish(builder), BlockNames.radioTelescope);
        register(r, new BlockMachineTiered(builder), BlockNames.machineTiered);
        register(r, new BlockPanelLighting(builder), BlockNames.panelLighting);
        register(r, new BlockSpinThruster(builder), BlockNames.spinThruster);
        register(r, new BlockTelemetry(builder), BlockNames.telemetry);
        register(r, new BlockConcealedRedstone(builder), BlockNames.concealedRedstone);
        register(r, new BlockConcealedRepeater(builder), BlockNames.concealedRepeater);
        register(r, new BlockConcealedDetector(builder), BlockNames.concealedDetector);

        builder = builder.hardnessAndResistance(1000.0F).tickRandomly();
        register(r, new BlockAirLockWall(builder), BlockNames.airLockSeal);

        builder = Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS);
        register(r, new BlockFluidPipe(builder, BlockFluidPipe.EnumPipeMode.NORMAL), BlockNames.oxygenPipe);
        register(r, new BlockFluidPipe(builder, BlockFluidPipe.EnumPipeMode.PULL), BlockNames.oxygenPipePull);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(40.0F).sound(SoundType.STONE);
        register(r, new BlockFallenMeteor(builder), BlockNames.fallenMeteor);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(1.0F).sound(SoundType.STONE);
        register(r, new BlockBasic(builder), BlockNames.basicBlock);

        //These glass types have to be registered as 6 separate blocks, (a) to allow different coloring of each one and (b) because the Forge MultiLayer custom model does not allow for different textures to be set for different variants
        builder = Block.Properties.create(Material.GLASS).sound(SoundType.GLASS).hardnessAndResistance(0.3F, 3.0F);
        register(r, new BlockSpaceGlass(builder, GlassType.VANILLA, GlassFrame.PLAIN, null), BlockNames.spaceGlassVanilla);
        register(r, new BlockSpaceGlass(builder, GlassType.CLEAR, GlassFrame.PLAIN, null), BlockNames.spaceGlassClear);
        register(r, new BlockSpaceGlass(builder, GlassType.VANILLA, GlassFrame.TIN_DECO, GCBlocks.spaceGlassVanilla), BlockNames.spaceGlassTinVanilla);
        register(r, new BlockSpaceGlass(builder, GlassType.CLEAR, GlassFrame.TIN_DECO, GCBlocks.spaceGlassClear), BlockNames.spaceGlassTinClear);

        builder = builder.hardnessAndResistance(4.0F, 35.0F);
        register(r, new BlockSpaceGlass(builder, GlassType.STRONG, GlassFrame.PLAIN, null), BlockNames.spaceGlassStrong);
        register(r, new BlockSpaceGlass(builder, GlassType.STRONG, GlassFrame.TIN_DECO, GCBlocks.spaceGlassStrong), BlockNames.spaceGlassTinStrong);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(-1.0F, 3600000.0F).noDrops();
        register(r, new BlockSpaceStationBase(builder), BlockNames.spaceStationBase);

        builder = Block.Properties.create(machine).hardnessAndResistance(1.0F, 3600000.0F).sound(SoundType.METAL);
        register(r, new BlockMulti(builder), BlockNames.fakeBlock);

        builder = Block.Properties.create(Material.CLAY).hardnessAndResistance(0.4F).sound(SoundType.STONE);
        register(r, new BlockEnclosed(builder), BlockNames.sealableBlock);

        builder = Block.Properties.create(Material.WOOD).hardnessAndResistance(3.0F).sound(SoundType.WOOD);
        register(r, new BlockParaChest(builder), BlockNames.parachest);

        builder = Block.Properties.create(Material.WOOL).hardnessAndResistance(0.2F).sound(SoundType.CLOTH);
        register(r, new BlockAluminumWire(builder), BlockNames.aluminumWire);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(1.5F, 2.5F).sound(SoundType.STONE);
        register(r, new BlockBasicMoon(builder), BlockNames.blockMoon);

        builder = Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH);
        register(r, new BlockCheese(builder), BlockNames.cheeseBlock);

        builder = Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1.0F).sound(SoundType.STONE);
        register(r, new BlockScreen(builder), BlockNames.screen);

        builder = Block.Properties.create(Material.GLASS).hardnessAndResistance(3.0F, 8.0F).sound(SoundType.GLASS);
        register(r, new BlockFluidTank(builder), BlockNames.fluidTank);

        builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(1000000.0F).noDrops();
        register(r, new BlockBossSpawner(builder), BlockNames.bossSpawner);

//        register(r, new BlockSlabGC(builder), BlockNames.slabGCHalf); TODO
//        register(r, new BlockDoubleSlabGC(builder), BlockNames.slabGCDouble);
//        register(r, new BlockStairsGC(builder), BlockNames.tinStairs1);
//        register(r, new BlockStairsGC(builder), BlockNames.tinStairs2);
//        register(r, new BlockStairsGC(builder), BlockNames.moonStoneStairs);
//        register(r, new BlockStairsGC(builder), BlockNames.moonBricksStairs);
//        register(r, new BlockWallGC(builder), BlockNames.wallGC);

        builder = Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F).sound(SoundType.METAL);
        register(r, new BlockPlatform(builder), BlockNames.platform);

        builder = Block.Properties.create(Material.IRON).hardnessAndResistance(4.0F, 70.0F).lightValue(15).sound(SoundType.METAL);
        register(r, new BlockEmergencyBox(builder), BlockNames.emergencyBox);

//        register(r, new BlockGrating(builder), BlockNames.grating); TODO
//        register(r, new BlockGrating(builder), BlockNames.gratingWater);
//        register(r, new BlockGrating(builder), BlockNames.gratingLava);

//        GCBlocks.breatheableAir = new BlockBreathableAir("breatheable_air");
//        GCBlocks.brightAir = new BlockBrightAir("bright_air");
//        GCBlocks.brightBreatheableAir = new BlockBrightBreathableAir("bright_breathable_air");
//        GCBlocks.brightLamp = new BlockBrightLamp("arclamp");
//        GCBlocks.treasureChestTier1 = new BlockTier1TreasureChest("treasure_chest");
//        GCBlocks.landingPad = new BlockLandingPad("landing_pad");
//        GCBlocks.landingPadFull = new BlockLandingPadFull("landing_pad_full");
//        GCBlocks.unlitTorch = new BlockUnlitTorch(false, "unlit_torch");
//        GCBlocks.unlitTorchLit = new BlockUnlitTorch(true, "unlit_torch_lit");
//        GCBlocks.oxygenDistributor = new BlockOxygenDistributor("distributor");
//        GCBlocks.oxygenPipe = new BlockFluidPipe("fluid_pipe", BlockFluidPipe.EnumPipeMode.NORMAL);
//        GCBlocks.oxygenPipePull = new BlockFluidPipe("fluid_pipe_pull", BlockFluidPipe.EnumPipeMode.PULL);
//        GCBlocks.oxygenCollector = new BlockOxygenCollector("collector");
//        GCBlocks.nasaWorkbench = new BlockNasaWorkbench("rocket_workbench");
//        GCBlocks.fallenMeteor = new BlockFallenMeteor("fallen_meteor");
//        GCBlocks.basicBlock = new BlockBasic("basic_block_core");
//        GCBlocks.airLockFrame = new BlockAirLockFrame("air_lock_frame");
//        GCBlocks.airLockSeal = new BlockAirLockWall("air_lock_seal");
//        //These glass types have to be registered as 6 separate blocks, (a) to allow different coloring of each one and (b) because the Forge MultiLayer custom model does not allow for different textures to be set for different variants
//        GCBlocks.spaceGlassVanilla = new BlockSpaceGlass("space_glass_vanilla", GlassType.VANILLA, GlassFrame.PLAIN, null).setHardness(0.3F).setResistance(3F);
//        GCBlocks.spaceGlassClear = new BlockSpaceGlass("space_glass_clear", GlassType.CLEAR, GlassFrame.PLAIN, null).setHardness(0.3F).setResistance(3F);
//        GCBlocks.spaceGlassStrong = new BlockSpaceGlass("space_glass_strong", GlassType.STRONG, GlassFrame.PLAIN, null).setHardness(4F).setResistance(35F);
//        GCBlocks.spaceGlassTinVanilla = new BlockSpaceGlass("space_glass_vanilla_tin", GlassType.VANILLA, GlassFrame.TIN_DECO, GCBlocks.spaceGlassVanilla).setHardness(0.3F).setResistance(4F);
//        GCBlocks.spaceGlassTinClear = new BlockSpaceGlass("space_glass_clear_tin", GlassType.CLEAR, GlassFrame.TIN_DECO, GCBlocks.spaceGlassClear).setHardness(0.3F).setResistance(4F);
//        GCBlocks.spaceGlassTinStrong = new BlockSpaceGlass("space_glass_strong_tin", GlassType.STRONG, GlassFrame.TIN_DECO, GCBlocks.spaceGlassStrong).setHardness(4F).setResistance(35F);
//        GCBlocks.crafting = new BlockCrafting("magnetic_table");
//        GCBlocks.refinery = new BlockRefinery("refinery");
//        GCBlocks.oxygenCompressor = new BlockOxygenCompressor(false, "oxygen_compressor");
//        GCBlocks.fuelLoader = new BlockFuelLoader("fuel_loader");
//        GCBlocks.spaceStationBase = new BlockSpaceStationBase("space_station_base");
//        GCBlocks.fakeBlock = new BlockMulti("block_multi");
//        GCBlocks.oxygenSealer = new BlockOxygenSealer("sealer");
//        GCBlocks.sealableBlock = new BlockEnclosed("enclosed");
//        GCBlocks.oxygenDetector = new BlockOxygenDetector("oxygen_detector");
//        GCBlocks.cargoLoader = new BlockCargoLoader("cargo");
//        GCBlocks.parachest = new BlockParaChest("parachest");
//        GCBlocks.solarPanel = new BlockSolar("solar");
//        GCBlocks.radioTelescope = new BlockDish("dishbase");
//        GCBlocks.machineBase = new BlockMachine("machine");
//        GCBlocks.machineBase2 = new BlockMachine2("machine2");
//        GCBlocks.machineBase3 = new BlockMachine3("machine3");
//        GCBlocks.machineBase4 = new BlockMachine4("machine4");
//        GCBlocks.machineTiered = new BlockMachineTiered("machine_tiered");
//        GCBlocks.aluminumWire = new BlockAluminumWire("aluminum_wire");
//        GCBlocks.panelLighting = new BlockPanelLighting("panel_lighting");
//        GCBlocks.glowstoneTorch = new BlockGlowstoneTorch("glowstone_torch");
//        GCBlocks.blockMoon = new BlockBasicMoon("basic_block_moon");
//        GCBlocks.cheeseBlock = new BlockCheese("cheese");
//        GCBlocks.spinThruster = new BlockSpinThruster("spin_thruster");
//        GCBlocks.screen = new BlockScreen("view_screen");
//        GCBlocks.telemetry = new BlockTelemetry("telemetry");
//        GCBlocks.fluidTank = new BlockFluidTank("fluid_tank");
//        GCBlocks.bossSpawner = new BlockBossSpawner("boss_spawner");
//        GCBlocks.slabGCHalf = new BlockSlabGC("slab_gc_half", Material.ROCK);
//        GCBlocks.slabGCDouble = new BlockDoubleSlabGC("slab_gc_double", Material.ROCK);
//        GCBlocks.tinStairs1 = new BlockStairsGC("tin_stairs_1", basicBlock.getDefaultState().with(BlockBasic.BASIC_TYPE, BlockBasic.EnumBlockBasic.ALUMINUM_DECORATION_BLOCK_0)).setHardness(2.0F);
//        GCBlocks.tinStairs2 = new BlockStairsGC("tin_stairs_2", basicBlock.getDefaultState().with(BlockBasic.BASIC_TYPE, BlockBasic.EnumBlockBasic.ALUMINUM_DECORATION_BLOCK_1)).setHardness(2.0F);
//        GCBlocks.moonStoneStairs = new BlockStairsGC("moon_stairs_stone", blockMoon.getDefaultState().with(BlockBasicMoon.BASIC_TYPE_MOON, BlockBasicMoon.EnumBlockBasicMoon.MOON_STONE)).setHardness(1.5F);
//        GCBlocks.moonBricksStairs = new BlockStairsGC("moon_stairs_brick", blockMoon.getDefaultState().with(BlockBasicMoon.BASIC_TYPE_MOON, BlockBasicMoon.EnumBlockBasicMoon.MOON_DUNGEON_BRICK)).setHardness(4.0F);
//        GCBlocks.wallGC = new BlockWallGC("wall_gc");
//        GCBlocks.concealedRedstone = new BlockConcealedRedstone("concealed_redstone");
//        GCBlocks.concealedRepeater_Powered = new BlockConcealedRepeater("concealed_repeater_pow", true);
//        GCBlocks.concealedRepeater_Unpowered = new BlockConcealedRepeater("concealed_repeater", false);
//        GCBlocks.concealedDetector = new BlockConcealedDetector("concealed_detector");
//        GCBlocks.platform = new BlockPlatform("platform");
//        GCBlocks.emergencyBox = new BlockEmergencyBox("emergency_box");
//        GCBlocks.grating = new BlockGrating("grating", ConfigManagerCore.allowLiquidGratings ? Material.CARPET : Material.IRON);
//        GCBlocks.gratingWater = new BlockGrating("grating1", Material.WATER);
//        GCBlocks.gratingLava = new BlockGrating("grating2", Material.LAVA).setLightLevel(1.0F);

        // Hide certain items from NEI
        GCBlocks.hiddenBlocks.add(GCBlocks.airLockSeal);
        GCBlocks.hiddenBlocks.add(GCBlocks.oxygenPipePull);
        GCBlocks.hiddenBlocks.add(GCBlocks.unlitTorch);
        GCBlocks.hiddenBlocks.add(GCBlocks.unlitTorchLit);
        GCBlocks.hiddenBlocks.add(GCBlocks.landingPadFull);
        GCBlocks.hiddenBlocks.add(GCBlocks.spaceStationBase);
        GCBlocks.hiddenBlocks.add(GCBlocks.bossSpawner);
        GCBlocks.hiddenBlocks.add(GCBlocks.slabGCDouble);

        // Register blocks before register ores, so that the ItemStack picks up the correct item
//        GCBlocks.registerBlocks();
//        GCBlocks.setHarvestLevels();
    }
    
    public static void oreDictRegistrations()
    {
        // TODO
//        OreDictionary.registerOre("oreCopper", new ItemStack(GCBlocks.basicBlock, 1, 5));
//        OreDictionary.registerOre("oreCopper", new ItemStack(GCBlocks.blockMoon, 1, 0));
//        OreDictionary.registerOre("oreTin", new ItemStack(GCBlocks.basicBlock, 1, 6));
//        OreDictionary.registerOre("oreTin", new ItemStack(GCBlocks.blockMoon, 1, 1));
//        OreDictionary.registerOre("oreAluminum", new ItemStack(GCBlocks.basicBlock, 1, 7));
//        OreDictionary.registerOre("oreAluminium", new ItemStack(GCBlocks.basicBlock, 1, 7));
//        OreDictionary.registerOre("oreNaturalAluminum", new ItemStack(GCBlocks.basicBlock, 1, 7));
//        OreDictionary.registerOre("oreSilicon", new ItemStack(GCBlocks.basicBlock, 1, 8));
//        OreDictionary.registerOre("oreCheese", new ItemStack(GCBlocks.blockMoon, 1, 2));
//
//        OreDictionary.registerOre("blockCopper", new ItemStack(GCBlocks.basicBlock, 1, 9));
//        OreDictionary.registerOre("blockTin", new ItemStack(GCBlocks.basicBlock, 1, 10));
//        OreDictionary.registerOre("blockAluminum", new ItemStack(GCBlocks.basicBlock, 1, 11));
//        OreDictionary.registerOre("blockAluminium", new ItemStack(GCBlocks.basicBlock, 1, 11));
//        OreDictionary.registerOre("blockSilicon", new ItemStack(GCBlocks.basicBlock, 1, 13));
//
//        OreDictionary.registerOre("turfMoon", new ItemStack(GCBlocks.blockMoon, 1, EnumBlockBasicMoon.MOON_TURF.getMeta()));
    }

//    public static void finalizeSort()
//    {
//        List<StackSorted> itemOrderListBlocks = Lists.newArrayList();
//        for (EnumSortCategoryBlock type : EnumSortCategoryBlock.values())
//        {
//            if (!GalacticraftCore.isPlanetsLoaded && type == EnumSortCategoryBlock.EGG)
//                continue;
//            List<StackSorted> stackSorteds = sortMapBlocks.get(type);
//            if (stackSorteds != null)
//            {
//                itemOrderListBlocks.addAll(stackSorteds);
//            }
//            else
//            {
//                System.out.println("ERROR: null sort stack: " + type.toString());
//            }
//        }
//        Comparator<ItemStack> tabSorterBlocks = Ordering.explicit(itemOrderListBlocks).onResultOf(input -> new StackSorted(input.getItem(), input.getItemDamage()));
//        GalacticraftCore.galacticraftBlocksTab.setTabSorter(tabSorterBlocks);
//    }

//    private static void setHarvestLevel(Block block, String toolClass, int level, int meta)
//    {
//        block.setHarvestLevel(toolClass, level, block.getStateFromMeta(meta));
//    }

//    public static void doOtherModsTorches(IForgeRegistry<Block> registry)
//    {
//        BlockUnlitTorch torch;
//        BlockUnlitTorch torchLit;
//
//        if (CompatibilityManager.isTConstructLoaded)
//        {
//            Block modTorch = null;
//            try
//            {
//                //tconstruct.world.TinkerWorld.stoneTorch
//                Class clazz = Class.forName("slimeknights.tconstruct.gadgets.TinkerGadgets");
//                modTorch = (Block) clazz.getDeclaredField("stoneTorch").get(null);
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//
//            if (modTorch != null)
//            {
//                torch = new BlockUnlitTorch(false, "unlit_torch_stone");
//                torchLit = new BlockUnlitTorch(true, "unlit_torch_stone_lit");
//                GCBlocks.hiddenBlocks.add(torch);
//                GCBlocks.hiddenBlocks.add(torchLit);
//                GCBlocks.otherModTorchesUnlit.add(torch);
//                GCBlocks.otherModTorchesLit.add(torchLit);
//                registerBlock(torch, ItemBlockGC.class);
//                registerBlock(torchLit, ItemBlockGC.class);
//                registry.register(torch);
//                registry.register(torchLit);
//                BlockUnlitTorch.register(torch, torchLit, modTorch);
//                GCLog.info("Galacticraft: activating Tinker's Construct compatibility.");
//            }
//        }
//    } TODO

//    public static void registerFuel()
//    {
//        GCBlocks.fuel = new BlockFluidGC(GCFluids.fluidFuel, "fuel");
//        ((BlockFluidGC) GCBlocks.fuel).setQuantaPerBlock(3);
//        GCBlocks.fuel.setUnlocalizedName("fuel");
//        GCBlocks.registerBlock(GCBlocks.fuel, ItemBlockGC.class);
//    }
//
//    public static void registerOil()
//    {
//        GCBlocks.crudeOil = new BlockFluidGC(GCFluids.fluidOil, "oil");
//        ((BlockFluidGC) GCBlocks.crudeOil).setQuantaPerBlock(3);
//        GCBlocks.crudeOil.setUnlocalizedName("crude_oil_still");
//        GCBlocks.registerBlock(GCBlocks.crudeOil, ItemBlockGC.class);
//    } TODO

//    public static void setHarvestLevels()
//    {
//        setHarvestLevel(GCBlocks.basicBlock, "pickaxe", 1, 5); //Copper ore
//        setHarvestLevel(GCBlocks.basicBlock, "pickaxe", 1, 6); //Tin ore
//        setHarvestLevel(GCBlocks.basicBlock, "pickaxe", 1, 7); //Aluminium ore
//        setHarvestLevel(GCBlocks.basicBlock, "pickaxe", 2, 8); //Silicon ore
//        setHarvestLevel(GCBlocks.fallenMeteor, "pickaxe", 3, 0);
//        setHarvestLevel(GCBlocks.blockMoon, "pickaxe", 1, 0); //Copper ore
//        setHarvestLevel(GCBlocks.blockMoon, "pickaxe", 1, 1); //Tin ore
//        setHarvestLevel(GCBlocks.blockMoon, "pickaxe", 1, 2); //Cheese ore
//        setHarvestLevel(GCBlocks.blockMoon, "shovel", 0, 3); //Moon dirt
//        setHarvestLevel(GCBlocks.blockMoon, "pickaxe", 0, 4); //Moon rock
//
//        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 1, 0);
//        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 1, 1);
//        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 1, 2);
//        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 3, 3);
//        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 1, 4);
//        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 3, 5);
//        setHarvestLevel(GCBlocks.slabGCHalf, "pickaxe", 1, 6);
//
//        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 1, 0);
//        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 1, 1);
//        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 1, 2);
//        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 3, 3);
//        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 1, 4);
//        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 3, 5);
//        setHarvestLevel(GCBlocks.slabGCDouble, "pickaxe", 1, 6);
//
//        setHarvestLevel(GCBlocks.tinStairs1, "pickaxe", 1, 0);
//        setHarvestLevel(GCBlocks.tinStairs1, "pickaxe", 1, 0);
//
//        setHarvestLevel(GCBlocks.moonStoneStairs, "pickaxe", 1, 0);
//        setHarvestLevel(GCBlocks.moonBricksStairs, "pickaxe", 3, 0);
//
//        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 1, 0);
//        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 1, 1);
//        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 1, 2);
//        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 3, 3);
//        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 0, 4);
//        setHarvestLevel(GCBlocks.wallGC, "pickaxe", 3, 5);
//
//        setHarvestLevel(GCBlocks.wallGC, "shovel", 0, 5);
//
//        setHarvestLevel(GCBlocks.blockMoon, "pickaxe", 3, 14); //Moon dungeon brick (actually unharvestable)
//    } TODO

//    public static void registerBlock(Block block, Class<? extends BlockItem> itemClass, Object... itemCtorArgs)
//    {
//        String name = block.getUnlocalizedName().substring(5);
//        if (block.getRegistryName() == null)
//        {
//            block.setRegistryName(name);
//        }
//        GCCoreUtil.registerGalacticraftBlock(name, block);
//
//        if (itemClass != null)
//        {
//            BlockItem item = null;
//            Class<?>[] ctorArgClasses = new Class<?>[itemCtorArgs.length + 1];
//            ctorArgClasses[0] = Block.class;
//            for (int idx = 1; idx < ctorArgClasses.length; idx++)
//            {
//                ctorArgClasses[idx] = itemCtorArgs[idx - 1].getClass();
//            }
//
//            try
//            {
//                Constructor<? extends BlockItem> constructor = itemClass.getConstructor(ctorArgClasses);
//                item = constructor.newInstance(ObjectArrays.concat(block, itemCtorArgs));
//            }
//            catch (Exception e)
//            {
//                e.printStackTrace();
//            }
//
//            if (item != null)
//            {
//                GCCoreUtil.registerGalacticraftItem(name, item);
//                if (item.getRegistryName() == null)
//                {
//                    item.setRegistryName(name);
//                }
//            }
//        }
//    }
    
//    public static void registerBlocks(IForgeRegistry<Block> registry)
//    {
//        for (Block block : GalacticraftCore.blocksList)
//        {
//            registry.register(block);
//        }
//
//        //Complete registration of various types of torches
//        BlockUnlitTorch.register((BlockUnlitTorch) GCBlocks.unlitTorch, (BlockUnlitTorch) GCBlocks.unlitTorchLit, Blocks.TORCH);
//    }

//    public static void registerBlocks()
//    {
//        registerBlock(GCBlocks.landingPad, ItemBlockLandingPad.class);
//        registerBlock(GCBlocks.landingPadFull, ItemBlockGC.class);
//        registerBlock(GCBlocks.unlitTorch, ItemBlockGC.class);
//        registerBlock(GCBlocks.unlitTorchLit, ItemBlockGC.class);
//        registerBlock(GCBlocks.breatheableAir, null);
//        registerBlock(GCBlocks.brightAir, null);
//        registerBlock(GCBlocks.brightBreatheableAir, null);
//        registerBlock(GCBlocks.oxygenDistributor, ItemBlockDesc.class);
//        registerBlock(GCBlocks.oxygenCollector, ItemBlockDesc.class);
//        registerBlock(GCBlocks.oxygenCompressor, ItemBlockOxygenCompressor.class);
//        registerBlock(GCBlocks.oxygenSealer, ItemBlockDesc.class);
//        registerBlock(GCBlocks.oxygenDetector, ItemBlockDesc.class);
//        registerBlock(GCBlocks.aluminumWire, ItemBlockAluminumWire.class);
//        registerBlock(GCBlocks.oxygenPipe, ItemBlockDesc.class);
//        registerBlock(GCBlocks.oxygenPipePull, ItemBlockDesc.class);
//        registerBlock(GCBlocks.refinery, ItemBlockDesc.class);
//        registerBlock(GCBlocks.fuelLoader, ItemBlockDesc.class);
//        registerBlock(GCBlocks.cargoLoader, ItemBlockCargoLoader.class);
//        registerBlock(GCBlocks.nasaWorkbench, ItemBlockNasaWorkbench.class);
//        registerBlock(GCBlocks.basicBlock, ItemBlockBase.class);
//        registerBlock(GCBlocks.airLockFrame, ItemBlockAirLock.class);
//        registerBlock(GCBlocks.airLockSeal, ItemBlockGC.class);
//        registerBlock(GCBlocks.spaceGlassClear, ItemBlockGlassGC.class);
//        registerBlock(GCBlocks.spaceGlassVanilla, ItemBlockGlassGC.class);
//        registerBlock(GCBlocks.spaceGlassStrong, ItemBlockGlassGC.class);
//        registerBlock(GCBlocks.spaceGlassTinClear, null);  //The corresponding item is already registered
//        registerBlock(GCBlocks.spaceGlassTinVanilla, null);  //The corresponding item is already registered
//        registerBlock(GCBlocks.spaceGlassTinStrong, null);  //The corresponding item is already registered
//        registerBlock(GCBlocks.crafting, ItemBlockDesc.class);
//        registerBlock(GCBlocks.sealableBlock, ItemBlockEnclosed.class);
//        registerBlock(GCBlocks.spaceStationBase, ItemBlockGC.class);
//        registerBlock(GCBlocks.fakeBlock, null);
//        registerBlock(GCBlocks.parachest, ItemBlockDesc.class);
//        registerBlock(GCBlocks.solarPanel, ItemBlockSolar.class);
//        registerBlock(GCBlocks.radioTelescope, ItemBlockGC.class);
//        registerBlock(GCBlocks.machineBase, ItemBlockMachine.class);
//        registerBlock(GCBlocks.machineBase2, ItemBlockMachine.class);
//        registerBlock(GCBlocks.machineBase3, ItemBlockMachine.class);
//        registerBlock(GCBlocks.machineTiered, ItemBlockMachine.class);
//        registerBlock(GCBlocks.machineBase4, ItemBlockMachine.class);
//        registerBlock(GCBlocks.panelLighting, ItemBlockPanel.class);
//        registerBlock(GCBlocks.glowstoneTorch, ItemBlockDesc.class);
//        registerBlock(GCBlocks.fallenMeteor, ItemBlockDesc.class);
//        registerBlock(GCBlocks.blockMoon, ItemBlockMoon.class);
//        registerBlock(GCBlocks.cheeseBlock, ItemBlockCheese.class);
//        registerBlock(GCBlocks.spinThruster, ItemBlockThruster.class);
//        registerBlock(GCBlocks.screen, ItemBlockDesc.class);
//        registerBlock(GCBlocks.telemetry, ItemBlockDesc.class);
//        registerBlock(GCBlocks.brightLamp, ItemBlockArclamp.class);
//        registerBlock(GCBlocks.treasureChestTier1, ItemBlockDesc.class);
//        registerBlock(GCBlocks.fluidTank, ItemBlockDesc.class);
//        registerBlock(GCBlocks.bossSpawner, ItemBlockGC.class);
//        registerBlock(GCBlocks.tinStairs1, ItemBlockGC.class);
//        registerBlock(GCBlocks.tinStairs2, ItemBlockGC.class);
//        registerBlock(GCBlocks.moonStoneStairs, ItemBlockGC.class);
//        registerBlock(GCBlocks.moonBricksStairs, ItemBlockGC.class);
//        registerBlock(GCBlocks.wallGC, ItemBlockWallGC.class);
//        registerBlock(GCBlocks.slabGCHalf, ItemBlockSlabGC.class, GCBlocks.slabGCHalf, GCBlocks.slabGCDouble);
//        registerBlock(GCBlocks.slabGCDouble, ItemBlockSlabGC.class, GCBlocks.slabGCHalf, GCBlocks.slabGCDouble);
//        registerBlock(GCBlocks.concealedRedstone, ItemBlockGC.class);
//        registerBlock(GCBlocks.concealedRepeater_Powered, ItemBlockGC.class);
//        registerBlock(GCBlocks.concealedRepeater_Unpowered, ItemBlockGC.class);
//        registerBlock(GCBlocks.concealedDetector, ItemBlockCreativeGC.class);
//        registerBlock(GCBlocks.platform, ItemBlockDesc.class);
//        registerBlock(GCBlocks.emergencyBox, ItemBlockEmergencyBox.class);
//        registerBlock(GCBlocks.grating, ItemBlockGC.class);
//        registerBlock(GCBlocks.gratingWater, null);
//        registerBlock(GCBlocks.gratingLava, null);
////        GCCoreUtil.sortBlock(GCBlocks.aluminumWire, 0, new StackSorted(GCBlocks.landingPad, 1));
////        GCCoreUtil.sortBlock(GCBlocks.aluminumWire, 1, new StackSorted(GCBlocks.aluminumWire, 0));
////        GCCoreUtil.sortBlock(GCBlocks.oxygenPipe, 0, new StackSorted(GCBlocks.aluminumWire, 1));
//    }
}
