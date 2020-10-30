package micdoodle8.mods.galacticraft.core;

import micdoodle8.mods.galacticraft.core.blocks.*;
import micdoodle8.mods.galacticraft.core.tile.*;
import micdoodle8.mods.galacticraft.planets.venus.blocks.BlockDungeonBrick;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.*;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID_CORE, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID_CORE)
public class GCBlocks
{
    public static final Block breatheableAir = new BlockBreathableAir(Block.Properties.create(Material.AIR).doesNotBlockMovement().noDrops().hardnessAndResistance(0.0F, 10000.0F));
    public static final Block brightAir = new BlockBrightAir(Block.Properties.from(breatheableAir).lightValue(15));
    public static final Block brightBreatheableAir = new BlockBrightBreathableAir(Block.Properties.from(brightAir));

    public static final Block arcLamp = new BlockArcLamp(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.1F).sound(SoundType.METAL).lightValue(13));

    public static final Block treasureChestTier1 = new BlockTier1TreasureChest(Block.Properties.create(Material.ROCK).hardnessAndResistance(100000.0F).sound(SoundType.STONE).lightValue(13));

    public static final Block landingPad = new BlockPad(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F).sound(SoundType.METAL));
    public static final Block buggyPad = new BlockPad(Block.Properties.from(landingPad));
    public static final Block landingPadFull = new BlockPadFull(Block.Properties.from(landingPad));
    public static final Block buggyPadFull = new BlockPadFull(Block.Properties.from(landingPad));

    public static final Block unlitTorch = new BlockUnlitTorch(false, Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.0F).lightValue(3).sound(SoundType.WOOD));
    public static final Block unlitTorchWall = new BlockUnlitTorchWall(false, Block.Properties.from(unlitTorch));
    public static final Block unlitTorchLit = new BlockUnlitTorch(true, Block.Properties.from(unlitTorch).lightValue(14));
    public static final Block unlitTorchWallLit = new BlockUnlitTorchWall(true, Block.Properties.from(unlitTorch).lightValue(14));
    public static final Block glowstoneTorch = new BlockGlowstoneTorch(Block.Properties.from(unlitTorch).lightValue(12));
    public static final Block glowstoneTorchWall = new BlockGlowstoneTorchWall(Block.Properties.from(unlitTorch).lightValue(12));

    public static final Block oxygenDistributor = new BlockOxygenDistributor(Block.Properties.create(Material.ROCK).hardnessAndResistance(1.0F).sound(SoundType.METAL));
    public static final Block oxygenCollector = new BlockOxygenCollector(Block.Properties.from(oxygenDistributor));
    public static final Block nasaWorkbench = new BlockNasaWorkbench(Block.Properties.from(oxygenDistributor));
    public static final Block airLockFrame = new BlockAirLockFrame(Block.Properties.from(oxygenDistributor));
    public static final Block airLockController = new BlockAirLockController(Block.Properties.from(oxygenDistributor));
    public static final Block ingotCompressor = new BlockIngotCompressor(Block.Properties.from(oxygenDistributor));
    public static final Block ingotCompressorElectric = new BlockIngotCompressorElectric(Block.Properties.from(oxygenDistributor));
    public static final Block ingotCompressorElectricAdvanced = new BlockIngotCompressorElectricAdvanced(Block.Properties.from(oxygenDistributor));
    public static final Block coalGenerator = new BlockCoalGenerator(Block.Properties.from(oxygenDistributor));
    public static final Block circuitFabricator = new BlockCircuitFabricator(Block.Properties.from(oxygenDistributor));
    public static final Block oxygenStorageModule = new BlockOxygenStorageModule(Block.Properties.from(oxygenDistributor));
    public static final Block deconstructor = new BlockDeconstructor(Block.Properties.from(oxygenDistributor));
    public static final Block painter = new BlockPainter(Block.Properties.from(oxygenDistributor));
    public static final Block crafting = new BlockCrafting(Block.Properties.from(oxygenDistributor));
    public static final Block refinery = new BlockRefinery(Block.Properties.from(oxygenDistributor));
    public static final Block fuelLoader = new BlockFuelLoader(Block.Properties.from(oxygenDistributor));
    public static final Block oxygenCompressor = new BlockOxygenCompressor(Block.Properties.from(oxygenDistributor));
    public static final Block oxygenDecompressor = new BlockOxygenCompressor(Block.Properties.from(oxygenDistributor));
    public static final Block oxygenSealer = new BlockOxygenSealer(Block.Properties.from(oxygenDistributor));
    public static final Block oxygenDetector = new BlockOxygenDetector(Block.Properties.from(oxygenDistributor));
    public static final Block cargoLoader = new BlockCargoLoader(Block.Properties.from(oxygenDistributor));
    public static final Block cargoUnloader = new BlockCargoUnloader(Block.Properties.from(oxygenDistributor));
    public static final Block solarPanel = new BlockSolar(Block.Properties.from(oxygenDistributor));
    public static final Block solarPanelAdvanced = new BlockSolarAdvanced(Block.Properties.from(oxygenDistributor));
//    public static final Block radioTelescope = new BlockDish(Block.Properties.from(oxygenDistributor));
    public static final Block storageModule = new BlockEnergyStorageModule(Block.Properties.from(oxygenDistributor));
    public static final Block storageCluster = new BlockEnergyStorageCluster(Block.Properties.from(oxygenDistributor));
    public static final Block furnaceElectric = new BlockFurnaceElectric(Block.Properties.from(oxygenDistributor));
    public static final Block furanceArc = new BlockFurnaceArc(Block.Properties.from(oxygenDistributor));
//    public static final Block panelLighting = new BlockPanelLighting(Block.Properties.from(oxygenDistributor));
//    public static final Block spinThruster = new BlockSpinThruster(Block.Properties.from(oxygenDistributor));
    public static final Block telemetry = new BlockTelemetry(Block.Properties.from(oxygenDistributor));
    public static final Block concealedRedstone = new BlockConcealedRedstone(Block.Properties.from(oxygenDistributor));
    public static final Block concealedRepeater = new BlockConcealedRepeater(Block.Properties.from(oxygenDistributor));
    public static final Block concealedDetector = new BlockConcealedDetector(Block.Properties.from(oxygenDistributor));

    public static final Block airLockSeal = new BlockAirLockWall(Block.Properties.from(oxygenDistributor).hardnessAndResistance(1000.0F).tickRandomly());

    public static final Block fluidPipe = new BlockFluidPipe(Block.Properties.create(Material.GLASS).hardnessAndResistance(0.3F).sound(SoundType.GLASS), BlockFluidPipe.EnumPipeMode.NORMAL);
    public static final Block fluidPipePull = new BlockFluidPipe(Block.Properties.from(fluidPipe), BlockFluidPipe.EnumPipeMode.PULL);

    public static final Block fallenMeteor = new BlockFallenMeteor(Block.Properties.create(Material.ROCK).hardnessAndResistance(40.0F).sound(SoundType.STONE));
//    public static final Block spaceGlassVanilla = new BlockSpaceGlass(builder, GlassType.VANILLA, GlassFrame.PLAIN, null);
//    public static final Block spaceGlassClear = new BlockSpaceGlass(builder, GlassType.CLEAR, GlassFrame.PLAIN, null);
//    public static final Block spaceGlassTinVanilla = new BlockSpaceGlass(builder, GlassType.VANILLA, GlassFrame.TIN_DECO, GCBlocks.spaceGlassVanilla);
//    public static final Block spaceGlassTinClear = new BlockSpaceGlass(builder, GlassType.CLEAR, GlassFrame.TIN_DECO, GCBlocks.spaceGlassClear);
//    public static final Block spaceGlassStrong = new BlockSpaceGlass(builder, GlassType.STRONG, GlassFrame.PLAIN, null);
//    public static final Block spaceGlassTinStrong = new BlockSpaceGlass(builder, GlassType.STRONG, GlassFrame.TIN_DECO, GCBlocks.spaceGlassStrong);

    public static final Block spaceStationBase = new BlockSpaceStationBase(Block.Properties.create(Material.ROCK).hardnessAndResistance(-1.0F, 3600000.0F).noDrops());

    public static final Block fakeBlock = new BlockMulti(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 3600000.0F).sound(SoundType.METAL).notSolid());

//    public static final Block sealableBlock = new BlockEnclosed(builder);

    public static final Block parachest = new BlockParaChest(Block.Properties.create(Material.WOOD).hardnessAndResistance(3.0F).sound(SoundType.WOOD));

    public static final Block aluminumWire = new BlockAluminumWire(Block.Properties.create(Material.WOOL).hardnessAndResistance(0.2F).sound(SoundType.CLOTH));
    public static final Block aluminumWireHeavy = new BlockAluminumWire(Block.Properties.from(aluminumWire));
    public static final Block aluminumWireSwitchable = new BlockAluminumWire(Block.Properties.from(aluminumWire));
    public static final Block aluminumWireSwitchableHeavy = new BlockAluminumWire(Block.Properties.from(aluminumWire));
//    public static final Block blockMoon = new BlockBasicMoon(builder);

    public static final Block cheeseBlock = new BlockCheese(Block.Properties.create(Material.CAKE).hardnessAndResistance(0.5F).sound(SoundType.CLOTH));

    public static final Block screen = new BlockScreen(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1.0F).sound(SoundType.STONE));

    public static final Block fluidTank = new BlockFluidTank(Block.Properties.create(Material.GLASS).hardnessAndResistance(3.0F, 8.0F).sound(SoundType.GLASS));

    public static final Block bossSpawner = new BlockBossSpawner(Block.Properties.create(Material.ROCK).hardnessAndResistance(1000000.0F).noDrops());

//    public static final Block slabGCHalf = new BlockSlabGC(builder);
//    public static final Block slabGCDouble = new BlockDoubleSlabGC(builder);

//    public static final Block tinStairs1 = new BlockStairsGC(builder);
//    public static final Block tinStairs2 = new BlockStairsGC(builder);
//    public static final Block moonStoneStairs = new BlockStairsGC(builder);
//    public static final Block moonBricksStairs = new BlockStairsGC(builder);

//    public static final Block wallGC = new BlockWallGC(builder);

    public static final Block platform = new BlockPlatform(Block.Properties.create(Material.IRON).hardnessAndResistance(1.0F, 10.0F).sound(SoundType.METAL));

    public static final Block emergencyBox = new BlockEmergencyBox(Block.Properties.create(Material.IRON).hardnessAndResistance(4.0F, 70.0F).lightValue(15).sound(SoundType.METAL));

    public static final Block oreCopper = new OreBlock(Block.Properties.create(Material.ROCK).sound(SoundType.STONE));
    public static final Block oreTin = new OreBlock(Block.Properties.from(oreCopper));
    public static final Block oreAluminum = new OreBlock(Block.Properties.from(oreCopper));
    public static final Block oreSilicon = new OreBlock(Block.Properties.from(oreCopper));
    public static final Block oreCopperMoon = new OreBlock(Block.Properties.from(oreCopper));
    public static final Block oreTinMoon = new OreBlock(Block.Properties.from(oreCopper));
    public static final Block oreCheeseMoon = new OreBlock(Block.Properties.from(oreCopper));
    public static final Block oreSapphire = new OreBlock(Block.Properties.from(oreCopper));
//    public static final Block oreMeteoricIron = new OreBlock(Block.Properties.from(oreCopper));

    public static final Block moonDirt = new BlockSimple(Block.Properties.create(Material.ROCK).sound(SoundType.STONE));
    public static final Block moonStone = new BlockSimple(Block.Properties.from(moonDirt));
    public static final Block moonTurf = new BlockSimple(Block.Properties.from(moonDirt));
    public static final Block moonDungeonBrick = new BlockDungeonBrick(Block.Properties.from(moonDirt));

    public static final Block decoBlock0 = new DecoBlock(Block.Properties.create(Material.ROCK).sound(SoundType.STONE));
    public static final Block decoBlock1 = new DecoBlock(Block.Properties.from(decoBlock0));
    public static final Block decoBlockCopper = new DecoBlock(Block.Properties.from(decoBlock0));
    public static final Block decoBlockTin = new DecoBlock(Block.Properties.from(decoBlock0));
    public static final Block decoBlockAluminum = new DecoBlock(Block.Properties.from(decoBlock0));
    public static final Block decoBlockMeteorIron = new DecoBlock(Block.Properties.from(decoBlock0));
    public static final Block decoBlockSilicon = new DecoBlock(Block.Properties.from(decoBlock0));
//    public static final Block grating = new BlockGrating(builder);
//    public static final Block gratingWater = new BlockGrating(builder);
//    public static final Block gratingLava = new BlockGrating(builder);

//    public static final Block breatheableAir = null;
//    public static final Block brightAir = null;
//    public static final Block brightBreatheableAir = null;
//    public static final Block brightLamp = null;
//    public static final Block treasureChestTier1 = null;
//    public static final Block landingPad = null;
//    public static final Block unlitTorch = null;
//    public static final Block unlitTorchLit = null;
//    public static final Block oxygenDistributor = null;
//    public static final Block oxygenPipe = null;
//    public static final Block oxygenPipePull = null;
//    public static final Block oxygenCollector = null;
//    public static final Block oxygenCompressor = null;
//    public static final Block oxygenSealer = null;
//    public static final Block oxygenDetector = null;
//    public static final Block nasaWorkbench = null;
//    public static final Block fallenMeteor = null;
//    public static final Block basicBlock = null;
//    public static final Block airLockFrame = null;
//    public static final Block airLockSeal = null;
//    public static BlockSpaceGlass spaceGlassClear;
//    public static BlockSpaceGlass spaceGlassVanilla;
//    public static BlockSpaceGlass spaceGlassStrong;
//    public static BlockSpaceGlass spaceGlassTinClear;
//    public static BlockSpaceGlass spaceGlassTinVanilla;
//    public static BlockSpaceGlass spaceGlassTinStrong;
//    public static final Block crafting = null;
//    public static final Block crudeOil = null;
//    public static final Block fuel = null;
//    public static final Block refinery = null;
//    public static final Block fuelLoader = null;
//    public static final Block landingPadFull = null;
//    public static final Block spaceStationBase = null;
//    public static final Block fakeBlock = null;
//    public static final Block sealableBlock = null;
//    public static final Block cargoLoader = null;
//    public static final Block parachest = null;
//    public static final Block solarPanel = null;
//    public static final Block radioTelescope = null;
//    public static final Block machineBase = null;
//    public static final Block machineBase2 = null;
//    public static final Block machineBase3 = null;
//    public static final Block machineBase4 = null;
//    public static final Block machineTiered = null;
//    public static final Block aluminumWire = null;
//    public static final Block panelLighting = null;
//    public static final Block glowstoneTorch = null;
//    public static final Block blockMoon = null;
//    public static final Block cheeseBlock = null;
//    public static final Block spinThruster = null;
//    public static final Block screen = null;
//    public static final Block telemetry = null;
//    public static final Block fluidTank = null;
//    public static final Block bossSpawner = null;
//    public static final Block slabGCHalf = null;
//    public static final Block slabGCDouble = null;
//    public static final Block tinStairs1 = null;
//    public static final Block tinStairs2 = null;
//    public static final Block moonStoneStairs = null;
//    public static final Block moonBricksStairs = null;
//    public static final Block wallGC = null;
//    public static final Block concealedRedstone = null;
//    public static final Block concealedRepeater_Powered = null;
//    public static final Block concealedRepeater_Unpowered = null;
//    public static final Block concealedDetector = null;
//    public static final Block platform = null;
//    public static final Block emergencyBox = null;
//    public static final Block grating = null;
//    public static final Block gratingWater = null;
//    public static final Block gratingLava = null;

//    public static final Material machine = new Material.Builder(MaterialColor.IRON).build();

    public static ArrayList<Block> hiddenBlocks = new ArrayList<Block>();
    public static ArrayList<Block> otherModTorchesLit = new ArrayList<Block>();
    public static ArrayList<Block> otherModTorchesUnlit = new ArrayList<Block>();

    public static HashMap<Block, Block> itemChanges = new HashMap<>(4, 1.0F);

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, ResourceLocation name)
    {
        reg.register(thing.setRegistryName(name));
        if (thing instanceof BlockItem)
        {
            GalacticraftCore.blocksList.add(name);
        }
        else if (thing instanceof Item)
        {
            GalacticraftCore.itemList.add(name);
        }
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, IForgeRegistryEntry<V> thing, String name)
    {
        ResourceLocation location = new ResourceLocation(Constants.MOD_ID_CORE, name);
        register(reg, thing, location);
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, String name, IForgeRegistryEntry<V> thing) {
        ResourceLocation location = new ResourceLocation(Constants.MOD_ID_CORE, name);
        register(reg, thing, location);
    }

    public static <V extends IForgeRegistryEntry<V>> void register(IForgeRegistry<V> reg, ResourceLocation name, IForgeRegistryEntry<V> thing) {
        register(reg, thing, name);
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> evt)
    {
        IForgeRegistry<Block> r = evt.getRegistry();
        register(r, BlockNames.breatheableAir, breatheableAir);
        register(r, BlockNames.brightAir, brightAir);
        register(r, BlockNames.brightBreatheableAir, brightBreatheableAir);
        register(r, BlockNames.arcLamp, arcLamp);
        register(r, BlockNames.treasureChestTier1, treasureChestTier1);
        register(r, BlockNames.landingPad, landingPad);
        register(r, BlockNames.buggyPad, buggyPad);
        register(r, BlockNames.landingPadFull, landingPadFull);
        register(r, BlockNames.buggyPadFull, buggyPadFull);
        register(r, BlockNames.unlitTorch, unlitTorch);
        register(r, BlockNames.unlitTorchWall, unlitTorchWall);
        register(r, BlockNames.unlitTorchLit, unlitTorchLit);
        register(r, BlockNames.unlitTorchWallLit, unlitTorchWallLit);
        register(r, BlockNames.glowstoneTorch, glowstoneTorch);
        register(r, BlockNames.glowstoneTorchWall, glowstoneTorchWall);
        register(r, BlockNames.oxygenDistributor, oxygenDistributor);
        register(r, BlockNames.oxygenCollector, oxygenCollector);
        register(r, BlockNames.nasaWorkbench, nasaWorkbench);
        register(r, BlockNames.airLockFrame, airLockFrame);
        register(r, BlockNames.airLockController, airLockController);
        register(r, BlockNames.ingotCompressor, ingotCompressor);
        register(r, BlockNames.ingotCompressorElectric, ingotCompressorElectric);
        register(r, BlockNames.ingotCompressorElectricAdvanced, ingotCompressorElectricAdvanced);
        register(r, BlockNames.coalGenerator, coalGenerator);
        register(r, BlockNames.circuitFabricator, circuitFabricator);
        register(r, BlockNames.oxygenStorageModule, oxygenStorageModule);
        register(r, BlockNames.deconstructor, deconstructor);
        register(r, BlockNames.painter, painter);
        register(r, BlockNames.crafting, crafting);
        register(r, BlockNames.refinery, refinery);
        register(r, BlockNames.fuelLoader, fuelLoader);
        register(r, BlockNames.oxygenCompressor, oxygenCompressor);
        register(r, BlockNames.oxygenDecompressor, oxygenDecompressor);
        register(r, BlockNames.oxygenSealer, oxygenSealer);
        register(r, BlockNames.oxygenDetector, oxygenDetector);
        register(r, BlockNames.cargoLoader, cargoLoader);
        register(r, BlockNames.cargoUnloader, cargoUnloader);
        register(r, BlockNames.solarPanel, solarPanel);
        register(r, BlockNames.solarPanelAdvanced, solarPanelAdvanced);
//        register(r, BlockNames.radioTelescope, radioTelescope);
        register(r, BlockNames.storageModule, storageModule);
        register(r, BlockNames.storageCluster, storageCluster);
        register(r, BlockNames.furnaceElectric, furnaceElectric);
        register(r, BlockNames.furanceArc, furanceArc);
//        register(r, BlockNames.panelLighting, panelLighting);
//        register(r, BlockNames.spinThruster, spinThruster);
        register(r, BlockNames.telemetry, telemetry);
        register(r, BlockNames.concealedRedstone, concealedRedstone);
        register(r, BlockNames.concealedRepeater, concealedRepeater);
        register(r, BlockNames.concealedDetector, concealedDetector);
        register(r, BlockNames.airLockSeal, airLockSeal);
        register(r, BlockNames.fluidPipe, fluidPipe);
        register(r, BlockNames.fluidPipePull, fluidPipePull);
        register(r, BlockNames.fallenMeteor, fallenMeteor);
//        register(r, BlockNames.spaceGlassVanilla, spaceGlassVanilla);
//        register(r, BlockNames.spaceGlassClear, spaceGlassClear);
//        register(r, BlockNames.spaceGlassTinVanilla, spaceGlassTinVanilla);
//        register(r, BlockNames.spaceGlassTinClear, spaceGlassTinClear);
//        register(r, BlockNames.spaceGlassStrong, spaceGlassStrong);
//        register(r, BlockNames.spaceGlassTinStrong, spaceGlassTinStrong);
        register(r, BlockNames.spaceStationBase, spaceStationBase);
        register(r, BlockNames.fakeBlock, fakeBlock);
//        register(r, BlockNames.sealableBlock, sealableBlock);
        register(r, BlockNames.parachest, parachest);
        register(r, BlockNames.aluminumWire, aluminumWire);
        register(r, BlockNames.aluminumWireHeavy, aluminumWireHeavy);
        register(r, BlockNames.aluminumWireSwitchable, aluminumWireSwitchable);
        register(r, BlockNames.aluminumWireSwitchableHeavy, aluminumWireSwitchableHeavy);
        register(r, BlockNames.cheeseBlock, cheeseBlock);
        register(r, BlockNames.screen, screen);
        register(r, BlockNames.fluidTank, fluidTank);
        register(r, BlockNames.bossSpawner, bossSpawner);
//        register(r, BlockNames.slabGCHalf, slabGCHalf);
//        register(r, BlockNames.slabGCDouble, slabGCDouble);
//        register(r, BlockNames.tinStairs1, tinStairs1);
//        register(r, BlockNames.tinStairs2, tinStairs2);
//        register(r, BlockNames.moonStoneStairs, moonStoneStairs);
//        register(r, BlockNames.moonBricksStairs, moonBricksStairs);
//        register(r, BlockNames.wallGC, wallGC);
        register(r, BlockNames.platform, platform);
        register(r, BlockNames.emergencyBox, emergencyBox);
        register(r, BlockNames.oreCopper, oreCopper);
        register(r, BlockNames.oreTin, oreTin);
        register(r, BlockNames.oreAluminum, oreAluminum);
        register(r, BlockNames.oreSilicon, oreSilicon);
        register(r, BlockNames.oreCopperMoon, oreCopperMoon);
        register(r, BlockNames.oreTinMoon, oreTinMoon);
        register(r, BlockNames.oreCheeseMoon, oreCheeseMoon);
        register(r, BlockNames.oreSapphire, oreSapphire);
//        register(r, BlockNames.oreMeteoricIron, oreMeteoricIron);
        register(r, BlockNames.moonDirt, moonDirt);
        register(r, BlockNames.moonStone, moonStone);
        register(r, BlockNames.moonTurf, moonTurf);
        register(r, BlockNames.moonDungeonBrick, moonDungeonBrick);
        register(r, BlockNames.decoBlock0, decoBlock0);
        register(r, BlockNames.decoBlock1, decoBlock1);
        register(r, BlockNames.decoBlockCopper, decoBlockCopper);
        register(r, BlockNames.decoBlockTin, decoBlockTin);
        register(r, BlockNames.decoBlockAluminum, decoBlockAluminum);
        register(r, BlockNames.decoBlockMeteorIron, decoBlockMeteorIron);
        register(r, BlockNames.decoBlockSilicon, decoBlockSilicon);
//        register(r, BlockNames.grating, grating);
//        register(r, BlockNames.gratingWater, gratingWater);
//        register(r, BlockNames.gratingLava, gratingLava);

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
//        GCBlocks.grating = new BlockGrating("grating", ConfigManagerCore.allowLiquidGratings.get() ? Material.CARPET : Material.IRON);
//        GCBlocks.gratingWater = new BlockGrating("grating1", Material.WATER);
//        GCBlocks.gratingLava = new BlockGrating("grating2", Material.LAVA).setLightLevel(1.0F);

        // Hide certain items from NEI
        GCBlocks.hiddenBlocks.add(GCBlocks.airLockSeal);
        GCBlocks.hiddenBlocks.add(GCBlocks.fluidPipePull);
        GCBlocks.hiddenBlocks.add(GCBlocks.unlitTorch);
        GCBlocks.hiddenBlocks.add(GCBlocks.unlitTorchLit);
        GCBlocks.hiddenBlocks.add(GCBlocks.landingPadFull);
        GCBlocks.hiddenBlocks.add(GCBlocks.spaceStationBase);
        GCBlocks.hiddenBlocks.add(GCBlocks.bossSpawner);
//        GCBlocks.hiddenBlocks.add(GCBlocks.slabGCDouble);

        // Register blocks before register ores, so that the ItemStack picks up the correct item
//        GCBlocks.registerBlocks();
//        GCBlocks.setHarvestLevels();
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> evt)
    {
        IForgeRegistry<Item> r = evt.getRegistry();
        Item.Properties props = GCItems.defaultBuilder().group(GalacticraftCore.galacticraftBlocksTab);
        register(r, Registry.BLOCK.getKey(arcLamp), new BlockItem(arcLamp, props));
        register(r, Registry.BLOCK.getKey(treasureChestTier1), new BlockItem(treasureChestTier1, props));
        register(r, Registry.BLOCK.getKey(landingPad), new BlockItem(landingPad, props));
        register(r, Registry.BLOCK.getKey(buggyPad), new BlockItem(buggyPad, props));
        register(r, Registry.BLOCK.getKey(glowstoneTorch), new WallOrFloorItem(glowstoneTorch, glowstoneTorchWall, props));
        register(r, Registry.BLOCK.getKey(oxygenDistributor), new BlockItem(oxygenDistributor, props));
        register(r, Registry.BLOCK.getKey(oxygenCollector), new BlockItem(oxygenCollector, props));
        register(r, Registry.BLOCK.getKey(nasaWorkbench), new BlockItem(nasaWorkbench, props));
        register(r, Registry.BLOCK.getKey(airLockFrame), new BlockItem(airLockFrame, props));
        register(r, Registry.BLOCK.getKey(airLockController), new BlockItem(airLockController, props));
        register(r, Registry.BLOCK.getKey(ingotCompressor), new BlockItem(ingotCompressor, props));
        register(r, Registry.BLOCK.getKey(ingotCompressorElectric), new BlockItem(ingotCompressorElectric, props));
        register(r, Registry.BLOCK.getKey(ingotCompressorElectricAdvanced), new BlockItem(ingotCompressorElectricAdvanced, props));
        register(r, Registry.BLOCK.getKey(coalGenerator), new BlockItem(coalGenerator, props));
        register(r, Registry.BLOCK.getKey(circuitFabricator), new BlockItem(circuitFabricator, props));
        register(r, Registry.BLOCK.getKey(oxygenStorageModule), new BlockItem(oxygenStorageModule, props));
        register(r, Registry.BLOCK.getKey(deconstructor), new BlockItem(deconstructor, props));
        register(r, Registry.BLOCK.getKey(painter), new BlockItem(painter, props));
        register(r, Registry.BLOCK.getKey(crafting), new BlockItem(crafting, props));
        register(r, Registry.BLOCK.getKey(refinery), new BlockItem(refinery, props));
        register(r, Registry.BLOCK.getKey(fuelLoader), new BlockItem(fuelLoader, props));
        register(r, Registry.BLOCK.getKey(oxygenCompressor), new BlockItem(oxygenCompressor, props));
        register(r, Registry.BLOCK.getKey(oxygenDecompressor), new BlockItem(oxygenDecompressor, props));
        register(r, Registry.BLOCK.getKey(oxygenSealer), new BlockItem(oxygenSealer, props));
        register(r, Registry.BLOCK.getKey(oxygenDetector), new BlockItem(oxygenDetector, props));
        register(r, Registry.BLOCK.getKey(cargoLoader), new BlockItem(cargoLoader, props));
        register(r, Registry.BLOCK.getKey(cargoUnloader), new BlockItem(cargoUnloader, props));
        register(r, Registry.BLOCK.getKey(solarPanel), new BlockItem(solarPanel, props));
        register(r, Registry.BLOCK.getKey(solarPanelAdvanced), new BlockItem(solarPanelAdvanced, props));
        register(r, Registry.BLOCK.getKey(storageModule), new BlockItem(storageModule, props));
        register(r, Registry.BLOCK.getKey(storageCluster), new BlockItem(storageCluster, props));
        register(r, Registry.BLOCK.getKey(furnaceElectric), new BlockItem(furnaceElectric, props));
        register(r, Registry.BLOCK.getKey(furanceArc), new BlockItem(furanceArc, props));
        register(r, Registry.BLOCK.getKey(telemetry), new BlockItem(telemetry, props));
        register(r, Registry.BLOCK.getKey(concealedRedstone), new BlockItem(concealedRedstone, props));
        register(r, Registry.BLOCK.getKey(concealedRepeater), new BlockItem(concealedRepeater, props));
        register(r, Registry.BLOCK.getKey(concealedDetector), new BlockItem(concealedDetector, props));
        register(r, Registry.BLOCK.getKey(airLockSeal), new BlockItem(airLockSeal, props));
        register(r, Registry.BLOCK.getKey(fluidPipe), new BlockItem(fluidPipe, props));
        register(r, Registry.BLOCK.getKey(fluidPipePull), new BlockItem(fluidPipePull, props));
        register(r, Registry.BLOCK.getKey(fallenMeteor), new BlockItem(fallenMeteor, props));
        register(r, Registry.BLOCK.getKey(parachest), new BlockItem(parachest, props));
        register(r, Registry.BLOCK.getKey(aluminumWire), new BlockItem(aluminumWire, props));
        register(r, Registry.BLOCK.getKey(aluminumWireHeavy), new BlockItem(aluminumWireHeavy, props));
        register(r, Registry.BLOCK.getKey(aluminumWireSwitchable), new BlockItem(aluminumWireSwitchable, props));
        register(r, Registry.BLOCK.getKey(aluminumWireSwitchableHeavy), new BlockItem(aluminumWireSwitchableHeavy, props));
        register(r, Registry.BLOCK.getKey(cheeseBlock), new BlockItem(cheeseBlock, props));
        register(r, Registry.BLOCK.getKey(screen), new BlockItem(screen, props));
        register(r, Registry.BLOCK.getKey(fluidTank), new BlockItem(fluidTank, props));
        register(r, Registry.BLOCK.getKey(emergencyBox), new BlockItem(emergencyBox, props));
        register(r, Registry.BLOCK.getKey(oreCopper), new BlockItem(oreCopper, props));
        register(r, Registry.BLOCK.getKey(oreTin), new BlockItem(oreTin, props));
        register(r, Registry.BLOCK.getKey(oreAluminum), new BlockItem(oreAluminum, props));
        register(r, Registry.BLOCK.getKey(oreSilicon), new BlockItem(oreSilicon, props));
        register(r, Registry.BLOCK.getKey(oreCopperMoon), new BlockItem(oreCopperMoon, props));
        register(r, Registry.BLOCK.getKey(oreTinMoon), new BlockItem(oreTinMoon, props));
        register(r, Registry.BLOCK.getKey(oreCheeseMoon), new BlockItem(oreCheeseMoon, props));
        register(r, Registry.BLOCK.getKey(oreSapphire), new BlockItem(oreSapphire, props));
        register(r, Registry.BLOCK.getKey(moonDirt), new BlockItem(moonDirt, props));
        register(r, Registry.BLOCK.getKey(moonStone), new BlockItem(moonStone, props));
        register(r, Registry.BLOCK.getKey(moonTurf), new BlockItem(moonTurf, props));
        register(r, Registry.BLOCK.getKey(moonDungeonBrick), new BlockItem(moonDungeonBrick, props));
        register(r, Registry.BLOCK.getKey(decoBlock0), new BlockItem(decoBlock0, props));
        register(r, Registry.BLOCK.getKey(decoBlock1), new BlockItem(decoBlock1, props));
        register(r, Registry.BLOCK.getKey(decoBlockCopper), new BlockItem(decoBlockCopper, props));
        register(r, Registry.BLOCK.getKey(decoBlockTin), new BlockItem(decoBlockTin, props));
        register(r, Registry.BLOCK.getKey(decoBlockAluminum), new BlockItem(decoBlockAluminum, props));
        register(r, Registry.BLOCK.getKey(decoBlockMeteorIron), new BlockItem(decoBlockMeteorIron, props));
        register(r, Registry.BLOCK.getKey(decoBlockSilicon), new BlockItem(decoBlockSilicon, props));
        register(r, Registry.BLOCK.getKey(platform), new BlockItem(platform, props));
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

    @SubscribeEvent
    public static void initTileEntities(RegistryEvent.Register<TileEntityType<?>> evt)
    {
        IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();

        register(r, TileEntityType.Builder.create(TileEntityTreasureChest::new, treasureChestTier1).build(null), BlockNames.treasureChestTier1);
        register(r, TileEntityType.Builder.create(TileEntityOxygenDistributor::new, oxygenDistributor).build(null), BlockNames.oxygenDistributor);
        register(r, TileEntityType.Builder.create(TileEntityOxygenCollector::new, oxygenCollector).build(null), BlockNames.oxygenCollector);
        register(r, TileEntityType.Builder.create(TileEntityFluidPipe::new, fluidPipe).build(null), BlockNames.fluidPipe);
        register(r, TileEntityType.Builder.create(TileEntityAirLock::new, airLockFrame).build(null), BlockNames.airLockFrame);
        register(r, TileEntityType.Builder.create(TileEntityRefinery::new, refinery).build(null), BlockNames.refinery);
        register(r, TileEntityType.Builder.create(TileEntityNasaWorkbench::new, nasaWorkbench).build(null), BlockNames.nasaWorkbench);
        register(r, TileEntityType.Builder.create(TileEntityDeconstructor::new, deconstructor).build(null), BlockNames.deconstructor);
        register(r, TileEntityType.Builder.create(TileEntityOxygenCompressor::new, oxygenCompressor).build(null), BlockNames.oxygenCompressor);
        register(r, TileEntityType.Builder.create(TileEntityOxygenDecompressor::new, oxygenDecompressor).build(null), BlockNames.oxygenDecompressor);
        register(r, TileEntityType.Builder.create(TileEntityFuelLoader::new, fuelLoader).build(null), BlockNames.fuelLoader);
        register(r, TileEntityType.Builder.create(TileEntityLandingPadSingle::new, landingPad).build(null), BlockNames.landingPad);
        register(r, TileEntityType.Builder.create(TileEntityLandingPad::new, landingPadFull).build(null), BlockNames.landingPadFull);
        register(r, TileEntityType.Builder.create(TileEntitySpaceStationBase::new, spaceStationBase).build(null), BlockNames.spaceStationBase);
        register(r, TileEntityType.Builder.create(TileEntityFake::new, fakeBlock).build(null), BlockNames.fakeBlock);
        register(r, TileEntityType.Builder.create(TileEntityOxygenSealer::new, oxygenSealer).build(null), BlockNames.oxygenSealer);
        register(r, TileEntityType.Builder.create(TileEntityDungeonSpawner::new, bossSpawner).build(null), BlockNames.bossSpawner);
        register(r, TileEntityType.Builder.create(TileEntityOxygenDetector::new, oxygenDetector).build(null), BlockNames.oxygenDetector);
        register(r, TileEntityType.Builder.create(TileEntityBuggyFueler::new, buggyPadFull).build(null), BlockNames.buggyPadFull);
        register(r, TileEntityType.Builder.create(TileEntityBuggyFuelerSingle::new, buggyPad).build(null), BlockNames.buggyPad);
        register(r, TileEntityType.Builder.create(TileEntityCargoLoader::new, cargoLoader).build(null), BlockNames.cargoLoader);
        register(r, TileEntityType.Builder.create(TileEntityCargoUnloader::new, cargoUnloader).build(null), BlockNames.cargoUnloader);
        register(r, TileEntityType.Builder.create(TileEntityParaChest::new, parachest).build(null), BlockNames.parachest);
        register(r, TileEntityType.Builder.create(TileEntitySolar.TileEntitySolarT1::new, solarPanel).build(null), BlockNames.solarPanel);
        register(r, TileEntityType.Builder.create(TileEntitySolar.TileEntitySolarT2::new, solarPanelAdvanced).build(null), BlockNames.solarPanelAdvanced);
//        register(r, TileEntityType.Builder.create(TileEntityDish::new, radioTelescope).build(null), BlockNames.radioTelescope);
        register(r, TileEntityType.Builder.create(TileEntityCrafting::new, crafting).build(null), BlockNames.crafting);
        register(r, TileEntityType.Builder.create(TileEntityEnergyStorageModule.TileEntityEnergyStorageModuleT1::new, storageModule).build(null), BlockNames.storageModule);
        register(r, TileEntityType.Builder.create(TileEntityEnergyStorageModule.TileEntityEnergyStorageModuleT2::new, storageCluster).build(null), BlockNames.storageCluster);
        register(r, TileEntityType.Builder.create(TileEntityCoalGenerator::new, coalGenerator).build(null), BlockNames.coalGenerator);
        register(r, TileEntityType.Builder.create(TileEntityElectricFurnace.TileEntityElectricFurnaceT1::new, furnaceElectric).build(null), BlockNames.furnaceElectric);
        register(r, TileEntityType.Builder.create(TileEntityElectricFurnace.TileEntityElectricFurnaceT2::new, furanceArc).build(null), BlockNames.furanceArc);
        register(r, TileEntityType.Builder.create(TileEntityAluminumWire.TileEntityAluminumWireT1::new, aluminumWire).build(null), BlockNames.aluminumWire);
        register(r, TileEntityType.Builder.create(TileEntityAluminumWire.TileEntityAluminumWireT2::new, aluminumWireHeavy).build(null), BlockNames.aluminumWireHeavy);
        register(r, TileEntityType.Builder.create(TileEntityAluminumWireSwitch.TileEntityAluminumWireSwitchableT1::new, aluminumWireSwitchable).build(null), BlockNames.aluminumWireSwitchable);
        register(r, TileEntityType.Builder.create(TileEntityAluminumWireSwitch.TileEntityAluminumWireSwitchableT2::new, aluminumWireSwitchableHeavy).build(null), BlockNames.aluminumWireSwitchableHeavy);
//        register(r, TileEntityType.Builder.create(TileEntityAluminumWireSwitch::new, "GC Switchable Aluminum Wire").build(null));
        register(r, TileEntityType.Builder.create(TileEntityFallenMeteor::new, fallenMeteor).build(null), BlockNames.fallenMeteor);
        register(r, TileEntityType.Builder.create(TileEntityIngotCompressor::new, ingotCompressor).build(null), BlockNames.ingotCompressor);
        register(r, TileEntityType.Builder.create(TileEntityElectricIngotCompressor.TileEntityElectricIngotCompressorT1::new, ingotCompressorElectric).build(null), BlockNames.ingotCompressorElectric);
        register(r, TileEntityType.Builder.create(TileEntityElectricIngotCompressor.TileEntityElectricIngotCompressorT2::new, ingotCompressorElectricAdvanced).build(null), BlockNames.ingotCompressorElectricAdvanced);
        register(r, TileEntityType.Builder.create(TileEntityCircuitFabricator::new, circuitFabricator).build(null), BlockNames.circuitFabricator);
        register(r, TileEntityType.Builder.create(TileEntityAirLockController::new, airLockController).build(null), BlockNames.airLockController);
        register(r, TileEntityType.Builder.create(TileEntityOxygenStorageModule::new, oxygenStorageModule).build(null), BlockNames.oxygenStorageModule);
//        register(r, TileEntityType.Builder.create(TileEntityThruster::new, spinThruster).build(null), BlockNames.spinThruster);
        register(r, TileEntityType.Builder.create(TileEntityArclamp::new, arcLamp).build(null), BlockNames.arcLamp);
        register(r, TileEntityType.Builder.create(TileEntityScreen::new, screen).build(null), BlockNames.screen);
//        register(r, TileEntityType.Builder.create(TileEntityPanelLight::new, panelLighting).build(null), BlockNames.panelLighting);
        register(r, TileEntityType.Builder.create(TileEntityTelemetry::new, telemetry).build(null), BlockNames.telemetry);
        register(r, TileEntityType.Builder.create(TileEntityPainter::new, painter).build(null), BlockNames.painter);
        register(r, TileEntityType.Builder.create(TileEntityFluidTank::new, fluidTank).build(null), BlockNames.fluidTank);
        register(r, TileEntityType.Builder.create(TileEntityPlayerDetector::new, concealedDetector).build(null), BlockNames.concealedDetector);
        register(r, TileEntityType.Builder.create(TileEntityPlatform::new, platform).build(null), BlockNames.platform);
        register(r, TileEntityType.Builder.create(TileEntityEmergencyBox::new, emergencyBox).build(null), BlockNames.emergencyBox);
//        register(r, TileEntityType.Builder.create(TileEntityNull::new, "GC Null Tile").build(null));
    }
}
