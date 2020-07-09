package micdoodle8.mods.galacticraft.core.inventory;

import micdoodle8.mods.galacticraft.api.prefab.entity.EntityTieredRocket;
import micdoodle8.mods.galacticraft.core.client.gui.container.*;
import micdoodle8.mods.galacticraft.core.entities.EntityBuggy;
import micdoodle8.mods.galacticraft.core.entities.player.GCPlayerStats;
import micdoodle8.mods.galacticraft.core.tile.*;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.IForgeRegistry;

import static micdoodle8.mods.galacticraft.core.GCBlocks.register;

public class GCContainers
{
    @SubscribeEvent
    public static void initContainers(RegistryEvent.Register<ContainerType<?>> evt)
    {
        IForgeRegistry<ContainerType<?>> r = evt.getRegistry();

        ContainerType<ContainerBuggy> buggy = IForgeContainerType.create((windowId, inv, data) -> new ContainerBuggy(windowId, inv, EntityBuggy.BuggyType.byId(data.readInt())));
        ContainerType<ContainerCargoLoader> cargoLoader = IForgeContainerType.create((windowId, inv, data) -> new ContainerCargoLoader(windowId, inv, (TileEntityCargoBase) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerCircuitFabricator> circuitFabricator = IForgeContainerType.create((windowId, inv, data) -> new ContainerCircuitFabricator(windowId, inv, (TileEntityCircuitFabricator) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerCoalGenerator> coalGenerator = IForgeContainerType.create((windowId, inv, data) -> new ContainerCoalGenerator(windowId, inv));
        ContainerType<ContainerCrafting> crafting = IForgeContainerType.create((windowId, inv, data) -> new ContainerCrafting(windowId, inv, (TileEntityCrafting) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerDeconstructor> deconstructor = IForgeContainerType.create((windowId, inv, data) -> new ContainerDeconstructor(windowId, inv));
        ContainerType<ContainerElectricFurnace> electricFurnace = IForgeContainerType.create((windowId, inv, data) -> new ContainerElectricFurnace(windowId, inv, (TileEntityElectricFurnace) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerElectricIngotCompressor> electricIngotCompressor = IForgeContainerType.create((windowId, inv, data) -> new ContainerElectricIngotCompressor(windowId, inv, (TileEntityElectricIngotCompressor) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerEnergyStorageModule> energyStorageModule = IForgeContainerType.create((windowId, inv, data) -> new ContainerEnergyStorageModule(windowId, inv, (TileEntityEnergyStorageModule) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerExtendedInventory> extendedInventory = IForgeContainerType.create((windowId, inv, data) ->
        {
            GCPlayerStats stats = GCPlayerStats.get(inv.player);
            return new ContainerExtendedInventory(windowId, inv, stats.getExtendedInventory());
        });
        ContainerType<ContainerFuelLoader> fuelLoader = IForgeContainerType.create((windowId, inv, data) -> new ContainerFuelLoader(windowId, inv, (TileEntityFuelLoader) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerIngotCompressor> ingotCompressor = IForgeContainerType.create((windowId, inv, data) -> new ContainerIngotCompressor(windowId, inv, (TileEntityIngotCompressor) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerOxygenCollector> oxygenCollector = IForgeContainerType.create((windowId, inv, data) -> new ContainerOxygenCollector(windowId, inv, (TileEntityOxygenCollector) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerOxygenCompressor> oxygenCompressor = IForgeContainerType.create((windowId, inv, data) -> new ContainerOxygenCompressor(windowId, inv, (TileEntityOxygenCompressor) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerOxygenDecompressor> oxygenDecompressor = IForgeContainerType.create((windowId, inv, data) -> new ContainerOxygenDecompressor(windowId, inv, (TileEntityOxygenDecompressor) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerOxygenDistributor> oxygenDistributor = IForgeContainerType.create((windowId, inv, data) -> new ContainerOxygenDistributor(windowId, inv, (TileEntityOxygenDistributor) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerOxygenSealer> oxygenSealer = IForgeContainerType.create((windowId, inv, data) -> new ContainerOxygenSealer(windowId, inv, (TileEntityOxygenSealer) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerOxygenStorageModule> oxygenStorageModule = IForgeContainerType.create((windowId, inv, data) -> new ContainerOxygenStorageModule(windowId, inv, (TileEntityOxygenStorageModule) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerPainter> painter = IForgeContainerType.create((windowId, inv, data) -> new ContainerPainter(windowId, inv, (TileEntityPainter) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerParaChest> parachest = IForgeContainerType.create((windowId, inv, data) -> new ContainerParaChest(windowId, inv, (TileEntityParaChest) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerRefinery> refinery = IForgeContainerType.create((windowId, inv, data) -> new ContainerRefinery(windowId, inv, (TileEntityRefinery) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ContainerRocketInventory> rocketInventory = IForgeContainerType.create((windowId, inv, data) -> new ContainerRocketInventory(windowId, inv, (EntityTieredRocket) inv.player.getRidingEntity()));
        ContainerType<ContainerSchematic> schematic = IForgeContainerType.create((windowId, inv, data) -> new ContainerSchematic(windowId, inv));
        ContainerType<ContainerSchematicTier1Rocket> schematicT1Rocket = IForgeContainerType.create((windowId, inv, data) -> new ContainerSchematicTier1Rocket(windowId, inv));
        ContainerType<ContainerSchematicBuggy> schematicBuggy = IForgeContainerType.create((windowId, inv, data) -> new ContainerSchematicBuggy(windowId, inv));
        ContainerType<ContainerSolar> solar = IForgeContainerType.create((windowId, inv, data) -> new ContainerSolar(windowId, inv, (TileEntitySolar) inv.player.world.getTileEntity(new BlockPos(data.readInt(), data.readInt(), data.readInt()))));
        ContainerType<ChestContainer> treasureT1 = IForgeContainerType.create((windowId, inv, data) -> ((TileEntityTreasureChest) inv.player.world.getTileEntity(data.readBlockPos())).createMenu(windowId, inv));

        register(r, buggy, GCContainerNames.BUGGY);
        register(r, cargoLoader, GCContainerNames.CARGO_LOADER);
        register(r, circuitFabricator, GCContainerNames.CIRCUIT_FABRICATOR);
        register(r, coalGenerator, GCContainerNames.COAL_GENERATOR);
        register(r, crafting, GCContainerNames.CRAFTING);
        register(r, deconstructor, GCContainerNames.DECONSTRUCTOR);
        register(r, electricFurnace, GCContainerNames.ELECTRIC_FURNACE);
        register(r, electricIngotCompressor, GCContainerNames.ELECTRIC_INGOT_COMPRESSOR);
        register(r, energyStorageModule, GCContainerNames.ENERGY_STORAGE_MODULE);
        register(r, extendedInventory, GCContainerNames.EXTENDED_INVENTORY);
        register(r, fuelLoader, GCContainerNames.FUEL_LOADER);
        register(r, ingotCompressor, GCContainerNames.INGOT_COMPRESSOR);
        register(r, oxygenCollector, GCContainerNames.OXYGEN_COLLECTOR);
        register(r, oxygenCompressor, GCContainerNames.OXYGEN_COMPRESSOR);
        register(r, oxygenDecompressor, GCContainerNames.OXYGEN_DECOMPRESSOR);
        register(r, oxygenDistributor, GCContainerNames.OXYGEN_DISTRIBUTOR);
        register(r, oxygenSealer, GCContainerNames.OXYGEN_SEALER);
        register(r, oxygenStorageModule, GCContainerNames.OXYGEN_STORAGE_MODULE);
        register(r, painter, GCContainerNames.PAINTER);
        register(r, parachest, GCContainerNames.PARACHEST);
        register(r, refinery, GCContainerNames.REFINERY);
        register(r, rocketInventory, GCContainerNames.ROCKET_INVENTORY);
        register(r, schematic, GCContainerNames.SCHEMATIC);
        register(r, schematicT1Rocket, GCContainerNames.SCHEMATIC_T1_ROCKET);
        register(r, schematicBuggy, GCContainerNames.SCHEMATIC_BUGGY);
        register(r, solar, GCContainerNames.SOLAR);
        register(r, treasureT1, GCContainerNames.TREASURE_CHEST_T1);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () ->
        {
            ScreenManager.registerFactory(buggy, GuiBuggy::new);
            ScreenManager.registerFactory(cargoLoader, GuiCargoLoader::new);
            ScreenManager.registerFactory(circuitFabricator, GuiCircuitFabricator::new);
            ScreenManager.registerFactory(coalGenerator, GuiCoalGenerator::new);
            ScreenManager.registerFactory(crafting, GuiCrafting::new);
            ScreenManager.registerFactory(deconstructor, GuiDeconstructor::new);
            ScreenManager.registerFactory(electricFurnace, GuiElectricFurnace::new);
            ScreenManager.registerFactory(electricIngotCompressor, GuiElectricIngotCompressor::new);
            ScreenManager.registerFactory(energyStorageModule, GuiEnergyStorageModule::new);
            ScreenManager.registerFactory(extendedInventory, GuiExtendedInventory::new);
            ScreenManager.registerFactory(fuelLoader, GuiFuelLoader::new);
            ScreenManager.registerFactory(ingotCompressor, GuiIngotCompressor::new);
            ScreenManager.registerFactory(oxygenCollector, GuiOxygenCollector::new);
            ScreenManager.registerFactory(oxygenCompressor, GuiOxygenCompressor::new);
            ScreenManager.registerFactory(oxygenDecompressor, GuiOxygenDecompressor::new);
            ScreenManager.registerFactory(oxygenDistributor, GuiOxygenDistributor::new);
            ScreenManager.registerFactory(oxygenSealer, GuiOxygenSealer::new);
            ScreenManager.registerFactory(oxygenStorageModule, GuiOxygenStorageModule::new);
            ScreenManager.registerFactory(painter, GuiPainter::new);
            ScreenManager.registerFactory(parachest, GuiParaChest::new);
            ScreenManager.registerFactory(refinery, GuiRefinery::new);
            ScreenManager.registerFactory(rocketInventory, GuiRocketInventory::new);
            ScreenManager.registerFactory(schematic, GuiSchematicInput::new);
            ScreenManager.registerFactory(schematicT1Rocket, GuiSchematicTier1Rocket::new);
            ScreenManager.registerFactory(schematicBuggy, GuiSchematicBuggy::new);
            ScreenManager.registerFactory(solar, GuiSolar::new);
            ScreenManager.registerFactory(treasureT1, ChestScreen::new);
        });
    }
}
