package micdoodle8.mods.galacticraft.core.blocks;

import java.util.ArrayList;
import micdoodle8.mods.galacticraft.core.GCCoreConfigManager;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlock;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockBase;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockCargoLoader;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockEnclosedBlock;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemBlockLandingPad;
import micdoodle8.mods.galacticraft.core.items.GCCoreItemSapling;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.util.Icon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * Copyright 2012-2013, micdoodle8
 * 
 * All rights reserved.
 * 
 */
public class GCCoreBlocks
{
    public static GCCoreBlockBreathableAir breatheableAir;
    public static Block treasureChest;
    public static Block landingPad;
    public static GCCoreBlockUnlitTorch unlitTorch;
    public static GCCoreBlockUnlitTorch unlitTorchLit;
    public static GCCoreBlockOxygenDistributor airDistributor;
    public static GCCoreBlockOxygenPipe oxygenPipe;
    public static Block blockAirCollector;
    public static GCCoreBlockSapling sapling;
    public static Block rocketBench;
    public static Block fallenMeteor;
    public static Block decorationBlocks;
    public static Block airLockFrame;
    public static Block airLockSeal;
//    public static GCCoreBlockCrudeOil crudeOilMoving;
    public static GCCoreBlockCrudeOil crudeOilStill;
    public static Block refinery;
    public static Block compressor;
    public static Block fuelLoader;
    public static Block landingPadFull;
    public static Block spaceStationBase;
    public static GCCoreBlockMulti dummyBlock;
    public static Block sealer;
    public static Block enclosedWire;
    public static Block oxygenDetector;
    public static Block cargoLoader;
    public static Block parachest;

    public static Icon[] blockIcons;

    public static Material crudeOil = new MaterialLiquid(MapColor.foliageColor);
    public static ArrayList<Integer> hiddenBlocks = new ArrayList<Integer>();

    public static void initBlocks()
    {
        GCCoreBlocks.breatheableAir = (GCCoreBlockBreathableAir) new GCCoreBlockBreathableAir(GCCoreConfigManager.idBlockBreatheableAir).setHardness(0.0F).setResistance(1000F).setUnlocalizedName("breatheableAir");
        GCCoreBlocks.treasureChest = new GCCoreBlockT1TreasureChest(GCCoreConfigManager.idBlockTreasureChest).setHardness(1.0F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("treasureChest");
        GCCoreBlocks.landingPad = new GCCoreBlockLandingPad(GCCoreConfigManager.idBlockLandingPad).setHardness(1.0F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("landingPad");
        GCCoreBlocks.landingPadFull = new GCCoreBlockLandingPadFull(GCCoreConfigManager.idBlockLandingPadFull).setHardness(1.0F).setResistance(10.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("landingPadFull");
        GCCoreBlocks.unlitTorch = (GCCoreBlockUnlitTorch) new GCCoreBlockUnlitTorch(GCCoreConfigManager.idBlockUnlitTorch, false).setHardness(0.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("unlitTorch");
        GCCoreBlocks.unlitTorchLit = (GCCoreBlockUnlitTorch) new GCCoreBlockUnlitTorch(GCCoreConfigManager.idBlockUnlitTorchLit, true).setHardness(0.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("unlitTorchLit");
        GCCoreBlocks.airDistributor = (GCCoreBlockOxygenDistributor) new GCCoreBlockOxygenDistributor(GCCoreConfigManager.idBlockAirDistributor).setHardness(3.5F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("distributor");
        GCCoreBlocks.oxygenPipe = (GCCoreBlockOxygenPipe) new GCCoreBlockOxygenPipe(GCCoreConfigManager.idBlockAirPipe).setHardness(0.3F).setStepSound(Block.soundGlassFootstep).setUnlocalizedName("oxygenPipe");
        GCCoreBlocks.blockAirCollector = new GCCoreBlockOxygenCollector(GCCoreConfigManager.idBlockAirCollector).setHardness(3.5F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("oxygenCollector");
        GCCoreBlocks.sapling = (GCCoreBlockSapling) new GCCoreBlockSapling(GCCoreConfigManager.idBlockSapling2).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setUnlocalizedName("sapling2");
        GCCoreBlocks.rocketBench = new GCCoreBlockAdvancedCraftingTable(GCCoreConfigManager.idBlockRocketBench).setHardness(2.5F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("rocketWorkbench");
        GCCoreBlocks.fallenMeteor = new GCCoreBlockFallenMeteor(GCCoreConfigManager.idBlockFallenMeteor).setHardness(50.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("fallenMeteor");
        GCCoreBlocks.decorationBlocks = new GCCoreBlock(GCCoreConfigManager.idBlockDecorationBlock).setHardness(3.0F).setUnlocalizedName("decorationblock");
        GCCoreBlocks.airLockFrame = new GCCoreBlockAirLockFrame(GCCoreConfigManager.idBlockAirLockFrame).setHardness(3.0F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("airLockFrame");
        GCCoreBlocks.airLockSeal = new GCCoreBlockAirLockWall(GCCoreConfigManager.idBlockAirLockSeal).setHardness(100.0F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("airLockSeal");
        GCCoreBlocks.crudeOilStill = (GCCoreBlockCrudeOil) new GCCoreBlockCrudeOil(GalacticraftCore.CRUDEOIL.getBlockID(), GCCoreBlocks.crudeOil).setHardness(3.0F).setUnlocalizedName("crudeOilStill");
        GCCoreBlocks.refinery = new GCCoreBlockRefinery(GCCoreConfigManager.idBlockRefinery, 0).setHardness(3.0F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("refinery");
        GCCoreBlocks.compressor = new GCCoreBlockOxygenCompressor(GCCoreConfigManager.idBlockAirCompressor, false).setHardness(3.5F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("oxygenCompressor");
        GCCoreBlocks.fuelLoader = new GCCoreBlockFuelLoader(GCCoreConfigManager.idBlockFuelLoader).setHardness(3.0F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("fuelLoader");
        GCCoreBlocks.spaceStationBase = new GCCoreBlockSpaceStationBase(GCCoreConfigManager.idBlockSpaceStationBase).setHardness(3.0F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("spaceStationBase");
        GCCoreBlocks.dummyBlock = (GCCoreBlockMulti) new GCCoreBlockMulti(GCCoreConfigManager.idBlockDummy).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("dummyblock");
        GCCoreBlocks.sealer = new GCCoreBlockOxygenSealer(GCCoreConfigManager.idBlockOxygenSealer).setHardness(3.5F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("sealer");
        GCCoreBlocks.enclosedWire = new GCCoreBlockEnclosed(GCCoreConfigManager.idBlockEnclosedWire).setHardness(3.5F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("enclosed");
        GCCoreBlocks.oxygenDetector = new GCCoreBlockOxygenDetector(GCCoreConfigManager.idBlockOxygenDetector).setHardness(3.0F).setStepSound(Block.soundStoneFootstep).setUnlocalizedName("oxygenDetector");
        GCCoreBlocks.cargoLoader = new GCCoreBlockCargoLoader(GCCoreConfigManager.idBlockCargoLoader).setHardness(3.0F).setStepSound(Block.soundMetalFootstep).setUnlocalizedName("cargo");
        GCCoreBlocks.parachest = new GCCoreBlockParachest(GCCoreConfigManager.idBlockParachest).setHardness(3.0F).setStepSound(Block.soundWoodFootstep).setUnlocalizedName("parachest");
        
        
        // Hide certain items from NEI
        GCCoreBlocks.hiddenBlocks.add(GCCoreBlocks.airLockSeal.blockID);
        GCCoreBlocks.hiddenBlocks.add(GCCoreBlocks.sapling.blockID);
        GCCoreBlocks.hiddenBlocks.add(GCCoreBlocks.breatheableAir.blockID);
        GCCoreBlocks.hiddenBlocks.add(GCCoreBlocks.unlitTorch.blockID);
        GCCoreBlocks.hiddenBlocks.add(GCCoreBlocks.unlitTorchLit.blockID);
        GCCoreBlocks.hiddenBlocks.add(GCCoreBlocks.landingPadFull.blockID);
        GCCoreBlocks.hiddenBlocks.add(GCCoreBlocks.dummyBlock.blockID);
        GCCoreBlocks.hiddenBlocks.add(GCCoreBlocks.spaceStationBase.blockID);
    }

    public static void setHarvestLevels()
    {
        // Block Meta Tool Harvest Level
        MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.decorationBlocks, 0, "pickaxe", 1);
        MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.decorationBlocks, 1, "pickaxe", 2);
        MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.decorationBlocks, 2, "pickaxe", 3);
        MinecraftForge.setBlockHarvestLevel(GCCoreBlocks.fallenMeteor, "pickaxe", 3);
    }

    public static void registerBlocks()
    {
        // Block ItemBlock class Block Name Mod ID
        GameRegistry.registerBlock(GCCoreBlocks.treasureChest, GCCoreItemBlock.class, GCCoreBlocks.treasureChest.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.landingPad, GCCoreItemBlockLandingPad.class, GCCoreBlocks.landingPad.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.landingPadFull, GCCoreItemBlock.class, GCCoreBlocks.landingPadFull.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.unlitTorch, GCCoreItemBlock.class, GCCoreBlocks.unlitTorch.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.unlitTorchLit, GCCoreItemBlock.class, GCCoreBlocks.unlitTorchLit.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.breatheableAir, GCCoreItemBlock.class, GCCoreBlocks.breatheableAir.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.airDistributor, GCCoreItemBlock.class, GCCoreBlocks.airDistributor.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.oxygenPipe, GCCoreItemBlock.class, GCCoreBlocks.oxygenPipe.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.blockAirCollector, GCCoreItemBlock.class, GCCoreBlocks.blockAirCollector.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.rocketBench, GCCoreItemBlock.class, GCCoreBlocks.rocketBench.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.fallenMeteor, GCCoreItemBlock.class, GCCoreBlocks.fallenMeteor.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.airLockFrame, GCCoreItemBlock.class, GCCoreBlocks.airLockFrame.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.airLockSeal, GCCoreItemBlock.class, GCCoreBlocks.airLockSeal.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.crudeOilStill, GCCoreItemBlock.class, GCCoreBlocks.crudeOilStill.getUnlocalizedName(), GalacticraftCore.MODID);
//        GameRegistry.registerBlock(GCCoreBlocks.crudeOilMoving, GCCoreItemBlock.class, GCCoreBlocks.crudeOilMoving.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.refinery, GCCoreItemBlock.class, GCCoreBlocks.refinery.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.compressor, GCCoreItemBlock.class, GCCoreBlocks.compressor.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.decorationBlocks, GCCoreItemBlockBase.class, GCCoreBlocks.decorationBlocks.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.sapling, GCCoreItemSapling.class, GCCoreBlocks.sapling.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.fuelLoader, GCCoreItemBlock.class, GCCoreBlocks.fuelLoader.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.spaceStationBase, GCCoreItemBlock.class, GCCoreBlocks.spaceStationBase.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.dummyBlock, GCCoreItemBlock.class, GCCoreBlocks.dummyBlock.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.sealer, GCCoreItemBlock.class, GCCoreBlocks.sealer.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.enclosedWire, GCCoreItemBlockEnclosedBlock.class, GCCoreBlocks.enclosedWire.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.oxygenDetector, GCCoreItemBlock.class, GCCoreBlocks.oxygenDetector.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.cargoLoader, GCCoreItemBlockCargoLoader.class, GCCoreBlocks.cargoLoader.getUnlocalizedName(), GalacticraftCore.MODID);
        GameRegistry.registerBlock(GCCoreBlocks.parachest, GCCoreItemBlock.class, GCCoreBlocks.parachest.getUnlocalizedName(), GalacticraftCore.MODID);
    }
}
